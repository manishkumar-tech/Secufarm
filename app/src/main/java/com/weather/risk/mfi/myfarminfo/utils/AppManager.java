package com.weather.risk.mfi.myfarminfo.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.customcamera.CameraSurfaceView;
import com.weather.risk.mfi.myfarminfo.mapfragments.LatLonCellID;
import com.weather.risk.mfi.myfarminfo.multiplelanguages.SingleGlobal;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.MODE_PRIVATE;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setDynamicLanguage;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setFontsStyle;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setFontsStyleTxt;

/**
 *
 */
public class AppManager {

    private static AppManager appManager;
    //    public String uploadImageURL1 = "http://pdjalna.myfarminfo.com/PDService.svc/uploadBase64Image";
//    public String createNewLogURL = "http://pdjalna.myfarminfo.com/PDService.svc/logRequest";
//    public String getAllUnResolveURL = "http://pdjalna.myfarminfo.com/PDService.svc/getAllUnresolved";
//    public String searchHistoryURL = "http://pdjalna.myfarminfo.com/PDService.svc/Search";
    public static String URLdomain = "http://pdjalna.apimfi.com/";
    public static String URLdomain_PlantDocImage = "https://ndviimages.s3.ap-south-1.amazonaws.com/PDImages/Thumbnail/";
    //https://ndviimages.s3.ap-south-1.amazonaws.com/PDImages/OriginalImages/b72ddbe5-94b2-4da5-911d-da226bd55b6e.jpg
    //Thumbnail -  https://ndviimages.s3.ap-south-1.amazonaws.com/PDImages/Thumbnail/b72ddbe5-94b2-4da5-911d-da226bd55b6e.jpg
    public String uploadImageURL1 = URLdomain + "PDService.svc/uploadBase64Image";
    public String createNewLogURL = URLdomain + "PDService.svc/logRequest";
    public String getAllUnResolveURL = URLdomain + "PDService.svc/getAllUnresolved";
    public String searchHistoryURL = URLdomain + "PDService.svc/Search";
    public String VoiceUploadURL = URLdomain + "PDService.svc/UploadVoice/";
    public String URL_BASE_PATH = "https://myfarminfo.com/yfirest.svc/";
    public String cropsInitial = URL_BASE_PATH + "Crops/Initial/";
    public String FarmYieldImprove = URL_BASE_PATH + "Farm/YieldImprove/";
    public String saveFarmInfo = URL_BASE_PATH + "saveFarmInfo2";
    //    public String getFarmList = URL_BASE_PATH + "Farms/2/";
    //    public String saveFarmInfo = URL_BASE_PATH + "saveFarmInfo3";
    //    saveFarmInfoMC
    public String cropsAllInitial = URL_BASE_PATH + "Crops/All/Initial/";
    public String login = URL_BASE_PATH + "Login/";
    public String forgot_password = URL_BASE_PATH + "ForgotPassword/";
    public String otp_password = URL_BASE_PATH + "Clients/GetOtp_Ver/";
    public String mandiOptimal = URL_BASE_PATH + "Mandi/OptimalMandi/";
    public String getFarmList = URL_BASE_PATH + "Farm/Basic/";
    public String getStateId = URL_BASE_PATH + "StateID/";
    public String getFarmDetail = URL_BASE_PATH + "Farms/ID";
    public String getFarmReport = URL_BASE_PATH + "getFilteredFarms";
    public String uploadImageURL = URL_BASE_PATH + "uploadBase64Image";
    public String contestURL = URL_BASE_PATH + "insertContestData";
    public String isContest = URL_BASE_PATH + "Contest/Data/";
    public String updateDeviceURL = URL_BASE_PATH + "Irrigation/Update/";
    public String getFarmData = URL_BASE_PATH + "Farms/ID/";
    public String getAdvisoryReport = URL_BASE_PATH + "getGGRCSMSData";
    public String getAdvisoryUpdate = URL_BASE_PATH + "updateGGRCSMS";
    public String uploadMyserviceImage = URL_BASE_PATH + "uploadBase64FarmImage";
    public String uploadMyserviceInfo = URL_BASE_PATH + "SaveFarmerInformation";
    public String getEmergencyReport = URL_BASE_PATH + "saveFarm_PanicInfo";
    public String sendCommentURL = URL_BASE_PATH + "addAct_Comment";
    public String create_schedulerURL = URL_BASE_PATH + "addAct_Task";
    //    https://apimfi.com/api/SecuFarmerInfo/imageUpload/appkey
//    public String uploadImageURL_SCHEDULER = URL_BASE_PATH + "uploadBase64Image_Scheduler";
//    public String uploadImageURL_SCHEDULER_Download = "https://myfarminfo.com/Tools/Img/AIS/Uploads/";
    public String uploadImageURL_SCHEDULER_Download = "http://apimfi.com/CSImg/";
    //    public String uploadImageURL_SCHEDULER = "https://myfarminfo.com/yfirest.svc/uploadBase64Image_Scheduler";
    public String uploadImageURL_SCHEDULER = "https://apimfi.com/api/SecuFarmerInfo/imageUpload/appkey";
    public String pop_done_API = URL_BASE_PATH + "UpdateFarmPop";
    public String submit_visit_log_putapi = URL_BASE_PATH + "SaveLogs";
    public String postResolveURL = URL_BASE_PATH + "UpdatePdRequest";
    //Add Herojit
    public String StateDistrictURL = "https://myfarminfo.com/yfirest.svc/GetSentinalData";
    //    public String ProjectListURL="https://www.myfarminfo.com/yfirest.svc/getAdminUsers";
    public String CropsListURL = "https://myfarminfo.com/yfirest.svc/All/Crops";
    public String AllStatesListURL = "https://myfarminfo.com/yfirest.svc/All/States";
    public String GetFarmPersonalInfoURL = "https://myfarminfo.com/yfirest.svc/Clients/GetFarmPersonalInfo";
    public String UploadFarmRegistrationURL = "https://myfarminfo.com/yfirest.svc/SaveFarmerBasicInformation";
    public String updateDeviceTokenURL = "https://myfarminfo.com/yfirest.svc/updateDeviceToken";
    public String UploadNotificationDataURL = "https://myfarminfo.com/yfirest.svc/SaveNotifyResponse";
    public String getFarmDistrictsFarmsforReactivation = "https://myfarminfo.com/yfirest.svc/getFarmDistricts";
    public String getFarmVillagesFarmsforReactivation = "https://myfarminfo.com/yfirest.svc/getFarmVillages";
    public String getFilteredFarmsforReactivation = "https://myfarminfo.com/yfirest.svc/getFilteredFarms";
    //App Screen Tracking
    public String UploadAppsScreenTracking = "http://weathersecurepro.com/PricingService.svc/save_AppScreens";
    public String UploadAppsScreenTracking_save_AppScreens = "https://myfarminfo.com/yfirest.svc/save_AppScreens";
    public String UploadEventLogError = "http://test.weathersecurepro.com/AndroidAPI/EventLog";
    public static String WeedMngtImageURL = "https://ndviimages.s3.ap-south-1.amazonaws.com/WeedImages/";

