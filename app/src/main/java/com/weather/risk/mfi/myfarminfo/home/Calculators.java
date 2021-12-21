package com.weather.risk.mfi.myfarminfo.home;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.database.DBAdapter;
import com.weather.risk.mfi.myfarminfo.firebasenotification.NotificationCountSMS;
import com.weather.risk.mfi.myfarminfo.utils.AppConstant;

import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.SN_Calculators;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getUIDforScreenTracking;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setScreenTracking;

public class Calculators extends AppCompatActivity {

    //NPKtoKg
    EditText nET, nKg, nBag;
    EditText pET, pKg, pBag;
    EditText kET, kKg, kBag;

    //KgtoNPK
    EditText uET, uKg, uBag;
    EditText sET, sKg, sBag;
    EditText mET, mKg, mBag;


    String UID = "";
    DBAdapter db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculators);

        db = new DBAdapter(this);

        nET = (EditText) findViewById(R.id.n_id);
        nKg = (EditText) findViewById(R.id.n_kg);
        nBag = (EditText) findViewById(R.id.n_bag);

        pET = (EditText) findViewById(R.id.p_id);
        pKg = (EditText) findViewById(R.id.p_kg);
        pBag = (EditText) findViewById(R.id.p_bag);

        kET = (EditText) findViewById(R.id.k_id);
        kKg = (EditText) findViewById(R.id.k_kg);
        kBag = (EditText) findViewById(R.id.k_bag);


        uET = (EditText) findViewById(R.id.u_id);
        uKg = (EditText) findViewById(R.id.u_kg);
        uBag = (EditText) findViewById(R.id.u_bag);

        sET = (EditText) findViewById(R.id.s_id);
        sKg = (EditText) findViewById(R.id.s_kg);
        sBag = (EditText) findViewById(R.id.s_bag);

        mET = (EditText) findViewById(R.id.m_id);
        mKg = (EditText) findViewById(R.id.m_kg);
        mBag = (EditText) findViewById(R.id.m_bag);


        nET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s != null && s.length() > 0) {
                    String nValue = s.toString();


                    Double aa = Double.parseDouble(nValue);

                    Double kg = aa * 2.17;
                    Double bg = aa * 0.043;

                    nKg.setText("" + String.format("%.2f", kg));
                    nBag.setText("" + String.format("%.2f", bg));

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && s.length() > 0) {


                }
            }
        });

        pET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s != null && s.length() > 0) {
                    String nValue = s.toString();


                    Double aa = Double.parseDouble(nValue);

                    Double kg = aa * 6.25;
                    Double bg = aa * 0.125;

                    pKg.setText("" + String.format("%.2f", kg));
                    pBag.setText("" + String.format("%.2f", bg));

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && s.length() > 0) {


                }
            }
        });

        kET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s != null && s.length() > 0) {
                    String nValue = s.toString();


                    Double aa = Double.parseDouble(nValue);

                    Double kg = aa * 1.67;
                    Double bg = aa * 0.033;

                    kKg.setText("" + String.format("%.2f", kg));
                    kBag.setText("" + String.format("%.2f", bg));

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && s.length() > 0) {


                }
            }
        });


        uET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s != null && s.length() > 0) {
                    String nValue = s.toString();


                    Double aa = Double.parseDouble(nValue);

                    Double kg = aa * 0.460829;
                    Double bg = aa * 0.019815;


                    uKg.setText("" + String.format("%.2f", kg));
                    uBag.setText("" + String.format("%.2f", bg));

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && s.length() > 0) {


                }
            }
        });

        sET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s != null && s.length() > 0) {
                    String nValue = s.toString();


                    Double aa = Double.parseDouble(nValue);

                    Double kg = aa * 0.16;
                    Double bg = aa * 0.02;

                    sKg.setText("" + String.format("%.2f", kg));
                    sBag.setText("" + String.format("%.2f", bg));

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && s.length() > 0) {


                }
            }
        });

        mET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s != null && s.length() > 0) {
                    String nValue = s.toString();


                    Double aa = Double.parseDouble(nValue);

                    Double kg = aa * 0.5988023;
                    Double bg = aa * 0.0197604;

                    mKg.setText("" + String.format("%.2f", kg));
                    mBag.setText("" + String.format("%.2f", bg));

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && s.length() > 0) {


                }
            }
        });

        //ScreenTracking
        UID = getUIDforScreenTracking();
        setScreenTracking(this, db, SN_Calculators, UID);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (db != null) {
            db = new DBAdapter(this);
        }
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
}
