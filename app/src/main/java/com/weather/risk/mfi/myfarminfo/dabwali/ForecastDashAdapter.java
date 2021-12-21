package com.weather.risk.mfi.myfarminfo.dabwali;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.weather.risk.mfi.myfarminfo.R;

import java.util.ArrayList;

public class ForecastDashAdapter extends  RecyclerView.Adapter<ForecastDashAdapter.ViewHolder> {
    private ArrayList<ForecastBean> mDataset = new ArrayList<ForecastBean>();

    public Context mContext;
    String imageString;
    int totalAmount=0;

    // Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        TextView date,minTempA,maxTempA,minTempF,maxTempF,rainA,rainF;




        public ViewHolder(View v) {
            super(v);
            date = (TextView) v.findViewById(R.id.date);
            minTempA= (TextView) v.findViewById(R.id.mint_a);
            maxTempA= (TextView) v.findViewById(R.id.maxt_a);
            minTempF= (TextView) v.findViewById(R.id.mint_f);
            maxTempF= (TextView) v.findViewById(R.id.maxt_f);
            rainA= (TextView) v.findViewById(R.id.rain_act);
            rainF= (TextView) v.findViewById(R.id.rain_for);


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
    public ForecastDashAdapter(Context con, ArrayList<ForecastBean> myDataset) {
        mDataset = myDataset;
        mContext = con;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.forecast_row, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {


        // holder.setIsRecyclable(false);


        holder.date.setText(mDataset.get(position).getDate());
        holder.minTempA.setText(mDataset.get(position).getMinTemp_Act());
        holder.maxTempA.setText(mDataset.get(position).getMaxTemp_Act());
        holder.minTempF.setText(mDataset.get(position).getMinTemp_For());
        holder.maxTempF.setText(mDataset.get(position).getMaxTemp_For());
        holder.rainA.setText(mDataset.get(position).getRain_Act());
        holder.rainF.setText(mDataset.get(position).getRain_For());

    }




    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }


}
