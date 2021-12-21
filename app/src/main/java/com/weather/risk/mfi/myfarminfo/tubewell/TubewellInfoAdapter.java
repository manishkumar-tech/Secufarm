package com.weather.risk.mfi.myfarminfo.tubewell;

import static com.weather.risk.mfi.myfarminfo.utils.Utility.setFontsStyleTxt;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.bean.TubewellListBean;

import java.util.ArrayList;

/**
 * Created by Admin on 11-04-2018.
 */
public class TubewellInfoAdapter  extends RecyclerView.Adapter<TubewellInfoAdapter.ViewHolder> {
    private ArrayList<TubewellListBean> mDataset = new ArrayList<TubewellListBean>();

    public Context mContext;

    // Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView name;

        public ViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.text_name);

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
    public TubewellInfoAdapter(Context con, ArrayList<TubewellListBean> myDataset) {
        mDataset = myDataset;
        mContext = con;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.tube_info_row, parent, false);

        ViewHolder vh = new ViewHolder(v);
        Log.v("lgfgdl",mDataset.size()+"");

        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        // holder.setIsRecyclable(false);

        setFontsStyleTxt(mContext, holder.name, 6);

        holder.name.setText(mDataset.get(position).getTubewellname());

    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }



}
