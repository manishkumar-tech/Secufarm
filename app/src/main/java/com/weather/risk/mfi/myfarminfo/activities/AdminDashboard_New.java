package com.weather.risk.mfi.myfarminfo.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.cattledashboard.CattleDashboards;
import com.weather.risk.mfi.myfarminfo.database.DBAdapter;
import com.weather.risk.mfi.myfarminfo.databinding.AdmindashboardNewBinding;
import com.weather.risk.mfi.myfarminfo.home.AppController;
import com.weather.risk.mfi.myfarminfo.marketplace.NewProductActivity;
import com.weather.risk.mfi.myfarminfo.retofitservices.ApiService;
import com.weather.risk.mfi.myfarminfo.retofitservices.ResponseObserver;
import com.weather.risk.mfi.myfarminfo.retrofitmodelnew.LoginCheckCattleDashboardResponse;
import com.weather.risk.mfi.myfarminfo.utils.AppConstant;
import com.weather.risk.mfi.myfarminfo.utils.TransparentProgressDialog;
import com.weather.risk.mfi.myfarminfo.utils.UtilFonts;
import com.weather.risk.mfi.myfarminfo.utils.Utility;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

import static com.weather.risk.mfi.myfarminfo.activities.LoginWithOtp_New.CattleDash_OwnerIDList;
import static com.weather.risk.mfi.myfarminfo.activities.LoginWithOtp_New.strPhoneno;
import static com.weather.risk.mfi.myfarminfo.activities.LoginWithOtp_New.strToken;
import static com.weather.risk.mfi.myfarminfo.logging.LocalFileWriter.SaveLocalFile;
import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.SN_AdminDashboard;
import static com.weather.risk.mfi.myfarminfo.utils.AppManager.getCattleOwnerID;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getAPIimeResponseinSecond;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getSMS;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getUIDforScreenTracking;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setDynamicLanguage;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setFontsStyleTxt;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setScreenTracking;

public class AdminDashboard_New extends AppCompatActivity {
    AdmindashboardNewBinding binding;
    SharedPreferences prefs;
    DBAdapter db;
    String UID = "";

    String CheckCattleDashBoard = "OFF";
    private ApiService apiService;
    private CompositeDisposable compositeDisposable;
    TransparentProgressDialog dialog;
    private long mRequestStartTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.admindashboard_new);

        apiService = AppController.getInstance().getApiServiceWeatherSecureProAPI();
        compositeDisposable = new CompositeDisposable();

        if (prefs == null) {
            prefs = getSharedPreferences(AppConstant.SHARED_PREFRENCE_NAME, MODE_PRIVATE);
        }

        AppConstant.PREFRENCE_KEY_CattleDashboardMobileValue = prefs.getString(AppConstant.PREFRENCE_KEY_CattleDashboardMobileKey, null);
        strPhoneno = AppConstant.PREFRENCE_KEY_CattleDashboardMobileValue;
        CheckCattleDashBoard = prefs.getString(AppConstant.PREFRENCE_KEY_IS_CattleDashBoardNOOFF, null);


        setclickListionter(binding.imgvwFarmerDashbaord, 1);
        setclickListionter(binding.imgvwFarmerDashbaords, 1);
        setclickListionter(binding.txtFarmerDashbaord, 1);


        setclickListionter(binding.imgvwCattleDashbaord, 3);
        setclickListionter(binding.imgvwCattleDashbaords, 3);
        setclickListionter(binding.txtCattleDashbaord, 3);

        db = new DBAdapter(this);

        binding.cardviewlanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminDashboard_New.this, POPUpLanguageSelection.class);
                intent.putExtra("ActivityName", "AdminDashboard_New");
                startActivity(intent);
            }
        });


        binding.imageviewLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accountAlert();
            }
        });

        //ScreenTracking
        UID = getUIDforScreenTracking();
        setScreenTracking(this, db, SN_AdminDashboard, UID);
