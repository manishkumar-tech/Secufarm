package com.weather.risk.mfi.myfarminfo.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.admin.DevicePolicyManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.speech.tts.TextToSpeech;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;
import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.adapter.CropPopupAdapterNew;
import com.weather.risk.mfi.myfarminfo.adapter.DashboarSMSListAdapter;
import com.weather.risk.mfi.myfarminfo.adapter.IrrigationMngtAdapter;
import com.weather.risk.mfi.myfarminfo.adapter.MainProfileActivity_PestDisease_Adapter;
import com.weather.risk.mfi.myfarminfo.adapter.NewPopupAdapter;
import com.weather.risk.mfi.myfarminfo.adapter.NotificationCustomAdapter;
import com.weather.risk.mfi.myfarminfo.adapter.NutritionMngtAdapter;
import com.weather.risk.mfi.myfarminfo.adapter.PestDiseaseAdapterNew;
import com.weather.risk.mfi.myfarminfo.adapter.PostHarvMngtAdapter;
import com.weather.risk.mfi.myfarminfo.adapter.PostHarvMngtImagelistAdapter;
import com.weather.risk.mfi.myfarminfo.adapter.WeedManagementAdapter;
import com.weather.risk.mfi.myfarminfo.adapter.YeildImprovementAdapter;
import com.weather.risk.mfi.myfarminfo.bean.CropStatusBean;
import com.weather.risk.mfi.myfarminfo.bean.DashboardSMS;
import com.weather.risk.mfi.myfarminfo.bean.IrrrigationMngt;
import com.weather.risk.mfi.myfarminfo.bean.IrrrigationMngtResponse;
import com.weather.risk.mfi.myfarminfo.bean.KeyValueBean;
import com.weather.risk.mfi.myfarminfo.bean.NotificationBean;
import com.weather.risk.mfi.myfarminfo.bean.NutritionMngt;
import com.weather.risk.mfi.myfarminfo.bean.NutritionMngtResponse;
import com.weather.risk.mfi.myfarminfo.bean.POPUpdate;
import com.weather.risk.mfi.myfarminfo.bean.PostHarvMngt;
import com.weather.risk.mfi.myfarminfo.bean.PostHarvMngtResponse;
import com.weather.risk.mfi.myfarminfo.bean.WeedMngt;
import com.weather.risk.mfi.myfarminfo.bean.WeedMngtRequest;
import com.weather.risk.mfi.myfarminfo.bean.WeedMngtResponse;
import com.weather.risk.mfi.myfarminfo.bean.YeildImprovementResponse;
import com.weather.risk.mfi.myfarminfo.cattledashboard.CattleDashboards;
import com.weather.risk.mfi.myfarminfo.cattledashboard.CattleObject;
import com.weather.risk.mfi.myfarminfo.database.DBAdapter;
import com.weather.risk.mfi.myfarminfo.databinding.OpenbannerpopupBinding;
import com.weather.risk.mfi.myfarminfo.entities.AllFarmDetail;
import com.weather.risk.mfi.myfarminfo.entities.CropQueryData;
import com.weather.risk.mfi.myfarminfo.entities.DataBean;
import com.weather.risk.mfi.myfarminfo.entities.SignInData;
import com.weather.risk.mfi.myfarminfo.firebasenotification.NotificationCountSMS;
import com.weather.risk.mfi.myfarminfo.firebasenotification.NotificationData;
import com.weather.risk.mfi.myfarminfo.firebasenotification.NotificationPOPDetailsDialog;
import com.weather.risk.mfi.myfarminfo.groundwater.GroundWaterForecasting;
import com.weather.risk.mfi.myfarminfo.home.AppController;
import com.weather.risk.mfi.myfarminfo.home.Calculators;
import com.weather.risk.mfi.myfarminfo.home.EditFarmActivity;
import com.weather.risk.mfi.myfarminfo.mapfragments.LatLonCellID;
import com.weather.risk.mfi.myfarminfo.marketplace.AddFarmOnMap_New;
import com.weather.risk.mfi.myfarminfo.marketplace.NewProductActivity;
import com.weather.risk.mfi.myfarminfo.menu.IconizedMenu;
import com.weather.risk.mfi.myfarminfo.pest_disease.CropBean;
import com.weather.risk.mfi.myfarminfo.policyregistration.PolicyFarmList;
import com.weather.risk.mfi.myfarminfo.policyregistration.PolicyList;
import com.weather.risk.mfi.myfarminfo.policyregistration.UserFarmResponse;
import com.weather.risk.mfi.myfarminfo.retofitservices.ApiService;
import com.weather.risk.mfi.myfarminfo.retofitservices.ResponseObserver;
import com.weather.risk.mfi.myfarminfo.retrofitmodelnew.Dashboard_FarmerResponse;
import com.weather.risk.mfi.myfarminfo.retrofitmodelnew.SMSLst;
import com.weather.risk.mfi.myfarminfo.services.CallRecordingService;
import com.weather.risk.mfi.myfarminfo.services.DeviceAdminDemo;
import com.weather.risk.mfi.myfarminfo.services.TService;
import com.weather.risk.mfi.myfarminfo.utils.AppConstant;
import com.weather.risk.mfi.myfarminfo.utils.AppManager;
import com.weather.risk.mfi.myfarminfo.utils.TransparentProgressDialog;
import com.weather.risk.mfi.myfarminfo.utils.UtilFonts;
import com.weather.risk.mfi.myfarminfo.utils.Utility;
import com.weather.risk.mfi.myfarminfo.youtubevideostream.YoutubeVideoRecyclerView;
import com.weather.risk.mfi.myfarminfo.youtubevideostream.YoutubeVideoStreamActivity;
import com.weather.risk.mfi.myfarminfo.youtubevideostream.YoutubeWebview;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
//import retrofit2.Response;

import static com.weather.risk.mfi.myfarminfo.database.DBAdapter.DATABASE_CREATED_SCREENTRACKING;
import static com.weather.risk.mfi.myfarminfo.database.DBAdapter.FarmIDs;
import static com.weather.risk.mfi.myfarminfo.database.DBAdapter.TABLE_SCREENTRACKING;
import static com.weather.risk.mfi.myfarminfo.database.DBAdapter.TABLE_YoutubeVideoDateTime;
import static com.weather.risk.mfi.myfarminfo.database.DBAdapter.UserID;
import static com.weather.risk.mfi.myfarminfo.database.DBAdapter.UserIDs;
import static com.weather.risk.mfi.myfarminfo.firebasenotification.UserTokenSubmit.checkTodayTokenKeyUpdatedOrNot;
import static com.weather.risk.mfi.myfarminfo.firebasenotification.UserTokenSubmit.uploadUserTkenDeviceID;
import static com.weather.risk.mfi.myfarminfo.logging.LocalFileWriter.SaveLocalFile;
import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.AISAvailable_Tubewell;
import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.AndroidDevice_IMEI;
import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.SN_MainProfileActivity;
import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.SN_MainProfileActivity_PestandDisease;
import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.SN_NewHomeScreen;
import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.SN_PackagePractices;
import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.SelectedLanguageValue;
import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.ServiceScreenJSON;
import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.checkScreenComingfromLanguageSelectionorNot;
import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.farmList;
import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.selectedFarmLat;
import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.selectedFarmLong;
import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.userfarmList;
import static com.weather.risk.mfi.myfarminfo.utils.AppManager.NOGPSDialog;
import static com.weather.risk.mfi.myfarminfo.utils.AppManager.WeedMngtImageURL;
import static com.weather.risk.mfi.myfarminfo.utils.AppManager.getCropNameSowingDate;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getAPIimeResponseinSecond;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getAppVersion;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getDynamicLanguageToast;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getDynamicLanguageValue;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getSMS;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getUIDforScreenTracking;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getdateYYYYMMDD;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.gettime;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setCustomSearchableSpinner;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setDynamicLanguage;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setDynamicLanguagevale;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setFontsStyle;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setFontsStyleTxt;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setScreenTracking;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setToastComingsoon;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setToastCropIDNotFound;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setToastNodataavailable;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setToastPleaseselectyour;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setToastServerError;


//public class MainProfileActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
public class MainProfileActivity extends AppCompatActivity {

    public static final int ACCESS_STORAGE = 112;
    ArrayList<String> str = new ArrayList<String>();
    ArrayList<String> strFarmId = new ArrayList<String>();
    LatLng latLng1, latLng2;
    ArrayList<FarmData> mandiArray = new ArrayList<>();
    ArrayList<KeyValueBean> pestDataList = new ArrayList<KeyValueBean>();
    ArrayList<KeyValueBean> pestDataList_check = new ArrayList<KeyValueBean>();
    ArrayList<String> pestImageList = new ArrayList<String>();
    ArrayList<String> pestManagementList = new ArrayList<String>();
    ArrayList<ArrayList<String>> pestImageListWhole = new ArrayList<ArrayList<String>>();
    ArrayList<ArrayList<String>> pestImageListWhole_check = new ArrayList<ArrayList<String>>();
    ArrayList<ArrayList<String>> pestManagementListWhole = new ArrayList<ArrayList<String>>();
    ArrayList<ArrayList<String>> pestManagementListWhole_check = new ArrayList<ArrayList<String>>();
    ArrayList<KeyValueBean> irrigationDataList = new ArrayList<KeyValueBean>();
    ArrayList<KeyValueBean> nextDataList = new ArrayList<KeyValueBean>();
    ArrayList<CropStatusBean> cropDataList = new ArrayList<CropStatusBean>();
    ArrayList<POPUpdate> POPUpdates = new ArrayList<POPUpdate>();
    ArrayList<LatLng> points = null;
    DBAdapter db;
    Double latitude = 0.0;
    Double longitude = 0.0;
    CustomSearchableSpinner header_chooseFarmSpinner;
    //  TextView addNewFarm;
    TextView txt_farmerselected, txt_Cropsms, txt_NextStepsms1, txt_NextStepsms2, txt_NextStepsms3, txt_NextStepsms4, txt_weatherforcastsms,
            txt_irrigationadvisorysms, txt_farmScore;
    CardView cardview_POP, cardview_Crop, cardview_Crop_new, cardview_NextStep, cardview_weatherforcast, cardview_PestDisease,
            cardview_irrigationadvisory, cardview_FarmScore;
    LinearLayout ll_weather, ll_Plantdoctor, ll_soildoctor, ll_CropInform, ll_Popupdate, MarketPlace_tab, PolicyRegistration_tab, Tubewell_tab,
            HealthVault_tab, ll_Groundwaterforecadting_tab;

    TransparentProgressDialog dialog;
    SharedPreferences prefs,prefs_once ;


    JSONArray pestname = null;
    RecyclerView pestdiseasePopup, recycleview_SMSList;
    ArrayList<ArrayList<String>> managementList_pop = null;
    int selectecposition = 0;
    Dialog dialogs;
    //Sound Display
    private TextToSpeech mTTS;
    static int audiospeech_flag = 0;

    //Notification Bell
    RelativeLayout notificationbell;
    ImageView imgnotification;
    TextView tv_badge;
    PopupWindow mPopupWindow;

    ImageView cardvieemergency, cardviewlanguage;
    String UID = "", POPHeading = "", SelectedFarmerNameID = "", CropConditionCheck = "", NextStep_POPName;


    TextView txt_Profilename, txt_ProfileCrop;
    ImageView edit_farmerID, editFram, logoutBTN, imgvw_setting, imageview_weather, imageview_Plantdoctor, imageview_soildoctor,
            imageview_CropInform, imageview_Popupdate, imgvw_MarketPlace_tab, imgvw_PolicyRegistration_tab, imgvw_Tubewell_tab, cardview_sowingdate,
            imgvw_HealthVault_tab, imgvw_Groundwaterforecadting_tab;
    TextView txt_weather, txt_Plantdoctor, txt_soildoctor, txt_CropInform, txt_Popupdate, txt_MarketPlace, txt_PolicyRegistration,
            txt_Tubewell, txt_HealthVault, txt_Groundwaterforecadting;
    TextView txt_POPHeading, txt_POPTitle, txt_POPsms, POPUpdates_btn, txt_CropHeading, txt_CropHeading_new, txt_NextStepHeading, txt_weatherforcastHeading,
            txt_PestDiseaseHeading, txt_irrigationadvisoryHeading, txt_FarmScore, txt_AdvisoryMessages;
    RecyclerView recycler_pestdesease;
    Button audio_sound_btn_POPUpdates, audio_sound_btn_POPUpdates_mute, audio_sound_btn_cropstatus, audio_sound_btn_nextstep, audio_sound_btn_weatherforecast,
            audio_sound_btn_pestdiease, audio_sound_btn_irrigation, audio_sound_btn_FarmScore;
    Button audio_sound_btn_cropstatus_mute, audio_sound_btn_nextstep_mute, audio_sound_btn_weatherforecast_mute,
            audio_sound_btn_pestdiease_mute, audio_sound_btn_irrigation_mute, audio_sound_btn_FarmScore_mute, imageview_cropthumbup, imageview_cropthumbup_new;
    String contour = null, lat = null, lon = null;
    ImageView imgeview_Youtube, imageviewBanner,imageviewLogo;
    boolean checkfirsttime = false;
    private ApiService apiService;
    private CompositeDisposable compositeDisposable;
    boolean checkLanguage = false;
    String CropUrl, ResponseCropNameSOwingDate = "";

    ImageView btn_left, btn_right;
    HorizontalScrollView hsv;

    TableRow tblrw_progressbar;
    ProgressBar progressbarFarmScore;
    TextView txt_FarmScoreValue;

    CardView cardview_CropInform, cardview_Popupdate, cardview_Plantdoctor,
            cardview_weather, cardview_soildoctor, cardview_MarketPlace, cardview_PolicyRegistration,
            cardview_Tubewell, cardview_Groundwaterforecadting;

    LinearLayout ll_AdvisoryMessages;
    public static ArrayList<DashboardSMS> dashboardSMS = new ArrayList<DashboardSMS>();

    String dashboardFirstTime;
    String dateForCropAd;
    String MessageTypeFinal = "";
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.new_main_page);
//        setContentView(R.layout.mainpage_new);
        setContentView(R.layout.mainprofileact_dashboard);
        dialogs = new Dialog(this, R.style.DialogSlideAnim);
        mPopupWindow = new PopupWindow();

//        apiService = AppController.getInstance().getApiServiceCheckout();
        apiService = AppController.getInstance().getApiServiceGson();
        compositeDisposable = new CompositeDisposable();
        setIdDefine();

        db = new DBAdapter(this);
        checkPermissionStorage();
        points = new ArrayList<LatLng>();

        if (!AppManager.getInstance().isLocationServicesAvailable(this))
            NOGPSDialog(this);

        //https://stackoverflow.com/questions/16079486/scrolling-a-horizontalscrollview-by-clicking-buttons-on-its-sides
        btn_left.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                hsv.scrollTo((int) hsv.getScrollX() - 100, (int) hsv.getScrollY());
            }
        });
        btn_right.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                hsv.scrollTo((int) hsv.getScrollX() + 100, (int) hsv.getScrollY());
            }
        });


        setAudioSpeechButton(audio_sound_btn_cropstatus, 1);
        setAudioSpeechButton(audio_sound_btn_nextstep, 2);
        setAudioSpeechButton(audio_sound_btn_weatherforecast, 3);
        setAudioSpeechButton(audio_sound_btn_pestdiease, 4);
        setAudioSpeechButton(audio_sound_btn_irrigation, 5);
        setAudioSpeechButton(audio_sound_btn_POPUpdates, 7);
        setAudioSpeechButton(audio_sound_btn_FarmScore, 8);

        setMuteAudioSpeechButton(audio_sound_btn_cropstatus_mute, 1);
        setMuteAudioSpeechButton(audio_sound_btn_nextstep_mute, 2);
        setMuteAudioSpeechButton(audio_sound_btn_weatherforecast_mute, 3);
        setMuteAudioSpeechButton(audio_sound_btn_pestdiease_mute, 4);
        setMuteAudioSpeechButton(audio_sound_btn_irrigation_mute, 5);
        setMuteAudioSpeechButton(audio_sound_btn_POPUpdates_mute, 7);
        setMuteAudioSpeechButton(audio_sound_btn_FarmScore_mute, 8);


        if (prefs == null) {
            prefs = getSharedPreferences(AppConstant.SHARED_PREFRENCE_NAME, MODE_PRIVATE);
        }

        if (prefs_once == null) {
            prefs_once = getSharedPreferences(AppConstant.SHARED_PREFRENCE_ONCE, MODE_PRIVATE);
        }



       dashboardFirstTime =   prefs_once.getString(getResources().getString(R.string.dashboard_pref_key),"");


        if(dashboardFirstTime.equals("")){
            dateForCropAd = "1-1-1";
            prefs_once.edit().putString(getResources().getString(R.string.dashboard_pref_key),"1").commit();
        }else{

            db.open();

           String queryForMaxDate =  "SELECT OutDate FROM sms_data where userId='" + AppConstant.user_id + "'"+" ORDER BY OutDate desc LIMIT 1 ";

        ArrayList<HashMap<String,String>> list =   db.getDynamicTableValue(queryForMaxDate);

           dateForCropAd = list.get(0).get("OutDate").split("T")[0];


        }


        System.out.println("manishtestdateforcropad"+dateForCropAd);

      //  prefs = getSharedPreferences(AppConstant.SHARED_PREFRENCE_NAME, MODE_PRIVATE);

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

        AppConstant.user_id = prefs.getString(AppConstant.PREFRENCE_KEY_USER_ID, null);
        System.out.println("Got User id Home : " + AppConstant.user_id);

        boolean isLogin = prefs.getBoolean(AppConstant.PREFRENCE_KEY_ISLOGIN, false);
        AppConstant.isLogin = isLogin;

        if (isLogin) {
            AppConstant.user_id = prefs.getString(AppConstant.PREFRENCE_KEY_USER_ID, null);
            System.out.println("Got User id Home : " + AppConstant.user_id);
            AppConstant.visible_Name = prefs.getString(AppConstant.PREFRENCE_KEY_VISIBLE_NAME, "Demo");
            AppConstant.mobile_no = prefs.getString(AppConstant.PREFRENCE_KEY_MOBILE, "1234567890");
            if (AppConstant.visible_Name == null ||
                    AppConstant.visible_Name.equalsIgnoreCase("null")
            ) {
                AppConstant.visible_Name = "";
            }


//            Intent in = new Intent(getApplicationContext(), LoginWithOtp_New.class);
//            startActivity(in);
//            finish();


        } else {
            AppConstant.user_id = prefs.getString(AppConstant.PREFRENCE_KEY_USER_ID, null);
            System.out.println("Got User id Home : " + AppConstant.user_id);
            AppConstant.visible_Name = prefs.getString(AppConstant.PREFRENCE_KEY_VISIBLE_NAME, "");
            AppConstant.mobile_no = prefs.getString(AppConstant.PREFRENCE_KEY_MOBILE, "");
            if (AppConstant.visible_Name == null ||
                    AppConstant.visible_Name.equalsIgnoreCase("null")
            ) {
                AppConstant.visible_Name = "";
            }
        }

        pestImageList = new ArrayList<String>();
        pestManagementList = new ArrayList<String>();

        cardview_POP.setVisibility(View.GONE);
        cardview_Crop.setVisibility(View.GONE);
        cardview_Crop_new.setVisibility(View.GONE);
        cardview_NextStep.setVisibility(View.GONE);
        cardview_weatherforcast.setVisibility(View.GONE);
        cardview_PestDisease.setVisibility(View.GONE);
        cardview_irrigationadvisory.setVisibility(View.GONE);
        cardview_FarmScore.setVisibility(View.GONE);
        recycleview_SMSList.setVisibility(View.GONE);
        ll_AdvisoryMessages.setVisibility(View.GONE);

        getAllFarmName();

        if (AppConstant.visible_Name != null && AppConstant.visible_Name.length() > 0) {
            String UserName = AppConstant.visible_Name.replace("%20", " ");
//            UserName = getResources().getString(R.string.WelcomeMr) + " " + UserName;
            txt_Profilename.setText(UserName);
        }
        setCropname();

        setNotificationCount();
//        if (AppConstant.NotificationCountNo_badge > 0) {
//            tv_badge.setVisibility(View.VISIBLE);
//            tv_badge.setText(String.valueOf(AppConstant.NotificationCountNo_badge));
//        } else {
//            tv_badge.setVisibility(View.GONE);
//        }

        if (AppConstant.CheckRegisterDeviceTokenKey == null || AppConstant.CheckRegisterDeviceTokenKey.equalsIgnoreCase("PendingUpload")
                || checkTodayTokenKeyUpdatedOrNot(this) == false) {
            getJsonUploadforTokenUpdate();
        }

//        if (checkTodayTokenKeyUpdatedOrNot(this) == false) {
//            getJsonUploadforTokenUpdate();
//        }

        String tokenID = null, Messgae = "", notftytype = "", StepID = "", FarmIDs = "", Title = "", NotifImageURL = "";
        //for firebase notification
        Bundle bundle = getIntent().getExtras();
//        String val = String.valueOf(bundle.get(EXTRA_MESSAGE));

        if (bundle != null) {
            //bundle must contain all info sent in "data" field of the notification
            try {
                if (bundle.size() > 0) {
                    //handle the data message here
                    Messgae = bundle.getString("Messgae");
                    notftytype = bundle.getString("notftytype");
                    StepID = bundle.getString("StepID");
                    FarmIDs = bundle.getString("FarmID");
                    Title = bundle.getString("Title");
                    MessageTypeFinal = bundle.getString("MessageTypeFinal");
                    tokenID = bundle.getString("tokenID");
                    NotifImageURL = bundle.getString("NotifImageURL");
//                    if (tokenID != null && tokenID.equalsIgnoreCase(AppConstant.RegisterDeviceTokenKey)) {
                    if (Messgae != null) {
                        NotificationCountSMS.setNotificationValueData(this, Messgae, notftytype, StepID, FarmIDs, Title, tokenID, NotifImageURL);

                        ArrayList<NotificationBean> mDataset = new ArrayList<NotificationBean>();
                        NotificationBean setdata = new NotificationBean();
                        setdata.setMessgae(Messgae);
                        setdata.setNotftytype(notftytype);
                        setdata.setStepID(StepID);
                        setdata.setFarmID(FarmIDs);
                        setdata.setTitle(Title);
                        setdata.setDateTime(Utility.getdate() + " " + gettime());
                        setdata.setNotifImageURL(NotifImageURL);
                        setdata.setDateTimeHHMMSS(Utility.getdate() + " " + gettime());

                        mDataset.add(setdata);

                        if (mDataset != null && mDataset.size() > 0) {
                            String FarmID = mDataset.get(0).getFarmID();
                            if (FarmID != null || FarmID == null) {
                                Intent in = new Intent(getApplicationContext(), NotificationPOPDetailsDialog.class);
                                in.putExtra("mDataset", (ArrayList<NotificationBean>) mDataset);
                                in.putExtra("position", 0);
                                startActivity(in);
                            } else {
                                setToastPleaseselectyour(MainProfileActivity.this);
                            }
                        }
                    }
                } else {
                    //Add new Concept
                    if (checkScreenComingfromLanguageSelectionorNot) {
                        NotificationFisrtimeAutoPopup();
                        checkScreenComingfromLanguageSelectionorNot = false;
                    }
                }
            } catch (Exception ee) {
                ee.printStackTrace();
            }
        } else {
            if (checkScreenComingfromLanguageSelectionorNot) {
                NotificationFisrtimeAutoPopup();
                checkScreenComingfromLanguageSelectionorNot = false;
            }
        }


        mTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = mTTS.setLanguage(new Locale("en", "IN"));

                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "Language not supported");
                    } else {
                        try {
                            switch (audiospeech_flag) {
                                case 1:
                                    audio_sound_btn_cropstatus.setEnabled(true);
                                    break;
                                case 2:
                                    audio_sound_btn_nextstep.setEnabled(true);
                                    break;
                                case 3:
                                    audio_sound_btn_weatherforecast.setEnabled(true);
                                    break;
                                case 4:
                                    audio_sound_btn_pestdiease.setEnabled(true);
                                    break;
                                case 5:
                                    audio_sound_btn_irrigation.setEnabled(true);
                                    break;
                                case 6:
                                    audio_sound_btn_POPUpdates.setEnabled(true);
                                    break;
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                    }
                } else {
                    Log.e("TTS", "Initialization failed");
                }
            }
        });

        edit_farmerID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImagePopup();
            }
        });
        cardview_sowingdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AppConstant.farm_id != null) {
                    new FarmdetailsAsynctask().execute();
                } else {
                    setToastPleaseselectyour(MainProfileActivity.this);
                }
            }
        });
        cardviewlanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkLanguage = true;
                Intent intent = new Intent(MainProfileActivity.this, POPUpLanguageSelection.class);
                intent.putExtra("ActivityName", "MainProfileActivity");
                startActivity(intent);
            }
        });

        editFram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (contour != null && contour.length() > 10) { // For Geo Tagging
                Intent in = new Intent(getApplicationContext(), EditFarmActivity.class);
                startActivity(in);
//                } else {
//                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.Thefarmernocontour), Toast.LENGTH_SHORT).show();
//                }
            }
        });

        logoutBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accountAlert();
            }
        });

        ll_Plantdoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPlantDoctorCall();
            }
        });
        txt_Plantdoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPlantDoctorCall();
            }
        });
        imageview_Plantdoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPlantDoctorCall();
            }
        });


        cardvieemergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setEmergencyCall();
            }
        });
        HealthVault_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setHealthVaultCall();
            }
        });
        imgvw_HealthVault_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setHealthVaultCall();
            }
        });
        txt_HealthVault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setHealthVaultCall();
            }
        });

        MarketPlace_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setMarketPlace();
            }
        });
        imgvw_MarketPlace_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setMarketPlace();
            }
        });
        txt_MarketPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setMarketPlace();
            }
        });

//Policy Registration
        PolicyRegistration_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setPolicyRegistration(1);
            }
        });

        setPolicyRegistration(2);

        imgvw_PolicyRegistration_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPolicyRegistration(1);
            }
        });
        txt_PolicyRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPolicyRegistration(1);
            }
        });


        //GroundWater
        ll_Groundwaterforecadting_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GroundWater();
            }
        });
        imgvw_Groundwaterforecadting_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GroundWater();
            }
        });
        txt_Groundwaterforecadting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GroundWater();
            }
        });


        ll_Popupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPOPCall("");
            }
        });
        imageview_Popupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                setPOPCall("OnlyCurrentStatus_false");
                setPOPCall("");
            }
        });
        txt_Popupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                setPOPCall("OnlyCurrentStatus_false");
                setPOPCall("");
            }
        });
        cardview_POP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (POPHeading != null && POPHeading.length() > 0)
//                    setPOPCall("OnlyCurrentStatus_true");
                    setPOPCall(POPHeading);
            }
        });
        POPUpdates_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (POPHeading != null && POPHeading.length() > 0)
//                    setPOPCall("OnlyCurrentStatus_true");
                    setPOPCall(POPHeading);
            }
        });

        cardview_Tubewell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTubewellCall();
            }
        });
        imgvw_Tubewell_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTubewellCall();
            }
        });
        txt_Tubewell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTubewellCall();
            }
        });


        ll_soildoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSoilDoctorCall();
            }
        });
        imageview_soildoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSoilDoctorCall();
            }
        });
        txt_soildoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSoilDoctorCall();
            }
        });

        ll_weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setWeatherCall();
            }
        });
        imageview_weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setWeatherCall();
            }
        });
        txt_weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setWeatherCall();
            }
        });

        ll_CropInform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCropInformationCall();
            }
        });
        imageview_CropInform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCropInformationCall();
            }
        });
        txt_CropInform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCropInformationCall();
            }
        });

        cardview_Crop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cropDataList.size() > 0) {
                    //    popupMethod(cropDataList, "Crop Status");
                    cropPopupMethod(cropDataList, "Crop Status");
                }
            }
        });


        cardview_PestDisease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("asfasfasfas", "" + pestDataList.size() + "==" + pestManagementList.size() + "==" + pestImageList.size());
                if (pestDataList.size() > 0) {
                    //Herojit Add
//                    popupPestMethod(pestDataList, pestImageListWhole, pestManagementListWhole, "Pest and Disease");
                    popupPestMethod(pestDataList_check, pestImageListWhole_check, pestManagementListWhole_check, "Pest and Disease");
                    //Herojit Change
                } else {
                    getDynamicLanguageToast(getApplicationContext(), "norecordfarm", R.string.norecordfarm);
                }
            }
        });
        cardview_irrigationadvisory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (irrigationDataList.size() > 0) {
                    popupMethod(irrigationDataList, "Irrigation Advisory");
                }
            }
        });

        cardview_NextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nextDataList.size() > 0) {
                    //remove the POPNext already show records
//                    ArrayList<KeyValueBean> newnextDataList = new ArrayList<KeyValueBean>();
//                    newnextDataList = nextDataList;
//                    if (NextStep_POPName != null && NextStep_POPName.length() > 0 && !NextStep_POPName.equalsIgnoreCase("")) {
//                        nextDataList = new ArrayList<>();
//                        for (int i = 0; i < newnextDataList.size(); i++) {
//                            KeyValueBean nextstep = newnextDataList.get(i);
//                            String Name = nextstep.getValue();
//                            if (!NextStep_POPName.equalsIgnoreCase(Name)) {
//                                nextDataList.add(nextstep);
//                            }
//                        }
//                    }
//                    //Cloese remove function
//                    if (nextDataList.size() > 0) {
//                        popupMethod(nextDataList, "Next Step");
//                    } else if (newnextDataList.size() > 0) {// this will call  having onlhy one record
//                        popupMethod(newnextDataList, "Next Step");
//                    }

                    popupMethod(nextDataList, "Next Step");
                }
            }
        });

        cardview_weatherforcast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setWeatherCall();
            }
        });

        //  addNewFarm = (TextView) findViewById(R.id.add_new_location);
        latitude = LatLonCellID.lat;
        longitude = LatLonCellID.lon;

        tv_badge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setNotification(view);
            }
        });
        notificationbell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setNotification(view);
            }
        });
        imgnotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setNotification(view);
            }
        });
        imgeview_Youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent in = new Intent(getApplicationContext(), YoutubeVideoStreamActivity.class);
