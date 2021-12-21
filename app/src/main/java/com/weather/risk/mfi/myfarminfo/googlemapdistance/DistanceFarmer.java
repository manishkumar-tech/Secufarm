package com.weather.risk.mfi.myfarminfo.googlemapdistance;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CustomCap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;
import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.mapfragments.LatLonCellID;

import java.util.ArrayList;
import java.util.List;

import static com.weather.risk.mfi.myfarminfo.mapfragments.LatLonCellID.currentLat;
import static com.weather.risk.mfi.myfarminfo.mapfragments.LatLonCellID.currentLon;

public class DistanceFarmer extends AppCompatActivity {


    private GoogleMap mMap;
    Button btn_cross;
    String FarmLag, FarmLong;
    Double farmer_lag, farmer_long;
    LatLng fromDistance, toDistance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.distancefarmer);

        btn_cross = (Button) findViewById(R.id.btn_cross);

        FarmLag = getIntent().getStringExtra("FarmerLatitude");
        FarmLong = getIntent().getStringExtra("FarmerLongitude");

        if (FarmLag != null && FarmLag.length() > 0) {
            farmer_lag = Double.valueOf(FarmLag);
        }
        if (FarmLong != null && FarmLong.length() > 0) {
            farmer_long = Double.valueOf(FarmLong);
        }

        fromDistance = new LatLng(currentLat, currentLon);
        toDistance = new LatLng(farmer_lag, farmer_long);

        setMapDistance();

        btn_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void setMapDistance() {
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_frag)).getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    mMap = googleMap;
                    setUpMap();
                    if (mMap != null) {
                        mMap.clear();
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLat, currentLon), 13.0f));

                        List<LatLng> list = new ArrayList<LatLng>();
                        list.add(fromDistance);
                        list.add(toDistance);

                        Polyline polyline = mMap.addPolyline(new PolylineOptions()
                                        .addAll(list)
                                        .width(20)
                                        .color(getResources().getColor(R.color.ColorPrimary))
                                        .geodesic(true)
                                        .startCap(new CustomCap(BitmapDescriptorFactory.fromResource(R.drawable.icon_googlemappinpoint), 100))
                                        .endCap(new CustomCap(BitmapDescriptorFactory.fromResource(R.drawable.icon_googlemappinpoint), 100))
//                                .startCap(new RoundCap())
//                                .endCap(new RoundCap())
                        );

                    }
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

    }
}
