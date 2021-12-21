package com.weather.risk.mfi.myfarminfo.adapter;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.entities.FarmAdvisoryDataSet;

import java.util.ArrayList;

/**
 */
public class CustomAdapter extends BaseAdapter{
    Context context;
    ArrayList<FarmAdvisoryDataSet> dataSet;



    public CustomAdapter(Context context,  ArrayList<FarmAdvisoryDataSet> dataSet) {
        // TODO Auto-generated constructor stub
       //super(context, R.layout.display_list, dataSet);
        this.context=context;
        System.out.println("DataSet : "+dataSet.size());
        this.dataSet = dataSet;
        Log.d("recach constructor","cunstructor called");

    }

    @Override
    public int getCount() {
        return dataSet.size();
    }

    @Override
    public Object getItem(int position) {
        return dataSet.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public class Holder
    {
        TextView nutrient;
        TextView content;
        TextView soilApplication;
        


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        Holder holder ;
        if (convertView == null) {
            Log.d("recach getview method", "getview");
            holder = new Holder();
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.display_list, parent, false);
            holder.nutrient = (TextView) convertView.findViewById(R.id.textViewNutrient);
            holder.content = (TextView) convertView.findViewById(R.id.textViewContent);
            holder.soilApplication = (TextView) convertView.findViewById(R.id.textViewSoilApplication);

            convertView.setTag(holder);


        }else
            holder = (Holder) convertView.getTag();
        String nodata = "<br/>&#8226";
        holder.nutrient.setText(Html.fromHtml(nodata) + " " + dataSet.get(position).getNutrient() + " - ");
        holder.content.setText( dataSet.get(position).getContent());
        holder.soilApplication.setText(dataSet.get(position).getSoilApplication());

/*
                holder.nutrient.setText(Html.fromHtml(nodata)+" "+dataSet.get(position).getNutrient() + " - " +
                dataSet.get(position).getContent() +
                dataSet.get(position).getSoilApplication().toString());
*/
        return convertView;

    }
}
