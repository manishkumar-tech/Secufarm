package com.weather.risk.mfi.myfarminfo.tubewell;

import android.Manifest;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

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
import com.weather.risk.mfi.myfarminfo.bean.ElectricStart;
import com.weather.risk.mfi.myfarminfo.bean.ElectricStatusBean;
import com.weather.risk.mfi.myfarminfo.bean.ElectricStop;
import com.weather.risk.mfi.myfarminfo.bean.Max1Bean;
import com.weather.risk.mfi.myfarminfo.bean.Max2Bean;
import com.weather.risk.mfi.myfarminfo.bean.Max3Bean;
import com.weather.risk.mfi.myfarminfo.bean.MaxCur1;
import com.weather.risk.mfi.myfarminfo.bean.MaxCur2;
import com.weather.risk.mfi.myfarminfo.bean.MaxCur3;
import com.weather.risk.mfi.myfarminfo.bean.Min1Bean;
import com.weather.risk.mfi.myfarminfo.bean.Min2Bean;
import com.weather.risk.mfi.myfarminfo.bean.Min3Bean;
import com.weather.risk.mfi.myfarminfo.bean.MotorStartBean;
import com.weather.risk.mfi.myfarminfo.bean.MotorStatus;
import com.weather.risk.mfi.myfarminfo.bean.MotorStopBean;
import com.weather.risk.mfi.myfarminfo.bean.MotorStopSatus;
import com.weather.risk.mfi.myfarminfo.bean.TubewellListBean;
import com.weather.risk.mfi.myfarminfo.bean.VodafoneBean;
import com.weather.risk.mfi.myfarminfo.entities.DataBean;
import com.weather.risk.mfi.myfarminfo.home.AppController;
import com.weather.risk.mfi.myfarminfo.utils.AppConstant;
import com.weather.risk.mfi.myfarminfo.utils.Utility;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static com.weather.risk.mfi.myfarminfo.utils.Utility.checkNoofDaysislessthan10;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getDynamicLanguageToast;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getDynamicLanguageValue;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setDynamicLanguage;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setFontsStyle;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setFontsStyleTxt;

/**
 * Created by Admin on 04-04-2018.
 */
public class VodafoneFragment extends Fragment {


    ArrayList<Max1Bean> arrayListMax1 = new ArrayList<Max1Bean>();
    ArrayList<Max1Bean> arrayListMax2 = new ArrayList<Max1Bean>();
    ArrayList<Max1Bean> arrayListMax3 = new ArrayList<Max1Bean>();
    ArrayList<Max1Bean> arrayListMin1 = new ArrayList<Max1Bean>();
    ArrayList<Max1Bean> arrayListMin2 = new ArrayList<Max1Bean>();
    ArrayList<Max1Bean> arrayListMin3 = new ArrayList<Max1Bean>();

    ArrayList<Max1Bean> arrayListMaxCur1 = new ArrayList<Max1Bean>();
    ArrayList<Max1Bean> arrayListMaxCur2 = new ArrayList<Max1Bean>();
    ArrayList<Max1Bean> arrayListMaxCur3 = new ArrayList<Max1Bean>();

    ArrayList<MotorStartBean> arrayListMotorStart = new ArrayList<MotorStartBean>();
    ArrayList<MotorStopBean> arrayListMotorStop = new ArrayList<MotorStopBean>();
    ArrayList<ElectricStatusBean> arrayListElectric = new ArrayList<ElectricStatusBean>();


    ArrayList<MotorStatus> arrayMotorStart = new ArrayList<MotorStatus>();
    ArrayList<MotorStopSatus> arrayMotorStop = new ArrayList<MotorStopSatus>();
    ArrayList<ElectricStop> arrayElectricStop = new ArrayList<ElectricStop>();
    ArrayList<ElectricStart> arrayElectricStart = new ArrayList<ElectricStart>();

    ArrayList<MandiData> tubewellArray = new ArrayList<>();


    String electricityON, electricityOFF, motorON, motorOFF;
    TextView date, toDate;
    private int mYear, mMonth, mDay;

    private String vodaArr[];
    String sDate;

    String userID = null;
    SharedPreferences prefs;
    private GoogleMap mMap;

    Spinner stationSpinner, stateSpinner, districtSpinner, statusSpinner;
    String vodafoneId = null;
    String vodafoneName = null;
    Button submitBTN;
    LinearLayout showBTN;
    Button minMaxBtn, min_max_vol, maxCurBtn, startStopBtn, elcStatusBtn;
    String tubewellId = null;
    String tubewellName = null;
    Double latitude = 26.4148;
    Double longitude = 80.2321;
    RecyclerView tubewell_list;
    TubewellAdapter adapter;

    String statusType = "All";
    ImageView btn_update;
    TextView txtffrom_date, txttto_date;

    public static VodafoneFragment newInstance() {
        VodafoneFragment fragment = new VodafoneFragment();

        return fragment;
    }

    public VodafoneFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.vodafone_fragment, container, false);
        //  ((DrawerLocker) getActivity()).setDrawerEnabled(false);

//        TextView farmInfo = (TextView) getActivity().findViewById(R.id.logo);
//        farmInfo.setText(getString(R.string.Tubewell));

        tubewell_list = (RecyclerView) view.findViewById(R.id.tubewell_listview);
        tubewell_list.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        tubewell_list.setLayoutManager(linearLayoutManager);


        btn_update = (ImageView) view.findViewById(R.id.btn_update);
        stationSpinner = (Spinner) view.findViewById(R.id.voda_station_Spinner);
        stateSpinner = (Spinner) view.findViewById(R.id.voda_state_spinner);
        districtSpinner = (Spinner) view.findViewById(R.id.voda_district_Spinner);
        submitBTN = (Button) view.findViewById(R.id.submit_voda);

        statusSpinner = (Spinner) view.findViewById(R.id.status_Spinner);

        showBTN = (LinearLayout) view.findViewById(R.id.voda_show_data_btn);

        minMaxBtn = (Button) view.findViewById(R.id.min_max_btn);
        min_max_vol = (Button) view.findViewById(R.id.min_max_vol);
        maxCurBtn = (Button) view.findViewById(R.id.max_cur_btn);
        startStopBtn = (Button) view.findViewById(R.id.start_stop_btn);
        elcStatusBtn = (Button) view.findViewById(R.id.elc_status_brn);

        final ArrayList<String> statusList = new ArrayList<String>();
        statusList.add("All");
        statusList.add("Electric Status");
        statusList.add("Motor Status");
        //  statusList.add("Current Status");
        statusList.add("Voltage Status");

        //Herojit Add
        minMaxBtn.setVisibility(View.GONE);

        ArrayAdapter<String> statusTypeAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, statusList);
        statusTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        statusSpinner.setAdapter(statusTypeAdapter);


        statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    statusType = statusList.get(position);
                } else {
                    statusType = "All";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                statusType = "All";

            }
        });


        ArrayList<String> stateList = new ArrayList<String>();
        stateList.add("Uttar Pradesh");

        ArrayAdapter<String> eventTypeAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, stateList);
        eventTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        stateSpinner.setAdapter(eventTypeAdapter);


        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });

        ArrayList<String> districtList = new ArrayList<String>();
        districtList.add("Kanpur Nagar");

        ArrayAdapter<String> distAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, districtList);
        distAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        districtSpinner.setAdapter(distAdapter);
        districtSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });


        minMaxBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent in = new Intent(getActivity(), ElectricStatusMain.class);
                    in.putExtra("list1", arrayListMax1);
                    in.putExtra("list2", arrayListMax2);
                    in.putExtra("list3", arrayListMax3);
                    in.putExtra("list4", arrayListMin1);
                    in.putExtra("list5", arrayListMin2);
                    in.putExtra("list6", arrayListMin3);
                    in.putExtra("list7", arrayListMaxCur1);
                    in.putExtra("list8", arrayListMaxCur2);
                    in.putExtra("list9", arrayListMaxCur3);
                    in.putExtra("list10", arrayListMotorStart);
                    in.putExtra("list11", arrayListElectric);
                    in.putExtra("list12", arrayElectricStart);
                    in.putExtra("list13", arrayElectricStop);
                    in.putExtra("list14", arrayMotorStart);
                    in.putExtra("list15", arrayMotorStop);
                    in.putExtra("status", "1");
                    in.putExtra("e_on", electricityON);
                    in.putExtra("e_off", electricityOFF);
                    in.putExtra("m_on", motorON);
                    in.putExtra("m_off", motorOFF);
                    startActivity(in);
                } catch (Exception ex) {
                    getDynamicLanguageToast(getActivity(), ex.toString());
                    ex.printStackTrace();
                }

            }
        });

        loadVodafoneData();

        prefs = getActivity().getSharedPreferences(AppConstant.SHARED_PREFRENCE_NAME, getActivity().MODE_PRIVATE);
        userID = prefs.getString(AppConstant.PREFRENCE_KEY_USER_ID, "");

        date = (TextView) view.findViewById(R.id.dateET_voda);
        toDate = (TextView) view.findViewById(R.id.dateToET_voda);
        txtffrom_date = (TextView) view.findViewById(R.id.txtffrom_date);
        txttto_date = (TextView) view.findViewById(R.id.txttto_date);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDatePicker(getActivity(), date, "DDMMYYYY");
            }
        });
        toDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDatePicker(getActivity(), toDate, "DDMMYYYY");
            }
        });

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = df.format(c);

        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_YEAR, -2);
        Date daysBeforeDate = cal.getTime();
        String formattedDate1 = df.format(daysBeforeDate);

        Log.v("dateee", formattedDate1 + "");
        date.setText(formattedDate1);
        toDate.setText(formattedDate);

        DecimalFormat mFormat = new DecimalFormat("00");

        submitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String da = date.getText().toString().trim();
                String da1 = toDate.getText().toString().trim();
                if (vodafoneId == null || vodafoneId.length() < 1) {
                    getDynamicLanguageToast(getActivity(), "Pleaseselectstation", R.string.Pleaseselectstation);
                } else if (da == null || da.length() < 5) {
                    getDynamicLanguageToast(getActivity(), "Pleaseselectfromdate", R.string.Pleaseselectfromdate);
                } else if (da1 == null || da1.length() < 5) {
                    getDynamicLanguageToast(getActivity(), "Pleaseselecttodate", R.string.Pleaseselecttodate);
                } else {
                    loadData(vodafoneId, da, da1);
                }

            }
        });


