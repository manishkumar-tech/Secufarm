package com.weather.risk.mfi.myfarminfo.dabwali;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.bean.NDBI_Bean;
import com.weather.risk.mfi.myfarminfo.home.Moisture_TouchValue;
import com.weather.risk.mfi.myfarminfo.pepsico.MoistureAdapter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DabwaliMoistureAdapter  extends RecyclerView.Adapter<DabwaliMoistureAdapter.ViewHolder> {
    private ArrayList<NDBI_Bean> mDataset = new ArrayList<NDBI_Bean>();

    public Context mContext;
    String imageString;
    TextToSpeech t1;

    // Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView date,ndviValue;
        public ImageView imageView;



        public ViewHolder(View v) {
            super(v);
            date = (TextView) v.findViewById(R.id.date_ndvi);
            ndviValue = (TextView) v.findViewById(R.id.ndvi_value);

            imageView = (ImageView)v.findViewById(R.id.ndvi_image);

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
    public DabwaliMoistureAdapter(Context con, ArrayList<NDBI_Bean> myDataset) {
        mDataset = myDataset;
        mContext = con;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ndvi_row, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {


        // holder.setIsRecyclable(false);


        holder.date.setText(mDataset.get(position).getDate());
        holder.ndviValue.setText(mDataset.get(position).getVillage_mean());

        String img = mDataset.get(position).getFinal_soilImg();
        if (img!=null && img.length()>5){

            Picasso.with(mContext).load(img).into(holder.imageView);
        }

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String imgUrl = mDataset.get(position).getCaptcha_Img();
                String contour = mDataset.get(position).getFarm_Data();
                String dt = mDataset.get(position).getDate();
                if (dt!=null){

                    @SuppressLint("SimpleDateFormat") DateFormat inputFormat = new SimpleDateFormat("dd MMM yyyy");
                    @SuppressLint("SimpleDateFormat") DateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");

                    Date date = null;
                    String outputDateStr = null;
                    try {
                        date = inputFormat.parse(dt);
                        outputDateStr = outputFormat.format(date);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (imgUrl!=null && imgUrl.length()>10) {

                        Intent in = new Intent(mContext, Moisture_TouchValue.class);
                        in.putExtra("image",imgUrl);
                        in.putExtra("contour",contour);
                        in.putExtra("date",outputDateStr);
                        mContext.startActivity(in);
                    }

                }else {
                    Toast.makeText(mContext,"Date Not Found",Toast.LENGTH_SHORT).show();
                }




            }
        });



    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    ProgressDialog dialog;
    SharedPreferences prefs;




}