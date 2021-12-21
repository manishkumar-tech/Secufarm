package com.weather.risk.mfi.myfarminfo.marketplace;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.databinding.TaggedRowBinding;
import com.weather.risk.mfi.myfarminfo.retrofitmodelnew.InfoDataDT;

import java.util.List;

public class TaggedAdapter extends RecyclerView.Adapter<TaggedAdapter.MyViewHolder> {

    Context context;
    private List<InfoDataDT> list;
    private LayoutInflater layoutInflater;


    public TaggedAdapter(Context con, List<InfoDataDT> mList) {
        this.list = mList;

        this.context = con;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        TaggedRowBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.tagged_row, parent, false);


        return new MyViewHolder(binding);
    }

    @SuppressLint({"SetTextI18n", "WrongConstant"})
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.area.setText("Area : "+list.get(position).getArea());
        holder.crop.setText("Crop Name : "+list.get(position).getCropName());

    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView area,crop;

        public MyViewHolder(final TaggedRowBinding binding) {
            super(binding.getRoot());


            area = binding.area;
            crop = binding.crop;

        }
    }


}
