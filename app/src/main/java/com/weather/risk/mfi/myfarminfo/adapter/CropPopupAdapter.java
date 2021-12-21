package com.weather.risk.mfi.myfarminfo.adapter;

import android.content.Context;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.bean.CropStatusBean;
import com.weather.risk.mfi.myfarminfo.activities.NewDashboardActivity;

import java.util.ArrayList;

public class CropPopupAdapter  extends RecyclerView.Adapter<CropPopupAdapter.ViewHolder> {
    private ArrayList<CropStatusBean> mDataset = new ArrayList<CropStatusBean>();

    public Context mContext;
    String imageString;
    TextToSpeech t1;

    // Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView name,status,benchmark;
        LinearLayout row;

        public ViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.name);
            status = (TextView) v.findViewById(R.id.status);
            benchmark = (TextView)v.findViewById(R.id.benchmark);
            row = (LinearLayout)v.findViewById(R.id.hea);
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
    public CropPopupAdapter(Context con, ArrayList<CropStatusBean> myDataset) {
        mDataset = myDataset;
        mContext = con;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cropstatus_row, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final String nam = mDataset.get(position).getName();
        holder.name.setText(nam);
        String st = mDataset.get(position).getStatus();
        String va = mDataset.get(position).getValue();
        if (va!=null && !va.equalsIgnoreCase("null") && va.length()>0){
            if (st!=null && !st.equalsIgnoreCase("null") && st.length()>0) {
                holder.row.setVisibility(View.VISIBLE);
                Log.v("svasvsva",mDataset.get(position).getStatus() + "( " + mDataset.get(position).getValue() + " )");
                holder.status.setText(mDataset.get(position).getStatus() + "( " + mDataset.get(position).getValue() + " )");
            }else {
                holder.row.setVisibility(View.VISIBLE);
                holder.status.setText(mDataset.get(position).getValue());
            }
        }else if (st!=null && !st.equalsIgnoreCase("null") && st.length()>0) {
            holder.row.setVisibility(View.VISIBLE);
            holder.status.setText(mDataset.get(position).getStatus());
        }else {
            holder.row.setVisibility(View.GONE);
        }
        if(nam.equalsIgnoreCase("Disease")){
            holder.benchmark.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }
        holder.benchmark.setText(mDataset.get(position).getBenchmark());
        if (nam!=null && !nam.equalsIgnoreCase("null")){
            holder.benchmark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (nam.equalsIgnoreCase("NDVI")){
                        Intent in = new Intent(mContext, NewDashboardActivity.class);
                        in.putExtra("from", "ndvi");
                        mContext.startActivity(in);
                    }else if (nam.equalsIgnoreCase("Soil Moisture")){
                        Intent in = new Intent(mContext, NewDashboardActivity.class);
                        in.putExtra("from", "soilm");
                        mContext.startActivity(in);
                    }
                }
            });

        }

    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }



}
