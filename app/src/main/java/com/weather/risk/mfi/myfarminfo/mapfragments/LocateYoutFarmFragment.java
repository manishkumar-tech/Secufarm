package com.weather.risk.mfi.myfarminfo.mapfragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.activities.CustomSearchableSpinner;
import com.weather.risk.mfi.myfarminfo.activities.SignUpActivity;
import com.weather.risk.mfi.myfarminfo.database.DBAdapter;
import com.weather.risk.mfi.myfarminfo.entities.AllFarmDetail;
import com.weather.risk.mfi.myfarminfo.entities.CropQueryData;
import com.weather.risk.mfi.myfarminfo.entities.FarmInformationData;
import com.weather.risk.mfi.myfarminfo.services.AuthenticateService;
import com.weather.risk.mfi.myfarminfo.home.ExternalStorageGPS;
import com.weather.risk.mfi.myfarminfo.activities.NewHomeScreen;
import com.weather.risk.mfi.myfarminfo.utils.AppConstant;
import com.weather.risk.mfi.myfarminfo.utils.AppManager;
import com.weather.risk.mfi.myfarminfo.utils.CustomHttpClient;
import com.weather.risk.mfi.myfarminfo.utils.TransparentProgressDialog;
import com.weather.risk.mfi.myfarminfo.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import io.requery.android.database.sqlite.SQLiteStatement;

import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.FarmEditActive;
import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.FarmEditDetails;
import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.FarmRegistration_AadharNo;
import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.FarmRegistration_ProjectID;
import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.SN_LocateYoutFarmFragment;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.dateCheckgreater;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getDynamicLanguageToast;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getDynamicLanguageValue;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getUIDforScreenTracking;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setCustomSearchableSpinner;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setScreenTracking;
/*
import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.isLogin;
import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.user_id;*/

public class LocateYoutFarmFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String CALLING_ACTIVITY = "callingActivity", FARM_NAME = "FarmName", ALL_POINTS = "AllLatLngPount", AREA = "area";
    public static String DistrictID = null, DistrictName = null, SubDistrictID = null, SubDistrictName = null,
            VillageID = null, VillageName = null, ProjectID = null, ProjectName = null, user_id = "", data = null;

    boolean isLogin = false;
    EditText farmerName, farmerNumber, editText_FatherName, farmArea, farmName, valueN, valueP,
            OtherVillageName_edt, OthersubDistrict_edt, valueK, otherNutrition, plotArea;
    Spinner baselDoseSpiner, yourConcernSpiner;
    CustomSearchableSpinner cropSpiner1, varietySpiner, spin_project, stateList, DistrictName_spin, SubDistrictName_spin, VillageName_spin;
    ArrayList<String> applyBasalDose;
    LinearLayout ly, ll_nutrition;
    FarmInformationData farmInformationData;
    AllFarmDetail allFarmDetail;

    Calendar myCalendar;
    ArrayList<String> yourConcern;
    int displayDateInEditText;
    Button crop1, crop2, crop3, crop4, submitForm;
    ArrayList<CropQueryData> cropQueryData = new ArrayList<>();
    String latitude, longitude, stateId, concId = null, baselD = null, allDrawLatLngPoint, creatString;
    SharedPreferences prefs;
    TextView farmInfo, please, farmN, farmerN, farmerNu, stateN, taggedF_A, actual_F_A, cropN,
            varietyN, sow_p_f, sow_p_t, have_you, whatis, emailBTN, title;
    TextView sowPeriodFrom, sowPeriodTo;
    CheckBox createAccountCheckBox;
    boolean isCheckBox = false, isVarietySelection = false;
    Cursor varietyCursor, allCrop;
    //Add Herojit
    ArrayList<HashMap<String, String>> Projects = new ArrayList<>();
    ArrayList<HashMap<String, String>> States = new ArrayList<>();
    ArrayList<HashMap<String, String>> Districts = new ArrayList<>();
    ArrayList<HashMap<String, String>> SubDistricts = new ArrayList<>();
    ArrayList<HashMap<String, String>> Villages = new ArrayList<>();
    ArrayList<String> AadharList = new ArrayList<>();
    ArrayList<String> PhoneType = new ArrayList<>();
    ImageView imge_state_refresh, project_refresh, imge_crop_refresh;
    RadioGroup radiogroup_aadhar;
    RadioButton rb_aadhar, rb_other;
    LinearLayout tblrow_other;
    EditText edtother, edtaadhar_no, edtnoofbags, edt_IBFarmerCode;
    LinearLayout ll_OtherDistrict, ll_OtherVillageName, ll_noofbags;
    Spinner spin_MobileType, spin_getAadharlsit;
    String PhoneTypeValue = "", AadharID_No = "";
    TableRow tbl_getAadhar;

    String FarmRegistration_FarmName = null, FarmRegistration_FarmerName = null,
            FarmRegistration_FarmerPhno = null, FarmRegistration_FatherName = null,
            FarmRegistration_PanNumber = null, FarmRegistration_StateID = null, FarmRegistration_DistrictID = null,
            FarmRegistration_Sub_District = null, FarmRegistration_VillageID = null;


    // TODO: Rename and change types of parameters
    private int callingActivity;
    private String selectedFarmName;
    private String area;
    private CropQueryData selectedCropQueryCount = null;

    DBAdapter db;
    String UID = "";
    String FarmRegistration_CropID = null, FarmRegistration_CropName = null, FarmRegistration_Variety = null, FarmRegistration_VillageStr = null;
//    Button btn_cross;

    private View.OnClickListener crop1ClickListner = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            System.out.println("crop query length : " + cropQueryData.size());
            if (cropQueryData.size() > 0) {
                selectedCropQueryCount = cropQueryData.get(0);
            } else {
                selectedCropQueryCount = new CropQueryData();
                cropQueryData.add(selectedCropQueryCount);
            }

            crop1.setBackgroundResource(R.drawable.clicked_line);
            crop2.setBackgroundResource(R.drawable.line);
            crop3.setBackgroundResource(R.drawable.line);
            crop4.setBackgroundResource(R.drawable.line);
            displayDetailOfSelectedFarm(selectedFarmName, selectedCropQueryCount);
        }
    };
    private View.OnClickListener crop2ClickListner = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if (isValidate()) {

                if (cropQueryData.size() > 1) {
                    selectedCropQueryCount = cropQueryData.get(1);
                } else {
                    selectedCropQueryCount = new CropQueryData();
                    cropQueryData.add(selectedCropQueryCount);
                }


//                selectedCropQueryCount = cropQueryData.get(1);
                crop1.setBackgroundResource(R.drawable.line);
                crop2.setBackgroundResource(R.drawable.clicked_line);
                crop3.setBackgroundResource(R.drawable.line);
                crop4.setBackgroundResource(R.drawable.line);
                displayDetailOfSelectedFarm(selectedFarmName, selectedCropQueryCount);
            }
        }
    };


    /////////////////function decleared//////////////////// function decleared//////////////////////function decleared//////////////////////
    private View.OnClickListener crop3ClickListner = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if (isValidate()) {

                if (cropQueryData.size() > 2) {
                    selectedCropQueryCount = cropQueryData.get(2);
                } else {
                    selectedCropQueryCount = new CropQueryData();
                    cropQueryData.add(selectedCropQueryCount);
                }


//                selectedCropQueryCount = cropQueryData.get(2);
                crop1.setBackgroundResource(R.drawable.line);
                crop2.setBackgroundResource(R.drawable.line);
                crop3.setBackgroundResource(R.drawable.clicked_line);
                crop4.setBackgroundResource(R.drawable.line);
                displayDetailOfSelectedFarm(selectedFarmName, selectedCropQueryCount);
            }
        }
    };
    private View.OnClickListener crop4ClickListner = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if (isValidate()) {

                if (cropQueryData.size() > 3) {
                    selectedCropQueryCount = cropQueryData.get(3);
                } else {
                    selectedCropQueryCount = new CropQueryData();
                    cropQueryData.add(selectedCropQueryCount);
                }


//                selectedCropQueryCount = cropQueryData.get(3);
                crop1.setBackgroundResource(R.drawable.line);
                crop2.setBackgroundResource(R.drawable.line);
                crop3.setBackgroundResource(R.drawable.line);
                crop4.setBackgroundResource(R.drawable.clicked_line);
                displayDetailOfSelectedFarm(selectedFarmName, selectedCropQueryCount);
            }
        }
    };


    public LocateYoutFarmFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LocateYoutFarmFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LocateYoutFarmFragment newInstance(String callingActivty, String farmName, String AllLatLngPount, String area) {
        LocateYoutFarmFragment fragment = new LocateYoutFarmFragment();
        Bundle args = new Bundle();
        args.putString(CALLING_ACTIVITY, callingActivty);
        args.putString(FARM_NAME, farmName);
        args.putString(ALL_POINTS, AllLatLngPount);
        args.putString(AREA, area);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            callingActivity = Integer.valueOf(getArguments().getString(CALLING_ACTIVITY));
            selectedFarmName = getArguments().getString(FARM_NAME);
            allDrawLatLngPoint = getArguments().getString(ALL_POINTS);
            area = getArguments().getString(AREA);
            System.out.println("locate your farm fragment Area : " + area);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_locate_yout_farm, container, false);
        SharedPreferences pref = getActivity().getSharedPreferences(AppConstant.SHARED_PREFRENCE_NAME, getActivity().MODE_PRIVATE);

        AppConstant.user_id = pref.getString(AppConstant.PREFRENCE_KEY_USER_ID, "");
        AppConstant.isLogin = pref.getBoolean(AppConstant.PREFRENCE_KEY_ISLOGIN, false);

        isLogin = AppConstant.isLogin;
        user_id = AppConstant.user_id;

        Log.v("editedfarm", "" + allDrawLatLngPoint);

        Log.v("allpoint", allDrawLatLngPoint + "");

//        btn_cross = (Button) view.findViewById(R.id.btn_cross);

        createAccountCheckBox = (CheckBox) view.findViewById(R.id.checkForAccount);
        isCheckBox = true;
        createAccountCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    isCheckBox = true;
                } else {
                    isCheckBox = false;
                }
            }
        });
//        btn_cross.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getActivity().finish();
//            }
//        });
        title = (TextView) view.findViewById(R.id.farm_inf);
        db = new DBAdapter(getActivity());
        db.open();
        ly = (LinearLayout) view.findViewById(R.id.linearLayout);
        ll_nutrition = (LinearLayout) view.findViewById(R.id.nutrition_ll);
        farmName = (EditText) view.findViewById(R.id.farmName);
        farmerName = (EditText) view.findViewById(R.id.farmerName);
        farmerNumber = (EditText) view.findViewById(R.id.farmerNumber);
        editText_FatherName = (EditText) view.findViewById(R.id.editText_FatherName);
        farmArea = (EditText) view.findViewById(R.id.farm_area);
        sowPeriodFrom = (TextView) view.findViewById(R.id.editTextShowPeriodFrom);
        sowPeriodTo = (TextView) view.findViewById(R.id.editTextShowPeriodTo);
        baselDoseSpiner = (Spinner) view.findViewById(R.id.basalDose);
        valueN = (EditText) view.findViewById(R.id.baselDoseTableN);
        valueP = (EditText) view.findViewById(R.id.baselDoseTableP);
        valueK = (EditText) view.findViewById(R.id.baselDoseTableK);
        OtherVillageName_edt = (EditText) view.findViewById(R.id.OtherVillageName_edt);
        OthersubDistrict_edt = (EditText) view.findViewById(R.id.OthersubDistrict_edt);
        yourConcernSpiner = (Spinner) view.findViewById(R.id.yourConcern);
        otherNutrition = (EditText) view.findViewById(R.id.nutrition);
        submitForm = (Button) view.findViewById(R.id.submit);
        crop1 = (Button) view.findViewById(R.id.crop1);
        crop2 = (Button) view.findViewById(R.id.crop2);
        crop3 = (Button) view.findViewById(R.id.crop3);
        crop4 = (Button) view.findViewById(R.id.crop4);

        ll_OtherDistrict = (LinearLayout) view.findViewById(R.id.ll_OtherDistrict);
        ll_OtherVillageName = (LinearLayout) view.findViewById(R.id.ll_OtherVillageName);
        ll_noofbags = (LinearLayout) view.findViewById(R.id.ll_noofbags);
