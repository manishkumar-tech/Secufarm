package com.weather.risk.mfi.myfarminfo.adapter;


import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.squareup.picasso.Picasso;
import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.pepsico.TempBean;

import java.util.ArrayList;
import java.util.Calendar;

public class ForecastAdapterList extends BaseAdapter {
    private ArrayList<TempBean> mDataset = new ArrayList<TempBean>();

    public Context mContext;
    String imageString;
    TextToSpeech t1;

    // Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder
    // Provide a suitable constructor (depends on the kind of dataset)
    public ForecastAdapterList(Context con, ArrayList<TempBean> myDataset) {
        myDataset.remove(0);
        myDataset.remove(myDataset.size() - 1);
        mDataset = myDataset;
        mContext = con;

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getCount() {
        return mDataset.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View holder, ViewGroup viewGroup) {
        holder = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.forecast_adapter_list, viewGroup, false); // inflate the layout
        TextView day, forecast_date, minTemp, maxTemp;
        ImageView imageView;
        RelativeLayout weather_row;
        //  date = (TextView) holder.findViewById(R.id.forecast_date);
        day = (TextView) holder.findViewById(R.id.forecast_day);
        forecast_date = (TextView) holder.findViewById(R.id.forecast_date);

        minTemp = (TextView) holder.findViewById(R.id.minTemp);
        maxTemp = (TextView) holder.findViewById(R.id.maxTemp);
        //  weatherText = (TextView) holder.findViewById(R.id.forecast_text);
        //   humidity = (TextView) holder.findViewById(R.id.humidity);
        //   wind = (TextView) holder.findViewById(R.id.wind);
        //  rain = (TextView)holder.findViewById(R.id.rain_data);

        imageView = (ImageView) holder.findViewById(R.id.forecast_image);
        weather_row = (RelativeLayout) holder.findViewById(R.id.weather_row);

        // date.setText(mDataset.get(position).getDate());
        forecast_date.setText(mDataset.get(position).getDate());
        day.setText(mDataset.get(position).getDay());
        //  humidity.setText(mDataset.get(position).getHumidity());
        minTemp.setText(mDataset.get(position).getMinTemp());
        maxTemp.setText(mDataset.get(position).getMaxTemp());
        // weatherText.setText(mDataset.get(position).getWeatherText());
        //  wind.setText(mDataset.get(position).getWindSpeed());

        //Herojit COmment
        setImage(mDataset, position, imageView);
        //Herojit Add
//        setImage(mDataset.get(position).getImagepath(), imageView);

        //  rain.setText(mDataset.get(position).getRain());

        weather_row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /* String dat= mDataset.get(position).getDate();
                Intent in = new Intent(mContext, ForecastDetailActivity.class);
                in.putExtra("date",dat+"");
                mContext.startActivity(in);
                */
            }
        });
        return holder;
    }

    public void setImage(String ImagePath, ImageView imageView) {
        try {
            Picasso.with(mContext).load(ImagePath).into(imageView);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setImage(ArrayList<TempBean> mDataset2, int position, ImageView img) {

        try {
            Picasso.with(mContext).load(mDataset2.get(position).getImagepath()).into(img);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

//        if (mDataset2.get(position).getImageType().equalsIgnoreCase("1")) {
//            img.setImageResource(R.drawable.img_1);
//        } else if (mDataset2.get(position).getImageType().equalsIgnoreCase("2")) {
//            img.setImageResource(R.drawable.img_2);
//        } else if (mDataset2.get(position).getImageType().equalsIgnoreCase("3")) {
//            img.setImageResource(R.drawable.img_3);
//        } else if (mDataset2.get(position).getImageType().equalsIgnoreCase("4")) {
//            img.setImageResource(R.drawable.img_4);
//        } else if (mDataset2.get(position).getImageType().equalsIgnoreCase("5")) {
//            img.setImageResource(R.drawable.img_5);
//        } else if (mDataset2.get(position).getImageType().equalsIgnoreCase("6")) {
//            img.setImageResource(R.drawable.img_6);
//        } else {
//            img.setImageResource(R.drawable.img_1);
//        }
    }
}
