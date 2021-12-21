package com.weather.risk.mfi.myfarminfo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.bean.NutritionBean;

import java.util.ArrayList;

/**
 * Created by Admin on 15-09-2017.
 */
public class NutritionAdapter extends RecyclerView.Adapter<NutritionAdapter.ViewHolder> {
    private ArrayList<NutritionBean> mDataset = new ArrayList<NutritionBean>();

    public Context mContext;

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView title,message;


        public ViewHolder(View v) {
            super(v);

            title = (TextView)v.findViewById(R.id.title);
            message = (TextView) v.findViewById(R.id.message);
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
    public NutritionAdapter(Context con, ArrayList<NutritionBean> myDataset) {
        mDataset = myDataset;
        mContext = con;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.nutrition_row, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder( ViewHolder holder,  int position) {

        holder.message.setText(mDataset.get(position).getMessage());
        holder.title.setText(mDataset.get(position).getTitle());



    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }


}