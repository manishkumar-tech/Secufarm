package com.weather.risk.mfi.myfarminfo.activities;

import static com.weather.risk.mfi.myfarminfo.utils.Utility.setDynamicLanguage;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setFontsStyleTxt;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.tubewell.VodafoneFragment;
import com.weather.risk.mfi.myfarminfo.utils.AppConstant;
import com.weather.risk.mfi.myfarminfo.utils.UtilFonts;

public class TubewellMainActivity extends AppCompatActivity {


    TextView title;
    RelativeLayout backBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tubewellmainactivity);

        backBTN = (RelativeLayout) findViewById(R.id.backbtn);
        title = (TextView) findViewById(R.id.logo);


        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if (AppConstant.isLogin) {
            Fragment fragment = VodafoneFragment.newInstance();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
        } else {
            Toast.makeText(this, "Login Required", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setLanguages();
    }

    public void setLanguages() {
        setFontsStyleTxt(this, title, 2);
        setDynamicLanguage(this, title, "Tubewell", R.string.Tubewell);

    }
}
