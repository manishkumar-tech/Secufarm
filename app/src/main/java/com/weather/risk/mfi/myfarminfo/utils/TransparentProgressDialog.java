package com.weather.risk.mfi.myfarminfo.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.weather.risk.mfi.myfarminfo.R;

public class TransparentProgressDialog extends Dialog {

    private ImageView iv;
    TextView tv;

    public TransparentProgressDialog(Context context,
                                     String text) {
        super(context, R.style.TransparentProgressDialog);
        try {
            Window window = getWindow();
            window.requestFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog);
            setCancelable(false);
            setOnCancelListener(null);
            iv = (ImageView) findViewById(R.id.progrress);
            tv = (TextView) findViewById(R.id.textdown);
            tv.setText(text);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void show() {
        super.show();
        try {
            RotateAnimation anim = new RotateAnimation(0.0f, 360.0f,
                    Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF,
                    .5f);
            anim.setInterpolator(new LinearInterpolator());
            anim.setRepeatCount(Animation.INFINITE);
            anim.setDuration(3000);
            iv.setAnimation(anim);
            iv.startAnimation(anim);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}