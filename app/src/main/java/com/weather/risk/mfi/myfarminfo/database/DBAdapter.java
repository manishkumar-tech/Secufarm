package com.weather.risk.mfi.myfarminfo.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

import com.weather.risk.mfi.myfarminfo.bean.FarmReportBean;
import com.weather.risk.mfi.myfarminfo.entities.AllFarmDetail;
import com.weather.risk.mfi.myfarminfo.mapfragments.LatLonCellID;
import com.weather.risk.mfi.myfarminfo.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import io.requery.android.database.sqlite.SQLiteDatabase;
import io.requery.android.database.sqlite.SQLiteOpenHelper;

import static com.weather.risk.mfi.myfarminfo.utils.LatLngDistance.getDistanceLagLong;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getDateFormatChanged;

public class DBAdapter {


    public static final String DATABASE_TABLE_CROP_VARIETY = "cropVariety";
    public static final String DATABASE_TABLE_ALL_FARM_DETAIL = "allFarmDetail";
    public static final String DATABASE_TABLE_ALL_FARM_REPORT = "allFarmReport";
    public static final String TABLE_CROP = "crop";
    public static final String TABLE_STATE = "state";
    public static final String TABLE_QUERY_CROP = "query_crop";
    public static final String TABLE_CREDENTIAL = "credential";
    public static final String TABLE_CROP_VARIETY = "crop_variety";
    //Herojit Add
    public static final String TABLE_StateDistrict = "MstStateDistrict";
    public static final String TABLE_Projectlist = "MstProjectlist";

    public static final String CROP = "crop";
    public static final String ID = "_id";



    public static final String VARIETY = "variety";
    //////////////////////////////////////////////////////////////////
    public static final String FARM_ID = "farmId";
    public static final String FARM_NAME = "farmName";
    public static final String FARMER_NAME = "farmerName";
    public static final String FARMER_PHONE = "farmerPhone";
    public static final String FARM_AREA = "farmArea";
    public static final String USER_ID = "userId";
    public static final String CONTOUR = "contour";
    public static final String CROP_ID = "cropId";
    public static final String CROPS_VARIETY = "crops_variety";
    public static final String STATE_ID = "state_id";
    public static final String STATE_NAME = "state_name";
    public static final String CROP_FROM = "cropFrom";
    public static final String CROP_TO = "cropTo";
    public static final String BASAL_DOSE_APPLY = "basalDoseApply";
    public static final String VALUE_N = "valueN";
    public static final String VALUE_P = "valueP";
    public static final String VALUE_K = "valueK";
    public static final String OTHER_NUTRIENT = "otherNutrient";
    public static final String CONCERN = "concern";
    public static final String AREA = "area";
    public static final String LOG_DATE = "logDate";
    public static final String MAX_LAT = "maxLat";
    public static final String MAX_LON = "maxLon";
    public static final String MIN_LAT = "minLat";
    public static final String MIN_LON = "minLon";
    public static final String CENTRE_LAT = "centerLat";
    public static final String CENTRE_LON = "centreLon";
    public static final String DISTRICT = "district";
    public static final String TABLE_CATCHED_DATA = "catched_data";
    public static final String TABLE_DASHBOARD_DATA = "dashboard_data";
    public static final String TABLE_SMS_DATA = "sms_data";
    public static final String TYPE = "type";
    public static final String DATASTRING = "datastring";
    public static final String LOGDATE = "logdate";
    public static final String STATUS = "status";
    public static final String USER_NAME = "user_name";
    public static final String VISIBLE_NAME = "visible_name";
    public static final String MOBILE_NO = "mobile_no";
    public static final String PASSWORD = "password";
    public static final String EMAIL_ADDRESS = "email_address";
    public static final String CREATED_DATE_TIME = "created_date_time";
    ///////////////////////////////////////////////////////////////////////
    public static final String NEED_TO_EDIT = "need_to_edit";
    public static final String SENDING_STATUS = "sending_status";
    //Add new fields to  DATABASE_TABLE_ALL_FARM_DETAIL = "allFarmDetail";
    //StateName,DistrictID,Block,VillageID,VillageStr,ProjectID,TaggingApp,FatherName,
    // AadharNo,Aadhar_Other,MobileType,NoOfBags,createaccflag
    public static final String StateName = "StateName";
    public static final String DistrictID = "DistrictID";
    public static final String Block = "Block";
    public static final String VillageID = "VillageID";
    public static final String VillageStr = "VillageStr";
    public static final String ProjectID = "ProjectID";
    public static final String TaggingApp = "TaggingApp";
    public static final String FatherName = "FatherName";
    public static final String AadharNo = "AadharNo";
    public static final String Aadhar_Other = "Aadhar_Other";
    public static final String MobileType = "MobileType";
    public static final String NoOfBags = "NoOfBags";
    public static final String IBCode = "IBCode";
    public static final String createaccflag = "createaccflag";


    public static final String SENT = "sent";
    public static final String SAVE = "save";
    public static final String TRUE = "true";
    public static final String FALSE = "false";
    public static final String CREATE_TABLE_STATE = "create table " + TABLE_STATE + " (" + ID + " integer primary key autoincrement, " +
            STATE_ID + " text not null," +
            STATE_NAME + " text not null);";
    public static final String CREATE_CROP_VATIETY = "create table " + TABLE_CROP_VARIETY + " (" + ID + " integer primary key autoincrement,"
            + STATE_ID + " text not null,"
            + CROP_ID + " text not null,"
            + CROP + " text not null,"
            + VARIETY + " text not null);";
    public static final String CREATE_StateDistrict = "create table " + TABLE_StateDistrict + " (" + "ID" + " integer primary key autoincrement,"
            + "StateID" + " text not null,"
            + "StateName" + " text not null,"
            + "DistrictID" + " text not null,"
            + "DistrictName" + " text not null);";
    public static final String CREATE_TABLE_Projectlist = "create table " + TABLE_Projectlist + " (" + "ID" + " integer primary key autoincrement,"
            + "ProjectID" + " text not null,"
            + "ProjectName" + " text not null);";
    private static final String TAG = "DBAdapter";
    private static final String DATABASE_NAME = "MyDB2";
    private static final int DATABASE_VERSION = 5;
    private static final String DATABASE_CREATE_CROP_VARIETY =
            "create table cropVariety (_id integer primary key autoincrement, "
                    + "crop text not null, variety text not null);";
    private static final String CREATE_TABLE_CROP =
            "create table " + TABLE_CROP + " (" + ID + " integer primary key autoincrement, " +
                    CROP_ID + " text not null," +
                    CROP + " text not null);";
    private static final String CREAT_CREDENTIAL = "create table " + TABLE_CREDENTIAL + " (" + ID + " integer primary key autoincrement,"
            + USER_NAME + " text not null,"
            + VISIBLE_NAME + " text not null,"
            + PASSWORD + " text not null,"
            + EMAIL_ADDRESS + " text,"
            + MOBILE_NO + " text,"
            + USER_ID + " text not null,"
            + CREATED_DATE_TIME + " text not null,"
            + NEED_TO_EDIT + " text not null,"
            + SENDING_STATUS + " text not null);";

    private static final String DATABASE_CREAT_ALL_FARM_DETAIL =
            "create table " + DATABASE_TABLE_ALL_FARM_DETAIL + " (" + ID + " integer primary key autoincrement,"
                    + FARM_ID + " text not null,"
                    + FARM_NAME + " text,"
                    + FARMER_NAME + " text,"
                    + FARMER_PHONE + " text,"
                    + FARM_AREA + " text,"
                    + USER_ID + " text,"
                    + CONTOUR + " text,"
                    + STATE_ID + " text,"
                    + CONCERN + " text,"
                    + AREA + " text,"
                    + LOG_DATE + " text,"
                    + MAX_LAT + " text,"
                    + MAX_LON + " text,"
                    + MIN_LAT + " text,"
                    + MIN_LON + " text,"
                    + CENTRE_LAT + " text,"
                    + CENTRE_LON + " text,"
                    + SENDING_STATUS + " text,"
                    //Add new Fields
                    + StateName + " text,"
                    + DistrictID + " text,"
                    + Block + " text,"
                    + VillageID + " text,"
                    + VillageStr + " text,"
                    + ProjectID + " text,"
                    + TaggingApp + " text,"
                    + FatherName + " text,"
                    + AadharNo + " text,"
                    + Aadhar_Other + " text,"
                    + MobileType + " text,"
                    + NoOfBags + " text,"
                    + IBCode + " text,"
                    + createaccflag + " text);";

