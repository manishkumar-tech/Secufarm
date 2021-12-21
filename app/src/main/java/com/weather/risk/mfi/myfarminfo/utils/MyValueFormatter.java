package com.weather.risk.mfi.myfarminfo.utils;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

/**
 * Created by Admin on 14-01-2018.
 */
public class MyValueFormatter implements ValueFormatter {

    private DecimalFormat mFormat;

    public MyValueFormatter() {
       // mFormat = new DecimalFormat("###,###,###"); // use no decimals
        mFormat = new DecimalFormat("###,###,##0.0");  // percent format
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {

        if(value < 1) return "";

        return mFormat.format(value)+ "";
    }
}