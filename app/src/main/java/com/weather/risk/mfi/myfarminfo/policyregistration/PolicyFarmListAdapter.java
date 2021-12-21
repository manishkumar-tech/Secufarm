package com.weather.risk.mfi.myfarminfo.policyregistration;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.databinding.PolicyfarmlistadapterBinding;
import com.weather.risk.mfi.myfarminfo.databinding.PolicylistfarmeradapterBinding;
import com.weather.risk.mfi.myfarminfo.databinding.ProductRowNewBinding;
import com.weather.risk.mfi.myfarminfo.marketplace.DataInterface;
import com.weather.risk.mfi.myfarminfo.pdfdownload.PDFDownloadTask;
import com.weather.risk.mfi.myfarminfo.retrofitmodelnew.CategoryDetailResponse;
import com.weather.risk.mfi.myfarminfo.utils.AppConstant;
import com.weather.risk.mfi.myfarminfo.utils.AppManager;
import com.weather.risk.mfi.myfarminfo.utils.Utility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.SelectFarmDetails_policy;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.GetRevisedFarmArea;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getDynamicLanguageValue;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setDynamicLanguage;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setFontsStyleTxt;

public class PolicyFarmListAdapter extends RecyclerView.Adapter<PolicyFarmListAdapter.MyViewHolder> implements Filterable {

    private List<UserFarmResponse> list;
    Context context;
    private LayoutInflater layoutInflater;
    AutoCompleteTextView search;
    private List<UserFarmResponse> filteredlistt;



    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtFarmID, txtFarmIDs, txtFarmerName, txtFarmerNames, txtFarm, txtFarms, txtFarmArea, txtFarmAreas;
        ImageView btnnext;


        public MyViewHolder(final PolicyfarmlistadapterBinding itemBinding) {
            super(itemBinding.getRoot());
            txtFarmID = itemBinding.txtFarmID;
            txtFarmIDs = itemBinding.txtFarmIDs;
            txtFarmerName = itemBinding.txtFarmerName;
            txtFarmerNames = itemBinding.txtFarmerNames;
            txtFarm = itemBinding.txtFarm;
            txtFarms = itemBinding.txtFarms;
            txtFarmArea = itemBinding.txtFarmArea;
            txtFarmAreas = itemBinding.txtFarmAreas;
            btnnext = itemBinding.btnnext;

        }
    }


    public PolicyFarmListAdapter(Context con, List<UserFarmResponse> sList, AutoCompleteTextView search) {
        this.list = sList;
        context = con;
        this.search =  search;
        this.filteredlistt = sList;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        PolicyfarmlistadapterBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.policyfarmlistadapter, parent, false);
        return new MyViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        try {
            if (Utility.setStringCheck(String.valueOf(filteredlistt.get(position).getId()))) {
                holder.txtFarmIDs.setText(String.valueOf(filteredlistt.get(position).getId()));
            }
            if (Utility.setStringCheck(String.valueOf(filteredlistt.get(position).getFarmerName()))) {
                holder.txtFarmerNames.setText(String.valueOf(filteredlistt.get(position).getFarmerName()));
            }
            if (Utility.setStringCheck(String.valueOf(filteredlistt.get(position).getFarmName()))) {
                holder.txtFarms.setText(String.valueOf(filteredlistt.get(position).getFarmName()));
            }
            if (Utility.setStringCheck(String.valueOf(filteredlistt.get(position).getArea()))) {
                try {
                    String FarmArea = String.valueOf(filteredlistt.get(position).getArea());
                    if (FarmArea != null && FarmArea.length() > 0 && FarmArea.contains(".")) {
                        String area[] = FarmArea.split("[.]");
                        int len = area.length;
                        if (!FarmArea.equalsIgnoreCase("0.0")) {
                            double roundfig = GetRevisedFarmArea(Double.parseDouble(FarmArea));
                            FarmArea = String.valueOf(roundfig);
                            holder.txtFarmAreas.setText(FarmArea);
                        } else {
                            holder.txtFarmAreas.setText("0.0");
                        }
                    } else if (FarmArea != null && FarmArea.length() > 0) {
                        holder.txtFarmAreas.setText(FarmArea);
                    } else {
                        holder.txtFarmAreas.setText("0.0");
                    }
//                        double roundfig = Double.parseDouble(area[1]);
//                        if (roundfig > 01 && roundfig < 25) {
//                            FarmArea = area[0] + ".25";
//                        } else if (roundfig > 25 && roundfig < 50) {
//                            FarmArea = area[0] + ".50";
//                        } else if (roundfig > 50 && roundfig < 75) {
//                            FarmArea = area[0] + ".50";
//                        } else if (roundfig > 75) {
//                            double fraction = Double.parseDouble(area[0]) + 1.00;
//                            FarmArea = String.valueOf(fraction);
//                        }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            holder.btnnext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    UserFarmResponse farm = filteredlistt.get(position);
                    AppConstant.stateID = farm.getStateID();
                    String farmid = String.valueOf(farm.getId());
                    new FarmdetailsAsynctask(farmid).execute();
                }
            });

            search.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    // TODO Auto-generated method stub
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    // TODO Auto-generated method stub
                }

                @Override
                public void afterTextChanged(Editable s) {

                    // filter your list from your input
                    //  filter(s.toString());
                    getFilter().filter(s.toString());
                    //you can use runnable postDelayed like 500 ms to delay search text
                }
            });

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return filteredlistt.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    filteredlistt = list;
                } else {
                    List<UserFarmResponse> filteredList = new ArrayList<>();
                    for (UserFarmResponse row : list) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or id match
                        if (row.getFarmName().trim().toLowerCase().contains(charString.toLowerCase()) || row.getFarmerName().trim().toLowerCase().contains(charString.toLowerCase())
                                || row.getId().toString().trim().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    filteredlistt = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredlistt;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredlistt = (ArrayList<UserFarmResponse>) filterResults.values;
                notifyDataSetChanged();
            }
        };

    }

