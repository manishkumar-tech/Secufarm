package com.weather.risk.mfi.myfarminfo.mapfragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

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
import com.weather.risk.mfi.myfarminfo.bean.ElectricStatusBean;

import java.util.ArrayList;

/**
 * Created by Admin on 05-04-2018.
 */
public class ElecStatusFrag extends Fragment implements OnChartGestureListener,
        OnChartValueSelectedListener {


    ArrayList<ElectricStatusBean> arrayListElectric = new ArrayList<ElectricStatusBean>();


    private LineChart mChart;

    TextView xAxisName, yAxisName;


    public ElecStatusFrag(ArrayList<ElectricStatusBean> list12) {
        // Required empty public constructor

        arrayListElectric = list12;


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.electric_status_frag, container, false);


        xAxisName = (TextView) view.findViewById(R.id.percent);
        yAxisName = (TextView) view.findViewById(R.id.year);

        xAxisName.setText("Value");
        yAxisName.setText("Time");


        mChart = (LineChart) view.findViewById(R.id.lineChart1);
        mChart.setOnChartGestureListener(this);
        mChart.setOnChartValueSelectedListener(this);
        mChart.setDrawGridBackground(false);

        mChart.setVisibleXRangeMaximum(10);


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
        upper_limit.enableDashedLine(8f, 8f, 0f);
        upper_limit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        upper_limit.setTextSize(8f);

        LimitLine lower_limit = new LimitLine(0f, "Lower Limit");
        lower_limit.setLineWidth(4f);
        lower_limit.enableDashedLine(8f, 8f, 0f);
        lower_limit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        lower_limit.setTextSize(8f);


        XAxis xAxis = mChart.getXAxis();
/*

        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);

        xAxis.setValueFormatter(new MyXAxisValueFormatter());
        xAxis.setLabelsToSkip(1);
*/



        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        // leftAxis.addLimitLine(upper_limit);
        // leftAxis.addLimitLine(lower_limit);
        leftAxis.setAxisMaxValue(1f);

        leftAxis.setAxisMinValue(0f);
        //leftAxis.setYOffset(20f);
        leftAxis.enableGridDashedLine(8f, 8f, 0f);
        leftAxis.setDrawZeroLine(false);
        leftAxis.setGranularity(1f); // interval 1
        leftAxis.setLabelCount(2, true);

        // limit lines are drawn behind data (and not on top)
        leftAxis.setDrawLimitLinesBehindData(true);

        mChart.getAxisRight().setEnabled(false);

        //mChart.getViewPortHandler().setMaximumScaleY(2f);
        //mChart.getViewPortHandler().setMaximumScaleX(2f);

        mChart.animateX(1500, Easing.EasingOption.EaseInOutQuart);
        mChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);


       /* xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);

        xAxis.setValueFormatter(new MyXAxisValueFormatter());
        xAxis.setLabelsToSkip(1);*/

        //  dont forget to refresh the drawing
        mChart.invalidate();
        maxMethod();


        if (arrayListElectric.size() < 1) {
            noDataMethod();
        }

        return view;
    }

    private void noDataMethod() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("No Data").
                setMessage("No data available for the period submitted").
                setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();

                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();

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
        upper_limit.enableDashedLine(8f, 8f, 0f);
        upper_limit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        upper_limit.setTextSize(8f);

        LimitLine lower_limit = new LimitLine(0f, "Lower Limit");
        lower_limit.setLineWidth(4f);
        lower_limit.enableDashedLine(8f, 8f, 0f);
        lower_limit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        lower_limit.setTextSize(8f);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        // leftAxis.addLimitLine(upper_limit);
        // leftAxis.addLimitLine(lower_limit);
        leftAxis.setAxisMaxValue(1f);
        leftAxis.setAxisMinValue(0f);
        //leftAxis.setYOffset(20f);
        leftAxis.enableGridDashedLine(8f, 8f, 0f);
        leftAxis.setDrawZeroLine(false);
        leftAxis.setGranularity(1f); // interval 1
        leftAxis.setLabelCount(2, true);

        // limit lines are drawn behind data (and not on top)
        leftAxis.setDrawLimitLinesBehindData(true);

        mChart.getAxisRight().setEnabled(false);

        //mChart.getViewPortHandler().setMaximumScaleY(2f);
        //mChart.getViewPortHandler().setMaximumScaleX(2f);

        mChart.animateX(1500, Easing.EasingOption.EaseInOutQuart);
        mChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

       /* XAxis xAxis = mChart.getXAxis();
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);


        xAxis.setValueFormatter(new MyXAxisValueFormatter());
        xAxis.setLabelsToSkip(1);*/
        //  dont forget to refresh the drawing
        mChart.invalidate();

        setDataMax1();


        mChart.clear();
        mChart.setVisibleXRangeMaximum(10);

        max1Method();


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
        upper_limit.enableDashedLine(8f, 8f, 0f);
        upper_limit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        upper_limit.setTextSize(8f);

        LimitLine lower_limit = new LimitLine(0f, "Lower Limit");
        lower_limit.setLineWidth(4f);
        lower_limit.enableDashedLine(8f, 8f, 0f);
        lower_limit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        lower_limit.setTextSize(8f);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        // leftAxis.addLimitLine(upper_limit);
        // leftAxis.addLimitLine(lower_limit);
        leftAxis.setAxisMaxValue(1f);
        leftAxis.setAxisMinValue(0f);
        //leftAxis.setYOffset(20f);
        leftAxis.enableGridDashedLine(8f, 8f, 0f);
        leftAxis.setDrawZeroLine(false);
        leftAxis.setGranularity(1f); // interval 1
        leftAxis.setLabelCount(2, true);

        // limit lines are drawn behind data (and not on top)
        leftAxis.setDrawLimitLinesBehindData(true);

        mChart.getAxisRight().setEnabled(false);

        //mChart.getViewPortHandler().setMaximumScaleY(2f);
        //mChart.getViewPortHandler().setMaximumScaleX(2f);

        mChart.animateX(1500, Easing.EasingOption.EaseInOutQuart);
        mChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

       /* XAxis xAxis = mChart.getXAxis();
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);

        xAxis.setValueFormatter(new MyXAxisValueFormatter());
        xAxis.setLabelsToSkip(1);*/
        //  dont forget to refresh the drawing
        mChart.invalidate();

        setDataMax1();

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

        Toast.makeText(getActivity(),arrayListElectric.get(entry.getXIndex()).getxValue()+"", Toast.LENGTH_SHORT).show();
     //   Log.v("lllll",entry.getVal()+"-"+entry.getXIndex()+"-"+entry.getData()+"-"+highlight.getDataSetIndex()+"-"+highlight.getXIndex());
    }

    @Override
    public void onNothingSelected() {

    }

    private ArrayList<String> setXAxisValuesMax1() {
        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < arrayListElectric.size(); i++) {
            String a = arrayListElectric.get(i).getxValue();
            if (a != null) {
                xVals.add(a);
            }

        }


        return xVals;
    }

    private ArrayList<Entry> setYAxisValuesMax1() {
        ArrayList<Entry> yVals = new ArrayList<Entry>();

        for (int i = 0; i < arrayListElectric.size(); i++) {
            String ss = arrayListElectric.get(i).getyValue();
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
        //   set1.enableDashedLine(8f, 5f, 0f);
        // set1.enableDashedHighlightLine(8f, 5f, 0f);
        set1.setColor(Color.BLACK);
        set1.setCircleColor(Color.BLACK);
        set1.setLineWidth(1f);
        set1.setCircleRadius(3f);
        set1.setDrawCircleHole(false);
        set1.setValueTextSize(9f);
        set1.setDrawFilled(true);
        set1.setDrawValues(false);

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(set1); // add the datasets

        // create a data object with the datasets
        LineData data = new LineData(xVals, dataSets);

        // set data
        mChart.setData(data);

    }

   /* public class MyXAxisValueFormatter implements XAxisValueFormatter {
        private SimpleDateFormat mFormat,mFormat1;

        public MyXAxisValueFormatter() {
        //    mFormat = new DecimalFormat("###,###,###,##0.0");
            mFormat = new SimpleDateFormat("dd-MMM HH:mm");
             mFormat1 = new SimpleDateFormat("dd-MM-yy HH:mm");
        }
        @Override
        public String getXValue(String s, int i, ViewPortHandler viewPortHandler) {

            Log.v("ldldsl",s+"");

            Date date = null;
            try {
                date = mFormat1.parse(s);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            return mFormat.format(date)+"";

        }




    }*/
}