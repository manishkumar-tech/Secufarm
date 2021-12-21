package com.weather.risk.mfi.myfarminfo.firebasenotification;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.squareup.picasso.Picasso;
import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.adapter.NotificationCustomAdapter;
import com.weather.risk.mfi.myfarminfo.bean.NotificationBean;
import com.weather.risk.mfi.myfarminfo.home.AppController;
import com.weather.risk.mfi.myfarminfo.mapfragments.LatLonCellID;
import com.weather.risk.mfi.myfarminfo.utils.AppConstant;
import com.weather.risk.mfi.myfarminfo.utils.AppManager;
import com.weather.risk.mfi.myfarminfo.utils.CameraUtils;
import com.weather.risk.mfi.myfarminfo.utils.TransparentProgressDialog;
import com.weather.risk.mfi.myfarminfo.utils.Utility;
import com.weather.risk.mfi.myfarminfo.volley_class.CustomJSONObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static com.weather.risk.mfi.myfarminfo.utils.Utility.getAppVersion;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getDynamicLanguageToast;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getDynamicLanguageValue;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setDynamicLanguage;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setFontsStyle;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setFontsStyleTxt;

public class NotificationPOPDetailsDialog extends Activity {


    private ArrayList<NotificationBean> mDataset = new ArrayList<NotificationBean>();
    private NotificationBean getData = new NotificationBean();
    int position = 0;
    String Messgae = null, Notftytype = null, StepID = null, FarmID = null, Title = null, DateTime = null, DateTimeHHMMSS = null, NotifImageURL = null, FeedbackStatus = null;
    TextView txt_title, txtDateTime, txtSMS;
    LinearLayout image_upload;
    TableRow tblrw_canyes;
    Button btn_cross, txt_closeup, txt_farm, imageupload1, imageupload2, submit_image_btn;
    ImageView choose_image1, choose_image2;
    TextView btn_cancel, btn_yes;

    private String imageString1, imageString2;
    public static final int MEDIA_TYPE_IMAGE = 1;
    int Imageselectflag = 0;

    private String imageStoragePath1 = "", imageStoragePath2 = "";
    private int REQUEST_CAMERA_START1 = 0, SELECT_FILE_START1 = 1;
    private int REQUEST_CAMERA_START2 = 3, SELECT_FILE_START2 = 2;

