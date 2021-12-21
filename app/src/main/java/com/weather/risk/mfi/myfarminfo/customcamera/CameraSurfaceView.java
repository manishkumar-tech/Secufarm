package com.weather.risk.mfi.myfarminfo.customcamera;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.location.LocationManager;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.database.DBAdapter;
import com.weather.risk.mfi.myfarminfo.mapfragments.LatLonCellID;
import com.weather.risk.mfi.myfarminfo.utils.AppManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPOutputStream;

import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.CustomCamera_ImageValue;
import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.CustomCamera_bitmap;
import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.getCheckCameraScreenOnOff;
import static com.weather.risk.mfi.myfarminfo.utils.AppManager.NOGPSDialog;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.checkgetLagLongnull;

public class CameraSurfaceView extends AppCompatActivity implements SurfaceHolder.Callback {

    Camera camera;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    boolean previewing = false;
    LayoutInflater controlInflater = null;

    Button btn_focus, btn_rotate, btn_capture, btn_unitchange;
    TextView prompt, txt_distancevalue_unit, text_confidencepercentage;
    LinearLayout layoutBackground;

    DrawingView drawingView;
    Camera.Face[] detectedFaces;
    final int RESULT_SAVEIMAGE = 0;
    private ScheduledExecutorService myScheduledExecutorService;


    private boolean cameraFront = false;

    DecimalFormat df = new DecimalFormat("#.#");
    String UnitChange = "meter", Distance = "0";
    float Distance_float = 0.0f;

    RelativeLayout rr_aftercamera, ll_beforecamera;
    Button btn_false, btn_true;
    ImageView imageviewcapture;

