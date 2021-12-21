package com.weather.risk.mfi.myfarminfo.activities;

import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.SelectedLanguageValue;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setDynamicLanguage;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setFontsStyleTxt;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.database.DBAdapter;
import com.weather.risk.mfi.myfarminfo.databinding.PopLanguageselectionBinding;
import com.weather.risk.mfi.myfarminfo.utils.AppConstant;
import com.weather.risk.mfi.myfarminfo.utils.AppManager;

import java.util.ArrayList;
import java.util.HashMap;


public class POPUpLanguageSelection extends Activity {

    PopLanguageselectionBinding binding;
    SharedPreferences prefs = null, prefs_once = null;
    //    String selectedlanguages = "english";/
    String ActivityName = "";
    DBAdapter db;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        setContentView(R.layout.pop_languageselection);
        binding = DataBindingUtil.setContentView(this, R.layout.pop_languageselection);

        db = new DBAdapter(this);

        prefs = getSharedPreferences(AppConstant.SHARED_PREFRENCE_NAME, MODE_PRIVATE);

        binding.btnCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        String language = AppManager.getInstance().getSelectedLanguages(this);
        try {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                ActivityName = bundle.getString("ActivityName");
            }
            switch (language) {
                case "english":
                    binding.rbEnglish.setChecked(true);
                    binding.rbHindi.setChecked(false);
                    binding.rbGujarati.setChecked(false);
                    binding.rbMarathi.setChecked(false);
                    binding.rbBengali.setChecked(false);
                    binding.rbTamil.setChecked(false);
                    break;
                case "hindi":
                    binding.rbEnglish.setChecked(false);
                    binding.rbHindi.setChecked(true);
                    binding.rbGujarati.setChecked(false);
                    binding.rbMarathi.setChecked(false);
                    binding.rbBengali.setChecked(false);
                    binding.rbTamil.setChecked(false);
                    break;
                case "gujarati":
                    binding.rbEnglish.setChecked(false);
                    binding.rbHindi.setChecked(false);
                    binding.rbGujarati.setChecked(true);
                    binding.rbMarathi.setChecked(false);
                    binding.rbBengali.setChecked(false);
                    binding.rbTamil.setChecked(false);
                    break;
                case "marathi":
                    binding.rbEnglish.setChecked(false);
                    binding.rbHindi.setChecked(false);
                    binding.rbGujarati.setChecked(false);
                    binding.rbMarathi.setChecked(true);
                    binding.rbBengali.setChecked(false);
                    binding.rbTamil.setChecked(false);
                    break;
                case "bengali":
                    binding.rbEnglish.setChecked(false);
                    binding.rbHindi.setChecked(false);
                    binding.rbGujarati.setChecked(false);
                    binding.rbMarathi.setChecked(false);
                    binding.rbBengali.setChecked(true);
                    binding.rbTamil.setChecked(false);
                    break;
                case "telugu":
                    binding.rbEnglish.setChecked(false);
                    binding.rbHindi.setChecked(false);
                    binding.rbGujarati.setChecked(false);
                    binding.rbMarathi.setChecked(false);
                    binding.rbBengali.setChecked(false);
                    binding.rbTamil.setChecked(true);
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        binding.radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_English) {
                    setRadioButtonClick("english");
                } else if (checkedId == R.id.rb_Hindi) {
                    setRadioButtonClick("hindi");
                } else if (checkedId == R.id.rb_Marathi) {
                    setRadioButtonClick("marathi");
                } else if (checkedId == R.id.rb_Gujarati) {
                    setRadioButtonClick("gujarati");
                } else if (checkedId == R.id.rb_Bengali) {
                    setRadioButtonClick("bengali");
                } else if (checkedId == R.id.rb_Tamil) {
                    setRadioButtonClick("tamil");
                }
            }
        });


    }

    private void setRadioButtonClick(final String flags) {
//        selectedlanguages = flags;
        switch (flags) {
            case "hindi":
                setLanguages(1);
                break;
            case "english":
                setLanguages(2);
                break;
            case "gujarati":
                setLanguages(3);
                break;
            case "marathi":
                setLanguages(4);
                break;
            case "bengali":
                setLanguages(5);
                break;
            case "tamil":
                setLanguages(6);
                break;
        }
    }

    public void setLanguages(int flag) {
//        SharedPreferences myPreference = getSharedPreferences(AppConstant.SHARED_PREFRENCE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor ed = prefs.edit();

        prefs_once = getSharedPreferences(AppConstant.SHARED_PREFRENCE_ONCE, MODE_PRIVATE);




        String SQL = "Select MyKey,English from tblLocalTranslation where English !='' order by MyKey";
        switch (flag) {
            case 1://hindi
                ed.putString(getResources().getString(R.string.language_pref_key), "2");
                prefs_once.edit().putString("langkey","2").commit();
                SQL = "Select MyKey,Hindi from tblLocalTranslation where Hindi !='' order by MyKey";
                break;
            case 2://english
                ed.putString(getResources().getString(R.string.language_pref_key), "1");
                prefs_once.edit().putString("langkey","1").commit();
                SQL = "Select MyKey,English from tblLocalTranslation where English !='' order by MyKey";
                break;
            case 3://gujarati
                ed.putString(getResources().getString(R.string.language_pref_key), "3");
                prefs_once.edit().putString("langkey","3").commit();
                SQL = "Select MyKey,Gujarati from tblLocalTranslation where Gujarati !='' order by MyKey";
                break;
            case 4://marathi
                ed.putString(getResources().getString(R.string.language_pref_key), "4");
                prefs_once.edit().putString("langkey","4").commit();
                SQL = "Select MyKey,Marathi from tblLocalTranslation where Marathi !='' order by MyKey";
                break;
            case 5://bengali
                ed.putString(getResources().getString(R.string.language_pref_key), "5");
                prefs_once.edit().putString("langkey","5").commit();
                SQL = "Select MyKey,Bengali from tblLocalTranslation where Bengali !='' order by MyKey";
                break;
            case 6://tamil
                ed.putString(getResources().getString(R.string.language_pref_key), "6");
                prefs_once.edit().putString("langkey","6").commit();
                SQL = "Select MyKey,Telugu from tblLocalTranslation where Telugu !='' order by MyKey";
                break;
        }
        ed.apply();
        getSelectedLanguages(SQL);
        AppManager.getInstance().setLanguages(flag, POPUpLanguageSelection.this);
        finish();
        if (ActivityName != null && ActivityName.length() > 0) {
            if (ActivityName.equalsIgnoreCase("MainProfileActivity")) {
                Intent in = new Intent(POPUpLanguageSelection.this, MainProfileActivity.class);
                startActivity(in);
            } else if (ActivityName.equalsIgnoreCase("AdminDashboard_New")) {
                Intent in = new Intent(POPUpLanguageSelection.this, AdminDashboard_New.class);
                startActivity(in);
            }
        }

    }


    public void getSelectedLanguages(String SQL) {
        try {
            db.open();
            SelectedLanguageValue = new HashMap<>();
            SelectedLanguageValue = db.getDynamicTableKeyValue(SQL);
            String valu = "";
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        // code here to show dialog
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();

        setFontsStyleTxt(this, binding.title, 2);
        //Tab Service
        setDynamicLanguage(this,  binding.title, "ResetyourLanguage", R.string.ResetyourLanguage);
    }

}