//                if (VideoID != null) {
//                    Intent in = new Intent(getApplicationContext(), YoutubeWebview.class);
//                    in.putExtra("YoutubeVideoURL", VideoID);
//                    startActivity(in);
//                } else {
//                    Toast.makeText(getApplicationContext(), "No URL", Toast.LENGTH_SHORT).show();
//                }



                if (YoutubeVideolist != null && YoutubeVideolist.size() > 0) {
                    if (YoutubeVideolist != null && YoutubeVideolist.size() > 1) {
                        Intent in = new Intent(getApplicationContext(), YoutubeVideoRecyclerView.class);
                        in.putExtra("YoutubeVideoURL", VideoID);
                        in.putExtra("YoutubeVideolist", YoutubeVideolist);
                        startActivity(in);
                    } else if (YoutubeVideolist != null && YoutubeVideolist.size() == 1) {
                        Intent in = new Intent(getApplicationContext(), YoutubeWebview.class);
                        in.putExtra("YoutubeVideoURL", VideoID);
                        startActivity(in);
                    }
                } else {
                    getDynamicLanguageToast(getApplicationContext(), "ThereisnoYouTube", R.string.ThereisnoYouTube);
                }

// manish comment

//                if ((VideoID != null && VideoID.length() > 2) || (YoutubeVideolist != null && YoutubeVideolist.size() > 0)) {
//                    if (YoutubeVideolist != null && YoutubeVideolist.size() > 0) {
//                        Intent in = new Intent(getApplicationContext(), YoutubeVideoRecyclerView.class);
//                        in.putExtra("YoutubeVideoURL", VideoID);
//                        in.putExtra("YoutubeVideolist", YoutubeVideolist);
//                        startActivity(in);
//                    } else if (VideoID != null && VideoID.length() > 2) {
//                        Intent in = new Intent(getApplicationContext(), YoutubeWebview.class);
//                        in.putExtra("YoutubeVideoURL", VideoID);
//                        startActivity(in);
//                    }
//                } else {
//                    getDynamicLanguageToast(getApplicationContext(), "ThereisnoYouTube", R.string.ThereisnoYouTube);
//                }
            }
        });

        //ScreenTracking
        UID = getUIDforScreenTracking();
        setScreenTracking(this, db, SN_MainProfileActivity, UID);

//        imgeview_Youtube.setVisibility(View.GONE);
        //YoutubeLiveVideo
        new getYouTubeLiveVideoAsyncTask(AppConstant.user_id).execute();

        //Youtubevideo call update
       checktodayyoutubevideoexistornot();

        uploadScreenTracking();

        imgvw_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopUpSetting(v);
            }
        });
        setServiceScreens();

    }


    public void getAllFarmName() {
        try {

            System.out.println("getAllFarmCalled");
            str = new ArrayList<String>();
            strFarmId = new ArrayList<String>();

//        if ((AppConstant.userTypeID != null && !AppConstant.userTypeID.equalsIgnoreCase("5"))) {
            if (AppConstant.userTypeID != null) {
//                str.add("Select farm");
                str.add(getDynamicLanguageValue(getApplicationContext(), "Select", R.string.Select));
//                str.add(getResources().getString(R.string.SelectFarm));
                strFarmId.add("0");
            }

            Log.v("userTypeID", AppConstant.userTypeID + "");
            db.open();
            Cursor c = db.getallFarmName(AppConstant.user_id);
//        ArrayList<HashMap<String, String>> FarmIDName = new ArrayList<>();
//        String sql = "select farmName,farmId from allFarmDetail where userId= '" + AppConstant.uswer_id + "' order by farmName asc";
//        FarmIDName = db.getDynamicTableValue(sql);
            int position = 0;
            String FarmID = AppConstant.farm_id;

            if (c.moveToFirst()) {
                do {
//                str.add(c.getString(0).toString() + " - " + c.getString(1).toString());
                    str.add(c.getString(0).toString());
                    strFarmId.add(c.getString(1).toString());
                    try {
                        if (selectecposition == 0) {
                            if (FarmID != null && !FarmID.equalsIgnoreCase("0")) {
                                if (FarmID.equalsIgnoreCase(c.getString(1).toString())) {
                                    selectecposition = position;//23354 22803
                                }
                            } else {
                                if (strFarmId.size() > 2) {
                                    selectecposition = 1;
                                }
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    position++;
                } while (c.moveToNext());
            }
            db.close();

            if (selectecposition == 0 && strFarmId.size() > 1) {
                selectecposition = 1;
            }
            //First time
            if (strFarmId.size() > 1) {
                setFarmIDSelection(selectecposition);
            }
        } catch (IndexOutOfBoundsException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        //dff
    }

    public void setFarmIDSelection(int position) {
        try {
            AppConstant.farm_id = strFarmId.get(position);
            AppConstant.selected_farm = str.get(position);
            SelectedFarmerNameID = AppConstant.selected_farm;

            txt_farmerselected.setText(SelectedFarmerNameID);

            if (AppConstant.farm_id != null && !AppConstant.farm_id.equalsIgnoreCase("0")) {
                loadProfileData(AppConstant.farm_id);
            } else {
                isFarmAvailable();
            }
        } catch (IndexOutOfBoundsException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void cropMgmtPopupMethod() {

        final Dialog dialog = new Dialog(this, R.style.DialogSlideAnim);

        //  final Dialog dialog = new Dialog(getActivity());

        dialog.setCanceledOnTouchOutside(true);
        Window window = dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);


        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.dimAmount = 0.5f;

        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        // wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        // Include dialog.xml file
        dialog.setContentView(R.layout.crop_management_popup);

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, 1300);
        /*} else {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        }*/

        //LinearLayout vulnerability = (LinearLayout) dialog.findViewById(R.id.vulnerability_popup);
        LinearLayout diseaseForecast = (LinearLayout) dialog.findViewById(R.id.pest_disease);
        LinearLayout ll_Nutrition = (LinearLayout) dialog.findViewById(R.id.ll_Nutrition);

        ImageView imgview_Nutrition = (ImageView) dialog.findViewById(R.id.imgview_Nutrition);

        LinearLayout ll_DiseaseDiagnosis = (LinearLayout) dialog.findViewById(R.id.ll_DiseaseDiagnosis);
        ImageView imgview_DiseaseDiagnosis = (ImageView) dialog.findViewById(R.id.imgview_DiseaseDiagnosis);
        TextView txt_DiseaseDiagnosis = (TextView) dialog.findViewById(R.id.txt_DiseaseDiagnosis);
        TextView txt_Nutrition = (TextView) dialog.findViewById(R.id.txt_Nutrition);

        TextView txt_WeedManagement = (TextView) dialog.findViewById(R.id.txt_WeedManagement);
        ImageView imgview_WeedManagement = (ImageView) dialog.findViewById(R.id.imgview_WeedManagement);
        LinearLayout ll_WeedManagements = (LinearLayout) dialog.findViewById(R.id.ll_WeedManagements);

        TextView txt_PostHarvestManagement = (TextView) dialog.findViewById(R.id.txt_PostHarvestManagement);
        ImageView imgview_PostHarvestManagement = (ImageView) dialog.findViewById(R.id.imgview_PostHarvestManagement);
        LinearLayout ll_PostHarvestManagement = (LinearLayout) dialog.findViewById(R.id.ll_PostHarvestManagement);

        TextView txt_IrrigationManagement = (TextView) dialog.findViewById(R.id.txt_IrrigationManagement);
        ImageView imgview_IrrigationManagement = (ImageView) dialog.findViewById(R.id.imgview_IrrigationManagement);
        LinearLayout ll_IrrigationManagement = (LinearLayout) dialog.findViewById(R.id.ll_IrrigationManagement);

        LinearLayout ll_Calculator = (LinearLayout) dialog.findViewById(R.id.ll_Calculator);
        ImageView imgview_Calculator = (ImageView) dialog.findViewById(R.id.imgview_Calculator);
        TextView txt_Calculator = (TextView) dialog.findViewById(R.id.txt_Calculator);

        TextView txt_Mandi = (TextView) dialog.findViewById(R.id.txt_Mandi);
        ImageView imgview_Mandi = (ImageView) dialog.findViewById(R.id.imgview_Mandi);
        LinearLayout ll_Mandi = (LinearLayout) dialog.findViewById(R.id.ll_Mandi);

        TextView txt_YeildImprovement = (TextView) dialog.findViewById(R.id.txt_YeildImprovement);
        ImageView imgview_YeildImprovement = (ImageView) dialog.findViewById(R.id.imgview_YeildImprovement);
        LinearLayout ll_YeildImprovement = (LinearLayout) dialog.findViewById(R.id.ll_YeildImprovement);

        TextView txt_IrrigationScheduler = (TextView) dialog.findViewById(R.id.txt_IrrigationScheduler);
        ImageView imgview_IrrigationScheduler = (ImageView) dialog.findViewById(R.id.imgview_IrrigationScheduler);
        LinearLayout ll_IrrigationScheduler = (LinearLayout) dialog.findViewById(R.id.ll_IrrigationScheduler);

//        TextView txt_Tubewell = (TextView) dialog.findViewById(R.id.txt_Tubewell);
//        ImageView imgview_Tubewell = (ImageView) dialog.findViewById(R.id.imgview_Tubewell);
//        LinearLayout ll_Tubewell = (LinearLayout) dialog.findViewById(R.id.ll_Tubewell);

        TextView txt_Guideliness = (TextView) dialog.findViewById(R.id.txt_Guideliness);
        ImageView imgview_Guideliness = (ImageView) dialog.findViewById(R.id.imgview_Guideliness);
        LinearLayout ll_Guideliness = (LinearLayout) dialog.findViewById(R.id.ll_Guideliness);

        Button btn_cross = (Button) dialog.findViewById(R.id.btn_cross);

        TextView txt_CI_heading = (TextView) dialog.findViewById(R.id.txt_CI_heading);
        TextView txt_CI_PestandDisease = (TextView) dialog.findViewById(R.id.txt_CI_PestandDisease);

//        txt_Tubewell.setTypeface(UtilFonts.KT_Medium);

        setDynamicLanguage(this, txt_CI_heading, "CropManagement", R.string.CropManagement);
        setDynamicLanguage(this, txt_CI_PestandDisease, "PestandDisease", R.string.PestandDisease);
        setDynamicLanguage(this, txt_Calculator, "Calculator", R.string.Calculator);
        setDynamicLanguage(this, txt_DiseaseDiagnosis, "DiseaseDiagnosis", R.string.DiseaseDiagnosis);
        setDynamicLanguage(this, txt_Nutrition, "NutritionManagement", R.string.NutritionManagement);
        setDynamicLanguage(this, txt_WeedManagement, "WeedManagements", R.string.WeedManagements);
        setDynamicLanguage(this, txt_PostHarvestManagement, "PostHarvestManagement", R.string.PostHarvestManagement);
        setDynamicLanguage(this, txt_IrrigationManagement, "IrrigationManagement", R.string.IrrigationManagement);
        setDynamicLanguage(this, txt_Mandi, "Mandi", R.string.Mandi);
        setDynamicLanguage(this, txt_YeildImprovement, "YeildImprovement", R.string.YeildImprovement);
        setDynamicLanguage(this, txt_IrrigationScheduler, "IrrigationScheduler", R.string.IrrigationScheduler);
        setDynamicLanguage(this, txt_Guideliness, "Guideliness", R.string.Guideliness);

        setFontsStyleTxt(this, txt_CI_heading, 2);
        setFontsStyleTxt(this, txt_CI_PestandDisease, 5);
        setFontsStyleTxt(this, txt_Calculator, 5);
        setFontsStyleTxt(this, txt_DiseaseDiagnosis, 5);
        setFontsStyleTxt(this, txt_Nutrition, 5);
        setFontsStyleTxt(this, txt_WeedManagement, 5);
        setFontsStyleTxt(this, txt_PostHarvestManagement, 5);
        setFontsStyleTxt(this, txt_IrrigationManagement, 5);
        setFontsStyleTxt(this, txt_Mandi, 5);
        setFontsStyleTxt(this, txt_YeildImprovement, 5);
        setFontsStyleTxt(this, txt_IrrigationScheduler, 5);
        setFontsStyleTxt(this, txt_Guideliness, 5);

//        ll_DiseaseDiagnosis.setVisibility(View.GONE);
//        String CROPNAME = AppConstant.selected_crop;
//        if (CROPNAME != null && CROPNAME.length() > 3 && (CROPNAME.equalsIgnoreCase("Cotton") || CROPNAME.equalsIgnoreCase("cotton"))) {
//            ll_DiseaseDiagnosis.setVisibility(View.VISIBLE);
//        } else {
//            ll_DiseaseDiagnosis.setVisibility(View.GONE);
//        }

        //Screen Service
        setServiceCropInformation(diseaseForecast, "PestandDisease");
        setServiceCropInformation(ll_Calculator, "NPKCalculator");
        setServiceCropInformation(ll_DiseaseDiagnosis, "DiseaseDiagnosis");
        setServiceCropInformation(ll_Nutrition, "NutritionManagement");
        setServiceCropInformation(ll_WeedManagements, "WeedManagement");
        setServiceCropInformation(ll_PostHarvestManagement, "PostHarvestManagement");
        setServiceCropInformation(ll_IrrigationManagement, "IrrigationManagement");
        setServiceCropInformation(ll_Mandi, "MandiInformation");
        setServiceCropInformation(ll_YeildImprovement, "YeildImprovement");

//        ll_Tubewell.setVisibility(View.GONE);
//        setServiceCropInformation(ll_Tubewell, "Tubewell");

//        if (AISAvailable_Tubewell != null && !AISAvailable_Tubewell.equalsIgnoreCase("null")
//                && (AISAvailable_Tubewell.equalsIgnoreCase("Yes") ||
//                AISAvailable_Tubewell.equalsIgnoreCase("yes"))) {
//            ll_Tubewell.setVisibility(View.VISIBLE);
//        } else {
//            ll_Tubewell.setVisibility(View.GONE);
//        }

        imgview_WeedManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                dialog.cancel();
                if (AppConstant.selected_cropId != null) {
                    getWeedMngt();
                } else {
                    setToastCropIDNotFound(MainProfileActivity.this);
                }
            }
        });
        ll_WeedManagements.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                dialog.cancel();
                if (AppConstant.selected_cropId != null) {
                    getWeedMngt();
                } else {
                    setToastCropIDNotFound(MainProfileActivity.this);
                }
            }
        });
        txt_WeedManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                dialog.cancel();
                if (AppConstant.selected_cropId != null) {
                    getWeedMngt();
                } else {
                    setToastCropIDNotFound(MainProfileActivity.this);
                }
            }
        });
        //Post Harvest
        imgview_PostHarvestManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                dialog.cancel();
                if (AppConstant.selected_cropId != null) {
                    getPostHarvestMngt();
                } else {
                    setToastCropIDNotFound(MainProfileActivity.this);
                }
            }
        });
        ll_PostHarvestManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                dialog.cancel();
                if (AppConstant.selected_cropId != null) {
                    getPostHarvestMngt();
                } else {
                    setToastCropIDNotFound(MainProfileActivity.this);
                }
            }
        });
        txt_PostHarvestManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                dialog.cancel();
                if (AppConstant.selected_cropId != null) {
                    getPostHarvestMngt();
                } else {
                    setToastCropIDNotFound(MainProfileActivity.this);
                }
            }
        });
        //Irrigation Management
        imgview_IrrigationManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                dialog.cancel();
                if (AppConstant.selected_cropId != null && AppConstant.farm_id != null) {
                    getIrrigationMngt();
                } else {
                    setToastCropIDNotFound(MainProfileActivity.this);
                }
            }
        });
        ll_IrrigationManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                dialog.cancel();
                if (AppConstant.selected_cropId != null && AppConstant.farm_id != null) {
                    getIrrigationMngt();
                } else {
                    setToastCropIDNotFound(MainProfileActivity.this);
                }
            }
        });
        txt_IrrigationManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                dialog.cancel();
                if (AppConstant.selected_cropId != null && AppConstant.farm_id != null) {
                    getIrrigationMngt();
                } else {
                    setToastCropIDNotFound(MainProfileActivity.this);
                }
            }
        });

        //Mandi Management
        imgview_Mandi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    dialog.cancel();
                    setMandiInformation();
                } catch (Exception s) {
                    s.printStackTrace();
                }
            }
        });
        ll_Mandi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    dialog.cancel();
                    setMandiInformation();
                } catch (Exception s) {
                    s.printStackTrace();
                }
            }
        });
        txt_Mandi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    dialog.cancel();
                    setMandiInformation();
                } catch (Exception s) {
                    s.printStackTrace();
                }
            }
        });

        btn_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.cancel();
            }
        });

        diseaseForecast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                dialog.cancel();
//                pestDiseasePopupMethod();
                Log.v("asfasfasfas", "" + pestDataList.size() + "==" + pestManagementList.size() + "==" + pestImageList.size());
                if (pestDataList.size() > 0) {
                    popupPestMethod(pestDataList, pestImageListWhole, pestManagementListWhole, "Pest and Disease");
                } else {
                    getDynamicLanguageToast(getApplicationContext(), "norecordfarm", R.string.norecordfarm);
                }
            }
        });


        txt_Nutrition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                dialog.cancel();
                getNutritionMngt();
            }
        });
        imgview_Nutrition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                dialog.cancel();
                getNutritionMngt();
            }
        });
        ll_Nutrition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                dialog.cancel();
                getNutritionMngt();
            }
        });
        //DiseaseDignosis
        ll_DiseaseDiagnosis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSowingdateDiseasecall();
//                Intent in = new Intent(getActivity(), DiseaseDisgnosis.class);
//                startActivity(in);
            }
        });
        imgview_DiseaseDiagnosis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSowingdateDiseasecall();
            }
        });
        txt_DiseaseDiagnosis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSowingdateDiseasecall();
            }
        });

        ll_Calculator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCalculatorCall();
            }
        });
        imgview_Calculator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCalculatorCall();
            }
        });
        txt_Calculator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCalculatorCall();
            }
        });
        //Yield Management
        imgview_YeildImprovement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setToastComingsoon(MainProfileActivity.this);
//                getYeildimprovement();
            }
        });
        ll_YeildImprovement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setToastComingsoon(MainProfileActivity.this);
//                getYeildimprovement();
            }
        });
        txt_YeildImprovement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setToastComingsoon(MainProfileActivity.this);
                //                getYeildimprovement();
            }
        });

        //Irrigation Scheduler
        imgview_IrrigationScheduler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setToastComingsoon(MainProfileActivity.this);
            }
        });
        ll_IrrigationScheduler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setToastComingsoon(MainProfileActivity.this);
            }
        });
        txt_IrrigationScheduler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setToastComingsoon(MainProfileActivity.this);
            }
        });
        //GuideLines
        imgview_Guideliness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getGuideLines();
            }
        });
        ll_Guideliness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getGuideLines();
            }
        });
        txt_Guideliness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getGuideLines();
            }
        });
        //Tubewell
//        imgview_Tubewell.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                setTubewellCall();
//            }
//        });
//        ll_Tubewell.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                setTubewellCall();
//            }
//        });
//        txt_Tubewell.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                setTubewellCall();
//            }
//        });


        dialog.show();

    }


    public void pestDiseasePopupMethod() {

        final Dialog dialog = new Dialog(this, R.style.DialogSlideAnim);

        //  final Dialog dialog = new Dialog(getActivity());

        dialog.setCanceledOnTouchOutside(true);
        Window window = dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);


        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.BOTTOM;
        wlp.dimAmount = 0.5f;

        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        // wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        // Include dialog.xml file
        dialog.setContentView(R.layout.pest_dieseasepopup);


        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, 1000);
        /*} else {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        }*/

        LinearLayout vulnerability = (LinearLayout) dialog.findViewById(R.id.vulnerability_popup);
        LinearLayout diseaseForecasts = (LinearLayout) dialog.findViewById(R.id.disease_forecast_popup);

        TextView txt_PD_heading = (TextView) dialog.findViewById(R.id.txt_PD_heading);
        TextView txt_Vulnerability = (TextView) dialog.findViewById(R.id.txt_Vulnerability);
        TextView txt_PD_PestDisease = (TextView) dialog.findViewById(R.id.txt_PD_PestDisease);

        setDynamicLanguage(this, txt_PD_heading, "PestDisease", R.string.PestDisease);
        setDynamicLanguage(this, txt_Vulnerability, "Vulnerability", R.string.Vulnerability);
        setDynamicLanguage(this, txt_PD_PestDisease, "PestDisease", R.string.PestDisease);

        setFontsStyleTxt(this, txt_PD_heading, 2);
        setFontsStyleTxt(this, txt_Vulnerability, 5);
        setFontsStyleTxt(this, txt_PD_PestDisease, 5);


        vulnerability.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                Intent in = new Intent(getApplicationContext(), NewDashboardActivity.class);
                in.putExtra("from", "vul");
                startActivity(in);
            }
        });

        diseaseForecasts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                //Herojit Comment
