package com.weather.risk.mfi.myfarminfo.home;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.activities.MainProfileActivity;
import com.weather.risk.mfi.myfarminfo.utils.AppConstant;
import com.weather.risk.mfi.myfarminfo.utils.AppManager;
import com.weather.risk.mfi.myfarminfo.utils.Utility;
import com.weather.risk.mfi.myfarminfo.volley_class.CustomJSONObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class MyServiceActivity extends AppCompatActivity {

    ImageView farmerImage, panImage, passbookImage, otherImage;
    EditText otp, holderName, accountNo, ifscCode, bankName, panEd, aadharED, village, block, district;
    Spinner relationSpinner, ownerRentedSpinner;
    String imageString_farmer, imageString_passbook, imageString_pan, imageString_other;
    RadioButton selfRadio, otherRadio;
    LinearLayout relationLay;
    String relationString = null;
    String landType = null;
    String farmID;
    String otpString;
    Button submitBTN;
    String isselfOther = "self";
    String mobileNO;
    private int REQUEST_CAMERA_START = 0, SELECT_FILE_START = 1;
    private int REQUEST_CAMERA_START_PASS = 2, SELECT_FILE_START_PASS = 3;
    private int REQUEST_CAMERA_START_PAN = 4, SELECT_FILE_START_PAN = 5;
    private int REQUEST_CAMERA_START_OTHER = 6, SELECT_FILE_START_OTHER = 6;
    private String userChoosenTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_image_myservice);
        imageString_farmer = null;
        imageString_passbook = null;
        imageString_pan = null;
        imageString_other = null;
        relationLay = (LinearLayout) findViewById(R.id.other_layout);
        selfRadio = (RadioButton) findViewById(R.id.self_radio);
        otherRadio = (RadioButton) findViewById(R.id.other_radio);
        panEd = (EditText) findViewById(R.id.panNo);
        aadharED = (EditText) findViewById(R.id.aadhar_no);
        selfRadio.setChecked(true);
        relationLay.setVisibility(View.GONE);
        otherRadio.setChecked(false);

        village = (EditText) findViewById(R.id.villagename);
        block = (EditText) findViewById(R.id.blockname);
        district = (EditText) findViewById(R.id.districtname);

        otherRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    selfRadio.setChecked(false);
                    relationLay.setVisibility(View.VISIBLE);
                    otherRadio.setChecked(true);
                    isselfOther = "self";
                }
            }
        });

        selfRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    otherRadio.setChecked(false);
                    relationLay.setVisibility(View.GONE);
                    selfRadio.setChecked(true);
                    isselfOther = "other";
                }
            }
        });

        otpString = getIntent().getStringExtra("otp");
        farmID = getIntent().getStringExtra("f_id");
        mobileNO = getIntent().getStringExtra("mobile_no");

        farmerImage = (ImageView) findViewById(R.id.farmer_image);
        panImage = (ImageView) findViewById(R.id.pancard_image);
        passbookImage = (ImageView) findViewById(R.id.bankpassbook_image);
        otherImage = (ImageView) findViewById(R.id.other_image);

        submitBTN = (Button) findViewById(R.id.submitBTN);
        submitBTN.setVisibility(View.VISIBLE);
       /* if (AppConstant.userTypeID!=null &&(AppConstant.userTypeID.equalsIgnoreCase("1") || AppConstant.userTypeID.equalsIgnoreCase("2")|| AppConstant.userTypeID.equalsIgnoreCase("18"))){
            submitBTN.setVisibility(View.VISIBLE);
        }else {
            submitBTN.setVisibility(View.GONE);
        }*/
        submitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValid()) {
                    if (imageString_farmer != null && imageString_farmer.length() > 10) {
                        uploadImage_farmer(imageString_farmer, holderName.getText().toString().trim(), "farmer");
                    } else if (imageString_passbook != null && imageString_passbook.length() > 10) {
                        uploadImage_passbook(imageString_passbook, holderName.getText().toString().trim(), "passbook");
                    } else if (imageString_pan != null && imageString_pan.length() > 10) {
                        uploadImage_pan(imageString_pan, holderName.getText().toString().trim(), "pan");
                    } else if (imageString_other != null && imageString_other.length() > 10) {
                        uploadImage_other(imageString_other, holderName.getText().toString().trim(), "other");
                    } else {
                        uploadinformation();
                    }
                }
            }
        });

        otp = (EditText) findViewById(R.id.otp_ed);


        holderName = (EditText) findViewById(R.id.holder_name_ed);
        accountNo = (EditText) findViewById(R.id.account_number_ed);
        ifscCode = (EditText) findViewById(R.id.ifsc_code_ed);
        bankName = (EditText) findViewById(R.id.bank_name_ed);

        relationSpinner = (Spinner) findViewById(R.id.relation_spinner);
        final ArrayList<String> list_relation = new ArrayList<>();
        list_relation.add("Select Relation");
        list_relation.add("Son");
        list_relation.add("Daughter");
        list_relation.add("Father");
        list_relation.add("Mother");
        list_relation.add("Neighbour");
        list_relation.add("Brother");
        ArrayAdapter<String> adapter_rela = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list_relation);
        adapter_rela.setDropDownViewResource(android.R.layout.simple_list_item_1);
        relationSpinner.setAdapter(adapter_rela);

        relationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    relationString = list_relation.get(i);
                } else {
                    relationString = null;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ownerRentedSpinner = (Spinner) findViewById(R.id.owned_rented_spinner);
        final ArrayList<String> list = new ArrayList<>();
        list.add("Owned");
        list.add("Rented Land");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        ownerRentedSpinner.setAdapter(adapter);

        ownerRentedSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                landType = list.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        farmerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage_farmer();
            }
        });

        panImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage_pan();
            }
        });

        passbookImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage_passbook();
            }
        });

        otherImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage_other();
            }
        });
    }

    private void selectImage_farmer() {
        final CharSequence[] items = {"Take Photo", "Select From Gallery",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //  builder.setTitle("Upload Document!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {


                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    boolean resultCam = Utility.checkPermissionCamera(MyServiceActivity.this);
                    if (resultCam) {
                        cameraIntent();
                    }

                } else if (items[item].equals("Select From Gallery")) {
                    userChoosenTask = "Select From Gallery";
                    boolean resultCam = Utility.checkPermissionGallery(MyServiceActivity.this);
                    if (resultCam) {
                        galleryIntent();
                    }
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE_START);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA_START);
    }


    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        farmerImage.setImageBitmap(thumbnail);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        imageString_farmer = Base64.encodeToString(byteArray, Base64.DEFAULT);


    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        farmerImage.setImageBitmap(bm);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);

        try {
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            imageString_farmer = Base64.encodeToString(byteArray, Base64.DEFAULT);

        } catch (IllegalAccessError e) {
            e.printStackTrace();
        }


    }

    private void selectImage_passbook() {
        final CharSequence[] items = {"Take Photo", "Select From Gallery",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //  builder.setTitle("Upload Document!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {


                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    boolean resultCam = Utility.checkPermissionCamera(MyServiceActivity.this);
                    if (resultCam) {
                        cameraIntent_pass();
                    }

                } else if (items[item].equals("Select From Gallery")) {
                    userChoosenTask = "Select From Gallery";
                    boolean resultCam = Utility.checkPermissionGallery(MyServiceActivity.this);
                    if (resultCam) {
                        galleryIntent_pass();
                    }
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent_pass() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE_START_PASS);
    }

    private void cameraIntent_pass() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA_START_PASS);
    }


    private void onCaptureImageResult_pass(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        passbookImage.setImageBitmap(thumbnail);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        imageString_passbook = Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult_pass(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        passbookImage.setImageBitmap(bm);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);

        try {
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            imageString_passbook = Base64.encodeToString(byteArray, Base64.DEFAULT);

        } catch (IllegalAccessError e) {
            e.printStackTrace();
        }


    }


    private void selectImage_pan() {
        final CharSequence[] items = {"Take Photo", "Select From Gallery",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //  builder.setTitle("Upload Document!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {


                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    boolean resultCam = Utility.checkPermissionCamera(MyServiceActivity.this);
                    if (resultCam) {
                        cameraIntent_pan();
                    }

                } else if (items[item].equals("Select From Gallery")) {
                    userChoosenTask = "Select From Gallery";
                    boolean resultCam = Utility.checkPermissionGallery(MyServiceActivity.this);
                    if (resultCam) {
                        galleryIntent_pan();
                    }
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void selectImage_other() {
        final CharSequence[] items = {"Take Photo", "Select From Gallery",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //  builder.setTitle("Upload Document!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {


                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    boolean resultCam = Utility.checkPermissionCamera(MyServiceActivity.this);
                    if (resultCam) {
                        cameraIntent_other();
                    }

                } else if (items[item].equals("Select From Gallery")) {
                    userChoosenTask = "Select From Gallery";
                    boolean resultCam = Utility.checkPermissionGallery(MyServiceActivity.this);
                    if (resultCam) {
                        galleryIntent_other();
                    }
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent_pan() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE_START_PAN);
    }

    private void cameraIntent_pan() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA_START_PAN);
    }

    private void galleryIntent_other() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE_START_OTHER);
    }

    private void cameraIntent_other() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA_START_OTHER);
    }


    private void onCaptureImageResult_pan(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        panImage.setImageBitmap(thumbnail);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        imageString_pan = Base64.encodeToString(byteArray, Base64.DEFAULT);


    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult_pan(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        panImage.setImageBitmap(bm);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);

        try {
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            imageString_pan = Base64.encodeToString(byteArray, Base64.DEFAULT);

        } catch (IllegalAccessError e) {
            e.printStackTrace();
        }


    }


    private void onCaptureImageResult_other(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        otherImage.setImageBitmap(thumbnail);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        imageString_other = Base64.encodeToString(byteArray, Base64.DEFAULT);


    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult_other(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        otherImage.setImageBitmap(bm);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);

        try {
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            imageString_other = Base64.encodeToString(byteArray, Base64.DEFAULT);

        } catch (IllegalAccessError e) {
            e.printStackTrace();
        }


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CAMERA_START) {
                onCaptureImageResult(data);
            } else if (requestCode == SELECT_FILE_START) {
                onSelectFromGalleryResult(data);
            } else if (requestCode == REQUEST_CAMERA_START_PASS) {
                onCaptureImageResult_pass(data);
            } else if (requestCode == SELECT_FILE_START_PASS) {
                onSelectFromGalleryResult_pass(data);
            } else if (requestCode == REQUEST_CAMERA_START_PAN) {
                onCaptureImageResult_pan(data);
            } else if (requestCode == SELECT_FILE_START_PAN) {
                onSelectFromGalleryResult_pan(data);
            } else if (requestCode == REQUEST_CAMERA_START_OTHER) {
                onCaptureImageResult_other(data);
            } else if (requestCode == SELECT_FILE_START_OTHER) {
                onSelectFromGalleryResult_other(data);
            }
        }
    }


    private void uploadImage_farmer(String imageS, String fName, String img_type) {

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Uploading Farmer Image...");
        pDialog.setCancelable(false);
        pDialog.show();
        JSONObject jsonObject = null;

        try {
            jsonObject = new JSONObject();

            jsonObject.putOpt("farmID", farmID);
            jsonObject.putOpt("farmerName", fName);
            jsonObject.putOpt("ImageType", img_type);
            jsonObject.putOpt("ImageString", imageS);
            Log.v("logg", jsonObject.toString() + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String URL=AppManager.getInstance().uploadMyserviceImage;
        CustomJSONObjectRequest jsonObjectRequest = new CustomJSONObjectRequest(Request.Method.PUT, AppManager.getInstance().uploadMyserviceImage, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                pDialog.cancel();
                Log.i("Response upload image", "" + response.toString());
                getResponse(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.cancel();

                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                40000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    private void getResponse(JSONObject response) {


        if (response != null) {

            Log.v("Response string", response + "");
            if (imageString_passbook != null && imageString_passbook.length() > 10) {
                uploadImage_passbook(imageString_passbook, holderName.getText().toString().trim(), "passbook");
            } else if (imageString_pan != null && imageString_pan.length() > 10) {
                uploadImage_pan(imageString_pan, holderName.getText().toString().trim(), "pan");
            } else if (imageString_other != null && imageString_other.length() > 10) {
                uploadImage_other(imageString_other, holderName.getText().toString().trim(), "other");
            } else {
                uploadinformation();
            }


        }

    }

    private void uploadImage_passbook(String imageS, String fName, String img_type) {

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Uploading Passbook Image...");
        pDialog.setCancelable(false);
        pDialog.show();
        JSONObject jsonObject = null;

        try {
            jsonObject = new JSONObject();
            jsonObject.putOpt("ImageString", imageS);
            jsonObject.putOpt("farmID", farmID);
            jsonObject.putOpt("farmerName", fName);
            jsonObject.putOpt("ImageType", img_type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        CustomJSONObjectRequest jsonObjectRequest = new CustomJSONObjectRequest(Request.Method.PUT, AppManager.getInstance().uploadMyserviceImage, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                pDialog.cancel();
                Log.i("Response upload image", "" + response.toString());
                getResponse2(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.cancel();

                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                40000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    private void getResponse2(JSONObject response) {


        if (response != null) {

            Log.v("Response string", response + "");
            if (imageString_pan != null && imageString_pan.length() > 10) {
                uploadImage_pan(imageString_pan, holderName.getText().toString().trim(), "pan");
            } else if (imageString_other != null && imageString_other.length() > 10) {
                uploadImage_other(imageString_other, holderName.getText().toString().trim(), "other");
            } else {
                uploadinformation();
            }


        }

    }

    private void uploadImage_pan(String imageS, String fName, String img_type) {

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Uploading Pan Image...");
        pDialog.setCancelable(false);
        pDialog.show();
        JSONObject jsonObject = null;

        try {
            jsonObject = new JSONObject();
            jsonObject.putOpt("ImageString", imageS);
            jsonObject.putOpt("farmID", farmID);
            jsonObject.putOpt("farmerName", fName);
            jsonObject.putOpt("ImageType", img_type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        CustomJSONObjectRequest jsonObjectRequest = new CustomJSONObjectRequest(Request.Method.PUT, AppManager.getInstance().uploadMyserviceImage, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                pDialog.cancel();
                Log.i("Response upload image", "" + response.toString());
                getResponse3(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.cancel();

                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                40000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    private void getResponse3(JSONObject response) {


        if (response != null) {

            Log.v("Response string", response + "");
            if (imageString_other != null && imageString_other.length() > 10) {
                uploadImage_other(imageString_other, holderName.getText().toString().trim(), "other");
            } else {
                uploadinformation();
            }


        }

    }


    private void uploadImage_other(String imageS, String fName, String img_type) {

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Uploading Pan Image...");
        pDialog.setCancelable(false);
        pDialog.show();
        JSONObject jsonObject = null;

        try {
            jsonObject = new JSONObject();
            jsonObject.putOpt("ImageString", imageS);
            jsonObject.putOpt("farmID", farmID);
            jsonObject.putOpt("farmerName", fName);
            jsonObject.putOpt("ImageType", img_type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        CustomJSONObjectRequest jsonObjectRequest = new CustomJSONObjectRequest(Request.Method.PUT, AppManager.getInstance().uploadMyserviceImage, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                pDialog.cancel();
                Log.i("Response upload image", "" + response.toString());
                getResponse4(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.cancel();

                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                40000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    private void getResponse4(JSONObject response) {


        if (response != null) {

            Log.v("Response string", response + "");
            uploadinformation();


        }

    }

    private void uploadinformation() {

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Uploading informations please wait...");
        pDialog.setCancelable(false);
        pDialog.show();
        JSONObject jsonObject = null;

        String otpp = otp.getText().toString().trim();
        String farmerName = holderName.getText().toString().trim();
        String bankNm = bankName.getText().toString().trim();
        String accounNo = accountNo.getText().toString().trim();
        String ifscC = ifscCode.getText().toString().trim();
        String panN = panEd.getText().toString().trim();
        String aadharN = aadharED.getText().toString().trim();
        try {
            jsonObject = new JSONObject();
            jsonObject.putOpt("OTP", otpp);
            jsonObject.putOpt("PanNo", panN);
            jsonObject.putOpt("AccountNo", AppConstant.user_id);
            jsonObject.putOpt("MobileNo", mobileNO);
            jsonObject.putOpt("AdhaarNo", aadharN);
            jsonObject.putOpt("Village", village.getText().toString().trim());
            jsonObject.putOpt("Block", block.getText().toString().trim());
            jsonObject.putOpt("District", district.getText().toString().trim());
            jsonObject.putOpt("FarmID", farmID);
            jsonObject.putOpt("FatherName", farmerName);
            jsonObject.putOpt("BankName", bankNm);
            jsonObject.putOpt("AccountNo", accounNo);
            jsonObject.putOpt("IFSCCode", ifscC);
            jsonObject.putOpt("ImageType", relationString);
            jsonObject.putOpt("AccountType", isselfOther);
            jsonObject.putOpt("AccountOwnerType ", landType);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject saveFarmerInfoJson = new JSONObject();
        try {
            saveFarmerInfoJson.putOpt("information", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CustomJSONObjectRequest jsonObjectRequest = new CustomJSONObjectRequest(Request.Method.PUT, AppManager.getInstance().uploadMyserviceInfo, saveFarmerInfoJson, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                pDialog.cancel();
                if (response != null) {
                    Log.v("InformationSubmitted", "" + response.toString());
                    Toast.makeText(getApplicationContext(), "Submitted informations successfully", Toast.LENGTH_SHORT).show();

                    imageString_farmer = null;
                    imageString_passbook = null;
                    imageString_pan = null;
                    imageString_other = null;

                    Intent intent = new Intent(MyServiceActivity.this, MainProfileActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.cancel();

                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                40000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    public boolean isValid() {
        boolean flag = false;
        String otpp = otp.getText().toString().trim();
        String panN = panEd.getText().toString().trim();
        String aadharN = aadharED.getText().toString().trim();
        String farmerName = holderName.getText().toString().trim();
        String bankNm = bankName.getText().toString().trim();
        String accounNo = accountNo.getText().toString().trim();
        String ifscC = ifscCode.getText().toString().trim();

        if (otpp != null && otpp.equalsIgnoreCase(otpString)) {
            flag = true;
        } else {

            Toast.makeText(getApplicationContext(), "please enter valid OTP", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (panN != null && panN.length() > 4) {
            flag = true;
        } else {

            Toast.makeText(getApplicationContext(), "please enter valid PAN", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (aadharN != null && aadharN.length() > 11) {
            flag = true;
        } else {

            Toast.makeText(getApplicationContext(), "please enter valid Aadhar Number", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (farmerName != null && farmerName.length() > 1) {
            flag = true;
        } else {
            Toast.makeText(getApplicationContext(), "please enter Farmer Name", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (bankNm != null && bankNm.length() > 2) {
            flag = true;
        } else {
            Toast.makeText(getApplicationContext(), "please enter Bank Name", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (accounNo != null && accounNo.length() > 7) {
            flag = true;

        } else {
            Toast.makeText(getApplicationContext(), "please enter valid account no", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (ifscC != null && ifscC.length() >= 10) {
            flag = true;
        } else {
            Toast.makeText(getApplicationContext(), "please enter valid IFSC Code", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (isselfOther.equalsIgnoreCase("other")) {
            if (relationString == null) {
                Toast.makeText(getApplicationContext(), "please select farmer relation", Toast.LENGTH_SHORT).show();
                return false;
            } else {
                flag = true;
            }
        }


        return flag;
    }
}
