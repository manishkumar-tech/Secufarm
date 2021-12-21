package com.weather.risk.mfi.myfarminfo.groundwater;

import static com.weather.risk.mfi.myfarminfo.utils.Utility.getDecimal2;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.databinding.GroundwaterforecastingadapterBinding;
import com.weather.risk.mfi.myfarminfo.utils.UtilFonts;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class GroundWaterForecastingAdapter extends RecyclerView.Adapter<GroundWaterForecastingAdapter.MyViewHolder> {

    private List<GroundWaterResponse> list;
    Context context;
    LayoutInflater layoutInflater;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        GroundwaterforecastingadapterBinding binding;

        public MyViewHolder(GroundwaterforecastingadapterBinding bindings) {
            super(bindings.getRoot());
            this.binding = bindings;
        }
    }

    public GroundWaterForecastingAdapter(Context con, List<GroundWaterResponse> sList) {
        this.list = sList;
        context = con;

    }

    @Override
    public GroundWaterForecastingAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        GroundwaterforecastingadapterBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.groundwaterforecastingadapter, parent, false);
        return new GroundWaterForecastingAdapter.MyViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final GroundWaterForecastingAdapter.MyViewHolder holder, final int position) {
        try {
            UtilFonts.UtilFontsInitialize(context);
            holder.binding.txtGrid.setTypeface(UtilFonts.KT_Regular);
            holder.binding.txtWaterlevelbase.setTypeface(UtilFonts.KT_Light);
            holder.binding.txtWaterlevelForecasted.setTypeface(UtilFonts.KT_Light);
            holder.binding.txtGroundWaterlevelStatus.setTypeface(UtilFonts.KT_Light);

            GroundWaterResponse data = list.get(position);
            double Actualgroundwater = getDecimal2(data.getActualgroundwater());
            double Estimatedgroundwater = getDecimal2(data.getEstimatedgroundwater());
            holder.binding.txtGrid.setText(data.getGridName());
            holder.binding.txtWaterlevelbase.setText(String.valueOf(Actualgroundwater +" m"));
            holder.binding.txtWaterlevelForecasted.setText(String.valueOf(Estimatedgroundwater +" m"));
            double divergance = data.getDivergance();
            if (divergance > 0.0) {
                holder.binding.txtGroundWaterlevelStatus.setText(context.getResources().getString(R.string.Improved));
            } else {
                holder.binding.txtGroundWaterlevelStatus.setText(context.getResources().getString(R.string.Decline));
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}