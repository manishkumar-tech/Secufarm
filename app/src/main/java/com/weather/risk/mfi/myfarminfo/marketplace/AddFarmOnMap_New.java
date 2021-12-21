package com.weather.risk.mfi.myfarminfo.marketplace;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;
import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.activities.AdminDashboard_New;
import com.weather.risk.mfi.myfarminfo.activities.CustomSearchableSpinner;
import com.weather.risk.mfi.myfarminfo.activities.NewDashboardActivity;
import com.weather.risk.mfi.myfarminfo.database.DBAdapter;
import com.weather.risk.mfi.myfarminfo.entities.LocationData;
import com.weather.risk.mfi.myfarminfo.mapfragments.LatLonCellID;
import com.weather.risk.mfi.myfarminfo.utils.AppConstant;
import com.weather.risk.mfi.myfarminfo.utils.AppManager;
import com.weather.risk.mfi.myfarminfo.utils.PlaceDetailsJSONParser;
import com.weather.risk.mfi.myfarminfo.utils.PlaceJSONParser;
import com.weather.risk.mfi.myfarminfo.utils.TransparentProgressDialog;
import com.weather.risk.mfi.myfarminfo.utils.UtilFonts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import pl.droidsonroids.gif.GifTextView;

import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.FarmEditActive;
import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.FarmEditDetails;
import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.SN_AddFarmOnMap_New;
import static com.weather.risk.mfi.myfarminfo.utils.AppManager.NOGPSDialog;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getDynamicLanguageToast;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getDynamicLanguageValue;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getUIDforScreenTracking;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setCustomSearchableSpinner;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setDynamicLanguage;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setFontsStyle;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setFontsStyleTxt;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setScreenTracking;

public class AddFarmOnMap_New extends AppCompatActivity implements OnMapReadyCallback/*, LocationListener */ {

    GoogleMap map;
    SupportMapFragment supportMapFragment;
    Button fltbtn_geoTag, fltbtn_editfarm, gotoHomeBtn, productPageBtn;
    //    Polyline polyline = null;
    Polygon polygon = null;
    Polyline polyline = null;
    //    PolylineOptions polylineOptions = null;
    ArrayList<LatLng> latLngList = new ArrayList<>();
    ArrayList<Marker> markerList = new ArrayList<>();
    String allPoints = "", latitude, longitude;
    Double lat = 0.0, log = 0.0;
    Boolean mDrawPolygon = false;

//    PolygonOptions polygonOptions = null;

