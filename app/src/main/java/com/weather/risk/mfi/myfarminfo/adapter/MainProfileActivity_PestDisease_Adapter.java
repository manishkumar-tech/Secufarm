package com.weather.risk.mfi.myfarminfo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.utils.UtilFonts;

import org.json.JSONArray;

public class MainProfileActivity_PestDisease_Adapter extends RecyclerView.Adapter<MainProfileActivity_PestDisease_Adapter.ViewHolder> {
    public Context mContext;
    public JSONArray jsonArray;

    // Provide a suitable constructor (depends on the kind of dataset)
    public MainProfileActivity_PestDisease_Adapter(Context con, JSONArray jsonArray) {

        mContext = con;
        this.jsonArray = jsonArray;


    }

      /*public void add(int position, String item) {
          mDataset.add(position, item);
          notifyItemInserted(position);
      }*/

    public void remove(int pos) {
        //   int position = mDataset.indexOf(item);
//        mDataset.remove(pos);
        notifyItemRemoved(pos);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.mainprofileactivity_pestdisease_adapter, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        try {

            UtilFonts.UtilFontsInitialize(mContext);
            holder.value.setTypeface(UtilFonts.KT_Regular);
//            JSONObject object = new JSONObject(jsonArray.get(position).toString());
//            JSONObject obj = jsonArray.getJSONObject(position);
            String pestname = "";
//            pestname = obj.getString("pestname");
            pestname = jsonArray.get(position).toString();
            holder.value.setText(pestname);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return jsonArray.length();
    }

    // Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView value;


        public ViewHolder(View v) {
            super(v);
            value = (TextView) v.findViewById(R.id.value);

        }
    }

}
