package com.weather.risk.mfi.myfarminfo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.database.DBAdapter;
import com.weather.risk.mfi.myfarminfo.databinding.FertilizercalculatorBinding;
import com.weather.risk.mfi.myfarminfo.utils.AppConstant;
import com.weather.risk.mfi.myfarminfo.utils.UtilFonts;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.SN_AdminDashboard;
import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.SN_Calculators;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getDynamicLanguageValue;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getStringCheck;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getUIDforScreenTracking;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setDynamicLanguage;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setFontsStyleTxt;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setScreenTracking;

public class FertilizerCalculator extends AppCompatActivity {

    FertilizercalculatorBinding binding;
    DBAdapter db;
    String UreaSPPMOPDAP_Selected = "UreaSPPMOP";
    int ValueN = 0, ValueP = 0, ValueK = 0;
    float FarmSize = 0.0f;
    String UID = "", CropName = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.fertilizercalculator);
        db = new DBAdapter(this);

        binding.llUreaSPPMOP.setVisibility(View.VISIBLE);
        binding.llDAPMOPUrea.setVisibility(View.GONE);
        binding.txtUreaSSPMOP.setVisibility(View.VISIBLE);
        binding.txtDAPMOPUrea.setVisibility(View.GONE);

        CropName = AppConstant.selected_crop;
        if (CropName != null && CropName.length() > 0) {
            binding.txtCroptitle.setText(CropName);
        } else {
            binding.txtCroptitle.setText("");
        }
        getFarmAreaDetails();
        binding.radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_UreaSPPMOP) {
                    binding.llUreaSPPMOP.setVisibility(View.VISIBLE);
                    binding.llDAPMOPUrea.setVisibility(View.GONE);
                    binding.txtUreaSSPMOP.setVisibility(View.VISIBLE);
                    binding.txtDAPMOPUrea.setVisibility(View.GONE);
                    UreaSPPMOPDAP_Selected = "UreaSPPMOP";
                    calculationValue("UreaSPPMOP");
                } else if (checkedId == R.id.rb_DAPMOPUrea) {
                    binding.llUreaSPPMOP.setVisibility(View.GONE);
                    binding.llDAPMOPUrea.setVisibility(View.VISIBLE);
                    binding.txtUreaSSPMOP.setVisibility(View.GONE);
                    binding.txtDAPMOPUrea.setVisibility(View.VISIBLE);
                    UreaSPPMOPDAP_Selected = "DAPMOPUrea";
                    calculationValue("DAPMOPUrea");
                }
            }
        });
        calculationValue("UreaSPPMOP");
        binding.imgvwBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.txtPlotSizeValue.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
