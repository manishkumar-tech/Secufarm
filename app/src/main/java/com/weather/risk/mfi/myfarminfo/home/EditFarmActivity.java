package com.weather.risk.mfi.myfarminfo.home;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.maps.android.SphericalUtil;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.activities.AdminDashboard_New;
import com.weather.risk.mfi.myfarminfo.activities.CustomSearchableSpinner;
import com.weather.risk.mfi.myfarminfo.activities.NewDashboardActivity;
import com.weather.risk.mfi.myfarminfo.database.DBAdapter;
import com.weather.risk.mfi.myfarminfo.entities.AllFarmDetail;
import com.weather.risk.mfi.myfarminfo.entities.CropQueryData;
import com.weather.risk.mfi.myfarminfo.mapfragments.LatLonCellID;
import com.weather.risk.mfi.myfarminfo.marketplace.AddFarmOnMap_New;
import com.weather.risk.mfi.myfarminfo.utils.AppConstant;
import com.weather.risk.mfi.myfarminfo.utils.AppManager;
import com.weather.risk.mfi.myfarminfo.utils.TransparentProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static com.weather.risk.mfi.myfarminfo.customcamera.CameraSurfaceView.IMAGE_DIRECTORY;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getDynamicLanguageToast;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getDynamicLanguageValue;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setCustomSearchableSpinner;

public class EditFarmActivity extends AppCompatActivity {
    ArrayList<String> str = new ArrayList<String>();
    ArrayList<String> strFarmId = new ArrayList<String>();
    Boolean mDrawPolygon = false;
    PolygonOptions rectOptions;
    ArrayList<LatLng> arrayPoints = new ArrayList<LatLng>();
    Double areadOfPolygon = null;
    String allPoints = "";
    int flagForDraw = 0;
    ArrayList<LatLng> points1 = null;
    Button saveBTN;
    Button createMap;
    Button clearMap;
    ImageView editBTN;
    LinearLayout clearLay;
    Boolean isEdited = false;
    CustomSearchableSpinner chooseYourFarmSpiner;
    DBAdapter db;
    ArrayList<LatLng> points = null;
    String latitude, longitude, farmID, contourr;
    int selectecposition = 0;
    TextView txt_farmname;
    Button btn_cross, btn_icon_details;
    private GoogleMap mMap;

    Button printimageview, btn_cross_ss;
    TextView txt_projectname, txt_AadharNo, txt_FarmNames, txt_FarmerName, txt_MobileNumber, txt_FatherName, txt_StateName,
            txt_DistrictName, txt_SubDistrictName, txt_VillageName, txt_OtherVillageName, txt_TaggedFarmArea, txt_ActualFarmArea,
            txt_Latitude, txt_Longitude, txt_Contourlatlong, txt_SowingDate, txt_Registrationdate, txt_Crops, txt_Variety, txt_NoofBags;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selected_farm);

        str = new ArrayList<String>();
        strFarmId = new ArrayList<String>();
        chooseYourFarmSpiner = (CustomSearchableSpinner) findViewById(R.id.header_chooseFarmSpinner);
        db = new DBAdapter(this);
