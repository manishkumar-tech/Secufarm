package com.weather.risk.mfi.myfarminfo.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.bean.KeyValueBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SoilReportAdapter  extends RecyclerView.Adapter<SoilReportAdapter.ViewHolder> {
    private ArrayList<KeyValueBean> mDataset = new ArrayList<KeyValueBean>();

    public Context mContext;
    String imageString;
    int totalAmount = 0;

    // Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        TextView title, value;
        RelativeLayout rl_row;


        public ViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.title_f);
            value = (TextView) v.findViewById(R.id.value_f);
            rl_row = (RelativeLayout)v.findViewById(R.id.ttttt);

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
    public SoilReportAdapter(Context con, ArrayList<KeyValueBean> myDataset) {
        mDataset = myDataset;
        mContext = con;

    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.farm_detail_row, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {


        // holder.setIsRecyclable(false);


        holder.rl_row.setVisibility(View.GONE);
        String ss = mDataset.get(position).getValue();

        if( (ss != null && !ss.equalsIgnoreCase("null"))&& ss.length()>0) {
            holder.rl_row.setVisibility(View.VISIBLE);
            holder.title.setText(mDataset.get(position).getName());
            holder.value.setText(ss);


        }else {
            holder.rl_row.setVisibility(View.GONE);
        }


    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }



}