    //    public String UploadPolicyRegistrationURL = "https://secu.farm/policy/savePolicy";
//    public String UploadPolicyRegistrationURL = "https://secu.farm/PolicyOrder/AddPolicy";
    public String UploadPolicyRegistrationURL = "https://apimfi.com/api/PolicyOrder/AddPolicy";
    //    public String UploadPolicyRegistrationURLTest = "https://test.apimfi.com/api/PolicyOrder/AddPolicy";
    //Local Translaion
//  public static String LocalTranslationURL="https://myfarminfo.com/yfirest.svc/getTranslations";
    public static String LocalTranslationURL = "http://apimfi.com/Utility/getTranslations";


    //Login Check API
    public static String getLoginCheckAPI(String Username, String Password) {
        String URL = appManager.URL_BASE_PATH + "Login/" + Username + "/" + Password + "/0/0/Secufarm";
        return URL;
    } //Login Check API
    public static String getLoginOTP(String mobile) {
        String URL = appManager.URL_BASE_PATH + "Clients/GetOtp/"  + mobile+ "/Secufarm";
        return URL;
    }

    //CattleDashboardAPI Check for Login
    public static String getCattleOwnerID(String strToken, String strPhoneno) {
        String URL = "http://test.weathersecurepro.com/AndroidAPI/getCattleOwnerID/" + strToken + "/" + strPhoneno;
        return URL;
    }