    JSONArray imageList = new JSONArray();
    ImageView image;
    String Status = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.notificationpopupdetails);

        txt_title = (TextView) findViewById(R.id.txt_title);
        txtDateTime = (TextView) findViewById(R.id.txtDateTime);
        txtSMS = (TextView) findViewById(R.id.txtSMS);
        image = (ImageView) findViewById(R.id.image);

        image_upload = (LinearLayout) findViewById(R.id.image_upload);
        tblrw_canyes = (TableRow) findViewById(R.id.tblrw_canyes);

        btn_cross = (Button) findViewById(R.id.btn_cross);
        imageupload1 = (Button) findViewById(R.id.imageupload1);
        imageupload2 = (Button) findViewById(R.id.imageupload2);
        btn_cancel = (TextView) findViewById(R.id.btn_cancel);
        btn_yes = (TextView) findViewById(R.id.btn_yes);
        submit_image_btn = (Button) findViewById(R.id.submit_image_btn);
        txt_closeup = (Button) findViewById(R.id.txt_closeup);
        txt_farm = (Button) findViewById(R.id.txt_farm);

        choose_image1 = (ImageView) findViewById(R.id.choose_image1);
        choose_image2 = (ImageView) findViewById(R.id.choose_image2);


        btn_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txt_closeup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThumbNailImages(1);
            }
        });
        txt_farm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThumbNailImages(2);
            }
        });


        choose_image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Imageselectflag = 1;
                imageString1 = null;
                choose_image1.setImageBitmap(null);
                selectImage();
            }
        });
        choose_image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Imageselectflag = 2;
                imageString2 = null;
                choose_image2.setImageBitmap(null);
                selectImage();
            }
        });
        imageupload1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageString1 != null) {
                    uploadImage(1);
                } else {
                    getDynamicLanguageToast(getApplicationContext(), "selecttheimage", R.string.selecttheimage);
                }
            }
        });
        imageupload2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageString2 != null) {
                    uploadImage(2);
                } else {
                    getDynamicLanguageToast(getApplicationContext(), "selecttheimage", R.string.selecttheimage);
                }
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                finish();
                Status = "No";
                if (Notftytype != null && Notftytype.equalsIgnoreCase("Intractive")) {
                    image_upload.setVisibility(View.VISIBLE);
                    tblrw_canyes.setVisibility(View.GONE);
                } else {
                    NotificationCountSMS.setRemoveNoficationKeyValue(NotificationPOPDetailsDialog.this, StepID, "removesinglevalues");
                    finish();
                }
            }
        });
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Status = "Yes";
                if (Notftytype != null && Notftytype.equalsIgnoreCase("Intractive")) {
                    image_upload.setVisibility(View.VISIBLE);
                    tblrw_canyes.setVisibility(View.GONE);
                } else {
                    NotificationCountSMS.setRemoveNoficationKeyValue(NotificationPOPDetailsDialog.this, StepID, "removesinglevalues");
                    finish();
                }

            }
        });
        submit_image_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (imageList != null && imageList.length() > 0) {
                new setFarmRegistrationSave("Intractive").execute();
//                } else {
//                    Toast.makeText(NotificationPOPDetailsDialog.this, getResources().getString(R.string.UploadImage), Toast.LENGTH_SHORT).show();
//                }
            }
        });
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            position = extras.getInt("position");
            mDataset = (ArrayList<NotificationBean>) getIntent().getSerializableExtra("mDataset");
            getData = mDataset.get(position);

            if (getData != null) {
//                 String Messgae = null, Notftytype = null, StepID = null, FarmID = null, Title = null, DateTime = null;
                Messgae = getData.getMessgae();
                Notftytype = getData.getNotftytype();
                StepID = getData.getStepID();
                FarmID = getData.getFarmID();
                Title = getData.getTitle();
                DateTime = getData.getDateTime();
                NotifImageURL = getData.getNotifImageURL();
                DateTimeHHMMSS = getData.getDateTimeHHMMSS();
                FeedbackStatus = getData.getFeedbackStatus();

                if (Title != null && Title.length() > 0 && !Title.equalsIgnoreCase("null")) {
                    txt_title.setText(Title);
                } else {
                    txt_title.setText("");
                }
                if (DateTime != null && DateTime.length() > 0) {
                    txtDateTime.setText(DateTime);
                } else {
                    txtDateTime.setText("");
                }
                if (Messgae != null && Messgae.length() > 0) {
                    txtSMS.setText(Messgae);
                } else {
                    txtSMS.setText("");
                }

                try {
                    if (NotifImageURL != null && NotifImageURL.length() > 6) {
//                        image.setVisibility(View.VISIBLE);
                        image.setVisibility(View.GONE);
//                        Picasso.with(NotificationPOPDetailsDialog.this).load(NotifImageURL).into(image);
                        Picasso.with(this).load(NotifImageURL)
                                .into(image, new com.squareup.picasso.Callback() {
                                    @Override
                                    public void onSuccess() {
                                        image.setVisibility(View.VISIBLE);
                                    }

                                    @Override
                                    public void onError() {
                                        image.setVisibility(View.GONE);
                                    }
                                });
                    } else {
                        image.setVisibility(View.GONE);
                    }
                    imageList = new JSONArray();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    image.setVisibility(View.GONE);
                }

                //In Normal After showing once remove from List
                if (Notftytype.equalsIgnoreCase("Normal")) {
                    NotificationCountSMS.setRemoveNoficationKeyValue(NotificationPOPDetailsDialog.this, StepID, "removesinglevalues");
                }
            }
        }
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NotifImageURL != null && NotifImageURL.length() > 6) {
                    NotificationImages(NotifImageURL);
                }
            }
        });

        image_upload.setVisibility(View.GONE);
        tblrw_canyes.setVisibility(View.GONE);
        image_upload.setVisibility(View.GONE);
        if (Notftytype != null && Notftytype.equalsIgnoreCase("Intractive")) {
            if (FeedbackStatus != null && FeedbackStatus.equalsIgnoreCase("IntractiveCompleted")) {
                tblrw_canyes.setVisibility(View.GONE);
            } else {
                tblrw_canyes.setVisibility(View.VISIBLE);
            }
            boolean check7dayslessornot = Utility.checkNoofDaysislessthan7or3(Utility.getdate() + " " + Utility.gettime(), DateTimeHHMMSS, Notftytype);
            if (!check7dayslessornot) {//more than 3 days
                NotificationCountSMS.setRemoveNoficationKeyValue(NotificationPOPDetailsDialog.this, StepID, "removesinglevalues");
            }
        }


    }

    //Herojit Add
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == REQUEST_CAMERA_START1) {
                    onCaptureImageResult1();
                } else if (requestCode == SELECT_FILE_START1) {
                    onSelectFromGalleryResult1(data);
                } else if (requestCode == REQUEST_CAMERA_START2) {
                    onCaptureImageResult2();
                } else if (requestCode == SELECT_FILE_START2) {
                    onSelectFromGalleryResult2(data);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private void selectImage() {
        try {
            final CharSequence[] items = {getDynamicLanguageValue(getApplicationContext(), "TakePhoto", R.string.TakePhoto),
                    getDynamicLanguageValue(getApplicationContext(), "Selectfromgallery", R.string.Selectfromgallery),
                    getDynamicLanguageValue(getApplicationContext(), "Cancel", R.string.Cancel)};

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            //  builder.setTitle("Upload Document!");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {

                    if (items[item].toString().equalsIgnoreCase(getDynamicLanguageValue(getApplicationContext(), "TakePhoto", R.string.TakePhoto))) {
                        //                    userChoosenTask = "Take Photo";
                        boolean resultCam = Utility.checkPermissionCamera(NotificationPOPDetailsDialog.this);
                        if (resultCam) {
                            if (CameraUtils.checkPermissions(getApplicationContext())) {
                                switch (Imageselectflag) {
                                    case 1:
                                        cameraIntent1();
                                        break;
                                    case 2:
                                        cameraIntent2();
                                        break;
                                }
                            }
                        }

                    } else if (items[item].toString().equalsIgnoreCase(getDynamicLanguageValue(getApplicationContext(), "Selectfromgallery", R.string.Selectfromgallery))) {
                        //                    userChoosenTask = "Select From Gallery";
                        boolean resultCam = Utility.checkPermissionGallery(NotificationPOPDetailsDialog.this);
                        if (resultCam) {
                            galleryIntent();
                        }
                    } else if (items[item].toString().equalsIgnoreCase(getDynamicLanguageValue(getApplicationContext(), "Cancel", R.string.Cancel))) {
                        dialog.dismiss();
                    }
                }
            });
            builder.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private void onCaptureImageResult1() {
        try {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            Bitmap bitmap = BitmapFactory.decodeFile(imageStoragePath1, options);
            choose_image1.setImageBitmap(bitmap);
            imageString1 = imageToString(bitmap);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void onCaptureImageResult2() {
        try {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            Bitmap bitmap = BitmapFactory.decodeFile(imageStoragePath2, options);

            choose_image2.setImageBitmap(bitmap);
            imageString2 = imageToString(bitmap);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private void galleryIntent() {
        try {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            switch (Imageselectflag) {
                case 1:
                    startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE_START1);
                    break;
                case 2:
                    startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE_START2);
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult1(Intent data) {
        try {
            Bitmap bm = null;
            if (data != null) {
                try {
                    bm = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            choose_image1.setImageBitmap(bm);
            imageString1 = imageToString(bm);
            String filePath = data.getData().getPath();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult2(Intent data) {
        try {
            Bitmap bm = null;
            if (data != null) {
                try {
                    bm = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            choose_image2.setImageBitmap(bm);
            imageString2 = imageToString(bm);
            String filePath = data.getData().getPath();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String imageToString(Bitmap bmp) {
        String encodedImage = "null";
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 80, baos);
            byte[] imageBytes = baos.toByteArray();
            encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        } catch (IllegalAccessError e) {
            e.printStackTrace();
        }
        return encodedImage;
    }

    private void cameraIntent1() {
        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File file = CameraUtils.getOutputMediaFile(MEDIA_TYPE_IMAGE);
            if (file != null) {
                imageStoragePath1 = file.getAbsolutePath();
            }
            Uri fileUri = CameraUtils.getOutputMediaFileUri(getApplicationContext(), file);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            // start the image capture Intent
            startActivityForResult(intent, REQUEST_CAMERA_START1);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void cameraIntent2() {
        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File file = CameraUtils.getOutputMediaFile(MEDIA_TYPE_IMAGE);
            if (file != null) {
                imageStoragePath2 = file.getAbsolutePath();
            }
            Uri fileUri = CameraUtils.getOutputMediaFileUri(getApplicationContext(), file);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            // start the image capture Intent
            startActivityForResult(intent, REQUEST_CAMERA_START2);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    @Override
    public void onBackPressed() {
        finish();
    }

    private void uploadImage(final int flag) {
        try {
            final TransparentProgressDialog pDialog = new TransparentProgressDialog(
                    NotificationPOPDetailsDialog.this, getDynamicLanguageValue(getApplicationContext(), "Dataisloading", R.string.Dataisloading));
            pDialog.setCancelable(false);
            pDialog.show();
            JSONObject jsonObject = null;
            String usi = AppConstant.user_id;

            Double lat = LatLonCellID.lat;
            Double lon = LatLonCellID.lon;
            Log.v("imageLat_long", lat + "," + lon);

            try {
                jsonObject = new JSONObject();
                switch (flag) {
                    case 1:
                        jsonObject.putOpt("ImageString", imageString1);
                        break;
                    case 2:
                        jsonObject.putOpt("ImageString", imageString2);
                        break;
                }
                jsonObject.putOpt("UserID", usi);
                //Add new
                jsonObject.putOpt("AppVersion", getAppVersion(this));
//            jsonObject.putOpt("TaskID", selected_taskId);

                // jsonObject.putOpt("Lat_Lng", lat+","+lon);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            CustomJSONObjectRequest jsonObjectRequest = new CustomJSONObjectRequest(Request.Method.PUT, AppManager.getInstance().uploadImageURL_SCHEDULER, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    Log.i("Response upload image", "" + response.toString());
                    //Herojit Comment
                    String res = response.toString();
                    res = res.replace("\":\"[\\\"", "\":\"");
                    res = res.replace("\\\"]\"", "\"");
                    res = res.replace("\\", "");
                    try {
                        JSONObject jsonObject = new JSONObject(res);
                        if (jsonObject.get("uploadBase64Image_SchedulerResult").toString().length() > 0) {
                            imageList = Utility.addImageName(jsonObject.get("uploadBase64Image_SchedulerResult").toString(), imageList, flag);
                            getDynamicLanguageToast(getApplicationContext(), "SubmittedSuccessfully", R.string.SubmittedSuccessfully);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
//                Toast.makeText(getApplicationContext(), getResources().getString(R.string.SubmittedSuccessfully), Toast.LENGTH_LONG).show();
                    pDialog.cancel();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pDialog.cancel();
                    Log.v("Response vishal coupon", "" + error.toString());
                    getDynamicLanguageToast(getApplicationContext(), error.getMessage());
                }
            });
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    40000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            AppController.getInstance().addToRequestQueue(jsonObjectRequest);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private class setFarmRegistrationSave extends AsyncTask<Void, Void, String> {

        TransparentProgressDialog progressDialog;

        String flag = "";

        public setFarmRegistrationSave(String flg) {
            flag = flg;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new TransparentProgressDialog(
                    NotificationPOPDetailsDialog.this,
                    getDynamicLanguageValue(getApplicationContext(), "Dataisloading", R.string.Dataisloading));
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @Override
        protected String doInBackground(Void... params) {
            String response = null;
            String URL = AppManager.getInstance().UploadNotificationDataURL;//Herojit Comment
            JSONObject object = getjsonvalueUpload();
            String jsonvalue = object.toString();

            response = AppManager.getInstance().httpRequestPutMethod(URL, jsonvalue);
            return "{" + response + "}";
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPreExecute();
            progressDialog.dismiss();
            try {
                if (response.contains("SaveNotifyResponseResult")) {
                    JSONObject obj = new JSONObject(response);
                    String res = obj.getString("SaveNotifyResponseResult");
                    if (res != null && res.equalsIgnoreCase("Response Save Sucessfully")) {
                        getDynamicLanguageToast(getApplicationContext(), "SubmittedSuccessfully", R.string.SubmittedSuccessfully);
                        if (flag.equalsIgnoreCase("Intractive")) {
                            NotificationCountSMS.setNotificationValueData_AddFeedback(NotificationPOPDetailsDialog.this, Messgae,
                                    Notftytype, StepID, FarmID, Title, "", NotifImageURL, "IntractiveCompleted");
                        }
                        NotificationCountSMS.setRemoveNoficationKeyValue(NotificationPOPDetailsDialog.this, StepID, "removesinglevalues");
                        finish();
                    } else {
                        getDynamicLanguageToast(getApplicationContext(), "FormattingError", R.string.FormattingError);
                    }
                } else {
                    getDynamicLanguageToast(getApplicationContext(), "ServerError", R.string.ServerError);
                }
            } catch (Exception ex) {
                getDynamicLanguageToast(getApplicationContext(), "FormattingError", R.string.FormattingError);
                ex.printStackTrace();
            }
        }
    }

    public JSONObject getjsonvalueUpload() {
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject1 = new JSONObject();
        try {
//            jsonObject.put("OTP", "");
            //Messgae = null, Notftytype = null, StepID = null, FarmID = null, Title = null, DateTime = null
            jsonObject.put("NotifyResponseMessage", Messgae);
            jsonObject.put("FarmID", FarmID);
            jsonObject.put("stepID", StepID);
            jsonObject.put("ImagePath", imageList.toString());
            //Add New Field
            jsonObject.put("Status", Status);
//            jsonObject1.put("notifyresponse", jsonObject.toString());

//            String parameterString = jsonObject.toString();
//            parameterString = parameterString.replace("\\\\\\\\\\\\\\", "\\\\\\");
            jsonObject1 = new JSONObject();
            jsonObject1.put("notifyresponse", jsonObject.toString());
//            jsonObject1 = new JSONObject(parameterString);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return jsonObject1;
    }

    //Herojit Add
    public void ThumbNailImages(int flag) {

        //final Dialog dialog = new Dialog(OtherUserProfile.this,android.R.style.Theme_Translucent_NoTitleBar);
        final Dialog dialog = new Dialog(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.CENTER;
        wlp.dimAmount = 0.7f;
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        // wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);

        // Include dialog.xml file
        dialog.setContentView(R.layout.thumbnail_image_popup);

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        TextView heading_text = (TextView) dialog.findViewById(R.id.heading_text);
        Button submitImageBTN = (Button) dialog.findViewById(R.id.submit_image_btn);

        ImageView thumbnail_closeup = (ImageView) dialog.findViewById(R.id.thumbnail_closeup);
        ImageView thumbnail_farm = (ImageView) dialog.findViewById(R.id.thumbnail_farm);
        ImageView thumbnail_pest = (ImageView) dialog.findViewById(R.id.thumbnail_pest);

        thumbnail_closeup.setVisibility(View.GONE);
        thumbnail_farm.setVisibility(View.GONE);
        thumbnail_pest.setVisibility(View.GONE);

        switch (flag) {
            case 1:
                thumbnail_closeup.setVisibility(View.VISIBLE);
                heading_text.setText(getDynamicLanguageValue(getApplicationContext(), "CloseUpThumbnail", R.string.CloseUpThumbnail));
                break;
            case 2:
                thumbnail_farm.setVisibility(View.VISIBLE);
                heading_text.setText(getDynamicLanguageValue(getApplicationContext(), "FarmThumbnail", R.string.FarmThumbnail));
                break;
            case 3:
                thumbnail_pest.setVisibility(View.VISIBLE);
                heading_text.setText(getDynamicLanguageValue(getApplicationContext(), "PestTrapThumbnail", R.string.PestTrapThumbnail));
                break;
        }

        submitImageBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });


        dialog.show();
    }

    //Herojit Add
    public void NotificationImages(String ImageURL) {

        //final Dialog dialog = new Dialog(OtherUserProfile.this,android.R.style.Theme_Translucent_NoTitleBar);
        final Dialog dialog = new Dialog(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.dimAmount = 0.7f;
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        // wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);

        // Include dialog.xml file
        dialog.setContentView(R.layout.imageopen_popup);

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        Button cancel = (Button) dialog.findViewById(R.id.cancel);
        ImageView imagenotif = (ImageView) dialog.findViewById(R.id.image);
        TextView txt_title1 = (TextView) dialog.findViewById(R.id.txt_title);

        setFontsStyleTxt(this, txt_title1, 2);
        setFontsStyle(this, cancel);

        setDynamicLanguage(this, txt_title1, "CHANNEL_NAME", R.string.CHANNEL_NAME);
        setDynamicLanguage(this, cancel, "Cancel", R.string.Cancel);

        try {
            if (ImageURL != null && ImageURL.length() > 6) {
                Picasso.with(this).load(ImageURL)
                        .into(imagenotif, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {
//                                imagenotif.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onError() {
//                                image.setVisibility(View.GONE);
                            }
                        });
            } else {
//                image.setVisibility(View.GONE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });


        dialog.show();
    }

    public void setLanguages() {
        //Heading
        setFontsStyleTxt(this, txt_title, 2);
        setFontsStyle(this, txt_closeup);
        setFontsStyle(this, txt_farm);
        setFontsStyle(this, imageupload1);
        setFontsStyle(this, imageupload2);
        setFontsStyle(this, submit_image_btn);
        setFontsStyleTxt(this, btn_cancel, 2);
        setFontsStyleTxt(this, btn_yes, 2);

        //Tab Service
//        setDynamicLanguage(this, txt_title, "UploadImage", R.string.UploadImage);
        setDynamicLanguage(this, txt_closeup, "CloseUp", R.string.CloseUp);
        setDynamicLanguage(this, txt_farm, "Farm", R.string.Farm);
        setDynamicLanguage(this, imageupload1, "Upload", R.string.Upload);
        setDynamicLanguage(this, imageupload2, "Upload", R.string.Upload);
        setDynamicLanguage(this, submit_image_btn, "ActivityDone", R.string.ActivityDone);
        setDynamicLanguage(this, btn_cancel, "No", R.string.No);
        setDynamicLanguage(this, btn_yes, "Yes", R.string.Yes);
    }
}
