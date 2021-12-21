package com.weather.risk.mfi.myfarminfo.utils;

import android.graphics.Bitmap;
import android.util.SparseBooleanArray;

import com.google.android.gms.maps.model.LatLng;
import com.weather.risk.mfi.myfarminfo.policyregistration.UserFarmResponse;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 *
 */
public class AppConstant {
    public static final String SHARED_PREFRENCE_ONCE = "only_once_created";
    public static final String SHARED_PREFRENCE_NAME = "my_farm_info";
    public static final String PREFRENCE_KEY_EMAIL = "pref_key_email";
    public static final String PREFRENCE_KEY_PASS = "pref_key_pass";
    public static final String PREFRENCE_KEY_ISSAVED = "pref_key_issaved";
    public static final String PREFRENCE_KEY_ISLOGIN = "pref_key_islogin";
    public static final String PREFRENCE_KEY_USER_ID = "pref_key_user_id";
    public static final String PREFRENCE_KEY_MOBILE = "mobile";
    public static final String PREFRENCE_KEY_VISIBLE_NAME = "pref_key_visible_name";
    public static final String PREFRENCE_KEY_ROLE = "role";

    public static final String PREFRENCE_KEY_DASHBOARD = "dashboard";
    public static final String PREFRENCE_KEY_CLIENT_NAME = "client_name";
    public static final String PREFRENCE_KEY_BASE_USER_ID = "0";
    public static final String PREFRENCE_KEY_ALL_RESPONSE = "all_response";
    public static final String PREFRENCE_KEY_IS_SAVE_ALL_RESPONSE = "is_save";
    public static final String PREFRENCE_KEY_IS_CROP = "Potato";
    public static final String PREFRENCE_KEY_IS_CROP_ID = "67";
    public static final String PREFRENCE_KEY_IS_SUB_DISTRICT = "subdistrict";
    public static final String PREFRENCE_KEY_IS_AGRONOMIST = "isagronomist";
    public static final String PREFRENCE_KEY_IS_USERTYPEID = "usertypeid";
    public static final String PREFRENCE_KEY_IS_CattleDashBoardNOOFF = "CattleDashBoardNOOFF";

    public static final String AISAvailable_TubewellKey = "AISAvailable_TubewellKey";
    public static String AISAvailable_Tubewell = null;

    public static final int ONLINE = 0;
    public static final int OFFLINE = 1;
    //////////////////////////////////////////////////////////////////
    public static final String DATA_SET = "data_set";
    public static final String MANDI_DETAIL = "mandi_detail";
    public static final String APPLIED_VALUE_OF_N = "a_value_N";
    public static final String APPLIED_VALUE_OF_P = "a_value_P";
    public static final String APPLIED_VALUE_OF_K = "a_value_K";
    public static final String IDEAL_VALUE_OF_N = "i_value_N";
    public static final String IDEAL_VALUE_OF_P = "i_value_P";
    public static final String IDEAL_VALUE_OF_K = "i_value_K";
    public static final String SOWING_DATE_FROM = "sowing_From";
    public static final String SOWING_DATE_TO = "sowing_To";
    public static final int INCREASE_REVENIUE = 1;
    public static final int INCREASE_YIELD = 2;
    public static final int BEST_PRICE = 3;
    ///////////////////////////////////////////////////////////////
    public static final int SOW_PERIOD_FROM = 0;
    public static final int SOW_PERIOD_TO = 1;
    public static final String STATE_ID = "state_id";
    public static final String CROP_INITIAL = "crop_initial";
    public static final String CROP_ALL_INITIAL = "crop_all_initial";
    ////////////observing activity
    public static final int HomeActivity = 1001;
    public static final int AddFarmMap = 1002;
    public static final int selectForm = 101;
    public static final int tellMeMore = 102;
    public static final String REGISTER_USER = "register_user";
    public static final String SERVER_CONNECTION_ERROR = "Could not connect to server";
    public static int APP_MODE = ONLINE; //0 for online mode  and 1 for offline mode
    public static String iddd = null;
    public static String state = null;
    public static String stateID;
    public static String farm_id;
    public static String baseID = "0";
    public static String isAgronimist = "no";
    public static String userTypeID = null;
    public static String isFarm = null;
    public static String selected_cropId;
    public static String selected_crop;
    public static String selected_variety;
    public static String selectedFarm;
    public static String user_id;
    public static String selected_district;
    public static String selected_village;
    public static String selected_SubDistrict;
    public static String isSubdistrict = "no";
    public static String selected_farm;
    public static String visible_Name;
    public static String mobile_no;
    public static String role;
    public static String dashboard;
    public static Boolean isLogin = false;
    public static String latitude = "28.4595";
    public static String longitude = "77.0266";
    public static String AllResponse = null;
    public static String isfetch_AllResponse = null;
    public static String contour;
    public static String farmName;
    public static String dabFarmId;
    public static String client_name = "";
    public static String centerLat = null;
    public static String centerLon = null;
    public static String maxY = null;
    public static String maxX = null;
    public static String minY = null;
    public static String minX = null;
    //////////////////////////////////////////////////////////////////
    public static int CONCERN_ID = 0;
    public static String[] syncArray = {STATE_ID, CROP_INITIAL, CROP_ALL_INITIAL};
    public static ArrayList<LatLng> routeArray = new ArrayList<>();
    public static Double maxDistance = 25.0;
    public static boolean isWrite = false;
    public static boolean isRequestedWrite = false;
    public static String h1 = "630";
    public static String h2 = "771";
    public static String h3 = "585";
    public static String h4 = "824";
    public static String h5 = "1046";
    public static String h6 = "584";
    public static String h7 = "772";
    public static String h8 = "458";
    public static String h9 = "831";
    public static String h10 = "463";
    public static String h11 = "466";
    public static String h12 = "1017";
    public static String h13 = "873";
    public static String h14 = "791";
    public static String h15 = "521";
    public static String h17 = "1134";
    public static String h16 = "582";
    public static String h18 = "630";
    public static String h19 = "886";
    public static String h20 = "1200";


