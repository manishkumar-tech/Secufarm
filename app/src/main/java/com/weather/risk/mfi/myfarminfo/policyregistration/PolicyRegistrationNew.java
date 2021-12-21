package com.weather.risk.mfi.myfarminfo.policyregistration;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.customcamera.CameraSurfaceView;
import com.weather.risk.mfi.myfarminfo.database.DBAdapter;
import com.weather.risk.mfi.myfarminfo.databinding.PolicyregistrationnewBinding;
import com.weather.risk.mfi.myfarminfo.home.AppController;
import com.weather.risk.mfi.myfarminfo.home.EditFarmActivity;
import com.weather.risk.mfi.myfarminfo.mapfragments.LatLonCellID;
import com.weather.risk.mfi.myfarminfo.marketplace.CheckoutActivityNew;
import com.weather.risk.mfi.myfarminfo.retofitservices.ApiService;
import com.weather.risk.mfi.myfarminfo.retofitservices.ResponseObserver;
import com.weather.risk.mfi.myfarminfo.retrofitmodelnew.BankNameResponse;
import com.weather.risk.mfi.myfarminfo.retrofitmodelnew.CategoryDetailResponse;
import com.weather.risk.mfi.myfarminfo.retrofitmodelnew.RequestCategoryBean;
import com.weather.risk.mfi.myfarminfo.utils.AppConstant;
import com.weather.risk.mfi.myfarminfo.utils.AppManager;
import com.weather.risk.mfi.myfarminfo.utils.TransparentProgressDialog;
import com.weather.risk.mfi.myfarminfo.utils.UtilFonts;
import com.weather.risk.mfi.myfarminfo.utils.Utility;
import com.weather.risk.mfi.myfarminfo.volley_class.CustomJSONObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

import static com.weather.risk.mfi.myfarminfo.logging.LocalFileWriter.SaveLocalFile;
import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.SelectFarmDetails_policy;
import static com.weather.risk.mfi.myfarminfo.policyregistration.PolicyList.SelectFarmerDetails_policy;
import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.CustomCamera_ImageValue;
import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.CustomCamera_bitmap;
import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.SN_PolicyRegistration;
import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.getCheckCameraScreenOnOff;
import static com.weather.risk.mfi.myfarminfo.utils.AppManager.getCreateImageName;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.ConvertDateFormat;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.GetRevisedFarmArea;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.checkmobileno;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getAPIimeResponseinSecond;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getDynamicLanguageToast;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getDynamicLanguageValue;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getTotalNoimageperDay;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.isValidAadharNumber;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setDynamicLanguage;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setFontsStyle;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setFontsStyleTxt;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setScreenTracking;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setStringCheck;

public class PolicyRegistrationNew extends AppCompatActivity {