    TextView text_geotag_heading, txt_area, txt_area_geotag, alertTitle;
    ImageView img_clear, img_back, img_delete, img_delete_geotag, img_savenext, img_search, img_start, img_stop, img_savenext_geotag;
    TableRow tab_search, tab_geotag, tab_geotagging;
    int selected = 0, MULTIPLE_PERMISSIONS = 7;
    final int PLACES = 0, PLACES_DETAILS = 1;
    AutoCompleteTextView txt_searchlocation;
    PlacesTask placesTask;
    ParserTask parserTask, placesParserTask, placeDetailsParserTask;
    TransparentProgressDialog pDialog;
    LocationData locationData;
    DownloadTask placeDetailsDownloadTask;
    String[] permissions = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.SEND_SMS,
            Manifest.permission.CAMERA
    };
    Timer timer;
    TimerTask timerTask;
    final Handler mHandler = new Handler();
    private boolean stopAlert = false;

    DBAdapter db;
    String UID = "";

    ArrayList<String> str = new ArrayList<String>();
    ArrayList<String> strFarmId = new ArrayList<String>();
    String SelectFarmID = "", SelectedFarm = "";


    String balanceAmount = null, farmerId = null, projectId = null;
    String farm_id = null;

    ArrayList<String> farmIdlist = new ArrayList<String>();
    Button btn_cross;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addfarmonmap_new);

        balanceAmount = getIntent().getStringExtra("balance");
        farmerId = getIntent().getStringExtra("farmerId");
        projectId = getIntent().getStringExtra("project_id");

        farm_id = getIntent().getStringExtra("farm_id");

        if (farm_id != null && !farm_id.equalsIgnoreCase("null") && farm_id.length() > 0) {
            String[] split = farm_id.split("=");
            farmIdlist = new ArrayList<String>();
            if (split.length > 0) {
                farmIdlist.addAll(Arrays.asList(split));
            }
        }

        setInitialazation();
        AppConstant.isWrite = false;
        if (!AppManager.getInstance().isLocationServicesAvailable(this)) {
            NOGPSDialog(this);
        }
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            NOGPSDialog(this);
        }
        checkPermissions();
        db = new DBAdapter(this);
        db.open();

        Intent intent = getIntent();
        latitude = intent.getStringExtra("lat");
        longitude = intent.getStringExtra("log");
        if (latitude != null) {
            lat = Double.parseDouble(latitude);
            log = Double.parseDouble(longitude);
        } else {
            lat = LatLonCellID.lat;
            log = LatLonCellID.lon;
        }
        TableRow bottom_lay = (TableRow) findViewById(R.id.bottom_lay);
        btn_cross = (Button) findViewById(R.id.btn_cross);
        btn_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        try {
            String ActivityName = intent.getStringExtra("ActivityName");
            if (ActivityName != null && ActivityName.equalsIgnoreCase("MainProfileActivity")) {
                bottom_lay.setVisibility(View.GONE);
            } else {
                bottom_lay.setVisibility(View.VISIBLE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);

        setVisible(3);
        selected = 0;
        txt_searchlocation.setThreshold(1);


        img_savenext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentCall();
            }
        });



        img_savenext_geotag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentCall();
            }
        });


        fltbtn_editfarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImagePopup();
            }
        });


        fltbtn_geoTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FarmEditActive = false;
                GeoFarmingPopup();
            }
        });

        img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDeleteClear("delete");
            }
        });
        img_delete_geotag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDeleteClear("delete");
            }
        });
        img_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDeleteClear("clear");
            }
        });
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (latLngList.size() > 1) {
                    addPolygonValues(null, 2);
                } else {
                    if (latLngList.size() == 1) {
                        map.clear();
                    }
                    getDynamicLanguageToast(getApplicationContext(), "Pleaseclickatleast", R.string.Pleaseclickatleast);
                }

            }
        });
        img_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setVisible(1);
            }
        });
        img_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_start.setVisibility(View.GONE);
                img_stop.setVisibility(View.VISIBLE);
                startTimer();
            }
        });
        img_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_start.setVisibility(View.VISIBLE);
                img_stop.setVisibility(View.GONE);
                stopTimertask();
            }
        });
        txt_searchlocation.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 4) {
                    placesTask = new PlacesTask();
                    placesTask.execute(s.toString());
                    if (AppConstant.APP_MODE != AppConstant.OFFLINE) {
                        if (selected == 1) {
                            try {
                                txt_searchlocation.dismissDropDown();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                txt_searchlocation.showDropDown();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (AppConstant.APP_MODE != AppConstant.OFFLINE) {
                    try {
                        if (selected == 1) {
                            try {
                                txt_searchlocation.dismissDropDown();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                txt_searchlocation.showDropDown();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
//
        txt_searchlocation.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (AppConstant.APP_MODE != AppConstant.OFFLINE) {
                    if (selected == 1) {
                        try {
                            txt_searchlocation.dismissDropDown();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        try {
                            txt_searchlocation.showDropDown();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                return false;
            }
        });


        // Setting an item click listener for the AutoCompleteTextView dropdown list
        txt_searchlocation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int index,
                                    long id) {
                if (AppConstant.APP_MODE != AppConstant.OFFLINE) {
                    selected = 1;
                    try {
                        txt_searchlocation.dismissDropDown();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    locationData = new LocationData();
                    SimpleAdapter adapter = (SimpleAdapter) arg0.getAdapter();
                    HashMap<String, String> hm = (HashMap<String, String>) adapter.getItem(index);
                    txt_searchlocation.setText(hm.get("description"));

                    // Creating a DownloadTask to download Places details of the selected place
                    placeDetailsDownloadTask = new DownloadTask(PLACES_DETAILS);

                    // Getting url to the Google Places details api
                    String url = getPlaceDetailsUrl(hm.get("reference"));


                    // Start downloading Google Place Details
                    // This causes to execute doInBackground() of DownloadTask class
                    placeDetailsDownloadTask.execute(url);
                    if (selected == 1) {
                        try {
                            txt_searchlocation.dismissDropDown();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        try {
                            txt_searchlocation.showDropDown();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });


        //ScreenTracking
        UID = getUIDforScreenTracking();
        setScreenTracking(this, db, SN_AddFarmOnMap_New, UID);


        gotoHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(AddFarmOnMap_New.this, AdminDashboard_New.class);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(in);
                finish();
            }
        });

        productPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (farmerId != null && !farmerId.equalsIgnoreCase("null")) {
                    if (projectId != null && !projectId.equalsIgnoreCase("null")) {
                        Intent in = new Intent(getApplicationContext(), ProductActivity.class);
                        in.putExtra("balance", balanceAmount);
                        in.putExtra("farmerId", farmerId);
                        in.putExtra("project_id", projectId);
                        in.putExtra("from", "from");
                        startActivity(in);
                    } else {
                        getDynamicLanguageToast(getApplicationContext(), "ProjectIDnotfound", R.string.ProjectIDnotfound);
                    }
                } else {
                    getDynamicLanguageToast(getApplicationContext(), "FarmerIDnotfound", R.string.FarmerIDnotfound);
                }
            }
        });

    }

    public void IntentCall() {
        try {
            Double area = 0.0;
            String Areas = txt_area.getText().toString();
            if (Areas == null || area == 0.0)
                area = Double.parseDouble(Areas);
            if (area == 0.0 || latLngList.size() < 2) {
                getDynamicLanguageToast(getApplicationContext(), "PleaseclickGeotag", R.string.PleaseclickGeotag);
            } else if (area < 0.01) {
                getDynamicLanguageToast(getApplicationContext(), "Geotaggingisnoteligible", R.string.Geotaggingisnoteligible);
            } else {
                Intent intent = new Intent(AddFarmOnMap_New.this, NewDashboardActivity.class);
                intent.putExtra("calling-activity", AppConstant.AddFarmMap);
                intent.putExtra("AllLatLngPount", allPoints);
                intent.putExtra("Area", Areas);
                intent.putExtra("add", "add");
                startActivity(intent);
                finish();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public void setDeleteClear(String flag) {
        switch (flag) {
            case "delete":
                latLngList = new ArrayList<>();
                markerList = new ArrayList<>();
                allPoints = "";
                txt_area.setText("0.0");
                txt_area_geotag.setText("0.0");
                setVisible(2);
                polyline = null;
                polygon = null;
                map.clear();
                AppConstant.isWrite = false;
                break;
            case "clear":
                latLngList = new ArrayList<>();
                markerList = new ArrayList<>();
                allPoints = "";
                txt_area.setText("0.0");
                txt_area_geotag.setText("0.0");
//                setVisible(3);

                polyline = null;
                polygon = null;
                map.clear();
                AppConstant.isWrite = true;
                break;
        }
    }

    public void setInitialazation() {
        txt_area = (TextView) findViewById(R.id.txt_area);
        txt_area_geotag = (TextView) findViewById(R.id.txt_area_geotag);
        alertTitle = (TextView) findViewById(R.id.alertTitle);
        text_geotag_heading = (TextView) findViewById(R.id.text_geotag_heading);
        txt_searchlocation = (AutoCompleteTextView) findViewById(R.id.txt_searchlocation);
        img_clear = (ImageView) findViewById(R.id.img_clear);
        img_delete = (ImageView) findViewById(R.id.img_delete);
        img_delete_geotag = (ImageView) findViewById(R.id.img_delete_geotag);
        img_savenext = (ImageView) findViewById(R.id.img_savenext);
        img_back = (ImageView) findViewById(R.id.img_back);
        img_search = (ImageView) findViewById(R.id.img_search);
        img_start = (ImageView) findViewById(R.id.img_start);
        img_stop = (ImageView) findViewById(R.id.img_stop);
        img_savenext_geotag = (ImageView) findViewById(R.id.img_savenext_geotag);

        tab_search = (TableRow) findViewById(R.id.tab_search);
        tab_geotag = (TableRow) findViewById(R.id.tab_geotag);
        tab_geotagging = (TableRow) findViewById(R.id.tab_geotagging);
        fltbtn_geoTag = (Button) findViewById(R.id.fltbtn_geoTag);
        fltbtn_editfarm = (Button) findViewById(R.id.fltbtn_editfarm);
        gotoHomeBtn = (Button) findViewById(R.id.home_btn);
        productPageBtn = (Button) findViewById(R.id.product_btn);


    }

    //set the location acc to lagtitude and longitude
    public void setPositionGoogleMap(Double latitude, Double Longitude) {
        try {
            LatLng mylocation = new LatLng(latitude, Longitude);
            map.addMarker(new MarkerOptions().position(mylocation).snippet("").title(""));
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
            map.setMyLocationEnabled(true);
            map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            map.getUiSettings().setMyLocationButtonEnabled(true);//remove border line
            map.animateCamera(CameraUpdateFactory.newLatLng(mylocation));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(mylocation, 18));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setVisible(int flag) {
        switch (flag) {
            case 1://Show Search button and Hide Geotag
                txt_searchlocation.setVisibility(View.VISIBLE);
                text_geotag_heading.setVisibility(View.GONE);
                img_search.setVisibility(View.GONE);
                break;
            case 2://Show Search button and Hide Geotag
                txt_searchlocation.setVisibility(View.GONE);
                tab_search.setVisibility(View.VISIBLE);
                text_geotag_heading.setVisibility(View.VISIBLE);
                img_search.setVisibility(View.VISIBLE);
                tab_geotag.setVisibility(View.GONE);
                tab_geotagging.setVisibility(View.GONE);
                break;
            case 3://Show Search button and Hide Geotag
                tab_search.setVisibility(View.VISIBLE);
                tab_geotag.setVisibility(View.GONE);
                tab_geotagging.setVisibility(View.GONE);
                break;
            case 4://Hide Search button and show Geotag by Drawing
                tab_search.setVisibility(View.GONE);
                tab_geotag.setVisibility(View.VISIBLE);
                tab_geotagging.setVisibility(View.GONE);
                break;
            case 5://Hide Search button and show Geotag by Geo tagging
                tab_search.setVisibility(View.GONE);
                tab_geotag.setVisibility(View.GONE);
                tab_geotagging.setVisibility(View.VISIBLE);
                img_start.setVisibility(View.VISIBLE);
                img_stop.setVisibility(View.GONE);
                break;

        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        setPositionGoogleMap(lat, log);

        map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                // Getting view from the layout file info_window_layout
                View v = getLayoutInflater().inflate(R.layout.googlemap_windowlayout, null);

                // Getting the position from the marker
                LatLng latLng = marker.getPosition();

                // Getting reference to the TextView to set latitude
                TextView tvLat = (TextView) v.findViewById(R.id.tv_lat);

                // Getting reference to the TextView to set longitude
                TextView tvLng = (TextView) v.findViewById(R.id.tv_lng);

                // Setting the latitude
                tvLat.setText("Latitude : " + latLng.latitude);

                // Setting the longitude
                tvLng.setText("Longitude : " + latLng.longitude);

                // Returning the view containing InfoWindow contents
                return v;
            }
        });
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.showInfoWindow();
                sethideWindow(marker);
                return true;
            }
        });

        // GoogleMap pin point Lat Long
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (AppConstant.isWrite) {
//                    map.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                    addPolygonValues(latLng, 1);
                }
            }
        });


        map.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker arg0) {
                // TODO Auto-generated method stub
                LatLng latLng = arg0.getPosition();
                Log.d("System out", "onMarkerDragStart..." + arg0.getPosition().latitude + "..." + arg0.getPosition().longitude);
            }

            @SuppressWarnings("unchecked")
            @Override
            public void onMarkerDragEnd(Marker arg0) {
                // TODO Auto-generated method stub
                LatLng latLng = arg0.getPosition();
                Log.d("System out", "onMarkerDragEnd..." + arg0.getPosition().latitude + "..." + arg0.getPosition().longitude);
//                map.animateCamera(CameraUpdateFactory.newLatLng(arg0.getPosition()));
                addPolygonValues(arg0.getPosition(), 1);
            }

            @Override
            public void onMarkerDrag(Marker arg0) {
                // TODO Auto-generated method stub
                LatLng latLng = arg0.getPosition();
                Log.i("System out", "onMarkerDrag...");

            }
        });


        //Long press on Google Map
        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

            @Override
            public void onMapLongClick(LatLng point) {
                // TODO Auto-generated method stub
                String value = "";// Long press on Google Map
            }
        });


        //Google Map Location Click