    //StateName,DistrictID,Block,VillageID,VillageStr,ProjectID,TaggingApp,FatherName,
    // AadharNo,Aadhar_Other,MobileType,NoOfBags,createaccflag

    private static final String DATABASE_CREAT_ALL_FARM_REPORT =
            "create table " + DATABASE_TABLE_ALL_FARM_REPORT + " (" + ID + " integer primary key autoincrement,"
                    + FARM_ID + " text not null,"
                    + FARM_NAME + " text,"
                    + FARMER_NAME + " text,"
                    + FARMER_PHONE + " text,"
                    + FARM_AREA + " text,"
                    + USER_ID + " text,"
                    + CONTOUR + " text,"
                    + CROP_ID + " text,"
                    + CROPS_VARIETY + " text,"
                    + STATE_ID + " text,"
                    + CROP_FROM + " text,"
                    + CROP_TO + " text,"
                    + BASAL_DOSE_APPLY + " text,"
                    + VALUE_N + " text,"
                    + VALUE_P + " text,"
                    + VALUE_K + " text,"
                    + OTHER_NUTRIENT + " text,"
                    + CONCERN + " text,"
                    + AREA + " text,"
                    + LOG_DATE + " text,"
                    + CENTRE_LAT + " text,"
                    + CENTRE_LON + " text,"
                    + DISTRICT + " text);";


    private static final String DATABASE_CREAT_QUERY_CROP =
            "create table " + TABLE_QUERY_CROP + " (" + ID + " integer primary key autoincrement,"
                    + FARM_ID + " text not null,"
                    + FARM_NAME + " text,"
                    + CROP_ID + " text,"
                    + CROPS_VARIETY + " text,"
                    + CROP_FROM + " text,"
                    + CROP_TO + " text,"
                    + BASAL_DOSE_APPLY + " text,"
                    + VALUE_N + " text,"
                    + VALUE_P + " text,"
                    + VALUE_K + " text,"
                    + OTHER_NUTRIENT + " text,"
                    + CONCERN + " text,"
                    + SENDING_STATUS + " text not null);";

    /*private static final String CREATE_TABLE_STATE = "create table "+TABLE_STATE+" ("+ID+" integer primary key autoincrement,"
            +STATE_ID+" text not null,"
            +STATE_ID+" text not null);";*/
    private static final String DATABASE_CREATED_CATCHED_TABLE =
            "create table " + TABLE_CATCHED_DATA + " (" + ID + " integer primary key autoincrement,"
                    + FARM_ID + " text not null,"
                    + TYPE + " text not null,"
                    + DATASTRING + " text,"
                    + LOGDATE + " text,"
                    + FARM_AREA + " text,"
                    + STATUS + " text);";





    //Add New Table


    public static final String  Message = "Message";
    public static final String  CCSID = "CCSID";
    public static final String  OutDate = "OutDate";
    public static final String  FeddbackDate = "FeddbackDate";
    public static final String  MessageType = "MessageType";
    public static final String  MediaType = "MediaType";
    public static final String  MediaURL = "MediaURL";
    public static final String  Feedback = "Feedback";
    public static final String  SMSID = "SMSID";
    public static final String  foundflag = "foundflag";
    public static final String UID = "UID";
    public static final String ScreenName = "ScreenName";
    public static final String SyncDate = "SyncDate";
    public static final String Data = "Data";
    public static final String InDate = "Date";
    public static final String InTime = "InTime";
    public static final String OutTime = "OutTime";
    public static final String TimeDuration = "TimeDuration";
    public static final String AppName = "App_Name";
    public static final String UserIDs = "UserID";
    public static final String FarmIDs = "FarmID";
    public static final String DeviceIMEI = "DeviceIMEI";
    public static final String TABLE_SCREENTRACKING = "tblScreenTracking";
    public static final String DATABASE_CREATED_SCREENTRACKING =
            "create table " + TABLE_SCREENTRACKING + " (" + UID + " text not null,"
                    + ScreenName + " text not null,"
                    + InDate + " text not null,"
                    + InTime + " text,"
                    + OutTime + " text,"
                    + TimeDuration + " text,"
                    + AppName + " text,"
                    + DeviceIMEI + " text,"
                    + UserIDs + " integer,"
                    + FarmIDs + " integer);";


    private static final String SMS_DATA_TABLE =
            "create table " + TABLE_SMS_DATA + " (" + ID + " integer primary key autoincrement,"
                    + Message + " text not null,"
                    + CCSID + " text not null,"
                    + OutDate + " text not null,"
                    + FeddbackDate + " text not null,"
                    + MessageType + " text not null,"
                    + MediaType + " text not null,"
                    + MediaURL + " text not null,"
                    + Feedback + " text not null,"
                    + SMSID + " text not null,"
                    + foundflag + " text not null,"
                    + USER_ID + " text not null,"
                    + SyncDate + " text);";

    private static final String DASHBOARD_DATA_TABLE =
            "create table " + TABLE_DASHBOARD_DATA + " (" + ID + " integer primary key autoincrement,"
                    + ScreenName + " text not null,"
                    + SyncDate + " text not null,"
                    + Data + " text);";

    public static final String ImageName = "ImageName";
    public static final String Current_Lat = "Current_Lat";
    public static final String Current_Long = "Current_Long";
    public static final String Distance = "Distance";

    public static final String TABLE_ImageLocalStorage = "tblImageLocalStorage";
    public static final String DATABASE_CREATED_ImageLocalStorage =
            "create table " + TABLE_ImageLocalStorage + " ("
                    + ImageName + " text not null,"
                    + Current_Lat + " text not null,"
                    + Current_Long + " text not null,"
                    + Distance + " text);";

    public static final String Youtube_ID = "ID";
    public static final String Title = "Title";
    public static final String Description = "Description";
    public static final String DateFrom = "DateFrom";
    public static final String DateFrom_time = "DateFromTime";
    public static final String DateTo = "DateTo";
    public static final String DateTo_time = "DateToTime";
    public static final String ProjectIDs = "ProjectID";
    public static final String VideoID = "VideoID";
    public static final String VisibleName = "VisibleName";
    public static final String UploadedDate = "UploadedDate";

    public static final String TABLE_YoutubeVideoDateTime = "tblYoutubeVideoDateTime";
    public static final String DATABASE_CREATED_YoutubeVideoDateTime =
            "create table " + TABLE_YoutubeVideoDateTime + " (" + Youtube_ID + " integer primary key autoincrement, "
                    + Title + " text not null,"
                    + Description + " text not null,"
                    + DateFrom + " text not null,"
                    + DateFrom_time + " text not null,"
                    + DateTo + " text not null,"
                    + DateTo_time + " text not null,"
                    + ProjectIDs + " text not null,"
                    + VideoID + " text not null,"
                    + UploadedDate + " text not null,"
                    + VisibleName + " text);";


    //Plant Doctor Local Storage
    public static final String TABLE_tblPlantdocLocalSMS = "tblPlantdocLocalSMS";
    public static final String ImageName1 = "ImageName1";
    public static final String ImageName2 = "ImageName2";
    public static final String VoiceRecName = "VoiceRecName";
    public static final String UploadedImageNamCloseup = "UploadedImageNamCloseup";
    public static final String UploadedImageNamFarm = "UploadedImageNamFarm";
    public static final String JsonFile = "JsonFile";
    public static final String DATABASE_CREATED_PlantdocLocalSMS =
            "create table " + TABLE_tblPlantdocLocalSMS + " (" + Youtube_ID + " integer primary key autoincrement, "
                    + ImageName1 + " text,"
                    + ImageName2 + " text,"
                    + VoiceRecName + " text,"
                    + UploadedImageNamCloseup + " text,"
                    + UploadedImageNamFarm + " text,"
                    + JsonFile + " text);";

