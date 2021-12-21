package com.weather.risk.mfi.myfarminfo.policyregistration;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.database.DBAdapter;
import com.weather.risk.mfi.myfarminfo.databinding.PolicyfarmlistBinding;
import com.weather.risk.mfi.myfarminfo.utils.AppConstant;

import androidx.lifecycle.ViewModelProviders;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.userfarmList;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setDynamicLanguage;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setFontsStyleTxt;
import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.userfarmListforpolicyfarmlist;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.sortAscFarmArrayList;

public class PolicyFarmList extends AppCompatActivity {

    PolicyfarmlistBinding binding;
    DBAdapter db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.policyfarmlist);

        db = new DBAdapter(this);
//        getAllFarmName();
        try {
            if (userfarmList != null && userfarmList.size() > 0) {

                userfarmListforpolicyfarmlist.clear();
                userfarmListforpolicyfarmlist.addAll(userfarmList);


                sortAscFarmArrayList();


                binding.recyclerViewFarmlist.setHasFixedSize(true);
                PolicyFarmListAdapter adapter = new PolicyFarmListAdapter(PolicyFarmList.this, userfarmListforpolicyfarmlist,binding.search);
                binding.recyclerViewFarmlist.setLayoutManager(new LinearLayoutManager(PolicyFarmList.this, LinearLayoutManager.VERTICAL, false));
                binding.recyclerViewFarmlist.setItemAnimator(new DefaultItemAnimator());
                binding.recyclerViewFarmlist.setAdapter(adapter);


//                binding.recyclerViewFarmlist.notifyAll();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        binding.backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.d("onStart Method", "MainActivity onStart Method is calling");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("onResume Method", "MainActivity onResume Method is calling");
        setLanguages();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("onRestart Method", "MainActivity onRestart Method is calling");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("onPause Method", "MainActivity onPause Method is calling");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("onStop Method", "MainActivity onStop Method is calling");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("onDestroy Method", "MainActivity onDestroy Method is calling");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d("onBackPressed Method", "MainActivity onBackPressed Method is calling");
    }

    public void setLanguages() {
        //Heading
        setFontsStyleTxt(this, binding.txtFarmRegistration, 2);

        setDynamicLanguage(this, binding.txtFarmRegistration, "SelectFarm", R.string.SelectFarm);
    }
}