//        date.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                boolean resultCam = Utility.checkPermissionSMS(getActivity());
//                if (resultCam) {
//
//                    final Calendar c = Calendar.getInstance();
//                    mYear = c.get(Calendar.YEAR);
//                    mMonth = c.get(Calendar.MONTH);
//                    mDay = c.get(Calendar.DAY_OF_MONTH);
//
//                    // Launch Date Picker Dialog
//                    DatePickerDialog dpd = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
//                        @Override
//                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//
//
//                            DecimalFormat mFormat = new DecimalFormat("00");
//                            mFormat.format(Double.valueOf(year));
//                            mFormat.setRoundingMode(RoundingMode.DOWN);
//                            String Dates = mFormat.format(Double.valueOf(dayOfMonth)) + "-" + mFormat.format(Double.valueOf(monthOfYear + 1)) + "-" + mFormat.format(Double.valueOf(year));
//
//                            date.setText(Dates);
//
//
//                        }
//                    }, mYear, mMonth, mDay);
//                    //    dpd.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
//                    dpd.setTitle("Select Date");
//                    dpd.show();
//                }
//            }
//
//        });
//
//        toDate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                boolean resultCam = Utility.checkPermissionSMS(getActivity());
//                if (resultCam) {
//
//                    final Calendar c = Calendar.getInstance();
//                    mYear = c.get(Calendar.YEAR);
//                    mMonth = c.get(Calendar.MONTH);
//                    mDay = c.get(Calendar.DAY_OF_MONTH);
//
//                    // Launch Date Picker Dialog
//                    DatePickerDialog dpd = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
//                        @Override
//                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//
//
//                            DecimalFormat mFormat = new DecimalFormat("00");
//                            mFormat.format(Double.valueOf(year));
//                            mFormat.setRoundingMode(RoundingMode.DOWN);
//                            String Dates = mFormat.format(Double.valueOf(dayOfMonth)) + "-" + mFormat.format(Double.valueOf(monthOfYear + 1)) + "-" + mFormat.format(Double.valueOf(year));
//
//                            toDate.setText(Dates);
//                            refreshMethod();
//
//
//                        }
//                    }, mYear, mMonth, mDay);
//                    //    dpd.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
//                    dpd.setTitle("Select Date");
//                    dpd.show();
//                }
//            }
//
//        });

        try {
            tubewelllist(formattedDate1, formattedDate);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (date.getText().toString() == null && date.getText().toString().length() <= 0) {
                    getDynamicLanguageToast(getActivity(), "Pleaseselectfromdate", R.string.Pleaseselectfromdate);
                } else if (toDate.getText().toString() == null && toDate.getText().toString().length() <= 0) {
                    getDynamicLanguageToast(getActivity(), "Pleaseselecttodate", R.string.Pleaseselecttodate);
                } else {
                    //CHeck max 10 days
                    boolean check10days = checkNoofDaysislessthan10(date.getText().toString(), toDate.getText().toString());
                    if (check10days) {
                        tubewell_list.setAdapter(null);
                        tubewell_list.setVisibility(View.GONE);
                        minMaxBtn.setVisibility(View.GONE);
                        tubewelllist(date.getText().toString(), toDate.getText().toString());
                    } else {
                        getDynamicLanguageToast(getActivity(), "Pleaseentethedaterange", R.string.Pleaseentethedaterange);
                    }
                }
            }
        });


        return view;
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
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 12.0f));
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


    @Override
    public void onResume() {
        super.onResume();
        setLanguages();

    }


    ProgressDialog dialoug;

    public void loadVodafoneData() {
        dialoug = ProgressDialog.show(getActivity(), "",
                getDynamicLanguageValue(getActivity(), "loadingInformation", R.string.loadingInformation), true);
        String url = "https://myfarminfo.com/yfirest.svc/FillVodafoneStation2/0/0/" + AppConstant.user_id;
//        String url = "https://myfarminfo.com/yfirest.svc/FillVodafoneStation2/0/0/243694";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialoug.cancel();
                        // Display the first 500 characters of the response string.
                        System.out.println("Volley Response : " + response);

                        response = response.trim();
                        //   response = response.substring(1, response.length() - 1);
                        response = response.replace("\\", "");
                        response = response.replace("\\", "");
                        response = response.replace("\"{", "{");
                        response = response.replace("}\"", "}");
                        response = response.replace("\"[", "[");
                        response = response.replace("]\"", "]");


                        DataBean bean = new DataBean();
                        bean = getEventTypeList(response);

                        ArrayList<VodafoneBean> stationList = new ArrayList<VodafoneBean>();
                        stationList = bean.getVodaList();
                        vodaArr = new String[stationList.size()];
                        for (int i = 0; i < stationList.size(); i++) {
                            vodaArr[i] = stationList.get(i).getStationName();
                        }

                        ArrayAdapter<String> eventTypeAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, vodaArr);
                        eventTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        stationSpinner.setAdapter(eventTypeAdapter);

                        final DataBean finalBean = bean;
                        stationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                vodafoneId = finalBean.getVodaList().get(position).getID();
                                vodafoneName = finalBean.getVodaList().get(position).getStationName();

                                Log.v("printtt", vodafoneId + "--" + vodafoneName);

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                                vodafoneId = null;
                                vodafoneName = null;
                            }
                        });


                        if (mMap == null) {
                            // Try to obtain the map from the SupportMapFragment.
                            final String finalResponse = response;
                            final ArrayList<VodafoneBean> finalStationList = stationList;
                            ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.tubeMap)).getMapAsync(new OnMapReadyCallback() {
                                @Override
                                public void onMapReady(GoogleMap googleMap) {
                                    mMap = googleMap;
                                    setUpMap();

                                    if (finalStationList.size() > 0) {
                                        tubewellArray = new ArrayList<MandiData>();
                                        LatLngBounds.Builder bc = new LatLngBounds.Builder();
                                        for (int i = 0; i < finalStationList.size(); i++) {

                                            MandiData data = new MandiData();
                                            String locationId = finalStationList.get(i).getID();
                                            data.setLocationId(locationId);
                                            data.setLocation(finalStationList.get(i).getStationName());
                                            data.setLatitude(finalStationList.get(i).getLatitude());
                                            data.setLongitude(finalStationList.get(i).getLongitude());
                                            data.setDistance(finalStationList.get(i).getStatus());
                                            if (mMap != null) {
                                                BitmapDescriptor icon = null;
                                                if (data.getDistance().equalsIgnoreCase("Active")) {
                                                    icon = BitmapDescriptorFactory.fromResource(R.drawable.irr_green);
                                                } else {
                                                    icon = BitmapDescriptorFactory.fromResource(R.drawable.irr_grey);
                                                }

                                                if (data.getLatitude() != null && data.getLatitude().length() > 4) {

                                                    Log.v("llllll", data.getLatitude() + "--" + data.getLatitude().length());
                                                    MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(Double.parseDouble(data.getLatitude()), Double.parseDouble(data.getLongitude())))
                                                            .title("Tubewell Name : " + data.getLocation() + "@" + locationId)
                                                            .snippet("Status : " + data.getDistance())
                                                            .icon(icon);


                                                    Marker mMarker = mMap.addMarker(markerOptions);
                                                    if (mMarker != null) {
                                                        bc.include(mMarker.getPosition());
                                                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 8.0f));
                                                        System.out.println("Marker has been added with " + data.getLatitude() + " , " + data.getLongitude() + " Location : " + data.getLocation());
                                                    }
                                                    data.setMarker(mMarker);
                                                }
                                            }
                                            tubewellArray.add(data);
                                        }
                                        try {
                                            if (tubewellArray.size() > 0) {
                                                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bc.build(), 18));
//                                                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bc.build(), 50));
                                            }
                                        } catch (Exception ex) {
                                            ex.printStackTrace();
                                        }
                                    }

                                    // Setting a custom info window adapter for the google map
                                    mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                                        // Use default InfoWindow frame
                                        @Override
                                        public View getInfoWindow(Marker arg0) {
                                            return null;
                                        }

                                        // Defines the contents of the InfoWindow
                                        @Override
                                        public View getInfoContents(Marker arg0) {
                                            View v = getActivity().getLayoutInflater().inflate(R.layout.info_window, null);
                                            // Getting reference to the TextView to set latitude
                                            TextView tvLat = (TextView) v.findViewById(R.id.title);

                                            // Getting reference to the TextView to set longitude
                                            TextView tvLng = (TextView) v.findViewById(R.id.distance);
                                            System.out.println("Title : " + arg0.getTitle());
                                            if (arg0.getTitle() != null && arg0.getTitle().length() > 0) {
                                                // Getting the position from the marker

                                                String[] titleArray = arg0.getTitle().split("@");
                                                String title = titleArray[0];
                                                tubewellId = titleArray[1];
                                                tubewellName = title;
                                                final String sssss = arg0.getSnippet();
                                                String distance = arg0.getSnippet();
                                                tvLat.setText(title);
                                                tvLng.setText(distance);
                                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                                builder.setTitle(title).
                                                        setMessage(distance).
                                                        setPositiveButton("Get Data", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {

                                                                if (sssss.equalsIgnoreCase("Status : " + "Active")) {
                                                                    if (tubewellId != null) {
                                                                        dialogInterface.cancel();

                                                                        String da = date.getText().toString().trim();
                                                                        String da1 = toDate.getText().toString().trim();
                                                                        if (tubewellId == null || tubewellId.length() < 1) {
                                                                            getDynamicLanguageToast(getActivity(), "TubewellIDnotfound", R.string.TubewellIDnotfound);
                                                                        } else if (da == null || da.length() < 5) {
                                                                            getDynamicLanguageToast(getActivity(), "Pleaseselectfromdate", R.string.Pleaseselectfromdate);
                                                                        } else if (da1 == null || da1.length() < 5) {
                                                                            getDynamicLanguageToast(getActivity(), "Pleaseselecttodate", R.string.Pleaseselecttodate);
                                                                        } else {
                                                                            loadData(tubewellId, da, da1);
                                                                        }

                                                                    }
                                                                } else {
                                                                    getDynamicLanguageToast(getActivity(), "Tubewellisinactive", R.string.Tubewellisinactive);
                                                                }
                                                            }
                                                        }).
                                                        setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                                dialogInterface.cancel();
                                                            }
                                                        });
                                                builder.show();

                                            } else {
                                                // Setting the latitude
                                                tvLat.setText(String.valueOf(arg0.getPosition().latitude));
                                                // Setting the longitude
                                                tvLng.setText(String.valueOf(arg0.getPosition().longitude));
                                            }
                                            return v;
                                        }
                                    });
                                }
                            });
                            // Check if we were successful in obtaining the map.
                            if (mMap != null) {

                            }
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


    ProgressDialog dialoug2;

    public void tubewelllist(String Datafrom, String DataTo) {
        dialoug2 = ProgressDialog.show(getActivity(), "",
                getDynamicLanguageValue(getActivity(), "fetchingtubewellpleasewait", R.string.fetchingtubewellpleasewait), true);

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = df.format(c);

        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_YEAR, -6);
        Date daysBeforeDate = cal.getTime();
        String formattedDate1 = df.format(daysBeforeDate);
