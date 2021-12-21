package com.weather.risk.mfi.myfarminfo.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.bean.FutureDataBean;
import com.weather.risk.mfi.myfarminfo.pest_disease.VulnerabilityBean;

import java.util.ArrayList;

/**
 * Created by Admin on 16-02-2018.
 */
public class ShowFutureAdapter  extends RecyclerView.Adapter<ShowFutureAdapter.ViewHolder> {
    private ArrayList<FutureDataBean> mDataset = new ArrayList<FutureDataBean>();

    public Context mContext;
    String imageString;
    TextToSpeech t1;

    // Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView date,maxTemp;




        public ViewHolder(View v) {
            super(v);
            date = (TextView) v.findViewById(R.id.date_feas);

            maxTemp = (TextView) v.findViewById(R.id.max_temp_feas);


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
    public ShowFutureAdapter(Context con, ArrayList<FutureDataBean> myDataset) {
        mDataset = myDataset;
        mContext = con;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_future_row, parent, false);

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
        }
        holder.maxTemp.setText(mDataset.get(position).getMaxTemp());


    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    ProgressDialog dialog;
    SharedPreferences prefs;




}
