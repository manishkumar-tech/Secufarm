package com.weather.risk.mfi.myfarminfo.home;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
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
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.activities.PlantDoctors;
import com.weather.risk.mfi.myfarminfo.bean.PopBean;
import com.weather.risk.mfi.myfarminfo.customcamera.CameraSurfaceView;
import com.weather.risk.mfi.myfarminfo.customcamera.ImageFilePath;
import com.weather.risk.mfi.myfarminfo.database.DBAdapter;
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
import java.util.HashMap;

import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.CustomCamera_ImageValue;
import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.CustomCamera_bitmap;
import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.getCheckCameraScreenOnOff;
import static com.weather.risk.mfi.myfarminfo.utils.AppManager.NOGPSDialog;
import static com.weather.risk.mfi.myfarminfo.utils.AppManager.getCreateImageName;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getAppVersion;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getDynamicLanguageToast;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getDynamicLanguageValue;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setDynamicLanguage;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setFontsStyle;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setFontsStyleTxt;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setNoofPlantDocImages;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setToastSMSShow;


public class POPUpdates_ImageUploadDialog extends Activity {


    public static final String IMAGE_EXTENSION = "jpg";
    public static final int MEDIA_TYPE_IMAGE = 1;
    Button btn_cross, txt_closeup, txt_farm, imageupload1, imageupload2, submit_image_btn;
    ImageView choose_image1, choose_image2;
    String imageString1, imageString2;
    int Imageselectflag = 0;
    int position = 0;
    String Data = null;
    JSONArray imageList = new JSONArray();
    private String imageStoragePath1 = "", imageStoragePath2 = "";
    private int REQUEST_CAMERA_START1 = 0, SELECT_FILE_START1 = 1;
    private int REQUEST_CAMERA_START2 = 3, SELECT_FILE_START2 = 2;
    private String userChoosenTask;
    private ArrayList<PopBean> mDataset = new ArrayList<PopBean>();
    String ImageName1 = "", ImageName2 = "";
    DBAdapter db;
    TextView Headingtitle;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popupdates_imageupload_popup);

        db = new DBAdapter(this);
        if (!AppManager.getInstance().isLocationServicesAvailable(this)) {
            NOGPSDialog(this);
        }
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            NOGPSDialog(this);
        }

        Headingtitle = (TextView) findViewById(R.id.heading_text);
        btn_cross = (Button) findViewById(R.id.btn_cross);
        txt_closeup = (Button) findViewById(R.id.txt_closeup);
        txt_farm = (Button) findViewById(R.id.txt_farm);
        imageupload1 = (Button) findViewById(R.id.imageupload1);
        imageupload2 = (Button) findViewById(R.id.imageupload2);
        submit_image_btn = (Button) findViewById(R.id.submit_image_btn);

        choose_image1 = (ImageView) findViewById(R.id.choose_image1);
        choose_image2 = (ImageView) findViewById(R.id.choose_image2);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            position = extras.getInt("position");
            mDataset = (ArrayList<PopBean>) getIntent().getSerializableExtra("mDataset");
            if (AppConstant.farm_id != null) {
                String images = mDataset.get(position).getImage();
                try {
                    JSONArray jbb = new JSONArray(images);
                    imageList = new JSONArray();
                    if (jbb != null) {
                        for (int i = 0; i < jbb.length(); i++) {
                            String strImage = jbb.get(i).toString();
                            imageList.put(strImage);
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

        }

        setButtonClickListioner(btn_cross, 0);
        setButtonClickListioner(txt_closeup, 1);
        setButtonClickListioner(txt_farm, 2);
        setButtonClickListioner(imageupload1, 3);
        setButtonClickListioner(imageupload2, 4);
        setButtonClickListioner(submit_image_btn, 5);

        choose_image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Imageselectflag = 1;
                imageString1 = null;
                choose_image1.setImageBitmap(null);
                selectImage();
            }
        });

        choose_image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Imageselectflag = 2;
                imageString2 = null;
                choose_image2.setImageBitmap(null);
                selectImage();
            }
        });


    }

    private void selectImage() {
        final CharSequence[] items = {getDynamicLanguageValue(getApplicationContext(), "TakePhoto", R.string.TakePhoto),
                getDynamicLanguageValue(getApplicationContext(), "Selectfromgallery", R.string.Selectfromgallery),
                getDynamicLanguageValue(getApplicationContext(), "Cancel", R.string.Cancel)};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //  builder.setTitle("Upload Document!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].toString().equalsIgnoreCase(getDynamicLanguageValue(getApplicationContext(), "TakePhoto", R.string.TakePhoto))) {
                    userChoosenTask = "Take Photo";
                    boolean resultCam = Utility.checkPermissionCamera(POPUpdates_ImageUploadDialog.this);
                    if (resultCam) {
                        String imageFileName = getCreateImageName();
                        if (CameraUtils.checkPermissions(getApplicationContext())) {
                            getCheckCameraScreenOnOff = true;
                            Intent in = new Intent(POPUpdates_ImageUploadDialog.this, CameraSurfaceView.class);
                            switch (Imageselectflag) {
                                case 1:
//                                    cameraIntent1();
                                    in.putExtra("CameraScreenTypeNearFar", "Near");//Close Up
                                    ImageName1 = imageFileName;
                                    break;
                                case 2:
//                                    cameraIntent2();
                                    in.putExtra("CameraScreenTypeNearFar", "Far");//Farms
                                    ImageName2 = imageFileName;
                                    break;
                            }
                            in.putExtra("imageFileName", imageFileName);
                            in.putExtra("ActivityNameComingFrom", "POPUpdates_ImageUploadDialog");
                            startActivity(in);
                        }
                    }

                } else if (items[item].toString().equalsIgnoreCase(getDynamicLanguageValue(getApplicationContext(), "Selectfromgallery", R.string.Selectfromgallery))) {
                    userChoosenTask = "Select From Gallery";
                    boolean resultCam = Utility.checkPermissionGallery(POPUpdates_ImageUploadDialog.this);
                    if (resultCam) {
                        galleryIntent();
                    }
                } else if (items[item].toString().equalsIgnoreCase(getDynamicLanguageValue(getApplicationContext(), "Cancel", R.string.Cancel))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public void setButtonClickListioner(Button button, final int flag) {
        try {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (flag) {
                        case 0:
                            finish();
                            break;
                        case 1:
                            ThumbNailImages(1);
                            break;
                        case 2:
                            ThumbNailImages(2);
                            break;
                        case 3:
                            if (imageString1 != null && imageString1.length() > 0)
                                uploadImage(1, ImageName1);
                            else
                                getDynamicLanguageToast(getApplicationContext(), "selecttheimage", R.string.selecttheimage);
                            break;
                        case 4:
                            if (imageString2 != null && imageString2.length() > 0)
                                uploadImage(2, ImageName2);
                            else
                                getDynamicLanguageToast(getApplicationContext(), "selecttheimage", R.string.selecttheimage);
                            break;
                        case 5:
                            if (AppConstant.farm_id != null) {
                                String abcs = mDataset.get(position).getId();
                                if (abcs != null && !abcs.equalsIgnoreCase("null")) {
                                    String jsonParameterString = createJsonParameterForPOP_Done(AppConstant.farm_id, abcs);
                                    Log.v("remove_done_pop_req", jsonParameterString);
                                    new completePOP(jsonParameterString).execute();
                                }
                            } else {
                                getDynamicLanguageToast(getApplicationContext(), "Pleaseselectyour", R.string.Pleaseselectyour);
                            }
                            break;
                    }
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private String createJsonParameterForPOP_Done(String farmId, String workId) {
        String parameterString = "";


        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("FarmID", farmId);
            JSONArray jsonArray = new JSONArray();

            JSONObject jb = new JSONObject();
            jb.put("WorkID", workId);
            jb.put("Status", "yes");
            jb.put("Pop_Images", imageList.toString());
            jsonArray.put(jb);

            jsonObject.put("msg", "" + jsonArray);

            //Add Herojit
            Double lat = LatLonCellID.lat;
            Double lon = LatLonCellID.lon;
            jsonObject.put("Latitude", lat);
            jsonObject.put("Longitude", lon);


            Log.v("pop_request_done", jsonObject.toString() + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        parameterString = jsonObject.toString();
        parameterString = parameterString.replace("\\\\\\\\\\\\\\", "\\\\\\");//for images
        return parameterString;
    }

    //Herojit Add
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
    }

    private void galleryIntent() {
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
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult1(Intent data) {
        try {
            String ImagePathName = ImageFilePath.getPath(POPUpdates_ImageUploadDialog.this, data.getData());
            if (ImagePathName != null && ImagePathName.length() > 0 && (ImagePathName.contains("SecurFarm") || ImagePathName.contains("SF_"))) {
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
                if (ImagePathName != null && ImagePathName.length() > 0) {
                    ImageName1 = ImagePathName.replace("/storage/emulated/0/SecurFarm/", "");
                }

            } else {
                setToastSMSShow(2, this, getResources().getString(R.string.PleaseSelectfromSecurFarm));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

//        Bitmap bm = null;
//        if (data != null) {
//            try {
//                bm = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        choose_image1.setImageBitmap(bm);
//        imageString1 = imageToString(bm);
//        String filePath = data.getData().getPath();

    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult2(Intent data) {
        try {
            String ImagePathName = ImageFilePath.getPath(POPUpdates_ImageUploadDialog.this, data.getData());
            if (ImagePathName != null && ImagePathName.length() > 0 && ImagePathName.contains("SecurFarm")) {
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
                if (ImagePathName != null && ImagePathName.length() > 0) {
                    ImageName2 = ImagePathName.replace("/storage/emulated/0/SecurFarm/", "");
                }

            } else {
                setToastSMSShow(2, this, getResources().getString(R.string.PleaseSelectfromSecurFarm));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
//        Bitmap bm = null;
//        if (data != null) {
//            try {
//                bm = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        choose_image2.setImageBitmap(bm);
//        imageString2 = imageToString(bm);
//        String filePath = data.getData().getPath();
    }

    private void onCaptureImageResult1() {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;
        Bitmap bitmap = BitmapFactory.decodeFile(imageStoragePath1, options);
        choose_image1.setImageBitmap(bitmap);
        imageString1 = imageToString(bitmap);

    }

    private void onCaptureImageResult2() {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;
        Bitmap bitmap = BitmapFactory.decodeFile(imageStoragePath2, options);

        choose_image2.setImageBitmap(bitmap);
        imageString2 = imageToString(bitmap);

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
        Button submitImageBTNs = (Button) dialog.findViewById(R.id.submit_image_btn);

        ImageView thumbnail_closeup = (ImageView) dialog.findViewById(R.id.thumbnail_closeup);
        ImageView thumbnail_farm = (ImageView) dialog.findViewById(R.id.thumbnail_farm);
        ImageView thumbnail_pest = (ImageView) dialog.findViewById(R.id.thumbnail_pest);

        thumbnail_closeup.setVisibility(View.GONE);
        thumbnail_farm.setVisibility(View.GONE);
        thumbnail_pest.setVisibility(View.GONE);


        setFontsStyleTxt(this, heading_text, 2);
        setFontsStyle(this, submitImageBTNs);

        setDynamicLanguage(this, submitImageBTNs, "Cancel", R.string.Cancel);

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

        submitImageBTNs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });


        dialog.show();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void uploadImage(final int flag, final String ImageNames) {

        final TransparentProgressDialog pDialog = new TransparentProgressDialog(
                POPUpdates_ImageUploadDialog.this, getDynamicLanguageValue(getApplicationContext(), "Dataisloading", R.string.Dataisloading));
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
                pDialog.cancel();
                Log.i("Response upload image", "" + response.toString());
                //Herojit Comment
                String res = response.toString();
                res = res.replace("\":\"[\\\"", "\":\"");
                res = res.replace("\\\"]\"", "\"");
                res = res.replace("\\", "");
                String Latitude = "0.0", Longitude = "0.0";
                try {
                    db.open();
                    ArrayList<HashMap<String, String>> hasmap = new ArrayList<>();
                    if (ImageNames != null && ImageNames.length() > 0) {
                        String sql = "Select * from " + db.TABLE_ImageLocalStorage + " where ImageName='" + ImageNames + "'";
                        hasmap = db.getDynamicTableValue(sql);
                        if (hasmap != null && hasmap.size() > 0) {
                            Latitude = hasmap.get(0).get("Current_Lat");
                            Longitude = hasmap.get(0).get("Current_Long");
                        } else {
                            Double lat = LatLonCellID.lat;
                            Double lon = LatLonCellID.lon;
                            Latitude = String.valueOf(lat);
                            Longitude = String.valueOf(lon);
                        }
                    }

                    String Imagename = response.getString("uploadBase64ImageResult");
                    if (Imagename.length() > 0) {
                        imageList = Utility.addImageName(Imagename.toString(), imageList, flag, Latitude, Longitude);
                        setNoofPlantDocImages(POPUpdates_ImageUploadDialog.this, AppConstant.farm_id);
                        //Button Disable
                        try {
                            switch (flag) {
                                case 1:
                                    imageupload1.setEnabled(false);
                                    imageupload1.setBackground(getResources().getDrawable(R.drawable.btn_bg_gray));
                                    break;
                                case 2:
                                    imageupload2.setEnabled(false);
                                    imageupload2.setBackground(getResources().getDrawable(R.drawable.btn_bg_gray));
                                    break;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                getDynamicLanguageToast(getApplicationContext(), "SubmittedSuccessfully", R.string.SubmittedSuccessfully);

//                try {
//                    JSONObject jsonObject = new JSONObject(res);
//                    if (jsonObject.get("uploadBase64Image_SchedulerResult").toString().length() > 0) {
////                        addImageName(jsonObject.get("uploadBase64Image_SchedulerResult").toString());
//                        imageList = Utility.addImageName(jsonObject.get("uploadBase64Image_SchedulerResult").toString(), imageList, flag);
//                    }
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                }

//                getResponse(response, co);

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
    }


    private class completePOP extends AsyncTask<Void, Void, String> {

        String result = null;
        String createdString;
        TransparentProgressDialog progressDialog;

        public completePOP(String createdString) {
            this.createdString = createdString;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new TransparentProgressDialog(
                    POPUpdates_ImageUploadDialog.this, getDynamicLanguageValue(getApplicationContext(), "Processing", R.string.Processing));
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @Override
        protected String doInBackground(Void... params) {
            String response = null;
            String sendPath = AppManager.getInstance().pop_done_API;
            response = AppManager.getInstance().httpRequestPutMethodReport(sendPath, createdString);
            System.out.println("AllFarmResponse :---" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPreExecute();
            progressDialog.dismiss();
            try {
                response = response.trim();
                // response = response.substring(1, response.length() - 1);
                response = response.replace("\\", "");
                response = response.replace("\"[", "[");
                response = response.replace("]\"", "]");
                System.out.println("All Farm Response : " + response);
                JSONObject obj = new JSONObject(response);

                if (obj.get("UpdateFarmPopResult").equals("Success"))
                    getDynamicLanguageToast(getApplicationContext(), "SubmittedSuccessfully", R.string.SubmittedSuccessfully);
                else
                    getDynamicLanguageToast(getApplicationContext(), "SubmitFailed", R.string.SubmitFailed);
            } catch (Exception e) {
                e.printStackTrace();
                getDynamicLanguageToast(getApplicationContext(), "ResponseFormattingError", R.string.ResponseFormattingError);
            }
        }
    }

    public void setCustomCameraImageView() {
        try {
            if (CustomCamera_ImageValue != null) {
                switch (Imageselectflag) {
                    case 1:
                        imageString1 = CustomCamera_ImageValue;
                        choose_image1.setImageBitmap(CustomCamera_bitmap);
                        break;
                    case 2:
                        imageString2 = CustomCamera_ImageValue;
                        choose_image2.setImageBitmap(CustomCamera_bitmap);
                        break;
                }
            }
            getCheckCameraScreenOnOff = false;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getCheckCameraScreenOnOff == true) {
            setCustomCameraImageView();
        }
        setLanguages();
    }


    public void setLanguages() {
        //Heading
        setFontsStyleTxt(this, Headingtitle, 2);
        setFontsStyle(this, txt_closeup);
        setFontsStyle(this, txt_farm);
        setFontsStyle(this, imageupload1);
        setFontsStyle(this, imageupload2);
        setFontsStyle(this, submit_image_btn);

        //Tab Service
        setDynamicLanguage(this, Headingtitle, "UploadImage", R.string.UploadImage);
        setDynamicLanguage(this, txt_closeup, "CloseUp", R.string.CloseUp);
        setDynamicLanguage(this, txt_farm, "Farm", R.string.Farm);
        setDynamicLanguage(this, imageupload1, "Upload", R.string.Upload);
        setDynamicLanguage(this, imageupload2, "Upload", R.string.Upload);
        setDynamicLanguage(this, submit_image_btn, "ActivityDone", R.string.ActivityDone);
    }


}
