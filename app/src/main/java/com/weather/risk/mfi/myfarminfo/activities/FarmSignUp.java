package com.weather.risk.mfi.myfarminfo.activities;

import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.FarmRegistration_AadharNo;
import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.FarmRegistration_ProjectID;
import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.SN_LocateYoutFarmFragment;
import static com.weather.risk.mfi.myfarminfo.utils.AppManager.NOGPSDialog;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getDynamicLanguageToast;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getDynamicLanguageValue;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setCustomSearchableSpinner;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setDynamicLanguage;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setFontsStyle;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setFontsStyleTxt;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setScreenTracking;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.database.DBAdapter;
import com.weather.risk.mfi.myfarminfo.databinding.FarmsignupBinding;
import com.weather.risk.mfi.myfarminfo.entities.CropQueryData;
import com.weather.risk.mfi.myfarminfo.entities.FarmInformationData;
import com.weather.risk.mfi.myfarminfo.mapfragments.FarmAdvisoryFragment;
import com.weather.risk.mfi.myfarminfo.mapfragments.LatLonCellID;
import com.weather.risk.mfi.myfarminfo.mapfragments.LocateYoutFarmFragment;
import com.weather.risk.mfi.myfarminfo.utils.AppConstant;
import com.weather.risk.mfi.myfarminfo.utils.AppManager;
import com.weather.risk.mfi.myfarminfo.utils.TransparentProgressDialog;
import com.weather.risk.mfi.myfarminfo.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import io.requery.android.database.sqlite.SQLiteStatement;

public class FarmSignUp extends AppCompatActivity {

    FarmsignupBinding binding;
    DBAdapter db;
    ArrayList<HashMap<String, String>> States = new ArrayList<>();
    ArrayList<HashMap<String, String>> Districts = new ArrayList<>();
    ArrayList<HashMap<String, String>> SubDistricts = new ArrayList<>();
    ArrayList<HashMap<String, String>> Villages = new ArrayList<>();
    Cursor allCrop;
    public static String StateID = null, StateName = null, DistrictID = null, DistrictName = null, SubDistrictID = null, SubDistrictName = null,
            VillageID = null, VillageName = null, ProjectID = null, ProjectName = null;
    String FarmRegistration_Sub_District = null, FarmRegistration_VillageID = null;
    String FarmRegistration_VillageStr = null;

