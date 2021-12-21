package com.weather.risk.mfi.myfarminfo.adapter;

import static com.weather.risk.mfi.myfarminfo.utils.Utility.getDynamicLanguageToast;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.activities.MainProfileActivity;
import com.weather.risk.mfi.myfarminfo.bean.NotificationBean;
import com.weather.risk.mfi.myfarminfo.firebasenotification.NotificationCountSMS;
import com.weather.risk.mfi.myfarminfo.firebasenotification.NotificationData;
import com.weather.risk.mfi.myfarminfo.firebasenotification.NotificationPOPDetailsDialog;
import com.weather.risk.mfi.myfarminfo.utils.AppManager;
import com.weather.risk.mfi.myfarminfo.utils.TransparentProgressDialog;

import org.json.JSONObject;

import java.util.ArrayList;

public class NotificationCustomAdapter extends ArrayAdapter<NotificationData> {

    Context mContext;
    private ArrayList<NotificationBean> mDataset = new ArrayList<NotificationBean>();
    ArrayList<NotificationData> dataSet;
    PopupWindow mPopupWindow;
    MainProfileActivity mainProfileActivity;

    // View lookup cache
    private static class ViewHolder {
        TextView txtSMS, txtDateTime;
        View viewline;
        LinearLayout ll_row;
        TableRow tb_Yesno;
        Button btn_details_YesNo;
        TextView btn_No, btn_Yes;

    }

