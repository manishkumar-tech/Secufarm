package com.weather.risk.mfi.myfarminfo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.firebasenotification.NotificationCountSMS;
import com.weather.risk.mfi.myfarminfo.utils.AppConstant;
import com.weather.risk.mfi.myfarminfo.utils.UtilFonts;

public class SplashScreen extends AppCompatActivity {


    Animation zoomin, zoomout;
    //    ImageView imgview_logo;
//    ImageView imgview_logo;
    TableRow tbl_logo;
    TextView txt_myfarm, txt_info;
    Bundle bundle = null;

    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);

//        imgview_logo = (ImageView) findViewById(R.id.imgview_logo);
        tbl_logo = (TableRow) findViewById(R.id.tbl_logo);

        txt_myfarm = (TextView) findViewById(R.id.txt_myfarm);
        txt_info = (TextView) findViewById(R.id.txt_info);

        UtilFonts.UtilFontsInitialize(this);
        txt_myfarm.setTypeface(UtilFonts.FS_UltraItalic);
        txt_info.setTypeface(UtilFonts.KT_Bold);

        zoomin = AnimationUtils.loadAnimation(this, R.anim.zoomin);
        zoomout = AnimationUtils.loadAnimation(this, R.anim.zoomout);
        tbl_logo.setAnimation(zoomin);
//        splashscreenBinding.imgviewLogo.setAnimation(zoomout);
//        Thread t = new Thread(new Zoom());
//        t.start();

        bundle = getIntent().getExtras();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // This method will be executed once the timer is over
                Intent in = new Intent(SplashScreen.this, NewHomeScreen.class);
                if (bundle != null) {
                    in.putExtras(bundle);
                }
                startActivity(in);
                finish();
            }
        }, 3000);

    }


    private class Zoom implements Runnable {
        @Override
        public void run() {
            while (true) {
                tbl_logo.startAnimation(zoomin);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                tbl_logo.startAnimation(zoomout);
            }
        }
    }


}