//        String url = "https://myfarminfo.com/yfirest.svc/getPumpStationData/" + formattedDate + "/" + formattedDate1 + "/" + AppConstant.user_id;
        String url = "https://myfarminfo.com/yfirest.svc/getPumpStationData/" + DataTo + "/" + Datafrom + "/" + AppConstant.user_id;
//        String url = "https://myfarminfo.com/yfirest.svc/getPumpStationData/" + formattedDate + "/" + formattedDate1 + "/243694";
//        String url = "https://myfarminfo.com/yfirest.svc/getPumpStationData/03-10-2019/27-10-2019/243694";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialoug2.cancel();
                        // Display the first 500 characters of the response string.


                        ArrayList<TubewellListBean> tubeList = new ArrayList<TubewellListBean>();

                        response = response.trim();
                        if (response != null) {
                            response = "" + Html.fromHtml(response);
                        }
                        //   response = response.substring(1, response.length() - 1);
                        response = response.replace("\\", "");
                        response = response.replace("\\", "");
                        response = response.replace("\"{", "{");
                        response = response.replace("}\"", "}");
                        response = response.replace("\"[", "[");
                        response = response.replace("]\"", "]");
                        System.out.println("Volley Response : " + response);
                        try {
                            JSONArray js = new JSONArray(response);
                            tubeList = new ArrayList<TubewellListBean>();
                            for (int i = 0; i < js.length(); i++) {
                                TubewellListBean bean = new TubewellListBean();
                                JSONObject jb = js.getJSONArray(i).getJSONObject(0);
                                bean.setTubewellname(jb.getString("Text"));
                                bean.setId(jb.getString("Id"));
                                bean.setIsActive(jb.getString("status"));
                                JSONArray jsonArray = js.getJSONArray(i);
                                bean.setResponse(jsonArray.toString());

                                tubeList.add(bean);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        if (tubeList.size() > 0) {
                            tubewell_list.setVisibility(View.VISIBLE);
                            final ArrayList<TubewellListBean> finalTubeList = tubeList;
                            adapter = new TubewellAdapter(getActivity(), finalTubeList, VodafoneFragment.this, new ItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    Log.d("TAG", "clicked position:" + position);
                                    //  long postId = data.get(position).getID();

                                    String da = date.getText().toString().trim();
                                    String da1 = toDate.getText().toString().trim();
                                    String id = finalTubeList.get(position).getId();

                                    if (id == null || id.length() < 1) {
                                        getDynamicLanguageToast(getActivity(), "TubewellIDnotfound", R.string.TubewellIDnotfound);
                                    } else if (da == null || da.length() < 5) {
                                        getDynamicLanguageToast(getActivity(), "Pleaseselectfromdate", R.string.Pleaseselectfromdate);
                                    } else if (da1 == null || da1.length() < 5) {
                                        getDynamicLanguageToast(getActivity(), "Pleaseselecttodate", R.string.Pleaseselecttodate);
                                    } else {
                                        loadData(id, da, da1);
                                    }

                                }
                            });
                            tubewell_list.setAdapter(adapter);


                        } else {
                            getDynamicLanguageToast(getActivity(), "Dataisnotfound", R.string.Dataisnotfound);
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialoug2.cancel();
                System.out.println("Volley Error : " + error);

            }
        });

        int socketTimeout = 60000;//60 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);

        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(stringRequest);
    }


    public void tubewellRefreshlist() {
        dialoug2 = ProgressDialog.show(getActivity(), "",
                getDynamicLanguageValue(getActivity(), "fetchingtubewellpleasewait", R.string.fetchingtubewellpleasewait), true);

        String fromd = date.getText().toString().trim();
        String toDat = toDate.getText().toString().trim();

        Log.v("urlurl", "https://myfarminfo.com/yfirest.svc/getPumpStationData/" + toDat + "/" + fromd);
        String url = "https://myfarminfo.com/yfirest.svc/getPumpStationData/" + toDat + "/" + fromd + "/" + AppConstant.user_id;
//        String url = "https://myfarminfo.com/yfirest.svc/getPumpStationData/" + toDat + "/" + fromd + "/243694";
//        String url = "https://myfarminfo.com/yfirest.svc/getPumpStationData/03-10-2019/27-10-2019/243694";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        dialoug2.cancel();
                        // Display the first 500 characters of the response string.


                        ArrayList<TubewellListBean> tubeList = new ArrayList<TubewellListBean>();

                        response = response.trim();
                        if (response != null) {
                            response = "" + Html.fromHtml(response);
                        }
                        //   response = response.substring(1, response.length() - 1);
                        response = response.replace("\\", "");
                        response = response.replace("\\", "");
                        response = response.replace("\"{", "{");
                        response = response.replace("}\"", "}");
                        response = response.replace("\"[", "[");
                        response = response.replace("]\"", "]");
                        System.out.println("Volley_Response_vodalist : " + response);
                        try {
                            JSONArray js = new JSONArray(response);
                            tubeList = new ArrayList<TubewellListBean>();
                            for (int i = 0; i < js.length(); i++) {
                                TubewellListBean bean = new TubewellListBean();
                                JSONObject jb = js.getJSONArray(i).getJSONObject(0);
                                bean.setTubewellname(jb.getString("Text"));
                                bean.setId(jb.getString("Id"));
                                bean.setIsActive(jb.getString("status"));
                                JSONArray jsonArray = js.getJSONArray(i);
                                bean.setResponse(jsonArray.toString());


                                tubeList.add(bean);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        if (tubeList.size() > 0) {
                            final ArrayList<TubewellListBean> finalTubeList = tubeList;
                            adapter = new TubewellAdapter(getActivity(), finalTubeList, VodafoneFragment.this, new ItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    Log.d("TAG", "clicked position:" + position);
                                    //  long postId = data.get(position).getID();

                                    String da = date.getText().toString().trim();
                                    String da1 = toDate.getText().toString().trim();
                                    String id = finalTubeList.get(position).getId();

                                    if (id == null || id.length() < 1) {
                                        getDynamicLanguageToast(getActivity(), "TubewellIDnotfound", R.string.TubewellIDnotfound);
                                    } else if (da == null || da.length() < 5) {
                                        getDynamicLanguageToast(getActivity(), "Pleaseselectfromdate", R.string.Pleaseselectfromdate);
                                    } else if (da1 == null || da1.length() < 5) {
                                        getDynamicLanguageToast(getActivity(), "Pleaseselecttodate", R.string.Pleaseselecttodate);
                                    } else {
                                        loadData(id, da, da1);
                                    }
                                }
                            });
                            tubewell_list.setAdapter(adapter);


                        } else {
                            getDynamicLanguageToast(getActivity(), "Dataisnotfound", R.string.Dataisnotfound);
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialoug2.cancel();
                System.out.println("Volley Error : " + error);

            }
        });

        int socketTimeout = 60000;//60 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);

        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(stringRequest);
    }


    public DataBean getEventTypeList(String response) {

        DataBean dataBean = new DataBean();
        ArrayList<VodafoneBean> eventTypeList = new ArrayList<VodafoneBean>();
        if (response != null) {
            try {

                JSONObject jb = new JSONObject(response);
                JSONArray jsonArray = jb.getJSONArray("VodaStation");

                for (int i = 0; i < jsonArray.length(); i++) {

                    VodafoneBean typeBean = new VodafoneBean();
                    typeBean.setID(jsonArray.getJSONObject(i).getString("ID"));
                    typeBean.setStationName(jsonArray.getJSONObject(i).getString("StationName"));
                    typeBean.setLongitude(jsonArray.getJSONObject(i).getString("Longitude"));
                    typeBean.setLatitude(jsonArray.getJSONObject(i).getString("Latitude"));
                    typeBean.setStatus(jsonArray.getJSONObject(i).getString("Status"));

                    eventTypeList.add(typeBean);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            dataBean.setVodaList(eventTypeList);

        }

        return dataBean;
    }


    ProgressDialog dialoug1;

    public void loadData(String id, String dat, String dat1) {

        electricityOFF = "100";
        electricityON = "0";
        motorOFF = "100";
        motorON = "0";
        dialoug1 = ProgressDialog.show(getActivity(), "",
                getDynamicLanguageValue(getActivity(), "loadingInformation", R.string.loadingInformation), true);
        Log.v("link", "https://myfarminfo.com/yfirest.svc/Clients/VodaFone/VodaData/" + id + "/" + dat + "/" + dat1);
        String url = "https://myfarminfo.com/yfirest.svc/Clients/VodaFone/VodaData/" + id + "/" + dat + "/" + dat1 + "/" + AppConstant.user_id;
//        String url = "https://myfarminfo.com/yfirest.svc/Clients/VodaFone/VodaData/" + id + "/" + dat + "/" + dat1 + "/243694";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialoug1.cancel();
                        // Display the first 500 characters of the response string.
                        System.out.println("data Response : " + response);

                        response = response.trim();
                        //   response = response.substring(1, response.length() - 1);
                        response = response.replace("\\", "");
                        response = response.replace("\\", "");
                        response = response.replace("\"{", "{");
                        response = response.replace("}\"", "}");
                        response = response.replace("\"[", "[");
                        response = response.replace("]\"", "]");

                        if (response != null) {

                            if (response.equalsIgnoreCase("\"NoData\"")) {

                                noDataMethod();

                            } else {
                                try {
                                    if (statusType != null && statusType.equalsIgnoreCase("All")) {
                                        minMaxBtn.setVisibility(View.VISIBLE);
                                    } else {
                                        minMaxBtn.setVisibility(View.GONE);
                                    }
                                    JSONObject jb = new JSONObject(response);
                                    JSONArray jsonArray = jb.getJSONArray("DT4");

                                    for (int i = 0; i < jsonArray.length(); i++) {//have to check

                                        String startTime = jsonArray.getJSONObject(i).getString("Motor_STARTtime");
                                        String stopTime = jsonArray.getJSONObject(i).getString("Motor_STOPtime");

                                    }


                                    JSONArray jsonArraypie = jb.getJSONArray("DT");

                                    electricityOFF = "100";
                                    electricityON = "0";
                                    motorOFF = "100";
                                    motorON = "0";
                                    for (int i = 0; i < jsonArraypie.length(); i++) {

                                        if (i == 0) {
                                            electricityON = jsonArraypie.getJSONObject(i).getString("OnRunTime");
                                            electricityOFF = jsonArraypie.getJSONObject(i).getString("OffRunTime");
                                            if (electricityON != null) {
                                                String[] splited = electricityON.split("\\s+");
                                                if (splited.length > 0) {
                                                    electricityON = splited[0];
                                                }
                                            } else {
                                                electricityON = "0";
                                            }

                                            if (electricityOFF != null) {
                                                String[] splited = electricityOFF.split("\\s+");
                                                if (splited.length > 0) {
                                                    electricityOFF = splited[0];
                                                }
                                            } else {
                                                electricityOFF = "100";
                                            }

                                        } else if (i == 1) {
                                            motorON = jsonArraypie.getJSONObject(i).getString("OnRunTime");
                                            motorOFF = jsonArraypie.getJSONObject(i).getString("OffRunTime");

                                            if (motorON != null) {
                                                String[] splited = motorON.split("\\s+");
                                                if (splited.length > 0) {
                                                    motorON = splited[0];
                                                }
                                            } else {
                                                motorON = "0";
                                            }

                                            if (motorOFF != null) {
                                                String[] splited = motorOFF.split("\\s+");
                                                if (splited.length > 0) {
                                                    motorOFF = splited[0];
                                                }
                                            } else {
                                                motorOFF = "100";
                                            }
                                        }

                                    }

                                    Log.v("el_on", electricityON);
                                    Log.v("el_off", electricityOFF);
                                    Log.v("el_on", motorON);
                                    Log.v("el_off", motorOFF);

                                    JSONArray jsonArray1 = jb.getJSONArray("lstChartDataSeries");

                                    arrayListMax1 = new ArrayList<Max1Bean>();
                                    arrayListMax2 = new ArrayList<Max1Bean>();
                                    arrayListMax3 = new ArrayList<Max1Bean>();
                                    arrayListMin1 = new ArrayList<Max1Bean>();
                                    arrayListMin2 = new ArrayList<Max1Bean>();
                                    arrayListMin3 = new ArrayList<Max1Bean>();

                                    arrayListMaxCur1 = new ArrayList<Max1Bean>();
                                    arrayListMaxCur2 = new ArrayList<Max1Bean>();
                                    arrayListMaxCur3 = new ArrayList<Max1Bean>();

                                    arrayListMotorStart = new ArrayList<MotorStartBean>();
                                    arrayListMotorStop = new ArrayList<MotorStopBean>();
                                    arrayListElectric = new ArrayList<ElectricStatusBean>();

                                    arrayMotorStart = new ArrayList<MotorStatus>();
                                    arrayMotorStop = new ArrayList<MotorStopSatus>();
                                    arrayElectricStart = new ArrayList<ElectricStart>();
                                    arrayElectricStop = new ArrayList<ElectricStop>();

                                    for (int i = 0; i < jsonArray1.length(); i++) {

                                        if (i == 0) {
                                            JSONArray jsonArray2 = jsonArray1.getJSONObject(0).getJSONArray("lstChartDataCl");
                                            // change he logic due to huge data
//                                            for (int j = 0; j < jsonArray2.length(); j++) {
//                                                Max1Bean max1Bean = new Max1Bean();
//
//                                                String value = jsonArray2.getJSONObject(j).getString("Value");
//                                                String xValueDT = jsonArray2.getJSONObject(j).getString("XValueDT");
//
//                                                DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//                                                Date date = format.parse(xValueDT);
//                                                String newString = new SimpleDateFormat("HH:mm").format(date);
//                                                max1Bean.setvValue("" + newString);
//                                                max1Bean.setyValue(value);
//                                                arrayListMax1.add(max1Bean);
//                                                //ojoj
//                                            }

                                            //Change 28072021
                                            setGraphArray(arrayListMax1, jsonArray2);

                                        } else if (i == 1) {
                                            JSONArray jsonArray2 = jsonArray1.getJSONObject(1).getJSONArray("lstChartDataCl");
//                                            for (int j = 0; j < jsonArray2.length(); j++) {
//                                                Max2Bean max2Bean = new Max2Bean();
//
//                                                String value = jsonArray2.getJSONObject(j).getString("Value");
//                                                String xValueDT = jsonArray2.getJSONObject(j).getString("XValueDT");
//
//                                                DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//                                                Date date = format.parse(xValueDT);
//                                                String newString = new SimpleDateFormat("HH:mm").format(date);
//                                                max2Bean.setvValue("" + newString);
//                                                max2Bean.setyValue(value);
//                                                arrayListMax2.add(max2Bean);
//                                            }

                                            //Change 28072021
                                            setGraphArray(arrayListMax2, jsonArray2);

                                        } else if (i == 2) {
                                            JSONArray jsonArray2 = jsonArray1.getJSONObject(2).getJSONArray("lstChartDataCl");
//                                            for (int j = 0; j < jsonArray2.length(); j++) {
//                                                Max3Bean max3Bean = new Max3Bean();
//
//                                                String value = jsonArray2.getJSONObject(j).getString("Value");
//                                                String xValueDT = jsonArray2.getJSONObject(j).getString("XValueDT");
//
//                                                DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//                                                Date date = format.parse(xValueDT);
//                                                String newString = new SimpleDateFormat("HH:mm").format(date);
//                                                max3Bean.setvValue("" + newString);
//                                                max3Bean.setyValue(value);
//                                                arrayListMax3.add(max3Bean);
//                                            }
                                            //Change 28072021
                                            setGraphArray(arrayListMax3, jsonArray2);


                                        } else if (i == 3) {
                                            JSONArray jsonArray2 = jsonArray1.getJSONObject(3).getJSONArray("lstChartDataCl");
//                                            for (int j = 0; j < jsonArray2.length(); j++) {
//                                                Min1Bean max1Bean = new Min1Bean();
//                                                String value = jsonArray2.getJSONObject(j).getString("Value");
//                                                String xValueDT = jsonArray2.getJSONObject(j).getString("XValueDT");
//
//                                                DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//                                                Date date = format.parse(xValueDT);
//                                                String newString = new SimpleDateFormat("HH:mm").format(date);
//                                                max1Bean.setvValue("" + newString);
//                                                max1Bean.setyValue(value);
//                                                arrayListMin1.add(max1Bean);
//                                            }
                                            //Change 28072021
                                            setGraphArray(arrayListMin1, jsonArray2);

                                        } else if (i == 4) {
                                            JSONArray jsonArray2 = jsonArray1.getJSONObject(4).getJSONArray("lstChartDataCl");
//                                            for (int j = 0; j < jsonArray2.length(); j++) {
//                                                {
//                                                    Min2Bean max2Bean = new Min2Bean();
//
//                                                    String value = jsonArray2.getJSONObject(j).getString("Value");
//                                                    String xValueDT = jsonArray2.getJSONObject(j).getString("XValueDT");
//
//                                                    DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//                                                    Date date = format.parse(xValueDT);
//                                                    String newString = new SimpleDateFormat("HH:mm").format(date);
//                                                    max2Bean.setvValue("" + newString);
//                                                    max2Bean.setyValue(value);
//                                                    arrayListMin2.add(max2Bean);
//                                                }
//                                            }
                                            //Change 28072021
                                            setGraphArray(arrayListMin2, jsonArray2);

                                        } else if (i == 5) {
                                            JSONArray jsonArray2 = jsonArray1.getJSONObject(5).getJSONArray("lstChartDataCl");
//                                            for (int j = 0; j < jsonArray2.length(); j++) {
//                                                    Min3Bean max3Bean = new Min3Bean();
//
//                                                    String value = jsonArray2.getJSONObject(j).getString("Value");
//                                                    String xValueDT = jsonArray2.getJSONObject(j).getString("XValueDT");
//
//                                                    DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//                                                    Date date = format.parse(xValueDT);
//                                                    String newString = new SimpleDateFormat("HH:mm").format(date);
//                                                    max3Bean.setvValue("" + newString);
//                                                    max3Bean.setyValue(value);
//                                                    arrayListMin3.add(max3Bean);
//                                            }

                                            //Change 28072021
                                            setGraphArray(arrayListMin3, jsonArray2);
                                        }
                                    }


                                    JSONArray series2 = jb.getJSONArray("lstChartDataSeries2");
                                    for (int i = 0; i < series2.length(); i++) {

                                        if (i == 0) {
                                            JSONArray jsonArray2 = series2.getJSONObject(0).getJSONArray("lstChartDataCl");
//                                            for (int j = 0; j < jsonArray2.length(); j++) {
//                                                MaxCur1 max1Bean = new MaxCur1();
//                                                String value = jsonArray2.getJSONObject(j).getString("Value");
//                                                String xValueDT = jsonArray2.getJSONObject(j).getString("XValueDT");
//
//                                                DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//                                                Date date = format.parse(xValueDT);
//                                                String newString = new SimpleDateFormat("dd-MMM HH:mm").format(date);
//                                                max1Bean.setxValue("" + newString);
//                                                max1Bean.setyValue(value);
//                                                arrayListMaxCur1.add(max1Bean);
//                                            }

                                            //Change 28072021
                                            setGraphArray(arrayListMaxCur1, jsonArray2);


                                        } else if (i == 1) {
                                            JSONArray jsonArray2 = series2.getJSONObject(1).getJSONArray("lstChartDataCl");
//                                            for (int j = 0; j < jsonArray2.length(); j++) {
//                                                MaxCur2 max2Bean = new MaxCur2();
//
//                                                String value = jsonArray2.getJSONObject(j).getString("Value");
//                                                String xValueDT = jsonArray2.getJSONObject(j).getString("XValueDT");
//
//                                                DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//                                                Date date = format.parse(xValueDT);
//                                                String newString = new SimpleDateFormat("dd-MMM HH:mm").format(date);
//                                                max2Bean.setxValue("" + newString);
//                                                max2Bean.setyValue(value);
//                                                arrayListMaxCur2.add(max2Bean);
//                                            }

                                            //Change 28072021
                                            setGraphArray(arrayListMaxCur2, jsonArray2);

                                        } else if (i == 2) {
                                            JSONArray jsonArray2 = series2.getJSONObject(2).getJSONArray("lstChartDataCl");
//                                            for (int j = 0; j < jsonArray2.length(); j++) {
//
//                                                MaxCur3 max3Bean = new MaxCur3();
//                                                String value = jsonArray2.getJSONObject(j).getString("Value");
//                                                String xValueDT = jsonArray2.getJSONObject(j).getString("XValueDT");
//
//                                                Log.v("SetMax3", "" + value);
//
//                                                DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//                                                Date date = format.parse(xValueDT);
//                                                String newString = new SimpleDateFormat("dd-MMM HH:mm").format(date);
//                                                max3Bean.setxValue("" + newString);
//                                                max3Bean.setyValue(value);
//                                                arrayListMaxCur3.add(max3Bean);
//                                            }

                                            //Change 28072021
                                            setGraphArray(arrayListMaxCur3, jsonArray2);


                                        }

                                    }


                                    JSONArray series3 = jb.getJSONArray("lstChartDataSeries3");
                                    for (int i = 0; i < series3.length(); i++) {

                                        if (i == 0) {
                                            JSONArray jsonArray2 = series3.getJSONObject(0).getJSONArray("lstChartDataCl");

                                            String prev = "2";

                                            for (int j = 0; j < jsonArray2.length(); j++) {

                                                MotorStartBean max1Bean = new MotorStartBean();
                                                MotorStatus motorStart = new MotorStatus();
                                                MotorStopSatus motorstop = new MotorStopSatus();

                                                String value = jsonArray2.getJSONObject(j).getString("Value");
                                                String xValueDT = jsonArray2.getJSONObject(j).getString("XValueDT");

                                                DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                                                Date date = format.parse(xValueDT);
                                                String newString = new SimpleDateFormat("HH:mm:ss").format(date);
                                                String newString1 = new SimpleDateFormat("dd-MM-yy HH:mm").format(date);
                                                String dateOnly = new SimpleDateFormat("dd-MM-yyyy").format(date);
                                                max1Bean.setxValue("" + newString1);
                                                max1Bean.setyValue(value);

                                                if (prev == null || prev.equalsIgnoreCase("2")) {
                                                    if (value != null && value.equalsIgnoreCase("1.0")) {
                                                        motorStart.setDate(dateOnly);
                                                        motorStart.setStartTime(newString);

                                                        Log.v("lll1", newString + "");
                                                        prev = value;
                                                    }
                                                } else if (prev.equalsIgnoreCase("1.0")) {
                                                    if (value != null && value.equalsIgnoreCase("0.0")) {
                                                        motorstop.setDate(dateOnly);
                                                        motorstop.setStopTime(newString);
                                                        Log.v("lll2", newString + "");
                                                        prev = value;
                                                    }
                                                } else if (prev.equalsIgnoreCase("0.0")) {
                                                    if (value != null && value.equalsIgnoreCase("1.0")) {
                                                        motorStart.setDate(dateOnly);
                                                        motorStart.setStartTime(newString);
                                                        prev = value;
                                                        Log.v("lll3", newString + "");
                                                    }
                                                }

                                                if (motorStart.getDate() != null) {
                                                    arrayMotorStart.add(motorStart);
                                                }

                                                if (motorstop.getDate() != null) {
                                                    arrayMotorStop.add(motorstop);
                                                }
                                                arrayListMotorStart.add(max1Bean);
                                            }


                                        } else if (i == 1) {
                                            JSONArray jsonArray2 = series3.getJSONObject(1).getJSONArray("lstChartDataCl");
                                            for (int j = 0; j < jsonArray2.length(); j++) {

                                                MotorStopBean max2Bean = new MotorStopBean();

                                                String value = jsonArray2.getJSONObject(j).getString("Value");
                                                String xValueDT = jsonArray2.getJSONObject(j).getString("XValueDT");

                                                DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                                                Date date = format.parse(xValueDT);
                                                String newString = new SimpleDateFormat("HH:mm").format(date);
                                                max2Bean.setxValue("" + newString);
                                                max2Bean.setyValue(value);
                                                arrayListMotorStop.add(max2Bean);
                                            }


                                        }
                                    }


                                    JSONArray series4 = jb.getJSONArray("lstChartDataSeries4");
                                    for (int i = 0; i < series4.length(); i++) {

                                        if (i == 0) {
                                            String prev = "2";
                                            JSONArray jsonArray2 = series4.getJSONObject(0).getJSONArray("lstChartDataCl");
                                            for (int j = 0; j < jsonArray2.length(); j++) {

                                                ElectricStart startStatus = new ElectricStart();
                                                ElectricStop stopStatus = new ElectricStop();
                                                ElectricStatusBean max1Bean = new ElectricStatusBean();
                                                String value = jsonArray2.getJSONObject(j).getString("Value");
                                                String xValueDT = jsonArray2.getJSONObject(j).getString("XValueDT");

                                                DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                                                Date date = format.parse(xValueDT);
                                                String newString = new SimpleDateFormat("HH:mm:ss").format(date);
                                                String dateOnly = new SimpleDateFormat("dd-MM-yyyy").format(date);
                                                String newString1 = new SimpleDateFormat("dd-MM-yy HH:mm").format(date);

                                                max1Bean.setxValue("" + newString1);
                                                max1Bean.setyValue(value);

                                                if (prev == null || prev.equalsIgnoreCase("2")) {
                                                    if (value != null && value.equalsIgnoreCase("1.0")) {
                                                        startStatus.setDate(dateOnly);
                                                        startStatus.setStartTime(newString);

                                                        Log.v("lll1", newString + "");
                                                        prev = value;
                                                    }
                                                } else if (prev.equalsIgnoreCase("1.0")) {
                                                    if (value != null && value.equalsIgnoreCase("0.0")) {
                                                        stopStatus.setDate(dateOnly);
                                                        stopStatus.setStopTime(newString);
                                                        Log.v("lll2", newString + "");
                                                        prev = value;
                                                    }
                                                } else if (prev.equalsIgnoreCase("0.0")) {
                                                    if (value != null && value.equalsIgnoreCase("1.0")) {
                                                        startStatus.setDate(dateOnly);
                                                        startStatus.setStartTime(newString);
                                                        prev = value;
                                                        Log.v("lll3", newString + "");
                                                    }
                                                }


                                                if (stopStatus.getDate() != null) {
                                                    arrayElectricStop.add(stopStatus);
                                                }


                                                if (startStatus.getDate() != null) {
                                                    arrayElectricStart.add(startStatus);
                                                }

                                                arrayListElectric.add(max1Bean);
                                            }


                                        }
                                    }


                                    if (statusType != null && statusType.equalsIgnoreCase("Electric Status")) {
                                        try {
                                            Intent in = new Intent(getActivity(), ElectricStatusMain.class);
                                            in.putExtra("list1", arrayListMax1);
                                            in.putExtra("list2", arrayListMax2);
                                            in.putExtra("list3", arrayListMax3);
                                            in.putExtra("list4", arrayListMin1);
                                            in.putExtra("list5", arrayListMin2);
                                            in.putExtra("list6", arrayListMin3);
                                            in.putExtra("list7", arrayListMaxCur1);
                                            in.putExtra("list8", arrayListMaxCur2);
                                            in.putExtra("list9", arrayListMaxCur3);
                                            in.putExtra("list10", arrayListMotorStart);
                                            in.putExtra("list11", arrayListElectric);
                                            in.putExtra("list12", arrayElectricStart);
                                            in.putExtra("list13", arrayElectricStop);
                                            in.putExtra("list14", arrayMotorStart);
                                            in.putExtra("list15", arrayMotorStop);
                                            in.putExtra("status", "0");
                                            in.putExtra("e_on", electricityON);
                                            in.putExtra("e_off", electricityOFF);
                                            in.putExtra("m_on", motorON);
                                            in.putExtra("m_off", motorOFF);
                                            startActivity(in);
                                        } catch (Exception e) {
                                            getDynamicLanguageToast(getActivity(), e.toString());
                                            e.printStackTrace();
                                        }

                                    } else if (statusType != null && statusType.equalsIgnoreCase("All")) {

                                        try {
                                            Intent in = new Intent(getActivity(), ElectricStatusMain.class);
                                            in.putExtra("list1", arrayListMax1);
                                            in.putExtra("list2", arrayListMax2);
                                            in.putExtra("list3", arrayListMax3);
                                            in.putExtra("list4", arrayListMin1);
                                            in.putExtra("list5", arrayListMin2);
                                            in.putExtra("list6", arrayListMin3);
                                            in.putExtra("list7", arrayListMaxCur1);
                                            in.putExtra("list8", arrayListMaxCur2);
                                            in.putExtra("list9", arrayListMaxCur3);
                                            in.putExtra("list10", arrayListMotorStart);
                                            in.putExtra("list11", arrayListElectric);
                                            in.putExtra("list12", arrayElectricStart);
                                            in.putExtra("list13", arrayElectricStop);
                                            in.putExtra("list14", arrayMotorStart);
                                            in.putExtra("list15", arrayMotorStop);
                                            in.putExtra("e_on", electricityON);
                                            in.putExtra("e_off", electricityOFF);
                                            in.putExtra("m_on", motorON);
                                            in.putExtra("m_off", motorOFF);
                                            in.putExtra("status", "1");
                                            startActivity(in);

                                        } catch (Exception e) {
                                            getDynamicLanguageToast(getActivity(), e.toString());
                                            e.printStackTrace();
                                        }
                                    } else if (statusType != null && statusType.equalsIgnoreCase("Motor Status")) {
                                        try {
                                            Intent in = new Intent(getActivity(), StartStopMain.class);
                                            in.putExtra("list1", arrayListMax1);
                                            in.putExtra("list2", arrayListMax2);
                                            in.putExtra("list3", arrayListMax3);
                                            in.putExtra("list4", arrayListMin1);
                                            in.putExtra("list5", arrayListMin2);
                                            in.putExtra("list6", arrayListMin3);
                                            in.putExtra("list7", arrayListMaxCur1);
                                            in.putExtra("list8", arrayListMaxCur2);
                                            in.putExtra("list9", arrayListMaxCur3);
                                            in.putExtra("list10", arrayListMotorStart);
                                            in.putExtra("list11", arrayMotorStart);
                                            in.putExtra("list12", arrayMotorStop);
                                            in.putExtra("m_on", motorON);
                                            in.putExtra("m_off", motorOFF);

                                            in.putExtra("status", "0");
                                            startActivity(in);
                                        } catch (Exception e) {
                                            getDynamicLanguageToast(getActivity(), e.toString());
                                            e.printStackTrace();
                                        }
                                    } else if (statusType != null && statusType.equalsIgnoreCase("Current Status")) {

                                        try {
                                            Intent in = new Intent(getActivity(), MaxCurFragment.class);
                                            in.putExtra("list1", arrayListMax1);
                                            in.putExtra("list2", arrayListMax2);
                                            in.putExtra("list3", arrayListMax3);
                                            in.putExtra("list4", arrayListMin1);
                                            in.putExtra("list5", arrayListMin2);
                                            in.putExtra("list6", arrayListMin3);
                                            in.putExtra("list7", arrayListMaxCur1);
                                            in.putExtra("list8", arrayListMaxCur2);
                                            in.putExtra("list9", arrayListMaxCur3);

                                            in.putExtra("status", "0");
                                            startActivity(in);
                                        } catch (Exception e) {
                                            getDynamicLanguageToast(getActivity(), e.toString());
                                            e.printStackTrace();
                                        }
                                    } else if (statusType != null && statusType.equalsIgnoreCase("Voltage Status")) {

                                        try {
                                            Intent in = new Intent(getActivity(), MinMaxLineChart.class);
                                            in.putExtra("list1", arrayListMax1);
                                            in.putExtra("list2", arrayListMax2);
                                            in.putExtra("list3", arrayListMax3);
                                            in.putExtra("list4", arrayListMin1);
                                            in.putExtra("list5", arrayListMin2);
                                            in.putExtra("list6", arrayListMin3);

                                            startActivity(in);
                                        } catch (Exception e) {
                                            getDynamicLanguageToast(getActivity(), e.toString());
                                            e.printStackTrace();
                                        }
                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }

                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialoug1.cancel();
                System.out.println("Volley Error : " + error);

            }
        }

        );

        int socketTimeout = 60000;//60 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);

        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(stringRequest);
    }


    private void noDataMethod() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getDynamicLanguageValue(getActivity(), "no_data", R.string.no_data)).
                setMessage(getDynamicLanguageValue(getActivity(), "Nodataisavailablesubmitted", R.string.Nodataisavailablesubmitted)).
                setPositiveButton(getDynamicLanguageValue(getActivity(), "Ok", R.string.Ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();

                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void refreshMethod() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Refresh Tubewell").
                setMessage("Do you want to refresh tubewell list?").
                setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        minMaxBtn.setVisibility(View.GONE);
                        tubewellRefreshlist();
                    }
                }).
                setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public void setGraphArray(ArrayList<Max1Bean> list, JSONArray jsonArray) {
        try {
            if (jsonArray != null && jsonArray.length() > 0) {
                int size = jsonArray.length();
                if (size <= 200) {
                    for (int j = 0; j < jsonArray.length(); j++) {
                        Max1Bean max1Bean = new Max1Bean();
                        String value = jsonArray.getJSONObject(j).getString("Value");
                        String xValueDT = jsonArray.getJSONObject(j).getString("XValueDT");

                        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                        Date date = format.parse(xValueDT);
                        String newString = new SimpleDateFormat("HH:mm").format(date);
                        max1Bean.setvValue("" + newString);
                        max1Bean.setyValue(value);
                        list.add(max1Bean);
                    }
                } else if (size > 200 && size <= 400) {
                    for (int j = 0; j < jsonArray.length(); j++) {
                        if (j % 2 == 0) {
                            Max1Bean max1Bean = new Max1Bean();
                            String value = jsonArray.getJSONObject(j).getString("Value");
                            String xValueDT = jsonArray.getJSONObject(j).getString("XValueDT");

                            DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                            Date date = format.parse(xValueDT);
                            String newString = new SimpleDateFormat("HH:mm").format(date);
                            max1Bean.setvValue("" + newString);
                            max1Bean.setyValue(value);
                            list.add(max1Bean);
                        }
                    }
                } else if (size > 400 && size <= 600) {
                    for (int j = 0; j < jsonArray.length(); j++) {
                        if (j % 3 == 0) {
                            Max1Bean max1Bean = new Max1Bean();
                            String value = jsonArray.getJSONObject(j).getString("Value");
                            String xValueDT = jsonArray.getJSONObject(j).getString("XValueDT");

                            DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                            Date date = format.parse(xValueDT);
                            String newString = new SimpleDateFormat("HH:mm").format(date);
                            max1Bean.setvValue("" + newString);
                            max1Bean.setyValue(value);
                            list.add(max1Bean);
                        }
                    }
                } else if (size > 600 && size <= 800) {
                    for (int j = 0; j < jsonArray.length(); j++) {
                        if (j % 4 == 0) {
                            Max1Bean max1Bean = new Max1Bean();
                            String value = jsonArray.getJSONObject(j).getString("Value");
                            String xValueDT = jsonArray.getJSONObject(j).getString("XValueDT");

                            DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                            Date date = format.parse(xValueDT);
                            String newString = new SimpleDateFormat("HH:mm").format(date);
                            max1Bean.setvValue("" + newString);
                            max1Bean.setyValue(value);
                            list.add(max1Bean);
                        }
                    }
                } else if (size > 800) {
                    for (int j = 0; j < jsonArray.length(); j++) {
                        if (j % 5 == 0) {
                            Max1Bean max1Bean = new Max1Bean();
                            String value = jsonArray.getJSONObject(j).getString("Value");
                            String xValueDT = jsonArray.getJSONObject(j).getString("XValueDT");

                            DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                            Date date = format.parse(xValueDT);
                            String newString = new SimpleDateFormat("HH:mm").format(date);
                            max1Bean.setvValue("" + newString);
                            max1Bean.setyValue(value);
                            list.add(max1Bean);
                        }
                    }
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
//    ArrayList<Max1Bean> arrayListMax1 = new ArrayList<Max1Bean>();
//    ArrayList<Max2Bean> arrayListMax2 = new ArrayList<Max2Bean>();
//    ArrayList<Max3Bean> arrayListMax3 = new ArrayList<Max3Bean>();
//    ArrayList<Min1Bean> arrayListMin1 = new ArrayList<Min1Bean>();
//    ArrayList<Min2Bean> arrayListMin2 = new ArrayList<Min2Bean>();
//    ArrayList<Min3Bean> arrayListMin3 = new ArrayList<Min3Bean>();
//
//    ArrayList<MaxCur1> arrayListMaxCur1 = new ArrayList<MaxCur1>();
//    ArrayList<MaxCur2> arrayListMaxCur2 = new ArrayList<MaxCur2>();
//    ArrayList<MaxCur3> arrayListMaxCur3 = new ArrayList<MaxCur3>();

    public void setDatePicker(Context context, final TextView txt, final String flag) {

        final Dialog datepic = new Dialog(context);
        Window window = datepic.getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);
        datepic.setContentView(R.layout.datepicker);
        datepic.setCanceledOnTouchOutside(true);
        Button btnset = (Button) datepic.findViewById(R.id.btn_Set);
        Button btnclear = (Button) datepic.findViewById(R.id.btn_Clear);
        Button btncancel = (Button) datepic.findViewById(R.id.btn_cancel);

        datepic.show();
        // datepic.getWindow().setLayout(240, 190);
        datepic.getWindow().setLayout(ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT);
        final DatePicker datetext = (DatePicker) datepic
                .findViewById(R.id.datepicker);
        btncancel.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                datepic.dismiss();
            }
        });

        btnclear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                txt.setText("");
                datepic.dismiss();

            }
        });
        btnset.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                int dt = datetext.getDayOfMonth();
                int month = datetext.getMonth();
                int year = datetext.getYear();
                int mnth = month + 1;
                String sDate;
                if (dt < 10) {
                    sDate = "0" + String.valueOf(dt);
                } else {
                    sDate = String.valueOf(dt);
                }
                String sMonth;
                if (mnth < 10) {
                    sMonth = "0" + String.valueOf(mnth);
                } else {
                    sMonth = String.valueOf(mnth);
                }
                String sSellectedDate = "";
                if (flag.equalsIgnoreCase("DDMMYYYY")) {
                    sSellectedDate = sDate + "-" + sMonth + "-"
                            + String.valueOf(year);
                } else if (flag.equalsIgnoreCase("YYYYMMDD")) {
                    sSellectedDate = String.valueOf(year) + "-" + sMonth + "-"
                            + sDate;
                }
                txt.setText(sSellectedDate);
                datepic.dismiss();

                if (date.getText().toString() == null && date.getText().toString().length() <= 0) {
                    getDynamicLanguageToast(getActivity(), "Pleaseselectfromdate", R.string.Pleaseselectfromdate);
                } else if (toDate.getText().toString() == null && toDate.getText().toString().length() <= 0) {
                    getDynamicLanguageToast(getActivity(), "Pleaseselecttodate", R.string.Pleaseselecttodate);
                } else {
                    //CHeck max 10 days
                    boolean check10days = checkNoofDaysislessthan10(date.getText().toString(), toDate.getText().toString());
                    if (check10days) {
                        tubewell_list.setAdapter(null);
                        tubewell_list.setVisibility(View.GONE);
                        minMaxBtn.setVisibility(View.GONE);
                        tubewelllist(date.getText().toString(), toDate.getText().toString());
                    } else {
                        getDynamicLanguageToast(getActivity(), "Pleaseentethedaterange", R.string.Pleaseentethedaterange);
                    }
                }
            }
        });

        datepic.show();

    }

    public void setAdapterclick(ArrayList<TubewellListBean> finalTubeList, int position) {
        try {
            String da = date.getText().toString().trim();
            String da1 = toDate.getText().toString().trim();
            String id = finalTubeList.get(position).getId();

            if (id == null || id.length() < 1) {
                getDynamicLanguageToast(getActivity(), "TubewellIDnotfound", R.string.TubewellIDnotfound);
            } else if (da == null || da.length() < 5) {
                getDynamicLanguageToast(getActivity(), "Pleaseselectfromdate", R.string.Pleaseselectfromdate);
            } else if (da1 == null || da1.length() < 5) {
                getDynamicLanguageToast(getActivity(), "Pleaseselecttodate", R.string.Pleaseselecttodate);
            } else {
                loadData(id, da, da1);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public void setLanguages() {

        TextView farmInfo = (TextView) getActivity().findViewById(R.id.logo);
//        farmInfo.setText(getString(R.string.Tubewell));

        setFontsStyleTxt(getActivity(), farmInfo, 2);
        setFontsStyleTxt(getActivity(), txtffrom_date, 6);
        setFontsStyleTxt(getActivity(), date, 5);
        setFontsStyleTxt(getActivity(), txttto_date, 6);
        setFontsStyleTxt(getActivity(), toDate, 5);

        setFontsStyle(getActivity(), min_max_vol);
        setFontsStyle(getActivity(), maxCurBtn);
        setFontsStyle(getActivity(), startStopBtn);
        setFontsStyle(getActivity(), elcStatusBtn);
        setFontsStyle(getActivity(), minMaxBtn);

        setDynamicLanguage(getActivity(), farmInfo, "Tubewell", R.string.Tubewell);
        setDynamicLanguage(getActivity(), txtffrom_date, "ffrom_date", R.string.ffrom_date);
        setDynamicLanguage(getActivity(), txttto_date, "tto_date", R.string.tto_date);

        setDynamicLanguage(getActivity(), min_max_vol, "min_max_vol", R.string.min_max_vol);
        setDynamicLanguage(getActivity(), maxCurBtn, "max_current", R.string.max_current);
        setDynamicLanguage(getActivity(), startStopBtn, "motor_start_stop", R.string.motor_start_stop);
        setDynamicLanguage(getActivity(), elcStatusBtn, "electric_status", R.string.electric_status);
        setDynamicLanguage(getActivity(), minMaxBtn, "Next", R.string.Next);

    }

}
