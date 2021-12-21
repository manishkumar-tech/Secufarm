package com.weather.risk.mfi.myfarminfo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.bean.GOBean;

import java.util.ArrayList;

/**
 * Created by Admin on 28-08-2017.
 */
public class GOAdapter extends  RecyclerView.Adapter<GOAdapter.ViewHolder> {
    private ArrayList<GOBean> mDataset = new ArrayList<GOBean>();

    public Context mContext;
    String imageString;
    int totalAmount=0;

    // Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        TextView date,minTemp,maxTemp,humidityMor,humidityEve,rain;




        public ViewHolder(View v) {
            super(v);
            date = (TextView) v.findViewById(R.id.date_go);
            minTemp = (TextView) v.findViewById(R.id.go_min);
            maxTemp = (TextView) v.findViewById(R.id.go_max);
            humidityMor = (TextView) v.findViewById(R.id.go_hum_m);
            humidityEve = (TextView) v.findViewById(R.id.go_hum_e);

            rain = (TextView) v.findViewById(R.id.go_rain);




        }
    }

      /*public void add(int position, String item) {
          mDataset.add(position, item);
          notifyItemInserted(position);
      }*/

    public void remove(int pos) {
        //   int position = mDataset.indexOf(item);
        mDataset.remove(pos);
        notifyItemRemoved(pos);
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public GOAdapter(Context con, ArrayList<GOBean> myDataset) {
        mDataset = myDataset;
        mContext = con;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.go_row, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {


        // holder.setIsRecyclable(false);


        String ssss = mDataset.get(position).getDate();
        String[] separated = ssss.split("T");
        if (separated.length>0){
            holder.date.setText(separated[0]);
        }        holder.minTemp.setText(mDataset.get(position).getMinTemp());
        holder.maxTemp.setText(mDataset.get(position).getMaxTemp());
        holder.humidityMor.setText(mDataset.get(position).getHumidityMax());
        holder.humidityEve.setText(mDataset.get(position).getHumidityMin());
        holder.rain.setText(mDataset.get(position).getRain());

    }




    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }


}