//        getAllFarmName();


        rectOptions = new PolygonOptions();

        arrayPoints = new ArrayList<LatLng>();
        points = new ArrayList<LatLng>();


        editBTN = (ImageView) findViewById(R.id.edit_map);
        saveBTN = (Button) findViewById(R.id.submit_selected_farm);
        btn_cross = (Button) findViewById(R.id.btn_cross);
        btn_icon_details = (Button) findViewById(R.id.btn_icon_details);
        txt_farmname = (TextView) findViewById(R.id.txt_farmname);


        createMap = (Button) findViewById(R.id.create_btn);
        clearLay = (LinearLayout) findViewById(R.id.clear_lay);
        clearMap = (Button) findViewById(R.id.clear_btn);

        clearMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMap != null) {
                    mMap.clear();
                    if (latitude != null && longitude != null) {
                        Double ddd = Double.valueOf(latitude);
                        Double lll = Double.valueOf(longitude);
                        rectOptions = new PolygonOptions();
                        arrayPoints = new ArrayList<LatLng>();
                        allPoints = "";
                        flagForDraw = 0;
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(ddd, lll), 17.0f));
                        mDrawPolygon = true;
                    }
                    isEdited = false;
                    saveBTN.setVisibility(View.GONE);

                }
            }
        });
        createMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (arrayPoints.size() > 2) {
                    rectOptions.fillColor(Color.GRAY);
                    rectOptions.strokeWidth(5);
                    mMap.addPolygon(rectOptions);
                    // allPoints = allPoints.substring(1, allPoints.length() - 1);
                    Log.d("allPoints", allPoints);
                    areadOfPolygon = SphericalUtil.computeArea(arrayPoints);

                    if (areadOfPolygon != null) {

                        Double acreageArea = (0.000247105) * areadOfPolygon;
                        String ss = null;
                        if (acreageArea != null) {
                            ss = String.format("%.2f", acreageArea);
                        }

                        Log.v("area", ss + "--" + allPoints);
                        isEdited = true;
                        saveBTN.setVisibility(View.VISIBLE);
                    }

                } else {
                    getDynamicLanguageToast(getApplicationContext(), "Pleaseclickatleast", R.string.Pleaseclickatleast);
                }
            }
        });

        editBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMap != null) {
                    mMap.clear();
                    if (latitude != null && longitude != null) {
                        Double ddd = Double.valueOf(latitude);
                        Double lll = Double.valueOf(longitude);
                        rectOptions = new PolygonOptions();
                        arrayPoints = new ArrayList<LatLng>();
                        allPoints = "";
                        flagForDraw = 0;
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(ddd, lll), 17.0f));
                        mDrawPolygon = true;
                    }
                }
            }
        });
        btn_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_icon_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FarmdetailsAsynctask().execute();
            }
        });
        saveBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEdited) {
                    String ss = null;
                    Intent intent = new Intent(getApplicationContext(), NewDashboardActivity.class);
                    intent.putExtra("calling-activity", AppConstant.HomeActivity);
                    intent.putExtra("FarmName", farmID);
                    if (areadOfPolygon != null) {
                        Double acreageArea = (0.000247105) * areadOfPolygon;
                        if (acreageArea != null) {
                            ss = String.format("%.2f", acreageArea);
                        }
                        Log.v("area", ss + "--" + allPoints);
                    }
                    if (allPoints != null && allPoints.length() > 2) {
                        if (allPoints.substring(0, 1).equals("-")) {
                            allPoints = allPoints.substring(1, allPoints.length() - 1); // this will remove dess(-) from string at starting point
                        }
                    } else {
                        allPoints = "";
                    }
                    intent.putExtra("edit_allpoints", allPoints);
                    intent.putExtra("edit_area", ss);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(getApplicationContext(), NewDashboardActivity.class);
                    intent.putExtra("calling-activity", AppConstant.HomeActivity);
                    intent.putExtra("FarmName", farmID);
                    startActivity(intent);
                    finish();
                }
            }
        });

        points1 = new ArrayList<LatLng>();
        setUpMapIfNeeded(); // For setting up the MapFragment


        if (AppConstant.farm_id != null) {
            setGeoMap();
        }
        Bundle bundle = getIntent().getExtras();
        try {
            if (bundle != null) {
                String ActivityName = bundle.getString("ActivityName");
                if (ActivityName != null && ActivityName.length() > 0 &&
                        ActivityName.equalsIgnoreCase("PolicyRegistrationNew")) {
                    btn_icon_details.setVisibility(View.GONE);
                } else {
                    btn_icon_details.setVisibility(View.VISIBLE);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getAllFarmName() {


        System.out.println("getAllFarmCalled");
        str = new ArrayList<String>();
        strFarmId = new ArrayList<String>();

        if (AppConstant.userTypeID != null && !AppConstant.userTypeID.equalsIgnoreCase("3")) {
            str.add(getDynamicLanguageValue(getApplicationContext(), "Select", R.string.Select));
            strFarmId.add("0");
        }

        db.open();
        Cursor c = db.getallFarmName(AppConstant.user_id);
        int position = 0;
        if (c.moveToFirst()) {
            do {
                str.add(c.getString(0).toString());
                strFarmId.add(c.getString(1).toString());
                if (AppConstant.farm_id.equalsIgnoreCase(c.getString(1).toString())) {
                    selectecposition = position;//23354 22803
                }
                position++;
            } while (c.moveToNext());
        }
        db.close();

        ArrayAdapter<String> chooseYourFarmAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, str);
        chooseYourFarmAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.chooseYourFarmSpiner.setAdapter(chooseYourFarmAdapter);
        setCustomSearchableSpinner(getApplicationContext(), chooseYourFarmSpiner, "SelectFarm", R.string.SelectFarm);


        String compareValue = AppConstant.selectedFarm;
//        if (compareValue != null && compareValue.length() > 2) {
//            // farmName.setText(compareValue);
//            int spinnerPosition = chooseYourFarmAdapter.getPosition(compareValue);
//            chooseYourFarmSpiner.setSelection(spinnerPosition);
//        }
        chooseYourFarmSpiner.setSelection(selectecposition);

    }

    private void getLatLongList(String point, String latit, String longit) {
        points1 = new ArrayList<LatLng>();
        List<String> l_List = Arrays.asList(point.split("-"));
        for (int j = 0; j < l_List.size(); j++) {
            String currentString = l_List.get(j);
            if (currentString != null) {
                String[] separated = currentString.split(",");
                if (separated.length > 1) {
                    String la = separated[0];
                    String lo = separated[1];
                    points1.add(new LatLng(Double.valueOf(la), Double.valueOf(lo)));
                }
            }
        }
        if (latit != null) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(latit), Double.parseDouble(longit)), 17.0f));
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);


        } else {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(LatLonCellID.lat, LatLonCellID.lon), 14.0f));
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        }
        if (mMap != null) {
            mMap.clear();
            setUpMap();
        }
    }


    /*****
     * Sets up the map if it is possible to do so
     *****/
    public void setUpMapIfNeeded() {
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_frag)).getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    mMap = googleMap;
                    setUpMap();

                    if (contourr != null) {
                        points = new ArrayList<LatLng>();
                        getLatLongList(contourr, latitude, longitude);
                    } else {
                        if (mMap != null) {
                            mMap.clear();
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(LatLonCellID.lat, LatLonCellID.lon), 13.0f));

                        }
                    }

                    mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {


                        @Override
                        public void onMapClick(LatLng point) {
                            if (mDrawPolygon) { //if the value is true then draw polygon. value will be true from drawPolygon click event

                                // TODO Auto-generated method stub
                                //  arrayPoints.add(new LatLng(point.latitude, point.longitude));
                                arrayPoints.add(point);
                                Log.d("arg0----------", point.latitude + "***************" + point.longitude);
                                //STORE ALL POINT HERE THAT FURTHER SEND FOR SERVER REQUEST WITH FARM INFORMATIOS
                                allPoints = allPoints + "-" + point.latitude + "," + point.longitude;
                                Log.d("allpoints-------", allPoints);
                                MarkerOptions markerOptions = new MarkerOptions();
                                markerOptions.position(point);
                                markerOptions.title("Position");
//                    BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.dot_marker);
                                BitmapDescriptor icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
                                markerOptions.icon(icon);
                       /* Double lat = point.latitude;
                        Double log = point.longitude;
                        //below lat lng point wil use in createdStringforFieldInprovemnt() method fo farminformation class
                        AppConstant.latitude = String.valueOf(point.latitude);
                        AppConstant.longitude = String.valueOf(point.longitude);*/
                                rectOptions.add(point);
                                mMap.addMarker(markerOptions);

                                flagForDraw++;

                                if (flagForDraw > 2) {

                                    clearLay.setVisibility(View.VISIBLE);
                                } else {
                                    clearLay.setVisibility(View.GONE);
                                }
                            }
                        }
                    });
                }
            });

        }
    }

    private void setUpMap() {
        // For showing a move to my loction button
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        // For dropping a marker at a point on the
        if (points1.size() > 2) {
            drawCircle();
            Log.v("asklcjlks", "sdkasmkld");
        }
    }

    public void drawCircle() {
        // Clear the map to remove the previous circle
        mMap.clear();
        // Generate the points
        mMap.addPolygon(new PolygonOptions().addAll(points1).strokeWidth(4).strokeColor(Color.RED).fillColor(Color.TRANSPARENT));
        // Create and return the polygon
    }


    public void setGeoMap() {

        String name = AppConstant.selected_farm;
        String idddd = AppConstant.farm_id;

        String contour = null;
        String lat = null;
        String lon = null;

        if (name != null)
            txt_farmname.setText(name);
        if ((AppConstant.userTypeID != null && !AppConstant.userTypeID.equalsIgnoreCase("3"))) {

            db.open();
            Cursor c = db.getStateFromSelectedFarm(idddd);
            Log.v("farmID", c.getCount() + "");
            if (c.moveToFirst()) {
                do {
                    Log.v("farmID", AppConstant.farm_id + "vishal");
                    AppConstant.farm_id = c.getString(c.getColumnIndex(DBAdapter.FARM_ID));

                    Log.v("farmID", AppConstant.farm_id + "vishal");

                    AppConstant.stateID = c.getString(c.getColumnIndex(DBAdapter.STATE_ID));
                    contour = c.getString(c.getColumnIndex(DBAdapter.CONTOUR));

                    if (contour != null && contour.length() > 10) {

                        lat = c.getString(c.getColumnIndex(DBAdapter.CENTRE_LAT));
                        lon = c.getString(c.getColumnIndex(DBAdapter.CENTRE_LON));
                        AppConstant.latitude = lat;
                        AppConstant.longitude = lon;
                        if (AppConstant.longitude != null) {
                            latitude = AppConstant.latitude;
                            longitude = AppConstant.longitude;
                        }

                        AppConstant.contour = contour;
                        AppConstant.farmName = idddd;
                        AppConstant.isFarm = "yes";

                        farmID = idddd;
                        contourr = contour;
                        if (AppConstant.farm_id != null) {

                            String farmId = AppConstant.farm_id;
                            Cursor getCropQuery = db.getCropQueryByFarmId(farmId);
                            if (c.moveToFirst()) {
                                do {
                                    if (getCropQuery.moveToFirst()) {

                                        AppConstant.selected_cropId = getCropQuery.getString(getCropQuery.getColumnIndex(DBAdapter.CROP_ID));
                                        AppConstant.selected_variety = getCropQuery.getString(getCropQuery.getColumnIndex(DBAdapter.CROPS_VARIETY));
                                        //  AppConstant.selected_crop = getCropQuery.getString(getCropQuery.getColumnIndex(DBAdapter.CROP));
                                        Log.v("cropIddddd", AppConstant.selected_cropId + "--" + AppConstant.selected_variety);

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

//                        getLatLongList(contour, latitude, longitude);

                    } else {
                        new getFarmDetailAsyncTask(AppConstant.farm_id).execute();
                    }
                }
                while (c.moveToNext());
            }
            db.close();

        } else {

            db.open();

            farmID = idddd;
            contourr = contour;

            Cursor c = db.getStateFromSelectedFarm(idddd);
            Log.v("farmID", c.getCount() + "");
            if (c.moveToFirst()) {
                do {
                    Log.v("farmID", AppConstant.farm_id + "vishal");
                    AppConstant.farm_id = c.getString(c.getColumnIndex(DBAdapter.FARM_ID));

                    Log.v("farmID", AppConstant.farm_id + "vishal");

                    AppConstant.stateID = c.getString(c.getColumnIndex(DBAdapter.STATE_ID));
                    contour = c.getString(c.getColumnIndex(DBAdapter.CONTOUR));

                    if (contour != null && contour.length() > 10) {

                        lat = c.getString(c.getColumnIndex(DBAdapter.CENTRE_LAT));
                        lon = c.getString(c.getColumnIndex(DBAdapter.CENTRE_LON));
                        AppConstant.latitude = lat;
                        AppConstant.longitude = lon;
                        if (AppConstant.longitude != null) {
                            latitude = AppConstant.latitude;
                            longitude = AppConstant.longitude;
                        }

                        AppConstant.contour = contour;
                        AppConstant.farmName = idddd;
                        AppConstant.isFarm = "yes";

                        if (AppConstant.farm_id != null) {

                            String farmId = AppConstant.farm_id;
                            Cursor getCropQuery = db.getCropQueryByFarmId(farmId);
                            if (c.moveToFirst()) {
                                do {
                                    if (getCropQuery.moveToFirst()) {

                                        AppConstant.selected_cropId = getCropQuery.getString(getCropQuery.getColumnIndex(DBAdapter.CROP_ID));
                                        AppConstant.selected_variety = getCropQuery.getString(getCropQuery.getColumnIndex(DBAdapter.CROPS_VARIETY));
                                        //   AppConstant.selected_crop = getCropQuery.getString(getCropQuery.getColumnIndex(DBAdapter.CROP));

                                        Log.v("cropIddddd", AppConstant.selected_cropId + "--" + AppConstant.selected_variety);

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


                    } else {
                        new getFarmDetailAsyncTask(AppConstant.farm_id).execute();
                    }
                }
                while (c.moveToNext());
            }
            db.close();
            //  farmMapMethod();
        }
    }

    private class getFarmDetailAsyncTask extends AsyncTask<Void, Void, String> {
        String f_id;
        String result = "";
        TransparentProgressDialog progressDialog;

        public getFarmDetailAsyncTask(String id) {
            //  this.data = data;
            f_id = id;
            farmID = id;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new TransparentProgressDialog(
                    EditFarmActivity.this, getResources().getString(R.string.Dataisloading));
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
            String sendRequest = null;
            try {
                sendRequest = AppManager.getInstance().getFarmDetail + "/" + AppConstant.user_id + "/" + f_id;
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
                        //   gotoHomeScreen();

                    } else {
                        AllFarmDetail addFarmDetail;

                        db.open();
                        //  db.deleteAllFarmDetailTable();
                        System.out.println("farm detail response " + response);

                        JSONObject jsonObject = new JSONObject(AppManager.getInstance().placeSpaceIntoString(response));
                        //  if (jArray.length() > 0) {
                        int deleteCount = db.db.delete(DBAdapter.DATABASE_TABLE_ALL_FARM_DETAIL, DBAdapter.FARM_ID + " = '" + f_id + "'", null);
                        int deleteCount1 = db.db.delete(DBAdapter.TABLE_QUERY_CROP, DBAdapter.FARM_ID + " = '" + f_id + "'", null);
                        System.out.println("deleteCount : " + deleteCount + " deleteCount1 : " + deleteCount1);
                        //   }
                        //   for (int i = 0; i < jArray.length(); i++) {


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
                                data.setSowPeriodTo(cropJsonObject.isNull("CropTo") ? "" : cropJsonObject.getString("CropTo"));
                                data.setSowPeriodForm(cropJsonObject.isNull("SowDate") ? "" : cropJsonObject.getString("SowDate"));
                                data.setOtherNutrition(cropJsonObject.isNull("OtherNutrient") ? "" : cropJsonObject.getString("OtherNutrient"));
                                data.setBesalDoseApply(cropJsonObject.isNull("BasalDoseApply") ? "" : cropJsonObject.getString("BasalDoseApply"));
                                long inserted = data.insert(db, DBAdapter.SENT);
                                System.out.println("database return value=" + l);
                            }
                        }

                        //   }


                        String contour = null;
                        String lat = null;
                        String lon = null;
                        String name = AppConstant.selectedFarm;

                        Cursor c = db.getStateFromSelectedFarm(farmId);
                        if (c.moveToFirst()) {
                            do {
                                AppConstant.farm_id = c.getString(c.getColumnIndex(DBAdapter.FARM_ID));
                                AppConstant.stateID = c.getString(c.getColumnIndex(DBAdapter.STATE_ID));
                                contour = c.getString(c.getColumnIndex(DBAdapter.CONTOUR));

                                if (contour != null && contour.length() > 10) {

                                    lat = c.getString(c.getColumnIndex(DBAdapter.CENTRE_LAT));
                                    lon = c.getString(c.getColumnIndex(DBAdapter.CENTRE_LON));
                                    AppConstant.latitude = lat;
                                    AppConstant.longitude = lon;
                                    if (AppConstant.longitude != null) {
                                        latitude = AppConstant.latitude;
                                        longitude = AppConstant.longitude;
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

                                                    Log.v("cropIddddd", AppConstant.selected_cropId + "--" + AppConstant.selected_variety);

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
//                                    getLatLongList(contour, latitude, longitude);
                                }
                            }
                            while (c.moveToNext());
                        }
                        db.close();
                        //   loadProfileData(f_id);


                        // gotoHomeScreen();
                    }
                } else {
                    getDynamicLanguageToast(getApplicationContext(), "Couldnotconnect", R.string.Couldnotconnect);
                }
            } catch (JSONException e) {
                e.printStackTrace();

                System.out.println("catch block Pls Try again");
            }

            progressDialog.dismiss();

        }
    }

    //Farm details show
    public void GeoFarmDetailsShow(String response) {

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
        dialog.setContentView(R.layout.geotagfarming);

        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);

        printimageview = (Button) dialog.findViewById(R.id.printimageview);
        btn_cross_ss = (Button) dialog.findViewById(R.id.btn_cross);
        txt_projectname = (TextView) dialog.findViewById(R.id.txt_projectname);
        txt_AadharNo = (TextView) dialog.findViewById(R.id.txt_AadharNo);
        txt_FarmNames = (TextView) dialog.findViewById(R.id.txt_FarmNames);
        txt_FarmerName = (TextView) dialog.findViewById(R.id.txt_FarmerName);
        txt_MobileNumber = (TextView) dialog.findViewById(R.id.txt_MobileNumber);
        txt_FatherName = (TextView) dialog.findViewById(R.id.txt_FatherName);
        txt_StateName = (TextView) dialog.findViewById(R.id.txt_StateName);
        txt_DistrictName = (TextView) dialog.findViewById(R.id.txt_DistrictName);
        txt_SubDistrictName = (TextView) dialog.findViewById(R.id.txt_SubDistrictName);
        txt_VillageName = (TextView) dialog.findViewById(R.id.txt_VillageName);
        txt_OtherVillageName = (TextView) dialog.findViewById(R.id.txt_OtherVillageName);
        txt_TaggedFarmArea = (TextView) dialog.findViewById(R.id.txt_TaggedFarmArea);
        txt_ActualFarmArea = (TextView) dialog.findViewById(R.id.txt_ActualFarmArea);
        txt_Latitude = (TextView) dialog.findViewById(R.id.txt_Latitude);
        txt_Longitude = (TextView) dialog.findViewById(R.id.txt_Longitude);
        txt_Contourlatlong = (TextView) dialog.findViewById(R.id.txt_Contourlatlong);
        txt_SowingDate = (TextView) dialog.findViewById(R.id.txt_SowingDate);
        txt_Registrationdate = (TextView) dialog.findViewById(R.id.txt_Registrationdate);
        txt_Crops = (TextView) dialog.findViewById(R.id.txt_Crops);
        txt_Variety = (TextView) dialog.findViewById(R.id.txt_Variety);
        txt_NoofBags = (TextView) dialog.findViewById(R.id.txt_NoofBags);

        try {
            JSONObject obj = new JSONObject(response);
            String ProjectID = obj.getString("ProjectID");
            db.open();
            String SQL = "";
            ArrayList<HashMap<String, String>> hasmap = new ArrayList<>();
            SQL = "Select ProjectName from " + db.TABLE_Projectlist + " where ProjectID='" + ProjectID + "'";
            hasmap = db.getDynamicTableValue(SQL);

            if (hasmap != null && hasmap.size() > 0) {
                String projectname = hasmap.get(0).get("ProjectName");
                txt_projectname.setText(setString(projectname));
            }

            txt_AadharNo.setText(setString(obj.getString("AadharNo")));
            txt_FarmNames.setText(setString(obj.getString("FarmName")));
            txt_FarmerName.setText(setString(obj.getString("FarmerName")));
            txt_MobileNumber.setText(setString(obj.getString("PhoneNo")));
            txt_FatherName.setText(setString(obj.getString("FatherName")));
            txt_StateName.setText(setString(obj.getString("StateName")));


            String StateID = obj.getString("State");
            String DistrictID = obj.getString("DistrictID");
            hasmap = new ArrayList<>();
            SQL = "select DistrictName from MstStateDistrict where StateID='" + StateID + "' and DistrictID='" + DistrictID + "'";
            hasmap = db.getDynamicTableValue(SQL);

            if (hasmap != null && hasmap.size() > 0) {
                String DistrictName = hasmap.get(0).get("DistrictName");
                txt_DistrictName.setText(setString(DistrictName));
            }

            String SubDistrict = obj.getString("Block");
            txt_SubDistrictName.setText(setString(SubDistrict));

            String VillageID = obj.getString("VillageID");

            hasmap = new ArrayList<>();
            SQL = "select DistrictName from MstStateDistrict where StateID='" + StateID + "' and DistrictID='" + DistrictID + "' and VillageID";
            hasmap = db.getDynamicTableValue(SQL);

            if (hasmap != null && hasmap.size() > 0) {
                String DistrictName = hasmap.get(0).get("DistrictName");
                txt_DistrictName.setText(setString(DistrictName));
            }

            String Village = obj.getString("VillageStr");

            txt_VillageName.setText(setString(Village));


            txt_TaggedFarmArea.setText(setString(obj.getString("FarmArea")));
            txt_ActualFarmArea.setText(setString(obj.getString("Area")));
            txt_Latitude.setText(setString(obj.getString("CenterLat")));
            txt_Longitude.setText(setString(obj.getString("CenterLon")));

            String Contour = obj.getString("Contour");
            if (Contour != null && Contour.length() > 0 && Contour.contains("-")) {
                try {
                    String new_Contour[] = Contour.split("-");
                    Contour = "";
                    for (int i = 0; i < new_Contour.length; i++) {
                        if (Contour.equalsIgnoreCase("")) {
                            Contour = new_Contour[i];
                        } else {
                            Contour = Contour + "\n\n" + new_Contour[i];
                        }
                    }
                    Contour.replace(",", "");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            txt_Contourlatlong.setText(setString(Contour));

//            txt_SowingDate.setText(setString(obj.getString("SowingDate")));
            txt_Registrationdate.setText(setString(obj.getString("RegDate")));

            String CropInfo = obj.getString("CropInfo");
            JSONArray aray = new JSONArray(CropInfo);
            JSONObject obj1 = new JSONObject(aray.get(0).toString());
            if (obj1 != null && obj1.length() > 0) {
                txt_Crops.setText(setString(obj1.getString("CropName")));
                txt_Variety.setText(setString(obj1.getString("Variety")));
                txt_SowingDate.setText(setString(obj1.getString("CropFrom")));//Sowing Date
            }

            txt_NoofBags.setText(setString(obj1.getString("NoofBags")));

        } catch (Exception ex) {
            ex.printStackTrace();
        }


        printimageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //fd
                savePdfTable();
            }
        });
        btn_cross_ss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public String setString(String val) {
        if (val != null && val.length() > 0 && !val.equalsIgnoreCase("") && !val.equalsIgnoreCase("null"))
            return val;
        else
            return "";
    }

    private class FarmdetailsAsynctask extends AsyncTask<Void, Void, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(EditFarmActivity.this);
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
                if (response != null && response.contains("FarmID")) {
                    GeoFarmDetailsShow(response);
                }

                progressDialog.dismiss();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    //https://www.mysamplecode.com/2012/10/create-table-pdf-java-and-itext.html
    public void savePdfTable() {
        Document doc = new Document();
        PdfWriter docWriter = null;

        DecimalFormat df = new DecimalFormat("0.00");
        try {
            //special font sizes
            Font bold_XL = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
            Font bold = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD);
            Font normal = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);


            //Current Date
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            System.out.println(dateFormat.format(date)); //2016/11/16 12:08:43
            String CurrentDate = String.valueOf(dateFormat.format(date));
            CurrentDate = CurrentDate.replace("/", "");
            CurrentDate = CurrentDate.replace(":", "").trim();
            //file path
            String mFileName = "MFI_GeoFarmDetails" + CurrentDate;
            String mFilePath = Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY + "/" + mFileName + ".pdf";
            docWriter = PdfWriter.getInstance(doc, new FileOutputStream(mFilePath));
//            String path = "docs/" + "PDFFileNameTest";
//            docWriter = PdfWriter.getInstance(doc, new FileOutputStream(path));

            //document header attributes
            doc.addAuthor("betterThanZero");
            doc.addCreationDate();
            doc.addProducer();
            doc.addCreator("MySampleCode.com");
            doc.addTitle("Report with Column Headings");
            doc.setPageSize(PageSize.LETTER);

            //open document
            doc.open();

            //specify column widths
            float[] columnWidths = {5f, 5f};
            //create PDF table with the given widths
            PdfPTable table = new PdfPTable(columnWidths);
            // set table width a percentage of the page width
            table.setWidthPercentage(90f);

            double orderTotal, total = 0;

            //just some random data to fill

            JSONObject jsonObject = getjsonvaluePDF1();
            if (jsonObject != null && jsonObject.length() > 0) {
                Iterator<String> keysItr = jsonObject.keys();
                while (keysItr.hasNext()) {
                    String getkey = keysItr.next();
                    String getvalue = jsonObject.getString(getkey);
                    insertCell(table, getkey, Element.ALIGN_LEFT, 1, normal);
                    insertCell(table, getvalue, Element.ALIGN_LEFT, 1, normal);
                }
            }
            //create a paragraph
            Paragraph paragraph = new Paragraph();

            Chunk chunk0 = new Chunk(getDynamicLanguageValue(getApplicationContext(), "GeoFarmDetails", R.string.GeoFarmDetails), bold_XL);
            Phrase ph0 = new Phrase(chunk0);


            paragraph.setAlignment(Element.ALIGN_CENTER);

            paragraph.add(ph0);

            //add the PDF table to the paragraph
            // add one empty line
            paragraph.add(table);


            // add the paragraph to the document
            doc.add(paragraph);

            getDynamicLanguageToast(getApplicationContext(), "PDFFilegenerated", R.string.PDFFilegenerated);

        } catch (DocumentException dex) {
            dex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (doc != null) {
                //close the document
                doc.close();
            }
            if (docWriter != null) {
                //close the writer
                docWriter.close();
            }
        }
    }

    public JSONObject getjsonvaluePDF1() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(getDynamicLanguageValue(getApplicationContext(), "Projects", R.string.Projects), txt_projectname.getText().toString());
            jsonObject.put(getDynamicLanguageValue(getApplicationContext(), "AadharNo", R.string.AadharNo), txt_AadharNo.getText().toString());
            jsonObject.put(getDynamicLanguageValue(getApplicationContext(), "FarmNames", R.string.FarmNames), txt_FarmNames.getText().toString());
            jsonObject.put(getDynamicLanguageValue(getApplicationContext(), "FarmerName", R.string.FarmerName), txt_FarmerName.getText().toString());
            jsonObject.put(getDynamicLanguageValue(getApplicationContext(), "MobileNumber", R.string.MobileNumber), txt_MobileNumber.getText().toString());
            jsonObject.put(getDynamicLanguageValue(getApplicationContext(), "FatherName", R.string.FatherName), txt_FatherName.getText().toString());
            jsonObject.put(getDynamicLanguageValue(getApplicationContext(), "StateName", R.string.StateName), txt_StateName.getText().toString());
            jsonObject.put(getDynamicLanguageValue(getApplicationContext(), "DistrictName", R.string.DistrictName), txt_DistrictName.getText().toString());
            jsonObject.put(getDynamicLanguageValue(getApplicationContext(), "SubDistrictName", R.string.SubDistrictName), txt_SubDistrictName.getText().toString());
            jsonObject.put(getDynamicLanguageValue(getApplicationContext(), "VillageName", R.string.VillageName), txt_VillageName.getText().toString());
//            jsonObject.put(getResources().getString(R.string.OtherVillageName), txt_OtherVillageName.getText().toString());
            jsonObject.put(getDynamicLanguageValue(getApplicationContext(), "TaggedFarmArea", R.string.TaggedFarmArea), txt_TaggedFarmArea.getText().toString());
            jsonObject.put(getDynamicLanguageValue(getApplicationContext(), "ActualFarmArea", R.string.ActualFarmArea), txt_ActualFarmArea.getText().toString());
            jsonObject.put(getDynamicLanguageValue(getApplicationContext(), "Latitude", R.string.Latitude), txt_Latitude.getText().toString());
            jsonObject.put(getDynamicLanguageValue(getApplicationContext(), "Longitude", R.string.Longitude), txt_Longitude.getText().toString());
            jsonObject.put(getDynamicLanguageValue(getApplicationContext(), "Contourlatlong", R.string.Contourlatlong), txt_Contourlatlong.getText().toString());
            jsonObject.put(getDynamicLanguageValue(getApplicationContext(), "SowingDate", R.string.SowingDate), txt_SowingDate.getText().toString());
            jsonObject.put(getDynamicLanguageValue(getApplicationContext(), "Registrationdate", R.string.Registrationdate), txt_Registrationdate.getText().toString());
            jsonObject.put(getDynamicLanguageValue(getApplicationContext(), "Crops", R.string.Crops), txt_Crops.getText().toString());
            jsonObject.put(getDynamicLanguageValue(getApplicationContext(), "Varieties", R.string.Varieties), txt_Variety.getText().toString());
            jsonObject.put(getDynamicLanguageValue(getApplicationContext(), "NoofBags", R.string.NoofBags), txt_NoofBags.getText().toString());


        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return jsonObject;
    }

    private void insertCell(PdfPTable table, String text, int align, int colspan, Font font) {

        //create a new cell with the specified Text and Font
        PdfPCell cell = new PdfPCell(new Phrase(text.trim(), font));
        //set the cell alignment
        cell.setHorizontalAlignment(align);
        //set the cell column span in case you want to merge two or more cells
        cell.setColspan(colspan);
        //in case there is no text and you wan to create an empty row
        if (text.trim().equalsIgnoreCase("")) {
            cell.setMinimumHeight(10f);
        }
        //add the call to the table
        table.addCell(cell);

    }


}