//        map.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
//            @Override
//            public boolean onMyLocationButtonClick() {
//                Location location = getLocation();
//                if (location != null) {
//                    AppConstant.latitude = "" + location.getLatitude();
//                    AppConstant.longitude = "" + location.getLongitude();
//
//                    CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(location.getLatitude(), location.getLongitude())).zoom(18).build();
//                    map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//                }
//                return true;
//
//
//            }
//        });


        if (ActivityCompat.checkSelfPermission(AddFarmOnMap_New.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(AddFarmOnMap_New.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
    }


    public void sethideWindow(final Marker marker) {
        try {
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Do something here
                    marker.hideInfoWindow();
                }
            }, 2000);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void addPolygonValues(LatLng point, int flag) {

        MarkerOptions markerOptions = new MarkerOptions();
//        MarkerOptions markerOptions = new MarkerOptions().position(point);
        //Herojit Icon Change
        int height = 65;
        int width = 65;
        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.icon_geotag);
        Bitmap b = bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
        BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(smallMarker);

        switch (flag) {
            case 1://add Laglongitude
                if (allPoints != null && allPoints.length() > 0) {
                    allPoints = allPoints + "-" + point.latitude + "," + point.longitude;
                } else {
                    allPoints = point.latitude + "," + point.longitude;
                }
                markerOptions.position(point);
                markerOptions.title("Position");
                markerOptions.icon(icon);
                markerOptions.draggable(false);
//                markerOptions.anchor(0.2f, 1.0f);
                //below lat lng point wil use in createdStringforFieldInprovemnt() method fo farminformation class
                AppConstant.latitude = String.valueOf(point.latitude);
                AppConstant.longitude = String.valueOf(point.longitude);
                //Create Marker
//                Marker marker = map.addMarker(markerOptions);
                Marker marker = map.addMarker(markerOptions);
                marker.setDraggable(false);
                // Animating to the currently touched position
                marker.showInfoWindow();
                sethideWindow(marker);
                //Add Latlng and Marker
                latLngList.add(point);
                markerList.add(marker);
                break;
            case 2://add Laglongitude
                try {
                    int pos = latLngList.size() - 1;
                    latLngList.remove(pos);
                    Marker removemarker = markerList.get(pos);
                    removemarker.remove();
                    markerList.remove(pos);
                    if (allPoints != null && allPoints.length() > 0) {
                        String newallpoints[] = allPoints.split("-");
                        allPoints = "";
                        for (int i = 0; i < newallpoints.length - 1; i++) {
                            if (allPoints != null && allPoints.length() > 0) {
                                allPoints = allPoints + "-" + newallpoints[i];
                            } else {
                                allPoints = newallpoints[i];
                            }
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                break;
        }

        setDrawMap(flag);
    }

    public void setDrawMap(int flag) {
        if (latLngList.size() > 1) {
            if (polygon != null) polygon.remove();
            PolygonOptions polygonOptions = new PolygonOptions().addAll(latLngList)
                    .clickable(true);
            polygonOptions.fillColor(getResources().getColor(R.color.un_selected_button_background));
            polygonOptions.strokeWidth(3);
            polygonOptions.strokeColor(getResources().getColor(R.color.ColorPrimary));
            polygonOptions.clickable(true);
//            map.addPolygon(polygonOptions);
            polygon = map.addPolygon(polygonOptions);
            polygon.setGeodesic(true);
            polygon.setStrokeJointType(20);
            polygon.setClickable(true);
            // allPoints = allPoints.substring(1, allPoints.length() - 1);
            Log.d("allPoints", allPoints);
            Double areadOfPolygon = 0.0;
            areadOfPolygon = SphericalUtil.computeArea(latLngList);
            if (areadOfPolygon != 0.0) {
                Double acreageArea = (0.000247105) * areadOfPolygon;
                String ss = null;
                if (acreageArea != null) {
                    ss = String.format("%.4f", acreageArea);
                }
                txt_area.setText(ss);//Herojit COmment
                txt_area_geotag.setText(ss);//Herojit COmment
            }

        } else {
            if (flag == 2) {
                setDeleteClear("clear");
            }
        }
    }

    double incr = 0;

    public void setGeoTaggingdraw(LatLng point) {
        MarkerOptions markerOptions = new MarkerOptions();

        int height = 20;
        int width = 20;
        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.icon_pinpoint);
        Bitmap b = bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
        BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(smallMarker);

        Double lati, longi;
        lati = point.latitude;
        longi = point.longitude;
//        incr = incr + 0.09253799170256f;
        if (allPoints != null && allPoints.length() > 0) {
            allPoints = allPoints + "-" + lati + "," + longi;
        } else {
            allPoints = lati + "," + longi;
        }
        markerOptions.position(point);
        markerOptions.icon(icon);
        markerOptions.title("Position");
        AppConstant.latitude = String.valueOf(lati);
        AppConstant.longitude = String.valueOf(longi);
        Marker marker = map.addMarker(markerOptions);
        marker.setDraggable(false);
        // Animating to the currently touched position
        marker.showInfoWindow();
        sethideWindow(marker);
        //Add Latlng and Marker
        latLngList.add(point);
        markerList.add(marker);

//        polyline = map.addPolyline(new PolylineOptions().add(new LatLng(lati, longi),
//                new LatLng(LatLonCellID.lat, LatLonCellID.lon)).width(3).
//                color(getResources().getColor(R.color.un_selected_button_background)).geodesic(true));
        if (latLngList.size() >= 2) {
            if (polyline != null) polyline.remove();
            PolylineOptions polygonOptions = new PolylineOptions().addAll(latLngList)
                    .clickable(true);
//            polygonOptions.fillColor(getResources().getColor(R.color.un_selected_button_background));
            polygonOptions.width(4);
            polygonOptions.color(getResources().getColor(R.color.ColorPrimary));
            polygonOptions.clickable(true);
//            map.addPolygon(polygonOptions);
            polyline = map.addPolyline(polygonOptions);
            polyline.setGeodesic(true);
//            polyline.setStrokeJointType(20);
            polyline.setClickable(true);
            // allPoints = allPoints.substring(1, allPoints.length() - 1);
            Log.d("allPoints", allPoints);
            Double areadOfPolygon = 0.0;
            areadOfPolygon = SphericalUtil.computeArea(latLngList);
            if (areadOfPolygon != 0.0) {
                Double acreageArea = (0.000247105) * areadOfPolygon;
                String ss = null;
                if (acreageArea != null) {
                    ss = String.format("%.4f", acreageArea);
                }
                txt_area.setText(ss);//Herojit COmment
                txt_area_geotag.setText(ss);//Herojit COmment
            }

        }

    }


    //Herojit Add
    public void GeoFarmingPopup() {

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
        dialog.setContentView(R.layout.popup_geotaggingselection);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, 1000);

        ImageView crossbtn = (ImageView) dialog.findViewById(R.id.crossbtn);
        LinearLayout draw_by_walk = (LinearLayout) dialog.findViewById(R.id.draw_by_walk);
        TextView draw_by_walk_txt = (TextView) dialog.findViewById(R.id.draw_by_walk_txt);
        GifTextView geotaggingwalk = (GifTextView) dialog.findViewById(R.id.geotaggingwalk);
        LinearLayout draw_button = (LinearLayout) dialog.findViewById(R.id.draw_button);
        TextView draw_button_txt = (TextView) dialog.findViewById(R.id.draw_button_txt);
        GifTextView geotaggingselection = (GifTextView) dialog.findViewById(R.id.geotaggingselection);


        geotaggingwalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                walkPopup();
            }
        });
        geotaggingselection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawPopup();
            }
        });
        draw_by_walk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                AppConstant.isWrite = false;
                setVisible(5);
            }
        });
        draw_by_walk_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                AppConstant.isWrite = false;
                setVisible(5);
            }
        });

        draw_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.cancel();
                AppConstant.isWrite = true;
                setVisible(4);
            }
        });
        draw_button_txt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.cancel();
                AppConstant.isWrite = true;
                setVisible(4);
            }
        });

        crossbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    public void walkPopup() {

        final Dialog dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setCanceledOnTouchOutside(true);
        Window window = dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.dimAmount = 0.7f;
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        // wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);

        // Include dialog.xml file
        dialog.setContentView(R.layout.walk_popup);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        Button cancel = (Button) dialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();

            }
        });

        dialog.show();
    }

    public void drawPopup() {

        final Dialog dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setCanceledOnTouchOutside(true);
        Window window = dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.dimAmount = 0.7f;
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        // wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);

        // Include dialog.xml file
        dialog.setContentView(R.layout.gif_popup);

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        Button cancel = (Button) dialog.findViewById(R.id.cancel);


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();

            }
        });

        dialog.show();
    }

    // Fetches all places from GooglePlaces AutoComplete Web Service
    private class PlacesTask extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... place) {
            // For storing data from web service
            String data = "";

            // Obtain browser key from https://code.google.com/apis/console

            String key = "key=" + getResources().getString(R.string.browser_key);  //previous key
            //  String key = "key="+getResources().getString(R.string.browser_key);
            String input = "";

            try {
                input = "input=" + URLEncoder.encode(place[0], "utf-8");
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }

            // place type to be searched
            String types = "types=geocode";

            // place searched by country
            String country = "components=country:in";

            // Sensor enabled
            String sensor = "sensor=false";

            // Building the parameters to the web service
            String parameters = input + "&" + types + "&" + sensor + "&" + country + "&" + key;

            // Output format
            String output = "json";

            // Building the url to the web service
            String url = "https://maps.googleapis.com/maps/api/place/autocomplete/" + output + "?" + parameters;

            try {
                System.out.println("URL  : " + url);
                // Fetching the data from we service
                data = downloadUrl(url);
                System.out.println("DATA : " + data); //complete address of locations

            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result.trim().length() > 0) {
                // Creating ParserTask
                parserTask = new ParserTask(PLACES);

                // Starting Parsing the JSON string returned by Web Service
                parserTask.execute(result);
                if (AppConstant.APP_MODE != AppConstant.OFFLINE) {
                    if (selected == 1) {
                        txt_searchlocation.dismissDropDown();
                    } else {
                        txt_searchlocation.showDropDown();
                    }
                }
            } else {
                getDynamicLanguageToast(getApplicationContext(), "Couldnotconnectlocation", R.string.Couldnotconnectlocation);
            }
        }
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);
            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();
            // Connecting to url
            urlConnection.connect();
            // Reading data from url
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception while downloading url" + e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String, String>>> {

        int parserType = 0;

        public ParserTask(int type) {
            this.parserType = type;
        }

        @Override
        protected void onPreExecute() {
            if (parserType == PLACES_DETAILS) {
                pDialog = new TransparentProgressDialog(
                        AddFarmOnMap_New.this, getResources().getString(R.string.Pleasewait));
                pDialog.setCancelable(false);
                pDialog.show();
            }
        }

        @Override
        protected List<HashMap<String, String>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<HashMap<String, String>> list = null;
            try {
                jObject = new JSONObject(jsonData[0]);
                switch (parserType) {
                    case PLACES:
                        PlaceJSONParser placeJsonParser = new PlaceJSONParser();
                        // Getting the parsed data as a List construct
                        list = placeJsonParser.parse(jObject);
                        break;
                    case PLACES_DETAILS:
                        PlaceDetailsJSONParser placeDetailsJsonParser = new PlaceDetailsJSONParser();
                        // Getting the parsed data as a List construct
                        list = placeDetailsJsonParser.parse(jObject);
                        //we will pick here state

                        //     JSONObject  job = new JSONObject(jsonData[0]);

                }

            } catch (Exception e) {
                Log.d("Exception", e.toString());
            }
            return list;
        }


        @Override
        protected void onPostExecute(List<HashMap<String, String>> result) {

            switch (parserType) {
                case PLACES:
                    if (result != null) {
                        String[] from = new String[]{"description"};
                        int[] to = new int[]{android.R.id.text1};

                        // Creating a SimpleAdapter for the AutoCompleteTextView
                        SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), result, android.R.layout.simple_list_item_1, from, to);

                        // Setting the adapter
                        txt_searchlocation.setAdapter(adapter);
                        if (AppConstant.APP_MODE != AppConstant.OFFLINE) {
                            if (selected == 1) {
                                txt_searchlocation.dismissDropDown();
                            } else {
                                txt_searchlocation.showDropDown();
                            }
                        }
                    }
                    break;
                case PLACES_DETAILS:
                    if (pDialog != null) {
                        pDialog.cancel();
                    }
                    if (result != null) {
                        HashMap<String, String> hm = result.get(0);
                        // Getting latitude from the parsed data
                        Double latitude = Double.parseDouble(hm.get("lat"));
                        // Getting longitude from the parsed data
                        Double longitude = Double.parseDouble(hm.get("lng"));
                        AppConstant.latitude = "" + latitude;
                        AppConstant.longitude = "" + longitude;

                        locationData.setLocationName(txt_searchlocation.getText().toString());
                        locationData.setLatitude(String.valueOf(latitude));
                        locationData.setLongitude(String.valueOf(longitude));
                        locationData.setModifiedDate(String.valueOf((new Date()).getTime()));

                        Log.d("HomeActivity", "" + locationData);
                        String lat = locationData.getLatitude();
                        String log = locationData.getLongitude();

                        setPositionGoogleMap(latitude, longitude);
//                        if (lat != null && lat.length() > 4) {
//                            MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title("Selected Location");
//                            map.addMarker(marker);
//                            CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(latitude, longitude)).zoom(18).build();
//                            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//                        } else {
//
//                            MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title("Current Location");
//                            map.addMarker(marker);
//                            CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(LatLonCellID.lat, LatLonCellID.lon)).zoom(18).build();
//                            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//                        }

                        selected = 0;

                        System.out.println("Place Detail Latitude : " + latitude + " longitude : " + longitude);

                    }

                    break; //End of second case
            }
        }
    }

    private class DownloadTask extends AsyncTask<String, Void, String> {

        private int downloadType = 0;

        // Constructor
        public DownloadTask(int type) {
            this.downloadType = type;
        }

        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";
            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                Log.d("HomeActicity-------", data);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            JSONObject jobject = null;
            try {
                jobject = new JSONObject(result);
                JSONObject jsonObject = jobject.getJSONObject("result");
                JSONArray jsonArray = jsonObject.getJSONArray("address_components");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jObject = jsonArray.getJSONObject(i);
                    JSONArray types = jObject.getJSONArray("types");
                    for (int k = 0; k < types.length(); k++) {
                        if (types.optString(k).contains("administrative_area_level_1")) {
                            AppConstant.state = jObject.getString("long_name");
                            System.out.println("State Name---" + AppConstant.state);
                            break;
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            switch (downloadType) {
                case PLACES:
                    // Creating ParserTask for parsing Google Places
                    placesParserTask = new ParserTask(PLACES);

                    placesParserTask.execute(result);
                    if (AppConstant.APP_MODE != AppConstant.OFFLINE) {
                        if (selected == 1) {
                            txt_searchlocation.dismissDropDown();
                        } else {
                            txt_searchlocation.showDropDown();
                        }
                    }
                    break;

                case PLACES_DETAILS:
                    // Creating ParserTask for parsing Google Places
                    placeDetailsParserTask = new ParserTask(PLACES_DETAILS);

                    // Starting Parsing the JSON string
                    // This causes to execute doInBackground() of ParserTask class
                    placeDetailsParserTask.execute(result);
                    if (AppConstant.APP_MODE != AppConstant.OFFLINE) {
                        if (selected == 1) {
                            txt_searchlocation.dismissDropDown();
                        } else {
                            txt_searchlocation.showDropDown();
                        }
                    }
            }
        }
    }

    private String getPlaceDetailsUrl(String ref) {
        // Obtain browser key from https://code.google.com/apis/console
        String key = "key=" + getResources().getString(R.string.browser_key);
//        String key = "key=" + getResources().getString(R.string.debug_key);
        // reference of place
        String reference = "reference=" + ref;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = reference + "&" + sensor + "&" + key;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/place/details/" + output + "?" + parameters;

        return url;
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


    //Geo tagging
    public void startTimer() {
        //set a new Timer
        timer = new Timer();
        //initialize the TimerTask's job
        initializeTimerTask();
        //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
        timer.schedule(timerTask, 100, 5000); //
    }

    public void stopTimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                mHandler.post(runnable);
            }
        };
    }

    private Runnable runnable = new Runnable() {
        public void run() {
            try {
                LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                Criteria criteria = new Criteria();
                String bestProvider = locationManager.getBestProvider(criteria, true);
//            Location location = getLocation();
////                Location location = locationManager.getLastKnownLocation(bestProvider);
//            if (location != null) {
//                onLocationChanged(location);
//            }
                if (ActivityCompat.checkSelfPermission(AddFarmOnMap_New.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(AddFarmOnMap_New.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                }
                locationManager.requestLocationUpdates(bestProvider, 2000, 1, locationListenerGPS);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    };


    LocationListener locationListenerGPS = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            LatLng latLng = new LatLng(latitude, longitude);
            setGeoTaggingdraw(latLng);//Geo Tagging
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    private Location getLocation() {
        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }
        return locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
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
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (db != null) {
            db = new DBAdapter(this);
        }
        Log.d("onStop Method", "onStop Method called");
        setScreenTracking(this, db, SN_AddFarmOnMap_New, UID);
    }

    @Override
    public void onBackPressed() {
        // code here to show dialog
        // super.onBackPressed();  // optional depending on your needs
        if (farmIdlist.size() > 0) {
            finish();
        } else {
            Log.d("onBackPressed Method", "onBackPressed Method called");
            Intent in = new Intent(AddFarmOnMap_New.this, AdminDashboard_New.class);
            startActivity(in);
            finish();
        }
    }


    //Farmer Edit

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
        dialog.setContentView(R.layout.geotaggingeditfarm);

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        Button btn_cross = (Button) dialog.findViewById(R.id.btn_cross);
        Button btn_Submit = (Button) dialog.findViewById(R.id.btn_Submit);
        TextView title = (TextView) dialog.findViewById(R.id.title);
        TextView txt_Pleaseselectyour = (TextView) dialog.findViewById(R.id.txt_Pleaseselectyour);

        CustomSearchableSpinner header_chooseFarmSpinner = (CustomSearchableSpinner) dialog.findViewById(R.id.header_chooseFarmSpinner);

        setDynamicLanguage(this, title, "GeoTaggingFarmEdi", R.string.GeoTaggingFarmEdi);
        setDynamicLanguage(this, txt_Pleaseselectyour, "Pleaseselectyour", R.string.Pleaseselectyour);

        setFontsStyleTxt(this, title, 2);
        setFontsStyleTxt(this, txt_Pleaseselectyour, 5);
        try {

            str = new ArrayList<String>();
            strFarmId = new ArrayList<String>();

//        if ((AppConstant.userTypeID != null && !AppConstant.userTypeID.equalsIgnoreCase("5"))) {
            if (AppConstant.userTypeID != null) {
                str.add(getDynamicLanguageValue(getApplicationContext(), "Select", R.string.Select));
                strFarmId.add("0");
            }

            Log.v("userTypeID", AppConstant.userTypeID + "");
            db.open();
            Cursor c = db.getallFarmName(AppConstant.user_id);
            if (c.moveToFirst()) {
                do {

                    //Vishal Tripathi added code
                    if (farmIdlist.size() > 0) {
                        String strr = c.getString(1).toString();
                        if (farmIdlist.contains(strr)) {
                            str.add(c.getString(0).toString());
                            strFarmId.add(c.getString(1).toString());
                        }
                    } else {
                        str.add(c.getString(0).toString());
                        strFarmId.add(c.getString(1).toString());
                    }

                    //Old code by vishal/herojit

                  /*  str.add(c.getString(0).toString());
                    strFarmId.add(c.getString(1).toString());*/

                } while (c.moveToNext());
            }
            db.close();
        } catch (IndexOutOfBoundsException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            if (str != null && str.size() > 1) {
                ArrayAdapter<String> chooseYourFarmAdapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, str);
                header_chooseFarmSpinner.setAdapter(chooseYourFarmAdapter);
                setCustomSearchableSpinner(getApplicationContext(), header_chooseFarmSpinner, "SelectFarm", R.string.SelectFarm);

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        header_chooseFarmSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                                               @Override
                                                               public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                                   if (position > 0) {
                                                                       SelectFarmID = strFarmId.get(position);
                                                                       SelectedFarm = str.get(position);
//                                                                       dialog.cancel();
//                                                                       Toast.makeText(getApplicationContext(), SelectFarmID + "  " + SelectedFarm, Toast.LENGTH_SHORT).show();

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
        btn_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
                if (SelectFarmID != null && SelectFarmID.length() > 1) {
                    new FarmdetailsAsynctask(SelectFarmID).execute();
                } else {
                    getDynamicLanguageToast(getApplicationContext(), "Pleaseselectyour", R.string.Pleaseselectyour);
                }
            }
        });


        dialog.show();
    }

    private class FarmdetailsAsynctask extends AsyncTask<Void, Void, String> {
        String FarmID = "";

        public FarmdetailsAsynctask(String farmid) {
            FarmID = farmid;
        }

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(AddFarmOnMap_New.this);
            progressDialog.setMessage(getResources().getString(R.string.Dataisloading));
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
                sendRequest = AppManager.getInstance().getFarmData + AppConstant.user_id + "/" + FarmID;
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
                    progressDialog.dismiss();
                    GeoFarmDetailsShow(response);

                }


            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    //Farm details show
    public void GeoFarmDetailsShow(String response) {

        try {
            JSONObject obj = new JSONObject(response);
            String Contour = setString(obj.getString("Contour"));
            String ProjectID = setString(obj.getString("ProjectID"));

            String CenterLat = setString(obj.getString("CenterLat"));
            String CenterLon = setString(obj.getString("CenterLon"));
            String AadharNo = setString(obj.getString("AadharNo"));
            double cenlat = 0.0;
            double cenlong = 0.0;
            if (CenterLat != null && CenterLat.length() > 1) {
                cenlat = Double.parseDouble(CenterLat);
            }
            if (CenterLon != null && CenterLon.length() > 1) {
                cenlong = Double.parseDouble(CenterLon);
            }
            setPositionGoogleMap(cenlat, cenlong);
            if (AadharNo != null && AadharNo.length() > 5) {
                AppConstant.FarmRegistration_AadharNo = AadharNo;
            }
            if (ProjectID != null && ProjectID.length() > 2) {
                AppConstant.FarmRegistration_ProjectID = ProjectID;
            }
            if (Contour != null && Contour.length() > 2) {
                FarmEditDetails = response;
                FarmEditActive = true;
                String countours[] = Contour.split("-");
                AppConstant.isWrite = true;
                setVisible(4);
                for (int i = 0; i < countours.length; i++) {
                    try {
                        String con = countours[i];
                        String newcon[] = con.split(",");
                        String lat = newcon[0];
                        String lon = newcon[1];
                        double latitude = 0.0;
                        double longitude = 0.0;
                        if (lat != null && lat.length() > 1) {
                            latitude = Double.parseDouble(lat);
                        }
                        if (lon != null && lon.length() > 1) {
                            longitude = Double.parseDouble(lon);
                        }

                        LatLng mylocation = new LatLng(latitude, longitude);
                        addPolygonValues(mylocation, 1);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

            } else {
                FarmEditActive = false;
                getDynamicLanguageToast(getApplicationContext(), "Thefarmernocontour", R.string.Thefarmernocontour);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public String setString(String val) {
        if (val != null && val.length() > 0 && !val.equalsIgnoreCase("") && !val.equalsIgnoreCase("null"))
            return val;
        else
            return "";
    }


    public void setLanguages() {
        //Heading
        setFontsStyleTxt(this, text_geotag_heading, 2);
        setFontsStyle(this, gotoHomeBtn);
        setFontsStyle(this, productPageBtn);
        //Tab Service
        setDynamicLanguage(this, text_geotag_heading, "GeoTag", R.string.GeoTag);
        setDynamicLanguage(this, gotoHomeBtn, "GotoHome", R.string.GotoHome);
        setDynamicLanguage(this, productPageBtn, "product_page", R.string.product_page);
    }
}