    //EventLog Error
    public static final String TABLE_tblEventLogError = "tblEventLogError";
    public static final String Apps = "Apps";
    public static final String ScreenActivity = "ScreenActivity";
    public static final String UserID = "UserID";
    public static final String Location = "Location";
    public static final String DateTime = "DateTime";
    public static final String URL = "URL";
    public static final String APITakingTime = "APITakingTime";
    public static final String Json = "Json";
    public static final String Error = "Error";
    public static final String Status = "Status";
    public static final String DATABASE_CREATED_EventLogError =
            "create table " + TABLE_tblEventLogError + " (" + Youtube_ID + " integer primary key autoincrement, "
                    + Apps + " text,"
                    + ScreenActivity + " text,"
                    + UserID + " text,"
                    + Location + " text,"
                    + DateTime + " text,"
                    + URL + " text,"
                    + APITakingTime + " text,"
                    + Json + " text,"
                    + Error + " text,"
                    + FarmIDs + " text,"
                    + Status + " text);";

    //Local STatic String

    public static final String tblLocalTranslation = "tblLocalTranslation";
    public static final String ID_Local = "ID";
    public static final String MyKey = "MyKey";
    public static final String English = "English";
    public static final String Telugu = "Telugu";
    public static final String Tamil = "Tamil";
    public static final String Marathi = "Marathi";
    public static final String Hindi = "Hindi";
    public static final String Gujarati = "Gujarati";
    public static final String Bengali = "Bengali";
    public static final String Category = "Category";
    public static final String DataType = "DataType";
    public static final String DATABASE_CREATED_LocalTranslation =
            "create table " + tblLocalTranslation + " (" + ID_Local + " int,"
                    + MyKey + " text,"
                    + English + " text,"
                    + Telugu + " text,"
                    + Tamil + " text,"
                    + Marathi + " text,"
                    + Hindi + " text,"
                    + Gujarati + " text,"
                    + Bengali + " text,"
                    + Category + " text,"
                    + DataType + " text);";

    private final Context context;
    public io.requery.android.database.sqlite.SQLiteDatabase db;
    private DatabaseHelper DBHelper;

    public DBAdapter(Context ctx) {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    public io.requery.android.database.sqlite.SQLiteDatabase getSQLiteDatabase() {
        io.requery.android.database.sqlite.SQLiteDatabase db = DBHelper.getWritableDatabase();
        return db;
    }

    //---opens the database---
    public DBAdapter open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    //---closes the database---
    public void close() {
        DBHelper.close();
    }

    public long insertCropVariety(String crop, String variety) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(CROP, crop);
        initialValues.put(VARIETY, variety);
        return db.insert(DATABASE_TABLE_CROP_VARIETY, null, initialValues);
    }
    ///////////////////////////////////////////////////////////////  all query written from here
    //---insert a contact into the database---

