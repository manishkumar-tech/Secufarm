package com.weather.risk.mfi.myfarminfo.live_cotton;

import static com.weather.risk.mfi.myfarminfo.utils.Utility.setFontsStyleTxt;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.utils.UtilFonts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Admin on 08-09-2017.
 */
public class LogOldMessageAdapter extends RecyclerView.Adapter<LogOldMessageAdapter.ViewHolder> {
    private ArrayList<MessegeListBean> mDataset = new ArrayList<MessegeListBean>();

    public Context mContext;

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView dateTime, message;
        LinearLayout row;
        CardView cardBg;
        TextView status;


        public ViewHolder(View v) {
            super(v);

            status = (TextView) v.findViewById(R.id.status);
            dateTime = (TextView) v.findViewById(R.id.date_time);
            message = (TextView) v.findViewById(R.id.message);
            cardBg = (CardView) v.findViewById(R.id.card_view);
            row = (LinearLayout) v.findViewById(R.id.message_row);

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
    public LogOldMessageAdapter(Context con, ArrayList<MessegeListBean> myDataset) {
        mDataset = myDataset;
        mContext = con;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_row, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        setFontsStyleTxt(mContext, holder.message, 7);
        setFontsStyleTxt(mContext, holder.dateTime, 4);
        setFontsStyleTxt(mContext, holder.status, 4);

        String status = mDataset.get(position).getStatus().trim();
        if (status.equalsIgnoreCase("sent")) {
            holder.status.setText(Html.fromHtml("&#x2713;&#x2713;"));
            holder.status.setTextColor(mContext.getResources().getColor(R.color.green_alert));
        } else if (status.equalsIgnoreCase("Unresolved")) {
            holder.status.setText(Html.fromHtml("&#x2713;"));
        } else if (status.equalsIgnoreCase("Resolved")) {
            holder.status.setText(Html.fromHtml("&#x2713;&#x2713;"));
            holder.status.setTextColor(mContext.getResources().getColor(R.color.green_alert));
        }

        String dt = mDataset.get(position).getDate();

        List<String> dateList = Arrays.asList(dt.split("T"));
        if (dateList.size() > 1) {
            String sss = dateList.get(0) + " & " + dateList.get(1);
            holder.dateTime.setText(sss);
        }

        holder.message.setText(mDataset.get(position).getMessage());


        holder.message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activitycall(position);

            }
        });
        holder.status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activitycall(position);

            }
        });
        holder.row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activitycall(position);
            }
        });

    }

    public void Activitycall(int position) {
        Intent in = new Intent(mContext, MessageDetailActivity.class);
        in.putExtra("req_id", mDataset.get(position).getRequestID());
        in.putExtra("isReopen", mDataset.get(position).getStatus());
        in.putExtra("imageName", mDataset.get(position).getImageName());
        mContext.startActivity(in);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }


}