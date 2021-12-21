package com.weather.risk.mfi.myfarminfo.cattledashboard;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.customcamera.CameraSurfaceView;
import com.weather.risk.mfi.myfarminfo.database.DBAdapter;
import com.weather.risk.mfi.myfarminfo.databinding.CattledoctorsBinding;
import com.weather.risk.mfi.myfarminfo.databinding.PopupCattlesdoctorBinding;
import com.weather.risk.mfi.myfarminfo.home.AppController;
import com.weather.risk.mfi.myfarminfo.live_cotton.LogOldMessageAdapter;
import com.weather.risk.mfi.myfarminfo.live_cotton.MessegeListBean;
import com.weather.risk.mfi.myfarminfo.mapfragments.LatLonCellID;
import com.weather.risk.mfi.myfarminfo.utils.AppConstant;
import com.weather.risk.mfi.myfarminfo.utils.AppManager;
import com.weather.risk.mfi.myfarminfo.utils.TransparentProgressDialog;
import com.weather.risk.mfi.myfarminfo.utils.UtilFonts;
import com.weather.risk.mfi.myfarminfo.utils.Utility;
import com.weather.risk.mfi.myfarminfo.volley_class.CustomJSONObjectRequest;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import static com.weather.risk.mfi.myfarminfo.cattledashboard.CattleDashboards.CattleDash_SelectCattleFarmerID;
import static com.weather.risk.mfi.myfarminfo.customcamera.CameraSurfaceView.IMAGE_DIRECTORY;
import static com.weather.risk.mfi.myfarminfo.logging.LocalFileWriter.SaveLocalFile;
import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.CustomCamera_ImageValue;
import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.CustomCamera_bitmap;
import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.SN_CattleDoctors;
import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.getCheckCameraScreenOnOff;
import static com.weather.risk.mfi.myfarminfo.utils.AppManager.NOGPSDialog;
import static com.weather.risk.mfi.myfarminfo.utils.AppManager.getCreateCallRecord;
import static com.weather.risk.mfi.myfarminfo.utils.AppManager.getCreateImageName;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getAPIimeResponseinSecond;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getTotalNoimageperDay;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getUIDforScreenTracking;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setNoofPlantDocImages;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setScreenTracking;


public class CattleDoctors extends AppCompatActivity {

    CattledoctorsBinding binding;
    DBAdapter db;