//        ll_noofbags.setVisibility(View.GONE);
        tblrow_other = (LinearLayout) view.findViewById(R.id.tblrow_other);

        edtother = (EditText) view.findViewById(R.id.edtother);
        edtaadhar_no = (EditText) view.findViewById(R.id.edtaadhar_no);
        edtnoofbags = (EditText) view.findViewById(R.id.edtnoofbags);
        edt_IBFarmerCode = (EditText) view.findViewById(R.id.edt_IBFarmerCode);
        spin_MobileType = (Spinner) view.findViewById(R.id.spin_MobileType);
        spin_getAadharlsit = (Spinner) view.findViewById(R.id.spin_getAadharlsit);
        tbl_getAadhar = (TableRow) view.findViewById(R.id.tbl_getAadhar);

        imge_state_refresh = (ImageView) view.findViewById(R.id.imge_state_refresh);
        project_refresh = (ImageView) view.findViewById(R.id.project_refresh);
        imge_crop_refresh = (ImageView) view.findViewById(R.id.imge_crop_refresh);

        farmInfo = (TextView) view.findViewById(R.id.farm_inf);
        please = (TextView) view.findViewById(R.id.please);
        farmN = (TextView) view.findViewById(R.id.full_n);
        farmerN = (TextView) view.findViewById(R.id.farmer_n);
        farmerNu = (TextView) view.findViewById(R.id.farmer_nu);
        stateN = (TextView) view.findViewById(R.id.state_n);
        taggedF_A = (TextView) view.findViewById(R.id.tagged_f_a);
        actual_F_A = (TextView) view.findViewById(R.id.actual_f);
        cropN = (TextView) view.findViewById(R.id.crop_t);
        varietyN = (TextView) view.findViewById(R.id.variety_t);
        sow_p_f = (TextView) view.findViewById(R.id.sow_p);
        sow_p_t = (TextView) view.findViewById(R.id.sow_p_t);
        have_you = (TextView) view.findViewById(R.id.have_y);
        whatis = (TextView) view.findViewById(R.id.what_is);

        cropSpiner1 = (CustomSearchableSpinner) view.findViewById(R.id.crop);

        varietySpiner = (CustomSearchableSpinner) view.findViewById(R.id.variety);
        spin_project = (CustomSearchableSpinner) view.findViewById(R.id.spin_project);
        stateList = (CustomSearchableSpinner) view.findViewById(R.id.stateList);
        DistrictName_spin = (CustomSearchableSpinner) view.findViewById(R.id.DistrictName_spin);
        SubDistrictName_spin = (CustomSearchableSpinner) view.findViewById(R.id.SubDistrictName_spin);
        VillageName_spin = (CustomSearchableSpinner) view.findViewById(R.id.VillageName_spin);
        emailBTN = (TextView) view.findViewById(R.id.emailJson);

        plotArea = (EditText) view.findViewById(R.id.plot_area);

        radiogroup_aadhar = (RadioGroup) view.findViewById(R.id.radiogroup_aadhar);

        rb_aadhar = (RadioButton) view.findViewById(R.id.rb_aadhar);
        rb_other = (RadioButton) view.findViewById(R.id.rb_other);

        radiogroup_aadhar.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                tbl_getAadhar.setVisibility(View.GONE);
                if (checkedId == R.id.rb_aadhar) {
                    radiogroup_aadhar.setVisibility(View.VISIBLE);
                    edtaadhar_no.setVisibility(View.VISIBLE);
                    tblrow_other.setVisibility(View.GONE);
                    edtother.setText("");
                } else if (checkedId == R.id.rb_other) {
                    radiogroup_aadhar.setVisibility(View.VISIBLE);
                    edtaadhar_no.setVisibility(View.GONE);
                    tblrow_other.setVisibility(View.VISIBLE);
                    edtaadhar_no.setText("");
                }

            }
        });
        if (area != null) {
            plotArea.setText(area);
        }
//        setSubtitleLanguage();
        ArrayList<String> stateList = new ArrayList<>();
        final Cursor stateCursor = db.getAllStates();
//        stateList.add("Select State");
        stateList.add(getDynamicLanguageValue(getActivity(), "Select", R.string.Select));
//        stateList.add(getResources().getString(R.string.SelectState));
        if (stateCursor.getCount() > 0) {
            stateCursor.moveToFirst();
            do {
                stateList.add(stateCursor.getString(stateCursor.getColumnIndex(DBAdapter.STATE_NAME)));
            } while (stateCursor.moveToNext());
        }


        emailBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String filename = "myFarmJson.txt";
                File filelocation = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), filename);
                Uri path = Uri.fromFile(filelocation);
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
// set the type to 'email'
                emailIntent.setType("vnd.android.cursor.dir/email");
                String to[] = {"vishal.tripathi@iembsys.com"};
                emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
// the attachment
                emailIntent.putExtra(Intent.EXTRA_STREAM, path);
// the mail subject
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Failed Json file");
                startActivity(Intent.createChooser(emailIntent, "Send email..."));

            }
        });

        // setSupportActionBar(toolbar);
        // getSupportActionBar().setDisplayShowTitleEnabled(false); //Make the default lable invisible
        farmInformationData = new FarmInformationData();
        allFarmDetail = new AllFarmDetail();
        if (selectedFarmName != null && selectedFarmName.length() > 0) {
            db.open();
            Cursor getFarmByFarmName = db.getFarmByFarmName(selectedFarmName);
            if (getFarmByFarmName.getCount() > 0) {
                getFarmByFarmName.moveToFirst();
                allFarmDetail.setFarmId(getFarmByFarmName.getString(getFarmByFarmName.getColumnIndex(DBAdapter.FARM_ID)));
                allFarmDetail.setFarmName(getFarmByFarmName.getString(getFarmByFarmName.getColumnIndex(DBAdapter.FARM_NAME)));
                farmName.setText(getFarmByFarmName.getString(getFarmByFarmName.getColumnIndex(DBAdapter.FARM_NAME)));
                farmName.setEnabled(false);
                allFarmDetail.setUserId(getFarmByFarmName.getString(getFarmByFarmName.getColumnIndex(DBAdapter.USER_ID)));
                allFarmDetail.setFarmerName(getFarmByFarmName.getString(getFarmByFarmName.getColumnIndex(DBAdapter.FARMER_NAME)));
                allFarmDetail.setFarmerPhone(getFarmByFarmName.getString(getFarmByFarmName.getColumnIndex(DBAdapter.FARMER_PHONE)));
                allFarmDetail.setActualFarmArea(getFarmByFarmName.getString(getFarmByFarmName.getColumnIndex(DBAdapter.FARM_AREA)));

                allFarmDetail.setArea(getFarmByFarmName.getString(getFarmByFarmName.getColumnIndex(DBAdapter.AREA)));

                farmerName.setText(allFarmDetail.getFarmerName());
                farmerNumber.setText(allFarmDetail.getFarmerPhone());
                farmArea.setText(allFarmDetail.getActualFarmArea());

                if (area != null) {
                    plotArea.setText(area);
                } else {
                    plotArea.setText(allFarmDetail.getArea());
                }


                allFarmDetail.setContour(getFarmByFarmName.getString(getFarmByFarmName.getColumnIndex(DBAdapter.CONTOUR)));
                allFarmDetail.setState(getFarmByFarmName.getString(getFarmByFarmName.getColumnIndex(DBAdapter.STATE_ID)));

            }
        } else {
            farmName.setEnabled(true);
        }

//        checkValue(); //loading crop list in spinner view


        System.out.println("State id for crop list : " + AppConstant.stateID);
        db.open();
        allCrop = db.getCropByState(AppConstant.stateID);
        final int allCropCount = allCrop.getCount();
        String[] cropStringArray = new String[allCropCount + 1];
//        cropStringArray[0] = "Select Crop";
        cropStringArray[0] = getDynamicLanguageValue(getActivity(), "Select", R.string.Select);
//        cropStringArray[0] = getResources().getString(R.string.SelectCrop);
        if (allCropCount > 0) {
            allCrop.moveToFirst();
            for (int i = 1; i <= allCropCount; i++) {
                cropStringArray[i] = allCrop.getString(allCrop.getColumnIndex(DBAdapter.CROP));
                allCrop.moveToNext();
            }
        }

        cropQueryData = new ArrayList<>();
        switch (callingActivity) {
            case AppConstant.HomeActivity:
                if (selectedFarmName != null) {
                    Cursor getSelectedFarmsValue = db.getSelectedFarmsValue(selectedFarmName);
                    if (getSelectedFarmsValue.getCount() > 0) {
                        getSelectedFarmsValue.moveToFirst();
                        String farmId = getSelectedFarmsValue.getString(getSelectedFarmsValue.getColumnIndex(DBAdapter.FARM_ID));
                        stateId = getSelectedFarmsValue.getString(getSelectedFarmsValue.getColumnIndex(DBAdapter.STATE_ID));
                        String contour = getSelectedFarmsValue.getString(getSelectedFarmsValue.getColumnIndex(DBAdapter.CONTOUR));
                        String[] latLng = contour.split("-")[0].split(",");
                        if (latLng != null && latLng.length > 2) {
                            if (latLng[0] != null && latLng[0].length() > 4) {
                                latitude = latLng[0];
                                longitude = latLng[1];
                            } else {
                                latitude = latLng[1];
                                longitude = latLng[2];
                            }
                        }

                        Cursor getCropQuery = db.getCropQueryByFarmId(farmId);
                        if (getCropQuery.getCount() > 0) {
                            getCropQuery.moveToFirst();
                            do {
                                CropQueryData cropQuery = new CropQueryData(getCropQuery);
                                cropQueryData.add(cropQuery);
                            } while (getCropQuery.moveToNext());
                        }

                    }
                }

                break;
            case AppConstant.AddFarmMap: // this will call whaen you choose farm from the list
                String[] latLng = allDrawLatLngPoint.split("-")[0].split(",");
                if (allDrawLatLngPoint != null && latLng.length > 1) {
                    latitude = latLng[0];
                    longitude = latLng[1];
                }
                stateId = AppConstant.stateID;
                break;
        }

        myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                disPlayDate();
            }

        };
        sowPeriodFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Utility.setDatePicker(getActivity(), sowPeriodFrom, "DDMMYYYY");
//                displayDateInEditText = AppConstant.SOW_PERIOD_FROM;
//                // TODO Auto-generated method stub
//                new DatePickerDialog(getActivity(), date, myCalendar
//                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
//                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        sowPeriodTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO Auto-generated method stub
                Utility.setDatePicker(getActivity(), sowPeriodTo, "DDMMYYYY");
//                displayDateInEditText = AppConstant.SOW_PERIOD_TO;
//                new DatePickerDialog(getActivity(), date, myCalendar
//                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
//                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        submitForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (prefs == null) {
                    prefs = getActivity().getSharedPreferences(AppConstant.SHARED_PREFRENCE_NAME, getActivity().MODE_PRIVATE);
                }
                String userId = prefs.getString(AppConstant.PREFRENCE_KEY_USER_ID, null);

                Log.v("userID_check", userId + "--");


                if (userId == null || userId.length() < 1) {
                    loginAlert();
                } else {
                    AppConstant.user_id = userId;
                    submitFarmDetail();
                }
            }

        });

        this.varietySpiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // ly.setVisibility(View.GONE); // disable three field N P K
            }
        });


        applyBasalDose = new ArrayList<String>();
//        applyBasalDose.add("Select");
        applyBasalDose.add(getDynamicLanguageValue(getActivity(), "Select", R.string.Select));
        applyBasalDose.add("Yes");
        applyBasalDose.add("No");

        yourConcern = new ArrayList<String>();
//        yourConcern.add("Select");
        yourConcern.add(getDynamicLanguageValue(getActivity(), "Select", R.string.Select));
        yourConcern.add("Increase my revenue");
        yourConcern.add("Increase my yield");
        yourConcern.add("Get me better price for my produce");


        ArrayAdapter<String> baselDoseSpiner = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, applyBasalDose);
        this.baselDoseSpiner.setAdapter(baselDoseSpiner);
        this.baselDoseSpiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {
                    ly.setVisibility(View.GONE);
                    ll_nutrition.setVisibility(View.GONE);
                    baselD = null;
                }
                if (position == 1) {  //yes condition
                    ll_nutrition.setVisibility(View.GONE);
                    ly.setVisibility(View.VISIBLE);

                    baselD = "yes";

                }
                if (position == 2) //no condition
                {
                    ly.setVisibility(View.GONE);
                    ll_nutrition.setVisibility(View.GONE);
                    baselD = "no";
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ArrayAdapter<String> yourConcernSpiner1 = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, yourConcern);
        yourConcernSpiner.setAdapter(yourConcernSpiner1);

        yourConcernSpiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position > 0) {
                    AppConstant.CONCERN_ID = yourConcernSpiner.getSelectedItemPosition();
                    concId = "yes";
                } else {
                    concId = null;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


// below method will display all farm detail on the farminformation class of every edittext or spinner

        crop1.setOnClickListener(crop1ClickListner);
        crop2.setOnClickListener(crop2ClickListner);
        crop3.setOnClickListener(crop3ClickListner);
        crop4.setOnClickListener(crop4ClickListner);

        crop1.performClick();

        if (AppConstant.APP_MODE == AppConstant.OFFLINE) {
            submitForm.setText(getDynamicLanguageValue(getActivity(), "Save", R.string.Save));

        } else {
            submitForm.setText(getDynamicLanguageValue(getActivity(), "Submit", R.string.Submit));
        }

//        displayDetailOfSelectedFarm(selectedFarmName, selectedCropQueryCount);

//SetFarmer Details
        if (FarmEditActive == true) {
            if (FarmEditDetails != null) {
                setEditFarmerDetails();
            }
        } else {
            //Herojit Add
            setProject();
            setPhoneType();
            setStateBind();
        }
        imge_state_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    new getStateProjectRefresh("StateDistrict", AppManager.getInstance().StateDistrictURL).execute();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
//                MasterStateDistrictDownload statecrop = new MasterStateDistrictDownload(getActivity());
//                statecrop.getStateDistrict();
//                setStateBind();
            }
        });
        project_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    new getStateProjectRefresh("Project", AppManager.getInstance().ProjectListURL(AppConstant.user_id)).execute();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
