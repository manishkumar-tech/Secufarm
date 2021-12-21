package com.weather.risk.mfi.myfarminfo.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.database.DBAdapter;
import com.weather.risk.mfi.myfarminfo.database.State_Crop_StateDistr_Project_download;
import com.weather.risk.mfi.myfarminfo.entities.AllFarmDetail;
import com.weather.risk.mfi.myfarminfo.entities.CropQueryData;
import com.weather.risk.mfi.myfarminfo.entities.Register;
import com.weather.risk.mfi.myfarminfo.entities.SignInData;
import com.weather.risk.mfi.myfarminfo.home.AppController;
import com.weather.risk.mfi.myfarminfo.home.PollReceiver;
import com.weather.risk.mfi.myfarminfo.mapfragments.LatLonCellID;
import com.weather.risk.mfi.myfarminfo.retofitservices.ApiService;
import com.weather.risk.mfi.myfarminfo.retofitservices.ResponseObserver;
import com.weather.risk.mfi.myfarminfo.retrofitmodelnew.Dashboard_FarmerResponse;
import com.weather.risk.mfi.myfarminfo.retrofitmodelnew.LoginCheckCattleDashboardResponse;
import com.weather.risk.mfi.myfarminfo.utils.AppConstant;
import com.weather.risk.mfi.myfarminfo.utils.AppManager;
import com.weather.risk.mfi.myfarminfo.utils.LocationPollReceiver;
import com.weather.risk.mfi.myfarminfo.utils.TransparentProgressDialog;
import com.weather.risk.mfi.myfarminfo.utils.UtilFonts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.requery.android.database.sqlite.SQLiteStatement;

import static com.weather.risk.mfi.myfarminfo.logging.LocalFileWriter.SaveLocalFile;
import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.AISAvailable_Tubewell;
import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.SN_LoginWithOtp;
import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.SelectedLanguageValue;
import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.ServiceScreenJSON;
import static com.weather.risk.mfi.myfarminfo.utils.AppManager.NOGPSDialog;
import static com.weather.risk.mfi.myfarminfo.utils.AppManager.getCattleOwnerID;
import static com.weather.risk.mfi.myfarminfo.utils.AppManager.getLoginCheckAPI;
import static com.weather.risk.mfi.myfarminfo.utils.AppManager.getLoginOTP;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.checkmobileno;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getAPIimeResponseinSecond;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getDynamicLanguageToast;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getDynamicLanguageValue;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getSMS;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getUIDforScreenTracking;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setDynamicLanguage;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setFontsStyle;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setFontsStyleTxt;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setScreenTracking;

public class LoginWithOtp_New extends AppCompatActivity {

    public static final String LOCATION = "location";
    public static boolean status = true;
    public static String syncFor = AppConstant.STATE_ID;
    public String syncMsg = "Syncronizing " + AppConstant.STATE_ID;
    Context context = this;
    DBAdapter db;
    HashMap<String, String> pickCropIdOrValue;
    double latitude;
    double longitude;
    int syncCount = 1;
    HashMap<String, String> hashMap;
    String storeCurrentStateId = "noValue";
    SharedPreferences prefs;
    Intent service;
    SharedPreferences prefs1 = null;
    String major_res = null, minor_res = null, build_res = null;
    // LinearLayout service_layout;
    boolean isServiceShow = false;
    TransparentProgressDialog dialoug;

    int MULTIPLE_PERMISSIONS = 7;
    String[] permissions = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.SEND_SMS,
            Manifest.permission.CAMERA
    };
    State_Crop_StateDistr_Project_download state_crop_stateDistrProjectdownload;


    private Register data;
    private Menu menu;

    String UID = "";


    RadioGroup radiogroup;
    RadioButton rb_WithoutPassword, rb_WithPassword;
    TextView txt_login, txt_username, txt_password;
    EditText edit_username, edit_password;
    CheckBox checkbox_savepassword;
    ImageView showpassword, hidepassword;
    Button btn_login, btn_forgotpassword, btn_GetOTP, btn_UserRegistration;
    String WithWithoutPassword = "WithoutPassword";
    String USERNAME = null, PASSWORD = null;
    LinearLayout ll_password;

    private ApiService apiService;
    private CompositeDisposable compositeDisposable;
    TransparentProgressDialog dialog;
    private long mRequestStartTime;

    //    public static String strToken = "234567", strPhoneno = "7093660861";
    public static String strToken = "234567", strPhoneno = "";
    public static List<LoginCheckCattleDashboardResponse> CattleDash_OwnerIDList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.login_with_otp);