//                Intent in = new Intent(getApplicationContext(), NewDashboardActivity.class);
//                in.putExtra("from", "fore");
//                startActivity(in);
                Log.v("asfasfasfas", "" + pestDataList.size() + "==" + pestManagementList.size() + "==" + pestImageList.size());
                if (pestDataList.size() > 0) {
                    popupPestMethod(pestDataList, pestImageListWhole, pestManagementListWhole, "Pest and Disease");
                } else {
                    getDynamicLanguageToast(getApplicationContext(), "norecordfarm", R.string.norecordfarm);
                }
            }
        });


        dialog.show();

    }


    public void isFarmAvailable() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getDynamicLanguageValue(getApplicationContext(), "Yourfarmisnot", R.string.Yourfarmisnot));
        builder.setMessage(getDynamicLanguageValue(getApplicationContext(), "Pleaseselectyour", R.string.Pleaseselectyour))
                .setCancelable(true)
                .setPositiveButton(getDynamicLanguageValue(getApplicationContext(), "Ok", R.string.Ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  AppConstant.isFarm = "yes";

                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }


    private boolean checkPermissionStorage() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, ACCESS_STORAGE);
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case ACCESS_STORAGE:

        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View view = getCurrentFocus();
        if (view != null && (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) && view instanceof EditText && !view.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            view.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + view.getLeft() - scrcoords[0];
            float y = ev.getRawY() + view.getTop() - scrcoords[1];
            if (x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom())
                ((InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow((this.getWindow().getDecorView().getApplicationWindowToken()), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

//    public void loadProfileData(String farmid) {
//        dialog = new TransparentProgressDialog(
//                MainProfileActivity.this, getResources().getString(R.string.loadingInformation));
//        dialog.setCancelable(false);
//
//        String languagePreference = prefs.getString(getResources().getString(R.string.language_pref_key), "1");
//        int flag = Integer.parseInt(languagePreference);
//
//        String language = AppManager.getInstance().getSelectedLanguages(this);
//        String requestString = "https://myfarminfo.com/yfirest.svc/GetCropAdvisory/" + farmid + "/" + language;
//        String createdString = AppManager.getInstance().removeSpaceForUrl(requestString);
//
//        Log.v("forecastDetailRequest", createdString);
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, createdString,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        dialog.cancel();
//                        response = response.trim();
////                        response = response.replace("\\", "");
////                        response = response.replace("\\", "");
////                        response = response.replace("\\", "");
////                        response = response.replace("\"{", "{");
////                        response = response.replace("}\"", "}");
////                        response = response.replace("\"[", "[");
////                        response = response.replace("]\"", "]");
//
//                        GsonBuilder builder = new GsonBuilder();
//                        Gson gson = builder.create();
//                        String str = gson.fromJson(response.toString(), String.class);
//                        response = str;
//                        setDFarmData(response, requestString);
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                dialog.cancel();
//                System.out.println("Volley Error : " + error);
//
//
//            }
//        });
//
//        int socketTimeout = 60000;//60 seconds - change to what you want
//        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//        stringRequest.setRetryPolicy(policy);
//
//        // Adding request to volley request queue
//        AppController.getInstance().addToRequestQueue(stringRequest);
//    }

    private long mRequestStartTime;

    public void loadProfileData(String farmid) {

        //Herojit Add for all spinner listioner
        cardview_NextStep.setVisibility(View.GONE);
        cardview_POP.setVisibility(View.GONE);
        cardview_Crop.setVisibility(View.GONE);
        cardview_Crop_new.setVisibility(View.GONE);
        cardview_weatherforcast.setVisibility(View.GONE);
        cardview_PestDisease.setVisibility(View.GONE);
        cardview_irrigationadvisory.setVisibility(View.GONE);
        cardview_FarmScore.setVisibility(View.GONE);
        tblrw_progressbar.setVisibility(View.GONE);
        recycleview_SMSList.setVisibility(View.GONE);
        ll_AdvisoryMessages.setVisibility(View.GONE);
        imageviewLogo.setVisibility(View.GONE);

        dashboardSMS = new ArrayList<>();

        dialog = new TransparentProgressDialog(
                MainProfileActivity.this, getDynamicLanguageValue(getApplicationContext(), "loadingInformation", R.string.loadingInformation));
        dialog.setCancelable(false);

        String languagePreference = prefs.getString(getResources().getString(R.string.language_pref_key), "1");
        int flag = Integer.parseInt(languagePreference);

        String language = AppManager.getInstance().getSelectedLanguages(this);
        String requestString = "https://myfarminfo.com/yfirest.svc/GetCropAdvisory/" + farmid + "/" + language;
        String createdString = AppManager.getInstance().removeSpaceForUrl(requestString);

        mRequestStartTime = System.currentTimeMillis();
        apiService.getDashbaordfarmerDetail(farmid, language, dateForCropAd).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResponseObserver<retrofit2.Response<Dashboard_FarmerResponse>>(compositeDisposable) {
                    @Override
                    public void onNetworkError(Throwable e) {
                        // calculate the duration in milliseconds
                        int seconds = getAPIimeResponseinSecond(mRequestStartTime);
                        dialog.cancel();
                        Log.v("wvqevqevqe", e.getMessage() + " " + e.toString());
                        SaveLocalFile(db, MainProfileActivity.this, SN_MainProfileActivity, requestString, "", getSMS(e.getMessage()), "" + seconds, AppConstant.farm_id, "Error");
//                        showError(getString(R.string.network_error));


//                        String sql = "Select * from " + db.TABLE_DASHBOARD_DATA + " where ScreenName='Dashboard' ";
//                        ArrayList<HashMap<String, String>> hasmap = db.getDynamicTableValue(sql);
//                        SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy");
//
//                        try {
//                            //String temp = "Thu Dec 17 15:37:43 GMT+05:30 2015";
//                            Date syncDate = formatter.parse(hasmap.get(0).get("SyncDate"));
//
//                            // 4 Day before date
//                            Date newDate = new Date(new Date().getTime() - 345600000L);
//
//                            if(syncDate.after(newDate)){
//                                setDFarmData(hasmap.get(0).get("Data"), requestString, seconds);
//                            }
//
//                            System.out.println("manishdate"+newDate.toString());
//
//                        } catch (Exception e1) {
//                            e1.printStackTrace();
//                        }
//
//
//                        setDFarmData(hasmap.get(0).get("Data"), requestString, seconds);

//


                        String queryForCountRecord = "Select count(*) from sms_data where userId='" + AppConstant.user_id + "'";

                        int recordsCount = db.getMaxRecord(queryForCountRecord);



                        //  if (array.length() > 0) {

                        if(recordsCount>0) {

                            String queryForRetrievingRecord = "Select * from sms_data where userId='" + AppConstant.user_id + "'";

                            ArrayList<HashMap<String, String>> records = db.getDynamicTableValue(queryForRetrievingRecord);

                            for (int i = 0; i < records.size(); i++) {
                                //  JSONObject obj = array.getJSONObject(i);
                                DashboardSMS dash = new DashboardSMS();
//                        dash.setId(obj.getString("Id"));
//                        dash.setMessage(obj.getString("Message"));
//                        dash.setMessageType(obj.getString("MessageType"));
//                        dash.setOutDate(obj.getString("OutDate"));
//                        dash.setFeedback(obj.getString("Feedback"));
//                        dash.setFoundflag(obj.getString("foundflag"));


                                dash.setId(records.get(i).get("SMSID"));
                                dash.setMessage(records.get(i).get("Message"));
                                dash.setMessageType(records.get(i).get("MessageType"));
                                dash.setOutDate(records.get(i).get("OutDate"));
                                dash.setFeedback(records.get(i).get("Feedback"));
                                dash.setFoundflag(records.get(i).get("foundflag"));
                                dash.setMediaURL(records.get(i).get("MediaURL"));
                                dash.setMediaType(records.get(i).get("MediaType"));


//                        if(obj.has("MediaType")){
//                            dash.setMediaType(obj.getString("MediaType"));
//                            dash.setMediaURL(obj.getString("MediaURL"));
//                        }else {
//                            dash.setMediaType("null");
//                            dash.setMediaURL("null");
//
//                        }
                                dashboardSMS.add(dash);
                            }


                            System.out.println("manish_sms_list" + dashboardSMS);


                            recycleview_SMSList.setVisibility(View.VISIBLE);
                            ll_AdvisoryMessages.setVisibility(View.VISIBLE);
                            setSMSList();

                        }
                    }

                    @Override
                    public void onServerError(Throwable e, int code) { // calculate the duration in milliseconds
                        int seconds = getAPIimeResponseinSecond(mRequestStartTime);
                        dialog.cancel();
//                        showError(getString(R.string.server_not_found));
                        SaveLocalFile(db, MainProfileActivity.this, SN_MainProfileActivity, requestString, "", "Server API Error", "" + seconds, AppConstant.farm_id, "Error");


//                        String sql = "Select * from " + db.TABLE_DASHBOARD_DATA + " where ScreenName='Dashboard' ";
//                        ArrayList<HashMap<String, String>> hasmap = db.getDynamicTableValue(sql);
//                        SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy");
//                        try {
//                            //String temp = "Thu Dec 17 15:37:43 GMT+05:30 2015";
//                            Date syncDate = formatter.parse(hasmap.get(0).get("SyncDate"));
//
//                            // 4 Day before date
//                            Date newDate = new Date(new Date().getTime() - 345600000L);
//
//                            if(syncDate.after(newDate)){
//                                setDFarmData(hasmap.get(0).get("Data"), requestString, seconds);
//                            }
//
//                            System.out.println("manishdate"+newDate.toString());
//
//                        } catch (Exception e1) {
//                            e1.printStackTrace();
//                        }

                            String queryForCountRecord = "Select count(*) from sms_data where userId='" + AppConstant.user_id + "'";

               int recordsCount = db.getMaxRecord(queryForCountRecord);



              //  if (array.length() > 0) {

                if(recordsCount>0) {

                    String queryForRetrievingRecord = "Select * from sms_data where userId='" + AppConstant.user_id + "'";

                    ArrayList<HashMap<String, String>> records = db.getDynamicTableValue(queryForRetrievingRecord);

                    for (int i = 0; i < records.size(); i++) {
                        //  JSONObject obj = array.getJSONObject(i);
                        DashboardSMS dash = new DashboardSMS();
//                        dash.setId(obj.getString("Id"));
//                        dash.setMessage(obj.getString("Message"));
//                        dash.setMessageType(obj.getString("MessageType"));
//                        dash.setOutDate(obj.getString("OutDate"));
//                        dash.setFeedback(obj.getString("Feedback"));
//                        dash.setFoundflag(obj.getString("foundflag"));


                        dash.setId(records.get(i).get("SMSID"));
                        dash.setMessage(records.get(i).get("Message"));
                        dash.setMessageType(records.get(i).get("MessageType"));
                        dash.setOutDate(records.get(i).get("OutDate"));
                        dash.setFeedback(records.get(i).get("Feedback"));
                        dash.setFoundflag(records.get(i).get("foundflag"));
                        dash.setMediaURL(records.get(i).get("MediaURL"));
                        dash.setMediaType(records.get(i).get("MediaType"));


//                        if(obj.has("MediaType")){
//                            dash.setMediaType(obj.getString("MediaType"));
//                            dash.setMediaURL(obj.getString("MediaURL"));
//                        }else {
//                            dash.setMediaType("null");
//                            dash.setMediaURL("null");
//
//                        }
                        dashboardSMS.add(dash);
                    }


                    System.out.println("manish_sms_list" + dashboardSMS);


                    recycleview_SMSList.setVisibility(View.VISIBLE);
                    ll_AdvisoryMessages.setVisibility(View.VISIBLE);
                    setSMSList();

                }
                    }

                    @Override
                    public void onNext(retrofit2.Response<Dashboard_FarmerResponse> Response) {
                        // calculate the duration in milliseconds
                        int seconds = getAPIimeResponseinSecond(mRequestStartTime);
                        dialog.cancel();
                        Dashboard_FarmerResponse Jsonstring = Response.body();

                        Gson gson = new Gson();
                        String response = gson.toJson(Jsonstring);
                        Log.v("ABC Log", response.toString());

                        if (seconds > 3) {
                            SaveLocalFile(db, MainProfileActivity.this, SN_MainProfileActivity, requestString, response, "", "" + seconds, AppConstant.farm_id, "Working");
                        }

                        db.open();

//                        String sql = "select count(*) from dashboard_data where ScreenName= 'Dashboard'";
//                        int dataCount = db.getMaxRecord(sql);
//
//                        if(dataCount > 0){
////                            String sqlDelete = "delete * from dashboard_data where ScreenName= 'Dashboard'";
//                            db.deleteDashboardData();
//                        }
//
 //                      db.insertDashboardData("Dashboard",new Date().toString(),response);

                     List<SMSLst> list= Jsonstring.getSMSLst();
                        if(list!=null){
                            for(SMSLst smsLst : list){

                                String queryForCountRecord = "Select count(*) from sms_data where SMSID='" + smsLst.getId() + "' and userId='" + AppConstant.user_id + "'";

                               int count= db.getMaxRecord(queryForCountRecord);

                               if(count == 0) {

                                   db.open();
                                   db.insertDashboardSMSData(smsLst.getMessage(), smsLst.getCCSID(), smsLst.getOutDate(), smsLst.getFeddbackDate(), smsLst.getMessageType(), smsLst.getMediaType(), smsLst.getMediaURL(), smsLst.getFeedback(), smsLst.getId() + "", smsLst.getFoundflag(), new Date().toString(), AppConstant.user_id);
                               }
                               }

                        }


                        setBanner(0);



                        setDFarmData(response, requestString, seconds);






                        //showError(getString(R.string.server_not_found));


                    }
                });
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public void loadFarmDetails(int flag) {

        dialog = new TransparentProgressDialog(
                MainProfileActivity.this, getResources().getString(R.string.loadingInformation));
        dialog.setCancelable(false);
        String requestString1 = "https://myfarminfo.com/yfirest.svc/Farm/Detailed/" + AppConstant.user_id;
        dialog.show();
        mRequestStartTime = System.currentTimeMillis();
        apiService.getUserFarmList(AppConstant.user_id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResponseObserver<retrofit2.Response<List<UserFarmResponse>>>(compositeDisposable) {
                    @Override
                    public void onNetworkError(Throwable e) {
                        // calculate the duration in milliseconds
                        int seconds = getAPIimeResponseinSecond(mRequestStartTime);
                        dialog.cancel();
                        Log.v("wvqevqevqe", e.getMessage() + " " + e.toString());
                        SaveLocalFile(db, MainProfileActivity.this, SN_MainProfileActivity, requestString1, "", getSMS(e.getMessage()), "" + seconds, AppConstant.farm_id, "Error");
//                        showError(getString(R.string.network_error));

                    }

                    @Override
                    public void onServerError(Throwable e, int code) { // calculate the duration in milliseconds
                        int seconds = getAPIimeResponseinSecond(mRequestStartTime);
                        dialog.cancel();
//                        showError(getString(R.string.server_not_found));
                        SaveLocalFile(db, MainProfileActivity.this, SN_MainProfileActivity, requestString1, "", "Server API Error", "" + seconds, AppConstant.farm_id, "Error");

                    }

                    @Override
                    public void onNext(retrofit2.Response<List<UserFarmResponse>> Response) {
                        // calculate the duration in milliseconds
                        int seconds = getAPIimeResponseinSecond(mRequestStartTime);
                        dialog.cancel();
                        List<UserFarmResponse> Jsonstring = Response.body();

                        farmList = Jsonstring;

                        if(farmList.size()>0){

                            selectedFarmLat = farmList.get(0).getCenterLat()+"";
                            selectedFarmLong = farmList.get(0).getCenterLon()+"";
                        }



                        if (seconds > 3) {
                            SaveLocalFile(db, MainProfileActivity.this, SN_MainProfileActivity, requestString1, Jsonstring.toString(), "", "" + seconds, AppConstant.farm_id, "Working");
                        }
                        userfarmList = new ArrayList<>();
                        userfarmList = Jsonstring;
                        if(flag == 1) {


                            if (userfarmList != null && userfarmList.size() > 0) {
                                Intent intent = new Intent(MainProfileActivity.this, PolicyFarmList.class);
                                startActivity(intent);
                            } else {
                                getDynamicLanguageToast(getApplicationContext(), "Thereisnoefarmagainstheuser", R.string.Thereisnoefarmagainstheuser);
                            }
                        }
//                        Intent intent = new Intent(MainProfileActivity.this, PolicyList.class);
//                        startActivity(intent);

                    }

                });
    }

    public void setDFarmData(String response, String URL, int seconds) {

        System.out.println("profileResponse : " + response);

        try {
            cropDataList = new ArrayList<CropStatusBean>();
            JSONObject jsonObject = new JSONObject(response.toString());
            if (jsonObject.has("cropStatus")) {
                JSONObject jbC = jsonObject.getJSONObject("cropStatus");
                if (jbC.getBoolean("Available")) {
                    String ss = jbC.getString("status");
                    CropConditionCheck = ss;
                    //Herojit Check
                    JSONObject jb1 = jbC.getJSONObject("Ndvi");
                    String a1 = jb1.getString("Status");
                    String ndviValue = jb1.getString("NdviValue");
                    String ndviBenchMark = jb1.getString("NdviBenchMark");

                    if (ndviValue != null && ndviValue.length() > 1) {
                        CropStatusBean keyValueBean = new CropStatusBean();
                        keyValueBean.setName("NDVI");
                        keyValueBean.setValue(ndviValue);
                        keyValueBean.setStatus(a1);
                        keyValueBean.setBenchmark(ndviBenchMark);
                        cropDataList.add(keyValueBean);
                    }

                    JSONObject jb2 = jbC.getJSONObject("rain");
                    String a2 = jb2.getString("Status");
                    String rainValue = jb2.getString("Value");
                    String rainBenchmark = jb2.getString("Benchmark");

                    if (rainValue != null && rainValue.length() > 1) {
                        CropStatusBean keyValueBean1 = new CropStatusBean();
                        keyValueBean1.setName("Rain ");
                        keyValueBean1.setValue(rainValue);
                        keyValueBean1.setBenchmark(rainBenchmark);
                        keyValueBean1.setStatus(a2);
                        cropDataList.add(keyValueBean1);
                    }

                    //Herojit Comment
                    JSONObject jb3 = jbC.getJSONObject("soilMoisture");
                    String a3 = jb3.getString("Status");
                    String soilValue = jb3.getString("SoilValue");
                    String soilBenchMark = jb3.getString("SoilBenchMark");

                    if (soilValue != null && soilValue.length() > 1) {
                        CropStatusBean keyValueBean2 = new CropStatusBean();
                        keyValueBean2.setName("Soil Moisture");
                        keyValueBean2.setValue(soilValue);
                        keyValueBean2.setBenchmark(soilBenchMark);
                        keyValueBean2.setStatus(a3);
                        cropDataList.add(keyValueBean2);
                    }

                    if (jbC.has("lstdisesecond")) {
                        JSONArray jsonArray = jbC.getJSONArray("lstdisesecond");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            if (jsonArray.getJSONObject(i).has("diseaseName")) {
                                String name = jsonArray.getJSONObject(i).getString("diseaseName");
                                String Conducivedays = jsonArray.getJSONObject(i).getString("Conducivedays");
                                if (name != null && name.length() > 0) {
                                    CropStatusBean keyValueBean3 = new CropStatusBean();
                                    keyValueBean3.setName("Disease");
                                    keyValueBean3.setValue(name);
//                                                    keyValueBean3.setBenchmark(Conducivedays + " (Conducive days)");
                                    keyValueBean3.setBenchmark(Conducivedays);
                                    cropDataList.add(keyValueBean3);
                                }
                            }
                        }
                    }

                    //Herojit Add
                    if (jbC.has("lstweathercond")) {
                        JSONArray jsonArray = jbC.getJSONArray("lstweathercond");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            if (jsonArray.getJSONObject(i).has("WeatherEventName")) {
                                String name = jsonArray.getJSONObject(i).getString("WeatherEventName");
                                String Conducivedays = jsonArray.getJSONObject(i).getString("Conducivedays");
                                if (name != null && name.length() > 0) {
                                    CropStatusBean keyValueBean3 = new CropStatusBean();
                                    keyValueBean3.setName("Weather Alert");
                                    keyValueBean3.setValue(name);
//                                                    keyValueBean3.setBenchmark(Conducivedays + " (Conducive days)");
                                    keyValueBean3.setBenchmark(Conducivedays);
                                    cropDataList.add(keyValueBean3);
                                }
                            }
                        }
                    }
                    if (ss != null && !ss.equalsIgnoreCase("null")) {
//                        cardview_Crop.setVisibility(View.VISIBLE); //Coment for change 20210825
                        cardview_Crop_new.setVisibility(View.VISIBLE);
                        //Normal and Under Stress
                        if (ss.equalsIgnoreCase("Normal")) {
                            txt_Cropsms.setText(getResources().getText(R.string.str_rb_normal));
                        } else if (ss.equalsIgnoreCase("Under Stress")) {
                            txt_Cropsms.setText(getResources().getText(R.string.UnderStress));
                        } else {
                            txt_Cropsms.setText(ss);
                        }
                    } else {
                        cardview_Crop.setVisibility(View.GONE);
                        cardview_Crop_new.setVisibility(View.GONE);
                    }
                } else {
                    cardview_Crop.setVisibility(View.GONE);
                    cardview_Crop_new.setVisibility(View.GONE);
                }

                //Thumbup and down
                try {
                    String statusThumb = jbC.getString("statusThumb");
                    if (statusThumb != null && !statusThumb.equalsIgnoreCase("null")) {
//                        imageview_cropthumbup.setVisibility(View.VISIBLE);//Coment
                        imageview_cropthumbup_new.setVisibility(View.VISIBLE);
                        if (statusThumb.equalsIgnoreCase("up") || statusThumb.equalsIgnoreCase("Up")) {
                            imageview_cropthumbup.setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_thumbup));
                            imageview_cropthumbup_new.setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_thumbup));
                        } else {
                            imageview_cropthumbup.setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_thumbdown));
                            imageview_cropthumbup_new.setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_thumbdown));
                        }
                    } else {
                        imageview_cropthumbup.setVisibility(View.GONE);
                        imageview_cropthumbup_new.setVisibility(View.GONE);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    imageview_cropthumbup.setVisibility(View.GONE);
                    imageview_cropthumbup_new.setVisibility(View.GONE);
                }
            }

            nextDataList = new ArrayList<KeyValueBean>();

            txt_NextStepsms1.setVisibility(View.GONE);
            txt_NextStepsms2.setVisibility(View.GONE);
            txt_NextStepsms3.setVisibility(View.GONE);
            txt_NextStepsms4.setVisibility(View.GONE);

            if (jsonObject.has("NextStep")) {
                JSONObject jbN = jsonObject.getJSONObject("NextStep");
                if (jbN.getBoolean("Available")) {

                    if (jbN.has("diseasemessagelst")) {
                        JSONArray jArray = jbN.getJSONArray("diseasemessagelst");
                        for (int i = 0; i < jArray.length(); i++) {
                            KeyValueBean keyValueBean = new KeyValueBean();
                            keyValueBean.setName("Disease Message");
                            String val = jArray.getString(i);
                            keyValueBean.setValue(val);
                            if (val != null && val.length() > 3) {
                                nextDataList.add(keyValueBean);
                            }
                        }
                    }
                    //Herojit Add
//                                    if (jbN.has("weathermessagelst")) {
//                                        JSONArray jArray = jbN.getJSONArray("weathermessagelst");
//                                        for (int i = 0; i < jArray.length(); i++) {
//                                            KeyValueBean keyValueBean = new KeyValueBean();
//                                            keyValueBean.setName("Weather Message");
//                                            String val = jArray.getString(i);
//                                            keyValueBean.setValue(val);
//                                            if (val != null && val.length() > 3) {
//                                                nextDataList.add(keyValueBean);
//                                            }
//                                            nextDataList.add(keyValueBean);
//                                        }
//                                    }
                    if (jbN.has("soiladvisorymsg")) {
                        String soilMsg = jbN.getString("soiladvisorymsg");
                        if (soilMsg != null && !soilMsg.equalsIgnoreCase("null") && soilMsg.length() > 4) {
                            KeyValueBean keyValueBean = new KeyValueBean();
                            keyValueBean.setName("Soil Advisory Message");
                            keyValueBean.setValue(soilMsg);
                            nextDataList.add(keyValueBean);
                        }
                    }
                    if (jbN.has("AutoIrrigationMessage")) {
                        String autirrigationmss = jbN.getString("AutoIrrigationMessage");
                        if (autirrigationmss != null && !autirrigationmss.equalsIgnoreCase("null") && autirrigationmss.length() > 4) {
                            KeyValueBean keyValueBean = new KeyValueBean();
                            keyValueBean.setName("Auto Irrigation Message");
                            keyValueBean.setValue(autirrigationmss);
                            nextDataList.add(keyValueBean);
                        }
                    }
                    if (jbN.has("nDVIAdvisory")) {
                        JSONObject jb = jbN.getJSONObject("nDVIAdvisory");
                        String ndviMsg = jb.getString("message");
                        if (ndviMsg != null && !ndviMsg.equalsIgnoreCase("null") && ndviMsg.length() > 4) {
                            KeyValueBean keyValueBean = new KeyValueBean();
                            keyValueBean.setName("NDVI Advisory");
                            keyValueBean.setValue(ndviMsg);
                            nextDataList.add(keyValueBean);
                        }
                    }
                    if (nextDataList.size() > 0) {
                        txt_NextStepsms1.setText(nextDataList.get(0).getValue());
                        //Comment
                        txt_NextStepsms1.setVisibility(View.VISIBLE);
//                        cardview_NextStep.setVisibility(View.VISIBLE);
                        cardview_NextStep.setVisibility(View.GONE);
                    } else {
                        cardview_NextStep.setVisibility(View.GONE);
                    }

//                                    if (jbN.has("lstnextPopDT")) {
//                                        JSONArray jsonArray = jbN.getJSONArray("lstnextPopDT");
//                                        if (jsonArray.length() > 0) {
//                                            String msg = null;
//                                            String stageN = null;
//                                            for (int i = 0; i < jsonArray.length(); i++) {
//                                                KeyValueBean keyValueBean = new KeyValueBean();
//                                                String ss = jsonArray.getJSONObject(i).getString("WorkName");
//                                                String vv = jsonArray.getJSONObject(i).getString("Work");
//                                                if (jsonArray.getJSONObject(i).has("StageName")) {
//                                                    stageN = jsonArray.getJSONObject(i).getString("StageName");
//                                                }
//                                                if (stageN != null) {
//                                                    msg = ss + "(" + stageN + ")" + " ---> " + vv;
//                                                } else {
//                                                    msg = ss + " ---> " + vv;
//                                                }
//                                                keyValueBean.setName(ss);
//                                                keyValueBean.setValue(vv);
//                                                nextDataList.add(keyValueBean);
//                                            }
//                                        }
//                                    }
                }

                //POP Upodates
                POPUpdates = new ArrayList<>();
                if (jbN.has("lstnextPopDT")) {
                    cardview_POP.setVisibility(View.VISIBLE);
                    JSONArray array = jbN.getJSONArray("lstnextPopDT");
                    try {
                        if (array != null && array.length() > 0) {
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject obj = array.getJSONObject(i);
                                POPUpdate pop = new POPUpdate();
                                pop.setStageName(obj.getString("StageName"));
                                pop.setWork(obj.getString("Work"));
                                pop.setWorkName(obj.getString("WorkName"));
                                POPUpdates.add(pop);
                            }
                            if (POPUpdates.size() > 0) {
                                POPHeading = POPUpdates.get(0).getStageName();
//                                                txt_POPHeading.setText(getResources().getString(R.string.POP) + " - " + POPHeading);
                                txt_POPTitle.setText(POPUpdates.get(0).getWorkName());
                                txt_POPsms.setText(POPUpdates.get(0).getWork());
                                NextStep_POPName = POPUpdates.get(0).getWork();

                            } else {
                                cardview_POP.setVisibility(View.GONE);
                                NextStep_POPName = null;
                            }
                        } else {
                            cardview_POP.setVisibility(View.GONE);
                            NextStep_POPName = null;
                        }
                    } catch (Exception ex) {
                        cardview_POP.setVisibility(View.GONE);
                        NextStep_POPName = null;
                        ex.printStackTrace();
                    }
                } else {
                    cardview_POP.setVisibility(View.GONE);
                    NextStep_POPName = null;
                }


            }

            if (jsonObject.has("weatherForecast")) {
                JSONObject jbW = jsonObject.getJSONObject("weatherForecast");
                if (jbW.getBoolean("Available")) {
                    JSONArray as = jbW.getJSONArray("lstAlertMessages");

                    if (as.length() > 0) {
                        //Comment
//                        cardview_weatherforcast.setVisibility(View.VISIBLE);
                        cardview_weatherforcast.setVisibility(View.GONE);
                        txt_weatherforcastsms.setText(as.getString(0));
                    } else {
                        cardview_weatherforcast.setVisibility(View.GONE);
                    }
                } else {
                    cardview_weatherforcast.setVisibility(View.GONE);
                }
            }

            pestDataList = new ArrayList<KeyValueBean>();
            if (jsonObject.has("PestDiseaseAlert")) {
                JSONObject jbN = jsonObject.getJSONObject("PestDiseaseAlert");
                if (jbN.getBoolean("Available")) {
                    if (jbN.has("lstPestDiseaseData")) {
                        JSONArray jsonArray = jbN.getJSONArray("lstPestDiseaseData");
                        if (jsonArray.length() > 0) {
                            String msg = null;
                            pestDataList = new ArrayList<KeyValueBean>();
                            pestImageListWhole = new ArrayList<ArrayList<String>>();
                            pestManagementListWhole = new ArrayList<ArrayList<String>>();

                            pestDataList_check = new ArrayList<KeyValueBean>();
                            pestImageListWhole_check = new ArrayList<ArrayList<String>>();
                            pestManagementListWhole_check = new ArrayList<ArrayList<String>>();

                            //Herojit Add
                            pestname = new JSONArray();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                KeyValueBean keyValueBean = new KeyValueBean();
                                String ss = jsonArray.getJSONObject(i).getString("pestname");
                                try {
                                    if (jsonArray.getJSONObject(i).getString("likelihood").equalsIgnoreCase("true")) {
                                        pestname.put(ss);
                                        //Herpjit ADD
                                        JSONArray jA = jsonArray.getJSONObject(i).getJSONArray("pestdescription");
                                        String vv = null;
                                        for (int l = 0; l < jA.length(); l++) {
                                            if (l == 0) {
                                                vv = jA.getString(l);
                                            } else {
                                                vv = vv + "\n" + jA.getString(l);
                                            }
                                        }


                                        if (vv != null && vv.length() > 4) {
                                            msg = ss + " ---> " + vv;
                                            keyValueBean.setName(ss);
                                            keyValueBean.setValue(vv);
                                            pestDataList.add(keyValueBean);
                                            pestDataList_check.add(keyValueBean);

                                            pestImageList = new ArrayList<String>();
                                            JSONArray jsonArray1 = jsonArray.getJSONObject(i).getJSONArray("Imagelst");
                                            for (int p = 0; p < jsonArray1.length(); p++) {
                                                pestImageList.add(jsonArray1.getString(p));
                                            }

                                            pestImageListWhole.add(pestImageList);
                                            pestImageListWhole_check.add(pestImageList);

                                            pestManagementList = new ArrayList<String>();
                                            JSONArray jsonArray2 = jsonArray.getJSONObject(i).getJSONArray("lstmanagement");
                                            for (int p = 0; p < jsonArray2.length(); p++) {
                                                pestManagementList.add(jsonArray2.getString(p));
                                            }

                                            pestManagementListWhole.add(pestManagementList);
                                            pestManagementListWhole_check.add(pestManagementList);
                                        }
                                    } else {
                                        JSONArray jA = jsonArray.getJSONObject(i).getJSONArray("pestdescription");
                                        String vv = null;
                                        for (int l = 0; l < jA.length(); l++) {
                                            if (l == 0) {
                                                vv = jA.getString(l);
                                            } else {
                                                vv = vv + "\n" + jA.getString(l);
                                            }
                                        }


                                        if (vv != null && vv.length() > 4) {
                                            msg = ss + " ---> " + vv;
                                            keyValueBean.setName(ss);
                                            keyValueBean.setValue(vv);
                                            pestDataList.add(keyValueBean);

                                            pestImageList = new ArrayList<String>();
                                            JSONArray jsonArray1 = jsonArray.getJSONObject(i).getJSONArray("Imagelst");
                                            for (int p = 0; p < jsonArray1.length(); p++) {
                                                pestImageList.add(jsonArray1.getString(p));
                                            }

                                            pestImageListWhole.add(pestImageList);

                                            pestManagementList = new ArrayList<String>();
                                            JSONArray jsonArray2 = jsonArray.getJSONObject(i).getJSONArray("lstmanagement");
                                            for (int p = 0; p < jsonArray2.length(); p++) {
                                                pestManagementList.add(jsonArray2.getString(p));
                                            }

                                            pestManagementListWhole.add(pestManagementList);
                                        }
                                    }
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }

                            cardview_PestDisease.setCardBackgroundColor(getResources().getColor(R.color.white));
                            if (pestname.length() > 0) {
                                //Comment
//                                cardview_PestDisease.setVisibility(View.VISIBLE);
                                cardview_PestDisease.setVisibility(View.GONE);
                                recycler_pestdesease.setHasFixedSize(true);
                                LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                                recycler_pestdesease.setLayoutManager(mLayoutManager);
                                MainProfileActivity_PestDisease_Adapter adapter = new MainProfileActivity_PestDisease_Adapter(getApplicationContext(), pestname);
                                recycler_pestdesease.setAdapter(adapter);
                            } else {
                                cardview_PestDisease.setVisibility(View.GONE);
                            }


                        }
                    }
                }
            }

            irrigationDataList = new ArrayList<KeyValueBean>();
            if (jsonObject.has("IrrigationAdvisory")) {
                JSONObject jbN = jsonObject.getJSONObject("IrrigationAdvisory");

                if (jbN.has("irrgationadvisoryDT")) {
                    JSONArray jsonArray = jbN.getJSONArray("irrgationadvisoryDT");
                    if (jsonArray.length() > 0) {
                        String msg = null;
                        String stag = null;
                        String messa = null;

                        for (int i = 0; i < jsonArray.length(); i++) {
                            KeyValueBean keyValueBean = new KeyValueBean();
                            String ss = jsonArray.getJSONObject(i).getString("WorkName");
                            String vv = jsonArray.getJSONObject(i).getString("Work");
                            if (jsonArray.getJSONObject(i).has("StageName")) {
                                stag = jsonArray.getJSONObject(i).getString("StageName");
                            }
                            if (jsonArray.getJSONObject(i).has("Message")) {
                                messa = jsonArray.getJSONObject(i).getString("Message");
                            }
//                                            if (stag != null) {
//                                                if (messa != null) {
//                                                    msg = stag + "(" + vv + ")" + " ---> " + messa;
//                                                } else {
//                                                    msg = stag + "(" + vv + ")";
//                                                }
//                                            }
                            if (stag != null) {
                                if (messa != null) {
                                    msg = stag + " ---> " + messa;
                                } else {
                                    msg = stag + "(" + vv + ")";
                                }
                            }

                            keyValueBean.setName(vv);
                            keyValueBean.setValue(messa);
                            irrigationDataList.add(keyValueBean);
                        }

                        if (msg != null && msg.length() > 4) {
                            //Comment
//                            cardview_irrigationadvisory.setVisibility(View.VISIBLE);
                            cardview_irrigationadvisory.setVisibility(View.GONE);
                            txt_irrigationadvisorysms.setText(msg);
                        } else {
                            txt_irrigationadvisorysms.setText("");
                            cardview_irrigationadvisory.setVisibility(View.GONE);
                        }
                    } else {
                        txt_irrigationadvisorysms.setText("");
                        cardview_irrigationadvisory.setVisibility(View.GONE);
                    }
                }
                if (jbN.has("dynamicguidance")) {
                    String irm = jbN.getString("dynamicguidance");
                    if (irm != null && irm.length() > 4) {
                        KeyValueBean keyValueBean = new KeyValueBean();
                        keyValueBean.setName("Dynamic Guidance");
                        keyValueBean.setValue("" + irm);
                        irrigationDataList.add(keyValueBean);
                    }
                }
            }
            String FarmScoreMessage = jsonObject.getString("FarmScoreMessage");
            txt_farmScore.setText("");
            cardview_FarmScore.setVisibility(View.GONE);
            if (FarmScoreMessage != null && FarmScoreMessage.length() > 4) {
                //Comment
//                cardview_FarmScore.setVisibility(View.VISIBLE);
                cardview_FarmScore.setVisibility(View.GONE);
                txt_farmScore.setText(FarmScoreMessage);
            } else {
                txt_farmScore.setText("");
                cardview_FarmScore.setVisibility(View.GONE);
            }
            String FarmScore = jsonObject.getString("FarmScore");
            if (FarmScore != null && FarmScore.length() > 0) {
                //Comment
//                tblrw_progressbar.setVisibility(View.VISIBLE);
                tblrw_progressbar.setVisibility(View.GONE);
                float farmvalue = Float.parseFloat(FarmScore);
                txt_FarmScoreValue.setText(farmvalue + " %");
                int value = (int) (Math.round(farmvalue));
                progressbarFarmScore.setProgress(value);
                if (farmvalue > 0.0 && farmvalue < 25.0) {
                    progressbarFarmScore.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.red_alret)));
                } else if (farmvalue >= 25.0 && farmvalue < 50.0) {
                    progressbarFarmScore.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.yellow)));
                } else if (farmvalue >= 50.0 && farmvalue < 75.0) {
                    progressbarFarmScore.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.ColorPrimaryLight)));
                } else if (farmvalue >= 75.0) {
                    progressbarFarmScore.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.ColorPrimaryDark)));
                } else {
                    txt_FarmScoreValue.setText("");
                    tblrw_progressbar.setVisibility(View.GONE);
                }

            } else {
                txt_FarmScoreValue.setText("");
                tblrw_progressbar.setVisibility(View.GONE);
            }
           String Logo= jsonObject.getString("Logo");
            if (Logo != null && Logo.length() > 6) {
                imageviewLogo.setVisibility(View.GONE);
                Picasso.with(this).load(Logo)
//                        .placeholder(R.drawable.bannerimage)
                        .into(imageviewLogo, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {
                                imageviewLogo.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onError() {
                                imageviewLogo.setVisibility(View.GONE);
                            }
                        });
            } else {
                imageviewLogo.setVisibility(View.GONE);
            }


            dashboardSMS.clear();
            //SMS List
            if (jsonObject.has("SMSLst")) {

              //  JSONArray array = jsonObject.getJSONArray("SMSLst");

                String queryForCountRecord = "Select count(*) from sms_data where userId='" + AppConstant.user_id + "'";

               int recordsCount = db.getMaxRecord(queryForCountRecord);



              //  if (array.length() > 0) {

                if(recordsCount>0){

                    String queryForRetrievingRecord = "Select * from sms_data where userId='" + AppConstant.user_id + "' ORDER BY OutDate desc";

                ArrayList<HashMap<String,String>> records =   db.getDynamicTableValue(queryForRetrievingRecord);

                    for (int i = 0; i < records.size(); i++) {
                      //  JSONObject obj = array.getJSONObject(i);
                        DashboardSMS dash = new DashboardSMS();
//                        dash.setId(obj.getString("Id"));
//                        dash.setMessage(obj.getString("Message"));
//                        dash.setMessageType(obj.getString("MessageType"));
//                        dash.setOutDate(obj.getString("OutDate"));
//                        dash.setFeedback(obj.getString("Feedback"));
//                        dash.setFoundflag(obj.getString("foundflag"));


                       dash.setId(records.get(i).get("SMSID"));
                       dash.setMessage(records.get(i).get("Message"));
                        dash.setMessageType(records.get(i).get("MessageType"));
                        dash.setOutDate(records.get(i).get("OutDate"));
                        dash.setFeedback(records.get(i).get("Feedback"));
                        dash.setFoundflag(records.get(i).get("foundflag"));
                        dash.setMediaURL(records.get(i).get("MediaURL"));
                        dash.setMediaType(records.get(i).get("MediaType"));


//                        if(obj.has("MediaType")){
//                            dash.setMediaType(obj.getString("MediaType"));
//                            dash.setMediaURL(obj.getString("MediaURL"));
//                        }else {
//                            dash.setMediaType("null");
//                            dash.setMediaURL("null");
//
//                        }
                        dashboardSMS.add(dash);
                    }


                    System.out.println("manish_sms_list"+dashboardSMS);


                    recycleview_SMSList.setVisibility(View.VISIBLE);
                    ll_AdvisoryMessages.setVisibility(View.VISIBLE);
                    setSMSList();
                }

            }


        } catch (JSONException e) {
            e.printStackTrace();
            SaveLocalFile(db, MainProfileActivity.this, SN_MainProfileActivity, URL, response, e.toString(), "" + seconds, AppConstant.farm_id, "Error");
        } catch (Exception e) {
            e.printStackTrace();
            SaveLocalFile(db, MainProfileActivity.this, SN_MainProfileActivity, URL, response, e.toString(), "" + seconds, AppConstant.farm_id, "Error");
        }
        try {
            //Herojit change the logic
            ResponseCropNameSOwingDate = "";
            new getUpdateCropName().execute();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void accountAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String VisiblName = AppConstant.visible_Name;
        if (VisiblName == null)
            VisiblName = "";
        builder.setMessage(getDynamicLanguageValue(getApplicationContext(), "Dear", R.string.Dear) + " " + VisiblName + " \n \n" + getDynamicLanguageValue(getApplicationContext(), "areusurelogout", R.string.areusurelogout));
        builder.setNegativeButton(getDynamicLanguageValue(getApplicationContext(), "No", R.string.No), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton(getDynamicLanguageValue(getApplicationContext(), "Yes", R.string.Yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

//                boolean isLogin = prefs.getBoolean(AppConstant.PREFRENCE_KEY_ISLOGIN, false);
//                Utility.setLogout(MainProfileActivity.this, prefs);
//                SharedPreferences.Editor editor = prefs.edit();
//                AppConstant.isLogin = false;
//                editor.putString(AppConstant.PREFRENCE_KEY_USER_ID, "");
//                editor.putString(AppConstant.PREFRENCE_KEY_VISIBLE_NAME, "");
//                editor.putBoolean(AppConstant.PREFRENCE_KEY_ISLOGIN, false);
//                editor.putString(AppConstant.PREFRENCE_KEY_IS_SAVE_ALL_RESPONSE, "null");
//                editor.apply();
                if (prefs == null) {
                    prefs = getSharedPreferences(AppConstant.SHARED_PREFRENCE_NAME, MODE_PRIVATE);
                }
                Utility.setLogout(MainProfileActivity.this, prefs);

                Intent in = new Intent(getApplicationContext(), LoginWithOtp_New.class);
                in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(in);
                finish();

            }
        });
        builder.show();
    }


    private void emergencyMethod() {
//        discussPopupMethod();
        callSmsMethod("07505022000");
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle(getResources().getString(R.string.Success)).
//                setMessage(getResources().getString(R.string.Youhavesuccessfully)).
//                setPositiveButton(getResources().getString(R.string.Ok), new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.cancel();
//                        discussPopupMethod();
//
//                    }
//                }).setNegativeButton(getResources().getString(R.string.Cancel), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                dialogInterface.cancel();
//
//
//            }
//        });
//        AlertDialog dialog = builder.create();
//        dialog.show();
    }

    private String createJsonParameterForDisease(String fd) {
        String parameterString = "";
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("UserID", fd);
            jsonObject.put("panictype", "Disease issue");
            Log.v("user_id", jsonObject.toString() + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        parameterString = jsonObject.toString();

        return parameterString;
    }

    private String createJsonParameterForGrowth(String fd) {
        String parameterString = "";
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("UserID", fd);
            jsonObject.put("panictype", "Growth issue");

            Log.v("user_id", jsonObject.toString() + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        parameterString = jsonObject.toString();

        return parameterString;
    }

    public void callSmsMethod(final String number) {

        final Dialog dialog = new Dialog(this, R.style.DialogSlideAnim);

        dialog.setCanceledOnTouchOutside(true);
        Window window = dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);


        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.CENTER;
        wlp.dimAmount = 0.5f;

        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        // wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        // Include dialog.xml file
        dialog.setContentView(R.layout.call_sms_popup);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        RelativeLayout callBTN = (RelativeLayout) dialog.findViewById(R.id.call_btn);
        RelativeLayout smsBTN = (RelativeLayout) dialog.findViewById(R.id.sms_btn);
        Button btn_cross = (Button) dialog.findViewById(R.id.btn_cross);

        TextView heading = (TextView) dialog.findViewById(R.id.heading);
        TextView txt_POPHeading = (TextView) dialog.findViewById(R.id.txt_POPHeading);
        TextView txtCall = (TextView) dialog.findViewById(R.id.txtCall);
        TextView txtSMS = (TextView) dialog.findViewById(R.id.txtSMS);

        setDynamicLanguage(this, heading, "Shareyourproblem", R.string.Shareyourproblem);
        setDynamicLanguage(this, txt_POPHeading, "Pleaseshaareyourproblems", R.string.Pleaseshaareyourproblems);
        setDynamicLanguage(this, txtCall, "Call", R.string.Call);
        setDynamicLanguage(this, txtSMS, "SMS", R.string.SMS);

        setFontsStyleTxt(this, heading, 2);
        setFontsStyleTxt(this, txt_POPHeading, 5);
        setFontsStyleTxt(this, txtCall, 5);
        setFontsStyleTxt(this, txtSMS, 5);

        btn_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        callBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                try {
//                    boolean resultCam = Utility.checkPermissionCall(MainProfileActivity.this);
                    boolean resultCam = Utility.checkPermissionCallRecord(MainProfileActivity.this);
                    if (resultCam) {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + number));
                        if (ActivityCompat.checkSelfPermission(MainProfileActivity.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                            final Intent intent = new Intent(MainProfileActivity.this, CallRecordingService.class);
//                            startService(intent);
                            startActivity(callIntent);
                        }
                    } else {
                        Utility.setPermissionsRecording(MainProfileActivity.this);
                        getDynamicLanguageToast(getApplicationContext(), "Permissionisdenied", R.string.Permissionisdenied);
                    }

                } catch (ActivityNotFoundException activityException) {
                    Log.e("Calling a Phone Number", "Call failed", activityException);
                }
            }
        });

        smsBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                //With Permission
