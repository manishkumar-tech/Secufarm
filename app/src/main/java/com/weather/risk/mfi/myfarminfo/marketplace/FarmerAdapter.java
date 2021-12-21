package com.weather.risk.mfi.myfarminfo.marketplace;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.databinding.FarmerListRowBinding;
import com.weather.risk.mfi.myfarminfo.retrofitmodelnew.FarmerResponse;

import java.util.List;

public class FarmerAdapter extends RecyclerView.Adapter<FarmerAdapter.MyViewHolder> {

    private List<FarmerResponse> list;

    Context context;
    private LayoutInflater layoutInflater;
    ItemClickInterface onClickListner;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name,area;
        LinearLayout row;
        public MyViewHolder(final FarmerListRowBinding itemBinding) {
            super(itemBinding.getRoot());
            name = itemBinding.name;
            area = itemBinding.area;
            row   = itemBinding.row;

        }
    }


    public FarmerAdapter(Context con,List< FarmerResponse>  sList,ItemClickInterface onC) {
        this.list = sList;
        context = con;
        this.onClickListner = onC;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        FarmerListRowBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.farmer_list_row, parent, false);




        return new MyViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.name.setText(list.get(position).getFarmerName()+" s/o "+list.get(position).getFatherName());
        holder.area.setText("Village Area Geo tagged : "+list.get(position).getVillageAreaGeotagged());

        holder.row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                onClickListner.onClick(list.get(position).getFarmerPersonelID());
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
