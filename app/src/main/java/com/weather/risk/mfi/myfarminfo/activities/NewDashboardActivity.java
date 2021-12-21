package com.weather.risk.mfi.myfarminfo.activities;

import static com.weather.risk.mfi.myfarminfo.utils.Utility.setDynamicLanguage;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setFontsStyleTxt;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.database.DBAdapter;
import com.weather.risk.mfi.myfarminfo.isconbalaji.VisitFragment;
import com.weather.risk.mfi.myfarminfo.isconbalaji.VisitReportFragment;
import com.weather.risk.mfi.myfarminfo.mapfragments.CropFeasibilityFragment;
import com.weather.risk.mfi.myfarminfo.mapfragments.CropSecureFragment;
import com.weather.risk.mfi.myfarminfo.mapfragments.LatLonCellID;
import com.weather.risk.mfi.myfarminfo.mapfragments.LocateYoutFarmFragment;
import com.weather.risk.mfi.myfarminfo.mapfragments.NematodeFragment;
import com.weather.risk.mfi.myfarminfo.mapfragments.NewMoistureFragment;
import com.weather.risk.mfi.myfarminfo.mapfragments.NewNDVIFragment;
import com.weather.risk.mfi.myfarminfo.mapfragments.NutritionFragment;
import com.weather.risk.mfi.myfarminfo.fragments.PackagePractices;
import com.weather.risk.mfi.myfarminfo.fragments.SoilDoctor;
import com.weather.risk.mfi.myfarminfo.fragments.WeatherForecast;
import com.weather.risk.mfi.myfarminfo.mapfragments.WeedFragment;
import com.weather.risk.mfi.myfarminfo.pest_disease.ForecastPestFragment;
import com.weather.risk.mfi.myfarminfo.pest_disease.VulnerabilityFragment;
import com.weather.risk.mfi.myfarminfo.utils.AppConstant;
import com.weather.risk.mfi.myfarminfo.utils.UtilFonts;


public class NewDashboardActivity extends AppCompatActivity {