//                boolean resultCam = Utility.checkPermissionSMS(MainProfileActivity.this);
//                if (resultCam) {
//                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + number));
//                    intent.putExtra("sms_body", "Type here");
//                    startActivity(intent);
//                }


//                String UserName = "Farmer Details: " + AppConstant.selected_farm;
                String FarmerID = AppConstant.farm_id;
                String Messages = FarmerID + "\n ";

                //Without Permission
                // Create the intent.
                Intent smsIntent = new Intent(Intent.ACTION_SENDTO);
                // Set the data for the intent as the phone number.
                smsIntent.setData(Uri.parse("smsto:" + number));
                // Add the message (sms) with the key ("sms_body").
                smsIntent.putExtra("sms_body", Messages);
                // If package resolves (target app installed), send intent.
                if (smsIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(smsIntent);
                } else {
                    //Log.e(TAG, "Can't resolve app for ACTION_SENDTO Intent");
                }
            }
        });
        dialog.show();


    }


    private void popupMethod(ArrayList<KeyValueBean> data, String title) {

        final Dialog dialog = new Dialog(this, R.style.DialogSlideAnim);

        //  final Dialog dialog = new Dialog(getActivity());

        dialog.setCanceledOnTouchOutside(true);
        Window window = dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);


        assert window != null;
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.BOTTOM;
        wlp.dimAmount = 0.5f;

        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        // wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        // Include dialog.xml file
        dialog.setContentView(R.layout.pop_weather);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, height);