//        checkScreenTracking(this, db);
//        setEnableDisable(CheckCattleDashBoard);
        if (CheckCattleDashBoard.equalsIgnoreCase("ON")) {
            setEnableDisable(CheckCattleDashBoard);
        } else {
            checkCattleDashboardLogin();
        }

    }

    public void setclickListionter(ImageView imageView, int flag) {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    switch (flag) {
                        case 1://Farmer Dashbaord
                            setFarmerDashbaord();
                            break;
                        case 3://Cattle Dashbaord
                            setCattleDashboard();
                            break;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public void setclickListionter(TextView textView, int flag) {
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    switch (flag) {
                        case 1://Farmer Dashbaord
                            setFarmerDashbaord();
                            break;
                        case 3://Cattle Dashbaord
                            setCattleDashboard();
                            break;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public void setFarmerDashbaord() {
        Intent in4 = new Intent(getApplicationContext(), MainProfileActivity.class);
        startActivity(in4);
    }


    public void setCattleDashboard() {
        Intent in4 = new Intent(getApplicationContext(), CattleDashboards.class);
        startActivity(in4);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (db != null) {
            db = new DBAdapter(this);
        }
        Log.d("onResume Method", "onResume Method called");
        setLanguages();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (db != null) {
            db = new DBAdapter(this);
        }
        Log.d("OnPause Method", "OnPause Method called");
        setScreenTracking(this, db, SN_AdminDashboard, UID);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (db != null) {
            db = new DBAdapter(this);
        }
        Log.d("onStop Method", "onStop Method called");
        setScreenTracking(this, db, SN_AdminDashboard, UID);
    }

    @Override
    public void onBackPressed() {
        // code here to show dialog
        super.onBackPressed();  // optional depending on your needs
        Log.d("onBackPressed Method", "onBackPressed Method called");

//        Intent in = new Intent(AdminDashboard_New.this, NewHomeScreen.class);
//        startActivity(in);
        finish();
    }

    public void setLanguages() {
        setDynamicLanguage(this, binding.txtFarmerDashbaord, "FarmerDashbaord", R.string.FarmerDashbaord);
        setDynamicLanguage(this, binding.txtCattleDashbaord, "CattleDashbaord", R.string.CattleDashbaord);

        setFontsStyleTxt(this, binding.txtFarmerDashbaord, 5);
        setFontsStyleTxt(this, binding.txtCattleDashbaord, 5);

    }

    public void setEnableDisable(String flag) {
        if (flag.equalsIgnoreCase("ON")) {
//            binding.cardviewCattleDashboard.setVisibility(View.VISIBLE);
//            binding.cardviewFarmerDashbaord.setVisibility(View.GONE);
            setCattleDashboard();
            finish();
        } else if (flag.equalsIgnoreCase("ONLY")) {
            binding.cardviewCattleDashboard.setVisibility(View.VISIBLE);
            binding.cardviewFarmerDashbaord.setVisibility(View.VISIBLE);
        } else {
//            binding.cardviewCattleDashboard.setVisibility(View.GONE);
//            binding.cardviewFarmerDashbaord.setVisibility(View.VISIBLE);
            setFarmerDashbaord();
            finish();
        }
    }


    public void checkCattleDashboardLogin() {
        dialog = new TransparentProgressDialog(
                AdminDashboard_New.this, getResources().getString(R.string.LoginCattleDashboardData));
        dialog.setCancelable(false);
//        String strToken = "", strPhoneno = "";
        String requestString = getCattleOwnerID(strToken, strPhoneno);
        mRequestStartTime = System.currentTimeMillis();
        apiService.getCattleOwnerID(strToken, strPhoneno).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResponseObserver<Response<List<LoginCheckCattleDashboardResponse>>>(compositeDisposable) {
                    @Override
                    public void onNetworkError(Throwable e) {
                        // calculate the duration in milliseconds
                        int seconds = getAPIimeResponseinSecond(mRequestStartTime);
//                        Toast.makeText(getApplicationContext(), String.valueOf(seconds), Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                        Log.v("wvqevqevqe", e.getMessage() + " " + e.toString());
                        SaveLocalFile(db, AdminDashboard_New.this, SN_AdminDashboard, requestString, "", getSMS(e.getMessage()), "" + seconds, AppConstant.farm_id, "Error");
                        setEnableDisable("OFF");
                    }

                    @Override
                    public void onServerError(Throwable e, int code) { // calculate the duration in milliseconds
                        int seconds = getAPIimeResponseinSecond(mRequestStartTime);
//                        Toast.makeText(getApplicationContext(), String.valueOf(seconds), Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                        SaveLocalFile(db, AdminDashboard_New.this, SN_AdminDashboard, requestString, "", "Server API Error", "" + seconds, AppConstant.farm_id, "Error");
                        setEnableDisable("OFF");
                    }

                    @Override
                    public void onNext(Response<List<LoginCheckCattleDashboardResponse>> Response) {
                        // calculate the duration in milliseconds
                        int seconds = getAPIimeResponseinSecond(mRequestStartTime);
//                        Toast.makeText(getApplicationContext(), String.valueOf(seconds), Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                        CattleDash_OwnerIDList = Response.body();

                        Gson gson = new Gson();
                        String response = gson.toJson(CattleDash_OwnerIDList);
                        Log.v("ABC Log", response.toString());


                        if (seconds > 3) {
                            SaveLocalFile(db, AdminDashboard_New.this, SN_AdminDashboard, requestString, response, "", "" + seconds, AppConstant.farm_id, "Working");
                        }
                        if (CattleDash_OwnerIDList != null && CattleDash_OwnerIDList.size() > 0) {
                            setCattleDashboardMobileNo(strPhoneno);
                            setEnableDisable("ONLY");
                        } else {
                            setEnableDisable("OFF");
                        }
                    }

                });
    }

    public void setCattleDashboardMobileNo(String Value) {
        SharedPreferences prefs1 = getSharedPreferences(AppConstant.SHARED_PREFRENCE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs1.edit();
        editor.putString(AppConstant.PREFRENCE_KEY_CattleDashboardMobileKey, Value);
        editor.apply();
        strPhoneno = Value;
        AppConstant.PREFRENCE_KEY_CattleDashboardMobileValue = prefs.getString(AppConstant.PREFRENCE_KEY_CattleDashboardMobileKey, Value);
    }

    private void accountAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String VisiblName = AppConstant.visible_Name;
        if (VisiblName == null)
            VisiblName = "";
        builder.setMessage(getResources().getString(R.string.Dear) + " " + VisiblName + " \n \n" + getResources().getString(R.string.areusurelogout));
        builder.setNegativeButton(getResources().getString(R.string.No), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton(getResources().getString(R.string.Yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


//                boolean isLogin = prefs.getBoolean(AppConstant.PREFRENCE_KEY_ISLOGIN, false);
//                Utility.setLogout(MainProfileActivity.this, prefs);
//                SharedPreferences.Editor editor = prefs.edit();
//                AppConstant.isLogin = false;
//                editor.putString(AppConstant.PREFRENCE_KEY_USER_ID, "");
//                editor.putString(AppConstant.PREFRENCE_KEY_VISIBLE_NAME, "");
//                editor.putBoolean(AppConstant.PREFRENCE_KEY_ISLOGIN, false);
//                editor.putString(AppConstant.PREFRENCE_KEY_IS_SAVE_ALL_RESPONSE, "null");
//                editor.apply();
                if (prefs == null) {
                    prefs = getSharedPreferences(AppConstant.SHARED_PREFRENCE_NAME, MODE_PRIVATE);
                }
                Utility.setLogout(AdminDashboard_New.this, prefs);

                Intent in = new Intent(getApplicationContext(), NewHomeScreen.class);
                in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(in);
                finish();

            }
        });
        builder.show();
    }

}
