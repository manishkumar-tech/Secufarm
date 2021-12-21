package com.weather.risk.mfi.myfarminfo.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.bean.MandiPriceBean;
import com.weather.risk.mfi.myfarminfo.bean.Weed;
import com.weather.risk.mfi.myfarminfo.home.AppController;
import com.weather.risk.mfi.myfarminfo.utils.AppConstant;
import com.weather.risk.mfi.myfarminfo.utils.AppManager;

import java.util.ArrayList;

/**
 * Created by Admin on 06-02-2018.
 */
public class WeedAdapter extends RecyclerView.Adapter<WeedAdapter.ViewHolder> {
    private ArrayList<Weed> mDataset = new ArrayList<Weed>();

    public Context mContext;
    String imageString;
    int totalAmount = 0;

    // Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView botanicalName, englishName, commonName;
        public ImageView imageView;


        public ViewHolder(View v) {
            super(v);
            botanicalName = (TextView) v.findViewById(R.id.botanical_weed);
            englishName = (TextView) v.findViewById(R.id.english_weed);
            commonName = (TextView) v.findViewById(R.id.common_weed);
            imageView = (ImageView) v.findViewById(R.id.weed_image);


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
    public WeedAdapter(Context con, ArrayList<Weed> myDataset) {
        mDataset = myDataset;
        mContext = con;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.weed_row, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {


        // holder.setIsRecyclable(false);


        holder.botanicalName.setText(mDataset.get(position).getBotanical());
        holder.englishName.setText(mDataset.get(position).getEnglish());
        holder.commonName.setText(mDataset.get(position).getCommon());
        String imgg = mDataset.get(position).getImage();


        if (imgg != null && imgg.length() > 4) {
            String ssss = AppManager.getInstance().removeSpaceForUrl(imgg);
            String imgUrl = "https://myfarminfo.com/Tools/Img/AIS/weed/small/" + ssss;
            Glide.with(mContext).load(imgUrl)
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.imageView);
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

                String img1 = mDataset.get(position).getImage();

                if (img1 != null && img1.length() > 4) {
                    String ssss = AppManager.getInstance().removeSpaceForUrl(img1);
                    String imgUrl = "https://myfarminfo.com/Tools/Img/AIS/weed/large/" + ssss;

                    Glide.with(mContext).load(imgUrl)
                            .thumbnail(0.5f)
                            .crossFade()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(img);
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

    private void exitMethod(final int pos, final String docID) {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(mContext.getResources().getString(R.string.Exit)).
                setMessage(mContext.getResources().getString(R.string.Doyouwantdelete)).
                setPositiveButton(mContext.getResources().getString(R.string.Yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        remove(pos);
                    }
                }).
                setNegativeButton(mContext.getResources().getString(R.string.No), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();

    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }


}