//        if (data.size() > 2) {
//            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, 950);
//        } else {
//            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, 750);
//        }

        RecyclerView listView = (RecyclerView) dialog.findViewById(R.id.weather_list);
        TextView titlell = (TextView) dialog.findViewById(R.id.title);
        TextView viewData = (TextView) dialog.findViewById(R.id.view_ndvi);
        Button cancel_btn = (Button) dialog.findViewById(R.id.cancel_btn);
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        if (title != null && title.equalsIgnoreCase("Crop Status")) {
            viewData.setVisibility(View.VISIBLE);
            viewData.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent in = new Intent(getApplicationContext(), NewDashboardActivity.class);
                    in.putExtra("from", "ndvi");
                    startActivity(in);
                }
            });
        } else {
            viewData.setVisibility(View.GONE);
        }

        setDynamicLanguage(this, viewData, "ViewNDVI", R.string.ViewNDVI);
        if (title != null && title.equalsIgnoreCase("Irrigation Advisory")) {
            setDynamicLanguage(this, titlell, "IrrigationAdvisory", R.string.IrrigationAdvisory);
        } else if (title != null && title.equalsIgnoreCase("Next Step")) {
            setDynamicLanguage(this, titlell, "CorrectiveMeasures", R.string.CorrectiveMeasures);
        } else if (title != null && title.equalsIgnoreCase("Crop Status")) {
            setDynamicLanguage(this, titlell, "CropStatus", R.string.CropStatus);
        } else {
            titlell.setText(title);
        }

        setFontsStyleTxt(this, titlell, 2);
        setFontsStyleTxt(this, viewData, 5);

        listView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        listView.setLayoutManager(mLayoutManager);
        if (data.size() > 0) {
            NewPopupAdapter adapter = new NewPopupAdapter(this, data);
            listView.setAdapter(adapter);
        } else {
            setToastNodataavailable(MainProfileActivity.this);
        }
        dialog.show();

    }

    boolean checkcropstatusPop = false;

    private void cropPopupMethod(ArrayList<CropStatusBean> data, String title) {
        if (checkcropstatusPop == false) {
            checkcropstatusPop = true;
            final Dialog dialog = new Dialog(this, R.style.DialogSlideAnim);
            //  final Dialog dialog = new Dialog(getActivity());
            dialog.setCanceledOnTouchOutside(true);
            Window window = dialog.getWindow();
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);


            assert window != null;
            WindowManager.LayoutParams wlp = window.getAttributes();

            wlp.gravity = Gravity.BOTTOM;
            wlp.dimAmount = 0.5f;

            dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            // wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            window.setAttributes(wlp);
            // Include dialog.xml file
            dialog.setContentView(R.layout.cropstatus_popup);

            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int height = displayMetrics.heightPixels;
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, height);
//        if (data.size() > 2) {
//            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, 950);
//        } else {
//            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, 750)
//        }

            TextView titlell = (TextView) dialog.findViewById(R.id.title);
            Button btn_cross = (Button) dialog.findViewById(R.id.btn_cross);
            RecyclerView listView = (RecyclerView) dialog.findViewById(R.id.weather_list);

            if (title != null && title.equalsIgnoreCase("Crop Status")) {
                setDynamicLanguage(this, titlell, "CropStatus", R.string.CropStatus);
            } else {
                titlell.setText(title);
            }
            setFontsStyleTxt(this, titlell, 2);

            btn_cross.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkcropstatusPop = false;
                    dialog.cancel();
                }
            });

            listView.setHasFixedSize(true);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
            listView.setLayoutManager(mLayoutManager);
            if (data.size() > 0) {
//            CropPopupAdapter adapter = new CropPopupAdapter(this, data);
                CropPopupAdapterNew adapter = new CropPopupAdapterNew(this, data);
                listView.setAdapter(adapter);
            } else {
                setToastNodataavailable(MainProfileActivity.this);
            }
            dialog.show();
        }


    }

    boolean checkPestDiseaePopUp = false;

    //Name and value, ImageName, Management
    private void popupPestMethod(final ArrayList<KeyValueBean> data, ArrayList<
            ArrayList<String>> imageList,
                                 final ArrayList<ArrayList<String>> managementList, String title) {
        if (checkPestDiseaePopUp == false) {
            checkPestDiseaePopUp = true;
            //Start ScreenTracking for Pest and Disease
            final String UID_PestDisease = getUIDforScreenTracking();
            setScreenTracking(this, db, SN_MainProfileActivity_PestandDisease, UID_PestDisease);

            final Dialog dialog = new Dialog(this, R.style.DialogSlideAnim);

            //  final Dialog dialog = new Dialog(getActivity());

            dialog.setCanceledOnTouchOutside(true);
            Window window = dialog.getWindow();
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);


            assert window != null;
            WindowManager.LayoutParams wlp = window.getAttributes();

            wlp.gravity = Gravity.BOTTOM;
            wlp.dimAmount = 0.5f;

            dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            // wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            window.setAttributes(wlp);
            // Include dialog.xml file
            dialog.setContentView(R.layout.pest_disease_popup);

            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int height = displayMetrics.heightPixels;
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, height);
//        if (data.size() > 1) {
//            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, 950);
//        } else {
//            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, 750);
//        }

            pestdiseasePopup = (RecyclerView) dialog.findViewById(R.id.recycleview_pestDisease);
            TextView titlell = (TextView) dialog.findViewById(R.id.title);
            ImageView cancel_btn = (ImageView) dialog.findViewById(R.id.cancel_btn);

            final Button audio_sound_btn_pestdieasepopup = (Button) dialog.findViewById(R.id.audio_sound_btn_pestdieasepopup);
            final Button audio_sound_btn_pestdieasepopup_mute = (Button) dialog.findViewById(R.id.audio_sound_btn_pestdieasepopup_mute);

            audio_sound_btn_pestdieasepopup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    audiospeech_flag = 6;
                    managementList_pop = managementList;
                    Soundspeak(1);
                    audio_sound_btn_pestdieasepopup.setVisibility(View.GONE);
                    audio_sound_btn_pestdieasepopup_mute.setVisibility(View.VISIBLE);
                }
            });
            audio_sound_btn_pestdieasepopup_mute.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    audiospeech_flag = 6;
                    managementList_pop = managementList;
                    Soundspeak(2);
                    audio_sound_btn_pestdieasepopup.setVisibility(View.VISIBLE);
                    audio_sound_btn_pestdieasepopup_mute.setVisibility(View.GONE);
                }
            });

            cancel_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
                    checkPestDiseaePopUp = false;
                    //Close ScreenTracking for Pest and Disease
                    setScreenTracking(MainProfileActivity.this, db, SN_MainProfileActivity_PestandDisease, UID_PestDisease);
                }
            });

            if (title.equalsIgnoreCase("Pest and Disease")) {
                setDynamicLanguage(this, titlell, "PestandDisease", R.string.PestandDisease);
            } else {
                titlell.setText(title);
            }
            setFontsStyleTxt(this, titlell, 2);

            pestdiseasePopup.setHasFixedSize(true);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
            pestdiseasePopup.setLayoutManager(mLayoutManager);

            if (data.size() > 0) {
                PestDiseaseAdapterNew adapter = new PestDiseaseAdapterNew(this, data, imageList, managementList);
                pestdiseasePopup.setAdapter(adapter);
            } else {
                setToastNodataavailable(MainProfileActivity.this);
            }
            dialog.show();
        }


    }

    public String gettextPestDeseasePopup() {
        String returnval = "";
        try {
            if (managementList_pop.size() > 0) {
                for (int i = 0; i < managementList_pop.size(); i++) {
                    View view = pestdiseasePopup.getChildAt(i); // This will give you entire row(child) from RecyclerView
                    if (view != null) {
                        TextView textView = (TextView) view.findViewById(R.id.name);
                        TextView value = (TextView) view.findViewById(R.id.value);
//                        TextView management_text = (TextView) view.findViewById(R.id.management_text);

                        String namevalue = textView.getText().toString();
                        String valuevalue = value.getText().toString();
//                        String management_textvalue = management_text.getText().toString();
                        if (i == 0)
                            returnval = namevalue + " \n" + valuevalue /*+ " \n" + "Management" + " \n" + management_textvalue*/;
                        else
                            returnval = " \n" + returnval + " \n" + namevalue /*+ " \n" + valuevalue + " \n" + "Management" + " \n" + management_textvalue*/;
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return returnval;
    }

    public boolean StringCheck(String val) {
        if (val.length() == 0 || val.equalsIgnoreCase("null") || val.equalsIgnoreCase("")) {
            return true;
        } else return false;
    }

    //Audio Sound Display
    private void Soundspeak(int ONOFF_Flag) {

//        float pitch = (float) seek_bar_pitch.getProgress() / 50;0.66
        float pitch = 0.66f;
        if (pitch < 0.1) pitch = 0.1f;
//        float speed = (float) seek_bar_speed.getProgress() / 50;.96
        float speed = 0.85f;
        if (speed < 0.1) speed = 0.1f;

        mTTS.setPitch(pitch);
        mTTS.setSpeechRate(speed);
        String ReadingText = getTextforAudioSpeech();
        if (ONOFF_Flag == 1)
            mTTS.speak(ReadingText, TextToSpeech.QUEUE_FLUSH, null);
        else if (ONOFF_Flag == 2)
            mTTS.stop();
    }

    public String getTextforAudioSpeech() {
        String value = "";
        try {
            switch (audiospeech_flag) {
                case 1:
                    value = txt_CropHeading.getText().toString() + " \n  " + txt_Cropsms.getText().toString();
                    break;
                case 2:
                    value = txt_NextStepHeading.getText().toString() + " \n  " + txt_NextStepsms1.getText().toString() + "  \n " + txt_NextStepsms2.getText().toString() + " \n  " + txt_NextStepsms3.getText().toString() + "   " + txt_NextStepsms4.getText().toString();
                    break;
                case 3:
                    value = txt_weatherforcastHeading.getText().toString() + " \n  " + txt_weatherforcastsms.getText().toString();
                    break;
                case 4:
                    value = txt_PestDiseaseHeading.getText().toString() + " \n  " + gettextPestDesease();
                    break;
                case 5:
                    value = txt_irrigationadvisoryHeading.getText().toString() + " \n  " + txt_irrigationadvisorysms.getText().toString();
                    break;
                case 6://Pest and Disease Adapter
                    value = gettextPestDeseasePopup();
                    break;
                case 7://POP Add NEw
                    value = txt_POPHeading.getText().toString() + " \n  " + txt_POPTitle.getText().toString() + " \n  " + txt_POPsms.getText().toString();
                    break;
                case 8://Farm Score
                    value = txt_FarmScore.getText().toString() + " \n  " + txt_farmScore.getText().toString();
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return value.replace(".", " \n ").toString().replace(",", " \n ").toString();
    }

    //Herojit Comment
    public String gettextPestDesease() {
        String returnval = "";
        try {
            if (pestname.length() > 0) {
                for (int i = 0; i < pestname.length(); i++) {
                    View view = recycler_pestdesease.getChildAt(i); // This will give you entire row(child) from RecyclerView
                    if (view != null) {
                        TextView textView = (TextView) view.findViewById(R.id.value);
                        String value = textView.getText().toString();
                        if (i == 0)
                            returnval = value;
                        else
                            returnval = " \n" + returnval + " \n" + value;
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return returnval;
    }

    public void setAudioSpeechButton(Button btn, final int flag) {

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                audiospeech_flag = flag;
                try {
                    switch (audiospeech_flag) {
                        case 1:
                            audio_sound_btn_cropstatus.setVisibility(View.GONE);
                            audio_sound_btn_cropstatus_mute.setVisibility(View.VISIBLE);
                            break;
                        case 2:
                            audio_sound_btn_nextstep.setVisibility(View.GONE);
                            audio_sound_btn_nextstep_mute.setVisibility(View.VISIBLE);
                            break;
                        case 3:
                            audio_sound_btn_weatherforecast.setVisibility(View.GONE);
                            audio_sound_btn_weatherforecast_mute.setVisibility(View.VISIBLE);
                            break;
                        case 4:
                            audio_sound_btn_pestdiease.setVisibility(View.GONE);
                            audio_sound_btn_pestdiease_mute.setVisibility(View.VISIBLE);
                            break;
                        case 5:
                            audio_sound_btn_irrigation.setVisibility(View.GONE);
                            audio_sound_btn_irrigation_mute.setVisibility(View.VISIBLE);
                            break;
                        case 7://POP New Add
                            audio_sound_btn_POPUpdates.setVisibility(View.GONE);
                            audio_sound_btn_POPUpdates_mute.setVisibility(View.VISIBLE);
                            break;
                        case 8://FarmScore
                            audio_sound_btn_FarmScore.setVisibility(View.GONE);
                            audio_sound_btn_FarmScore_mute.setVisibility(View.VISIBLE);
                            break;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                Soundspeak(1);
            }
        });
    }

    public void setMuteAudioSpeechButton(Button btn, final int flag) {

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                audiospeech_flag = flag;
                try {
                    switch (audiospeech_flag) {
                        case 1:
                            audio_sound_btn_cropstatus.setVisibility(View.VISIBLE);
                            audio_sound_btn_cropstatus_mute.setVisibility(View.GONE);
                            break;
                        case 2:
                            audio_sound_btn_nextstep.setVisibility(View.VISIBLE);
                            audio_sound_btn_nextstep_mute.setVisibility(View.GONE);
                            break;
                        case 3:
                            audio_sound_btn_weatherforecast.setVisibility(View.VISIBLE);
                            audio_sound_btn_weatherforecast_mute.setVisibility(View.GONE);
                            break;
                        case 4:
                            audio_sound_btn_pestdiease.setVisibility(View.VISIBLE);
                            audio_sound_btn_pestdiease_mute.setVisibility(View.GONE);
                            break;
                        case 5:
                            audio_sound_btn_irrigation.setVisibility(View.VISIBLE);
                            audio_sound_btn_irrigation_mute.setVisibility(View.GONE);
                            break;
                        case 7://POP New Add
                            audio_sound_btn_POPUpdates.setVisibility(View.VISIBLE);
                            audio_sound_btn_POPUpdates_mute.setVisibility(View.GONE);
                            break;
                        case 8://FarmScore
                            audio_sound_btn_FarmScore.setVisibility(View.VISIBLE);
                            audio_sound_btn_FarmScore_mute.setVisibility(View.GONE);
                            break;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                Soundspeak(2);
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (mTTS != null) {
            mTTS.stop();
            mTTS.shutdown();
        }

        super.onDestroy();
    }

    public void setfarmeridSelection() {
        try {
            String name = AppConstant.selected_farm;
            String idddd = AppConstant.farm_id;

            db.open();
            Cursor c = db.getStateFromSelectedFarm(idddd);
            Log.v("farmID", c.getCount() + "");
            if (c.moveToFirst()) {
                do {
                    AppConstant.farm_id = c.getString(c.getColumnIndex(DBAdapter.FARM_ID));
                    AppConstant.stateID = c.getString(c.getColumnIndex(DBAdapter.STATE_ID));
                    contour = c.getString(c.getColumnIndex(DBAdapter.CONTOUR));
                    // Check Contour from DB which are already download not the first time
                    // Herojit change condition
                    lat = c.getString(c.getColumnIndex(DBAdapter.CENTRE_LAT));
                    lon = c.getString(c.getColumnIndex(DBAdapter.CENTRE_LON));

//               if (contour != null && contour.length() > 10) {
                    // Herojit change condition
                    if ((contour != null && contour.length() > 10) ||
                            (lat != null && lat.length() > 1 && lon != null && lon.length() > 1)) {
                        checkfirsttime = false;
//                  lat = c.getString(c.getColumnIndex(DBAdapter.CENTRE_LAT));
//                  lon = c.getString(c.getColumnIndex(DBAdapter.CENTRE_LON));
                        AppConstant.latitude = lat;
                        AppConstant.longitude = lon;
                        if (AppConstant.longitude != null) {
                            latitude = Double.parseDouble(AppConstant.latitude);
                            longitude = Double.parseDouble(AppConstant.longitude);
                        }
                        AppConstant.contour = contour;
                        AppConstant.farmName = idddd;
                        AppConstant.isFarm = "yes";

                        points = new ArrayList<LatLng>();
                        List<String> l_List = new ArrayList<String>();
                        String point = contour;
                        Log.v("points", point + "");
                        if (point != null) {
                            l_List = Arrays.asList(point.split("-"));
                        }
                        if (l_List.size() > 1) {
                            for (int j = 0; j < l_List.size(); j++) {
                                String currentString = l_List.get(j);
                                if (currentString != null) {
                                    String[] separated = currentString.split(",");
                                    if (separated.length > 1) {
                                        String la = separated[0];
                                        String lo = separated[1];
                                        points.add(new LatLng(Double.valueOf(la), Double.valueOf(lo)));
                                    }
                                }
                            }
                        }
                        uploadScreenTracking();
                    } else if (!checkfirsttime) {
                        new getFarmDetailAsyncTask(AppConstant.farm_id).execute();
                    }
                }
                while (c.moveToNext());
            }
            db.close();
        } catch (IndexOutOfBoundsException Ex) {
            Ex.printStackTrace();
        } catch (Exception Ex) {
            Ex.printStackTrace();
        }
    }

    String getFarmURL;

    private class getFarmDetailAsyncTask extends AsyncTask<Void, Void, String> {
        String f_id;
        String result = "";
        TransparentProgressDialog progressDialog;

        public getFarmDetailAsyncTask(String id) {
            //  this.data = data;
            f_id = id;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new TransparentProgressDialog(MainProfileActivity.this, getDynamicLanguageValue(getApplicationContext(), "getfarmdetails", R.string.getfarmdetails));
            progressDialog.setCancelable(false);
            progressDialog.show();
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
            try {
                mRequestStartTime = System.currentTimeMillis();
                getFarmURL = AppManager.getInstance().getFarmDetail + "/" + AppConstant.user_id + "/" + f_id;
                Log.d("get farm url", getFarmURL);
                String response = AppManager.getInstance().httpRequestGetMethod(getFarmURL);
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
            int seconds = getAPIimeResponseinSecond(mRequestStartTime);
            try {
                if (seconds > 3) {
                    SaveLocalFile(db, MainProfileActivity.this, SN_MainProfileActivity, getFarmURL, response, "", "" + seconds, AppConstant.farm_id, "Working");
                }
                if (response != null) {
                    if (response.contains("No Farms")) {
                        System.out.println("Farm not available");
                        getDynamicLanguageToast(getApplicationContext(), "Farmnotavailable", R.string.Farmnotavailable);
                        //   gotoHomeScreen();
                    } else {
                        AllFarmDetail addFarmDetail;
                        db.open();
                        //  db.deleteAllFarmDetailTable();
                        System.out.println("farm detail response " + response);

                        JSONObject jsonObject = new JSONObject(AppManager.getInstance().placeSpaceIntoString(response));
                        //  if (jArray.length() > 0) {
//Herojit Comment
//                        int deleteCount = db.db.delete(DBAdapter.DATABASE_TABLE_ALL_FARM_DETAIL, DBAdapter.FARM_ID + " = '" + f_id + "'", null);
                        int deleteCount1 = db.db.delete(DBAdapter.TABLE_QUERY_CROP, DBAdapter.FARM_ID + " = '" + f_id + "'", null);
//                        System.out.println("deleteCount : " + deleteCount + " deleteCount1 : " + deleteCount1);
                        //   }
                        //   for (int i = 0; i < jArray.length(); i++) {


                        addFarmDetail = new AllFarmDetail(jsonObject);
                        addFarmDetail.setUserId(AppConstant.user_id);
                        String farmId = addFarmDetail.getFarmId();
                        String farmName = addFarmDetail.getFarmName();
                        String farmerName = addFarmDetail.getFarmerName();
                        String farmerNumber = addFarmDetail.getFarmerPhone();
                        String concern = addFarmDetail.getConcern();
                        //Herojit Comment
//                        Long l = db.insertAllFarmDetail(addFarmDetail, DBAdapter.SENT);
                        Long l = db.UpdatedFarmerDetails(addFarmDetail, DBAdapter.SENT);
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
                                data.setSowPeriodTo(cropJsonObject.isNull("CropTo") ? "" : cropJsonObject.getString("CropTo"));
                                data.setSowPeriodForm(cropJsonObject.isNull("SowDate") ? "" : cropJsonObject.getString("SowDate"));
                                data.setOtherNutrition(cropJsonObject.isNull("OtherNutrient") ? "" : cropJsonObject.getString("OtherNutrient"));
                                data.setBesalDoseApply(cropJsonObject.isNull("BasalDoseApply") ? "" : cropJsonObject.getString("BasalDoseApply"));
                                long inserted = data.insert(db, DBAdapter.SENT);
                                System.out.println("database return value=" + l);
                            }
                        }
//                        String contour = null;
//                        String lat = null;
//                        String lon = null;
//                        String name = AppConstant.selectedFarm;

                        Cursor c = db.getStateFromSelectedFarm(farmId);
                        if (c.moveToFirst()) {
                            do {
                                AppConstant.farm_id = c.getString(c.getColumnIndex(DBAdapter.FARM_ID));
                                AppConstant.stateID = c.getString(c.getColumnIndex(DBAdapter.STATE_ID));
                                contour = c.getString(c.getColumnIndex(DBAdapter.CONTOUR));
                                // Herojit change condition
                                lat = c.getString(c.getColumnIndex(DBAdapter.CENTRE_LAT));
                                lon = c.getString(c.getColumnIndex(DBAdapter.CENTRE_LON));

//                                if (contour != null && contour.length() > 10) {
                                // Herojit change condition
                                if ((contour != null && contour.length() > 10) ||
                                        (lat != null && lat.length() > 1 && lon != null && lon.length() > 1)) {

//                                    lat = c.getString(c.getColumnIndex(DBAdapter.CENTRE_LAT));
//                                    lon = c.getString(c.getColumnIndex(DBAdapter.CENTRE_LON));
                                    AppConstant.latitude = lat;
                                    AppConstant.longitude = lon;
                                    if (AppConstant.longitude != null) {
                                        latitude = Double.parseDouble(AppConstant.latitude);
                                        longitude = Double.parseDouble(AppConstant.longitude);
                                    }

                                    AppConstant.contour = contour;
                                    AppConstant.farmName = AppConstant.farm_id;
                                    AppConstant.isFarm = "yes";

                                    if (AppConstant.farm_id != null) {

                                        String farmIdd = AppConstant.farm_id;
                                        Cursor getCropQuery = db.getCropQueryByFarmId(farmIdd);
                                        if (c.moveToFirst()) {
                                            do {
                                                if (getCropQuery.moveToFirst()) {

                                                    AppConstant.selected_cropId = getCropQuery.getString(getCropQuery.getColumnIndex(DBAdapter.CROP_ID));
                                                    AppConstant.selected_variety = getCropQuery.getString(getCropQuery.getColumnIndex(DBAdapter.CROPS_VARIETY));
                                                    //   AppConstant.selected_crop = getCropQuery.getString(getCropQuery.getColumnIndex(DBAdapter.CROP));
                                                    if (AppConstant.selected_cropId != null) {
                                                        AppConstant.selected_crop = db.getCropNameByID(AppConstant.selected_cropId);
                                                    }
                                                    Log.v("cropIddddd", AppConstant.selected_cropId + "--" + AppConstant.selected_crop);

                                                    if (AppConstant.selected_crop != null) {
                                                        setCropname();
                                                    }
                                                }
                                            } while (c.moveToNext());
                                        }
                                    }

                                    points = new ArrayList<LatLng>();
                                    List<String> l_List = new ArrayList<String>();


                                    String point = contour;
                                    Log.v("points", point + "");
                                    if (point != null) {
                                        l_List = Arrays.asList(point.split("-"));
                                    }

                                    if (l_List.size() > 1) {

                                        for (int j = 0; j < l_List.size(); j++) {
                                            String currentString = l_List.get(j);
                                            if (currentString != null) {
                                                String[] separated = currentString.split(",");
                                                if (separated.length > 1) {
                                                    String la = separated[0];
                                                    String lo = separated[1];
                                                    points.add(new LatLng(Double.valueOf(la), Double.valueOf(lo)));
                                                }
                                            }
                                        }
                                    }


                                }
                            }
                            while (c.moveToNext());
                        }
                        db.close();
                        checkfirsttime = true;
                        loadProfileData(f_id);
                        // gotoHomeScreen();

                    }
                } else {
                    setToastServerError(MainProfileActivity.this);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                SaveLocalFile(db, MainProfileActivity.this, SN_MainProfileActivity, getFarmURL, response, e.toString(), "" + seconds, AppConstant.farm_id, "Error");
                System.out.println("catch block Pls Try again");
            }

            progressDialog.dismiss();

        }
    }

    String getYoutubeURL;

    private class getYouTubeLiveVideoAsyncTask extends AsyncTask<Void, Void, String> {
        String UserID;
        String result = "";
        ProgressDialog progressDialog;

        public getYouTubeLiveVideoAsyncTask(String userid) {
            //  this.data = data;
            UserID = userid;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainProfileActivity.this);
            progressDialog.setMessage(getDynamicLanguageValue(getApplicationContext(), "Dataisloading", R.string.Dataisloading));
            progressDialog.setCancelable(false);
            progressDialog.show();
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
            try {
//                FarmID = "236557";
                mRequestStartTime = System.currentTimeMillis();
                getYoutubeURL = AppManager.getInstance().getYoutubeStreams(UserID);
                Log.d("get farm url", getYoutubeURL);
                String response = AppManager.getInstance().httpRequestGetMethod(getYoutubeURL);
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
            int seconds = getAPIimeResponseinSecond(mRequestStartTime);
            progressDialog.dismiss();
            try {
                if (response != null &&
                        !response.equalsIgnoreCase("Could not connect to server")) {
                    if (!response.contains("Not Found")) {
                        JSONArray json = new JSONArray(response.toString());
                        if (json.length() > 0) {
//                        db.insertYoutubeVideoDateTime(json);
                            insertYoutubeVideoDateTime(json);
                        }
                    } else {
                        db.open();
                        String URLdelete = "delete from " + TABLE_YoutubeVideoDateTime + "";
                        db.getSQLiteDatabase().execSQL(URLdelete);
                        YoutubeVideolist = new ArrayList<>();
                        VideoID = null;
                    }
                } else {
                    setToastServerError(MainProfileActivity.this);
                }
            } catch (Exception e) {
                e.printStackTrace();
                SaveLocalFile(db, MainProfileActivity.this, SN_MainProfileActivity, getYoutubeURL, response, "JSON Response Error", "" + seconds, AppConstant.farm_id, "Error");
                System.out.println("catch block Pls Try again");
            }


        }
    }

    private class FarmData {

        String farmerName;
        double latitude;
        double longitude;

        Marker marker;


        public String getFarmerName() {
            return farmerName;
        }

        public void setFarmerName(String farmerName) {
            this.farmerName = farmerName;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }


        public Marker getMarker() {
            return marker;
        }

        public void setMarker(Marker marker) {
            this.marker = marker;
        }
    }


    private class getUpdateCropName extends AsyncTask<Void, Void, String> {
        String result = "";
        TransparentProgressDialog progressDialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new TransparentProgressDialog(
                    MainProfileActivity.this, getDynamicLanguageValue(getApplicationContext(), "loadingInformation", R.string.loadingInformation));
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
            try {
                String language = AppManager.getInstance().getSelectedLanguages(MainProfileActivity.this);
                mRequestStartTime = System.currentTimeMillis();
//                CropUrl = "https://myfarminfo.com/yfirest.svc/Clients/GetFarmerInfo/gfd/" + AppConstant.farm_id + "/" + AppConstant.user_id;
                CropUrl = getCropNameSowingDate(AppConstant.farm_id, AppConstant.user_id, language);
                Log.d("get farm url", CropUrl);
                String response = AppManager.getInstance().httpRequestGetMethod(CropUrl);
                response = response.replace("\\", "");
                response = response.replace("\"{", "{");
                response = response.replace("}\"", "}");

                int seconds = getAPIimeResponseinSecond(mRequestStartTime);
                if (seconds > 3) {
                    SaveLocalFile(db, MainProfileActivity.this, SN_MainProfileActivity, CropUrl, response, "", "" + seconds, AppConstant.farm_id, "Working");
                }
                return response;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null; //show network problem
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPreExecute();
            int seconds = getAPIimeResponseinSecond(mRequestStartTime);
            String CropID = "", CropName = "", Variety = "";
            try {
                ResponseCropNameSOwingDate = response;
                if (response != null) {
                    JSONObject obj = new JSONObject(response.toString());
                    JSONArray corpInfoArray = obj.getJSONArray("CropInfo");
                    if (corpInfoArray.length() > 0) {
                        JSONObject cropJsonObject = corpInfoArray.getJSONObject(0);
                        CropID = cropJsonObject.getString("CropID");
                        CropName = cropJsonObject.getString("CropName");
                        Variety = cropJsonObject.getString("Variety");

                        AppConstant.selected_cropId = CropID;
                        AppConstant.selected_crop = CropName;
                        AppConstant.selected_variety = Variety;
                        setCropname();
//                        txt_ProfileCrop.setText(AppConstant.selected_crop);
                    }

                } else {
                    setToastServerError(MainProfileActivity.this);
                }

            } catch (JSONException e) {
                SaveLocalFile(db, MainProfileActivity.this, SN_MainProfileActivity, CropUrl, response, "JSON Response Error", "" + seconds, AppConstant.farm_id, "Error");
                e.printStackTrace();
            } catch (Exception ex) {
                SaveLocalFile(db, MainProfileActivity.this, SN_MainProfileActivity, CropUrl, response, "JSON Response Error", "" + seconds, AppConstant.farm_id, "Error");
                ex.printStackTrace();
            }
            try {
                progressDialog.dismiss();
                setfarmeridSelection();
            } catch (Exception ex) {
                progressDialog.dismiss();
                ex.printStackTrace();
            }
        }
    }

    public void setNotification(View view) {
        ArrayList<String> value = NotificationCountSMS.getNotificationValueData(MainProfileActivity.this);
        if (value.size() > 0) {
            openPopUp(view);
        } else {
            getDynamicLanguageToast(getApplicationContext(), "ThereisnoNotification", R.string.ThereisnoNotification);
        }
    }

    public void openPopUp(View v) {
        List list = new ArrayList();
        ArrayList<NotificationBean> mDataset = new ArrayList<NotificationBean>();
        ArrayList<NotificationData> dataSet = new ArrayList<>();
        ArrayList<String> value = NotificationCountSMS.getNotificationValueData(this);
        value.removeAll(Collections.singleton(null));
        value.removeAll(Collections.singleton(""));
        ArrayList<SpannableString> value1 = new ArrayList<SpannableString>();
//       Messgae + "#" + notftytype + "#" + StepID + "#" + FarmID + "#" + Title + "#" + Utility.getdate() + " " + Utility.gettime());
        try {
            for (int i = 0; i < value.size(); i++) {
                String[] data = value.get(i).split("#");
                String time = Utility.ConvertdatedifferenceDays(Utility.getdate() + " " + gettime(), data[5]);
                //check for normal or interactive
                String notftytype = data[1];
                boolean check7dayslessornot = Utility.checkNoofDaysislessthan7or3(Utility.getdate() + " " + gettime(), data[5], notftytype);
                //7 days Check
                if (check7dayslessornot) {
                    SpannableString c = new SpannableString(data[1] + time);
                    String lenght = time;
//                String lenght = data[0] + time; //due to length is too long index bound exception
                    c.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.black)), 0, lenght.length(), 0);
                    value1.add(c);
//            String Currendate = Utility.getdate();
//            if (data[1].contains(Currendate) || data[0].contains("Friend request has received"))
                    dataSet.add(new NotificationData(data[0], time));

                    NotificationBean setdata = new NotificationBean();
                    try {
                        setdata.setMessgae(data[0]);
                        setdata.setNotftytype(data[1]);
                        setdata.setStepID(data[2]);
                        setdata.setFarmID(data[3]);
                        setdata.setTitle(data[4]);
                        setdata.setDateTime(time);
                        setdata.setDateTimeHHMMSS(data[5]);
                        try {
                            setdata.setNotifImageURL(data[6]);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        try {
                            if (data.length > 7) {
                                setdata.setFeedbackStatus(data[7]);
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    mDataset.add(setdata);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.notificationpopup, null);

//        PopupWindow mPopupWindow = new PopupWindow(
        mPopupWindow = new PopupWindow(
                customView,
                (int) getResources().getDimension(R.dimen._220sdp),
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        if (Build.VERSION.SDK_INT >= 21) {
            mPopupWindow.setElevation(5.0f);
        }
        mPopupWindow.setOutsideTouchable(true);
//        NotificationCustomAdapter adapternotifcation = new NotificationCustomAdapter(dataSet, MainProfileActivity.this, mDataset);
        NotificationCustomAdapter adapternotifcation = new NotificationCustomAdapter(dataSet, MainProfileActivity.this, mDataset, mPopupWindow, MainProfileActivity.this);
        ListView lv = (ListView) customView.findViewById(R.id.lv);
        LinearLayout mLinearLayout = (LinearLayout) customView.findViewById(R.id.rl_custom_layout);
        TextView tv = (TextView) customView.findViewById(R.id.tv);
        setDynamicLanguage(this, tv, "mfinotification", R.string.mfinotification);
        setFontsStyleTxt(this, tv, 2);
        lv.setAdapter(adapternotifcation);

        mPopupWindow.showAtLocation(mLinearLayout, Gravity.NO_GRAVITY, (int) (v.getX() + getResources().getDimension(R.dimen._60sdp)), (int) (v.getY() + getResources().getDimension(R.dimen._40sdp)));

    }

    @Override
    public void onBackPressed() {

//        String userTypeID = AppConstant.userTypeID;
//        if (userTypeID != null && (userTypeID.equalsIgnoreCase("1") ||
//                userTypeID.equalsIgnoreCase("2") || userTypeID.equalsIgnoreCase("18"))) {
//            finish();
//        } else {
//            Intent intent = new Intent(MainProfileActivity.this, AdminDashboard_New.class);
//            startActivity(intent);
//            finish();
//        }
//        Intent intent = new Intent(MainProfileActivity.this, AdminDashboard_New.class);
//        startActivity(intent);
        finish();

    }

    public void setWeatherCall() {
//        if (AppConstant.farm_id != null) {
        Intent in = new Intent(getApplicationContext(), NewDashboardActivity.class);
        in.putExtra("from", "weather");
        startActivity(in);
//        } else {
//            Toast.makeText(getApplicationContext(), getResources().getString(R.string.Pleaseselectyour), Toast.LENGTH_SHORT).show();
//        }
    }

    public void setPlantDoctorCall() {
//        if (AppConstant.farm_id != null) {
//            Intent in = new Intent(MainProfileActivity.this, LiveCottonActivity.class);
//            in.putExtra("data", "dash");
//            startActivity(in);
        Intent in = new Intent(MainProfileActivity.this, PlantDoctors.class);
        startActivity(in);
//        } else {
//            Toast.makeText(getApplicationContext(), getResources().getString(R.string.Pleaseselectyour), Toast.LENGTH_SHORT).show();
//        }
    }

    public void setSoilDoctorCall() {
        if (AppConstant.farm_id != null) {
            Intent in = new Intent(getApplicationContext(), NewDashboardActivity.class);
            in.putExtra("from", "soil");
            startActivity(in);
        } else {
            setToastPleaseselectyour(MainProfileActivity.this);
        }
    }

    public void setCropInformationCall() {
        if (AppConstant.farm_id != null) {
            cropMgmtPopupMethod();
        } else {
            setToastPleaseselectyour(MainProfileActivity.this);
        }
    }

    public void setPOPCall(String flag) {
        if (AppConstant.farm_id != null) {
            if (ResponseCropNameSOwingDate != null && ResponseCropNameSOwingDate.length() > 0) {
                String SowDate = "";
                Intent in = new Intent(getApplicationContext(), NewDashboardActivity.class);
                in.putExtra("from", "pop");
                in.putExtra("OnlyCurrentStatus", flag);
                in.putExtra("SelectedCropID", AppConstant.selected_cropId);
                try {
                    if (ResponseCropNameSOwingDate != null) {
                        JSONObject obj = new JSONObject(ResponseCropNameSOwingDate.toString());
                        JSONArray corpInfoArray = obj.getJSONArray("CropInfo");

                        for (int i = 0; i < corpInfoArray.length(); i++) {
                            JSONObject cropJsonObject = corpInfoArray.getJSONObject(i);
                            SowDate = cropJsonObject.getString("SowDate");
                            if (SowDate.length() > 0 && SowDate != null)
                                break;
                        }

                    } else {
                        setToastServerError(MainProfileActivity.this);
                    }
                    if (AppConstant.farm_id != null && AppConstant.user_id != null) {
                        in.putExtra("SowDate", SowDate);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    if (AppConstant.farm_id != null && AppConstant.user_id != null) {
                        in.putExtra("SowDate", SowDate);
                    }
                }
                startActivity(in);
            }
//            new getSowingDate(flag).execute();
        } else {
            setToastPleaseselectyour(MainProfileActivity.this);
        }
    }

    public void setEmergencyCall() {
        if (AppConstant.farm_id != null) {
            emergencyMethod();
        } else {
            setToastPleaseselectyour(MainProfileActivity.this);
        }
    }

    public void setHealthVaultCall() {
        setToastComingsoon(MainProfileActivity.this);
    }

    public void setMarketPlace() {
        if (AppConstant.farm_id != null) {
//            Intent intent = new Intent(MainProfileActivity.this, Calculators.class);
            Intent intent = new Intent(MainProfileActivity.this, NewProductActivity.class);
            startActivity(intent);
        } else {
            setToastPleaseselectyour(MainProfileActivity.this);
        }
    }

    public void setPolicyRegistration(int flag) {
        if (AppConstant.farm_id != null) {
//            Intent intent = new Intent(MainProfileActivity.this, Calculators.class);
//            new getFarmListAsyncTask(1).execute();
//            Intent intent = new Intent(MainProfileActivity.this, PolicyList.class);
//            startActivity(intent);
            loadFarmDetails(flag);
        } else {
            setToastPleaseselectyour(MainProfileActivity.this);
        }
    }

    public void GroundWater() {
        if (AppConstant.farm_id != null) {
            Intent intent = new Intent(MainProfileActivity.this, GroundWaterForecasting.class);
            intent.putExtra("lat", lat);
            intent.putExtra("lon", lon);
            startActivity(intent);
        } else {
            setToastPleaseselectyour(MainProfileActivity.this);
        }
    }


    public void setCalculatorCall() {
        if (AppConstant.farm_id != null) {
            Intent intent = new Intent(MainProfileActivity.this, FertilizerCalculator.class);
            startActivity(intent);
        } else {
            setToastPleaseselectyour(MainProfileActivity.this);
        }
    }

    public void setMandiInformation() {
        if (AppConstant.farm_id != null) {
            Intent intent = new Intent(MainProfileActivity.this, MandiInformation.class);
            startActivity(intent);
        } else {
            setToastPleaseselectyour(MainProfileActivity.this);
        }
    }

    public void setTubewellCall() {
        if (AppConstant.farm_id != null) {
            Intent intent = new Intent(MainProfileActivity.this, TubewellMainActivity.class);
            startActivity(intent);
        } else {
            setToastPleaseselectyour(MainProfileActivity.this);
        }
    }

    //getJsonfotTokenUpdate
    public void getJsonUploadforTokenUpdate() {
        JSONObject object = new JSONObject();
        try {
            object.putOpt("UserID", AppConstant.user_id);
            object.putOpt("DeviceToken", AppConstant.RegisterDeviceTokenKey);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        uploadUserTkenDeviceID(this, object);
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (db != null) {
            db = new DBAdapter(this);
        }
        setCropname();
//        txt_ProfileCrop.setText(AppConstant.selected_crop);
        mPopupWindow.dismiss();

//        setNotificationCount();
//
//        Log.d("OnPause Method", "OnPause Method called");
//        setScreenTracking(this, db, SN_MainProfileActivity, UID);
//        //Youtubevideo call update
//       checktodayyoutubevideoexistornot();
//
//        setBanner(0);
//        setLanguages();
//       // downloadLocalTranslation();
//        if (checkLanguage == true) {
//            if (AppConstant.farm_id != null && !AppConstant.farm_id.equalsIgnoreCase("0")) {
//                loadProfileData(AppConstant.farm_id);
//            }
//        }
//        userfarmList = new ArrayList<>();


        if(MessageTypeFinal.equalsIgnoreCase("forecast")){
            MessageTypeFinal = "";
            setWeatherCall();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (db != null) {
            db = new DBAdapter(this);
        }
        Log.d("OnPause Method", "OnPause Method called");
        setScreenTracking(this, db, SN_MainProfileActivity, UID);
    }

    @Override
    protected void onStop() {
        super.onStop();
        NotificationCountSMS.getNotificationCount(this);
        mPopupWindow.dismiss();

        if (db != null) {
            db = new DBAdapter(this);
        }
        Log.d("onStop Method", "onStop Method called");
        setScreenTracking(this, db, SN_MainProfileActivity, UID);
    }

    private void setIdDefine() {

        hsv = (HorizontalScrollView) findViewById(R.id.hsv);
        btn_left = (ImageView) findViewById(R.id.btn_left);
        btn_right = (ImageView) findViewById(R.id.btn_right);

        tblrw_progressbar = (TableRow) findViewById(R.id.tblrw_progressbar);
        progressbarFarmScore = (ProgressBar) findViewById(R.id.progressbarFarmScore);
        txt_FarmScoreValue = (TextView) findViewById(R.id.txt_FarmScoreValue);

        cardview_CropInform = (CardView) findViewById(R.id.cardview_CropInform);
        cardview_Popupdate = (CardView) findViewById(R.id.cardview_Popupdate);
        cardview_Plantdoctor = (CardView) findViewById(R.id.cardview_Plantdoctor);
        cardview_weather = (CardView) findViewById(R.id.cardview_weather);
        cardview_soildoctor = (CardView) findViewById(R.id.cardview_soildoctor);
        cardview_MarketPlace = (CardView) findViewById(R.id.cardview_MarketPlace);
        cardview_PolicyRegistration = (CardView) findViewById(R.id.cardview_PolicyRegistration);
        cardview_Tubewell = (CardView) findViewById(R.id.cardview_Tubewell);
        cardview_Groundwaterforecadting = (CardView) findViewById(R.id.cardview_Groundwaterforecadting);


        txt_Profilename = (TextView) findViewById(R.id.txt_Profilename);
        txt_ProfileCrop = (TextView) findViewById(R.id.txt_ProfileCrop);
        tv_badge = (TextView) findViewById(R.id.tv_badge);

        txt_farmerselected = (TextView) findViewById(R.id.txt_farmerselected);

        txt_weather = (TextView) findViewById(R.id.txt_weather);
        txt_soildoctor = (TextView) findViewById(R.id.txt_soildoctor);
        txt_Popupdate = (TextView) findViewById(R.id.txt_Popupdate);
        txt_Plantdoctor = (TextView) findViewById(R.id.txt_Plantdoctor);
        txt_CropInform = (TextView) findViewById(R.id.txt_CropInform);
        txt_MarketPlace = (TextView) findViewById(R.id.txt_MarketPlace);
        txt_PolicyRegistration = (TextView) findViewById(R.id.txt_PolicyRegistration);
        txt_Tubewell = (TextView) findViewById(R.id.txt_Tubewell);
        txt_Groundwaterforecadting = (TextView) findViewById(R.id.txt_Groundwaterforecadting);

        txt_POPHeading = (TextView) findViewById(R.id.txt_POPHeading);
        txt_AdvisoryMessages = (TextView) findViewById(R.id.txt_AdvisoryMessages);
        txt_POPTitle = (TextView) findViewById(R.id.txt_POPTitle);
        txt_POPsms = (TextView) findViewById(R.id.txt_POPsms);
        txt_CropHeading = (TextView) findViewById(R.id.txt_CropHeading);
        txt_CropHeading_new = (TextView) findViewById(R.id.txt_CropHeading_new);
        txt_Cropsms = (TextView) findViewById(R.id.txt_Cropsms);
        txt_NextStepHeading = (TextView) findViewById(R.id.txt_NextStepHeading);
        txt_NextStepsms1 = (TextView) findViewById(R.id.txt_NextStepsms1);
        txt_NextStepsms2 = (TextView) findViewById(R.id.txt_NextStepsms2);
        txt_NextStepsms3 = (TextView) findViewById(R.id.txt_NextStepsms3);
        txt_NextStepsms4 = (TextView) findViewById(R.id.txt_NextStepsms4);
        txt_weatherforcastHeading = (TextView) findViewById(R.id.txt_weatherforcastHeading);
        txt_weatherforcastsms = (TextView) findViewById(R.id.txt_weatherforcastsms);
        txt_PestDiseaseHeading = (TextView) findViewById(R.id.txt_PestDiseaseHeading);
        txt_irrigationadvisoryHeading = (TextView) findViewById(R.id.txt_irrigationadvisoryHeading);
        txt_irrigationadvisorysms = (TextView) findViewById(R.id.txt_irrigationadvisorysms);
        txt_FarmScore = (TextView) findViewById(R.id.txt_FarmScore);
        txt_farmScore = (TextView) findViewById(R.id.txt_farmScore);

        imageview_cropthumbup = (Button) findViewById(R.id.imageview_cropthumbup);
        imageview_cropthumbup_new = (Button) findViewById(R.id.imageview_cropthumbup_new);

        cardvieemergency = (ImageView) findViewById(R.id.cardvieemergency);
        logoutBTN = (ImageView) findViewById(R.id.logout_lay);
        imgvw_setting = (ImageView) findViewById(R.id.imgvw_setting);
        imageview_weather = (ImageView) findViewById(R.id.imageview_weather);
        imageview_Plantdoctor = (ImageView) findViewById(R.id.imageview_Plantdoctor);
        imageview_soildoctor = (ImageView) findViewById(R.id.imageview_soildoctor);
        imageview_CropInform = (ImageView) findViewById(R.id.imageview_CropInform);
        imageview_Popupdate = (ImageView) findViewById(R.id.imageview_Popupdate);
        imgvw_MarketPlace_tab = (ImageView) findViewById(R.id.imgvw_MarketPlace_tab);
        imgvw_PolicyRegistration_tab = (ImageView) findViewById(R.id.imgvw_PolicyRegistration_tab);
        imgvw_Groundwaterforecadting_tab = (ImageView) findViewById(R.id.imgvw_Groundwaterforecadting_tab);
        imgvw_Tubewell_tab = (ImageView) findViewById(R.id.imgvw_Tubewell_tab);
        imgvw_HealthVault_tab = (ImageView) findViewById(R.id.imgvw_HealthVault_tab);
        cardview_sowingdate = (ImageView) findViewById(R.id.cardview_sowingdate);

        ll_weather = (LinearLayout) findViewById(R.id.ll_weather);
        ll_Plantdoctor = (LinearLayout) findViewById(R.id.ll_Plantdoctor);
        ll_soildoctor = (LinearLayout) findViewById(R.id.ll_soildoctor);
        ll_CropInform = (LinearLayout) findViewById(R.id.ll_CropInform);
        ll_Popupdate = (LinearLayout) findViewById(R.id.ll_Popupdate);
        HealthVault_tab = (LinearLayout) findViewById(R.id.HealthVault_tab);
        MarketPlace_tab = (LinearLayout) findViewById(R.id.MarketPlace_tab);
        PolicyRegistration_tab = (LinearLayout) findViewById(R.id.PolicyRegistration_tab);
        Tubewell_tab = (LinearLayout) findViewById(R.id.Tubewell_tab);
        ll_Groundwaterforecadting_tab = (LinearLayout) findViewById(R.id.ll_Groundwaterforecadting_tab);

        edit_farmerID = (ImageView) findViewById(R.id.edit_farmerID);
        editFram = (ImageView) findViewById(R.id.edit_farm);
        txt_HealthVault = (TextView) findViewById(R.id.txt_HealthVault);
        POPUpdates_btn = (TextView) findViewById(R.id.POPUpdates_btn);


        audio_sound_btn_POPUpdates = (Button) findViewById(R.id.audio_sound_btn_POPUpdates);
        audio_sound_btn_POPUpdates_mute = (Button) findViewById(R.id.audio_sound_btn_POPUpdates_mute);
        audio_sound_btn_cropstatus = (Button) findViewById(R.id.audio_sound_btn_cropstatus);
        audio_sound_btn_nextstep = (Button) findViewById(R.id.audio_sound_btn_nextstep);
        audio_sound_btn_weatherforecast = (Button) findViewById(R.id.audio_sound_btn_weatherforecast);
        audio_sound_btn_pestdiease = (Button) findViewById(R.id.audio_sound_btn_pestdiease);
        audio_sound_btn_irrigation = (Button) findViewById(R.id.audio_sound_btn_irrigation);
        audio_sound_btn_cropstatus_mute = (Button) findViewById(R.id.audio_sound_btn_cropstatus_mute);
        audio_sound_btn_nextstep_mute = (Button) findViewById(R.id.audio_sound_btn_nextstep_mute);
        audio_sound_btn_weatherforecast_mute = (Button) findViewById(R.id.audio_sound_btn_weatherforecast_mute);
        audio_sound_btn_pestdiease_mute = (Button) findViewById(R.id.audio_sound_btn_pestdiease_mute);
        audio_sound_btn_irrigation_mute = (Button) findViewById(R.id.audio_sound_btn_irrigation_mute);
        recycler_pestdesease = (RecyclerView) findViewById(R.id.recycler_pestdesease);


        audio_sound_btn_FarmScore = (Button) findViewById(R.id.audio_sound_btn_FarmScore);
        audio_sound_btn_FarmScore_mute = (Button) findViewById(R.id.audio_sound_btn_FarmScore_mute);

        cardview_POP = (CardView) findViewById(R.id.cardview_POP);
        cardview_Crop = (CardView) findViewById(R.id.cardview_Crop);
        cardview_Crop_new = (CardView) findViewById(R.id.cardview_Crop_new);
        cardview_NextStep = (CardView) findViewById(R.id.cardview_NextStep);
        cardview_weatherforcast = (CardView) findViewById(R.id.cardview_weatherforcast);
        cardview_PestDisease = (CardView) findViewById(R.id.cardview_PestDisease);
        cardview_irrigationadvisory = (CardView) findViewById(R.id.cardview_irrigationadvisory);
        cardview_FarmScore = (CardView) findViewById(R.id.cardview_FarmScore);

        cardviewlanguage = (ImageView) findViewById(R.id.cardviewlanguage);
        notificationbell = (RelativeLayout) findViewById(R.id.notificationbell);
        imgnotification = (ImageView) findViewById(R.id.imgnotification);

        imgeview_Youtube = (ImageView) findViewById(R.id.imgeview_Youtube);
        imageviewBanner = (ImageView) findViewById(R.id.imageviewBanner);
        imageviewLogo = (ImageView) findViewById(R.id.imageviewLogo);

        recycleview_SMSList = (RecyclerView) findViewById(R.id.recycleview_SMSList);
        ll_AdvisoryMessages = (LinearLayout) findViewById(R.id.ll_AdvisoryMessages);

    }


    private void setCropname() {
        String Dynamicword = "", Cropname = "";
        Cropname = AppConstant.selected_crop;
//        if (CropConditionCheck != null && CropConditionCheck.length() > 0) {
//            if (CropConditionCheck.equalsIgnoreCase("Normal")) {
//                Dynamicword = getResources().getString(R.string.str_rb_normal);
//            } else if (CropConditionCheck.equalsIgnoreCase("Under Stress")) {
//                Dynamicword = getResources().getString(R.string.UnderStress);
//            } else {
//                Dynamicword = CropConditionCheck;
//            }
//        }
//        String SMS = getResources().getString(R.string.YourFarms) + " " + Cropname + " " +
//                getResources().getString(R.string.cropcondition) + Dynamicword +
//                " " + getResources().getString(R.string.Secufarmwants) + Cropname + getResources().getString(R.string.Sopleasefollow);

        txt_ProfileCrop.setText(Cropname);
    }

    public void selectImagePopup() {

        //final Dialog dialog = new Dialog(OtherUserProfile.this,android.R.style.Theme_Translucent_NoTitleBar);
        final Dialog dialog = new Dialog(this);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        Window window = dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.CENTER;
        wlp.dimAmount = 0.7f;
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        // wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);

        // Include dialog.xml file
        dialog.setContentView(R.layout.popdashbaordfarmselection);

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        Button btn_cross = (Button) dialog.findViewById(R.id.btn_cross);
        ImageView farmer_refresh = (ImageView) dialog.findViewById(R.id.farmer_refresh);

        TextView title = (TextView) dialog.findViewById(R.id.title);
        TextView txt_Pleaseselectyour = (TextView) dialog.findViewById(R.id.txt_Pleaseselectyour);

        header_chooseFarmSpinner = (CustomSearchableSpinner) dialog.findViewById(R.id.header_chooseFarmSpinner);

        setDynamicLanguage(this, title, "SelectFarm", R.string.SelectFarm);
        setDynamicLanguage(this, txt_Pleaseselectyour, "Pleaseselectyour", R.string.Pleaseselectyour);

        setFontsStyleTxt(this, title, 2);
        setFontsStyleTxt(this, txt_Pleaseselectyour, 5);

        try {
            ArrayAdapter<String> chooseYourFarmAdapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, str);
            header_chooseFarmSpinner.setAdapter(chooseYourFarmAdapter);
            setCustomSearchableSpinner(getApplicationContext(), header_chooseFarmSpinner, "SelectFarm", R.string.SelectFarm);
//            header_chooseFarmSpinner.setTitle(getDynamicLanguageValue(getApplicationContext(), "SelectFarm", R.string.SelectFarm));
//            header_chooseFarmSpinner.setPositiveButton(getDynamicLanguageValue(getApplicationContext(), "Cancel", R.string.Cancel));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
//        header_chooseFarmSpinner.setSelection(selectecposition);


        farmer_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sdfg
                dialog.cancel();
                new getFarmListAsyncTask(0).execute();
            }
        });


        header_chooseFarmSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                                               @Override
                                                               public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                                   if (position > 0) {
                                                                       checkfirsttime = false;
                                                                       selectecposition = position;
                                                                       setFarmIDSelection(position);

                                                                       selectedFarmLat = farmList.get(position-1).getCenterLat()+"";
                                                                       selectedFarmLong = farmList.get(position-1).getCenterLon()+"";
                                                                       dialog.cancel();
                                                                   }

                                                               }

                                                               @Override
                                                               public void onNothingSelected(AdapterView<?> parent) {

                                                               }
                                                           }

        );


        btn_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });


        dialog.show();
    }

    public void setUpdateSowingDate(String SowingDate, final String JsonResponse) {

        //final Dialog dialog = new Dialog(OtherUserProfile.this,android.R.style.Theme_Translucent_NoTitleBar);
        final Dialog dialog = new Dialog(this);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        Window window = dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.CENTER;
        wlp.dimAmount = 0.7f;
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        // wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);

        // Include dialog.xml file
        dialog.setContentView(R.layout.pop_updatesowingdate);

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        Button btn_cross = (Button) dialog.findViewById(R.id.btn_cross);
        Button btnSubmit = (Button) dialog.findViewById(R.id.btnSubmit);
        TextView title = (TextView) dialog.findViewById(R.id.title);
        TextView txt_SowingDate = (TextView) dialog.findViewById(R.id.txt_SowingDate);
        final TextView txt_sowingdate = (TextView) dialog.findViewById(R.id.txt_sowingdate);
        final ImageView imgeviewsowingdate = (ImageView) dialog.findViewById(R.id.imgeviewsowingdate);

        header_chooseFarmSpinner = (CustomSearchableSpinner) dialog.findViewById(R.id.header_chooseFarmSpinner);

        setDynamicLanguage(this, title, "UpdasteSowingdate", R.string.UpdasteSowingdate);
        setDynamicLanguage(this, txt_SowingDate, "SowingDate", R.string.SowingDate);
        setDynamicLanguage(this, btnSubmit, "Submit", R.string.Submit);

        setFontsStyleTxt(this, title, 2);
        setFontsStyleTxt(this, txt_SowingDate, 5);
        setFontsStyle(this, btnSubmit);

        if (SowingDate != null && SowingDate.length() > 0) {
            txt_sowingdate.setText(SowingDate);
        }
        txt_sowingdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.setDatePickerMaxMin(MainProfileActivity.this, txt_sowingdate, "DDMMYYYY", txt_sowingdate.getText().toString());
            }
        });
        imgeviewsowingdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.setDatePickerMaxMin(MainProfileActivity.this, txt_sowingdate, "DDMMYYYY", txt_sowingdate.getText().toString());
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
                if (txt_sowingdate.getText().toString() != null && txt_sowingdate.getText().toString().length() > 0) {
                    new sentRequestForFarmSave(JsonResponse, txt_sowingdate.getText().toString()).execute();
                }
            }
        });

        btn_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });


        dialog.show();
    }
    //YoutubeliveStream

    public void checktodayyoutubevideoexistornot() {
        try {
            String TodayDate = Utility.getdate();
//        TodayDate = "03-06-2020";

            db.open();
            ArrayList<HashMap<String, String>> hasmap = new ArrayList<>();
//        String SQL = "Select * from  tblYOutubeVideoDateTime where DateFrom='" + TodayDate + "'";
//            String SQL = "Select (select date('now'))as TodayDate,date(DateFrom) as DateFrom,DateFromTime,date(DateTo) as DateTo,DateToTime,VideoID,ProjectID  from tblYoutubeVideoDateTime where (TodayDate  between DateFrom AND DateTo)";
           String SQL = "Select * from  tblYOutubeVideoDateTime";

            hasmap = db.getDynamicTableValue(SQL);
            if (hasmap.size() > 0) {
                for (int i = 0; i < hasmap.size(); i++) {
                    HashMap<String, String> hashMap = new HashMap<>();
                    Title = hasmap.get(i).get("Title");
                    Description = hasmap.get(i).get("Description");
                    DateFrom = hasmap.get(i).get("DateFrom");
                    DateFromTime = hasmap.get(i).get("DateFromTime");
                    DateTo = hasmap.get(i).get("DateTo");
                    DateToTime = hasmap.get(i).get("DateToTime");
                    ProjectID = hasmap.get(i).get("ProjectID");
                    VideoID = hasmap.get(i).get("VideoID");
                    VisibleName = hasmap.get(i).get("VisibleName");
                    LogDate = hasmap.get(i).get("UploadedDate");
                }
//                if (DateFrom != null) {
//                    TimerCheck();
//                }
            }
            YoutubeVideolist = new ArrayList<>();
//            String SQL1 = "Select VideoID,Title  from tblYoutubeVideoDateTime where VideoID!='" + VideoID + "'";
//            YoutubeVideolist = db.getDynamicTableValue(SQL1);

            YoutubeVideolist.addAll(hasmap);


        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    //YoutubeLiveVideo
    String Title = null, Description = null, DateFrom = null, DateFromTime = null, DateTo = null,
            DateToTime = null, ProjectID = null, VideoID = null, VisibleName = null, LogDate = null;
    ArrayList<HashMap<String, String>> YoutubeVideolist = new ArrayList<>();

    public void insertYoutubeVideoDateTime(JSONArray jsonArray) {
        try {
            db.open();
            String SQL = "select max(ID)+1 from tblYoutubeVideoDateTime";
            int ID = db.getMaxRecord(SQL);
            String date = Utility.getdate() + " " + gettime();
            String IDs = "", Titles = "", Descriptions = "", DateFroms = "", DateFrom_time = "",
                    DateTos = "", DateTo_time = "", ProjectIDs = "", VideoIDs = "", VisibleNames = "", LogDates= "";

            if (jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        JSONObject ob = jsonArray.getJSONObject(i);
                        IDs = ob.getString("ID");
                        Titles = ob.getString("Title");
                        Descriptions = ob.getString("Description");
                        DateFroms = ob.getString("DateFrom");
                        DateFroms = DateFroms.replace("T", " ");
//                        DateFroms = getDateFormatChanged(DateFroms, "DDMMYYYY");
                        String DATETIME[] = null;
                        if (DateFroms.contains(" ")) {
                            DATETIME = DateFroms.split(" ");
                            DateFroms = DATETIME[0];
                            DateFrom_time = DATETIME[1];
                        }
                        DateTos = ob.getString("DateTo");
                        DateTos = DateTos.replace("T", " ");
//                        DateTos = getDateFormatChanged(DateTos, "DDMMYYYY");
                        if (DateTos.contains(" ")) {
                            DATETIME = DateTos.split(" ");
                            DateTos = DATETIME[0];
                            DateTo_time = DATETIME[1];
//                            DateTo_time = "12:00:00";
                        }
                        ProjectIDs = ob.getString("ProjectID");
                        VideoIDs = ob.getString("VideoID");
                        LogDates = ob.getString("LogDate");
//                        VisibleNames = ob.getString("VisibleName");

                        String SQL_count = "select count(*) from tblYoutubeVideoDateTime where DateFrom='" + DateFroms + "' and DateFromTime='" + DateFrom_time + "' and VideoID='" + VideoIDs + "'";
                        int count = db.getMaxRecord(SQL_count);
                        if (count == 0) {
                            String URL = "insert into " + TABLE_YoutubeVideoDateTime + " (Title,Description,DateFrom,DateFromTime,DateTo,DateToTime,ProjectID,VideoID,UploadedDate,VisibleName) values('" +
                                    Titles + "','" + Descriptions + "','" + DateFroms + "','" + DateFrom_time + "','" + DateTos + "','" + DateTo_time + "','" + ProjectIDs + "','" + VideoIDs + "','" + LogDates + "','" +VisibleNames + "')";
                            db.getSQLiteDatabase().execSQL(URL);
                        }
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }
                }
                //call for update
                checktodayyoutubevideoexistornot();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    //Youtube Timmer Check

    Thread startThread = null;
    Thread closethread = null;

    public void TimerCheck() {

        String TodayDate = getdateYYYYMMDD();
//        String TodayDate = "2020-06-07";
        if (!DateTo.equalsIgnoreCase(TodayDate)) {
            imgeview_Youtube.setVisibility(View.VISIBLE);
        } else {
            imgeview_Youtube.setVisibility(View.GONE);
            //Thread for STart check
            Runnable startRunnableThread = new CountDownRunnerForStart();
            startThread = new Thread(startRunnableThread);
            startThread.start();
        }


    }

    public void doWorkstart() {
        runOnUiThread(new Runnable() {
            public void run() {
                try {
                    String CurrentTime = gettime();
                    String fromtime = DateFromTime;
                    String totime = DateToTime;


                    long millse_start = Utility.reversetime(fromtime).getTime() - Utility.reversetime(CurrentTime).getTime();
//                    long mills_start = Math.abs(millse_start);
                    long mills_start = (int) millse_start;
                    int Hours_start = (int) (mills_start / (1000 * 60 * 60));
                    int Mins_start = (int) (mills_start / (1000 * 60)) % 60;
                    long Secs_start = (int) (mills_start / 1000) % 60;


                    long millse_close = Utility.reversetime(totime).getTime() - Utility.reversetime(CurrentTime).getTime();
//                    long mills_close = Math.abs(millse_close);
                    int mills_close = (int) millse_close;
                    int Hours_close = (int) (mills_close / (1000 * 60 * 60));
                    int Mins_close = (int) (mills_close / (1000 * 60)) % 60;
                    long Secs_close = (int) (mills_close / 1000) % 60;

//                    String TodayDate = Utility.getdateYYYYMMDD();
//                    String TodayDate = "2020-06-07";
//                    if (!DateTo.equalsIgnoreCase(TodayDate)) {
//                        imgeview_Youtube.setVisibility(View.VISIBLE);
//                    } else {
                    if ((Hours_start == 0 && Mins_start <= 15) || (Hours_close >= 0 && Mins_close > 0)) {
//                    if (Mins_start < 35) {//for Testing
                        imgeview_Youtube.setVisibility(View.VISIBLE);
                    } else {
//                    } else if (Mins_close < 10) {//for testing
                        imgeview_Youtube.setVisibility(View.GONE);
                        startThread.stop();
                    }
//                    }

                } catch (Exception e) {

                }
            }
        });
    }

    class CountDownRunnerForStart implements Runnable {
        // @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    doWorkstart();
//                    Thread.sleep(1000); // Pause of 1 Second
                    Thread.sleep(600000); // Pause of 10 mins
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (Exception e) {
                }
            }
        }
    }


    private class FarmdetailsAsynctask extends AsyncTask<Void, Void, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainProfileActivity.this);
            progressDialog.setMessage(getDynamicLanguageValue(getApplicationContext(), "Dataisloading", R.string.Dataisloading));
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(true);
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
                sendRequest = AppManager.getInstance().getFarmData + AppConstant.user_id + "/" + AppConstant.farm_id;
                String response = AppManager.getInstance().httpRequestGetMethod(sendRequest);
                return response;

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return sendRequest;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPreExecute();
            try {
                JSONObject val = new JSONObject(response);
                JSONArray arry = val.getJSONArray("CropInfo");
                JSONObject obj = new JSONObject(arry.get(0).toString());
                String SowingDate = obj.getString("CropFrom");
                if (SowingDate != null) {
                    setUpdateSowingDate(SowingDate, response);
                }

                progressDialog.dismiss();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


    public class sentRequestForFarmSave extends AsyncTask<Void, Void, String> {

        ProgressDialog progressDialog;
        String JsonResponse, SowingDATE;

        public sentRequestForFarmSave(String JSON, String SOWDate) {
            JsonResponse = JSON;
            SowingDATE = SOWDate;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainProfileActivity.this);
            progressDialog.setMessage(getDynamicLanguageValue(getApplicationContext(), "Submittingtagged", R.string.Submittingtagged));
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(true);
            progressDialog.show();

        }

        @Override
        protected String doInBackground(Void... params) {
            String response = null;
            String sendPath = AppManager.getInstance().saveFarmInfo;//Herojit Comment
            String createdString = null;
            String jsonParameterString = createJson(JsonResponse, SowingDATE);
            createdString = AppManager.getInstance().removeSpaceForUrl(jsonParameterString);
            response = AppManager.getInstance().httpRequestPutMethod(sendPath, createdString);
            System.out.println("Save Response :---" + response);
//            return response;
            return "{" + response + "}";
        }

        @Override
        protected void onPostExecute(String Response) {
            super.onPreExecute();
            progressDialog.dismiss();
            try {
                JSONObject obj = new JSONObject(Response);
                String response = obj.getString("saveFarmInfo2Result");
                if (response != null && response.contains("Success")) {
                    getDynamicLanguageToast(getApplicationContext(), "SubmittedSuccessfully", R.string.SubmittedSuccessfully);
//                    finish();
                } else if (response.contains("NotSave")) {
                    getDynamicLanguageToast(getApplicationContext(), "ServerRejected", R.string.ServerRejected);
                } else {
                    setToastServerError(MainProfileActivity.this);
                }
                Log.v("response_saved_farm", response.toString() + "");
            } catch (Exception ex) {
                ex.printStackTrace();
                setToastServerError(MainProfileActivity.this);
            }

        }
    }

    public String createJson(String JsonResponse, String SowingDATE) {
        String returnval = null;
        try {
            if (JsonResponse != null) {
                JSONObject objValue = new JSONObject();
                JSONObject val = new JSONObject(JsonResponse);
                JSONArray arry = val.getJSONArray("CropInfo");
                JSONObject obj = new JSONObject(arry.get(0).toString());

                JSONObject farminfo = new JSONObject();
                farminfo.put("FarmID", val.getString("FarmID"));
                farminfo.put("FarmName", val.getString("FarmName"));
                farminfo.put("FarmerName", val.getString("FarmerName"));
                farminfo.put("PhoneNo", val.getString("PhoneNo"));
                farminfo.put("FarmArea", val.getString("FarmArea"));
                farminfo.put("Contour", val.getString("Contour"));
                farminfo.put("Area", val.getString("Area"));
//                farminfo.put("Concerns",val.getString(""));
                farminfo.put("State", val.getString("State"));
                farminfo.put("StateName", val.getString("StateName"));
                farminfo.put("District", val.getString("District"));
                farminfo.put("Block", val.getString("Block"));
                farminfo.put("VillageStr", val.getString("VillageStr"));
                farminfo.put("VillageID", val.getString("VillageID"));
                farminfo.put("ProjectID", val.getString("ProjectID"));
                farminfo.put("TaggingApp", val.getString("TaggingApp"));
                farminfo.put("FatherName", val.getString("FatherName"));
                farminfo.put("AadharNo", val.getString("AadharNo"));
//                farminfo.put("Other",val.getString(""));
                farminfo.put("MobileType", val.getString("MobileType"));
                farminfo.put("NoOfBags", val.getString("NoOfBags"));
                farminfo.put("IBCode", val.getString("IBCode"));

                JSONArray array = new JSONArray();
                JSONObject cropinfo = new JSONObject();
                cropinfo.put("CropID", obj.getString("CropID"));
                cropinfo.put("CropName", obj.getString("CropName"));
                cropinfo.put("Variety", obj.getString("Variety"));
                cropinfo.put("BasalDoseApply", obj.getString("BasalDoseApply"));
                cropinfo.put("N", obj.getString("N"));
                cropinfo.put("P", obj.getString("P"));
                cropinfo.put("K", obj.getString("K"));
                cropinfo.put("SowDate", SowingDATE);
                cropinfo.put("CropFrom", SowingDATE);
                cropinfo.put("CropTo", obj.getString("CropTo"));
                cropinfo.put("OtherNutrient", obj.getString("OtherNutrient"));
                array.put(cropinfo);
                farminfo.put("CropInfo", array);
//                obj.put("SowDate", SowingDATE);
//                obj.put("CropFrom", SowingDATE);
//                val.put("CropInfo", obj);
//                val.put("SowingDate", SowingDATE);
                objValue.put("UserID", AppConstant.user_id);
                objValue.put("FarmInfo", farminfo.toString());

                returnval = objValue.toString();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return returnval;
    }

    public void setSowingdateDiseasecall() {
        if (AppConstant.farm_id != null) {
            String CROPNAME = AppConstant.selected_crop;
            if (CROPNAME != null && CROPNAME.length() > 3 && (CROPNAME.equalsIgnoreCase("Cotton") || CROPNAME.equalsIgnoreCase("cotton"))) {
                if (ResponseCropNameSOwingDate != null && ResponseCropNameSOwingDate.length() > 0) {
                    String SowDate = "";
                    Intent in = new Intent(MainProfileActivity.this, DiseaseDisgnosis.class);
                    try {
                        if (ResponseCropNameSOwingDate != null) {
                            JSONObject obj = new JSONObject(ResponseCropNameSOwingDate.toString());
                            JSONArray corpInfoArray = obj.getJSONArray("CropInfo");

                            for (int i = 0; i < corpInfoArray.length(); i++) {
                                JSONObject cropJsonObject = corpInfoArray.getJSONObject(i);
                                SowDate = cropJsonObject.getString("SowDate");
                                if (SowDate.length() > 0 && SowDate != null)
                                    break;
                            }

                        } else {
                            setToastServerError(MainProfileActivity.this);
                        }
                        if (AppConstant.farm_id != null && AppConstant.user_id != null) {
                            in.putExtra("SowDate", SowDate);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        if (AppConstant.farm_id != null && AppConstant.user_id != null) {
                            in.putExtra("SowDate", SowDate);
                        }
                    }
                    startActivity(in);
                }
//                new getSowingDateDiseaseDiagnosis().execute();
            } else {
                getDynamicLanguageToast(getApplicationContext(), "Dataisnotfoundforthiscrop", R.string.Dataisnotfoundforthiscrop);
            }

        } else {
            setToastPleaseselectyour(MainProfileActivity.this);
        }
    }

//    private class getSowingDateDiseaseDiagnosis extends AsyncTask<Void, Void, String> {
//
//        TransparentProgressDialog progressDialog;
//
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            super.onPreExecute();
//            progressDialog = new TransparentProgressDialog(
//                    MainProfileActivity.this, getResources().getString(R.string.loadingInformation));
//            progressDialog.setCancelable(false);
//            progressDialog.show();
//
//        }
//
//        @Override
//        protected String doInBackground(Void... params) {
//            String sendRequest = null;
//            try {
//                sendRequest = getCropNameSowingDate(AppConstant.farm_id, AppConstant.user_id);
////                sendRequest = "https://myfarminfo.com/yfirest.svc/Clients/GetFarmerInfo/gfd/" + AppConstant.farm_id + "/" + AppConstant.user_id;
//                Log.d("get farm url", sendRequest);
//                String response = AppManager.getInstance().httpRequestGetMethod(sendRequest);
//                response = response.replace("\\", "");
//                response = response.replace("\"{", "{");
//                response = response.replace("}\"", "}");
//
//                return response;
//
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//            return null; //show network problem
//        }
//
//        @Override
//        protected void onPostExecute(String response) {
//            super.onPreExecute();
//            String SowDate = "";
//            Intent in = new Intent(MainProfileActivity.this, DiseaseDisgnosis.class);
//            try {
//
//                if (response != null) {
//                    JSONObject obj = new JSONObject(response.toString());
//                    JSONArray corpInfoArray = obj.getJSONArray("CropInfo");
//
//                    for (int i = 0; i < corpInfoArray.length(); i++) {
//                        JSONObject cropJsonObject = corpInfoArray.getJSONObject(i);
//                        SowDate = cropJsonObject.getString("SowDate");
//                        if (SowDate.length() > 0 && SowDate != null)
//                            break;
//                    }
//
//                } else {
//                    Toast.makeText(MainProfileActivity.this, getResources().getString(R.string.ServerError), Toast.LENGTH_LONG).show();
//                }
//                if (AppConstant.farm_id != null && AppConstant.user_id != null) {
//                    in.putExtra("SowDate", SowDate);
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//                if (AppConstant.farm_id != null && AppConstant.user_id != null) {
//                    in.putExtra("SowDate", SowDate);
//                }
//            }
//
//            progressDialog.dismiss();
//            startActivity(in);
//
//        }
//    }

    public void setLanguages() {
        //Heading
        setFontsStyleTxt(this, txt_Profilename, 2);
        setFontsStyleTxt(this, txt_ProfileCrop, 7);
        setFontsStyleTxt(this, tv_badge, 7);
        setFontsStyleTxt(this, txt_farmerselected, 2);

        //Tab Service
        setDynamicLanguage(this, txt_soildoctor, "SoilDoctor", R.string.SoilDoctor);
        setDynamicLanguage(this, txt_Plantdoctor, "PlantDoctor", R.string.PlantDoctor);
        setDynamicLanguage(this, txt_Popupdate, "CropAdvisory", R.string.CropAdvisory);
        setDynamicLanguage(this, txt_weather, "Weather", R.string.Weather);
        setDynamicLanguage(this, txt_CropInform, "CropManagement", R.string.CropManagement);
        setDynamicLanguage(this, txt_MarketPlace, "market_place", R.string.market_place);
        setDynamicLanguage(this, txt_PolicyRegistration, "PolicyRegistration", R.string.PolicyRegistration);
        setDynamicLanguage(this, txt_Tubewell, "Tubewell", R.string.Tubewell);
        setDynamicLanguage(this, txt_HealthVault, "HealthVault", R.string.HealthVault);
        setDynamicLanguage(this, txt_Groundwaterforecadting, "Groundwaterforecadting", R.string.Groundwaterforecadting);

        setFontsStyleTxt(this, txt_soildoctor, 5);
        setFontsStyleTxt(this, txt_Plantdoctor, 5);
        setFontsStyleTxt(this, txt_Popupdate, 5);
        setFontsStyleTxt(this, txt_weather, 5);
        setFontsStyleTxt(this, txt_CropInform, 5);
        setFontsStyleTxt(this, txt_MarketPlace, 5);
        setFontsStyleTxt(this, txt_PolicyRegistration, 5);
        setFontsStyleTxt(this, txt_Tubewell, 5);
        setFontsStyleTxt(this, txt_HealthVault, 5);
        setFontsStyleTxt(this, txt_Groundwaterforecadting, 5);


        setDynamicLanguage(this, txt_POPHeading, "NextStep", R.string.NextStep);
        setDynamicLanguage(this, txt_AdvisoryMessages, "AdvisoryMessages", R.string.AdvisoryMessages);
        setDynamicLanguage(this, txt_CropHeading, "CropStatus", R.string.CropStatus);
        setDynamicLanguage(this, txt_CropHeading_new, "CropStatus", R.string.CropStatus);
        setDynamicLanguage(this, txt_PestDiseaseHeading, "PestDiseaseAlert", R.string.PestDiseaseAlert);
        setDynamicLanguage(this, txt_weatherforcastHeading, "WeatherForecast", R.string.WeatherForecast);
        setDynamicLanguage(this, txt_NextStepHeading, "CorrectiveMeasures", R.string.CorrectiveMeasures);
        setDynamicLanguage(this, txt_irrigationadvisoryHeading, "CorrectiveMeasures", R.string.CorrectiveMeasures);
        setDynamicLanguage(this, txt_FarmScore, "CorrectiveMeasures", R.string.CorrectiveMeasures);

        setFontsStyleTxt(this, txt_POPHeading, 2);
        setFontsStyleTxt(this, txt_POPTitle, 5);
        setFontsStyleTxt(this, txt_POPsms, 6);
        setFontsStyleTxt(this, txt_AdvisoryMessages, 2);
        setFontsStyleTxt(this, txt_CropHeading_new, 2);
        setFontsStyleTxt(this, txt_CropHeading, 2);
        setFontsStyleTxt(this, txt_Cropsms, 6);
        setFontsStyleTxt(this, txt_PestDiseaseHeading, 2);
        setFontsStyleTxt(this, txt_weatherforcastHeading, 2);
        setFontsStyleTxt(this, txt_weatherforcastsms, 6);
        setFontsStyleTxt(this, txt_NextStepHeading, 2);
        setFontsStyleTxt(this, txt_NextStepsms1, 6);
        setFontsStyleTxt(this, txt_NextStepsms2, 6);
        setFontsStyleTxt(this, txt_NextStepsms3, 6);
        setFontsStyleTxt(this, txt_NextStepsms4, 6);
        setFontsStyleTxt(this, txt_irrigationadvisoryHeading, 2);
        setFontsStyleTxt(this, txt_irrigationadvisorysms, 6);
        setFontsStyleTxt(this, txt_FarmScore, 2);
        setFontsStyleTxt(this, txt_farmScore, 6);


    }

    public void setBanner(int flag) {
        try {
            String language = AppManager.getInstance().getSelectedLanguages(this);
            switch (flag) {
                case 0://First time check with selected languages
                    language = AppManager.getInstance().getSelectedLanguages(this);
                    break;
                case 1://after failed to load with selected langauges then default with english
                    language = "english";
                    break;
            }
            String Cropname = AppConstant.selected_crop;
//            String ImageURL = "https://te32.s3.us-east-1.amazonaws.com/SecuFarm_Banner/Potato/" + language + "/strip_03.jpg";
//            String URL = "https://te32.s3.us-east-1.amazonaws.com/SecuFarm_Banner/Potato/hindi/Banner.jpg";
            String ImageBannerURL = "https://te32.s3.us-east-1.amazonaws.com/SecuFarm_Banner/" + Cropname + "/" + language + "/strip.jpg";
            String ImageDetailsBannerURL = "https://te32.s3.us-east-1.amazonaws.com/SecuFarm_Banner/" + Cropname + "/" + language + "/Banner.jpg";
            if (ImageBannerURL != null && ImageBannerURL.length() > 6) {
                imageviewBanner.setVisibility(View.GONE);
                Picasso.with(this).load(ImageBannerURL)
                        .placeholder(R.drawable.bannerimage)
                        .into(imageviewBanner, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {
                                imageviewBanner.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onError() {
                                imageviewBanner.setVisibility(View.GONE);
                                if (flag == 0) {
                                    setBanner(1);
                                }
                            }
                        });
            } else {
                imageviewBanner.setVisibility(View.GONE);
            }

            imageviewBanner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OpenBannerPOPUp(ImageBannerURL, ImageDetailsBannerURL);
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
            imageviewBanner.setVisibility(View.GONE);
        }
    }

    String new_ImageDetailsBannerURL = null;

    public void OpenBannerPOPUp(String BannerURL, String ImageDetailsBannerURL) {
        try {
            final Dialog dialog = new Dialog(this, R.style.DialogSlideAnim);

            //  final Dialog dialog = new Dialog(getActivity());

            dialog.setCanceledOnTouchOutside(true);
            Window window = dialog.getWindow();
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);


            WindowManager.LayoutParams wlp = window.getAttributes();
            wlp.gravity = Gravity.CENTER;
            wlp.dimAmount = 0.5f;

            dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            // wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            window.setAttributes(wlp);
            // Include dialog.xml file
            OpenbannerpopupBinding popbinding = OpenbannerpopupBinding.inflate(LayoutInflater.from(this));
            dialog.setContentView(popbinding.getRoot());

            window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);

            try {
                if (BannerURL != null && BannerURL.length() > 6) {
                    popbinding.imageviewBanner.setVisibility(View.GONE);
                    Picasso.with(this).load(BannerURL)
                            .placeholder(R.drawable.bannerimage)
                            .into(popbinding.imageviewBanner, new com.squareup.picasso.Callback() {
                                @Override
                                public void onSuccess() {
                                    popbinding.imageviewBanner.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onError() {
                                    popbinding.imageviewBanner.setVisibility(View.GONE);
                                }
                            });
                } else {
                    popbinding.imageviewBanner.setVisibility(View.GONE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                popbinding.imageviewBanner.setVisibility(View.GONE);
            }

            try {
                if (ImageDetailsBannerURL != null && ImageDetailsBannerURL.length() > 6) {
                    popbinding.imageviewDetails.setVisibility(View.GONE);
                    Picasso.with(this).load(ImageDetailsBannerURL)
                            .placeholder(R.drawable.bannerimage)
                            .into(popbinding.imageviewDetails, new com.squareup.picasso.Callback() {
                                @Override
                                public void onSuccess() {
                                    popbinding.imageviewDetails.setVisibility(View.VISIBLE);
                                    new_ImageDetailsBannerURL = null;
                                }

                                @Override
                                public void onError() {
                                    popbinding.imageviewDetails.setVisibility(View.GONE);
                                    String Cropname = AppConstant.selected_crop;
                                    if (new_ImageDetailsBannerURL == null) {
                                        new_ImageDetailsBannerURL = "https://te32.s3.us-east-1.amazonaws.com/SecuFarm_Banner/" + Cropname + "/english/Banner.jpg";
                                        Picasso.with(MainProfileActivity.this).load(new_ImageDetailsBannerURL)
                                                .placeholder(R.drawable.bannerimage)
                                                .into(popbinding.imageviewDetails, new com.squareup.picasso.Callback() {
                                                    @Override
                                                    public void onSuccess() {
                                                        popbinding.imageviewDetails.setVisibility(View.VISIBLE);
                                                    }

                                                    @Override
                                                    public void onError() {
                                                        popbinding.imageviewDetails.setVisibility(View.GONE);

                                                    }
                                                });
                                    }

                                }
                            });
                } else {
                    popbinding.imageviewDetails.setVisibility(View.GONE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                popbinding.imageviewDetails.setVisibility(View.GONE);
            }


            dialog.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getNutritionMngt() {

        dialog = new TransparentProgressDialog(
                MainProfileActivity.this, getDynamicLanguageValue(getApplicationContext(), "Dataisloading", R.string.Dataisloading));
        dialog.setCancelable(false);
        dialog.show();

        String language = AppManager.getInstance().getSelectedLanguages(this);
        apiService_weed = AppController.getInstance().getApiServiceGson_weed();
        mRequestStartTime = System.currentTimeMillis();
        WeedMngtRequest request = new WeedMngtRequest();
        request.setCropid(Integer.valueOf(AppConstant.selected_cropId));
        request.setLanguage(language);

        apiService_weed.getNutritionManagement(request).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResponseObserver<retrofit2.Response<List<NutritionMngtResponse>>>(compositeDisposable) {
                    @Override
                    public void onNetworkError(Throwable e) {
                        // calculate the duration in milliseconds
                        int seconds = getAPIimeResponseinSecond(mRequestStartTime);
                        dialog.cancel();
                        Log.v("wvqevqevqe", e.getMessage() + " " + e.toString());
//                        SaveLocalFile(db, MainProfileActivity.this, SN_MainProfileActivity, requestString, "", getSMS(e.getMessage()), "" + seconds, AppConstant.farm_id, "Error");
//                        showError(getString(R.string.network_error));

                    }

                    @Override
                    public void onServerError(Throwable e, int code) { // calculate the duration in milliseconds
                        int seconds = getAPIimeResponseinSecond(mRequestStartTime);
                        dialog.cancel();
//                        showError(getString(R.string.server_not_found));
//                        SaveLocalFile(db, MainProfileActivity.this, SN_MainProfileActivity, requestString, "", "Server API Error", "" + seconds, AppConstant.farm_id, "Error");

                    }

                    @Override
                    public void onNext(retrofit2.Response<List<NutritionMngtResponse>> response) {
                        // calculate the duration in milliseconds
                        int seconds = getAPIimeResponseinSecond(mRequestStartTime);
                        dialog.cancel();
                        List<NutritionMngtResponse> responsesData = response.body();
//                        Gson gson = new Gson();
//                        String response1 = gson.toJson(responsesData);
                        Log.v("ABC Log", response.toString());
                        if (responsesData != null && responsesData.size() > 0) {
                            NutritionPopUp(responsesData);
                        } else {
                            getDynamicLanguageToast(getApplicationContext(), "Dataisnotfoundforthiscrop", R.string.Dataisnotfoundforthiscrop);
                        }
//
//                        if (seconds > 3) {
//                            SaveLocalFile(db, MainProfileActivity.this, SN_MainProfileActivity, requestString, response, "", "" + seconds, AppConstant.farm_id, "Working");
//                        }
//                        setBanner(0);
//                        setDFarmData(response, requestString, seconds);
//                        showError(getString(R.string.server_not_found));

                    }

                });
    }

    private void NutritionPopUp(List<NutritionMngtResponse> data) {

        final Dialog dialog = new Dialog(this, R.style.DialogSlideAnim);

        //  final Dialog dialog = new Dialog(getActivity());

        dialog.setCanceledOnTouchOutside(true);
        Window window = dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);


        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.dimAmount = 0.5f;

        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        // wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        // Include dialog.xml file
        dialog.setContentView(R.layout.nutrition_popup);

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, 1300);


//        LinearLayout ll_vermicompost = (LinearLayout) dialog.findViewById(R.id.ll_vermicompost);
//        LinearLayout ll_Trichoderma = (LinearLayout) dialog.findViewById(R.id.ll_Trichoderma);
//        LinearLayout ll_NeeemOil = (LinearLayout) dialog.findViewById(R.id.ll_NeeemOil);

        TextView heading = (TextView) dialog.findViewById(R.id.heading);
//        TextView txt_vermicompost = (TextView) dialog.findViewById(R.id.txt_vermicompost);
//        TextView txt_Trichoderma = (TextView) dialog.findViewById(R.id.txt_Trichoderma);
//        TextView txt_NeeemOil = (TextView) dialog.findViewById(R.id.txt_NeeemOil);

        Button btn_cross = (Button) dialog.findViewById(R.id.btn_cross);
        RecyclerView recyclerview_Imagelist = (RecyclerView) dialog.findViewById(R.id.recyclerview_Imagelist);


        setDynamicLanguage(this, heading, "NutritionManagement_H", R.string.NutritionManagement_H);

        setFontsStyleTxt(this, heading, 2);
//
//        ll_Trichoderma.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.cancel();
//                ThumbnailNutritionImages("Trichoderma");
//            }
//        });
//        ll_vermicompost.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.cancel();
//                ThumbnailNutritionImages("Vermicompost");
//            }
//        });
//        ll_NeeemOil.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.cancel();
//                ThumbnailNutritionImages("NeeemOil");
//            }
//        });
//        txt_Trichoderma.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.cancel();
//                ThumbnailNutritionImages("Trichoderma");
//            }
//        });
//        txt_vermicompost.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.cancel();
//                ThumbnailNutritionImages("Vermicompost");
//            }
//        });
//        txt_NeeemOil.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.cancel();
//                ThumbnailNutritionImages("NeeemOil");
//            }
//        });

        btn_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        ArrayList<NutritionMngt> pestdis = new ArrayList<NutritionMngt>();
        try {
            if (data != null && data.size() > 0) {
                for (int i = 0; i < data.size(); i++) {
                    NutritionMngtResponse res = data.get(i);
                    NutritionMngt pestds = new NutritionMngt();
                    String imagename = res.getImagefile();
//                    String imagePath = WeedMngtImageURL + imagename;
                    pestds.setId(res.getId());
                    pestds.setImageTitle(res.getImageTitle());
                    pestds.setImagefile(imagename);
                    pestds.setCropid(res.getCropid());
                    pestds.setLanguage(res.getLanguage());
                    pestds.setCropname(res.getCropname());
                    pestdis.add(pestds);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        recyclerview_Imagelist.setHasFixedSize(true);
//            LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL, false);
        recyclerview_Imagelist.setLayoutManager(mLayoutManager);
        if (pestdis.size() > 0) {
            recyclerview_Imagelist.setVisibility(View.VISIBLE);
            NutritionMngtAdapter adapter = new NutritionMngtAdapter(this, pestdis);

            recyclerview_Imagelist.setAdapter(adapter);
        } else {
            recyclerview_Imagelist.setVisibility(View.GONE);
        }


        dialog.show();

    }

    @SuppressLint("StaticFieldLeak")
    private class getFarmListAsyncTask extends AsyncTask<Void, Void, String> {
        SignInData data;
        String result = "";
        int flags = 0;
        TransparentProgressDialog progressDialog;

        public getFarmListAsyncTask(int flag) {
            this.data = data;
            this.flags = flag;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new TransparentProgressDialog(
                    MainProfileActivity.this, getDynamicLanguageValue(getApplicationContext(), "getfarmdetails", R.string.getfarmdetails));
            progressDialog.setCancelable(false);
            progressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                                                @Override
                                                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                                                    return false;
                                                }
                                            }
            );
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            String sendRequest = null;
            try {
                if (flags == 0) {
                    sendRequest = AppManager.getInstance().getFarmList + AppConstant.user_id;
                } else if (flags == 1) {
                    sendRequest = AppManager.getInstance().getFarmList + AppConstant.user_id + "/true";
                }
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
                        getDynamicLanguageToast(getApplicationContext(), "Farmnotavailable", R.string.Farmnotavailable);
//                        gotoHomeScreen();

                    } else {

                        if (flags == 0) {
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
                            getAllFarmName();
                        } else if (flags == 1) {
                            //Update the Contour and Cen Lat Long
                            JSONArray array = new JSONArray(response);
                            db.open();
                            db.UpdateFarmDetailContourCenLagLong(array);//Update the contour and Lag long
                            Intent intent = new Intent(MainProfileActivity.this, PolicyList.class);
                            startActivity(intent);
                        }
                    }
                } else {
                    setToastServerError(MainProfileActivity.this);
                }
            } catch (JSONException e) {
                e.printStackTrace();

                System.out.println("catch block Pls Try again");
            } catch (Exception e) {
                e.printStackTrace();

                System.out.println("catch block Pls Try again");
            }
            progressDialog.dismiss();

        }
    }


    private ApiService apiService_weed;

    public void getWeedMngt() {

        dialog = new TransparentProgressDialog(
                MainProfileActivity.this, getDynamicLanguageValue(getApplicationContext(), "Dataisloading", R.string.Dataisloading));
        dialog.setCancelable(false);
        dialog.show();

        String language = AppManager.getInstance().getSelectedLanguages(this);
        apiService_weed = AppController.getInstance().getApiServiceGson_weed();
        mRequestStartTime = System.currentTimeMillis();
        WeedMngtRequest request = new WeedMngtRequest();
        request.setCropid(Integer.valueOf(AppConstant.selected_cropId));
        request.setLanguage(language);

        apiService_weed.getWeedManagement(request).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResponseObserver<retrofit2.Response<List<WeedMngtResponse>>>(compositeDisposable) {
                    @Override
                    public void onNetworkError(Throwable e) {
                        // calculate the duration in milliseconds
                        int seconds = getAPIimeResponseinSecond(mRequestStartTime);
                        dialog.cancel();
                        Log.v("wvqevqevqe", e.getMessage() + " " + e.toString());
//                        SaveLocalFile(db, MainProfileActivity.this, SN_MainProfileActivity, requestString, "", getSMS(e.getMessage()), "" + seconds, AppConstant.farm_id, "Error");
//                        showError(getString(R.string.network_error));

                    }

                    @Override
                    public void onServerError(Throwable e, int code) { // calculate the duration in milliseconds
                        int seconds = getAPIimeResponseinSecond(mRequestStartTime);
                        dialog.cancel();
//                        showError(getString(R.string.server_not_found));
//                        SaveLocalFile(db, MainProfileActivity.this, SN_MainProfileActivity, requestString, "", "Server API Error", "" + seconds, AppConstant.farm_id, "Error");

                    }

                    @Override
                    public void onNext(retrofit2.Response<List<WeedMngtResponse>> response) {
                        // calculate the duration in milliseconds
                        int seconds = getAPIimeResponseinSecond(mRequestStartTime);
                        dialog.cancel();
                        List<WeedMngtResponse> responsesData = response.body();
//                        Gson gson = new Gson();
//                        String response1 = gson.toJson(responsesData);
                        Log.v("ABC Log", response.toString());
                        if (responsesData != null && responsesData.size() > 0) {
                            WeedMgmtPopupMethod(responsesData);
                        } else {
                            getDynamicLanguageToast(getApplicationContext(), "Dataisnotfoundforthiscrop", R.string.Dataisnotfoundforthiscrop);
                        }
//
//                        if (seconds > 3) {
//                            SaveLocalFile(db, MainProfileActivity.this, SN_MainProfileActivity, requestString, response, "", "" + seconds, AppConstant.farm_id, "Working");
//                        }
//                        setBanner(0);
//                        setDFarmData(response, requestString, seconds);
//                        showError(getString(R.string.server_not_found));

                    }

                });
    }

    public void WeedMgmtPopupMethod(List<WeedMngtResponse> data) {

        final Dialog dialog = new Dialog(this, R.style.DialogSlideAnim);

        //  final Dialog dialog = new Dialog(getActivity());

        dialog.setCanceledOnTouchOutside(true);
        Window window = dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);


        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.dimAmount = 0.5f;

        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        // wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        // Include dialog.xml file
        dialog.setContentView(R.layout.weedmanagement_popup);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, height);
//        if (data.size() > 2) {
//            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, 950);
//        } else {
//            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, 750)
//        }

//        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, 1300);

        RecyclerView recyclerview_weed = (RecyclerView) dialog.findViewById(R.id.recyclerview_weed);

        Button btn_cross = (Button) dialog.findViewById(R.id.btn_cross);

        TextView heading = (TextView) dialog.findViewById(R.id.heading);

        setDynamicLanguage(this, heading, "WeedManagement", R.string.WeedManagement);
        setFontsStyleTxt(this, heading, 2);

        try {
            ArrayList<WeedMngt> pestdis = new ArrayList<WeedMngt>();
            if (data != null && data.size() > 0) {
                for (int i = 0; i < data.size(); i++) {
                    WeedMngtResponse res = data.get(i);
                    WeedMngt pestds = new WeedMngt();
                    String imagename = res.getImagefile();
                    String imagePath = WeedMngtImageURL + imagename;
                    pestds.setImageName(imagePath);
                    pestds.setDiseaseInsectName(res.getName());
                    pestds.setScientificName(res.getScientificName());
                    pestds.setDamageIntensity(res.getDamageIntensity());
                    pestds.setMngtPreEmergence(res.getPreManagement());
                    pestds.setMngtPostEmergence(res.getPostManagement());
                    pestdis.add(pestds);
                }
            }
            if (pestdis.size() > 0) {
                recyclerview_weed.setHasFixedSize(true);
                LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
                recyclerview_weed.setLayoutManager(mLayoutManager);
                WeedManagementAdapter adapter = new WeedManagementAdapter(this, pestdis);
                recyclerview_weed.setAdapter(adapter);
                Log.v("ladk", "saldsa");
            } else {
                getDynamicLanguageToast(getApplicationContext(), "thedatanotfoundcrop", R.string.thedatanotfoundcrop);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        btn_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    public void getYeildimprovement() {
        String language = AppManager.getInstance().getSelectedLanguages(this);
        apiService_weed = AppController.getInstance().getApiServiceGson_weed();
        mRequestStartTime = System.currentTimeMillis();
        WeedMngtRequest request = new WeedMngtRequest();
        request.setCropid(Integer.valueOf(AppConstant.selected_cropId));
        request.setLanguage(language);

        apiService_weed.getYeildImprovement(request).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResponseObserver<retrofit2.Response<List<YeildImprovementResponse>>>(compositeDisposable) {
                    @Override
                    public void onNetworkError(Throwable e) {
                        // calculate the duration in milliseconds
                        int seconds = getAPIimeResponseinSecond(mRequestStartTime);
                        dialog.cancel();
                        Log.v("wvqevqevqe", e.getMessage() + " " + e.toString());
//                        SaveLocalFile(db, MainProfileActivity.this, SN_MainProfileActivity, requestString, "", getSMS(e.getMessage()), "" + seconds, AppConstant.farm_id, "Error");
//                        showError(getString(R.string.network_error));

                    }

                    @Override
                    public void onServerError(Throwable e, int code) { // calculate the duration in milliseconds
                        int seconds = getAPIimeResponseinSecond(mRequestStartTime);
                        dialog.cancel();
//                        showError(getString(R.string.server_not_found));
//                        SaveLocalFile(db, MainProfileActivity.this, SN_MainProfileActivity, requestString, "", "Server API Error", "" + seconds, AppConstant.farm_id, "Error");

                    }

                    @Override
                    public void onNext(retrofit2.Response<List<YeildImprovementResponse>> response) {
                        // calculate the duration in milliseconds
                        int seconds = getAPIimeResponseinSecond(mRequestStartTime);
                        dialog.cancel();
                        List<YeildImprovementResponse> responsesData = response.body();
//                        Gson gson = new Gson();
//                        String response1 = gson.toJson(responsesData);
                        Log.v("ABC Log", response.toString());
                        if (responsesData != null && responsesData.size() > 0) {
                            YeildimprovementPopupMethod(responsesData);
                        } else {
                            getDynamicLanguageToast(getApplicationContext(), "Dataisnotfoundforthiscrop", R.string.Dataisnotfoundforthiscrop);
                        }
//
//                        if (seconds > 3) {
//                            SaveLocalFile(db, MainProfileActivity.this, SN_MainProfileActivity, requestString, response, "", "" + seconds, AppConstant.farm_id, "Working");
//                        }
//                        setBanner(0);
//                        setDFarmData(response, requestString, seconds);
//                        showError(getString(R.string.server_not_found));

                    }

                });
    }

    public void YeildimprovementPopupMethod(List<YeildImprovementResponse> data) {

        final Dialog dialog = new Dialog(this, R.style.DialogSlideAnim);

        //  final Dialog dialog = new Dialog(getActivity());

        dialog.setCanceledOnTouchOutside(true);
        Window window = dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);


        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.dimAmount = 0.5f;

        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        // wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        // Include dialog.xml file
        dialog.setContentView(R.layout.weedmanagement_popup);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, height);
//        if (data.size() > 2) {
//            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, 950);
//        } else {
//            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, 750)
//        }

//        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, 1300);

        RecyclerView recyclerview_weed = (RecyclerView) dialog.findViewById(R.id.recyclerview_weed);

        Button btn_cross = (Button) dialog.findViewById(R.id.btn_cross);

        TextView heading = (TextView) dialog.findViewById(R.id.heading);

        setDynamicLanguage(this, heading, "YeildImprovement_H", R.string.YeildImprovement_H);
        setFontsStyleTxt(this, heading, 2);

        try {
            ArrayList<WeedMngt> pestdis = new ArrayList<WeedMngt>();
            if (data != null && data.size() > 0) {
                for (int i = 0; i < data.size(); i++) {
                    YeildImprovementResponse res = data.get(i);
                    WeedMngt pestds = new WeedMngt();
                    String imagename = res.getImageURL();
                    String imagePath = WeedMngtImageURL + imagename;
                    pestds.setImageName(imagePath);
                    pestds.setDiseaseInsectName(res.getTitle());
                    pestds.setScientificName(res.getSymptoms());
                    pestds.setDamageIntensity(res.getControlMeasures());
                    pestds.setMngtPreEmergence(null);
                    pestds.setMngtPostEmergence(null);
                    pestdis.add(pestds);
                }
            }
            if (pestdis.size() > 0) {
                recyclerview_weed.setHasFixedSize(true);
                LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
                recyclerview_weed.setLayoutManager(mLayoutManager);
                YeildImprovementAdapter adapter = new YeildImprovementAdapter(this, pestdis);
                recyclerview_weed.setAdapter(adapter);
                Log.v("ladk", "saldsa");
            } else {
                getDynamicLanguageToast(MainProfileActivity.this, "thedatanotfoundcrop", R.string.thedatanotfoundcrop);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        btn_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    public void getPostHarvestMngt() {

        dialog = new TransparentProgressDialog(
                MainProfileActivity.this, getDynamicLanguageValue(getApplicationContext(), "Dataisloading", R.string.Dataisloading));
        dialog.setCancelable(false);
        dialog.show();

        String language = AppManager.getInstance().getSelectedLanguages(this);
        apiService_weed = AppController.getInstance().getApiServiceGson_weed();
        mRequestStartTime = System.currentTimeMillis();
        WeedMngtRequest request = new WeedMngtRequest();
        request.setCropid(Integer.valueOf(AppConstant.selected_cropId));
        request.setLanguage(language);

        apiService_weed.getPostHarvManagement(request).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResponseObserver<retrofit2.Response<List<PostHarvMngtResponse>>>(compositeDisposable) {
                    @Override
                    public void onNetworkError(Throwable e) {
                        // calculate the duration in milliseconds
                        int seconds = getAPIimeResponseinSecond(mRequestStartTime);
                        dialog.cancel();
                        Log.v("wvqevqevqe", e.getMessage() + " " + e.toString());
//                        SaveLocalFile(db, MainProfileActivity.this, SN_MainProfileActivity, requestString, "", getSMS(e.getMessage()), "" + seconds, AppConstant.farm_id, "Error");
//                        showError(getString(R.string.network_error));

                    }

                    @Override
                    public void onServerError(Throwable e, int code) { // calculate the duration in milliseconds
                        int seconds = getAPIimeResponseinSecond(mRequestStartTime);
                        dialog.cancel();
//                        showError(getString(R.string.server_not_found));
//                        SaveLocalFile(db, MainProfileActivity.this, SN_MainProfileActivity, requestString, "", "Server API Error", "" + seconds, AppConstant.farm_id, "Error");

                    }

                    @Override
                    public void onNext(retrofit2.Response<List<PostHarvMngtResponse>> response) {
                        // calculate the duration in milliseconds
                        int seconds = getAPIimeResponseinSecond(mRequestStartTime);
                        dialog.cancel();
                        List<PostHarvMngtResponse> responsesData = response.body();
//                        Gson gson = new Gson();
//                        String response1 = gson.toJson(responsesData);
                        Log.v("ABC Log", response.toString());
                        if (responsesData != null && responsesData.size() > 0) {
                            PostHarvMgmtPopupMethod(responsesData);
                        } else {
                            getDynamicLanguageToast(getApplicationContext(), "Dataisnotfoundforthiscrop", R.string.Dataisnotfoundforthiscrop);
                        }
//
//                        if (seconds > 3) {
//                            SaveLocalFile(db, MainProfileActivity.this, SN_MainProfileActivity, requestString, response, "", "" + seconds, AppConstant.farm_id, "Working");
//                        }
//                        setBanner(0);
//                        setDFarmData(response, requestString, seconds);
//                        showError(getString(R.string.server_not_found));

                    }

                });
    }

    public void PostHarvMgmtPopupMethod(List<PostHarvMngtResponse> data) {

        final Dialog dialog = new Dialog(this, R.style.DialogSlideAnim);

        //  final Dialog dialog = new Dialog(getActivity());

        dialog.setCanceledOnTouchOutside(true);
        Window window = dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);


        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.dimAmount = 0.5f;

        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        // wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        // Include dialog.xml file
        dialog.setContentView(R.layout.weedmanagement_popup);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, height);

        RecyclerView recyclerview_posthrv = (RecyclerView) dialog.findViewById(R.id.recyclerview_weed);

        Button btn_cross = (Button) dialog.findViewById(R.id.btn_cross);

        TextView heading = (TextView) dialog.findViewById(R.id.heading);

        setDynamicLanguage(this, heading, "PostHarvestManagement_H", R.string.PostHarvestManagement_H);
        setFontsStyleTxt(this, heading, 2);

        try {
            ArrayList<PostHarvMngt> pestdis = new ArrayList<PostHarvMngt>();
            if (data != null && data.size() > 0) {
                for (int i = 0; i < data.size(); i++) {
                    PostHarvMngtResponse res = data.get(i);
                    PostHarvMngt pestds = new PostHarvMngt();
                    String imagename = res.getImagefile();
//                    String imagePath = WeedMngtImageURL + imagename;
                    pestds.setId(res.getId().toString());
                    pestds.setTitle(res.getTitle());
                    pestds.setImagefile(imagename);
                    pestds.setCropid(res.getCropid().toString());
                    pestds.setDescription(res.getDescription());
                    pestds.setLanguage(res.getLanguage());
                    pestds.setCropname(res.getCropname());
                    pestdis.add(pestds);
                }
            }
            if (pestdis.size() > 0) {
                recyclerview_posthrv.setHasFixedSize(true);
                LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
                recyclerview_posthrv.setLayoutManager(mLayoutManager);
                PostHarvMngtAdapter adapter = new PostHarvMngtAdapter(this, pestdis);
                recyclerview_posthrv.setAdapter(adapter);
                Log.v("ladk", "saldsa");
            } else {
                getDynamicLanguageToast(getApplicationContext(), "thedatanotfoundcrop", R.string.thedatanotfoundcrop);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        btn_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    public void getIrrigationMngt() {

        dialog = new TransparentProgressDialog(
                MainProfileActivity.this, getDynamicLanguageValue(getApplicationContext(), "Dataisloading", R.string.Dataisloading));
        dialog.setCancelable(false);
        dialog.show();

        String language = AppManager.getInstance().getSelectedLanguages(this);
        apiService_weed = AppController.getInstance().getApiServiceGson_weed();
        mRequestStartTime = System.currentTimeMillis();
        WeedMngtRequest request = new WeedMngtRequest();
        request.setCropid(Integer.valueOf(AppConstant.selected_cropId));
        request.setLanguage(language);
        request.setFarmID(AppConstant.farm_id);

//        request.setCropid(Integer.valueOf("292"));
//        request.setLanguage(language);
//        request.setFarmID("531991");

        apiService_weed.getNutritionIrrigationManagement(request).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResponseObserver<retrofit2.Response<List<IrrrigationMngtResponse>>>(compositeDisposable) {
                    @Override
                    public void onNetworkError(Throwable e) {
                        // calculate the duration in milliseconds
                        int seconds = getAPIimeResponseinSecond(mRequestStartTime);
                        dialog.cancel();
                        Log.v("wvqevqevqe", e.getMessage() + " " + e.toString());
//                        SaveLocalFile(db, MainProfileActivity.this, SN_MainProfileActivity, requestString, "", getSMS(e.getMessage()), "" + seconds, AppConstant.farm_id, "Error");
//                        showError(getString(R.string.network_error));

                    }

                    @Override
                    public void onServerError(Throwable e, int code) { // calculate the duration in milliseconds
                        int seconds = getAPIimeResponseinSecond(mRequestStartTime);
                        dialog.cancel();
//                        showError(getString(R.string.server_not_found));
//                        SaveLocalFile(db, MainProfileActivity.this, SN_MainProfileActivity, requestString, "", "Server API Error", "" + seconds, AppConstant.farm_id, "Error");

                    }

                    @Override
                    public void onNext(retrofit2.Response<List<IrrrigationMngtResponse>> response) {
                        // calculate the duration in milliseconds
                        int seconds = getAPIimeResponseinSecond(mRequestStartTime);
                        dialog.cancel();
                        List<IrrrigationMngtResponse> responsesData = response.body();
//                        Gson gson = new Gson();
//                        String response1 = gson.toJson(responsesData);
                        Log.v("ABC Log", response.toString());
                        if (responsesData != null && responsesData.size() > 0) {
                            IrrigationMgmtPopupMethod(responsesData);
                        } else {
                            getDynamicLanguageToast(getApplicationContext(), "Dataisnotfoundforthiscrop", R.string.Dataisnotfoundforthiscrop);
                        }
//
//                        if (seconds > 3) {
//                            SaveLocalFile(db, MainProfileActivity.this, SN_MainProfileActivity, requestString, response, "", "" + seconds, AppConstant.farm_id, "Working");
//                        }
//                        setBanner(0);
//                        setDFarmData(response, requestString, seconds);
//                        showError(getString(R.string.server_not_found));

                    }

                });
    }

    public void IrrigationMgmtPopupMethod(List<IrrrigationMngtResponse> data) {

        final Dialog dialog = new Dialog(this, R.style.DialogSlideAnim);

        //  final Dialog dialog = new Dialog(getActivity());

        dialog.setCanceledOnTouchOutside(true);
        Window window = dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);


        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.dimAmount = 0.5f;

        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        // wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        // Include dialog.xml file
        dialog.setContentView(R.layout.weedmanagement_popup);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, height);

        RecyclerView recyclerview_posthrv = (RecyclerView) dialog.findViewById(R.id.recyclerview_weed);

        Button btn_cross = (Button) dialog.findViewById(R.id.btn_cross);

        TextView heading = (TextView) dialog.findViewById(R.id.heading);

        setDynamicLanguage(this, heading, "IrrigationManagement_H", R.string.IrrigationManagement_H);
        setFontsStyleTxt(this, heading, 2);

        try {
            ArrayList<IrrrigationMngt> pestdis = new ArrayList<IrrrigationMngt>();
            if (data != null && data.size() > 0) {
                for (int i = 0; i < data.size(); i++) {
                    IrrrigationMngtResponse res = data.get(i);
                    IrrrigationMngt pestds = new IrrrigationMngt();
                    pestds.setId(res.getId());
                    pestds.setDAS(res.getDas());
                    pestds.setNotification(res.getNotification());
                    pestds.setCropid(res.getCropid());
                    pestds.setLanguage(res.getLanguage());
                    pestds.setCropname(res.getCropname());
                    pestdis.add(pestds);
                }
            }
            if (pestdis.size() > 0) {
                recyclerview_posthrv.setHasFixedSize(true);
                LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
                recyclerview_posthrv.setLayoutManager(mLayoutManager);
                IrrigationMngtAdapter adapter = new IrrigationMngtAdapter(this, pestdis);
                recyclerview_posthrv.setAdapter(adapter);
                Log.v("ladk", "saldsa");
            } else {
                getDynamicLanguageToast(getApplicationContext(), "thedatanotfoundcrop", R.string.thedatanotfoundcrop);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        btn_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    public void getGuideLines() {
        String language = AppManager.getInstance().getSelectedLanguages(this);
        apiService_weed = AppController.getInstance().getApiServiceGson_weed();
        mRequestStartTime = System.currentTimeMillis();
        WeedMngtRequest request = new WeedMngtRequest();
        request.setCropid(Integer.valueOf(AppConstant.selected_cropId));
        request.setLanguage(language);

        apiService_weed.getPostHarvManagement(request).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResponseObserver<retrofit2.Response<List<PostHarvMngtResponse>>>(compositeDisposable) {
                    @Override
                    public void onNetworkError(Throwable e) {
                        // calculate the duration in milliseconds
                        int seconds = getAPIimeResponseinSecond(mRequestStartTime);
                        dialog.cancel();
                        Log.v("wvqevqevqe", e.getMessage() + " " + e.toString());
//                        SaveLocalFile(db, MainProfileActivity.this, SN_MainProfileActivity, requestString, "", getSMS(e.getMessage()), "" + seconds, AppConstant.farm_id, "Error");
//                        showError(getString(R.string.network_error));

                    }

                    @Override
                    public void onServerError(Throwable e, int code) { // calculate the duration in milliseconds
                        int seconds = getAPIimeResponseinSecond(mRequestStartTime);
                        dialog.cancel();
//                        showError(getString(R.string.server_not_found));
//                        SaveLocalFile(db, MainProfileActivity.this, SN_MainProfileActivity, requestString, "", "Server API Error", "" + seconds, AppConstant.farm_id, "Error");

                    }

                    @Override
                    public void onNext(retrofit2.Response<List<PostHarvMngtResponse>> response) {
                        // calculate the duration in milliseconds
                        int seconds = getAPIimeResponseinSecond(mRequestStartTime);
                        dialog.cancel();
                        List<PostHarvMngtResponse> responsesData = response.body();
//                        Gson gson = new Gson();
//                        String response1 = gson.toJson(responsesData);
                        Log.v("ABC Log", response.toString());
                        if (responsesData != null && responsesData.size() > 0) {
                            GuideLinesPopupMethod(responsesData);
                        } else {
                            getDynamicLanguageToast(getApplicationContext(), "Dataisnotfoundforthiscrop", R.string.Dataisnotfoundforthiscrop);
                        }
//
//                        if (seconds > 3) {
//                            SaveLocalFile(db, MainProfileActivity.this, SN_MainProfileActivity, requestString, response, "", "" + seconds, AppConstant.farm_id, "Working");
//                        }
//                        setBanner(0);
//                        setDFarmData(response, requestString, seconds);
//                        showError(getString(R.string.server_not_found));

                    }

                });
    }

    public void GuideLinesPopupMethod(List<PostHarvMngtResponse> data) {

        final Dialog dialog = new Dialog(this, R.style.DialogSlideAnim);

        //  final Dialog dialog = new Dialog(getActivity());

        dialog.setCanceledOnTouchOutside(true);
        Window window = dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);


        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.dimAmount = 0.5f;

        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        // wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        // Include dialog.xml file
        dialog.setContentView(R.layout.weedmanagement_popup);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, height);

        RecyclerView recyclerview_posthrv = (RecyclerView) dialog.findViewById(R.id.recyclerview_weed);

        Button btn_cross = (Button) dialog.findViewById(R.id.btn_cross);

        TextView heading = (TextView) dialog.findViewById(R.id.heading);

        setDynamicLanguage(this, heading, "Guideliness_H", R.string.Guideliness_H);
        setFontsStyleTxt(this, heading, 2);

        try {
            ArrayList<PostHarvMngt> pestdis = new ArrayList<PostHarvMngt>();
            if (data != null && data.size() > 0) {
                for (int i = 0; i < data.size(); i++) {
                    PostHarvMngtResponse res = data.get(i);
                    PostHarvMngt pestds = new PostHarvMngt();
                    String imagename = res.getImagefile();
//                    String imagePath = WeedMngtImageURL + imagename;
                    pestds.setId(res.getId().toString());
                    pestds.setTitle(res.getTitle());
                    pestds.setImagefile(imagename);
                    pestds.setCropid(res.getCropid().toString());
                    pestds.setDescription(res.getDescription());
                    pestds.setLanguage(res.getLanguage());
                    pestds.setCropname(res.getCropname());
                    pestdis.add(pestds);
                }
            }


            if (pestdis.size() > 0) {
                recyclerview_posthrv.setHasFixedSize(true);
                LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
                recyclerview_posthrv.setLayoutManager(mLayoutManager);
                PostHarvMngtAdapter adapter = new PostHarvMngtAdapter(this, pestdis);
                recyclerview_posthrv.setAdapter(adapter);
                Log.v("ladk", "saldsa");
            } else {
                getDynamicLanguageToast(MainProfileActivity.this, "thedatanotfoundcrop", R.string.thedatanotfoundcrop);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        btn_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    public void uploadScreenTracking() {
        //Screen Tracking Data Uploaded
        try {
            //Upload the pending ScreenTracking
            List<JSONObject> jsonObj = new ArrayList<JSONObject>();
            JSONObject obj = new JSONObject();
            obj.putOpt("UID", getUIDforScreenTracking() + "_" + getAppVersion(this));
            obj.putOpt("ScreenName", SN_MainProfileActivity);
            obj.putOpt("Date", getdateYYYYMMDD());
            obj.putOpt("InTime", gettime());
            obj.putOpt("OutTime", gettime());
            obj.putOpt("TimeDuration", "1");
            obj.putOpt("App_Name", "SecuFarm Farmer");
            obj.putOpt("DeviceIMEI", AndroidDevice_IMEI);
            obj.putOpt("UserID", AppConstant.user_id);
            obj.putOpt("FarmID", AppConstant.farm_id);
            jsonObj.add(obj);
            JSONArray test = new JSONArray(jsonObj);
            //COnvert JSONArray into JSONObject
            JSONObject object = new JSONObject();
            object.put("App_Screentracker", test.toString());
            new getFarmerDeatials(object, db, this).execute();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public class getFarmerDeatials extends AsyncTask<Void, Void, String> {
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
                    context, getDynamicLanguageValue(getApplicationContext(), "ScreenTrackingDatais", R.string.ScreenTrackingDatais));
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
            try {
                progressDialog.dismiss();
                if (response != null && !response.equalsIgnoreCase("Could not connect to server"))
                    obj = new JSONObject(response);
                try {
                    if (response != null && response.length() > 0) {
                        String ResponseValue = obj.getString("save_AppScreensResult");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
                getDynamicLanguageToast(MainProfileActivity.this, "ResponseFormattingError", R.string.ResponseFormattingError);
            }
        }

    }

    public void showPopUpSetting(View v) {
//        Context wrapper = new ContextThemeWrapper(this, R.style.popupMenuStyle);
//        PopupMenu popupMenu = new PopupMenu(wrapper, v);
//        popupMenu.setOnMenuItemClickListener(this);
//        popupMenu.inflate(R.menu.menu_dashboard);
//        popupMenu.show();

        Context wrapper = new ContextThemeWrapper(this, R.style.popupMenuStyle);
        IconizedMenu popup = new IconizedMenu(wrapper, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_dashboard, popup.getMenu());

//        Menu menu = popup.getMenu();
//        menu.findItem(R.id.itemEmergency).setTitle("Hello");
//        setDynamicLanguage(this, txt_login, "login", R.string.login);
        setDynamicLanguagevale(this, popup, R.id.itemEmergency, "Emergency", R.string.Emergency);
        setDynamicLanguagevale(this, popup, R.id.itemResetLanguage, "ResetLanguage", R.string.ResetLanguage);
        setDynamicLanguagevale(this, popup, R.id.itemRefreshLanguages, "RefreshLanguages", R.string.RefreshLanguages);
        setDynamicLanguagevale(this, popup, R.id.itemSelectFarm, "SelectFarm", R.string.SelectFarm);
        setDynamicLanguagevale(this, popup, R.id.itemRefreshFarm, "RefreshFarm", R.string.RefreshFarm);
        setDynamicLanguagevale(this, popup, R.id.itemUpdasteSowingdate, "UpdasteSowingdate", R.string.UpdasteSowingdate);
        setDynamicLanguagevale(this, popup, R.id.itemFarmMarker, "FarmMarker", R.string.FarmMarker);
        setDynamicLanguagevale(this, popup, R.id.itemGeoTag, "GeoTag", R.string.GeoTag);
        setDynamicLanguagevale(this, popup, R.id.itemYoutubeVideos, "YoutubeVideos", R.string.YoutubeVideos);
//        setDynamicLanguagevale(this, popup, R.id.itemVersion, "Version", R.string.Version);
        setDynamicLanguagevale(this, popup, R.id.itemLogout, "Logout", R.string.Logout);

        popup.setOnMenuItemClickListener(new IconizedMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.itemEmergency:
                        setEmergencyCall();
                        return true;
                    case R.id.itemResetLanguage:
                        checkLanguage = true;
                        Intent intent = new Intent(MainProfileActivity.this, POPUpLanguageSelection.class);
                        intent.putExtra("ActivityName", "MainProfileActivity");
                        startActivity(intent);
                        return true;
                    case R.id.itemRefreshLanguages:
                        downloadLocalTranslation();
                        return true;
                    case R.id.itemSelectFarm:
                        selectImagePopup();
                        return true;
                    case R.id.itemRefreshFarm:
                        new getFarmListAsyncTask(0).execute();
                        return true;
                    case R.id.itemUpdasteSowingdate:
                        if (AppConstant.farm_id != null) {
                            new FarmdetailsAsynctask().execute();
                        } else {
                            setToastPleaseselectyour(MainProfileActivity.this);
                        }
                        return true;
                    case R.id.itemFarmMarker:
//                if (contour != null && contour.length() > 10) { // For Geo Tagging
                        Intent in = new Intent(getApplicationContext(), EditFarmActivity.class);
                        startActivity(in);
//                } else {
//                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.Thefarmernocontour), Toast.LENGTH_SHORT).show();
//                }
                        return true;
                    case R.id.itemGeoTag:
                        Intent in11 = new Intent(MainProfileActivity.this, AddFarmOnMap_New.class);
                        in11.putExtra("ActivityName", "MainProfileActivity");
                        startActivity(in11);
                        return true;
                    case R.id.itemYoutubeVideos:

                        // manish comment

//                        if ((VideoID != null && VideoID.length() > 2) || (YoutubeVideolist != null && YoutubeVideolist.size() > 0)) {
//                            if (YoutubeVideolist != null && YoutubeVideolist.size() > 0) {
//                                Intent in1 = new Intent(getApplicationContext(), YoutubeVideoRecyclerView.class);
//                                in1.putExtra("YoutubeVideoURL", VideoID);
//                                in1.putExtra("YoutubeVideolist", YoutubeVideolist);
//                                startActivity(in1);
//                            } else if (VideoID != null && VideoID.length() > 2) {
//                                Intent in2 = new Intent(getApplicationContext(), YoutubeWebview.class);
//                                in2.putExtra("YoutubeVideoURL", VideoID);
//                                startActivity(in2);
//                            }
//                        } else {
//                            getDynamicLanguageToast(MainProfileActivity.this, "ThereisnoYouTube", R.string.ThereisnoYouTube);
//                        }

                        if (YoutubeVideolist != null && YoutubeVideolist.size() > 0) {
                            if (YoutubeVideolist != null && YoutubeVideolist.size() > 1) {
                                Intent in1 = new Intent(getApplicationContext(), YoutubeVideoRecyclerView.class);
                                in1.putExtra("YoutubeVideoURL", VideoID);
                                in1.putExtra("YoutubeVideolist", YoutubeVideolist);
                                startActivity(in1);
                            } else if (YoutubeVideolist != null && YoutubeVideolist.size() == 1) {
                                Intent in2 = new Intent(getApplicationContext(), YoutubeWebview.class);
                                in2.putExtra("YoutubeVideoURL", VideoID);
                                startActivity(in2);
                            }
                        } else {
                            getDynamicLanguageToast(getApplicationContext(), "ThereisnoYouTube", R.string.ThereisnoYouTube);
                        }


                        return true;
                    case R.id.itemVersion:
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.VersionLang), Toast.LENGTH_LONG).show();
                        return true;
                    case R.id.itemLogout:
                        accountAlert();
                        return true;
                    default:
                        return false;
                }
            }
        });
        popup.show();//showing popup menu
    }

    public void ScreenService() {
        try {
            if (ServiceScreenJSON != null && ServiceScreenJSON.length() > 0) {
//                if (ServiceScreenJSON.getString("Soil Doctor").equalsIgnoreCase("true"))
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setScreenService(String flag, CardView cardView) {
        try {
            if (flag.equalsIgnoreCase("true")) {
                cardView.setVisibility(View.VISIBLE);
            } else {
                cardView.setVisibility(View.GONE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setServiceScreens() {
        try {
            if (ServiceScreenJSON != null && ServiceScreenJSON.length() > 0) {
                JSONObject obj = ServiceScreenJSON.getJSONObject("ServiceScreens");
//                cardview_CropInform.setVisibility(View.VISIBLE);
//                cardview_PolicyRegistration.setVisibility(View.VISIBLE);
                //Crop Advisory
                try {
                    if (obj.getString("CropAdvisory").equalsIgnoreCase("true")) {
                        cardview_Popupdate.setVisibility(View.VISIBLE);
                    } else {
                        cardview_Popupdate.setVisibility(View.GONE);
                    }
                } catch (Exception ex) {
                    cardview_Popupdate.setVisibility(View.GONE);
                    ex.printStackTrace();
                }
                //PlantDoctor
                try {
                    if (obj.getString("PlantDoctor").equalsIgnoreCase("true")) {
                        cardview_Plantdoctor.setVisibility(View.VISIBLE);
                    } else {
                        cardview_Plantdoctor.setVisibility(View.GONE);
                    }
                } catch (Exception ex) {
                    cardview_Plantdoctor.setVisibility(View.GONE);
                    ex.printStackTrace();
                }
                //Weather
                try {
                    if (obj.getString("Weather").equalsIgnoreCase("true")) {
                        cardview_weather.setVisibility(View.VISIBLE);
                    } else {
                        cardview_weather.setVisibility(View.GONE);
                    }
                } catch (Exception ex) {
                    cardview_weather.setVisibility(View.GONE);
                    ex.printStackTrace();
                }
                //SoilDoctor
                try {
                    if (obj.getString("SoilDoctor").equalsIgnoreCase("true")) {
                        cardview_soildoctor.setVisibility(View.VISIBLE);
                    } else {
                        cardview_soildoctor.setVisibility(View.GONE);
                    }
                } catch (Exception ex) {
                    cardview_soildoctor.setVisibility(View.GONE);
                    ex.printStackTrace();
                }
                //MarketPlace
                try {
                    if (obj.getString("MarketPlace").equalsIgnoreCase("true")) {
                        cardview_MarketPlace.setVisibility(View.VISIBLE);
                    } else {
                        cardview_MarketPlace.setVisibility(View.GONE);
                    }
                } catch (Exception ex) {
                    cardview_MarketPlace.setVisibility(View.GONE);
                    ex.printStackTrace();
                }
                //Crop Information
                try {
                    if (obj.getString("CropInformation").equalsIgnoreCase("true")) {
                        cardview_CropInform.setVisibility(View.VISIBLE);
                    } else {
                        cardview_CropInform.setVisibility(View.GONE);
                    }
                } catch (Exception ex) {
                    cardview_CropInform.setVisibility(View.GONE);
                    ex.printStackTrace();
                }
                //Service Registration
                try {
                    if (obj.getString("Policy").equalsIgnoreCase("true")) {
                        cardview_PolicyRegistration.setVisibility(View.VISIBLE);
                    } else {
                        cardview_PolicyRegistration.setVisibility(View.GONE);
                    }
                } catch (Exception ex) {
                    cardview_PolicyRegistration.setVisibility(View.GONE);
                    ex.printStackTrace();
                }
                //GroundWater
                if (obj.getString("Groundwater").equalsIgnoreCase("true")) {
                    cardview_Groundwaterforecadting.setVisibility(View.VISIBLE);
                } else {
                    cardview_Groundwaterforecadting.setVisibility(View.GONE);
                }
                //Tubewell
                if (AISAvailable_Tubewell != null && !AISAvailable_Tubewell.equalsIgnoreCase("null")
                        && (AISAvailable_Tubewell.equalsIgnoreCase("Yes") ||
                        AISAvailable_Tubewell.equalsIgnoreCase("yes"))) {
                    cardview_Tubewell.setVisibility(View.VISIBLE);
                } else {
                    cardview_Tubewell.setVisibility(View.GONE);
                }


            } else {
                cardview_CropInform.setVisibility(View.VISIBLE);
                cardview_Popupdate.setVisibility(View.VISIBLE);
                cardview_Plantdoctor.setVisibility(View.VISIBLE);
                cardview_weather.setVisibility(View.VISIBLE);
                cardview_soildoctor.setVisibility(View.VISIBLE);
                cardview_MarketPlace.setVisibility(View.VISIBLE);
                cardview_Tubewell.setVisibility(View.VISIBLE);
                cardview_CropInform.setVisibility(View.VISIBLE);
                cardview_PolicyRegistration.setVisibility(View.VISIBLE);
                cardview_Groundwaterforecadting.setVisibility(View.VISIBLE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void setServiceCropInformation(LinearLayout ll, String Keys) {
        try {
            if (ServiceScreenJSON != null && ServiceScreenJSON.length() > 0) {
                JSONObject obj = ServiceScreenJSON.getJSONObject("ServiceScreens");
                ll.setVisibility(View.VISIBLE);
                try {
                    if (obj.getString(Keys).equalsIgnoreCase("true")) {
                        ll.setVisibility(View.VISIBLE);
                    } else {
                        ll.setVisibility(View.GONE);
                    }
                } catch (Exception ex) {
                    ll.setVisibility(View.GONE);
                    ex.printStackTrace();
                }
            } else {
                ll.setVisibility(View.VISIBLE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void NotificationFisrtimeAutoPopup() {
        try {
            ArrayList<NotificationBean> mDataset = new ArrayList<NotificationBean>();
            ArrayList<String> value = NotificationCountSMS.getNotificationValueData(this);
            value.removeAll(Collections.singleton(null));
            value.removeAll(Collections.singleton(""));
            try {
                for (int i = 0; i < value.size(); i++) {
                    String[] data = value.get(i).split("#");
                    String time = Utility.ConvertdatedifferenceDays(Utility.getdate() + " " + gettime(), data[5]);
                    //check for normal or interactive
                    String notftytype = data[1];
                    boolean check7dayslessornot = Utility.checkNoofDaysislessthan7or3(Utility.getdate() + " " + gettime(), data[5], notftytype);
                    //7 days Check
                    if (check7dayslessornot) {
                        NotificationBean setdata = new NotificationBean();
                        setdata.setMessgae(data[0]);
                        setdata.setNotftytype(data[1]);
                        setdata.setStepID(data[2]);
                        setdata.setFarmID(data[3]);
                        setdata.setTitle(data[4]);
                        setdata.setDateTime(time);
                        setdata.setDateTimeHHMMSS(data[5]);
                        try {
                            if (data.length > 5) {
                                setdata.setNotifImageURL(data[6]);
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        try {
                            if (data.length > 7) {
                                setdata.setFeedbackStatus(data[7]);
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        mDataset.add(setdata);

                        String NotifyType = data[1];

                        if (NotifyType != null &&
                                (NotifyType.equalsIgnoreCase("Intractive") ||
                                        NotifyType.equalsIgnoreCase("Feedback"))) {
                            Intent in = new Intent(getApplicationContext(), NotificationPOPDetailsDialog.class);
                            in.putExtra("mDataset", (ArrayList<NotificationBean>) mDataset);
                            in.putExtra("position", i);
                            startActivity(in);
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void setSMSList() {

        recycleview_SMSList.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recycleview_SMSList.setLayoutManager(mLayoutManager);

        if (dashboardSMS.size() > 0) {
            DashboarSMSListAdapter adapter = new DashboarSMSListAdapter(this, dashboardSMS, AppConstant.farm_id);
            recycleview_SMSList.setAdapter(adapter);
        }

    }


    public void downloadLocalTranslation() {
        try {
            final TransparentProgressDialog dialoug = new TransparentProgressDialog(MainProfileActivity.this,
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
                                setToastServerError(MainProfileActivity.this);
                            }
                            setSelectedLanguages();
                            setLanguages();
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

    public void setSelectedLanguages() {
        SharedPreferences prefs = getSharedPreferences(AppConstant.SHARED_PREFRENCE_NAME, MODE_PRIVATE);
        String langPref = prefs.getString(getResources().getString(R.string.language_pref_key), "1");
        int flag = Integer.parseInt(langPref);
        String SQL = "Select MyKey,English from tblLocalTranslation where English !='' order by MyKey";
        switch (flag) {
            case 2://hindi
                SQL = "Select MyKey,Hindi from tblLocalTranslation where Hindi !='' order by MyKey";
                break;
            case 1://english
                SQL = "Select MyKey,English from tblLocalTranslation where English !='' order by MyKey";
                break;
            case 3://gujarati
                SQL = "Select MyKey,Gujarati from tblLocalTranslation where Gujarati !='' order by MyKey";
                break;
            case 4://marathi
                SQL = "Select MyKey,Marathi from tblLocalTranslation where Marathi !='' order by MyKey";
                break;
            case 5://bengali
                SQL = "Select MyKey,Bengali from tblLocalTranslation where Bengali !='' order by MyKey";
                break;
            case 6://tamil
                SQL = "Select MyKey,Telugu from tblLocalTranslation where Telugu !='' order by MyKey";
                break;
        }
        getSelectedLanguages(SQL);

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

    public void setNotificationCount() {
        try {
            NotificationCountSMS.getNotificationCount(this);
            if (AppConstant.NotificationCountNo_badge > 0) {
                tv_badge.setVisibility(View.VISIBLE);
                tv_badge.setText(String.valueOf(AppConstant.NotificationCountNo_badge));
            } else {
                tv_badge.setVisibility(View.GONE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
