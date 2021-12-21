package com.weather.risk.mfi.myfarminfo.services;

import android.app.AlertDialog;
import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.database.DBAdapter;
import com.weather.risk.mfi.myfarminfo.entities.CropQueryData;
import com.weather.risk.mfi.myfarminfo.entities.FarmInformationData;
import com.weather.risk.mfi.myfarminfo.home.ExternalStorageGPS;
import com.weather.risk.mfi.myfarminfo.logging.LocalFileWriter;
import com.weather.risk.mfi.myfarminfo.mapfragments.LatLonCellID;
import com.weather.risk.mfi.myfarminfo.utils.AppConstant;
import com.weather.risk.mfi.myfarminfo.utils.AppManager;
import com.weather.risk.mfi.myfarminfo.utils.CustomHttpClient;
import com.weather.risk.mfi.myfarminfo.home.AppController;
import com.weather.risk.mfi.myfarminfo.utils.Utility;
import com.weather.risk.mfi.myfarminfo.volley_class.CustomJSONObjectRequest;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import static com.weather.risk.mfi.myfarminfo.customcamera.CameraSurfaceView.IMAGE_DIRECTORY;
import static com.weather.risk.mfi.myfarminfo.database.DBAdapter.DATABASE_CREATED_SCREENTRACKING;
import static com.weather.risk.mfi.myfarminfo.database.DBAdapter.DATABASE_TABLE_ALL_FARM_DETAIL;
import static com.weather.risk.mfi.myfarminfo.database.DBAdapter.TABLE_ImageLocalStorage;
import static com.weather.risk.mfi.myfarminfo.database.DBAdapter.TABLE_SCREENTRACKING;
import static com.weather.risk.mfi.myfarminfo.database.DBAdapter.TABLE_tblEventLogError;
import static com.weather.risk.mfi.myfarminfo.database.DBAdapter.TABLE_tblPlantdocLocalSMS;
import static com.weather.risk.mfi.myfarminfo.logging.LocalFileWriter.SaveLocalFile;
import static com.weather.risk.mfi.myfarminfo.logging.LocalFileWriter.getBase64FromPath;
import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.SN_NewHomeScreen;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getAPIimeResponseinSecond;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getAppVersion;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getdateYYYYMMDD;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.gettime;

public class AuthenticateService extends IntentService {

    public static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final String AUTHENTICATION_METHOD = "imei_authentication";


    public AuthenticateService() {
        super("AuthenticateService");

    }

//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        // should return START_STICKY
//        return START_STICKY;
//
//    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(getClass().getSimpleName(), "My farm info ran!");

        String result = "";
        DBAdapter db = new DBAdapter(getApplicationContext());
        db.open();

        //testing the Service
//        try {
//            String textVBody = "";
//            textVBody = "Apps : " + getdateYYYYMMDD() + " " + gettime();
//
////            String mFilePath = Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY + "/" + mFileName + ".pdf";
//            File root = new File(Environment.getExternalStorageDirectory(), IMAGE_DIRECTORY);
//            if (!root.exists()) {
//                root.mkdirs();
//            }
//            String sFileName = "SecuFarmSurveyor_ErrorFile" + getdateYYYYMMDD() + " " + gettime();
//            File gpxfile = new File(root, sFileName);
//            FileWriter writer = new FileWriter(gpxfile, true);
//            writer.append(textVBody + "\n\n");
//            writer.flush();
//            writer.close();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
        //Screen Tracking Data Uploaded
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
//                new Utility.getFarmerDeatials(object, db, context).execute();

