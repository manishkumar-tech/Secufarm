package com.weather.risk.mfi.myfarminfo.marketplace;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
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

import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.SN_MyCartNew;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getDynamicLanguageToast;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getUIDforScreenTracking;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setDynamicLanguage;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setFontsStyle;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setFontsStyleTxt;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setScreenTracking;

public class MyCartNew extends BaseActivity implements DataInterface {
    CartActivityBinding binding;
    ArrayList<CategoryDetailResponse> myList = new ArrayList<>();
    String balanceAmnt;

    String farmerId, projectID;
    DBAdapter db;
    String farmID = null;

    @Override
    protected void initView(Bundle bundle, ViewDataBinding viewDataBinding) {

        binding = (CartActivityBinding) viewDataBinding;
        balanceAmnt = getIntent().getStringExtra("balance");
        farmerId = getIntent().getStringExtra("farmerId");
        projectID = getIntent().getStringExtra("project_id");
        farmID = getIntent().getStringExtra("farmID");
        db = new DBAdapter(this);
        db.open();

        Cursor selectedService = db.getAllCartData();
        final int selectedServiceCount = selectedService.getCount();

        if (selectedServiceCount > 0) {
            selectedService.moveToFirst();
            for (int i = 1; i <= selectedServiceCount; i++) {
                CategoryDetailResponse bean = new CategoryDetailResponse();
                String a = selectedService.getString(selectedService.getColumnIndex(DBAdapter.quantity));
                if (a != null && !a.equalsIgnoreCase("null")) {
                    int qu = Integer.parseInt(a);
                    bean.setQuantity(qu);

                    String b = selectedService.getString(selectedService.getColumnIndex(DBAdapter.quantityUnit));
                    String c = selectedService.getString(selectedService.getColumnIndex(DBAdapter.price));
                    if (b != null && !b.equalsIgnoreCase("null") && b.length() > 0) {
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
                    if (c != null && !c.equalsIgnoreCase("null") && c.length() > 0) {
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


            CartAdapter adapter = new CartAdapter(MyCartNew.this, MyCartNew.this, myList);
            binding.recyclerView.setLayoutManager(new LinearLayoutManager(MyCartNew.this, LinearLayoutManager.VERTICAL, false));
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

                if (myList != null && myList.size() > 0) {
                    double amntTotal = 0.0;
                    for (int i = 0; i < myList.size(); i++) {
                        double pr = myList.get(i).getPrice();
                        double qty = myList.get(i).getQuantity();
                        amntTotal = amntTotal + (pr * qty);

                    }
                    Intent in = new Intent(getApplicationContext(), CheckoutActivityNew.class);
                    in.putExtra("balance", balanceAmnt);
                    in.putExtra("farmerId", farmerId);
                    in.putExtra("project_id", projectID);

                    in.putExtra("farmID", farmID);
                    in.putExtra("amount", amntTotal);
                    in.putParcelableArrayListExtra("list", myList);
                    startActivity(in);
                    // finish();
                } else {
                    getDynamicLanguageToast(getApplicationContext(), "Thereisnoitems", R.string.Thereisnoitems);
                }
            }
        });

    }

    @Override
    public void onBackPressed() {

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
                    db.open();
                    db.deleteCartItemByServiceId(value.getServiceID());
                    db.close();
                    myList.remove(i);
                    getDynamicLanguageToast(getApplicationContext(), "Itemremovedsuccessfully", R.string.Itemremovedsuccessfully);
                    break;
                }
            }
        }

    }

    public void backMethod() {
       /* Intent in = new Intent(getApplicationContext(), ProductActivity.class);
        in.putParcelableArrayListExtra("list", myList);
        in.putExtra("balance", balanceAmnt);
        in.putExtra("farmerId", farmerId);
        in.putExtra("project_id",projectID);
        startActivity(in);*/
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

    @Override
    protected void onResume() {
        super.onResume();
        setLanguages();
    }
    public void setLanguages() {
        //Heading
        setFontsStyleTxt(this, binding.txtFarmRegistration, 2);
        setFontsStyle(this, binding.moreBtn);
        setFontsStyle(this, binding.proceedBtn);

        //Tab Service
        setDynamicLanguage(this, binding.txtFarmRegistration, "YourCart", R.string.YourCart);
        setDynamicLanguage(this, binding.moreBtn, "MoreProducts", R.string.MoreProducts);
        setDynamicLanguage(this, binding.proceedBtn, "ProceedToCheckout", R.string.ProceedToCheckout);

    }

}
