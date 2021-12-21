package com.weather.risk.mfi.myfarminfo.pepsico;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.speech.tts.TextToSpeech;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.bean.NDBI_Bean;


import java.util.ArrayList;

/**
 * Created by Admin on 28-08-2017.
 */
public class MoistureAdapter extends RecyclerView.Adapter<MoistureAdapter.ViewHolder> {
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
    public MoistureAdapter(Context con, ArrayList<NDBI_Bean> myDataset) {
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

        String img = mDataset.get(position).getImage();
        if (img!=null && img.length()>5){

            Picasso.with(mContext).load(img).into(holder.imageView);
        }

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // final Dialog dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);

                final Dialog dialog = new Dialog(mContext);

                dialog.setCanceledOnTouchOutside(true);
                Window window = dialog.getWindow();
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);


                WindowManager.LayoutParams wlp = window.getAttributes();

                wlp.gravity = Gravity.BOTTOM;
                wlp.dimAmount = 0.5f;

                dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                // wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                window.setAttributes(wlp);
                // Include dialog.xml file
                dialog.setContentView(R.layout.screen_popup);
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);


                dialog.show();

                final ImageView cancel = (ImageView) dialog.findViewById(R.id.cancel_popup);
                final ImageView img = (ImageView) dialog.findViewById(R.id.image_popup);

                String imgUrl = mDataset.get(position).getImage();
                if (imgUrl!=null && imgUrl.length()>10) {

                    Picasso.with(mContext).load(imgUrl).into(img);
                }

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.cancel();
                    }
                });

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