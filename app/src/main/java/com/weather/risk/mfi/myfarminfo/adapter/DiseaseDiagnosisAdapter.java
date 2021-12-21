package com.weather.risk.mfi.myfarminfo.adapter;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.weather.risk.mfi.myfarminfo.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.itemSelectedCotton_Disease;
import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.itemStateArray_Disease;
import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.itemStateArray_Disease_setPosition;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setFontsStyleTxt;


public class DiseaseDiagnosisAdapter extends RecyclerView.Adapter<DiseaseDiagnosisAdapter.ViewHolder> {
    public Context mContext;
    public JSONArray jsonvalue;

    // Provide a suitable constructor (depends on the kind of dataset)
    public DiseaseDiagnosisAdapter(Context con, JSONArray value) {

        mContext = con;
        this.jsonvalue = value;
    }


    public void remove(int pos) {
        jsonvalue.remove(pos);
        notifyItemRemoved(pos);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.diseasediagnosisadapter, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        try {
            String Id = "", Name = "", CropID = "";
//            JSONArray jsonArray = new JSONArray(jsonvalue);
            JSONObject obj = new JSONObject(jsonvalue.get(position).toString());
            Id = obj.getString("Id");
            Name = obj.getString("Name");
            CropID = obj.getString("CropID");
            holder.txt_ID.setText(Id);
            holder.txt_Name.setText(Name);
            holder.checkbox.setText(Id);
            holder.txt_CropID.setText(CropID);


            setFontsStyleTxt(mContext, holder.txt_ID, 5);
            setFontsStyleTxt(mContext, holder.txt_Name, 5);
            setFontsStyleTxt(mContext, holder.checkbox, 5);
            setFontsStyleTxt(mContext, holder.txt_CropID, 5);

            boolean checkvalue = itemStateArray_Disease_setPosition.get(position, false);
            if (!itemStateArray_Disease_setPosition.get(position, false)) {
                holder.checkbox.setChecked(false);
            } else {
                holder.checkbox.setChecked(true);
            }
            holder.checkbox.setOnCheckedChangeListener(
                    new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            int adapterPosition = holder.getAdapterPosition();
                            itemStateArray_Disease = new SparseBooleanArray();
                            if (buttonView.isChecked()) {
                                // checked
                                holder.checkbox.setChecked(true);
                                itemStateArray_Disease.put(adapterPosition, true);
                                itemStateArray_Disease_setPosition.put(adapterPosition, true);
                            } else {
                                // not checked
                                holder.checkbox.setChecked(false);
                                itemStateArray_Disease.put(adapterPosition, false);
                                itemStateArray_Disease_setPosition.put(adapterPosition, false);
                            }
                            setSelectedCheckIDList(itemStateArray_Disease.toString(), jsonvalue);
                        }
                    }
            );

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return jsonvalue.length();
    }

    // Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView txt_ID, txt_Name, txt_CropID;
        CheckBox checkbox;

        public ViewHolder(View v) {
            super(v);
            txt_ID = (TextView) v.findViewById(R.id.txt_ID);
            txt_Name = (TextView) v.findViewById(R.id.txt_Name);
            txt_CropID = (TextView) v.findViewById(R.id.txt_CropID);
            checkbox = (CheckBox) v.findViewById(R.id.checkbox);

        }
    }

    public void setSelectedCheckIDList(String values, JSONArray addnewcall_new) {
        try {
            String value = values;
            if (value != null && value.length() > 0) {
                value = value.replace("{", "");
                value = value.replace("}", "");
                String[] array = value.split(",");
                String newselectedvalue = itemSelectedCotton_Disease;
                if (itemSelectedCotton_Disease != null && itemSelectedCotton_Disease.length() > 0) {
                    newselectedvalue = getRemoveString(newselectedvalue);
                }
                for (int i = 0; i < array.length; i++) {
                    String subarray = array[i].trim();
                    String[] newarray = subarray.split("=");
                    int position = Integer.valueOf(newarray[0]);
                    JSONObject obj = new JSONObject(addnewcall_new.get(position).toString());
                    int valueId = Integer.parseInt(obj.get("Id").toString());
                    //remove the uncheck ID
                    if (newarray[1].equals("true")) {
                        if (newselectedvalue != null && newselectedvalue.length() > 0) {
                            newselectedvalue = newselectedvalue + "," + valueId;
                        } else {
                            newselectedvalue = String.valueOf(valueId);
                        }
                    } else {
                        if (newselectedvalue != null && newselectedvalue.length() > 0) {
                            String[] newarray1 = newselectedvalue.split(",");
                            List<String> list = new ArrayList<String>(Arrays.asList(newarray1));
                            list.remove(String.valueOf(valueId));
                            newarray1 = list.toArray(new String[0]);
                            if (list.size() != 0) {
                                for (int j = 0; j < newarray1.length; j++) {
                                    if (j == 0) {
                                        newselectedvalue = newarray1[j];
                                    } else {
                                        newselectedvalue = newselectedvalue + "," + newarray1[j];
                                    }
                                }
                            } else {
                                newselectedvalue = "";
                            }
                        }
                    }
                }
                itemSelectedCotton_Disease = newselectedvalue;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String getRemoveString(String value) {
        String val = value;
        try {
            if (value != null && value.length() > 0) {
                String array[] = value.split(",");
                array = new HashSet<String>(Arrays.asList(array)).toArray(new String[0]);
                String newval = "";
                for (int i = 0; i < array.length; i++) {
                    if (i == 0) {
                        newval = array[i];
                    } else {
                        newval = newval + "," + array[i];
                    }
                }
                val = newval;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return val;
    }

}