    SharedPreferences prefs;
    String phone, name, msg, locality, country, zipCode, subLocality, district, lati, longi, selectedState;
    private int REQUEST_CAMERA_START = 0, SELECT_FILE_START = 1;
    public static final int RequestPermissionCode = 1;
    String cropName = "Cotton", sendFlag = null, responsePath = null;
    int Imageselectflag = 0;
    String imageString1 = null, imageString2 = null;
    private String imageStoragePath1 = "", imageStoragePath2 = "";
    private int REQUEST_CAMERA_START1 = 0, SELECT_FILE_START1 = 1;
    private int REQUEST_CAMERA_START2 = 3, SELECT_FILE_START2 = 2;
    JSONArray imageList = new JSONArray();
    String ImageName1 = "", ImageName2 = "";
    private long mRequestStartTime;
    PopupCattlesdoctorBinding popbinding;
    Random random;
    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;
    String UID = "", URL = "", imageString, userChoosenTask, AudioSavePathInDevice = null, RandomAudioFileName = "ABCDEFGHIJKLMNOP", uploadedPicName;
    TransparentProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.cattledoctors);
        //        setFonts();
        db = new DBAdapter(this);

        prefs = getSharedPreferences(AppConstant.SHARED_PREFRENCE_NAME, MODE_PRIVATE);

        phone = AppConstant.mobile_no;
        name = AppConstant.visible_Name;
        if (!AppManager.getInstance().isLocationServicesAvailable(this)) {
            NOGPSDialog(this);
        }
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            NOGPSDialog(this);
        }

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            //bundle must contain all info sent in "data" field of the notification
            try {
                if (bundle.size() > 0) {
                    lati = bundle.getString("lat");
                    longi = bundle.getString("long");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        binding.backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (lati == null || lati.length() < 1 || longi == null || longi.length() < 1) {
            longi = "" + LatLonCellID.lat;
            longi = "" + LatLonCellID.lon;
        }
        if (lati != null && longi != null) {
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(this, Locale.getDefault());

            try {
                addresses = geocoder.getFromLocation(Double.parseDouble(lati), Double.parseDouble(longi), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                subLocality = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                locality = addresses.get(0).getLocality();
                selectedState = addresses.get(0).getAdminArea();
                country = addresses.get(0).getCountryName();
                zipCode = addresses.get(0).getPostalCode();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        binding.imgeviewAddnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SMSchattingPopup();
            }
        });


        binding.swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                binding.swipeContainer.setRefreshing(true);
                loadMessageList(AppConstant.mobile_no);
            }
        });
        binding.swipeContainer.setColorSchemeResources(R.color.ColorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        loadMessageList(AppConstant.mobile_no);
        //ScreenTracking
        UID = getUIDforScreenTracking();
        setScreenTracking(CattleDoctors.this, db, SN_CattleDoctors, UID);
    }

    public void SMSchattingPopup() {
        try {
            sendFlag = null;
            imageString1 = null;
            imageString2 = null;
            imageList = new JSONArray();
            Imageselectflag = 0;

            final Dialog dialog = new Dialog(this, R.style.DialogSlideAnim);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            Window window = dialog.getWindow();
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

            WindowManager.LayoutParams wlp = window.getAttributes();
            wlp.gravity = Gravity.CENTER;
            wlp.dimAmount = 0.5f;

            dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            // wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            window.setAttributes(wlp);
            // Include dialog.xml file
            popbinding = PopupCattlesdoctorBinding.inflate(LayoutInflater.from(this));
            dialog.setContentView(popbinding.getRoot());
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            UtilFonts.UtilFontsInitialize(this);
            popbinding.txtLogNewRequest.setTypeface(UtilFonts.FS_Ultra);

            popbinding.txtImage.setTypeface(UtilFonts.KT_Light);
            popbinding.txtVoicerecord.setTypeface(UtilFonts.KT_Light);
            popbinding.txtMessage.setTypeface(UtilFonts.KT_Light);
            popbinding.logNewMessage.setTypeface(UtilFonts.KT_Medium);

            popbinding.txtCloseup.setTypeface(UtilFonts.KT_Medium);
            popbinding.txtFarm.setTypeface(UtilFonts.KT_Medium);
            popbinding.imageupload1.setTypeface(UtilFonts.KT_Medium);
            popbinding.imageupload2.setTypeface(UtilFonts.KT_Medium);

            popbinding.imageupload1.setEnabled(true);
            popbinding.imageupload2.setEnabled(true);
            popbinding.imageupload1.setBackground(getResources().getDrawable(R.drawable.btn_bg));
            popbinding.imageupload2.setBackground(getResources().getDrawable(R.drawable.btn_bg));

            random = new Random();

            popbinding.txtCloseup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ThumbNailImages(1);
                }
            });
            popbinding.txtFarm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ThumbNailImages(2);
                }
            });
            popbinding.chooseImage1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Imageselectflag = 1;
                    imageString1 = null;
                    popbinding.chooseImage1.setImageBitmap(null);
                    selectImage(1);
                }
            });

            popbinding.chooseImage2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Imageselectflag = 2;
                    imageString2 = null;
                    popbinding.chooseImage2.setImageBitmap(null);
                    selectImage(2);
                }
            });

            popbinding.imageupload1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (imageString1 != null && imageString1.length() > 0) {
                        if (getTotalNoimageperDay(CattleDoctors.this, AppConstant.farm_id) < 6) {
                            if (isNetworkAvailable()) {
                                uploadImage(1, ImageName1);
                            } else {
                                Toast.makeText(CattleDoctors.this, getResources().getString(R.string.Networknotavailableclicksubmit), Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(CattleDoctors.this, getResources().getString(R.string.Yourlimitedexpired), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(CattleDoctors.this, getResources().getString(R.string.selecttheimage), Toast.LENGTH_SHORT).show();
                    }
                }
            });
            popbinding.imageupload2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (imageString2 != null && imageString2.length() > 0) {
                        if (getTotalNoimageperDay(CattleDoctors.this, AppConstant.farm_id) < 6) {
                            if (isNetworkAvailable()) {
                                uploadImage(2, ImageName2);
                            } else {
                                Toast.makeText(CattleDoctors.this, getResources().getString(R.string.Networknotavailableclicksubmit), Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(CattleDoctors.this, getResources().getString(R.string.Yourlimitedexpired), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(CattleDoctors.this, getResources().getString(R.string.selecttheimage), Toast.LENGTH_SHORT).show();
                    }
                }
            });
//            popbinding.ImageDetection1.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
////                if (imageString1 != null && imageString1.length() > 0) {
////                    Intent in = new Intent(CattleDoctors.this, ImageDetectionPOPup.class);
//                    Intent in = new Intent(CattleDoctors.this, ObjectDetectorActivity.class);
//                    startActivity(in);
////                } else {
////                    Toast.makeText(CattleDoctors.this, getResources().getString(R.string.selecttheimage), Toast.LENGTH_SHORT).show();
////                }
//                }
//            });
//            popbinding.ImageDetection2.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
////                if (imageString2 != null && imageString2.length() > 0) {
////                    Intent in = new Intent(CattleDoctors.this, ImageDetectionPOPup.class);
//                    Intent in = new Intent(CattleDoctors.this, ObjectDetectorActivity.class);
//                    startActivity(in);
////                } else {
////                    Toast.makeText(CattleDoctors.this, getResources().getString(R.string.selecttheimage), Toast.LENGTH_SHORT).show();
////                }
//                }
//            });

            popbinding.imgeviewLogrequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectImage(1);
                }
            });
            popbinding.btnLogRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isNetworkAvailable()) {
                        if (isValid()) {
                            //Upload Voice
                            dialog.dismiss();
//                        uploadImage();
                            if (imageList != null && imageList.length() > 0) {
                                String Imagelist = imageList.toString();
                                getResponse(Imagelist);
                            }
                        }
                    } else {
                        if (isValidwithounetwork()) {
                            dialog.dismiss();
                            String Jsonvalue = createNewLogStringToLoacalStore("", popbinding.logNewMessage.getText().toString());
                            db.open();
//                        db.saveLocalPlatdoc(ImageName1, ImageName1, VoiceRecName, Jsonvalue);
                            db.saveLocalPlatdoc(ImageName1, ImageName1, AudioSavePathInDevice, Jsonvalue);
                            Toast.makeText(CattleDoctors.this, getResources().getString(R.string.Networknotavailablenow), Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });

            popbinding.imgeviewCross.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                sendFlag = null;
//                responsePath = null;
//                dialog.cancel();
                    confirmationPOPup(dialog);
                }
            });


            popbinding.btnRecording.setBackground(getDrawable(R.drawable.icon_recordingon));
            popbinding.btnStop.setBackground(getDrawable(R.drawable.icon_stopoff));
            popbinding.btnPlay.setBackground(getDrawable(R.drawable.icon_playingoff));
            popbinding.btnSpeaker.setBackground(getDrawable(R.drawable.icon_speakeroff));
            popbinding.btnClear.setBackground(getDrawable(R.drawable.icon_clearoff));

            popbinding.btnRecording.setEnabled(true);
            popbinding.btnStop.setEnabled(false);
            popbinding.btnPlay.setEnabled(false);
            popbinding.btnSpeaker.setEnabled(false);
            popbinding.btnClear.setEnabled(false);


            //Button Listioner
            popbinding.btnRecording.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (checkPermission()) {

                        popbinding.btnRecording.setBackground(getDrawable(R.drawable.icon_recordingoff));
                        popbinding.btnStop.setBackground(getDrawable(R.drawable.icon_stopon));
                        popbinding.btnPlay.setBackground(getDrawable(R.drawable.icon_playingoff));
                        popbinding.btnSpeaker.setBackground(getDrawable(R.drawable.icon_speakeroff));
                        popbinding.btnClear.setBackground(getDrawable(R.drawable.icon_clearon));

                        popbinding.btnRecording.setEnabled(false);
                        popbinding.btnStop.setEnabled(true);
                        popbinding.btnPlay.setEnabled(false);
                        popbinding.btnSpeaker.setEnabled(false);
                        popbinding.btnClear.setEnabled(true);


//                    AudioSavePathInDevice = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +
//                            CreateRandomAudioFileName(5) + "AudioRecording.mp3";
                        AudioSavePathInDevice = Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY + "/" + getCreateCallRecord();
                        MediaRecorderReady();
                        try {
                            mediaRecorder.prepare();
                            mediaRecorder.start();
                        } catch (IllegalStateException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        Toast.makeText(CattleDoctors.this, getResources().getString(R.string.Recordingisstarted), Toast.LENGTH_LONG).show();
                        sendFlag = "start";
                    } else {
                        requestPermission();
                    }
                }
            });
            popbinding.btnStop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    try {
                        popbinding.btnRecording.setBackground(getDrawable(R.drawable.icon_recordingon));
                        popbinding.btnStop.setBackground(getDrawable(R.drawable.icon_stopoff));
                        popbinding.btnPlay.setBackground(getDrawable(R.drawable.icon_playingon));
                        popbinding.btnSpeaker.setBackground(getDrawable(R.drawable.icon_speakeroff));
                        popbinding.btnClear.setBackground(getDrawable(R.drawable.icon_clearon));

                        popbinding.btnRecording.setEnabled(true);
                        popbinding.btnStop.setEnabled(false);
                        popbinding.btnPlay.setEnabled(true);
                        popbinding.btnSpeaker.setEnabled(false);
                        popbinding.btnClear.setEnabled(false);

                        mediaRecorder.stop();
                        Toast.makeText(CattleDoctors.this, getResources().getString(R.string.Recordingiscompleted),
                                Toast.LENGTH_LONG).show();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
            popbinding.btnPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    popbinding.btnRecording.setBackground(getDrawable(R.drawable.icon_recordingoff));
                    popbinding.btnStop.setBackground(getDrawable(R.drawable.icon_stopoff));
                    popbinding.btnPlay.setBackground(getDrawable(R.drawable.icon_playingon));
                    popbinding.btnSpeaker.setBackground(getDrawable(R.drawable.icon_speakeron));
                    popbinding.btnClear.setBackground(getDrawable(R.drawable.icon_clearon));

                    popbinding.btnRecording.setEnabled(false);
                    popbinding.btnStop.setEnabled(false);
                    popbinding.btnPlay.setEnabled(true);
                    popbinding.btnSpeaker.setEnabled(true);
                    popbinding.btnClear.setEnabled(true);

                    mediaPlayer = new MediaPlayer();
                    try {
                        mediaPlayer.setDataSource(AudioSavePathInDevice);
                        mediaPlayer.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    mediaPlayer.start();
                    Toast.makeText(CattleDoctors.this, getResources().getString(R.string.Recordingisplaying),
                            Toast.LENGTH_LONG).show();
                }
            });
            popbinding.btnSpeaker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    popbinding.btnRecording.setBackground(getDrawable(R.drawable.icon_recordingon));
                    popbinding.btnStop.setBackground(getDrawable(R.drawable.icon_stopoff));
                    popbinding.btnPlay.setBackground(getDrawable(R.drawable.icon_playingon));
                    popbinding.btnSpeaker.setBackground(getDrawable(R.drawable.icon_speakeroff));
                    popbinding.btnClear.setBackground(getDrawable(R.drawable.icon_clearon));

                    popbinding.btnRecording.setEnabled(true);
                    popbinding.btnStop.setEnabled(false);
                    popbinding.btnPlay.setEnabled(true);
                    popbinding.btnSpeaker.setEnabled(false);
                    popbinding.btnClear.setEnabled(true);

                    if (mediaPlayer != null) {
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        MediaRecorderReady();
                    }
                }
            });
            popbinding.btnClear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    popbinding.btnRecording.setBackground(getDrawable(R.drawable.icon_recordingon));
                    popbinding.btnStop.setBackground(getDrawable(R.drawable.icon_stopoff));
                    popbinding.btnPlay.setBackground(getDrawable(R.drawable.icon_playingoff));
                    popbinding.btnSpeaker.setBackground(getDrawable(R.drawable.icon_speakeroff));
                    popbinding.btnClear.setBackground(getDrawable(R.drawable.icon_clearoff));

                    popbinding.btnRecording.setEnabled(true);
                    popbinding.btnStop.setEnabled(false);
                    popbinding.btnPlay.setEnabled(false);
                    popbinding.btnSpeaker.setEnabled(false);
                    popbinding.btnClear.setEnabled(false);

                    if (mediaPlayer != null) {
                        mediaRecorder = new MediaRecorder();
//                    mediaPlayer.stop();
//                    mediaPlayer.release();
//                    MediaRecorderReady();
                    }
                    sendFlag = null;
                    responsePath = null;
                }
            });
            dialog.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new
                String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, RequestPermissionCode);
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
                heading_text.setText(String.valueOf(getResources().getString(R.string.CloseUpThumbnail)));
                break;
            case 2:
                thumbnail_farm.setVisibility(View.VISIBLE);
                heading_text.setText(String.valueOf(getResources().getString(R.string.FarmThumbnail)));
                break;
            case 3:
                thumbnail_pest.setVisibility(View.VISIBLE);
                heading_text.setText(String.valueOf(getResources().getString(R.string.PestTrapThumbnail)));
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

    private void selectImage(final int flag) {
        final CharSequence[] items = {getResources().getString(R.string.TakePhoto), getResources().getString(R.string.Selectfromgallery),
                getResources().getString(R.string.Cancel)};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //  builder.setTitle("Upload Document!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {


                if (items[item].equals(getResources().getString(R.string.TakePhoto))) {
                    userChoosenTask = "Take Photo";
                    boolean resultCam = Utility.checkPermissionCamera(CattleDoctors.this);
                    String imageFileName = getCreateImageName();
                    if (resultCam) {
//                        cameraIntent();
//                        getCheckCameraScreenOnOff = true;
                        Intent in = new Intent(CattleDoctors.this, CameraSurfaceView.class);
                        in.putExtra("CameraScreenTypeNearFar", "Near");//Close Up
                        in.putExtra("ActivityNameComingFrom", "CattleDoctor");
                        in.putExtra("FarmID", CattleDash_SelectCattleFarmerID);
                        if (flag == 1) {
                            ImageName1 = imageFileName;
                        } else if (flag == 2) {
                            ImageName2 = imageFileName;
                        }
                        in.putExtra("imageFileName", imageFileName);
                        startActivity(in);
                    }

                } else if (items[item].equals(getResources().getString(R.string.Selectfromgallery))) {
                    userChoosenTask = "Select From Gallery";
                    boolean resultCam = Utility.checkPermissionGallery(CattleDoctors.this);
                    if (resultCam) {
                        galleryIntent();
                    }
                } else if (items[item].equals(getResources().getString(R.string.Cancel))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
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

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void MediaRecorderReady() {
        try {
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
            mediaRecorder.setOutputFile(AudioSavePathInDevice);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void sendData(final String imgename) {

//        String url = "http://pdjalna.apimfi.com/PDService.svc/UploadVoice/";
        String url = AppManager.getInstance().VoiceUploadURL;
        File file = new File(AudioSavePathInDevice);
        try {
            HttpClient httpclient = new DefaultHttpClient();

            HttpPost httppost = new HttpPost(url);

            InputStreamEntity reqEntity = new InputStreamEntity(
                    new FileInputStream(file), -1);
            reqEntity.setContentType("binary/octet-stream");
            reqEntity.setChunked(true); // Send in multiple parts if needed
            httppost.setEntity(reqEntity);
            HttpResponse response = httpclient.execute(httppost);

            BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer sb = new StringBuffer("");
            String line = "";
            String NL = System.getProperty("line.separator");
            while ((line = in.readLine()) != null) {
                sb.append(line + NL);
            }
            in.close();

            String result = sb.toString();
            System.out.println("Result : " + result);

            if (result != null) {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.has("UploadVoiceResult")) {
                    responsePath = jsonObject.getString("UploadVoiceResult");
                } else {
                    responsePath = null;
                }
            }
            if (responsePath != null) {
                new Thread(new Runnable() {
                    public void run() {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                AlertDialog.Builder builder = new AlertDialog.Builder(CattleDoctors.this);
                                builder.setCancelable(false);
                                builder.setTitle(getResources().getString(R.string.Success)).
                                        setMessage(getResources().getString(R.string.Voiceuploadedsuccessfully)).
                                        setPositiveButton(getResources().getString(R.string.Ok), new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.cancel();
                                                if (msg != null && msg.length() > 0) {
                                                    createNewLogStringToServer(imgename, msg);
                                                } else {
                                                    createNewLogStringToServer(imgename, "Voice_Message");
                                                }
                                            }
                                        });

                                AlertDialog dialog = builder.create();
                                dialog.show();
                                Log.v("jksajkjsk", responsePath + "");
                            }
                        });
                    }
                }).start();
            }
        } catch (Exception e) {
            // show error
        }
    }

