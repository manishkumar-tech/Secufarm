package com.weather.risk.mfi.myfarminfo.adapter;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.bean.KeyValueBean;
import com.weather.risk.mfi.myfarminfo.utils.UtilFonts;

import java.util.ArrayList;

public class NewPopupAdapter extends RecyclerView.Adapter<NewPopupAdapter.ViewHolder> {
    private ArrayList<KeyValueBean> mDataset = new ArrayList<KeyValueBean>();

    public Context mContext;
    String imageString;
    TextToSpeech t1;

    // Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView name, value;

        public ViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.name);
            value = (TextView) v.findViewById(R.id.value);

        }
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

    // Provide a suitable constructor (depends on the kind of dataset)
    public NewPopupAdapter(Context con, ArrayList<KeyValueBean> myDataset) {
        mDataset = myDataset;
        mContext = con;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_adapter_row, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        UtilFonts.UtilFontsInitialize(mContext);
        holder.name.setTypeface(UtilFonts.KT_Bold);
        holder.value.setTypeface(UtilFonts.KT_Regular);

        holder.name.setText(mDataset.get(position).getName());
        holder.name.setVisibility(View.GONE);
        holder.value.setText(String.valueOf(position + 1) + ". " + mDataset.get(position).getValue());

    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }


}
