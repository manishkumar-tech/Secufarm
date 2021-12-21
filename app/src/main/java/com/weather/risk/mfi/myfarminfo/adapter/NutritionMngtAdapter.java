package com.weather.risk.mfi.myfarminfo.adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.bean.NutritionMngt;
import com.weather.risk.mfi.myfarminfo.utils.UtilFonts;

import org.json.JSONArray;

import java.util.ArrayList;

import static com.weather.risk.mfi.myfarminfo.utils.AppManager.WeedMngtImageURL;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setDynamicLanguage;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setFontsStyleTxt;

public class NutritionMngtAdapter extends RecyclerView.Adapter<NutritionMngtAdapter.ViewHolder> {
    public Context mContext;
    String imageString;
    int totalAmount = 0;
    private ArrayList<NutritionMngt> mDataset = new ArrayList<NutritionMngt>();
    Dialog dialog;
    int SelectedPosition = 0;

    // Provide a suitable constructor (depends on the kind of dataset)
    public NutritionMngtAdapter(Context con, ArrayList<NutritionMngt> myDataset) {
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.nutritionmngtadapter, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.txt_Title.setText(mDataset.get(position).getImageTitle());

        setFontsStyleTxt(mContext, holder.txt_Title, 2);

        String ImageURLs = mDataset.get(position).getImagefile();
        String ImageURL = "";
        try {
            ArrayList<String> data = new ArrayList<String>();
            JSONArray array = new JSONArray(ImageURLs);
            String Name = array.get(0).toString();
            ImageURL = WeedMngtImageURL + Name;
//            JSONArray array = new JSONArray(ImageURLs);
//            if (array.length() > 0) {
////                for (int i = 0; i < array.length(); i++) {
//                for (int i = 0; i < 1; i++) {
//                    String Name = array.get(i).toString();
//                    String imagePath = WeedMngtImageURL + Name;
//                    data.add(imagePath);
//                    ImageURL = WeedMngtImageURL + Name;
//                }
//            }
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
                SelectedPosition = position;
                ThumbnailNutritionImages(SelectedPosition);
            }
        });
    }

    public void ThumbnailNutritionImages(int position) {

        //final Dialog dialog = new Dialog(OtherUserProfile.this,android.R.style.Theme_Translucent_NoTitleBar);
//        final Dialog dialog = new Dialog(mContext);
        dialog = new Dialog(mContext);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.dimAmount = 0.7f;
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        // wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);

        // Include dialog.xml file
        dialog.setContentView(R.layout.nutrition_popup_details);

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        Button cancel = (Button) dialog.findViewById(R.id.btn_cross);
        ImageView imageValue = (ImageView) dialog.findViewById(R.id.image);
        ImageView btnPrevious = (ImageView) dialog.findViewById(R.id.btnPrevious);
        ImageView btnNext = (ImageView) dialog.findViewById(R.id.btnNext);
        TextView txt_title = (TextView) dialog.findViewById(R.id.txt_title);

        setFontsStyleTxt(mContext, txt_title, 2);

        UtilFonts.UtilFontsInitialize(mContext);
        txt_title.setTypeface(UtilFonts.FS_Ultra);
        cancel.setTypeface(UtilFonts.KT_Medium);
        try {
            String title = mDataset.get(position).getImageTitle();
            String ImageURLs = mDataset.get(position).getImagefile();
            String ImageURL = "";
            try {
                ArrayList<String> data = new ArrayList<String>();
                JSONArray array = new JSONArray(ImageURLs);
                String Name = array.get(0).toString();
                ImageURL = WeedMngtImageURL + Name;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            SelectedPosition = position;

            if (mDataset.size() > 1) {
                btnPrevious.setVisibility(View.GONE);
                btnNext.setVisibility(View.VISIBLE);
            }
            if (SelectedPosition >= 1) {
                btnPrevious.setVisibility(View.VISIBLE);
            }
            if (mDataset.size() == SelectedPosition + 1) {
                btnPrevious.setVisibility(View.VISIBLE);
                btnNext.setVisibility(View.GONE);
            }
            if (mDataset.size() == 1) {
                btnPrevious.setVisibility(View.GONE);
                btnNext.setVisibility(View.GONE);
            }

            setImageTextShow(txt_title, imageValue, title, ImageURL);
//            txt_title.setText(title);
//            if (ImageURL != null && ImageURL.length() > 6) {
//                imageValue.setVisibility(View.GONE);
////                        Picasso.with(NotificationPOPDetailsDialog.this).load(NotifImageURL).into(image);
//                Picasso.with(mContext).load(ImageURL)
//                        .into(imageValue, new com.squareup.picasso.Callback() {
//                            @Override
//                            public void onSuccess() {
//                                imageValue.setVisibility(View.VISIBLE);
//                            }
//
//                            @Override
//                            public void onError() {
////                                image_popup.setVisibility(View.GONE);
//                            }
//                        });
//            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
//                dialog.cancel();
                    SelectedPosition--;
                    String title = mDataset.get(SelectedPosition).getImageTitle();
                    String ImageURLs = mDataset.get(SelectedPosition).getImagefile();
                    String ImageURL = "";
                    try {
                        ArrayList<String> data = new ArrayList<String>();
                        JSONArray array = new JSONArray(ImageURLs);
                        String Name = array.get(0).toString();
                        ImageURL = WeedMngtImageURL + Name;
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    setImageTextShow(txt_title, imageValue, title, ImageURL);

                    if (SelectedPosition == 0) {
                        btnPrevious.setVisibility(View.GONE);
//                    btnNext.setVisibility(View.GONE);
                    }
                    btnNext.setVisibility(View.VISIBLE);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
//                dialog.show();
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                dialog.cancel();
                try {
                    SelectedPosition++;
                    String title = mDataset.get(SelectedPosition).getImageTitle();
                    String ImageURLs = mDataset.get(SelectedPosition).getImagefile();
                    String ImageURL = "";
                    try {
                        ArrayList<String> data = new ArrayList<String>();
                        JSONArray array = new JSONArray(ImageURLs);
                        String Name = array.get(0).toString();
                        ImageURL = WeedMngtImageURL + Name;
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    setImageTextShow(txt_title, imageValue, title, ImageURL);
                    if (mDataset.size() == SelectedPosition + 1) {
//                    btnPrevious.setVisibility(View.GONE);
                        btnNext.setVisibility(View.GONE);
                    }
                    btnPrevious.setVisibility(View.VISIBLE);
//                dialog.show();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });


        dialog.show();
    }

    public void setImageTextShow(TextView textView, ImageView imageValue, String textalue, String ImageURL) {
        textView.setText(textalue);
        if (ImageURL != null && ImageURL.length() > 6) {
//            imageValue.setVisibility(View.GONE);
//                        Picasso.with(NotificationPOPDetailsDialog.this).load(NotifImageURL).into(image);
            Picasso.with(mContext).load(ImageURL)
                    .into(imageValue, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            imageValue.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onError() {
//                                image_popup.setVisibility(View.GONE);
                        }
                    });
        }
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
        public ImageView imageview;
        public TextView txt_Title;

        public ViewHolder(View v) {
            super(v);
            imageview = (ImageView) v.findViewById(R.id.imageview);
            txt_Title = (TextView) v.findViewById(R.id.txt_Title);
        }
    }


}