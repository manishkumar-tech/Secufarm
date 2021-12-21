package com.weather.risk.mfi.myfarminfo.mapfragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
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
import com.weather.risk.mfi.myfarminfo.adapter.NematodeAdapter;
import com.weather.risk.mfi.myfarminfo.adapter.ShowFutureAdapter;
import com.weather.risk.mfi.myfarminfo.bean.FutureDataBean;
import com.weather.risk.mfi.myfarminfo.bean.NematodeBean;
import com.weather.risk.mfi.myfarminfo.bean.VodafoneBean;
import com.weather.risk.mfi.myfarminfo.database.DBAdapter;
import com.weather.risk.mfi.myfarminfo.entities.DataBean;
import com.weather.risk.mfi.myfarminfo.entities.VillageBean;
import com.weather.risk.mfi.myfarminfo.home.AppController;
import com.weather.risk.mfi.myfarminfo.pest_disease.CropBean;
import com.weather.risk.mfi.myfarminfo.utils.AppConstant;
import com.weather.risk.mfi.myfarminfo.utils.AppManager;
import com.weather.risk.mfi.myfarminfo.utils.TransparentProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.SN_NematodeFragment;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getUIDforScreenTracking;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setScreenTracking;

/**
 * Created by Admin on 16-02-2018.
 */
