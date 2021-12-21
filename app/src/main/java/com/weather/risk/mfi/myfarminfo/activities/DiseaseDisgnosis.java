package com.weather.risk.mfi.myfarminfo.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;
import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.adapter.DiseaseDiagnosisAdapter;
import com.weather.risk.mfi.myfarminfo.adapter.DiseaseDiagnosisImageAdapter;
import com.weather.risk.mfi.myfarminfo.database.DBAdapter;
import com.weather.risk.mfi.myfarminfo.utils.AppConstant;
import com.weather.risk.mfi.myfarminfo.utils.AppManager;
import com.weather.risk.mfi.myfarminfo.utils.TransparentProgressDialog;
import com.weather.risk.mfi.myfarminfo.utils.UtilFonts;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.Addcallnew_DiseaseDiagnosis;
import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.SN_DiseaseDiagnosis;
import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.itemSelectedCotton_Disease;
import static com.weather.risk.mfi.myfarminfo.utils.AppManager.GetRecommendationURL;
import static com.weather.risk.mfi.myfarminfo.utils.AppManager.GetSymptomsDignosisURL;
import static com.weather.risk.mfi.myfarminfo.utils.AppManager.getFindDiagnosis;
import static com.weather.risk.mfi.myfarminfo.utils.AppManager.getSowdateConvertddmmYYYY;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getArrayforSpinnerBind;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getUIDforScreenTracking;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setDynamicLanguage;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setFontsStyle;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setFontsStyleTxt;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setScreenTracking;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setToastSMSShow;
import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.itemStateArray_Disease;
import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.itemStateArray_Disease_setPosition;


public class DiseaseDisgnosis extends AppCompatActivity {

    String UID = "";
    DBAdapter db;
    ArrayList<HashMap<String, String>> crops = new ArrayList<>();
    ArrayList<HashMap<String, String>> sownOn = new ArrayList<>();
    ArrayList<HashMap<String, String>> prepActivity = new ArrayList<>();
    ArrayList<HashMap<String, String>> questionType = new ArrayList<>();
    ArrayList<HashMap<String, String>> seed = new ArrayList<>();
    ArrayList<HashMap<String, String>> intercrop = new ArrayList<>();
    ArrayList<HashMap<String, String>> diagnosis = new ArrayList<>();
    ArrayList<HashMap<String, String>> diagnosis_default = new ArrayList<>();

    Spinner spin_Crop, spin_SownOn, spin_prepa_activity, spin_Questiontype, spin_Seed, spin_InterCrop;
    TextView txt_tilte, txt_Crop, txt_SownOn, txt_SownOndetails, txt_Preparatoryactivites, txt_Questiontype,
            txt_Seed, txt_InterCrop;
    LinearLayout ll_seed, ll_InterCrop, tblrow_foreward, ll_Fertilization;
    EditText txt_SeedResistant, txt_InterCropDetails, edt_recommendation;
    RadioGroup radioDose;
    RadioButton Dose1, Dose2, Dose3, Dose4;
    CheckBox checkbox_recommen;
    ImageView foreward_btn;

    int CropSelectID = 0, QuestiontypeSelectID = 0, Addcallnew_CropId = 0, SownOnSelectID = 0,
            PreActSelectID = 0, SeedSelectID = 0, InterCropSelectID = 0, diagnosisSelectID = 0;
    String RadioDose = "", RadioDoseType = "", Biofertilizers = "", IsBiofertilizers = "";

    String jsonvalue_DiseaseGrid = null, jsonvalue_DiseaseDiagnosis = null,
            jsonvalue_DiseaseDiagnosisImageList = null;

//    private var addNewCall_DiseaseAdapter: AddNewCall_DiseaseAdapter? = null
//    private var addNewCall_DiseaseImageAdapter: AddNewCall_DiseaseImageAdapter? = null

    RecyclerView recyview_disease, recyview_imagedisease;
    Spinner spin_diagnosis;
    String SowingDate = null;
    ImageView back_button;
    RelativeLayout rl_diagnosis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diseasedisgnosis);

        db = new DBAdapter(this);
        setIdDefine();
//        dialogbox = new Dialog(this);
        setCrops();
        spinbindSownOn();
        spinbindpreActivity();
        spinbindQuestiontype();
        spinbindSeed();
        spinbindInterCrop();
        QuestiontypeSelectID = 4;

        SowingDate = getIntent().getStringExtra("SowDate");
        if (SowingDate != null) {
            SowingDate = getSowdateConvertddmmYYYY(SowingDate);
        }

        setspinListioner(spin_Crop, "Crop");
        setspinListioner(spin_SownOn, "SownOn");
        setspinListioner(spin_prepa_activity, "PrepAct");
