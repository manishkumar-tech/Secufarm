package com.weather.risk.mfi.myfarminfo.tubewell;

import static com.weather.risk.mfi.myfarminfo.utils.Utility.setDynamicLanguage;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setFontsStyle;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setFontsStyleTxt;

import android.graphics.Color;
import android.os.Bundle;
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
import com.weather.risk.mfi.myfarminfo.bean.Min1Bean;
import com.weather.risk.mfi.myfarminfo.bean.Min2Bean;
import com.weather.risk.mfi.myfarminfo.bean.Min3Bean;


import java.util.ArrayList;

/**
 * Created by Admin on 04-04-2018.
 */
public class MinMaxLineChart extends AppCompatActivity implements OnChartGestureListener,
        OnChartValueSelectedListener {

    ArrayList<Max1Bean> arrayListMax1 = new ArrayList<Max1Bean>();
    ArrayList<Max1Bean> arrayListMax2 = new ArrayList<Max1Bean>();
    ArrayList<Max1Bean> arrayListMax3 = new ArrayList<Max1Bean>();
    ArrayList<Max1Bean> arrayListMin1 = new ArrayList<Max1Bean>();
    ArrayList<Max1Bean> arrayListMin2 = new ArrayList<Max1Bean>();
    ArrayList<Max1Bean> arrayListMin3 = new ArrayList<Max1Bean>();


    Button max1, max2, max3;
    Button min1, min2, min3;


    private LineChart mChart;

    TextView xAxisName, yAxisName,txtVoltageStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.min_max_layout);

        ImageView backBTN = (ImageView) findViewById(R.id.backBTN);
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


        max1 = (Button) findViewById(R.id.max1);
        max2 = (Button) findViewById(R.id.max2);
        max3 = (Button) findViewById(R.id.max3);

        min1 = (Button) findViewById(R.id.min1);
        min2 = (Button) findViewById(R.id.min2);
        min3 = (Button) findViewById(R.id.min3);


        xAxisName = (TextView) findViewById(R.id.percent);
        yAxisName = (TextView) findViewById(R.id.year);
        txtVoltageStatus = (TextView) findViewById(R.id.txtVoltageStatus);

        xAxisName.setText("Volt");
        yAxisName.setText("Time");


      /*  max2.setBackgroundResource(R.drawable.btn_bg);
        max1.setBackgroundResource(R.drawable.selected_btn_bg);
        max3.setBackgroundResource(R.drawable.btn_bg);
        min1.setBackgroundResource(R.drawable.btn_bg);
        min2.setBackgroundResource(R.drawable.btn_bg);
        min3.setBackgroundResource(R.drawable.btn_bg);*/


        mChart = (LineChart) findViewById(R.id.lineChart1);
        mChart.setOnChartGestureListener(this);
        mChart.setOnChartValueSelectedListener(this);
        mChart.setDrawGridBackground(false);

        mChart.setVisibleXRangeMaximum(10);
        max1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


              /*  max1.setBackgroundResource(R.drawable.selected_btn_bg);
                max2.setBackgroundResource(R.drawable.btn_bg);
                max3.setBackgroundResource(R.drawable.btn_bg);
                min1.setBackgroundResource(R.drawable.btn_bg);
                min2.setBackgroundResource(R.drawable.btn_bg);
                min3.setBackgroundResource(R.drawable.btn_bg);
*/

                mChart.clear();

                mChart.setVisibleXRangeMaximum(10);
                max1Method();

            }
        });

        max2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                /*max2.setBackgroundResource(R.drawable.selected_btn_bg);
                max1.setBackgroundResource(R.drawable.btn_bg);
                max3.setBackgroundResource(R.drawable.btn_bg);
                min1.setBackgroundResource(R.drawable.btn_bg);
                min2.setBackgroundResource(R.drawable.btn_bg);
                min3.setBackgroundResource(R.drawable.btn_bg);
*/
                mChart.clear();

                mChart.setVisibleXRangeMaximum(10);
                max2Method();
            }
        });

        max3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