    public static final String PREFRENCE_KEY_CattleDashboardMobileKey = "Cattledashboard_mobileNo";
    public static String PREFRENCE_KEY_CattleDashboardMobileValue;

    //Herojit Add
//    public static String jsonarray_selected_descript_Scheduler = null;
    public static String RegisterDeviceTokenKey = null;
    public static String CheckRegisterDeviceTokenKey = null;//AlreadyUpload or PendingUpload
    public static String CheckTokenKey_Daily = "CheckTokenKey_DailyUpdate";//AlreadyUpload or PendingUpload
    public static String CheckTokenKey_DailyUpdateOrNot = null;//AlreadyUpdate or PendingUpdate

    public static final String PREFRENCE_KEY_DailyLoginCheck = "DailyLoginChecked";

    public static String CheckTokenKey_DailyUpdate_value() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        Date date = new Date();
        String TodayDate = String.valueOf(sdf.format(date));
        return TodayDate;//CheckTokenKey_DailyUpdate Key
    }


    public static final String SHARED_PREFRENCE_NAME_Token = "my_farm_info_token";

    public static int NotificationCountNo_badge = 0;
    public static String NotificationSMSList = null; //In JSON format in key value


    public static String FarmRegistration_AadharNo = null;
    public static String FarmRegistration_ProjectID = null;

    //Custom Camera for Distance Check

    public static boolean getCheckCameraScreenOnOff = false;
    public static Bitmap CustomCamera_bitmap;
    public static String CustomCamera_ImageValue = null;

    //Screen Tracking Screen Names

    public static String SN_NewHomeScreen = "PreLoginScreen";
    public static String SN_LoginWithOtp = "LoginScreen";
    public static String SN_AdminDashboard = "ServiceScreen";
    public static String SN_MainProfileActivity = "FarmerDashboard";
    public static String SN_WeatherForecast = "WeatherForecast";
    public static String SN_WeatherForecast_Details = "ForecastDetails";
    public static String SN_SoilDoctor = "SoilDoctor";
    public static String SN_PackagePractices = "PackagePractices";
    public static String SN_LiveCottonActivity = "PlantDoctor";
    public static String SN_MainProfileActivity_PestandDisease = "PestandDisease";
    public static String SN_Calculators = "Calculator";
    public static String SN_NutritionFragment = "Nutrition";
    public static String SN_NematodeFragment = "Nematode";
    public static String SN_WeedFragment = "Weed";
    public static String SN_CropFeasibilityFragment = "CropFeasibility";
    public static String SN_FarmRegistration = "FarmRegistration";
    public static String SN_FarmVisitAcitivity = "FarmVisit";
    public static String SN_SchedulerActivity = "CropScheduler";
    public static String SN_CreateSchedulerActivity = "CropSchedulerActivityDetails";
    public static String SN_AddFarmOnMap_New = "GeoTagging";
    public static String SN_LocateYoutFarmFragment = "GeoTagFarmRegistration";
    public static String SN_FarmerReactivation = "FarmerReactivation";
    public static String SN_YoutubeVideoActivity = "YoutubeVideoActivity";
    public static String SN_PolicyRegistration = "PolicyRegistration";

    public static String AndroidDevice_IMEI = null;

    public static String SN_DiseaseDiagnosis = "DiseaseDiagnosis";
    public static String SN_Mandi = "Mandi";
    //DiseaseDiagnosis
    public static int Addcallnew_CropId = 0;
    public static String Addcallnew_DiseaseDiagnosis = null;
    public static SparseBooleanArray itemStateArray_Disease = new SparseBooleanArray();
    public static SparseBooleanArray itemStateArray_Disease_setPosition = new SparseBooleanArray();
    public static String itemSelectedCotton_Disease = "";

    //Cattle Dashbaord

    public static String SN_CattleWeather = "CattleWeather";
    public static String SN_CattleWeatherForecast = "CattleWeatherForecast";
    public static String SN_CattleActualWeather = "CattleActualWeather";
    public static String SN_CattleDashbaords = "CattleDashbaords";
    public static String SN_CattleDoctors = "CattleDoctors";
    public static String SN_CattleAdvisory = "CattleAdvisory";
    public static String SN_CropSchedulerGeoTag = "CropSchedulerGeoTag";
    public static String FarmEditDetails = null;
    public static boolean FarmEditActive = false;

    public static String FOLDER_NAME = "wrms_mfi";

    //Market Place
    public static String SN_NewProductActivity = "ProductList";
    public static String SN_MyCartNew = "YourCart";
    public static String SN_CheckoutActivityNew = "CheckOut";

    //ServicesScreen
    public static JSONObject ServiceScreenJSON = new JSONObject();
    public static final String PREFRENCE_KEY_ServiceScreen = "ServiceScreenJson";
    //https://myfarminfo.com/yfirest.svc/servicescreens/{UserID}

    public static List<UserFarmResponse> userfarmList = new ArrayList<>();
    public static List<UserFarmResponse> userfarmListforpolicyfarmlist = new ArrayList<>();

    public static JSONObject SelectFarmDetails_policy = new JSONObject();

    public static boolean checkScreenComingfromLanguageSelectionorNot = false;

    //Languages Selection

    public static HashMap<String, String> SelectedLanguageValue = null;

    public static List<UserFarmResponse> farmList = null;

    public static String selectedFarmLat = "";

    public static String selectedFarmLong = "";
}