    PolicyregistrationnewBinding binding;
    DBAdapter db;
    SharedPreferences prefs;
    String userChoosenTask, imageString1, ImageName1 = "",
            imageString2, ImageName2 = "", UID = "";
    private int SELECT_FILE_START1 = 1, SELECT_FILE_START2 = 2;
    private long mRequestStartTime;
    JSONArray imageList1 = new JSONArray();
    JSONArray imageList2 = new JSONArray();
    ArrayList<HashMap<String, String>> BankNameList = new ArrayList<>();
    CategoryDetailResponse response;
    String PolicyStartDate = null, PolicyCloseDate = null, SettlementLevel = null;
    int ValueAssured = 0, BenchmarkYield = 0, PolicyMasterId = 0;
    double price = 0;
    private ApiService apiService;
    private CompositeDisposable compositeDisposable;
    int FarmerId = 0, FarmID = 0;
    String cropID, cropName, Variety;
    int Imageselectflag = 0;
    String FarmerName = "", PhoneNo = "", FatherName = "", AadharNumber = "", BankID = "", BankName = "", AccountName = "",
            AccountNo = "", IFSCode = "";
    String FarmName = "", StateID = "", StateName = "", DistrictID = "", District = "", Block = "", Village = "",
            FarmArea = "", Contours = "", CenterLat = "", CenterLon = "", SowDate = "";
    double areaSize = 0.0, totalValueAssured = 0.0, totalFee = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.policyregistrationnew);

        imageList1 = new JSONArray();
        imageList2 = new JSONArray();

        db = new DBAdapter(this);
        prefs = getSharedPreferences(AppConstant.SHARED_PREFRENCE_NAME, MODE_PRIVATE);

        apiService = AppController.getInstance().getApiServiceGson_weed();
        compositeDisposable = new CompositeDisposable();

        getBakAPI();

        Bundle bundle = getIntent().getExtras();
        try {
            if (bundle != null) {
                response = bundle.getParcelable("response");

//                Intent i = getIntent();
//                CategoryDetailResponse myParcelableObject = (CategoryDetailResponse) i.getParcelableExtra("response");

                if (bundle.getString("FarmerId") != null && bundle.getString("FarmerId").length() > 0) {
                    FarmerId = Integer.valueOf(bundle.getString("FarmerId"));
                }
                if (bundle.getString("FarmID") != null && bundle.getString("FarmID").length() > 0) {
                    FarmID = Integer.valueOf(bundle.getString("FarmID"));
                }
                if (bundle.getString("cropID") != null && bundle.getString("cropID").length() > 0) {
                    cropID = bundle.getString("cropID");
                }
                if (bundle.getString("cropName") != null && bundle.getString("cropName").length() > 0) {
                    cropName = bundle.getString("cropName");
                }
                if (bundle.getString("Variety") != null && bundle.getString("Variety").length() > 0) {
                    Variety = bundle.getString("Variety");
                }
                if (response != null) {
                    SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");

                    Date date = null;
                    try {
                        if (response.getPolicyStartDate() != null && response.getPolicyStartDate().length() > 4) {
                            date = inputFormat.parse(response.getPolicyStartDate());
                            PolicyStartDate = outputFormat.format(date);
                        }
                        if (response.getPolicyCloseDate() != null && response.getPolicyCloseDate().length() > 4) {
                            date = inputFormat.parse(response.getPolicyCloseDate());
                            PolicyCloseDate = outputFormat.format(date);
                        }

//                        BrandName = response.getBrandName();
                        BankName = response.getBrandName();
                        SettlementLevel = response.getSettlementLevel();
                        ValueAssured = response.getValueAssured();
                        BenchmarkYield = response.getBenchmarkYield();
                        PolicyMasterId = response.getPolicyMasterID();
                        price = response.getPrice();


                        //SelectFarmerDetails_policy
                        try {
                            if (SelectFarmerDetails_policy != null) {
                                FarmerName = SelectFarmerDetails_policy.getFarmerpersonalData().get(0).getFarmerName();
                                FatherName = SelectFarmerDetails_policy.getFarmerpersonalData().get(0).getFatherName();
                                PhoneNo = SelectFarmerDetails_policy.getFarmerpersonalData().get(0).getFarmerPhno();
//                            AadharNumber = SelectFarmerDetails_policy.getFarmerpersonalData().get(0).getA();
//                            BandName = SelectFarmerDetails_policy.getFarmerpersonalData().get(0).getAc();
//                            AccountName = SelectFarmerDetails_policy.getFarmerpersonalData().get(0).getAccountN();
                                AccountNo = SelectFarmerDetails_policy.getFarmerpersonalData().get(0).getAccountNo();
//                            IFSCode = SelectFarmerDetails_policy.getFarmerpersonalData().get(0).getIFs();
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        //SelectFarmDetails_policy
                        if (SelectFarmDetails_policy != null && SelectFarmDetails_policy.length() > 0) {
                            try {
                                FarmName = SelectFarmDetails_policy.getString("FarmName");
                                StateID = SelectFarmDetails_policy.getString("State");
                                StateName = SelectFarmDetails_policy.getString("StateName");
                                DistrictID = SelectFarmDetails_policy.getString("DistrictID");
                                District = SelectFarmDetails_policy.getString("District");
                                Block = SelectFarmDetails_policy.getString("Block");
                                Village = SelectFarmDetails_policy.getString("VillageStr");
                                FarmArea = SelectFarmDetails_policy.getString("Area");
                                Contours = SelectFarmDetails_policy.getString("Contour");
                                CenterLat = SelectFarmDetails_policy.getString("CenterLat");
                                CenterLon = SelectFarmDetails_policy.getString("CenterLon");

                                AadharNumber = SelectFarmDetails_policy.getString("AadharNo");

                                JSONArray array = SelectFarmDetails_policy.getJSONArray("CropInfo");
                                JSONObject obj = array.getJSONObject(0);
                                SowDate = obj.getString("SowDate");
                                SowDate = ConvertDateFormat(SowDate, 1);
                                Variety = obj.getString("Variety");
                                try {
                                    if (FarmArea != null && FarmArea.length() > 0 && FarmArea.contains(".")) {
                                        double roundfig = GetRevisedFarmArea(Double.parseDouble(FarmArea));
                                        FarmArea = String.valueOf(roundfig);
                                    }
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }

                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }


                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }


        binding.txtExpectedHarvestingDates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.setDatePicker(PolicyRegistrationNew.this, binding.txtExpectedHarvestingDates, "YYYYMMDD");
            }
        });

        binding.btnCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.imgvwFarmMarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(), EditFarmActivity.class);
                in.putExtra("ActivityName", "PolicyRegistrationNew");
                startActivity(in);
            }
        });
        binding.chooseImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.chooseImage1.setImageBitmap(null);
                Imageselectflag = 1;
                selectImage(1);
            }
        });
        binding.chooseImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.chooseImage1.setImageBitmap(null);
                Imageselectflag = 2;
                selectImage(2);
            }
        });
        binding.imageupload1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (imageString1 != null && imageString1.length() > 0) {
                    if (getTotalNoimageperDay(PolicyRegistrationNew.this, AppConstant.farm_id) < 6) {
                        if (isNetworkAvailable()) {
                            uploadImage(1, ImageName1);
                        } else {
                            getDynamicLanguageToast(getApplicationContext(), "Networknotavailableclicksubmit", R.string.Networknotavailableclicksubmit);
                        }
                    } else {
                        getDynamicLanguageToast(getApplicationContext(), "Yourlimitedexpired", R.string.Yourlimitedexpired);
                    }
                } else {
                    getDynamicLanguageToast(getApplicationContext(), "selecttheimage", R.string.selecttheimage);
                }
            }
        });

        binding.imvwDropdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.llFarmInf.setVisibility(View.VISIBLE);
                binding.imvwDropdown.setVisibility(View.GONE);
                binding.imvwDropUp.setVisibility(View.VISIBLE);
            }
        });
        binding.imvwDropUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                binding.llFarmInf.setVisibility(View.GONE);
                binding.imvwDropdown.setVisibility(View.VISIBLE);
                binding.imvwDropUp.setVisibility(View.GONE);

            }
        });

        binding.imageupload2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (imageString2 != null && imageString2.length() > 0) {
                    if (getTotalNoimageperDay(PolicyRegistrationNew.this, AppConstant.farm_id) < 6) {
                        if (isNetworkAvailable()) {
                            uploadImage(2, ImageName2);
                        } else {
                            getDynamicLanguageToast(getApplicationContext(), "Networknotavailableclicksubmit", R.string.Networknotavailableclicksubmit);
                        }
                    } else {
                        getDynamicLanguageToast(getApplicationContext(), "Yourlimitedexpired", R.string.Yourlimitedexpired);
                    }
                } else {
                    getDynamicLanguageToast(getApplicationContext(), "selecttheimage", R.string.selecttheimage);
                }
            }
        });

        setFlag(0);

        setRelativeLayoutlistener(binding.rl1, 1);
        setRelativeLayoutlistener(binding.rl2, 2);
        setRelativeLayoutlistener(binding.rl3, 3);
        //Prev
        setButtonListenerPrev(binding.Prev2, 1);
        setButtonListenerPrev(binding.Prev3, 2);
        //Next
        setButtonListenerNxt(binding.Next1, 1);
        setButtonListenerNxt(binding.Next2, 2);
