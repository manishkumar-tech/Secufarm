package com.weather.risk.mfi.myfarminfo.marketplace;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.databinding.ProductOrderDetailRowBinding;


import java.util.List;

public class ProductOrderDetailAdapter extends RecyclerView.Adapter<ProductOrderDetailAdapter.MyViewHolder> {

    private List<DataDT> list;

    Context context;
    boolean flagO;
    private LayoutInflater layoutInflater;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, amount, unit, category, brand, quantityPurchased, quantityDelivered;
        TextView quantity;
        ImageView plusBTN, minusBTN;
        ImageView imageView, removeBTN;
        LinearLayout deliver_lay;


        public MyViewHolder(final ProductOrderDetailRowBinding itemBinding) {
            super(itemBinding.getRoot());
            name = itemBinding.productName;
            amount = itemBinding.amount;
            quantity = itemBinding.quantity;
            category = itemBinding.category;
            brand = itemBinding.brand;
            unit = itemBinding.unit;
            imageView = itemBinding.image;
            plusBTN = itemBinding.plusIcon;
            minusBTN = itemBinding.minusIcon;
            removeBTN = itemBinding.removeBtn;
            quantityDelivered = itemBinding.quantityDelivered;
            quantityPurchased = itemBinding.purchasedQuantity;
            deliver_lay = itemBinding.deliverLay;

        }
    }


    public ProductOrderDetailAdapter(Context con, List<DataDT> sList, boolean flag) {
        this.list = sList;
        context = con;
        this.flagO = flag;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        ProductOrderDetailRowBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.product_order_detail_row, parent, false);
        return new MyViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.name.setText(list.get(position).getProductName());
        //  list.get(position).get

        holder.quantity.setText("" + list.get(position).getDeliverQty());

        if (list.get(position).getQuantityPurchased() != null && list.get(position).getNoOfUnitSold() != null && list.get(position).getQuantityPurchased() == list.get(position).getNoOfUnitSold()) {
            holder.deliver_lay.setVisibility(View.GONE);
            list.get(position).setDeliverQty(0);

        } else if (flagO) {
            holder.deliver_lay.setVisibility(View.GONE);
            list.get(position).setDeliverQty(0);
        } else {
            holder.deliver_lay.setVisibility(View.VISIBLE);
        }

        if (list.get(position).getQuantityPurchased() != null) {
            holder.quantityPurchased.setText("" + list.get(position).getQuantityPurchased());
        } else {
            holder.quantityPurchased.setText("0");
        }
        if (list.get(position).getNoOfUnitSold() != null) {
            holder.quantityDelivered.setText("" + list.get(position).getNoOfUnitSold());
        } else {
            holder.quantityDelivered.setText("0");
        }
        holder.brand.setText("" + list.get(position).getBrandName());
        holder.category.setText("" + list.get(position).getProductCategory());
        holder.amount.setText(list.get(position).getProductPrice() + "");
        if (list.get(position).getImagePath() != null && list.get(position).getImagePath().length() > 4) {
            holder.imageView.setVisibility(View.VISIBLE);
            Glide.with(context).load(list.get(position).getImagePath()).into(holder.imageView);
        } else {
            holder.imageView.setVisibility(View.GONE);
        }
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list.get(position).getImagePath() != null && list.get(position).getImagePath().length() > 4) {
                    imagePopup(list.get(position).getImagePath());
                }
            }
        });
        if (list.get(position).getProductUnit() != null) {
            holder.unit.setVisibility(View.VISIBLE);
            holder.unit.setText(" / " + list.get(position).getProductUnit());
        } else {
            holder.unit.setVisibility(View.GONE);
        }
        holder.plusBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String val = holder.quantity.getText().toString().trim();
                Integer max = list.get(position).getRemainingUnitToSold();
                if (val != null && !val.equalsIgnoreCase("null") && val.length() > 0 && max!=null) {
                    int ab = Integer.parseInt(val);
                    if (max>ab) {
                        holder.quantity.setText((ab + 1) + "");
                        list.get(position).setDeliverQty((ab + 1));
                    }else {
                        Toast.makeText(context,"You can't enter more than "+max,Toast.LENGTH_SHORT).show();
                    }

                } else {
                    holder.quantity.setText("0");
                    list.get(position).setDeliverQty(0);

                }
            }
        });
        holder.minusBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String val = holder.quantity.getText().toString().trim();
                if (val != null && !val.equalsIgnoreCase("null") && val.length() > 0) {
                    int ab = Integer.parseInt(val);
                    if (ab >= 1) {
                        holder.quantity.setText((ab - 1) + "");
                        list.get(position).setDeliverQty((ab - 1));
                    }
                } else {
                    holder.quantity.setText("0");
                    list.get(position).setDeliverQty(0);

                }
            }
        });
        holder.removeBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(position);
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public void imagePopup(String imag) {
        final Dialog dialog = new Dialog(context);
        dialog.setCanceledOnTouchOutside(true);
        Window window = dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.dimAmount = 0.5f;
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        // wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        // Include dialog.xml file
        dialog.setContentView(R.layout.image_popup);
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.show();
        final ImageView cancel = (ImageView) dialog.findViewById(R.id.cancel_popup);
        final ImageView img = (ImageView) dialog.findViewById(R.id.image_popup);
        Glide.with(context).load(imag).into(img);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

    }

    public void remove(int pos) {
        list.remove(pos);
        notifyDataSetChanged();
    }
}


