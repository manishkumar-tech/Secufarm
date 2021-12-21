package com.weather.risk.mfi.myfarminfo.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.activities.DiseaseDisgnosis;

import org.json.JSONArray;
import org.json.JSONObject;

import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.itemSelectedCotton_Disease;
import static com.weather.risk.mfi.myfarminfo.utils.AppManager.getDiseaseDignosisImageURL;


public class DiseaseDiagnosisImageAdapter extends RecyclerView.Adapter<DiseaseDiagnosisImageAdapter.ViewHolder> {
    public Context mContext;
    public JSONArray jsonvalue;
    LinearLayout linearLayout;
    ImageView imageview;
    TextView txt_Imagetitle;
    String DiagnosisId;
    Activity activity;

    // Provide a suitable constructor (depends on the kind of dataset)
    public DiseaseDiagnosisImageAdapter(Context con, JSONArray value, LinearLayout llayout,
                                        ImageView imview, TextView imagetitle, String Diagnosisid, Activity activi) {

        mContext = con;
        this.jsonvalue = value;
        this.linearLayout = llayout;
        this.imageview = imview;
        this.txt_Imagetitle = imagetitle;
        this.DiagnosisId = Diagnosisid;
        this.activity = activi;
    }


    public void remove(int pos) {
        notifyItemRemoved(pos);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.diseasediagnosisimageadapter, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        try {
            final String ImageName, IMAGEURL;
            String Imgname = "";

            Imgname = jsonvalue.get(position).toString();
            IMAGEURL = getDiseaseDignosisImageURL(DiagnosisId, Imgname);
            if (Imgname != null) {
                Imgname = Imgname.replace(".jpeg", "");
                Imgname = Imgname.replace(".jpg", "");
                Imgname = Imgname.replace(".png", "");
                ImageName = Imgname;
            } else {
                ImageName = null;
            }

            Picasso.with(mContext).load(IMAGEURL).into(holder.imgview_diseaseimage);
            holder.txt_imgeURL.setText(IMAGEURL);
            holder.txt_imgeName.setText(Imgname);

            holder.imgview_diseaseimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //             JSONArray jsonvalue;
//            LinearLayout linearLayout;
//            ImageView imageview;
//            TextView txt_Imagetitle;
                    DiseaseDisgnosis.ImagePopup(linearLayout, imageview, txt_Imagetitle, ImageName, IMAGEURL,activity);
                }
            });


        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return jsonvalue.length();
    }

    // Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView txt_imgeURL, txt_imgeName;
        ImageView imgview_diseaseimage;


        public ViewHolder(View v) {
            super(v);
            txt_imgeURL = (TextView) v.findViewById(R.id.txt_imgeURL);
            txt_imgeName = (TextView) v.findViewById(R.id.txt_imgeName);
            imgview_diseaseimage = (ImageView) v.findViewById(R.id.imgview_diseaseimage);

        }
    }

    public void setSelectedCheckIDList(String values, JSONArray addnewcall_new) {
        try {
            String value = values;
            if (value != null && value.length() > 0) {
                value = value.replace("{", "");
                value = value.replace("}", "");
                String[] array = value.split(",");
                String newselectedvalue = itemSelectedCotton_Disease;
                for (int i = 0; i < array.length; i++) {
                    String subarray = array[i].trim();
                    String[] newarray = subarray.split("=");
                    int position = Integer.valueOf(newarray[0]);
                    JSONObject obj = new JSONObject(addnewcall_new.get(position).toString());
                    int valueId = Integer.parseInt(obj.get("Id").toString());
                    if (newarray[1].equals("true")) {
                        if (newselectedvalue != null && newselectedvalue.length() > 0) {
                            newselectedvalue = newselectedvalue + "," + valueId;
                        } else {
                            newselectedvalue = String.valueOf(valueId);
                        }
                    } else {
                        if (newselectedvalue != null && newselectedvalue.length() > 0) {
                            String[] newarray1 = newselectedvalue.split(",");
                            for (int j = 0; j < newarray1.length; j++) {
                                if (newarray1.length > 1) {
                                    if (newarray1[j] != String.valueOf(valueId)) {
                                        if (newselectedvalue != null && newselectedvalue.length() > 0) {
                                            newselectedvalue = newselectedvalue + "," + valueId;
                                        } else {
                                            newselectedvalue = String.valueOf(valueId);
                                        }
                                    } else {
                                        newselectedvalue = "";
                                    }
                                } else {
                                    newselectedvalue = "";
                                }
                            }
                        }
                    }
                }
                itemSelectedCotton_Disease = newselectedvalue;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