//        setContentView(R.layout.loginwithotp);
        setContentView(R.layout.loginwithotp_new);

        setIdDefine();
        CattleDash_OwnerIDList = new ArrayList<>();
        strPhoneno = "";
        checkPermissions();

        apiService = AppController.getInstance().getApiServiceWeatherSecureProAPI();
        compositeDisposable = new CompositeDisposable();

        db = new DBAdapter(this);
        data = new Register();
        hashMap = new HashMap<>();


        state_crop_stateDistrProjectdownload = new State_Crop_StateDistr_Project_download(LoginWithOtp_New.this);
        if (!AppManager.getInstance().isLocationServicesAvailable(this))
            NOGPSDialog(this);


        if (prefs1 == null) {
            prefs1 = getSharedPreferences("version", MODE_PRIVATE);
        }
        if (prefs == null) {
            prefs = getSharedPreferences(AppConstant.SHARED_PREFRENCE_NAME, MODE_PRIVATE);
        }


        db.open();
        Cursor cursor = db.getAllStates();
        if (!(cursor.getCount() > 0)) {
//            state_crop_stateDistrProjectdownload.loadData();
            loadData();
        }

        Cursor getAllUser = db.getAllCredentials();
        if (getAllUser.getCount() > 0) {
            getAllUser.moveToFirst();
            do {
                System.out.println("User Name : " + getAllUser.getString(getAllUser.getColumnIndex(DBAdapter.USER_NAME)));
                System.out.println("Visible Name : " + getAllUser.getString(getAllUser.getColumnIndex(DBAdapter.VISIBLE_NAME)));
                System.out.println("Password : " + getAllUser.getString(getAllUser.getColumnIndex(DBAdapter.PASSWORD)));
                System.out.println("Email : " + getAllUser.getString(getAllUser.getColumnIndex(DBAdapter.EMAIL_ADDRESS)));
                System.out.println("Created Date Time : " + getAllUser.getString(getAllUser.getColumnIndex(DBAdapter.CREATED_DATE_TIME)));
                System.out.println("User id : " + getAllUser.getString(getAllUser.getColumnIndex(DBAdapter.USER_ID)));
                System.out.println("Sending Status : " + getAllUser.getString(getAllUser.getColumnIndex(DBAdapter.SENDING_STATUS)));

            } while (getAllUser.moveToNext());
        }
        db.close();

        PollReceiver.scheduleAlarms(this);


        AppConstant.mobile_no = prefs.getString(AppConstant.PREFRENCE_KEY_MOBILE, "1234567890");
        AppConstant.role = prefs.getString(AppConstant.PREFRENCE_KEY_ROLE, null);
        AppConstant.dashboard = prefs.getString(AppConstant.PREFRENCE_KEY_DASHBOARD, "");
        AppConstant.isSubdistrict = prefs.getString(AppConstant.PREFRENCE_KEY_IS_SUB_DISTRICT, "No");
        AppConstant.isAgronimist = prefs.getString(AppConstant.PREFRENCE_KEY_IS_AGRONOMIST, "No");

        AppConstant.userTypeID = prefs.getString(AppConstant.PREFRENCE_KEY_IS_USERTYPEID, null);
        AppConstant.client_name = prefs.getString(AppConstant.PREFRENCE_KEY_CLIENT_NAME, "");
        AppConstant.baseID = prefs.getString(AppConstant.PREFRENCE_KEY_BASE_USER_ID, "0");
        AppConstant.selected_crop = prefs.getString(AppConstant.PREFRENCE_KEY_IS_CROP, "Cotton");
        AppConstant.selected_cropId = prefs.getString(AppConstant.PREFRENCE_KEY_IS_CROP_ID, "12");

        AppConstant.selected_district = prefs.getString("district_s", null);
        AppConstant.selected_village = prefs.getString("village_s", null);
        AppConstant.selected_farm = prefs.getString("farm_s", null);

        AISAvailable_Tubewell = prefs.getString(AppConstant.AISAvailable_TubewellKey, null);

        prefs = getSharedPreferences(AppConstant.SHARED_PREFRENCE_NAME, MODE_PRIVATE);
        AppConstant.user_id = prefs.getString(AppConstant.PREFRENCE_KEY_USER_ID, null);
        System.out.println("Got User id Home : " + AppConstant.user_id);

        boolean isLogin = prefs.getBoolean(AppConstant.PREFRENCE_KEY_ISLOGIN, false);
        AppConstant.isLogin = isLogin;
        if (isLogin) {
            AppConstant.user_id = prefs.getString(AppConstant.PREFRENCE_KEY_USER_ID, null);
            AppConstant.PREFRENCE_KEY_CattleDashboardMobileValue = prefs.getString(AppConstant.PREFRENCE_KEY_CattleDashboardMobileKey, null);
            System.out.println("Got User id Home : " + AppConstant.user_id);
            AppConstant.visible_Name = prefs.getString(AppConstant.PREFRENCE_KEY_VISIBLE_NAME, "Demo");
            AppConstant.mobile_no = prefs.getString(AppConstant.PREFRENCE_KEY_MOBILE, "1234567890");
            AppConstant.PREFRENCE_KEY_CattleDashboardMobileValue = prefs.getString(AppConstant.PREFRENCE_KEY_CattleDashboardMobileKey, null);
            strPhoneno = AppConstant.PREFRENCE_KEY_CattleDashboardMobileValue;
            //Screen Service
            try {
                String ScreenService = prefs.getString(AppConstant.PREFRENCE_KEY_ServiceScreen, null);
//                ScreenService = "{\"ServiceScreens\":{\"SoilDoctor\":false,\"PlantDoctor\":true,\"CropAdvisory\":true,\"Weather\":false,\"MarketPlace\":false,\"PestandDisease\":false,\"NPKCalculator\":false,\"DiseaseDiagnosis\":true,\"NutritionManagement\":true,\"WeedManagement\":false,\"PostHarvestManagement\":true,\"IrrigationManagement\":false,\"MandiInformation\":true,\"YeildImprovement\":true,\"GuideLines\":false,\"Tubewell\":true}}";
                if (ScreenService != null && ScreenService.length() > 0) {
                    ServiceScreenJSON = new JSONObject(ScreenService);
                } else {
                    ServiceScreenJSON = new JSONObject();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            if (AppConstant.visible_Name == null ||
                    AppConstant.visible_Name.equalsIgnoreCase("null")
            ) {
                AppConstant.visible_Name = "";
            }
        } else {
            AppConstant.user_id = prefs.getString(AppConstant.PREFRENCE_KEY_USER_ID, null);
            System.out.println("Got User id Home : " + AppConstant.user_id);
            AppConstant.visible_Name = prefs.getString(AppConstant.PREFRENCE_KEY_VISIBLE_NAME, "");
            AppConstant.mobile_no = prefs.getString(AppConstant.PREFRENCE_KEY_MOBILE, "");
            AppConstant.PREFRENCE_KEY_CattleDashboardMobileValue = prefs.getString(AppConstant.PREFRENCE_KEY_CattleDashboardMobileKey, null);
            strPhoneno = AppConstant.PREFRENCE_KEY_CattleDashboardMobileValue;
            if (AppConstant.visible_Name == null ||
                    AppConstant.visible_Name.equalsIgnoreCase("null")
            ) {
                AppConstant.visible_Name = "";
            }
            //Screen Service
            try {
                String ScreenService = prefs.getString(AppConstant.PREFRENCE_KEY_ServiceScreen, null);
//                ScreenService = "{\"ServiceScreens\":{\"SoilDoctor\":false,\"PlantDoctor\":true,\"CropAdvisory\":true,\"Weather\":false,\"MarketPlace\":false,\"PestandDisease\":false,\"NPKCalculator\":false,\"DiseaseDiagnosis\":true,\"NutritionManagement\":true,\"WeedManagement\":false,\"PostHarvestManagement\":true,\"IrrigationManagement\":false,\"MandiInformation\":true,\"YeildImprovement\":true,\"GuideLines\":false,\"Tubewell\":true}}";
                if (ScreenService != null && ScreenService.length() > 0) {
                    ServiceScreenJSON = new JSONObject(ScreenService);
                } else {
                    ServiceScreenJSON = new JSONObject();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }


        String userTypeID = AppConstant.userTypeID;
        String CattleDashboadMobileNo = AppConstant.PREFRENCE_KEY_CattleDashboardMobileValue;
        Log.v("useridddd", AppConstant.user_id + "===" + userTypeID);

        if (AppConstant.user_id != null && !AppConstant.user_id.equalsIgnoreCase("null") && AppConstant.user_id.length() > 0) {
            Log.v("useridddd", AppConstant.user_id + "");
            gotoHomeScreen(1);
        } else if (strPhoneno != null && !strPhoneno.equalsIgnoreCase("null") &&
                strPhoneno.length() > 0) {
            Log.v("useridddd", AppConstant.user_id + "");
            gotoHomeScreen(2);
        }

        //Heroit Comment
        LocationPollReceiver.scheduleAlarms(LoginWithOtp_New.this);


        String saved_major1 = prefs1.getString("major", null);
        if (saved_major1 == null) {
            SharedPreferences.Editor editor = prefs1.edit();
            editor.putString("major", "4");
            editor.putString("minor", "6");
            editor.putString("build", "8");
            editor.apply();
        }


        //ScreenTracking
        UID = getUIDforScreenTracking();
        //if the screen is already having users the this screen will not show and no need for screen tracking
        if (AppConstant.user_id == null || AppConstant.user_id.equalsIgnoreCase("null")) {
            Log.v("useridddd", AppConstant.user_id + "");
            setScreenTracking(this, db, SN_LoginWithOtp, UID);
        }


        showpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showpassword.setVisibility(View.GONE);
                hidepassword.setVisibility(View.VISIBLE);
                edit_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        });

        hidepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showpassword.setVisibility(View.VISIBLE);
                hidepassword.setVisibility(View.GONE);
                edit_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
        });
//        try {
//            String user_name = prefs.getString(AppConstant.PREFRENCE_KEY_EMAIL, "");
//            String password = prefs.getString(AppConstant.PREFRENCE_KEY_PASS, "");
//            Boolean bool = prefs.getBoolean(AppConstant.PREFRENCE_KEY_ISSAVED, false);
//            checkbox_savepassword.setChecked(bool);
//            edit_username.setText(user_name);
//            edit_password.setText(password);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        String mob = AppConstant.mobile_no;
//        edit_username.setText(mob);
//        edit_password.setText("");
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                USERNAME = edit_username.getText().toString();
//                if (WithWithoutPassword.equalsIgnoreCase("WithoutPassword")) {
//                    PASSWORD = USERNAME;
//                } else if (WithWithoutPassword.equalsIgnoreCase("WithPassword")) {
//                    PASSWORD = edit_password.getText().toString();
//                }
                PASSWORD = edit_password.getText().toString();
                data.setMailId(USERNAME);
                data.setPassword(PASSWORD);
                SharedPreferences.Editor editor = prefs.edit();
                if (checkbox_savepassword.isChecked()) {
                    System.out.println("isCHecked");
                    editor.putString(AppConstant.PREFRENCE_KEY_EMAIL, USERNAME);
                    editor.putString(AppConstant.PREFRENCE_KEY_PASS, PASSWORD);
                    editor.putBoolean(AppConstant.PREFRENCE_KEY_ISSAVED, true);
                    editor.putBoolean(AppConstant.PREFRENCE_KEY_ISLOGIN, false);
                    editor.commit();
                } else {
                    editor.putString(AppConstant.PREFRENCE_KEY_EMAIL, "");
                    editor.putString(AppConstant.PREFRENCE_KEY_PASS, "");
                    editor.putBoolean(AppConstant.PREFRENCE_KEY_ISSAVED, false);
                    editor.putBoolean(AppConstant.PREFRENCE_KEY_ISLOGIN, false);
                    editor.commit();
                }


//                if (USERNAME == null || USERNAME.length() < 3) {
//                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.Pleaseentervalid), Toast.LENGTH_SHORT).show();
//                } else {
//                    if (PASSWORD == null || PASSWORD.length() < 2) {
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.Enteryourpassword), Toast.LENGTH_SHORT).show();
//                    } else {
//                        submit();
//                    }
//                }
                if (USERNAME != null && USERNAME.length() == 10 && checkmobileno(USERNAME)) {
                    submit();
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.Pleaseentervalid), Toast.LENGTH_SHORT).show();
                }

            }
        });
        btn_forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forgotPasswordMethod();
            }
        });


        edit_password.setText("");
