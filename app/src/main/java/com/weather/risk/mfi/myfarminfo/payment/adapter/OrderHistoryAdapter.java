package com.weather.risk.mfi.myfarminfo.payment.adapter;

import static com.weather.risk.mfi.myfarminfo.utils.Utility.getDynamicLanguageValue;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setDynamicLanguage;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setFontsStyleTxt;

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
import com.weather.risk.mfi.myfarminfo.databinding.OrderRowBinding;
import com.weather.risk.mfi.myfarminfo.payment.ItemClick;
import com.weather.risk.mfi.myfarminfo.payment.model.OrderHistoryBean;
import com.weather.risk.mfi.myfarminfo.utils.AppConstant;

import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.MyViewHolder> {

    private List<OrderHistoryBean> list;

    Context context;
    private LayoutInflater layoutInflater;
    ItemClick onClickListner;
    Double pAmnt = 0.0;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView orderNumber, orderDate, deliveryStatus, orderMode, amount, collectedAmount, pendingAmount;
        LinearLayout row, pendingRow, collectedrow, paymentModeLay;
        TextView payNowBTN, payAndDeliverBTN, deliverBTN, viewDetail;
        TextView txtorderNumber, txtdate, txtdeliveryStatus, txtpaymentMode, txtamount, txtcollectAmount, txtpendingAmount;

        public MyViewHolder(final OrderRowBinding itemBinding) {
            super(itemBinding.getRoot());
            orderDate = itemBinding.date;
            orderNumber = itemBinding.orderNumber;
            deliveryStatus = itemBinding.deliveryStatus;
            orderMode = itemBinding.paymentMode;
            amount = itemBinding.amount;
            collectedAmount = itemBinding.collectAmount;
            pendingAmount = itemBinding.pendingAmount;
            row = itemBinding.row;
            payNowBTN = itemBinding.payNow;
            payAndDeliverBTN = itemBinding.payDeliverBtn;
            deliverBTN = itemBinding.deliveryBtn;
            pendingRow = itemBinding.pendingRow;
            collectedrow = itemBinding.collectedRow;
            paymentModeLay = itemBinding.paymentModeLay;
            viewDetail = itemBinding.viewDetailBtn;
            txtorderNumber = itemBinding.txtorderNumber;
            txtdate = itemBinding.txtdate;
            txtdeliveryStatus = itemBinding.txtdeliveryStatus;
            txtpaymentMode = itemBinding.txtpaymentMode;
            txtamount = itemBinding.txtamount;
            txtcollectAmount = itemBinding.txtcollectAmount;
            txtpendingAmount = itemBinding.txtpendingAmount;

        }
    }


    public OrderHistoryAdapter(Context con, List<OrderHistoryBean> sList, ItemClick onC) {
        this.list = sList;
        context = con;
        this.onClickListner = onC;


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        OrderRowBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.order_row, parent, false);
        return new MyViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {


        setFontsStyleTxt(context, holder.txtorderNumber, 6);
        setFontsStyleTxt(context, holder.orderNumber, 5);
        setFontsStyleTxt(context, holder.txtdate, 6);
        setFontsStyleTxt(context, holder.orderDate, 5);
        setFontsStyleTxt(context, holder.txtdeliveryStatus, 6);
        setFontsStyleTxt(context, holder.deliveryStatus, 5);
        setFontsStyleTxt(context, holder.txtpaymentMode, 6);
        setFontsStyleTxt(context, holder.orderMode, 5);
        setFontsStyleTxt(context, holder.txtamount, 6);
        setFontsStyleTxt(context, holder.amount, 5);
        setFontsStyleTxt(context, holder.txtcollectAmount, 6);
        setFontsStyleTxt(context, holder.collectedAmount, 5);
        setFontsStyleTxt(context, holder.txtpendingAmount, 6);
        setFontsStyleTxt(context, holder.pendingAmount, 5);
        setFontsStyleTxt(context, holder.deliverBTN, 5);
        setFontsStyleTxt(context, holder.payNowBTN, 5);
        setFontsStyleTxt(context, holder.payAndDeliverBTN, 5);

        setDynamicLanguage(context, holder.txtorderNumber, "OrderNumbers", R.string.OrderNumbers);
        setDynamicLanguage(context, holder.txtdate, "Date", R.string.Date);
        setDynamicLanguage(context, holder.txtdeliveryStatus, "delivery_status", R.string.delivery_status);
        setDynamicLanguage(context, holder.txtpaymentMode, "payment_mode", R.string.payment_mode);
        setDynamicLanguage(context, holder.txtamount, "Amount", R.string.Amount);
        setDynamicLanguage(context, holder.txtcollectAmount, "collected_amount", R.string.collected_amount);
        setDynamicLanguage(context, holder.txtpendingAmount, "pending_amount_rs", R.string.pending_amount_rs);

        holder.orderNumber.setText("" + list.get(position).getOrderID());
        holder.orderDate.setText("" + list.get(position).getOrderdate());
        if (list.get(position).getPaymentType() != null && list.get(position).getPaymentType().length() > 0) {
            holder.paymentModeLay.setVisibility(View.VISIBLE);
            holder.orderMode.setText("" + list.get(position).getPaymentType());
        } else {
            holder.paymentModeLay.setVisibility(View.GONE);
        }
        holder.deliveryStatus.setText("" + list.get(position).getDeliveryStatus());
        holder.amount.setText("" + list.get(position).getOrderAmount());
        Double penAmount = list.get(position).getPendingAmount();
        Double colAmount = list.get(position).getCollectedAmount();
        String deliveryStatus = list.get(position).getDeliveryStatus();
        if (penAmount != null && penAmount > 0) {
//            holder.payNowBTN.setText("Only Collection");
            holder.payNowBTN.setText(getDynamicLanguageValue(context, "OnlyCollection", R.string.OnlyCollection));
//            holder.payNowBTN.setText(context.getResources().getString(R.string.OnlyCollection));
            holder.payAndDeliverBTN.setVisibility(View.VISIBLE);
            if (deliveryStatus != null && deliveryStatus.equalsIgnoreCase("Pending")) {
                holder.payAndDeliverBTN.setVisibility(View.VISIBLE);
            } else if (deliveryStatus != null && deliveryStatus.equalsIgnoreCase("partial")) {
                holder.payAndDeliverBTN.setVisibility(View.VISIBLE);
            } else {
                holder.payAndDeliverBTN.setVisibility(View.GONE);

            }


            holder.deliverBTN.setVisibility(View.GONE);
        } else {
//            holder.payNowBTN.setText("View Detail");
            holder.payNowBTN.setText(getDynamicLanguageValue(context, "ViewDetail", R.string.ViewDetail));
//            holder.payNowBTN.setText(context.getResources().getString(R.string.ViewDetail));
            holder.payAndDeliverBTN.setVisibility(View.GONE);
            if (deliveryStatus != null && deliveryStatus.equalsIgnoreCase("Pending")) {
                holder.deliverBTN.setVisibility(View.VISIBLE);
            } else if (deliveryStatus != null && deliveryStatus.equalsIgnoreCase("partial")) {
                holder.deliverBTN.setVisibility(View.VISIBLE);
            } else {
                holder.payAndDeliverBTN.setVisibility(View.GONE);
                holder.deliverBTN.setVisibility(View.GONE);
            }
        }
        if (penAmount != null) {
            holder.pendingRow.setVisibility(View.VISIBLE);
            holder.pendingAmount.setText("" + penAmount);
            pAmnt = penAmount;

        } else {
            holder.pendingRow.setVisibility(View.GONE);
            pAmnt = 0.0;
            holder.pendingAmount.setText("0");
        }
        if (colAmount != null) {
            holder.collectedrow.setVisibility(View.VISIBLE);
            holder.collectedAmount.setText("" + colAmount);

        } else {
            holder.collectedrow.setVisibility(View.GONE);
            holder.collectedAmount.setText("0");
        }
        if (AppConstant.userTypeID != null && (AppConstant.userTypeID.equalsIgnoreCase("1") || AppConstant.userTypeID.equalsIgnoreCase("2") || AppConstant.userTypeID.equalsIgnoreCase("18"))) {
            holder.viewDetail.setVisibility(View.GONE);
        } else {
            holder.payNowBTN.setVisibility(View.GONE);
            holder.payAndDeliverBTN.setVisibility(View.GONE);
            holder.deliverBTN.setVisibility(View.GONE);
            holder.viewDetail.setVisibility(View.VISIBLE);
        }
        holder.viewDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (list.get(position).getOrderID() != null && list.get(position).getOrderID().length() > 0) {
                    onClickListner.onClick(list.get(position).getOrderID() + "=view_detail");
                }
            }
        });
        holder.payNowBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (list.get(position).getOrderID() != null && list.get(position).getOrderID().length() > 0) {
                    onClickListner.onClick(list.get(position).getOrderID() + "=pay");
                }
            }
        });
        holder.payAndDeliverBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (list.get(position).getOrderID() != null && list.get(position).getOrderID().length() > 0) {
                    onClickListner.onClick(list.get(position).getOrderID() + "=pay_delivery");
                }
            }
        });
        holder.deliverBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (list.get(position).getOrderID() != null && list.get(position).getOrderID().length() > 0) {
                    onClickListner.onClick(list.get(position).getOrderID() + "=delivery");
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