//        setspinListioner(spin_Questiontype, "Questiontype")
        setspinListioner(spin_Seed, "Seed");
        setspinListioner(spin_InterCrop, "InterCrop");

        radioDose.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (R.id.Dose1 == checkedId) {
                    RadioDose = getString(R.string.Dose1SMS);
                    RadioDoseType = "Dose1";
                } else if (R.id.Dose2 == checkedId) {
                    RadioDose = getString(R.string.Dose2SMS);
                    RadioDoseType = "Dose2";
                } else if (R.id.Dose3 == checkedId) {
                    RadioDose = getString(R.string.Dose3SMS);
                    RadioDoseType = "Dose3";
                } else if (R.id.Dose4 == checkedId) {
                    RadioDose = getString(R.string.Dose4SMS);
                    RadioDoseType = "Dose4";
                } else {
                    RadioDose = "";
                    RadioDoseType = "";
                }
                setFertilizerText(RadioDose, Biofertilizers);
            }
        });

        checkbox_recommen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    Biofertilizers = getString(R.string.TriclodermaViridaeSMS);
                    IsBiofertilizers = "true";
                } else {
                    Biofertilizers = "";
                    IsBiofertilizers = "false";
                }
                setFertilizerText(RadioDose, Biofertilizers);
            }
        });

        foreward_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CropSelectID != 0) {
                    new getSymptomsDignosis().execute();
                }
            }
        });
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        UID = getUIDforScreenTracking();
        setScreenTracking(this, db, SN_DiseaseDiagnosis, UID);
    }

    public void setCrops() {
        HashMap<String, String> hash = new HashMap<>();
        hash.put("CropID", "0");
        hash.put("CropName", "Select");
        crops.add(hash);

        if (checkcropName("355", "Chilli")) {
            hash = new HashMap();
            hash.put("CropID", "355");
            hash.put("CropName", "Chilli");
            crops.add(hash);
        }
        if (checkcropName("12", "Cotton")) {
            hash = new HashMap();
            hash.put("CropID", "12");
            hash.put("CropName", "Cotton");
            crops.add(hash);
        }
        if (checkcropName("29", "Groundnut")) {
            hash = new HashMap();
            hash.put("CropID", "29");
            hash.put("CropName", "Groundnut");
            crops.add(hash);
        }
        if (checkcropName("5", "Maize")) {
            hash = new HashMap();
            hash.put("CropID", "5");
            hash.put("CropName", "Maize");
            crops.add(hash);
        }
        if (checkcropName("354", "Paddy")) {
            hash = new HashMap();
            hash.put("CropID", "354");
            hash.put("CropName", "Paddy");
            crops.add(hash);
        }
        if (checkcropName("32", "Potato")) {
            hash = new HashMap();
            hash.put("CropID", "32");
            hash.put("CropName", "Potato");
            crops.add(hash);
        }
        if (checkcropName("8", "Rice")) {
            hash = new HashMap();
            hash.put("CropID", "8");
            hash.put("CropName", "Rice");
            crops.add(hash);
        }
        if (checkcropName("67", "Soyabean")) {
            hash = new HashMap();
            hash.put("CropID", "67");
            hash.put("CropName", "Soyabean");
            crops.add(hash);
        }
        if (checkcropName("314", "Vegetables")) {
            hash = new HashMap();
            hash.put("CropID", "314");
            hash.put("CropName", "Vegetables");
            crops.add(hash);
        }
        if (crops.size() == 1) {
            hash = new HashMap();
            hash.put("CropID", "12");
            hash.put("CropName", "Cotton");
            crops.add(hash);
        }
        getArrayforSpinnerBind(DiseaseDisgnosis.this, spin_Crop, crops, "CropName");
        if (crops.size() == 2) {
            spin_Crop.setSelection(1);
        }
    }

    public boolean checkcropName(String cropId, String CropName) {
        if (AppConstant.selected_cropId != null && AppConstant.selected_cropId.length() > 0) {
            if (AppConstant.selected_cropId.equalsIgnoreCase(cropId)) {
                return true;
            }
        }
        if (AppConstant.selected_crop != null && AppConstant.selected_crop.length() > 0) {
            if (AppConstant.selected_crop.equalsIgnoreCase(CropName)) {
                return true;
            }
        }
        return false;

    }

    public void spinbindSownOn() {
        HashMap<String, String> hash = new HashMap<>();
        hash.put("SownOnID", "0");
        hash.put("SownOnName", "Select");
        sownOn.add(hash);
        hash = new HashMap();
        hash.put("SownOnID", "5-7");
        hash.put("SownOnName", "Early May");
        sownOn.add(hash);
        hash = new HashMap();
        hash.put("SownOnID", "5-23");
        hash.put("SownOnName", "Late May");
        sownOn.add(hash);
        hash = new HashMap();
        hash.put("SownOnID", "6-7");
        hash.put("SownOnName", "Early Jun");
        sownOn.add(hash);
        hash = new HashMap();
        hash.put("SownOnID", "6-23");
        hash.put("SownOnName", "Late June");
        sownOn.add(hash);
        getArrayforSpinnerBind(DiseaseDisgnosis.this, spin_SownOn, sownOn, "SownOnName");
    }

    public void spinbindpreActivity() {
        HashMap<String, String> hash = new HashMap<>();
        hash.put("PreActID", "0");
        hash.put("PreActName", "Select");
        prepActivity.add(hash);
        hash = new HashMap();
        hash.put("PreActID", "1");
        hash.put("PreActName", "Tillage");
        prepActivity.add(hash);
        hash = new HashMap();
        hash.put("PreActID", "2");
        hash.put("PreActName", "Rotavator");
        prepActivity.add(hash);
        hash = new HashMap();
        hash.put("PreActID", "3");
        hash.put("PreActName", "Spacing");
        prepActivity.add(hash);
        getArrayforSpinnerBind(DiseaseDisgnosis.this, spin_prepa_activity, prepActivity, "PreActName");
    }

    public void spinbindQuestiontype() {
        HashMap<String, String> hash = new HashMap<>();
        hash.put("QuestypeID", "0");
        hash.put("QuestypeName", "Select");
        questionType.add(hash);
        hash = new HashMap();
        hash.put("QuestypeID", "1");
        hash.put("QuestypeName", "Seed");
        questionType.add(hash);
        hash = new HashMap();
        hash.put("QuestypeID", "2");
        hash.put("QuestypeName", "InterCrop");
        questionType.add(hash);
        hash = new HashMap();
        hash.put("QuestypeID", "3");
        hash.put("QuestypeName", "Fertilization");
        questionType.add(hash);
        hash = new HashMap();
        hash.put("QuestypeID", "4");
        hash.put("QuestypeName", "Disease");
        questionType.add(hash);
        hash = new HashMap();
        hash.put("QuestypeID", "5");
        hash.put("QuestypeName", "Other");
        questionType.add(hash);
        getArrayforSpinnerBind(DiseaseDisgnosis.this, spin_Questiontype, questionType, "QuestypeName");

        if ((CropSelectID == 12 || CropSelectID == 5) && QuestiontypeSelectID == 4) {
            ll_seed.setVisibility(View.GONE);
            ll_InterCrop.setVisibility(View.GONE);
            tblrow_foreward.setVisibility(View.GONE);
            ll_Fertilization.setVisibility(View.GONE);
        }
    }

    public void spinbindSeed() {
        HashMap<String, String> hash = new HashMap<>();
        hash.put("SeedID", "0");
        hash.put("SeedName", "Select");
        seed.add(hash);
        hash = new HashMap();
        hash.put("SeedID", "1");
        hash.put("SeedName", "Tiger (Ganga kaveri)");
        seed.add(hash);
        hash = new HashMap();
        hash.put("SeedID", "2");
        hash.put("SeedName", "Kanak (Mahico 7351)");
        seed.add(hash);
        hash = new HashMap();
        hash.put("SeedID", "3");
        hash.put("SeedName", "Jaadhu");
        seed.add(hash);
        getArrayforSpinnerBind(DiseaseDisgnosis.this, spin_Seed, seed, "SeedName");
    }

    public void spinbindInterCrop() {
        HashMap<String, String> hash = new HashMap<>();
        hash.put("InterCropID", "0");
        hash.put("InterCropName", "Select");
        intercrop.add(hash);
        hash = new HashMap();
        hash.put("InterCropID", "1");
        hash.put("InterCropName", "Red Gram");
        intercrop.add(hash);
        hash = new HashMap();
        hash.put("InterCropID", "2");
        hash.put("InterCropName", "Green Gram");
        intercrop.add(hash);
        hash = new HashMap();
        hash.put("InterCropID", "3");
        hash.put("InterCropName", "Soyabean");
        intercrop.add(hash);
        hash = new HashMap();
        hash.put("InterCropID", "4");
        hash.put("InterCropName", "Coriander");
        intercrop.add(hash);
        getArrayforSpinnerBind(DiseaseDisgnosis.this, spin_InterCrop, intercrop, "InterCropName");
    }

    public void setspinListioner(final Spinner spin, final String flag) {
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (flag) {
                    case "Crop":
//                        CropSelectID = position
                        spin_SownOn.setSelection(0);
                        spin_prepa_activity.setSelection(0);
//                        spin_Questiontype.setSelection(0)
                        spin_Seed.setSelection(0);
                        ll_seed.setVisibility(View.GONE);
                        ll_InterCrop.setVisibility(View.GONE);
//                        tblrow_foreward.setVisibility(View.GONE);
                        ll_Fertilization.setVisibility(View.GONE);

                        HashMap<String, String> crop = new HashMap();
                        crop = crops.get(position);
                        CropSelectID = Integer.parseInt(crop.get("CropID").toString());
                        Addcallnew_CropId = CropSelectID;
                        if (position == 0 || CropSelectID == 314 || CropSelectID == 354) {
                            tblrow_foreward.setVisibility(View.GONE);
                        } else {
                            tblrow_foreward.setVisibility(View.VISIBLE);
                        }
                        break;
                    case "SownOn":
                        if (CropSelectID > 0) {
                            SownOnSelectID = position;
                            switch (position) {
                                case 0:
                                    txt_SownOndetails.setVisibility(View.GONE);
                                    txt_SownOndetails.setText("");
                                    break;
                                case 1:
                                    txt_SownOndetails.setVisibility(View.VISIBLE);
                                    if (CropSelectID == 12) {
                                        txt_SownOndetails.setText(getResources().getString(R.string.SownOnEarlyMay_Cotton));
                                    } else if (CropSelectID == 5) {
                                        txt_SownOndetails.setText(getResources().getString(R.string.SownOnEarlyMay_Maize));
                                    } else if (CropSelectID == 8) {
                                        txt_SownOndetails.setText(getResources().getString(R.string.SownOnEarlyMay_Rice));
                                    } else {
                                        txt_SownOndetails.setText("");
                                        txt_SownOndetails.setVisibility(View.GONE);
                                    }
                                    break;
                                case 2:
                                    txt_SownOndetails.setVisibility(View.VISIBLE);
                                    if (CropSelectID == 12) {
                                        txt_SownOndetails.setText(getResources().getString(R.string.SownOnLateMay_Cotton));
                                    } else if (CropSelectID == 5) {
                                        txt_SownOndetails.setText(getResources().getString(R.string.SownOnLateMay_Maize));
                                    } else if (CropSelectID == 8) {
                                        txt_SownOndetails.setText(getResources().getString(R.string.SownOnLateMay_Rice));
                                    } else {
                                        txt_SownOndetails.setText("");
                                        txt_SownOndetails.setVisibility(View.GONE);
                                    }
                                    break;
                                case 3:
                                    txt_SownOndetails.setVisibility(View.VISIBLE);
                                    if (CropSelectID == 12) {
                                        txt_SownOndetails.setText(getResources().getString(R.string.SownOnEarlyJune_Cotton));
                                    } else if (CropSelectID == 5) {
                                        txt_SownOndetails.setText(getResources().getString(R.string.SownOnEarlyJune_Maize));
                                    } else if (CropSelectID == 8) {
                                        txt_SownOndetails.setText(getResources().getString(R.string.SownOnEarlyJune_Rice));
                                    } else {
                                        txt_SownOndetails.setText("");
                                        txt_SownOndetails.setVisibility(View.GONE);
                                    }
                                    break;
                                case 4:
                                    txt_SownOndetails.setVisibility(View.VISIBLE);
                                    if (CropSelectID == 12) {
                                        txt_SownOndetails.setText(getResources().getString(R.string.SownOnLateJune_Cotton));
                                    } else if (CropSelectID == 5) {
                                        txt_SownOndetails.setText(getResources().getString(R.string.SownOnLateJune_Maize));
                                    } else if (CropSelectID == 8) {
                                        txt_SownOndetails.setText(getResources().getString(R.string.SownOnLateJune_Rice));
                                    } else {
                                        txt_SownOndetails.setText("");
                                        txt_SownOndetails.setVisibility(View.GONE);
                                    }
                                    break;
                            }

                        } else if (position > 0) {
                            spin.setSelection(0);
                            txt_SownOndetails.setVisibility(View.GONE);
                            setToastSMSShow(1, DiseaseDisgnosis.this, getResources().getString(R.string.select_crops));
                        }
                        break;
                    case "PrepAct":
                        if (CropSelectID > 0) {
                            PreActSelectID = position;
                        } else if (position > 0) {
                            spin.setSelection(0);
                            setToastSMSShow(1, DiseaseDisgnosis.this, getResources().getString(R.string.select_crops));
                        }
                        break;
                    case "Questiontype":
                        if (CropSelectID > 0) {
                            QuestiontypeSelectID = position;
                            if (CropSelectID == 12 && QuestiontypeSelectID == 1) {
                                ll_seed.setVisibility(View.VISIBLE);
                                ll_InterCrop.setVisibility(View.GONE);
                                tblrow_foreward.setVisibility(View.GONE);
                                ll_Fertilization.setVisibility(View.GONE);
                            } else if (CropSelectID == 12 && QuestiontypeSelectID == 2) {
                                ll_seed.setVisibility(View.GONE);
                                ll_InterCrop.setVisibility(View.VISIBLE);
                                tblrow_foreward.setVisibility(View.GONE);
                                ll_Fertilization.setVisibility(View.GONE);
                            } else if (CropSelectID == 12 && QuestiontypeSelectID == 3) {
                                ll_seed.setVisibility(View.GONE);
                                ll_InterCrop.setVisibility(View.GONE);
                                tblrow_foreward.setVisibility(View.GONE);
                                ll_Fertilization.setVisibility(View.VISIBLE);
                            } else if ((CropSelectID == 12 || CropSelectID == 5) && QuestiontypeSelectID == 4) {
                                ll_seed.setVisibility(View.GONE);
                                ll_InterCrop.setVisibility(View.GONE);
                                tblrow_foreward.setVisibility(View.VISIBLE);
                                ll_Fertilization.setVisibility(View.GONE);
                            } else {
                                ll_seed.setVisibility(View.GONE);
                                ll_InterCrop.setVisibility(View.GONE);
                                tblrow_foreward.setVisibility(View.GONE);
                                ll_Fertilization.setVisibility(View.GONE);
                            }
                        } else if (position > 0) {
                            spin.setSelection(0);
                            setToastSMSShow(1, DiseaseDisgnosis.this, getResources().getString(R.string.select_crops));
                        }
                        break;
                    case "Seed":
                        SeedSelectID = position;
                        if (position > 0) {
                            if (CropSelectID == 12 && QuestiontypeSelectID == 1 && position == 1) {
                                txt_SeedResistant.setText(getResources().getString(R.string.SeedRequirement));
//                                txt_SeedResistant.visibility = View.VISIBLE
                            } else {
                                txt_SeedResistant.setText("");
//                                txt_SeedResistant.visibility = View.GONE
                            }
                        }
                        break;
                    case "InterCrop":
                        InterCropSelectID = position;
                        if (position > 0) {
                            if (CropSelectID == 12 && QuestiontypeSelectID == 2) {
//                                txt_InterCropDetails.visibility = View.VISIBLE
                                if (position == 1) {
                                    txt_InterCropDetails.setText(getResources().getString(R.string.Intercrop_Red));
                                } else if (position == 2) {
                                    txt_InterCropDetails.setText(getResources().getString(R.string.Intercrop_Greed));
                                } else if (position == 3) {
                                    txt_InterCropDetails.setText(getResources().getString(R.string.Intercrop_Soyabeen));
                                } else if (position == 4) {
                                    txt_InterCropDetails.setText(getResources().getString(R.string.Intercrop_Cariander));
                                }
                            } else {
                                txt_InterCropDetails.setText("");
//                                txt_InterCropDetails.visibility = View.GONE
                            }
                        }
                        break;

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void setFertilizerText(String choseDose, String biofertilization) {
        String value = "";
        if ((choseDose == null || choseDose.equals("")) &&
                (biofertilization == null || biofertilization.equals(""))) {
            value = "";
        } else if (choseDose.length() > 0 && biofertilization.length() > 0) {
            value = choseDose + " , " + biofertilization;
        } else if (choseDose.length() > 0) {
            value = choseDose;
        } else if (biofertilization.length() > 0) {
            value = biofertilization;
        }
        edt_recommendation.setText(value);
    }

    public void setIdDefine() {
        txt_tilte = (TextView) findViewById(R.id.txt_tilte);
        txt_Crop = (TextView) findViewById(R.id.txt_Crop);
        spin_Crop = (Spinner) findViewById(R.id.spin_Crop);
        txt_SownOn = (TextView) findViewById(R.id.txt_SownOn);
        txt_SownOndetails = (TextView) findViewById(R.id.txt_SownOndetails);
        spin_SownOn = (Spinner) findViewById(R.id.spin_SownOn);
        txt_Preparatoryactivites = (TextView) findViewById(R.id.txt_Preparatoryactivites);
        spin_prepa_activity = (Spinner) findViewById(R.id.spin_prepa_activity);
        txt_Questiontype = (TextView) findViewById(R.id.txt_Questiontype);
        spin_Questiontype = (Spinner) findViewById(R.id.spin_Questiontype);
        txt_Seed = (TextView) findViewById(R.id.txt_Seed);
        spin_Seed = (Spinner) findViewById(R.id.spin_Seed);
        txt_InterCrop = (TextView) findViewById(R.id.txt_InterCrop);
        spin_InterCrop = (Spinner) findViewById(R.id.spin_InterCrop);

        txt_SeedResistant = (EditText) findViewById(R.id.txt_SeedResistant);
        txt_InterCropDetails = (EditText) findViewById(R.id.txt_InterCropDetails);
        edt_recommendation = (EditText) findViewById(R.id.edt_recommendation);

        ll_seed = (LinearLayout) findViewById(R.id.ll_seed);
        ll_InterCrop = (LinearLayout) findViewById(R.id.ll_InterCrop);
        tblrow_foreward = (LinearLayout) findViewById(R.id.tblrow_foreward);
        ll_Fertilization = (LinearLayout) findViewById(R.id.ll_Fertilization);

        checkbox_recommen = (CheckBox) findViewById(R.id.checkbox_recommen);

        radioDose = (RadioGroup) findViewById(R.id.radioDose);
        Dose1 = (RadioButton) findViewById(R.id.Dose1);
        Dose2 = (RadioButton) findViewById(R.id.Dose2);
        Dose3 = (RadioButton) findViewById(R.id.Dose3);
        Dose4 = (RadioButton) findViewById(R.id.Dose4);

        foreward_btn = (ImageView) findViewById(R.id.foreward_btn);
        back_button = (ImageView) findViewById(R.id.back_button);

    }



    public void setLanguages() {
        setDynamicLanguage(this, txt_tilte, "DiseaseDiagnosis", R.string.DiseaseDiagnosis);
        setDynamicLanguage(this, txt_Crop, "Crop", R.string.Crop);
        setDynamicLanguage(this, txt_SownOn, "SownOn", R.string.SownOn);
        setDynamicLanguage(this, txt_Preparatoryactivites, "Preparatoryactivites", R.string.Preparatoryactivites);
        setDynamicLanguage(this, txt_Questiontype, "Questiontype", R.string.Questiontype);
        setDynamicLanguage(this, txt_Seed, "Seed", R.string.Seed);
        setDynamicLanguage(this, txt_InterCrop, "InterCrop", R.string.InterCrop);

        setDynamicLanguage(this, Dose1, "Dose1", R.string.Dose1);
        setDynamicLanguage(this, Dose2, "Dose2", R.string.Dose2);
        setDynamicLanguage(this, Dose3, "Dose3", R.string.Dose3);
        setDynamicLanguage(this, Dose4, "Dose4", R.string.Dose4);

        setFontsStyleTxt(this, txt_tilte, 2);
        setFontsStyleTxt(this, txt_Crop, 5);
        setFontsStyleTxt(this, txt_SownOn, 5);
        setFontsStyleTxt(this, txt_SownOndetails, 5);
        setFontsStyleTxt(this, txt_Preparatoryactivites, 5);
        setFontsStyleTxt(this, txt_Questiontype, 5);
        setFontsStyleTxt(this, txt_Seed, 5);
        setFontsStyleTxt(this, txt_InterCrop, 5);
        setFontsStyleTxt(this, txt_SeedResistant, 6);
        setFontsStyleTxt(this, txt_InterCropDetails, 6);
        setFontsStyle(this, edt_recommendation);
        setFontsStyle(this, Dose1);
        setFontsStyle(this, Dose2);
        setFontsStyle(this, Dose3);
        setFontsStyle(this, Dose4);

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (db != null) {
            db = new DBAdapter(this);
        }
        Log.d("OnPause Method", "OnPause Method called");
        setScreenTracking(this, db, SN_DiseaseDiagnosis, UID);
        setLanguages();
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (db != null) {
            db = new DBAdapter(this);
        }
        Log.d("OnPause Method", "OnPause Method called");
        setScreenTracking(this, db, SN_DiseaseDiagnosis, UID);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (db != null) {
            db = new DBAdapter(this);
        }
        Log.d("onStop Method", "onStop Method called");
        setScreenTracking(this, db, SN_DiseaseDiagnosis, UID);
    }

    private class getSymptomsDignosis extends AsyncTask<Void, Void, String> {
        String result = "";
        TransparentProgressDialog progressDialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new TransparentProgressDialog(
                    DiseaseDisgnosis.this, getResources().getString(R.string.Dataisloading));
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
            String sendRequest = null;
            try {
                sendRequest = GetSymptomsDignosisURL(String.valueOf(CropSelectID));
                Log.d("get farm url", sendRequest);
                String response = AppManager.getInstance().httpRequestGetMethod(sendRequest);
                response = response.replace("\\", "");
                response = response.replace("\"{", "{");
                response = response.replace("}\"", "}");

                return response;

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null; //show network problem
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPreExecute();
            String CropID = "", CropName = "", Variety = "";
            if (response == null || response.equals("[]") || response.equals("[[],[]]")) {
                setToastSMSShow(1, DiseaseDisgnosis.this, getResources().getString(R.string.Nodataavailable));
            } else {
                //condition should be check here
                setDiseaseJsonBind(response);
            }
            progressDialog.dismiss();
        }
    }

    public void setDiseaseJsonBind(String json) {
        try {
            JSONArray jsonarry = new JSONArray(json);
            jsonvalue_DiseaseGrid = jsonarry.get(0).toString();
            jsonvalue_DiseaseDiagnosis = jsonarry.get(1).toString();
            try {
                diagnosis = new ArrayList();
                JSONArray jsonarray = new JSONArray(jsonvalue_DiseaseDiagnosis);
                HashMap<String, String> hash = new HashMap();//get Name will return
                hash.put("Id", "0");
                hash.put("Name", "Select");
                diagnosis.add(hash);
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject obj = jsonarray.getJSONObject(i);
                    hash = new HashMap();
                    hash.put("Id", obj.getString("Id"));
                    hash.put("Name", obj.getString("Name"));
                    diagnosis.add(hash);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } //Disease pop up call
            diagnosis_default = new ArrayList<>();
            diagnosis_default = diagnosis;
            onPopUp("");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void onPopUp(String json) {
        if (QuestiontypeSelectID > 0) {
            switch (QuestiontypeSelectID) {
                case 0:
                    break;
                case 4://Disease
                    popupDisease();
                    break;
            }
        } else {
            setToastSMSShow(1, DiseaseDisgnosis.this, getResources().getString(R.string.selectquestiontype));
        }
    }

    public void popupDisease() {
        try {
            itemSelectedCotton_Disease = "";
            final Dialog dialogbox = new Dialog(this, R.style.DialogSlideAnim);
            dialogbox.setCanceledOnTouchOutside(true);
            dialogbox.setCancelable(true);
            Window window = dialogbox.getWindow();
            dialogbox.requestWindowFeature(Window.FEATURE_NO_TITLE);

            WindowManager.LayoutParams wlp = window.getAttributes();

            wlp.gravity = Gravity.CENTER;
            wlp.dimAmount = 1f;
            dialogbox.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            // wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            window.setAttributes(wlp);

            // Include dialog.xml file
            dialogbox.setContentView(R.layout.popup_diseasediagnosis_disease);

            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT);

            ImageView back_btn = (ImageView) dialogbox.findViewById(R.id.back_btn);
            recyview_disease = (RecyclerView) dialogbox.findViewById(R.id.recyview_disease);
            recyview_imagedisease = (RecyclerView) dialogbox.findViewById(R.id.recyview_imagedisease);
            TextView txt_Croptitle = (TextView) dialogbox.findViewById(R.id.txt_Croptitle);
            TextView txt_Disease = (TextView) dialogbox.findViewById(R.id.txt_Disease);
            ImageView save_btn = (ImageView) dialogbox.findViewById(R.id.save_btn);
            TextView txt_Diagnosis = (TextView) dialogbox.findViewById(R.id.txt_Diagnosis);
            Button btn_Submit = (Button) dialogbox.findViewById(R.id.btn_Submit);
            spin_diagnosis = (Spinner) dialogbox.findViewById(R.id.spin_diagnosis);
            TextView txt_Recommendationdiagnosis = (TextView) dialogbox.findViewById(R.id.txt_Recommendationdiagnosis);
            ImageView imgview_save = (ImageView) dialogbox.findViewById(R.id.imgview_save);
            final TextView txt_Recommendation = (TextView) dialogbox.findViewById(R.id.txt_Recommendation);
            final LinearLayout ll_imagePopup = (LinearLayout) dialogbox.findViewById(R.id.ll_imagePopup);
            final TextView txt_Imagetitle = (TextView) dialogbox.findViewById(R.id.txt_Imagetitle);
            ImageView imageviewcancel = (ImageView) dialogbox.findViewById(R.id.imageviewcancel);
            final ImageView imgeviewshow = (ImageView) dialogbox.findViewById(R.id.imgeviewshow);
            rl_diagnosis = (RelativeLayout) dialogbox.findViewById(R.id.rl_diagnosis);

            recyview_disease.setVisibility(View.GONE);
            recyview_imagedisease.setVisibility(View.GONE);
            rl_diagnosis.setVisibility(View.GONE);

            imageviewcancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ll_imagePopup.setVisibility(View.GONE);
//                    dialogbox.cancel();
                }
            });
            back_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    ll_imagePopup.setVisibility(View.GONE);
                    dialogbox.cancel();
                }
            });
            btn_Submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemSelectedCotton_Disease != null && itemSelectedCotton_Disease.length() > 0) {
                        new getrebindDiseaseDiagnosiSpinner().execute();
                    } else {
                        rl_diagnosis.setVisibility(View.GONE);
                        setToastSMSShow(2, DiseaseDisgnosis.this, getResources().getString(R.string.PleaseselectSymptoms));
                    }
                }
            });

            JSONArray jsonArray = new JSONArray(jsonvalue_DiseaseGrid);
            if (jsonArray.length() > 0) {
                itemStateArray_Disease = new SparseBooleanArray();
                itemStateArray_Disease_setPosition = new SparseBooleanArray();
                recyview_disease.setVisibility(View.VISIBLE);
                recyview_disease.setHasFixedSize(true);
                LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyview_disease.setLayoutManager(mLayoutManager);
                DiseaseDiagnosisAdapter adapter = new DiseaseDiagnosisAdapter(getApplicationContext(), jsonArray);

//                //Scrolldown Position
//                Parcelable recyclerViewState;
//                recyclerViewState = recyview_disease.getLayoutManager().onSaveInstanceState();
////                // Restore state
//                recyview_disease.getLayoutManager().onRestoreInstanceState(recyclerViewState);
                adapter.notifyDataSetChanged();
//                adapter.notifyItemRangeChanged(0, adapter.getItemCount());

                recyview_disease.setAdapter(adapter);
            }

            getArrayforSpinnerBind(DiseaseDisgnosis.this, spin_diagnosis, diagnosis, "Name");

            spin_diagnosis.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    diagnosisSelectID = position;
                    if (position > 0) {
                        recyview_imagedisease.setVisibility(View.VISIBLE);
                        HashMap<String, String> disgno = new HashMap();
                        disgno = diagnosis.get(position);
                        String diagnosisId = disgno.get("Id");
//                            var diagnosisName = disgno.get("Name")
                        diagnosisSelectID = Integer.parseInt(diagnosisId.toString());
                        Addcallnew_DiseaseDiagnosis = diagnosisId;
                        new getImagesDiagnosis(recyview_imagedisease, txt_Recommendation, ll_imagePopup, imgeviewshow, txt_Imagetitle, Addcallnew_DiseaseDiagnosis).execute();
                    } else {
                        recyview_imagedisease.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            dialogbox.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private class getImagesDiagnosis extends AsyncTask<Void, Void, String> {
        String DiagnosisId = "";
        TransparentProgressDialog progressDialog;
        RecyclerView rcyView;
        TextView edt_Recommendation;
        LinearLayout ll_imagepop;
        ImageView imgview;
        TextView txt_Imagetilte;

        getImagesDiagnosis(RecyclerView recyclerView, TextView editText, LinearLayout linearLayout,
                           ImageView imageView, TextView textView, String diagnosisId) {
            rcyView = recyclerView;
            edt_Recommendation = editText;
            ll_imagepop = linearLayout;
            imgview = imageView;
            txt_Imagetilte = textView;
            DiagnosisId = diagnosisId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new TransparentProgressDialog(
                    DiseaseDisgnosis.this, getResources().getString(R.string.Dataisloading));
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
            String sendRequest = null;
            try {
                sendRequest = GetRecommendationURL(String.valueOf(DiagnosisId));
                Log.d("get farm url", sendRequest);
                String response = AppManager.getInstance().httpRequestGetMethod(sendRequest);
//                response = response.replace("\\", "");
//                response = response.replace("\"{", "{");
//                response = response.replace("}\"", "}");
                response = response.replace("\\", "");
                response = response.replace("\"{", "{");
                response = response.replace("}\"", "}");
                response = response.replace("\"[", "[");
                response = response.replace("]\"", "]");

                return response;

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null; //show network problem
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPreExecute();
            progressDialog.dismiss();
            JSONObject obj = new JSONObject();
            String Recommendation = "";
            if (response == null || response.equals("[]") || response.equals("[[],[]]")) {
                setToastSMSShow(1, DiseaseDisgnosis.this, getResources().getString(R.string.Nodataavailable));
            } else {
                //condition should be check here
                JSONArray imagefile = new JSONArray();
                try {
                    JSONArray array = new JSONArray(response);
                    if (array.length() > 0) {
                        obj = array.getJSONObject(0);
                        Recommendation = obj.getString("Recommendation");
                        jsonvalue_DiseaseDiagnosisImageList = obj.getString("FileName");
                        imagefile = new JSONArray(jsonvalue_DiseaseDiagnosisImageList);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                edt_Recommendation.setText(Recommendation);
                if (imagefile.length() > 0) {
                    LinearLayoutManager layoutManager = new LinearLayoutManager(
                            DiseaseDisgnosis.this, LinearLayoutManager.HORIZONTAL, false);
                    recyview_imagedisease.setVisibility(View.VISIBLE);
                    recyview_imagedisease.setHasFixedSize(true);
                    recyview_imagedisease.setLayoutManager(layoutManager);
                    DiseaseDiagnosisImageAdapter adapter = new DiseaseDiagnosisImageAdapter(getApplicationContext(), imagefile, ll_imagepop, imgview, txt_Imagetilte, DiagnosisId, DiseaseDisgnosis.this);
                    recyview_imagedisease.setAdapter(adapter);
                }

            }
        }
    }

    public static void ImagePopup(LinearLayout ll_imagePopups, ImageView imgeviewshows, TextView txt_Imagetitle,
                                  String ImageTitle, String imageurl, Activity acttivity) {
        try {
            ll_imagePopups.setVisibility(View.VISIBLE);
            txt_Imagetitle.setText(ImageTitle);
            Picasso.with(acttivity).load(imageurl).into(imgeviewshows);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public JsonObject onRefreshDiagnosisDisease() {
        JsonObject json = new JsonObject();
        json.addProperty("CropId", String.valueOf(CropSelectID));
        json.addProperty("CropDays", String.valueOf(SowingDate));
//        if (itemSelectedCotton_Disease != null) {
//            itemSelectedCotton_Disease = getremoveDuplicacy(itemSelectedCotton_Disease);
//        }
        json.addProperty("SymptomsIds", "[" + itemSelectedCotton_Disease + "]");
        return json;
    }


    private class getrebindDiseaseDiagnosiSpinner extends AsyncTask<Void, Void, String> {
        TransparentProgressDialog progressDialog;

        @Override

        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new TransparentProgressDialog(
                    DiseaseDisgnosis.this, getResources().getString(R.string.Dataisloading));
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
            String sendRequest = null;
            try {
                String url = getFindDiagnosis;
                String Json = String.valueOf(onRefreshDiagnosisDisease());
//                sendRequest = AppManager.getInstance().httpRequestPutMethod(url, Json);
                sendRequest = AppManager.getInstance().httpRequestPutMethodReport(url, Json);
                sendRequest = sendRequest.replace("\\", "");
                sendRequest = sendRequest.replace("\"{", "{");
                sendRequest = sendRequest.replace("}\"", "}");
                sendRequest = sendRequest.replace("\"[", "[");
                sendRequest = sendRequest.replace("]\"", "]");
                return sendRequest;

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null; //show network problem
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPreExecute();
            progressDialog.dismiss();
            JSONObject obj = new JSONObject();
            String Recommendation = "";
            if (response == null || response.equals("[]") || response.equals("[[],[]]")) {
                rebinddefaultDiagnosis();
            } else {
                //condition should be check here
                try {
                    JSONObject ob = new JSONObject(response);
                    JSONArray jsonarray = ob.getJSONArray("FindDiagnosisResult");
                    try {
                        if (jsonarray.length() > 0) {
                            diagnosis = new ArrayList();
                            HashMap<String, String> hash = new HashMap<String, String>();//get Name will return
                            hash.put("Id", "0");
                            hash.put("Name", "Select");
                            diagnosis.add(hash);
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject obj1 = jsonarray.getJSONObject(i);
                                hash = new HashMap();
                                hash.put("Id", obj1.getString("Id"));
                                hash.put("Name", obj1.getString("Name"));
                                diagnosis.add(hash);
                            }
                            rl_diagnosis.setVisibility(View.VISIBLE);
                            getArrayforSpinnerBind(DiseaseDisgnosis.this, spin_diagnosis, diagnosis, "Name");
                            if (diagnosis.size() > 1) {
                                spin_diagnosis.setSelection(1);
                            }
                        } else {
                            rebinddefaultDiagnosis();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        rebinddefaultDiagnosis();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    rebinddefaultDiagnosis();
                }
            }
        }
    }

    public void rebinddefaultDiagnosis() {
        rl_diagnosis.setVisibility(View.GONE);
        setToastSMSShow(2, DiseaseDisgnosis.this, getResources().getString(R.string.Thesesymptomsarenotmatched));
        if (diagnosis_default != null) {
            diagnosis = diagnosis_default;
            getArrayforSpinnerBind(DiseaseDisgnosis.this, spin_diagnosis, diagnosis, "Name");
        }
    }


}
