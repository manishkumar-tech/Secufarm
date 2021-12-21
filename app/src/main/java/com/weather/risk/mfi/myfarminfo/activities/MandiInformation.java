package com.weather.risk.mfi.myfarminfo.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.adapter.MandiPriceAdapter;
import com.weather.risk.mfi.myfarminfo.bean.MandiPriceBean;
import com.weather.risk.mfi.myfarminfo.database.DBAdapter;
import com.weather.risk.mfi.myfarminfo.databinding.MandiinformationBinding;
import com.weather.risk.mfi.myfarminfo.home.AppController;
import com.weather.risk.mfi.myfarminfo.utils.AppConstant;
import com.weather.risk.mfi.myfarminfo.utils.AppManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.SN_Mandi;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getUIDforScreenTracking;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setScreenTracking;

public class MandiInformation extends AppCompatActivity {


    DBAdapter db;
    MandiinformationBinding binding;
    private double latitude = 0.0, longitude = 0.0;
    private GoogleMap mMap;
    private RecyclerView.LayoutManager mLayoutManager;
    String UID = "";
    ArrayList<OptimalMandiData> mandiArray = new ArrayList<>();
    ArrayList<Crop> cropArray = new ArrayList<>();
    ArrayList<String> varietyArray = new ArrayList<>();
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";
    RecyclerView listView;

    public String Guest_latitude = null;
    public String Guest_longitude = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.mandiinformation);
        db = new DBAdapter(this);

        if (AppConstant.latitude != null && AppConstant.longitude != null) {
            String aa = AppConstant.latitude;
            String bb = AppConstant.longitude;
            if (aa != null && bb != null) {
                latitude = Double.parseDouble(aa);
                longitude = Double.parseDouble(bb);
            }
        }


//
        String l = "https://myfarminfo.com//yfirest.svc/Mandi/CropVariety/" + latitude + "/" + longitude + "/" + 12;
//        Log.e("mandi_url", l);
        setUpMapIfNeeded(); // For setting up the MapFragment