    //CattleDashboardAPI Check for Login
    public static String getOwnerIDLiset(String strToken, String strPhoneno) {
        String URL = "http://test.weathersecurepro.com/AndroidAPI/getCattleID/" + strToken + "/" + strPhoneno;
        return URL;
    }

    //Weather FOrecast
    public static String getForecastURL(String latitude, String longitude, String language) {
        String URL = "https://myfarminfo.com/yfirest.svc/Forecast/v2/" + latitude + "/" + longitude + "/" + language;
        return URL;
    }

    //YoutubeStreams
    public static String getYoutubeStreams(String FarmID) {
//        String URL = "https://myfarminfo.com/yfirest.svc/Streams/" + FarmID + "/false";
        String URL = "https://myfarminfo.com/yfirest.svc/Streams/" + FarmID + "/true";
        return URL;
    }


    public static String getCropNameSowingDate(String FarmID, String UserId, String language) {
        String URL = "https://myfarminfo.com/yfirest.svc/Clients/GetFarmerInfo/gfd/" + FarmID + "/" + UserId + "/" + language;
        return URL;
    }

    //
    public String getCropSchedulerImageTypesURL(String language) {
        if (language == null || language.length() < 1)
            language = "English";
        return "https://myfarminfo.com/yfirest.svc/Scheduler/ImageTypes/" + language;
    }

    public static String GetSymptomsDignosisURL(String cropId) {
        String url = "https://myfarminfo.com/callcenter.svc/GetSymptomsDignosis/" + cropId;
        return url;
    }

    public static String GetRecommendationURL(String cropId) {
        String url = "https://myfarminfo.com/callcenter.svc/GetRecommendation/" + cropId;
        return url;
    }

    public static String getFindDiagnosis = "https://myfarminfo.com/callcenter.svc/FindDiagnosis";

    public static String getDiseaseDignosisImageURL(String diagnosisId, String ImageName) {
        String url = "https://ndviimages.s3.ap-south-1.amazonaws.com/tools/DiseaseImages/" + diagnosisId + "/" + ImageName;
        return url;
    }

    private AppManager() {
    }

    public static String ProjectListURL(String UserID) {
//        public String ProjectListURL="https://www.myfarminfo.com/yfirest.svc/getAdminUsers";
        return "https://www.myfarminfo.com/yfirest.svc/getAdminUsers/" + UserID;
    }

    public static String getServicesAPIURL(String UserID) {
        return "https://myfarminfo.com/yfirest.svc/servicescreens/" + UserID + "/Secufarm";
    }

    public static String GetAdharNumber(String ProjectID) {
        return "https://www.myfarminfo.com/yfirest.svc/Clients/GetAdharNumber/" + ProjectID;
    }

    public static String GetFarmPersonalInfo(String AdharNo) {
        return "https://www.myfarminfo.com/yfirest.svc/Clients/GetFarmPersonalInfo/" + AdharNo;
    }

    //Cattle Registration

    public static String GetCattleSummary(String UserId) {
        return "https://secu.farm/yfirest.svc/Cattle/Summary/" + UserId;
    }

    public static String GetCattleBreedDetails(String LifStageID, String CattleID, String OwnerID) {
        return "https://myfarminfo.com/yfirest.svc/Cattle/Detail/" + LifStageID + "/" + CattleID + "/" + OwnerID;
//        return "https://secu.farm/yfirest.svc/Cattle/LifeStageBreed/2/1?{}\n" + LifStageID + "/" + CattleID + "/" + Userid;
    }

    public static String GetCattleLifeSatgeBreed(String MainCattleID, String GendeID) {
//        return "https://myfarminfo.com/yfirest.svc/Cattle/Detail/" + LifStageID + "/" + CattleID + "/" + Userid;
//        return "https://secu.farm/yfirest.svc/Cattle/LifeStageBreed/" + MainCattleID + "/" + GendeID;
        return "https://myfarminfo.com/yfirest.svc/Cattle/LifeStageBreed/" + MainCattleID + "/" + GendeID;
    }

