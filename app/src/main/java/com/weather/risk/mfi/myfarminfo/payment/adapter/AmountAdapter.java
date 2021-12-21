package com.weather.risk.mfi.myfarminfo.payment.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.databinding.AmountPendingRowBinding;
import com.weather.risk.mfi.myfarminfo.marketplace.ItemClickInterface;
import com.weather.risk.mfi.myfarminfo.payment.model.AmountListResponse;

import java.util.List;

public class AmountAdapter extends RecyclerView.Adapter<AmountAdapter.MyViewHolder> {

    private List<AmountListResponse> list;

    Context context;
    private LayoutInflater layoutInflater;
    ItemClickInterface onClickListner;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView projectName,totalFarmer, area, pendingAmount, totalAmount, msgInfo;
        LinearLayout row;
        ImageView nextBTN;

        public MyViewHolder(final AmountPendingRowBinding itemBinding) {
            super(itemBinding.getRoot());
            projectName = itemBinding.projectName;
            totalFarmer = itemBinding.totalFarmer;
            area = itemBinding.area;
            pendingAmount = itemBinding.pendingAmount;
            totalAmount = itemBinding.totalAmount;
            msgInfo = itemBinding.pendigMsg;
            row = itemBinding.row;
            nextBTN = itemBinding.next;


        }
    }


    public AmountAdapter(Context con, List<AmountListResponse> sList, ItemClickInterface onC) {
        this.list = sList;
        context = con;
        this.onClickListner = onC;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        AmountPendingRowBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.amount_pending_row, parent, false);


        return new MyViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.projectName.setText(""+list.get(position).getProjectName() );
        holder.totalFarmer.setText(""+list.get(position).getTotalFarmer() );
        holder.totalAmount.setText(""+list.get(position).getTotalProjectWiseTotalAmount() );

        holder.area.setText(""+list.get(position).getTotalAreaGeoTag());
        holder.pendingAmount.setText(""+list.get(position).getTotalProjectWisePendingAmount());

        holder.msgInfo.setText("Pending farmers: " + list.get(position).getNoofFarmerPaymentPending()+" | Pending Area: "+list.get(position).getPendingArea());

        holder.row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (list.get(position).getProjectID()!=null && list.get(position).getProjectID()>0) {
                    onClickListner.onClick(list.get(position).getProjectID());
                }
            }
        });

        holder.nextBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (list.get(position).getProjectID()!=null && list.get(position).getProjectID()>0) {
                    onClickListner.onClick(list.get(position).getProjectID());
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