    String ActivityNameComingFrom = "", CameraScreenTypeNearFar = "";
    TableRow tblrow_distance, tblrow_btn;
    public static final String IMAGE_DIRECTORY = "/SecurFarm";
    Double farmer_lag = 0.0, farmer_long = 0.0;
    String ImageFileName = "", FarmIDFOLDER = "", CropSche_ImageSizeMoreReduce = "No";
    DBAdapter db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camerasurfaceview);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        db = new DBAdapter(this);

        if (!AppManager.getInstance().isLocationServicesAvailable(this)) {
            NOGPSDialog(this);
        }
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            NOGPSDialog(this);
        }
        Bundle bundle = getIntent().getExtras();
        CameraScreenTypeNearFar = bundle.getString("CameraScreenTypeNearFar");//Near or Far or Middle
        ActivityNameComingFrom = bundle.getString("ActivityNameComingFrom");

        try {
            if (ActivityNameComingFrom != null && (ActivityNameComingFrom.equalsIgnoreCase("CreateSchedulerActivity") ||
                    ActivityNameComingFrom.equalsIgnoreCase("OfflineMode")) ||
                    ActivityNameComingFrom.equalsIgnoreCase("CattleDoctors") ||
                    ActivityNameComingFrom.equalsIgnoreCase("PlantDoctor")) {
                farmer_lag = bundle.getDouble("FarmerLatitude");
                farmer_long = bundle.getDouble("FarmerLongitude");
                ImageFileName = bundle.getString("imageFileName");
                FarmIDFOLDER = bundle.getString("FarmID");
                if (ActivityNameComingFrom.equalsIgnoreCase("CreateSchedulerActivity"))
                    CropSche_ImageSizeMoreReduce = bundle.getString("ImageSizeMoreReduce");
            }
            if (checkgetLagLongnull(String.valueOf(farmer_lag)) || checkgetLagLongnull(String.valueOf(farmer_lag))) {
                farmer_lag = LatLonCellID.lat;
                farmer_long = LatLonCellID.lon;
                ImageFileName = bundle.getString("imageFileName");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        getWindow().setFormat(PixelFormat.UNKNOWN);
        surfaceView = (SurfaceView) findViewById(R.id.camera_preview);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);


        CustomCamera_bitmap = null;
        CustomCamera_ImageValue = null;

        drawingView = new DrawingView(this);
        ViewGroup.LayoutParams layoutParamsDrawing
                = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.FILL_PARENT);
        this.addContentView(drawingView, layoutParamsDrawing);

        controlInflater = LayoutInflater.from(getBaseContext());
        View viewControl = controlInflater.inflate(R.layout.control, null);
        ViewGroup.LayoutParams layoutParamsControl
                = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.FILL_PARENT);
        this.addContentView(viewControl, layoutParamsControl);

        //btn_focus,btn_rotate,
        btn_focus = (Button) findViewById(R.id.btn_focus);
        btn_rotate = (Button) findViewById(R.id.btn_rotate);
        btn_capture = (Button) findViewById(R.id.btn_capture);
        btn_unitchange = (Button) findViewById(R.id.btn_unitchange);
        layoutBackground = (LinearLayout) findViewById(R.id.background);
        prompt = (TextView) findViewById(R.id.txt_distancevalue);
        txt_distancevalue_unit = (TextView) findViewById(R.id.txt_distancevalue_unit);

        rr_aftercamera = (RelativeLayout) findViewById(R.id.rr_aftercamera);
        ll_beforecamera = (RelativeLayout) findViewById(R.id.ll_beforecamera);
        btn_false = (Button) findViewById(R.id.btn_false);
        btn_true = (Button) findViewById(R.id.btn_true);
        imageviewcapture = (ImageView) findViewById(R.id.imageviewcapture);
        tblrow_distance = (TableRow) findViewById(R.id.tblrow_distance);
        tblrow_btn = (TableRow) findViewById(R.id.tblrow_btn);


        btn_capture.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                try {
                    if (ActivityNameComingFrom.equalsIgnoreCase("CreateSchedulerActivity")) {
                        if (!checkgetLagLongnull(String.valueOf(farmer_lag)) && !checkgetLagLongnull(String.valueOf(farmer_lag))) {
//                        if (farmer_lag != 0.0 && farmer_long != 0.0) {
                            getCheckCameraScreenOnOff = true;
//                camera.takePicture(myShutterCallback,myPictureCallback_RAW, myPictureCallback_JPG);
                            if (getImageType() == true) {
                                camera.takePicture(null, null, myPictureCallback_JPG);
                            }
                        } else {
                            Toast.makeText(CameraSurfaceView.this, getResources().getString(R.string.PleaseEnabletheGPS0), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        getCheckCameraScreenOnOff = true;
//                camera.takePicture(myShutterCallback,myPictureCallback_RAW, myPictureCallback_JPG);
                        if (getImageType() == true) {
                            camera.takePicture(null, null, myPictureCallback_JPG);
                        }
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        layoutBackground.setOnClickListener(new LinearLayout.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                btn_capture.setEnabled(false);
                camera.autoFocus(myAutoFocusCallback);
            }
        });

        btn_focus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
//                    Camera.Parameters params = camera.getParameters();
//                    List<Camera.Size> sizes = params.getSupportedPictureSizes();
//                    int w = 0, h = 0;
//                    for (Camera.Size size : sizes) {
//                        if (size.width > w || size.height > h) {
//                            w = size.width;
//                            h = size.height;
//                        }
//                    }
//                    params.setPictureSize(w, h);
//                    if (params.getSupportedFocusModes().contains(
//                            Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
//                        params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
//                    }
//                    camera.setParameters(params);
                    setCameraresizeandFocus();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        btn_rotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Refresh
                btn_capture.setEnabled(false);
                camera.autoFocus(myAutoFocusCallback);
            }
        });

        btn_unitchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (UnitChange.equalsIgnoreCase("meter")) {
                    UnitChange = "inch";
                } else if (UnitChange.equalsIgnoreCase("inch")) {
                    UnitChange = "feet";
                } else if (UnitChange.equalsIgnoreCase("feet")) {
                    UnitChange = "meter";
                }
                setUnitChanged();

            }
        });