//    public void setCustomCameraImageView() {
//        try {
//            if (CustomCamera_ImageValue != null) {
////                imageString = CustomCamera_ImageValue;
////                imgeview_logrequest.setImageBitmap(CustomCamera_bitmap);
//                if (Imageselectflag == 1) {
//                    imageString1 = CustomCamera_ImageValue;
//                    popbinding.chooseImage1.setImageBitmap(CustomCamera_bitmap);
//                } else if (Imageselectflag == 2) {
//                    imageString2 = CustomCamera_ImageValue;
//                    popbinding.chooseImage2.setImageBitmap(CustomCamera_bitmap);
//                } else {
//                    imageString = CustomCamera_ImageValue;
//                    popbinding.imgeviewLogrequest.setImageBitmap(CustomCamera_bitmap);
//                }
//            }
//            getCheckCameraScreenOnOff = false;
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }

    public boolean isValid() {
        msg = popbinding.logNewMessage.getText().toString().trim();
//        if (imageString != null && imageString.length() > 0) {
        if (imageList != null && imageList.length() > 0) {
            msg = popbinding.logNewMessage.getText().toString();
            if ((msg != null && msg.length() > 0) || (sendFlag != null && sendFlag.length() > 0)) {
                return true;
            } else {
                Toast.makeText(CattleDoctors.this, getResources().getString(R.string.Pleasetextaudiosms), Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            Toast.makeText(CattleDoctors.this, getResources().getString(R.string.selecttheimageupload), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public boolean isValidwithounetwork() {
        msg = popbinding.logNewMessage.getText().toString().trim();
//        if (imageString != null && imageString.length() > 0) {
        if ((imageString1 != null && imageString1.length() > 2) || (imageString2 != null && imageString2.length() > 2)) {
            msg = popbinding.logNewMessage.getText().toString();
            if ((msg != null && msg.length() > 0) || (sendFlag != null && sendFlag.length() > 0)) {
                return true;
            } else {
                Toast.makeText(CattleDoctors.this, getResources().getString(R.string.Pleasetextaudiosms), Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            Toast.makeText(CattleDoctors.this, getResources().getString(R.string.selecttheimage), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void getResponse(String ImageNameList) {
        uploadedPicName = null;
        if (ImageNameList != null) {
            try {
                uploadedPicName = ImageNameList;
                //Add IMage type add
                if (sendFlag != null) {
                    responsePath = null;
                    new Thread(new Runnable() {
                        public void run() {
                            if (sendFlag != null) {
                                sendData(uploadedPicName);
                            } else {
                                Toast.makeText(CattleDoctors.this, getResources().getString(R.string.Pleaserecordthevoicefirst), Toast.LENGTH_SHORT).show();
                            }
                            dialog.cancel();
                        }
                    }).start();

                } else {
                    if (uploadedPicName != null) {
                        createNewLogStringToServer(uploadedPicName, msg);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void createNewLogStringToServer(String picName, String message) {

        String URL = AppManager.getInstance().createNewLogURL;

        final TransparentProgressDialog pDialog = new TransparentProgressDialog(this,
                getResources().getString(R.string.Dataisloading));
        pDialog.show();
        JSONObject jsonObject = null;

        try {
            jsonObject = new JSONObject();
            jsonObject.put("filename", picName);
            if (phone != null) {
                jsonObject.put("PhoneNumber", phone);
            } else {
                jsonObject.put("PhoneNumber", "8285686540");
            }
            jsonObject.put("state", selectedState);
            jsonObject.put("district", subLocality);
            jsonObject.put("locality", locality);
            jsonObject.put("country", country);
            jsonObject.put("zipcode", zipCode);
            jsonObject.put("lat", lati);
            jsonObject.put("lon", longi);
            jsonObject.put("Name", name);
            jsonObject.put("Crop", cropName);
            jsonObject.put("CropID", "12");
            jsonObject.put("VisibleName", name);
            jsonObject.put("Message", message);
            jsonObject.put("VoiceFile", responsePath);
            jsonObject.put("UserId", AppConstant.user_id);
            //Add Herojit
            jsonObject.put("FarmID", AppConstant.farm_id);

//            JSONObject ob = new JSONObject(jsonObject.toString());
            String JSON = jsonObject.toString();
            JSON = JSON.replace("\\\\\\\\\\\\\\", "\\\\\\");
            jsonObject = new JSONObject();
            jsonObject = new JSONObject(JSON);

            Log.v("request", jsonObject.toString());


        } catch (JSONException e) {
            e.printStackTrace();
        }
        mRequestStartTime = System.currentTimeMillis();
        CustomJSONObjectRequest jsonObjectRequest = new CustomJSONObjectRequest(Request.Method.PUT, URL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                pDialog.cancel();
                Log.i("Response new log", "" + response.toString());
                uploadedPicName = null;
                int seconds = getAPIimeResponseinSecond(mRequestStartTime);
                if (seconds > 3)
                    SaveLocalFile(db, CattleDoctors.this, SN_CattleDoctors, URL, response.toString(), "", "" + seconds, AppConstant.farm_id, "Working");
                //Refresh the screen
                loadMessageList(AppConstant.mobile_no);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.cancel();
                Log.v("Response new log", "" + error.toString());
                Toast.makeText(CattleDoctors.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                int seconds = getAPIimeResponseinSecond(mRequestStartTime);
                SaveLocalFile(db, CattleDoctors.this, SN_CattleDoctors, URL, "", "Internet Connection Error / Server API Error / Timeout Error", "" + seconds, AppConstant.farm_id, "Error");
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                40000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    public void loadMessageList(String mobileno) {
        dialog = new TransparentProgressDialog(this,
                getResources().getString(R.string.Dataisloading));
        dialog.show();
        URL = "https://myfarminfo.com/yfirest.svc/pd/RequestData/0/0/0/" + AppConstant.user_id + "/0/0/0";
//        Log.v("sjdks_old", URLdomain + "PDService.svc/Threads/" + mobileno);
        Log.v("sjdks_new", "https://myfarminfo.com/yfirest.svc/pd/RequestData/0/0/0/" + AppConstant.user_id + "/0/0/0");
        mRequestStartTime = System.currentTimeMillis();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            int seconds = getAPIimeResponseinSecond(mRequestStartTime);
                            binding.swipeContainer.setRefreshing(false);
                            response = response.trim();
                            if (seconds > 3) {
                                SaveLocalFile(db, CattleDoctors.this, SN_CattleDoctors, URL, response, "", "" + seconds, AppConstant.farm_id, "Working");
                            }
                            //Herojit Comment
//                            response = "" + Html.fromHtml(response);
                            // response = response.substring(1, response.length() - 1);

//                        response = response.replace("\\", "");//
//                        response = response.replace("\\", "");
//                        response = response.replace("\"{", "{");
//                        response = response.replace("}\"", "}");
//                        response = response.replace("\"[", "[");
//                        response = response.replace("]\"", "]");

                            GsonBuilder builder = new GsonBuilder();
                            Gson gson = builder.create();
                            String str = gson.fromJson(response.toString(), String.class);
                            response = str;

                            System.out.println("old log response : " + response);

                            if (!response.equalsIgnoreCase("\"NoData\"")) {
                                try {

                                    ArrayList<MessegeListBean> messageList = new ArrayList<MessegeListBean>();
                                    JSONArray ja = new JSONArray(response);
                                    for (int i = 0; i < ja.length(); i++) {
                                        MessegeListBean bean = new MessegeListBean();
                                        JSONObject jsonObject = ja.getJSONObject(i);
                                        bean.setRequestID(jsonObject.getString("ReqId"));
                                        bean.setDate(jsonObject.getString("RequestDate"));
                                        bean.setMessage(jsonObject.getString("Message"));
                                        bean.setStatus(jsonObject.getString("Status"));
                                        bean.setImageName(jsonObject.getString("ImagePath"));
                                        messageList.add(bean);
                                    }

                                    if (messageList.size() > 0) {
                                        LinearLayoutManager llm = new LinearLayoutManager(CattleDoctors.this);
                                        llm.setOrientation(LinearLayoutManager.VERTICAL);
                                        binding.messageRecyclerView.setLayoutManager(llm);
                                        LogOldMessageAdapter adapter = new LogOldMessageAdapter(CattleDoctors.this, messageList);
                                        binding.messageRecyclerView.setAdapter(adapter);

                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    SaveLocalFile(db, CattleDoctors.this, SN_CattleDoctors, URL, response, "JSON Response Error", "" + seconds, AppConstant.farm_id, "Error");
                                }
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        dialog.cancel();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                int seconds = getAPIimeResponseinSecond(mRequestStartTime);
                SaveLocalFile(db, CattleDoctors.this, SN_CattleDoctors, URL, "", "Internet Connection Error / Server API Error / Timeout Error", "" + seconds, AppConstant.farm_id, "Error");
                dialog.cancel();
                System.out.println("Volley Error : " + error);
                noInternetMethod();
                binding.swipeContainer.setRefreshing(false);
            }
        });

        int socketTimeout = 60000;//60 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);

        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void noInternetMethod() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.Nointernet)).
                setMessage(getResources().getString(R.string.Doyouwantrefresh)).
                setPositiveButton(getResources().getString(R.string.Yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        loadMessageList(AppConstant.mobile_no);
                    }
                }).
                setNegativeButton(getResources().getString(R.string.No), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void uploadImage(final int flag, final String ImageNames) {

        final TransparentProgressDialog pDialog = new TransparentProgressDialog(
                CattleDoctors.this, getResources().getString(R.string.Dataisloading));
        pDialog.setCancelable(false);
        pDialog.show();
        JSONObject jsonObject = null;
        String usi = AppConstant.user_id;

        Double lat = LatLonCellID.lat;
        Double lon = LatLonCellID.lon;
        Log.v("imageLat_long", lat + "," + lon);
        String URLImageUpload = AppManager.getInstance().uploadImageURL1;
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

            // jsonObject.putOpt("Lat_Lng", lat+","+lon);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        mRequestStartTime = System.currentTimeMillis();
        CustomJSONObjectRequest jsonObjectRequest = new CustomJSONObjectRequest(Request.Method.PUT, URLImageUpload, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                pDialog.cancel();
                Log.i("Response upload image", "" + response.toString());
                String Latitude = "0.0", Longitude = "0.0";
                try {

                    int seconds = getAPIimeResponseinSecond(mRequestStartTime);
                    if (seconds > 3)
                        SaveLocalFile(db, CattleDoctors.this, SN_CattleDoctors, URLImageUpload, response.toString(), "", "" + seconds, AppConstant.farm_id, "Working");
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
                        setNoofPlantDocImages(CattleDoctors.this, AppConstant.farm_id);
                        //Button Disable
                        try {
                            switch (flag) {
                                case 1:
                                    popbinding.imageupload1.setEnabled(false);
                                    popbinding.imageupload1.setBackground(getResources().getDrawable(R.drawable.btn_bg_gray));
                                    break;
                                case 2:
                                    popbinding.imageupload2.setEnabled(false);
                                    popbinding.imageupload2.setBackground(getResources().getDrawable(R.drawable.btn_bg_gray));
                                    break;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.imageuploadedwithsmsandrecord), Toast.LENGTH_LONG).show();

//                getResponse(response, co);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.cancel();
                Log.v("Response vishal coupon", "" + error.toString());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                int seconds = getAPIimeResponseinSecond(mRequestStartTime);
                SaveLocalFile(db, CattleDoctors.this, SN_CattleDoctors, URLImageUpload, "", "Internet Connection Error / Server API Error / Timeout Error", "" + seconds, AppConstant.farm_id, "Error");

            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                40000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    public void confirmationPOPup(final Dialog dialogs) {
        final Dialog dialog = new Dialog(this);
        // hide to default title for Dialog
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // inflate the layout dialog_layout.xml and set it as contentView
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_confirmation, null, false);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(view);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        Button btnUpdate = (Button) dialog.findViewById(R.id.btnUpdate);
        TextView txt_title = (TextView) dialog.findViewById(R.id.txt_title);
        TextView txtsms = (TextView) dialog.findViewById(R.id.txtsms);

        btnCancel.setText(getResources().getString(R.string.No));
        btnUpdate.setText(getResources().getString(R.string.Yes));
        txt_title.setText(getResources().getString(R.string.Confirmation));
        txtsms.setText(getResources().getString(R.string.yourrequestnologged));
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    dialog.dismiss();
                    dialogs.dismiss();
                    sendFlag = null;
                    responsePath = null;
                    imageList = new JSONArray();

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                dialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        // Display the dialog
        dialog.show();
    }

    private String createNewLogStringToLoacalStore(String picName, String message) {

        String URL = AppManager.getInstance().createNewLogURL;
        String Value = "";
        JSONObject jsonObject = null;

        try {
            jsonObject = new JSONObject();
            jsonObject.put("filename", picName);
            if (phone != null) {
                jsonObject.put("PhoneNumber", phone);
            } else {
                jsonObject.put("PhoneNumber", "8285686540");
            }
            jsonObject.put("state", selectedState);
            jsonObject.put("district", subLocality);
            jsonObject.put("locality", locality);
            jsonObject.put("country", country);
            jsonObject.put("zipcode", zipCode);
            jsonObject.put("lat", lati);
            jsonObject.put("lon", longi);
            jsonObject.put("Name", name);
            jsonObject.put("Crop", cropName);
            jsonObject.put("CropID", "12");
            jsonObject.put("VisibleName", name);
            jsonObject.put("Message", message);
            jsonObject.put("VoiceFile", responsePath);
            jsonObject.put("UserId", AppConstant.user_id);
            //Add Herojit
            jsonObject.put("FarmID", AppConstant.farm_id);

//            JSONObject ob = new JSONObject(jsonObject.toString());
            String JSON = jsonObject.toString();
            JSON = JSON.replace("\\\\\\\\\\\\\\", "\\\\\\");
            jsonObject = new JSONObject();
            jsonObject = new JSONObject(JSON);

            Log.v("request", jsonObject.toString());
            Value = jsonObject.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return Value;
    }

    public void setCustomCameraImageView() {
        try {
            if (CustomCamera_ImageValue != null) {
//                imageString = CustomCamera_ImageValue;
//                imgeview_logrequest.setImageBitmap(CustomCamera_bitmap);
                if (Imageselectflag == 1) {
                    imageString1 = CustomCamera_ImageValue;
                    popbinding.chooseImage1.setImageBitmap(CustomCamera_bitmap);
                } else if (Imageselectflag == 2) {
                    imageString2 = CustomCamera_ImageValue;
                    popbinding.chooseImage2.setImageBitmap(CustomCamera_bitmap);
                } else {
                    imageString = CustomCamera_ImageValue;
                    popbinding.imgeviewLogrequest.setImageBitmap(CustomCamera_bitmap);
                }
            }
            getCheckCameraScreenOnOff = false;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (db != null) {
            db = new DBAdapter(this);
        }
        Log.d("OnPause Method", "OnPause Method called");
    }

    @Override
    public void onResume() {
        super.onResume();
        if (db != null) {
            db = new DBAdapter(this);
        }
        if (getCheckCameraScreenOnOff == true) {
            setCustomCameraImageView();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (db != null) {
            db = new DBAdapter(this);
        }
        Log.d("onStop Method", "onStop Method called");
        setScreenTracking(this, db, SN_CattleDoctors, UID);
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
            if (requestCode == REQUEST_CAMERA_START) {
                onCaptureImageResult(data);
            } else if (requestCode == SELECT_FILE_START) {
                onSelectFromGalleryResult(data);
            }
        }
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

        popbinding.imgeviewLogrequest.setImageBitmap(thumbnail);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        imageString = Base64.encodeToString(byteArray, Base64.DEFAULT);


    }

    private void onCaptureImageResult1() {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;
        Bitmap bitmap = BitmapFactory.decodeFile(imageStoragePath1, options);
        popbinding.chooseImage1.setImageBitmap(bitmap);
        imageString1 = imageToString(bitmap);

    }

    private void onCaptureImageResult2() {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;
        Bitmap bitmap = BitmapFactory.decodeFile(imageStoragePath2, options);

        popbinding.chooseImage2.setImageBitmap(bitmap);
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

            popbinding.chooseImage1.setImageBitmap(bm);
            imageString1 = imageToString(bm);
            CustomCamera_bitmap = bm;
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
            popbinding.chooseImage2.setImageBitmap(bm);
            imageString2 = imageToString(bm);
            CustomCamera_bitmap = bm;
            String filePath = data.getData().getPath();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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

        popbinding.imgeviewLogrequest.setImageBitmap(bm);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);

        try {
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            imageString = Base64.encodeToString(byteArray, Base64.DEFAULT);

        } catch (IllegalAccessError e) {
            e.printStackTrace();
        }
    }
}
