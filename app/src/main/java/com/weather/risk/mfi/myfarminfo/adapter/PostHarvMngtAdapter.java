package com.weather.risk.mfi.myfarminfo.adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.squareup.picasso.Picasso;
import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.bean.PostHarvMngt;

import org.json.JSONArray;

import java.util.ArrayList;

import static com.weather.risk.mfi.myfarminfo.utils.AppManager.WeedMngtImageURL;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setDynamicLanguage;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setFontsStyleTxt;

public class PostHarvMngtAdapter extends RecyclerView.Adapter<PostHarvMngtAdapter.ViewHolder> {
    public Context mContext;
    String imageString;
    int totalAmount = 0;
    private ArrayList<PostHarvMngt> mDataset = new ArrayList<PostHarvMngt>();

    // Provide a suitable constructor (depends on the kind of dataset)
    public PostHarvMngtAdapter(Context con, ArrayList<PostHarvMngt> myDataset) {
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.postharvmngtadapter, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.txt_Title.setText(mDataset.get(position).getTitle());
        holder.txt_descriptionVal.setText(mDataset.get(position).getDescription());
        String ImageURL = mDataset.get(position).getImagefile();
        setDynamicLanguage(mContext, holder.txt_description, "Description", R.string.Description);
        setFontsStyleTxt(mContext, holder.txt_Title, 2);
        setFontsStyleTxt(mContext, holder.txt_description, 4);
        setFontsStyleTxt(mContext, holder.txt_descriptionVal, 7);
        try {
            ArrayList<String> data = new ArrayList<String>();
            JSONArray array = new JSONArray(ImageURL);
            if (array.length() > 0) {
                for (int i = 0; i < array.length(); i++) {
                    String Name = array.get(i).toString();
                    String imagePath = WeedMngtImageURL + Name;
                    data.add(imagePath);
                }
            }
            holder.recyclerview_Imagelist.setHasFixedSize(true);
//            LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(
                    mContext, LinearLayoutManager.HORIZONTAL, false);
            holder.recyclerview_Imagelist.setLayoutManager(mLayoutManager);
            if (data.size() > 0) {
                holder.recyclerview_Imagelist.setVisibility(View.VISIBLE);
                PostHarvMngtImagelistAdapter adapter = new PostHarvMngtImagelistAdapter(mContext, data);
                holder.recyclerview_Imagelist.setAdapter(adapter);
            } else {
                holder.recyclerview_Imagelist.setVisibility(View.GONE);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
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
        public TextView txt_Title, txt_description,txt_descriptionVal;
        public RecyclerView recyclerview_Imagelist;

        public ViewHolder(View v) {
            super(v);
            txt_Title = (TextView) v.findViewById(R.id.txt_Title);
            txt_descriptionVal = (TextView) v.findViewById(R.id.txt_descriptionVal);
            txt_description = (TextView) v.findViewById(R.id.txt_description);
            recyclerview_Imagelist = (RecyclerView) v.findViewById(R.id.recyclerview_Imagelist);

        }
    }


}

