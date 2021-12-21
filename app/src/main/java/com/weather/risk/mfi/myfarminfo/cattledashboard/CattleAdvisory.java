package com.weather.risk.mfi.myfarminfo.cattledashboard;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.database.DBAdapter;
import com.weather.risk.mfi.myfarminfo.databinding.CattleadvisoryBinding;

import java.util.ArrayList;

import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.SN_CattleAdvisory;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getCropName;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getUIDforScreenTracking;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setScreenTracking;

public class CattleAdvisory extends AppCompatActivity {

    DBAdapter db;
    String UID = "";
    CattleadvisoryBinding binding;
    String cropName = null;
    ArrayList<CattlePOP> cattlepop_array;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.cattleadvisory);
        //        setFonts();
        db = new DBAdapter(this);
        setCropName();
        setAdapter();

        binding.backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //ScreenTracking
        UID = getUIDforScreenTracking();
        setScreenTracking(CattleAdvisory.this, db, SN_CattleAdvisory, UID);
    }

    public void setAdapter() {
        try {
            cattlepop_array = new ArrayList<>();
            String[] popStage = {getResources().getString(R.string.DAS_10), getResources().getString(R.string.DAS_0),
                    getResources().getString(R.string.DAS_010),
                    getResources().getString(R.string.DAS_1120), getResources().getString(R.string.DAS_2150),
                    getResources().getString(R.string.DAS_5080), getResources().getString(R.string.DAS_80130),
                    getResources().getString(R.string.DAS_130200), getResources().getString(R.string.DAS_200290),
                    getResources().getString(R.string.DAS_290320), getResources().getString(R.string.DAS_320380)};
            String[] popDescription = {getResources().getString(R.string.DAS_10_Dets), getResources().getString(R.string.DAS_0_Dets),
                     getResources().getString(R.string.DAS_010_Dets),
                    getResources().getString(R.string.DAS_1120_Dets), getResources().getString(R.string.DAS_2150_Dets),
                    getResources().getString(R.string.DAS_5080_Dets), getResources().getString(R.string.DAS_80130_Dets),
                    getResources().getString(R.string.DAS_130200_Dets), getResources().getString(R.string.DAS_200290_Dets),
                    getResources().getString(R.string.DAS_290320_Dets), getResources().getString(R.string.DAS_320380_Dets)};

            for (int i = 0; i < 11; i++) {
                CattlePOP pop = new CattlePOP();
                pop.setStage(popStage[i]);
                pop.setDescription(popDescription[i]);
                cattlepop_array.add(pop);
            }
            LinearLayoutManager llm = new LinearLayoutManager(this);
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            binding.recyclerPackage.setLayoutManager(llm);
            CattleAdvisoryAdapter adapter = new CattleAdvisoryAdapter(getApplicationContext(), cattlepop_array, "SimpleFeed");
            binding.recyclerPackage.setAdapter(adapter);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setCropName() {
        String CropName = getCropName(this, cropName);
        if (CropName != null && CropName.length() > 0) {
            binding.txtStages.setText(CropName);
        } else {
            binding.txtStages.setText("");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (db != null) {
            db = new DBAdapter(this);
        }
        Log.d("OnPause Method", "OnPause Method called");
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (db != null) {
            db = new DBAdapter(this);
        }
        Log.d("onStop Method", "onStop Method called");
        setScreenTracking(this, db, SN_CattleAdvisory, UID);
    }
}