/*
                max3.setBackgroundResource(R.drawable.selected_btn_bg);
                max2.setBackgroundResource(R.drawable.btn_bg);
                max1.setBackgroundResource(R.drawable.btn_bg);
                min1.setBackgroundResource(R.drawable.btn_bg);
                min2.setBackgroundResource(R.drawable.btn_bg);
                min3.setBackgroundResource(R.drawable.btn_bg);*/
                mChart.clear();

                mChart.setVisibleXRangeMaximum(10);
                max3Method();
            }
        });


        min1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


             /*   max1.setBackgroundResource(R.drawable.btn_bg);
                max2.setBackgroundResource(R.drawable.btn_bg);
                max3.setBackgroundResource(R.drawable.btn_bg);
                min1.setBackgroundResource(R.drawable.selected_btn_bg);
                min2.setBackgroundResource(R.drawable.btn_bg);
                min3.setBackgroundResource(R.drawable.btn_bg);
*/

                mChart.clear();
                mChart.setVisibleXRangeMaximum(10);
                min1Method();

            }
        });

        min2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


              /*  max1.setBackgroundResource(R.drawable.btn_bg);
                max2.setBackgroundResource(R.drawable.btn_bg);
                max3.setBackgroundResource(R.drawable.btn_bg);
                min2.setBackgroundResource(R.drawable.selected_btn_bg);
                min1.setBackgroundResource(R.drawable.btn_bg);
                min3.setBackgroundResource(R.drawable.btn_bg);
*/

                mChart.clear();
                mChart.setVisibleXRangeMaximum(10);
                min2Method();
            }
        });

        min3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


             /*   max1.setBackgroundResource(R.drawable.btn_bg);
                max2.setBackgroundResource(R.drawable.btn_bg);
                max3.setBackgroundResource(R.drawable.btn_bg);
                min3.setBackgroundResource(R.drawable.selected_btn_bg);
                min2.setBackgroundResource(R.drawable.btn_bg);
                min1.setBackgroundResource(R.drawable.btn_bg);
*/

                mChart.clear();
                mChart.setVisibleXRangeMaximum(10);
                min3Method();
            }
        });


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
        leftAxis.setAxisMaxValue(9f);

        leftAxis.setAxisMinValue(0f);
        //leftAxis.setYOffset(20f);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawZeroLine(false);
        leftAxis.setGranularity(5f); // interval 1
        leftAxis.setLabelCount(50, true);

        // limit lines are drawn behind data (and not on top)
        leftAxis.setDrawLimitLinesBehindData(true);

        mChart.getAxisRight().setEnabled(false);

        //mChart.getViewPortHandler().setMaximumScaleY(2f);
        //mChart.getViewPortHandler().setMaximumScaleX(2f);

        mChart.animateX(1500, Easing.EasingOption.EaseInOutQuart);
        mChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

        mChart.setVisibleXRangeMaximum(10);
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
        leftAxis.setAxisMaxValue(500f);
        leftAxis.setAxisMinValue(0f);
        //leftAxis.setYOffset(20f);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawZeroLine(false);
        leftAxis.setGranularity(5f); // interval 1
        leftAxis.setLabelCount(50, true);

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
        leftAxis.setAxisMaxValue(500f);
        leftAxis.setAxisMinValue(0f);
        //leftAxis.setYOffset(20f);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawZeroLine(false);
        leftAxis.setGranularity(5f); // interval 1
        leftAxis.setLabelCount(50, true);

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
        leftAxis.setAxisMaxValue(500f);
        leftAxis.setAxisMinValue(0f);
        //leftAxis.setYOffset(20f);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawZeroLine(false);
        leftAxis.setGranularity(5f); // interval 1
        leftAxis.setLabelCount(50, true);

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
        leftAxis.setAxisMaxValue(500f);
        leftAxis.setAxisMinValue(0f);
        //leftAxis.setYOffset(20f);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawZeroLine(false);
        leftAxis.setGranularity(5f); // interval 1
        leftAxis.setLabelCount(50, true);

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
        leftAxis.setAxisMaxValue(500f);
        leftAxis.setAxisMinValue(0f);
        //leftAxis.setYOffset(20f);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawZeroLine(false);
        leftAxis.setGranularity(5f); // interval 1
        leftAxis.setLabelCount(50, true);

        // limit lines are drawn behind data (and not on top)
        leftAxis.setDrawLimitLinesBehindData(true);

        mChart.getAxisRight().setEnabled(false);

        //mChart.getViewPortHandler().setMaximumScaleY(2f);
        //mChart.getViewPortHandler().setMaximumScaleX(2f);

        mChart.animateX(1500, Easing.EasingOption.EaseInOutQuart);
        mChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

        //  dont forget to refresh the drawing
        mChart.invalidate();
        setDataMax3();
    }

    public void min1Method() {


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

        leftAxis.setAxisMaxValue(500f);

        leftAxis.setAxisMinValue(0f);
        //leftAxis.setYOffset(20f);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawZeroLine(false);
        leftAxis.setGranularity(5f); // interval 1
        leftAxis.setLabelCount(50, true);

        // limit lines are drawn behind data (and not on top)
        leftAxis.setDrawLimitLinesBehindData(true);

        mChart.getAxisRight().setEnabled(false);

        //mChart.getViewPortHandler().setMaximumScaleY(2f);
        //mChart.getViewPortHandler().setMaximumScaleX(2f);

        mChart.animateX(1500, Easing.EasingOption.EaseInOutQuart);
        mChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);


        //  dont forget to refresh the drawing
        mChart.invalidate();

        setDataMin1();

    }

    public void min2Method() {


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

        leftAxis.setAxisMaxValue(500f);

        leftAxis.setAxisMinValue(0f);
        //leftAxis.setYOffset(20f);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawZeroLine(false);
        leftAxis.setGranularity(5f); // interval 1
        leftAxis.setLabelCount(50, true);

        // limit lines are drawn behind data (and not on top)
        leftAxis.setDrawLimitLinesBehindData(true);

        mChart.getAxisRight().setEnabled(false);

        //mChart.getViewPortHandler().setMaximumScaleY(2f);
        //mChart.getViewPortHandler().setMaximumScaleX(2f);

        mChart.animateX(2000, Easing.EasingOption.EaseInOutQuart);
        mChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);


        //  dont forget to refresh the drawing
        mChart.invalidate();

        setDataMin2();

    }

    public void min3Method() {


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

        leftAxis.setAxisMaxValue(500f);

        leftAxis.setAxisMinValue(0f);
        //leftAxis.setYOffset(20f);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawZeroLine(false);
        leftAxis.setGranularity(5f); // interval 1
        leftAxis.setLabelCount(50, true);

        // limit lines are drawn behind data (and not on top)
        leftAxis.setDrawLimitLinesBehindData(true);

        mChart.getAxisRight().setEnabled(false);

        //mChart.getViewPortHandler().setMaximumScaleY(2f);
        //mChart.getViewPortHandler().setMaximumScaleX(2f);

        mChart.animateX(2000, Easing.EasingOption.EaseInOutQuart);
        mChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);


        //  dont forget to refresh the drawing
        mChart.invalidate();

        setDataMin3();

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

        for (int i = 0; i < arrayListMax1.size(); i++) {
            String a = arrayListMax1.get(i).getvValue();
            if (a != null) {
                xVals.add(a);
            }

        }


        return xVals;
    }

    private ArrayList<Entry> setYAxisValuesMax1() {
        ArrayList<Entry> yVals = new ArrayList<Entry>();

        for (int i = 0; i < arrayListMax1.size(); i++) {
            String ss = arrayListMax1.get(i).getyValue();
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
        set1.setCircleRadius(2f);
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

        ArrayList<String> xVals4 = setXAxisValuesMin1();
        ArrayList<Entry> yVal4 = setYAxisValuesMin1();
        LineDataSet set4;
        // create a dataset and give it a type
        set4 = new LineDataSet(yVal4, "");

        set4.setFillAlpha(110);
        // set1.setFillColor(Color.RED);

        // set the line to be drawn like this "- - - - - -"
        //   set1.enableDashedLine(10f, 5f, 0f);
        // set1.enableDashedHighlightLine(10f, 5f, 0f);
        set4.setColor(Color.RED);
        set4.setCircleColor(Color.RED);
        set4.setLineWidth(1f);
        set4.setCircleRadius(3f);
        set4.setDrawCircleHole(false);
        set4.setValueTextSize(9f);
        set4.setDrawFilled(true);

        ArrayList<String> xVals5 = setXAxisValuesMin2();
        ArrayList<Entry> yVal5 = setYAxisValuesMin2();
        LineDataSet set5;
        // create a dataset and give it a type
        set5 = new LineDataSet(yVal5, "");

        set5.setFillAlpha(110);
        // set1.setFillColor(Color.RED);

        // set the line to be drawn like this "- - - - - -"
        //   set1.enableDashedLine(10f, 5f, 0f);
        // set1.enableDashedHighlightLine(10f, 5f, 0f);
        set5.setColor(Color.YELLOW);
        set5.setCircleColor(Color.YELLOW);
        set5.setLineWidth(1f);
        set5.setCircleRadius(3f);
        set5.setDrawCircleHole(false);
        set5.setValueTextSize(9f);
        set5.setDrawFilled(true);

        ArrayList<String> xVals6 = setXAxisValuesMin3();
        ArrayList<Entry> yVal6 = setYAxisValuesMin3();
        LineDataSet set6;
        // create a dataset and give it a type
        set6 = new LineDataSet(yVal6, "");

        set6.setFillAlpha(110);
        // set1.setFillColor(Color.RED);

        // set the line to be drawn like this "- - - - - -"
        //   set1.enableDashedLine(10f, 5f, 0f);
        // set1.enableDashedHighlightLine(10f, 5f, 0f);
        set6.setColor(Color.BLUE);
        set6.setCircleColor(Color.BLUE);
        set6.setLineWidth(1f);
        set6.setCircleRadius(3f);
        set6.setDrawCircleHole(false);
        set6.setValueTextSize(9f);
        set6.setDrawFilled(true);

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(set1); // add the datasets
        dataSets.add(set2); // add the datasets
        dataSets.add(set3); // add the datasets
        dataSets.add(set4); // add the datasets
        dataSets.add(set5); // add the datasets
        dataSets.add(set6); // add the datasets

        // create a data object with the datasets
        LineData data = new LineData(xVals, dataSets);


        // set data
        mChart.setData(data);

    }


    private ArrayList<String> setXAxisValuesMax2() {
        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < arrayListMax2.size(); i++) {

            String a = arrayListMax2.get(i).getvValue();
            if (a != null) {
                xVals.add(a);
            }

        }


        return xVals;
    }

    private ArrayList<Entry> setYAxisValuesMax2() {
        ArrayList<Entry> yVals = new ArrayList<Entry>();

        for (int i = 0; i < arrayListMax2.size(); i++) {
            String ss = arrayListMax2.get(i).getyValue();
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
        set1.setCircleRadius(2f);
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

        for (int i = 0; i < arrayListMax3.size(); i++) {

            String a = arrayListMax3.get(i).getvValue();
            if (a != null) {
                xVals.add(a);
            }

        }


        return xVals;
    }

    private ArrayList<Entry> setYAxisValuesMax3() {
        ArrayList<Entry> yVals = new ArrayList<Entry>();

        for (int i = 0; i < arrayListMax3.size(); i++) {
            String ss = arrayListMax3.get(i).getyValue();
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
        set1.setCircleRadius(2f);
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


    private ArrayList<String> setXAxisValuesMin1() {
        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < arrayListMin1.size(); i++) {

            String a = arrayListMin1.get(i).getvValue();
            if (a != null) {
                xVals.add(a);
            }

        }


        return xVals;
    }

    private ArrayList<Entry> setYAxisValuesMin1() {
        ArrayList<Entry> yVals = new ArrayList<Entry>();

        for (int i = 0; i < arrayListMin1.size(); i++) {
            String ss = arrayListMin1.get(i).getyValue();
            if (ss != null) {
                yVals.add(new Entry(Float.parseFloat(ss), i));
            }
        }

        return yVals;
    }

    private void setDataMin1() {
        ArrayList<String> xVals = setXAxisValuesMin1();

        ArrayList<Entry> yVals = setYAxisValuesMin1();

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
        set1.setCircleRadius(2f);
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


    private ArrayList<String> setXAxisValuesMin2() {
        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < arrayListMin2.size(); i++) {

            String a = arrayListMin2.get(i).getvValue();
            if (a != null) {
                xVals.add(a);
            }

        }


        return xVals;
    }

    private ArrayList<Entry> setYAxisValuesMin2() {
        ArrayList<Entry> yVals = new ArrayList<Entry>();

        for (int i = 0; i < arrayListMin2.size(); i++) {
            String ss = arrayListMin2.get(i).getyValue();
            if (ss != null) {
                yVals.add(new Entry(Float.parseFloat(ss), i));
            }
        }

        return yVals;
    }

    private void setDataMin2() {
        ArrayList<String> xVals = setXAxisValuesMin2();

        ArrayList<Entry> yVals = setYAxisValuesMin2();

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
        set1.setCircleRadius(2f);
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


    private ArrayList<String> setXAxisValuesMin3() {
        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < arrayListMin3.size(); i++) {

            String a = arrayListMin3.get(i).getvValue();
            if (a != null) {
                xVals.add(a);
            }

        }


        return xVals;
    }

    private ArrayList<Entry> setYAxisValuesMin3() {
        ArrayList<Entry> yVals = new ArrayList<Entry>();

        for (int i = 0; i < arrayListMin3.size(); i++) {
            String ss = arrayListMin3.get(i).getyValue();
            if (ss != null) {
                yVals.add(new Entry(Float.parseFloat(ss), i));
            }
        }

        return yVals;
    }

    private void setDataMin3() {
        ArrayList<String> xVals = setXAxisValuesMin3();

        ArrayList<Entry> yVals = setYAxisValuesMin3();

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
        set1.setCircleRadius(2f);
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

    public void setLanguages() {

        setFontsStyleTxt(getApplicationContext(), txtVoltageStatus, 2);
        setFontsStyle(getApplicationContext(), max1);
        setFontsStyle(getApplicationContext(), max2);
        setFontsStyle(getApplicationContext(), max3);
        setFontsStyle(getApplicationContext(), min1);
        setFontsStyle(getApplicationContext(), min2);
        setFontsStyle(getApplicationContext(), min3);

        setDynamicLanguage(getApplicationContext(), txtVoltageStatus, "VoltageStatus", R.string.VoltageStatus);
        setDynamicLanguage(getApplicationContext(), max1, "Max_R", R.string.Max_R);
        setDynamicLanguage(getApplicationContext(), max2, "Max_Y", R.string.Max_Y);
        setDynamicLanguage(getApplicationContext(), max3, "Max_B", R.string.Max_B);
        setDynamicLanguage(getApplicationContext(), min1, "Min_R", R.string.Min_R);
        setDynamicLanguage(getApplicationContext(), min2, "Min_Y", R.string.Min_Y);
        setDynamicLanguage(getApplicationContext(), min3, "Min_B", R.string.Min_B);
    }

}