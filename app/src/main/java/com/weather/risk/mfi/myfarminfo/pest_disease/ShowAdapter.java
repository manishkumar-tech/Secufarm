package com.weather.risk.mfi.myfarminfo.pest_disease;

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
import com.weather.risk.mfi.myfarminfo.pepsico.DiseaseBean;

import java.util.ArrayList;

/**
 */
public class ShowAdapter extends RecyclerView.Adapter<ShowAdapter.ViewHolder> {
    private ArrayList<VulnerabilityBean> mDataset = new ArrayList<VulnerabilityBean>();

    public Context mContext;
    String imageString;
    TextToSpeech t1;

    // Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView date,minTemp,maxTemp,rain,humidityMor,humidityEve;




        public ViewHolder(View v) {
            super(v);
            date = (TextView) v.findViewById(R.id.date_show);
            minTemp = (TextView) v.findViewById(R.id.minTemp_show);
            maxTemp = (TextView) v.findViewById(R.id.maxTemp_show);
            rain = (TextView) v.findViewById(R.id.rain_show);
            humidityMor = (TextView) v.findViewById(R.id.humidity_mor_show);
            humidityEve = (TextView) v.findViewById(R.id.humidity_eve_show);


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
    public ShowAdapter(Context con, ArrayList<VulnerabilityBean> myDataset) {
        mDataset = myDataset;
        mContext = con;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_data_row, parent, false);

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
        holder.minTemp.setText(mDataset.get(position).getMinTemp());
        holder.maxTemp.setText(mDataset.get(position).getMaxTem());
        holder.rain.setText(mDataset.get(position).getRain());
        holder.humidityMor.setText(mDataset.get(position).getHimidityMor());
        holder.humidityEve.setText(mDataset.get(position).getHumidityEve());


    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    ProgressDialog dialog;
    SharedPreferences prefs;




}