    //    public static String getCattleSaveURL = "https://secu.farm/yfirest.svc/addCattleInfo";
    public static String getCattleSaveURL = "https://myfarminfo.com/yfirest.svc/addCattleInfo";
    public static String getShedSaveURL = "https://myfarminfo.com/yfirest.svc/SaveCattleShedInfo";

    public static String GetCattleDetails(String ID) {
//        return "https://secu.farm/yfirest.svc/Cattle/View/" + ID;
        return "https://myfarminfo.com/yfirest.svc/Cattle/View/" + ID;
    }

//    public static String GetCattleDetails(String ID) {
////        return "https://secu.farm/yfirest.svc/Cattle/View/" + ID;
//        return "https://myfarminfo.com/yfirest.svc/FarmerPersonel/GetFarmerPersonelInfo" + ID;
//    }

    public static String GetShedList(String OwnerID) {
        return "https://myfarminfo.com/yfirest.svc/GetCattleShedInfo/" + OwnerID;
    }

    public static String GetCattleList(String ShedID) {
        return "https://myfarminfo.com/yfirest.svc/Cattle/GetCattleByShedId/" + ShedID;
    }

    public static String getOwnerListURL = "https://apimfi.com/api/FarmerPersonel/GetFarmerPersonelInfo";

    public static String getCattleDetails = "https://apimfi.com/api/FarmerPersonel/GetFarmerDetails";

    public static String getOwnerCattlelist(String UserID) {
        return "http://test.weathersecurepro.com/AndroidAPI/getRegisteredCattle/" + UserID;
    }


    public static AppManager getInstance() {
        if (appManager == null)
            appManager = new AppManager();
        return appManager;
    }

