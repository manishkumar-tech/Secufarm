package com.weather.risk.mfi.myfarminfo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.bean.IrrrigationMngt;

import org.json.JSONArray;

import java.util.ArrayList;

import static com.weather.risk.mfi.myfarminfo.utils.AppManager.WeedMngtImageURL;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setDynamicLanguage;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setFontsStyleTxt;

public class IrrigationMngtAdapter extends RecyclerView.Adapter<IrrigationMngtAdapter.ViewHolder> {
    public Context mContext;
    private ArrayList<IrrrigationMngt> mDataset = new ArrayList<IrrrigationMngt>();

    // Provide a suitable constructor (depends on the kind of dataset)
    public IrrigationMngtAdapter(Context con, ArrayList<IrrrigationMngt> myDataset) {
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.irrigationmngtadapter, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        //txt_DAS,txt_DASVal, txt_Notification,txt_NotificationVal;
        if (mDataset.get(position).getDAS() != null) {
            holder.txt_DAS.setVisibility(View.VISIBLE);
            holder.txt_DASVal.setVisibility(View.VISIBLE);
            holder.txt_DASVal.setText(mDataset.get(position).getDAS());
        } else {
            holder.txt_DAS.setVisibility(View.GONE);
            holder.txt_DASVal.setVisibility(View.GONE);
        }

        setDynamicLanguage(mContext, holder.txt_DAS, "DAS", R.string.DAS);
        setDynamicLanguage(mContext, holder.txt_Notification, "Notification", R.string.Notification);
        setFontsStyleTxt(mContext, holder.txt_DAS, 4);
        setFontsStyleTxt(mContext, holder.txt_DASVal, 7);
        setFontsStyleTxt(mContext, holder.txt_Notification, 4);
        setFontsStyleTxt(mContext, holder.txt_NotificationVal, 7);

        if (mDataset.get(position).getNotification() != null) {
            holder.txt_Notification.setVisibility(View.VISIBLE);
            holder.txt_NotificationVal.setVisibility(View.VISIBLE);
            holder.txt_NotificationVal.setText(mDataset.get(position).getNotification());
        } else {
            holder.txt_Notification.setVisibility(View.GONE);
            holder.txt_NotificationVal.setVisibility(View.GONE);
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
        public TextView txt_DAS, txt_DASVal, txt_Notification, txt_NotificationVal;

        public ViewHolder(View v) {
            super(v);
            txt_DAS = (TextView) v.findViewById(R.id.txt_DAS);
            txt_DASVal = (TextView) v.findViewById(R.id.txt_DASVal);
            txt_Notification = (TextView) v.findViewById(R.id.txt_Notification);
            txt_NotificationVal = (TextView) v.findViewById(R.id.txt_NotificationVal);

        }
    }


}
