package com.weather.risk.mfi.myfarminfo.tubewell;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.bean.ElectricStart;
import com.weather.risk.mfi.myfarminfo.bean.ElectricStop;

import java.util.ArrayList;

/**
 * Created by Admin on 10-04-2018.
 */
public class ElectricityTable extends Fragment {


    ArrayList<ElectricStart> arrayStartList = new ArrayList<ElectricStart>();
    ArrayList<ElectricStop> arrayStopList = new ArrayList<ElectricStop>();

    public ElectricityTable(ArrayList<ElectricStart> list15, ArrayList<ElectricStop> list16) {

        arrayStartList = list15;
        arrayStopList = list16;


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
// Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.pie_popup, container, false);

        RecyclerView listView = (RecyclerView) view.findViewById(R.id.pieListValue);
        listView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listView.setLayoutManager(linearLayoutManager);

        TextView noData = (TextView) view.findViewById(R.id.no_data);

        if (arrayStartList!=null || arrayStartList.size() > 0) {

            noData.setVisibility(View.GONE);




            ShowPieDataAdapter adapter = new ShowPieDataAdapter(getActivity(),arrayStartList,arrayStopList);
            listView.setAdapter(adapter);


        } else {

            noData.setVisibility(View.VISIBLE);
        }


        return view;

    }
}

