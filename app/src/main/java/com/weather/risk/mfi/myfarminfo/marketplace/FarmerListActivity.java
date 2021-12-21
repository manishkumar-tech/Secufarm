package com.weather.risk.mfi.myfarminfo.marketplace;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.database.DBAdapter;
import com.weather.risk.mfi.myfarminfo.databinding.SearchFarmActivityBinding;
import com.weather.risk.mfi.myfarminfo.home.AppController;
import com.weather.risk.mfi.myfarminfo.retofitservices.ApiService;
import com.weather.risk.mfi.myfarminfo.retofitservices.ResponseObserver;
import com.weather.risk.mfi.myfarminfo.retrofitmodelnew.FarmerListRequest;
import com.weather.risk.mfi.myfarminfo.retrofitmodelnew.FarmerResponse;
import com.weather.risk.mfi.myfarminfo.utils.AppConstant;
import com.weather.risk.mfi.myfarminfo.utils.AppManager;
import com.weather.risk.mfi.myfarminfo.utils.TransparentProgressDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class FarmerListActivity extends BaseActivity implements ItemClickInterface {

    private ApiService apiService;
    SearchFarmActivityBinding binding;
    Spinner spin_state, spin_district, spin_subdistrict, spin_villageName;

    ArrayList<HashMap<String, String>> States = new ArrayList<>();
    ArrayList<HashMap<String, String>> Districts = new ArrayList<>();
    ArrayList<HashMap<String, String>> SubDistricts = new ArrayList<>();
    ArrayList<HashMap<String, String>> Villages = new ArrayList<>();
    DBAdapter db;
    String StateID = "", StateName = "", DistrictID = "", DistrictName = "", SubDistrictID = "",
            SubDistrictName = "", VillageID = null, VillageName = null;


    Spinner spin_project;
    ArrayList<HashMap<String, String>> Projects = new ArrayList<>();
    public static String ProjectID = null, ProjectName = null;


    ImageView project_refresh;

    @Override
    protected void initView(Bundle bundle, ViewDataBinding viewDataBinding) {

        binding = (SearchFarmActivityBinding) viewDataBinding;


        spin_project = binding.spinProject;
        project_refresh = binding.projectRefresh;

        spin_state = binding.spinState;
        spin_district = binding.spinDistrict;
        spin_subdistrict = binding.spinSubdistrict;
        spin_villageName = binding.spinVillageName;

        apiService = AppController.getInstance().getApiService();

        binding.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String adharN = binding.edtAadharNo.getText().toString().trim();
                String phn = binding.edtMobileNo.getText().toString().trim();

                if (adharN.length() == 12 || phn.length() == 10 || VillageID!=null || ProjectID!=null) {
                    getFarmerListMethod( phn,adharN);
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter valid Aadhar or Phone number or Village", Toast.LENGTH_SHORT).show();
                }

            }
        });

        binding.newEnrollmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getApplicationContext(), FarmRegistrationNew.class);
                startActivity(in);
                finish();
            }
        });

        db = new DBAdapter(this);
        db.open();
        db.clearCartItem();
        db.close();

        setStateBind();

        project_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    new getStateProjectRefresh("Project", AppManager.getInstance().ProjectListURL(AppConstant.user_id)).execute();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        try {
            new getStateProjectRefresh("Project", AppManager.getInstance().ProjectListURL(AppConstant.user_id)).execute();
        } catch (Exception ex) {
            ex.printStackTrace();
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
        hash.put("StateName", "Select State");
        States.add(hash);
        list.add("Select State");
        if (hasmap.size() > 0) {
            for (int i = 1; i <= hasmap.size(); i++) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("StateID", hasmap.get(i - 1).get("StateID"));
                list.add(hasmap.get(i - 1).get("StateName"));
                hashMap.put("StateName", hasmap.get(i - 1).get("StateName"));
                States.add(hashMap);
            }
        }
        ArrayAdapter<String> stateListAdapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, list);
        spin_state.setAdapter(stateListAdapter);
        spin_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position > 0 && States.size() > 0) {
                    try {
                        //State

                        StateID = States.get(position).get("StateID");
                        StateName = States.get(position).get("StateName");
//                        setCropsBind();
//                        allbindCrop();
                        setDitrictBind(StateID);

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else {
                    StateID = "0";
                    StateName = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        db.close();
    }


    public void setDitrictBind(String State_id) {
        db.open();
        Districts = new ArrayList<>();

        ArrayList<HashMap<String, String>> hasmap = new ArrayList<>();
        hasmap = db.getDynamicTableValue("select ID,StateID,Upper(StateName) as StateName,DistrictID,Upper(DistrictName) as DistrictName from MstStateDistrict where StateID='" + State_id + "' and DistrictID!='' and DistrictName!='' group by DistrictID order by DistrictName");
        ArrayList<String> list = new ArrayList<>();

        HashMap<String, String> hash = new HashMap<>();
        hash.put("DistrictID", "0");
        hash.put("DistrictName", "Select District");
        Districts.add(hash);
        list.add("Select District");
        if (hasmap.size() > 0) {
            for (int i = 1; i <= hasmap.size(); i++) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("DistrictID", hasmap.get(i - 1).get("DistrictID"));
                list.add(hasmap.get(i - 1).get("DistrictName"));
                hashMap.put("DistrictName", hasmap.get(i - 1).get("DistrictName"));
                Districts.add(hashMap);
            }
        }
        ArrayAdapter<String> stateListAdapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, list);
        spin_district.setAdapter(stateListAdapter);
        spin_district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position > 0 && Districts.size() > 0) {
                    DistrictID = Districts.get(position).get("DistrictID");
                    DistrictName = Districts.get(position).get("DistrictName");
                    getAPIURL(3, DistrictID, DistrictName);
                } else {
                    DistrictID = "0";
                    DistrictName = "";
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
                    sms = getResources().getString(R.string.LoadingSubDistricts);
                    break;
                case 4://Village
                    sms = getResources().getString(R.string.LoadingVillages);
                    break;
            }
            progressDialog = new TransparentProgressDialog(
                    FarmerListActivity.this, sms);
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
                return AppManager.getInstance().RemoveStringUnwanted(response);

            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPreExecute();

            if (result.equalsIgnoreCase("No Data") || result.equalsIgnoreCase("Could not connect to server")) {
                switch (flag) {
//                    case 1://State
//                        States = null;
//                        AppConstant.state = "";
//                        AppConstant.stateID = "";
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
                        SetSpinner(flag, spin_subdistrict, SubDistricts);
                        //Village
                        Villages = null;
                        VillageName = null;
                        VillageID = null;
                        SetSpinner(flag, spin_villageName, Villages);
                        break;
                    case 4://Village
                        Villages = null;
                        VillageName = null;
                        VillageID = null;
                        SetSpinner(flag, spin_villageName, Villages);
                        break;
                }
//                Toast.makeText(FarmerListActivity.this, getResources().getString(R.string.Nodataavailable), Toast.LENGTH_LONG).show();
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
                            SetSpinner(flag, spin_subdistrict, SubDistricts);
                            break;
                        case 4://Village
                            JSONArray jsonArray_Villages = new JSONArray(result.toString());
                            Villages = getStateDistrictValues(flag, jsonArray_Villages.toString());
                            SetSpinner(flag, spin_villageName, Villages);
                            break;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(FarmerListActivity.this, getResources().getString(R.string.Couldnotconnect), Toast.LENGTH_LONG).show();
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


    public void SetSpinner(final int flag, Spinner spinner, ArrayList<HashMap<String, String>> value) {
        final ArrayList<String> getValue = new ArrayList();
        final ArrayList<String> getIDs = new ArrayList();
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
                    getValue.add("Select Sub District");
                    getIDs.add("0");
                    for (int i = 0; i < value.size(); i++) {
                        getValue.add(value.get(i).get("Sub_district"));
                        getIDs.add(value.get(i).get("Sub_district_Pos"));
                    }
                    break;
                case 4://Village
                    getValue.add("Select Village");
                    getIDs.add("0");
                    for (int i = 0; i < value.size(); i++) {
                        getValue.add(value.get(i).get("Village_Final"));
                        getIDs.add(value.get(i).get("Village_ID"));
                    }
                    break;
            }
        }
        ArrayAdapter<String> stateListAdapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, getValue);
        spinner.setAdapter(stateListAdapter);
        db.close();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
