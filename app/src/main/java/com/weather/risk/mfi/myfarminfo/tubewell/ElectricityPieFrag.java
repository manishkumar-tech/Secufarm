package com.weather.risk.mfi.myfarminfo.tubewell;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.utils.MyValueFormatter;


import java.util.ArrayList;

/**
 * Created by Admin on 07-05-2018.
 */
public class ElectricityPieFrag extends Fragment implements SeekBar.OnSeekBarChangeListener,
        OnChartValueSelectedListener {


    public static final int[] Player1 = new int[]{Color.rgb(0, 191, 101), Color.rgb(252,0,0), Color.rgb(244, 170, 67), Color.rgb(28, 231, 138), Color.rgb(50, 86, 4)};

    private float in1, in2, in3, in4, in5;


    SpannableString s;

    private String averageValue, pieRank;

    String e_on, e_off;

    public ElectricityPieFrag(String on, String off) {
        // Required empty public constructor
        e_on = on;
        e_off = off;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private PieChart mChart;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.electricity_pie_fragm, container, false);

        mChart = (PieChart) view.findViewById(R.id.pieChart);


        mChart.setDrawSliceText(true);
        mChart.getLegend().setEnabled(true);
        mChart.invalidate();
        mChart.setUsePercentValues(false);
        mChart.setDescription("");
        mChart.setExtraOffsets(5, 10, 5, 5);


        mChart.setDragDecelerationFrictionCoef(0.95f);

        mChart.setCenterText(generateCenterSpannableText());
       /* if (chartFlag == 0) {

            mChart.setCenterText(generateCenterSpannableText());
        } else {
            mChart.setCenterText(generateCenterSpannableText1());
        }*/

        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(Color.WHITE);

        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);

        mChart.setHoleRadius(50f);
        mChart.setTransparentCircleRadius(70f);

        mChart.setDrawCenterText(true);

        mChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mChart.setRotationEnabled(true);
        mChart.setHighlightPerTapEnabled(true);

        mChart.setOnChartValueSelectedListener(this);

        setData(2, 100);
        mChart.animateY(1200, Easing.EasingOption.EaseInOutQuad);

        //if data is not availabe
        mChart.getData().setDrawValues(true);


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();


    }

    private void setData(int count, float range) {



        in1 = 0;
        in2 = 0;

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        ArrayList<String> xVals = new ArrayList<String>();




        in1 = Float.parseFloat(e_on);
        in2 = Float.parseFloat(e_off);


        yVals1.add(new Entry(in1, 0));
        yVals1.add(new Entry(in2, 1));

        xVals.add("Electricity ON");
        xVals.add("Electricity OFF");

        Log.v("e_on_pie",e_on+"--"+in1);
        Log.v("e_off_pie",e_off+"--"+in2);

        PieDataSet dataSet = new PieDataSet(yVals1, "");
        dataSet.setSliceSpace(2f);
        dataSet.setSelectionShift(3f);

        //   dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<Integer>();


        for (int c : Player1)
            colors.add(c);


        //colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(xVals, dataSet);
        // data.setValueFormatter(new PercentFormatter());
        data.setValueFormatter(new MyValueFormatter());
        data.setValueTextSize(13f);
        //  data.setValueTypeface(Lato_Bold);
        data.setValueTextColor(Color.WHITE);
        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        mChart.invalidate();
    }


    private SpannableString generateCenterSpannableText1() {

        if (pieRank == null) {
            pieRank = "-";
        } else if (pieRank.length() > 2) {
            s = new SpannableString(pieRank);
            s.setSpan(new RelativeSizeSpan(4.5f), 0, s.length(), 0);
            s.setSpan(new StyleSpan(Typeface.BOLD), 0, s.length(), 0);
            s.setSpan(Typeface.BOLD, 0, s.length(), 0);
            s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.blue)), 0, s.length(), 0);


        } else {
            s = new SpannableString(pieRank);
            s.setSpan(new RelativeSizeSpan(5.8f), 0, s.length(), 0);
            s.setSpan(new StyleSpan(Typeface.BOLD), 0, s.length(), 0);
            s.setSpan(Typeface.ITALIC, 0, s.length(), 0);
            s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.blue)), 0, s.length(), 0);

        }

        return s;
    }

    private SpannableString generateCenterSpannableText() {

       /* if (averageValue.equalsIgnoreCase("-")) {
            averageValue = "--";
        }*/

        int total = Integer.parseInt(e_on)+ Integer.parseInt(e_off);
        averageValue = ""+total+" Hr";

        SpannableString s = new SpannableString(averageValue);
        s.setSpan(new RelativeSizeSpan(4.8f), 0, averageValue.length(), 0);
        s.setSpan(new StyleSpan(Typeface.BOLD), 0, averageValue.length(), 0);
        //s.setSpan(Lato_Regular, 0, 2, 0);
        s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.blue)), 0, averageValue.length(), 0);
        s.setSpan(new RelativeSizeSpan(0.8f), 0, s.length(), 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 0, s.length(), 0);
        // s.setSpan(new ForegroundColorSpan(Color.GRAY), 2, s.length(), 0);
        // s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 2, s.length(), 0);
        return s;
    }


    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

        if (e == null)
            return;
        Log.i("VAL SELECTED", "Value: " + e.getVal() + ", xIndex: " + e.getXIndex() + ", DataSet index: " + dataSetIndex);

      /*  if (chartFlag == 0) {
            chartFlag = 1;
            mChart.setCenterText(generateCenterSpannableText());

        } else {
            chartFlag = 0;
            mChart.setCenterText(generateCenterSpannableText1());

        }*/

    }


    @Override
    public void onNothingSelected() {
        Log.i("PieChart", "nothing selected");
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub

    }


}