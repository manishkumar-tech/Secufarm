package com.weather.risk.mfi.myfarminfo.cattledashboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.databinding.CattleadvisoryadapterBinding;
import com.weather.risk.mfi.myfarminfo.utils.UtilFonts;

import java.util.ArrayList;

public class CattleAdvisoryAdapter extends RecyclerView.Adapter<CattleAdvisoryAdapter.ViewHolder> {

    Context context;
    ArrayList<CattlePOP> data;
    LayoutInflater layoutInflater;
    String flags;

    // Provide a suitable constructor (depends on the kind of dataset)
    public CattleAdvisoryAdapter(Context con, ArrayList<CattlePOP> cattlepop, String Flags) {
        context = con;
        this.data = cattlepop;
        this.flags = Flags;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        CattleadvisoryadapterBinding binding =
                DataBindingUtil.inflate(layoutInflater, R.layout.cattleadvisoryadapter, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {

            UtilFonts.UtilFontsInitialize(context);
            holder.binding.txtStage.setTypeface(UtilFonts.KT_Regular);
            holder.binding.txtDescrip.setTypeface(UtilFonts.KT_Light);


            CattlePOP pop = new CattlePOP();
            pop = data.get(position);
            String stage = pop.getStage();
            String descrip = pop.getDescription();
            holder.binding.txtStage.setText(stage);
            holder.binding.txtDescrip.setText(descrip);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
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
        CattleadvisoryadapterBinding binding;

        public ViewHolder(CattleadvisoryadapterBinding bindings) {
            super(bindings.getRoot());
            this.binding = bindings;
        }
    }
}
