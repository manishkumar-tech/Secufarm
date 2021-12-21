package com.weather.risk.mfi.myfarminfo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.utils.UtilFonts;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

public class SoilInfoAdapterAdapter extends RecyclerView.Adapter<SoilInfoAdapterAdapter.ViewHolder> {
    //    private ArrayList<SoilInfoBean> mDataset = new ArrayList<SoilInfoBean>();
    private ArrayList<HashMap<String, String>> mDataset = new ArrayList();

    public Context mContext;
    String imageString;

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_title, txt_value;

        public ViewHolder(View v) {
            super(v);
            txt_title = (TextView) v.findViewById(R.id.txt_title);
            txt_value = (TextView) v.findViewById(R.id.txt_value);
        }
    }

    public void remove(int pos) {
        mDataset.remove(pos);
        notifyItemRemoved(pos);
    }

    public SoilInfoAdapterAdapter(Context con, ArrayList<HashMap<String, String>> myDataset) {
        mDataset = myDataset;
        mContext = con;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.soisoilinfoadapteradapter, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        UtilFonts.UtilFontsInitialize(mContext);
        holder.txt_title.setTypeface(UtilFonts.KT_Medium);
        holder.txt_value.setTypeface(UtilFonts.KT_Medium);

        //Get Dynamic Key from Hashmap
        try {
            HashMap<String, String> map = mDataset.get(position);
            Set<String> keys = map.keySet();
            String Key = String.valueOf(keys);
            Key = Key.replace("[", "");
            Key = Key.replace("]", "");
            String value = map.get(Key);
//            Collection<String> value = map.values();
            holder.txt_title.setText(Key);
            if (value != null)
                holder.txt_value.setText(value + " %");
        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        int count = mDataset.size();
        return count;
    }

}
