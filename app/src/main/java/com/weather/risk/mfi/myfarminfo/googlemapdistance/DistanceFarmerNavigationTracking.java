package com.weather.risk.mfi.myfarminfo.googlemapdistance;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.utils.ConnectionDetector;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.weather.risk.mfi.myfarminfo.googlemapdistance.DataParser.GoogleMapDistancefromCurrenttoFarmerLocation;
import static com.weather.risk.mfi.myfarminfo.mapfragments.LatLonCellID.currentLat;
import static com.weather.risk.mfi.myfarminfo.mapfragments.LatLonCellID.currentLon;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.checkLocationOnOff;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setEnableLocation;

public class DistanceFarmerNavigationTracking extends AppCompatActivity {
//https://www.androidtutorialpoint.com/intermediate/google-maps-draw-path-two-points-using-google-directions-google-map-android-api-v2/

    private GoogleMap mMap;
    private ArrayList<LatLng> markerPoints;

    Button btn_cross, btn_Go, btn_Stop;
    String FarmLag, FarmLong;
    Double farmer_lag, farmer_long;
    LatLng Current_fromDistance, Des_toDistance;
    TextView txt_farmname;
    ConnectionDetector connectionDetector;
    int netpopcheck = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.distancefarmernavigationtracking);

        connectionDetector = new ConnectionDetector(this);
        // Initializing
        markerPoints = new ArrayList<LatLng>();

        btn_cross = (Button) findViewById(R.id.btn_cross);
        btn_Go = (Button) findViewById(R.id.btn_Go);
        btn_Stop = (Button) findViewById(R.id.btn_Stop);
        txt_farmname = (TextView) findViewById(R.id.txt_farmname);

        FarmLag = getIntent().getStringExtra("FarmerLatitude");
        FarmLong = getIntent().getStringExtra("FarmerLongitude");

        if (FarmLag != null && FarmLag.length() > 0) {
            farmer_lag = Double.valueOf(FarmLag);
        }
        if (FarmLong != null && FarmLong.length() > 0) {
            farmer_long = Double.valueOf(FarmLong);
        }

        Current_fromDistance = new LatLng(currentLat, currentLon);
        Des_toDistance = new LatLng(farmer_lag, farmer_long);

        setMapDistance();
        if (!checkLocationOnOff(DistanceFarmerNavigationTracking.this)) {
            setEnableLocation(DistanceFarmerNavigationTracking.this);
        }

        //Herojit Add DIstance value
        setTextDistance(getIntent().getStringExtra("Distance"));

        btn_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_Go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer();
                btn_Go.setVisibility(View.GONE);
                btn_Stop.setVisibility(View.VISIBLE);
            }
        });
        btn_Stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopTimertask();
                btn_Go.setVisibility(View.VISIBLE);
                btn_Stop.setVisibility(View.GONE);
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