//                if (position > 0) {
                try {
                    switch (flag) {
                        case 3://Sub District
                            if (getValue.size() > 0) {
                                SubDistrictID = getIDs.get(position);
                                SubDistrictName = getValue.get(position);
                            } else {
                                SubDistrictID = null;
                                SubDistrictName = null;
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

                            break;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }


//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    protected int getActivityLayout() {
        return R.layout.search_farm_activity;
    }


    private void getFarmerListMethod(String phNo, String aadharN) {
        // display a progress dialog
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Please Wait..."); // set message
        progressDialog.show(); // show progress dialog

        FarmerListRequest request = new FarmerListRequest();
        if (phNo!=null) {
            request.setPhoneNumber(phNo);
        }
        if (aadharN!=null) {
            request.setAdhaar(aadharN);
        }
        if (VillageID!=null && VillageID.length()>0) {
            request.setVillageID(Integer.valueOf(VillageID));
        }

        if (ProjectID!=null && ProjectID.length()>0) {
            request.setProjectID(Integer.valueOf(ProjectID));
        }




        apiService.getFarmerList(request).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResponseObserver<Response<List<FarmerResponse>>>(getCompositeDisposable()) {
                    @Override
                    public void onNetworkError(Throwable e) {
                        progressDialog.cancel();
                        Log.v("wvqevqevqe", e.getMessage() + " " + e.toString());
                        showError(getString(R.string.network_error));

                    }

                    @Override
                    public void onServerError(Throwable e, int code) {
                        progressDialog.cancel();
                        showError(getString(R.string.server_not_found));

                    }

                    @Override
                    public void onNext(Response<List<FarmerResponse>> response) {
                        progressDialog.cancel();

                        List<FarmerResponse> responsesData = response.body();

                        if (responsesData != null && responsesData.size() > 0) {
                            binding.emptyList.setVisibility(View.GONE);
                            binding.farmRecyclerView.setVisibility(View.VISIBLE);

                            FarmerAdapter adapter = new FarmerAdapter(FarmerListActivity.this, responsesData, FarmerListActivity.this);
                            binding.farmRecyclerView.setLayoutManager(new LinearLayoutManager(FarmerListActivity.this, LinearLayoutManager.VERTICAL, false));
                            binding.farmRecyclerView.setItemAnimator(new DefaultItemAnimator());
                            binding.farmRecyclerView.setAdapter(adapter);

                            binding.newEnrollmentBtn.setVisibility(View.VISIBLE);


                        } else {
                            binding.emptyList.setVisibility(View.VISIBLE);
                            binding.farmRecyclerView.setVisibility(View.GONE);
                            binding.newEnrollmentBtn.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

    @Override
    public void onClick(int value) {

        if (value >= 0) {

            //it will be call on registration page
            Log.v("cwqcqwcqw", value + "");

            Intent intent = new Intent(getApplicationContext(), FarmerDetailsActivity.class);
            intent.putExtra("farmerId", "" + value);
            startActivity(intent);
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                EditText edit = ((EditText) v);
                Rect outR = new Rect();
                edit.getGlobalVisibleRect(outR);
                Boolean isKeyboardOpen = !outR.contains((int) ev.getRawX(), (int) ev.getRawY());
                System.out.print("Is Keyboard? " + isKeyboardOpen);
                if (isKeyboardOpen) {
                    System.out.print("Entro al IF");
                    edit.clearFocus();
                    InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(edit.getWindowToken(), 0);
                }

                edit.setCursorVisible(!isKeyboardOpen);

            }
        }
        return super.dispatchTouchEvent(ev);
    }

    public void setProject() {
        db.open();
        Projects = new ArrayList<>();

        ArrayList<HashMap<String, String>> hasmap = new ArrayList<>();
        hasmap = db.getDynamicTableValue("Select * from " + db.TABLE_Projectlist + " where ProjectName !='' and  ProjectName !='null' order by ProjectName");
        ArrayList<String> list = new ArrayList<>();

        HashMap<String, String> hash = new HashMap<>();
        hash.put("ProjectID", "0");
        hash.put("ProjectName", "Select Project");
        Projects.add(hash);
        list.add("Select Project");
        if (hasmap.size() > 0) {
            for (int i = 1; i <= hasmap.size(); i++) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("ProjectID", hasmap.get(i - 1).get("ProjectID"));
                list.add(hasmap.get(i - 1).get("ProjectName"));
                hashMap.put("ProjectName", hasmap.get(i - 1).get("ProjectName"));
                Projects.add(hashMap);
            }
        }
        ArrayAdapter<String> stateListAdapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, list);
        spin_project.setAdapter(stateListAdapter);
        spin_project.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position > 0 && Projects.size() > 0) {
                    ProjectID = Projects.get(position).get("ProjectID");
                    ProjectName = Projects.get(position).get("ProjectName");
                } else {
                    ProjectID = null;
                    ProjectName = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        db.close();
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
                    sms = getResources().getString(R.string.LoadingProject);
                    break;
                case "StateDistrict"://District
                    sms = getResources().getString(R.string.Loadingstate);
                    break;
                case "Crops"://District
                    sms = getResources().getString(R.string.LoadingCrops);
                    break;

            }
            progressDialog = new TransparentProgressDialog(FarmerListActivity.this, sms);
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
                            Toast.makeText(FarmerListActivity.this, getResources().getString(R.string.FormattingError), Toast.LENGTH_LONG).show();
                        }
                        SqliteDB.setTransactionSuccessful();
                        SqliteDB.endTransaction();
                        db.getClass();

                        setProject();
                        break;

                }

            } else
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.Nodataavailable), Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        }
    }



}