//    public void updateList(List<UserFarmResponse> list){
//        this.list = list;
//        notifyDataSetChanged();
//    }
//
//
//    void filter(String text){
//        List<UserFarmResponse> temp = new ArrayList();
//        for(UserFarmResponse d: list){
//            //or use .equal(text) with you want equal match
//            //use .toLowerCase() for better matches
//            if(d.getFarmerName().contains(text)){
//                temp.add(d);
//            }
//        }
//        //update recyclerview
//        updateList(temp);
//    }

    private class FarmdetailsAsynctask extends AsyncTask<Void, Void, String> {
        String FarmID = "";

        public FarmdetailsAsynctask(String farmid) {
            FarmID = farmid;
        }

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage(context.getResources().getString(R.string.Dataisloading));
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(true);
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    // cancel AsyncTask
                    cancel(false);
                }
            });
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            String sendRequest = null;
            try {
                sendRequest = AppManager.getInstance().getFarmData + AppConstant.user_id + "/" + FarmID;
                String response = AppManager.getInstance().httpRequestGetMethod(sendRequest);
                return response;

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return sendRequest;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPreExecute();
            try {
                if (response != null && response.contains("FarmID")) {
                    progressDialog.dismiss();
                    Log.v("sacascascas", response);
                    if (response != null && response.contains("armerID")) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            SelectFarmDetails_policy = obj;
                            String FarmerID, farmid, ProjectID, farmerid, cropID = "", cropName = "";
                            FarmerID = obj.getString("FarmerID");
                            ProjectID = obj.getString("ProjectID");
                            farmid = obj.getString("FarmID");
                            if (obj.has("CropInfo")) {
                                JSONArray jsonArray = obj.getJSONArray("CropInfo");
                                if (jsonArray != null && jsonArray.length() > 0) {
                                    if (jsonArray.getJSONObject(0).has("CropID")) {
                                        cropID = jsonArray.getJSONObject(0).getString("CropID");
                                        cropName = jsonArray.getJSONObject(0).getString("CropName");
                                    }
                                }
                            }
                            Intent in = new Intent(context.getApplicationContext(), PolicyList.class);
                            in.putExtra("farmID", FarmID);
                            in.putExtra("FarmerID", FarmerID);
                            in.putExtra("ProjectID", ProjectID);
                            in.putExtra("cropID", cropID);
                            in.putExtra("CropName", cropName);
                            context.startActivity(in);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            SelectFarmDetails_policy = new JSONObject();
                        }
                    }

                }


            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


}