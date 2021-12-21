package com.weather.risk.mfi.myfarminfo.groundwater;

import static com.weather.risk.mfi.myfarminfo.groundwater.GroundWaterForecasting.GroundwaterContours;
import static com.weather.risk.mfi.myfarminfo.groundwater.GroundWaterForecasting.listLatLong;
import static com.weather.risk.mfi.myfarminfo.groundwater.GroundWaterForecasting.listdivergance;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setDynamicLanguage;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setFontsStyleTxt;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
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
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.databinding.GroundwatergooglemapBinding;
import com.weather.risk.mfi.myfarminfo.mapfragments.LatLonCellID;
import com.weather.risk.mfi.myfarminfo.utils.AppConstant;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GroundWaterGoogleMap extends AppCompatActivity implements
        OnMapReadyCallback,
        GoogleMap.OnPolygonClickListener {


    GroundwatergooglemapBinding binding;
    String UID = "";
    SupportMapFragment supportMapFragment;
    private GoogleMap mMap;
    Double Latitude = 0.0, Longitude = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.groundwatergooglemap);

        try {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                Latitude = Double.parseDouble(bundle.getString("lat"));
                Longitude = Double.parseDouble(bundle.getString("lon"));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        AppConstant.isWrite = false;
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_frag);
        supportMapFragment.getMapAsync(this);

        binding.btnCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    int countflag = 0;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        try {
            List<ArrayList<LatLng>> contourList = getContourList();
            if (contourList.size() > 0) {
                for (int i = 0; i < contourList.size(); i++) {
                    ArrayList<LatLng> contourvalue = contourList.get(i);

                    JSONObject obj = GroundwaterContours.getJSONObject(i);
                    String SelectedgridName = obj.getString("gridName");
                    String Actualgroundwater = obj.getString("Actualgroundwater");
                    String Estimatedgroundwater = obj.getString("Estimatedgroundwater");
                    String Title = SelectedgridName + " \nLY - " + Actualgroundwater + " \nFV - " + Estimatedgroundwater;

                    LatLng latlon = listLatLong.get(i);


                    mMap.addPolygon(addPolygon(contourvalue, listdivergance.get(i)));
//                    if (SelectedgridName != null && SelectedgridName.length() > 0) {
//                        mMap.addPolygon(addPolygon(contourvalue, listdivergance.get(i))).setTag(SelectedgridName);
//                    } else {
//                        mMap.addPolygon(addPolygon(contourvalue, listdivergance.get(i))).setTag("");
//
//                    }
                    //Herojit Icon Change
                    int height = 45;
                    int width = 45;
                    BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.icon_googlemappinpoint);
                    Bitmap b = bitmapdraw.getBitmap();
                    Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
                    BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(smallMarker);

                    mMap.addMarker(new MarkerOptions()
                            .position(latlon)
                            .title(SelectedgridName) // Proper title is not show due to lengthy
                            .icon(icon));
//                    Polyline polyline = googleMap.addPolyline(new PolylineOptions()
//                            .clickable(true)
//                            .addAll(contourvalue));
//                    // Store a data object with the polyline, used here to indicate an arbitrary type.
//                    polyline.setTag(SelectedgridName);

//                    Polygon polygon = mMap.addPolygon(addPolygon(contourvalue, listdivergance.get(i)));
////                    polygon.setFillColor(Color.YELLOW);
//                    polygon.setTag(SelectedgridName);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        // Position the map's camera near Alice Springs in the center of Australia,
        // and set the zoom factor so most of Australia shows on the screen.
//        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-23.684, 133.903), 4));
//        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Latitude, Longitude), 14.0f));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Latitude, Longitude), 14.0f));

        // Set listeners for click events.
//        googleMap.setOnPolylineClickListener(this);
        googleMap.setOnPolygonClickListener(this);
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        LatLng latlon = new LatLng(Latitude, Longitude);
        mMap.addMarker(new MarkerOptions()
                .position(latlon)
                .title(""));

//        mMap.getUiSettings().setMyLocationButtonEnabled(true);

    }


    @Override
    public void onPolygonClick(Polygon polygon) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public PolygonOptions addPolygon(ArrayList<LatLng> latlong, double divergance) {

        PolygonOptions polygonOptions = null;
        polygonOptions = new PolygonOptions();
        for (int i = 0; i < latlong.size(); i++) {
            LatLng ltlng = latlong.get(i);
            polygonOptions.add(ltlng);
            polygonOptions.strokeWidth(3);
//            polygonOptions.strokeColor(Color.RED);
//            polygonOptions.strokeColor(getResources().getColor(R.color.ColorPrimary));
//            polygonOptions.fillColor(getResources().getColor(R.color.un_selected_button_background));
            if (divergance > 0) {
                polygonOptions.fillColor(getResources().getColor(R.color.secondary_blue3));
            } else {
                polygonOptions.fillColor(getResources().getColor(R.color.lightorange));
            }
//            polygonOptions.strokeColor(getResources().getColor(R.color.gray));
//            polygonOptions.fillColor(Color.TRANSPARENT);
//            mMap.addPolygon(polygonOptions);
        }
        return polygonOptions;

    }

    public List<ArrayList<LatLng>> getContourList() {
        List<ArrayList<LatLng>> contourlist = new ArrayList<>();
        try {
            JSONArray jsonArray = GroundwaterContours;
            ArrayList<LatLng> latLngList = new ArrayList<>();
            String latit = null, longit = null;
            for (int i = 0; i < jsonArray.length(); i++) {
                latLngList = new ArrayList<>();
                try {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    String SelectedContour = obj.getString("Contour");
                    if (SelectedContour != null && SelectedContour.length() > 2) {
                        List<String> l_List = Arrays.asList(SelectedContour.split("-"));
                        for (int j = 0; j < l_List.size(); j++) {
                            String currentString = l_List.get(j);
                            if (currentString != null) {
                                String[] separated = currentString.split(",");
                                if (separated.length > 1) {
                                    latit = separated[0];
                                    longit = separated[1];
                                    latLngList.add(new LatLng(Double.valueOf(latit), Double.valueOf(longit)));
                                    if (Latitude == null || Latitude == 0.0 || Longitude == null || Longitude == 0.0) {
                                        Latitude = Double.valueOf(latit);
                                        Longitude = Double.valueOf(longit);
                                    }
                                }
                            }
                        }
                        contourlist.add(latLngList);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return contourlist;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("onResume Method", "onResume Method called");
        setLanguages();
    }


    @Override
    protected void onPause() {
        super.onPause();
//        if (db != null) {
//            db = new DBAdapter(this);
//        }
        Log.d("OnPause Method", "OnPause Method called");
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.d("onStop Method", "onStop Method called");
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void setLanguages() {
        setDynamicLanguage(this, binding.txtFarmname, "GroundWaterMap", R.string.GroundWaterMap);
        setDynamicLanguage(this, binding.txtImproved, "Improved", R.string.Improved);
        setDynamicLanguage(this, binding.txtDecline, "Decline", R.string.Decline);

        setFontsStyleTxt(this, binding.txtFarmname, 4);
        setFontsStyleTxt(this, binding.txtImproved, 5);
        setFontsStyleTxt(this, binding.txtDecline, 5);
    }

}