//        edit_password.setEnabled(false);
//        showpassword.setEnabled(false);
//        hidepassword.setEnabled(false);
//        checkbox_savepassword.setEnabled(false);
//        rb_WithoutPassword.setChecked(true);
//        ll_password.setVisibility(View.GONE);
        btn_forgotpassword.setVisibility(View.GONE);
        txt_username.setText(getResources().getString(R.string.MobileNumber));

        setVisibleOTPLogin(1);
        btn_GetOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                USERNAME = edit_username.getText().toString();
                if (USERNAME != null && USERNAME.length() == 10 && checkmobileno(USERNAME)) {
                    getOTPMethod(USERNAME);
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.Pleaseentervalid), Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_UserRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(LoginWithOtp_New.this, SignUpActivity.class);
                Intent intent = new Intent(LoginWithOtp_New.this, FarmSignUp.class);
                startActivity(intent);

            }
        });

        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_WithoutPassword) {
                    USERNAME = null;
                    PASSWORD = null;
                    edit_password.setText("");
                    edit_password.setEnabled(false);
                    showpassword.setEnabled(false);
                    hidepassword.setEnabled(false);
                    checkbox_savepassword.setEnabled(false);
                    WithWithoutPassword = "WithoutPassword";
                    ll_password.setVisibility(View.GONE);
                    btn_forgotpassword.setVisibility(View.GONE);
                    txt_username.setText(getResources().getString(R.string.MobileNumber));
                } else if (checkedId == R.id.rb_WithPassword) {
                    USERNAME = null;
                    PASSWORD = null;
                    edit_password.setEnabled(true);
                    showpassword.setEnabled(true);
                    hidepassword.setEnabled(true);
                    checkbox_savepassword.setEnabled(true);
                    WithWithoutPassword = "WithPassword";
                    ll_password.setVisibility(View.VISIBLE);
//                    btn_forgotpassword.setVisibility(View.VISIBLE);
                    txt_username.setText(getResources().getString(R.string.Username));
                }
            }
        });

        if (SelectedLanguageValue == null || SelectedLanguageValue.size() == 0) {
            String SQL = "Select MyKey,English from tblLocalTranslation where English !='' order by MyKey";
            getSelectedLanguages(SQL);
            if (SelectedLanguageValue == null || SelectedLanguageValue.size() == 0) {
                downloadLocalTranslation();
            }
        }

    }

    public void downloadLocalTranslation() {
        try {
            final TransparentProgressDialog dialoug = new TransparentProgressDialog(LoginWithOtp_New.this,
                    getDynamicLanguageValue(getApplicationContext(), "TranslationDataisdownloading", R.string.TranslationDataisdownloading));
            dialoug.show();
            String URL = AppManager.getInstance().LocalTranslationURL;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.
                            try {
                                if (response != null && response.length() > 0) {
//									GsonBuilder builder = new GsonBuilder();
//									Gson gson = builder.create();
//									String str = gson.fromJson(response, String.class);
                                    JSONArray array = new JSONArray(response);
                                    if (array.length() > 0) {
                                        db.open();
                                        db.insertLocalTranslation(array);
                                    }
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                Toast.makeText(LoginWithOtp_New.this, getResources().getString(R.string.ServerError), Toast.LENGTH_LONG).show();

                            }

                            dialoug.dismiss();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    dialoug.cancel();
                    System.out.println("Volley Error : " + error);
                }
            });

            int socketTimeout = 60000;//60 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, 0, 0);
            stringRequest.setRetryPolicy(policy);
            // Adding request to volley request queue
            AppController.getInstance().addToRequestQueue(stringRequest);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getSelectedLanguages(String SQL) {
        try {
            db.open();
            SelectedLanguageValue = new HashMap<>();
            SelectedLanguageValue = db.getDynamicTableKeyValue(SQL);
            String valu = "";
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void submit() {
        if (isValidEntry()) {
            if (AppConstant.APP_MODE == AppConstant.OFFLINE) {
                db.open();
                Cursor cursor = db.isAuthenticated(data.getMailId(), data.getPassword());
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    String userId = cursor.getString(cursor.getColumnIndex(DBAdapter.USER_ID));
                    String visibleName = cursor.getString(cursor.getColumnIndex(DBAdapter.VISIBLE_NAME));
                    if (prefs == null) {
                        prefs = getSharedPreferences(AppConstant.SHARED_PREFRENCE_NAME, MODE_PRIVATE);
                    }
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString(AppConstant.PREFRENCE_KEY_USER_ID, userId);
                    editor.putString(AppConstant.PREFRENCE_KEY_VISIBLE_NAME, visibleName);
                    editor.putBoolean(AppConstant.PREFRENCE_KEY_ISLOGIN, true);
                    editor.commit();
                    AppConstant.isLogin = true;
                    AppConstant.user_id = userId;
                    AppConstant.visible_Name = visibleName;
                    if (AppConstant.visible_Name == null ||
                            AppConstant.visible_Name.equalsIgnoreCase("null")
                    ) {
                        AppConstant.visible_Name = "";
                    }
//                    Intent intent = new Intent(LoginWithOtp_New.this, AddFarmOnMap_New.class);
//                    intent.putExtra("calling-activity", AppConstant.HomeActivity);
//                    intent.putExtra("lat", String.valueOf(LatLonCellID.currentLat));
//                    intent.putExtra("log", String.valueOf(LatLonCellID.currentLon));
//                    intent.putExtra("hashMapValue", hashMap);
//                    intent.putExtra("from_where", "home");
//                    startActivity(intent);

                } else {
                    Toast.makeText(LoginWithOtp_New.this, getDynamicLanguageValue(getApplicationContext(), "Userdoesnotexistcerned", R.string.Userdoesnotexistcerned), Toast.LENGTH_LONG).show();
                }
                db.close();
            } else {
                new LoginAsyncTask(data).execute();
            }
        } else {
            Toast.makeText(LoginWithOtp_New.this, getDynamicLanguageValue(getApplicationContext(), "InvalidEntry", R.string.InvalidEntry), Toast.LENGTH_LONG).show();
        }
    }


    public boolean isValidEntry() {

        return data.getMailId().toString().length() > 0 && data.getPassword().toString().length() > 0;
    }


    public void forgotPasswordMethod() {

        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.loginforgotpassword_popup, null);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);


        TextView txt_forgotpassword = (TextView) promptsView.findViewById(R.id.txt_forgotpassword);
        TextView txt_emailid = (TextView) promptsView.findViewById(R.id.txt_emailid);

        final EditText edit_emailid = (EditText) promptsView.findViewById(R.id.edit_emailid);
        final Button btn_passwordsubmit = (Button) promptsView.findViewById(R.id.btn_passwordsubmit);

        alertDialogBuilder.setView(promptsView);
        final AlertDialog ad = alertDialogBuilder.show();
        alertDialogBuilder.setCancelable(true);

        UtilFonts.UtilFontsInitialize(this);
        txt_forgotpassword.setTypeface(UtilFonts.FS_Ultra);
        txt_emailid.setTypeface(UtilFonts.KT_Medium);
        edit_emailid.setTypeface(UtilFonts.KT_Regular);

        btn_passwordsubmit.setTypeface(UtilFonts.KT_Bold);


        btn_passwordsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emm = edit_emailid.getText().toString().trim();
                if (emm == null || emm.length() < 5) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.Pleaseentervalidemail), Toast.LENGTH_SHORT).show();
                } else {
                    ad.dismiss();
                    new forgotAsyncTask(emm).execute();
                }

            }
        });


    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.wantotexit))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.Yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //   AppConstant.isLogin = false;

                        finishAffinity();
