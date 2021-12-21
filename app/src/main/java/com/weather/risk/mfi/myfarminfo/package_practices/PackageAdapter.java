package com.weather.risk.mfi.myfarminfo.package_practices;

import static com.weather.risk.mfi.myfarminfo.utils.Utility.getDynamicLanguageToast;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getDynamicLanguageValue;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setFontsStyleTxt;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.adapter.POPpopupAdaptrer;
import com.weather.risk.mfi.myfarminfo.bean.PackageBean;
import com.weather.risk.mfi.myfarminfo.bean.PopBean;
import com.weather.risk.mfi.myfarminfo.utils.AppConstant;
import com.weather.risk.mfi.myfarminfo.utils.AppManager;
import com.weather.risk.mfi.myfarminfo.utils.TransparentProgressDialog;
import com.weather.risk.mfi.myfarminfo.utils.UtilFonts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Admin on 25-04-2018.
 */
public class PackageAdapter extends RecyclerView.Adapter<PackageAdapter.ViewHolder> {
    public Context mContext;
    String idddd = "0";
    private ArrayList<PackageBean> mDataset = new ArrayList<PackageBean>();
    private ArrayList<String> titleList = new ArrayList<String>();
    private ArrayList<String> workIdList = new ArrayList<String>();
    private ArrayList<String> statusList = new ArrayList<String>();
    private ArrayList<String> FlagID = new ArrayList<String>();
    String OnlyCurrentStatus;


    // Provide a suitable constructor (depends on the kind of dataset)
    public PackageAdapter(Context con, ArrayList<PackageBean> myDataset,
                          ArrayList<String> titleList1, ArrayList<String> titleList11,
                          ArrayList<String> stList, ArrayList<String> Flagid, String onlyCurrentStatus) {
        mDataset = myDataset;
        mContext = con;
        titleList = titleList1;
        workIdList = titleList11;
        statusList = stList;
        FlagID = Flagid;
        OnlyCurrentStatus = onlyCurrentStatus;

    }

      /*public void add(int position, String item) {
          mDataset.add(position, item);00.....
          notifyItemInserted(position);
      }*/

    public void remove(int pos) {
        //   int position = mDataset.indexOf(item);
        titleList.remove(pos);
        notifyItemRemoved(pos);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.package_row, parent, false);

