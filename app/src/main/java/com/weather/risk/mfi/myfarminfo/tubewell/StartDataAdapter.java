package com.weather.risk.mfi.myfarminfo.tubewell;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.bean.MotorStartBean;

import java.util.ArrayList;

/**
 * Created by Admin on 08-04-2018.
 */
public class StartDataAdapter extends RecyclerView.Adapter<StartDataAdapter.ViewHolder> {
    private ArrayList<MotorStartBean> mDataset = new ArrayList<MotorStartBean>();

    public Context mContext;

    // Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView name,value;




        public ViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.date);
            value = (TextView) v.findViewById(R.id.start_time);
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
    public StartDataAdapter(Context con, ArrayList<MotorStartBean> myDataset) {
        mDataset = myDataset;
        mContext = con;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_pie_row, parent, false);

        ViewHolder vh = new ViewHolder(v);
        Log.v("lgfgdl",mDataset.size()+"");

        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        // holder.setIsRecyclable(false);

        holder.name.setText(mDataset.get(position).getxValue());
        holder.value.setText(mDataset.get(position).getyValue());

    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }



}