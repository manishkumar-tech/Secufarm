package com.weather.risk.mfi.myfarminfo.tubewell;

import static com.weather.risk.mfi.myfarminfo.utils.Utility.setDynamicLanguage;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setFontsStyle;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setFontsStyleTxt;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.bean.Max1Bean;
import com.weather.risk.mfi.myfarminfo.bean.Max2Bean;
import com.weather.risk.mfi.myfarminfo.bean.Max3Bean;
import com.weather.risk.mfi.myfarminfo.bean.MaxCur1;
import com.weather.risk.mfi.myfarminfo.bean.MaxCur2;
import com.weather.risk.mfi.myfarminfo.bean.MaxCur3;
import com.weather.risk.mfi.myfarminfo.bean.Min1Bean;
import com.weather.risk.mfi.myfarminfo.bean.Min2Bean;
import com.weather.risk.mfi.myfarminfo.bean.Min3Bean;


import java.util.ArrayList;

/**
 * Created by Admin on 05-04-2018.
 */
public class MaxCurFragment extends AppCompatActivity implements OnChartGestureListener,
        OnChartValueSelectedListener {
    ArrayList<Max1Bean> arrayListMax1 = new ArrayList<Max1Bean>();
    ArrayList<Max1Bean> arrayListMax2 = new ArrayList<Max1Bean>();
    ArrayList<Max1Bean> arrayListMax3 = new ArrayList<Max1Bean>();
    ArrayList<Max1Bean> arrayListMin1 = new ArrayList<Max1Bean>();
    ArrayList<Max1Bean> arrayListMin2 = new ArrayList<Max1Bean>();
    ArrayList<Max1Bean> arrayListMin3 = new ArrayList<Max1Bean>();

    ArrayList<Max1Bean> arrayListMaxCur1 = new ArrayList<Max1Bean>();
    ArrayList<Max1Bean> arrayListMaxCur2 = new ArrayList<Max1Bean>();
    ArrayList<Max1Bean> arrayListMaxCur3 = new ArrayList<Max1Bean>();


    Button max1, max2, max3;
    Button nextBTN;

    private LineChart mChart;

    TextView xAxisName, yAxisName;
    String statusType = null;
    TextView txtCurrentAmpStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.max_cur_lay);

        ImageView backBTN = (ImageView) findViewById(R.id.backBTN);

        txtCurrentAmpStatus = (TextView) findViewById(R.id.txtCurrentAmpStatus);

        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        arrayListMax1 = (ArrayList<Max1Bean>) getIntent().getSerializableExtra("list1");
        arrayListMax2 = (ArrayList<Max1Bean>) getIntent().getSerializableExtra("list2");
        arrayListMax3 = (ArrayList<Max1Bean>) getIntent().getSerializableExtra("list3");

        arrayListMin1 = (ArrayList<Max1Bean>) getIntent().getSerializableExtra("list4");
        arrayListMin2 = (ArrayList<Max1Bean>) getIntent().getSerializableExtra("list5");
        arrayListMin3 = (ArrayList<Max1Bean>) getIntent().getSerializableExtra("list6");

        arrayListMaxCur1 = (ArrayList<Max1Bean>) getIntent().getSerializableExtra("list7");
        arrayListMaxCur2 = (ArrayList<Max1Bean>) getIntent().getSerializableExtra("list8");
        arrayListMaxCur3 = (ArrayList<Max1Bean>) getIntent().getSerializableExtra("list9");

        max1 = (Button) findViewById(R.id.max1);
        max2 = (Button) findViewById(R.id.max2);
        max3 = (Button) findViewById(R.id.max3);


        xAxisName = (TextView) findViewById(R.id.percent);
        yAxisName = (TextView) findViewById(R.id.year);

        xAxisName.setText("Amp");
        yAxisName.setText("Time");

        nextBTN = (Button) findViewById(R.id.nextBTN);

        if (statusType != null && statusType.equalsIgnoreCase("0")) {

            nextBTN.setVisibility(View.GONE);
        } else {
            nextBTN.setVisibility(View.VISIBLE);
        }

        nextBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(), MinMaxLineChart.class);
                in.putExtra("list1", arrayListMax1);
                in.putExtra("list2", arrayListMax2);
                in.putExtra("list3", arrayListMax3);
                in.putExtra("list4", arrayListMin1);
                in.putExtra("list5", arrayListMin2);
                in.putExtra("list6", arrayListMin3);

                startActivity(in);
            }
        });


        mChart = (LineChart) findViewById(R.id.lineChart1);
        mChart.setOnChartGestureListener(this);
        mChart.setOnChartValueSelectedListener(this);
        mChart.setDrawGridBackground(false);

        mChart.setVisibleXRangeMaximum(10);
        max1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mChart.clear();

                mChart.setVisibleXRangeMaximum(10);
                max1Method();

            }
        });

        max2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mChart.clear();

                mChart.setVisibleXRangeMaximum(10);
                max2Method();
            }
        });

        max3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mChart.clear();

                mChart.setVisibleXRangeMaximum(10);
                max3Method();
            }
        });


        // add data


        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();

        // modify the legend ...
        // l.setPosition(LegendPosition.LEFT_OF_CHART);
        l.setForm(Legend.LegendForm.LINE);

        // no description text
        mChart.setDescription("");
        mChart.setNoDataTextDescription("You need to provide data for the chart.");

        // enable touch gestures
        mChart.setTouchEnabled(true);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        // mChart.setScaleXEnabled(true);
        // mChart.setScaleYEnabled(true);

        LimitLine upper_limit = new LimitLine(47f, "Upper Limit");
        upper_limit.setLineWidth(4f);
        upper_limit.enableDashedLine(10f, 10f, 0f);
        upper_limit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        upper_limit.setTextSize(10f);

        LimitLine lower_limit = new LimitLine(0f, "Lower Limit");
        lower_limit.setLineWidth(4f);
        lower_limit.enableDashedLine(10f, 10f, 0f);
        lower_limit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        lower_limit.setTextSize(10f);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        // leftAxis.addLimitLine(upper_limit);
        // leftAxis.addLimitLine(lower_limit);
        leftAxis.setAxisMaxValue(50f);

        leftAxis.setAxisMinValue(0f);
        //leftAxis.setYOffset(20f);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawZeroLine(false);
        leftAxis.setGranularity(2.5f); // interval 1
        leftAxis.setLabelCount(20, true);

        // limit lines are drawn behind data (and not on top)
        leftAxis.setDrawLimitLinesBehindData(true);

        mChart.getAxisRight().setEnabled(false);

        //mChart.getViewPortHandler().setMaximumScaleY(2f);
        //mChart.getViewPortHandler().setMaximumScaleX(2f);

        mChart.animateX(1500, Easing.EasingOption.EaseInOutQuart);
        mChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

        //  dont forget to refresh the drawing
        mChart.invalidate();
        maxMethod();


    }

    @Override
    public void onResume() {
        super.onResume();
        setLanguages();
    }

    public void maxMethod() {


        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();

        // modify the legend ...
        // l.setPosition(LegendPosition.LEFT_OF_CHART);
        l.setForm(Legend.LegendForm.LINE);

        // no description text
        mChart.setDescription("");
        mChart.setNoDataTextDescription("You need to provide data for the chart.");
        // enable touch gestures
        mChart.setTouchEnabled(true);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        // mChart.setScaleXEnabled(true);
        // mChart.setScaleYEnabled(true);

        LimitLine upper_limit = new LimitLine(47f, "Upper Limit");
        upper_limit.setLineWidth(4f);
        upper_limit.enableDashedLine(10f, 10f, 0f);
        upper_limit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        upper_limit.setTextSize(10f);

        LimitLine lower_limit = new LimitLine(0f, "Lower Limit");
        lower_limit.setLineWidth(4f);
        lower_limit.enableDashedLine(10f, 10f, 0f);
        lower_limit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        lower_limit.setTextSize(10f);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        // leftAxis.addLimitLine(upper_limit);
        // leftAxis.addLimitLine(lower_limit);
        leftAxis.setAxisMaxValue(50f);

        leftAxis.setAxisMinValue(0f);
        //leftAxis.setYOffset(20f);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawZeroLine(false);
        leftAxis.setGranularity(2.5f); // interval 1
        leftAxis.setLabelCount(20, true);

        // limit lines are drawn behind data (and not on top)
        leftAxis.setDrawLimitLinesBehindData(true);

        mChart.getAxisRight().setEnabled(false);

        //mChart.getViewPortHandler().setMaximumScaleY(2f);
        //mChart.getViewPortHandler().setMaximumScaleX(2f);

        mChart.animateX(1500, Easing.EasingOption.EaseInOutQuart);
        mChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

        //  dont forget to refresh the drawing
        mChart.invalidate();

        setDataMax1();


        mChart.clear();
        mChart.setVisibleXRangeMaximum(10);
        allDataMethod();

    }


    public void allDataMethod() {


        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();

        // modify the legend ...
        // l.setPosition(LegendPosition.LEFT_OF_CHART);
        l.setForm(Legend.LegendForm.LINE);

        // no description text
        mChart.setDescription("");
        mChart.setNoDataTextDescription("You need to provide data for the chart.");
        // enable touch gestures
        mChart.setTouchEnabled(true);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        // mChart.setScaleXEnabled(true);
        // mChart.setScaleYEnabled(true);

        LimitLine upper_limit = new LimitLine(47f, "Upper Limit");
        upper_limit.setLineWidth(4f);
        upper_limit.enableDashedLine(10f, 10f, 0f);
        upper_limit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        upper_limit.setTextSize(10f);

        LimitLine lower_limit = new LimitLine(0f, "Lower Limit");
        lower_limit.setLineWidth(4f);
        lower_limit.enableDashedLine(10f, 10f, 0f);
        lower_limit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        lower_limit.setTextSize(10f);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        // leftAxis.addLimitLine(upper_limit);
        // leftAxis.addLimitLine(lower_limit);
        leftAxis.setAxisMaxValue(50f);
        leftAxis.setAxisMinValue(0f);
        //leftAxis.setYOffset(20f);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawZeroLine(false);
        leftAxis.setGranularity(2.5f); // interval 1
        leftAxis.setLabelCount(20, true);

        // limit lines are drawn behind data (and not on top)
        leftAxis.setDrawLimitLinesBehindData(true);

        mChart.getAxisRight().setEnabled(false);

        //mChart.getViewPortHandler().setMaximumScaleY(2f);
        //mChart.getViewPortHandler().setMaximumScaleX(2f);

        mChart.animateX(1500, Easing.EasingOption.EaseInOutQuart);
        mChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);


        //  dont forget to refresh the drawing
        mChart.invalidate();

        setAllDataMaxMin();


    }


    public void max1Method() {


        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();

        // modify the legend ...
        // l.setPosition(LegendPosition.LEFT_OF_CHART);
        l.setForm(Legend.LegendForm.LINE);

        // no description text
        mChart.setDescription("");
        mChart.setNoDataTextDescription("You need to provide data for the chart.");
        // enable touch gestures
        mChart.setTouchEnabled(true);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        // mChart.setScaleXEnabled(true);
        // mChart.setScaleYEnabled(true);

        LimitLine upper_limit = new LimitLine(47f, "Upper Limit");
        upper_limit.setLineWidth(4f);
        upper_limit.enableDashedLine(10f, 10f, 0f);
        upper_limit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        upper_limit.setTextSize(10f);

        LimitLine lower_limit = new LimitLine(0f, "Lower Limit");
        lower_limit.setLineWidth(4f);
        lower_limit.enableDashedLine(10f, 10f, 0f);
        lower_limit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        lower_limit.setTextSize(10f);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        // leftAxis.addLimitLine(upper_limit);
        // leftAxis.addLimitLine(lower_limit);
        leftAxis.setAxisMaxValue(50f);
        leftAxis.setAxisMinValue(0f);
        //leftAxis.setYOffset(20f);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawZeroLine(false);
        leftAxis.setGranularity(2.5f); // interval 1
        leftAxis.setLabelCount(20, true);

        // limit lines are drawn behind data (and not on top)
        leftAxis.setDrawLimitLinesBehindData(true);

        mChart.getAxisRight().setEnabled(false);

        //mChart.getViewPortHandler().setMaximumScaleY(2f);
        //mChart.getViewPortHandler().setMaximumScaleX(2f);

        mChart.animateX(1500, Easing.EasingOption.EaseInOutQuart);
        mChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

        //  dont forget to refresh the drawing
        mChart.invalidate();

        setDataMax1();

    }

    public void max2Method() {


        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();

        // modify the legend ...
        // l.setPosition(LegendPosition.LEFT_OF_CHART);
        l.setForm(Legend.LegendForm.LINE);

        // no description text
        mChart.setDescription("");
        mChart.setNoDataTextDescription("You need to provide data for the chart.");
        // enable touch gestures
        mChart.setTouchEnabled(true);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        // mChart.setScaleXEnabled(true);
        // mChart.setScaleYEnabled(true);

        LimitLine upper_limit = new LimitLine(47f, "Upper Limit");
        upper_limit.setLineWidth(4f);
        upper_limit.enableDashedLine(10f, 10f, 0f);
        upper_limit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        upper_limit.setTextSize(10f);

        LimitLine lower_limit = new LimitLine(0f, "Lower Limit");
        lower_limit.setLineWidth(4f);
        lower_limit.enableDashedLine(10f, 10f, 0f);
        lower_limit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        lower_limit.setTextSize(10f);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        // leftAxis.addLimitLine(upper_limit);
        // leftAxis.addLimitLine(lower_limit);

        leftAxis.setAxisMaxValue(50f);
        leftAxis.setAxisMinValue(0f);
        //leftAxis.setYOffset(20f);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawZeroLine(false);
        leftAxis.setGranularity(2.5f); // interval 1
        leftAxis.setLabelCount(20, true);

        // limit lines are drawn behind data (and not on top)
        leftAxis.setDrawLimitLinesBehindData(true);

        mChart.getAxisRight().setEnabled(false);

        //mChart.getViewPortHandler().setMaximumScaleY(2f);
        //mChart.getViewPortHandler().setMaximumScaleX(2f);

        mChart.animateX(1500, Easing.EasingOption.EaseInOutQuart);
        mChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

        //  dont forget to refresh the drawing
        mChart.invalidate();
        setDataMax2();
    }

    public void max3Method() {


        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();

        // modify the legend ...
        // l.setPosition(LegendPosition.LEFT_OF_CHART);
        l.setForm(Legend.LegendForm.LINE);

        // no description text
        mChart.setDescription("");
        mChart.setNoDataTextDescription("You need to provide data for the chart.");
        // enable touch gestures
        mChart.setTouchEnabled(true);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        // mChart.setScaleXEnabled(true);
        // mChart.setScaleYEnabled(true);

        LimitLine upper_limit = new LimitLine(47f, "Upper Limit");
        upper_limit.setLineWidth(4f);
        upper_limit.enableDashedLine(10f, 10f, 0f);
        upper_limit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        upper_limit.setTextSize(10f);

        LimitLine lower_limit = new LimitLine(0f, "Lower Limit");
        lower_limit.setLineWidth(4f);
        lower_limit.enableDashedLine(10f, 10f, 0f);
        lower_limit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        lower_limit.setTextSize(10f);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        // leftAxis.addLimitLine(upper_limit);
        // leftAxis.addLimitLine(lower_limit);
        leftAxis.setAxisMaxValue(50f);
        leftAxis.setAxisMinValue(0f);
        //leftAxis.setYOffset(20f);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawZeroLine(false);
        leftAxis.setGranularity(2.5f); // interval 1
        leftAxis.setLabelCount(20, true);

        // limit lines are drawn behind data (and not on top)
        leftAxis.setDrawLimitLinesBehindData(true);

        mChart.getAxisRight().setEnabled(false);

        //mChart.getViewPortHandler().setMaximumScaleY(2f);
        //mChart.getViewPortHandler().setMaximumScaleX(2f);

        mChart.animateX(2500, Easing.EasingOption.EaseInOutQuart);
        mChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

        //  dont forget to refresh the drawing
        mChart.invalidate();
        setDataMax3();
    }


    @Override
    public void onChartGestureStart(MotionEvent motionEvent, ChartTouchListener.ChartGesture chartGesture) {

    }

    @Override
    public void onChartGestureEnd(MotionEvent motionEvent, ChartTouchListener.ChartGesture chartGesture) {

    }

    @Override
    public void onChartLongPressed(MotionEvent motionEvent) {

    }

    @Override
    public void onChartDoubleTapped(MotionEvent motionEvent) {

    }

    @Override
    public void onChartSingleTapped(MotionEvent motionEvent) {

    }

    @Override
    public void onChartFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {

    }

    @Override
    public void onChartScale(MotionEvent motionEvent, float v, float v1) {

    }

    @Override
    public void onChartTranslate(MotionEvent motionEvent, float v, float v1) {

    }

    @Override
    public void onValueSelected(Entry entry, int i, Highlight highlight) {

    }

    @Override
    public void onNothingSelected() {

    }

    private ArrayList<String> setXAxisValuesMax1() {
        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < arrayListMaxCur1.size(); i++) {
            String a = arrayListMaxCur1.get(i).getvValue();
            if (a != null) {
                xVals.add(a);
            }

        }


        return xVals;
    }

    private ArrayList<Entry> setYAxisValuesMax1() {
        ArrayList<Entry> yVals = new ArrayList<Entry>();

        for (int i = 0; i < arrayListMaxCur1.size(); i++) {
            String ss = arrayListMaxCur1.get(i).getyValue();
            if (ss != null) {
                yVals.add(new Entry(Float.parseFloat(ss), i));
            }
        }

        return yVals;
    }

    private void setDataMax1() {
        ArrayList<String> xVals = setXAxisValuesMax1();

        ArrayList<Entry> yVals = setYAxisValuesMax1();

        LineDataSet set1;

        // create a dataset and give it a type
        set1 = new LineDataSet(yVals, "");

        set1.setFillAlpha(110);
        // set1.setFillColor(Color.RED);

        // set the line to be drawn like this "- - - - - -"
        //   set1.enableDashedLine(10f, 5f, 0f);
        // set1.enableDashedHighlightLine(10f, 5f, 0f);
        set1.setColor(Color.RED);
        set1.setCircleColor(Color.RED);
        set1.setLineWidth(1f);
        set1.setCircleRadius(3f);
        set1.setDrawCircleHole(false);
        set1.setValueTextSize(9f);
        set1.setDrawFilled(true);

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(set1); // add the datasets

        // create a data object with the datasets
        LineData data = new LineData(xVals, dataSets);

        // set data
        mChart.setData(data);

    }

    private ArrayList<String> setXAxisValuesMax2() {
        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < arrayListMaxCur2.size(); i++) {

            String a = arrayListMaxCur2.get(i).getvValue();
            if (a != null) {
                xVals.add(a);
            }

        }


        return xVals;
    }

    private ArrayList<Entry> setYAxisValuesMax2() {
        ArrayList<Entry> yVals = new ArrayList<Entry>();

        for (int i = 0; i < arrayListMaxCur2.size(); i++) {
            String ss = arrayListMaxCur2.get(i).getyValue();
            if (ss != null) {
                yVals.add(new Entry(Float.parseFloat(ss), i));
            }
        }

        return yVals;
    }


    private void setDataMax2() {
        ArrayList<String> xVals = setXAxisValuesMax2();

        ArrayList<Entry> yVals = setYAxisValuesMax2();

        LineDataSet set1;

        // create a dataset and give it a type
        set1 = new LineDataSet(yVals, "");

        set1.setFillAlpha(110);
        // set1.setFillColor(Color.RED);

        // set the line to be drawn like this "- - - - - -"
        //   set1.enableDashedLine(10f, 5f, 0f);
        // set1.enableDashedHighlightLine(10f, 5f, 0f);
        set1.setColor(Color.YELLOW);
        set1.setCircleColor(Color.YELLOW);
        set1.setLineWidth(1f);
        set1.setCircleRadius(3f);
        set1.setDrawCircleHole(false);
        set1.setValueTextSize(9f);
        set1.setDrawFilled(true);

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(set1); // add the datasets

        // create a data object with the datasets
        LineData data = new LineData(xVals, dataSets);

        // set data
        mChart.setData(data);

    }

    private ArrayList<String> setXAxisValuesMax3() {
        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < arrayListMaxCur3.size(); i++) {

            String a = arrayListMaxCur3.get(i).getvValue();
            if (a != null) {
                xVals.add(a);
            }

        }


        return xVals;
    }

    private ArrayList<Entry> setYAxisValuesMax3() {
        ArrayList<Entry> yVals = new ArrayList<Entry>();

        for (int i = 0; i < arrayListMaxCur3.size(); i++) {
            String ss = arrayListMaxCur3.get(i).getyValue();

            Log.v("Max3Value", "" + ss);
            if (ss != null) {
                yVals.add(new Entry(Float.parseFloat(ss), i));
            }
        }

        return yVals;
    }

    private void setDataMax3() {
        ArrayList<String> xVals = setXAxisValuesMax3();

        ArrayList<Entry> yVals = setYAxisValuesMax3();

        LineDataSet set1;

        // create a dataset and give it a type
        set1 = new LineDataSet(yVals, "");

        set1.setFillAlpha(110);
        // set1.setFillColor(Color.RED);

        // set the line to be drawn like this "- - - - - -"
        //   set1.enableDashedLine(10f, 5f, 0f);
        // set1.enableDashedHighlightLine(10f, 5f, 0f);
        set1.setColor(Color.BLUE);
        set1.setCircleColor(Color.BLUE);
        set1.setLineWidth(1f);
        set1.setCircleRadius(3f);
        set1.setDrawCircleHole(false);
        set1.setValueTextSize(9f);
        set1.setDrawFilled(true);

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(set1); // add the datasets

        // create a data object with the datasets
        LineData data = new LineData(xVals, dataSets);

        // set data
        mChart.setData(data);

    }

    private void setAllDataMaxMin() {
        ArrayList<String> xVals = setXAxisValuesMax1();

        ArrayList<Entry> yVals = setYAxisValuesMax1();

        LineDataSet set1;

        // create a dataset and give it a type
        set1 = new LineDataSet(yVals, "");

        set1.setFillAlpha(110);
        // set1.setFillColor(Color.RED);

        // set the line to be drawn like this "- - - - - -"
        //   set1.enableDashedLine(10f, 5f, 0f);
        // set1.enableDashedHighlightLine(10f, 5f, 0f);
        set1.setColor(Color.RED);
        set1.setCircleColor(Color.RED);
        set1.setLineWidth(1f);
        set1.setCircleRadius(3f);
        set1.setDrawCircleHole(false);
        set1.setValueTextSize(9f);
        set1.setDrawFilled(true);


        ArrayList<String> xVals2 = setXAxisValuesMax2();

        ArrayList<Entry> yVal2 = setYAxisValuesMax2();

        LineDataSet set2;

        // create a dataset and give it a type
        set2 = new LineDataSet(yVal2, "");

        set2.setFillAlpha(110);
        // set1.setFillColor(Color.RED);

        // set the line to be drawn like this "- - - - - -"
        //   set1.enableDashedLine(10f, 5f, 0f);
        // set1.enableDashedHighlightLine(10f, 5f, 0f);
        set2.setColor(Color.YELLOW);
        set2.setCircleColor(Color.YELLOW);
        set2.setLineWidth(1f);
        set2.setCircleRadius(3f);
        set2.setDrawCircleHole(false);
        set2.setValueTextSize(9f);
        set2.setDrawFilled(true);

        ArrayList<String> xVals3 = setXAxisValuesMax3();
        ArrayList<Entry> yVal3 = setYAxisValuesMax3();
        LineDataSet set3;
        // create a dataset and give it a type
        set3 = new LineDataSet(yVal3, "");

        set3.setFillAlpha(110);
        // set1.setFillColor(Color.RED);

        // set the line to be drawn like this "- - - - - -"
        //   set1.enableDashedLine(10f, 5f, 0f);
        // set1.enableDashedHighlightLine(10f, 5f, 0f);
        set3.setColor(Color.BLUE);
        set3.setCircleColor(Color.BLUE);
        set3.setLineWidth(1f);
        set3.setCircleRadius(3f);
        set3.setDrawCircleHole(false);
        set3.setValueTextSize(9f);
        set3.setDrawFilled(true);


        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(set1); // add the datasets
        dataSets.add(set2); // add the datasets
        dataSets.add(set3); // add the datasets

        // create a data object with the datasets
        LineData data = new LineData(xVals, dataSets);


        // set data
        mChart.setData(data);

    }

    public void setLanguages() {
        setFontsStyleTxt(getApplicationContext(), txtCurrentAmpStatus, 2);
        setFontsStyle(getApplicationContext(), nextBTN);
        setFontsStyle(getApplicationContext(), max1);
        setFontsStyle(getApplicationContext(), max2);
        setFontsStyle(getApplicationContext(), max3);

        setDynamicLanguage(getApplicationContext(), txtCurrentAmpStatus, "ElectricityStatus", R.string.ElectricityStatus);
        setDynamicLanguage(getApplicationContext(), nextBTN, "Next", R.string.Next);
        setDynamicLanguage(getApplicationContext(), max1, "Max_R", R.string.Max_R);
        setDynamicLanguage(getApplicationContext(), max2, "Max_Y", R.string.Max_Y);
        setDynamicLanguage(getApplicationContext(), max3, "Max_B", R.string.Max_B);
    }

}