    String cropId = "-1", cropName = "", lat = "", lon = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.farmsignup);
        db = new DBAdapter(this);
        db.open();

        setStateBind();
        if (!AppManager.getInstance().isLocationServicesAvailable(this))
            NOGPSDialog(this);


        binding.imgeStateRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    new getStateProjectRefresh("StateDistrict", AppManager.getInstance().StateDistrictURL).execute();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (isValidate()) {
                        new sentRequestForFarmSave().execute();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (db != null) {
            db = new DBAdapter(FarmSignUp.this);
        }
        try {
            lat = String.valueOf(LatLonCellID.currentLat);
            lon = String.valueOf(LatLonCellID.currentLon);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        setLanguages();
    }


    @Override
    public void onPause() {
        super.onPause();
        if (db != null) {
            db = new DBAdapter(FarmSignUp.this);
        }
        Log.d("OnPause Method", "OnPause Method called");
    }

    @Override
    public void onStop() {
        super.onStop();
        if (db != null) {
            db = new DBAdapter(FarmSignUp.this);
        }
        Log.d("onStop Method", "onStop Method called");
    }

    private class getStateProjectRefresh extends AsyncTask<Void, Void, String> {

        String flag = "";
        String URL = "";
        TransparentProgressDialog progressDialog;

        public getStateProjectRefresh(String flags, String URLs) {
            flag = flags;
            URL = URLs;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            String sms = "";
            switch (flag) {
                case "Project"://State
                    sms = getDynamicLanguageValue(FarmSignUp.this, "LoadingProject", R.string.LoadingProject);
                    break;
                case "StateDistrict"://District
                    sms = getDynamicLanguageValue(FarmSignUp.this, "Loadingstate", R.string.Loadingstate);
                    break;
                case "Crops"://District
                    sms = getDynamicLanguageValue(FarmSignUp.this, "LoadingCrops", R.string.LoadingCrops);
                    break;
                case "AadharList"://Aadhar List base on ProjectID
                    sms = getDynamicLanguageValue(FarmSignUp.this, "loadingAadhar", R.string.loadingAadhar);
                    break;
                case "AadharInformation"://Aadhar List base on ProjectID
                    sms = getDynamicLanguageValue(FarmSignUp.this, "loadingInformation", R.string.loadingInformation);
                    break;
            }
            progressDialog = new TransparentProgressDialog(FarmSignUp.this, sms);
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
                String response = AppManager.getInstance().httpRequestGetMethod(URL);
                return "[" + response + "]";

            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(String response) {
            super.onPreExecute();
            try {
                if (response != null) {
                    switch (flag) {
                        case "Project":
                            response = response.trim();
                            response = response.substring(1, response.length() - 1);
                            response = response.replace("\\", "");

                            db.open();
                            db.getSQLiteDatabase().execSQL("DROP TABLE IF EXISTS " + db.TABLE_Projectlist);
                            db.getSQLiteDatabase().execSQL(db.CREATE_TABLE_Projectlist);

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
                                getDynamicLanguageToast(FarmSignUp.this, "FormattingError", R.string.FormattingError);
                            }
                            SqliteDB.setTransactionSuccessful();
                            SqliteDB.endTransaction();
                            db.getClass();

//                            setProject();
                            break;
                        case "StateDistrict":
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
                                } else {
                                    getDynamicLanguageToast(FarmSignUp.this, "ServerError", R.string.ServerError);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                getDynamicLanguageToast(FarmSignUp.this, "FormattingError", R.string.FormattingError);
                            }
                            setStateBind();
                            break;
                        case "Crops":
                            response = response.trim();
                            response = response.substring(1, response.length() - 1);
                            response = response.replace("\\", "");

                            db.open();
                            db.getSQLiteDatabase().execSQL("DROP TABLE IF EXISTS " + db.TABLE_CROP_VARIETY);
                            db.getSQLiteDatabase().execSQL(db.CREATE_CROP_VATIETY);

                            io.requery.android.database.sqlite.SQLiteDatabase SqliteDB1 = db.getSQLiteDatabase();
                            SqliteDB1.beginTransaction();
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                String query = "INSERT INTO " + DBAdapter.TABLE_CROP_VARIETY + "(" + DBAdapter.STATE_ID + "," + DBAdapter.CROP_ID + "," + DBAdapter.CROP + "," + DBAdapter.VARIETY + ") VALUES (?,?,?,?)";
                                SQLiteStatement stmt = SqliteDB1.compileStatement(query);

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
                                getDynamicLanguageToast(FarmSignUp.this, "FormattingError", R.string.FormattingError);
                            }

                            SqliteDB1.setTransactionSuccessful();
                            SqliteDB1.endTransaction();
                            setCropsBind();
                            break;
                        case "AadharList":
                            response = response.trim();
                            response = response.substring(1, response.length() - 1);
                            response = response.replace("\\", "");
//                            setAadharList(response);
                            break;
                        case "AadharInformation"://set Aadhar INformation
                            response = response.trim();
                            response = response.substring(1, response.length() - 1);
                            response = response.replace("\\", "");
//                            setInformationBaseonAadharNo(response);
                            break;
                    }

                } else
                    getDynamicLanguageToast(FarmSignUp.this, "Nodataavailable", R.string.Nodataavailable);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            progressDialog.dismiss();
        }

    }

    public void setStateBind() {
        db.open();
        States = new ArrayList<>();

        ArrayList<HashMap<String, String>> hasmap = new ArrayList<>();
        hasmap = db.getDynamicTableValue("select ID,StateID,Upper(StateName) as StateName,DistrictID,Upper(DistrictName) as DistrictName from MstStateDistrict where StateID!='' and StateName!='' group by StateID order by StateName");
        ArrayList<String> list = new ArrayList<>();

        HashMap<String, String> hash = new HashMap<>();
        hash.put("StateID", "0");
//        hash.put("StateName", "Select State");
        hash.put("StateName", getDynamicLanguageValue(FarmSignUp.this, "Select", R.string.Select));
        States.add(hash);
//        list.add("Select State");
        list.add(getDynamicLanguageValue(FarmSignUp.this, "Select", R.string.Select));

        int StateposselectedatFarmRegistrtion = 0;
        if (hasmap.size() > 0) {
            for (int i = 1; i <= hasmap.size(); i++) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("StateID", hasmap.get(i - 1).get("StateID"));
                list.add(hasmap.get(i - 1).get("StateName"));
                hashMap.put("StateName", hasmap.get(i - 1).get("StateName"));
                States.add(hashMap);
            }
        }
        ArrayAdapter<String> stateListAdapter = new ArrayAdapter<String>(FarmSignUp.this, R.layout.spinner_layout, list);
        binding.stateList.setAdapter(stateListAdapter);
        setCustomSearchableSpinner(getApplicationContext(), binding.stateList,"SelectState", R.string.SelectState);
