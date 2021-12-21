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
import java.util.Arrays;
import java.util.List;

/**
 * Created by Admin on 19-03-2018.
 */
public class CropSecureAdapter extends RecyclerView.Adapter<CropSecureAdapter.ViewHolder> {
    private ArrayList<String> mDataset = new ArrayList<String>();

    public Context mContext;

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView diseaseName,pathogen,weatherCOnductive,purchase;


        public ViewHolder(View v) {
            super(v);

            diseaseName = (TextView)v.findViewById(R.id.dis_name_cs);
            pathogen = (TextView)v.findViewById(R.id.load_cs);
            weatherCOnductive = (TextView)v.findViewById(R.id.condition_cs);
            purchase = (TextView)v.findViewById(R.id.purchase_cs);
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
    public CropSecureAdapter(Context con, ArrayList<String> myDataset) {
        mDataset = myDataset;
        mContext = con;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.crop_secure_row, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder( ViewHolder holder,  int position) {


        String ss = mDataset.get(position).toString();
        if (ss!=null){
            List<String> list = Arrays.asList(ss.split(","));

            if (list.size()>0){
                holder.diseaseName.setText(list.get(0));
            }
            if (list.size()>1){
                holder.pathogen.setText(list.get(1));
            }
            if (list.size()>2){
                holder.weatherCOnductive.setText(list.get(2));
            }
            holder.purchase.setText("Yes");
        }




    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }


}