    public long insertAllFarmDetail(AllFarmDetail allFarmDetail, String sendingStatus) {

        long l = -1;
        try {
            ContentValues initialValues = new ContentValues();
            initialValues.put(FARM_ID, allFarmDetail.getFarmId());
            initialValues.put(FARM_NAME, allFarmDetail.getFarmName());
            initialValues.put(FARMER_NAME, allFarmDetail.getFarmerName());
            initialValues.put(FARMER_PHONE, allFarmDetail.getFarmerPhone());
            initialValues.put(FARM_AREA, allFarmDetail.getActualFarmArea());
            System.out.println("Saved userId with farm : " + allFarmDetail.getUserId());
            initialValues.put(USER_ID, allFarmDetail.getUserId());
            initialValues.put(CONTOUR, allFarmDetail.getContour());
        /*initialValues.put(CROP_ID, allFarmDetail.getCropId());
		initialValues.put(CROPS_VARIETY, allFarmDetail.getVariety());*/
            initialValues.put(STATE_ID, allFarmDetail.getState());
		/*initialValues.put(CROP_FROM, allFarmDetail.getCropFrom());
		initialValues.put(CROP_TO, allFarmDetail.getCropTo());
		initialValues.put(BASAL_DOSE_APPLY, allFarmDetail.getBasalDoseApply());
		initialValues.put(VALUE_N, allFarmDetail.getValueN());
		initialValues.put(VALUE_P, allFarmDetail.getValueP());
		initialValues.put(VALUE_K, allFarmDetail.getValueK());
		initialValues.put(OTHER_NUTRIENT, allFarmDetail.getOtherNutrient());*/
            initialValues.put(CONCERN, allFarmDetail.getConcern());
            initialValues.put(AREA, allFarmDetail.getArea());
            initialValues.put(LOG_DATE, allFarmDetail.getLogDate());
            initialValues.put(MAX_LAT, allFarmDetail.getMaxLat());
            initialValues.put(MAX_LON, allFarmDetail.getMaxLon());
            initialValues.put(MIN_LAT, allFarmDetail.getMinLat());
            initialValues.put(MIN_LON, allFarmDetail.getMinLon());
            initialValues.put(CENTRE_LAT, allFarmDetail.getCenterLat());
            initialValues.put(CENTRE_LON, allFarmDetail.getCentreLon());
            initialValues.put(SENDING_STATUS, sendingStatus);

            l = db.insert(DATABASE_TABLE_ALL_FARM_DETAIL, null, initialValues);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return l;
    }


    public long deleteDashboardData() {
        try {
            return db.delete(TABLE_DASHBOARD_DATA, ScreenName + " = 'Dashboard' ", null);

        } catch (Exception e) {

            e.printStackTrace();
            return -1;
        }
    }

    public long insertDashboardData(String screenName, String syncdate, String data) {

        long l = -1;
        try {
            ContentValues initialValues = new ContentValues();

            initialValues.put(ScreenName, screenName);
            initialValues.put(SyncDate, syncdate);
            initialValues.put(Data, data);

            l = db.insert(TABLE_DASHBOARD_DATA, null, initialValues);

            System.out.println("Manishtest Dashboard Data inserted");

        } catch (Exception e) {
            System.out.println("Manishtest Dashboard Data not inserted");
            e.printStackTrace();
        }
        return l;
    }


    public long insertDashboardSMSData(String message, String ccsid, String outDate,String feddbackdate,String messagetype,String mediatype,String mediaurl,String feedback,String smsid,String foundflagg,String syncDate,String userid) {

        long l = -1;
        try {
            ContentValues initialValues = new ContentValues();

            initialValues.put(Message, message);
            initialValues.put(CCSID, ccsid);
            initialValues.put(OutDate,outDate);
            initialValues.put(FeddbackDate, feddbackdate);
            initialValues.put(MessageType, messagetype);
            initialValues.put(MediaType, mediatype+"");
            initialValues.put(MediaURL, mediaurl+"");
            initialValues.put(Feedback, feedback);
            initialValues.put(SMSID, smsid);
            initialValues.put(foundflag, foundflagg);
            initialValues.put(SyncDate, syncDate);
            initialValues.put(USER_ID, userid);

            l = db.insert(TABLE_SMS_DATA, null, initialValues);

            System.out.println("Manishtest Dashboard Data inserted");

        } catch (Exception e) {
            System.out.println("Manishtest Dashboard Data not inserted");
            e.printStackTrace();
        }
        return l;
    }

    //Update after farmer selection in Dashboard
    public long UpdatedFarmerDetails(AllFarmDetail allFarmDetail, String sendingStatus) {

        long l = -1;
        try {
            ContentValues initialValues = new ContentValues();
//            initialValues.put(FARM_ID, allFarmDetail.getFarmId());
//            initialValues.put(FARM_NAME, allFarmDetail.getFarmName());
            initialValues.put(FARMER_NAME, allFarmDetail.getFarmerName());
            initialValues.put(FARMER_PHONE, allFarmDetail.getFarmerPhone());
            initialValues.put(FARM_AREA, allFarmDetail.getActualFarmArea());
            System.out.println("Saved userId with farm : " + allFarmDetail.getUserId());
//            initialValues.put(USER_ID, allFarmDetail.getUserId());
            initialValues.put(CONTOUR, allFarmDetail.getContour());
            initialValues.put(STATE_ID, allFarmDetail.getState());
            initialValues.put(CONCERN, allFarmDetail.getConcern());
            initialValues.put(AREA, allFarmDetail.getArea());
            initialValues.put(LOG_DATE, allFarmDetail.getLogDate());
            initialValues.put(MAX_LAT, allFarmDetail.getMaxLat());
            initialValues.put(MAX_LON, allFarmDetail.getMaxLon());
            initialValues.put(MIN_LAT, allFarmDetail.getMinLat());
            initialValues.put(MIN_LON, allFarmDetail.getMinLon());
            initialValues.put(CENTRE_LAT, allFarmDetail.getCenterLat());
            initialValues.put(CENTRE_LON, allFarmDetail.getCentreLon());
//            initialValues.put(SENDING_STATUS, sendingStatus);
            l = db.update(DATABASE_TABLE_ALL_FARM_DETAIL, initialValues, FARM_ID + " = '" + allFarmDetail.getFarmId() + "'", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return l;
    }


    public void UpdateFarmDetailContourCenLagLong(JSONArray array) {
        try {
            if (array != null && array.length() > 0) {
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    String contour = object.getString("Contour");
                    String centerLat = object.getString("CenterLat");
                    String centreLon = object.getString("CenterLon");
                    String farmId = object.getString("ID");

                    String sql = "update allFarmDetail set contour='" + contour + "',centerLat='" + centerLat + "',centreLon='" + centreLon + "' where farmId='" + farmId + "' ";
                    db.execSQL(sql);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public long insertAllFarmReport(FarmReportBean farmReportBean) {

        long l = -1;
        try {
            ContentValues initialValues = new ContentValues();
            initialValues.put(FARM_ID, farmReportBean.getFarmID());
            initialValues.put(FARM_NAME, farmReportBean.getFarmName());
            initialValues.put(FARMER_NAME, farmReportBean.getFarmerName());
            initialValues.put(FARMER_PHONE, farmReportBean.getPhoneNo());
            initialValues.put(FARM_AREA, farmReportBean.getFarmArea());
            System.out.println("Saved userId with farm : " + farmReportBean.getUserName());
            initialValues.put(USER_ID, farmReportBean.getUserId());
            initialValues.put(CONTOUR, farmReportBean.getContour());
            initialValues.put(CROP_ID, farmReportBean.getCropName());
            initialValues.put(CROPS_VARIETY, farmReportBean.getVariety());
            initialValues.put(STATE_ID, farmReportBean.getState());
            initialValues.put(CROP_FROM, farmReportBean.getCropFrom());
            initialValues.put(CROP_TO, farmReportBean.getCropTo());
            initialValues.put(BASAL_DOSE_APPLY, farmReportBean.getBasalDoseApply());
            initialValues.put(VALUE_N, farmReportBean.getN());
            initialValues.put(VALUE_P, farmReportBean.getP());
            initialValues.put(VALUE_K, farmReportBean.getK());
            initialValues.put(OTHER_NUTRIENT, farmReportBean.getOtherNutrient());
            initialValues.put(CONCERN, farmReportBean.getConcern());
            initialValues.put(AREA, farmReportBean.getArea());
            initialValues.put(LOG_DATE, farmReportBean.getSowDate());
            initialValues.put(CENTRE_LAT, farmReportBean.getCenterLat());
            initialValues.put(CENTRE_LON, farmReportBean.getCenterLon());
            initialValues.put(DISTRICT, farmReportBean.getDistrict());

            l = db.insert(DATABASE_TABLE_ALL_FARM_REPORT, null, initialValues);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return l;
    }

    public long insertStateDistrict(JSONArray statedisValue) {

        long l = -1;
        try {
            if (statedisValue.length() > 0) {
                for (int i = 0; i < statedisValue.length(); i++) {
                    try {
                        JSONObject obj = new JSONObject(statedisValue.get(i).toString());
                        ContentValues initialValues = new ContentValues();
                        initialValues.put("ID", i + 1);
                        initialValues.put("StateID", obj.get("StateID").toString());
                        initialValues.put("StateName", obj.get("State").toString());
                        initialValues.put("DistrictID", obj.get("District_ID").toString());
                        initialValues.put("DistrictName", obj.get("District").toString());
                        l = db.insert(TABLE_StateDistrict, null, initialValues);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return l;
    }

    public Cursor getStateDistrict(String StateID, String DistrictID, int flag) {
        try {
            String SQL = "";
            if (flag == 1)
                SQL = "select StateID,StateName from " + TABLE_StateDistrict + " group by StateID";//State
            return db.rawQuery(SQL, null);
        } catch (Exception ex) {
            return null;
        }
//        return db.query(DATABASE_TABLE_ALL_FARM_DETAIL, new String[]{FARM_NAME,FARM_ID,FARMER_NAME}, USER_ID + " = '" + userId + "'", null, null, null, null);

    }

    public void deleteFarmTable() {
        db.execSQL("delete from " + DATABASE_TABLE_ALL_FARM_REPORT);
    }

    public Cursor getallFarmName(String userId) {
        try {
            String sql = "select farmName,farmId,contour,centerLat,centreLon from allFarmDetail where userId= '" + userId + "' order by farmName asc";
            return db.rawQuery(sql, null);
        } catch (Exception ex) {
            return null;
        }
//        return db.query(DATABASE_TABLE_ALL_FARM_DETAIL, new String[]{FARM_NAME,FARM_ID,FARMER_NAME}, USER_ID + " = '" + userId + "'", null, null, null, null);

    }

    public Cursor getallContour(String userId) {
        return db.query(DATABASE_TABLE_ALL_FARM_DETAIL, new String[]{FARM_NAME, CENTRE_LAT, CENTRE_LON}, USER_ID + " = '" + userId + "'", null, null, null, null);

    }

    //---retrieves all the contacts---
    public Cursor getCropList() {
        return db.query(DATABASE_TABLE_CROP_VARIETY, new String[]{CROP,
                VARIETY}, null, null, null, null, null);
    }

    //---retrieves a particular contact---
    public Cursor getVariety(String name) throws SQLException {
        return db.query(true, DATABASE_TABLE_CROP_VARIETY, new String[]{
                VARIETY}, CROP + "='" + name + "'", null, null, null, null, null);

    }

    public Cursor getAllCrop() {
        return db.query(true, TABLE_CROP, new String[]{CROP_ID, CROP}, null, null, null, null, null, null);
    }

    public String getCropIdByName(String name) {
        String cropId = "";
        try {
            Cursor cursor = db.query(true, TABLE_CROP_VARIETY, new String[]{CROP_ID, CROP}, CROP + " = '" + name + "'", null, null, null, null, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                cropId = cursor.getString(cursor.getColumnIndex(CROP_ID));
            }
            cursor.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return cropId;
    }

    public String getCropNameByID(String id) {
        String cropName = "";
        Cursor cursor = db.query(true, TABLE_CROP_VARIETY, new String[]{CROP_ID, CROP}, CROP_ID + " = '" + id + "'", null, null, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            cropName = cursor.getString(cursor.getColumnIndex(CROP));
        }
        cursor.close();
        return cropName;
    }

    public Cursor getSelectedFarmsValue(String farmName) {
        return db.query(true, DATABASE_TABLE_ALL_FARM_DETAIL, new String[]
                        {
                                FARM_ID,
                                FARM_NAME,
                                FARMER_NAME,
                                FARMER_PHONE,
                                FARM_AREA,
                                USER_ID,
                                CONTOUR,
                                STATE_ID,
                                CONCERN,
                                AREA
                        },
                FARM_ID + "='" + farmName + "'", null, null, null, null, null);
    }

    public Cursor getStateFromSelectedFarm(String farmId) {
        return db.query(true, DATABASE_TABLE_ALL_FARM_DETAIL, new String[]{FARM_ID, CENTRE_LAT, CONTOUR, CENTRE_LON, STATE_ID, AREA}, FARM_ID + "='" + farmId + "'", null, null, null, null, null);
    }

    public Cursor getSavedForm() {
        return db.query(true, DATABASE_TABLE_ALL_FARM_DETAIL, null, SENDING_STATUS + "='" + SAVE + "'", null, null, null, null, null);
    }

    public Cursor isFarmAlreadyExist(String farmName, String userId, String FARMER_PHONEs) {
        return db.query(true, DATABASE_TABLE_ALL_FARM_DETAIL, null, FARM_NAME + "='" + farmName + "' AND " + USER_ID + " ='" + userId + "' AND " + FARMER_PHONE + " ='" + FARMER_PHONEs + "'", null, null, null, null, null);
    }

    public Cursor isFarmAlreadyExist(String farmId) {
        return db.query(true, DATABASE_TABLE_ALL_FARM_DETAIL, null, FARM_ID + "='" + farmId + "'", null, null, null, null, null);
    }

    public Cursor getFarmByFarmName(String farmName) {
        return db.query(true, DATABASE_TABLE_ALL_FARM_DETAIL, null, FARM_ID + "='" + farmName + "'", null, null, null, null, null);
    }

    public Cursor getFarmByFarmID(String farmId) {
        return db.query(true, DATABASE_TABLE_ALL_FARM_REPORT, null, FARM_ID + "='" + farmId + "'", null, null, null, null, null);
    }

    public Cursor getAllForm() {
        return db.query(true, DATABASE_TABLE_ALL_FARM_DETAIL, null, null, null, null, null, null, null);
    }

    public boolean updateFormByUserId(String oldUserId, String newUserId) {
        boolean isUpdated = false;
        Cursor cursor = db.query(true, DATABASE_TABLE_ALL_FARM_DETAIL, null, USER_ID + "='" + oldUserId + "'", null, null, null, null, null);
        System.out.println("Coresponding farm with old userId : " + cursor.getCount());
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            ContentValues values = new ContentValues();
            values.put(ID, cursor.getString(cursor.getColumnIndex(ID)));
            values.put(FARM_ID, cursor.getString(cursor.getColumnIndex(FARM_ID)));
            values.put(FARM_NAME, cursor.getString(cursor.getColumnIndex(FARM_NAME)));
            values.put(FARMER_NAME, cursor.getString(cursor.getColumnIndex(FARMER_NAME)));
            values.put(FARMER_PHONE, cursor.getString(cursor.getColumnIndex(FARMER_PHONE)));
            values.put(FARM_AREA, cursor.getString(cursor.getColumnIndex(FARM_AREA)));
            values.put(USER_ID, newUserId);
            values.put(CONTOUR, cursor.getString(cursor.getColumnIndex(CONTOUR)));
            values.put(STATE_ID, cursor.getString(cursor.getColumnIndex(STATE_ID)));
            values.put(CONCERN, cursor.getString(cursor.getColumnIndex(CONCERN)));
            values.put(AREA, cursor.getString(cursor.getColumnIndex(AREA)));
            values.put(LOG_DATE, cursor.getString(cursor.getColumnIndex(LOG_DATE)));
            values.put(MAX_LAT, cursor.getString(cursor.getColumnIndex(MAX_LAT)));
            values.put(MAX_LON, cursor.getString(cursor.getColumnIndex(MAX_LON)));
            values.put(MIN_LAT, cursor.getString(cursor.getColumnIndex(MIN_LAT)));
            values.put(MIN_LON, cursor.getString(cursor.getColumnIndex(MIN_LON)));
            values.put(CENTRE_LAT, cursor.getString(cursor.getColumnIndex(CENTRE_LAT)));
            values.put(CENTRE_LON, cursor.getString(cursor.getColumnIndex(CENTRE_LON)));
            values.put(SENDING_STATUS, cursor.getString(cursor.getColumnIndex(SENDING_STATUS)));
            long k = db.update(DATABASE_TABLE_ALL_FARM_DETAIL, values, USER_ID + " = '" + oldUserId + "'", null);
            if (k != -1) {
                isUpdated = true;
            }
        }
        cursor.close();

        return isUpdated;
    }

    //Herojit Update after Farmer Selection

    public boolean updateFarmReportByFarmId(String oldFarmiId, FarmReportBean farmReportBean) {
        boolean isUpdated = false;
        Cursor cursor = db.query(true, DATABASE_TABLE_ALL_FARM_REPORT, null, FARM_ID + "='" + oldFarmiId + "'", null, null, null, null, null);
        System.out.println("Coresponding farm with old userId : " + cursor.getCount());
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            ContentValues initialValues = new ContentValues();
            initialValues.put(FARM_ID, farmReportBean.getFarmID());
            initialValues.put(FARM_NAME, farmReportBean.getFarmName());
            initialValues.put(FARMER_NAME, farmReportBean.getFarmerName());
            initialValues.put(FARMER_PHONE, farmReportBean.getPhoneNo());
            initialValues.put(FARM_AREA, farmReportBean.getFarmArea());
            System.out.println("Saved userId with farm : " + farmReportBean.getUserName());
            initialValues.put(USER_ID, farmReportBean.getUserId());
            initialValues.put(CONTOUR, farmReportBean.getContour());
            initialValues.put(CROP_ID, farmReportBean.getCropName());
            initialValues.put(CROPS_VARIETY, farmReportBean.getVariety());
            initialValues.put(STATE_ID, farmReportBean.getState());
            initialValues.put(CROP_FROM, farmReportBean.getCropFrom());
            initialValues.put(CROP_TO, farmReportBean.getCropTo());
            initialValues.put(BASAL_DOSE_APPLY, farmReportBean.getBasalDoseApply());
            initialValues.put(VALUE_N, farmReportBean.getN());
            initialValues.put(VALUE_P, farmReportBean.getP());
            initialValues.put(VALUE_K, farmReportBean.getK());
            initialValues.put(OTHER_NUTRIENT, farmReportBean.getOtherNutrient());
            initialValues.put(CONCERN, farmReportBean.getConcern());
            initialValues.put(AREA, farmReportBean.getArea());
            initialValues.put(LOG_DATE, farmReportBean.getSowDate());
            initialValues.put(CENTRE_LAT, farmReportBean.getCenterLat());
            initialValues.put(CENTRE_LON, farmReportBean.getCenterLon());
            initialValues.put(DISTRICT, farmReportBean.getDistrict());
            long k = db.update(DATABASE_TABLE_ALL_FARM_REPORT, initialValues, FARM_ID + " = '" + oldFarmiId + "'", null);
            if (k != -1) {
                isUpdated = true;
            }
        }
        cursor.close();

        return isUpdated;
    }

    public long deleteAllCropVarietyTableRecord() {
        try {
            return db.delete(DATABASE_TABLE_CROP_VARIETY, null, null);

        } catch (Exception e) {

            e.printStackTrace();
            return -1;
        }
    }

    public Cursor getCropQueryByFarmId(String farmId) {
        System.out.println("FarmId : " + farmId);
        return db.query(true, TABLE_QUERY_CROP, null, FARM_ID + "='" + farmId + "'", null, null, null, null, null);
    }

/*
    public long deleteAllFarmDetailTable() {
        try {
            return db.delete(DATABASE_TABLE_ALL_FARM_DETAIL, null, null);

        } catch (Exception e) {

            e.printStackTrace();
            return -1;
        }
    }
*/

    public Cursor getCropQueryById(String id) {
        return db.query(true, TABLE_QUERY_CROP, null, ID + "='" + id + "'", null, null, null, null, null);
    }

    public Cursor getAllStates() {
        return db.query(true, TABLE_STATE, null, STATE_NAME + " != '_Unknown'", null, null, null, null, null);
    }

    public Cursor getCropByState(String stateId) {
        return db.query(true, TABLE_CROP_VARIETY, null, STATE_ID + " = '" + stateId + "'", null, CROP_ID, null, CROP, null);
    }

    public Cursor getVarietyByStateAndCrop(String stateId, String cropId) {
        return db.query(true, TABLE_CROP_VARIETY, new String[]{VARIETY}, STATE_ID + " = '" + stateId + "' AND " + CROP_ID + " = '" + cropId + "'", null, null, null, null, null);
    }

    public Cursor getAllCropVariety() {
        return db.query(true, TABLE_CROP_VARIETY, null, null, null, null, null, null, null);
    }

    public Cursor getAllStateDistrict() {
        return db.query(true, TABLE_StateDistrict, null, null, null, null, null, null, null);
    }

    public Cursor getAllProject() {
        return db.query(true, TABLE_Projectlist, null, null, null, null, null, null, null);
    }

    public Cursor getCropNameById(String cropId) {
        return db.query(true, TABLE_CROP_VARIETY, null, CROP_ID + " = '" + cropId + "'", null, null, null, null, null);
    }

    public Cursor getSavedCredentials() {
        return db.query(true, TABLE_CREDENTIAL, null, SENDING_STATUS + " = '" + SAVE + "'", null, null, null, null, null);
    }

    public Cursor getAllCredentials() {
        return db.query(true, TABLE_CREDENTIAL, null, null, null, null, null, null, null);
    }

    public Cursor isUserExist(String emailAddress) {
        return db.query(true, TABLE_CREDENTIAL, null, EMAIL_ADDRESS + "='" + emailAddress + "'", null, null, null, null, null);

    }

    public Cursor isUserSaved(String userId) {
        return db.query(true, TABLE_CREDENTIAL, null, USER_ID + "='" + userId + "' AND " + SENDING_STATUS + " ='" + SAVE + "'", null, null, null, null, null);

    }

    public Cursor isAuthenticated(String emailAddress, String password) {
        return db.query(true, TABLE_CREDENTIAL, null, EMAIL_ADDRESS + "='" + emailAddress + "' AND " + PASSWORD + " = '" + password + "'", null, null, null, null, null);

    }

    public Cursor getCatchedData(String farmidOut, String typeOut, String logDate, String statusOut) {
        return db.query(false, TABLE_CATCHED_DATA, new String[]{FARM_ID, TYPE, DATASTRING, LOGDATE, STATUS}, FARM_ID + " = '" + farmidOut + "' and " + TYPE + " = '" + typeOut + "' and " + LOGDATE + " = '" + logDate + "' and " + STATUS + " = '" + statusOut + "'", null, null, null, null, null);
    }

    public long deleteCatchedData(String farmidOut, String typeOut, String logDate, String statusOut) {
        try {
            return db.delete(TABLE_CATCHED_DATA, FARM_ID + " = '" + farmidOut + "' and " + TYPE + " = '" + typeOut + "' and " + LOGDATE + " <> '" + logDate + "' and " + STATUS + " = '" + statusOut + "'", null);

        } catch (Exception e) {

            e.printStackTrace();
            return -1;
        }
    }

    public long insertCatchedData(String FarmId, String TypeOut, String DataOut, String StatusOut) {

        long l = -1;
        try {
            Date date = new Date();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            //to convert Date to String, use format method of SimpleDateFormat class.
            String strDate = dateFormat.format(date);

            ContentValues initialValues = new ContentValues();
            initialValues.put(FARM_ID, FarmId);
            initialValues.put(TYPE, TypeOut);
            initialValues.put(DATASTRING, DataOut);
            initialValues.put(LOGDATE, strDate);
            initialValues.put(STATUS, StatusOut);

            l = db.insert(TABLE_CATCHED_DATA, null, initialValues);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return l;
    }

    //Herojit Add
    public ArrayList<HashMap<String, String>> getDynamicTableValue(String SQL) {
        ArrayList<HashMap<String, String>> data = null;

        Cursor cursor = null;
        try {
            cursor = db.rawQuery(SQL, null);
            if (cursor != null) {
                data = new ArrayList<HashMap<String, String>>();
                cursor.moveToFirst();
                while (cursor.isAfterLast() == false) {
                    HashMap<String, String> params = new HashMap<String, String>();
                    for (int i = 0; i < cursor.getColumnNames().length; i++) {
                        params.put(cursor.getColumnName(i), cursor.getString(i));
                    }
                    data.add(params);
                    cursor.moveToNext();
                }
                cursor.close();
            }
            return data;
        } catch (Exception exception) {
            Log.e("DataProvider",
                    "Error in tblUser :: " + exception.getMessage());
        }
        return data;
    }

    //Herojit Add
    public HashMap<String, String> getDynamicTableKeyValue(String SQL) {
        HashMap<String, String> data = null;

        Cursor cursor = null;
        try {
            cursor = db.rawQuery(SQL, null);
            if (cursor != null) {
                data = new HashMap<String, String>();
                cursor.moveToFirst();
                while (cursor.isAfterLast() == false) {
                    String col1 = cursor.getColumnName(0);
                    String col2 = cursor.getColumnName(1);
                    data.put(cursor.getString(0), cursor.getString(1));
//                    params.put(cursor.getColumnName(i), cursor.getString(i));
                    cursor.moveToNext();
                }
                cursor.close();
            }
            return data;
        } catch (Exception exception) {
            Log.e("DataProvider",
                    "Error in tblUser :: " + exception.getMessage());
        }
        return data;
    }

    public static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(DATABASE_CREATE_CROP_VARIETY);
                db.execSQL(DATABASE_CREAT_ALL_FARM_REPORT);
                db.execSQL(CREATE_TABLE_CROP);
                db.execSQL(DATABASE_CREAT_QUERY_CROP);
                db.execSQL(CREAT_CREDENTIAL);
                db.execSQL(CREATE_TABLE_STATE);
                db.execSQL(CREATE_CROP_VATIETY);
                db.execSQL(DATABASE_CREAT_ALL_FARM_DETAIL);
                db.execSQL(DATABASE_CREATED_CATCHED_TABLE);
                //Herojit Add
                db.execSQL(CREATE_StateDistrict);
                db.execSQL(CREATE_TABLE_Projectlist);
                db.execSQL(DATABASE_CREATED_SCREENTRACKING);
                db.execSQL(DATABASE_CREATED_ImageLocalStorage);
                db.execSQL(DATABASE_CREATED_YoutubeVideoDateTime);
                db.execSQL(DATABASE_CREATED_PlantdocLocalSMS);
                db.execSQL(DATABASE_CREATED_EventLogError);
                db.execSQL(DATABASE_CREATED_LocalTranslation);
                db.execSQL(CREATE_CART);

                db.execSQL(DASHBOARD_DATA_TABLE);

                db.execSQL(SMS_DATA_TABLE);

            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS contacts");
            db.execSQL("DROP TABLE IF EXISTS " + "cropVariety");
            // db.execSQL(DATABASE_CREAT_ALL_FARM_DETAIL);
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_ALL_FARM_REPORT);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CROP);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUERY_CROP);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CREDENTIAL);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_STATE);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CROP_VARIETY);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATCHED_DATA);
            //Herojit Add
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CROP_VARIETY);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_STATE);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_StateDistrict);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_Projectlist);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCREENTRACKING);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_DASHBOARD_DATA);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_SMS_DATA);

            onCreate(db);

        }
    }

    //Add Herojit Farm Insert Update
    public int SaveUpdateFarmdetails(ContentValues cv, String flag) {
        String sql = "";
        int count = getMaxRecord("select max(_Id) from " + DATABASE_TABLE_ALL_FARM_DETAIL) + 1;
        if (count == 0) {
            count = 1;
        }
        try {
            if (flag == "I") {
                sql = "insert into " + DATABASE_TABLE_ALL_FARM_DETAIL + " values ('"
                        + count + "','"
                        + cv.get(FARM_ID) + "','"
                        + cv.get(FARM_NAME) + "','"
                        + cv.get(FARMER_NAME) + "','"
                        + cv.get(FARMER_PHONE) + "','"
                        + cv.get(FARM_AREA) + "','"
                        + cv.get(USER_ID) + "','"
                        + cv.get(CONTOUR) + "','"
                        + cv.get(STATE_ID) + "','"
                        + cv.get(CONCERN) + "','"
                        + cv.get(AREA) + "','"
                        + cv.get(LOG_DATE) + "','"
                        + cv.get(MAX_LAT) + "','"
                        + cv.get(MAX_LON) + "','"
                        + cv.get(MIN_LAT) + "','"
                        + cv.get(MIN_LON) + "','"
                        + cv.get(CENTRE_LAT) + "','"
                        + cv.get(CENTRE_LON) + "','"
                        + cv.get(SENDING_STATUS) + "','"
                        + cv.get(StateName) + "','"
                        + cv.get(DistrictID) + "','"
                        + cv.get(Block) + "','"
                        + cv.get(VillageID) + "','"
                        + cv.get(VillageStr) + "','"
                        + cv.get(ProjectID) + "','"
                        + cv.get(TaggingApp) + "','"
                        + cv.get(FatherName) + "','"
                        + cv.get(AadharNo) + "','"
                        + cv.get(Aadhar_Other) + "','"
                        + cv.get(MobileType) + "','"
                        + cv.get(NoOfBags) + "','"
                        + cv.get(IBCode) + "','"
                        + cv.get(createaccflag) + "')";

            } else if (flag == "U") {
                sql = "update " + DATABASE_TABLE_ALL_FARM_DETAIL + " set " + FARM_NAME + " = '" + cv.get(FARM_NAME) +
                        "'," + FARMER_NAME + " = '" + cv.get(FARMER_NAME) + "'," + FARMER_PHONE + " = '" + cv.get(FARMER_PHONE) +
                        "'," + FARM_AREA + " = '" + cv.get(FARM_AREA) + "'," + CONTOUR + " = '" + cv.get(CONTOUR) +
                        "'," + STATE_ID + " = '" + cv.get(STATE_ID) + "'," + CONCERN + " = '" + cv.get(CONCERN) +
                        "'," + AREA + " = '" + cv.get(AREA) + "'," + LOG_DATE + " = '" + cv.get(LOG_DATE) +
                        "'," + MAX_LAT + " = '" + cv.get(MAX_LAT) + "'," + MAX_LON + " = '" + cv.get(MAX_LON) +
                        "'," + MIN_LAT + " = '" + cv.get(MIN_LAT) + "'," + MIN_LON + " = '" + cv.get(MIN_LON) +
                        "'," + CENTRE_LAT + " = '" + cv.get(CENTRE_LAT) + "'," + CENTRE_LON + " = '" + cv.get(CENTRE_LON) +
                        "'," + SENDING_STATUS + " = '" + cv.get(SENDING_STATUS) + "'," + StateName + " = '" + cv.get(StateName) +
                        "'," + DistrictID + " = '" + cv.get(DistrictID) + "'," + Block + " = '" + cv.get(Block) +
                        "'," + VillageID + " = '" + cv.get(VillageID) + "'," + VillageStr + " = '" + cv.get(VillageStr) +
                        "'," + ProjectID + " = '" + cv.get(ProjectID) + "'," + TaggingApp + " = '" + cv.get(TaggingApp) +
                        "'," + FatherName + " = '" + cv.get(FatherName) + "'," + AadharNo + " = '" + cv.get(AadharNo) +
                        "'," + Aadhar_Other + " = '" + cv.get(Aadhar_Other) + "'," + MobileType + " = '" + cv.get(MobileType) +
                        "'," + NoOfBags + " = '" + cv.get(NoOfBags) + "'," + IBCode + " = '" + cv.get(IBCode) + "'," + createaccflag + " = '" + cv.get(createaccflag) + "' where " +
                        FARM_NAME + " = '" + cv.get(FARM_NAME) + "' and  " + USER_ID + " = '" + cv.get(USER_ID) + "' and  " + FARMER_PHONE + " = '" + cv.get(FARMER_PHONE) + "'";
            }

            db.execSQL(sql);
            return 1;
        } catch (Exception e) {
            // TODO: handle exception
            Log.e("DataProvider",
                    "Error in savechildtrackgridMalaria:: " + e.getMessage());
            return 0;
        }
    }

    public int getMaxRecord(String Sql) {
        int iIntegerValue = 0;
        Cursor cursor = null;
        open();
        try {
            if (db == null) {
                db = DBHelper.getWritableDatabase();
            }
            cursor = db.rawQuery(Sql, null);
            if (cursor != null) {
                cursor.moveToFirst();
                while (cursor.isAfterLast() == false) {
                    iIntegerValue = cursor.getInt(0);
                    cursor.moveToNext();
                }
                cursor.close();
            }
        } catch (Exception exception) {
            iIntegerValue = -1;
            Log.e("DataProvider",
                    "Error in getMaxRecord :: " + exception.getMessage());
        }
        return iIntegerValue;
    }