//                MasterProjectDownload statecrop = new MasterProjectDownload(getActivity());
//                statecrop.getAllProjectList();
//                setProject();
            }
        });
        imge_crop_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    new getStateProjectRefresh("Crops", AppManager.getInstance().CropsListURL).execute();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });


        //ScreenTracking
        UID = getUIDforScreenTracking();
        setScreenTracking(getActivity(), db, SN_LocateYoutFarmFragment, UID);

        return view;
    }

    public void submitFarmDetail() {
        try {
            if (!isValidate()) {
                return;
            } else {
                switch (callingActivity) {
                    case AppConstant.HomeActivity:
                        if (allDrawLatLngPoint != null && allDrawLatLngPoint.length() > 10) {
                            farmInformationData.setUserID(allFarmDetail.getUserId().toString());
                            farmInformationData.setFarmId(allFarmDetail.getFarmId().toString());
                            farmInformationData.setAllLatLngPoint(allDrawLatLngPoint);
                            farmInformationData.setState(allFarmDetail.getState().toString());
                        } else {
                            farmInformationData.setUserID(allFarmDetail.getUserId().toString());
                            farmInformationData.setFarmId(allFarmDetail.getFarmId().toString());
                            farmInformationData.setAllLatLngPoint(allFarmDetail.getContour().toString());
                            farmInformationData.setState(allFarmDetail.getState().toString());
                        }

                        break;
                    case AppConstant.AddFarmMap: // this will call whaen you choose farm from the list
                        //Add Herojit
//                    farmInformationData.setFarmId(AppConstant.farm_id);
                        farmInformationData.setUserID(AppConstant.user_id);
                        if (FarmEditActive == false) {
                            farmInformationData.setFarmId("0");//Herojit Comment
                        }
                        farmInformationData.setAllLatLngPoint(allDrawLatLngPoint); // this is conture
                        farmInformationData.setState(AppConstant.stateID);
                        break;
                }

           /* farmInformationData.setCrop(cropSpiner1.getSelectedItem().toString());
            farmInformationData.setCropID(pickCropIdOrValue.get(farmInformationData.getCrop().toString()));*/

                String uservalue = farmName.getText().toString();
                uservalue = uservalue.replace("'", "");

                farmInformationData.setFarmName(uservalue);
                farmInformationData.setFarmerName(farmerName.getText().toString());
                farmInformationData.setFarmerNumber(farmerNumber.getText().toString());
                farmInformationData.setActualFarmArea(farmArea.getText().toString());
                farmInformationData.setVariety(varietySpiner.getSelectedItem().toString());
                farmInformationData.setYourCencern(String.valueOf(yourConcernSpiner.getSelectedItemPosition()));
                AppConstant.CONCERN_ID = yourConcernSpiner.getSelectedItemPosition(); //this will decide which api will be called
                farmInformationData.setBasalDoseN(valueN.getText().toString());
                farmInformationData.setBasalDoseP(valueP.getText().toString());
                farmInformationData.setBasalDoseK(valueK.getText().toString());
                farmInformationData.setOtherNutrition(otherNutrition.getText().toString());
                if (baselDoseSpiner.getSelectedItem().toString().trim() == "Yes") {
                    farmInformationData.setOtherNutrition("0".toString());
                    farmInformationData.setBasalDoseN(valueN.getText().toString());
                    farmInformationData.setBasalDoseP(valueP.getText().toString());
                    farmInformationData.setBasalDoseK(valueK.getText().toString());
                    System.out.println();
                } else {
                    farmInformationData.setBasalDoseN("0");
                    farmInformationData.setBasalDoseP("0");
                    farmInformationData.setBasalDoseK("0");
                }
                farmInformationData.setBesalDoseApply(baselDoseSpiner.getSelectedItem().toString());   ///apply condition yes/no/nothing
                farmInformationData.setSowPeriodForm(sowPeriodFrom.getText().toString());
                farmInformationData.setSowPeriodTo(sowPeriodTo.getText().toString());
            }
            saveInitialFarm(DBAdapter.SAVE);
            new sentRequestForFarmSave().execute();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private boolean saveInitialFarm(String sendingStatus) {
        boolean isSave = false;
        try {
            String maxLat = "";
            String maxLon = "";
            String minLat = "";
            String minLon = "";
            String centerLat = "";
            String centerLon = "";
            String[] allPoints = allDrawLatLngPoint.split("-");
            if (allPoints.length > 0) {
                for (int i = 0; i < allPoints.length; i++) {
                    try {
                        String[] latlng = allPoints[i].split(",");
                        if (i == 1) {
                            maxLat = latlng[0];
                            maxLon = latlng[1];
                        }
                        int middle = allPoints.length / 2;
                        if (i == middle) {
                            centerLat = latlng[0];
                            centerLon = latlng[1];
                        }
                        int last = allPoints.length - 1;
                        if (i == last) {
                            minLat = latlng[0];
                            minLon = latlng[1];
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
            Cursor farmAllreadyExist = db.isFarmAlreadyExist(farmInformationData.getFarmName(), AppConstant.user_id, farmInformationData.getFarmerNumber());

            String farmId = "";
            if (farmInformationData.getFarmId() != null && farmInformationData.getFarmId().trim().length() > 0) {
//                farmAllreadyExist = db.isFarmAlreadyExist(farmInformationData.getFarmId());
                farmId = farmInformationData.getFarmId();
            } else {
                farmId = String.valueOf(System.currentTimeMillis() / 1000);
            }
            ContentValues initialValues = new ContentValues();
            initialValues.put(DBAdapter.FARM_ID, farmId);
            initialValues.put(DBAdapter.FARM_NAME, farmInformationData.getFarmName());
            initialValues.put(DBAdapter.FARMER_NAME, farmInformationData.getFarmerName());
            initialValues.put(DBAdapter.FARMER_PHONE, farmInformationData.getFarmerNumber());
            initialValues.put(DBAdapter.FARM_AREA, farmInformationData.getActualFarmArea());
            initialValues.put(DBAdapter.USER_ID, AppConstant.user_id);
            initialValues.put(DBAdapter.CONTOUR, farmInformationData.getAllLatLngPoint());
            initialValues.put(DBAdapter.STATE_ID, AppConstant.stateID);
            initialValues.put(DBAdapter.CONCERN, farmInformationData.getYourCencern());
            initialValues.put(DBAdapter.AREA, area);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            initialValues.put(DBAdapter.LOG_DATE, sdf.format(date));
            initialValues.put(DBAdapter.MAX_LAT, maxLat);
            initialValues.put(DBAdapter.MAX_LON, maxLon);
            initialValues.put(DBAdapter.MIN_LAT, minLat);
            initialValues.put(DBAdapter.MIN_LON, minLon);
            initialValues.put(DBAdapter.CENTRE_LAT, centerLat);
            initialValues.put(DBAdapter.CENTRE_LON, centerLon);
            initialValues.put(DBAdapter.SENDING_STATUS, sendingStatus);

            //Add new field
            //StateName,DistrictID,Block,VillageID,VillageStr,ProjectID,TaggingApp,FatherName,
            // AadharNo,Aadhar_Other,MobileType,NoOfBags,createaccflag
            initialValues.put(DBAdapter.StateName, AppConstant.state);
            initialValues.put(DBAdapter.DistrictID, DistrictID);
            if (OthersubDistrict_edt.getText().toString().trim().length() > 0) {
                initialValues.put(DBAdapter.Block, OthersubDistrict_edt.getText().toString());
            } else {
                initialValues.put(DBAdapter.Block, SubDistrictName);
            }
            if (OtherVillageName_edt.getText().toString().trim().length() > 0) {
                initialValues.put(DBAdapter.VillageID, "0");
                initialValues.put(DBAdapter.VillageStr, OtherVillageName_edt.getText().toString());
            } else {
                initialValues.put(DBAdapter.VillageID, VillageID);
                initialValues.put(DBAdapter.VillageStr, VillageName);
            }
            initialValues.put(DBAdapter.ProjectID, ProjectID);
            initialValues.put(DBAdapter.TaggingApp, "MFI");
            initialValues.put(DBAdapter.FatherName, editText_FatherName.getText().toString());

            if (AadharID_No != null && !AadharID_No.equalsIgnoreCase("") && AadharID_No.length() > 0) {

            } else {
                if (edtaadhar_no.getText().toString() != null && edtaadhar_no.getText().toString().length() > 0) {
                    AadharID_No = edtaadhar_no.getText().toString();
                } else if (edtother.getText().toString() != null && edtother.getText().toString().length() > 0) {
                    AadharID_No = edtother.getText().toString();
                }
            }
            initialValues.put(DBAdapter.AadharNo, AadharID_No);
            initialValues.put(DBAdapter.Aadhar_Other, edtother.getText().toString());
            initialValues.put(DBAdapter.MobileType, PhoneTypeValue);
            initialValues.put(DBAdapter.NoOfBags, edtnoofbags.getText().toString());
            initialValues.put(DBAdapter.IBCode, edt_IBFarmerCode.getText().toString());

            if (isCheckBox) {
                initialValues.put(DBAdapter.createaccflag, "true");
            } else {
                initialValues.put(DBAdapter.createaccflag, "false");
            }

            long k = -1;
//            if (farmAllreadyExist.getCount() > 0) {
//                // String ss = existAlert();
//                //  if (ss==null || !ss.equalsIgnoreCase("1") ) {
//                k = db.db.update(DBAdapter.DATABASE_TABLE_ALL_FARM_DETAIL, initialValues, DBAdapter.FARM_NAME + "='" + farmInformationData.getFarmName() + "' AND " + DBAdapter.USER_ID + " ='" + AppConstant.user_id + "'", null);
//                System.out.println("Farm updated + " + k);
//                //  }
//            } else {
//                k = db.db.insert(DBAdapter.DATABASE_TABLE_ALL_FARM_DETAIL, null, initialValues);
//                System.out.println("Farm inserted + " + k);
//            }

            if (farmAllreadyExist.getCount() > 0) {
                k = db.SaveUpdateFarmdetails(initialValues, "U");
                //  }
            } else {
                k = db.SaveUpdateFarmdetails(initialValues, "I");
            }


            System.out.println("farm detail saved : " + k);
            Cursor getCropQueryByFarmId = db.getCropQueryByFarmId(farmId);
            if (getCropQueryByFarmId.getCount() > 0) {
                getCropQueryByFarmId.moveToFirst();
                int i = 0;
                do {
                    String id = getCropQueryByFarmId.getString(getCropQueryByFarmId.getColumnIndex(DBAdapter.ID));
                    if (cropQueryData.size() > i) {
                        cropQueryData.get(i).setDbId(id);
                    }
                    i++;
                } while (getCropQueryByFarmId.moveToNext());
            }
            for (CropQueryData data : cropQueryData) {
                data.setFarmId(farmId);


                long j = data.insert(db, sendingStatus);

                System.out.println("cropData inserted : " + j);
            }


            if (k == 1) {
                getDynamicLanguageToast(getActivity(), "SubmittedSuccessfully", R.string.SubmittedSuccessfully);
            } else {
                getDynamicLanguageToast(getActivity(), "Farmnotsaved", R.string.Farmnotsaved);
            }
//            return;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return isSave;
    }

    private String createJsonParameterForSaveForm(FarmInformationData farmData, ArrayList<CropQueryData> cropQueryData) {
        String parameterString = "";
        if (isLogin) {
            JSONArray cropInfo = new JSONArray();
            for (CropQueryData data : cropQueryData) {
                try {
                    JSONObject cropInfoJsonObject = new JSONObject();
                    cropInfoJsonObject.put("CropID", data.getCropID());
                    cropInfoJsonObject.put("CropName", data.getCrop());
                    cropInfoJsonObject.put("Variety", data.getVariety());
                    cropInfoJsonObject.put("BasalDoseApply", data.getBesalDoseApply());
                    cropInfoJsonObject.put("N", data.getBasalDoseN());
                    cropInfoJsonObject.put("P", data.getBasalDoseP());
                    cropInfoJsonObject.put("K", data.getBasalDoseK());
                    cropInfoJsonObject.put("SowDate", data.getSowPeriodForm());
                    cropInfoJsonObject.put("CropFrom", data.getSowPeriodForm());
                    cropInfoJsonObject.put("CropTo", data.getSowPeriodTo());
                    cropInfoJsonObject.put("OtherNutrient", data.getOtherNutrition());
                    cropInfo.put(cropInfoJsonObject);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            JSONObject finalJson = new JSONObject();
            JSONObject jsonObject = new JSONObject();
            try {
//                jsonObject.put("UserID", farmData.getUserID());
                jsonObject.put("FarmID", farmData.getFarmId());
                jsonObject.put("FarmName", farmData.getFarmName());
                jsonObject.put("FarmerName", farmData.getFarmerName());
                jsonObject.put("PhoneNo", farmData.getFarmerNumber());
                jsonObject.put("FarmArea", farmData.getActualFarmArea());
                jsonObject.put("Contour", farmData.getAllLatLngPoint());
                jsonObject.put("CropID", farmData.getCropID());
                jsonObject.put("State", AppConstant.stateID);
                jsonObject.put("StateName", AppConstant.state);
                //Herojit Add
                jsonObject.put("District", DistrictID);
//                jsonObject.put("Sub_district", SubDistrictName);
                jsonObject.put("CropInfo", cropInfo);
                jsonObject.put("Concerns", farmData.getYourCencern());
                jsonObject.put("Area", area);
                //Add Herojit

                if (OthersubDistrict_edt.getText().toString().trim().length() > 0) {
                    jsonObject.put("Block", OthersubDistrict_edt.getText().toString());
                } else {
                    jsonObject.put("Block", SubDistrictName);
                }
                if (OtherVillageName_edt.getText().toString().trim().length() > 0) {
                    jsonObject.put("VillageStr", OtherVillageName_edt.getText().toString());
                    jsonObject.put("VillageID", "0");
                } else {
                    jsonObject.put("VillageStr", VillageName);
                    jsonObject.put("VillageID", VillageID);
                }

                jsonObject.put("ProjectID", ProjectID);
                jsonObject.put("TaggingApp", "MFI");
                //Herojit Comment
//                if (isCheckBox) {
//                    jsonObject.put("createaccflag", "true");
//                }

                //Add New

                jsonObject.put("FatherName", editText_FatherName.getText().toString());

                if (AadharID_No != null && !AadharID_No.equalsIgnoreCase("") && AadharID_No.length() > 0) {

                } else {
                    if (edtaadhar_no.getText().toString() != null && edtaadhar_no.getText().toString().length() > 0) {
                        AadharID_No = edtaadhar_no.getText().toString();
                    } else if (edtother.getText().toString() != null && edtother.getText().toString().length() > 0) {
                        AadharID_No = edtother.getText().toString();
//                        jsonObject.put("AadharNo", edtother.getText().toString());
                    }
                }
//                if (edtaadhar_no.getText().toString() != null && edtaadhar_no.getText().toString().length() > 0) {
//                    jsonObject.put("AadharNo", edtaadhar_no.getText().toString());
//                } else if (edtother.getText().toString() != null && edtother.getText().toString().length() > 0) {
//                    jsonObject.put("AadharNo", edtother.getText().toString());
//                } else {
//                    jsonObject.put("AadharNo", AadharID_No);
//                }
                jsonObject.put("AadharNo", AadharID_No);
//                jsonObject.put("AadharNo", edtaadhar_no.getText().toString());
                jsonObject.put("Other", edtother.getText().toString());
                jsonObject.put("MobileType", PhoneTypeValue);
                jsonObject.put("NoOfBags", edtnoofbags.getText().toString());
                jsonObject.put("IBCode", edt_IBFarmerCode.getText().toString());

                System.out.println("Sended Area : " + area);
                finalJson.put("UserID", farmData.getUserID());
                if (isCheckBox) {
                    finalJson.put("createaccflag", "true");
                }
                finalJson.put("FarmInfo", jsonObject.toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }
            //      jsonObject.put("guarderiasIdGuarderias",jsonObject2);
            parameterString = finalJson.toString();
        }
        return parameterString;
    }


    public boolean isValidate() {
        try {

            //manish
//            if (!(ProjectName != null && !ProjectName.equalsIgnoreCase("Select Project"))) {
            if (!(ProjectName != null && !ProjectName.equalsIgnoreCase(getDynamicLanguageValue(getActivity(), "EnterProjectName", R.string.EnterProjectName)))) {
                getDynamicLanguageToast(getActivity(), "EnterProjectName", R.string.EnterProjectName);
                return false;
            }

            if (spin_getAadharlsit.getSelectedItemPosition() == 0 && !Utility.setStringCheck(edtaadhar_no.getText().toString()) && !Utility.setStringCheck(edtother.getText().toString())) {
                getDynamicLanguageToast(getActivity(), "EnterAadharother", R.string.EnterAadharother);
                return false;
            }

            if (!(farmName.getText().toString().length() > 0)) {
                getDynamicLanguageToast(getActivity(), "EnterFarmName", R.string.EnterFarmName);
                return false;
            }

            if (AppManager.getInstance().checkNullorNot(farmerNumber.getText().toString()) == false || !AppManager.getInstance().isMobileNoValid(farmerNumber.getText().toString())) {
                getDynamicLanguageToast(getActivity(), "validmobileno", R.string.validmobileno);
                return false;
            }
            if (AppManager.getInstance().checkNullorNot(AppConstant.state) == false) {
                getDynamicLanguageToast(getActivity(), "PleaseSelectState", R.string.PleaseSelectState);
                return false;
            }
            if (AppManager.getInstance().checkNullorNot(DistrictName) == false) {
                getDynamicLanguageToast(getActivity(), "PleaseselectDistrict", R.string.PleaseselectDistrict);
                return false;
            }
//        if (AppManager.getInstance().checkNullorNot(SubDistrictName) == false) {
//            Toast.makeText(getActivity(), getResources().getString(R.string.PleaseselectSubDistrict), Toast.LENGTH_LONG).show();
//            return false;
//        }
            if ((AppManager.getInstance().checkNullorNot(OthersubDistrict_edt.getText().toString()) == false) &&
//                    (SubDistrictName.equalsIgnoreCase("Select Sub District"))) {
                    (SubDistrictName.equalsIgnoreCase(getDynamicLanguageValue(getActivity(), "Select", R.string.Select)))) {
                getDynamicLanguageToast(getActivity(), "Pleaseselectsubdistrict", R.string.Pleaseselectsubdistrict);
                return false;
            }
            if ((AppManager.getInstance().checkNullorNot(OtherVillageName_edt.getText().toString()) == false) &&
                    (VillageName.equalsIgnoreCase(getDynamicLanguageValue(getActivity(), "Select", R.string.Select)))) {
                getDynamicLanguageToast(getActivity(), "Pleaseselectviilage", R.string.Pleaseselectviilage);
                return false;
            }

            if (!(cropSpiner1.getSelectedItem().toString().trim() != getDynamicLanguageValue(getActivity(), "Select", R.string.Select))) {
                getDynamicLanguageToast(getActivity(), "PleaseselectCrop", R.string.PleaseselectCrop);
                return false;
            } else {
                String cropName = cropSpiner1.getSelectedItem().toString().trim();
                selectedCropQueryCount.setCrop(cropName);
                selectedCropQueryCount.setCropID(db.getCropIdByName(cropName));
            }
            if (!(varietySpiner.getSelectedItem().toString().trim() != getDynamicLanguageValue(getActivity(), "Select", R.string.Select))) {
                getDynamicLanguageToast(getActivity(), "Pleaseselectvariety", R.string.Pleaseselectvariety);
                return false;
            } else {
                String variety = varietySpiner.getSelectedItem().toString().trim();
                selectedCropQueryCount.setVariety(variety);
            }


//            if (!(sowPeriodFrom.getText().toString().length() > 0 && sowPeriodTo.getText().toString().length() > 0)) {
//                getDynamicLanguageToast(getActivity(), "Pleaseselectshowingduration", R.string.Pleaseselectshowingduration);
//                return false;
//            } else {
//                selectedCropQueryCount.setSowPeriodForm(sowPeriodFrom.getText().toString());
//                selectedCropQueryCount.setSowPeriodTo(sowPeriodTo.getText().toString());
//            }
            if (sowPeriodFrom.getText().toString().length() < 1) {
                getDynamicLanguageToast(getActivity(), "Pleaseselectsowingdatefrom", R.string.Pleaseselectsowingdatefrom);
                return false;
            } else if (sowPeriodTo.getText().toString().length() < 1) {
                getDynamicLanguageToast(getActivity(), "Pleaseselectsowingdateto", R.string.Pleaseselectsowingdateto);
                return false;
            } else if (!dateCheckgreater(sowPeriodFrom.getText().toString(), sowPeriodTo.getText().toString())) {
                getDynamicLanguageToast(getActivity(), "Pleaseselectsowingdateduration", R.string.Pleaseselectsowingdateduration);
                return false;
            } else {
                selectedCropQueryCount.setSowPeriodForm(sowPeriodFrom.getText().toString());
                selectedCropQueryCount.setSowPeriodTo(sowPeriodTo.getText().toString());
            }


            if (baselD != null && baselD.equalsIgnoreCase("yes")) {
                if (!(valueN.getText().toString().length() > 0 && valueP.getText().toString().length() > 0 && valueK.getText().toString().length() > 0)) {
                    getDynamicLanguageToast(getActivity(), "FillbeasalDose", R.string.FillbeasalDose);
                    return false;
                } else {
                    selectedCropQueryCount.setBasalDoseN(valueN.getText().toString());
                    selectedCropQueryCount.setBasalDoseP(valueP.getText().toString());
                    selectedCropQueryCount.setBasalDoseK(valueK.getText().toString());
                }
                selectedCropQueryCount.setBesalDoseApply("1");
            } else if (baselD != null && baselD.equalsIgnoreCase("no")) {

                selectedCropQueryCount.setNutrient(otherNutrition.getText().toString());

                selectedCropQueryCount.setBesalDoseApply("2");
            } else {
                getDynamicLanguageToast(getActivity(), "PleasebeasalDose", R.string.PleasebeasalDose);
                return false;
            }
            if (concId != null) {
                selectedCropQueryCount.setYourCencern(String.valueOf(yourConcernSpiner.getSelectedItemPosition()));
            } else {
                getDynamicLanguageToast(getActivity(), "Selectyourconcern", R.string.Selectyourconcern);
                return false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return true;
    }

    public void displayDetailOfSelectedFarm(String farmName, CropQueryData data) {
        System.out.println("Inside display form : " + (data != null) + " and " + (allDrawLatLngPoint == null));
        String name = farmName;
        if (allDrawLatLngPoint == null) {

            Cursor c = db.getSelectedFarmsValue(name);
            if (c.moveToFirst()) {
                if (c.getCount() > 0) {
                    boolean isValueExist = this.farmName.getText().toString() != null && this.farmName.getText().toString().length() > 0;
                    if (!isValueExist) {
                        do {

                            allFarmDetail.setFarmId(c.getString(c.getColumnIndex(DBAdapter.FARM_ID)));
                            String str = c.getString(c.getColumnIndex(DBAdapter.FARM_ID));
                            allFarmDetail.setFarmName(c.getString(c.getColumnIndex(DBAdapter.FARM_NAME)));
                            this.farmName.setText(allFarmDetail.getFarmName().toString());

                            allFarmDetail.setFarmerName(c.getString(c.getColumnIndex(DBAdapter.FARMER_NAME)));
                            allFarmDetail.setFarmerPhone(c.getString(c.getColumnIndex(DBAdapter.FARMER_PHONE)));
                            allFarmDetail.setActualFarmArea(c.getString(c.getColumnIndex(DBAdapter.FARM_AREA)));
                            this.farmerName.setText(allFarmDetail.getFarmerName().toString());
                            this.farmerNumber.setText(allFarmDetail.getFarmerPhone().toString());
                            this.farmArea.setText(allFarmDetail.getActualFarmArea().toString());

                            if (area != null) {
                                plotArea.setText(area);
                            } else {

                                this.plotArea.setText(allFarmDetail.getArea().toString());
                            }

                            allFarmDetail.setUserId(AppConstant.user_id);
                            allFarmDetail.setContour(c.getString(c.getColumnIndex(DBAdapter.CONTOUR)));
                            allFarmDetail.setState(c.getString(c.getColumnIndex(DBAdapter.STATE_ID)));
                            allFarmDetail.setConcern(c.getString(c.getColumnIndex(DBAdapter.CONCERN)));
                            int concernLocation = 0;
                            try {
                                concernLocation = Integer.parseInt(data.getYourCencern());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            this.yourConcernSpiner.setSelection(concernLocation);


                        }
                        while (c.moveToNext());
                    }

                }
            }
        }
        if (data != null) {
            System.out.println("data.getSowPeriodForm() : " + data.getSowPeriodForm());
            sowPeriodFrom.setText(data.getSowPeriodForm());
            System.out.println("data.getSowPeriodTo() : " + data.getSowPeriodTo());
            sowPeriodTo.setText(data.getSowPeriodTo());
            System.out.println("data.getCropID() : " + data.getCropID() + (data.getCropID() != null && (!data.getCropID().equals("-1"))));

            /*String variety = data.getVariety();
            if(variety.trim().length()>0){
                isVarietySelection = true;
            }else{
                isVarietySelection = false;
            }*/

            if (data.getCropID() != null && (!data.getCropID().equals("-1"))) {
                int cropCount = allCrop.getCount();
                allCrop.moveToFirst();
                for (int d = 1; d <= cropCount; d++) {
                    if (data.getCropID().trim().equals(allCrop.getString(allCrop.getColumnIndex(DBAdapter.CROP_ID)))) {
                        cropSpiner1.setSelection(d, true);
                        System.out.println("data.getVariety() : " + data.getVariety());
                        if (data.getVariety() != null && data.getVariety().trim().length() > 0) {
                            isVarietySelection = true;
                        }
                        break;
                    }
                    allCrop.moveToNext();
                }
            } else {
                cropSpiner1.setSelection(0);
            }

            System.out.println("data.getBesalDoseApply() : " + data.getBesalDoseApply());
            this.baselDoseSpiner.setSelection(applyBasalDose.indexOf(data.getBesalDoseApply()));
            if (data.getBasalDoseN() != null && (!data.getBasalDoseN().equals("0"))) {
                this.baselDoseSpiner.setSelection(applyBasalDose.indexOf("Yes"));
            }
            if (data.getBesalDoseApply().trim().equalsIgnoreCase("2")) {
                valueN.setText("0");
                valueP.setText("0");
                valueK.setText("0");
            } else {
                System.out.println("data.getBasalDoseN() : " + data.getBasalDoseN());
                valueN.setText(data.getBasalDoseN());
                System.out.println("data.getBasalDoseP() : " + data.getBasalDoseP());
                valueP.setText(data.getBasalDoseP());
                System.out.println("data.getBasalDoseK() : " + data.getBasalDoseK());
                valueK.setText(data.getBasalDoseK().toString());


            }
            System.out.println("data.getOtherNutrition() : " + data.getOtherNutrition());
            otherNutrition.setText(data.getOtherNutrition());

//                    this.yourConcernSpiner.setSelection(yourConcern.indexOf(data.getYourCencern()));
        }


    }

    private void setVariety(String stateId, String cropId) {
        try {
            db.open();
            varietyCursor = db.getVarietyByStateAndCrop(stateId, cropId);
            System.out.println("cropName inside set Variety  : " + cropId);

            final int varietyCount = varietyCursor.getCount();
            System.out.println("varietyCount  : " + varietyCount);

            String[] varietyStringArray = new String[varietyCount + 1];
//            varietyStringArray[0] = "Select Variety";
            varietyStringArray[0] = getDynamicLanguageValue(getActivity(), "Select", R.string.Select);
            if (varietyCount > 0) {
                varietyCursor.moveToFirst();
                for (int i = 1; i <= varietyCount; i++) {
                    varietyStringArray[i] = varietyCursor.getString(varietyCursor.getColumnIndex(DBAdapter.VARIETY));
                    varietyCursor.moveToNext();
                }
            }

            ArrayAdapter<String> varietyArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, varietyStringArray); //selected item will look like a spinner set from XML
            varietySpiner.setAdapter(varietyArrayAdapter);
            setCustomSearchableSpinner(getActivity(), varietySpiner, "SelectVariety", R.string.SelectVariety);
//            varietySpiner.setTitle(getDynamicLanguageValue(getActivity(), "SelectVariety", R.string.SelectVariety));
            varietySpiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String varietyName = "-1";
                    if (position > 0) {
                        varietyCursor.moveToPosition(position - 1);
                        varietyName = varietyCursor.getString(varietyCursor.getColumnIndex(DBAdapter.VARIETY));
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            if (isVarietySelection) {
                isVarietySelection = false;
                int terCount = varietyCursor.getCount();
                System.out.println("variety : " + selectedCropQueryCount.getVariety() + " varietyCount : " + terCount);
                if (terCount > 0 && (selectedCropQueryCount.getVariety().trim().length() > 0)) {
                    varietyCursor.moveToFirst();
                    for (int d = 1; d <= terCount; d++) {
                        System.out.println(d + ":" + varietyCursor.getString(varietyCursor.getColumnIndex(DBAdapter.VARIETY)));
                        if (selectedCropQueryCount.getVariety().trim().equals(varietyCursor.getString(varietyCursor.getColumnIndex(DBAdapter.VARIETY)))) {
                            varietySpiner.setSelection(d, true);
                            break;
                        }
                        varietyCursor.moveToNext();
                    }
                }
            }
            int VarietyNameposselectedatFarmRegistrtion = 0;
            if (FarmRegistration_Variety != null) {
                for (int i = 1; i <= varietyStringArray.length; i++) {
                    String cropName = varietyStringArray[i];
                    if (FarmRegistration_Variety.equalsIgnoreCase(cropName)) {
                        VarietyNameposselectedatFarmRegistrtion = i;
                        break;
                    }
                }
                varietySpiner.setSelection(VarietyNameposselectedatFarmRegistrtion);
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    private void disPlayDate() {

        String myFormat = "dd-MM-yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        switch (displayDateInEditText) {
            case AppConstant.SOW_PERIOD_FROM:
                sowPeriodFrom.setText(sdf.format(myCalendar.getTime()));
                break;
            case AppConstant.SOW_PERIOD_TO:
                sowPeriodTo.setText(sdf.format(myCalendar.getTime()));
                break;
        }

    }

    private void savingAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getDynamicLanguageValue(getActivity(), "FarmSaved", R.string.FarmSaved));
        builder.setMessage(getDynamicLanguageValue(getActivity(), "Farmcannotbe", R.string.Farmcannotbe));
        builder.setPositiveButton(getDynamicLanguageValue(getActivity(), "Ok", R.string.Ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                getActivity().finish();
                Intent intent = new Intent(getActivity(), NewHomeScreen.class);
                startActivity(intent);
            }
        });
        builder.show();
    }

    private void loginAlert() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getDynamicLanguageValue(getActivity(), "NotLoggedIn", R.string.NotLoggedIn));
        builder.setMessage(getDynamicLanguageValue(getActivity(), "Pleaseregisterlogin", R.string.Pleaseregisterlogin));
        builder.setPositiveButton(getDynamicLanguageValue(getActivity(), "login", R.string.login), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                goFarSignIn();
            }
        });
        builder.setNeutralButton(getDynamicLanguageValue(getActivity(), "Register", R.string.Register), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                Intent intent = new Intent(getActivity(), SignUpActivity.class);
                intent.putExtra("add_farm", "farm");
                startActivity(intent);
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.Cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();


            }
        });
        builder.show();
    }

    private String saveUser(Cursor cursor) {

        cursor.moveToFirst();
        String message = "Error";
        String id = cursor.getString(cursor.getColumnIndex(DBAdapter.ID));
        String mailId = cursor.getString(cursor.getColumnIndex(DBAdapter.EMAIL_ADDRESS));
        String userName = cursor.getString(cursor.getColumnIndex(DBAdapter.USER_NAME));
        String password = cursor.getString(cursor.getColumnIndex(DBAdapter.PASSWORD));
        String mobileNo = cursor.getString(cursor.getColumnIndex(DBAdapter.MOBILE_NO));
        String visibleName = cursor.getString(cursor.getColumnIndex(DBAdapter.VISIBLE_NAME));
        String createdDateTime = cursor.getString(cursor.getColumnIndex(DBAdapter.CREATED_DATE_TIME));
        String oldUserId = cursor.getString(cursor.getColumnIndex(DBAdapter.USER_ID));
        System.out.println("gotUser id : " + oldUserId);
        String completeStringForRegister = mailId + "/" + userName + "/" + password + "/" + visibleName + "/" + mobileNo;
        String response;

        try {
            ExternalStorageGPS.write_file("MFI_RESPONSE_LOG", true, AuthenticateService.format.format(AuthenticateService.cal().getTime()) + "---USER REGISTER STRING : " + "https://myfarminfo.com/yfirest.svc/Register/" + completeStringForRegister + " \n\r");
            response = CustomHttpClient.executeHttpPut("https://myfarminfo.com/yfirest.svc/Register/" + completeStringForRegister);
            ExternalStorageGPS.write_file("MFI_RESPONSE_LOG", true, AuthenticateService.format.format(AuthenticateService.cal().getTime()) + "---USER REGISTER RESPONSE : " + response + " \n\r");
            System.out.println("response " + response);
            Log.d("RegistrationData", response);
            if (response.contains("Error")) {
                String s = "Error";
                ContentValues values = new ContentValues();
                values.put(DBAdapter.ID, id);
                values.put(DBAdapter.USER_NAME, userName);
                values.put(DBAdapter.VISIBLE_NAME, visibleName);
                values.put(DBAdapter.PASSWORD, password);
                values.put(DBAdapter.EMAIL_ADDRESS, mailId);
                values.put(DBAdapter.MOBILE_NO, mobileNo);
                values.put(DBAdapter.USER_ID, oldUserId);
                values.put(DBAdapter.CREATED_DATE_TIME, createdDateTime);
                values.put(DBAdapter.SENDING_STATUS, DBAdapter.SENT);

                db.db.update(DBAdapter.TABLE_CREDENTIAL, values, DBAdapter.ID + " = '" + id + "'", null);
                message = "Error";
            }
            response = response.trim();
            response = response.substring(1, response.length() - 1);
            response = response.replace("\\", "");
            JSONArray jArray = new JSONArray(response);
            Log.d("afterfilterResponse", response);

            if (jArray.length() == 0) {
                message = "No Registered";
            }

            for (int i = 0; i < jArray.length(); i++) {

                JSONObject jObject = jArray.getJSONObject(i);
                String userId = jObject.getString("UserID");
                String visibleName1 = jObject.getString("VisibleName");

                ContentValues values = new ContentValues();
                values.put(DBAdapter.ID, id);
                values.put(DBAdapter.USER_NAME, userName);
                values.put(DBAdapter.VISIBLE_NAME, visibleName1);
                values.put(DBAdapter.PASSWORD, password);
                values.put(DBAdapter.EMAIL_ADDRESS, mailId);
                values.put(DBAdapter.MOBILE_NO, mobileNo);
                values.put(DBAdapter.USER_ID, userId);
                values.put(DBAdapter.CREATED_DATE_TIME, createdDateTime);
                values.put(DBAdapter.NEED_TO_EDIT, DBAdapter.FALSE);
                values.put(DBAdapter.SENDING_STATUS, DBAdapter.SENT);

                db.db.update(DBAdapter.TABLE_CREDENTIAL, values, DBAdapter.ID + " = '" + id + "'", null);
                boolean isUpdated = db.updateFormByUserId(oldUserId, userId);
                System.out.println("Corresponding farm is updated : " + isUpdated);

                if (AppConstant.user_id != null) {
                    if (oldUserId.equals(AppConstant.user_id)) {
                        AppConstant.user_id = userId;
                    }
                }

                message = "OK";
            }
        } catch (Exception e) {
//
            e.printStackTrace();
            Log.d("Status", "" + e);
            message = "Could not connect to server";
        }
        return message;
    }

    public void goFarSignIn() {

        LayoutInflater li = LayoutInflater.from(getActivity());
        View promptsView = li.inflate(R.layout.login_popup_new, null);
        prefs = getActivity().getSharedPreferences(AppConstant.SHARED_PREFRENCE_NAME, getActivity().MODE_PRIVATE);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText inputUid = (EditText) promptsView.findViewById(R.id.UserID);
        final EditText inputPWD = (EditText) promptsView.findViewById(R.id.UserPassword);
        final CheckBox checkBox = (CheckBox) promptsView.findViewById(R.id.checkBox);

        try {

            String user_name = prefs.getString(AppConstant.PREFRENCE_KEY_EMAIL, "");
            System.out.println("user name" + user_name);
            String password = prefs.getString(AppConstant.PREFRENCE_KEY_PASS, "");
            System.out.println("password" + password);
            Boolean bool = prefs.getBoolean(AppConstant.PREFRENCE_KEY_ISSAVED, false);
            System.out.println("BoolValue" + bool);
            checkBox.setChecked(bool);
            inputUid.setText(user_name.toString());
            inputPWD.setText(password.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        alertDialogBuilder.setPositiveButton(getDynamicLanguageValue(getActivity(), "Ok", R.string.Ok), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {


                SharedPreferences.Editor editor = prefs.edit();
                if (checkBox.isChecked()) {
                    System.out.println("isCHecked");
                    editor.putString(AppConstant.PREFRENCE_KEY_EMAIL, inputUid.getText().toString());
                    editor.putString(AppConstant.PREFRENCE_KEY_PASS, inputPWD.getText().toString());
                    editor.putBoolean(AppConstant.PREFRENCE_KEY_ISSAVED, true);
                    editor.putBoolean(AppConstant.PREFRENCE_KEY_ISLOGIN, true);
                    editor.apply();

                } else {
                    editor.putString(AppConstant.PREFRENCE_KEY_EMAIL, "");
                    editor.putString(AppConstant.PREFRENCE_KEY_PASS, "");
                    editor.putBoolean(AppConstant.PREFRENCE_KEY_ISSAVED, false);
                    editor.putBoolean(AppConstant.PREFRENCE_KEY_ISLOGIN, true);
                    editor.apply();

                }
                String id = inputUid.getText().toString().trim();
                String pas = inputPWD.getText().toString().trim();
                submit(id, pas);
            }
        });
        alertDialogBuilder.setNegativeButton(getDynamicLanguageValue(getActivity(), "Cancel", R.string.Cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }

        });
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.show();

    }

    public void submit(String id, String pas) {

        if (id == null || id.length() < 1) {
            getDynamicLanguageToast(getActivity(), "EnterEmailmobile", R.string.EnterEmailmobile);
        } else if (pas == null || pas.length() < 1) {
            getDynamicLanguageToast(getActivity(), "Enteryourpassword", R.string.Enteryourpassword);
        } else {
            new LoginAsyncTask(id, pas).execute();
        }

    }


    public void submitPopup_english() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getDynamicLanguageValue(getActivity(), "Success", R.string.Success)).
                setMessage(getDynamicLanguageValue(getActivity(), "Dearyouhavebeen", R.string.Dearyouhavebeen)).
                setPositiveButton(getDynamicLanguageValue(getActivity(), "Ok", R.string.Ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

//                        Intent in = new Intent(getActivity(), AdminDashboard_New.class);
//                        getActivity().finish();
//                        startActivity(in);
                        //Herojit Comment
//                        final FragmentTransaction ft = getFragmentManager().beginTransaction();
//                        String latLngStateId = latitude + "," + longitude + "," + stateId;
//                        Fragment fragment = FarmAdvisoryFragment.newInstance(cropQueryData, latLngStateId, farmInformationData.getFarmId());
//                        ft.replace(R.id.fragmentContainer, fragment, "NewFragmentTag");
//                        ft.addToBackStack("advr");
//                        ft.commit();


                    }
                });
        builder.show();
    }



    public ArrayList<HashMap<String, String>> getStateDistrictValues(int flag, String JsonValues) {
        ArrayList<HashMap<String, String>> values = new ArrayList<>();

        JSONArray jsonArray = null;

        try {
            jsonArray = new JSONArray(JsonValues);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        switch (flag) {
            case 1://State
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        String StateID = "", StateName = "";
                        StateID = obj.getString("StateID");
                        StateName = obj.getString("StateName");
                        HashMap<String, String> setval = new HashMap<>();
                        setval.put("StateID", StateID);
                        setval.put("StateName", StateName);
                        values.add(setval);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                break;
            case 2://District
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        String DistrictID = "", District = "";
                        DistrictID = obj.getString("DistrictID");
                        District = obj.getString("District");
                        HashMap<String, String> setval = new HashMap<>();
                        setval.put("DistrictID", DistrictID);
                        setval.put("District", District);
                        values.add(setval);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                break;
            case 3://Sub District
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        String Sub_district = "", Sub_district_Pos = "";
                        Sub_district = obj.getString("Sub_district");
                        Sub_district_Pos = String.valueOf(i);
                        HashMap<String, String> setval = new HashMap<>();
                        setval.put("Sub_district", Sub_district);
                        setval.put("Sub_district_Pos", Sub_district_Pos);
                        values.add(setval);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                break;
            case 4://Village
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        String Village_Final = "", Village_ID = "";
                        Village_Final = obj.getString("Village_Final");
                        Village_ID = obj.getString("Village_ID");
                        HashMap<String, String> setval = new HashMap<>();
                        setval.put("Village_Final", Village_Final);
                        setval.put("Village_ID", Village_ID);
                        values.add(setval);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                break;
        }

        return values;
    }

    public void getAPIURL(int flag, String ID, String Name) {
        String URL = "";
        switch (flag) {
//            case 1://State
//                URL = "https://myfarminfo.com/yfirest.svc/Clients/GetAllStates/2";
//                break;
//            case 2://District
//                URL = "https://myfarminfo.com/yfirest.svc/Clients/GetStateDistrict/" + ID;
//                break;
            case 3://Sub District
                URL = "https://myfarminfo.com/yfirest.svc/Clients/GetDistrictsubdistrict/" + ID;
                break;
            case 4://Village
                URL = "https://myfarminfo.com/yfirest.svc/Clients/GetsubdistrictVillage/" + ID + "/" + Name;
                break;
        }
        new getStateDistVillage(flag, URL).execute();
    }

    public void SetSpinner(final int flag, CustomSearchableSpinner spinner, ArrayList<HashMap<String, String>> value) {
        final ArrayList<String> getValue = new ArrayList();
        final ArrayList<String> getIDs = new ArrayList();
        int SubDistrictposselectedatFarmRegistrtion = 0, VillageposselectedatFarmRegistrtion = 0;
        if (value != null) {
            switch (flag) {
                case 1://State
                    for (int i = 0; i < value.size(); i++) {
                        getValue.add(value.get(i).get("StateName"));
                        getIDs.add(value.get(i).get("StateID"));
                    }
                    break;
                case 2://District
                    for (int i = 0; i < value.size(); i++) {
                        getValue.add(value.get(i).get("District"));
                        getIDs.add(value.get(i).get("DistrictID"));
                    }
                    break;
                case 3://Sub District
//                    getValue.add("Select Sub District");
                    getValue.add(getDynamicLanguageValue(getActivity(), "Select", R.string.Select));
//                    getValue.add(getResources().getString(R.string.SelectDistrict));
                    getIDs.add("0");
                    for (int i = 0; i < value.size(); i++) {
                        getValue.add(value.get(i).get("Sub_district"));
                        getIDs.add(value.get(i).get("Sub_district_Pos"));
                        if (FarmRegistration_Sub_District != null) {
                            if (FarmRegistration_Sub_District.equalsIgnoreCase(value.get(i).get("Sub_district"))) {
                                SubDistrictposselectedatFarmRegistrtion = i;
                            }
                        }
                    }
                    break;
                case 4://Village
//                    getValue.add("Select Village");
                    getValue.add(getDynamicLanguageValue(getActivity(), "Select", R.string.Select));
//                    getValue.add(getResources().getString(R.string.SelectVillage));
                    getIDs.add("0");
                    for (int i = 0; i < value.size(); i++) {
                        getValue.add(value.get(i).get("Village_Final"));
                        getIDs.add(value.get(i).get("Village_ID"));
                        if (FarmRegistration_VillageID != null) {
                            if (FarmRegistration_VillageID.equalsIgnoreCase(value.get(i).get("Village_ID"))) {
                                VillageposselectedatFarmRegistrtion = i;
                            }
                        }
                    }


                    break;
            }
        }
        ArrayAdapter<String> stateListAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, getValue);
        spinner.setAdapter(stateListAdapter);
        switch (flag) {
            case 1://State
                setCustomSearchableSpinner(getActivity(), spinner, "SelectState", R.string.SelectState);
//                spinner.setTitle(getDynamicLanguageValue(getActivity(), "SelectState", R.string.SelectState));
                break;
            case 2:
                break;
            case 3://Sub Districtzx
                setCustomSearchableSpinner(getActivity(), spinner, "SelectSubDistrict", R.string.SelectSubDistrict);
//                spinner.setTitle(getDynamicLanguageValue(getActivity(), "SelectSubDistrict", R.string.SelectSubDistrict));
//                getValue.add(getResources().getString(R.string.SelectDistrict));
                break;
            case 4://Village
                setCustomSearchableSpinner(getActivity(), spinner, "SelectVillage", R.string.SelectVillage);
//                spinner.setTitle(getDynamicLanguageValue(getActivity(), "SelectVillage", R.string.SelectVillage));
//                    getValue.add("Select Village");
//                getValue.add(getResources().getString(R.string.SelectVillage));
                break;
        }
        db.close();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
//                if (position > 0) {
                try {
                    switch (flag) {
//                        case 1://State
//                            AppConstant.stateID = getIDs.get(position);
//                            AppConstant.state = getValue.get(position);
//                            db.open();
//                            allCrop = db.getCropByState(AppConstant.stateID);
//                            final int allCropCount = allCrop.getCount();
//                            String[] cropStringArray = new String[allCropCount + 1];
//                            cropStringArray[0] = "Select Crop";
//                            if (allCropCount > 0) {
//                                allCrop.moveToFirst();
//                                for (int i = 1; i <= allCropCount; i++) {
//                                    cropStringArray[i] = allCrop.getString(allCrop.getColumnIndex(DBAdapter.CROP));
//                                    allCrop.moveToNext();
//                                }
//                            }
//
//                            ArrayAdapter<String> cropArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, cropStringArray); //selected item will look like a spinner set from XML
//                            cropArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                            cropSpiner1.setAdapter(cropArrayAdapter);
//                            cropSpiner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                                @Override
//                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                                    String cropId = "-1";
//                                    String cropName = "";
//                                    System.out.println("Inside CropId selection " + position);
//                                    if (position > 0) {
//                                        allCrop.moveToPosition(position - 1);
//                                        cropId = allCrop.getString(allCrop.getColumnIndex(DBAdapter.CROP_ID));
//                                        cropName = allCrop.getString(allCrop.getColumnIndex(DBAdapter.CROP));
//                                    }
//                                    farmInformationData.setCrop(cropName);
//                                    farmInformationData.setCropID(cropId);
//                                    setVariety(AppConstant.stateID, cropId);
//                                }
//
//                                @Override
//                                public void onNothingSelected(AdapterView<?> parent) {
//
//                                }
//                            });
////Call District
//                            getAPIURL(2, AppConstant.stateID, AppConstant.state);
//                            break;
//                        case 2://District
//                            if (getValue.size() > 0) {
//                                DistrictID = getIDs.get(position);
//                                DistrictName = getValue.get(position);
//                            } else {
//                                DistrictID = null;
//                                DistrictName = null;
//                            }
//                            getAPIURL(3, DistrictID, DistrictName);
//                            break;
                        case 3://Sub District
                            if (getValue.size() > 0) {
                                SubDistrictID = getIDs.get(position);
                                SubDistrictName = getValue.get(position);
                            } else {
                                SubDistrictID = null;
                                SubDistrictName = null;
                            }
                            if (position > 0) {
                                ll_OtherDistrict.setVisibility(View.GONE);
                                OthersubDistrict_edt.setText("");
                            } else {
                                ll_OtherDistrict.setVisibility(View.VISIBLE);
                            }
                            getAPIURL(4, DistrictID, SubDistrictName);
                            break;
                        case 4://Village
                            if (getValue.size() > 0) {
                                VillageID = getIDs.get(position);
                                VillageName = getValue.get(position);
                            } else {
                                VillageID = null;
                                VillageName = null;
                            }
                            if (position > 0) {
                                ll_OtherVillageName.setVisibility(View.GONE);
                                OtherVillageName_edt.setText("");
                            } else {
                                ll_OtherVillageName.setVisibility(View.VISIBLE);
                            }
                            break;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }


//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        try {
            switch (flag) {
//                        case 1://State
//                        case 2://District
                case 3://Sub District
                    if (FarmRegistration_Sub_District != null) {
                        if (SubDistrictposselectedatFarmRegistrtion != 0) {
                            spinner.setSelection(SubDistrictposselectedatFarmRegistrtion);
                        } else {
                            OthersubDistrict_edt.setText(FarmRegistration_Sub_District);
                        }
                    }
                    break;
                case 4://Village
                    if (FarmRegistration_VillageID != null) {
                        if (spinner.getCount() == 0) {
                            if (FarmRegistration_VillageStr != null) {
                                OtherVillageName_edt.setText(FarmRegistration_VillageStr);
                            }
                        } else {
                            spinner.setSelection(VillageposselectedatFarmRegistrtion);
                        }
                    }
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setPhoneType() {
        PhoneType = new ArrayList<>();
//        PhoneType.add("Select Phone Type");
        PhoneType.add(getDynamicLanguageValue(getActivity(), "SelectPhoneType", R.string.SelectPhoneType));
        PhoneType.add("Keypad Phones");
        PhoneType.add("Android Phones");
        PhoneType.add("Apple iPhones");
        PhoneType.add("Others");
        ArrayAdapter<String> phonetypes = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, PhoneType);
        spin_MobileType.setAdapter(phonetypes);
        spin_MobileType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position > 0 && PhoneType.size() > 0) {
                    PhoneTypeValue = PhoneType.get(position).toString();
                } else {
                    PhoneTypeValue = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    public void setProject() {
        db.open();
        Projects = new ArrayList<>();

        ArrayList<HashMap<String, String>> hasmap = new ArrayList<>();
        hasmap = db.getDynamicTableValue("Select * from " + db.TABLE_Projectlist + " where ProjectName !='' and  ProjectName !='null' order by ProjectName");
        ArrayList<String> list = new ArrayList<>();

        HashMap<String, String> hash = new HashMap<>();
        hash.put("ProjectID", "0");
//        hash.put("ProjectName", "Select Project");
        hash.put("ProjectName", getDynamicLanguageValue(getActivity(), "Select", R.string.Select));
        Projects.add(hash);
//        list.add("Select Project");
        list.add(getDynamicLanguageValue(getActivity(), "Select", R.string.Select));
//        list.add(getResources().getString(R.string.EnterProjectName));
        int positionselectedatFarmRegistrtion = 0;
        if (hasmap.size() > 0) {
            for (int i = 1; i <= hasmap.size(); i++) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("ProjectID", hasmap.get(i - 1).get("ProjectID"));
                list.add(hasmap.get(i - 1).get("ProjectName"));
                hashMap.put("ProjectName", hasmap.get(i - 1).get("ProjectName"));
                Projects.add(hashMap);
                if (FarmRegistration_ProjectID != null) {
                    if (FarmRegistration_ProjectID.equalsIgnoreCase(hasmap.get(i - 1).get("ProjectID"))) {
                        positionselectedatFarmRegistrtion = i;
                    }
                }
            }
        }
        ArrayAdapter<String> stateListAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, list);
        spin_project.setAdapter(stateListAdapter);
        setCustomSearchableSpinner(getActivity(), spin_project, "EnterProjectName", R.string.EnterProjectName);
//        spin_project.setTitle(getDynamicLanguageValue(getActivity(), "EnterProjectName", R.string.EnterProjectName));

        spin_project.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position > 0 && Projects.size() > 0) {
                    ProjectID = Projects.get(position).get("ProjectID");
                    ProjectName = Projects.get(position).get("ProjectName");
                    if (FarmEditActive == false) {
                        String URL = AppManager.getInstance().GetAdharNumber(ProjectID);
                        try {
                            new getStateProjectRefresh("AadharList", URL).execute();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        db.close();
        if (FarmEditActive == false) {
            if (FarmRegistration_ProjectID != null) {
                spin_project.setSelection(positionselectedatFarmRegistrtion);
            }
        }
    }

    public void setStateBind() {
        db.open();
        States = new ArrayList<>();

        ArrayList<HashMap<String, String>> hasmap = new ArrayList<>();
        hasmap = db.getDynamicTableValue("select ID,StateID,Upper(StateName) as StateName,DistrictID,Upper(DistrictName) as DistrictName from MstStateDistrict where StateID!='' and StateName!='' group by StateID order by StateName");
        ArrayList<String> list = new ArrayList<>();

        HashMap<String, String> hash = new HashMap<>();
        hash.put("StateID", "0");
//        hash.put("StateName", "Select State");
        hash.put("StateName", getDynamicLanguageValue(getActivity(), "Select", R.string.Select));
        States.add(hash);
//        list.add("Select State");
        list.add(getDynamicLanguageValue(getActivity(), "Select", R.string.Select));

        int StateposselectedatFarmRegistrtion = 0;
        if (hasmap.size() > 0) {
            for (int i = 1; i <= hasmap.size(); i++) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("StateID", hasmap.get(i - 1).get("StateID"));
                list.add(hasmap.get(i - 1).get("StateName"));
                hashMap.put("StateName", hasmap.get(i - 1).get("StateName"));
                States.add(hashMap);
                if (FarmRegistration_StateID != null) {
                    if (FarmRegistration_StateID.equalsIgnoreCase(hasmap.get(i - 1).get("StateID"))) {
                        StateposselectedatFarmRegistrtion = i;
                    }
                }
            }
        }
        ArrayAdapter<String> stateListAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, list);
        stateList.setAdapter(stateListAdapter);
        setCustomSearchableSpinner(getActivity(), stateList, "SelectState", R.string.SelectState);
//        stateList.setTitle(getDynamicLanguageValue(getActivity(), "SelectState", R.string.SelectState));

        stateList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position > 0 && States.size() > 0) {
                    try {
                        //State
                        AppConstant.stateID = States.get(position).get("StateID");
                        AppConstant.state = States.get(position).get("StateName");

                        setCropsBind();
                        setDitrictBind(States.get(position).get("StateID"));

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        db.close();

        if (FarmRegistration_StateID != null) {
            stateList.setSelection(StateposselectedatFarmRegistrtion);
        }
    }

    public void setCropsBind() {
        try {
            db.open();
            allCrop = db.getCropByState(AppConstant.stateID);
            final int allCropCount = allCrop.getCount();
            String[] cropStringArray = new String[allCropCount + 1];
//            cropStringArray[0] = "Select Crop";
            cropStringArray[0] = getDynamicLanguageValue(getActivity(), "Select", R.string.Select);
            if (allCropCount > 0) {
                allCrop.moveToFirst();
                for (int i = 1; i <= allCropCount; i++) {
                    cropStringArray[i] = allCrop.getString(allCrop.getColumnIndex(DBAdapter.CROP));
                    allCrop.moveToNext();
                }
            }
            ArrayAdapter<String> cropArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, cropStringArray); //selected item will look like a spinner set from XML
            cropSpiner1.setAdapter(cropArrayAdapter);
            setCustomSearchableSpinner(getActivity(), cropSpiner1, "SelectCrop", R.string.SelectCrop);
//            cropSpiner1.setTitle(getDynamicLanguageValue(getActivity(), "SelectCrop", R.string.SelectCrop));
            cropSpiner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String cropId = "-1";
                    String cropName = "";
                    System.out.println("Inside CropId selection " + position);
                    if (position > 0) {
                        allCrop.moveToPosition(position - 1);
                        cropId = allCrop.getString(allCrop.getColumnIndex(DBAdapter.CROP_ID));
                        cropName = allCrop.getString(allCrop.getColumnIndex(DBAdapter.CROP));
                    }
//                    if (cropId.equalsIgnoreCase("67") || cropId.equalsIgnoreCase("Potato")) {
//                        ll_noofbags.setVisibility(View.VISIBLE);
//                    } else {
//                        ll_noofbags.setVisibility(View.GONE);
//                    }
                    farmInformationData.setCrop(cropName);
                    farmInformationData.setCropID(cropId);
                    setVariety(AppConstant.stateID, cropId);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            int CropIdposselectedatFarmRegistrtion = 0;
            if (FarmRegistration_CropName != null) {
                for (int i = 1; i <= cropStringArray.length; i++) {
                    String cropName = cropStringArray[i];
                    if (FarmRegistration_CropName.equalsIgnoreCase(cropName)) {
                        CropIdposselectedatFarmRegistrtion = i;
                        break;
                    }
                }
                cropSpiner1.setSelection(CropIdposselectedatFarmRegistrtion);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setDitrictBind(String State_id) {
        db.open();
        Districts = new ArrayList<>();

        ArrayList<HashMap<String, String>> hasmap = new ArrayList<>();
        String sql = "select ID,StateID,Upper(StateName) as StateName,DistrictID,Upper(DistrictName) as DistrictName from MstStateDistrict where StateID='" + State_id + "' and DistrictID!='' and DistrictName!='' group by DistrictID order by DistrictName";
        hasmap = db.getDynamicTableValue(sql);
        ArrayList<String> list = new ArrayList<>();

        HashMap<String, String> hash = new HashMap<>();
        hash.put("DistrictID", "0");
//        hash.put("DistrictName", "Select District");
        hash.put("DistrictName", getDynamicLanguageValue(getActivity(), "Select", R.string.Select));
        Districts.add(hash);
//        list.add("Select District");
        list.add(getDynamicLanguageValue(getActivity(), "Select", R.string.Select));

        int DistrictposselectedatFarmRegistrtion = 0;
        if (hasmap.size() > 0) {
            for (int i = 1; i <= hasmap.size(); i++) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("DistrictID", hasmap.get(i - 1).get("DistrictID"));
                list.add(hasmap.get(i - 1).get("DistrictName"));
                hashMap.put("DistrictName", hasmap.get(i - 1).get("DistrictName"));
                Districts.add(hashMap);
                if (FarmRegistration_DistrictID != null) {
                    if (FarmRegistration_DistrictID.equalsIgnoreCase(hasmap.get(i - 1).get("DistrictID"))) {
                        DistrictposselectedatFarmRegistrtion = i;
                    }
                }
            }
        }
        ArrayAdapter<String> stateListAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, list);
        DistrictName_spin.setAdapter(stateListAdapter);
        setCustomSearchableSpinner(getActivity(), DistrictName_spin, "SelectDistrict", R.string.SelectDistrict);
//        DistrictName_spin.setTitle(getDynamicLanguageValue(getActivity(), "SelectDistrict", R.string.SelectDistrict));
        DistrictName_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position > 0 && Districts.size() > 0) {
                    DistrictID = Districts.get(position).get("DistrictID");
                    DistrictName = Districts.get(position).get("DistrictName");
                    getAPIURL(3, DistrictID, DistrictName);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        db.close();

        if (FarmRegistration_DistrictID != null) {
            DistrictName_spin.setSelection(DistrictposselectedatFarmRegistrtion);
        }

    }

    private class getStateDistVillage extends AsyncTask<Void, Void, String> {

        int flag = 0;
        String URL = "";
        TransparentProgressDialog progressDialog;

        public getStateDistVillage(int flags, String URLs) {
            flag = flags;
            URL = URLs;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            String sms = "";
            switch (flag) {
//                case 1://State
//                    progressDialog.setMessage(getResources().getString(R.string.LoadingStates));
//                    break;
//                case 2://District
//                    progressDialog.setMessage(getResources().getString(R.string.LoadingDistricts));
//                    break;
                case 3://Sub District
                    sms = getDynamicLanguageValue(getActivity(), "LoadingSubDistricts", R.string.LoadingSubDistricts);
                    break;
                case 4://Village
                    sms = getDynamicLanguageValue(getActivity(), "LoadingVillages", R.string.LoadingVillages);
                    break;
            }
            progressDialog = new TransparentProgressDialog(
                    getActivity(), sms);
            progressDialog.setCancelable(false);
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    // cancel AsyncTask
                    cancel(false);
                }
            });
            progressDialog.show();
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
            try {
                String response = AppManager.getInstance().httpRequestGetMethod(URL);
                return AppManager.getInstance().RemoveStringUnwanted(response);

            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPreExecute();

//            if (result.equalsIgnoreCase("No Data")) {
            if (result.equalsIgnoreCase("No Data") || result.equalsIgnoreCase("Could not connect to server")) {
                switch (flag) {
//                    case 1://State
//                        States = null;
//                        AppConstant.state = "";
//                        AppConstant.stateID = "";
//                        SetSpinner(flag, stateList, States);
//                        break;
//                    case 2://District
//                        Districts = null;
//                        DistrictName = null;
//                        DistrictID = null;
//                        SetSpinner(flag, DistrictName_spin, Districts);
//                        //Sub District
//                        SubDistricts = null;
//                        SubDistrictName = null;
//                        SubDistrictID = null;
//                        SetSpinner(flag, SubDistrictName_spin, SubDistricts);
//                        //Village
//                        Villages = null;
//                        VillageName = null;
//                        VillageID = null;
//                        SetSpinner(flag, VillageName_spin, Villages);
//                        break;
                    case 3://Sub District
                        SubDistricts = null;
                        SubDistrictName = null;
                        SubDistrictID = null;
                        SetSpinner(flag, SubDistrictName_spin, SubDistricts);
                        //Village
                        Villages = null;
                        VillageName = null;
                        VillageID = null;
                        SetSpinner(flag, VillageName_spin, Villages);
                        break;
                    case 4://Village
                        Villages = null;
                        VillageName = null;
                        VillageID = null;
                        SetSpinner(flag, VillageName_spin, Villages);
                        break;
                }
//                Toast.makeText(getActivity(), getResources().getString(R.string.Nodataavailable), Toast.LENGTH_LONG).show();
            } else {
                try {
                    //stateList,DistrictName_spin,SubDistrictName_spin,VillageName_spin
                    switch (flag) {
//                        case 1://State
//                            JSONObject json = new JSONObject(result.toString());
//                            JSONArray jsonArray_State = json.getJSONArray("DT");
//                            States = getStateDistrictValues(flag, jsonArray_State.toString());
//                            SetSpinner(flag, stateList, States);
//                            break;
//                        case 2://District
//                            JSONArray jsonArray_Districts = new JSONArray(result.toString());
//                            Districts = getStateDistrictValues(flag, jsonArray_Districts.toString());
//                            SetSpinner(flag, DistrictName_spin, Districts);
//                            break;
                        case 3://Sub District
                            JSONArray jsonArray_SubDistricts = new JSONArray(result.toString());
                            SubDistricts = getStateDistrictValues(flag, jsonArray_SubDistricts.toString());
                            SetSpinner(flag, SubDistrictName_spin, SubDistricts);
                            break;
                        case 4://Village
                            JSONArray jsonArray_Villages = new JSONArray(result.toString());
                            Villages = getStateDistrictValues(flag, jsonArray_Villages.toString());
                            SetSpinner(flag, VillageName_spin, Villages);
                            break;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    getDynamicLanguageToast(getActivity(), "Couldnotconnect", R.string.Couldnotconnect);
                }
            }

            progressDialog.dismiss();

        }
    }

    private class sentRequestForFarmSave extends AsyncTask<Void, Void, String> {

        String result = null;
        ProgressDialog progressDialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage(getDynamicLanguageValue(getActivity(), "Submittingtagged", R.string.Submittingtagged));
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(true);
            progressDialog.show();

        }

        @Override
        protected String doInBackground(Void... params) {

            String response = null;
            try {
                db.open();
                String sendPath = AppManager.getInstance().saveFarmInfo;//Herojit Comment
                Cursor isUserSaved = db.isUserSaved(AppConstant.user_id);

                System.out.println("Save URL : " + sendPath + " Is user saved : " + isUserSaved.getCount() + " For Userid : " + AppConstant.user_id);
                String createdString = null;
                String jsonParameterString = createJsonParameterForSaveForm(farmInformationData, cropQueryData);
                createdString = AppManager.getInstance().removeSpaceForUrl(jsonParameterString);
                if (isUserSaved.getCount() > 0) {
                    //   writeToSDFile( data);
                    String result = saveUser(isUserSaved);
                    if (result.contains("OK")) {
                        farmInformationData.setUserID(AppConstant.user_id);
                        Log.v("vishal", "--" + creatString);
                        Log.v("tripathi", "---" + jsonParameterString);
                        data = createdString;

                        response = AppManager.getInstance().httpRequestPutMethod(sendPath, createdString);
                    } else {
                        return AppConstant.SERVER_CONNECTION_ERROR;
                    }
                } else {
                    response = AppManager.getInstance().httpRequestPutMethod(sendPath, createdString);
                }
                System.out.println("Save Response :---" + response);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPreExecute();
            progressDialog.dismiss();

            Log.v("response_saved_farm", response.toString() + "");
            try {
                if (response != null) {
                    if (response.contains("Success")) {
                        response = response.replace("\"", "");
                        String[] resArray = response.split(":");
                        if (resArray.length > 0) {
                            int i = 0;
                            do {
                                if (resArray[i].contains("Success")) {
                                    if (resArray.length > i + 1) {
                                        String newFormId = resArray[i + 1];
                                        farmInformationData.setFarmId(newFormId);
                                        break;
                                    }
                                }
                                i++;
                            } while (i < resArray.length);
                        }
                        saveInitialFarm(DBAdapter.SENT);
                        //   Toast.makeText(getActivity(), "Farm save successfully", Toast.LENGTH_LONG).show();
                        if (AppManager.isOnline(getActivity())) {
//                    new sentFarmInformationData().execute();
//                        SharedPreferences myPreference = getActivity().getSharedPreferences(AppConstant.SHARED_PREFRENCE_NAME, getActivity().MODE_PRIVATE);
//                        String languagePreference = myPreference.getString(getResources().getString(R.string.language_pref_key), "1");
//                        int languageConstant = Integer.parseInt(languagePreference);

                            submitPopup_english();
                            AppConstant.selectedFarm = farmerName.getText().toString();

                        } else {
                            getDynamicLanguageToast(getActivity(), "Networknotavailable", R.string.Networknotavailable);
                        }
                    }
                    if (response.contains("NotSave")) {
                        saveInitialFarm(DBAdapter.SENT);
                        getDynamicLanguageToast(getActivity(), "ServerRejected", R.string.ServerRejected);
                        return;
                    }
                    if (response.contains("Error")) {
                        saveInitialFarm(DBAdapter.SENT);
                        getDynamicLanguageToast(getActivity(), "Farmalreadyexist", R.string.Farmalreadyexist);

                        final FragmentTransaction ft = getFragmentManager().beginTransaction();
                        String latLngStateId = latitude + "," + longitude + "," + stateId;
                        Fragment fragment = FarmAdvisoryFragment.newInstance(cropQueryData, latLngStateId, farmInformationData.getFarmId());
                        ft.replace(R.id.fragmentContainer, fragment, "NewFragmentTag");
                        ft.addToBackStack("advr");
                        ft.commit();

                        AppConstant.selectedFarm = farmerName.getText().toString();

                        return;
                    }
                    if (response.contains(AppConstant.SERVER_CONNECTION_ERROR)) {
                        saveInitialFarm(DBAdapter.SAVE);

                        //

                        //       writeToSDFile( data);
                        // Toast.makeText(getActivity(), "Could not connect to server", Toast.LENGTH_LONG).show();
                        savingAlert();
                        return;
                    }
                } else {
                    //          writeToSDFile( data);
                    saveInitialFarm(DBAdapter.SAVE);
                    savingAlert();
                    // Toast.makeText(getActivity(), "Could not connect to server", Toast.LENGTH_LONG).show();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            FarmRegistration_AadharNo = null;
            FarmRegistration_ProjectID = null;

        }
    }

    private class LoginAsyncTask extends AsyncTask<Void, Void, String> {

        String u_id = null;
        String u_pass = null;
        ProgressDialog progressDialog;

        public LoginAsyncTask(String id, String pas) {
            u_id = id;
            u_pass = pas;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage(getDynamicLanguageValue(getActivity(), "LoginProgress", R.string.LoginProgress));
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

                sendRequest = AppManager.getInstance().login + AppManager.getInstance().removeSpaceForUrl(u_id) + "/" + u_pass;
                Log.d("sync login data", sendRequest);
                return AppManager.getInstance().httpRequestGetMethod(sendRequest);


            } catch (Exception ex) {
                ex.printStackTrace();

                return null;
            }

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPreExecute();
            try {

                if (result.contains("[]")) {
                    getDynamicLanguageToast(getActivity(), "IncorrectUsernameorPassword", R.string.IncorrectUsernameorPassword);

                } else {
                    //  SharedPreferences prefs = getSharedPreferences("user_detail", MODE_PRIVATE);
                    // SharedPreferences.Editor edit = prefs.edit();
                    JSONArray jArray = new JSONArray(result);
                    for (int i = 0; i < jArray.length(); i++) {

                        if (i == 0) {
                            JSONObject jObject = jArray.getJSONObject(i);
                            if (prefs == null) {
                                prefs = getActivity().getSharedPreferences(AppConstant.SHARED_PREFRENCE_NAME, getActivity().MODE_PRIVATE);
                            }
                            SharedPreferences.Editor editor = prefs.edit();

                            AppConstant.user_id = jObject.getString("UserID");
                            AppConstant.visible_Name = jObject.getString("VisibleName");


                            editor.putString(AppConstant.PREFRENCE_KEY_USER_ID, AppConstant.user_id);
                            editor.putString(AppConstant.PREFRENCE_KEY_VISIBLE_NAME, AppConstant.visible_Name);
                            editor.apply();

                            AppConstant.isLogin = true;
                        }


                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                getDynamicLanguageToast(getActivity(), "Couldnotconnect", R.string.Couldnotconnect);
            }

            progressDialog.dismiss();

        }
    }

    private class getStateProjectRefresh extends AsyncTask<Void, Void, String> {

        String flag = "";
        String URL = "";
        TransparentProgressDialog progressDialog;

        public getStateProjectRefresh(String flags, String URLs) {
            flag = flags;
            URL = URLs;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            String sms = "";
            switch (flag) {
                case "Project"://State
                    sms = getDynamicLanguageValue(getActivity(), "LoadingProject", R.string.LoadingProject);
                    break;
                case "StateDistrict"://District
                    sms = getDynamicLanguageValue(getActivity(), "Loadingstate", R.string.Loadingstate);
                    break;
                case "Crops"://District
                    sms = getDynamicLanguageValue(getActivity(), "LoadingCrops", R.string.LoadingCrops);
                    break;
                case "AadharList"://Aadhar List base on ProjectID
                    sms = getDynamicLanguageValue(getActivity(), "loadingAadhar", R.string.loadingAadhar);
                    break;
                case "AadharInformation"://Aadhar List base on ProjectID
                    sms = getDynamicLanguageValue(getActivity(), "loadingInformation", R.string.loadingInformation);
                    break;
            }
            progressDialog = new TransparentProgressDialog(getActivity(), sms);
            progressDialog.setCancelable(false);
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
            try {
                String response = AppManager.getInstance().httpRequestGetMethod(URL);
                return "[" + response + "]";

            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(String response) {
            super.onPreExecute();
            try {
                if (response != null) {
                    switch (flag) {
                        case "Project":
                            response = response.trim();
                            response = response.substring(1, response.length() - 1);
                            response = response.replace("\\", "");

                            db.open();
                            db.getSQLiteDatabase().execSQL("DROP TABLE IF EXISTS " + db.TABLE_Projectlist);
                            db.getSQLiteDatabase().execSQL(db.CREATE_TABLE_Projectlist);

                            io.requery.android.database.sqlite.SQLiteDatabase SqliteDB = db.getSQLiteDatabase();
                            SqliteDB.beginTransaction();
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = new JSONObject(jsonArray.get(i).toString());
                                    String query = "INSERT INTO " + DBAdapter.TABLE_Projectlist + "(ProjectID,ProjectName) VALUES ('" + obj.get("ID").toString() + "','" + obj.get("Name").toString() + "')";
                                    db.getSQLiteDatabase().execSQL(query);
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                                getDynamicLanguageToast(getActivity(), "FormattingError", R.string.FormattingError);
                            }
                            SqliteDB.setTransactionSuccessful();
                            SqliteDB.endTransaction();
                            db.getClass();

                            setProject();
                            break;
                        case "StateDistrict":
                            response = response.trim();
                            response = response.substring(1, response.length() - 1);
                            response = response.replace("\\", "");
                            db.open();
                            db.getSQLiteDatabase().execSQL("DROP TABLE IF EXISTS " + db.TABLE_StateDistrict);
                            db.getSQLiteDatabase().execSQL(db.CREATE_StateDistrict);
                            try {
                                if (response != null) {
                                    JSONObject jsonObject = new JSONObject(response);
                                    JSONArray jsonArray = jsonObject.getJSONArray("DT");
                                    db.insertStateDistrict(jsonArray);
                                    db.close();
                                } else {
                                    getDynamicLanguageToast(getActivity(), "ServerError", R.string.ServerError);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                getDynamicLanguageToast(getActivity(), "FormattingError", R.string.FormattingError);
                            }
                            setStateBind();
                            break;
                        case "Crops":
                            response = response.trim();
                            response = response.substring(1, response.length() - 1);
                            response = response.replace("\\", "");

                            db.open();
                            db.getSQLiteDatabase().execSQL("DROP TABLE IF EXISTS " + db.TABLE_CROP_VARIETY);
                            db.getSQLiteDatabase().execSQL(db.CREATE_CROP_VATIETY);

                            io.requery.android.database.sqlite.SQLiteDatabase SqliteDB1 = db.getSQLiteDatabase();
                            SqliteDB1.beginTransaction();
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                String query = "INSERT INTO " + DBAdapter.TABLE_CROP_VARIETY + "(" + DBAdapter.STATE_ID + "," + DBAdapter.CROP_ID + "," + DBAdapter.CROP + "," + DBAdapter.VARIETY + ") VALUES (?,?,?,?)";
                                SQLiteStatement stmt = SqliteDB1.compileStatement(query);

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONArray territoryElementArray = jsonArray.getJSONArray(i);

                                    stmt.bindString(1, territoryElementArray.get(3).toString());
                                    stmt.bindString(2, territoryElementArray.get(0).toString());
                                    stmt.bindString(3, territoryElementArray.get(1).toString());
                                    stmt.bindString(4, territoryElementArray.get(2).toString());
                                    stmt.execute();
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                                getDynamicLanguageToast(getActivity(), "FormattingError", R.string.FormattingError);
                            }

                            SqliteDB1.setTransactionSuccessful();
                            SqliteDB1.endTransaction();
                            setCropsBind();
                            break;
                        case "AadharList":
                            response = response.trim();
                            response = response.substring(1, response.length() - 1);
                            response = response.replace("\\", "");
                            setAadharList(response);
                            break;
                        case "AadharInformation"://set Aadhar INformation
                            response = response.trim();
                            response = response.substring(1, response.length() - 1);
                            response = response.replace("\\", "");
                            setInformationBaseonAadharNo(response);
                            break;
                    }

                } else
                    getDynamicLanguageToast(getActivity(), "Nodataavailable", R.string.Nodataavailable);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            progressDialog.dismiss();
        }

    }

    public void setInformationBaseonAadharNo(String Response) {
//        "[{\"FarmerName\":\"farm name\",\"FarmerPhno\":\"9638566666\",\"FatherName\":\"father name\"," +
//                "\"PanNumber\":\"\",\"VillageID\":\"\",\"StateID\":\"NA\",\"DistrictID\":\"NA\",\"Sub_District\":\"NA\"}]"


        if (Response != null && Response.length() > 0) {
            try {
                JSONArray array = new JSONArray(Response.toString());
                JSONObject obj = new JSONObject(array.get(0).toString());
//                FarmRegistration_PhoneType = obj.getString("PhoneType");
                FarmRegistration_FarmName = "";
                FarmRegistration_FarmerName = obj.getString("FarmerName");
                FarmRegistration_FarmerPhno = obj.getString("FarmerPhno");
                FarmRegistration_FatherName = obj.getString("FatherName");
                FarmRegistration_PanNumber = obj.getString("PanNumber");
                FarmRegistration_StateID = obj.getString("StateID");
                FarmRegistration_DistrictID = obj.getString("DistrictID");
                FarmRegistration_Sub_District = obj.getString("Sub_District");
                FarmRegistration_VillageID = obj.getString("VillageID");
//              FarmRegistration_FarmerName = null, FarmRegistration_FarmerPhno = null, FarmRegistration_FatherName = null,
//              FarmRegistration_PanNumber = null, FarmRegistration_StateID = null, FarmRegistration_DistrictID = null,
//              FarmRegistration_Sub_District = null, FarmRegistration_VillageID = null;

                if (FarmRegistration_FarmName != null) {
                    farmName.setText(FarmRegistration_FarmName);
                }
                if (FarmRegistration_FarmerName != null) {
                    farmerName.setText(FarmRegistration_FarmerName);
                }
                if (FarmRegistration_FarmerPhno != null) {
                    farmerNumber.setText(FarmRegistration_FarmerPhno);
                }
                if (FarmRegistration_FatherName != null) {
                    editText_FatherName.setText(FarmRegistration_FatherName);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            setPhoneType();
            setStateBind();
        } else {
            cleatFields();
        }
    }

    public void cleatFields() {
        farmName.setText("");
        farmerName.setText("");
        farmerNumber.setText("");
        editText_FatherName.setText("");
        stateList.setSelection(0);
        DistrictName_spin.setSelection(0);
        DistrictName_spin.setSelection(0);
        SubDistrictName_spin.setSelection(0);
        OthersubDistrict_edt.setText("");
        VillageName_spin.setSelection(0);
        OtherVillageName_edt.setText("");
    }

    public void setAadharList(String response) {

        try {
            JSONArray array = new JSONArray();
            JSONObject obj = new JSONObject();
            int AaadharposselectedatFarmRegistrtion = 0;

            try {
                JSONObject ob = new JSONObject(response);
//                array = new JSONArray(response);
                array = ob.getJSONArray("DT");
                AadharList = new ArrayList<>();
//                AadharList.add("Select Aadhar Number");
                AadharList.add(getDynamicLanguageValue(getActivity(), "SelectAadharNumber", R.string.SelectAadharNumber));
                for (int i = 0; i < array.length(); i++) {
                    obj = new JSONObject(array.get(i).toString());
                    String aadhar = obj.get("AdharNumber").toString();
                    AadharList.add(aadhar);
                    if (FarmEditActive == false) {
                        if (FarmRegistration_AadharNo != null) {
                            if (FarmRegistration_AadharNo.equalsIgnoreCase(aadhar)) {
                                AaadharposselectedatFarmRegistrtion = i;
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();

            }

            radiogroup_aadhar.setVisibility(View.VISIBLE);
            edtaadhar_no.setVisibility(View.VISIBLE);
            tblrow_other.setVisibility(View.GONE);
            edtother.setText("");
            if (AadharList.size() > 1) {
                tbl_getAadhar.setVisibility(View.VISIBLE);
            } else {
                tbl_getAadhar.setVisibility(View.GONE);
            }

            ArrayAdapter<String> phonetypes = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, AadharList);
            spin_getAadharlsit.setAdapter(phonetypes);
            spin_getAadharlsit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                    if (AadharList.size() > 0 && position > 0) {
                        AadharID_No = AadharList.get(position).toString();
                        if (FarmEditActive == false) {
                            String URL = AppManager.getInstance().GetFarmPersonalInfo(AadharID_No);
                            try {
                                new getStateProjectRefresh("AadharInformation", URL).execute();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                            radiogroup_aadhar.setVisibility(View.GONE);
                            edtaadhar_no.setVisibility(View.GONE);
                            edtaadhar_no.setText("");
                            tblrow_other.setVisibility(View.GONE);
                            edtother.setText("");
                        }
                    } else {
                        if (FarmEditActive == false) {
                            AadharID_No = "";
                            tbl_getAadhar.setVisibility(View.VISIBLE);
                            radiogroup_aadhar.setVisibility(View.VISIBLE);
                            edtaadhar_no.setVisibility(View.VISIBLE);
                            tblrow_other.setVisibility(View.GONE);
                            edtother.setText("");
                            if (AadharList.size() > 1) {
                                tbl_getAadhar.setVisibility(View.VISIBLE);
                            } else {
                                tbl_getAadhar.setVisibility(View.GONE);
                            }
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            if (FarmEditActive == false) {
                if (FarmRegistration_AadharNo != null) {
                    spin_getAadharlsit.setSelection(AaadharposselectedatFarmRegistrtion);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();

        }
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
        setScreenTracking(getActivity(), db, SN_LocateYoutFarmFragment, UID);
    }

    //set FarmDetails
    public void setEditFarmerDetails() {

        try {
            JSONObject obj = new JSONObject(FarmEditDetails);
            String Contour = setString(obj.getString("Contour"));
            String ProjectID = setString(obj.getString("ProjectID"));

            String CenterLat = setString(obj.getString("CenterLat"));
            String CenterLon = setString(obj.getString("CenterLon"));
            String FarmID = setString(obj.getString("FarmID"));

            farmInformationData.setFarmId(FarmID);
//            String AadharNo = setString(obj.getString("AadharNo"));
//            String MobileType = setString(obj.getString("MobileType"));
//            String FarmName = setString(obj.getString("FarmName"));
//            String FarmerName = setString(obj.getString("FarmerName"));
//            String PhoneNo = setString(obj.getString("PhoneNo"));
//            String FatherName = setString(obj.getString("FatherName"));
//            String StateID = setString(obj.getString("State"));
//            String District = setString(obj.getString("District"));
//            String Sub_District = setString(obj.getString("Sub_District"));
//            String VillageID = setString(obj.getString("VillageID"));
//            String VillageStr = setString(obj.getString("VillageStr"));
//            String TagArea = setString(obj.getString("Area"));
//            String ActualFarmArea = setString(obj.getString("FarmArea"));

            FarmRegistration_FarmName = obj.getString("FarmName");
            FarmRegistration_FarmerName = obj.getString("FarmerName");

            FarmRegistration_FarmerPhno = obj.getString("PhoneNo");
            FarmRegistration_FatherName = obj.getString("FatherName");
            FarmRegistration_StateID = obj.getString("State");
            FarmRegistration_DistrictID = obj.getString("DistrictID");
//            FarmRegistration_Sub_District = obj.getString("Sub_District");
            FarmRegistration_Sub_District = obj.getString("Block");
            FarmRegistration_VillageID = obj.getString("VillageID");
            FarmRegistration_VillageStr = setString(obj.getString("VillageStr"));
            String NoOfBags = setString(obj.getString("NoOfBags"));


            if (FarmRegistration_FarmName != null) {
                farmName.setText(FarmRegistration_FarmName);
            }
            if (FarmRegistration_FarmerName != null) {
                farmerName.setText(FarmRegistration_FarmerName);
            }
            if (FarmRegistration_FarmerPhno != null) {
                farmerNumber.setText(FarmRegistration_FarmerPhno);
            }
            if (FarmRegistration_FatherName != null) {
                editText_FatherName.setText(FarmRegistration_FatherName);
            }
            if (FarmRegistration_VillageID != null && FarmRegistration_VillageID.length() > 2) {
            } else {
                OtherVillageName_edt.setText(FarmRegistration_VillageStr);
            }
            if (NoOfBags != null) {
                edtnoofbags.setText(NoOfBags);
            }
            if (NoOfBags != null) {
                edtnoofbags.setText(NoOfBags);
            }
            //Add this 2 field news
            try {
                String IBCode = setString(obj.getString("IBCode"));
                if (IBCode.length() > 0) {
                    edt_IBFarmerCode.setText(IBCode);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            JSONArray array = obj.getJSONArray("CropInfo");
            String N = null, P = null, K = null, SowDate = null, CropFrom = null, CropTo = null;
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj1 = new JSONObject(array.get(0).toString());
                FarmRegistration_CropID = setString(obj1.getString("CropID"));
                FarmRegistration_CropName = setString(obj1.getString("CropName"));
                FarmRegistration_Variety = setString(obj1.getString("Variety"));
                N = setString(obj1.getString("N"));
                P = setString(obj1.getString("P"));
                K = setString(obj1.getString("K"));
                String BasalDoseApply = setString(obj1.getString("BasalDoseApply"));
                SowDate = setString(obj1.getString("SowDate"));
                CropFrom = setString(obj1.getString("CropFrom"));
                CropTo = setString(obj1.getString("CropTo"));
                String OtherNutrient = setString(obj1.getString("OtherNutrient"));
                break;
            }

            //Aadhar Other
            radiogroup_aadhar.setVisibility(View.VISIBLE);
            edtaadhar_no.setVisibility(View.GONE);
            tblrow_other.setVisibility(View.VISIBLE);
            rb_other.setChecked(true);
            edtaadhar_no.setText(FarmRegistration_AadharNo);

            setProject();
            setPhoneType();
            setStateBind();
            setCropsBind();
            sowPeriodFrom.setText(CropFrom);
            sowPeriodTo.setText(CropTo);
            if ((N != null && N.length() > 1) || (N != null && N.length() > 1) || (N != null && N.length() > 1)) {
                baselDoseSpiner.setSelection(1);
                valueN.setText(N);
                valueP.setText(P);
                valueK.setText(K);
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


}
