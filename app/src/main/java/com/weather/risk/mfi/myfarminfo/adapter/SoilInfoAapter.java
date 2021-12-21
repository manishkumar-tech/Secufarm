package com.weather.risk.mfi.myfarminfo.adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.bean.GOBean;
import com.weather.risk.mfi.myfarminfo.bean.SoilInfoBean;
import com.weather.risk.mfi.myfarminfo.utils.UtilFonts;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class SoilInfoAapter extends RecyclerView.Adapter<SoilInfoAapter.ViewHolder> {
    //    private ArrayList<SoilInfoBean> mDataset = new ArrayList<SoilInfoBean>();
    private ArrayList<HashMap<String, String>> mDataset = new ArrayList<>();

    public Context mContext;
    String imageString;
    int totalAmount = 0;

    // Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        TextView title;
        RelativeLayout itemRow;


        public ViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.title);
            itemRow = (RelativeLayout) v.findViewById(R.id.info_row);
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
    public SoilInfoAapter(Context con, ArrayList<HashMap<String, String>> myDataset) {
        mDataset = myDataset;
        mContext = con;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.info_row, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {


        // holder.setIsRecyclable(false);
        UtilFonts.UtilFontsInitialize(mContext);
        holder.title.setTypeface(UtilFonts.KT_Medium);

        holder.title.setText(mDataset.get(position).get("Heading"));
        holder.itemRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soilInfoPopupMethod(mDataset.get(position), position);
            }
        });
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    public void soilInfoPopupMethod(HashMap<String, String> data, int pos) {

        final Dialog dialog = new Dialog(mContext, R.style.DialogSlideAnim);

        //  final Dialog dialog = new Dialog(getActivity());

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
        dialog.setContentView(R.layout.soilinfo_popup);


        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, 1200);

        TextView title = (TextView) dialog.findViewById(R.id.title_popup);
        RecyclerView recyclerview_soilvalue = (RecyclerView) dialog.findViewById(R.id.recyclerview_soilvalue);

        UtilFonts.UtilFontsInitialize(mContext);
        title.setTypeface(UtilFonts.KT_Bold);

        title.setText(data.get("Heading"));

        if (data.size() > 0) {
            try {
                ArrayList<HashMap<String,String>> array = new ArrayList();
                JSONObject json = new JSONObject(data);
                Iterator<String> keys = json.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    String val = String.valueOf(json.get(key));
                    HashMap<String,String> object = new HashMap<>();
                    if (!key.equalsIgnoreCase("Heading")) {
                        object.put(key, val);
                        array.add(object);
                    }
                }
                LinearLayoutManager llm = new LinearLayoutManager(mContext);
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerview_soilvalue.setLayoutManager(llm);
                SoilInfoAdapterAdapter adapter = new SoilInfoAdapterAdapter(mContext, array);
                recyclerview_soilvalue.setAdapter(adapter);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        dialog.show();


    }
}