//    public void AddnewColumnFarmreg() {
//        String sql = "";
//        String FieldNames[] = {StateName, DistrictID, Block, VillageID, VillageStr, ProjectID, TaggingApp, FatherName, AadharNo, Aadhar_Other, MobileType, NoOfBags, createaccflag};
//        open();
//        try {
//            sql = "select StateName from " + DATABASE_TABLE_ALL_FARM_DETAIL;
//            db.execSQL(sql);
//        } catch (Exception ex) {//means filed doesnot exist have to add
//            try {
//                for (int i = 0; i < FieldNames.length; i++) {
//                    sql = "Alter Table " + DATABASE_TABLE_ALL_FARM_DETAIL + " add Column " + FieldNames[i] + " TEXT";
//                    db.execSQL(sql);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            ex.printStackTrace();
//        }
//    }

    public void checktableAllFarmDetail() {
        String sql = "";
        open();
        try {
            String SQL_Count = "Select IBCode from " + DATABASE_TABLE_ALL_FARM_DETAIL + "";
            int count = getMaxRecord(SQL_Count);
            if (count == -1) {
                db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_ALL_FARM_DETAIL);
                db.execSQL(DATABASE_CREAT_ALL_FARM_DETAIL);
            }
//            sql = "select * from " + TABLE_tblPlantdocLocalSMS;
//            db.execSQL(sql);
        } catch (Exception ex) {//means filed doesnot exist have to add
            try {
                db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_ALL_FARM_DETAIL);
                db.execSQL(DATABASE_CREAT_ALL_FARM_DETAIL);
            } catch (Exception e) {
                e.printStackTrace();
            }
            ex.printStackTrace();
        }
    }

    public void checktableImageLocalStorage() {
        String sql = "";
        open();
        try {
            String SQL_Count = "Select count(*) from " + TABLE_ImageLocalStorage + "";
            int count = getMaxRecord(SQL_Count);
            if (count == -1) {
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_ImageLocalStorage);
                db.execSQL(DATABASE_CREATED_ImageLocalStorage);
            }
//            sql = "select * from " + TABLE_tblPlantdocLocalSMS;
//            db.execSQL(sql);
        } catch (Exception ex) {//means filed doesnot exist have to add
            try {
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_ImageLocalStorage);
                db.execSQL(DATABASE_CREATED_ImageLocalStorage);
            } catch (Exception e) {
                e.printStackTrace();
            }
            ex.printStackTrace();
        }
    }

    public void checktableYoutubeVideoDateTime() {
        String sql = "";
        open();
        try {
            sql = "select * from " + TABLE_YoutubeVideoDateTime;
            db.execSQL(sql);
        } catch (Exception ex) {//means filed doesnot exist have to add
            try {
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_YoutubeVideoDateTime);
                db.execSQL(DATABASE_CREATED_YoutubeVideoDateTime);
            } catch (Exception e) {
                e.printStackTrace();
            }
            ex.printStackTrace();
        }
    }


    public void insertImageLocalStorage(String ImageName, Double FarmerLag, Double FarmerLong) {
        try {
            String Latitude = String.valueOf(LatLonCellID.currentLat), Longtitude = String.valueOf(LatLonCellID.currentLon);
            String Distance = String.valueOf(getDistanceLagLong(FarmerLag, FarmerLong));
            String URL = "insert into " + TABLE_ImageLocalStorage + " (ImageName,Current_Lat,Current_Long,Distance) values('" + ImageName + "','" + Latitude + "','" + Longtitude + "','" + Distance + "')";
            db.execSQL(URL);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    //    public void insertYoutubeVideoDateTime(JSONArray jsonArray) {
//        try {
//            String SQL = "select MAX(ID)+1 from tblYOutubeVideoDateTime";
//            int ID = getMaxRecord(SQL);
//            String date = Utility.getdate() + " " + Utility.gettime();
//            String IDs = "", Titles = "", Descriptions = "", DateFroms = "", DateFrom_time = "",
//                    DateTos = "", DateTo_time = "", ProjectIDs = "", VideoIDs = "", VisibleNames = "";
//            if (jsonArray.length() > 0) {
//                for (int i = 0; i < jsonArray.length(); i++) {
//                    try {
//                        JSONObject ob = jsonArray.getJSONObject(i);
//                        IDs = ob.getString("ID");
//                        Titles = ob.getString("Title");
//                        Descriptions = ob.getString("Description");
//                        DateFroms = ob.getString("DateFrom");
//                        DateFroms = DateFroms.replace("T", " ");
//                        DateFroms = getDateFormatChanged(DateFroms, "DDMMYYYY");
//                        String DATETIME[] = null;
//                        if (DateFroms.contains(" ")) {
//                            DATETIME = DateFroms.split(" ");
//                            DateFroms = DATETIME[0];
//                            DateFrom_time = DATETIME[1];
//                        }
//                        DateTos = ob.getString("DateTo");
//                        DateTos = DateTos.replace("T", " ");
//                        DateTos = getDateFormatChanged(DateTos, "DDMMYYYY");
//                        if (DateTos.contains(" ")) {
//                            DATETIME = DateTos.split(" ");
//                            DateTos = DATETIME[0];
//                            DateTo_time = DATETIME[1];
//                        }
//                        ProjectIDs = ob.getString("ProjectID");
//                        VideoIDs = ob.getString("VideoID");
//                        VisibleNames = ob.getString("VisibleName");
//
//                        String SQL_count = "select count(*) from tblYOutubeVideoDateTime where DateFrom='" + DateFroms + "' and DateFromTime='" + DateFrom_time + "'";
//                        int count = getMaxRecord(SQL_count);
//                        if (count == 0) {
//                            String URL = "insert into " + TABLE_YoutubeVideoDateTime + " (Title,Description,DateFrom,DateFromTime,DateTo,DateToTime,ProjectID,VideoID,VisibleName) values('" +
//                                    Titles + "','" + Descriptions + "','" + DateFroms + "','" + DateFrom_time + "','" + DateTos + "','" + DateTo_time + "','" + ProjectIDs + "','" + VideoIDs + "','" + VisibleNames + "')";
//                            db.execSQL(URL);
//                        }
//                    } catch (JSONException ex) {
//                        ex.printStackTrace();
//                    }
//                }
//            }
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }
    public void checktabletblPlantdocLocalSMS() {
        String sql = "";
        open();
        try {
            String SQL_Count = "Select count(*) from " + TABLE_tblPlantdocLocalSMS + "";
            int count = getMaxRecord(SQL_Count);
            if (count == -1) {
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_tblPlantdocLocalSMS);
                db.execSQL(DATABASE_CREATED_PlantdocLocalSMS);
            }
//            sql = "select * from " + TABLE_tblPlantdocLocalSMS;
//            db.execSQL(sql);
        } catch (Exception ex) {//means filed doesnot exist have to add
            try {
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_tblPlantdocLocalSMS);
                db.execSQL(DATABASE_CREATED_PlantdocLocalSMS);
            } catch (Exception e) {
                e.printStackTrace();
            }
            ex.printStackTrace();
        }
    }

    public void saveLocalPlatdoc(String ImageNam1, String ImageNam2, String VoiceRecName, String Jsonvalue) {
        try {
            String SQL_Count = "Select count(*) from " + TABLE_tblPlantdocLocalSMS + " where ImageName1='" + ImageNam1 + "' or ImageName2='" + ImageNam2 + "'";
            int count = getMaxRecord(SQL_Count);
            String SQL = "";
            if (count == 0) {
                SQL = "insert into " + TABLE_tblPlantdocLocalSMS + " (ImageName1,ImageName2,VoiceRecName,JsonFile) values ('" + ImageNam1 + "','" + ImageNam2 + "','" + VoiceRecName + "','" + Jsonvalue + "')";
                db.execSQL(SQL);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void checktabletblEventLog() {
        String sql = "";
        open();
        try {
            String SQL_Count = "Select count(*) from " + TABLE_tblEventLogError + "";
            int count = getMaxRecord(SQL_Count);
            if (count == -1) {
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_tblEventLogError);
                db.execSQL(DATABASE_CREATED_EventLogError);
            }
        } catch (Exception ex) {//means filed doesnot exist have to add
            try {
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_tblEventLogError);
                db.execSQL(DATABASE_CREATED_EventLogError);
            } catch (Exception e) {
                e.printStackTrace();
            }
            ex.printStackTrace();
        }
    }

    public void checkLocalTranslation() {
        String sql = "";
        open();
        try {
            String SQL_Count = "Select count(*) from " + tblLocalTranslation + "";
            int count = getMaxRecord(SQL_Count);
            if (count == -1) {
                db.execSQL("DROP TABLE IF EXISTS " + tblLocalTranslation);
                db.execSQL(DATABASE_CREATED_LocalTranslation);
            }
        } catch (Exception ex) {//means filed doesnot exist have to add
            try {
                db.execSQL("DROP TABLE IF EXISTS " + tblLocalTranslation);
                db.execSQL(DATABASE_CREATED_LocalTranslation);
            } catch (Exception e) {
                e.printStackTrace();
            }
            ex.printStackTrace();
        }
    }

    public static final String TABLE_CART = "table_cart";
    public static final String ProductDescription = "productdescription";
    public static final String ImagePath = "imagepath";
    public static final String BrandName = "brandname";
    public static final String ProductUnit = "ProductUnit";
    public static final String quantityUnit = "quantityUnit";
    public static final String serviceID = "serviceID";
    public static final String startDate = "startDate";
    public static final String endDate = "endDate";
    public static final String price = "price";
    public static final String service = "service";
    public static final String quantity = "quantity";

    public static final String CREATE_CART = "create table " + TABLE_CART + " (" + ID + " integer primary key autoincrement,"

            + ProductDescription + " text,"
            + ImagePath + " text,"
            + BrandName + " text,"
            + ProductUnit + " text,"
            + quantityUnit + " text,"
            + serviceID + " text not null,"
            + startDate + " text,"
            + endDate + " text,"
            + price + " text not null,"
            + service + " text not null,"
            + quantity + " text not null);";

    public Cursor getAllCartData() {
        return db.query(true, TABLE_CART, null, null, null, null, null, null, null);
    }

    public Cursor getCartByServiceId(String service_id) {
        return db.query(true, TABLE_CART, null, serviceID + " = '" + service_id + "'", null, null, null, null, null);
    }

    public void updateCartByServiceId(String service_id, String qty) {
        Cursor cursor = db.query(true, TABLE_CART, null, serviceID + " = '" + service_id + "'", null, null, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            ContentValues initialValues = new ContentValues();
            initialValues.put(quantity, qty);

            db.update(TABLE_CART, initialValues, serviceID + " = '" + service_id + "'", null);

        }
        cursor.close();

    }

    public long deleteCartItemByServiceId(String sId) {
        try {
            return db.delete(TABLE_CART, serviceID + " = '" + sId + "'", null);

        } catch (Exception e) {

            e.printStackTrace();
            return -1;
        }
    }

    public long clearCartItem() {
        try {
            return db.delete(TABLE_CART, null, null);

        } catch (Exception e) {

            e.printStackTrace();
            return -1;
        }
    }

    public void checktabletblCart() {
        String sql = "";
        open();
        try {
            String SQL_Count = "Select count(*) from " + TABLE_CART + "";
            int count = getMaxRecord(SQL_Count);
            if (count == -1) {
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
                db.execSQL(CREATE_CART);
            }
//            sql = "select * from " + TABLE_tblPlantdocLocalSMS;
//            db.execSQL(sql);
        } catch (Exception ex) {//means filed doesnot exist have to add
            try {
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
                db.execSQL(CREATE_CART);
            } catch (Exception e) {
                e.printStackTrace();
            }
            ex.printStackTrace();
        }
    }

    public long insertLocalTranslation(JSONArray localtranslation) {

        long l = -1;
        try {
            if (localtranslation.length() > 0) {
                db.delete(tblLocalTranslation, null, null);
                for (int i = 0; i < localtranslation.length(); i++) {
                    try {
                        JSONObject obj = new JSONObject(localtranslation.get(i).toString());
                        ContentValues initialValues = new ContentValues();
                        initialValues.put(ID_Local, obj.getInt("ID"));
                        initialValues.put(MyKey, obj.getString("MyKey"));
                        initialValues.put(English, obj.getString("English"));
                        initialValues.put(Telugu, obj.getString("Telugu"));
                        initialValues.put(Tamil, obj.getString("Tamil"));
                        initialValues.put(Marathi, obj.getString("Marathi"));
                        initialValues.put(Hindi, obj.getString("Hindi"));
                        initialValues.put(Gujarati, obj.getString("Gujarati"));
                        initialValues.put(Bengali, obj.getString("Bengali"));
                        initialValues.put(Category, obj.getString("Category"));
                        initialValues.put(DataType, obj.getString("DataType"));
                        l = db.insert(tblLocalTranslation, null, initialValues);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return l;
    }

}