        Log.v("lengthhh", titleList.size() + "");

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {


        setFontsStyleTxt(mContext, holder.title, 7);
        setFontsStyleTxt(mContext, holder.status, 2);

        final String ttt = titleList.get(position);
        String FLAG = FlagID.get(position);
//        //Herojit Add
        int flag = 1;
        if (FLAG != null && !FLAG.equalsIgnoreCase("")) {
            flag = Integer.valueOf(FLAG);
        }


        holder.title.setText(ttt);
        if (statusList.size() > position) {
            String aaaa = statusList.get(position);
            if (aaaa.equalsIgnoreCase("no")) {
                holder.status.setText(getDynamicLanguageValue(mContext, "Cancel", R.string.Cancel));
            } else {
                flag = 1;
                holder.status.setText(getDynamicLanguageValue(mContext, "Completed", R.string.Completed));
            }
        }

        holder.row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String aaaa = statusList.get(position);
                if (aaaa.equalsIgnoreCase("no")) {
                    popupPackageData(ttt, "no");
                } else {
                    popupPackageData(ttt, "yes");
                }

            }
        });
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String aaaa = statusList.get(position);
                if (aaaa.equalsIgnoreCase("no")) {
                    popupPackageData(ttt, "no");
                } else {
                    popupPackageData(ttt, "yes");
                }

            }
        });
        holder.status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String aaaa = statusList.get(position);
                if (aaaa.equalsIgnoreCase("no")) {
                    popupPackageData(ttt, "no");
                } else {
                    popupPackageData(ttt, "yes");
                }

            }
        });

        switch (flag) {
            case 1:
                holder.status.setBackgroundColor(mContext.getResources().getColor(R.color.gray));
//                holder.status.setText(mContext.getResources().getString(R.string.Completed));
                break;
            case 2:
                holder.status.setBackgroundColor(mContext.getResources().getColor(R.color.orange));
//                holder.status.setText(mContext.getResources().getString(R.string.Pending));
                break;
            case 3:
                holder.status.setBackgroundColor(mContext.getResources().getColor(R.color.ColorPrimary));
//                holder.status.setText(mContext.getResources().getString(R.string.Pending));
                break;
            default:
                holder.status.setBackgroundColor(mContext.getResources().getColor(R.color.ColorPrimary));
//                holder.status.setText(mContext.getResources().getString(R.string.Pending));
                break;
        }

        if (OnlyCurrentStatus != null && OnlyCurrentStatus.length() > 0) {
//            if (OnlyCurrentStatus.equalsIgnoreCase("OnlyCurrentStatus_true")) {
////                if (flag == 2)
////                    holder.hhhh.setVisibility(View.VISIBLE);
////                else
////                    holder.hhhh.setVisibility(View.GONE);
//            } else {//OnlyCurrentStatus_false
//            //Normal condition
//            }
            if (ttt.equalsIgnoreCase(OnlyCurrentStatus)) {
                holder.hhhh.setVisibility(View.VISIBLE);
                String aaaa = statusList.get(position);
                if (aaaa.equalsIgnoreCase("no")) {
                    popupPackageData(ttt, "no");
                } else {
                    popupPackageData(ttt, "yes");
                }
            } else {
                holder.hhhh.setVisibility(View.GONE);
            }
        }


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return titleList.size();
    }

    public void popupPackageData(String titl, String asd) {

        final Dialog dialog = new Dialog(mContext, R.style.DialogSlideAnim);

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
        dialog.setContentView(R.layout.pop_popup);

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
//        if (mDataset.size() > 4) {
//            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, 1150);
//        } else {
//            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, 650);
//        }

        RecyclerView listView = (RecyclerView) dialog.findViewById(R.id.pop_list);
        TextView heading = (TextView) dialog.findViewById(R.id.heading);
        TextView popStatusBTN = (TextView) dialog.findViewById(R.id.done_pop);

        setFontsStyleTxt(mContext, heading, 2);
        setFontsStyleTxt(mContext, popStatusBTN, 5);

        if (asd != null && asd.equalsIgnoreCase("no")) {
            popStatusBTN.setVisibility(View.VISIBLE);
        } else {
            popStatusBTN.setVisibility(View.GONE);
        }

        heading.setText("" + titl);
        listView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        listView.setLayoutManager(mLayoutManager);


        ImageView image = (ImageView) dialog.findViewById(R.id.package_image);


        String img = null;
        String val = null;
        String stg = null;
        ArrayList<PopBean> listPOP = new ArrayList<PopBean>();

        idddd = "0";

        for (int i = 0; i < mDataset.size(); i++) {

            String ssttgg = mDataset.get(i).getStage();

            if (ssttgg != null && ssttgg.equalsIgnoreCase(titl)) {
                PopBean bean = new PopBean();

                idddd = mDataset.get(i).getWorkId();

                img = mDataset.get(i).getImage();
                val = mDataset.get(i).getWork();
                stg = mDataset.get(i).getWork_name();
                if (val != null && val.length() > 4) {
                    bean.setTitle(stg);
                    bean.setValue(val);
                    bean.setId(idddd);
                    bean.setImage(img);
                    listPOP.add(bean);
                }
            }
        }


        popStatusBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("popID", idddd + "");

                if (AppConstant.farm_id != null) {

                    dialog.cancel();
                    String jsonParameterString = createJsonParameterForPOP_Done(AppConstant.farm_id, idddd);
                    String createdString = AppManager.getInstance().removeSpaceForUrl(jsonParameterString);
                    Log.v("remove_done_pop_req", createdString);

                    new completePOP(createdString).execute();
                } else {
                    getDynamicLanguageToast(mContext, "Pleaseselectanyfarm", R.string.Pleaseselectanyfarm);
                }

            }
        });


        if (img != null) {
            Log.v("image", img + "");
            if (img.length() > 10) {
                image.setVisibility(View.VISIBLE);
                String src = img.substring(10, img.length() - 4);
                Log.v("image_parse", src + "");
                Picasso.with(mContext).load("https://myfarminfo.com" + src).error(R.drawable.no_image).into(image);
            } else {
                image.setVisibility(View.GONE);
            }
        }

        if (listPOP.size() > 0) {
            POPpopupAdaptrer adapter = new POPpopupAdaptrer(mContext, listPOP, asd);
            listView.setAdapter(adapter);

            if (listPOP.size() > 1) {
                popStatusBTN.setText((getDynamicLanguageValue(mContext, "AllActivityDone", R.string.AllActivityDone)));
            } else {
                popStatusBTN.setText((getDynamicLanguageValue(mContext, "ActivityDone", R.string.ActivityDone)));
            }
        }


        dialog.show();
    }

    private String createJsonParameterForPOP_Done(String farmId, String workId) {
        String parameterString = "";


        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("FarmID", farmId);
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < workIdList.size(); i++) {
                JSONObject jb = new JSONObject();
                if (workIdList.get(i).equalsIgnoreCase(workId)) {
                    jb.put("WorkID", workIdList.get(i));
                    jb.put("Status", "yes");
                    jb.put("Pop_Images", "null");
                } else {
                    jb.put("WorkID", workIdList.get(i));
                    jb.put("Status", statusList.get(i));
                    jb.put("Pop_Images", "null");
                }
                jsonArray.put(jb);
            }

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
        public TextView title, status;
        public TableRow row;
        public CardView hhhh;


        public ViewHolder(View v) {
            super(v);
            row = (TableRow) v.findViewById(R.id.package_row);
            title = (TextView) v.findViewById(R.id.title_show);
            status = (TextView) v.findViewById(R.id.done_btn);
            hhhh = (CardView) v.findViewById(R.id.hhhh);

        }
    }

    private class completePOP extends AsyncTask<Void, Void, String> {

        String result = null;
        String createdString;
        TransparentProgressDialog progressDialog;

        public completePOP(String createdString) {
            this.createdString = createdString;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new TransparentProgressDialog(mContext,
                    getDynamicLanguageValue(mContext, "ActivityDone", R.string.Dataisloading));
            progressDialog.show();

        }

        @Override
        protected String doInBackground(Void... params) {
            String response = null;
            String sendPath = AppManager.getInstance().pop_done_API;
//            createdString = createdString.replace("\\", "");
//            createdString = createdString.replace("\"[", "[");
//            createdString = createdString.replace("]\"", "]");
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
                String result = jb.getString("UpdateFarmPopResult");
                if (result != null && result.length() > 2 && result.equalsIgnoreCase("Success")) {
                    getDynamicLanguageToast(mContext, "SubmittedSuccessfully", R.string.SubmittedSuccessfully);
                } else {
                    getDynamicLanguageToast(mContext, "ResponseFormattingError", R.string.ResponseFormattingError);
                }

            } catch (Exception e) {
                e.printStackTrace();
                getDynamicLanguageToast(mContext, "ResponseFormattingError", R.string.ResponseFormattingError);
            }


        }


    }


}