    public NotificationCustomAdapter(ArrayList<NotificationData> data, Context context, ArrayList<NotificationBean> mDataset,
                                     PopupWindow PopupWindow, MainProfileActivity activity ) {
        super(context, R.layout.notificationsms_adpater, data);
        this.dataSet = data;
        this.mContext = context;
        this.mDataset = mDataset;
        this.mPopupWindow = PopupWindow;
        this.mainProfileActivity = activity;

    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        NotificationData dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        try {
            if (convertView == null) {

                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.notificationsms_adpater, parent, false);
                viewHolder.ll_row = (LinearLayout) convertView.findViewById(R.id.ll_row);
                viewHolder.txtSMS = (TextView) convertView.findViewById(R.id.txtSMS);
                viewHolder.txtDateTime = (TextView) convertView.findViewById(R.id.txtDateTime);
                viewHolder.viewline = (View) convertView.findViewById(R.id.viewline);
                //btn_thumbdown,btn_thumbup,btn_details
                viewHolder.tb_Yesno = (TableRow) convertView.findViewById(R.id.tb_Yesno);
                viewHolder.btn_No = (TextView) convertView.findViewById(R.id.btn_No);
                viewHolder.btn_Yes = (TextView) convertView.findViewById(R.id.btn_Yes);
                viewHolder.btn_details_YesNo = (Button) convertView.findViewById(R.id.btn_details_YesNo);

                result = convertView;

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
                result = convertView;
            }
            viewHolder.txtSMS.setText(String.valueOf(dataModel.getSMS()));
            viewHolder.txtDateTime.setText(String.valueOf(dataModel.getDatetime()));
            if (mDataset.size() - 1 == position) {
                viewHolder.viewline.setVisibility(View.GONE);
            } else {
                viewHolder.viewline.setVisibility(View.VISIBLE);
            }

            viewHolder.tb_Yesno.setVisibility(View.GONE);

            String Notftytype = mDataset.get(position).getNotftytype();
            if (Notftytype != null && Notftytype.equalsIgnoreCase("Intractive")) {
//                viewHolder.ll_row.setBackgroundColor(mContext.getResources().getColor(R.color.gray));
                String FeedbackStatus = mDataset.get(position).getFeedbackStatus();
                if (FeedbackStatus != null && FeedbackStatus.equalsIgnoreCase("IntractiveCompleted")) {
                } else {
                    viewHolder.tb_Yesno.setVisibility(View.VISIBLE);
                }
            }

            viewHolder.ll_row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setIntent(position);

                }
            });
            viewHolder.btn_No.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mPopupWindow.dismiss();
                    new setFarmRegistrationSave("No", position, "Intractive").execute();

                }
            });
            viewHolder.btn_Yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mPopupWindow.dismiss();
                    new setFarmRegistrationSave("Yes", position, "Intractive").execute();

                }
            });
            viewHolder.btn_details_YesNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setIntent(position);

                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        // Return the completed view to render on screen
        return convertView;
    }

    public void setIntent(final int position) {
        if (mDataset != null && mDataset.size() > 0) {
            String FarmID = mDataset.get(position).getFarmID();
            if (FarmID != null || FarmID == null) {
                Intent in = new Intent(mContext.getApplicationContext(), NotificationPOPDetailsDialog.class);
                in.putExtra("mDataset", (ArrayList<NotificationBean>) mDataset);
                in.putExtra("position", position);
                mContext.startActivity(in);
            } else {
                getDynamicLanguageToast(mContext, "Pleaseselectyour", R.string.Pleaseselectyour);
            }
        }
    }

    private class setFarmRegistrationSave extends AsyncTask<Void, Void, String> {

        TransparentProgressDialog progressDialog;
        String Status = "";
        String Flag = "";
        int position = 0;

        public setFarmRegistrationSave(String status, int pos, String flag) {
            Status = status;
            position = pos;
            Flag = flag;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new TransparentProgressDialog(
                    mContext, mContext.getResources().getString(R.string.Dataisloading));
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @Override
        protected String doInBackground(Void... params) {
            String response = null;
            String URL = AppManager.getInstance().UploadNotificationDataURL;//Herojit Comment
            JSONObject object = getjsonvalueUpload(position, Status);
            String jsonvalue = object.toString();

            response = AppManager.getInstance().httpRequestPutMethod(URL, jsonvalue);
            return "{" + response + "}";
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPreExecute();
            progressDialog.dismiss();
            try {
                String Messgae = mDataset.get(position).getMessgae();
                String Notftytype = mDataset.get(position).getNotftytype();
                String StepID = mDataset.get(position).getStepID();
                String FarmID = mDataset.get(position).getFarmID();
                String Title = mDataset.get(position).getTitle();
                String DateTime = mDataset.get(position).getDateTime();
                String NotifImageURL = mDataset.get(position).getNotifImageURL();
                String DateTimeHHMMSS = mDataset.get(position).getDateTimeHHMMSS();
                if (response.contains("SaveNotifyResponseResult")) {
                    JSONObject obj = new JSONObject(response);
                    String res = obj.getString("SaveNotifyResponseResult");
                    if (res != null && res.equalsIgnoreCase("Response Save Sucessfully")) {
                        getDynamicLanguageToast(mContext, "SubmittedSuccessfully", R.string.SubmittedSuccessfully);
                        if (Flag.equalsIgnoreCase("Feedback")) {
                            NotificationCountSMS.setNotificationValueData_AddFeedback(mContext, Messgae,
                                    Notftytype, StepID, FarmID, Title, "", NotifImageURL, "FeedbackCompleted");
                        NotificationCountSMS.setRemoveNoficationKeyValue(mContext, StepID, "removesinglevalues");
                        } else if (Flag.equalsIgnoreCase("Intractive")) {
                            NotificationCountSMS.setNotificationValueData_AddFeedback(mContext, Messgae,
                                    Notftytype, StepID, FarmID, Title, "", NotifImageURL, "IntractiveCompleted");
                            NotificationCountSMS.setRemoveNoficationKeyValue(mContext, StepID, "removesinglevalues");
                        }
                        mainProfileActivity.setNotificationCount();
                    } else {
                        getDynamicLanguageToast(mContext, "FormattingError", R.string.FormattingError);
                    }
                } else {
                    getDynamicLanguageToast(mContext, "ServerError", R.string.ServerError);
                }
            } catch (Exception ex) {
                getDynamicLanguageToast(mContext, "FormattingError", R.string.FormattingError);
                ex.printStackTrace();
            }
        }
    }

    public JSONObject getjsonvalueUpload(int pos, String Status) {

        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject1 = new JSONObject();
        try {
//            jsonObject.put("OTP", "");
            //Messgae = null, Notftytype = null, StepID = null, FarmID = null, Title = null, DateTime = null
            jsonObject.put("NotifyResponseMessage", mDataset.get(pos).getMessgae());
            jsonObject.put("FarmID", mDataset.get(pos).getFarmID());
            jsonObject.put("stepID", mDataset.get(pos).getStepID());
            jsonObject.put("ImagePath", mDataset.get(pos).getNotifImageURL());
            //Add New Field
            jsonObject.put("Status", Status);
//            jsonObject1.put("notifyresponse", jsonObject.toString());

//            String parameterString = jsonObject.toString();
//            parameterString = parameterString.replace("\\\\\\\\\\\\\\", "\\\\\\");
            jsonObject1 = new JSONObject();
            jsonObject1.put("notifyresponse", jsonObject.toString());
//            jsonObject1 = new JSONObject(parameterString);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return jsonObject1;
    }

}