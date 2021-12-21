package com.weather.risk.mfi.myfarminfo.pepsico;

import static com.weather.risk.mfi.myfarminfo.utils.Utility.setDynamicLanguage;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setFontsStyleTxt;

import android.content.Context;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.home.ForecastDetailActivity;

import java.util.ArrayList;

/**
 * Created by Admin on 30-08-2017.
 */
public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ViewHolder> {
    private ArrayList<TempBean> mDataset = new ArrayList<TempBean>();

    public Context mContext;
    String imageString;
    TextToSpeech t1;

    // Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView day, date, minTemp, maxTemp, weatherText, humidity, wind, rain, txt_Humidity, txt_Wind;
        public ImageView imageView;

        LinearLayout weather_row;


        public ViewHolder(View v) {
            super(v);
            date = (TextView) v.findViewById(R.id.forecast_date);
            day = (TextView) v.findViewById(R.id.forecast_day);

            minTemp = (TextView) v.findViewById(R.id.minTemp);
            maxTemp = (TextView) v.findViewById(R.id.maxTemp);
            weatherText = (TextView) v.findViewById(R.id.forecast_text);
            humidity = (TextView) v.findViewById(R.id.humidity);
            wind = (TextView) v.findViewById(R.id.wind);
            rain = (TextView) v.findViewById(R.id.rain_data);

            txt_Humidity = (TextView) v.findViewById(R.id.txt_Humidity);
            txt_Wind = (TextView) v.findViewById(R.id.txt_Wind);

            imageView = (ImageView) v.findViewById(R.id.forecast_image);
            weather_row = (LinearLayout) v.findViewById(R.id.weather_row);
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
    public ForecastAdapter(Context con, ArrayList<TempBean> myDataset) {
        mDataset = myDataset;
        mContext = con;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.forecast_adapter, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        try {
            holder.date.setText(mDataset.get(position).getDate());
            holder.day.setText(mDataset.get(position).getDay());
            holder.humidity.setText(mDataset.get(position).getHumidity());
            holder.minTemp.setText(mDataset.get(position).getMinTemp());
            holder.maxTemp.setText(mDataset.get(position).getMaxTemp());
            holder.weatherText.setText(mDataset.get(position).getWeatherText());
            holder.wind.setText(mDataset.get(position).getWindSpeed());

            setFontsStyleTxt(mContext, holder.day, 5);
            setFontsStyleTxt(mContext, holder.date, 6);
            setFontsStyleTxt(mContext, holder.maxTemp, 5);
            setFontsStyleTxt(mContext, holder.minTemp, 6);
            setFontsStyleTxt(mContext, holder.txt_Wind, 5);
            setFontsStyleTxt(mContext, holder.humidity, 6);
            setFontsStyleTxt(mContext, holder.txt_Wind, 5);
            setFontsStyleTxt(mContext, holder.wind, 6);
            setFontsStyleTxt(mContext, holder.weatherText, 4);

            setDynamicLanguage(mContext, holder.txt_Humidity, "Humidity", R.string.Humidity);
            setDynamicLanguage(mContext, holder.txt_Wind, "Wind", R.string.Wind);


//        if (mDataset.get(position).getImageType().equalsIgnoreCase("1")) {
//            holder.imageView.setImageResource(R.drawable.img_1);
//        } else if (mDataset.get(position).getImageType().equalsIgnoreCase("2")) {
//            holder.imageView.setImageResource(R.drawable.img_2);
//        } else if (mDataset.get(position).getImageType().equalsIgnoreCase("3")) {
//            holder.imageView.setImageResource(R.drawable.img_3);
//        } else if (mDataset.get(position).getImageType().equalsIgnoreCase("4")) {
//            holder.imageView.setImageResource(R.drawable.img_4);
//        } else if (mDataset.get(position).getImageType().equalsIgnoreCase("5")) {
//            holder.imageView.setImageResource(R.drawable.img_5);
//        } else if (mDataset.get(position).getImageType().equalsIgnoreCase("6")) {
//            holder.imageView.setImageResource(R.drawable.img_6);
//        } else {
//            holder.imageView.setImageResource(R.drawable.img_1);
//        }

            String ImagePath = mDataset.get(position).getImagepath();
            setImage(ImagePath, holder.imageView);

            holder.rain.setText(mDataset.get(position).getRain());

            holder.weather_row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

//                String dat= mDataset.get(position).getDate();
//                Intent in = new Intent(mContext, ForecastDetailActivity.class);
//                in.putExtra("date",dat+"");
//                mContext.startActivity(in);
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setImage(String ImagePath, ImageView imageView) {
        try {
            Picasso.with(mContext).load(ImagePath).into(imageView);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }


}