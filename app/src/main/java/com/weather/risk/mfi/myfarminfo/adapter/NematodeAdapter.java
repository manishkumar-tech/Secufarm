package com.weather.risk.mfi.myfarminfo.adapter;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.bean.NematodeBean;

import java.util.ArrayList;

/**
 * Created by Admin on 16-02-2018.
 */
public class NematodeAdapter   extends RecyclerView.Adapter<NematodeAdapter.ViewHolder> {
    private ArrayList<NematodeBean> mDataset = new ArrayList<NematodeBean>();

    public Context mContext;
    String imageString;
    TextToSpeech t1;

    // Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView name;

        public ImageView image;



        public ViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.nematode_name);
            image = (ImageView) v.findViewById(R.id.nematode_image);

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
    public NematodeAdapter(Context con, ArrayList<NematodeBean> myDataset) {
        mDataset = myDataset;
        mContext = con;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.nematode_row, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {


        // holder.setIsRecyclable(false);
        holder.name.setText(mDataset.get(position).getName());
        if (mDataset.get(position).getImage()!=null){
            Log.v("hhhhh","https://myfarminfo.com/Tools/Img/Nematode/"+mDataset.get(position).getImage());
            Picasso.with(mContext).load("https://myfarminfo.com/Tools/Img/Nematode/"+mDataset.get(position).getImage()).into(holder.image);
        }


    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }



}