                long mRequestStartTime = System.currentTimeMillis();
                String ScreenTracking_URL = AppManager.getInstance().UploadAppsScreenTracking_save_AppScreens;
                String response = null;
                int seconds = 0;
                try {
                    String PassJSON = object.toString();
                    response = AppManager.getInstance().httpRequestPutMethodReport(ScreenTracking_URL, PassJSON);

                    JSONObject obj = null;
                    try {
                        // calculate the duration in milliseconds
                        seconds = getAPIimeResponseinSecond(mRequestStartTime);
                        if (seconds > 3) {
                            SaveLocalFile(db, AuthenticateService.this, SN_NewHomeScreen, ScreenTracking_URL, response, "", "" + seconds, "", "Working");
                        }
                        if (response != null && !response.equalsIgnoreCase("Could not connect to server"))
                            obj = new JSONObject(response);
                        try {
                            if (response != null && response.length() > 0) {
                                String ResponseValue = obj.getString("save_AppScreensResult");
                                if (ResponseValue.equalsIgnoreCase("Success")) {
                                    if (db != null) {
                                        String deletesql = "delete from " + db.TABLE_SCREENTRACKING;
                                        db.getSQLiteDatabase().execSQL(deletesql);
                                    }
                                }
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            SaveLocalFile(db, AuthenticateService.this, SN_NewHomeScreen, ScreenTracking_URL, response, "JSON Response Error", "" + seconds, "", "Error");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
//                        Toast.makeText(AuthenticateService.this, getResources().getString(R.string.ResponseFormattingError), Toast.LENGTH_LONG).show();
                        SaveLocalFile(db, AuthenticateService.this, SN_NewHomeScreen, ScreenTracking_URL, response, "JSON Response Error", "" + seconds, "", "Error");
                    }

                } catch (Exception e) {//android.os.NetworkOnMainThreadException
                    e.printStackTrace();
                    SaveLocalFile(db, AuthenticateService.this, SN_NewHomeScreen, ScreenTracking_URL, response, "JSON Response Error", "" + seconds, "", "Error");
                    //occurs whenever you try to make long running tasks/process on Main UI Thread directly.
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        //Upload Eventload file
        try {
            String sql = "Select count(*) from " + TABLE_tblEventLogError + "";
            int count = db.getMaxRecord(sql);
            if (count > 0) {
                //Uploading vent Log Error
                getUploadLocalFile(db, this);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        //Plant DOctor Image Upload
        try {
            String SQL_Count = "Select count(*) from " + TABLE_tblPlantdocLocalSMS + "";
            int count = db.getMaxRecord(SQL_Count);
            if (count > 0) {
                ArrayList<HashMap<String, String>> PlantDoc = new ArrayList<>();
                String sql = "Select * from " + TABLE_tblPlantdocLocalSMS + "";
                PlantDoc = db.getDynamicTableValue(sql);
                int flag = 0;
                String ImageName1s, ImageName2s, UploadedImageNamCloseups = "", UploadedImageNamFarms = "", VoiceRecName = "", JsonFile = "";

                Double lat = LatLonCellID.lat;
                Double lon = LatLonCellID.lon;
                String Latitude1 = "", Longitude1 = "", Latitude2 = "", Longitude2 = "";
                for (int i = 0; i < PlantDoc.size(); i++) {
                    ImageName1s = PlantDoc.get(i).get("ImageName1");
                    ImageName2s = PlantDoc.get(i).get("ImageName2");
                    if (ImageName1s != null && ImageName1s.length() > 2) {
                        String SQL = "Select * from " + TABLE_ImageLocalStorage + " where ImageName='" + ImageName1s + "'";
                        ArrayList<HashMap<String, String>> imagelaglon = new ArrayList<>();
                        imagelaglon = db.getDynamicTableValue(SQL);
                        if (imagelaglon != null && imagelaglon.size() > 0) {
                            Latitude1 = imagelaglon.get(i).get("Current_Lat");
                            Longitude1 = imagelaglon.get(i).get("Current_Long");
                        } else {
                            Latitude1 = String.valueOf(lat);
                            Longitude1 = String.valueOf(lon);
                        }
                        //Upload Image
                        UploadedImageNamCloseups = uploadImage(ImageName1s, 1);
                    }
                    if (ImageName2s != null && ImageName2s.length() > 2) {
                        //Upload Image
                        String SQL = "Select * from " + TABLE_ImageLocalStorage + " where ImageName='" + ImageName2s + "'";
                        ArrayList<HashMap<String, String>> imagelaglon = new ArrayList<>();
                        imagelaglon = db.getDynamicTableValue(SQL);
                        if (imagelaglon != null && imagelaglon.size() > 0) {
                            Latitude2 = imagelaglon.get(i).get("Current_Lat");
                            Longitude2 = imagelaglon.get(i).get("Current_Long");
                        } else {
                            Latitude2 = String.valueOf(lat);
                            Longitude2 = String.valueOf(lon);
                        }
                        //Upload Image
                        UploadedImageNamFarms = uploadImage(ImageName2s, 2);
                    }
                    String VoiceRecordName = PlantDoc.get(i).get("VoiceRecName");
                    if (VoiceRecordName != null && !VoiceRecordName.equalsIgnoreCase("null") && VoiceRecordName.length() > 2) {
                        VoiceRecName = uploadVoiceRecord(VoiceRecordName);
                    }
//                    if ((UploadedImageNamCloseups != null && UploadedImageNamCloseups.length() > 2) ||
//                            (UploadedImageNamFarms != null && UploadedImageNamFarms.length() > 2)) {
//                        String SQL = "update " + TABLE_tblPlantdocLocalSMS + " set UploadedImageNamCloseup='" + UploadedImageNamCloseups + "',UploadedImageNamFarm='" + UploadedImageNamFarms + "',VoiceRecName='" + VoiceRecName + "' where ImageName1='" + ImageName1s + "' and ImageName2='" + ImageName2s + "'";
//                        db.getSQLiteDatabase().execSQL(SQL);
//                    }

                    JSONArray imageList = new JSONArray();
                    if (UploadedImageNamCloseups != null && UploadedImageNamCloseups.length() > 2) {
                        imageList = Utility.addImageName(UploadedImageNamCloseups, imageList, 1, Latitude1, Longitude1);
                    }
                    if (UploadedImageNamFarms != null && UploadedImageNamFarms.length() > 2) {
                        imageList = Utility.addImageName(UploadedImageNamFarms, imageList, 2, Latitude2, Longitude2);
                    }

                    JsonFile = PlantDoc.get(i).get("JsonFile");
                    JSONObject jsonObject = new JSONObject(JsonFile);
                    jsonObject.put("filename", imageList.toString());
                    jsonObject.put("VoiceFile", VoiceRecName);
                    String JSON = jsonObject.toString();
                    JSON = JSON.replace("\\\\\\\\\\\\\\", "\\\\\\");
                    jsonObject = new JSONObject();
                    jsonObject = new JSONObject(JSON);

                    String path = AppManager.getInstance().createNewLogURL;
                    try {
                        String getResponse = CustomHttpClient.executeHttpPut(path, jsonObject.toString());
                        JSONObject obj = new JSONObject(getResponse);
                        String response = obj.getString("logRequestResult");
                        if (response != null && response.length() > 2 && response.equalsIgnoreCase("Added")) {
                            String SQL = "delete from " + TABLE_tblPlantdocLocalSMS + " where ImageName1='" + ImageName1s + "' and ImageName2='" + ImageName2s + "'";
                            db.getSQLiteDatabase().execSQL(SQL);
                        }

                    } catch (Exception e) {//android.os.NetworkOnMainThreadException
                        e.printStackTrace();//occurs whenever you try to make long running tasks/process on Main UI Thread directly.
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        Cursor getSavedCredentials = db.getSavedCredentials();
        if (getSavedCredentials.getCount() > 0) {
            getSavedCredentials.moveToFirst();
            do {
                String id = getSavedCredentials.getString(getSavedCredentials.getColumnIndex(DBAdapter.ID));
                String mailId = getSavedCredentials.getString(getSavedCredentials.getColumnIndex(DBAdapter.EMAIL_ADDRESS));
                String mobileNo = getSavedCredentials.getString(getSavedCredentials.getColumnIndex(DBAdapter.MOBILE_NO));
                String userName = getSavedCredentials.getString(getSavedCredentials.getColumnIndex(DBAdapter.USER_NAME));
                String password = getSavedCredentials.getString(getSavedCredentials.getColumnIndex(DBAdapter.PASSWORD));
                String visibleName = getSavedCredentials.getString(getSavedCredentials.getColumnIndex(DBAdapter.VISIBLE_NAME));
                String createdDateTime = getSavedCredentials.getString(getSavedCredentials.getColumnIndex(DBAdapter.CREATED_DATE_TIME));
                String oldUserId = getSavedCredentials.getString(getSavedCredentials.getColumnIndex(DBAdapter.USER_ID));
                System.out.println("gotUser id : " + oldUserId);
                String completeStringForRegister = mailId + "/" + userName + "/" + password + "/" + visibleName + "/" + mobileNo;
                String response;

                try {
                    ExternalStorageGPS.write_file("MFI_RESPONSE_LOG", true, format.format(cal().getTime()) + "---USER REGISTER STRING : " + "https://myfarminfo.com/yfirest.svc/Register/" + completeStringForRegister + " \n\r");
                    response = CustomHttpClient.executeHttpPut("https://myfarminfo.com/yfirest.svc/Register/" + completeStringForRegister);
                    ExternalStorageGPS.write_file("MFI_RESPONSE_LOG", true, format.format(cal().getTime()) + "---USER REGISTER RESPONSE : " + response + " \n\r");
                    System.out.println("response " + response);
                    Log.d("RegistrationData", response);
                    if (response.contains("Error")) {
                        String s = "Error";
                        ContentValues values = new ContentValues();
                        values.put(DBAdapter.ID, id);
                        values.put(DBAdapter.USER_NAME, userName);
                        values.put(DBAdapter.VISIBLE_NAME, visibleName);
                        values.put(DBAdapter.PASSWORD, password);
                        values.put(DBAdapter.EMAIL_ADDRESS, mailId);
                        values.put(DBAdapter.MOBILE_NO, mobileNo);
                        values.put(DBAdapter.USER_ID, oldUserId);
                        values.put(DBAdapter.CREATED_DATE_TIME, createdDateTime);
                        values.put(DBAdapter.SENDING_STATUS, DBAdapter.SENT);

                        db.db.update(DBAdapter.TABLE_CREDENTIAL, values, DBAdapter.ID + " = '" + id + "'", null);
                        continue;
                    }
                    response = response.trim();
                    response = response.substring(1, response.length() - 1);
                    response = response.replace("\\", "");
                    JSONArray jArray = new JSONArray(response);
                    Log.d("afterfilterResponse", response);

                    if (jArray.length() == 0) {
                        String message = "No Registered";
                    }

                    for (int i = 0; i < jArray.length(); i++) {

                        JSONObject jObject = jArray.getJSONObject(i);
                        String userId = jObject.getString("UserID");
                        String visibleName1 = jObject.getString("VisibleName");

                        ContentValues values = new ContentValues();
                        values.put(DBAdapter.ID, id);
                        values.put(DBAdapter.USER_NAME, userName);
                        values.put(DBAdapter.VISIBLE_NAME, visibleName1);
                        values.put(DBAdapter.PASSWORD, password);
                        values.put(DBAdapter.EMAIL_ADDRESS, mailId);
                        values.put(DBAdapter.EMAIL_ADDRESS, mobileNo);
                        values.put(DBAdapter.USER_ID, userId);
                        values.put(DBAdapter.CREATED_DATE_TIME, createdDateTime);
                        values.put(DBAdapter.NEED_TO_EDIT, DBAdapter.FALSE);
                        values.put(DBAdapter.SENDING_STATUS, DBAdapter.SENT);

                        db.db.update(DBAdapter.TABLE_CREDENTIAL, values, DBAdapter.ID + " = '" + id + "'", null);
                        boolean isUpdated = db.updateFormByUserId(oldUserId, userId);
                        System.out.println("Corresponding farm is updated : " + isUpdated);

                        if (AppConstant.user_id != null) {
                            if (oldUserId.equals(AppConstant.user_id)) {
                                AppConstant.user_id = userId;
                            }
                        }

                        String message = "OK";
                    }
                } catch (Exception e) {
//
                    e.printStackTrace();
                    Log.d("Status", "" + e);
                }

            } while (getSavedCredentials.moveToNext());
        }
        getSavedCredentials.close();

        /*Cursor getAllForm = db.getAllForm();
        if(getAllForm.getCount()>0){
            getAllForm.moveToFirst();
            System.out.println("Name : "+getAllForm.getString(getAllForm.getColumnIndex(DBAdapter.FARM_NAME))+" Sending Status : "+getAllForm.getString(getAllForm.getColumnIndex(DBAdapter.SENDING_STATUS)));
        }*/

//        int count = db.getMaxRecord("select count(*) from " + DATABASE_TABLE_ALL_FARM_DETAIL + " where sending_status='save' ");
        Cursor getSavedFarm = db.getSavedForm();
        if (getSavedFarm.getCount() > 0) {
            getSavedFarm.moveToFirst();
            do {
                FarmInformationData data = new FarmInformationData();

                try {
                    String id = getSavedFarm.getString(getSavedFarm.getColumnIndex(DBAdapter.ID));
                    String farmId = getSavedFarm.getString(getSavedFarm.getColumnIndex(DBAdapter.FARM_ID));
                    String farmName = getSavedFarm.getString(getSavedFarm.getColumnIndex(DBAdapter.FARM_NAME));
                    String farmerName = getSavedFarm.getString(getSavedFarm.getColumnIndex(DBAdapter.FARMER_NAME));
                    String farmerPhone = getSavedFarm.getString(getSavedFarm.getColumnIndex(DBAdapter.FARMER_PHONE));
                    String farmArea = getSavedFarm.getString(getSavedFarm.getColumnIndex(DBAdapter.FARM_AREA));
                    String userId = getSavedFarm.getString(getSavedFarm.getColumnIndex(DBAdapter.USER_ID));
                    String contour = getSavedFarm.getString(getSavedFarm.getColumnIndex(DBAdapter.CONTOUR));
                    String state = getSavedFarm.getString(getSavedFarm.getColumnIndex(DBAdapter.STATE_ID));
                    String concern = getSavedFarm.getString(getSavedFarm.getColumnIndex(DBAdapter.CONCERN));
                    String area = getSavedFarm.getString(getSavedFarm.getColumnIndex(DBAdapter.AREA));
                    String logDate = getSavedFarm.getString(getSavedFarm.getColumnIndex(DBAdapter.LOG_DATE));
                    String maxLat = getSavedFarm.getString(getSavedFarm.getColumnIndex(DBAdapter.MAX_LAT));
                    String maxLng = getSavedFarm.getString(getSavedFarm.getColumnIndex(DBAdapter.MAX_LON));
                    String minLat = getSavedFarm.getString(getSavedFarm.getColumnIndex(DBAdapter.MIN_LAT));
                    String minLng = getSavedFarm.getString(getSavedFarm.getColumnIndex(DBAdapter.MIN_LON));
                    String centerLat = getSavedFarm.getString(getSavedFarm.getColumnIndex(DBAdapter.CENTRE_LAT));
                    String centerLng = getSavedFarm.getString(getSavedFarm.getColumnIndex(DBAdapter.CENTRE_LON));

                    //Add new fields
                    String StateName = getSavedFarm.getString(getSavedFarm.getColumnIndex(DBAdapter.StateName));
                    String DistrictID = getSavedFarm.getString(getSavedFarm.getColumnIndex(DBAdapter.DistrictID));
                    String Block = getSavedFarm.getString(getSavedFarm.getColumnIndex(DBAdapter.Block));
                    String VillageID = getSavedFarm.getString(getSavedFarm.getColumnIndex(DBAdapter.VillageID));
                    String VillageStr = getSavedFarm.getString(getSavedFarm.getColumnIndex(DBAdapter.VillageStr));
                    String ProjectID = getSavedFarm.getString(getSavedFarm.getColumnIndex(DBAdapter.ProjectID));
                    String TaggingApp = getSavedFarm.getString(getSavedFarm.getColumnIndex(DBAdapter.TaggingApp));
                    String FatherName = getSavedFarm.getString(getSavedFarm.getColumnIndex(DBAdapter.FatherName));
                    String AadharNo = getSavedFarm.getString(getSavedFarm.getColumnIndex(DBAdapter.AadharNo));
                    String Aadhar_Other = getSavedFarm.getString(getSavedFarm.getColumnIndex(DBAdapter.Aadhar_Other));
                    String MobileType = getSavedFarm.getString(getSavedFarm.getColumnIndex(DBAdapter.MobileType));
                    String NoOfBags = getSavedFarm.getString(getSavedFarm.getColumnIndex(DBAdapter.NoOfBags));
                    String createaccflag = getSavedFarm.getString(getSavedFarm.getColumnIndex(DBAdapter.createaccflag));


                    data.setFarmId(farmId);
                    data.setFarmName(farmName);
                    data.setFarmerName(farmerName);
                    data.setFarmerNumber(farmerPhone);
                    data.setActualFarmArea(farmArea);
                    data.setUserID(userId);
                    data.setAllLatLngPoint(contour);
                    data.setState(state);
                    data.setYourCencern(concern);
                    data.setArea(area);

                    //Add new Fields
                    data.setStateName(StateName);
                    data.setDistrictID(DistrictID);
                    data.setBlock(Block);
                    data.setVillageID(VillageID);
                    data.setVillageStr(VillageStr);
                    data.setProjectID(ProjectID);
                    data.setTaggingApp(TaggingApp);
                    data.setFatherName(FatherName);
                    data.setAadharNo(AadharNo);
                    data.setAadhar_Other(Aadhar_Other);
                    data.setMobileType(MobileType);
                    data.setNoOfBags(NoOfBags);
                    data.setCreateaccflag(createaccflag);


                    ArrayList<CropQueryData> cropQueryDataArray = new ArrayList<>();

                    Cursor cropDetail = db.getCropQueryByFarmId(farmId);
                    System.out.println(("CropDeatil count : " + cropDetail.getCount()));
                    if (cropDetail.getCount() > 0) {
                        cropDetail.moveToFirst();
                        do {
                            CropQueryData cropData = new CropQueryData(cropDetail);
                            cropQueryDataArray.add(cropData);

                        } while (cropDetail.moveToNext());
                    }
                    String parameterString = createJsonParameterForSaveForm(data, cropQueryDataArray, db);
                    String createdString = AppManager.getInstance().removeSpaceForUrl(parameterString);

                    String sendPath = AppManager.getInstance().saveFarmInfo;
                    System.out.println("Save URL : " + sendPath);

                    ExternalStorageGPS.write_file("MFI_RESPONSE_LOG", true, format.format(cal().getTime()) + "---FARM STRING : " + createdString + " \n\r");

                    String response = AppManager.getInstance().httpRequestPutMethod(sendPath, createdString);
                    ExternalStorageGPS.write_file("MFI_RESPONSE_LOG", true, format.format(cal().getTime()) + "---FARM RESPONSE : " + response + " \n\r");
                    System.out.println("Save Response :---" + response);
                    if (response != null) {
                        if (response.contains("Success") || response.contains("Error:")) {
                            if (cropDetail.getCount() > 0) {
                                cropDetail.moveToFirst();
                                do {
                                    String cropQueryColumnId = cropDetail.getString(cropDetail.getColumnIndex(DBAdapter.ID));
                                    CropQueryData cropData = new CropQueryData(cropDetail);
                                    boolean isUpdated = cropData.updateStatusToSent(db, cropQueryColumnId);
                                    System.out.println("Is Crop Detail Updated : " + isUpdated);
                                } while (cropDetail.moveToNext());
                            }

                            ContentValues values = new ContentValues();
                            values.put(DBAdapter.ID, id);
                            values.put(DBAdapter.FARM_ID, farmId);
                            values.put(DBAdapter.FARM_NAME, farmName);
                            values.put(DBAdapter.USER_ID, userId);
                            values.put(DBAdapter.CONTOUR, contour);
                            values.put(DBAdapter.STATE_ID, state);
                            values.put(DBAdapter.CONCERN, concern);
                            values.put(DBAdapter.AREA, area);
                            values.put(DBAdapter.LOG_DATE, logDate);
                            values.put(DBAdapter.MAX_LAT, maxLat);
                            values.put(DBAdapter.MAX_LON, maxLng);
                            values.put(DBAdapter.MIN_LAT, minLat);
                            values.put(DBAdapter.MIN_LON, minLng);
                            values.put(DBAdapter.CENTRE_LAT, centerLat);
                            values.put(DBAdapter.CENTRE_LON, centerLng);
                            values.put(DBAdapter.SENDING_STATUS, DBAdapter.SENT);

                            long j = db.db.update(DATABASE_TABLE_ALL_FARM_DETAIL, values, DBAdapter.ID + " = '" + id + "'", null);

                            if (j != -1) {
                                System.out.println("Farm detail updated : " + j);
                            }
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            } while (getSavedFarm.moveToNext());
        }


    }

    private String createJsonParameterForSaveForm(FarmInformationData farmData, ArrayList<CropQueryData> cropQueryData, DBAdapter db) {
        String parameterString = "";
        JSONArray cropInfo = new JSONArray();

        for (CropQueryData data : cropQueryData) {
            try {
                if (data.getCropID().equals("-1")) {
                    continue;
                } else {
                    Cursor cursor = db.getCropNameById(data.getCropID());
                    if (cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        data.setCrop(cursor.getString(cursor.getColumnIndex(DBAdapter.CROP)));
                    }
                    cursor.close();
                }
                JSONObject cropInfoJsonObject = new JSONObject();
                cropInfoJsonObject.put("CropID", data.getCropID());
                cropInfoJsonObject.put("CropName", data.getCrop());
                cropInfoJsonObject.put("Variety", data.getVariety());
                cropInfoJsonObject.put("N", data.getBasalDoseN());
                cropInfoJsonObject.put("P", data.getBasalDoseP());
                cropInfoJsonObject.put("K", data.getBasalDoseK());
                cropInfoJsonObject.put("BasalDoseApply", data.getBesalDoseApply());
                cropInfoJsonObject.put("SowDate", data.getSowPeriodForm());
                cropInfoJsonObject.put("CropFrom", data.getSowPeriodForm());
                cropInfoJsonObject.put("CropTo", data.getSowPeriodTo());
                cropInfoJsonObject.put("OtherNutrient", data.getOtherNutrition());
                cropInfo.put(cropInfoJsonObject);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        JSONObject finalJson = new JSONObject();
        JSONObject jsonObject = new JSONObject();
        try {
//                jsonObject.put("UserID", farmData.getUserID());
            String farmId = farmData.getFarmId();
            if (farmId.length() < 5) {
                jsonObject.put("FarmID", farmId);
            } else {
                jsonObject.put("FarmID", "0");
            }
            jsonObject.put("FarmName", farmData.getFarmName());
            jsonObject.put("FarmerName", farmData.getFarmerName());
            jsonObject.put("PhoneNo", farmData.getFarmerNumber());
            jsonObject.put("FarmArea", farmData.getActualFarmArea());
            jsonObject.put("Contour", farmData.getAllLatLngPoint());
            jsonObject.put("CropID", farmData.getCropID());
            jsonObject.put("State", farmData.getState());
            jsonObject.put("CropInfo", cropInfo);
            jsonObject.put("Concerns", farmData.getYourCencern());
            jsonObject.put("Concerns", farmData.getYourCencern());
            jsonObject.put("Area", farmData.getArea());

            //Add new Fields

            jsonObject.put("StateName", farmData.getStateName());
            jsonObject.put("District", farmData.getDistrictID());
            jsonObject.put("Block", farmData.getBlock());
            jsonObject.put("VillageStr", farmData.getVillageStr());
            jsonObject.put("VillageID", farmData.getVillageID());
            jsonObject.put("ProjectID", farmData.getProjectID());
            jsonObject.put("TaggingApp", "MFI");
            jsonObject.put("FatherName", farmData.getFatherName());
            jsonObject.put("AadharNo", farmData.getAadharNo());
            jsonObject.put("Other", farmData.getAadhar_Other());
            jsonObject.put("MobileType", farmData.getMobileType());
            jsonObject.put("NoOfBags", farmData.getNoOfBags());

            finalJson.put("UserID", farmData.getUserID());
            finalJson.put("FarmInfo", jsonObject.toString());
            finalJson.put("createaccflag", farmData.getCreateaccflag());


        } catch (JSONException e) {
            e.printStackTrace();
        }
        //      jsonObject.put("guarderiasIdGuarderias",jsonObject2);
        parameterString = finalJson.toString();
        String replaceString = "\"";
        return parameterString;
    }

    public static Calendar cal() {
        Calendar cal = Calendar.getInstance();
        return cal;
    }

    String value = "";

    public String uploadImage(String Imagevalue, int flag) {
        try {
            JSONObject jsonObject = new JSONObject();
            String usi = AppConstant.user_id;
            String Value = getImage(Imagevalue);
            jsonObject.putOpt("ImageString", Value);
            jsonObject.putOpt("UserID", usi);

            // jsonObject.putOpt("Lat_Lng", lat+","+lon);AppManager.getInstance().httpRequestPutMethod
            String path = AppManager.getInstance().uploadImageURL1;
            try {
                String getResponse = CustomHttpClient.executeHttpPut(path, jsonObject.toString());
                JSONObject obj = new JSONObject(getResponse);
                value = obj.getString("uploadBase64ImageResult");
                Log.d("HTTP", "HTTP: OK");
            } catch (Exception e) {//android.os.NetworkOnMainThreadException
                e.printStackTrace();//occurs whenever you try to make long running tasks/process on Main UI Thread directly.
                Log.e("HTTP", "Error in http connection " + e.toString());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return value;
    }

    //https://stackoverflow.com/questions/32088022/android-get-image-to-bitmap-from-filepath
    public String getImage(String ImageName) {
        String ImageValue = null;
        try {
//            ImageName = "SF_20200902_071355.jpg";
            String Path = Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY + "/" + ImageName;
            File imageFile = new File(Path);
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath(), bmOptions);
            ImageValue = imageToString(bitmap);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ImageValue;
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

    public String uploadVoiceRecord(final String AudioSavePathInDevice) {

        String responsePath = "";
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

            if (result != null) {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.has("UploadVoiceResult")) {
                    responsePath = jsonObject.getString("UploadVoiceResult");
                } else {
                    responsePath = null;
                }
            }

        } catch (Exception e) {
            // show error
        }
        return responsePath;
    }

    public static void getUploadLocalFile(DBAdapter db, Context context) {
        FileOutputStream fos = null;
        try {
            JSONObject obj = new JSONObject();
            JSONArray array = new JSONArray();
            db.open();
            try {
                db.open();
                ArrayList<HashMap<String, String>> hasmap = new ArrayList<>();
                String sql = "Select * from " + TABLE_tblEventLogError;
                hasmap = db.getDynamicTableValue(sql);
                if (hasmap != null && hasmap.size() > 0) {
                    for (int i = 0; i < hasmap.size(); i++) {
                        JSONObject objs = new JSONObject();
                        objs.put("Apps", hasmap.get(0).get("Apps"));
                        objs.put("ScreenActivity", hasmap.get(0).get("ScreenActivity"));
                        objs.put("UserID", hasmap.get(0).get("UserID"));
                        objs.put("Location", hasmap.get(0).get("Location"));
                        objs.put("DateTime", hasmap.get(0).get("DateTime"));
                        objs.put("URL", hasmap.get(0).get("URL"));
                        objs.put("APITakingTime", hasmap.get(0).get("APITakingTime"));
                        objs.put("Json", hasmap.get(0).get("Json"));
                        objs.put("Error", hasmap.get(0).get("Error"));
                        objs.put("FarmID", hasmap.get(0).get("FarmID"));
                        objs.put("Status", hasmap.get(0).get("Status"));
                        objs.put("AppVerion", getAppVersion(context));
                        array.put(objs);
                    }
                    obj.put("EventLogError", array);
                }
            } catch (Exception ex) {
            }
            String sFileName = "SecuFarmSurveyor_ErrorFile";
            File root = new File(Environment.getExternalStorageDirectory(), IMAGE_DIRECTORY);
            if (!root.exists()) {
                root.mkdirs();
            }
            String FilePath = String.valueOf(root) + "/" + sFileName;
            String Filevalue = getBase64FromPath(FilePath);
            obj.put("File", Filevalue);
            String response = null;
            try {
                String sendPath = AppManager.getInstance().UploadEventLogError;
                String PassJSON = obj.toString();
                response = AppManager.getInstance().httpRequestPostMethodReport(sendPath, PassJSON);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            if (response != null && response.contains("Data Inserted Successfully")) {
                try {
                    String SQL = "delete from " + TABLE_tblEventLogError;
                    db.getSQLiteDatabase().execSQL(SQL);
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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
