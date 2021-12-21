package com.weather.risk.mfi.myfarminfo.marketplace;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.database.DBAdapter;
import com.weather.risk.mfi.myfarminfo.databinding.CartActivityBinding;
import com.weather.risk.mfi.myfarminfo.retrofitmodelnew.CategoryDetailResponse;

import java.util.ArrayList;

public class MyCartActivity extends BaseActivity implements DataInterface {
    CartActivityBinding binding;
    String balanceAmnt;
    String farmerId, projectID;
    ArrayList<CategoryDetailResponse> myList = new ArrayList<>();
    DBAdapter db;

    @Override
    protected void initView(Bundle bundle, ViewDataBinding viewDataBinding) {

        binding = (CartActivityBinding) viewDataBinding;
        balanceAmnt = getIntent().getStringExtra("balance");
        farmerId = getIntent().getStringExtra("farmerId");
        projectID = getIntent().getStringExtra("project_id");
        db = new DBAdapter(this);
        db.open();

        Cursor selectedService = db.getAllCartData();
        final int selectedServiceCount = selectedService.getCount();

        if (selectedServiceCount > 0) {
            selectedService.moveToFirst();
            for (int i = 1; i <= selectedServiceCount; i++) {
                CategoryDetailResponse bean = new CategoryDetailResponse();
                String a = selectedService.getString(selectedService.getColumnIndex(DBAdapter.quantity));
                if (a!=null && !a.equalsIgnoreCase("null")) {
                    int qu = Integer.parseInt(a);
                    bean.setQuantity(qu);

                    String b = selectedService.getString(selectedService.getColumnIndex(DBAdapter.quantityUnit));
                    String c = selectedService.getString(selectedService.getColumnIndex(DBAdapter.price));
                    if (b!=null && !b.equalsIgnoreCase("null")) {
                        double b1 = Double.parseDouble(b);
                        bean.setQuantityUnit(b1);
                    }
                    bean.setProductUnit(selectedService.getString(selectedService.getColumnIndex(DBAdapter.ProductUnit)));
                    bean.setBrandName(selectedService.getString(selectedService.getColumnIndex(DBAdapter.BrandName)));
                    bean.setService(selectedService.getString(selectedService.getColumnIndex(DBAdapter.service)));
                    bean.setServiceID(selectedService.getString(selectedService.getColumnIndex(DBAdapter.serviceID)));
                    bean.setImagePath(selectedService.getString(selectedService.getColumnIndex(DBAdapter.ImagePath)));
                    bean.setEndDate(selectedService.getString(selectedService.getColumnIndex(DBAdapter.endDate)));
                    bean.setStartDate(selectedService.getString(selectedService.getColumnIndex(DBAdapter.startDate)));
                    bean.setProductDescription(selectedService.getString(selectedService.getColumnIndex(DBAdapter.ProductDescription)));
                    if (c!=null && !c.equalsIgnoreCase("null")) {
                        double c1 = Double.parseDouble(c);
                        bean.setPrice(c1);
                    }
                    myList.add(bean);
                }
                selectedService.moveToNext();
            }
        }
        db.close();


        if (myList != null && myList.size() > 0) {

            binding.recyclerView.setVisibility(View.VISIBLE);


            CartAdapter adapter = new CartAdapter(MyCartActivity.this, MyCartActivity.this, myList);
            binding.recyclerView.setLayoutManager(new LinearLayoutManager(MyCartActivity.this, LinearLayoutManager.VERTICAL, false));
            binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
            binding.recyclerView.setAdapter(adapter);


        }

        binding.moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backMethod();
            }
        });

        binding.proceedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                double amntTotal = 0.0;
                for (int i = 0; i < myList.size(); i++) {
                    double pr = myList.get(i).getPrice();
                    double qty = myList.get(i).getQuantity();
                    amntTotal = amntTotal + (pr * qty);

                }
                Intent in = new Intent(getApplicationContext(), CheckoutActivity.class);
                in.putExtra("balance", balanceAmnt);
                in.putExtra("farmerId", farmerId);
                in.putExtra("project_id", projectID);

                in.putExtra("amount", amntTotal);
                in.putParcelableArrayListExtra("list", myList);
                startActivity(in);
                // finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        backMethod();
    }

    @Override
    protected int getActivityLayout() {
        return R.layout.cart_activity;
    }

    @Override
    public void onClick(CategoryDetailResponse value) {
        if (value != null && myList != null) {
            for (int i = 0; i < myList.size(); i++) {
                if (myList.get(i).getService().equalsIgnoreCase(value.getService())) {
                    myList.remove(i);
                    db.open();
                    db.deleteCartItemByServiceId(value.getServiceID());
                    db.close();
                    break;
                }
            }
        }

    }

    public void backMethod() {

        finish();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                EditText edit = ((EditText) v);
                Rect outR = new Rect();
                edit.getGlobalVisibleRect(outR);
                Boolean isKeyboardOpen = !outR.contains((int) ev.getRawX(), (int) ev.getRawY());
                System.out.print("Is Keyboard? " + isKeyboardOpen);
                if (isKeyboardOpen) {
                    System.out.print("Entro al IF");
                    edit.clearFocus();
                    InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(edit.getWindowToken(), 0);
                }

                edit.setCursorVisible(!isKeyboardOpen);

            }
        }
        return super.dispatchTouchEvent(ev);
    }
}