//                        mMap.setMyLocationEnabled(true);
//                        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                        Current_fromDistance = new LatLng(currentLat, currentLon);
                        addmaker(Current_fromDistance);
                        addmaker(Des_toDistance);


                    }
                }
            });

        } else {
            mMap.clear();
            Current_fromDistance = new LatLng(currentLat, currentLon);
            addmaker(Current_fromDistance);
            addmaker(Des_toDistance);
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

    // Fetches data from url passed
    private class FetchUrl extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                Log.d("Background Task data", data.toString());
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                ParserTask parserTask = new ParserTask();

                // Invokes the thread for parsing the JSON data
                parserTask.execute(result);
            } catch (Exception ex) {
                ex.printStackTrace();
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
            Log.d("downloadUrl", data.toString());
            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                Log.d("ParserTask", jsonData[0].toString());
                DataParser parser = new DataParser();
                Log.d("ParserTask", parser.toString());

                // Starts parsing data
                routes = parser.parse(jObject);
                Log.d("ParserTask", "Executing routes");
                Log.d("ParserTask", routes.toString());

            } catch (Exception e) {
                Log.d("ParserTask", e.toString());
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points;
            PolylineOptions lineOptions = null;
            try {
                // Traversing through all the routes
                for (int i = 0; i < result.size(); i++) {
                    points = new ArrayList<>();
                    lineOptions = new PolylineOptions();

                    // Fetching i-th route
                    List<HashMap<String, String>> path = result.get(i);

                    // Fetching all the points in i-th route
                    for (int j = 0; j < path.size(); j++) {
                        HashMap<String, String> point = path.get(j);

                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);

                        points.add(position);
                    }

                    // Adding all the points in the route to LineOptions
                    lineOptions.addAll(points);
                    lineOptions.width(10);
                    lineOptions.color(Color.RED);

                    Log.d("onPostExecute", "onPostExecute lineoptions decoded");

                }

                // Drawing polyline in the Google Map for the i-th route
                if (lineOptions != null) {
                    mMap.addPolyline(lineOptions);
                } else {
                    Log.d("onPostExecute", "without Polylines drawn");
                }
                //Herojit Add DIstance value
                if (!GoogleMapDistancefromCurrenttoFarmerLocation.equalsIgnoreCase("0 mt")) {
                    setTextDistance(GoogleMapDistancefromCurrenttoFarmerLocation);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Waypoints
        String waypoints = "";
        for (int i = 2; i < markerPoints.size(); i++) {
            LatLng point = (LatLng) markerPoints.get(i);
            if (i == 2)
                waypoints = "waypoints=";
            waypoints += point.latitude + "," + point.longitude + "|";
        }

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + waypoints;

        // Output format
        String output = "json";

        String key = "key=" + getResources().getString(R.string.browser_key);  //previous key
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&" + key;

        return url;
    }

    public void addmaker(LatLng point) {

        try {
            // Already two locations
            if (markerPoints.size() > 1) {
                markerPoints.clear();
                mMap.clear();
            }

            // Adding new item to the ArrayList
            markerPoints.add(point);

            // Creating MarkerOptions
            MarkerOptions options = new MarkerOptions();

            // Setting the position of the marker
            options.position(point);

            /**
             * For the start location, the color of marker is GREEN and
             * for the end location, the color of marker is RED.
             */
            if (markerPoints.size() == 1) {
                options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            } else if (markerPoints.size() == 2) {
                options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            }


            // Add new marker to the Google Map Android API V2
            mMap.addMarker(options);

            // Checks, whether start and end locations are captured
            if (markerPoints.size() >= 2) {
                LatLng origin = markerPoints.get(0);
                LatLng dest = markerPoints.get(1);

                // Getting URL to the Google Directions API
                String url = getDirectionsUrl(origin, dest);
                Log.d("onMapClick", url.toString());
                FetchUrl fetchUrl = new FetchUrl();

                // Start downloading json data from Google Directions API
                fetchUrl.execute(url);
                //move map camera
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(origin));
//            mMap.animateCamera(CameraUpdateFactory.zoomTo(13.0f));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setTextDistance(String val) {
        String value = getResources().getString(R.string.DistancefromCurrent);
        if (val != null && val.length() > 0) {
            value = value + " : " + val;
        } else {
            value = value + " : 0 mt";
        }

        txt_farmname.setText(value);

    }


    Timer timer;
    TimerTask timerTask;
    final Handler mHandler = new Handler();

    //Geo tagging
    public void startTimer() {
        //set a new Timer
        timer = new Timer();
        //initialize the TimerTask's job
        initializeTimerTask();
        //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
        timer.schedule(timerTask, 100, 2000); //
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
                if (connectionDetector.isConnectingToInternet()) {
                    setMapDistance();
                } else {
                    if (netpopcheck == 0)
                        noInternetMethoddd();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    };

    private void noInternetMethoddd() {
        netpopcheck = 1;
        AlertDialog.Builder builder = new AlertDialog.Builder(DistanceFarmerNavigationTracking.this);
        builder.setTitle(getResources().getString(R.string.InternetError)).
                setMessage(getResources().getString(R.string.Doyouwantrefresh)).
                setPositiveButton(getResources().getString(R.string.Yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        netpopcheck = 0;
                        Intent gps = new Intent(
                                Settings.ACTION_WIFI_SETTINGS);
                        startActivity(gps);
                    }
                }).
                setNegativeButton(getResources().getString(R.string.No), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        netpopcheck = 0;
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();

    }
}