package com.weather.risk.mfi.myfarminfo.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.weather.risk.mfi.myfarminfo.BuildConfig;
import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.activities.CustomSearchableSpinner;
import com.weather.risk.mfi.myfarminfo.database.DBAdapter;
import com.weather.risk.mfi.myfarminfo.home.AppController;
import com.weather.risk.mfi.myfarminfo.mapfragments.LatLonCellID;
import com.weather.risk.mfi.myfarminfo.menu.IconizedMenu;
import com.weather.risk.mfi.myfarminfo.policyregistration.UserFarmResponse;
import com.weather.risk.mfi.myfarminfo.volley_class.CustomJSONObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import static android.content.Context.MODE_PRIVATE;
import static com.weather.risk.mfi.myfarminfo.activities.LoginWithOtp_New.strPhoneno;
import static com.weather.risk.mfi.myfarminfo.customcamera.CameraSurfaceView.IMAGE_DIRECTORY;
import static com.weather.risk.mfi.myfarminfo.database.DBAdapter.DATABASE_CREATED_SCREENTRACKING;
import static com.weather.risk.mfi.myfarminfo.database.DBAdapter.DeviceIMEI;
import static com.weather.risk.mfi.myfarminfo.database.DBAdapter.OutTime;
import static com.weather.risk.mfi.myfarminfo.database.DBAdapter.TABLE_SCREENTRACKING;
import static com.weather.risk.mfi.myfarminfo.database.DBAdapter.TimeDuration;
import static com.weather.risk.mfi.myfarminfo.database.DBAdapter.UserIDs;
import static com.weather.risk.mfi.myfarminfo.logging.LocalFileWriter.SaveLocalFile;
import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.AndroidDevice_IMEI;
import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.FOLDER_NAME;
import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.SN_NewHomeScreen;
import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.SelectedLanguageValue;
import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.userfarmListforpolicyfarmlist;


/**
 * Created by Vishal on 18/03/17.
 */
