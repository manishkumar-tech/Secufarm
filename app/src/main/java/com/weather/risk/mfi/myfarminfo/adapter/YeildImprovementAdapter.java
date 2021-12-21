package com.weather.risk.mfi.myfarminfo.adapter;

import android.app.Dialog;
import android.content.Context;
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
import com.weather.risk.mfi.myfarminfo.bean.WeedMngt;

import java.util.ArrayList;

public class YeildImprovementAdapter extends RecyclerView.Adapter<YeildImprovementAdapter.ViewHolder> {
    public Context mContext;
    String imageString;
    int totalAmount = 0;
    private ArrayList<WeedMngt> mDataset = new ArrayList<WeedMngt>();

    // Provide a suitable constructor (depends on the kind of dataset)
    public YeildImprovementAdapter(Context con, ArrayList<WeedMngt> myDataset) {
        mDataset = myDataset;
        mContext = con;

    }

    public void remove(int pos) {
        //   int position = mDataset.indexOf(item);
        mDataset.remove(pos);
        notifyItemRemoved(pos);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.weedmanagementadapter, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.txt_Title.setText(mDataset.get(position).getDiseaseInsectName());
        String ImageURL = mDataset.get(position).getImageName();
        try {
            if (ImageURL != null && ImageURL.length() > 6) {
                holder.imageview.setVisibility(View.GONE);
//                        Picasso.with(NotificationPOPDetailsDialog.this).load(NotifImageURL).into(image);
                Picasso.with(mContext).load(ImageURL)
                        .into(holder.imageview, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {
                                holder.imageview.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onError() {
//                                holder.imageview.setVisibility(View.GONE);
                            }
                        });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        holder.imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ImageURL = mDataset.get(position).getImageName();
                setPopUpImage(ImageURL);
            }
        });


        holder.txt_scientifictitle.setText(mContext.getResources().getString(R.string.Symptoms));
        holder.txt_scientificName.setText(mDataset.get(position).getScientificName());

        holder.txt_DamageIntensityTitle.setVisibility(View.GONE);
        holder.txt_DamageIntensity.setVisibility(View.GONE);


        holder.next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPopUpDescription(position);
            }
        });


    }

    public void setPopUpDescription(int pos) {
        final Dialog dialog = new Dialog(mContext);

        dialog.setCanceledOnTouchOutside(true);
        Window window = dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);


        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.CENTER;
        wlp.dimAmount = 0.5f;

        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        // wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        // Include dialog.xml file
//        dialog.setContentView(R.layout.weedadapter);
        dialog.setContentView(R.layout.weedmngt_popup);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);


        dialog.show();

        final ImageView cancel = (ImageView) dialog.findViewById(R.id.cancel_popup);

        TextView txttitle = (TextView) dialog.findViewById(R.id.txttitle);
        TextView txtpreemergency = (TextView) dialog.findViewById(R.id.txtpreemergency);
        TextView txt_PreEmer = (TextView) dialog.findViewById(R.id.txt_PreEmer);
        TextView txtpostemergency = (TextView) dialog.findViewById(R.id.txtpostemergency);
        TextView txt_PostEmer = (TextView) dialog.findViewById(R.id.txt_PostEmer);

        txtpostemergency.setVisibility(View.GONE);
        txt_PostEmer.setVisibility(View.GONE);

        txttitle.setText(mDataset.get(pos).getDiseaseInsectName());
        txtpreemergency.setText(mContext.getResources().getString(R.string.ControlMeasures));
        txt_PostEmer.setText(mDataset.get(pos).getDamageIntensity());

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.cancel();
            }
        });
    }

    public void setPopUpImage(String ImageURL) {
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
        final ImageView image_popup = (ImageView) dialog.findViewById(R.id.image_popup);

//        image_popup.setImageDrawable(mContext.getResources().getDrawable(mDataset.get(pos).getImageName()));

        try {
            if (ImageURL != null && ImageURL.length() > 6) {
                image_popup.setVisibility(View.GONE);
//                        Picasso.with(NotificationPOPDetailsDialog.this).load(NotifImageURL).into(image);
                Picasso.with(mContext).load(ImageURL)
                        .into(image_popup, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {
                                image_popup.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onError() {
//                                image_popup.setVisibility(View.GONE);
                            }
                        });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.cancel();
            }
        });
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView txt_Title, txt_scientifictitle, txt_scientificName, txt_DamageIntensityTitle, txt_DamageIntensity, next_btn;
        public ImageView imageview;
        LinearLayout ll_description;

        public ViewHolder(View v) {
            super(v);
            txt_Title = (TextView) v.findViewById(R.id.txt_Title);
            txt_scientifictitle = (TextView) v.findViewById(R.id.txt_scientifictitle);
            txt_scientificName = (TextView) v.findViewById(R.id.txt_scientificName);
            txt_DamageIntensityTitle = (TextView) v.findViewById(R.id.txt_DamageIntensityTitle);
            txt_DamageIntensity = (TextView) v.findViewById(R.id.txt_DamageIntensity);
            next_btn = (TextView) v.findViewById(R.id.next_btn);
            imageview = (ImageView) v.findViewById(R.id.imageview);
            ll_description = (LinearLayout) v.findViewById(R.id.ll_description);
        }
    }


}