//        //Start Thread
//        startTimer();
        btn_false.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCheckCameraScreenOnOff = false;
                imageviewcapture.setBackground(null);
                rr_aftercamera.setVisibility(View.GONE);
                ll_beforecamera.setVisibility(View.VISIBLE);

            }
        });
        btn_true.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rr_aftercamera.setVisibility(View.VISIBLE);
                ll_beforecamera.setVisibility(View.GONE);
                if (CustomCamera_bitmap != null && !CustomCamera_bitmap.equals("")) {
                    //Store the Imag in Local Storage
                    if (ActivityNameComingFrom.equalsIgnoreCase("CreateSchedulerActivity")) {
                        saveImage(CustomCamera_bitmap, ImageFileName, FarmIDFOLDER);
                    } else {
                        saveImage(CustomCamera_bitmap, ImageFileName, "");
                    }
                    //insert image name into the Local storage
                    db.open();
                    if (checkgetLagLongnull(String.valueOf(farmer_lag)) || checkgetLagLongnull(String.valueOf(farmer_lag))) {
                        farmer_lag = LatLonCellID.lat;
                        farmer_long = LatLonCellID.lon;
                    }
                    db.insertImageLocalStorage(ImageFileName, farmer_lag, farmer_long);
                    db.close();
                }
                finish();
            }
        });

        if (CameraScreenTypeNearFar.equalsIgnoreCase("Far")) {
            tblrow_distance.setVisibility(View.GONE);
        } else {
            tblrow_distance.setVisibility(View.VISIBLE);
        }

    }


    Camera.FaceDetectionListener faceDetectionListener
            = new Camera.FaceDetectionListener() {

        @Override
        public void onFaceDetection(Camera.Face[] faces, Camera tcamera) {

            try {
                if (faces.length == 0) {
                    //prompt.setText(" No Face Detected! ");
                    drawingView.setHaveFace(false);
                } else {
                    //prompt.setText(String.valueOf(faces.length) + " Face Detected :) ");
                    drawingView.setHaveFace(true);
                    detectedFaces = faces;

                    //Set the FocusAreas using the first detected face
                    List<Camera.Area> focusList = new ArrayList<Camera.Area>();
                    Camera.Area firstFace = new Camera.Area(faces[0].rect, 1000);
                    focusList.add(firstFace);

                    if (camera.getParameters().getMaxNumFocusAreas() > 0) {
                        camera.getParameters().setFocusAreas(focusList);
                    }

                    if (camera.getParameters().getMaxNumMeteringAreas() > 0) {
                        camera.getParameters().setMeteringAreas(focusList);
                    }

                    btn_capture.setEnabled(false);
                    //camera.getParameters().setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);

                    //Stop further Face Detection
                    camera.stopFaceDetection();
                    btn_capture.setEnabled(false);
    /*
     * Allways throw java.lang.RuntimeException: autoFocus failed
     * if I call autoFocus(myAutoFocusCallback) here!
     *
     camera.autoFocus(myAutoFocusCallback);
    */
                    try {
                        //Delay call autoFocus(myAutoFocusCallback)
                        myScheduledExecutorService = Executors.newScheduledThreadPool(1);
                        myScheduledExecutorService.schedule(new Runnable() {
                            public void run() {
//                                Toast.makeText(CameraSurfaceView.this, "111 faceDetectionListener error", Toast.LENGTH_LONG).show();
                                camera.autoFocus(myAutoFocusCallback);
                            }
                        }, 200, TimeUnit.MILLISECONDS);
                    } catch (Exception ex) {
                        ex.printStackTrace();
//                        Toast.makeText(CameraSurfaceView.this, "myScheduledExecutorService error", Toast.LENGTH_LONG).show();
                    }

                }

                drawingView.invalidate();
            } catch (Exception ex) {
                ex.printStackTrace();
//                Toast.makeText(CameraSurfaceView.this, "faceDetectionListener error", Toast.LENGTH_LONG).show();
            }


        }
    };

    Camera.AutoFocusCallback myAutoFocusCallback = new Camera.AutoFocusCallback() {

        @Override
        public void onAutoFocus(boolean arg0, Camera arg1) {
            // TODO Auto-generated method stub
            try {
                if (arg0) {
                    btn_capture.setEnabled(true);
                    camera.cancelAutoFocus();
                }

                float focusDistances[] = new float[3];
                arg1.getParameters().getFocusDistances(focusDistances);
//            prompt.setText("Optimal Focus Distance(meters): "
//                    + focusDistances[Camera.Parameters.FOCUS_DISTANCE_OPTIMAL_INDEX]);
                float value = 0.0f;
                float value_Near = Float.valueOf(focusDistances[Camera.Parameters.FOCUS_DISTANCE_NEAR_INDEX]);
                float value1_Averg = Float.valueOf(focusDistances[Camera.Parameters.FOCUS_DISTANCE_OPTIMAL_INDEX]);
                float value2_Far = Float.valueOf(focusDistances[Camera.Parameters.FOCUS_DISTANCE_FAR_INDEX]);
                if (CameraScreenTypeNearFar.equalsIgnoreCase("Far")) {//More than 3 meters
                    value = value2_Far;
                } else {
                    if (String.valueOf(value1_Averg).contains("3.813")) { // means infinity concepts
                        value = value_Near; //3.81381
                    } else {
                        value = value1_Averg;
                    }
                }
                if (!String.valueOf(value).equalsIgnoreCase("Infinity")) {
                    value = Float.valueOf(df.format(value));
                    float fix = 9.1f;
                    if (value != fix) {
                        Distance_float = value;
                    } else {
                        Distance_float = 0.0f;
                    }
                } else {
                    Distance_float = 0.0f;
                }
                setUnitChanged();
                setCameraresizeandFocus();
            } catch (Exception ex) {
                ex.printStackTrace();
//                Toast.makeText(CameraSurfaceView.this, "myAutoFocusCallback error", Toast.LENGTH_LONG).show();
            }
        }
    };
    Camera.PictureCallback myPictureCallback_JPG = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] arg0, Camera arg1) {
            try {
                CustomCamera_bitmap = null;
                CustomCamera_ImageValue = null;
                CustomCamera_bitmap = BitmapFactory.decodeByteArray(arg0, 0, arg0.length);
                if (CustomCamera_bitmap != null) {
                    CustomCamera_bitmap = getResizedBitmap(CustomCamera_bitmap);
                }
//                imageviewcapture.setImageBitmap(CustomCamera_bitmap);//go to face detection
                CustomCamera_ImageValue = imageToString(CustomCamera_bitmap);
//                writeToFile(CustomCamera_ImageValue);
//                byte[] compressvalue = compress(CustomCamera_ImageValue);
//                int size = compressvalue.length;
//                String str = new String(compressvalue, StandardCharsets.UTF_8);
//                String strvale = new String(compressvalue);

//                rr_aftercamera.setVisibility(View.VISIBLE);
//                ll_beforecamera.setVisibility(View.GONE);

                camera.setPreviewDisplay(surfaceHolder);
                camera.startPreview();
                camera.startFaceDetection();
                if (CustomCamera_bitmap != null && !CustomCamera_bitmap.equals("")) {
                    //Remove sky check  condition
//                    if (CameraScreenTypeNearFar.equalsIgnoreCase("Far")) {
//                    getDetectImage(CustomCamera_bitmap);
//                    } else {
//                        getSKYImage();
//                        imageviewcapture.setImageBitmap(CustomCamera_bitmap);
//                    }

                    getSKYImage();
                    imageviewcapture.setImageBitmap(CustomCamera_bitmap);
                }


            } catch (
                    Exception ex) {
                ex.printStackTrace();
            }
        }
    };

    public String imageToString(Bitmap bmp) {
        String encodedImage = "null";
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
            byte[] imageBytes = bytes.toByteArray();
            encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        } catch (IllegalAccessError e) {
            e.printStackTrace();
            encodedImage = null;
        } catch (RuntimeException ex) {
            ex.printStackTrace();
            encodedImage = null;
        }
        return encodedImage;
    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        try {
            // TODO Auto-generated method stub
            if (previewing) {
                camera.stopFaceDetection();
                camera.stopPreview();
                previewing = false;
            }

            if (camera != null) {
                try {
                    camera.setPreviewDisplay(surfaceHolder);
                    camera.startPreview();

//                prompt.setText(String.valueOf(
//                        "Max Face: " + camera.getParameters().getMaxNumDetectedFaces()));
                    camera.startFaceDetection();
                    previewing = true;

                    //Herojit
                    camera.autoFocus(myAutoFocusCallback);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
//            Toast.makeText(CameraSurfaceView.this, "surfaceChanged error", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        try {
            camera = Camera.open();
            camera.setFaceDetectionListener(faceDetectionListener);
            camera.setDisplayOrientation(90);
            setCameraDisplayOrientation(this, Surface.ROTATION_90, camera);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        camera.stopFaceDetection();
        camera.stopPreview();
        camera.release();
        camera = null;
        previewing = false;
    }

    //Camera Orientation
    public static void setCameraDisplayOrientation(Activity activity,
                                                   int cameraId, Camera camera) {
        try {
            Camera.CameraInfo info =
                    new Camera.CameraInfo();
            Camera.getCameraInfo(cameraId, info);
            int rotation = activity.getWindowManager().getDefaultDisplay()
                    .getRotation();
            int degrees = 0;
            switch (rotation) {
                case Surface.ROTATION_0:
                    degrees = 0;
                    break;
                case Surface.ROTATION_90:
                    degrees = 90;
                    break;
                case Surface.ROTATION_180:
                    degrees = 180;
                    break;
                case Surface.ROTATION_270:
                    degrees = 270;
                    break;
            }

            int result;
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                result = (info.orientation + degrees) % 360;
                result = (360 - result) % 360;  // compensate the mirror
            } else {  // back-facing
                result = (info.orientation - degrees + 360) % 360;
            }
            camera.setDisplayOrientation(result);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public void setUnitChanged() {
        String flag = UnitChange, Unit = "m";
        try {
            switch (flag) {
                case "meter":
                    Distance = String.valueOf(Distance_float);
                    Unit = "m";
                    break;
                case "inch":
                    double inch = 39.3701;//39.3701
                    Distance = String.valueOf(Math.round(Distance_float * inch));
                    Unit = "in";
                    break;
                case "feet":
                    double feet = 3.28084;//3.28084
                    Distance = String.valueOf(Math.round(Distance_float * feet));
                    Unit = "ft";
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        txt_distancevalue_unit.setText(String.valueOf(Unit));
        prompt.setText(String.valueOf(Distance));

    }


    //Timer
    public void startTimer() {
        //set a new Timer
        timer = new Timer();
        //initialize the TimerTask's job
        initializeTimerTask();
        //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
        timer.schedule(timerTask, 2000, 3000); //
    }

    public void stopTimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                mHandler.post(new Runnable() {
                    public void run() {
//                        System.out.println("timer is running");
                        try {
                            camera.autoFocus(myAutoFocusCallback);
                        } catch (Exception e) {
                            // TODO: handle exception
                        }

                    }
                });
            }
        };
    }


    Timer timer;
    TimerTask timerTask;
    final Handler mHandler = new Handler();

    @Override
    public void onStop() {
        super.onStop();
        stopTimertask();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (farmer_lag == 0.0 || farmer_long == 0.0) {
            farmer_lag = LatLonCellID.lat;
            farmer_long = LatLonCellID.lon;
        }
        startTimer();
    }

    boolean value;

    public boolean getImageType() {
        String flag = UnitChange, SMS = "", Unit = "m";
        double checkvalue = 0.0f;

        if (CameraScreenTypeNearFar.equalsIgnoreCase("Near")) {//Less than 1.5 meters
            try {
                switch (flag) {
                    case "meter":
                        checkvalue = 2.0f;
                        if (Distance_float <= checkvalue) {
                            value = true;
                        } else {
                            value = false;
                        }
                        Unit = "m";
                        break;
                    case "inch":
                        double inch = 39.3701;
                        checkvalue = Math.round(2.0f * inch);
                        if (Math.round(Distance_float * inch) <= checkvalue) {
                            value = true;
                        } else {
                            value = false;
                        }
                        Unit = "in";
                        break;
                    case "feet":
                        double feet = 3.2808;
                        checkvalue = Math.round(2.0f * feet);
                        if (Math.round(Distance_float * feet) <= checkvalue) {
                            value = true;
                        } else {
                            value = false;
                        }
                        Unit = "ft";
                        break;
                }
                SMS = getResources().getString(R.string.Yourmobilecameraless) + " " + String.valueOf(checkvalue) + " " + Unit;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (CameraScreenTypeNearFar.equalsIgnoreCase("Middle")) {//Less than 3 meters
            try {
                switch (flag) {
                    case "meter":
                        checkvalue = 3.0f;
                        if (Distance_float <= checkvalue) {
                            value = true;
                        } else {
                            value = false;
                        }
                        Unit = "m";
                        break;
                    case "inch":
                        double inch = 39.3701;
                        checkvalue = Math.round(3.0f * inch);
                        if (Math.round(Distance_float * inch) <= checkvalue) {
                            value = true;
                        } else {
                            value = false;
                        }
                        Unit = "in";
                        break;
                    case "feet":
                        double feet = 3.2808;
                        checkvalue = Math.round(3.0f * feet);
                        if (Math.round(Distance_float * feet) <= checkvalue) {
                            value = true;
                        } else {
                            value = false;
                        }
                        Unit = "ft";
                        break;
                }
                SMS = getResources().getString(R.string.Yourmobilecameraless) + " " + String.valueOf(checkvalue) + " " + Unit;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (CameraScreenTypeNearFar.equalsIgnoreCase("Far")) {//More than 3 meters
            try {
                switch (flag) {
                    case "meter":
                        checkvalue = 3.0f;
                        if (Distance_float > checkvalue) {
                            value = true;
                        } else {
//                            value = false;
                            value = true;//Change the condition always true in far not limit has checked
                        }
                        Unit = "m";
                        break;
                    case "inch":
                        double inch = 39.3701;
                        checkvalue = Math.round(3.0f * inch);
                        if (Math.round(Distance_float * inch) > checkvalue) {
                            value = true;
                        } else {
//                            value = false;
                            value = true;//Change the condition always true in far not limit has checked
                        }
                        Unit = "in";
                        break;
                    case "feet":
                        double feet = 3.2808;
                        checkvalue = Math.round(3.0f * feet);
                        if (Math.round(Distance_float * feet) > checkvalue) {
                            value = true;
                        } else {
//                            value = false;
                            value = true;//Change the condition always true in far not limit has checked
                        }
                        Unit = "ft";
                        break;
                }
                SMS = getResources().getString(R.string.Yourmobilecameramore) + " " + String.valueOf(checkvalue) + " " + Unit;

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        if (!value) {
            Toast.makeText(CameraSurfaceView.this, SMS, Toast.LENGTH_SHORT).show();
        }

        return value;
    }


    public void getSKYImage() {
        rr_aftercamera.setVisibility(View.VISIBLE);
        ll_beforecamera.setVisibility(View.GONE);
        tblrow_btn.setVisibility(View.VISIBLE);
    }

    //Firebase Image Level Detection

    public static ArrayList<HashMap<String, String>> imageLabels = new ArrayList<>();

//    public void getDetectImage(Bitmap bitmap) {
//
//        final FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);
//        FirebaseVisionImageLabeler labeler = FirebaseVision.getInstance().getOnDeviceImageLabeler();
////        ConnectionDetector connectionDetector = new ConnectionDetector(context);
////        if (connectionDetector.isConnectingToInternet()) {
////            labeler = FirebaseVision.getInstance().getCloudImageLabeler();
////        } else {
////            labeler = FirebaseVision.getInstance().getOnDeviceImageLabeler();
////        }
//        labeler.processImage(image)
//                .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionImageLabel>>() {
//                    @Override
//                    public void onSuccess(List<FirebaseVisionImageLabel> labels) {
//                        // Task completed successfully
//                        try {
//                            imageLabels = new ArrayList<>();
//                            for (FirebaseVisionImageLabel label : labels) {
//                                String text = label.getText();
//                                String entityId = label.getEntityId();
//                                float confidence = label.getConfidence();
//                                HashMap<String, String> hash = new HashMap<>();
//                                hash.put("ImageType", text);
//                                hash.put("ImageID", entityId);
//                                hash.put("ImageConfidence", String.valueOf(confidence));
//                                if (text.equalsIgnoreCase("Sky")) {
//                                    imageLabels.add(hash);
//                                    break;
//                                }
//                            }
//                            if (imageLabels.size() > 0) {
//
//                                float condivalue = 0.0f;
//                                if (imageLabels.get(0).get("ImageConfidence") != null && imageLabels.get(0).get("ImageConfidence").length() > 0) {
//                                    condivalue = Float.valueOf(imageLabels.get(0).get("ImageConfidence"));
//                                }
//                                DecimalFormat df1 = new DecimalFormat("#.##");
//                                condivalue = Float.valueOf(df1.format(condivalue));
//                                int confidencevalue = Math.round(condivalue * 100);
//                                if (confidencevalue > 70) {
//                                    imageviewcapture.setImageBitmap(CustomCamera_bitmap);
//                                    rr_aftercamera.setVisibility(View.VISIBLE);
//                                    tblrow_btn.setVisibility(View.VISIBLE);
//                                    ll_beforecamera.setVisibility(View.GONE);
//                                } else {
//                                    imageLabels = new ArrayList<>();
//                                    CustomCamera_bitmap = null;
//                                    CustomCamera_ImageValue = null;
//                                    rr_aftercamera.setVisibility(View.GONE);
//                                    tblrow_btn.setVisibility(View.GONE);
//                                    ll_beforecamera.setVisibility(View.VISIBLE);
//                                    Toast.makeText(CameraSurfaceView.this, getResources().getString(R.string.Theproperskynotcovered), Toast.LENGTH_LONG).show();
//                                }
//                            } else {
//                                imageLabels = new ArrayList<>();
//                                CustomCamera_bitmap = null;
//                                CustomCamera_ImageValue = null;
//                                rr_aftercamera.setVisibility(View.GONE);
//                                tblrow_btn.setVisibility(View.GONE);
//                                ll_beforecamera.setVisibility(View.VISIBLE);
//                                Toast.makeText(CameraSurfaceView.this, getResources().getString(R.string.NoSky), Toast.LENGTH_LONG).show();
//
//                            }
//                            tblrow_distance.setVisibility(View.GONE);
//                        } catch (Exception ex) {
//                            ex.printStackTrace();
//                        }
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        // Task failed with an exception
//                        String check = "";
//                        check = "aa";
//                        imageLabels = new ArrayList<>();
//                    }
//                });
//
//    }


    public String saveImage(Bitmap myBitmap, String imageFileName, String FarmerIdFolder) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File localStorage;//=localStorage = new File(Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        if (FarmerIdFolder != null && FarmerIdFolder.length() > 0) {
            localStorage = new File(Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY + "/" + FarmerIdFolder);
        } else {
            localStorage = new File(Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        }

        // have the object build the directory structure, if needed.

//        String filename = Environment.getExternalStorageDirectory().getPath()+ "/SecuFarm/";
//        File localStorage = new File(filename);
//        String Unedited_Img_Name = "myfile"+ String.valueOf(System.currentTimeMillis()) + ".jpg";
//        final File f = new File(localStorage, imageFileName);


        if (!localStorage.exists()) {
            Log.d("dirrrrrr", "" + localStorage.mkdirs());
            localStorage.mkdirs();
        }
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
//        String imageFileName = "SF_" + timeStamp + ".jpg";

        try {
            File f = new File(localStorage, imageFileName);
            f.createNewFile();   //give read write permission
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    /**
     * reduces the size of the image
     *
     * @param image
     * @return
     */
    public Bitmap getResizedBitmap(Bitmap image) {
        try {
            int width = image.getWidth(), height = image.getHeight();
            if (CropSche_ImageSizeMoreReduce.equalsIgnoreCase("Yes")) {
                width = width / 3;
                height = height / 3;
            } else {
                width = (width * 2) / 3;
                height = (height * 2) / 3;
            }
            return Bitmap.createScaledBitmap(image, width, height, true);
        } catch (Exception ex) {
            ex.printStackTrace();
            return Bitmap.createScaledBitmap(image, image.getWidth(), image.getHeight(), true);
        }
    }

    public void setCameraresizeandFocus() {
        try {
            Camera.Parameters params = camera.getParameters();
            List<Camera.Size> sizes = params.getSupportedPictureSizes();
            int w = 0, h = 0;
            for (Camera.Size size : sizes) {
                if (size.width > w || size.height > h) {
                    w = size.width;
                    h = size.height;
                }
            }
            params.setPictureSize(w, h);
            if (params.getSupportedFocusModes().contains(
                    Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
                params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
            }
            camera.setParameters(params);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static byte[] compress(String data) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length());
        GZIPOutputStream gzip = new GZIPOutputStream(bos);
        gzip.write(data.getBytes());
        gzip.close();
        byte[] compressed = bos.toByteArray();
        bos.close();
        return compressed;
    }

    private void writeToFile(String data) {

        try {
//            File localStorage;//=localStorage = new File(Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
//            if (FarmerIdFolder != null && FarmerIdFolder.length() > 0) {
//                localStorage = new File(Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY + "/" + FarmerIdFolder);
//            } else {
//                localStorage = new File(Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
//            }

            File path = Environment.getExternalStorageDirectory();
            String mypath = path.toString() + IMAGE_DIRECTORY;
            OutputStreamWriter out;
            File f = new File(mypath, "/secufarmfile.txt");
            out = new OutputStreamWriter(openFileOutput(f.getPath(), MODE_PRIVATE));
            out.write("test");
            out.close();

//            OutputStreamWriter out;
//            String filename = "Secufarm.txt";
//            File path = new File(getFilesDir(), "SecurFarm_txtfile");
//            File mypath = new File(path, filename);
//            FileInputStream fis = new FileInputStream(new File(NAME_OF_FILE));  // 2nd line
//            if (!mypath.exists()) {
////                out = new OutputStreamWriter(openFileOutput(mypath.getAbsolutePath(), MODE_PRIVATE));
//                out = new OutputStreamWriter(openFileOutput(Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY, MODE_PRIVATE));
//                out.write(data);
//                out.close();
//            }
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        } catch (Exception e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

}
