package com.weather.risk.mfi.myfarminfo.marketplace;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
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
import com.weather.risk.mfi.myfarminfo.activities.MainProfileActivity;
import com.weather.risk.mfi.myfarminfo.database.DBAdapter;
import com.weather.risk.mfi.myfarminfo.home.AppController;
import com.weather.risk.mfi.myfarminfo.mapfragments.LatLonCellID;
import com.weather.risk.mfi.myfarminfo.retofitservices.ApiService;
import com.weather.risk.mfi.myfarminfo.retofitservices.ResponseObserver;
import com.weather.risk.mfi.myfarminfo.retrofitmodelnew.FarmerDetailsResponse;
import com.weather.risk.mfi.myfarminfo.retrofitmodelnew.FarmerListRequest;
import com.weather.risk.mfi.myfarminfo.utils.AppConstant;
import com.weather.risk.mfi.myfarminfo.utils.AppManager;
import com.weather.risk.mfi.myfarminfo.utils.CameraUtils;
import com.weather.risk.mfi.myfarminfo.utils.TransparentProgressDialog;
import com.weather.risk.mfi.myfarminfo.utils.UtilFonts;
import com.weather.risk.mfi.myfarminfo.utils.Utility;
import com.weather.risk.mfi.myfarminfo.volley_class.CustomJSONObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.requery.android.database.sqlite.SQLiteStatement;
import retrofit2.Response;

import static com.weather.risk.mfi.myfarminfo.customcamera.CameraSurfaceView.IMAGE_DIRECTORY;
import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.SN_FarmRegistration;
import static com.weather.risk.mfi.myfarminfo.utils.AppManager.NOGPSDialog;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getDynamicLanguageValue;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getUIDforScreenTracking;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setScreenTracking;


public class FarmRegistrationNew extends AppCompatActivity {

    public static String StateID = null, StateName = null, DistrictID = null, DistrictName = null, SubDistrictID = null,
            SubDistrictName = null, VillageID = null, VillageName = null, ProjectID = null, ProjectName = null;
    ImageView printimageview, imge_state_refresh, project_refresh, imge_crop_refresh,
            choose_image1, choose_image2, choose_image3, choose_image4, btn_imageuploadnext;
    Button imageupload1, imageupload2, imageupload3, imageupload4;
    ArrayList<HashMap<String, String>> Projects = new ArrayList<>();
    ArrayList<HashMap<String, String>> States = new ArrayList<>();
    ArrayList<HashMap<String, String>> Districts = new ArrayList<>();
    ArrayList<HashMap<String, String>> SubDistricts = new ArrayList<>();
    ArrayList<HashMap<String, String>> Villages = new ArrayList<>();
    ArrayList<HashMap<String, String>> Crops = new ArrayList<>();
    ArrayList<HashMap<String, String>> Variety = new ArrayList<>();
    ArrayList<String> PhoneType = new ArrayList<>();

    TableRow tblrow_aadhar, tblrow_other, ll_OtherDistrict, ll_OtherVillageName;
    RadioGroup radiogroup_aadhar, radiogroup_aacounttype;
    String /*PhoneTypeValue = "",*/ AccountType = "Self",
            imageString1 = "", imageString2 = "", imageString3 = "", imageString4 = "",
            imageStoragePath1 = "", imageStoragePath2 = "", imageStoragePath3 = "", imageStoragePath4 = "", userChoosenTask = "";

    Button submit, btnGeoTag, product_btn;

    String cropId2 = "0", cropName2 = "",
            VarietyID2 = "0", VarietyName2 = "";


    int Imageselectflag = 1;
    public static final String IMAGE_EXTENSION = "jpg";
    public static final int MEDIA_TYPE_IMAGE = 1;
    private int REQUEST_CAMERA_START1 = 0, SELECT_FILE_START1 = 1, REQUEST_CAMERA_START2 = 2, SELECT_FILE_START2 = 3,
            REQUEST_CAMERA_START3 = 4, SELECT_FILE_START3 = 5, REQUEST_CAMERA_START4 = 6, SELECT_FILE_START4 = 7;
    JSONArray imageList;
    String checkAadharOther = "Aadhar";

    String UID = "";


    TextView txt_FarmRegistration, txt_Project, txt_AadharNo, txt_Other, txt_FarmerName, txt_FatherName, txt_Dates, txt_UploadImage,
            txt_FarmerPhoneNumber, txt_StateName, txt_DistrictName, txt_SubDistrictName, txt_OtherDistrict, txt_VillageName,
            txt_OtherVillageName, txt_SowingDates, txt_AccountType, txt_BankName, txt_BranchName, txt_AccountNumber, txt_IFSCCode,
            txt_Crops, txt_Varieties;
    TextView txt_Date, txt_SowingDate;
    RadioButton rb_aadhar, rb_other, radio_self, radio_other;
    Spinner spin_project, spin_state, spin_district, spin_subdistrict, spin_villageName,
            spin_crop_2,
            spin_variety_2;
    EditText edtaadhar_no, edtother, edtFarmerName, edit_FatherName, editFarmerPhoneNumber,
            edit_othersubdistrict, edit_othervillageName;
    EditText edit_BankName, edit_BranchName, edit_AccountNumber, edit_IFSCCode;


    private ApiService apiService;
    private CompositeDisposable compositeDisposable;
    FarmerDetailsResponse responsesData = null;

    DBAdapter db;

    String balanceAmount = null;
    String state_id = null;
    String district_id = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.farmregistrationnew);
        compositeDisposable = new CompositeDisposable();
        apiService = AppController.getInstance().getApiService();
        setIdDefine();
        db = new DBAdapter(this);
        imageList = new JSONArray();
        if (!AppManager.getInstance().isLocationServicesAvailable(this)) {
            NOGPSDialog(this);
        }
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            NOGPSDialog(this);
        }
        setProject();