//                if (s.length() > 0) {
//                    calculationValue("UreaSPPMOP");
//                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                try {
                    if (s.length() > 0) {
                        FarmSize = Float.parseFloat(s.toString());
                    } else {
                        FarmSize = 0.0f;
                    }
                    calculationValue("UreaSPPMOP");

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        //ScreenTracking
        UID = getUIDforScreenTracking();
        setScreenTracking(this, db, SN_Calculators, UID);
    }

    public void getFarmAreaDetails() {
        try {
            db.open();
            ArrayList<HashMap<String, String>> hasmap = new ArrayList<>();
            hasmap = db.getDynamicTableValue("Select * from " + db.TABLE_QUERY_CROP + " where farmId='" + AppConstant.farm_id + "'");
            if (hasmap.size() > 0) {
                ValueN = Integer.parseInt(getStringCheck(hasmap.get(0).get("valueN")));
                ValueP = Integer.parseInt(getStringCheck(hasmap.get(0).get("valueP")));
                ValueK = Integer.parseInt(getStringCheck(hasmap.get(0).get("valueK")));
            }

            hasmap = new ArrayList<>();
            hasmap = db.getDynamicTableValue("Select * from " + db.DATABASE_TABLE_ALL_FARM_DETAIL + " where farmId='" + AppConstant.farm_id + "'");
            if (hasmap.size() > 0) {
                String farmArea = hasmap.get(0).get("area");
                if (farmArea != null && farmArea.length() > 0 && !farmArea.equalsIgnoreCase("null")) {
                    FarmSize = Float.parseFloat(farmArea);
                }
            }
            db.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
//        binding.txtPlotSizeValue.setText(String.valueOf(FarmSize));
        binding.txtPlotSizeValue.setText("" + String.format("%.3f", FarmSize));
    }

    private void calculationValue(String flag) {
        try {
            if (flag.equalsIgnoreCase("UreaSPPMOP")) {
                if (ValueN == 0) {
                    ValueN = 120;//Normal Standard Value
                }
                if (ValueP == 0) {
                    ValueP = 60;//Normal Standard Value
                }
                if (ValueK == 0) {
                    ValueK = 60;//Normal Standard Value
                }
                //For N,P,K in 100 kg of Urea,SSP,MOP
                float N_1kgN_in100KgUrea = 100.0f / 46.0f;
                float P_1kgP_in100KgSSP = 100.0f / 16.0f;
                float K_1kgK_in100KgMOP = 100.0f / 58.0f;

                //User Required  1 Hector=2.47105 Acre  USM-Urea SSP MOP
                float N_UserRequired_USM = (float) ((float) (ValueN * N_1kgN_in100KgUrea) / 2.47105f) * (float) FarmSize;
                float P_UserRequired_USM = (float) ((float) (ValueP * P_1kgP_in100KgSSP) / 2.47105f) * (float) FarmSize;
                float K_UserRequired_USM = (float) ((float) (ValueK * K_1kgK_in100KgMOP) / 2.47105f) * (float) FarmSize;

                DecimalFormat df = new DecimalFormat("#.##");
                df.setRoundingMode(RoundingMode.CEILING);

                binding.txtUreaSPPMOPUreakg.setText("" + String.format(df.format(N_UserRequired_USM)) + getKGUnit());
                binding.txtUreaSPPMOPSSPkg.setText("" + String.format(df.format(P_UserRequired_USM)) + getKGUnit());
                binding.txtUreaSPPMOPMOPkg.setText("" + String.format(df.format(K_UserRequired_USM)) + getKGUnit());

                //No of Urea (45kg per bag size), SSP (50kg per bag size), MOP Bags (50kg per bag size)
                float N_NoofBags_USM = (float) N_UserRequired_USM / 45.0f;
                float P_NoofBags_USM = (float) P_UserRequired_USM / 50.0f;
                float K_NoofBags_USM = (float) K_UserRequired_USM / 50.0f;


                binding.txtUreaSPPMOPUreaBags.setText("" + String.format(df.format(N_NoofBags_USM)) + getBagsUnit());
                binding.txtUreaSPPMOPSSPBags.setText("" + String.format(df.format(P_NoofBags_USM)) + getBagsUnit());
                binding.txtUreaSPPMOPMOPBags.setText("" + String.format(df.format(K_NoofBags_USM)) + getBagsUnit());

            } else if (flag.equalsIgnoreCase("DAPMOPUrea")) {
                if (ValueN == 0) {
                    ValueN = 120;//Normal Standard Value
                }
                if (ValueP == 0) {
                    ValueP = 60;//Normal Standard Value
                }
                if (ValueK == 0) {
                    ValueK = 60;//Normal Standard Value
                }
                //For N,P,K in 100 kg of Urea,DAP,MOP
                float N_1kgN_in100KgUrea = 100.0f / (46.0f + 18.0f);//In DAP, N and P value have both N=18,P=46 remove N from Urea
                float P_1kgP_in100KgDAP = 100.0f / 46.0f;
                float K_1kgK_in100KgMOP = 100.0f / 58.0f;

                //User Required  1 Hector=2.47105 Acre  USM-Urea DAP MOP
                float N_UserRequired_UDM = (float) ((float) (ValueN * N_1kgN_in100KgUrea) / 2.47105f) * (float) FarmSize;
                float P_UserRequired_UDM = (float) ((float) (ValueP * P_1kgP_in100KgDAP) / 2.47105f) * (float) FarmSize;
                float K_UserRequired_UDM = (float) ((float) (ValueK * K_1kgK_in100KgMOP) / 2.47105f) * (float) FarmSize;

                DecimalFormat df = new DecimalFormat("#.##");
                df.setRoundingMode(RoundingMode.CEILING);

                binding.txtDAPMOPUreaUreakg.setText("" + String.format(df.format(N_UserRequired_UDM)) + getKGUnit());
                binding.txtDAPMOPUreaDAPkg.setText("" + String.format(df.format(P_UserRequired_UDM)) + getKGUnit());
                binding.txtDAPMOPUreaMOPkg.setText("" + String.format(df.format(K_UserRequired_UDM)) + getKGUnit());

                //No of Urea (45kg per bag size), DAP (50kg per bag size), MOP Bags (50kg per bag size)
                float N_NoofBags_UDM = (float) N_UserRequired_UDM / 45.0f;
                float P_NoofBags_UDM = (float) P_UserRequired_UDM / 50.0f;
                float K_NoofBags_UDM = (float) K_UserRequired_UDM / 50.0f;


                binding.txtDAPMOPUreaUreaBags.setText("" + String.format(df.format(N_NoofBags_UDM)) + getBagsUnit());
                binding.txtDAPMOPUreaDAPBags.setText("" + String.format(df.format(P_NoofBags_UDM)) + getBagsUnit());
                binding.txtDAPMOPUreaMOPBags.setText("" + String.format(df.format(K_NoofBags_UDM)) + getBagsUnit());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    public void setLanguages() {
        setDynamicLanguage(this, binding.txtTitle, "Calculator", R.string.Calculator);
        setDynamicLanguage(this, binding.txtCalculators, "Calculators", R.string.Calculators);
        setDynamicLanguage(this, binding.txtPlotSizeValueUnit, "acre", R.string.acre);
        setDynamicLanguage(this, binding.rbUreaSPPMOP, "UreaSPPMOP", R.string.UreaSPPMOP);
        setDynamicLanguage(this, binding.rbDAPMOPUrea, "DAPMOPUrea", R.string.DAPMOPUrea);

        setDynamicLanguage(this, binding.txtUreaSPPMOPUrea, "Urea", R.string.Urea);
        setDynamicLanguage(this, binding.txtUreaSPPMOPSSP, "SSP", R.string.SSP);
        setDynamicLanguage(this, binding.txtUreaSPPMOPMOP, "MOP", R.string.MOP);

        setDynamicLanguage(this, binding.txtDAPMOPUreaDAP, "DAP", R.string.DAP);
        setDynamicLanguage(this, binding.txtDAPMOPUreaMOP, "MOP", R.string.MOP);
        setDynamicLanguage(this, binding.txtDAPMOPUreaUrea, "Urea", R.string.Urea);

        setDynamicLanguage(this, binding.txtNumberofkgbag, "Numberofkgbag", R.string.Numberofkgbag);
        setDynamicLanguage(this, binding.txtUreaSSPMOP, "UreaSSPMOP", R.string.UreaSSPMOP);
        setDynamicLanguage(this, binding.txtDAPMOPUrea, "DAPMOPUreas", R.string.DAPMOPUreas);

        setFontsStyleTxt(this, binding.txtTitle, 2);
        setFontsStyleTxt(this, binding.txtCroptitle, 2);
        setFontsStyleTxt(this, binding.txtPlotSize, 5);
        setFontsStyleTxt(this, binding.txtCalculators, 5);
        setFontsStyleTxt(this, binding.txtPlotSizeValue, 2);
        setFontsStyleTxt(this, binding.txtPlotSizeValueUnit, 7);
        setFontsStyleTxt(this, binding.rbUreaSPPMOP, 5);
        setFontsStyleTxt(this, binding.rbDAPMOPUrea, 5);

        setFontsStyleTxt(this, binding.txtUreaSPPMOPUrea, 5);
        setFontsStyleTxt(this, binding.txtUreaSPPMOPSSP, 5);
        setFontsStyleTxt(this, binding.txtUreaSPPMOPMOP, 5);
        setFontsStyleTxt(this, binding.txtUreaSPPMOPUreakg, 7);
        setFontsStyleTxt(this, binding.txtUreaSPPMOPSSPkg, 7);
        setFontsStyleTxt(this, binding.txtUreaSPPMOPMOPkg, 7);
        setFontsStyleTxt(this, binding.txtUreaSPPMOPUreaBags, 7);
        setFontsStyleTxt(this, binding.txtUreaSPPMOPSSPBags, 7);
        setFontsStyleTxt(this, binding.txtUreaSPPMOPMOPBags, 7);

        setFontsStyleTxt(this, binding.txtDAPMOPUreaDAP, 5);
        setFontsStyleTxt(this, binding.txtDAPMOPUreaMOP, 5);
        setFontsStyleTxt(this, binding.txtDAPMOPUreaUrea, 5);
        setFontsStyleTxt(this, binding.txtDAPMOPUreaDAPkg, 7);
        setFontsStyleTxt(this, binding.txtDAPMOPUreaMOPkg, 7);
        setFontsStyleTxt(this, binding.txtDAPMOPUreaUreakg, 7);
        setFontsStyleTxt(this, binding.txtDAPMOPUreaDAPBags, 7);
        setFontsStyleTxt(this, binding.txtDAPMOPUreaMOPBags, 7);
        setFontsStyleTxt(this, binding.txtDAPMOPUreaUreaBags, 7);

        setFontsStyleTxt(this, binding.txtNumberofkgbag, 7);
        setFontsStyleTxt(this, binding.txtUreaSSPMOP, 7);
        setFontsStyleTxt(this, binding.txtDAPMOPUrea, 7);

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (db != null) {
            db = new DBAdapter(this);
        }
        setLanguages();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (db != null) {
            db = new DBAdapter(this);
        }
        Log.d("OnPause Method", "OnPause Method called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (db != null) {
            db = new DBAdapter(this);
        }
        Log.d("onStop Method", "onStop Method called");
        setScreenTracking(this, db, SN_Calculators, UID);
    }

    public String getKGUnit() {
        String KG = "";
        KG = " " + getDynamicLanguageValue(getApplicationContext(), "kg", R.string.kg);
        return KG;
    }

    public String getBagsUnit() {
        String Bags = "";
        Bags = " " + getDynamicLanguageValue(getApplicationContext(), "Bags", R.string.Bags);
        return Bags;
    }

}
