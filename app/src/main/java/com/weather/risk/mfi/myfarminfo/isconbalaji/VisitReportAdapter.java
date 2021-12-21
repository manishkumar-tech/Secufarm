package com.weather.risk.mfi.myfarminfo.isconbalaji;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
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
import com.weather.risk.mfi.myfarminfo.bean.VisitReportBean;

import java.util.ArrayList;

public class VisitReportAdapter extends RecyclerView.Adapter<VisitReportAdapter.ViewHolder> {


    private ArrayList<VisitReportBean> mDataset = new ArrayList<VisitReportBean>();
    private ArrayList<VisitReportBean> listSuggesion = new ArrayList<VisitReportBean>();

    public Context mContext;


    // Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView famerName, farmName, village, subDistrict, visitorName, visitDate, visitLatitude, visitLongitude, images, distance;


        public ViewHolder(View v) {
            super(v);
            famerName = (TextView) v.findViewById(R.id.farmer_name_visit);
            farmName = (TextView) v.findViewById(R.id.farm_name_visit);
            village = (TextView) v.findViewById(R.id.village_visit);
            subDistrict = (TextView) v.findViewById(R.id.sub_district_visit);
            visitorName = (TextView) v.findViewById(R.id.visitor_name_visit);
            visitDate = (TextView) v.findViewById(R.id.visit_date_visit);
            visitLatitude = (TextView) v.findViewById(R.id.visit_latitude_visit);
            visitLongitude = (TextView) v.findViewById(R.id.visit_longitude_visit);
            images = (TextView) v.findViewById(R.id.image_visit);
            distance = (TextView) v.findViewById(R.id.distance_farm_visit);
        }
    }


    public void remove(int pos) {
        //   int position = mDataset.indexOf(item);
        mDataset.remove(pos);
        notifyItemRemoved(pos);
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public VisitReportAdapter(Context con, ArrayList<VisitReportBean> myDataset) {
        mDataset = myDataset;
        listSuggesion = myDataset;
        mContext = con;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.visit_report_row, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // holder.setIsRecyclable(false);
        holder.famerName.setText(mDataset.get(position).getFarmername());
        holder.farmName.setText(mDataset.get(position).getFarmname());
        holder.village.setText(mDataset.get(position).getVillage());
        holder.subDistrict.setText(mDataset.get(position).getSub_district());
        holder.visitorName.setText(mDataset.get(position).getVisitor_Name());
        holder.visitDate.setText(mDataset.get(position).getVisit_Date());
        holder.visitLatitude.setText(mDataset.get(position).getVisit_Latitude());
        holder.visitLongitude.setText(mDataset.get(position).getVisit_Longitude());

        holder.images.setText("Images");
        holder.distance.setText(mDataset.get(position).getDisFromFarm());

        holder.images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String images = mDataset.get(position).getImages();
                Log.v("imagesss",images+"");
                ArrayList<String> imageList = new ArrayList<String>();
                if (images != null && images.length() > 10) {
                    String[] split1 = images.split("\\$");
                    for (int i = 0; i < split1.length; i++) {
                        if (split1[i] != null && split1[i].length() > 10) {
                            String[] split2 = split1[i].split("#");
                            if (split2.length > 1) {
                                imageList.add(split2[1]);
                            }

                        }
                    }
                }

                if (imageList.size() > 1) {
                    imagePop(imageList);
                } else if (imageList.size() == 1) {
                    imagePopUpMethod(imageList.get(0));
                }
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    public void imagePop(ArrayList<String> imgggList) {
        final Dialog dialog = new Dialog(mContext);

        dialog.setCanceledOnTouchOutside(true);
        Window window = dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);


        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.BOTTOM;
        wlp.dimAmount = 1.0f;

        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        // wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        // Include dialog.xml file
        dialog.setContentView(R.layout.image_grid_layout);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);


        dialog.show();

        final ImageView cancel = (ImageView) dialog.findViewById(R.id.cancel_popup);
        final ImageView img1 = (ImageView) dialog.findViewById(R.id.image_popup1);
        final ImageView img2 = (ImageView) dialog.findViewById(R.id.image_popup2);
        final ImageView img3 = (ImageView) dialog.findViewById(R.id.image_popup3);
        final ImageView img4 = (ImageView) dialog.findViewById(R.id.image_popup4);


        for (int i = 0; i < imgggList.size(); i++) {
            if (i == 0) {
                final String imggg = imgggList.get(0);
                if (imggg != null && imggg.length() > 10) {
                    Picasso.with(mContext).load(imggg).into(img1);
                    img1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            imagePopUpMethod(imggg);
                        }
                    });
                }
            } else if (i == 1) {
                final String imggg = imgggList.get(1);
                if (imggg != null && imggg.length() > 10) {

                    Picasso.with(mContext).load(imggg).into(img2);
                    img2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            imagePopUpMethod(imggg);
                        }
                    });
                }
            } else if (i == 2) {
                final String imggg = imgggList.get(2);
                if (imggg != null && imggg.length() > 10) {

                    Picasso.with(mContext).load(imggg).into(img3);
                    img3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            imagePopUpMethod(imggg);
                        }
                    });
                }
            } else if (i == 3) {
                final String imggg = imgggList.get(3);
                if (imggg != null && imggg.length() > 10) {

                    Picasso.with(mContext).load(imggg).into(img4);
                    img4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            imagePopUpMethod(imggg);
                        }
                    });
                }
            }
        }

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.cancel();
            }
        });

    }


    public void imagePopUpMethod(String imgUrl) {
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


        if (imgUrl != null && imgUrl.length() > 10) {

            Picasso.with(mContext).load(imgUrl).into(img);
        }

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.cancel();
            }
        });

    }
}
