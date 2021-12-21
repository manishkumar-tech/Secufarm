package com.weather.risk.mfi.myfarminfo.adapter;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.bean.TempDetailBean;
import com.weather.risk.mfi.myfarminfo.pepsico.TempBean;

import java.util.ArrayList;

/**
 * Created by Admin on 21-06-2018.
 */
public class ForecastDetailAdapter  extends RecyclerView.Adapter<ForecastDetailAdapter.ViewHolder> {
    private ArrayList<TempDetailBean> mDataset = new ArrayList<TempDetailBean>();

    public Context mContext;
    String imageString;
    TextToSpeech t1;

    // Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView time,date,minTemp,maxTemp,weatherText,humidity,wind,rain;
        public ImageView imageView;

        LinearLayout weather_row;


        public ViewHolder(View v) {
            super(v);
            date = (TextView) v.findViewById(R.id.forecast_date);
            time = (TextView) v.findViewById(R.id.forecast_time);

            minTemp = (TextView) v.findViewById(R.id.minTemp);
            maxTemp = (TextView) v.findViewById(R.id.maxTemp);
            weatherText = (TextView) v.findViewById(R.id.forecast_text);
            humidity = (TextView) v.findViewById(R.id.humidity);
            wind = (TextView) v.findViewById(R.id.wind);
            rain = (TextView)v.findViewById(R.id.rain_data);

            imageView = (ImageView)v.findViewById(R.id.forecast_image);
            weather_row = (LinearLayout)v.findViewById(R.id.weather_row);
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
    public ForecastDetailAdapter(Context con, ArrayList<TempDetailBean> myDataset) {
        mDataset = myDataset;
        mContext = con;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_forecast_row, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        String ssss = mDataset.get(position).getDate();
        String[] separated = ssss.split("T");
        if (separated.length>0){
            holder.date.setText(separated[0]);
        }
        holder.time.setText(mDataset.get(position).getTime());
        holder.humidity.setText(mDataset.get(position).getHumidity());
        holder.minTemp.setText(mDataset.get(position).getMinTemp());
        holder.maxTemp.setText(mDataset.get(position).getMaxTemp());
        holder.weatherText.setText(mDataset.get(position).getWeatherText());
        holder.wind.setText(mDataset.get(position).getWindSpeed());

        if (mDataset.get(position).getImageType().equalsIgnoreCase("1")){
            holder.imageView.setImageResource(R.drawable.img_1);
        }else if (mDataset.get(position).getImageType().equalsIgnoreCase("2")){
            holder.imageView.setImageResource(R.drawable.img_2);
        }else if (mDataset.get(position).getImageType().equalsIgnoreCase("3")){
            holder.imageView.setImageResource(R.drawable.img_3);
        }else if (mDataset.get(position).getImageType().equalsIgnoreCase("4")){
            holder.imageView.setImageResource(R.drawable.img_4);
        }else if (mDataset.get(position).getImageType().equalsIgnoreCase("5")){
            holder.imageView.setImageResource(R.drawable.img_5);
        }else if (mDataset.get(position).getImageType().equalsIgnoreCase("6")){
            holder.imageView.setImageResource(R.drawable.img_6);
        } else {
            holder.imageView.setImageResource(R.drawable.img_1);
        }
        holder.rain.setText(mDataset.get(position).getRain());

    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }


}