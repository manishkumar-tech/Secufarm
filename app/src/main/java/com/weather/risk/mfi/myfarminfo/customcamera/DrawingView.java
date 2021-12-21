package com.weather.risk.mfi.myfarminfo.customcamera;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Camera;
import android.view.View;

public class DrawingView extends View {

    boolean haveFace;
    Paint drawingPaint;
    Camera.Face[] detectedFaces;


    public DrawingView(Context context) {
        super(context);
        haveFace = false;
        drawingPaint = new Paint();
        drawingPaint.setColor(Color.GREEN);
        drawingPaint.setStyle(Paint.Style.STROKE);
        drawingPaint.setStrokeWidth(2);
    }

    public void setHaveFace(boolean h) {
        haveFace = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        try {
            if (haveFace) {

                // Camera driver coordinates range from (-1000, -1000) to (1000, 1000).
                // UI coordinates range from (0, 0) to (width, height).

                int vWidth = getWidth();
                int vHeight = getHeight();

                for (int i = 0; i < detectedFaces.length; i++) {

                    if (i == 0) {
                        drawingPaint.setColor(Color.GREEN);
                    } else {
                        drawingPaint.setColor(Color.RED);
                    }

                    int l = detectedFaces[i].rect.left;
                    int t = detectedFaces[i].rect.top;
                    int r = detectedFaces[i].rect.right;
                    int b = detectedFaces[i].rect.bottom;
                    int left = (l + 1000) * vWidth / 2000;
                    int top = (t + 1000) * vHeight / 2000;
                    int right = (r + 1000) * vWidth / 2000;
                    int bottom = (b + 1000) * vHeight / 2000;
                    canvas.drawRect(left, top, right, bottom, drawingPaint);
                }
            } else {
                canvas.drawColor(Color.TRANSPARENT);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