//        binding.stateList.setTitle(getDynamicLanguageValue(FarmSignUp.this, "SelectState", R.string.SelectState));

        binding.stateList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position > 0 && States.size() > 0) {
                    try {
                        //State
                        StateID = States.get(position).get("StateID");
                        StateName = States.get(position).get("StateName");

                        setCropsBind();
                        setDitrictBind(States.get(position).get("StateID"));

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else {
                    StateID = null;
                    StateName = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        db.close();

    }

    public void setCropsBind() {
        try {
            db.open();
            allCrop = db.getCropByState(StateID);
            final int allCropCount = allCrop.getCount();
            String[] cropStringArray = new String[allCropCount + 1];
//            cropStringArray[0] = "Select Crop";
            cropStringArray[0] = getDynamicLanguageValue(FarmSignUp.this, "Select", R.string.Select);
            if (allCropCount > 0) {
                allCrop.moveToFirst();
                for (int i = 1; i <= allCropCount; i++) {
                    cropStringArray[i] = allCrop.getString(allCrop.getColumnIndex(DBAdapter.CROP));
                    allCrop.moveToNext();
                }
            }
            ArrayAdapter<String> cropArrayAdapter = new ArrayAdapter<String>(FarmSignUp.this, R.layout.spinner_layout, cropStringArray); //selected item will look like a spinner set from XML
            binding.spincrop.setAdapter(cropArrayAdapter);
            setCustomSearchableSpinner(getApplicationContext(), binding.spincrop,"SelectCrop", R.string.SelectCrop);
//            binding.spincrop.setTitle(getDynamicLanguageValue(FarmSignUp.this, "SelectCrop", R.string.SelectCrop));
            binding.spincrop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    System.out.println("Inside CropId selection " + position);
                    if (position > 0) {
                        allCrop.moveToPosition(position - 1);
                        cropId = allCrop.getString(allCrop.getColumnIndex(DBAdapter.CROP_ID));
                        cropName = allCrop.getString(allCrop.getColumnIndex(DBAdapter.CROP));
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setDitrictBind(String State_id) {
        db.open();
        Districts = new ArrayList<>();

        ArrayList<HashMap<String, String>> hasmap = new ArrayList<>();
        String sql = "select ID,StateID,Upper(StateName) as StateName,DistrictID,Upper(DistrictName) as DistrictName from MstStateDistrict where StateID='" + State_id + "' and DistrictID!='' and DistrictName!='' group by DistrictID order by DistrictName";
        hasmap = db.getDynamicTableValue(sql);
        ArrayList<String> list = new ArrayList<>();

        HashMap<String, String> hash = new HashMap<>();
        hash.put("DistrictID", "0");
//        hash.put("DistrictName", "Select District");
        hash.put("DistrictName", getDynamicLanguageValue(FarmSignUp.this, "Select", R.string.Select));
        Districts.add(hash);
//        list.add("Select District");
        list.add(getDynamicLanguageValue(FarmSignUp.this, "Select", R.string.Select));

        int DistrictposselectedatFarmRegistrtion = 0;
        if (hasmap.size() > 0) {
            for (int i = 1; i <= hasmap.size(); i++) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("DistrictID", hasmap.get(i - 1).get("DistrictID"));
                list.add(hasmap.get(i - 1).get("DistrictName"));
                hashMap.put("DistrictName", hasmap.get(i - 1).get("DistrictName"));
                Districts.add(hashMap);
            }
        }
        ArrayAdapter<String> stateListAdapter = new ArrayAdapter<String>(FarmSignUp.this, R.layout.spinner_layout, list);
        binding.DistrictNameSpin.setAdapter(stateListAdapter);
        setCustomSearchableSpinner(getApplicationContext(), binding.DistrictNameSpin,"SelectDistrict", R.string.SelectDistrict);
//        binding.DistrictNameSpin.setTitle(getDynamicLanguageValue(FarmSignUp.this, "SelectDistrict", R.string.SelectDistrict));
        binding.DistrictNameSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position > 0 && Districts.size() > 0) {
                    DistrictID = Districts.get(position).get("DistrictID");
                    DistrictName = Districts.get(position).get("DistrictName");
                    getAPIURL(3, DistrictID, DistrictName);
                } else {
                    DistrictID = null;
                    DistrictName = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        db.close();


    }

    public void getAPIURL(int flag, String ID, String Name) {
        String URL = "";
        switch (flag) {
//            case 1://State
//                URL = "https://myfarminfo.com/yfirest.svc/Clients/GetAllStates/2";
//                break;
//            case 2://District
//                URL = "https://myfarminfo.com/yfirest.svc/Clients/GetStateDistrict/" + ID;
//                break;
            case 3://Sub District
                URL = "https://myfarminfo.com/yfirest.svc/Clients/GetDistrictsubdistrict/" + ID;
                break;
            case 4://Village
                URL = "https://myfarminfo.com/yfirest.svc/Clients/GetsubdistrictVillage/" + ID + "/" + Name;
                break;
        }
        new getStateDistVillage(flag, URL).execute();
    }

    public void SetSpinner(final int flag, CustomSearchableSpinner spinner, ArrayList<HashMap<String, String>> value) {
        final ArrayList<String> getValue = new ArrayList();
        final ArrayList<String> getIDs = new ArrayList();
        int SubDistrictposselectedatFarmRegistrtion = 0, VillageposselectedatFarmRegistrtion = 0;
        if (value != null) {
            switch (flag) {
                case 1://State
                    for (int i = 0; i < value.size(); i++) {
                        getValue.add(value.get(i).get("StateName"));
                        getIDs.add(value.get(i).get("StateID"));
                    }
                    break;
                case 2://District
                    for (int i = 0; i < value.size(); i++) {
                        getValue.add(value.get(i).get("District"));
                        getIDs.add(value.get(i).get("DistrictID"));
                    }
                    break;
                case 3://Sub District
//                    getValue.add("Select Sub District");
                    getValue.add(getDynamicLanguageValue(FarmSignUp.this, "Select", R.string.Select));
//                    getValue.add(getResources().getString(R.string.SelectDistrict));
                    getIDs.add("0");
                    for (int i = 0; i < value.size(); i++) {
                        getValue.add(value.get(i).get("Sub_district"));
                        getIDs.add(value.get(i).get("Sub_district_Pos"));
                        if (FarmRegistration_Sub_District != null) {
                            if (FarmRegistration_Sub_District.equalsIgnoreCase(value.get(i).get("Sub_district"))) {
                                SubDistrictposselectedatFarmRegistrtion = i;
                            }
                        }
                    }
                    break;
                case 4://Village
//                    getValue.add("Select Village");
                    getValue.add(getDynamicLanguageValue(FarmSignUp.this, "Select", R.string.Select));
//                    getValue.add(getResources().getString(R.string.SelectVillage));
                    getIDs.add("0");
                    for (int i = 0; i < value.size(); i++) {
                        getValue.add(value.get(i).get("Village_Final"));
                        getIDs.add(value.get(i).get("Village_ID"));
                    }
                    break;
            }
        }
        ArrayAdapter<String> stateListAdapter = new ArrayAdapter<String>(FarmSignUp.this, R.layout.spinner_layout, getValue);
        spinner.setAdapter(stateListAdapter);
        switch (flag) {
            case 1://State
                setCustomSearchableSpinner(getApplicationContext(), spinner,"SelectState", R.string.SelectState);
//                spinner.setTitle(getDynamicLanguageValue(FarmSignUp.this, "SelectState", R.string.SelectState));
                break;
            case 2:
                break;
            case 3://Sub Districtzx
                setCustomSearchableSpinner(getApplicationContext(), spinner,"SelectSubDistrict", R.string.SelectSubDistrict);
//                spinner.setTitle(getDynamicLanguageValue(FarmSignUp.this, "SelectSubDistrict", R.string.SelectSubDistrict));
//                getValue.add(getResources().getString(R.string.SelectDistrict));
                break;
            case 4://Village
                setCustomSearchableSpinner(getApplicationContext(), spinner,"SelectVillage", R.string.SelectVillage);
//                spinner.setTitle(getDynamicLanguageValue(FarmSignUp.this, "SelectVillage", R.string.SelectVillage));
//                    getValue.add("Select Village");
//                getValue.add(getResources().getString(R.string.SelectVillage));
                break;
        }
        db.close();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
//                if (position > 0) {
                try {
                    switch (flag) {
//                        case 1://State
//                            StateID = getIDs.get(position);
//                            StateName = getValue.get(position);
//                            db.open();
//                            allCrop = db.getCropByState(StateID);
//                            final int allCropCount = allCrop.getCount();
//                            String[] cropStringArray = new String[allCropCount + 1];
//                            cropStringArray[0] = "Select Crop";
//                            if (allCropCount > 0) {
//                                allCrop.moveToFirst();
//                                for (int i = 1; i <= allCropCount; i++) {
//                                    cropStringArray[i] = allCrop.getString(allCrop.getColumnIndex(DBAdapter.CROP));
//                                    allCrop.moveToNext();
//                                }
//                            }
//
//                            ArrayAdapter<String> cropArrayAdapter = new ArrayAdapter<String>(FarmSignUp.this, R.layout.spinner_layout, cropStringArray); //selected item will look like a spinner set from XML
//                            cropArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                            cropSpiner1.setAdapter(cropArrayAdapter);
//                            cropSpiner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                                @Override
//                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                                    String cropId = "-1";
//                                    String cropName = "";
//                                    System.out.println("Inside CropId selection " + position);
//                                    if (position > 0) {
//                                        allCrop.moveToPosition(position - 1);
//                                        cropId = allCrop.getString(allCrop.getColumnIndex(DBAdapter.CROP_ID));
//                                        cropName = allCrop.getString(allCrop.getColumnIndex(DBAdapter.CROP));
//                                    }
//                                    farmInformationData.setCrop(cropName);
//                                    farmInformationData.setCropID(cropId);
//                                    setVariety(StateID, cropId);
//                                }
//
//                                @Override
//                                public void onNothingSelected(AdapterView<?> parent) {
//
//                                }
//                            });
////Call District
//                            getAPIURL(2, StateID, StateName);
//                            break;
//                        case 2://District
//                            if (getValue.size() > 0) {
//                                DistrictID = getIDs.get(position);
//                                DistrictName = getValue.get(position);
//                            } else {
//                                DistrictID = null;
//                                DistrictName = null;
//                            }
//                            getAPIURL(3, DistrictID, DistrictName);
//                            break;
                        case 3://Sub District
                            if (getValue.size() > 0) {
                                SubDistrictID = getIDs.get(position);
                                SubDistrictName = getValue.get(position);
                            } else {
                                SubDistrictID = null;
                                SubDistrictName = null;
                            }
                            if (position > 0) {
                                binding.llOtherDistrict.setVisibility(View.GONE);
                                binding.OthersubDistrictEdt.setText("");
                            } else {
                                binding.llOtherDistrict.setVisibility(View.VISIBLE);
                            }
                            getAPIURL(4, DistrictID, SubDistrictName);
                            break;
                        case 4://Village
                            if (getValue.size() > 0) {
                                VillageID = getIDs.get(position);
                                VillageName = getValue.get(position);
                            } else {
                                VillageID = null;
                                VillageName = null;
                            }
                            if (position > 0) {
                                binding.llOtherVillageName.setVisibility(View.GONE);
                                binding.OtherVillageNameEdt.setText("");
                            } else {
                                binding.llOtherVillageName.setVisibility(View.VISIBLE);
                            }
                            break;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        try {
            switch (flag) {
//                        case 1://State
//                        case 2://District
                case 3://Sub District
                    if (FarmRegistration_Sub_District != null) {
                        if (SubDistrictposselectedatFarmRegistrtion != 0) {
                            spinner.setSelection(SubDistrictposselectedatFarmRegistrtion);
                        } else {
                            binding.OthersubDistrictEdt.setText(FarmRegistration_Sub_District);
                        }
                    }
                    break;
                case 4://Village
                    if (FarmRegistration_VillageID != null) {
                        if (spinner.getCount() == 0) {
                            if (FarmRegistration_VillageStr != null) {
                                binding.OtherVillageNameEdt.setText(FarmRegistration_VillageStr);
                            }
                        } else {
                            spinner.setSelection(VillageposselectedatFarmRegistrtion);
                        }
                    }
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private class getStateDistVillage extends AsyncTask<Void, Void, String> {

        int flag = 0;
        String URL = "";
        TransparentProgressDialog progressDialog;

        public getStateDistVillage(int flags, String URLs) {
            flag = flags;
            URL = URLs;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            String sms = "";
            switch (flag) {
//                case 1://State
//                    progressDialog.setMessage(getResources().getString(R.string.LoadingStates));
//                    break;
//                case 2://District
//                    progressDialog.setMessage(getResources().getString(R.string.LoadingDistricts));
//                    break;
                case 3://Sub District
                    sms = getDynamicLanguageValue(FarmSignUp.this, "LoadingSubDistricts", R.string.LoadingSubDistricts);
                    break;
                case 4://Village
                    sms = getDynamicLanguageValue(FarmSignUp.this, "LoadingVillages", R.string.LoadingVillages);
                    break;
            }
            progressDialog = new TransparentProgressDialog(
                    FarmSignUp.this, sms);
            progressDialog.setCancelable(false);
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    // cancel AsyncTask
                    cancel(false);
                }
            });
            progressDialog.show();
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
                String response = AppManager.getInstance().httpRequestGetMethod(URL);
                return AppManager.getInstance().RemoveStringUnwanted(response);

            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPreExecute();

//            if (result.equalsIgnoreCase("No Data")) {
            if (result.equalsIgnoreCase("No Data") || result.equalsIgnoreCase("Could not connect to server")) {
                switch (flag) {
//                    case 1://State
//                        States = null;
//                        StateName = "";
//                        StateID = "";
//                        SetSpinner(flag, stateList, States);
//                        break;
//                    case 2://District
//                        Districts = null;
//                        DistrictName = null;
//                        DistrictID = null;
//                        SetSpinner(flag, DistrictName_spin, Districts);
//                        //Sub District
//                        SubDistricts = null;
//                        SubDistrictName = null;
//                        SubDistrictID = null;
//                        SetSpinner(flag, SubDistrictName_spin, SubDistricts);
//                        //Village
//                        Villages = null;
//                        VillageName = null;
//                        VillageID = null;
//                        SetSpinner(flag, VillageName_spin, Villages);
//                        break;
                    case 3://Sub District
                        SubDistricts = null;
                        SubDistrictName = null;
                        SubDistrictID = null;
                        SetSpinner(flag, binding.SubDistrictNameSpin, SubDistricts);
                        //Village
                        Villages = null;
                        VillageName = null;
                        VillageID = null;
                        SetSpinner(flag, binding.VillageNameSpin, Villages);
                        break;
                    case 4://Village
                        Villages = null;
                        VillageName = null;
                        VillageID = null;
                        SetSpinner(flag, binding.VillageNameSpin, Villages);
                        break;
                }
//                Toast.makeText(FarmSignUp.this, getResources().getString(R.string.Nodataavailable), Toast.LENGTH_LONG).show();
            } else {
                try {
                    //stateList,DistrictName_spin,SubDistrictName_spin,VillageName_spin
                    switch (flag) {
//                        case 1://State
//                            JSONObject json = new JSONObject(result.toString());
//                            JSONArray jsonArray_State = json.getJSONArray("DT");
//                            States = getStateDistrictValues(flag, jsonArray_State.toString());
//                            SetSpinner(flag, stateList, States);
//                            break;
//                        case 2://District
//                            JSONArray jsonArray_Districts = new JSONArray(result.toString());
//                            Districts = getStateDistrictValues(flag, jsonArray_Districts.toString());
//                            SetSpinner(flag, DistrictName_spin, Districts);
//                            break;
                        case 3://Sub District
                            JSONArray jsonArray_SubDistricts = new JSONArray(result.toString());
                            SubDistricts = getStateDistrictValues(flag, jsonArray_SubDistricts.toString());
                            SetSpinner(flag, binding.SubDistrictNameSpin, SubDistricts);
                            break;
                        case 4://Village
                            JSONArray jsonArray_Villages = new JSONArray(result.toString());
                            Villages = getStateDistrictValues(flag, jsonArray_Villages.toString());
                            SetSpinner(flag, binding.VillageNameSpin, Villages);
                            break;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    getDynamicLanguageToast(FarmSignUp.this, "Couldnotconnect", R.string.Couldnotconnect);
                }
            }

            progressDialog.dismiss();

        }
    }

    public ArrayList<HashMap<String, String>> getStateDistrictValues(int flag, String JsonValues) {
        ArrayList<HashMap<String, String>> values = new ArrayList<>();

        JSONArray jsonArray = null;

        try {
            jsonArray = new JSONArray(JsonValues);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        switch (flag) {
            case 1://State
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        String StateID = "", StateName = "";
                        StateID = obj.getString("StateID");
                        StateName = obj.getString("StateName");
                        HashMap<String, String> setval = new HashMap<>();
                        setval.put("StateID", StateID);
                        setval.put("StateName", StateName);
                        values.add(setval);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                break;
            case 2://District
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        String DistrictID = "", District = "";
                        DistrictID = obj.getString("DistrictID");
                        District = obj.getString("District");
                        HashMap<String, String> setval = new HashMap<>();
                        setval.put("DistrictID", DistrictID);
                        setval.put("District", District);
                        values.add(setval);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                break;
            case 3://Sub District
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        String Sub_district = "", Sub_district_Pos = "";
                        Sub_district = obj.getString("Sub_district");
                        Sub_district_Pos = String.valueOf(i);
                        HashMap<String, String> setval = new HashMap<>();
                        setval.put("Sub_district", Sub_district);
                        setval.put("Sub_district_Pos", Sub_district_Pos);
                        values.add(setval);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                break;
            case 4://Village
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        String Village_Final = "", Village_ID = "";
                        Village_Final = obj.getString("Village_Final");
                        Village_ID = obj.getString("Village_ID");
                        HashMap<String, String> setval = new HashMap<>();
                        setval.put("Village_Final", Village_Final);
                        setval.put("Village_ID", Village_ID);
                        values.add(setval);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                break;
        }

        return values;
    }

    private class sentRequestForFarmSave extends AsyncTask<Void, Void, String> {

        String result = null;
        ProgressDialog progressDialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(FarmSignUp.this);
            progressDialog.setMessage(getDynamicLanguageValue(FarmSignUp.this, "Submittingtagged", R.string.Submittingtagged));
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(true);
            progressDialog.show();

        }

        @Override
        protected String doInBackground(Void... params) {

            String response = null;
            try {
                db.open();
                String sendPath = AppManager.getInstance().saveFarmInfo;//Herojit Comment
                String createdString = null;
                String jsonParameterString = createJsonParameterForSaveForm();
                createdString = AppManager.getInstance().removeSpaceForUrl(jsonParameterString);
                response = AppManager.getInstance().httpRequestPutMethod(sendPath, createdString);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPreExecute();
            progressDialog.dismiss();

            Log.v("response_saved_farm", response.toString() + "");
            try {
                if (response != null) {
                    if (response.contains("Success")) {
                        getDynamicLanguageToast(FarmSignUp.this, "SubmittedSuccessfully", R.string.SubmittedSuccessfully);
                        finish();
                    } else {
                        getDynamicLanguageToast(FarmSignUp.this, "Networknotavailable", R.string.Networknotavailable);
                    }
                }
                if (response.contains("NotSave")) {
                    getDynamicLanguageToast(FarmSignUp.this, "ServerRejected", R.string.ServerRejected);
                }
                if (response.contains("Error")) {
                    getDynamicLanguageToast(FarmSignUp.this, "Farmalreadyexist", R.string.Farmalreadyexist);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                getDynamicLanguageToast(FarmSignUp.this, "server_not_found", R.string.server_not_found);

            }


        }


        private String createJsonParameterForSaveForm() {
            String parameterString = "";

            JSONArray cropInfo = new JSONArray();


            JSONObject finalJson = new JSONObject();
            JSONObject jsonObject = new JSONObject();
            try {
                JSONObject obj = new JSONObject();
                obj.put("CropID", cropId);
                obj.put("CropName", cropName);
                cropInfo.put(obj);

//                Lat,Lon-Lat,Lon-Lat,Lon-Lat,Con
                String contour = lat + "," + lon + "-" + lat + "," + lon + "-" + lat + "," + lon + "-" + lat + "," + lon;

//                jsonObject.put("UserID", farmData.getUserID());
                jsonObject.put("FarmID", 0);
//                jsonObject.put("FarmName", farmData.getFarmName());
                jsonObject.put("FarmerName", binding.editName.getText().toString());
                jsonObject.put("PhoneNo", binding.editNameMobileNumber.getText().toString());
                jsonObject.put("Contour", contour);
                jsonObject.put("CropID", cropId);
                jsonObject.put("State", StateID);
                jsonObject.put("StateName", StateName);
                //Herojit Add
                jsonObject.put("District", DistrictID);
//                jsonObject.put("Sub_district", SubDistrictName);
                jsonObject.put("CropInfo", cropInfo);
                jsonObject.put("Area", "0.0");

                if (binding.OthersubDistrictEdt.getText().toString().trim().length() > 0) {
                    jsonObject.put("Block", binding.OthersubDistrictEdt.getText().toString());
                } else {
                    jsonObject.put("Block", SubDistrictName);
                }
                if (binding.OtherVillageNameEdt.getText().toString().trim().length() > 0) {
                    jsonObject.put("VillageStr", binding.OtherVillageNameEdt.getText().toString());
                    jsonObject.put("VillageID", "0");
                } else {
                    jsonObject.put("VillageStr", VillageName);
                    jsonObject.put("VillageID", VillageID);
                }

                jsonObject.put("ProjectID", "373413");
                jsonObject.put("TaggingApp", "MFI");
                //Herojit Comment

                finalJson.put("FarmInfo", jsonObject.toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }
            //      jsonObject.put("guarderiasIdGuarderias",jsonObject2);
            parameterString = finalJson.toString();

            return parameterString;
        }

    }

    public boolean isValidate() {
        try {
            if (!(binding.editName.getText().toString().length() > 0)) {
                getDynamicLanguageToast(FarmSignUp.this, "Pleasentename", R.string.Pleasentename);
                return false;
            }
            if (AppManager.getInstance().checkNullorNot(binding.editNameMobileNumber.getText().toString()) == false ||
                    !AppManager.getInstance().isMobileNoValid(binding.editNameMobileNumber.getText().toString())) {
                getDynamicLanguageToast(FarmSignUp.this, "validmobileno", R.string.validmobileno);
                return false;
            }
            if (AppManager.getInstance().checkNullorNot(StateID) == false) {
                getDynamicLanguageToast(FarmSignUp.this, "PleaseSelectState", R.string.PleaseSelectState);
                return false;
            }
            if (AppManager.getInstance().checkNullorNot(DistrictName) == false) {
                getDynamicLanguageToast(FarmSignUp.this, "PleaseselectDistrict", R.string.PleaseselectDistrict);
                return false;
            }
            if ((AppManager.getInstance().checkNullorNot(binding.OthersubDistrictEdt.getText().toString()) == false) &&
                    (SubDistrictName.equalsIgnoreCase(getDynamicLanguageValue(FarmSignUp.this, "Select", R.string.Select)))) {
                getDynamicLanguageToast(FarmSignUp.this, "Pleaseselectsubdistrict", R.string.Pleaseselectsubdistrict);
                return false;
            }
            if ((AppManager.getInstance().checkNullorNot(binding.OtherVillageNameEdt.getText().toString()) == false) &&
                    (VillageName.equalsIgnoreCase(getDynamicLanguageValue(FarmSignUp.this, "Select", R.string.Select)))) {
                getDynamicLanguageToast(FarmSignUp.this, "Pleaseselectviilage", R.string.Pleaseselectviilage);
                return false;
            }

            if (!(binding.spincrop.getSelectedItem().toString().trim() != getDynamicLanguageValue(FarmSignUp.this, "Select", R.string.Select))) {
                getDynamicLanguageToast(FarmSignUp.this, "PleaseselectCrop", R.string.PleaseselectCrop);
                return false;
            } else {
                cropName = binding.spincrop.getSelectedItem().toString().trim();
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return true;
    }

    public void setLanguages() {
        setDynamicLanguage(this, binding.titleMain, "SignUp", R.string.SignUp);
        setDynamicLanguage(this, binding.txtAllfieldmandetory, "Allfieldmandetory", R.string.Allfieldmandetory);
        setDynamicLanguage(this, binding.txtName, "Name", R.string.Name);
        setDynamicLanguage(this, binding.txtMobileNumber, "MobileNumber", R.string.MobileNumber);
        setDynamicLanguage(this, binding.txtStateName, "StateName", R.string.StateName);
        setDynamicLanguage(this, binding.txtDistrictName, "DistrictName", R.string.DistrictName);
        setDynamicLanguage(this, binding.txtSubDistrictName, "SubDistrictName", R.string.SubDistrictName);
        setDynamicLanguage(this, binding.txtOthersubDistrict, "OtherDistrict", R.string.OtherDistrict);
        setDynamicLanguage(this, binding.txtVillageName, "VillageName", R.string.VillageName);
        setDynamicLanguage(this, binding.txtOtherVillageName, "OtherVillageName", R.string.OtherVillageName);
        setDynamicLanguage(this, binding.txtcrop, "Crops", R.string.Crops);
        setDynamicLanguage(this, binding.btnSubmit, "Submit", R.string.Submit);

        setFontsStyleTxt(this, binding.titleMain, 2);
        setFontsStyleTxt(this, binding.txtAllfieldmandetory, 5);
        setFontsStyleTxt(this, binding.txtName, 5);
        setFontsStyleTxt(this, binding.txtMobileNumber, 5);
        setFontsStyleTxt(this, binding.txtStateName, 5);
        setFontsStyleTxt(this, binding.txtDistrictName, 5);
        setFontsStyleTxt(this, binding.txtSubDistrictName, 5);
        setFontsStyleTxt(this, binding.txtOthersubDistrict, 5);
        setFontsStyleTxt(this, binding.txtVillageName, 5);
        setFontsStyleTxt(this, binding.txtOtherVillageName, 5);
        setFontsStyleTxt(this, binding.txtcrop, 5);
        setFontsStyle(this, binding.btnSubmit);
    }
}