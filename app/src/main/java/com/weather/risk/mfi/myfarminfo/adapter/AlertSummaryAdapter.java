package com.weather.risk.mfi.myfarminfo.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.bean.LogBean;
import com.weather.risk.mfi.myfarminfo.bean.PanicBean;
import com.weather.risk.mfi.myfarminfo.utils.AppConstant;

import java.util.ArrayList;

public class AlertSummaryAdapter  extends RecyclerView.Adapter<AlertSummaryAdapter.ViewHolder> {
    private ArrayList<LogBean> mDataset = new ArrayList<LogBean>();

    public Context mContext;



    // Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView farmer_name,logTV;


        public ViewHolder(View v) {
            super(v);
            farmer_name = (TextView) v.findViewById(R.id.farmer_tv);
            logTV = (TextView) v.findViewById(R.id.log_tv);
        }
    }


    public void remove(int pos) {
        //   int position = mDataset.indexOf(item);
        mDataset.remove(pos);
        notifyItemRemoved(pos);
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public AlertSummaryAdapter(Context con, ArrayList<LogBean> myDataset) {
        mDataset = myDataset;
        mContext = con;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.alert_summary_row, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {


        // holder.setIsRecyclable(false);
        String strr1 = mDataset.get(position).getName();
        holder.farmer_name.setText(Html.fromHtml(strr1));
        String strr = mDataset.get(position).getLogString();
        holder.logTV.setText(Html.fromHtml(strr));

    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

  /*  private void setSubtitleLanguage() {
        SharedPreferences myPreference = mContext.getSharedPreferences(AppConstant.SHARED_PREFRENCE_NAME, mContext.MODE_PRIVATE);
        String languagePreference = myPreference.getString(mContext.getResources().getString(R.string.language_pref_key), "1");
        int languageConstant = Integer.parseInt(languagePreference);

        System.out.println("language Constant : " + languageConstant);
        switch (languageConstant) {
            case 1:
                setEnglishText();
                break;
            case 2:
                setGujratiText();
                break;
            case 3:
                setHindiText();
                break;
            default:
                setEnglishText();
        }


    }

    private void setGujratiText() {

        farmInfo.setText("ચેતવણી સારાંશ");
        distTV.setText("જિલ્લા *");
        blockTV.setText("બ્લોક *");
        villTV.setText("ગામ *");
        msgTV.setText("સંદેશ *");
        submitBTN.setText("સબમિટ કરો");
    }

    private void setEnglishText() {

        farmInfo.setText("Alert Summary");
        distTV.setText("District *");
        blockTV.setText("Block *");
        villTV.setText("Village *");
        msgTV.setText("Message *");
        submitBTN.setText("Submit");
    }

    private void setHindiText() {

        farmInfo.setText("अलर्ट सारांश");
        distTV.setText("जिला *");
        blockTV.setText("ब्लॉक *");
        villTV.setText("गाँव *");
        msgTV.setText("संदेश *");
        submitBTN.setText("जमा करें");

    }

*/

}