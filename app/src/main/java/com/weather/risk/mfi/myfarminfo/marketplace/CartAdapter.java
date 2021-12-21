package com.weather.risk.mfi.myfarminfo.marketplace;

import static com.weather.risk.mfi.myfarminfo.utils.Utility.setDynamicLanguage;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setFontsStyleTxt;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.database.DBAdapter;
import com.weather.risk.mfi.myfarminfo.databinding.CartRowBinding;
import com.weather.risk.mfi.myfarminfo.retrofitmodelnew.CategoryDetailResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {

    private List<CategoryDetailResponse> list;

    Context context;
    private LayoutInflater layoutInflater;
    DataInterface onItemClick;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, amount, validity, unit,txtAmount,txtQuantity,txtvalidity,rem;
        TextView quantity;
        LinearLayout deleteBTN;
        LinearLayout validityLay;
        ImageView imageView;
        ImageView plusBTN, minusBTN;

        public MyViewHolder(final CartRowBinding itemBinding) {
            super(itemBinding.getRoot());
            name = itemBinding.productName;
            amount = itemBinding.amount;
            quantity = itemBinding.orderQuantity;
            deleteBTN = itemBinding.deleteCartItem;
            validity = itemBinding.validity;
            validityLay = itemBinding.validityLay;
            unit = itemBinding.unit;
            imageView = itemBinding.image;
            plusBTN = itemBinding.plusIcon;
            minusBTN = itemBinding.minusIcon;
            txtAmount = itemBinding.txtAmount;
            txtQuantity = itemBinding.txtQuantity;
            txtvalidity = itemBinding.txtvalidity;
            rem = itemBinding.rem;

        }
    }


    public CartAdapter(Context con, DataInterface dI, List<CategoryDetailResponse> sList) {
        this.list = sList;
        context = con;
        this.onItemClick = dI;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        CartRowBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.cart_row, parent, false);


        return new MyViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        setFontsStyleTxt(context, holder.txtAmount, 5);
        setFontsStyleTxt(context, holder.txtQuantity, 5);
        setFontsStyleTxt(context, holder.txtvalidity, 5);
        setFontsStyleTxt(context, holder.rem, 5);

        setDynamicLanguage(context, holder.txtAmount, "Amount", R.string.Amount);
        setDynamicLanguage(context, holder.txtQuantity, "Quantitys", R.string.Quantitys);
        setDynamicLanguage(context, holder.txtvalidity, "validity", R.string.validity);
        setDynamicLanguage(context, holder.rem, "REMOVE", R.string.REMOVE);

        holder.name.setText(list.get(position).getService());
        holder.quantity.setText("" + list.get(position).getQuantity());
        holder.amount.setText(list.get(position).getPrice() + "");
        Log.v("vsdvdss", list.get(position).getQuantity() + "");

        if (list.get(position).getImagePath() != null && list.get(position).getImagePath().length() > 4) {
            holder.imageView.setVisibility(View.VISIBLE);
            Glide.with(context).load(list.get(position).getImagePath()).into(holder.imageView);
        } else {
            holder.imageView.setVisibility(View.GONE);
        }

        if (list.get(position).getQuantityUnit() != null && list.get(position).getProductUnit() != null) {
            holder.unit.setVisibility(View.VISIBLE);
            holder.unit.setText(" / " + list.get(position).getQuantityUnit() + " " + list.get(position).getProductUnit());
        } else {
            holder.unit.setVisibility(View.GONE);
        }

        holder.plusBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String val = holder.quantity.getText().toString().trim();
                if (val != null && !val.equalsIgnoreCase("null")) {
                    int ab = Integer.parseInt(val);
                    holder.quantity.setText((ab + 1) + "");
                    list.get(position).setQuantity(ab + 1);
                    updateDbCart(list.get(position).getServiceID(), "" + (ab + 1));
                } else {
                    holder.quantity.setText("0");
                    list.get(position).setQuantity(0);
                }
            }
        });
        holder.minusBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String val = holder.quantity.getText().toString().trim();


                if (val != null && !val.equalsIgnoreCase("null")) {
                    int ab = Integer.parseInt(val);
                    if (ab > 1) {
                        holder.quantity.setText((ab - 1) + "");
                        list.get(position).setQuantity(ab - 1);
                        updateDbCart(list.get(position).getServiceID(), "" + (ab - 1));
                    }
                } else {
                    holder.quantity.setText("0");
                    list.get(position).setQuantity(0);
                }
            }
        });


        String formattedDateStart = null;
        String formattedDateEND = null;
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy");

        if (list.get(position).getStartDate() != null && list.get(position).getStartDate().length() > 10) {

            Date date = null;
            try {
                date = inputFormat.parse(list.get(position).getStartDate());
                formattedDateStart = outputFormat.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (list.get(position).getEndDate() != null && list.get(position).getEndDate().length() > 10) {

            Date date = null;
            try {
                date = inputFormat.parse(list.get(position).getEndDate());
                formattedDateEND = outputFormat.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (formattedDateStart != null && formattedDateStart.length() > 4) {

            holder.validityLay.setVisibility(View.VISIBLE);
            holder.validity.setText(formattedDateStart + " - " + formattedDateEND);
        } else {
            holder.validityLay.setVisibility(View.GONE);
        }


        //Herojit Add
        holder.validityLay.setVisibility(View.GONE);

        holder.deleteBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onItemClick.onClick(list.get(position));
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void updateDbCart(String sid, String qty) {

        DBAdapter db = new DBAdapter(context);
        db.open();

        db.updateCartByServiceId(sid, "" + qty);
        db.close();
    }
}

