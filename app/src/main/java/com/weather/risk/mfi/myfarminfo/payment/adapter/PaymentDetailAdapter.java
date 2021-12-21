package com.weather.risk.mfi.myfarminfo.payment.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.databinding.PaymentDetailRowBinding;
import com.weather.risk.mfi.myfarminfo.payment.ItemClick;
import com.weather.risk.mfi.myfarminfo.payment.model.ModelList;

import java.util.List;

public class PaymentDetailAdapter  extends RecyclerView.Adapter<PaymentDetailAdapter.MyViewHolder> {

    private List<ModelList> list;

    Context context;
    private LayoutInflater layoutInflater;
    ItemClick onClickListner;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name,villageName,  pendingAmount, totalAmount,colledtedAmount, msgInfo;
        LinearLayout row;
        TextView payNowBTN;

        public MyViewHolder(final PaymentDetailRowBinding itemBinding) {
            super(itemBinding.getRoot());
            name = itemBinding.name;
            villageName = itemBinding.villageName;
            colledtedAmount = itemBinding.collectedAmount;
            pendingAmount = itemBinding.pendingAmount;
            totalAmount = itemBinding.totalAmount;
            msgInfo = itemBinding.pendigMsg;
            row = itemBinding.row;
            payNowBTN = itemBinding.payNow;



        }
    }


    public PaymentDetailAdapter(Context con, List<ModelList> sList, ItemClick onC) {
        this.list = sList;
        context = con;
        this.onClickListner = onC;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        PaymentDetailRowBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.payment_detail_row, parent, false);


        return new MyViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.name.setText(""+list.get(position).getFarmerName()+" s/o "+list.get(position).getFatherName() );
        holder.villageName.setText(""+list.get(position).getVillageName() );
        holder.totalAmount.setText(""+list.get(position).getTotalAmount() );

        holder.colledtedAmount.setText(""+list.get(position).getCollectedAmount());
        holder.pendingAmount.setText(""+list.get(position).getPendingAmount());

        holder.msgInfo.setText("Farm Tagged: " + list.size()+" | Area Tagged (acre): " + list.get(position).getTotalAreaGeoTag()+"\nArea Confirmed (acre): "+list.get(position).getTotalAreaServiceTaken()+" | Service Amount (Rs): "+list.get(position).getTotalAmount()+"\nPending Area (acre): "+list.get(position).getPendingArea());

        holder.row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (list.get(position).getProjectID()!=null && list.get(position).getProjectID()>0) {
                    onClickListner.onClick(list.get(position).getProjectID()+"="+list.get(position).getFarmerID()+"="+list.get(position).getPendingAmount()+"="+list.get(position).getCollectedAmount()+"="+list.get(position).getTotalAmount());
                }
            }
        });

        holder.payNowBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (list.get(position).getProjectID()!=null && list.get(position).getProjectID()>0) {
                    onClickListner.onClick(list.get(position).getProjectID()+"="+list.get(position).getFarmerID()+"="+list.get(position).getPendingAmount()+"="+list.get(position).getCollectedAmount()+"="+list.get(position).getTotalAmount());
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