//                        Intent in = new Intent(LoginWithOtp_New.this, NewHomeScreen.class);
//                        startActivity(in);


                    }
                })
                .setNegativeButton(getResources().getString(R.string.No), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }

    public void gotoHomeScreen(int flag) {

        storeCurrentStateId = AppConstant.stateID;
//        AppConstant.userTypeID = "1";
        String userTypeID = AppConstant.userTypeID;

//        if (userTypeID != null && (userTypeID.equalsIgnoreCase("1") || userTypeID.equalsIgnoreCase("2") || userTypeID.equalsIgnoreCase("18"))) {
////            Intent intent = new Intent(LoginWithOtp_New.this, AdminDashboard_New.class);
//            Intent intent = new Intent(LoginWithOtp_New.this, AdminDashboard_New.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
//            finish();
//
//        } else {
//            Intent intent = new Intent(LoginWithOtp_New.this, MainProfileActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
//            finish();
//
//        }

//        Intent intent = new Intent(LoginWithOtp_New.this, MainProfileActivity.class);
        Intent intent = new Intent(LoginWithOtp_New.this, AdminDashboard_New.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        if (prefs == null) {
            prefs = getSharedPreferences(AppConstant.SHARED_PREFRENCE_NAME, MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = prefs.edit();
        if (flag == 1) {//Farmer Dashboard
            editor.putString(AppConstant.PREFRENCE_KEY_IS_CattleDashBoardNOOFF, "OFF");
        } else if (flag == 2) {//Cattle Dashboard
            editor.putString(AppConstant.PREFRENCE_KEY_IS_CattleDashBoardNOOFF, "ON");
        }
        editor.commit();
        startActivity(intent);
        finish();

    }


    public String syncForApplicationDataSetting(String value) {
        String returnResult = null;
        System.out.println("value:" + value);
        if (value.contains(AppConstant.STATE_ID)) {
            returnResult = syncForStateId();
            return returnResult;

        }
        /*if (value.contains(AppConstant.CROP_INITIAL)) {
            db.open();
            long l = db.deleteAllCropVarietyTableRecord();
            db.close();
            System.out.print("value of long" + l);
            hashMap.clear();
            returnResult = syncForCropInitial();
            System.out.println("crop_initial_called");

        }
        if (value.contains(AppConstant.CROP_ALL_INITIAL)) {
            returnResult = syncForCropAllInitial();
            System.out.println("crop_all_initial called");

        }*/
        return returnResult;
    }

    public String syncForStateId() {

        String message = "NoValue";
        String sendRequest = null;
        sendRequest = AppManager.getInstance().getStateId + AppConstant.state + "/" + latitude + "/" + longitude;
        System.out.println("StateRequest_URL : " + sendRequest);
        String response = AppManager.getInstance().httpRequestGetMethod(AppManager.getInstance().removeSpaceForUrl(sendRequest));
        System.out.println("state id is - --" + response);
        System.out.println("state id is - fcasdfasfasdfasf--");

        if (response == "") {
            return message;
        } else {
            AppConstant.stateID = response;
            message = "Success";
            return message;
        }
    }


    public void alertDialogBox(String msg, int syncCount) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.Yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        new appDataSetting().execute();


                    }
                })
                .setNegativeButton(getResources().getString(R.string.No), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }


    private void checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MULTIPLE_PERMISSIONS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissionsList[], int[] grantResults) {
        switch (requestCode) {
            case 6:
                if (grantResults.length > 0) {
                    String permissionsDenied = "";
                    for (String per : permissionsList) {
                        if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                            permissionsDenied += "\n" + per;

                        }

                    }

                }
                break;


        }
    }

    /*private void isNeedToEdit(String userId){
        Cursor cursor = db.getC
    }*/

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        View view = getCurrentFocus();
        boolean ret = super.dispatchTouchEvent(event);

        if (view instanceof EditText) {
            View w = getCurrentFocus();
            int scrcoords[] = new int[2];
            w.getLocationOnScreen(scrcoords);
            float x = event.getRawX() + w.getLeft() - scrcoords[0];
            float y = event.getRawY() + w.getTop() - scrcoords[1];

            if (event.getAction() == MotionEvent.ACTION_UP
                    && (x < w.getLeft() || x >= w.getRight()
                    || y < w.getTop() || y > w.getBottom())) {

                try {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                }

            }
        }
        return ret;
    }


    public void successForgotPassword() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.Success));
        builder.setMessage(getResources().getString(R.string.PleaseCheck))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.Ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void getScreenSerViceAPI() {
        try {
//            final ProgressDialog dialoug = ProgressDialog.show(LoginWithOtp_New.this, "",
//                    getResources().getString(R.string.LoadingProject), true);
            final TransparentProgressDialog dialoug = new TransparentProgressDialog(LoginWithOtp_New.this,
                    getResources().getString(R.string.Servicedataisloading));
            dialoug.show();
            String URL = AppManager.getInstance().getServicesAPIURL(AppConstant.user_id);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.
                            try {
                                System.out.println("Volley State Response : " + response);
                                response = response.trim();
                                response = response.substring(1, response.length() - 1);
                                response = response.replace("\\", "");
                                ServiceScreenJSON = new JSONObject(response);
                                SharedPreferences prefs1 = getSharedPreferences(AppConstant.SHARED_PREFRENCE_NAME, MODE_PRIVATE);
                                SharedPreferences.Editor editor = prefs1.edit();
                                editor.putString(AppConstant.PREFRENCE_KEY_ServiceScreen, response);
                                editor.apply();

                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                            getAllProjectList();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    dialoug.cancel();
                    System.out.println("Volley Error : " + error);
                }
            });

            int socketTimeout = 60000;//60 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, 0, 0);
            stringRequest.setRetryPolicy(policy);
            // Adding request to volley request queue
            AppController.getInstance().addToRequestQueue(stringRequest);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getAllProjectList() {
        try {
//            final ProgressDialog dialoug = ProgressDialog.show(LoginWithOtp_New.this, "",
//                    getResources().getString(R.string.LoadingProject), true);
            final TransparentProgressDialog dialoug = new TransparentProgressDialog(LoginWithOtp_New.this,
                    getResources().getString(R.string.LoadingProject));
            dialoug.show();
            String URL = AppManager.getInstance().ProjectListURL(AppConstant.user_id);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.
                            System.out.println("Volley State Response : " + response);
                            response = response.trim();
                            response = response.substring(1, response.length() - 1);
                            response = response.replace("\\", "");
                            db.open();
                            if (response != null) {
                                db.getSQLiteDatabase().execSQL("DROP TABLE IF EXISTS " + db.TABLE_Projectlist);
                                db.getSQLiteDatabase().execSQL(db.CREATE_TABLE_Projectlist);
                            }
                            io.requery.android.database.sqlite.SQLiteDatabase SqliteDB = db.getSQLiteDatabase();
                            SqliteDB.beginTransaction();
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = new JSONObject(jsonArray.get(i).toString());
                                    String query = "INSERT INTO " + DBAdapter.TABLE_Projectlist + "(ProjectID,ProjectName) VALUES ('" + obj.get("ID").toString() + "','" + obj.get("Name").toString() + "')";
                                    db.getSQLiteDatabase().execSQL(query);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(context, context.getResources().getString(R.string.FormattingError), Toast.LENGTH_LONG).show();
                            }
                            SqliteDB.setTransactionSuccessful();
                            SqliteDB.endTransaction();
                            db.getClass();

                            dialoug.cancel();
                            gotoHomeScreen(1);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    dialoug.cancel();
                    System.out.println("Volley Error : " + error);
                }
            });

            int socketTimeout = 60000;//60 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, 0, 0);
            stringRequest.setRetryPolicy(policy);
            // Adding request to volley request queue
            AppController.getInstance().addToRequestQueue(stringRequest);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private class LoginAsyncTask extends AsyncTask<Void, Void, String> {
        Register data;
        String result = "";
        TransparentProgressDialog progressDialog;

        public LoginAsyncTask(Register data) {
            this.data = data;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new TransparentProgressDialog(
                    context, context.getResources().getString(
                    R.string.LogginIn));
            progressDialog.setCancelable(false);
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    // cancel AsyncTask
                    cancel(false);
                }
            });
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            String sendRequest = null;
            try {

                sendRequest = getLoginCheckAPI(AppManager.getInstance().removeSpaceForUrl(data.getMailId()), AppManager.getInstance().removeSpaceForUrl(data.getPassword()));
//                sendRequest = AppManager.getInstance().login + AppManager.getInstance().removeSpaceForUrl(data.getMailId()) + "/" + AppManager.getInstance().removeSpaceForUrl(data.getPassword());
                Log.d("sync login data", sendRequest);
                return AppManager.getInstance().httpRequestGetMethod(sendRequest);

            } catch (Exception ex) {
                ex.printStackTrace();

                return null;
            }

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPreExecute();

            try {
                if (result != null &&
                        result.contains("Farmer can't login in Surveyor App")) {
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.FarmercantLogin)
                            , Toast.LENGTH_LONG).show();
                } else if (result != null &&
                        result.contains("Surveyor can't login in SecuFarm App")) {
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.FarmercantLogin)
                            , Toast.LENGTH_LONG).show();
                } else if (result.contains(AppConstant.SERVER_CONNECTION_ERROR)) {
                    db.open();
                    Cursor isAuthenticated = db.isAuthenticated(data.getMailId(), data.getPassword());
                    if (isAuthenticated.getCount() > 0) {
                        isAuthenticated.moveToFirst();

                        prefs = getSharedPreferences(AppConstant.SHARED_PREFRENCE_NAME, MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();

                        AppConstant.user_id = isAuthenticated.getString(isAuthenticated.getColumnIndex(DBAdapter.USER_ID));
                        AppConstant.mobile_no = isAuthenticated.getString(isAuthenticated.getColumnIndex(DBAdapter.MOBILE_NO));
                        AppConstant.visible_Name = isAuthenticated.getString(isAuthenticated.getColumnIndex(DBAdapter.VISIBLE_NAME));
                        String sendingStatus = isAuthenticated.getString(isAuthenticated.getColumnIndex(DBAdapter.SENDING_STATUS));

                        data.setUser_id(AppConstant.user_id);
                        data.setVisibleName(AppConstant.visible_Name);
                        data.save(db, sendingStatus);

                        editor.putString(AppConstant.PREFRENCE_KEY_USER_ID, AppConstant.user_id);
                        editor.putString(AppConstant.PREFRENCE_KEY_VISIBLE_NAME, AppConstant.visible_Name);
                        editor.putString(AppConstant.PREFRENCE_KEY_MOBILE, AppConstant.mobile_no);
                        editor.putBoolean(AppConstant.PREFRENCE_KEY_ISLOGIN, true);
                        editor.apply();

                        AppConstant.isLogin = true;

                    } else {
                        Toast.makeText(getBaseContext(), AppConstant.SERVER_CONNECTION_ERROR, Toast.LENGTH_LONG).show();
                    }
                    db.close();
                } else if (result.contains("[]")) {
//                    This toast will show after both Username and CattleDasboard Api are invalid
                    checkCattleDashboardLogin(USERNAME);
                } else {
                    //  SharedPreferences prefs = getSharedPreferences("user_detail", MODE_PRIVATE);
                    // SharedPreferences.Editor edit = prefs.edit();

                    result = result.replace("\\", "");
                    result = result.replace("\"[", "[");
                    result = result.replace("]\"", "]");
                    result = result.replace("\"{", "{");
                    result = result.replace("}\"", "}");

                    JSONArray jArray = new JSONArray(result);
                    for (int i = 0; i < jArray.length(); i++) {
                        if (i == 0) {
                            JSONObject jObject = jArray.getJSONObject(i);

                            SharedPreferences prefs1 = getSharedPreferences(AppConstant.SHARED_PREFRENCE_NAME, MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefs1.edit();

                            AppConstant.user_id = jObject.getString("UserID");
                            AppConstant.visible_Name = jObject.getString("VisibleName");
                            if (AppConstant.visible_Name == null ||
                                    AppConstant.visible_Name.equalsIgnoreCase("null")
                            ) {
                                AppConstant.visible_Name = "";
                            }
                            AppConstant.mobile_no = jObject.getString("Device_ID");
                            if (AppConstant.mobile_no == null || AppConstant.mobile_no.length() < 7) {
                                if (jObject.has("PhNo") && jObject.getString("PhNo") != null && !jObject.getString("PhNo").equalsIgnoreCase("null")) {
                                    AppConstant.mobile_no = jObject.getString("PhNo");
                                } else {
                                    AppConstant.mobile_no = setMobileNo(AppManager.getInstance().removeSpaceForUrl(data.getMailId()));
//                                    AppConstant.mobile_no = "1234567890";
                                }
                            }
                            AppConstant.role = jObject.getString("Role");

                            String str = "";
                            if (jObject.has("DashboardURL")) {
                                str = jObject.getString("DashboardURL");
                            } else {
                                str = "";
                            }

                            if (AppConstant.mobile_no == null || AppConstant.mobile_no.length() == 10) {
                                AppConstant.mobile_no = setMobileNo(AppManager.getInstance().removeSpaceForUrl(data.getMailId()));
//                                AppConstant.mobile_no = "1234567890";
                            }

                            AppConstant.dashboard = str;
                            data.setMobileNo(AppConstant.mobile_no);

                            Log.v("mobile_no", AppConstant.mobile_no + "");
                            Log.v("dashboard_url", AppConstant.dashboard + "");

                            data.setUser_id(jObject.getString("UserID"));
                            data.setVisibleName(jObject.getString("VisibleName"));
                            data.setCreatedDateTime(jObject.getString("UserSince"));

                            if (jObject.has("ClientDisplayName")) {
                                AppConstant.client_name = jObject.getString("ClientDisplayName");
                                AppConstant.client_name = jObject.getString("ClientName");
                            } else {
                                AppConstant.client_name = "";
                            }


                            if (jObject.has("showsubdistrict")) {
                                AppConstant.isSubdistrict = jObject.getString("showsubdistrict");

                            } else {
                                AppConstant.isSubdistrict = "No";
                            }

                            if (jObject.has("ClientName")) {
                                AppConstant.client_name = jObject.getString("ClientName");
                            }

                            if (jObject.has("BaseUserID")) {
                                String aaaaa = jObject.getString("BaseUserID");
                                if (aaaaa != null && aaaaa.length() > 4) {
                                    AppConstant.baseID = aaaaa;
                                }
                            }


                            if (jObject.has("ClientCropId")) {
                                AppConstant.selected_cropId = jObject.getString("ClientCropId");
                            } else {
                                AppConstant.selected_cropId = "";
                            }

                            if (jObject.has("ClientCrop")) {
                                AppConstant.selected_crop = jObject.getString("ClientCrop");
                            } else {
                                AppConstant.selected_crop = "";
                            }


                            if (jObject.has("BaseAdminID")) {
                                String aaaaa = jObject.getString("BaseAdminID");
                                if (aaaaa != null && aaaaa.length() > 4) {
                                    AppConstant.baseID = aaaaa;

                                }
                            }


                            if (jObject.has("UserTypeID")) {
                                String aaaaabbb = jObject.getString("UserTypeID");
                                AppConstant.userTypeID = aaaaabbb;
                                if (aaaaabbb != null && aaaaabbb.equalsIgnoreCase("12")) {
                                    AppConstant.isAgronimist = "yes";
                                } else {
                                    AppConstant.isAgronimist = "no";
                                }
                            }

                            //New Add for TUbeWell AIS Available
                            if (jObject.has("AISAvailable")) {
                                String AISAvailable = jObject.getString("AISAvailable");
                                AISAvailable_Tubewell = AISAvailable;
                            }


                            db.open();
                            data.save(db, DBAdapter.SENT);
                            db.close();
                            editor.putString(AppConstant.PREFRENCE_KEY_USER_ID, AppConstant.user_id);
                            editor.putString(AppConstant.PREFRENCE_KEY_VISIBLE_NAME, AppConstant.visible_Name);
                            editor.putString(AppConstant.PREFRENCE_KEY_MOBILE, AppConstant.mobile_no);
                            editor.putBoolean(AppConstant.PREFRENCE_KEY_ISLOGIN, true);
                            editor.putString(AppConstant.PREFRENCE_KEY_ROLE, AppConstant.role);
                            editor.putString(AppConstant.PREFRENCE_KEY_DASHBOARD, str);
                            editor.putString(AppConstant.PREFRENCE_KEY_CLIENT_NAME, AppConstant.client_name);
                            editor.putString(AppConstant.PREFRENCE_KEY_BASE_USER_ID, AppConstant.baseID);
                            editor.putString(AppConstant.PREFRENCE_KEY_IS_CROP, AppConstant.selected_crop);
                            editor.putString(AppConstant.PREFRENCE_KEY_IS_CROP_ID, AppConstant.selected_cropId);
                            editor.putString(AppConstant.PREFRENCE_KEY_IS_SUB_DISTRICT, AppConstant.isSubdistrict);
                            editor.putString(AppConstant.PREFRENCE_KEY_IS_AGRONOMIST, AppConstant.isAgronimist);
                            editor.putString(AppConstant.PREFRENCE_KEY_IS_USERTYPEID, AppConstant.userTypeID);
                            //Add Tubewell Key
                            editor.putString(AppConstant.AISAvailable_TubewellKey, AISAvailable_Tubewell);
                            //  editor.putString("Device_Id", device_id);
                            //  editor.putString("Device_Id", device_id);
                            editor.apply();
                            Log.v("aaaaaa", "" + AppConstant.user_id);
                            AppConstant.isLogin = true;

                            //Add New
                            setCattleDashboardMobileNo(USERNAME);

                            new getFarmDetailAsyncTask().execute();
                        }

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getBaseContext(), getResources().getString(R.string.ServerError), Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getBaseContext(), getResources().getString(R.string.ServerError), Toast.LENGTH_LONG).show();
            }

            progressDialog.dismiss();

        }
    }

    private class forgotAsyncTask extends AsyncTask<Void, Void, String> {
        String email_f;
        String result = "";
        TransparentProgressDialog progressDialog;

        public forgotAsyncTask(String emai) {
            this.email_f = emai;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new TransparentProgressDialog(
                    context, context.getResources().getString(
                    R.string.getfarmdetails));
            progressDialog.setCancelable(false);
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    // cancel AsyncTask
                    cancel(false);
                }
            });
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            String sendRequest = null;
            try {

                sendRequest = AppManager.getInstance().forgot_password + email_f;
                Log.d("sync forgot data", sendRequest);
                return AppManager.getInstance().httpRequestGetMethod(sendRequest);


            } catch (Exception ex) {
                ex.printStackTrace();

                return null;
            }

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPreExecute();
            if (result.contains(AppConstant.SERVER_CONNECTION_ERROR)) {

                Toast.makeText(getBaseContext(), AppConstant.SERVER_CONNECTION_ERROR, Toast.LENGTH_LONG).show();

            } else if (result.contains("NoData")) {

                Toast.makeText(getBaseContext(), getResources().getString(R.string.emailiddoesnotexist), Toast.LENGTH_LONG).show();

            } else {
                successForgotPassword();
            }

            progressDialog.dismiss();

        }
    }

    //////////////////////////////////////////////////////////////////////////
    @SuppressLint("StaticFieldLeak")
    private class getFarmDetailAsyncTask extends AsyncTask<Void, Void, String> {
        SignInData data;
        String result = "";
        TransparentProgressDialog progressDialog;

        public getFarmDetailAsyncTask() {
            this.data = data;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new TransparentProgressDialog(
                    context, context.getResources().getString(
                    R.string.getfarmdetails));
            progressDialog.setCancelable(false);
            progressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                                                @Override
                                                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                                                    return false;
                                                }
                                            }
            );
        }

        @Override
        protected String doInBackground(Void... params) {
            String sendRequest = null;
            try {
                sendRequest = AppManager.getInstance().getFarmList + AppConstant.user_id;
                Log.d("get farm url", sendRequest);
                String response = AppManager.getInstance().httpRequestGetMethod(sendRequest);
                System.out.println("farm_details :" + response);
                return response;


            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null; //show network problem
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPreExecute();
            try {
                if (response != null) {
                    if (response.contains("No Farms")) {
                        System.out.println("Farm not available");
                        Toast.makeText(getBaseContext(), getResources().getString(R.string.Farmnotavailable), Toast.LENGTH_LONG).show();
//                        gotoHomeScreen();

                    } else {
                        AllFarmDetail addFarmDetail;

                        db.open();
                        //  db.deleteAllFarmDetailTable();
                        System.out.println("farm detail response " + response);

                        JSONArray jArray = new JSONArray(AppManager.getInstance().placeSpaceIntoString(response));
                        System.out.println("farm detail response " + jArray.length());
                        if (jArray.length() > 0) {
                            int deleteCount = db.db.delete(DBAdapter.DATABASE_TABLE_ALL_FARM_DETAIL, DBAdapter.SENDING_STATUS + " = '" + DBAdapter.SENT + "'", null);
                            int deleteCount1 = db.db.delete(DBAdapter.TABLE_QUERY_CROP, DBAdapter.SENDING_STATUS + " = '" + DBAdapter.SENT + "'", null);
                            System.out.println("deleteCount : " + deleteCount + " deleteCount1 : " + deleteCount1);
                        }
                        for (int i = 0; i < jArray.length(); i++) {

                            JSONObject jsonObject = jArray.getJSONObject(i);
                            addFarmDetail = new AllFarmDetail(jsonObject);
                            addFarmDetail.setUserId(AppConstant.user_id);
                            String farmId = addFarmDetail.getFarmId();
                            String farmName = addFarmDetail.getFarmName();
                            String farmerName = addFarmDetail.getFarmerName();
                            String farmerNumber = addFarmDetail.getFarmerPhone();
                            String concern = addFarmDetail.getConcern();
                            Long l = db.insertAllFarmDetail(addFarmDetail, DBAdapter.SENT);
                            if (jsonObject.has("CropInfo")) {
                                JSONArray corpInfoArray = jsonObject.getJSONArray("CropInfo");

                                for (int j = 0; j < corpInfoArray.length(); j++) {
                                    JSONObject cropJsonObject = corpInfoArray.getJSONObject(j);
                                    CropQueryData data = new CropQueryData();
                                    data.setFarmId(farmId);
                                    data.setFarmName(farmName);


                                    data.setYourCencern(concern);
                                    data.setCropID(cropJsonObject.isNull("CropID") ? "" : cropJsonObject.getString("CropID"));
                                    data.setCrop(cropJsonObject.isNull("CropName") ? "" : cropJsonObject.getString("CropName"));
                                    String variety = cropJsonObject.isNull("Variety") ? "" : cropJsonObject.getString("Variety");
                                    data.setVariety(variety.replaceAll("%20", " "));
                                    data.setBasalDoseN(cropJsonObject.isNull("N") ? "0" : cropJsonObject.getString("N"));
                                    data.setBasalDoseP(cropJsonObject.isNull("P") ? "0" : cropJsonObject.getString("P"));
                                    data.setBasalDoseK(cropJsonObject.isNull("K") ? "0" : cropJsonObject.getString("K"));
                                    data.setSowPeriodForm(cropJsonObject.isNull("SowDate") ? "" : cropJsonObject.getString("SowDate"));
                                    data.setOtherNutrition(cropJsonObject.isNull("OtherNutrient") ? "" : cropJsonObject.getString("OtherNutrient"));
                                    data.setBesalDoseApply(cropJsonObject.isNull("BasalDoseApply") ? "" : cropJsonObject.getString("BasalDoseApply"));
                                    long inserted = data.insert(db, DBAdapter.SENT);
                                    System.out.println("database return value=" + l);
                                }
                            }

                        }
                        db.close();
//                        new getStateDistrict().execute();
//                        gotoHomeScreen();

                    }
                } else {
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.ServerError), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();

                System.out.println("catch block Pls Try again");
            } catch (Exception e) {
                e.printStackTrace();

                System.out.println("catch block Pls Try again");
            }
            progressDialog.dismiss();
//            getAllProjectList();
            getScreenSerViceAPI();

        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    class appDataSetting extends AsyncTask<Void, Void, String> {
        boolean firstRound = true;

        //  ProgressDialog dialoug;

        public appDataSetting() {
        }

        @Override
        protected void onPreExecute() {
            //  dialoug = ProgressDialog.show(NewHomeScreen.this, "" + syncMsg, " Please wait...", true);
        }

        @Override
        protected String doInBackground(Void... params) {

            String result = syncForApplicationDataSetting(syncFor);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                String msgDisplayInDailogBox = null;
                //   dialoug.dismiss();
                if (!result.contains("Success")) {
                    if (syncCount == 1) {

                        msgDisplayInDailogBox = getResources().getString(R.string.Stateidsync);

                    }
                    if (syncCount == 2) {

                        msgDisplayInDailogBox = getResources().getString(R.string.Cropinitialsync);

                    }
                    if (syncCount == 3) {

                        msgDisplayInDailogBox = getResources().getString(R.string.Cropallinitialsync);

                    }
                    syncMsg = "Syncronizing for " + NewHomeScreen.syncFor;
                    alertDialogBox(msgDisplayInDailogBox, syncCount);
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (storeCurrentStateId != null && storeCurrentStateId.contains(AppConstant.stateID)) {


                Log.v("checking", storeCurrentStateId + "");

            } else {

                if (syncCount < AppConstant.syncArray.length) {

                    NewHomeScreen.syncFor = AppConstant.syncArray[syncCount];
                    System.out.println("value in syncFor " + NewHomeScreen.syncFor);
                    syncMsg = "Syncronizing for " + NewHomeScreen.syncFor;
                    syncCount++;
                    new appDataSetting().execute();

                }

            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (db != null) {
            db = new DBAdapter(this);
        }
        Log.d("onResume Method", "onResume Method called");
        setLanguages();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (db != null) {
            db = new DBAdapter(this);
        }
        Log.d("OnPause Method", "OnPause Method called");
        setScreenTracking(this, db, SN_LoginWithOtp, UID);
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (db != null) {
            db = new DBAdapter(this);
        }
        Log.d("onStop Method", "onStop Method called");
        setScreenTracking(this, db, SN_LoginWithOtp, UID);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private void setIdDefine() {

        ll_password = (LinearLayout) findViewById(R.id.ll_password);
        txt_login = (TextView) findViewById(R.id.txt_login);
        txt_username = (TextView) findViewById(R.id.txt_username);
        edit_username = (EditText) findViewById(R.id.edit_username);

        radiogroup = (RadioGroup) findViewById(R.id.radiogroup);
        rb_WithoutPassword = (RadioButton) findViewById(R.id.rb_WithoutPassword);
        rb_WithPassword = (RadioButton) findViewById(R.id.rb_WithPassword);

        txt_password = (TextView) findViewById(R.id.txt_password);
        edit_password = (EditText) findViewById(R.id.edit_password);

        checkbox_savepassword = (CheckBox) findViewById(R.id.checkbox_savepassword);

        showpassword = (ImageView) findViewById(R.id.showpassword);
        hidepassword = (ImageView) findViewById(R.id.hidepassword);

        btn_login = (Button) findViewById(R.id.btn_login);
        btn_forgotpassword = (Button) findViewById(R.id.btn_forgotpassword);
        btn_GetOTP = (Button) findViewById(R.id.btn_GetOTP);
        btn_UserRegistration = (Button) findViewById(R.id.btn_UserRegistration);
    }


    public void setLanguages() {
        setDynamicLanguage(this, txt_login, "login", R.string.login);
        setDynamicLanguage(this, txt_username, "MobileNumber", R.string.MobileNumber);
        setDynamicLanguage(this, txt_password, "EnterOTP", R.string.EnterOTP);
        setDynamicLanguage(this, btn_login, "login", R.string.login);
        setDynamicLanguage(this, btn_forgotpassword, "ForgotPassword", R.string.ForgotPassword);
        setDynamicLanguage(this, btn_GetOTP, "GetOTP", R.string.GetOTP);
        setDynamicLanguage(this, btn_UserRegistration, "SignUp", R.string.SignUp);


        setFontsStyleTxt(this, txt_login, 2);
        setFontsStyleTxt(this, txt_username, 5);
        setFontsStyleTxt(this, txt_password, 5);
        setFontsStyle(this, edit_username);
        setFontsStyle(this, edit_password);
        setFontsStyle(this, btn_login);
        setFontsStyle(this, btn_forgotpassword);
        setFontsStyle(this, btn_GetOTP);
        setFontsStyle(this, btn_UserRegistration);

        setFontsStyle(this, rb_WithoutPassword);
        setFontsStyle(this, rb_WithPassword);
        setFontsStyle(this, checkbox_savepassword);

    }

    public String setMobileNo(String UserName) {
        String value = "1234567890";
        if (UserName != null) {
            if (checkmobileno(UserName)) {
                value = UserName;
            }
        }
        return value;
    }


    public void loadData() {
        dialoug = new TransparentProgressDialog(this, getResources().getString(R.string.Loadingstate));
        dialoug.show();

//        final ProgressDialog dialoug = ProgressDialog.show(context, "",
//                context.getResources().getString(R.string.Loadingstate), true);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppManager.getInstance().AllStatesListURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // Display the first 500 characters of the response string.
                            System.out.println("Volley State Response : " + response);

                            response = response.trim();
                            response = response.substring(1, response.length() - 1);
                            response = response.replace("\\", "");

                            db.open();
                            db.getSQLiteDatabase().execSQL("DROP TABLE IF EXISTS " + db.TABLE_STATE);
                            db.getSQLiteDatabase().execSQL(db.CREATE_TABLE_STATE);

                            io.requery.android.database.sqlite.SQLiteDatabase SqliteDB = db.getSQLiteDatabase();
                            SqliteDB.beginTransaction();
                            try {
                                JSONArray jsonArray = new JSONArray(response);

                                String query = "INSERT INTO " + DBAdapter.TABLE_STATE + "(" + DBAdapter.STATE_ID + "," + DBAdapter.STATE_NAME + ") VALUES (?,?)";
                                SQLiteStatement stmt = SqliteDB.compileStatement(query);

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject territoryElementArray = jsonArray.getJSONObject(i);

                                    String stateId = String.valueOf((int) territoryElementArray.getDouble("StateID"));

                                    stmt.bindString(1, stateId);
                                    stmt.bindString(2, territoryElementArray.getString("StateName"));
                                    stmt.execute();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(LoginWithOtp_New.this, getResources().getString(R.string.FormattingError), Toast.LENGTH_LONG).show();
                            }
                            SqliteDB.setTransactionSuccessful();
                            SqliteDB.endTransaction();
                            db.getClass();
                            dialoug.cancel();
                            getCropVariety();
                        } catch (Exception e) {
                            dialoug.cancel();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialoug.cancel();
                System.out.println("Volley Error : " + error);
            }
        });

        int socketTimeout = 60000;//60 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);

        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(stringRequest);
    }


    private void getCropVariety() {
        dialoug = new TransparentProgressDialog(
                this, getResources().getString(
                R.string.Loadingcrop));
        dialoug.show();
//        final ProgressDialog dialoug = ProgressDialog.show(context, "",
//                context.getResources().getString(R.string.Loadingcrop), true);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppManager.getInstance().CropsListURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // Display the first 500 characters of the response string.
                            System.out.println("Volley State Response : " + response);

                            response = response.trim();
                            response = response.substring(1, response.length() - 1);
                            response = response.replace("\\", "");

                            db.open();
                            db.getSQLiteDatabase().execSQL("DROP TABLE IF EXISTS " + db.TABLE_CROP_VARIETY);
                            db.getSQLiteDatabase().execSQL(db.CREATE_CROP_VATIETY);

                            io.requery.android.database.sqlite.SQLiteDatabase SqliteDB = db.getSQLiteDatabase();
                            SqliteDB.beginTransaction();
                            try {

                                JSONArray jsonArray = new JSONArray(response);

                                String query = "INSERT INTO " + DBAdapter.TABLE_CROP_VARIETY + "(" + DBAdapter.STATE_ID + "," + DBAdapter.CROP_ID + "," + DBAdapter.CROP + "," + DBAdapter.VARIETY + ") VALUES (?,?,?,?)";
                                SQLiteStatement stmt = SqliteDB.compileStatement(query);

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONArray territoryElementArray = jsonArray.getJSONArray(i);

                                    stmt.bindString(1, territoryElementArray.get(3).toString());
                                    stmt.bindString(2, territoryElementArray.get(0).toString());
                                    stmt.bindString(3, territoryElementArray.get(1).toString());
                                    stmt.bindString(4, territoryElementArray.get(2).toString());
                                    stmt.execute();
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(context, context.getResources().getString(R.string.FormattingError), Toast.LENGTH_LONG).show();
                            }
                            SqliteDB.setTransactionSuccessful();
                            SqliteDB.endTransaction();
//                        db.close();
                            dialoug.cancel();
                            getStateDistrict();
                        } catch (Exception e) {
                            dialoug.cancel();
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialoug.cancel();
                Toast.makeText(context, context.getResources().getString(R.string.Couldnotconnect), Toast.LENGTH_LONG).show();
                System.out.println("Volley Error : " + error);
            }
        });
        int socketTimeout = 60000;//60 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);

        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    public void getStateDistrict() {
        dialoug = new TransparentProgressDialog(
                context, context.getResources().getString(
                R.string.Loadingstate));
        dialoug.show();
//        final ProgressDialog dialoug = ProgressDialog.show(context, "",
//                context.getResources().getString(R.string.Loadingstate), true);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppManager.getInstance().StateDistrictURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        System.out.println("Volley State Response : " + response);
                        try {
                            response = response.trim();
                            response = response.substring(1, response.length() - 1);
                            response = response.replace("\\", "");

                            db.open();
                            db.getSQLiteDatabase().execSQL("DROP TABLE IF EXISTS " + db.TABLE_StateDistrict);
                            db.getSQLiteDatabase().execSQL(db.CREATE_StateDistrict);

                            try {
                                if (response != null) {

                                    JSONObject jsonObject = new JSONObject(response);
                                    JSONArray jsonArray = jsonObject.getJSONArray("DT");
                                    db.insertStateDistrict(jsonArray);
                                    db.close();
//                                getAllProjectList();
                                } else {
                                    Toast.makeText(context, context.getResources().getString(R.string.ServerError), Toast.LENGTH_LONG).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(context, context.getResources().getString(R.string.FormattingError), Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        dialoug.cancel();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialoug.cancel();
                System.out.println("Volley Error : " + error);
            }
        });

        int socketTimeout = 60000;//60 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);

        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    public void checkCattleDashboardLogin(String MobileNumber) {
        dialog = new TransparentProgressDialog(
                LoginWithOtp_New.this, getResources().getString(R.string.LoginCattleDashboardData));
        dialog.setCancelable(false);
//        String strToken = "", strPhoneno = "";
//        String strToken = "234567", strPhoneno = "7093660861";
        String requestString = getCattleOwnerID(strToken, MobileNumber);

        CattleDash_OwnerIDList = new ArrayList<>();
        mRequestStartTime = System.currentTimeMillis();
        apiService.getCattleOwnerID(strToken, MobileNumber).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResponseObserver<retrofit2.Response<List<LoginCheckCattleDashboardResponse>>>(compositeDisposable) {
                    @Override
                    public void onNetworkError(Throwable e) {
                        // calculate the duration in milliseconds
                        int seconds = getAPIimeResponseinSecond(mRequestStartTime);
//                        Toast.makeText(getApplicationContext(), String.valueOf(seconds), Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                        Log.v("wvqevqevqe", e.getMessage() + " " + e.toString());
                        SaveLocalFile(db, LoginWithOtp_New.this, SN_LoginWithOtp, requestString, "", getSMS(e.getMessage()), "" + seconds, AppConstant.farm_id, "Error");

                        Toast.makeText(LoginWithOtp_New.this, getDynamicLanguageValue(getApplicationContext(), "UnregisteredMobileNo", R.string.UnregisteredMobileNo), Toast.LENGTH_LONG).show();
                        setCattleDashboardMobileNo(null);
                    }

                    @Override
                    public void onServerError(Throwable e, int code) { // calculate the duration in milliseconds
                        int seconds = getAPIimeResponseinSecond(mRequestStartTime);
//                        Toast.makeText(getApplicationContext(), String.valueOf(seconds), Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                        SaveLocalFile(db, LoginWithOtp_New.this, SN_LoginWithOtp, requestString, "", "Server API Error", "" + seconds, AppConstant.farm_id, "Error");
                        Toast.makeText(LoginWithOtp_New.this, getDynamicLanguageValue(getApplicationContext(), "UnregisteredMobileNo", R.string.UnregisteredMobileNo), Toast.LENGTH_LONG).show();
                        setCattleDashboardMobileNo(null);
                    }

                    @Override
                    public void onNext(retrofit2.Response<List<LoginCheckCattleDashboardResponse>> Response) {
                        // calculate the duration in milliseconds
                        int seconds = getAPIimeResponseinSecond(mRequestStartTime);
//                        Toast.makeText(getApplicationContext(), String.valueOf(seconds), Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                        CattleDash_OwnerIDList = Response.body();

                        if (seconds > 3) {
                            SaveLocalFile(db, LoginWithOtp_New.this, SN_LoginWithOtp, requestString, CattleDash_OwnerIDList.toString(), "", "" + seconds, AppConstant.farm_id, "Working");
                        }
//                        if (CattleDash_OwnerIDList.size() > 0) {
                        if (CattleDash_OwnerIDList != null && CattleDash_OwnerIDList.size() > 0) {
                            setCattleDashboardMobileNo(USERNAME);
                            gotoHomeScreen(2);
                        } else {
                            Toast.makeText(LoginWithOtp_New.this, getDynamicLanguageValue(getApplicationContext(), "UnregisteredMobileNo", R.string.UnregisteredMobileNo), Toast.LENGTH_LONG).show();
                        }
                    }

                });
    }

    public void setCattleDashboardMobileNo(String Value) {
        SharedPreferences prefs1 = getSharedPreferences(AppConstant.SHARED_PREFRENCE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs1.edit();
        editor.putString(AppConstant.PREFRENCE_KEY_CattleDashboardMobileKey, Value);
        editor.apply();
        strPhoneno = Value;
        AppConstant.PREFRENCE_KEY_CattleDashboardMobileValue = prefs.getString(AppConstant.PREFRENCE_KEY_CattleDashboardMobileKey, Value);
    }

    public void setVisibleOTPLogin(int flag) {
        try {
            switch (flag) {//first time
                case 1:
                    ll_password.setVisibility(View.GONE);
                    btn_login.setVisibility(View.GONE);
                    btn_GetOTP.setVisibility(View.VISIBLE);
                    btn_UserRegistration.setVisibility(View.VISIBLE);
                    break;
                case 2://get OTP click
                    ll_password.setVisibility(View.VISIBLE);
                    btn_login.setVisibility(View.VISIBLE);
                    btn_GetOTP.setVisibility(View.GONE);
                    btn_UserRegistration.setVisibility(View.GONE);
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getOTPMethod(final String mobile) {
        String usID = AppConstant.user_id;

        final Dialog dialoug2 = ProgressDialog.show(this, "",
                getDynamicLanguageValue(getApplicationContext(), "OTPissending", R.string.OTPissending), true);

        String ssss = getLoginOTP(mobile);
//        String ssss = "https://myfarminfo.com/yfirest.svc/Clients/GetOtp/" + mobile;
        String url = AppManager.getInstance().removeSpaceForUrl(ssss);
        Log.v("kkkkk", url);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialoug2.cancel();

                        response = response.trim();
                        try {
                            if (response != null && response.contains("Sucess")) {
//                            setVisibleOTPLogin(2);
                                response = response.replace("\\", "");
                                response = response.replace("\"", "");
                                response = response.replace("Sucess:", "");
                                LoginOTPPopup(response);
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialoug2.cancel();
                System.out.println("Volley Error : " + error);

            }
        });

        int socketTimeout = 60000;//60 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, 0, 0);
        stringRequest.setRetryPolicy(policy);
        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    public void LoginOTPPopup(String OTP) {

        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.mobileotp, null);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        TextView txt_EnterOTP = (TextView) promptsView.findViewById(R.id.txt_EnterOTP);
        TextView txtPleasenetertheotp = (TextView) promptsView.findViewById(R.id.txtPleasenetertheotp);
        EditText edit_OTP = (EditText) promptsView.findViewById(R.id.edit_OTP);

        final Button btn_passwordsubmit = (Button) promptsView.findViewById(R.id.btn_passwordsubmit);


        setDynamicLanguage(this, txt_EnterOTP, "EnterOTP", R.string.EnterOTP);
        setDynamicLanguage(this, txtPleasenetertheotp, "Pleasenetertheotp", R.string.Pleasenetertheotp);
        setDynamicLanguage(this, btn_passwordsubmit, "Submit", R.string.Submit);

        setFontsStyleTxt(this, txt_EnterOTP, 2);
        setFontsStyleTxt(this, txtPleasenetertheotp, 6);
        setFontsStyle(this, btn_passwordsubmit);

        alertDialogBuilder.setView(promptsView);
        final AlertDialog ad = alertDialogBuilder.show();
        alertDialogBuilder.setCancelable(true);

        btn_passwordsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String getotp = edit_OTP.getText().toString().trim();
                    if (getotp != null && getotp.equalsIgnoreCase(OTP)) {
                        ad.dismiss();
//                    USERNAME = edit_username.getText().toString();
                        PASSWORD = getotp;
                        data.setMailId(USERNAME);
                        data.setPassword(PASSWORD);
                        SharedPreferences.Editor editor = prefs.edit();
                        if (checkbox_savepassword.isChecked()) {
                            System.out.println("isCHecked");
                            editor.putString(AppConstant.PREFRENCE_KEY_EMAIL, USERNAME);
                            editor.putString(AppConstant.PREFRENCE_KEY_PASS, PASSWORD);
                            editor.putBoolean(AppConstant.PREFRENCE_KEY_ISSAVED, true);
                            editor.putBoolean(AppConstant.PREFRENCE_KEY_ISLOGIN, false);
                            editor.commit();
                        } else {
                            editor.putString(AppConstant.PREFRENCE_KEY_EMAIL, "");
                            editor.putString(AppConstant.PREFRENCE_KEY_PASS, "");
                            editor.putBoolean(AppConstant.PREFRENCE_KEY_ISSAVED, false);
                            editor.putBoolean(AppConstant.PREFRENCE_KEY_ISLOGIN, false);
                            editor.commit();
                        }
                        new LoginAsyncTask(data).execute();
                    } else {
                        getDynamicLanguageToast(getApplicationContext(), "Pleasenetervalidotp", R.string.Pleasenetervalidotp);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

    }

}

