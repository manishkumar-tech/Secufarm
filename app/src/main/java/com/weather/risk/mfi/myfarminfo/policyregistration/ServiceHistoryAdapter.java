package com.weather.risk.mfi.myfarminfo.policyregistration;

import static com.weather.risk.mfi.myfarminfo.utils.Utility.checkgetLagLongnull;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setDynamicLanguage;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setFontsStyleTxt;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.databinding.OrderRowBinding;
import com.weather.risk.mfi.myfarminfo.databinding.ServicehistoryadapterBinding;
import com.weather.risk.mfi.myfarminfo.payment.ItemClick;
import com.weather.risk.mfi.myfarminfo.payment.adapter.OrderHistoryAdapter;
import com.weather.risk.mfi.myfarminfo.payment.model.OrderHistoryBean;
import com.weather.risk.mfi.myfarminfo.utils.AppConstant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ServiceHistoryAdapter extends RecyclerView.Adapter<ServiceHistoryAdapter.MyViewHolder> {


    private List<ServiceHistoryResponse> list;

    Context context;
    private LayoutInflater layoutInflater;
    ItemClick onClickListner;
    Double pAmnt = 0.0;
    int previousposition = -1;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView orderNumber, orderDate, deliveryStatus, VerificationStatus, orderMode, amount, collectedAmount, pendingAmount;
        LinearLayout row, pendingRow, collectedrow, paymentModeLay, lldetails, llServiceStatus, llVerificationStatus;
        TextView payNowBTN, txt_FarmID/*, payAndDeliverBTN, deliverBTN, viewDetail*/;
        ImageView imvw_Dropdown, imvw_DropUp;

        TableRow tblrw_FarmID, tblrw_PolicyName, tblrw_PolicyServiceDate, tblrw_BookingStatus, tblrw_FarmArea, tblrw_Crops, tblrw_ValueAssured;
        TextView txtPolicyName, txtPolicyServiceDate, txtBookingStatus, txtFarmArea, txtCrops, txtValueAssured;

        TextView txt_FarmIDs, txtorder_number, txtBookingDate, txtServiceStatus, txtVerificationStatus, txtpayment_modes, txtFees,
                txtcollected_amount, txtPendingFees, txtPolicyNames, txtPolicyServiceDates, txtBookingStatuss, txtFarmAreas, txtCrop,
                txtValueAssureds;

        public MyViewHolder(final ServicehistoryadapterBinding itemBinding) {
            super(itemBinding.getRoot());
            orderDate = itemBinding.date;
            orderNumber = itemBinding.orderNumber;
            deliveryStatus = itemBinding.deliveryStatus;
            llServiceStatus = itemBinding.llServiceStatus;
            llVerificationStatus = itemBinding.llVerificationStatus;
            VerificationStatus = itemBinding.VerificationStatus;
            orderMode = itemBinding.paymentMode;
            amount = itemBinding.amount;
            collectedAmount = itemBinding.collectAmount;
            pendingAmount = itemBinding.pendingAmount;
            row = itemBinding.row;
            payNowBTN = itemBinding.payNow;
//            payAndDeliverBTN = itemBinding.payDeliverBtn;
//            deliverBTN = itemBinding.deliveryBtn;
            pendingRow = itemBinding.pendingRow;
            collectedrow = itemBinding.collectedRow;
            paymentModeLay = itemBinding.paymentModeLay;
//            viewDetail = itemBinding.viewDetailBtn;
            txt_FarmID = itemBinding.txtFarmID;
            imvw_Dropdown = itemBinding.imvwDropdown;
            imvw_DropUp = itemBinding.imvwDropUp;

            tblrw_FarmID = itemBinding.tblrwFarmID;
            tblrw_PolicyName = itemBinding.tblrwPolicyName;
            tblrw_PolicyServiceDate = itemBinding.tblrwPolicyServiceDate;
            tblrw_BookingStatus = itemBinding.tblrwBookingStatus;
            tblrw_Crops = itemBinding.tblrwCrops;
            tblrw_FarmArea = itemBinding.tblrwFarmArea;
            tblrw_ValueAssured = itemBinding.tblrwValueAssured;

            txtPolicyName = itemBinding.txtPolicyName;
            txtPolicyServiceDate = itemBinding.txtPolicyServiceDate;
            txtBookingStatus = itemBinding.txtBookingStatus;
            txtFarmArea = itemBinding.txtFarmArea;
            txtCrops = itemBinding.txtCrops;
            txtValueAssured = itemBinding.txtValueAssured;

            lldetails = itemBinding.lldetails;

            txt_FarmIDs = itemBinding.txtFarmIDs;
            txtorder_number = itemBinding.txtorderNumber;
            txtBookingDate = itemBinding.txtBookingDate;
            txtServiceStatus = itemBinding.txtServiceStatus;
            txtVerificationStatus = itemBinding.txtVerificationStatus;
            txtpayment_modes = itemBinding.txtpaymentModes;
            txtFees = itemBinding.txtFees;
            txtcollected_amount = itemBinding.txtcollectedAmount;
            txtPendingFees = itemBinding.txtPendingFees;
            txtPolicyNames = itemBinding.txtPolicyNames;
            txtPolicyServiceDates = itemBinding.txtPolicyServiceDates;
            txtBookingStatuss = itemBinding.txtBookingStatuss;
            txtFarmAreas = itemBinding.txtFarmAreas;
            txtCrop = itemBinding.txtCrop;
            txtValueAssureds = itemBinding.txtValueAssureds;

        }
    }


    public ServiceHistoryAdapter(Context con, List<ServiceHistoryResponse> sList, ItemClick onC) {
        this.list = sList;
        context = con;
        this.onClickListner = onC;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }

        ServicehistoryadapterBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.servicehistoryadapter, parent, false);
        return new MyViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {


        setFontsStyleTxt(context, holder.txt_FarmIDs, 6);
        setFontsStyleTxt(context, holder.txt_FarmID, 5);
        setFontsStyleTxt(context, holder.txtorder_number, 6);
        setFontsStyleTxt(context, holder.orderNumber, 5);
        setFontsStyleTxt(context, holder.txtBookingDate, 6);
        setFontsStyleTxt(context, holder.orderDate, 5);
        setFontsStyleTxt(context, holder.txtServiceStatus, 6);
        setFontsStyleTxt(context, holder.deliveryStatus, 5);
        setFontsStyleTxt(context, holder.txtVerificationStatus, 6);
        setFontsStyleTxt(context, holder.VerificationStatus, 5);
        setFontsStyleTxt(context, holder.txtpayment_modes, 6);
        setFontsStyleTxt(context, holder.orderMode, 5);
        setFontsStyleTxt(context, holder.txtFees, 6);
        setFontsStyleTxt(context, holder.amount, 5);
        setFontsStyleTxt(context, holder.txtcollected_amount, 6);
        setFontsStyleTxt(context, holder.collectedAmount, 5);
        setFontsStyleTxt(context, holder.txtPendingFees, 6);
        setFontsStyleTxt(context, holder.pendingAmount, 5);
        setFontsStyleTxt(context, holder.txtPolicyNames, 6);
        setFontsStyleTxt(context, holder.txtPolicyName, 5);
        setFontsStyleTxt(context, holder.txtPolicyServiceDates, 6);
        setFontsStyleTxt(context, holder.txtPolicyServiceDate, 5);
        setFontsStyleTxt(context, holder.txtBookingStatuss, 6);
        setFontsStyleTxt(context, holder.txtBookingStatus, 5);
        setFontsStyleTxt(context, holder.txtFarmAreas, 6);
        setFontsStyleTxt(context, holder.txtFarmArea, 5);
        setFontsStyleTxt(context, holder.txtCrop, 6);
        setFontsStyleTxt(context, holder.txtCrops, 5);
        setFontsStyleTxt(context, holder.txtValueAssureds, 6);
        setFontsStyleTxt(context, holder.txtValueAssured, 5);

        setDynamicLanguage(context, holder.txt_FarmIDs, "FarmID", R.string.FarmID);
        setDynamicLanguage(context, holder.txtorder_number, "ServiceNumber", R.string.ServiceNumber);
        setDynamicLanguage(context, holder.txtBookingDate, "BookingDate", R.string.BookingDate);
        setDynamicLanguage(context, holder.txtServiceStatus, "ServiceStatus", R.string.ServiceStatus);
        setDynamicLanguage(context, holder.txtVerificationStatus, "VerificationStatus", R.string.VerificationStatus);
        setDynamicLanguage(context, holder.txtpayment_modes, "payment_modes", R.string.payment_modes);
        setDynamicLanguage(context, holder.txtFees, "Fees", R.string.Fees);
        setDynamicLanguage(context, holder.txtcollected_amount, "collected_amount", R.string.collected_amount);
        setDynamicLanguage(context, holder.txtPendingFees, "PendingFees", R.string.PendingFees);
        setDynamicLanguage(context, holder.txtPolicyNames, "PolicyName", R.string.PolicyName);
        setDynamicLanguage(context, holder.txtPolicyServiceDates, "PolicyServiceDate", R.string.PolicyServiceDate);
        setDynamicLanguage(context, holder.txtBookingStatuss, "BookingStatus", R.string.BookingStatus);
        setDynamicLanguage(context, holder.txtFarmAreas, "FarmArea", R.string.FarmArea);
        setDynamicLanguage(context, holder.txtCrop, "Crops", R.string.Crops);
        setDynamicLanguage(context, holder.txtValueAssureds, "ValueAssured", R.string.ValueAssured);

        if (list.get(position).getFarmID() != null && list.get(position).getFarmID() > 0) {
            holder.tblrw_FarmID.setVisibility(View.VISIBLE);
            holder.txt_FarmID.setText("" + list.get(position).getCropname());
        } else {
            holder.tblrw_FarmID.setVisibility(View.GONE);
        }
        holder.txt_FarmID.setText("" + list.get(position).getFarmID());
        holder.orderNumber.setText("" + list.get(position).getOrderID());
        holder.orderDate.setText("" + list.get(position).getOrderdate());
        if (list.get(position).getPaymentType() != null && list.get(position).getPaymentType().length() > 0) {
            holder.paymentModeLay.setVisibility(View.VISIBLE);
            holder.orderMode.setText("" + list.get(position).getPaymentType());
        } else {
            holder.paymentModeLay.setVisibility(View.GONE);
        }

        try {
            String ServiceStatus = list.get(position).getServiceStatus();
            if (!checkgetLagLongnull(ServiceStatus)) {
                holder.deliveryStatus.setText("" + ServiceStatus);
            } else {
                holder.llServiceStatus.setVisibility(View.GONE);
            }
            String VerificationStatus = list.get(position).getPolicyVerificatioStatus();
            if (!checkgetLagLongnull(VerificationStatus)) {
                holder.VerificationStatus.setText("" + VerificationStatus);
            } else {
                holder.llVerificationStatus.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.amount.setText("" + list.get(position).getOrderAmount());
        Double penAmount = list.get(position).getPendingAmount();
        Double colAmount = list.get(position).getCollectedAmount();
        if (penAmount != null && penAmount > 0.0) {
//            holder.payNowBTN.setText(context.getResources().getString(R.string.OnlyCollection));
            holder.payNowBTN.setVisibility(View.VISIBLE);
        } else {
//            holder.payNowBTN.setText(context.getResources().getString(R.string.ViewDetail));
            holder.payNowBTN.setVisibility(View.GONE);
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
        holder.collectedrow.setVisibility(View.GONE);
        holder.payNowBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (list.get(position).getOrderID() != null && list.get(position).getOrderID().length() > 0) {
//                    onClickListner.onClick(list.get(position).getOrderID() + "=pay");
                    onClickListner.onClick(list.get(position).getOrderID() + "=" + position);
                }
            }
        });

        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy");
        try {
            if (list.get(position).getPolicyName() != null && list.get(position).getPolicyName().length() > 0) {
                holder.tblrw_PolicyName.setVisibility(View.VISIBLE);
                holder.txtPolicyName.setText("" + list.get(position).getPolicyName());
            } else {
                holder.tblrw_PolicyName.setVisibility(View.GONE);
            }
            String PolicyStartDate = null, PolicyCloseDate = null;
            if (list.get(position).getStartDate() != null && !list.get(position).getStartDate().equalsIgnoreCase("null")
                    && list.get(position).getStartDate().length() > 10) {
                Date date = null;
                try {
                    date = inputFormat.parse(list.get(position).getStartDate());
                    PolicyStartDate = outputFormat.format(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            if (list.get(position).getCloseDate() != null && !list.get(position).getCloseDate().equalsIgnoreCase("null")
                    && list.get(position).getCloseDate().length() > 10) {
                Date date = null;
                try {
                    date = inputFormat.parse(list.get(position).getCloseDate());
                    PolicyCloseDate = outputFormat.format(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            if (PolicyStartDate != null && PolicyStartDate.length() > 4) {
                holder.tblrw_PolicyServiceDate.setVisibility(View.VISIBLE);
                holder.txtPolicyServiceDate.setText(PolicyStartDate + " - " + PolicyCloseDate);
            } else {
                holder.tblrw_PolicyServiceDate.setVisibility(View.GONE);
            }
            if (list.get(position).getOrderStatus() != null && list.get(position).getOrderStatus().length() > 0) {
                holder.tblrw_BookingStatus.setVisibility(View.VISIBLE);
                holder.txtBookingStatus.setText("" + list.get(position).getOrderStatus());
            } else {
                holder.tblrw_BookingStatus.setVisibility(View.GONE);
            }
            if (list.get(position).getCropname() != null && list.get(position).getCropname().length() > 0) {
                holder.tblrw_Crops.setVisibility(View.VISIBLE);
                holder.txtCrops.setText("" + list.get(position).getCropname());
            } else {
                holder.tblrw_Crops.setVisibility(View.GONE);
            }
            if (list.get(position).getArea() != null && list.get(position).getArea() > 0.0) {
                holder.tblrw_FarmArea.setVisibility(View.VISIBLE);
                holder.txtFarmArea.setText("" + list.get(position).getArea());
            } else {
                holder.tblrw_FarmArea.setVisibility(View.GONE);
            }
            if (list.get(position).getValueAssured() != null && list.get(position).getValueAssured() > 0.0) {
                holder.tblrw_ValueAssured.setVisibility(View.VISIBLE);
                holder.txtValueAssured.setText("" + list.get(position).getValueAssured());
            } else {
                holder.tblrw_ValueAssured.setVisibility(View.GONE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
//        imvw_Dropdown, imvw_DropUp
        holder.imvw_Dropdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previousposition = position;
                holder.lldetails.setVisibility(View.VISIBLE);
                holder.imvw_Dropdown.setVisibility(View.GONE);
                holder.imvw_DropUp.setVisibility(View.VISIBLE);

//                boolean expanded = movie.isExpanded();
//                movie.setExpanded(!expanded);
//                notifyItemChanged(position);
            }
        });
        holder.imvw_DropUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previousposition = -1;
                holder.lldetails.setVisibility(View.GONE);
                holder.imvw_Dropdown.setVisibility(View.VISIBLE);
                holder.imvw_DropUp.setVisibility(View.GONE);

//                notifyItemChanged(position);

            }
        });
        if (previousposition >= 0 && previousposition == position) {
            holder.lldetails.setVisibility(View.VISIBLE);
            holder.imvw_Dropdown.setVisibility(View.GONE);
            holder.imvw_DropUp.setVisibility(View.VISIBLE);
        } else {
            holder.lldetails.setVisibility(View.GONE);
            holder.imvw_Dropdown.setVisibility(View.VISIBLE);
            holder.imvw_DropUp.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void remove(int pos) {
        //   int position = mDataset.indexOf(item);
//        int actualPosition = holder.getAdapterPosition();
        list.remove(pos);
        notifyItemRemoved(pos);
//        notifyItemRangeChanged(actualPosition, model.size());
    }
}