    Double latitude = 0.0;
    Double longitude = 0.0;
    int callingActivity;
    String selectedFarmName;
    String allDrawLatLngPoint;
    String area;
    String stateId;
    DBAdapter db;
    TextView title;
    RelativeLayout backBTN;
    //Add Herojit
    private int REQUEST_CAMERA_START1 = 0, SELECT_FILE_START1 = 1;
    private int REQUEST_CAMERA_START2 = 3, SELECT_FILE_START2 = 2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_dashboard);
        db = new DBAdapter(this);
        db.open();
        backBTN = (RelativeLayout) findViewById(R.id.backbtn);
        title = (TextView) findViewById(R.id.title_main);

        setFontsStyleTxt(this, title, 2);

        callingActivity = getIntent().getIntExtra("calling-activity", 0);
        selectedFarmName = getIntent().getStringExtra("FarmName");
        allDrawLatLngPoint = getIntent().getStringExtra("AllLatLngPount");
        area = getIntent().getStringExtra("Area");
        if (area == null) {
            area = "0";
        }
        Log.v("calling", callingActivity + "");

        switch (callingActivity) {
            case AppConstant.HomeActivity:
                Log.v("calling111", selectedFarmName + "");
                if (selectedFarmName != null) {
                    System.out.println("Selected Farm Name " + selectedFarmName);
                    Cursor cursor = db.getStateFromSelectedFarm(selectedFarmName);
                    if (cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        stateId = cursor.getString(cursor.getColumnIndex(DBAdapter.STATE_ID));
                        allDrawLatLngPoint = cursor.getString(cursor.getColumnIndex(DBAdapter.CONTOUR));
                        area = cursor.getString(cursor.getColumnIndex(DBAdapter.AREA));
                        System.out.println("Navigation drawer activity Area : " + area);
                    }
                    cursor.close();
                }
                break;
            case AppConstant.AddFarmMap: // this will call when you choose farm from the list
                //26.434334196791397,80.34493837505579-26.435601124064195,80.34630931913853-26.434692660565318,80.3474086895585-26.43347706155533,80.3462949022650
                if (allDrawLatLngPoint != null) {
                    String[] landPoints = allDrawLatLngPoint.split("-");
                    System.out.println("allDrawLatLngPoint : " + allDrawLatLngPoint);
                    int midPoint = landPoints.length / 2;
                    String[] latlng = landPoints[midPoint].split(",");

                    if (AppConstant.stateID != null) {
                        stateId = AppConstant.stateID;
                    }

                }
                break;
        }


        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        String sslatitude = AppConstant.latitude;
        String sslongitude = AppConstant.longitude;

        if (sslatitude != null) {
            latitude = Double.parseDouble(sslatitude);
            longitude = Double.parseDouble(sslongitude);
        }
        if (latitude == 0.0) {
            latitude = LatLonCellID.lat;
            longitude = LatLonCellID.lon;
        }

        String fromDraw = getIntent().getStringExtra("from");

        if (fromDraw != null && fromDraw.equalsIgnoreCase("weather")) {
//            title.setText("Weather Forecast");
            setDynamicLanguage(this, title, "WeatherForecast", R.string.WeatherForecast);
            Fragment fragment = new WeatherForecast("" + latitude, "" + longitude);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
        }/*else if (selectedFarmName != null || fromDraw != null) {

            String allpoints = getIntent().getStringExtra("edit_allpoints");
            String editedArea = getIntent().getStringExtra("edit_area");
            if (allpoints != null && allpoints.length() > 4) {
                Log.v("editedfarm", "edited");
                Fragment fragment = LocateYoutFarmFragment.newInstance(String.valueOf(callingActivity), selectedFarmName, allpoints, editedArea);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
            } else {
                Log.v("editedfarm", "non_edited");
                Fragment fragment = LocateYoutFarmFragment.newInstance(String.valueOf(callingActivity), selectedFarmName, allDrawLatLngPoint, area);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
            }
        }*/ else if (allDrawLatLngPoint != null && allDrawLatLngPoint.length() > 10) {

            setDynamicLanguage(this, title, "FarmInformation", R.string.FarmInformation);
            //Add New Add
            Fragment fragment = LocateYoutFarmFragment.newInstance(String.valueOf(callingActivity), selectedFarmName, allDrawLatLngPoint, area);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
        } else if (fromDraw != null && fromDraw.equalsIgnoreCase("ndvi")) {
            setDynamicLanguage(this, title, "NDVIData", R.string.NDVIData);
            Fragment fragment = new NewNDVIFragment(null);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).addToBackStack("dash").commit();

        } else if (fromDraw != null && fromDraw.equalsIgnoreCase("soilm")) {
            setDynamicLanguage(this, title, "SoilMoisture", R.string.SoilMoisture);
            Fragment fragment = new NewMoistureFragment(null);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).addToBackStack("dash").commit();

        } else if (fromDraw != null && fromDraw.equalsIgnoreCase("pop")) {
            setDynamicLanguage(this, title, "CropAdvisory", R.string.CropAdvisory);
            //Herojit Add
            String SowDate = "", OnlyCurrentStatus = "", SelectedCropID = "";
            try {
                SowDate = getIntent().getStringExtra("SowDate");
                OnlyCurrentStatus = getIntent().getStringExtra("OnlyCurrentStatus");
                SelectedCropID = getIntent().getStringExtra("SelectedCropID");
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            Bundle bundle = new Bundle();
            bundle.putString("SowDate", SowDate);
            bundle.putString("OnlyCurrentStatus", OnlyCurrentStatus);
            bundle.putString("SelectedCropID", SelectedCropID);
            Fragment fragment = new PackagePractices();
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
        } else if (fromDraw != null && fromDraw.equalsIgnoreCase("soil")) {
            setDynamicLanguage(this, title, "SoilInformation", R.string.SoilInformation);
            Fragment fragment = new SoilDoctor();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
        } else if (fromDraw != null && fromDraw.equalsIgnoreCase("vul")) {
            setDynamicLanguage(this, title, "Vulnerability", R.string.Vulnerability);
            Fragment fragment = new VulnerabilityFragment("" + latitude, "" + longitude);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
        } else if (fromDraw != null && fromDraw.equalsIgnoreCase("fore")) {
            setDynamicLanguage(this, title, "DiseaseForecast", R.string.DiseaseForecast);
            Fragment fragment = new ForecastPestFragment("" + latitude, "" + longitude);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
        } else if (fromDraw != null && fromDraw.equalsIgnoreCase("visit")) {
            setDynamicLanguage(this, title, "VisitActivity", R.string.VisitActivity);
            Fragment fragment = new VisitFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
        } else if (fromDraw != null && fromDraw.equalsIgnoreCase("visit_summary")) {
            setDynamicLanguage(this, title, "VisitSummary", R.string.VisitSummary);
            Fragment fragment = new VisitReportFragment("" + latitude, "" + longitude);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
        } else if (fromDraw != null && fromDraw.equalsIgnoreCase("feasibility")) {
            setDynamicLanguage(this, title, "CropFeasibility", R.string.CropFeasibility);
            Fragment fragment = new CropFeasibilityFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
        } else if (fromDraw != null && fromDraw.equalsIgnoreCase("crop_secure")) {
            setDynamicLanguage(this, title, "CropSecure", R.string.CropSecure);
            Fragment fragment = new CropSecureFragment("" + latitude, "" + longitude);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
        } else if (fromDraw != null && fromDraw.equalsIgnoreCase("weed")) {
            setDynamicLanguage(this, title, "WeedManagement", R.string.WeedManagement);
            Fragment fragment = new WeedFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
        } else if (fromDraw != null && fromDraw.equalsIgnoreCase("nutri")) {
            setDynamicLanguage(this, title, "Nutrition", R.string.Nutrition);
            Fragment fragment = new NutritionFragment("" + latitude, "" + longitude);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
        } else if (fromDraw != null && fromDraw.equalsIgnoreCase("nematode")) {
            setDynamicLanguage(this, title, "Nematode", R.string.Nematode);
            Fragment fragment = new NematodeFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }




}