public class Utility {
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    public static final int MY_PERMISSIONS_REQUEST_Camera = 122;
    public static final int MY_PERMISSIONS_REQUEST_PHONE_STATE = 121;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 120;
    public static final int MY_PERMISSIONS_REQUEST_SMS = 124;
    public static final int MY_PERMISSIONS_REQUEST_CALL = 129;
    //    public static String MyPlantDocImageCount = "appPlantDocImageCount.xml";
    public static String MyPlantDocImageFileList = "appPlantDocImageFileList.xml";

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkPermissionGallery(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();

                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkPermissionReadPhoneState(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_PHONE_STATE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("Phone permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_PHONE_STATE}, MY_PERMISSIONS_REQUEST_PHONE_STATE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();

                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_PHONE_STATE}, MY_PERMISSIONS_REQUEST_PHONE_STATE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }


    public static boolean checkPermissionCamera(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.CAMERA)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("Camera permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_Camera);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();

                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_Camera);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    public static boolean checkPermissionLocation(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("Location permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();

                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    public static boolean checkPermissionSMS(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.SEND_SMS)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("SMS permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SMS);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();

                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SMS);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    public static boolean checkPermissionCallRecord(final Context context) {

//        Manifest.permission.PROCESS_OUTGOING_CALLS,  Manifest.permission.MODIFY_AUDIO_SETTINGS
        int result = ContextCompat.checkSelfPermission(context.getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(context.getApplicationContext(), Manifest.permission.RECORD_AUDIO);
//        int result2 = ContextCompat.checkSelfPermission(context.getApplicationContext(), Manifest.permission.PROCESS_OUTGOING_CALLS);
//        int result3 = ContextCompat.checkSelfPermission(context.getApplicationContext(), Manifest.permission.MODIFY_AUDIO_SETTINGS);
        int result4 = ContextCompat.checkSelfPermission(context.getApplicationContext(), Manifest.permission.READ_PHONE_STATE);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED &&
//                result2 == PackageManager.PERMISSION_GRANTED && result3 == PackageManager.PERMISSION_GRANTED &&
//                result3 == PackageManager.PERMISSION_GRANTED &&
                result4 == PackageManager.PERMISSION_GRANTED;
    }

    static int MULTIPLE_PERMISSIONS = 7;
    static String[] permissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.PROCESS_OUTGOING_CALLS,
            Manifest.permission.MODIFY_AUDIO_SETTINGS
    };

    public static void setPermissionsRecording(Activity context) {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(context.getApplicationContext(), p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(context, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MULTIPLE_PERMISSIONS);
        }
    }

    public static boolean checkPermissionCall(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.CALL_PHONE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("Call permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();

                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }


    //Date Picker
    public static void setDatePicker(Context context, final TextView txt, final String flag) {

        final Dialog datepic = new Dialog(context);
        Window window = datepic.getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);
        datepic.setContentView(R.layout.datepicker);
        datepic.setCanceledOnTouchOutside(true);
        Button btnset = (Button) datepic.findViewById(R.id.btn_Set);
        Button btnclear = (Button) datepic.findViewById(R.id.btn_Clear);
        Button btncancel = (Button) datepic.findViewById(R.id.btn_cancel);

        datepic.show();
        // datepic.getWindow().setLayout(240, 190);
        datepic.getWindow().setLayout(ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT);
        final DatePicker datetext = (DatePicker) datepic
                .findViewById(R.id.datepicker);
        btncancel.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                datepic.dismiss();
            }
        });

        btnclear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                txt.setText("");
                datepic.dismiss();

            }
        });
        btnset.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                int dt = datetext.getDayOfMonth();
                int month = datetext.getMonth();
                int year = datetext.getYear();
                int mnth = month + 1;
                String sDate;
                if (dt < 10) {
                    sDate = "0" + String.valueOf(dt);
                } else {
                    sDate = String.valueOf(dt);
                }
                String sMonth;
                if (mnth < 10) {
                    sMonth = "0" + String.valueOf(mnth);
                } else {
                    sMonth = String.valueOf(mnth);
                }
                String sSellectedDate = "";
                if (flag.equalsIgnoreCase("DDMMYYYY")) {
                    sSellectedDate = sDate + "-" + sMonth + "-"
                            + String.valueOf(year);
                } else if (flag.equalsIgnoreCase("YYYYMMDD")) {
                    sSellectedDate = String.valueOf(year) + "-" + sMonth + "-"
                            + sDate;
                }
                txt.setText(sSellectedDate);
                datepic.dismiss();
            }
        });

        datepic.show();

    }

    //Datepicker with max and min date
    public static void setDatePickerMaxMin(Context context, final TextView txt, final String flag, String SowingDate) {
        try {
            final Dialog datepic = new Dialog(context);
            Window window = datepic.getWindow();
            window.requestFeature(Window.FEATURE_NO_TITLE);
            datepic.setContentView(R.layout.datepicker);
            datepic.setCanceledOnTouchOutside(true);
            Button btnset = (Button) datepic.findViewById(R.id.btn_Set);
            Button btnclear = (Button) datepic.findViewById(R.id.btn_Clear);
            Button btncancel = (Button) datepic.findViewById(R.id.btn_cancel);


            // datepic.getWindow().setLayout(240, 190);
            datepic.getWindow().setLayout(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
            final DatePicker datetext = (DatePicker) datepic.findViewById(R.id.datepicker);

            int YEAR = 2020, MONTH = 2, DATE = 15;
            if (SowingDate != null) {
                String[] swdate = SowingDate.split("-");//10-06-2020
                DATE = Integer.parseInt(swdate[0]);
                MONTH = Integer.parseInt(swdate[1]) - 1;
                YEAR = Integer.parseInt(swdate[2]);
            }
            final Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, YEAR);
            calendar.set(Calendar.MONTH, MONTH);
            calendar.set(Calendar.DATE, DATE);
            long now = calendar.getTimeInMillis();
            long MaxMin = 1000 * 60 * 60 * 24 * 15;//15 days
            datetext.setMinDate(now - MaxMin);//15 days before
            datetext.setMaxDate(now + MaxMin);//15 days forwards


            btncancel.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    datepic.dismiss();
                }
            });

            btnclear.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    txt.setText("");
                    datepic.dismiss();

                }
            });
            btnset.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    int dt = datetext.getDayOfMonth();
                    int month = datetext.getMonth();
                    int year = datetext.getYear();
                    int mnth = month + 1;
                    String sDate;
                    if (dt < 10) {
                        sDate = "0" + String.valueOf(dt);
                    } else {
                        sDate = String.valueOf(dt);
                    }
                    String sMonth;
                    if (mnth < 10) {
                        sMonth = "0" + String.valueOf(mnth);
                    } else {
                        sMonth = String.valueOf(mnth);
                    }
                    String sSellectedDate = "";
                    if (flag.equalsIgnoreCase("DDMMYYYY")) {
                        sSellectedDate = sDate + "-" + sMonth + "-"
                                + String.valueOf(year);
                    } else if (flag.equalsIgnoreCase("YYYYMMDD")) {
                        sSellectedDate = String.valueOf(year) + "-" + sMonth + "-"
                                + sDate;
                    }
                    txt.setText(sSellectedDate);
                    datepic.dismiss();
                }
            });

            datepic.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    } //Datepicker with max and min date

    //String Check

    public static boolean setStringCheck(final String value) {
        if (value != null && !value.equalsIgnoreCase("null") && !value.trim().equalsIgnoreCase(""))
            return true;
        return false;
    }

    public static String ConvertdatedifferenceDays(String StartDate, String NextDate) {
        String value = "";
        if (NextDate != null && NextDate.length() > 0 && StartDate != null && StartDate.length() > 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH);
            Date Date1 = null, Date2 = null;
            try {
                Date1 = sdf.parse(NextDate);
                Date2 = sdf.parse(StartDate);
                long diff = (Date2.getTime() - Date1.getTime());
                long diffSeconds = diff / 1000 % 60;
                long diffMinutes = diff / (60 * 1000) % 60;
                long diffHours = diff / (60 * 60 * 1000) % 24;
                long diffDays = diff / (24 * 60 * 60 * 1000);

                if (diffDays > 0) {
                    value = diffDays + " days ";
                }
                if (diffHours > 0) {
                    value = value + diffHours + " hrs ";
                }
                if (diffMinutes > 0) {
                    value = value + diffMinutes + " mins ";
                }
                if (diffSeconds > 0) {
                    value = value + diffSeconds + " sec ";
                }

                value = value + "ago";

//                System.out.print(diffDays + " days, ");
//                System.out.print(diffHours + " hours, ");
//                System.out.print(diffMinutes + " minutes, ");
//                System.out.print(diffSeconds + " seconds.");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return value;
        } else {
            return value;
        }
    }

    public static boolean checkNoofDaysislessthan7or3(String StartDate, String NextDate, String notftytype) {
        boolean value = true;
        if (NextDate != null && NextDate.length() > 0 && StartDate != null && StartDate.length() > 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH);
            Date Date1 = null, Date2 = null;
            try {
                Date1 = sdf.parse(NextDate);
                Date2 = sdf.parse(StartDate);
                long diff = (Date2.getTime() - Date1.getTime());
                long diffSeconds = diff / 1000 % 60;
                long diffMinutes = diff / (60 * 1000) % 60;
                long diffHours = diff / (60 * 60 * 1000) % 24;
                long diffDays = diff / (24 * 60 * 60 * 1000);

                int flag = 1;
                if (notftytype != null && notftytype.length() > 2 &&
                        (notftytype.equalsIgnoreCase("Normal")
                                || notftytype.equalsIgnoreCase("normal"))
                        || notftytype.equalsIgnoreCase("NORMAL")) {
                    flag = 1;
                } else {
                    flag = 2;
                }
                if (flag == 2) {//CHeckfor7 days for intractive
                    if (diffDays > 7) {
                        value = false;
                    }
                } else if (flag == 1) {//CHeckfor3 days for normal
                    if (diffDays > 3) {
                        value = false;
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return value;
    }

    public static String getdate() {
        SimpleDateFormat sdf = new SimpleDateFormat(("dd-MM-yyyy"), Locale.ENGLISH);
        String currentDateandTime = sdf.format(new Date());

        return currentDateandTime;
//        return "09-05-2019";
    }

    public static String getdateYYYYMMDD() {
        SimpleDateFormat sdf = new SimpleDateFormat(("yyyy-MM-dd"), Locale.ENGLISH);
        String currentDateandTime = sdf.format(new Date());

        return currentDateandTime;
//        return "09-05-2019";
    }

    public static String gettime() {
        SimpleDateFormat sdf = new SimpleDateFormat(("HH:mm:ss"), Locale.ENGLISH);
        String currentDateandTime = sdf.format(new Date());

        return currentDateandTime;
    }

    public static Date reversetime(String TIME) {
        SimpleDateFormat sdf = new SimpleDateFormat(("HH:mm:ss"), Locale.ENGLISH);
        Date date = null;
        try {
            if (TIME != null) {
                date = sdf.parse(TIME);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return date;
    }


    public static JSONArray getImagelist(JSONObject obj, String keyname) {

        JSONArray jbb = obj.optJSONArray(keyname);
        JSONArray imageList = new JSONArray();
        try {
            if (jbb.toString().contains("[[") || jbb.toString().contains("]]")) {
                try {
                    String replace = jbb.toString().replace("[[", "[");
                    replace = replace.replace("]]", "]");
                    jbb = new JSONArray(replace);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            JSONObject jsonObject = new JSONObject();
            if (jbb != null) {
                try {
                    for (int i = 0; i < jbb.length(); i++) {
                        String strImage = jbb.get(i).toString();
                        try {
                            JSONObject obj1 = new JSONObject(strImage);
                            try {
                                //Both 3 are exists
                                jsonObject = new JSONObject();
                                jsonObject.put("QuestionNo", obj1.getString("QuestionNo"));
                                jsonObject.put("Type", obj1.getString("Type"));
                                jsonObject.put(keyname, obj1.getString(keyname));

                            } catch (Exception e1) {
                                //Only 2 are exists
                                try {
                                    jsonObject = new JSONObject();
                                    jsonObject.put("QuestionNo", "");
                                    jsonObject.put("Type", obj1.getString("Type"));
                                    jsonObject.put(keyname, obj1.getString(keyname));

                                } catch (Exception e2) {
                                    //Only 1 is exists
                                    try {
                                        jsonObject = new JSONObject();
                                        jsonObject.put("QuestionNo", "");
                                        jsonObject.put("Type", "");
                                        jsonObject.put(keyname, obj1.getString(keyname));
                                    } catch (Exception e3) {
                                        jsonObject = new JSONObject();
                                        jsonObject.put("QuestionNo", "");
                                        jsonObject.put("Type", "");
                                        jsonObject.put(keyname, strImage);
                                        e3.printStackTrace();
                                    }
                                    e2.printStackTrace();
                                }
                                e1.printStackTrace();
                            }
                        } catch (Exception ex) {
                            jsonObject = new JSONObject();
                            jsonObject.put("QuestionNo", "");
                            jsonObject.put("Type", "");
                            jsonObject.put(keyname, strImage);
                            ex.printStackTrace();
                        }
                        imageList.put(jsonObject);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return imageList;
    }

    public static String getImageName(String value) {
        String retVal = "";
        if (value != null && value.length() > 0 && !value.equalsIgnoreCase("null")) {
            try {
                JSONObject obj = new JSONObject(value);
//                String key = obj.keys().next();
                retVal = obj.getString("ImageName");
            } catch (Exception ex) {
                retVal = value;// for without key only value
                try {
                    JSONObject obj1 = new JSONObject(value);
                    String key = obj1.keys().next();
                    String Imagename = obj1.getString(key);
                    if (Imagename != null && Imagename.length() > 0) {
                        retVal = Imagename;
                    }
                } catch (Exception exs) {
                    exs.printStackTrace();
                }
                ex.printStackTrace();
            }
        }
        return retVal;
    }

    public static JSONArray addImageNameCropScheduler(
            String Response, JSONArray imageList, int flag,
            String QuestionNo, String OtherSelectImageType,
            String lat, String lon, String Distance, String DateTime) {
        JSONObject obj = new JSONObject();
        try {
            switch (flag) {
                case 1:
                    obj.put("Type", "CloseUp");
                    break;
                case 2:
                    obj.put("Type", "Farm");
                    break;
                case 3:
                    obj.put("Type", "PestTrap");
                    break;
                //Crop Scheduler Other Image type
                case 4:
                    obj.put("Type", OtherSelectImageType);
                    break;
            }
            obj.put("ImageName", Response);
            obj.put("QuestionNo", QuestionNo);
            obj.putOpt("Latitude", lat);
            obj.putOpt("Longitude", lon);
            obj.putOpt("Distance", Distance);
            obj.putOpt("DateTime", DateTime);

            imageList.put(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageList;

    }

    public static JSONArray addImageName(String Response, JSONArray imageList, int flag, String Latitude, String Longitude) {

        JSONObject obj = new JSONObject();
        try {
//            Double lat = LatLonCellID.lat;
//            Double lon = LatLonCellID.lon;
            switch (flag) {
                case 1:
                    obj.put("Type", "CloseUp");
                    obj.put("ImageName", Response);
                    break;
                case 2:
                    obj.put("Type", "Farm");
                    obj.put("ImageName", Response);
                    break;
                case 3:
                    obj.put("Type", "PestTrap");
                    obj.put("ImageName", Response);
                    break;

                //Farm Registration
                case 4://ReceiptImage
                    obj.put("Type", "ReceiptImage");
                    obj.put("ImageName", Response);
                    break;
                case 5://FarmerImage
                    obj.put("Type", "FarmerImage");
                    obj.put("ImageName", Response);
                    break;
                case 6://AadharImage
                    obj.put("Type", "AadharImage");
                    obj.put("ImageName", Response);
                    break;
                case 7://BankPaasbookImage
                    obj.put("Type", "BankPaasbookImage");
                    obj.put("ImageName", Response);
                    break;

                case 8://BankPaasbookImage
                    obj.put("Type", "FarmerImage");
                    obj.put("ImageName", Response);
                    break;

                case 9://BankPaasbookImage
                    obj.put("Type", "CattleImage");
                    obj.put("ImageName", Response);
                    break;


            }
            //Add New
//            obj.putOpt("Lat_Lng", lat + "," + lon);
            obj.putOpt("Latitude", Latitude);
            obj.putOpt("Longitude", Longitude);
            imageList.put(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageList;

    }

    public static JSONArray addImageName(String Response, JSONArray imageList, int flag) {

        JSONObject obj = new JSONObject();
        try {
            Double lat = LatLonCellID.lat;
            Double lon = LatLonCellID.lon;
            switch (flag) {
                case 1:
                    obj.put("Type", "CloseUp");
                    obj.put("ImageName", Response);
                    break;
                case 2:
                    obj.put("Type", "Farm");
                    obj.put("ImageName", Response);
                    break;
                case 3:
                    obj.put("Type", "PestTrap");
                    obj.put("ImageName", Response);
                    break;

                //Farm Registration
                case 4://ReceiptImage
                    obj.put("Type", "ReceiptImage");
                    obj.put("ImageName", Response);
                    break;
                case 5://FarmerImage
                    obj.put("Type", "FarmerImage");
                    obj.put("ImageName", Response);
                    break;
                case 6://AadharImage
                    obj.put("Type", "AadharImage");
                    obj.put("ImageName", Response);
                    break;
                case 7://BankPaasbookImage
                    obj.put("Type", "BankPaasbookImage");
                    obj.put("ImageName", Response);
                    break;
            }
            //Add New
//            obj.putOpt("Lat_Lng", lat + "," + lon);
            imageList.put(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageList;

    }


    public static void setLogout(Context context, SharedPreferences prefs) {
        SharedPreferences.Editor editor = prefs.edit();
        AppConstant.isLogin = false;
        editor.putString(AppConstant.PREFRENCE_KEY_USER_ID, "");
        editor.putString(AppConstant.PREFRENCE_KEY_VISIBLE_NAME, "");
        editor.putBoolean(AppConstant.PREFRENCE_KEY_ISLOGIN, false);
        editor.putString(AppConstant.PREFRENCE_KEY_IS_SAVE_ALL_RESPONSE, "null");

    SharedPreferences    prefs_once = context.getSharedPreferences(AppConstant.SHARED_PREFRENCE_ONCE, MODE_PRIVATE);

    prefs_once.edit().putString(context.getResources().getString(R.string.dashboard_pref_key),"").apply();

        editor.clear();
        editor.apply();
        strPhoneno = null;
    }

    public static boolean checkLocationOnOff(Context context) {
        int locationMode = 0;
        String locationProviders;
        boolean isAvailable = false;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }

            isAvailable = (locationMode != Settings.Secure.LOCATION_MODE_OFF);
        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            isAvailable = !TextUtils.isEmpty(locationProviders);
        }

        boolean coarsePermissionCheck = (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED);
        boolean finePermissionCheck = (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);

        return isAvailable && (coarsePermissionCheck || finePermissionCheck);
    }


    public static void setEnableLocation(final Context context) {

        LocationRequest mLocationRequest = LocationRequest.create().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000).setFastestInterval(1 * 1000);
        LocationSettingsRequest.Builder settingsBuilder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        settingsBuilder.setAlwaysShow(true);
        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(context)
                .checkLocationSettings(settingsBuilder.build());
        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                int LOCATION_SETTINGS_REQUEST = 199;
                try {
                    LocationSettingsResponse response =
                            task.getResult(ApiException.class);
                } catch (ApiException ex) {
                    switch (ex.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                ResolvableApiException resolvableApiException =
                                        (ResolvableApiException) ex;
                                resolvableApiException
                                        .startResolutionForResult((Activity) context,
                                                LOCATION_SETTINGS_REQUEST);
                            } catch (IntentSender.SendIntentException e) {

                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:

                            break;
                    }
                }
            }
        });
    }

//    public static void sendUserIdtoFirebaseAccount(FirebaseAnalytics firebaseAnalytics) {
//
//        try {
//            if (firebaseAnalytics != null) {
//                Bundle bundle = new Bundle();
//                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "123");
//                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Herojit");
//                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "data");
////                firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
//
//                //Logs an app event.
//                firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
////Sets whether analytics collection is enabled for this app on this device.
//                firebaseAnalytics.setAnalyticsCollectionEnabled(true);
//////Sets the minimum engagement time required before starting a session. The default value is 10000 (10 seconds). Let's make it 20 seconds just for the fun
////                firebaseAnalytics.setMinimumSessionDuration(20000);
//////Sets the duration of inactivity that terminates the current session. The default value is 1800000 (30 minutes).
////                firebaseAnalytics.setSessionTimeoutDuration(500);
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }

    public static void setScreenTracking(Context context, DBAdapter db, String ScnName, String UID) {
        try {
            db.open();
            io.requery.android.database.sqlite.SQLiteDatabase SqliteDB = db.getSQLiteDatabase();
            SqliteDB.beginTransaction();
            if (AndroidDevice_IMEI == null)
                AndroidDevice_IMEI = getDeviceIMEI(context);
            try {
                UID = UID + "_" + getAppVersion(context);
                String sql = "select count(*) from " + db.TABLE_SCREENTRACKING + " where UID='" + UID + "'";
                int count = db.getMaxRecord(sql);

                String ScreenNames = ScnName, Dates = "", InTimes = "", OutTimes = "", TimeDurations = "", AppName = "SecuFarm Farmer", IsUploadeds = "0";
                String query = "";
                if (count == 0) {
                    Dates = getdateYYYYMMDD();
//                    Dates = getdate();
                    InTimes = gettime();
                    query = "INSERT INTO " + db.TABLE_SCREENTRACKING + " VALUES ('" + UID + "','" + ScreenNames + "','" + Dates + "','" + InTimes + "','" + OutTimes + "','" + TimeDurations + "','" + AppName + "','" + AndroidDevice_IMEI + "','" + AppConstant.user_id + "','" + AppConstant.farm_id + "')";
                } else if (count > 0) {
                    String sqlintime = "select * from " + db.TABLE_SCREENTRACKING + " where UID='" + UID + "'";
                    ArrayList<HashMap<String, String>> hasmap = new ArrayList<>();
                    hasmap = db.getDynamicTableValue(sqlintime);

                    if (hasmap != null && hasmap.size() > 0) {
                        Dates = hasmap.get(0).get("Date");
                        InTimes = hasmap.get(0).get("InTime");
                    }
                    OutTimes = gettime();
                    TimeDurations = getdatedifferenceinSeconds(Dates + " " + InTimes, Dates + " " + OutTimes);
                    query = "update " + db.TABLE_SCREENTRACKING + " set " + OutTime + " = '" + OutTimes + "', " + TimeDuration + " = '" + TimeDurations + "'," + UserIDs + " = '" + AppConstant.user_id + "'," + DeviceIMEI + " = '" + AndroidDevice_IMEI + "' where UID='" + UID + "'";
                }
                db.getSQLiteDatabase().execSQL(query);

            } catch (Exception e) {
                e.printStackTrace();
                //Means Farms IDs does not exit
                try {
                    db.getSQLiteDatabase().execSQL("DROP TABLE IF EXISTS " + TABLE_SCREENTRACKING);
                    db.getSQLiteDatabase().execSQL(DATABASE_CREATED_SCREENTRACKING);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
            SqliteDB.setTransactionSuccessful();
            SqliteDB.endTransaction();
            db.getClass();


        } catch (Exception ex) {
            ex.printStackTrace();
        }
        db.close();
    }

    public static String getdatedifferenceinSeconds(String StartDate, String NextDate) {
        String value = "";
        if (NextDate != null && NextDate.length() > 0 && StartDate != null && StartDate.length() > 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH);
            Date Date1 = null, Date2 = null;
            try {
                Date1 = sdf.parse(NextDate);
                Date2 = sdf.parse(StartDate);
                long time = Math.abs(Date1.getTime() - Date2.getTime()) / 1000;
                value = String.valueOf(time);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return value;
        } else {
            return value;
        }
    }

    public static String getUIDforScreenTracking() {
        String datetime = "";
        try {
            datetime = getdate() + "_" + gettime();
            datetime = datetime.replace("-", "");
            datetime = datetime.replace(":", "");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return datetime;
    }

    public static void checkScreenTracking(Context context, DBAdapter db) {
        if (db != null) {
            db.open();
            try {
                String sql = "Select count(*) from " + TABLE_SCREENTRACKING + "  where TimeDuration !=''";
                int count = db.getMaxRecord(sql);
                if (count == -1) {
                    //In case the Apps is already installed but not exist the table
                    db.getSQLiteDatabase().execSQL(DATABASE_CREATED_SCREENTRACKING);
                } else if (count > 0) {
                    //Upload the pending ScreenTracking
                    ArrayList<HashMap<String, String>> hasmap = new ArrayList<>();
                    String SQL = "select * from " + db.TABLE_SCREENTRACKING + "  where TimeDuration !=''";
                    hasmap = db.getDynamicTableValue(SQL);

                    List<JSONObject> jsonObj = new ArrayList<JSONObject>();
                    for (HashMap<String, String> data : hasmap) {
                        JSONObject obj = new JSONObject(data);
                        jsonObj.add(obj);
                    }

                    JSONArray test = new JSONArray(jsonObj);
                    //COnvert JSONArray into JSONObject
//                    String json = new Gson().toJson(test);
                    JSONObject object = new JSONObject();
                    object.put("App_Screentracker", test.toString());
                    new getFarmerDeatials(object, db, context).execute();
//                    SynchScreenTracking(object, db,context);
                }
            } catch (Exception ex) {
                db.getSQLiteDatabase().execSQL("DROP TABLE IF EXISTS " + db.TABLE_SCREENTRACKING);
                db.getSQLiteDatabase().execSQL(DATABASE_CREATED_SCREENTRACKING);
                ex.printStackTrace();
            }
            db.close();
        }
    }

    public static class getFarmerDeatials extends AsyncTask<Void, Void, String> {
        TransparentProgressDialog progressDialog;
        JSONObject jsonObjects;
        DBAdapter db;
        Context context;
        long mRequestStartTime;
        String ScreenTracking_URL;

        public getFarmerDeatials(JSONObject jsonObject, final DBAdapter dbs, Context contexts) {
            jsonObjects = jsonObject;
            db = dbs;
            context = contexts;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new TransparentProgressDialog(
                    context, context.getResources().getString(R.string.ScreenTrackingDatais));
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @Override
        protected String doInBackground(Void... params) {
            String response = null;
            try {
                mRequestStartTime = System.currentTimeMillis(); // set the request start time just before you send the request.
                ScreenTracking_URL = AppManager.getInstance().UploadAppsScreenTracking_save_AppScreens;
                String PassJSON = jsonObjects.toString();
                response = AppManager.getInstance().httpRequestPutMethodReport(ScreenTracking_URL, PassJSON);
                System.out.println("AllResponse :---" + response);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPreExecute();
            JSONObject obj = null;
            int seconds = 0;
            try {
                // calculate the duration in milliseconds
                seconds = getAPIimeResponseinSecond(mRequestStartTime);
                if (seconds > 3) {
                    SaveLocalFile(db, context, SN_NewHomeScreen, ScreenTracking_URL, response, "", "" + seconds, "", "Working");
                }
                progressDialog.dismiss();
                if (response != null && !response.equalsIgnoreCase("Could not connect to server"))
                    obj = new JSONObject(response);
                try {
                    if (response != null && response.length() > 0) {
                        String ResponseValue = obj.getString("save_AppScreensResult");
                        if (ResponseValue.equalsIgnoreCase("Success")) {
                            if (db != null) {
                                db.open();
                                String deletesql = "delete from " + db.TABLE_SCREENTRACKING;
                                db.getSQLiteDatabase().execSQL(deletesql);
                                db.close();
                            }
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    SaveLocalFile(db, context, SN_NewHomeScreen, ScreenTracking_URL, response, "JSON Response Error", "" + seconds, "", "Error");
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(context, context.getResources().getString(R.string.ResponseFormattingError), Toast.LENGTH_LONG).show();
                SaveLocalFile(db, context, SN_NewHomeScreen, ScreenTracking_URL, response, "JSON Response Error", "" + seconds, "", "Error");
            }
        }
    }

    private static void SynchScreenTracking(JSONObject jsonObject, final DBAdapter db) {

        String url = AppManager.getInstance().UploadAppsScreenTracking;
        CustomJSONObjectRequest jsonObjectRequest = new CustomJSONObjectRequest(Request.Method.PUT, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("Response upload image", "" + response.toString());
                //Herojit Comment
                try {
                    if (response != null && response.length() > 0) {
                        String ResponseValue = response.getString("save_AppScreensResult");
                        if (ResponseValue.equalsIgnoreCase("Success")) {
                            if (db != null) {
                                db.open();
                                String deletesql = "delete from " + db.TABLE_SCREENTRACKING;
                                db.getSQLiteDatabase().execSQL(deletesql);
                                db.close();
                            }
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("Response vishal coupon", "" + error.toString());
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                40000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);


    }

    public static String getDeviceIMEI(Context context) {
        String myuniqueID = "";
        try {
            int myversion = Integer.valueOf(android.os.Build.VERSION.SDK);
            if (myversion < 23) {
                WifiManager manager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                WifiInfo info = manager.getConnectionInfo();
                myuniqueID = info.getMacAddress();
                if (myuniqueID == null) {
                    TelephonyManager mngr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

                    if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                        return null;
                    }
                    myuniqueID = mngr.getDeviceId();
                }
            } else if (myversion > 23 && myversion < 29) {
                TelephonyManager mngr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    return null;
                }
                myuniqueID = mngr.getDeviceId();
            } else {
                String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
                myuniqueID = androidId;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
//        String deviceUniqueIdentifier = null;
//        try {
//            String deviceId = android.provider.Settings.Secure.getString(
//                    context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
//            deviceUniqueIdentifier = deviceId;
//            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//            if (null != tm) {
//                deviceUniqueIdentifier = tm.getDeviceId();
//            }
////            if (null == deviceUniqueIdentifier || 0 == deviceUniqueIdentifier.length()) {
////                deviceUniqueIdentifier = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
////            }
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
        return myuniqueID;
//        return deviceUniqueIdentifier;
    }

    public static boolean checkmobileno(String mobileno) {

        if (mobileno != null && !mobileno.equalsIgnoreCase("null") && mobileno.length() > 0) {
            String s = mobileno; // get your editext value here
            Pattern pattern = Pattern.compile("[4-9]{1}[0-9]{9}");
            Matcher matcher = pattern.matcher(s);
            // Check if pattern matches
            if (matcher.matches()) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    // Function to validate Aadhar number.
    public static boolean isValidAadharNumber(String str) {
        if (str != null && !str.equalsIgnoreCase("null") && str.length() > 0) {
            String s = str; // get your editext value here
            Pattern pattern = Pattern.compile("[2-9]{1}[0-9]{3}[0-9]{4}[0-9]{4}");
            Matcher matcher = pattern.matcher(s);
            // Check if pattern matches
            if (matcher.matches()) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public static String getDaysinDynamicLangauge(Context context, String days) {
        String val = "";
        if (days != null && days.length() > 0) {
            switch (days) {
                case "Monday":
                    val = context.getResources().getString(R.string.Monday);
                    break;
                case "Tuesday":
                    val = context.getResources().getString(R.string.Tuesday);
                    break;
                case "Wednesday":
                    val = context.getResources().getString(R.string.Wednesday);
                    break;
                case "Thursday":
                    val = context.getResources().getString(R.string.Thursday);
                    break;
                case "Friday":
                    val = context.getResources().getString(R.string.Friday);
                    break;
                case "Saturday":
                    val = context.getResources().getString(R.string.Saturday);
                    break;
                case "Sunday":
                    val = context.getResources().getString(R.string.Sunday);
                    break;
            }
        }


        return val;
    }

    public static void setToastSMSShow(int flag, final Context context, String SMS) {
        Toast ToastMessage = Toast.makeText(context, SMS, Toast.LENGTH_LONG);
        //Set listed gravity here.
        if (flag == 1) {
            ToastMessage.setGravity(Gravity.TOP, 0, 0);
        }
        if (flag == 2) {
            ToastMessage.setGravity(Gravity.CENTER, 0, 0);
        }
        ToastMessage.show();
    }

    public static String getDateFormatChanged(String dateTime, String Flag) {
//        DateFormat formatter = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        String Datevalue = "";
        SimpleDateFormat ddmmyyyy = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH);
        SimpleDateFormat yyyymmdd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        String dateSample = "10-01-2010 21:10:05";

        Date date1;
        try {

            switch (Flag) {
                case "DDMMYYYY":
                    Datevalue = ddmmyyyy.format(yyyymmdd.parse(dateTime));
                    date1 = yyyymmdd.parse(dateTime);
                    String dt = yyyymmdd.format(date1);
                    break;
                case "YYYYMMDD":
                    Datevalue = yyyymmdd.format(ddmmyyyy.parse(dateTime));
                    date1 = ddmmyyyy.parse(dateTime);
                    String dt1 = ddmmyyyy.format(date1);
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Datevalue;
    }

    public static String getDate(String value) {//11 Jun 2019 into dd-mm-yyyy
        String returnvalue = null;
        try {
            if (value != null) {
                String arry[] = value.split(" ");
//                value = value.replace(" ", "-");
                //Instantiating the SimpleDateFormat class
//                SimpleDateFormat sdf = new SimpleDateFormat(("dd-MM-yyyy"), Locale.ENGLISH);
//                Date date = sdf.parse(value);
                String month = getMonth(arry[1]);
                returnvalue = arry[0] + "-" + month + "-" + arry[2];
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return returnvalue;
    }

    public static String getMonth(String val) {
        String Value = null;
        try {
            switch (val) {
                case "Jan":
                    Value = "01";
                    break;
                case "Feb":
                    Value = "02";
                    break;
                case "Mar":
                    Value = "03";
                    break;
                case "Apr":
                    Value = "04";
                    break;
                case "May":
                    Value = "05";
                    break;
                case "Jun":
                    Value = "06";
                    break;
                case "Jul":
                    Value = "07";
                    break;
                case "Aug":
                    Value = "08";
                    break;
                case "Sep":
                    Value = "09";
                    break;
                case "Oct":
                    Value = "10";
                    break;
                case "Nov":
                    Value = "11";
                    break;
                case "Dec":
                    Value = "12";
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Value;
    }

    public static void getArrayforSpinnerBind(Context context, Spinner spinner,
                                              ArrayList<HashMap<String, String>> spinarrayvalue, String getkeyValue) {
        try {
            ArrayList<String> list = new ArrayList<>();
            if (spinarrayvalue.size() > 0) {
                for (int i = 0; i < spinarrayvalue.size(); i++) {
                    try {
                        list.add(spinarrayvalue.get(i).get(getkeyValue));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
            if (list.size() > 0) {
                setSpinnerBind(context, spinner, list);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void setSpinnerBind(Context context, Spinner
            spinner, ArrayList<String> spinarrayvalue) {
        try {
            if (spinarrayvalue.size() > 0) {
                ArrayAdapter<String> chooseYourFarmAdapter = new ArrayAdapter<String>(context, R.layout.spinner_layout, spinarrayvalue);
                spinner.setAdapter(chooseYourFarmAdapter);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static String getCropName(Context context, String cropname) {
        String CropName = cropname;
        try {
            switch (cropname) {
                case "Cotton":
                    CropName = context.getResources().getString(R.string.Cotton);
                    break;
                case "Chilli":
                    CropName = context.getResources().getString(R.string.Chilli);
                    break;
                case "Groundnut":
                    CropName = context.getResources().getString(R.string.Groundnut);
                    break;
                case "Maize":
                    CropName = context.getResources().getString(R.string.Maize);
                    break;
                case "Paddy":
                    CropName = context.getResources().getString(R.string.Paddy);
                    break;
                case "Potato":
                    CropName = context.getResources().getString(R.string.Potato);
                    break;
                case "Rice":
                    CropName = context.getResources().getString(R.string.Rice);
                    break;
                case "Soyabean":
                    CropName = context.getResources().getString(R.string.Soyabean);
                    break;
                case "Vegetables":
                    CropName = context.getResources().getString(R.string.Vegetables);
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return CropName;

    }

    public static void setNoofPlantDocImages(Context context, String FarmID) {
        try {
//            SharedPreferences prefs = context.getSharedPreferences(MyPlantDocImageCount, MODE_PRIVATE);
            SharedPreferences prefs = context.getSharedPreferences(MyPlantDocImageCount(context, FarmID), MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            String Timedate = getdate() + " " + gettime();
//            String Timedate = "14-09-2020 09:25:07";
            editor.putString("PlantDocImageCount" + Timedate, Timedate);
            editor.apply();
            refreshNoofPlantDocCount(context, FarmID);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void refreshNoofPlantDocCount(Context context, String FarmID) {
        try {
            SharedPreferences pref = context.getSharedPreferences(MyPlantDocImageCount(context, FarmID), MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            HashMap<String, String> map = (HashMap<String, String>) pref.getAll();
//            ArrayList<String> value = new ArrayList<>();
            List<String> maplist = new ArrayList(map.keySet());
            for (int i = 0; i < map.keySet().size(); i++) {
                //check 7 days less or not
                try {
                    String Key = maplist.get(i);
                    String Value = map.get(maplist.get(i));
                    String CurrentDateTime = getdate() + " " + gettime();
                    String DateTimeValue = getdatedifferenceinHours(Value, CurrentDateTime);
                    if (DateTimeValue != null && DateTimeValue.length() > 0) {
                        int times = Integer.parseInt(DateTimeValue);
                        if (times > 12) {
                            editor.remove(Key);
                            editor.commit();
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static int getTotalNoimageperDay(Context context, String FarmID) {
        int value = 0;
        try {
            SharedPreferences pref = context.getSharedPreferences(MyPlantDocImageCount(context, FarmID), MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            HashMap<String, String> map = (HashMap<String, String>) pref.getAll();
            value = map.size();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return value;
    }

    public static String getdatedifferenceinHours(String LastDateTime, String CurrentDateTime) {
        String value = "";
        if (CurrentDateTime != null && CurrentDateTime.length() > 0 && LastDateTime != null && LastDateTime.length() > 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH);
            Date Date1 = null, Date2 = null;
            try {
                Date1 = sdf.parse(CurrentDateTime);
                Date2 = sdf.parse(LastDateTime);
                long diff = Math.abs(Date2.getTime() - Date1.getTime());
                long diffHours = diff / (60 * 60 * 1000);
//                long diffHours = diff / (60 * 60 * 1000) % 24;
//                long diffDays = diff / (24 * 60 * 60 * 1000);
                value = String.valueOf(diffHours);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return value;
        } else {
            return value;
        }
    }

    public static String getdatedifferenceDays(String LastDateTime, String CurrentDateTime) {

        String value = "";
        try {
            if (CurrentDateTime != null && CurrentDateTime.length() > 0 && LastDateTime != null && LastDateTime.length() > 0) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                Date Date1 = null, Date2 = null;
                try {
                    Date date1 = sdf.parse(LastDateTime);
                    Date date2 = sdf.parse(CurrentDateTime);
                    long diff = date2.getTime() - date1.getTime();
                    float days = (diff / (1000 * 60 * 60 * 24));
                    value = String.valueOf(days);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return value;
    }

    // write text to file
//    public void WriteBtn(View v) {
//        // add-write text into file
//        try {
//            FileOutputStream fileout=new openFileOutput("mytextfile.txt", MODE_PRIVATE);
//            OutputStreamWriter outputWriter=new OutputStreamWriter(fileout);
//            outputWriter.write(textmsg.getText().toString());
//            outputWriter.close();
//
//            //display file saved message
//            Toast.makeText(getBaseContext(), "File saved successfully!",
//                    Toast.LENGTH_SHORT).show();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


    //    public static String MyPlantDocImageCount = "appPlantDocImageCount.xml";
    public static String MyPlantDocImageCount(Context context, String FarmerID) {
        String CurrentDate = getdate();
        CurrentDate = CurrentDate.replace("-", "");
        String FileName = FarmerID + "_" + CurrentDate + "_appPlantDocImageCount.xml";
        if (!checkXMLFilesExistorNot(context, FileName)) {
            setXMLFileList(context, FileName);
        }
        return FileName;
    }

    public static void setXMLFileList(Context context, String FileName) {
        try {
            SharedPreferences prefs = context.getSharedPreferences(MyPlantDocImageFileList, MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(FileName, "1");
            editor.apply();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public static boolean checkXMLFilesExistorNot(Context context, String FileName) {
        boolean value = false;
        try {
            SharedPreferences pref = context.getSharedPreferences(MyPlantDocImageFileList, MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            HashMap<String, String> map = (HashMap<String, String>) pref.getAll();
            List<String> maplist = new ArrayList(map.keySet());
            for (int i = 0; i < map.keySet().size(); i++) {
                String Key = maplist.get(i);
                String Value = map.get(maplist.get(i));
                if (FileName.equalsIgnoreCase(Key)) {
                    value = true;
                    break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return value;
    }

    public static String getAppVersion(Context context) {
        String version = context.getResources().getString(R.string.Version);
        version = version.replace("Version", "");
        version = version.replace("(", "");
        version = version.replace(")", "").trim();
        return version;
    }

    public static void downloadResponseMethod(String filename, Context context) {

        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
        File folder = new File(extStorageDirectory, FOLDER_NAME);

        File fileBrochure = new File(folder + "/" + filename);

        MimeTypeMap mime = MimeTypeMap.getSingleton();
        String ext = fileBrochure.getName().substring(fileBrochure.getName().lastIndexOf(".") + 1);
        String type = mime.getMimeTypeFromExtension(ext);

        try {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Uri contentUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileprovider", fileBrochure);
                intent.setDataAndType(contentUri, type);
            } else {
                intent.setDataAndType(Uri.fromFile(fileBrochure), type);
            }
            context.startActivity(intent);
        } catch (ActivityNotFoundException anfe) {
            Toast.makeText(context, "No activity found to open this attachment.", Toast.LENGTH_LONG).show();
        }
    }

    public static boolean checkgetLagLongnull(String val) {
        boolean value = false;
        if (val == null || val.equalsIgnoreCase("null") || val.equalsIgnoreCase("0.0")) {
            value = true;
        }
        return value;
    }

    public static String getStringCheck(String value) {
        String Value = "0";
        try {
            if (value != null && value.length() > 0 && !value.equalsIgnoreCase("null")) {
                Value = value;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Value;
    }

    public static int getAPIimeResponseinSecond(long starttime) {
        long totalRequestTime = System.currentTimeMillis() - starttime;
        int seconds = (int) ((totalRequestTime / 1000) % 60);
        if (seconds > 0) {
            return seconds;
        } else {
            return 0;
        }
    }

    public static String getCheckDate(String datevalue) {
        if (datevalue != null && datevalue.length() > 2) {
            return datevalue;
        } else
            return "0001-01-01";
    }

    public static String getDateReplace(String datevalue) {
        if (datevalue != null && !datevalue.equalsIgnoreCase("null") && datevalue.length() > 2) {
            datevalue = datevalue.replace("T00:00:00", "");
            datevalue = datevalue.replace("T12:00:00", "");
            datevalue = datevalue.replace("0001-01-01", "");
            return datevalue;
        } else
            return "";
    }

    public static String checkValueZero(String val) {
        if (val != null && val.length() > 0) {
            return val;
        } else return "0";
    }

    public static String getSMS(String sms) {
        String SMS = "JSON Response Error";
        try {
            if (sms != null && sms.contains("Unable to resolve host")) {
                SMS = "Internet Connection Error";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return SMS;
    }

    public static void OldFiledelete(DBAdapter db, int flag) {
        String sFileName = "SecuFarmSurveyor_ErrorFile";
        if (flag == 1) {
            try {//Not First time login
                File root = new File(Environment.getExternalStorageDirectory(), IMAGE_DIRECTORY);
                if (!root.exists()) {
                    root.mkdirs();
                }
                String FilePath = String.valueOf(root) + "/" + sFileName;
                File fdelete = new File(FilePath);
                int file_size_inKB = Integer.parseInt(String.valueOf(fdelete.length() / 1024));//in KB
                int file_size_inMB = Integer.parseInt(String.valueOf(file_size_inKB / 1024));//in MB

                if (file_size_inMB <= 40) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        Path path = Paths.get(FilePath);
                        BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);
                        String CurrentDate = getdateYYYYMMDD();
                        String CreatedDate = String.valueOf(attr.creationTime());
                        String createddate[] = CreatedDate.split("T");
//        String va = "2021-04-07T08:04:18Z";
//        va=va.replace("T"," ");
//        va=va.replace("Z","");
                        String dayinhours = getdatedifferenceDays(createddate[0], getdateYYYYMMDD());
//            String dayinhours = getdatedifferenceinHours(createddate[0], CurrentDate);
                        if (dayinhours != null) {
                            int nValue = Integer.parseInt(dayinhours);
                            if (nValue > 48) {
                                if (fdelete.exists()) {
                                    if (fdelete.delete()) {
                                        System.out.println("file Deleted :" + sFileName);
                                    } else {
                                        System.out.println("file not Deleted :" + sFileName);
                                    }
                                }
                            }
                        }
//                        System.out.println("creationTime: " + attr.creationTime());
//                        System.out.println("lastAccessTime: " + attr.lastAccessTime());
//                        System.out.println("lastModifiedTime: " + attr.lastModifiedTime());
                    }
                } else {
                    if (fdelete.exists()) {
                        if (fdelete.delete()) {
                            System.out.println("file Deleted :" + sFileName);
                        } else {
                            System.out.println("file not Deleted :" + sFileName);
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            //Database
            try {
                db.open();
                io.requery.android.database.sqlite.SQLiteDatabase SqliteDB = db.getSQLiteDatabase();
                SqliteDB.beginTransaction();
                String sqldelete = "delete from " + db.TABLE_tblEventLogError + " where DateTime <= date('now','-2 day')";
                db.getSQLiteDatabase().execSQL(sqldelete);

                SqliteDB.setTransactionSuccessful();
                SqliteDB.endTransaction();
                db.getClass();

                db.close();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (flag == 2) {
            try {//Not First time login
                File root = new File(Environment.getExternalStorageDirectory(), IMAGE_DIRECTORY);
                if (!root.exists()) {
                    root.mkdirs();
                }
                String FilePath = String.valueOf(root) + "/" + sFileName;
                File fdelete = new File(FilePath);
                if (fdelete.exists()) {
                    if (fdelete.delete()) {
                        System.out.println("file Deleted :" + sFileName);
                    } else {
                        System.out.println("file not Deleted :" + sFileName);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            //Database
            try {
                db.open();
                io.requery.android.database.sqlite.SQLiteDatabase SqliteDB = db.getSQLiteDatabase();
                SqliteDB.beginTransaction();
                String sqldelete = "delete from " + db.TABLE_tblEventLogError + "";
                db.getSQLiteDatabase().execSQL(sqldelete);

                SqliteDB.setTransactionSuccessful();
                SqliteDB.endTransaction();
                db.getClass();

                db.close();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void setDateAfter30Days(Context context, final TextView txt, final String flag) {
        try {
            final Dialog datepic = new Dialog(context);
            Window window = datepic.getWindow();
            window.requestFeature(Window.FEATURE_NO_TITLE);
            datepic.setContentView(R.layout.datepicker);
            datepic.setCanceledOnTouchOutside(true);
            Button btnset = (Button) datepic.findViewById(R.id.btn_Set);
            Button btnclear = (Button) datepic.findViewById(R.id.btn_Clear);
            Button btncancel = (Button) datepic.findViewById(R.id.btn_cancel);


            // datepic.getWindow().setLayout(240, 190);
            datepic.getWindow().setLayout(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
            final DatePicker datetext = (DatePicker) datepic.findViewById(R.id.datepicker);


            final Calendar calendar = Calendar.getInstance();

            long now = calendar.getTimeInMillis();
            long MaxMin = 1000 * 60 * 60 * 24 * 21;//30 days
            datetext.setMinDate(now);//30 days before
            datetext.setMaxDate(now + MaxMin);//30 days forwards


            btncancel.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    datepic.dismiss();
                }
            });

            btnclear.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    txt.setText("");
                    datepic.dismiss();

                }
            });
            btnset.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    int dt = datetext.getDayOfMonth();
                    int month = datetext.getMonth();
                    int year = datetext.getYear();
                    int mnth = month + 1;
                    String sDate;
                    if (dt < 10) {
                        sDate = "0" + String.valueOf(dt);
                    } else {
                        sDate = String.valueOf(dt);
                    }
                    String sMonth;
                    if (mnth < 10) {
                        sMonth = "0" + String.valueOf(mnth);
                    } else {
                        sMonth = String.valueOf(mnth);
                    }
                    String sSellectedDate = "";
                    if (flag.equalsIgnoreCase("DDMMYYYY")) {
                        sSellectedDate = sDate + "-" + sMonth + "-"
                                + String.valueOf(year);
                    } else if (flag.equalsIgnoreCase("YYYYMMDD")) {
                        sSellectedDate = String.valueOf(year) + "-" + sMonth + "-"
                                + sDate;
                    }
                    txt.setText(sSellectedDate);
                    datepic.dismiss();
                }
            });

            datepic.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    } //Datepicker with max and min date

    public static double getDecimal(double number) {
        double Value = 0.0;
        try {
            DecimalFormat df = new DecimalFormat("#.###");
            df.setRoundingMode(RoundingMode.CEILING);
            Value = Double.parseDouble(df.format(number));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Value;
    }

    public static double getDecimal2(double number) {
        double Value = 0.0;
        try {
            DecimalFormat df = new DecimalFormat("#.##");
            df.setRoundingMode(RoundingMode.CEILING);
            Value = Double.parseDouble(df.format(number));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Value;
    }

    public static String ConvertDateFormat(String date, int flag) {
        String Datevalue = "2001-01-01";
        SimpleDateFormat ddmmyyyy = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
        SimpleDateFormat yyyymmdd = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        try {
            Date date1;
            switch (flag) {
                case 1:
                    if (date != null && date.length() > 0 && !date.equalsIgnoreCase("null")) {
                        Datevalue = yyyymmdd.format(ddmmyyyy.parse(date));
                    }
                    break;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Datevalue;
    }

    public static boolean checkNoofDaysislessthan10(String StartDate, String NextDate) {
        boolean value = false;
        if (NextDate != null && NextDate.length() > 0 && StartDate != null && StartDate.length() > 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
            Date Date1 = null, Date2 = null;
            try {
                Date1 = sdf.parse(NextDate);
                Date2 = sdf.parse(StartDate);
                long diff = (Date1.getTime() - Date2.getTime());
                long diffSeconds = diff / 1000 % 60;
                long diffMinutes = diff / (60 * 1000) % 60;
                long diffHours = diff / (60 * 60 * 1000) % 24;
                long diffDays = diff / (24 * 60 * 60 * 1000);

                if (diffDays <= 10) {
                    value = true;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return value;
    }

    public static void setFontsStyleTxt(Context context, TextView txtview, int flag) {
        try {
            UtilFonts.UtilFontsInitialize(context);
            switch (flag) {
                case 1://Main Heading _18sdp
                    txtview.setTypeface(UtilFonts.FS_Ultra);
                    break;
                case 2://Sub Heading White _12sdp/_10sdp
                    txtview.setTypeface(UtilFonts.FS_Bold);
                    break;
                case 3://Sub Heading Heading _18sdp
                    txtview.setTypeface(UtilFonts.FS_UltraItalic);
                    break;
                case 4://Sub Heading _18sdp
                    txtview.setTypeface(UtilFonts.KT_Bold);
                    break;
                case 5://Black Text _12sdp
                    txtview.setTypeface(UtilFonts.KT_Medium);
                    break;
                case 6://Gray/ Black Text _10sdp
                    txtview.setTypeface(UtilFonts.KT_Regular);
                    break;
                case 7://White _10sdp
                    txtview.setTypeface(UtilFonts.KT_Light);
                    break;
            }
//            public static Typeface FS_Ultra;
//            public static Typeface FS_Bold;
//            public static Typeface FS_UltraItalic;
//            //KleinText
//            public static Typeface KT_Bold;
//            public static Typeface KT_Medium;
//            public static Typeface KT_Regular;
//            public static Typeface KT_Light;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void setFontsStyle(Context context, Button button) {
        try {
            UtilFonts.UtilFontsInitialize(context);
            //Button _10sdp
            button.setTypeface(UtilFonts.KT_Bold);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void setFontsStyle(Context context, EditText edit) {
        try {
            UtilFonts.UtilFontsInitialize(context);
            //Button _10sdp
            edit.setTypeface(UtilFonts.KT_Medium);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void setFontsStyle(Context context, RadioButton btn) {
        try {
            UtilFonts.UtilFontsInitialize(context);
            //Button _10sdp
            btn.setTypeface(UtilFonts.KT_Medium);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void setFontsStyle(Context context, CheckBox btn) {
        try {
            UtilFonts.UtilFontsInitialize(context);
            //Button _10sdp
            btn.setTypeface(UtilFonts.KT_Medium);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void setToastCropIDNotFound(Context context) {
        getDynamicLanguageToast(context, "CropIDnotfound", R.string.CropIDnotfound);
    }

    public static void setToastComingsoon(Context context) {
        getDynamicLanguageToast(context, "Comingsoon", R.string.Comingsoon);
    }

    public static void setToastPleaseselectyour(Context context) {
        getDynamicLanguageToast(context, "Pleaseselectyour", R.string.Pleaseselectyour);
    }

    public static void setToastNodataavailable(Context context) {
        getDynamicLanguageToast(context, "Nodataavailable", R.string.Nodataavailable);
    }

    public static void setToastServerError(Context context) {
        getDynamicLanguageToast(context, "ServerError", R.string.ServerError);
    }

    public static void getDynamicLanguageToast(Context con, String Key, int Keys) {
        String TextValue = "";
        try {
            if (SelectedLanguageValue != null && SelectedLanguageValue.size() > 0) {
                if (SelectedLanguageValue.containsKey(Key)) {
                    TextValue = SelectedLanguageValue.get(Key);
                    if (TextValue == null || TextValue.length() == 0 || TextValue.equalsIgnoreCase("")) {
                        TextValue = con.getResources().getString(Keys);
                    }
                } else {
                    TextValue = con.getResources().getString(Keys);
                }
            } else {
                TextValue = con.getResources().getString(Keys);
            }
            Toast.makeText(con, TextValue, Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void getDynamicLanguageToast(Context con, String Value) {
        try {
            Toast.makeText(con, Value, Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void setDynamicLanguagevale(Context con, IconizedMenu iconmenu, int Rid, String Key, int Keys) {
        String TextValue = "";
        try {
            if (SelectedLanguageValue != null && SelectedLanguageValue.size() > 0) {
                if (SelectedLanguageValue.containsKey(Key)) {
                    TextValue = SelectedLanguageValue.get(Key);
                    if (TextValue == null || TextValue.length() == 0 || TextValue.equalsIgnoreCase("")) {
                        TextValue = con.getResources().getString(Keys);
                    }
                } else {
                    TextValue = con.getResources().getString(Keys);
                }
            } else {
                TextValue = con.getResources().getString(Keys);
            }
            Menu menu = iconmenu.getMenu();
            menu.findItem(Rid).setTitle(TextValue);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
//        return TextValue;
    }

    public static void setDynamicLanguage(Context con, TextView textView, String Key, int Keys) {
        String TextValue = "";
        try {
            if (SelectedLanguageValue != null && SelectedLanguageValue.size() > 0) {
                if (SelectedLanguageValue.containsKey(Key)) {
                    TextValue = SelectedLanguageValue.get(Key);
                    if (TextValue == null || TextValue.length() == 0 || TextValue.equalsIgnoreCase("")) {
                        TextValue = con.getResources().getString(Keys);
                    }
                } else {
                    TextValue = con.getResources().getString(Keys);
                }
            } else {
                TextValue = con.getResources().getString(Keys);
            }
            textView.setText(TextValue);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void setDynamicLanguage(Context con, Button textView, String Key, int Keys) {
        String TextValue = "";
        try {
            if (SelectedLanguageValue != null) {
                if (SelectedLanguageValue.containsKey(Key)) {
                    TextValue = SelectedLanguageValue.get(Key);
                    if (TextValue == null || TextValue.length() == 0 || TextValue.equalsIgnoreCase("")) {
                        TextValue = con.getResources().getString(Keys);
                    }
                } else {
                    TextValue = con.getResources().getString(Keys);
                }
            } else {
                TextValue = con.getResources().getString(Keys);
            }
            textView.setText(TextValue);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void setDynamicLanguage(Context con, RadioButton textView, String Key, int Keys) {
        String TextValue = "";
        try {
            if (SelectedLanguageValue != null) {
                if (SelectedLanguageValue.containsKey(Key)) {
                    TextValue = SelectedLanguageValue.get(Key);
                    if (TextValue == null || TextValue.length() == 0 || TextValue.equalsIgnoreCase("")) {
                        TextValue = con.getResources().getString(Keys);
                    }
                } else {
                    TextValue = con.getResources().getString(Keys);
                }
            } else {
                TextValue = con.getResources().getString(Keys);
            }
            textView.setText(TextValue);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static String getDynamicLanguageValue(Context con, String Key, int Keys) {
        String TextValue = "";
        try {
            if (SelectedLanguageValue != null && SelectedLanguageValue.size() > 0) {
                if (SelectedLanguageValue.containsKey(Key)) {
                    TextValue = SelectedLanguageValue.get(Key);
                    if (TextValue == null || TextValue.length() == 0 || TextValue.equalsIgnoreCase("")) {
                        TextValue = con.getResources().getString(Keys);
                    }
                } else {
                    TextValue = con.getResources().getString(Keys);
                }
            } else {
                TextValue = con.getResources().getString(Keys);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return TextValue;
    }

    public static String getRandomNumber() {
        Random rnd = new Random();
        int n = 10000000 + rnd.nextInt(90000000);
        return "" + n;
    }

    public static void setCustomSearchableSpinner(Context con, CustomSearchableSpinner spin, String keys, int key) {
        spin.setTitle(getDynamicLanguageValue(con, keys, key));
        spin.setPositiveButton(getDynamicLanguageValue(con, "Cancel", R.string.Cancel));
    }

    public static boolean dateCheckgreater(String Datefrom, String Dateto) {
        boolean value = true;
        try {
            Date date1 = new SimpleDateFormat("dd-MM-yyyy").parse(Datefrom);
            Date date2 = new SimpleDateFormat("dd-MM-yyyy").parse(Dateto);
            if (date1.compareTo(date2) >= 0) {
                System.out.println("Date 1 occurs after Date 2");
                value = false;
            }
//            else if(d1.compareTo(d2) < 0) {
//                System.out.println("Date 1 occurs before Date 2");
//            } else if(d1.compareTo(d2) == 0) {
//                System.out.println("Both dates are equal");
//            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return value;
    }

    static public double GetRevisedFarmArea(double Area) {
        double minvalue = 0;
        double celingvalue = 0.25;
        int counter = 2;
        int iPart = (int) Area;
        double fractionpart = Area - iPart;
        if (fractionpart == celingvalue)
            return Area;
        while (minvalue < 1) {
            if (minvalue <= fractionpart && fractionpart < celingvalue) {
                Area = iPart + celingvalue;
                break;
            }
            minvalue = celingvalue;
            celingvalue = .25 * counter;
            counter++;
        }
        return Area;
    }


    public static void sortAscFarmArrayList(){

        //ascending order
        Comparator<UserFarmResponse> compareByName = new Comparator<UserFarmResponse>() {
            @Override
            public int compare(UserFarmResponse o1, UserFarmResponse o2) {
                return o1.getFarmerName().toLowerCase().trim().compareTo(o2.getFarmerName().toLowerCase().trim());
            }
        };


        Collections.sort(userfarmListforpolicyfarmlist, compareByName);


    }

}
