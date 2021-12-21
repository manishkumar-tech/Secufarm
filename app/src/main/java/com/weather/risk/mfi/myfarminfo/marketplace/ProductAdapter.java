package com.weather.risk.mfi.myfarminfo.marketplace;

import static com.weather.risk.mfi.myfarminfo.utils.Utility.getDynamicLanguageValue;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setDynamicLanguage;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setFontsStyle;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setFontsStyleTxt;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.database.DBAdapter;
import com.weather.risk.mfi.myfarminfo.databinding.ProductRowBinding;
import com.weather.risk.mfi.myfarminfo.retrofitmodelnew.CategoryDetailResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {

    private List<CategoryDetailResponse> list;
    DataInterface onItemClick;
    Context context;
    private LayoutInflater layoutInflater;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView validity, addBTN, name, amount, description, unit, txtAmount, txtQuantity,txtvalidity;
        TextView quantity;
        ImageView plusBTN, minusBTN;
        RelativeLayout validityLay;
        ImageView imageView;


        public MyViewHolder(final ProductRowBinding itemBinding) {
            super(itemBinding.getRoot());
            name = itemBinding.productName;
            amount = itemBinding.amount;
            quantity = itemBinding.orderQuantity;
            plusBTN = itemBinding.plusIcon;
            minusBTN = itemBinding.minusIcon;
            validityLay = itemBinding.validityLay;
            validity = itemBinding.validity;
            addBTN = itemBinding.addBtn;
            txtAmount = itemBinding.txtAmount;
            txtQuantity = itemBinding.txtQuantity;
            txtvalidity = itemBinding.txtvalidity;
            unit = itemBinding.unit;
            description = itemBinding.description;
            imageView = itemBinding.image;

        }
    }


    public ProductAdapter(Context con, DataInterface ml, List<CategoryDetailResponse> sList) {
        this.list = sList;
        context = con;
        this.onItemClick = ml;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        ProductRowBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.product_row, parent, false);
        return new MyViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        setFontsStyleTxt(context, holder.addBTN, 5);
        setFontsStyleTxt(context, holder.txtAmount, 5);
        setFontsStyleTxt(context, holder.txtQuantity, 5);
        setFontsStyleTxt(context, holder.txtvalidity, 5);

        setDynamicLanguage(context, holder.addBTN, "Add", R.string.Add);
        setDynamicLanguage(context, holder.txtAmount, "Amount", R.string.Amount);
        setDynamicLanguage(context, holder.txtQuantity, "Quantitys", R.string.Quantitys);
        setDynamicLanguage(context, holder.txtvalidity, "validity", R.string.validity);


        holder.name.setText(list.get(position).getService());
        holder.quantity.setText("" + list.get(position).getQuantity());

        holder.amount.setText(list.get(position).getPrice() + "");
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

        if (list.get(position).getProductDescription() != null && list.get(position).getProductDescription().length() > 0) {
            holder.description.setVisibility(View.VISIBLE);
            holder.description.setText(list.get(position).getProductDescription());
        } else {
            holder.description.setVisibility(View.GONE);
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
                Log.v("wdasdas", val);
                if (val != null && !val.equalsIgnoreCase("null")) {
                    int ab = Integer.parseInt(val);
                    holder.quantity.setText((ab + 1) + "");
                    list.get(position).setQuantity(ab + 1);

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
                int ab = Integer.parseInt(val);
                if (val != null && !val.equalsIgnoreCase("null")) {
                    if (ab > 0) {
                        holder.quantity.setText((ab - 1) + "");
                        list.get(position).setQuantity(ab - 1);

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

        holder.addBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String qt = holder.quantity.getText().toString();
                if (qt != null && qt.length() > 0 && !qt.equalsIgnoreCase("0")) {
                    onItemClick.onClick(list.get(position));
                    holder.quantity.setText("0");

                    // updateDbCart(list.get(position).getServiceID(),""+qt);

                    Toast toast = Toast.makeText(context, list.get(position).getService() + "  "+getDynamicLanguageValue(context, "Addedinyourcart", R.string.Addedinyourcart), Toast.LENGTH_SHORT);
                    View view1 = toast.getView();
                    if (view1 != null) {
                        view1.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
                        TextView text = view1.findViewById(android.R.id.message);
                        text.setTextColor(Color.WHITE);
                    }
                    toast.show();

                } else {
                    Toast toast = Toast.makeText(context, getDynamicLanguageValue(context, "Pleaseenterquantity", R.string.Pleaseenterquantity), Toast.LENGTH_SHORT);
                    View view1 = toast.getView();
                    if (view1 != null) {
                        view1.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
                        TextView text = view1.findViewById(android.R.id.message);
                        text.setTextColor(Color.WHITE);
                    }
                    toast.show();

                }


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
}


