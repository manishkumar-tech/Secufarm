package com.weather.risk.mfi.myfarminfo.tubewell;

import static com.weather.risk.mfi.myfarminfo.utils.Utility.setDynamicLanguage;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setFontsStyle;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setFontsStyleTxt;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.google.android.material.tabs.TabLayout;
import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.bean.Max1Bean;
import com.weather.risk.mfi.myfarminfo.bean.Max2Bean;
import com.weather.risk.mfi.myfarminfo.bean.Max3Bean;
import com.weather.risk.mfi.myfarminfo.bean.MaxCur1;
import com.weather.risk.mfi.myfarminfo.bean.MaxCur2;
import com.weather.risk.mfi.myfarminfo.bean.MaxCur3;
import com.weather.risk.mfi.myfarminfo.bean.Min1Bean;
import com.weather.risk.mfi.myfarminfo.bean.Min2Bean;
import com.weather.risk.mfi.myfarminfo.bean.Min3Bean;
import com.weather.risk.mfi.myfarminfo.bean.MotorStartBean;
import com.weather.risk.mfi.myfarminfo.bean.MotorStatus;
import com.weather.risk.mfi.myfarminfo.bean.MotorStopBean;
import com.weather.risk.mfi.myfarminfo.bean.MotorStopSatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 10-04-2018.
 */
public class StartStopMain extends AppCompatActivity {

    private TabLayout tabLayout;
    // private ViewPager viewPager;

    Button nextBTN;

    ArrayList<Max1Bean> arrayListMax1 = new ArrayList<Max1Bean>();
    ArrayList<Max1Bean> arrayListMax2 = new ArrayList<Max1Bean>();
    ArrayList<Max1Bean> arrayListMax3 = new ArrayList<Max1Bean>();
    ArrayList<Max1Bean> arrayListMin1 = new ArrayList<Max1Bean>();
    ArrayList<Max1Bean> arrayListMin2 = new ArrayList<Max1Bean>();
    ArrayList<Max1Bean> arrayListMin3 = new ArrayList<Max1Bean>();

    ArrayList<Max1Bean> arrayListMaxCur1 = new ArrayList<Max1Bean>();
    ArrayList<Max1Bean> arrayListMaxCur2 = new ArrayList<Max1Bean>();
    ArrayList<Max1Bean> arrayListMaxCur3 = new ArrayList<Max1Bean>();

    ArrayList<MotorStartBean> arrayListMotorStart = new ArrayList<MotorStartBean>();
    ArrayList<MotorStopBean> arrayListMotorStop = new ArrayList<MotorStopBean>();

    ArrayList<MotorStatus> arrayStartList = new ArrayList<MotorStatus>();
    ArrayList<MotorStopSatus> arrayStopList = new ArrayList<MotorStopSatus>();

    String statusType;
    StartStopTable fragmentOne;
    StartStopFrag fragmentTwo;
    MotorPieFrag fragmentThree;