//        setButtonListenerNxt(binding.Next3, 3);
        binding.Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Condition Check
                if (isCheckCondition()) {
                    new setPolicySave().execute();
                }
            }
        });
    }

    public void setRelativeLayoutlistener(RelativeLayout rl, int flag) {

        rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    setFlag(flag - 1);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

    }

    public void setButtonListenerPrev(Button button, int flag) {

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    setFlag(flag - 1);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

    }

    public void setButtonListenerNxt(Button btn, int flag) {

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFlag(flag);
            }
        });


    }

    public void setFlag(int flag) {
        try {
            switch (flag) {
                case 0:
                    binding.rl1.setBackground(getResources().getDrawable(R.drawable.circle_primary));
                    binding.rl2.setBackground(getResources().getDrawable(R.drawable.circle_grayborder));
                    binding.rl3.setBackground(getResources().getDrawable(R.drawable.circle_grayborder));

                    binding.txtTitle1.setTextColor(getResources().getColor(R.color.ColorPrimary));
                    binding.txtTitle2.setTextColor(getResources().getColor(R.color.black));
                    binding.txtTitle3.setTextColor(getResources().getColor(R.color.black));

                    binding.txt1.setText("1");
                    binding.txt2.setText("2");
                    binding.txt3.setText("3");

                    binding.txt1.setTextColor(getResources().getColor(R.color.white));
                    binding.txt2.setTextColor(getResources().getColor(R.color.gray));
                    binding.txt3.setTextColor(getResources().getColor(R.color.gray));

                    binding.view1.setBackgroundColor(getResources().getColor(R.color.gray));
                    binding.view2.setBackgroundColor(getResources().getColor(R.color.gray));

                    setPolicy(1);
                    break;
                case 1:
                    binding.rl1.setBackground(getResources().getDrawable(R.drawable.circle_correct));
                    binding.rl2.setBackground(getResources().getDrawable(R.drawable.circle_primary));
                    binding.rl3.setBackground(getResources().getDrawable(R.drawable.circle_grayborder));

                    binding.txtTitle1.setTextColor(getResources().getColor(R.color.ColorPrimary));
                    binding.txtTitle2.setTextColor(getResources().getColor(R.color.ColorPrimary));
                    binding.txtTitle3.setTextColor(getResources().getColor(R.color.black));

                    binding.txt1.setText("");
                    binding.txt2.setText("2");
                    binding.txt3.setText("3");

//                            binding.txt1.setTextColor(getResources().getColor(R.color.white));
                    binding.txt2.setTextColor(getResources().getColor(R.color.white));
                    binding.txt3.setTextColor(getResources().getColor(R.color.gray));

                    binding.view1.setBackgroundColor(getResources().getColor(R.color.ColorPrimary));
                    binding.view2.setBackgroundColor(getResources().getColor(R.color.gray));

                    setPolicy(2);
                    break;
                case 2:
                    binding.rl1.setBackground(getResources().getDrawable(R.drawable.circle_correct));
                    binding.rl2.setBackground(getResources().getDrawable(R.drawable.circle_correct));
                    binding.rl3.setBackground(getResources().getDrawable(R.drawable.circle_primary));

                    binding.txtTitle1.setTextColor(getResources().getColor(R.color.ColorPrimary));
                    binding.txtTitle2.setTextColor(getResources().getColor(R.color.ColorPrimary));
                    binding.txtTitle3.setTextColor(getResources().getColor(R.color.ColorPrimary));

                    binding.txt1.setText("");
                    binding.txt2.setText("");
                    binding.txt3.setText("3");

//                            binding.txt1.setTextColor(getResources().getColor(R.color.white));
//                            binding.txt2.setTextColor(getResources().getColor(R.color.white));
                    binding.txt3.setTextColor(getResources().getColor(R.color.white));

                    binding.view1.setBackgroundColor(getResources().getColor(R.color.ColorPrimary));
                    binding.view2.setBackgroundColor(getResources().getColor(R.color.ColorPrimary));

                    setPolicy(3);
                    break;
                case 3:
                    binding.rl1.setBackground(getResources().getDrawable(R.drawable.circle_correct));
                    binding.rl2.setBackground(getResources().getDrawable(R.drawable.circle_correct));
                    binding.rl3.setBackground(getResources().getDrawable(R.drawable.circle_correct));

                    binding.txtTitle1.setTextColor(getResources().getColor(R.color.ColorPrimary));
                    binding.txtTitle2.setTextColor(getResources().getColor(R.color.ColorPrimary));
                    binding.txtTitle3.setTextColor(getResources().getColor(R.color.ColorPrimary));

                    binding.txt1.setText("");
                    binding.txt2.setText("");
                    binding.txt3.setText("");

//                            binding.txt1.setTextColor(getResources().getColor(R.color.white));
//                            binding.txt2.setTextColor(getResources().getColor(R.color.white));
//                            binding.txt3.setTextColor(getResources().getColor(R.color.white));

                    binding.view1.setBackgroundColor(getResources().getColor(R.color.ColorPrimary));
                    binding.view2.setBackgroundColor(getResources().getColor(R.color.ColorPrimary));
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setPolicy(int flag) {
        try {
            switch (flag) {
                case 1:
                    binding.llFarmerInfo.setVisibility(View.VISIBLE);
                    binding.llFarmInfo.setVisibility(View.GONE);
                    binding.llPolicyInfo.setVisibility(View.GONE);

                    setTextValue(FarmerName, binding.tbwFarmerName, binding.txtFarmerNames);
                    setTextValue(PhoneNo, binding.tbwPhoneno, binding.txtPhonenos);
                    setTextValue(FatherName, binding.tbwFatherName, binding.txtFatherNames);
                    setTextValue(AadharNumber, binding.tbwAadharNo, binding.editAadharNo);
//                    setTextValue(BankName, binding.tbwBankName, binding.editBankName);
                    setTextValue(AccountName, binding.tbwAccountName, binding.editAccountName);
                    setTextValue(AccountNo, binding.tbwAccountNumber, binding.editAccountNumber);
                    setTextValue(IFSCode, binding.tbwIFSCCode, binding.editIFSCCode);


                    FarmerName = binding.txtFarmerNames.getText().toString();
//                    PhoneNo = binding.editPhoneno.getText().toString();
//                    FatherName = binding.editFatherName.getText().toString();
                    AadharNumber = binding.editAadharNo.getText().toString();
//                    BankName = binding.editAadharNo.getText().toString();
                    AccountName = binding.editAccountName.getText().toString();
                    AccountNo = binding.editAccountNumber.getText().toString();
                    IFSCode = binding.editIFSCCode.getText().toString();

                    break;
                case 2:
                    binding.llFarmerInfo.setVisibility(View.GONE);
                    binding.llFarmInfo.setVisibility(View.VISIBLE);
                    binding.llPolicyInfo.setVisibility(View.GONE);

                    setTextValue(FarmName, binding.tbwFarmName, binding.editFarmName);
                    setTextValue(StateName, binding.tbwStateName, binding.txtStateNames);
                    setTextValue(District, binding.tbwDistrictName, binding.txtDistrictNames);
                    setTextValue(Block, binding.tbwBlockName, binding.txtBlockNames);
                    setTextValue(Village, binding.tbwVillageName, binding.txtVillageNames);
                    setTextValue(cropName, binding.tbwCropName, binding.txtCropNames);
                    setTextValue(FarmArea, binding.tbwFarmAreaFarm, binding.txtFarmAreasFarm);
                    setTextValue(Variety, binding.tbwVariety, binding.txtVarietys);
                    setTextValue(SowDate, binding.tbwPlantingTranplantingDate, binding.txtPlantingTranplantingDates);

                    FarmName = binding.editFarmName.getText().toString();

                    break;
                case 3:
                    binding.llFarmerInfo.setVisibility(View.GONE);
                    binding.llFarmInfo.setVisibility(View.GONE);
                    binding.llPolicyInfo.setVisibility(View.VISIBLE);

                    setTextValue(PolicyStartDate, binding.tbwPolicyStartDate, binding.txtPolicyStartDates);
                    setTextValue(PolicyCloseDate, binding.tbwPolicyCloseDate, binding.txtPolicyCloseDates);
                    setTextValue(FarmArea, binding.tbwFarmArea, binding.txtFarmAreas);
                    setTextValue(String.valueOf(BenchmarkYield), binding.tbwBenchmarkYield, binding.txtBenchmarkYields);


                    if (FarmArea != null && FarmArea.length() > 0) {
                        areaSize = Double.parseDouble(FarmArea);
                        if (areaSize > 0.0) {
                            if (ValueAssured > 0) {
                                totalValueAssured = areaSize * ValueAssured;
                            }
                            if (price > 0.0) {
                                totalFee = areaSize * price;
                            }
                        }
                    }

                    setTextValue(String.valueOf(totalValueAssured), binding.tbwValueAssured, binding.txtValueAssureds);
                    setTextValue(String.valueOf(totalFee), binding.tbwFee, binding.txtFees);


                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setTextValue(String Value, TableRow tblrw, TextView textView) {
        try {
            if (Value != null && Value.length() > 0) {
//                tblrw.setVisibility(View.VISIBLE);
                textView.setText(Value);
            }
//            else {
//                tblrw.setVisibility(View.GONE);
//            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setTextValue(String Value, TableRow tblrw, EditText textView) {
        try {
            if (Value != null && Value.length() > 0) {
//                tblrw.setVisibility(View.VISIBLE);
                textView.setText(Value);
            }
//            else {
//                tblrw.setVisibility(View.GONE);
//            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void getBakAPI() {
        // display a progress dialog
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage(getDynamicLanguageValue(getApplicationContext(), "Pleasewait", R.string.Pleasewait)); // set message
        progressDialog.show(); // show progress dialog
        RequestCategoryBean requestCategoryBean = new RequestCategoryBean();

        apiService.getBankName().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResponseObserver<Response<List<BankNameResponse>>>(compositeDisposable) {
                    @Override
                    public void onNetworkError(Throwable e) {
                        progressDialog.cancel();
//                        showError(getString(R.string.no_data));
//                        binding.emptyView.setVisibility(View.VISIBLE);
//                        binding.productRecyclerView.setVisibility(View.GONE);
//                        binding.title.setText(getResources().getString(R.string.Currentlythereisnoserviceavailable));
                    }

                    @Override
                    public void onServerError(Throwable e, int code) {
                        progressDialog.cancel();
//                        showError(getString(R.string.server_not_found));
//                        binding.emptyView.setVisibility(View.VISIBLE);
//                        binding.productRecyclerView.setVisibility(View.GONE);
//                        binding.title.setText(getResources().getString(R.string.Currentlythereisnoserviceavailable));
                    }

                    @Override
                    public void onNext(Response<List<BankNameResponse>> response) {
                        progressDialog.cancel();
                        List<BankNameResponse> responsesData = response.body();
                        setBank(responsesData);
                    }
                });

    }

    public void setBank(List<BankNameResponse> data) {
        BankNameList = new ArrayList<>();

        ArrayList<String> list = new ArrayList<>();

        HashMap<String, String> hash = new HashMap<>();
        hash.put("BankID", "0");
        hash.put("BankName", getDynamicLanguageValue(getApplicationContext(), "Select", R.string.Select));
        BankNameList.add(hash);
        list.add(getDynamicLanguageValue(getApplicationContext(), "Select", R.string.Select));
        if (data.size() > 0) {
            for (int i = 0; i < data.size(); i++) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("BankID", String.valueOf(data.get(i).getId()));
                hashMap.put("BankName", data.get(i).getBankName());
                list.add(data.get(i).getBankName());
                BankNameList.add(hashMap);
            }
        }
        ArrayAdapter<String> stateListAdapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, list);
        binding.spinBankName.setAdapter(stateListAdapter);
        binding.spinBankName.setTitle(getDynamicLanguageValue(getApplicationContext(), "SelectBank", R.string.SelectBank));
        binding.spinBankName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position > 0 && BankNameList.size() > 0) {
                    BankID = BankNameList.get(position).get("BankID");
                    BankName = BankNameList.get(position).get("BankName");
                } else {
                    BankID = "0";
                    BankName = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        db.close();
    }


    private class setPolicySave extends AsyncTask<Void, Void, String> {

        String result = null;
        String json;
        TransparentProgressDialog progressDialog;

        public setPolicySave() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new TransparentProgressDialog(
                    PolicyRegistrationNew.this,
                    getDynamicLanguageValue(getApplicationContext(), "Dataisloading", R.string.Dataisloading));
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @Override
        protected String doInBackground(Void... params) {
            String response = null;
//            String URL = AppManager.getInstance().UploadPolicyRegistrationURL;//Herojit Comment
            String URL = AppManager.getInstance().UploadPolicyRegistrationURL;//Herojit Comment
//            String URL = AppManager.getInstance().UploadPolicyRegistrationURLTest;//Herojit Comment
            JSONObject object = getJSON();
            String jsonvalue = object.toString();

            response = AppManager.getInstance().httpRequestPostMethodReport(URL, jsonvalue);
            response = response.replace("\"{", "{");
            response = response.replace("}\"", "}");
            response = response.replace("\"", "");
            return response;
//            return "R8MY-A981";
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPreExecute();
            progressDialog.dismiss();

//            if (response != null && response.contains("Status")) {
            if (response != null) {
                if (response.length() > 1 && response.length() < 10) {
                    try {
//                    JSONObject obj = new JSONObject(response);
//                    String Status = obj.getString("Status");
//                    String policyID = obj.getString("Id");
//                    if (Status.equalsIgnoreCase("Success")) {
                        Intent in = new Intent(getApplicationContext(), PolicyPayment.class);
                        in.putExtra("ActivityName", "PolicyRegistrationNew");
                        in.putExtra("totalAssured", String.valueOf(totalValueAssured));
                        in.putExtra("areaSize", String.valueOf(areaSize));
                        in.putExtra("totalFee", String.valueOf(totalFee));
                        in.putExtra("projectID", SelectFarmDetails_policy.getString("ProjectID"));
                        in.putExtra("farmID", String.valueOf(FarmID));
                        in.putExtra("farmerId", String.valueOf(FarmerId));
                        in.putExtra("PolicyID", String.valueOf(response));
                        in.putExtra("PendingAmount", "0.0");
                        startActivity(in);
                        finish();
                    } catch (Exception ex) {
                        getDynamicLanguageToast(getApplicationContext(), "FormattingError", R.string.FormattingError);
                        ex.printStackTrace();
                    }
                } else if (response.equalsIgnoreCase("Fail:Policy Overlap")) {
                    getDynamicLanguageToast(getApplicationContext(), "Registrationisfailed", R.string.Registrationisfailed);
                } else if (response.length() < 20) {
                    getDynamicLanguageToast(getApplicationContext(), response.toString());
                } else {
                    getDynamicLanguageToast(getApplicationContext(), "FormattingError", R.string.FormattingError);
                }
            } else {
                getDynamicLanguageToast(getApplicationContext(), "server_not_found", R.string.server_not_found);
            }
        }

    }

    public JSONObject getJSON() {

        JSONObject obj = new JSONObject();

        try {
            obj.put("DeliveryStatus", "Pending");
            obj.put("UserID", AppConstant.user_id);
            obj.put("farmerid", String.valueOf(FarmerId));
            obj.put("projectid", SelectFarmDetails_policy.getString("ProjectID"));
            obj.put("PaymentType", "");

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("StartDate", binding.txtPolicyStartDates.getText().toString());
            jsonObject.put("CloseDate", binding.txtPolicyCloseDates.getText().toString());

            jsonObject.put("Transplanting", SowDate);//for testing purpose
            jsonObject.put("ExpectedHarvestingDate", binding.txtExpectedHarvestingDates.getText().toString());
            jsonObject.put("FarmID", FarmID);
            jsonObject.put("FarmerID", FarmerId);
            jsonObject.put("PhoneNumber", PhoneNo);
            jsonObject.put("Address", "");
            jsonObject.put("PolicyMasterId", PolicyMasterId);
            jsonObject.put("ValueAssured", totalValueAssured);
            String Fee = binding.txtFees.getText().toString();
            jsonObject.put("Fee", totalFee);
            jsonObject.put("AadhaarNo", binding.editAadharNo.getText().toString());
            jsonObject.put("AadhaarPhotoID", imageList2.toString());
            jsonObject.put("LandDocumentPhotoID", imageList1.toString());
            jsonObject.put("AccountName", binding.editAccountName.getText().toString());
            jsonObject.put("AccountNo", binding.editAccountNumber.getText().toString());
            jsonObject.put("IFSCCode", binding.editIFSCCode.getText().toString());
            if (BankID != null && BankID.length() > 0) {
                jsonObject.put("BankID", Integer.valueOf(BankID));
            } else {
                jsonObject.put("BankID", 0);
            }

            obj.put("PolicyModel", jsonObject);
            obj.put("farmerserviceslst", getFarmerSrvicelist());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return obj;
    }

    public JSONArray getFarmerSrvicelist() {

        JSONArray list = new JSONArray();
        try {
            JSONObject obj = new JSONObject();
            obj.put("FarmerID", FarmerId);
            obj.put("UserID", Integer.valueOf(AppConstant.user_id));
            obj.put("ProjectID", SelectFarmDetails_policy.getString("ProjectID"));
            obj.put("FarmerServiceID", 1);
            list.put(obj);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    private void selectImage(int flag) {
        final CharSequence[] items = {getDynamicLanguageValue(getApplicationContext(), "TakePhoto", R.string.TakePhoto), getDynamicLanguageValue(getApplicationContext(), "Selectfromgallery", R.string.Selectfromgallery),
                getDynamicLanguageValue(getApplicationContext(), "Cancel", R.string.Cancel)};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //  builder.setTitle("Upload Document!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {


                if (items[item].equals(getDynamicLanguageValue(getApplicationContext(), "TakePhoto", R.string.TakePhoto))) {
                    userChoosenTask = "Take Photo";
                    boolean resultCam = Utility.checkPermissionCamera(PolicyRegistrationNew.this);
                    String imageFileName = getCreateImageName();
                    if (resultCam) {
//                        cameraIntent();
//                        getCheckCameraScreenOnOff = true;
                        Intent in = new Intent(PolicyRegistrationNew.this, CameraSurfaceView.class);
                        in.putExtra("CameraScreenTypeNearFar", "Near");//Close Up
                        in.putExtra("ActivityNameComingFrom", "PolicyRegistration");
                        if (flag == 1) {
                            ImageName1 = imageFileName;
                        } else if (flag == 2) {
                            ImageName2 = imageFileName;
                        } else
                            in.putExtra("imageFileName", imageFileName);
                        startActivity(in);
                    }

                } else if (items[item].equals(getDynamicLanguageValue(getApplicationContext(), "Selectfromgallery", R.string.Selectfromgallery))) {
                    userChoosenTask = "Select From Gallery";
                    boolean resultCam = Utility.checkPermissionGallery(PolicyRegistrationNew.this);
                    if (resultCam) {
                        galleryIntent();
                    }
                } else if (items[item].equals(getDynamicLanguageValue(getApplicationContext(), "Cancel", R.string.Cancel))) {
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
        }
    }

    public void setCustomCameraImageView() {
        try {
            if (CustomCamera_ImageValue != null) {
                if (Imageselectflag == 1) {
                    imageString1 = CustomCamera_ImageValue;
                    binding.chooseImage1.setImageBitmap(CustomCamera_bitmap);
                } else if (Imageselectflag == 2) {
                    imageString2 = CustomCamera_ImageValue;
                    binding.chooseImage2.setImageBitmap(CustomCamera_bitmap);
                }

            }
            getCheckCameraScreenOnOff = false;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (db != null) {
            db = new DBAdapter(this);
        }
        setLanguages();
        if (getCheckCameraScreenOnOff == true) {
            setCustomCameraImageView();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (db != null) {
            db = new DBAdapter(this);
        }
        Log.d("OnPause Method", "OnPause Method called");
        setScreenTracking(this, db, SN_PolicyRegistration, UID);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (db != null) {
            db = new DBAdapter(this);
        }
        Log.d("onStop Method", "onStop Method called");
        setScreenTracking(this, db, SN_PolicyRegistration, UID);
    }

    //Herojit Add
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE_START1) {
                onSelectFromGalleryResult1(data);
            } else if (requestCode == SELECT_FILE_START2) {
                onSelectFromGalleryResult2(data);
            }
        }
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult1(Intent data) {
        try {
            Bitmap bm = null;
            if (data != null) {
                try {
                    bm = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            binding.chooseImage1.setImageBitmap(bm);
            imageString1 = imageToString(bm);
            CustomCamera_bitmap = bm;
            String filePath = data.getData().getPath();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult2(Intent data) {
        try {
            Bitmap bm = null;
            if (data != null) {
                try {
                    bm = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            binding.chooseImage2.setImageBitmap(bm);
            imageString2 = imageToString(bm);
            CustomCamera_bitmap = bm;
            String filePath = data.getData().getPath();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

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

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void uploadImage(final int flag, final String ImageNames) {

        final TransparentProgressDialog pDialog = new TransparentProgressDialog(
                PolicyRegistrationNew.this, getDynamicLanguageValue(getApplicationContext(), "Dataisloading", R.string.Dataisloading));
        pDialog.setCancelable(false);
        pDialog.show();
        JSONObject jsonObject = null;
        String usi = AppConstant.user_id;

        Double lat = LatLonCellID.lat;
        Double lon = LatLonCellID.lon;
        Log.v("imageLat_long", lat + "," + lon);
        String URLImageUpload = AppManager.getInstance().uploadImageURL1;
        try {
            jsonObject = new JSONObject();
            if (flag == 1) {
                jsonObject.putOpt("ImageString", imageString1);
            } else if (flag == 2) {
                jsonObject.putOpt("ImageString", imageString2);
            }
            jsonObject.putOpt("UserID", usi);

            // jsonObject.putOpt("Lat_Lng", lat+","+lon);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        mRequestStartTime = System.currentTimeMillis();
        CustomJSONObjectRequest jsonObjectRequest = new CustomJSONObjectRequest(Request.Method.PUT, URLImageUpload, jsonObject, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                pDialog.cancel();
                Log.i("Response upload image", "" + response.toString());
                String Latitude = "0.0", Longitude = "0.0";
                try {

                    int seconds = getAPIimeResponseinSecond(mRequestStartTime);
                    if (seconds > 3)
                        SaveLocalFile(db, PolicyRegistrationNew.this, SN_PolicyRegistration, URLImageUpload, response.toString(), "", "" + seconds, AppConstant.farm_id, "Working");
                    db.open();
                    ArrayList<HashMap<String, String>> hasmap = new ArrayList<>();
                    if (ImageNames != null && ImageNames.length() > 0) {
                        String sql = "Select * from " + db.TABLE_ImageLocalStorage + " where ImageName='" + ImageNames + "'";
                        hasmap = db.getDynamicTableValue(sql);
                        if (hasmap != null && hasmap.size() > 0) {
                            Latitude = hasmap.get(0).get("Current_Lat");
                            Longitude = hasmap.get(0).get("Current_Long");
                        } else {
                            Double lat = LatLonCellID.lat;
                            Double lon = LatLonCellID.lon;
                            Latitude = String.valueOf(lat);
                            Longitude = String.valueOf(lon);
                        }
                    }

                    String Imagename = response.getString("uploadBase64ImageResult");
                    if (Imagename.length() > 0) {
                        if (flag == 1) {
                            imageList1 = Utility.addImageName(Imagename.toString(), imageList1, 1, Latitude, Longitude);
                            imageString1 = "";
                            binding.chooseImage1.setImageBitmap(null);
                            binding.chooseImage1.setBackgroundDrawable(getResources().getDrawable(R.drawable.image_upload_icon));
//                            binding.imageupload1.setEnabled(false);
//                            binding.imageupload1.setBackground(getResources().getDrawable(R.drawable.btn_bg_gray));
                        } else if (flag == 2) {
                            imageList2 = Utility.addImageName(Imagename.toString(), imageList2, 1, Latitude, Longitude);
                            imageString2 = "";
                            binding.chooseImage2.setImageBitmap(null);
                            binding.chooseImage2.setBackgroundDrawable(getResources().getDrawable(R.drawable.image_upload_icon));
//                            binding.imageupload2.setEnabled(false);
//                            binding.imageupload2.setBackground(getResources().getDrawable(R.drawable.btn_bg_gray));
                        }
                        setCount();

                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                getDynamicLanguageToast(getApplicationContext(), "ImageUploadedSuccessfully", R.string.ImageUploadedSuccessfully);

//                getResponse(response, co);

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.cancel();
                Log.v("Response vishal coupon", "" + error.toString());
                getDynamicLanguageToast(getApplicationContext(), error.getMessage());
                int seconds = getAPIimeResponseinSecond(mRequestStartTime);
                SaveLocalFile(db, PolicyRegistrationNew.this, SN_PolicyRegistration, URLImageUpload, "", "Internet Connection Error / Server API Error / Timeout Error", "" + seconds, AppConstant.farm_id, "Error");

            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                40000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    public boolean isCheckCondition() {
        boolean value = false;
        if (!setStringCheck(binding.txtPolicyStartDates.getText().toString())) {
            getDynamicLanguageToast(getApplicationContext(), "PleaseenterthePolicyStartDate", R.string.PleaseenterthePolicyStartDate);
            return value;
        } else if (!setStringCheck(binding.txtPolicyCloseDates.getText().toString())) {
            getDynamicLanguageToast(getApplicationContext(), "PleaseenterthePolicyCloseDate", R.string.PleaseenterthePolicyCloseDate);
            return value;
        } else if (!setStringCheck(binding.txtExpectedHarvestingDates.getText().toString())) {
            getDynamicLanguageToast(getApplicationContext(), "PleaseentertheExpectedHarvestingDate", R.string.PleaseentertheExpectedHarvestingDate);
            return value;
        }
//        else if (!checkmobileno(binding.editPhoneno.getText().toString())) {
//            Toast.makeText(getApplicationContext(), getResources().getString(R.string.Pleaseenterthevalidmobile), Toast.LENGTH_LONG).show();
//            return value;
//        } else if (!isValidAadharNumber(binding.editAadharNo.getText().toString())) {
//            Toast.makeText(getApplicationContext(), getResources().getString(R.string.PleaseenterthevalidAadharno), Toast.LENGTH_LONG).show();
//            return value;
//        }

        return true;
    }

    public void setCount() {
        try {
            if (imageList1 != null && imageList1.length() > 0) {
                int landcount = imageList1.length();
                binding.txtLandDocumentPhotoCount.setText(String.valueOf(" ( " + landcount + " ) "));
            } else {
                binding.txtLandDocumentPhotoCount.setText("");
            }
            if (imageList2 != null && imageList2.length() > 0) {
                int landcount = imageList2.length();
                binding.txtAadharCardPhotoCount.setText(String.valueOf(" ( " + landcount + " ) "));
            } else {
                binding.txtAadharCardPhotoCount.setText("");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setLanguages() {
        //Heading
        setFontsStyleTxt(this, binding.txtFarmPolicy, 2);
        setFontsStyleTxt(this, binding.txt1, 6);
        setFontsStyleTxt(this, binding.txt2, 6);
        setFontsStyleTxt(this, binding.txt3, 6);
        setFontsStyleTxt(this, binding.txtTitle1, 5);
        setFontsStyleTxt(this, binding.txtTitle2, 5);
        setFontsStyleTxt(this, binding.txtTitle3, 5);
        setFontsStyleTxt(this, binding.txtFarmerInformation, 4);
        setFontsStyleTxt(this, binding.txtFarmInformation, 4);
        setFontsStyleTxt(this, binding.txtPolicyInformation, 4);

        setFontsStyleTxt(this, binding.txtFarmerName, 6);
        setFontsStyleTxt(this, binding.txtFarmerNames, 5);
        setFontsStyleTxt(this, binding.txtPhoneno, 6);
        setFontsStyleTxt(this, binding.txtPhonenos, 5);
        setFontsStyleTxt(this, binding.txtFatherName, 6);
        setFontsStyleTxt(this, binding.txtFatherNames, 5);
        setFontsStyleTxt(this, binding.txtAadharNo, 6);
        setFontsStyle(this, binding.editAadharNo);
        setFontsStyleTxt(this, binding.txtBankName, 6);
        setFontsStyleTxt(this, binding.txtAccountName, 6);
        setFontsStyle(this, binding.editAccountName);
        setFontsStyleTxt(this, binding.txtAccountNumber, 6);
        setFontsStyle(this, binding.editAccountNumber);
        setFontsStyleTxt(this, binding.txtIFSCCode, 6);
        setFontsStyle(this, binding.editIFSCCode);

        setFontsStyleTxt(this, binding.txtFarmName, 6);
        setFontsStyleTxt(this, binding.editFarmName, 5);
        setFontsStyleTxt(this, binding.txtStateName, 6);
        setFontsStyleTxt(this, binding.txtStateNames, 5);
        setFontsStyleTxt(this, binding.txtDistrictName, 6);
        setFontsStyleTxt(this, binding.txtDistrictNames, 5);
        setFontsStyleTxt(this, binding.txtBlockName, 6);
        setFontsStyleTxt(this, binding.txtBlockNames, 5);
        setFontsStyleTxt(this, binding.txtVillageName, 6);
        setFontsStyleTxt(this, binding.txtVillageNames, 5);
        setFontsStyleTxt(this, binding.txtCropName, 6);
        setFontsStyleTxt(this, binding.txtCropNames, 5);
        setFontsStyleTxt(this, binding.txtVariety, 6);
        setFontsStyleTxt(this, binding.txtVarietys, 5);
        setFontsStyleTxt(this, binding.txtFarmAreaFarm, 6);
        setFontsStyleTxt(this, binding.txtFarmAreasFarm, 5);
        setFontsStyleTxt(this, binding.txtPlantingTranplantingDate, 6);
        setFontsStyleTxt(this, binding.txtPlantingTranplantingDates, 5);
        setFontsStyleTxt(this, binding.txtAadharCardPhoto, 6);
        setFontsStyleTxt(this, binding.txtAadharCardPhotoCount, 5);
        setFontsStyleTxt(this, binding.txtLandDocumentPhoto, 6);
        setFontsStyleTxt(this, binding.txtLandDocumentPhotoCount, 5);
        setFontsStyle(this, binding.imageupload1);
        setFontsStyle(this, binding.imageupload2);

        setFontsStyle(this, binding.Next1);
        setFontsStyle(this, binding.Next2);
        setFontsStyle(this, binding.Submit);
        setFontsStyle(this, binding.Prev1);
        setFontsStyle(this, binding.Prev2);
        setFontsStyle(this, binding.Prev3);
        setDynamicLanguage(this, binding.Next1, "Next", R.string.Next);
        setDynamicLanguage(this, binding.Next2, "Next", R.string.Next);
        setDynamicLanguage(this, binding.Submit, "Submit", R.string.Submit);
        setDynamicLanguage(this, binding.Prev1, "Prev", R.string.Prev);
        setDynamicLanguage(this, binding.Prev2, "Prev", R.string.Prev);
        setDynamicLanguage(this, binding.Prev3, "Prev", R.string.Prev);


        setFontsStyleTxt(this, binding.txtPolicyStartDate, 6);
        setFontsStyleTxt(this, binding.txtPolicyStartDates, 5);
        setFontsStyleTxt(this, binding.txtPolicyCloseDate, 6);
        setFontsStyleTxt(this, binding.txtPolicyCloseDates, 5);
        setFontsStyleTxt(this, binding.txtFarmArea, 6);
        setFontsStyleTxt(this, binding.txtFarmAreas, 5);
        setFontsStyleTxt(this, binding.txtBenchmarkYield, 6);
        setFontsStyleTxt(this, binding.txtBenchmarkYields, 5);
        setFontsStyleTxt(this, binding.txtValueAssured, 6);
        setFontsStyleTxt(this, binding.txtValueAssureds, 5);
        setFontsStyleTxt(this, binding.txtFee, 6);
        setFontsStyleTxt(this, binding.txtFees, 5);
        setFontsStyleTxt(this, binding.txtExpectedHarvestingDate, 6);
        setFontsStyleTxt(this, binding.txtExpectedHarvestingDates, 5);
        setDynamicLanguage(this, binding.txtFarmPolicy, "PolicyRegistration", R.string.PolicyRegistration);
        setDynamicLanguage(this, binding.txtTitle1, "FarmerInfo", R.string.FarmerInfo);
        setDynamicLanguage(this, binding.txtTitle2, "FarmInfo", R.string.FarmInfo);
        setDynamicLanguage(this, binding.txtTitle3, "PolicyInfo", R.string.PolicyInfo);
        setDynamicLanguage(this, binding.txtFarmerInformation, "FarmerInformation", R.string.FarmerInformation);
        setDynamicLanguage(this, binding.txtFarmInformation, "FarmInformation", R.string.FarmInformation);
        setDynamicLanguage(this, binding.txtPolicyInformation, "PolicyInformation", R.string.PolicyInformation);

        setDynamicLanguage(this, binding.txtFarmerName, "FarmerName", R.string.FarmerName);
        setDynamicLanguage(this, binding.txtPhoneno, "Phoneno", R.string.Phoneno);
        setDynamicLanguage(this, binding.txtFatherName, "FatherName", R.string.FatherName);
        setDynamicLanguage(this, binding.txtAadharNo, "AadharNo", R.string.AadharNo);
        setDynamicLanguage(this, binding.txtBankName, "BankName", R.string.BankName);
        setDynamicLanguage(this, binding.txtAccountName, "AccountName", R.string.AccountName);
        setDynamicLanguage(this, binding.txtAccountNumber, "AccountNumber", R.string.AccountNumber);
        setDynamicLanguage(this, binding.txtIFSCCode, "IFSCCode", R.string.IFSCCode);

        setDynamicLanguage(this, binding.txtFarmName, "FarmNames", R.string.FarmNames);
        setDynamicLanguage(this, binding.txtStateName, "State", R.string.State);
        setDynamicLanguage(this, binding.txtDistrictName, "District", R.string.District);
        setDynamicLanguage(this, binding.txtBlockName, "Block", R.string.Block);
        setDynamicLanguage(this, binding.txtVillageName, "Village", R.string.Village);
        setDynamicLanguage(this, binding.txtCropName, "Crops", R.string.Crops);
        setDynamicLanguage(this, binding.txtVariety, "Varieties", R.string.Varieties);
        setDynamicLanguage(this, binding.txtFarmAreaFarm, "FarmArea", R.string.FarmArea);
        setDynamicLanguage(this, binding.txtPlantingTranplantingDate, "PlantingTranplantingDate", R.string.PlantingTranplantingDate);
        setDynamicLanguage(this, binding.txtFarmAreaFarm, "FarmArea", R.string.FarmArea);
        setDynamicLanguage(this, binding.txtAadharCardPhoto, "AadharCardPhoto", R.string.AadharCardPhoto);
        setDynamicLanguage(this, binding.txtLandDocumentPhoto, "LandDocumentPhoto", R.string.LandDocumentPhoto);


        setDynamicLanguage(this, binding.txtPolicyStartDate, "PolicyStartDate", R.string.PolicyStartDate);
        setDynamicLanguage(this, binding.txtPolicyCloseDate, "PolicyCloseDate", R.string.PolicyCloseDate);
        setDynamicLanguage(this, binding.txtFarmArea, "FarmArea", R.string.FarmArea);
        setDynamicLanguage(this, binding.txtBenchmarkYield, "BenchmarkYields", R.string.BenchmarkYields);
        setDynamicLanguage(this, binding.txtValueAssured, "ValueAssured", R.string.ValueAssured);
        setDynamicLanguage(this, binding.txtFee, "Fee", R.string.Fee);
        setDynamicLanguage(this, binding.txtExpectedHarvestingDate, "ExpectedHarvestingDate", R.string.ExpectedHarvestingDate);

    }


}
