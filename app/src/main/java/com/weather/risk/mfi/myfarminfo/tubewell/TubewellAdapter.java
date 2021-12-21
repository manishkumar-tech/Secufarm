package com.weather.risk.mfi.myfarminfo.tubewell;

import static com.weather.risk.mfi.myfarminfo.utils.Utility.setDynamicLanguage;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setFontsStyle;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setFontsStyleTxt;

import android.app.Dialog;
import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.bean.TubewellListBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Admin on 11-04-2018.
 */
public class TubewellAdapter extends RecyclerView.Adapter<TubewellAdapter.ViewHolder> {
    private ArrayList<TubewellListBean> mDataset = new ArrayList<TubewellListBean>();

    public Context mContext;
    String imageString;
    TextToSpeech t1;
    private ItemClickListener mListner;
    VodafoneFragment fragments;


    // Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView expenseType;
        public Button btn, showpassword;
        TableRow cardView;


        public ViewHolder(View v) {
            super(v);
            expenseType = (TextView) v.findViewById(R.id.new_text);
            btn = (Button) v.findViewById(R.id.info_btn);
            showpassword = (Button) v.findViewById(R.id.showpassword);
            cardView = (TableRow) v.findViewById(R.id.card_id);


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
    public TubewellAdapter(Context con, ArrayList<TubewellListBean> myDataset, VodafoneFragment fragment, ItemClickListener listener) {
        mDataset = myDataset;
        mContext = con;
        this.mListner = listener;
        this.fragments = fragment;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.tubewell_row, parent, false);

        final ViewHolder vh = new ViewHolder(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListner.onItemClick(v, vh.getPosition());
            }
        });

        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {


        // holder.setIsRecyclable(false);


        setFontsStyleTxt(mContext, holder.expenseType, 6);
        setFontsStyle(mContext, holder.showpassword);
        setFontsStyle(mContext, holder.btn);

        setDynamicLanguage(mContext, holder.showpassword, "Details", R.string.Details);
        setDynamicLanguage(mContext, holder.btn, "InfoSummary", R.string.InfoSummary);

        holder.expenseType.setText(mDataset.get(position).getTubewellname());

        String ss = mDataset.get(position).getIsActive();

        if (ss != null && ss.equalsIgnoreCase("Active")) {
            holder.cardView.setBackgroundColor(mContext.getResources().getColor(R.color.green_alert));
            Log.v("ggggg_act", ss + "");
        } else {
            holder.cardView.setBackgroundColor(mContext.getResources().getColor(R.color.gray));
            Log.v("ggggg_de", ss + "");
        }


        Log.v("slkas", mDataset.get(position) + "");

     /*   t1=new TextToSpeech(mContext, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(new Locale("hin"));
                }
            }
        });
*/

        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String strrrr = mDataset.get(position).getResponse();
                tubewellInfoMethod(strrrr);
            }
        });
        holder.showpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragments.setAdapterclick(mDataset, position);
            }
        });


    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void tubewellInfoMethod(String response) {

        ArrayList<TubewellListBean> tubeList = new ArrayList<TubewellListBean>();

        try {

            JSONArray js = new JSONArray(response);

            tubeList = new ArrayList<TubewellListBean>();

            for (int i = 0; i < js.length(); i++) {
                TubewellListBean bean = new TubewellListBean();
                JSONObject jb = js.getJSONObject(i);
                bean.setTubewellname(jb.getString("Text"));

                tubeList.add(bean);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


        Log.v("kkdjklfs", tubeList.size() + "");

        final Dialog dialog = new Dialog(mContext, R.style.DialogSlideAnim);

        //  final Dialog dialog = new Dialog(getActivity());

        dialog.setCanceledOnTouchOutside(true);
        Window window = dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);


        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.BOTTOM;
        wlp.dimAmount = 0.5f;

        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        // wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        // Include dialog.xml file
        dialog.setContentView(R.layout.tubewell_info);

        if (tubeList.size() > 5) {

            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, 750);
        } else {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        }

        RecyclerView listView = (RecyclerView) dialog.findViewById(R.id.tubewellValue);
        TextView heading = (TextView) dialog.findViewById(R.id.heading);
        listView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listView.setLayoutManager(linearLayoutManager);

        TextView noData = (TextView) dialog.findViewById(R.id.no_data);

        setFontsStyleTxt(mContext, heading, 2);
        setFontsStyleTxt(mContext, noData, 5);

        setDynamicLanguage(mContext, heading, "TubewellInfo", R.string.TubewellInfo);
        setDynamicLanguage(mContext, noData, "Nodataavailable", R.string.Nodataavailable);

        if (tubeList.size() > 0) {

            noData.setVisibility(View.GONE);


            heading.setText(tubeList.get(0).getTubewellname());

            TubewellInfoAdapter adapter = new TubewellInfoAdapter(mContext, tubeList);
            listView.setAdapter(adapter);


        } else {

            noData.setVisibility(View.VISIBLE);
        }

        dialog.show();

    }


}