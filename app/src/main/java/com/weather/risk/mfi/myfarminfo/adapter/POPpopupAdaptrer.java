package com.weather.risk.mfi.myfarminfo.adapter;

import static com.weather.risk.mfi.myfarminfo.utils.Utility.setDynamicLanguage;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setFontsStyleTxt;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.bean.PopBean;
import com.weather.risk.mfi.myfarminfo.home.POPUpdates_ImageUploadDialog;
import com.weather.risk.mfi.myfarminfo.utils.AppConstant;
import com.weather.risk.mfi.myfarminfo.utils.AppManager;
import com.weather.risk.mfi.myfarminfo.utils.UtilFonts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class POPpopupAdaptrer extends RecyclerView.Adapter<POPpopupAdaptrer.ViewHolder> {
    public static final String IMAGE_EXTENSION = "jpg";
    public static final int MEDIA_TYPE_IMAGE = 1;
    public Context mContext;
    String imageString;
    int totalAmount = 0;
    String statusType = null;
    //Add Herojit
    String imageString1, imageString2;
    int Imageselectflag = 0;
    ArrayList<String> imageList = new ArrayList<String>();
    private ArrayList<PopBean> mDataset = new ArrayList<PopBean>();
    private String imageStoragePath1 = "", imageStoragePath2 = "";
    private String userChoosenTask;
    private int REQUEST_CAMERA_START1 = 0, SELECT_FILE_START1 = 1;
    private int REQUEST_CAMERA_START2 = 3, SELECT_FILE_START2 = 2;

    // Provide a suitable constructor (depends on the kind of dataset)
    public POPpopupAdaptrer(Context con, ArrayList<PopBean> myDataset, String stat) {
        mDataset = myDataset;
        mContext = con;
        statusType = stat;
    }

    public POPpopupAdaptrer(Context con) {
        mContext = con;
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

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.pop_row, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {


        // holder.setIsRecyclable(false);
        UtilFonts.UtilFontsInitialize(mContext);
        holder.title.setTypeface(UtilFonts.KT_Bold);
        holder.imageupload_btn.setTypeface(UtilFonts.KT_Bold);
        holder.doneBTN.setTypeface(UtilFonts.KT_Bold);
        holder.value.setTypeface(UtilFonts.KT_Regular);

        holder.title.setText(mDataset.get(position).getTitle());
        String stri = mDataset.get(position).getValue();

        Log.v("vishal", stri + "");
        holder.value.setText(Html.fromHtml(mDataset.get(position).getValue()));

        setFontsStyleTxt(mContext, holder.title, 4);
        setFontsStyleTxt(mContext, holder.value, 7);
        setFontsStyleTxt(mContext, holder.imageupload_btn, 5);
        setDynamicLanguage(mContext,holder.imageupload_btn,"submit",R.string.Submit);


        if (statusType != null && statusType.equalsIgnoreCase("no")) {
            holder.imageupload_btn.setVisibility(View.VISIBLE);
//            holder.doneBTN.setVisibility(View.VISIBLE);
        } else {
            holder.imageupload_btn.setVisibility(View.GONE);
//            holder.doneBTN.setVisibility(View.GONE);
        }



        holder.doneBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (AppConstant.farm_id != null) {

                    String abcs = mDataset.get(position).getId();

                    if (abcs != null && !abcs.equalsIgnoreCase("null")) {

                        String jsonParameterString = createJsonParameterForPOP_Done(AppConstant.farm_id, abcs);
                        String createdString = AppManager.getInstance().removeSpaceForUrl(jsonParameterString);
                        Log.v("remove_done_pop_req", createdString);

                        new completePOP(createdString).execute();
                    }
                } else {
                    Toast.makeText(mContext, "Please select any farm", Toast.LENGTH_SHORT).show();
                }

            }
        });
        holder.imageupload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (AppConstant.farm_id != null) {
                    Intent in = new Intent(mContext.getApplicationContext(), POPUpdates_ImageUploadDialog.class);
                    in.putExtra("mDataset", (ArrayList<PopBean>) mDataset);
                    in.putExtra("position", position);
                    mContext.startActivity(in);
//                    String abcs = mDataset.get(position).getId();
//
//                    if (abcs != null && !abcs.equalsIgnoreCase("null")) {
//
//                        String jsonParameterString = createJsonParameterForPOP_Done(AppConstant.farm_id, abcs);
//                        String createdString = AppManager.getInstance().removeSpaceForUrl(jsonParameterString);
//                        Log.v("remove_done_pop_req", createdString);
//
//                        new completePOP(createdString).execute();
//                    }
                } else {
                    Toast.makeText(mContext, mContext.getResources().getString(R.string.Pleaseselectyour), Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    private String createJsonParameterForPOP_Done(String farmId, String workId) {
        String parameterString = "";


        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("FarmID", farmId);
            JSONArray jsonArray = new JSONArray();

            JSONObject jb = new JSONObject();
            jb.put("WorkID", workId);
            jb.put("Status", "yes");
            jb.put("Pop_Images", null);
            jsonArray.put(jb);

            jsonObject.put("msg", "" + jsonArray);


            Log.v("pop_request_done", jsonObject.toString() + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        parameterString = jsonObject.toString();

        return parameterString;
    }


    // Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView title, value, doneBTN, imageupload_btn;
        LinearLayout row;

        ImageView choose_image1, choose_image2;

        public ViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.ttl);
            value = (TextView) v.findViewById(R.id.vl);
            doneBTN = (TextView) v.findViewById(R.id.done_btn);
            imageupload_btn = (TextView) v.findViewById(R.id.imageupload_btn);
            choose_image1 = (ImageView) v.findViewById(R.id.choose_image1);
            choose_image2 = (ImageView) v.findViewById(R.id.choose_image2);
        }
    }

    private class completePOP extends AsyncTask<Void, Void, String> {

        String result = null;
        String createdString;
        ProgressDialog progressDialog;

        public completePOP(String createdString) {
            this.createdString = createdString;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(mContext);
            progressDialog.setMessage("Processing ... ");
            progressDialog.setCancelable(true);
            progressDialog.setIndeterminate(true);
            progressDialog.show();

        }

        @Override
        protected String doInBackground(Void... params) {
            String response = null;
            String sendPath = AppManager.getInstance().pop_done_API;

            response = AppManager.getInstance().httpRequestPutMethodReport(sendPath, createdString);

            System.out.println("AllFarmResponse :---" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPreExecute();
            progressDialog.dismiss();
            try {
                response = response.trim();
                // response = response.substring(1, response.length() - 1);
                response = response.replace("\\", "");
                response = response.replace("\"[", "[");
                response = response.replace("]\"", "]");
                System.out.println("All Farm Response : " + response);

                JSONObject jb = new JSONObject(response);


            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(mContext, mContext.getResources().getString(R.string.ResponseFormattingError), Toast.LENGTH_LONG).show();
            }
        }
    }

}