//
//        // cropSpinner = (Spinner) view.findViewById(R.id.cropSpinner);
        binding.showBTN.setVisibility(View.GONE);

        binding.btnCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        binding.showBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mandiArray.size() > 0) {
                    mandiPopupMethod(mandiArray);
                } else {
                    Toast.makeText(MandiInformation.this, getResources().getString(R.string.Mandiisnotfound), Toast.LENGTH_SHORT).show();
                }
            }
        });
        //ScreenTracking
        UID = getUIDforScreenTracking();
        setScreenTracking(this, db, SN_Mandi, UID);

        if ((AppConstant.selected_crop != null && AppConstant.selected_crop.length() > 0)) {
            binding.txtCropName.setText(String.valueOf(AppConstant.selected_crop));
        } else {
            binding.txtCropName.setText("");
            binding.txtCropName.setVisibility(View.GONE);
        }
    }

    private void getVarietyList(final String latitude, final String longitude, final String cropId) {
        String farm_ID;
        String url;

        if (AppConstant.isLogin) {
            farm_ID = AppConstant.farm_id;
            url = "https://myfarminfo.com//yfirest.svc/Mandi/CropVariety/" + latitude + "/" + longitude + "/" + cropId;
        } else {
            farm_ID = "42688";
            url = "https://myfarminfo.com//yfirest.svc/Mandi/CropVariety/" + Guest_latitude + "/" + Guest_longitude + "/" + cropId;
        }

        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //to convert Date to String, use format method of SimpleDateFormat class.
        String strDate = dateFormat.format(date);
        db.open();


        final String finalstrDate = strDate;
        final String finalFramId = farm_ID;
        db.deleteCatchedData(finalFramId + "_" + cropId, "Variety_List", finalstrDate, "Active");

        Cursor cursor = db.getCatchedData(farm_ID + "_" + cropId, "Variety_List", strDate, "Active");
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            String dataOut = cursor.getString(cursor.getColumnIndex("datastring"));
            OperationVarietyList(dataOut, cropId);
        } else {
            final ProgressDialog dialoug = ProgressDialog.show(this, "",
                    getResources().getString(R.string.FetchingVariety), true);

            StringRequest stringVarietyRequest = new StringRequest(Request.Method.GET,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String varietyResponse) {
                            dialoug.cancel();

                            ///////////////////////////Insert in Catched Table//////
                            db.open();
                            long l = db.insertCatchedData(finalFramId + "_" + cropId, "Variety_List", varietyResponse.toString(), "Active");
                            if (l > 0)
                                db.deleteCatchedData(finalFramId + "_" + cropId, "Variety_List", finalstrDate, "Active");
                            db.close();

                            ////////////////////////
                            OperationVarietyList(varietyResponse, cropId);

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Log.e("eror", "MandiResponse: " + volleyError.getMessage());
                    Toast.makeText(MandiInformation.this, getResources().getString(R.string.Couldnotconnect), Toast.LENGTH_LONG).show();
                    dialoug.cancel();
                }
            });

            int socketTimeout = 60000;//60 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            stringVarietyRequest.setRetryPolicy(policy);

            AppController.getInstance().addToRequestQueue(stringVarietyRequest);
        }
    }

    public void OperationVarietyList(String varietyResponse, final String cropId) {
        try {
            System.out.println("Variety Respose : " + varietyResponse);
            varietyResponse = varietyResponse.trim();
            varietyResponse = varietyResponse.substring(1, varietyResponse.length() - 1);
            varietyResponse = varietyResponse.replace("\\", "");
            varietyArray = new ArrayList<>();

            if (varietyResponse.equalsIgnoreCase("NoData")) {
                Toast.makeText(this, getResources().getString(R.string.Dataisnotfound), Toast.LENGTH_LONG).show();
            }

            JSONArray jsonArray = new JSONArray(varietyResponse);
            if (jsonArray.length() > 0) {
                //   varietyArray.add("Select Variety");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String name = jsonObject.isNull("Variety") ? "" : jsonObject.getString("Variety");
                    varietyArray.add(name);
                }
            }
            ArrayAdapter<String> varietySpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, varietyArray);
            binding.varietySpinner.setAdapter(varietySpinnerAdapter);
            binding.varietySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (i >= 0) {
                        String variety = varietyArray.get(i);
                        getMandiDetail(cropId, variety, Double.toString(latitude), Double.toString(longitude));
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getMandiDetail(String cropId, String variety, String latitude, String longitude) {
//        /YFIRest.svc/Mandi/OptimalMandi/46/Other/25/77
        String farm_ID;
        String url;

        if (AppConstant.isLogin) {
            farm_ID = AppConstant.farm_id;
            url = "https://myfarminfo.com/YFIRest.svc/Mandi/OptimalMandi/" + cropId + "/" + variety + "/" + latitude + "/" + longitude;
        } else {
            farm_ID = "42688";
            url = "https://myfarminfo.com/YFIRest.svc/Mandi/OptimalMandi/" + cropId + "/" + variety + "/" + Guest_latitude + "/" + Guest_longitude;
        }


        mandiArray = new ArrayList<OptimalMandiData>();
        if (mMap != null) {
            mMap.clear();
        }
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //to convert Date to String, use format method of SimpleDateFormat class.
        String strDate = dateFormat.format(date);
        db.open();
        Cursor cursor = db.getCatchedData(farm_ID + "_" + cropId + "_" + variety, "Mandi_List", strDate, "Active");
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            String dataOut = cursor.getString(cursor.getColumnIndex("datastring"));
            OperationGetMandiDetail(dataOut);

        } else {
            final String finalstrDate = strDate;
            final String finalFramId = farm_ID;
            final String finalcropId = cropId;
            final String finalvariety = variety;
            final ProgressDialog dialoug = ProgressDialog.show(this, "",
                    getResources().getString(R.string.FetchingOptimalMandi), true);

            Log.v("mamamaam", url);
            // Request a string response from the provided URL.
            String strs = "https://myfarminfo.com/YFIRest.svc/Mandi/OptimalMandi/" + cropId + "/" + variety + "/" + latitude + "/" + longitude;
            String url1 = AppManager.getInstance().removeSpaceForUrl(url);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, "" + url1,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            dialoug.cancel();
                            // Display the first 500 characters of the response string.
                            System.out.println("Volley Response : " + response);

                            try {
                                response = response.trim();
                                response = response.substring(1, response.length() - 1);
                                response = response.replace("\\", "");
                                response = response.replace("\\", "");
                                response = response.replace("\"{", "{");
                                response = response.replace("}\"", "}");
                                response = response.replace("\"[", "[");
                                response = response.replace("]\"", "]");
                                ///////////////////////////Insert in Catched Table//////
                                db.open();
                                long l = db.insertCatchedData(finalFramId + "_" + finalcropId + "_" + finalvariety, "Mandi_List", response.toString(), "Active");
                                if (l > 0)
                                    db.deleteCatchedData(finalFramId + "_" + finalcropId + "_" + finalvariety, "Mandi_List", finalstrDate, "Active");
                                db.close();

                                ////////////////////////

                                OperationGetMandiDetail(response);
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(MandiInformation.this, getResources().getString(R.string.nno_data_found), Toast.LENGTH_LONG).show();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("Volley Error : " + error);
                    dialoug.cancel();
                }
            });

            int socketTimeout = 60000;//60 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            stringRequest.setRetryPolicy(policy);

            // Adding request to volley request queue
            AppController.getInstance().addToRequestQueue(stringRequest);
        }
    }

    public void OperationGetMandiDetail(String response) {
        try {
            JSONArray locationArray = new JSONArray(response);
            LatLngBounds.Builder bc = new LatLngBounds.Builder();
            for (int i = 0; i < locationArray.length(); i++) {
                JSONObject locationObject = locationArray.getJSONObject(i);
                OptimalMandiData data = new OptimalMandiData();
                data.setPrice(locationObject.getString("Price"));
                data.setLocation(locationObject.getString("Location"));
                data.setLatitude(locationObject.getDouble("Latitude"));
                data.setLongitude(locationObject.getDouble("Longitude"));
                data.setDistance(locationObject.getDouble("Distance"));
                data.setUnit(locationObject.getString("Unit"));
                data.setOptimal(locationObject.getString("Optimal"));
                data.setDate(locationObject.getString("Date"));

                if (mMap != null) {
                    BitmapDescriptor icon;
                    if (data.getOptimal() != null && data.getOptimal().equalsIgnoreCase("yes")) {

                                       /* int height = 140;
                                        int width = 140;
                                        BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.b_mandi_ic);
                                        Bitmap b=bitmapdraw.getBitmap();
                                        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);*/
                        icon = BitmapDescriptorFactory.fromResource(R.drawable.best_m_icon);
                        //    icon = BitmapDescriptorFactory.fromBitmap(smallMarker);

                    } else {
                        icon = BitmapDescriptorFactory.fromResource(R.drawable.mandinopt);
                    }
                    MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(data.getLatitude(), data.getLongitude()))
                            .title("Location : " + data.getLocation())
                            .snippet("Distance : " + data.getDistance())
                            .icon(icon);
                    Marker mMarker = mMap.addMarker(markerOptions);
                    if (mMarker != null) {
                        bc.include(mMarker.getPosition());
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(data.getLatitude(), data.getLongitude()), 10));
                        System.out.println("Marker has been added with " + data.getLatitude() + " , " + data.getLongitude() + " Location : " + data.getLocation());
                    }
                    data.setMarker(mMarker);
                }
                mandiArray.add(data);
            }
            if (mandiArray.size() > 0) {
                binding.showBTN.setVisibility(View.VISIBLE);

            } else {
                Toast.makeText(this, getResources().getString(R.string.Mandiisnotfound), Toast.LENGTH_SHORT).show();
                binding.showBTN.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, getResources().getString(R.string.nno_data_found), Toast.LENGTH_LONG).show();
        }
    }

    /*****
     * Sets up the map if it is possible to do so
     *****/
    public void setUpMapIfNeeded() {
        try {
            // Do a null check to confirm that we have not already instantiated the map.
            if (mMap == null) {
                // Try to obtain the map from the SupportMapFragment.
                ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mandiMap)).getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        mMap = googleMap;
                        setUpMap();

                        //  getCropList(String.valueOf(latitude), String.valueOf(longitude));
                        //getVarietyList(String.valueOf(latitude), String.valueOf(longitude), "12");
                        if (AppConstant.isLogin) {
//                        getVarietyList(String.valueOf(latitude), String.valueOf(longitude), "12");
                            getVarietyList(String.valueOf(latitude), String.valueOf(longitude), AppConstant.selected_cropId);
                        } else {
//                        getVarietyList(String.valueOf(Guest_latitude), String.valueOf(Guest_longitude), "12");
                            getVarietyList(String.valueOf(Guest_latitude), String.valueOf(Guest_longitude), AppConstant.selected_cropId);
                        }

                    }
                });
                // Check if we were successful in obtaining the map.


            }
        } catch (Exception ex) {
            ex.printStackTrace();
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

        if (AppConstant.isLogin) {
            mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("My Home").snippet("Home Address"));
            // For zooming automatically to the Dropped PIN Location
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 12.0f));
        } else {
            mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(Guest_latitude), Double.parseDouble(Guest_latitude))).title("My Home").snippet("Home Address"));
            // For zooming automatically to the Dropped PIN Location
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(Guest_latitude), Double.parseDouble(Guest_latitude)), 12.0f));
        }
    }

    public void noCropFound(String crop_nam) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Optimal mandi not found for this " + ", Please select another crop")
                .setCancelable(false)
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    public void mandiPopupMethod(ArrayList<OptimalMandiData> data) {

        final Dialog dialog = new Dialog(this, R.style.DialogSlideAnim);

        //  final Dialog dialog = new Dialog(this);

        dialog.setCanceledOnTouchOutside(true);
        Window window = dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);


        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.BOTTOM;
        wlp.dimAmount = 0.5f;

        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        // wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        // Include dialog.xml file
        dialog.setContentView(R.layout.optimal_mandi_popup);


        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, 750);

        listView = (RecyclerView) dialog.findViewById(R.id.mandi_price_listview);
        listView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        listView.setLayoutManager(mLayoutManager);


        dialog.show();


        if (data.size() > 0) {


            ArrayList<MandiPriceBean> list = new ArrayList<MandiPriceBean>();

            for (int i = 0; i < data.size(); i++) {
                MandiPriceBean bean = new MandiPriceBean();
                bean.setCommodity(data.get(i).getLocation());
                bean.setPrice(data.get(i).getPrice());
                bean.setVariety(String.valueOf(data.get(i).getDistance()));
                bean.setDate(data.get(i).getDate());
                bean.setIsbest(data.get(i).getOptimal());
                list.add(bean);

            }

            MandiPriceAdapter adapter = new MandiPriceAdapter(this, list);
            listView.setAdapter(adapter);
        } else {

        }

    }

    private class OptimalMandiData {

        String location;
        double latitude;
        double longitude;
        double distance;
        Marker marker;
        String price;
        String unit;
        String optimal;
        String date;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getOptimal() {
            return optimal;
        }

        public void setOptimal(String optimal) {
            this.optimal = optimal;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public double getDistance() {
            return distance;
        }

        public void setDistance(double distance) {
            this.distance = distance;
        }

        public Marker getMarker() {
            return marker;
        }

        public void setMarker(Marker marker) {
            this.marker = marker;
        }
    }

    private class Crop {
        private String cropId;
        private String cropName;

        public Crop(String cropId, String cropName) {
            this.cropId = cropId;
            this.cropName = cropName;
        }

        public String getCropId() {
            return cropId;
        }

        public void setCropId(String cropId) {
            this.cropId = cropId;
        }

        public String getCropName() {
            return cropName;
        }

        public void setCropName(String cropName) {
            this.cropName = cropName;
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (db != null) {
            db = new DBAdapter(this);
        }
        setScreenTracking(this, db, SN_Mandi, UID);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (db != null) {
            db = new DBAdapter(this);
        }
        Log.d("OnPause Method", "OnPause Method called");
        setScreenTracking(this, db, SN_Mandi, UID);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (db != null) {
            db = new DBAdapter(this);
        }
        Log.d("onStop Method", "onStop Method called");
        setScreenTracking(this, db, SN_Mandi, UID);
    }
}
