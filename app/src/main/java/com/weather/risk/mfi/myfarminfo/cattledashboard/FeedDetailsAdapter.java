package com.weather.risk.mfi.myfarminfo.cattledashboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.databinding.FeeddetailsadapterBinding;
import com.weather.risk.mfi.myfarminfo.utils.UtilFonts;

import org.json.JSONArray;
import org.json.JSONObject;

public class FeedDetailsAdapter extends RecyclerView.Adapter<FeedDetailsAdapter.ViewHolder> {

    Context context;
    JSONArray jsonvalue;
    LayoutInflater layoutInflater;
    String flags;

    // Provide a suitable constructor (depends on the kind of dataset)
    public FeedDetailsAdapter(Context con, JSONArray value, String Flags) {
        context = con;
        this.jsonvalue = value;
        this.flags = Flags;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        FeeddetailsadapterBinding binding =
                DataBindingUtil.inflate(layoutInflater, R.layout.feeddetailsadapter, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {

            UtilFonts.UtilFontsInitialize(context);
            holder.binding.title.setTypeface(UtilFonts.KT_Regular);
            holder.binding.txtName.setTypeface(UtilFonts.KT_Regular);

            JSONObject obj = jsonvalue.getJSONObject(position);
            String ValueName = obj.getString("ID");
            holder.binding.title.setText(String.valueOf(position + 1));
            holder.binding.txtName.setText(ValueName);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return jsonvalue.length();
    }

    public void remove(int pos) {
//        jsonvalue.remove(pos);
        notifyItemRemoved(pos);
    }

    // Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        FeeddetailsadapterBinding binding;

        public ViewHolder(FeeddetailsadapterBinding bindings) {
            super(bindings.getRoot());
            this.binding = bindings;
        }
    }
}
