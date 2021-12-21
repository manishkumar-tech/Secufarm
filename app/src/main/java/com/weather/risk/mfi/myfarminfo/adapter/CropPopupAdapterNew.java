package com.weather.risk.mfi.myfarminfo.adapter;

import android.content.Context;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.bean.CropStatusBean;
import com.weather.risk.mfi.myfarminfo.activities.NewDashboardActivity;
import com.weather.risk.mfi.myfarminfo.utils.GraphData;
import com.weather.risk.mfi.myfarminfo.utils.UtilFonts;

import java.util.ArrayList;

import static com.weather.risk.mfi.myfarminfo.utils.Utility.setToastSMSShow;

public class CropPopupAdapterNew extends RecyclerView.Adapter<CropPopupAdapterNew.ViewHolder> {
    private ArrayList<CropStatusBean> mDataset = new ArrayList<CropStatusBean>();

    public Context mContext;
    String imageString;
    TextToSpeech t1;
    String Status, Value, Benchmark, Name;

    // Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView txt_status, txt_statusunit, txt_value, txt_valueunit, txt_benchmark, txt_benchmarkunit, txt_name;
        public TextView txt_Status, txt_Value, txt_Benchmark;
        ImageView alertimg, ImgeNext, imgvw_ndvi_info;
        LinearLayout v, ll_graph;
        WebView webView;
        ;
        TableRow tblrw_benchmark;

        public ViewHolder(View view) {
            super(view);
            v = view.findViewById(R.id.v);
            txt_status = view.findViewById(R.id.txt_status);
            txt_statusunit = view.findViewById(R.id.txt_statusunit);
            txt_value = view.findViewById(R.id.txt_value);
            txt_valueunit = view.findViewById(R.id.txt_valueunit);
            txt_benchmark = view.findViewById(R.id.txt_benchmark);
            txt_benchmarkunit = view.findViewById(R.id.txt_benchmarkunit);
            txt_name = view.findViewById(R.id.txt_name);
            alertimg = view.findViewById(R.id.alertimg);
            ImgeNext = view.findViewById(R.id.ImgeNext);
            imgvw_ndvi_info = view.findViewById(R.id.imgvw_ndvi_info);

            txt_Status = view.findViewById(R.id.txt_Status);
            txt_Value = view.findViewById(R.id.txt_Value);
            txt_Benchmark = view.findViewById(R.id.txt_Benchmark);

            tblrw_benchmark = view.findViewById(R.id.tblrw_benchmark);
            webView = view.findViewById(R.id.webView);
            ll_graph = view.findViewById(R.id.ll_graph);

        }
    }

      /*public void add(int position, String item) {
          mDataset.add(position, item);
          notifyItemInserted(position);
      }*/

    public void remove(int pos) {
        //   int position = mDataset.indexOf(item);
        mDataset.remove(pos);
        notifyItemRemoved(pos);
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public CropPopupAdapterNew(Context con, ArrayList<CropStatusBean> myDataset) {
        mDataset = myDataset;
        mContext = con;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cropstatus_row_new, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        try {
            CropStatusBean model = mDataset.get(position);

            Status = model.getStatus();
            Value = model.getValue();
            Benchmark = model.getBenchmark();
            Name = model.getName();

            UtilFonts.UtilFontsInitialize(mContext);
            holder.txt_name.setTypeface(UtilFonts.KT_Bold);
            holder.txt_Status.setTypeface(UtilFonts.KT_Medium);
            holder.txt_status.setTypeface(UtilFonts.KT_Regular);
            holder.txt_statusunit.setTypeface(UtilFonts.KT_Light);
            holder.txt_Value.setTypeface(UtilFonts.KT_Medium);
            holder.txt_value.setTypeface(UtilFonts.KT_Regular);
            holder.txt_valueunit.setTypeface(UtilFonts.KT_Light);
            holder.txt_Benchmark.setTypeface(UtilFonts.KT_Medium);
            holder.txt_benchmark.setTypeface(UtilFonts.KT_Regular);
            holder.txt_benchmarkunit.setTypeface(UtilFonts.KT_Light);


//            holder.txt_status.setText(Status);
//            holder.txt_value.setText(Value);
//            holder.txt_benchmark.setText(Benchmark);


//            holder.txt_name.setText(Name);
            if (Status != null && Status.equalsIgnoreCase("Normal")) {
                holder.alertimg.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.icon_thumbup));
                Status = mContext.getResources().getString(R.string.Normal);
//                holder.txt_status.setText(mContext.getResources().getString(R.string.Normal));
            } else if (Status != null && Status.equalsIgnoreCase("Ok")) {
                holder.alertimg.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.icon_thumbup));
                Status = mContext.getResources().getString(R.string.Ok);