//        setPhoneType();
        setStateBind();
        radiogroup_aadhar.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_aadhar) {
                    tblrow_aadhar.setVisibility(View.VISIBLE);
                    tblrow_other.setVisibility(View.GONE);
                    edtother.setText("");
                    checkAadharOther = "Aadhar";
                } else if (checkedId == R.id.rb_other) {
                    tblrow_aadhar.setVisibility(View.GONE);
                    tblrow_other.setVisibility(View.VISIBLE);
                    edtaadhar_no.setText("");
                    checkAadharOther = "Other";
                }

            }
        });
        radiogroup_aacounttype.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radio_self) {
                    AccountType = "Self";
                } else if (checkedId == R.id.radio_other) {
                    AccountType = "Other";
                }

            }
        });
        imge_state_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    new getStateProjectRefresh("StateDistrict", AppManager.getInstance().StateDistrictURL).execute();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
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
        //Date Picker
        txt_Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.setDatePicker(FarmRegistrationNew.this, txt_Date, "YYYYMMDD");
            }
        });
        txt_SowingDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.setDatePicker(FarmRegistrationNew.this, txt_SowingDate, "YYYYMMDD");
            }
        });
        final String farmerId = getIntent().getStringExtra("farmerId");
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkValidation(1) == true) {
                    new setFarmRegistrationSave("Submit").execute();
                }


              /*  Intent in = new Intent(getApplicationContext(), ProductActivity.class);
                in.putExtra("balance", balanceAmount);
                in.putExtra("farmerId", farmerId);
                in.putExtra("project_id", ProjectID);
                startActivity(in);*/
            }
        });
        product_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkValidation(1) == true) {
                    new setFarmRegistrationSave("Product").execute();
                }


              /*  Intent in = new Intent(getApplicationContext(), ProductActivity.class);
                in.putExtra("balance", balanceAmount);
                in.putExtra("farmerId", farmerId);
                in.putExtra("project_id", ProjectID);
                startActivity(in);*/
            }
        });
        btnGeoTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkValidation(1) == true) {
                    new setFarmRegistrationSave("SubmitGeoTag").execute();
                }

            }
        });
        btn_imageuploadnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImagePopup();
            }
        });
        printimageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkValidation(0) == true) {
                    savePdfTable();
                }
            }
        });
        //ScreenTracking
        UID = getUIDforScreenTracking();
        setScreenTracking(this, db, SN_FarmRegistration, UID);
        if (farmerId != null && !farmerId.equalsIgnoreCase("null")) {
            getFarmerDetailsMethod(farmerId);
        }
    }

    private boolean checkValidation(int flag) {
        /*if (!Utility.setStringCheck(ProjectName) || ProjectName.equalsIgnoreCase("Select Project")) {
            Toast.makeText(this, getResources().getString(R.string.EnterProjectName), Toast.LENGTH_LONG).show();
            return false;
        } else*/
        if (!Utility.setStringCheck(edtaadhar_no.getText().toString()) && !Utility.setStringCheck(edtother.getText().toString())) {
            Toast.makeText(this, getResources().getString(R.string.EnterAadharother), Toast.LENGTH_LONG).show();
            return false;
        } else if (edtaadhar_no.getText().toString().length() > 1 && edtaadhar_no.getText().toString().length() != 12) {
            Toast.makeText(this, getResources().getString(R.string.EnterAadharNumber), Toast.LENGTH_LONG).show();
            return false;
        } else if (!Utility.setStringCheck(txt_Date.getText().toString())) {
            Toast.makeText(this, getResources().getString(R.string.EnterDate), Toast.LENGTH_LONG).show();
            return false;
        } else if (!Utility.setStringCheck(editFarmerPhoneNumber.getText().toString()) || editFarmerPhoneNumber.getText().toString().length() != 10 || !AppManager.getInstance().isMobileNoValid(editFarmerPhoneNumber.getText().toString())) {
            Toast.makeText(this, getResources().getString(R.string.Pleaseentervalid), Toast.LENGTH_LONG).show();
            return false;
        } else if (!Utility.setStringCheck(StateName) || StateName.equalsIgnoreCase("Select State")) {
            Toast.makeText(this, getResources().getString(R.string.PleaseSelectState), Toast.LENGTH_LONG).show();
            return false;
        } else if (!Utility.setStringCheck(DistrictName) || DistrictName.equalsIgnoreCase("Select District")) {
            Toast.makeText(this, getResources().getString(R.string.PleaseselectDistrict), Toast.LENGTH_LONG).show();
            return false;
        } else if ((!Utility.setStringCheck(SubDistrictName) || SubDistrictName.equalsIgnoreCase("Select Sub District")) && !Utility.setStringCheck(edit_othersubdistrict.getText().toString())) {
            Toast.makeText(this, getResources().getString(R.string.Pleaseselectsubdistrict), Toast.LENGTH_LONG).show();
            return false;
        } else if ((!Utility.setStringCheck(VillageName) || VillageName.equalsIgnoreCase("Select Village")) && !Utility.setStringCheck(edit_othervillageName.getText().toString())) {
            Toast.makeText(this, getResources().getString(R.string.Pleaseselectviilage), Toast.LENGTH_LONG).show();
            return false;
        }
        if (flag == 1)//Upload Button
        {
            if (imageList.length() < 1) {
                Toast.makeText(this, getResources().getString(R.string.PleaseuploadImage), Toast.LENGTH_LONG).show();
                return false;
            }
        }
        return true;
    }

    public JSONObject getjsonvalueUpload() {
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject1 = new JSONObject();
        try {
//            jsonObject.put("OTP", "");
            jsonObject.put("FarmerName", edtFarmerName.getText().toString());
            jsonObject.put("FatherName", edit_FatherName.getText().toString());
            jsonObject.put("State", StateID);
            jsonObject.put("StateName", StateName);
            jsonObject.put("District", DistrictID);
//            jsonObject.put("Block", "");
            if (edit_othersubdistrict.getText().toString().trim().length() > 0) {
                jsonObject.put("Block", edit_othersubdistrict.getText().toString());
            } else {
                jsonObject.put("Block", SubDistrictName);//same concept should be there in Sub district like village
            }
            jsonObject.put("Village", "");
            jsonObject.put("FarmerVillageID", VillageID);
            {
                if (edit_othervillageName.getText().toString() != null && edit_othervillageName.getText().toString().length() > 0) {
                    jsonObject.put("FarmVillageFound", "false");
                    jsonObject.put("Village", edit_othervillageName.getText().toString());
                } else {
                    jsonObject.put("Village", VillageName);
                    jsonObject.put("FarmVillageFound", "true");
                }
            }
//            jsonObject.put("PinCode", "");
            if (edtaadhar_no.getText().toString() != null && edtaadhar_no.getText().toString().length() > 0) {
                jsonObject.put("AdhaarNo", edtaadhar_no.getText().toString());
            } else if (edtother.getText().toString() != null && edtother.getText().toString().length() > 0) {
                jsonObject.put("AdhaarNo", edtother.getText().toString());
            } else {
                jsonObject.put("AdhaarNo", "");
            }
            jsonObject.put("PanNo", "");
            jsonObject.put("BankName", edit_BankName.getText().toString());
//            jsonObject.put("BankPassbookInfo", "");
            jsonObject.put("BranchName", edit_BranchName.getText().toString());
            jsonObject.put("MobileNo", editFarmerPhoneNumber.getText().toString());
            jsonObject.put("AccountNo", edit_AccountNumber.getText().toString());
            jsonObject.put("IFSCCode", edit_IFSCCode.getText().toString());
            jsonObject.put("AccountType", AccountType);
//            jsonObject.put("AccountOwnerType", "");
//            jsonObject.put("FarmerVillageID", "");
//            jsonObject.put("MyImageName", "");//List<ImagesName> MyImageName
//            JSONArray jsonArray = new JSONArray();
//            for (int s = 0; s < imageList.size(); s++) {
//                jsonArray.put(imageList.get(s));
//            }
            jsonObject.put("MyImageName", imageList.toString());
            JSONArray farmerServices = getFarmerServices();
            jsonObject.put("farmerServices", farmerServices);//List<FarmerServices> farmerServices
//            jsonObject.put("FarmID", "");
            jsonObject.put("UserID", AppConstant.user_id);
//            jsonObject.put("FarmVillageFound", "");
//            jsonObject.put("FarmVillageName", "");
//            jsonObject.put("StateName", "");
//            jsonObject.put("DistrictName", "");
//            jsonObject.put("DistrictID", "");
//            jsonObject.put("SubDistrictName", "");
//            jsonObject.put("POPDateFrom", "");
//            jsonObject.put("POPDateTo", "");
//            jsonObject.put("POPLiablity", "");
//            jsonObject.put("POPPlanName", "");
//            jsonObject.put("POPPlanFees", "");
//            jsonObject.put("POPvalueproduc", "");
//            jsonObject.put("POPcultivacost", "");
//            jsonObject.put("POPthresprice", "");
//            jsonObject.put("DateOfHarvestFrom", "");
//            jsonObject.put("DateOfHarvestTo", "");
//            jsonObject.put("SeedBrand", "");
//            jsonObject.put("SeedQuantity", "");
            //Herojit Add
//            jsonObject.put("MobileType", PhoneTypeValue);
            //  jsonObject.put("ReceiptNo", edit_ReceiptNo.getText().toString());
//            jsonObject.put("FarmName", edtFarmName.getText().toString());
//            jsonObject.put("Total", txt_TotalAmount.getText().toString());
//            jsonObject.put("SACCode", txtSACCode.getText().toString());
            jsonObject1.put("information", jsonObject.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return jsonObject1;
    }

    public JSONObject getjsonvaluePDF1() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Project Name", ProjectName);
            jsonObject.put("Aadhar Number", edtaadhar_no.getText().toString());
            jsonObject.put("Other", edtother.getText().toString());
//            jsonObject.put("Phone Type", PhoneTypeValue);
            jsonObject.put("Farmer Name", edtFarmerName.getText().toString());
            jsonObject.put("Father Name", edit_FatherName.getText().toString());
            jsonObject.put("Date", txt_Date.getText().toString());
            jsonObject.put("Farmer Phone Number", editFarmerPhoneNumber.getText().toString());
            jsonObject.put("State Name", StateName);
            jsonObject.put("District Name", DistrictName);
            jsonObject.put("Sub District Name", SubDistrictName);
            jsonObject.put("Other Sub District Name", edit_othersubdistrict.getText().toString());
            jsonObject.put("Village Name", VillageName);
            jsonObject.put("Other Village Name", edit_othervillageName.getText().toString());
            jsonObject.put("Sow Date", txt_SowingDate.getText().toString());
            jsonObject.put("Account Type", AccountType);
            jsonObject.put("Bank Name", edit_BankName.getText().toString());
            jsonObject.put("Branch Name", edit_BranchName.getText().toString());
            jsonObject.put("Mobile Number", editFarmerPhoneNumber.getText().toString());
            jsonObject.put("Account Number", edit_AccountNumber.getText().toString());
            jsonObject.put("IFSC Code", edit_IFSCCode.getText().toString());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return jsonObject;
    }

    public JSONObject getjsonvaluePDF2() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Description1", getResources().getString(R.string.FarmIncome));
            jsonObject.put("Description2", getResources().getString(R.string.MyFarmInfoService));
            jsonObject.put("Description3", getResources().getString(R.string.SoilTesting6));
            jsonObject.put("Description4", getResources().getString(R.string.SoilTesting14));
            jsonObject.put("Description5", getResources().getString(R.string.Soiltreatmentkit));
            jsonObject.put("Description6", getResources().getString(R.string.Seedtreatmentkit));
            jsonObject.put("Description7", getResources().getString(R.string.Weedicideskit));
            jsonObject.put("Crop2", cropName2);
            jsonObject.put("Variety2", VarietyName2);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return jsonObject;
    }

    public void setProject() {
        db.open();
        Projects = new ArrayList<>();
        ArrayList<HashMap<String, String>> hasmap = new ArrayList<>();
        hasmap = db.getDynamicTableValue("Select * from " + db.TABLE_Projectlist + " where ProjectName !='' and  ProjectName !='null' order by ProjectName");
        ArrayList<String> list = new ArrayList<>();
        HashMap<String, String> hash = new HashMap<>();
        hash.put("ProjectID", "0");
        hash.put("ProjectName", "Select Project");
        Projects.add(hash);
        list.add("Select Project");
        if (hasmap.size() > 0) {
            for (int i = 1; i <= hasmap.size(); i++) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("ProjectID", hasmap.get(i - 1).get("ProjectID"));
                list.add(hasmap.get(i - 1).get("ProjectName"));
                hashMap.put("ProjectName", hasmap.get(i - 1).get("ProjectName"));
                Projects.add(hashMap);
            }
        }
        ArrayAdapter<String> stateListAdapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, list);
        spin_project.setAdapter(stateListAdapter);
        spin_project.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position > 0 && Projects.size() > 0) {
                    ProjectID = Projects.get(position).get("ProjectID");
                    ProjectName = Projects.get(position).get("ProjectName");
                } else {
                    ProjectID = "0";
                    ProjectName = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        db.close();
    }

    public void setStateBind() {
        db.open();
        States = new ArrayList<>();
        ArrayList<HashMap<String, String>> hasmap = new ArrayList<>();
        hasmap = db.getDynamicTableValue("select ID,StateID,Upper(StateName) as StateName,DistrictID,Upper(DistrictName) as DistrictName from MstStateDistrict where StateID!='' and StateName!='' group by StateID order by StateName");
        ArrayList<String> list = new ArrayList<>();
        HashMap<String, String> hash = new HashMap<>();
        hash.put("StateID", "0");
        hash.put("StateName", "Select State");
        States.add(hash);
        list.add("Select State");
        if (hasmap.size() > 0) {
            for (int i = 1; i <= hasmap.size(); i++) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("StateID", hasmap.get(i - 1).get("StateID"));
                list.add(hasmap.get(i - 1).get("StateName"));
                hashMap.put("StateName", hasmap.get(i - 1).get("StateName"));
                States.add(hashMap);
            }
        }
        ArrayAdapter<String> stateListAdapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, list);
        spin_state.setAdapter(stateListAdapter);
        spin_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position > 0 && States.size() > 0) {
                    try {
                        //State
                        StateID = States.get(position).get("StateID");
                        StateName = States.get(position).get("StateName");
                        state_id = StateID;
//                        setCropsBind();
                        allbindCrop();
                        setDitrictBind(StateID);

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else {
                    StateID = "0";
                    StateName = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        db.close();
    }

    public void allbindCrop() {
        db.open();
        Crops = new ArrayList<>();
        ArrayList<HashMap<String, String>> hasmap = new ArrayList<>();
        String sql = "select * from crop_variety where state_id='" + StateID + "' group by cropid order by crop";
        hasmap = db.getDynamicTableValue(sql);
        ArrayList<String> list = new ArrayList<>();
        HashMap<String, String> hash = new HashMap<>();
        hash.put("cropId", "0");
        hash.put("crop", "Select Crop");
        Crops.add(hash);
        list.add("Select Crop");
        if (hasmap.size() > 0) {
            for (int i = 1; i <= hasmap.size(); i++) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("cropId", hasmap.get(i - 1).get("cropId"));
                list.add(hasmap.get(i - 1).get("crop"));
                hashMap.put("crop", hasmap.get(i - 1).get("crop"));
                Crops.add(hashMap);
            }
        }
        setCropsBind(spin_crop_2, list, "crop2");

    }

    public void setCropsBind(Spinner spincrop, ArrayList<String> list, final String flag) {
        ArrayAdapter<String> cropArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, list);
//        ArrayAdapter<String> cropArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, cropStringArray); //selected item will look like a spinner set from XML
//        cropArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spincrop.setAdapter(cropArrayAdapter);
        spincrop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    switch (flag) {
                        case "crop2":
                            if (position > 0) {
                                cropId2 = Crops.get(position).get("cropId");
                                cropName2 = Crops.get(position).get("crop");
                            } else {
                                cropId2 = "0";
                                cropName2 = "";
                            }
                            setVariety(spin_variety_2, cropId2, flag);
                            break;

                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setVariety(Spinner spin_Variety, String cropId, String flag) {
        try {
            db.open();
            Variety = new ArrayList<>();
            ArrayList<HashMap<String, String>> hasmap = new ArrayList<>();
            String sql = "select * from crop_variety where state_id=" + StateID + " and cropId=" + cropId + " group by variety order by variety";
            hasmap = db.getDynamicTableValue(sql);
            HashMap<String, String> hash = new HashMap<>();
            hash.put("VarietyID", "0");
            hash.put("VarietyName", "Select Variety");
            Variety.add(hash);
            if (hasmap.size() > 0) {
                for (int i = 1; i <= hasmap.size(); i++) {
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("VarietyID", hasmap.get(i - 1).get("_id"));
                    hashMap.put("VarietyName", hasmap.get(i - 1).get("variety"));
                    Variety.add(hashMap);
                }
            }
            db.close();
            setVarietyBind(spin_Variety, Variety, flag);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void setVarietyBind(Spinner spinVariety, final ArrayList<HashMap<String, String>> Varieties, final String flag) {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < Varieties.size(); i++) {
            list.add(Variety.get(i).get("VarietyName"));
        }
        ArrayAdapter<String> cropArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, list);
        spinVariety.setAdapter(cropArrayAdapter);
        spinVariety.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    if ("crop2".equals(flag)) {
                        if (position > 0) {
                            VarietyID2 = Varieties.get(position).get("VarietyID");
                            VarietyName2 = Varieties.get(position).get("VarietyName");
                        } else {
                            VarietyID2 = "0";
                            VarietyName2 = "";
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void setDitrictBind(String State_id) {
        db.open();
        Districts = new ArrayList<>();
        ArrayList<HashMap<String, String>> hasmap = new ArrayList<>();
        hasmap = db.getDynamicTableValue("select ID,StateID,Upper(StateName) as StateName,DistrictID,Upper(DistrictName) as DistrictName from MstStateDistrict where StateID='" + State_id + "' and DistrictID!='' and DistrictName!='' group by DistrictID order by DistrictName");
        ArrayList<String> list = new ArrayList<>();
        HashMap<String, String> hash = new HashMap<>();
        hash.put("DistrictID", "0");
        hash.put("DistrictName", "Select District");
        Districts.add(hash);
        list.add("Select District");
        if (hasmap.size() > 0) {
            for (int i = 1; i <= hasmap.size(); i++) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("DistrictID", hasmap.get(i - 1).get("DistrictID"));
                list.add(hasmap.get(i - 1).get("DistrictName"));
                hashMap.put("DistrictName", hasmap.get(i - 1).get("DistrictName"));
                Districts.add(hashMap);
            }
        }
        ArrayAdapter<String> stateListAdapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, list);
        spin_district.setAdapter(stateListAdapter);
        spin_district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position > 0 && Districts.size() > 0) {
                    DistrictID = Districts.get(position).get("DistrictID");
                    DistrictName = Districts.get(position).get("DistrictName");
                    district_id = DistrictID;
                    getAPIURL(3, DistrictID, DistrictName);
                } else {
                    DistrictID = "0";
                    DistrictName = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        db.close();
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

    public void SetSpinner(final int flag, Spinner spinner, ArrayList<HashMap<String, String>> value) {
        final ArrayList<String> getValue = new ArrayList();
        final ArrayList<String> getIDs = new ArrayList();
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
                    getValue.add("Select Sub District");
                    getIDs.add("0");
                    for (int i = 0; i < value.size(); i++) {
                        getValue.add(value.get(i).get("Sub_district"));
                        getIDs.add(value.get(i).get("Sub_district_Pos"));
                    }
                    break;
                case 4://Village
                    getValue.add("Select Village");
                    getIDs.add("0");
                    for (int i = 0; i < value.size(); i++) {
                        getValue.add(value.get(i).get("Village_Final"));
                        getIDs.add(value.get(i).get("Village_ID"));
                    }
                    break;
            }
        }
        ArrayAdapter<String> stateListAdapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, getValue);
        spinner.setAdapter(stateListAdapter);
        db.close();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
//                if (position > 0) {
                try {
                    switch (flag) {
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
                                edit_othersubdistrict.setText("");
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
                                edit_othervillageName.setText("");
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
            String mFileName = "MFI_" + CurrentDate;
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
            //insert column headings
//            insertCell(table, "Question", Element.ALIGN_LEFT, 1, bold);
//            insertCell(table, "Answer", Element.ALIGN_LEFT, 1, bold);
//            table.setHeaderRows(1);
            //insert an empty row
//            insertCell(table, "", Element.ALIGN_LEFT, 4, bold);
            //create section heading by cell merging
//            insertCell(table, "New York Orders ...", Element.ALIGN_LEFT, 4, bold);
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
            //add the PDF table to the paragraph
//            paragraph.add(table);
            //Create new Table
            //specify column widths
            float[] columnWidths1 = {1f, 4f, 4f, 2f, 2f, 2f, 2f};
            //create PDF table with the given widths
            PdfPTable table1 = new PdfPTable(columnWidths1);
            // set table width a percentage of the page width
            table1.setWidthPercentage(90f);
            //insert column headings
            insertCell(table1, "S No.", Element.ALIGN_LEFT, 1, bold);
            insertCell(table1, "Description", Element.ALIGN_LEFT, 1, bold);
            insertCell(table1, "Crop", Element.ALIGN_LEFT, 1, bold);
            insertCell(table1, "Variety", Element.ALIGN_LEFT, 1, bold);
            insertCell(table1, "Quantity (per arc)", Element.ALIGN_LEFT, 1, bold);
            insertCell(table1, "Rate (Rs.)", Element.ALIGN_LEFT, 1, bold);
            insertCell(table1, "Amount (Rs.)", Element.ALIGN_LEFT, 1, bold);
            table1.setHeaderRows(1);
            //insert an empty row
//            insertCell(table, "", Element.ALIGN_LEFT, 4, bold);
            //create section heading by cell merging
//            insertCell(table, "New York Orders ...", Element.ALIGN_LEFT, 4, bold);
            //just some random data to fill
            JSONObject jsonObject1 = getjsonvaluePDF2();
//            Crop1,SACCode1,SACCode1,Quantity1,Rate1,Amount1
            if (jsonObject1 != null && jsonObject1.length() > 0) {
                for (int i = 1; i <= 7; i++) {
                    String Description = jsonObject1.getString("Description" + i);
                    String Crop = jsonObject1.getString("Crop" + i);
                    String SACCode = jsonObject1.getString("Variety" + i);
                    String Quantity = jsonObject1.getString("Quantity" + i);
                    String Rate = jsonObject1.getString("Rate" + i);
                    String Amount = jsonObject1.getString("Amount" + i);
                    insertCell(table1, String.valueOf(i), Element.ALIGN_LEFT, 1, normal);
                    insertCell(table1, Description, Element.ALIGN_LEFT, 1, normal);
                    insertCell(table1, Crop, Element.ALIGN_LEFT, 1, normal);
                    insertCell(table1, SACCode, Element.ALIGN_LEFT, 1, normal);
                    insertCell(table1, Quantity, Element.ALIGN_LEFT, 1, normal);
                    insertCell(table1, Rate, Element.ALIGN_LEFT, 1, normal);
                    insertCell(table1, Amount, Element.ALIGN_LEFT, 1, normal);
                }
                insertCell(table1, getResources().getString(R.string.Total), Element.ALIGN_LEFT, 6, normal);
                insertCell(table1, jsonObject1.getString("Total"), Element.ALIGN_LEFT, 1, normal);
            }
            //create a paragraph
            Paragraph paragraph = new Paragraph();
            Chunk chunk0 = new Chunk(getResources().getString(R.string.RECEIPT), bold_XL);
            Phrase ph0 = new Phrase(chunk0);
            Chunk chunk1 = new Chunk("\n\n" + getResources().getString(R.string.GSTIN), normal);
            Phrase ph1 = new Phrase(chunk1);
            Chunk chunk2 = new Chunk("\n" + getResources().getString(R.string.WRMS), bold);
            Phrase ph2 = new Phrase(chunk2);
            Chunk chunk3 = new Chunk("\n" + getResources().getString(R.string.HeadOffice), normal);
            Phrase ph3 = new Phrase(chunk3);
            Chunk chunk4 = new Chunk("\n" + getResources().getString(R.string.Telphoneno) + "\n\n", normal);
            Phrase ph4 = new Phrase(chunk4);
            paragraph.setAlignment(Element.ALIGN_CENTER);
            paragraph.add(ph0);
            paragraph.add(ph1);
            paragraph.add(ph2);
            paragraph.add(ph3);
            paragraph.add(ph4);
            //add the PDF table to the paragraph
            // add one empty line
            paragraph.add(table);
            //create  paragraph
            Paragraph paragraph1 = new Paragraph();
            Chunk chunk5 = new Chunk("\n\n" + getResources().getString(R.string.DisclaimerPleasenote), normal);
            Phrase ph5 = new Phrase(chunk5);
            paragraph1.add(table1);
            paragraph1.add(ph5);
            // add the paragraph to the document
            doc.add(paragraph);
            doc.add(new Paragraph("\n\n"));
            doc.add(paragraph1);
            Toast.makeText(this, getResources().getString(R.string.PDFFilegenerated), Toast.LENGTH_SHORT).show();

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
                    sms = getResources().getString(R.string.LoadingSubDistricts);
                    break;
                case 4://Village
                    sms = getResources().getString(R.string.LoadingVillages);
                    break;
            }
            progressDialog = new TransparentProgressDialog(
                    FarmRegistrationNew.this, sms);
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
                return AppManager.getInstance().RemoveStringUnwanted(response);

            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPreExecute();
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
                        SetSpinner(flag, spin_subdistrict, SubDistricts);
                        //Village
                        Villages = null;
                        VillageName = null;
                        VillageID = null;
                        SetSpinner(flag, spin_villageName, Villages);
                        break;
                    case 4://Village
                        Villages = null;
                        VillageName = null;
                        VillageID = null;
                        SetSpinner(flag, spin_villageName, Villages);
                        break;
                }
//                Toast.makeText(FarmRegistrationNew.this, getResources().getString(R.string.Nodataavailable), Toast.LENGTH_LONG).show();
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
                            SetSpinner(flag, spin_subdistrict, SubDistricts);
                            break;
                        case 4://Village
                            JSONArray jsonArray_Villages = new JSONArray(result.toString());
                            Villages = getStateDistrictValues(flag, jsonArray_Villages.toString());
                            SetSpinner(flag, spin_villageName, Villages);
                            break;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(FarmRegistrationNew.this, getResources().getString(R.string.Couldnotconnect), Toast.LENGTH_LONG).show();
                }
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
                    sms = getResources().getString(R.string.LoadingProject);
                    break;
                case "StateDistrict"://District
                    sms = getResources().getString(R.string.Loadingstate);
                    break;
                case "Crops"://District
                    sms = getResources().getString(R.string.LoadingCrops);
                    break;

            }
            progressDialog = new TransparentProgressDialog(FarmRegistrationNew.this, sms);
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
                            Toast.makeText(FarmRegistrationNew.this, getResources().getString(R.string.FormattingError), Toast.LENGTH_LONG).show();
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
                                Toast.makeText(FarmRegistrationNew.this, getResources().getString(R.string.ServerError), Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(FarmRegistrationNew.this, getResources().getString(R.string.FormattingError), Toast.LENGTH_LONG).show();
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
                            Toast.makeText(FarmRegistrationNew.this, getResources().getString(R.string.FormattingError), Toast.LENGTH_LONG).show();
                        }
                        SqliteDB1.setTransactionSuccessful();
                        SqliteDB1.endTransaction();