    String m_on, m_off;
    TextView txtMotorStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_stop_main);

        ImageView backBTN = (ImageView) findViewById(R.id.backBTN);
        txtMotorStatus = (TextView) findViewById(R.id.txtMotorStatus);

        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        arrayListMax1 = (ArrayList<Max1Bean>) getIntent().getSerializableExtra("list1");
        arrayListMax2 = (ArrayList<Max1Bean>) getIntent().getSerializableExtra("list2");
        arrayListMax3 = (ArrayList<Max1Bean>) getIntent().getSerializableExtra("list3");

        arrayListMin1 = (ArrayList<Max1Bean>) getIntent().getSerializableExtra("list4");
        arrayListMin2 = (ArrayList<Max1Bean>) getIntent().getSerializableExtra("list5");
        arrayListMin3 = (ArrayList<Max1Bean>) getIntent().getSerializableExtra("list6");

        arrayListMaxCur1 = (ArrayList<Max1Bean>) getIntent().getSerializableExtra("list7");
        arrayListMaxCur2 = (ArrayList<Max1Bean>) getIntent().getSerializableExtra("list8");
        arrayListMaxCur3 = (ArrayList<Max1Bean>) getIntent().getSerializableExtra("list9");
        arrayListMotorStart = (ArrayList<MotorStartBean>) getIntent().getSerializableExtra("list10");

        arrayStartList = (ArrayList<MotorStatus>) getIntent().getSerializableExtra("list11");
        arrayStopList = (ArrayList<MotorStopSatus>) getIntent().getSerializableExtra("list12");
        statusType = getIntent().getStringExtra("status");
        m_on = getIntent().getStringExtra("m_on");
        m_off = getIntent().getStringExtra("m_off");

        if (m_on != null) {
            String[] splited = m_on.split("\\s+");
            if (splited.length > 0) {
                m_on = splited[0];
            }
        } else {
            m_on = "100";
        }

        if (m_off != null) {
            String[] splited = m_off.split("\\s+");
            if (splited.length > 0) {
                m_off = splited[0];
            }
        } else {
            m_off = "100";
        }


        nextBTN = (Button) findViewById(R.id.nextBTN);

        if (statusType != null && statusType.equalsIgnoreCase("0")) {

            nextBTN.setVisibility(View.GONE);
        } else {
            nextBTN.setVisibility(View.VISIBLE);
        }

        nextBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(getApplicationContext(), MaxCurFragment.class);
                in.putExtra("list1", arrayListMax1);
                in.putExtra("list2", arrayListMax2);
                in.putExtra("list3", arrayListMax3);
                in.putExtra("list4", arrayListMin1);
                in.putExtra("list5", arrayListMin2);
                in.putExtra("list6", arrayListMin3);
                in.putExtra("list7", arrayListMaxCur1);
                in.putExtra("list8", arrayListMaxCur2);
                in.putExtra("list9", arrayListMaxCur3);

                in.putExtra("status", "1");
                startActivity(in);


            }
        });


        //  viewPager = (ViewPager)findViewById(R.id.pager);
        // setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        // tabLayout.setupWithViewPager(viewPager);

        LinearLayout linearLayout = (LinearLayout) tabLayout.getChildAt(0);
        linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(Color.WHITE);
        drawable.setSize(2, 1);
        linearLayout.setDividerPadding(0);
        linearLayout.setDividerDrawable(drawable);


        bindWidgetsWithAnEvent();
        setupTabLayout();

    }

    private void setupTabLayout() {
        fragmentOne = new StartStopTable(arrayStartList, arrayStopList);
        fragmentTwo = new StartStopFrag(arrayListMotorStart);
        fragmentThree = new MotorPieFrag(m_on, m_off);
        tabLayout.addTab(tabLayout.newTab().setText("Table View"), true);
        tabLayout.addTab(tabLayout.newTab().setText("Line Chart"));
        tabLayout.addTab(tabLayout.newTab().setText("Pie Chart"));
    }

    private void bindWidgetsWithAnEvent() {
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setCurrentTabFragment(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void setCurrentTabFragment(int tabPosition) {
        switch (tabPosition) {
            case 0:
                replaceFragment(fragmentOne);
                break;
            case 1:
                replaceFragment(fragmentTwo);
                break;
            case 2:
                replaceFragment(fragmentThree);
                break;
        }
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frame_container, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(this.getSupportFragmentManager());
        adapter.addFragment(new StartStopTable(arrayStartList, arrayStopList), "TableView");
        adapter.addFragment(new StartStopFrag(arrayListMotorStart), "LineChart");
        adapter.addFragment(new MotorPieFrag(m_on, m_off), "Pie Chart");


        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        setLanguages();
    }

    public void setLanguages() {
        setFontsStyleTxt(getApplicationContext(), txtMotorStatus, 2);
        setFontsStyle(getApplicationContext(), nextBTN);

        setDynamicLanguage(getApplicationContext(), txtMotorStatus, "MotorStatus", R.string.MotorStatus);
        setDynamicLanguage(getApplicationContext(), nextBTN, "Next", R.string.Next);
    }
}