//                holder.txt_status.setText(mContext.getResources().getString(R.string.Ok));
            } else if (Status != null && Status.equalsIgnoreCase("Alert")) {
                holder.alertimg.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.icon_thumbdown));
                Status = mContext.getResources().getString(R.string.Alert);
//                holder.txt_status.setText(mContext.getResources().getString(R.string.Alert));
            } else {
                if ((Name != null && Name.equalsIgnoreCase("Disease") || Name != null && Name.equalsIgnoreCase("Weather Alert")) &&
                        (Status == null || Status.equalsIgnoreCase("null") || Status.equalsIgnoreCase(""))) {
                    holder.alertimg.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.icon_thumbdown));
                    Status = mContext.getResources().getString(R.string.Alert);
//                    holder.txt_status.setText(mContext.getResources().getString(R.string.Alert));
                    holder.txt_Value.setText(mContext.getResources().getString(R.string.Name));
                    holder.txt_Benchmark.setText(mContext.getResources().getString(R.string.Conducivedays));
                } else {
//                    holder.txt_status.setText(Value);
                    Status = Value;
                }
            }

            if (Name != null && (Name.equalsIgnoreCase("NDVI") ||
                    Name.equalsIgnoreCase("Soil Moisture"))) {
                holder.ImgeNext.setVisibility(View.VISIBLE);
                holder.imgvw_ndvi_info.setVisibility(View.VISIBLE);
            } else {
                holder.ImgeNext.setVisibility(View.GONE);
                holder.imgvw_ndvi_info.setVisibility(View.GONE);
            }
            if (Name != null && (Name.equalsIgnoreCase("NDVI"))) {
                holder.imgvw_ndvi_info.setVisibility(View.VISIBLE);
            } else {
                holder.imgvw_ndvi_info.setVisibility(View.GONE);
            }

            String fromsowingdate = mContext.getResources().getString(R.string.FromSowingDate);
            String past5daysvalues = mContext.getResources().getString(R.string.Past5daysvalues);
            String next7days = mContext.getResources().getString(R.string.Next7days);

            holder.tblrw_benchmark.setVisibility(View.VISIBLE);
            if (Name != null && (Name.equalsIgnoreCase("Soil Moisture") || Name.equalsIgnoreCase("soil Moisture"))) {
                holder.txt_name.setText(mContext.getResources().getString(R.string.SoilMoisture));
                // Soil Moisture
                holder.txt_status.setText(Status);
                holder.txt_value.setText(Value);
                holder.txt_valueunit.setText(past5daysvalues);
                holder.txt_benchmark.setText(Benchmark);

            } else if (Name != null && (Name.trim().equalsIgnoreCase("Disease") || Name.trim().equalsIgnoreCase("disease"))) {
                holder.txt_name.setText(mContext.getResources().getString(R.string.Disease));
                //Disease
                holder.txt_status.setText(mContext.getResources().getString(R.string.Alert));
                holder.txt_statusunit.setText(next7days);
                holder.txt_value.setText(Value);
                holder.txt_benchmark.setText(Benchmark);
                holder.txt_benchmarkunit.setText(fromsowingdate);
            } else if (Name != null && (Name.trim().equalsIgnoreCase("Weather Alert") || Name.trim().equalsIgnoreCase("weather alert"))) {
                holder.txt_name.setText(mContext.getResources().getString(R.string.WeatherAlert));
                //Weather Alert
                holder.txt_status.setText(mContext.getResources().getString(R.string.Alert));
                holder.txt_statusunit.setText(next7days);
                holder.txt_value.setText(Value);
                holder.txt_benchmark.setText(Benchmark);
                holder.tblrw_benchmark.setVisibility(View.GONE);
            } else if (Name != null && (Name.trim().equalsIgnoreCase("Rain") || Name.trim().equalsIgnoreCase("rain"))) {
                holder.txt_name.setText(mContext.getResources().getString(R.string.Rain));
                //Rain
                holder.txt_status.setText(Status);
                holder.txt_value.setText(Value);
                holder.txt_valueunit.setText(fromsowingdate);
                holder.txt_benchmark.setText(Benchmark);
                holder.txt_benchmarkunit.setText(fromsowingdate);

            } else {
                holder.txt_name.setText(Name);
                if (Name != null && (Name.equalsIgnoreCase("NDVI")) || Name.equalsIgnoreCase("ndvi")) {
                    holder.txt_status.setText(Status);
                    holder.txt_value.setText(Value);
                    holder.txt_valueunit.setText(past5daysvalues);
                    holder.txt_benchmark.setText(Benchmark);
                } else {
                    holder.txt_status.setText(Status);
                    holder.txt_value.setText(Value);
                    holder.txt_benchmark.setText(Benchmark);
                }
            }


            if (Name != null && !Name.equalsIgnoreCase("null")) {
                holder.ImgeNext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //not working properly

                        CropStatusBean model1 = mDataset.get(position);
                        String disName = model1.getName();

                        if (disName.equalsIgnoreCase("NDVI")) {
                            Intent in = new Intent(mContext, NewDashboardActivity.class);
                            in.putExtra("from", "ndvi");
                            mContext.startActivity(in);
                        } else if (disName.equalsIgnoreCase("Soil Moisture")) {
                            Intent in = new Intent(mContext, NewDashboardActivity.class);
                            in.putExtra("from", "soilm");
                            mContext.startActivity(in);
                        }
                    }
                });
                holder.imgvw_ndvi_info.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setToastSMSShow(1, mContext, mContext.getResources().getString(R.string.NDVICropStandard));
                    }
                });

            }

            //Graph
            holder.ll_graph.setVisibility(View.GONE);
            String Heading = "";
            String[] Labels = new String[2];
            Labels[0] = mContext.getResources().getString(R.string.Value);
            Labels[1] = mContext.getResources().getString(R.string.BenchMark);
            String[] Values = new String[2];
            String[] ColorCode = new String[2];
            String MaxValues = "1";
            try {
                if (Name != null && (Name.equalsIgnoreCase("NDVI") || Name.equalsIgnoreCase("ndvi"))) {
                    holder.ll_graph.setVisibility(View.VISIBLE);
                    if (Value != null && Value.length() > 0) {
                        Values[0] = Value;
                    }
                    if (Benchmark != null && Benchmark.length() > 0) {
                        if (Benchmark.contains("-")) {
                            String[] val = Benchmark.split("-");
                            Values[1] = val[0];
                        }
                    }
                    MaxValues = "1";
                    Heading = "NDVI";
                    ColorCode[0] = "#7FE817";
                    ColorCode[1] = "#5F8925";
                } else if (Name != null && (Name.equalsIgnoreCase("Soil Moisture") || Name.equalsIgnoreCase("soil Moisture"))) {
                    holder.ll_graph.setVisibility(View.VISIBLE);
                    if (Value != null && Value.length() > 0) {
                        String val = Value.replace("%", "");
                        if (val.contains("-")) {
                            String[] vals = val.split("-");
                            Values[0] = String.valueOf((Float.parseFloat(vals[0].trim()) + Float.parseFloat(vals[1].trim())) / 2);
                            MaxValues = vals[1].trim();
                        }
                    }
                    if (Benchmark != null && Benchmark.length() > 0) {
                        String newbenchmrk = Benchmark.replace("%", "");
                        if (newbenchmrk.contains("-")) {
                            String[] val = newbenchmrk.split("-");
                            Values[1] = String.valueOf((Float.parseFloat(val[0].trim()) + Float.parseFloat(val[1].trim())) / 2);
                            MaxValues = val[1].trim();
                        }
                    }
                    Heading = "Soil Moisture";
                    ColorCode[0] = "#cbf5ef";
                    ColorCode[1] = "#8aede0";
                } else if (Name != null && (Name.trim().equalsIgnoreCase("Rain") || Name.trim().equalsIgnoreCase("rain"))) {
                    holder.ll_graph.setVisibility(View.VISIBLE);
                    if (Value != null && Value.length() > 0) {
                        Values[0] = Value;
                        MaxValues = String.valueOf(Float.parseFloat(Value.trim()) + 10);
                    }
                    if (Benchmark != null && Benchmark.length() > 0) {
                        Values[1] = Benchmark;
                        MaxValues = String.valueOf(Float.parseFloat(Benchmark.trim()) + 10);
                    }
                    Heading = "Rain";
                    ColorCode[0] = "#c3eefa";
                    ColorCode[1] = "#6fdaf7";
                } else {
                    holder.ll_graph.setVisibility(View.GONE);
                }

                holder.webView.getSettings().setJavaScriptEnabled(true);
                String Data = GraphData.setGraph(Heading, Labels, Values, MaxValues, ColorCode);
                holder.webView.loadDataWithBaseURL(null, Data, "text/html", "utf-8", null);


            } catch (Exception ex) {
                ex.printStackTrace();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }


}
