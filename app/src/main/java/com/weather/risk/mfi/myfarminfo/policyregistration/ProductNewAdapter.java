package com.weather.risk.mfi.myfarminfo.policyregistration;

import static com.weather.risk.mfi.myfarminfo.utils.Utility.setDynamicLanguage;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setFontsStyleTxt;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.databinding.ProductRowNewBinding;
import com.weather.risk.mfi.myfarminfo.marketplace.DataInterface;
import com.weather.risk.mfi.myfarminfo.pdfdownload.PDFDownloadTask;
import com.weather.risk.mfi.myfarminfo.retrofitmodelnew.CategoryDetailResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ProductNewAdapter extends RecyclerView.Adapter<ProductNewAdapter.MyViewHolder> {

    private List<CategoryDetailResponse> list;
    DataInterface onItemClick;
    Context context;
    private LayoutInflater layoutInflater;
    int SelectedCategoryValue = 0;
    String FarmerId, FarmID, cropID, cropName;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView validity, name, amount, /*description,*/ unit, txtServiceFees, txtvalidity,
                txtPolicyStartDate, txtSettlementlvl, txtValueAssur, txtBenchmarkYild, txtAssessmentType;
        RelativeLayout validityLay;
        ImageView imageView;
        TableRow tblrwPolicyStartDates, tblrwSettlementLevel, tblrwValueAssured, tblrwBenchmarkYield, tblrwAssismentName;
        TextView txtPolicyStartDates, txtSettlementLevel, txtValueAssured, txtBenchmarkYield, Registration_btn, txtAssismentName;
        LinearLayout ll_yeildInsurance;
        ImageView imgvw_downloaddocs, imgvw_sharedocs;

        public MyViewHolder(final ProductRowNewBinding itemBinding) {
            super(itemBinding.getRoot());
            name = itemBinding.productName;
            amount = itemBinding.amount;
            validityLay = itemBinding.validityLay;
            validity = itemBinding.validity;
            unit = itemBinding.unit;
//            description = itemBinding.description;
            imageView = itemBinding.image;
            tblrwPolicyStartDates = itemBinding.tblrwPolicyStartDates;
            txtPolicyStartDates = itemBinding.txtPolicyStartDates;
            tblrwSettlementLevel = itemBinding.tblrwSettlementLevel;
            txtSettlementLevel = itemBinding.txtSettlementLevel;
            tblrwValueAssured = itemBinding.tblrwValueAssured;
            txtValueAssured = itemBinding.txtValueAssured;
            txtAssessmentType = itemBinding.txtAssessmentType;
            tblrwBenchmarkYield = itemBinding.tblrwBenchmarkYield;
            txtBenchmarkYield = itemBinding.txtBenchmarkYield;
            Registration_btn = itemBinding.RegistrationBtn;
            imgvw_downloaddocs = itemBinding.imgvwDownloaddocs;
            imgvw_sharedocs = itemBinding.imgvwSharedocs;
            ll_yeildInsurance = itemBinding.llYeildInsurance;
            txtAssismentName = itemBinding.txtAssismentName;
            tblrwAssismentName = itemBinding.tblrwAssismentName;
            txtServiceFees = itemBinding.txtServiceFees;
            txtvalidity = itemBinding.txtvalidity;
            txtPolicyStartDate = itemBinding.txtPolicyStartDate;
            txtSettlementlvl = itemBinding.txtSettlementlvl;
            txtValueAssur = itemBinding.txtValueAssur;
            txtBenchmarkYild = itemBinding.txtBenchmarkYild;

        }
    }


    public ProductNewAdapter(Context con, DataInterface ml, List<CategoryDetailResponse> sList,
                             int SelectedCategoryValues, String farmerId, String farmID,
                             String cropID, String cropName) {
        this.list = sList;
        context = con;
        this.onItemClick = ml;
        this.SelectedCategoryValue = SelectedCategoryValues;
        this.FarmerId = farmerId;
        this.FarmID = farmID;
        this.cropID = cropID;
        this.cropName = cropName;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        ProductRowNewBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.product_row_new, parent, false);
        return new MyViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        try {
            setFontsStyleTxt(context, holder.name, 5);
            setFontsStyleTxt(context, holder.txtServiceFees, 6);
            setFontsStyleTxt(context, holder.amount, 5);
            setFontsStyleTxt(context, holder.unit, 6);
            setFontsStyleTxt(context, holder.txtvalidity, 6);
            setFontsStyleTxt(context, holder.validity, 5);
            setFontsStyleTxt(context, holder.txtPolicyStartDate, 6);
            setFontsStyleTxt(context, holder.txtPolicyStartDates, 5);
            setFontsStyleTxt(context, holder.txtSettlementlvl, 6);
            setFontsStyleTxt(context, holder.txtSettlementLevel, 5);
            setFontsStyleTxt(context, holder.txtValueAssur, 6);
            setFontsStyleTxt(context, holder.txtValueAssured, 5);
            setFontsStyleTxt(context, holder.txtBenchmarkYild, 6);
            setFontsStyleTxt(context, holder.txtBenchmarkYield, 5);
            setFontsStyleTxt(context, holder.txtAssessmentType, 6);
            setFontsStyleTxt(context, holder.txtAssismentName, 5);
            setFontsStyleTxt(context, holder.Registration_btn, 5);

            setDynamicLanguage(context, holder.txtServiceFees, "ServiceFees", R.string.ServiceFees);
            setDynamicLanguage(context, holder.txtvalidity, "validity", R.string.validity);
            setDynamicLanguage(context, holder.txtPolicyStartDate, "PolicyStartDates", R.string.PolicyStartDates);
            setDynamicLanguage(context, holder.txtSettlementlvl, "SettlementLevel", R.string.SettlementLevel);
            setDynamicLanguage(context, holder.txtValueAssur, "ValueAssureds", R.string.ValueAssureds);
            setDynamicLanguage(context, holder.txtBenchmarkYild, "Benchmarks", R.string.Benchmarks);
            setDynamicLanguage(context, holder.txtAssessmentType, "AssessmentType", R.string.AssessmentType);
            setDynamicLanguage(context, holder.Registration_btn, "PolicyRegistration", R.string.PolicyRegistration);

            holder.name.setText(list.get(position).getService());
            holder.name.setVisibility(View.GONE);
//        holder.quantity.setText("" + list.get(position).getQuantity());

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

//            if (list.get(position).getProductDescription() != null && list.get(position).getProductDescription().length() > 0) {
//                holder.description.setVisibility(View.VISIBLE);
//                holder.description.setText(list.get(position).getProductDescription());
//            } else {
//                holder.description.setVisibility(View.GONE);
//            }

            if (list.get(position).getQuantityUnit() != null && list.get(position).getProductUnit() != null) {
                holder.unit.setVisibility(View.VISIBLE);
                holder.unit.setText(" / " + list.get(position).getQuantityUnit() + " " + list.get(position).getProductUnit());
            } else {
                holder.unit.setVisibility(View.GONE);
            }
//        holder.plusBTN.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String val = holder.quantity.getText().toString().trim();
//                Log.v("wdasdas", val);
//                if (val != null && !val.equalsIgnoreCase("null")) {
//                    int ab = Integer.parseInt(val);
//                    holder.quantity.setText((ab + 1) + "");
//                    list.get(position).setQuantity(ab + 1);
//
//                } else {
//                    holder.quantity.setText("0");
//                    list.get(position).setQuantity(0);
//                }
//            }
//        });


//        holder.minusBTN.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String val = holder.quantity.getText().toString().trim();
//                int ab = Integer.parseInt(val);
//                if (val != null && !val.equalsIgnoreCase("null")) {
//                    if (ab > 0) {
//                        holder.quantity.setText((ab - 1) + "");
//                        list.get(position).setQuantity(ab - 1);
//
//                    }
//                } else {
//                    holder.quantity.setText("0");
//                    list.get(position).setQuantity(0);
//                }
//            }
//        });
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

            holder.validityLay.setVisibility(View.GONE);
//        if (formattedDateStart != null && formattedDateStart.length() > 4) {
//            holder.validityLay.setVisibility(View.VISIBLE);
//            holder.validity.setText(formattedDateStart + " - " + formattedDateEND);
//        } else {
//            holder.validityLay.setVisibility(View.GONE);
//        }


//            holder.addBTN.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    String qt = holder.quantity.getText().toString();
//                    if (qt != null && qt.length() > 0 && !qt.equalsIgnoreCase("0")) {
//                        onItemClick.onClick(list.get(position));
//                        holder.quantity.setText("0");
//
//                        // updateDbCart(list.get(position).getServiceID(),""+qt);
//
//                        Toast toast = Toast.makeText(context, list.get(position).getService() + "  Added in your cart", Toast.LENGTH_SHORT);
//                        View view1 = toast.getView();
//                        if (view1 != null) {
//                            view1.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
//                            TextView text = view1.findViewById(android.R.id.message);
//                            text.setTextColor(Color.WHITE);
//                        }
//                        toast.show();
//
//                    } else {
//                        Toast toast = Toast.makeText(context, "Please enter quantity", Toast.LENGTH_SHORT);
//                        View view1 = toast.getView();
//                        if (view1 != null) {
//                            view1.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
//                            TextView text = view1.findViewById(android.R.id.message);
//                            text.setTextColor(Color.WHITE);
//                        }
//                        toast.show();
//
//                    }
//
//
//                }
//            });

            //Add for Policy

            String PolicyStartDate = null, PolicyCloseDate = null;
            if (list.get(position).getPolicyStartDate() != null && !list.get(position).getPolicyStartDate().equalsIgnoreCase("null")
                    && list.get(position).getPolicyStartDate().length() > 10) {
                Date date = null;
                try {
                    date = inputFormat.parse(list.get(position).getPolicyStartDate());
                    PolicyStartDate = outputFormat.format(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            if (list.get(position).getPolicyCloseDate() != null && !list.get(position).getPolicyCloseDate().equalsIgnoreCase("null")
                    && list.get(position).getPolicyCloseDate().length() > 10) {
                Date date = null;
                try {
                    date = inputFormat.parse(list.get(position).getPolicyCloseDate());
                    PolicyCloseDate = outputFormat.format(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            try {
                if (PolicyStartDate != null && PolicyStartDate.length() > 4) {
                    String start[] = PolicyStartDate.split("-");
                    String close[] = PolicyCloseDate.split("-");
                    holder.tblrwPolicyStartDates.setVisibility(View.VISIBLE);
                    if (start.length > 1 && close.length > 1) {
                        holder.txtPolicyStartDates.setText(start[0] + " " + start[1] + " - " + close[0] + " " + close[1] + " , " + close[2]);
                    } else {
                        holder.txtPolicyStartDates.setText(PolicyStartDate + " - " + PolicyCloseDate);
                    }
                } else {
//                holder.tblrwPolicyStartDates.setVisibility(View.GONE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            String SettlementLevel = list.get(position).getSettlementLevel();
            if (SettlementLevel != null && SettlementLevel.length() > 0) {
                holder.tblrwSettlementLevel.setVisibility(View.VISIBLE);
                holder.txtSettlementLevel.setText(SettlementLevel);
            } else {
//                holder.tblrwSettlementLevel.setVisibility(View.GONE);
            }
            int ValueAssured = list.get(position).getValueAssured();
            if (ValueAssured != 0) {
                holder.tblrwValueAssured.setVisibility(View.VISIBLE);
                holder.txtValueAssured.setText(String.valueOf(ValueAssured));
            } else {
//                holder.tblrwValueAssured.setVisibility(View.GONE);
            }


            String AssismentName = list.get(position).getAssismentName();
            String AssismentType = list.get(position).getAssismentType();
            if (AssismentName != null && AssismentName.length() > 0) {
                holder.tblrwAssismentName.setVisibility(View.VISIBLE);
                holder.txtAssismentName.setText(String.valueOf(AssismentName));
            } else {
                holder.tblrwAssismentName.setVisibility(View.GONE);
            }

            int BenchmarkYield = list.get(position).getBenchmarkYield();
            String BenchMarkvalue = "" + BenchmarkYield;
            if (AssismentType != null && AssismentType.length() > 0) {
                if (AssismentType.equalsIgnoreCase("percentage") ||
                        AssismentName.equalsIgnoreCase("Percentage")) {
                    BenchMarkvalue = BenchMarkvalue + " %";
                } else {
                    BenchMarkvalue = BenchMarkvalue + " " + AssismentType;
                }

            }

            if (BenchmarkYield != 0 && BenchMarkvalue != null) {
                holder.tblrwBenchmarkYield.setVisibility(View.VISIBLE);
                holder.txtBenchmarkYield.setText(String.valueOf(BenchMarkvalue));
            } else {
//                holder.tblrwBenchmarkYield.setVisibility(View.GONE);
            }

            holder.Registration_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CategoryDetailResponse response = list.get(position);
//                Intent intent = new Intent(context, PolicyRegistration.class);
                    Intent intent = new Intent(context, PolicyRegistrationNew.class);
                    intent.putExtra("response", response);
                    intent.putExtra("FarmerId", FarmerId);
                    intent.putExtra("FarmID", FarmID);
                    intent.putExtra("cropID", cropID);
                    intent.putExtra("cropName", cropName);
                    context.startActivity(intent);
                }
            });

            holder.imgvw_downloaddocs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CategoryDetailResponse response = list.get(position);
                    String DocFilesURL = response.getDocumentPath();
                    new PDFDownloadTask(context, DocFilesURL, 0);//Download Only
                }
            });
            holder.imgvw_sharedocs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    CategoryDetailResponse response = list.get(position);
                    String DocFilesURL = response.getDocumentPath();
                    new PDFDownloadTask(context, DocFilesURL, 1);//Download with share
                }
            });

        } catch (Exception ex) {
            ex.printStackTrace();
        }

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