    public static boolean isOnline(Context context) {
        boolean isConnected = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(context.CONNECTIVITY_SERVICE);
            isConnected = cm.getActiveNetworkInfo().isConnected();
        } catch (Exception ex) {
            isConnected = false;
        }
        return isConnected;
    }

    public static boolean isMobileNoValid(String MobileNo) {
        if (MobileNo != null && !MobileNo.equalsIgnoreCase("null") && MobileNo.length() > 0 && MobileNo.length() == 10) {
            String s = MobileNo; // get your editext value here
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

    public static boolean isEmailIDValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    public static boolean isLocationServicesAvailable(Context context) {
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

    public static void NOGPSDialog(final Context context) {

        final Dialog dialog = new Dialog(context);
        // hide to default title for Dialog
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // inflate the layout dialog_layout.xml and set it as contentView
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.gps_popup, null, false);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(view);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        TextView txt_title = (TextView) dialog.findViewById(R.id.txt_title);
        TextView txtsms = (TextView) dialog.findViewById(R.id.txtsms);
        Button btnoff = (Button) dialog.findViewById(R.id.btnoff);
        Button btnon = (Button) dialog.findViewById(R.id.btnon);

        setFontsStyleTxt(context, txt_title, 2);
        setFontsStyleTxt(context, txtsms, 2);
        setFontsStyle(context, btnoff);
        setFontsStyle(context, btnon);

        //Tab Service
        setDynamicLanguage(context, txt_title, "EnableGPS", R.string.EnableGPS);
        setDynamicLanguage(context, txtsms, "GPSOFF", R.string.GPSOFF);
        setDynamicLanguage(context, btnoff, "OFF", R.string.OFF);
        setDynamicLanguage(context, btnon, "ON", R.string.ON);

        btnon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callGPSSettingIntent = new Intent(
                        Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(callGPSSettingIntent);
                dialog.cancel();
                dialog.dismiss();
            }
        });
        btnoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });
        // Display the dialog
        dialog.show();


    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    public String removeSpaceForUrl(String str) {
        if (str == null) {
            return str;
        }
        str = str.replace(" ", "%20");
        Log.d("after replacing space", str);
        return str;
    }

    public String placeSpaceIntoString(String str) {
        if (str == null) {
            return str;
        }
        str = str.replace("%20", " ");
        Log.d("after replacing space", str);
        return str;
    }

    public String removeShaleshFromVariety(String str) {
        if (str == null) {
        }
        return str = str.replace("/", "~");

    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////
    public String httpRequestGetMethod(String path) {

        String completeUrl = path;
        Log.d("complete url", completeUrl);
        String response = "";
        try {
            response = CustomHttpClient.executeHttpGet(completeUrl);
            System.out.println("response" + response);

            if (response == null) {
                return response;
            }
            if (!response.isEmpty()) {
                response = response.trim();
                response = response.substring(1, response.length() - 1);
                response = response.replace("\\", "");

            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
            response = AppConstant.SERVER_CONNECTION_ERROR;
            System.out.println("" + e.getMessage());
        } catch (ProtocolException e) {
            e.printStackTrace();
            response = AppConstant.SERVER_CONNECTION_ERROR;
            System.out.println("" + e.getMessage());
        } catch (IOException e) {
            System.out.println("" + e.getMessage());
            response = AppConstant.SERVER_CONNECTION_ERROR;
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("" + e.getMessage());
            response = AppConstant.SERVER_CONNECTION_ERROR;
        }
        return response;


    }

//Herojit Add

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public String httpRequestPutMethod(String path, String parameters) {

        String completeUrl = path;

        Log.d("complete url", completeUrl);
        String response = null;
        try {
            response = CustomHttpClient.executeHttpPut(path, parameters);
            System.out.println("before trim response " + response);
            if (response.contains("Error1")) {
                return "Error1";
            }
            if (!response.isEmpty()) {
                response = response.trim();
                response = response.substring(1, response.length() - 1);
                response = response.replace("\\", "");
            }
            return response;

        } catch (MalformedURLException e) {
            response = AppConstant.SERVER_CONNECTION_ERROR;
            e.printStackTrace();
        } catch (ProtocolException e) {
            response = AppConstant.SERVER_CONNECTION_ERROR;
            e.printStackTrace();
            System.out.println("Request processing error Try again!");
        } catch (IOException e) {
            response = AppConstant.SERVER_CONNECTION_ERROR;
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            response = AppConstant.SERVER_CONNECTION_ERROR;
        }
        return response;
    }

    public String httpRequestPutMethodReport(String path, String parameters) {

        String completeUrl = path;

        Log.d("complete url", completeUrl);
        String response = null;
        try {
            String RemoveUnwanted = RemoveStringUnwanted(parameters);
            response = CustomHttpClient.executeHttpPut(path, parameters);
            System.out.println("before trim response " + response);
            if (response.contains("Error1")) {
                return "Error1";
            }
            if (!response.isEmpty()) {
                response = response.trim();
                //   response = response.substring(1, response.length() - 1);
                response = response.replace("\\", "");
            }
            return response;

        } catch (MalformedURLException e) {
            response = AppConstant.SERVER_CONNECTION_ERROR;
            e.printStackTrace();
        } catch (ProtocolException e) {
            response = AppConstant.SERVER_CONNECTION_ERROR;
            e.printStackTrace();
            System.out.println("Request processing error Try again!");
        } catch (IOException e) {
            response = AppConstant.SERVER_CONNECTION_ERROR;
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            response = AppConstant.SERVER_CONNECTION_ERROR;
        }
        return response;
    }

    public String httpRequestPostMethodReport(String path, String parameters) {

        String completeUrl = path;

        Log.d("complete url", completeUrl);
        String response = null;
        try {
            String RemoveUnwanted = RemoveStringUnwanted(parameters);
            response = CustomHttpClient.executeHttpPost(path, parameters);
            System.out.println("before trim response " + response);
            if (response.contains("Error1")) {
                return "Error1";
            }
            if (!response.isEmpty()) {
                response = response.trim();
                //   response = response.substring(1, response.length() - 1);
                response = response.replace("\\", "");
            }
            return response;

        } catch (MalformedURLException e) {
            response = AppConstant.SERVER_CONNECTION_ERROR;
            e.printStackTrace();
        } catch (ProtocolException e) {
            response = AppConstant.SERVER_CONNECTION_ERROR;
            e.printStackTrace();
            System.out.println("Request processing error Try again!");
        } catch (IOException e) {
            response = AppConstant.SERVER_CONNECTION_ERROR;
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            response = AppConstant.SERVER_CONNECTION_ERROR;
        }
        return response;
    }


    public int DateDifference(String SowDt) {
        int val = 0;
        if (!SowDt.equalsIgnoreCase("") && SowDt.length() > 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
            Date Date_sowDate = null, Date_Current = null;
            Date date = new Date();
            try {
                Date Sowdate = (new SimpleDateFormat(("dd MMMM yyyy"), Locale.ENGLISH)).parse(SowDt);
                String dt = (new SimpleDateFormat(("dd-MM-yyyy"), Locale.ENGLISH)).format(Sowdate);
                Date_sowDate = sdf.parse(dt);
                String dt1 = sdf.format(date);
                Date_Current = sdf.parse(dt1);
                long diff = (Date_Current.getTime() - Date_sowDate.getTime());
                long diffDays = diff / (24 * 60 * 60 * 1000);
                if (diffDays > 0) {
                    val = (int) diffDays;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return val;
    }

    public String RemoveStringUnwanted(String value) {
        try {
            if (value != null & value.length() > 0) {
                value = value.trim();
                value = value.replace("\\", "");
                value = value.replace("\"{", "{");
                value = value.replace("}\"", "}");
                value = value.replace("\"[", "[");
                value = value.replace("]\"", "]");
            }
        } catch (OutOfMemoryError ex) {
        } catch (Exception ex) {
        }
        return value;
    }

    public void setLanguages(int flag, Activity activity) {
        SingleGlobal glan = SingleGlobal.getInstance();
        switch (flag) {
            case 1:
                glan.setData("hi");
                break;
            case 2:
                glan.setData("en");
                break;
            case 3:
                glan.setData("gu");
                break;
            case 4:
                glan.setData("mr");
                break;
            case 5:
                glan.setData("bn");
                break;
            case 6:
                glan.setData("te");
                break;
        }

        Locale locale = new Locale(glan.getData());
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        activity.getBaseContext().getResources().updateConfiguration(config,
                activity.getBaseContext().getResources().getDisplayMetrics());
    }

    public boolean checkNullorNot(String value) {

        if (value != null && value.length() > 0 && !value.equalsIgnoreCase("")) {
            return true;
        } else {
            return false;
        }
    }

    public String getSelectedLanguages(Context context) {
        String language = "english";
        try {
            SharedPreferences prefs = context.getSharedPreferences(AppConstant.SHARED_PREFRENCE_NAME, MODE_PRIVATE);
            String langPref = prefs.getString(context.getResources().getString(R.string.language_pref_key), "1");
            int flag = Integer.parseInt(langPref);
            switch (flag) {
                case 2:
                    language = "hindi";
                    break;
                case 1:
                    language = "english";
                    break;
                case 3:
                    language = "gujarati";
                    break;
                case 4:
                    language = "marathi";
                    break;
                case 5:
                    language = "bengali";
                    break;
                case 6:
                    language = "telugu";
                    break;
                default:
                    language = "english";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return language;
    }

    static String FilterName = "";


    public static String getCreateImageName() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "SF_" + timeStamp + ".jpg";
        return imageFileName;
    }

    public static String getCreateImageName(String CustomName) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date());
        String imageFileName = "SF_" + timeStamp + "_" + CustomName + ".jpg";
        return imageFileName;
    }

    public static String getCreateCallRecord() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "SF_" + timeStamp + "_rec.mp3";
        return imageFileName;
    }


    public static String getSowdateConvertddmmYYYY(String SowDt) {
        String val = "";
        if (!SowDt.equalsIgnoreCase("") && SowDt.length() > 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
            try {
                Date Sowdate = (new SimpleDateFormat(("dd MMMM yyyy"), Locale.ENGLISH)).parse(SowDt);
                String dt = (new SimpleDateFormat(("dd-MM-yyyy"), Locale.ENGLISH)).format(Sowdate);
                val = dt;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return val;
    }
}
