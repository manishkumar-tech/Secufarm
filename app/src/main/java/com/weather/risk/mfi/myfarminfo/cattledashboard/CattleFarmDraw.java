package com.weather.risk.mfi.myfarminfo.cattledashboard;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.database.DBAdapter;
import com.weather.risk.mfi.myfarminfo.databinding.CattlefarmdrawBinding;
import com.weather.risk.mfi.myfarminfo.mapfragments.LatLonCellID;
import com.weather.risk.mfi.myfarminfo.utils.UtilFonts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CattleFarmDraw extends AppCompatActivity {

    CattlefarmdrawBinding binding;
    DBAdapter db;
    ArrayList<String> str = new ArrayList<String>();
    ArrayList<String> strFarmId = new ArrayList<String>();
    Boolean mDrawPolygon = false;
    PolygonOptions rectOptions;
    ArrayList<LatLng> arrayPoints = new ArrayList<LatLng>();
    String allPoints = "";
    int flagForDraw = 0;
    ArrayList<LatLng> points1 = null;
    ArrayList<LatLng> points = null;
    String latitude, longitude,  contourr;
    private GoogleMap mMap;
    String CattleName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.cattlefarmdraw);

        setFonts();
        db = new DBAdapter(this);

        str = new ArrayList<String>();
        strFarmId = new ArrayList<String>();
        rectOptions = new PolygonOptions();
        arrayPoints = new ArrayList<LatLng>();
        points = new ArrayList<LatLng>();


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            //bundle must contain all info sent in "data" field of the notification
            try {
                if (bundle.size() > 0) {
                    CattleName = bundle.getString("CattleName");
                    contourr = bundle.getString("contour");
                    latitude = bundle.getString("lat");
                    longitude = bundle.getString("long");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        binding.btnCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        points1 = new ArrayList<LatLng>();
        setUpMapIfNeeded(); // For setting up the MapFragment


        if (contourr != null) {
            setGeoMap();
        }

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

//                                if (flagForDraw > 2) {
//                                   clearLay.setVisibility(View.VISIBLE);
//                                } else {
//                                    clearLay.setVisibility(View.GONE);
//                                }
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
        if (CattleName != null && contourr != null) {
            binding.txtFarmname.setText(CattleName);
            List<String> l_List = new ArrayList<String>();
            String point = contourr;
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


    public String setString(String val) {
        if (val != null && val.length() > 0 && !val.equalsIgnoreCase("") && !val.equalsIgnoreCase("null"))
            return val;
        else
            return "";
    }


    //https://www.mysamplecode.com/2012/10/create-table-pdf-java-and-itext.html
    public void setFonts() {
        UtilFonts.UtilFontsInitialize(CattleFarmDraw.this);
        binding.txtFarmname.setTypeface(UtilFonts.KT_Bold);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("OnPause Method", "OnPause Method called");
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("onStop Method", "onStop Method called");
    }

    @Override
    public void onBackPressed() {
        // code here to show dialog
        super.onBackPressed();  // optional depending on your needs
        finish();
    }
}
