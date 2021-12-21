package com.weather.risk.mfi.myfarminfo.cattledashboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.databinding.VacdisdetailsadapterBinding;
import com.weather.risk.mfi.myfarminfo.utils.UtilFonts;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;

public class VacDisDetailsAdapter extends RecyclerView.Adapter<VacDisDetailsAdapter.ViewHolder> {

    Context context;
    JSONArray jsonvalue;
    LayoutInflater layoutInflater;
    String flags;

    // Provide a suitable constructor (depends on the kind of dataset)
    public VacDisDetailsAdapter(Context con, JSONArray value, String Flags) {
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
        VacdisdetailsadapterBinding binding =
                DataBindingUtil.inflate(layoutInflater, R.layout.vacdisdetailsadapter, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            UtilFonts.UtilFontsInitialize(context);
            holder.binding.txtNameoftheVaccination.setTypeface(UtilFonts.KT_Regular);
            holder.binding.txtVaccinationDate.setTypeface(UtilFonts.KT_Regular);

            JSONObject obj = jsonvalue.getJSONObject(position);
            Iterator<String> listKEY = obj.keys();
            String IDName = listKEY.next();
            String ValueName = obj.getString(IDName);
            holder.binding.txtNameoftheVaccination.setText(IDName);
            holder.binding.txtVaccinationDate.setText(ValueName);
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
        VacdisdetailsadapterBinding binding;

        public ViewHolder(VacdisdetailsadapterBinding bindings) {
            super(bindings.getRoot());
            this.binding = bindings;
        }
    }
}