//                        setCropsBind();
                        allbindCrop();
                        break;
                }

            } else
                Toast.makeText(FarmRegistrationNew.this, getResources().getString(R.string.Nodataavailable), Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        }
    }
//    public void setPhoneType() {
//        PhoneType = new ArrayList<>();
//        PhoneType.add("Select Phone Type");
//        PhoneType.add("Keypad Phones");
//        PhoneType.add("Android Phones");
//        PhoneType.add("Apple iPhones");
//        PhoneType.add("Others");
//        ArrayAdapter<String> phonetypes = new ArrayAdapter<String>(this, R.layout.spinner_layout, PhoneType);
//        spin_MobileType.setAdapter(phonetypes);
//        spin_MobileType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
//                if (position > 0 && PhoneType.size() > 0) {
//                    PhoneTypeValue = PhoneType.get(position).toString();
//                } else {
//                    PhoneTypeValue = "";
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
//    }

    private class setFarmRegistrationSave extends AsyncTask<Void, Void, String> {

        String result = null;
        String json;
        String flag = null;
        TransparentProgressDialog progressDialog;

        public setFarmRegistrationSave(String flags) {
            flag = flags;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new TransparentProgressDialog(
                    FarmRegistrationNew.this, getResources().getString(
                    R.string.Dataisloading));
            progressDialog.setCancelable(false);

        }

        @Override
        protected String doInBackground(Void... params) {
            String response = null;
            String URL = AppManager.getInstance().UploadFarmRegistrationURL;//Herojit Comment
            JSONObject object = getjsonvalueUpload();
            String jsonvalue = object.toString();
            jsonvalue = jsonvalue.replace("\\\\\\\\\\\\\\", "\\");
            jsonvalue = jsonvalue.replace("\\\\\\", "\\");
            jsonvalue = jsonvalue.replace("\\\"[", "[");
            jsonvalue = jsonvalue.replace("]\\\"", "]");
            Log.v("vishaltripathiRequest", jsonvalue + "");
            response = AppManager.getInstance().httpRequestPutMethod(URL, jsonvalue);
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPreExecute();
            progressDialog.dismiss();
            Log.v("vishaltripathi", response + "");
            if (response != null && response.contains("SaveFarmerBasicInformationResult")) {
                try {
                    response = "{" + response + "}";
                    JSONObject obj = new JSONObject(response);
                    if (obj.getString("SaveFarmerBasicInformationResult").contains("Success")) {
//                        btnGeoTag.setVisibility(View.VISIBLE);
//                        submit.setVisibility(View.GONE);
                        String suc = obj.getString("SaveFarmerBasicInformationResult");
                        String farmer_id = null;
                        if (suc != null && suc.length() > 4) {
                            String[] split = suc.split(":");
                            if (split.length > 1) {
                                farmer_id = split[1];
                            }
                        }
                        Toast.makeText(FarmRegistrationNew.this, getResources().getString(R.string.SubmittedSuccessfully), Toast.LENGTH_LONG).show();
                        if (flag.equalsIgnoreCase("Submit")) {
                            finish();

                        } else if (flag.equalsIgnoreCase("SubmitGeoTag")) {
                            String Aadhar = null;
                            if (checkAadharOther.equalsIgnoreCase("Aadhar")) {
                                Aadhar = edtaadhar_no.getText().toString();
                                checkAadharOther = "Aadhar";
                            } else if (checkAadharOther.equalsIgnoreCase("Other")) {
                                Aadhar = edtother.getText().toString();
                                checkAadharOther = "Other";
                            }
                            AppConstant.FarmRegistration_AadharNo = Aadhar;
                            AppConstant.FarmRegistration_ProjectID = ProjectID;
                            Intent intent = new Intent(FarmRegistrationNew.this, AddFarmOnMap_New.class);
                            intent.putExtra("calling-activity", AppConstant.HomeActivity);
                            intent.putExtra("lat", String.valueOf(LatLonCellID.currentLat));
                            intent.putExtra("log", String.valueOf(LatLonCellID.currentLon));
                            intent.putExtra("balance", balanceAmount);
                            intent.putExtra("farmerId", farmer_id);
                            intent.putExtra("project_id", ProjectID);
                            intent.putExtra("state_id", state_id);
                            intent.putExtra("district_id", district_id);

                            //intent.putExtra("hashMapValue", hashMap);
                            startActivity(intent);
                        } else if (flag.equalsIgnoreCase("Product")) {
                            String Aadhar = null;
                            if (checkAadharOther.equalsIgnoreCase("Aadhar")) {
                                Aadhar = edtaadhar_no.getText().toString();
                                checkAadharOther = "Aadhar";
                            } else if (checkAadharOther.equalsIgnoreCase("Other")) {
                                Aadhar = edtother.getText().toString();
                                checkAadharOther = "Other";
                            }
                            AppConstant.FarmRegistration_AadharNo = Aadhar;
                            AppConstant.FarmRegistration_ProjectID = ProjectID;
                            if (farmer_id != null && !farmer_id.equalsIgnoreCase("null")) {
                                Intent in = new Intent(getApplicationContext(), NewProductActivity.class);
                                in.putExtra("farmerId", farmer_id);
                                in.putExtra("state_id", state_id);
                                in.putExtra("district_id", district_id);
                                startActivity(in);
                            } else {
                                Toast.makeText(getApplicationContext(), "Farmer Id not found", Toast.LENGTH_SHORT).show();
                            }
                        }


                    } else {
                        Toast.makeText(FarmRegistrationNew.this, getResources().getString(R.string.FormattingError), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception ex) {
                    Toast.makeText(FarmRegistrationNew.this, getResources().getString(R.string.FormattingError), Toast.LENGTH_LONG).show();
                    ex.printStackTrace();
                }
            } else {
                Toast.makeText(FarmRegistrationNew.this, getResources().getString(R.string.ServerError), Toast.LENGTH_LONG).show();
            }
        }

    }

    public JSONArray getFarmerServices() {
        JSONArray farmerServices = new JSONArray();
        try {
            JSONObject cropInfoJsonObject2 = new JSONObject();
//            cropInfoJsonObject2.put("ServiceType", getResources().getString(R.string.MyFarmInfoService));
            cropInfoJsonObject2.put("ServiceType", "My farm info service");
            cropInfoJsonObject2.put("CropName", cropName2);
            cropInfoJsonObject2.put("Variety", VarietyName2);
            //  cropInfoJsonObject2.put("Area", areaCrop.getText().toString());
            cropInfoJsonObject2.put("ProjectID", ProjectID);
            cropInfoJsonObject2.put("SowingDate", txt_SowingDate.getText().toString());
            cropInfoJsonObject2.put("RegistrationDate", txt_Date.getText().toString());
            farmerServices.put(cropInfoJsonObject2);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return farmerServices;
    }

    private void selectImage() {
        final CharSequence[] items = {getDynamicLanguageValue(getApplicationContext(), "TakePhoto", R.string.TakePhoto),
                getDynamicLanguageValue(getApplicationContext(), "Selectfromgallery", R.string.Selectfromgallery),
                getDynamicLanguageValue(getApplicationContext(), "Cancel", R.string.Cancel)};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //  builder.setTitle("Upload Document!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].toString().equalsIgnoreCase(getDynamicLanguageValue(getApplicationContext(), "TakePhoto", R.string.TakePhoto))) {
                    userChoosenTask = "Take Photo";
                    boolean resultCam = Utility.checkPermissionCamera(FarmRegistrationNew.this);
                    if (resultCam) {
                        if (CameraUtils.checkPermissions(getApplicationContext())) {
                            switch (Imageselectflag) {
                                case 1:
                                    cameraIntent1();
                                    break;
                                case 2:
                                    cameraIntent2();
                                    break;
                                case 3:
                                    cameraIntent3();
                                    break;
                                case 4:
                                    cameraIntent4();
                                    break;
                            }
                        }
                    }

                } else if (items[item].toString().equalsIgnoreCase(getDynamicLanguageValue(getApplicationContext(), "Selectfromgallery", R.string.Selectfromgallery))) {
                    userChoosenTask = "Select From Gallery";
                    boolean resultCam = Utility.checkPermissionGallery(FarmRegistrationNew.this);
                    if (resultCam) {
                        galleryIntent();
                    }
                } else if (items[item].toString().equalsIgnoreCase(getDynamicLanguageValue(getApplicationContext(), "Cancel", R.string.Cancel))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        switch (Imageselectflag) {
            case 1:
                startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE_START1);
                break;
            case 2:
                startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE_START2);
                break;
            case 3:
                startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE_START3);
                break;
            case 4:
                startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE_START4);
                break;

        }

    }

    private void cameraIntent1() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = CameraUtils.getOutputMediaFile(MEDIA_TYPE_IMAGE);
        if (file != null) {
            imageStoragePath1 = file.getAbsolutePath();
        }
        Uri fileUri = CameraUtils.getOutputMediaFileUri(getApplicationContext(), file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        // start the image capture Intent
        startActivityForResult(intent, REQUEST_CAMERA_START1);
    }

    private void cameraIntent2() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = CameraUtils.getOutputMediaFile(MEDIA_TYPE_IMAGE);
        if (file != null) {
            imageStoragePath2 = file.getAbsolutePath();
        }
        Uri fileUri = CameraUtils.getOutputMediaFileUri(getApplicationContext(), file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        // start the image capture Intent
        startActivityForResult(intent, REQUEST_CAMERA_START2);
    }

    private void cameraIntent3() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = CameraUtils.getOutputMediaFile(MEDIA_TYPE_IMAGE);
        if (file != null) {
            imageStoragePath3 = file.getAbsolutePath();
        }
        Uri fileUri = CameraUtils.getOutputMediaFileUri(getApplicationContext(), file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        // start the image capture Intent
        startActivityForResult(intent, REQUEST_CAMERA_START3);
    }

    private void cameraIntent4() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = CameraUtils.getOutputMediaFile(MEDIA_TYPE_IMAGE);
        if (file != null) {
            imageStoragePath4 = file.getAbsolutePath();
        }
        Uri fileUri = CameraUtils.getOutputMediaFileUri(getApplicationContext(), file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        // start the image capture Intent
        startActivityForResult(intent, REQUEST_CAMERA_START4);
    }

    private void uploadImage(final int flag) {
        try {
            final TransparentProgressDialog pDialog;
            pDialog = new TransparentProgressDialog(FarmRegistrationNew.this,
                    getResources().getString(R.string.Dataisloading));
            pDialog.setCancelable(false);
            pDialog.show();
            JSONObject jsonObject = null;
            String usi = AppConstant.user_id;
            Double lat = LatLonCellID.lat;
            Double lon = LatLonCellID.lon;
            Log.v("imageLat_long", lat + "," + lon);
            try {
                jsonObject = new JSONObject();
                switch (flag) {
                    case 1:
                        jsonObject.putOpt("ImageString", imageString1);
                        break;
                    case 2:
                        jsonObject.putOpt("ImageString", imageString2);
                        break;
                    case 3:
                        jsonObject.putOpt("ImageString", imageString3);
                        break;
                    case 4:
                        jsonObject.putOpt("ImageString", imageString4);
                        break;
                }
                jsonObject.putOpt("UserID", usi);
                // jsonObject.putOpt("Lat_Lng", lat+","+lon);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            CustomJSONObjectRequest jsonObjectRequest = new CustomJSONObjectRequest(Request.Method.PUT, AppManager.getInstance().uploadImageURL_SCHEDULER, jsonObject, new com.android.volley.Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    pDialog.cancel();
                    Log.i("Response upload image", "" + response.toString());
                    //Herojit Comment
                    String res = response.toString();
                    res = res.replace("\":\"[\\\"", "\":\"");
                    res = res.replace("\\\"]\"", "\"");
                    res = res.replace("\\", "");
                    try {
                        JSONObject jsonObject = new JSONObject(res);
                        if (jsonObject.get("uploadBase64Image_SchedulerResult").toString().length() > 0) {
                            // Image may be not from secufarm folder
                            Double lat = LatLonCellID.lat;
                            Double lon = LatLonCellID.lon;
                            imageList = Utility.addImageName(jsonObject.get("uploadBase64Image_SchedulerResult").toString(), imageList, flag + 3, String.valueOf(lat), String.valueOf(lon));
//                            addImageName(jsonObject.get("uploadBase64Image_SchedulerResult").toString(), flag);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.SubmittedSuccessfully), Toast.LENGTH_LONG).show();
//                getResponse(response, co);
                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pDialog.cancel();
                    Log.v("Response vishal coupon", "" + error.toString());
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    40000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            AppController.getInstance().addToRequestQueue(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//    public void addImageName(String Response, int flag) {
//
//        JSONObject jsonObject = new JSONObject();
//        String ImageName = "", ImageType = "";
//        switch (flag) {
//            case 1://ReceiptImage
////                ImageName = "ReceiptImageName";
//                ImageType = "ReceiptImage";
//                break;
//            case 2://FarmerImage
////                ImageName = "FarmerImageName";
//                ImageType = "FarmerImage";
//                break;
//            case 3://AadharImage
////                ImageName = "AadharImageName";
//                ImageType = "AadharImage";
//                break;
//            case 4://BankPaasbookImage
////                ImageName = "BankPaasbookImageName";
//                ImageType = "BankPaasbookImage";
//                break;
//        }
//        try {
//            jsonObject.put("Type", ImageType);
//            jsonObject.put("ImageName", Response);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        ArrayList<String> list = new ArrayList<>();
//        if (imageList != null && imageList.size() > 0) {
//            for (int i = 0; i < imageList.size(); i++) {
//                list.add(imageList.get(i));
//            }
//        }
//        list.add(jsonObject.toString());
//        imageList = new ArrayList<>();
//        imageList = list;
//    }

    //Herojit Add
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CAMERA_START1) {
                onCaptureImageResult1();
            } else if (requestCode == SELECT_FILE_START1) {
                onSelectFromGalleryResult1(data);
            } else if (requestCode == REQUEST_CAMERA_START2) {
                onCaptureImageResult2();
            } else if (requestCode == SELECT_FILE_START2) {
                onSelectFromGalleryResult2(data);
            } else if (requestCode == REQUEST_CAMERA_START3) {
                onCaptureImageResult3();
            } else if (requestCode == SELECT_FILE_START3) {
                onSelectFromGalleryResult3(data);
            } else if (requestCode == REQUEST_CAMERA_START4) {
                onCaptureImageResult4();
            } else if (requestCode == SELECT_FILE_START4) {
                onSelectFromGalleryResult4(data);
            }
        }
    }

    private void onCaptureImageResult1() {
        try {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            Bitmap bitmap = BitmapFactory.decodeFile(imageStoragePath1, options);
            choose_image1.setImageBitmap(bitmap);
            imageString1 = imageToString(bitmap);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void onCaptureImageResult2() {
        try {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            Bitmap bitmap = BitmapFactory.decodeFile(imageStoragePath2, options);
            choose_image2.setImageBitmap(bitmap);
            imageString2 = imageToString(bitmap);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void onCaptureImageResult3() {
        try {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            Bitmap bitmap = BitmapFactory.decodeFile(imageStoragePath3, options);
            choose_image3.setImageBitmap(bitmap);
            imageString3 = imageToString(bitmap);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void onCaptureImageResult4() {
        try {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            Bitmap bitmap = BitmapFactory.decodeFile(imageStoragePath4, options);
            choose_image4.setImageBitmap(bitmap);
            imageString4 = imageToString(bitmap);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult1(Intent data) {
        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        choose_image1.setImageBitmap(bm);
        imageString1 = imageToString(bm);
//        String filePath = data.getData().getPath();
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult2(Intent data) {
        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        choose_image2.setImageBitmap(bm);
        imageString2 = imageToString(bm);
//        String filePath = data.getData().getPath();
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult3(Intent data) {
        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        choose_image3.setImageBitmap(bm);
        imageString3 = imageToString(bm);
//        String filePath = data.getData().getPath();
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult4(Intent data) {
        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        choose_image4.setImageBitmap(bm);
        imageString4 = imageToString(bm);
//        String filePath = data.getData().getPath();
    }

    public String imageToString(Bitmap bmp) {
        String encodedImage = "null";
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 80, baos);
            byte[] imageBytes = baos.toByteArray();
            encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        } catch (IllegalAccessError e) {
            e.printStackTrace();
        }
        return encodedImage;
    }

    public void selectImagePopup() {
        //final Dialog dialog = new Dialog(OtherUserProfile.this,android.R.style.Theme_Translucent_NoTitleBar);
        final Dialog dialog = new Dialog(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.dimAmount = 0.7f;
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setAttributes(wlp);
        // Include dialog.xml file
        dialog.setContentView(R.layout.popup_imagefarmerregistration);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        Button btn_cross = (Button) dialog.findViewById(R.id.btn_cross);
        TextView heading_text = (TextView) dialog.findViewById(R.id.heading_text);
        TextView txt_ReceiptImage = (TextView) dialog.findViewById(R.id.txt_ReceiptImage);
        TextView txt_FarmerImage = (TextView) dialog.findViewById(R.id.txt_FarmerImage);
        TextView txt_AadharImage = (TextView) dialog.findViewById(R.id.txt_AadharImage);
        TextView txt_BankPaasbookImage = (TextView) dialog.findViewById(R.id.txt_BankPaasbookImage);
        choose_image1 = (ImageView) dialog.findViewById(R.id.choose_image1);
        choose_image2 = (ImageView) dialog.findViewById(R.id.choose_image2);
        choose_image3 = (ImageView) dialog.findViewById(R.id.choose_image3);
        choose_image4 = (ImageView) dialog.findViewById(R.id.choose_image4);
        imageupload1 = (Button) dialog.findViewById(R.id.imageupload1);
        imageupload2 = (Button) dialog.findViewById(R.id.imageupload2);
        imageupload3 = (Button) dialog.findViewById(R.id.imageupload3);
        imageupload4 = (Button) dialog.findViewById(R.id.imageupload4);
        UtilFonts.UtilFontsInitialize(this);
        heading_text.setTypeface(UtilFonts.FS_Ultra);
        txt_ReceiptImage.setTypeface(UtilFonts.KT_Medium);
        txt_FarmerImage.setTypeface(UtilFonts.KT_Medium);
        txt_AadharImage.setTypeface(UtilFonts.KT_Medium);
        txt_BankPaasbookImage.setTypeface(UtilFonts.KT_Medium);
        imageupload1.setTypeface(UtilFonts.KT_Bold);
        imageupload2.setTypeface(UtilFonts.KT_Bold);
        imageupload4.setTypeface(UtilFonts.KT_Bold);
        imageupload4.setTypeface(UtilFonts.KT_Bold);
        btn_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        choose_image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Imageselectflag = 1;
                imageString1 = null;
                choose_image1.setImageBitmap(null);
                selectImage();
            }
        });
        choose_image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Imageselectflag = 2;
                imageString2 = null;
                choose_image2.setImageBitmap(null);
                selectImage();
            }
        });
        choose_image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Imageselectflag = 3;
                imageString3 = null;
                choose_image3.setImageBitmap(null);
                selectImage();
            }
        });
        choose_image4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Imageselectflag = 4;
                imageString4 = null;
                choose_image4.setImageBitmap(null);
                selectImage();
            }
        });
        imageupload1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageString1 != null && imageString1.length() > 10) {
                    uploadImage(1);
                } else {
                    Toast.makeText(FarmRegistrationNew.this, getResources().getString(R.string.Setimageupload), Toast.LENGTH_LONG).show();
                }
            }
        });
        imageupload2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageString2 != null && imageString2.length() > 10) {
                    uploadImage(2);
                } else {
                    Toast.makeText(FarmRegistrationNew.this, getResources().getString(R.string.Setimageupload), Toast.LENGTH_LONG).show();
                }
            }
        });
        imageupload3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageString3 != null && imageString3.length() > 10) {
                    uploadImage(3);
                } else {
                    Toast.makeText(FarmRegistrationNew.this, getResources().getString(R.string.Setimageupload), Toast.LENGTH_LONG).show();
                }
            }
        });
        imageupload4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageString4 != null && imageString4.length() > 10) {
                    uploadImage(4);
                } else {
                    Toast.makeText(FarmRegistrationNew.this, getResources().getString(R.string.Setimageupload), Toast.LENGTH_LONG).show();
                }
            }
        });
        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (db != null) {
            db = new DBAdapter(this);
        }
        Log.d("onResume Method", "onResume Method called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (db != null) {
            db = new DBAdapter(this);
        }
        Log.d("OnPause Method", "OnPause Method called");
        setScreenTracking(this, db, SN_FarmRegistration, UID);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (db != null) {
            db = new DBAdapter(this);
        }
        Log.d("onStop Method", "onStop Method called");
        setScreenTracking(this, db, SN_FarmRegistration, UID);
    }

    @Override
    public void onBackPressed() {
        // code here to show dialog
        super.onBackPressed();  // optional depending on your needs
        Log.d("onBackPressed Method", "onBackPressed Method called");
//        Intent in = new Intent(FarmRegistrationNew.this, AdminDashboard_New.class);
        Intent in = new Intent(FarmRegistrationNew.this, MainProfileActivity.class);
        startActivity(in);
        finish();
    }


    private void setIdDefine() {
        txt_FarmRegistration = (TextView) findViewById(R.id.txt_FarmRegistration);
        txt_Project = (TextView) findViewById(R.id.txt_Project);
        spin_project = (Spinner) findViewById(R.id.spin_project);
        rb_aadhar = (RadioButton) findViewById(R.id.rb_aadhar);
        rb_other = (RadioButton) findViewById(R.id.rb_other);
        txt_AadharNo = (TextView) findViewById(R.id.txt_AadharNo);
        edtaadhar_no = (EditText) findViewById(R.id.edtaadhar_no);
        txt_Other = (TextView) findViewById(R.id.txt_Other);
        edtother = (EditText) findViewById(R.id.edtother);
        txt_FarmerName = (TextView) findViewById(R.id.txt_FarmerName);
        edtFarmerName = (EditText) findViewById(R.id.edtFarmerName);
        txt_FatherName = (TextView) findViewById(R.id.txt_FatherName);
        edit_FatherName = (EditText) findViewById(R.id.edit_FatherName);
        txt_Dates = (TextView) findViewById(R.id.txt_Dates);
        txt_Date = (TextView) findViewById(R.id.txt_Date);
        txt_UploadImage = (TextView) findViewById(R.id.txt_UploadImage);
        txt_FarmerPhoneNumber = (TextView) findViewById(R.id.txt_FarmerPhoneNumber);
        editFarmerPhoneNumber = (EditText) findViewById(R.id.editFarmerPhoneNumber);
        txt_StateName = (TextView) findViewById(R.id.txt_StateName);
        spin_state = (Spinner) findViewById(R.id.spin_state);
        txt_DistrictName = (TextView) findViewById(R.id.txt_DistrictName);
        spin_district = (Spinner) findViewById(R.id.spin_district);
        txt_SubDistrictName = (TextView) findViewById(R.id.txt_SubDistrictName);
        spin_subdistrict = (Spinner) findViewById(R.id.spin_subdistrict);
        txt_OtherDistrict = (TextView) findViewById(R.id.txt_OtherDistrict);
        edit_othersubdistrict = (EditText) findViewById(R.id.edit_othersubdistrict);
        txt_VillageName = (TextView) findViewById(R.id.txt_VillageName);
        spin_villageName = (Spinner) findViewById(R.id.spin_villageName);
        txt_OtherVillageName = (TextView) findViewById(R.id.txt_OtherVillageName);
        edit_othervillageName = (EditText) findViewById(R.id.edit_othervillageName);
        txt_SowingDates = (TextView) findViewById(R.id.txt_SowingDates);
        txt_SowingDate = (TextView) findViewById(R.id.txt_SowingDate);
        txt_AccountType = (TextView) findViewById(R.id.txt_AccountType);
        radio_self = (RadioButton) findViewById(R.id.radio_self);
        radio_other = (RadioButton) findViewById(R.id.radio_other);
        txt_BankName = (TextView) findViewById(R.id.txt_BankName);
        edit_BankName = (EditText) findViewById(R.id.edit_BankName);
        txt_BranchName = (TextView) findViewById(R.id.txt_BranchName);
        edit_BranchName = (EditText) findViewById(R.id.edit_BranchName);
        txt_AccountNumber = (TextView) findViewById(R.id.txt_AccountNumber);
        edit_AccountNumber = (EditText) findViewById(R.id.edit_AccountNumber);
        txt_IFSCCode = (TextView) findViewById(R.id.txt_IFSCCode);
        edit_IFSCCode = (EditText) findViewById(R.id.edit_IFSCCode);
        txt_Crops = (TextView) findViewById(R.id.txt_Crops);
        txt_Varieties = (TextView) findViewById(R.id.txt_Varieties);
        spin_crop_2 = (Spinner) findViewById(R.id.spin_crop_2);
        spin_variety_2 = (Spinner) findViewById(R.id.spin_variety_2);
        printimageview = (ImageView) findViewById(R.id.printimageview);
        imge_state_refresh = (ImageView) findViewById(R.id.imge_state_refresh);
        project_refresh = (ImageView) findViewById(R.id.project_refresh);
        imge_crop_refresh = (ImageView) findViewById(R.id.imge_crop_refresh);
        btn_imageuploadnext = (ImageView) findViewById(R.id.btn_imageuploadnext);
        tblrow_aadhar = (TableRow) findViewById(R.id.tblrow_aadhar);
        tblrow_other = (TableRow) findViewById(R.id.tblrow_other);
        ll_OtherDistrict = (TableRow) findViewById(R.id.ll_OtherDistrict);
        ll_OtherVillageName = (TableRow) findViewById(R.id.ll_OtherVillageName);
        submit = (Button) findViewById(R.id.submit);
        btnGeoTag = (Button) findViewById(R.id.btnGeoTag);
        product_btn = (Button) findViewById(R.id.product_btn);
        radiogroup_aadhar = (RadioGroup) findViewById(R.id.radiogroup_aadhar);
        radiogroup_aacounttype = (RadioGroup) findViewById(R.id.radiogroup_aacounttype);
        setFonts();
    }

    public void setFonts() {
        UtilFonts.UtilFontsInitialize(this);
        txt_FarmRegistration.setTypeface(UtilFonts.FS_Ultra);
        txt_Project.setTypeface(UtilFonts.KT_Medium);
//        spin_project.setTypeface(UtilFonts.KT_Regular);
        rb_aadhar.setTypeface(UtilFonts.KT_Medium);
        rb_other.setTypeface(UtilFonts.KT_Medium);
        txt_AadharNo.setTypeface(UtilFonts.KT_Medium);
        edtaadhar_no.setTypeface(UtilFonts.KT_Regular);
        txt_Other.setTypeface(UtilFonts.KT_Medium);
        edtother.setTypeface(UtilFonts.KT_Regular);
        txt_FarmerName.setTypeface(UtilFonts.KT_Medium);
        edtFarmerName.setTypeface(UtilFonts.KT_Regular);
        txt_FatherName.setTypeface(UtilFonts.KT_Medium);
        edit_FatherName.setTypeface(UtilFonts.KT_Regular);
        txt_Dates.setTypeface(UtilFonts.KT_Medium);
        txt_Date.setTypeface(UtilFonts.KT_Regular);
        txt_UploadImage.setTypeface(UtilFonts.KT_Medium);
        txt_FarmerPhoneNumber.setTypeface(UtilFonts.KT_Medium);
        editFarmerPhoneNumber.setTypeface(UtilFonts.KT_Regular);
        txt_StateName.setTypeface(UtilFonts.KT_Medium);
//        spin_state = (Spinner) findViewById(R.id.spin_state);
        txt_DistrictName.setTypeface(UtilFonts.KT_Medium);
//        spin_district = (Spinner) findViewById(R.id.spin_district);
        txt_SubDistrictName.setTypeface(UtilFonts.KT_Medium);
//        spin_subdistrict = (Spinner) findViewById(R.id.spin_subdistrict);
        txt_OtherDistrict.setTypeface(UtilFonts.KT_Medium);
        edit_othersubdistrict.setTypeface(UtilFonts.KT_Regular);
        txt_VillageName.setTypeface(UtilFonts.KT_Medium);
//        spin_villageName = (Spinner) findViewById(R.id.spin_villageName);
        txt_OtherVillageName.setTypeface(UtilFonts.KT_Medium);
        edit_othervillageName.setTypeface(UtilFonts.KT_Regular);
        txt_SowingDates.setTypeface(UtilFonts.KT_Medium);
        txt_SowingDate.setTypeface(UtilFonts.KT_Regular);
        txt_AccountType.setTypeface(UtilFonts.KT_Medium);
        radio_self.setTypeface(UtilFonts.KT_Regular);
        radio_other.setTypeface(UtilFonts.KT_Regular);
        txt_BankName.setTypeface(UtilFonts.KT_Medium);
        edit_BankName.setTypeface(UtilFonts.KT_Regular);
        txt_BranchName.setTypeface(UtilFonts.KT_Medium);
        edit_BranchName.setTypeface(UtilFonts.KT_Regular);
        txt_AccountNumber.setTypeface(UtilFonts.KT_Medium);
        edit_AccountNumber.setTypeface(UtilFonts.KT_Regular);
        txt_IFSCCode.setTypeface(UtilFonts.KT_Medium);
        edit_IFSCCode.setTypeface(UtilFonts.KT_Regular);
        txt_Crops.setTypeface(UtilFonts.KT_Regular);
        txt_Varieties.setTypeface(UtilFonts.KT_Regular);
        product_btn.setTypeface(UtilFonts.KT_Bold);
        submit.setTypeface(UtilFonts.KT_Bold);
        btnGeoTag.setTypeface(UtilFonts.KT_Bold);


    }


    private void getFarmerDetailsMethod(final String fId) {
        // display a progress dialog
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Please Wait..."); // set message
        progressDialog.show(); // show progress dialog
        FarmerListRequest request = new FarmerListRequest();
        // request.setProjectID(pId);
        request.setFarmerPersonelID(Integer.valueOf(fId));
        apiService.getFarmerDetails(request).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResponseObserver<Response<FarmerDetailsResponse>>(compositeDisposable) {
                    @Override
                    public void onNetworkError(Throwable e) {
                        progressDialog.cancel();
                        Log.v("wvqevqevqe", e.getMessage() + " " + e.toString());


                    }

                    @Override
                    public void onServerError(Throwable e, int code) {
                        progressDialog.cancel();


                    }

                    @Override
                    public void onNext(Response<FarmerDetailsResponse> response) {
                        progressDialog.cancel();
                        responsesData = response.body();
                        if (responsesData != null && responsesData.getFarmerpersonalData() != null && responsesData.getFarmerpersonalData().size() > 0) {
                            edtFarmerName.setText(responsesData.getFarmerpersonalData().get(0).getFarmerName());
                            edit_FatherName.setText(responsesData.getFarmerpersonalData().get(0).getFatherName());
                            editFarmerPhoneNumber.setText(responsesData.getFarmerpersonalData().get(0).getFarmerPhno());
                            edit_AccountNumber.setText(responsesData.getFarmerpersonalData().get(0).getAccountNo());
                            balanceAmount = responsesData.getFarmerpersonalData().get(0).getBalanceAmount();
                            state_id = responsesData.getFarmerpersonalData().get(0).getStateID();
                            district_id = responsesData.getFarmerpersonalData().get(0).getDistrictID();
                        }
                    }
                });

    }

}