public class NematodeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";
    private static final String STATE_ID = "stateId";
    String crop_name = null;


    public NematodeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    Spinner cropSpinner;
    private String cropArr[];
    String cropId = null;
    String cropName = null;
    String soilId = null;
    String soilName = null;

    Button searchButton;

    String lat, lon, lat1, lon1;
    String stateId;
    RecyclerView recyclerView;
    private GoogleMap mMap;

    String UID = "";
    DBAdapter db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.nematode, container, false);

        cropSpinner = (Spinner) view.findViewById(R.id.nematode_crops_spinner);
        stateId = AppConstant.stateID;

        db = new DBAdapter(getActivity());

        recyclerView = (RecyclerView) view.findViewById(R.id.nematode_recycler);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);


        SharedPreferences prefs = getActivity().getSharedPreferences(AppConstant.SHARED_PREFRENCE_NAME, getActivity().MODE_PRIVATE);
        lat1 = prefs.getString("lat", "23");
        lon1 = prefs.getString("lon", "77");


        if (lat1 == null || lon1 == null) {
            lat = AppConstant.latitude;
            lon = AppConstant.longitude;
        }
        if (lat == null || lon == null) {
            lat = "23.4829533333333";
            lon = "77.4829533333333";
        } else {
            lat = lat1;
            lon = lon1;
        }

        String aa = AppConstant.latitude;
        String bb = AppConstant.longitude;
        if (aa != null && bb != null) {

            lat = AppConstant.latitude;
            lon = AppConstant.longitude;

        }
        searchButton = (Button) view.findViewById(R.id.nematode_submit);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cropId == null || cropId.length() < 5) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.PleaseselectCrop), Toast.LENGTH_SHORT).show();
                } else {

                    String array1[] = cropId.split(":");

                    if (array1.length > 0) {

                        getNematodeReport(array1[0]);
                    } else {
                        Toast.makeText(getActivity(), getResources().getString(R.string.CropIDnotfound), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        stateId = AppConstant.stateID;

        loadCropList(stateId);

        //ScreenTracking
        UID = getUIDforScreenTracking();
        setScreenTracking(getActivity(), db, SN_NematodeFragment, UID);

        return view;
    }

    public void loadCropList(String stateId) {
        cropName = null;
        final TransparentProgressDialog dialoug = new TransparentProgressDialog(getActivity(),
                getResources().getString(R.string.Dataisloading));
        dialoug.show();
        Log.v("knsknklanl", "https://myfarminfo.com/yfirest.svc/getNematodes/" + lat + "/" + lon);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://myfarminfo.com/yfirest.svc/getNematodes/" + lat + "/" + lon,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialoug.cancel();
                        // Display the first 500 characters of the response string.
                        System.out.println("Volley crop feasibility Response : " + response);

                        response = response.trim();
                        response = response.substring(1, response.length() - 1);
                        response = response.replace("\\", "");


                        DataBean bean = new DataBean();
                        bean = getCropTypeList(response);
                        ArrayList<CropBean> cropList = new ArrayList<CropBean>();
                        cropList = bean.getCropList();
                        if (cropList.size() > 0) {
                            cropArr = new String[cropList.size() + 1];
                            cropArr[0] = "select crop";
                            for (int i = 0; i < cropList.size(); i++) {
                                cropArr[i + 1] = cropList.get(i).getCropName();

                                String nn = cropList.get(i).getCropId();
                                String name = cropList.get(i).getCropName();
                                String compareValue = AppConstant.selected_cropId;
                                if (compareValue != null && compareValue.equalsIgnoreCase(nn)) {
                                    crop_name = name;
                                }
                            }

                            ArrayAdapter<String> eventTypeAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, cropArr);
                            cropSpinner.setAdapter(eventTypeAdapter);
                            if (crop_name != null) {
                                int spinnerPosition = eventTypeAdapter.getPosition(crop_name);
                                cropSpinner.setSelection(spinnerPosition);
                            } else {
                                noCropFound(crop_name);
                            }
                            final DataBean finalBean = bean;
                            cropSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    if (position > 0) {
                                        cropId = finalBean.getCropList().get(position).getCropId();
                                        cropName = finalBean.getCropList().get(position).getCropName();

                                    } else {
                                        cropId = null;
                                        cropName = null;
                                    }


                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialoug.cancel();
                System.out.println("Volley Error : " + error);
            }
        });

        int socketTimeout = 60000;//60 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);

        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void setUpMap() {
        // For showing a move to my loction button
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
        // For dropping a marker at a point on the Map
        // mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("My Home").snippet("Home Address"));
        // For zooming automatically to the Dropped PIN Location
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(lat), Double.parseDouble(lon)), 12.0f));
    }


    private void getNematodeReport(String id) {

        final TransparentProgressDialog dialoug = new TransparentProgressDialog(getActivity(),
                getResources().getString(R.string.Dataisloading));
        dialoug.show();
        String ss = AppManager.getInstance().removeSpaceForUrl("https://myfarminfo.com/yfirest.svc/NematodeImages/9" + "/" + id + "/" + lat + "/" + lon);

        StringRequest stringFeasibilityRequest = new StringRequest(Request.Method.GET, ss,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String cropfeasibleResponse) {
                        dialoug.cancel();
                        try {
                            System.out.println("Feasibility Respose : " + cropfeasibleResponse);
                            cropfeasibleResponse = cropfeasibleResponse.trim();

                            cropfeasibleResponse = cropfeasibleResponse.replace("\\", "");
                            cropfeasibleResponse = cropfeasibleResponse.replace("\\", "");
                            cropfeasibleResponse = cropfeasibleResponse.replace("\\", "");
                            cropfeasibleResponse = cropfeasibleResponse.replace("\"{", "{");
                            cropfeasibleResponse = cropfeasibleResponse.replace("}\"", "}");
                            cropfeasibleResponse = cropfeasibleResponse.replace("\"[", "[");
                            cropfeasibleResponse = cropfeasibleResponse.replace("]\"", "]");

                            JSONArray jArray1 = new JSONArray(cropfeasibleResponse);
                            if (jArray1.length() > 0) {
                                JSONArray jArray = jArray1.getJSONArray(0);

                                ArrayList<NematodeBean> arrayList = new ArrayList<NematodeBean>();

                                for (int i = 0; i < jArray.length(); i++) {
                                    NematodeBean bean = new NematodeBean();
                                    bean.setName(jArray.getJSONObject(i).getString("Name"));
                                    bean.setNematode_id(jArray.getJSONObject(i).getString("nematode_id"));
                                    bean.setLongMin(jArray.getJSONObject(i).getString("xmin"));
                                    bean.setLongMax(jArray.getJSONObject(i).getString("xmax"));
                                    bean.setLatMin(jArray.getJSONObject(i).getString("ymin"));
                                    bean.setLatMax(jArray.getJSONObject(i).getString("ymax"));
                                    bean.setImage(jArray.getJSONObject(i).getString("ImageName"));
                                    arrayList.add(bean);


                                }

                                if (mMap == null) {
                                    // Try to obtain the map from the SupportMapFragment.
                                    final ArrayList<MandiData> tubewellArray = new ArrayList<MandiData>();
                                    final ArrayList<NematodeBean> finalStationList = arrayList;
                                    ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.tubeMap)).getMapAsync(new OnMapReadyCallback() {
                                        @Override
                                        public void onMapReady(GoogleMap googleMap) {
                                            mMap = googleMap;
                                            setUpMap();
                                            if (finalStationList.size() > 0) {
                                                LatLngBounds.Builder bc = new LatLngBounds.Builder();
                                                for (int i = 0; i < finalStationList.size(); i++) {
                                                    MandiData data = new MandiData();
                                                    String locationId = finalStationList.get(i).getNematode_id();
                                                    data.setLocationId(locationId);
                                                    data.setLocation("" + finalStationList.get(i).getName());
                                                    data.setLatitude(finalStationList.get(i).getLatMin());
                                                    data.setLongitude(finalStationList.get(i).getLongMin());
                                                    data.setDistance(finalStationList.get(i).getImage());
                                                    if (mMap != null) {
                                                        BitmapDescriptor icon = null;
                                                        icon = BitmapDescriptorFactory.fromResource(R.drawable.irr_grey);
                                                        if (data.getLatitude() != null && data.getLatitude().length() > 4) {

                                                            Log.v("llllll", data.getLatitude() + "--" + data.getLatitude().length());
                                                            MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(Double.parseDouble(data.getLatitude()), Double.parseDouble(data.getLongitude())))
                                                                    .title(" Name : " + data.getLocation() + "@" + locationId)
                                                                    .snippet("Status : " + data.getDistance())
                                                                    .icon(icon);
                                                            Marker mMarker = mMap.addMarker(markerOptions);
                                                            if (mMarker != null) {
                                                                bc.include(mMarker.getPosition());
                                                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(lat), Double.parseDouble(lon)), 8.0f));
                                                                System.out.println("Marker has been added with " + data.getLatitude() + " , " + data.getLongitude() + " Location : " + data.getLocation());
                                                            }
                                                            data.setMarker(mMarker);
                                                        }
                                                    }
                                                    tubewellArray.add(data);
                                                }
                                                if (tubewellArray.size() > 0) {
                                                    mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bc.build(), 50));
                                                }
                                            }
                                        }
                                    });
                                    // Check if we were successful in obtaining the map.
                                    if (mMap != null) {

                                    }
                                }
                                if (arrayList.size() > 0) {
                                    NematodeAdapter adapter = new NematodeAdapter(getActivity(), arrayList);
                                    recyclerView.setAdapter(adapter);
                                } else {
                                    Toast.makeText(getActivity(), getResources().getString(R.string.Nodatafoundcrop), Toast.LENGTH_SHORT).show();
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getActivity(), getResources().getString(R.string.Couldnotconnect), Toast.LENGTH_LONG).show();
                dialoug.cancel();
            }
        });

        int socketTimeout = 60000;//60 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringFeasibilityRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(stringFeasibilityRequest);
    }


    public DataBean getCropTypeList(String response) {

        DataBean dataBean = new DataBean();
        ArrayList<CropBean> cropTypeList = new ArrayList<CropBean>();
        if (response != null) {
            try {


                JSONArray jsonArray = new JSONArray(response);


                for (int i = 0; i < jsonArray.length(); i++) {

                    CropBean typeBean = new CropBean();
                    typeBean.setCropName(jsonArray.getJSONObject(i).getString("cropName"));
                    typeBean.setCropId(jsonArray.getJSONObject(i).getString("Nematode"));
                    cropTypeList.add(typeBean);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            dataBean.setCropList(cropTypeList);


        }

        return dataBean;
    }

    private class MandiData {
        String locationId;
        String location;
        String latitude;
        String longitude;
        String distance;
        Marker marker;

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public String getLocationId() {
            return locationId;
        }

        public void setLocationId(String locationId) {
            this.locationId = locationId;
        }

        public String getLocation() {
            return location;
        }


        public Marker getMarker() {
            return marker;
        }

        public void setMarker(Marker marker) {
            this.marker = marker;
        }
    }

    public void noCropFound(String crop_nam) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getResources().getString(R.string.Nodatafoundcrop) + " " + crop_nam + " " + getResources().getString(R.string.Pleaseselectanothercrop))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.Ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (db != null) {
            db = new DBAdapter(getActivity());
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        if (db != null) {
            db = new DBAdapter(getActivity());
        }
        Log.d("OnPause Method", "OnPause Method called");
    }

    @Override
    public void onStop() {
        super.onStop();
        if (db != null) {
            db = new DBAdapter(getActivity());
        }
        Log.d("onStop Method", "onStop Method called");
        setScreenTracking(getActivity(), db, SN_NematodeFragment, UID);
    }

}

