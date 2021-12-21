package com.weather.risk.mfi.myfarminfo.cattledashboard;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.activities.AdminDashboard_New;
import com.weather.risk.mfi.myfarminfo.activities.NewHomeScreen;
import com.weather.risk.mfi.myfarminfo.database.DBAdapter;
import com.weather.risk.mfi.myfarminfo.databinding.CattledashboardfarmerpopupBinding;
import com.weather.risk.mfi.myfarminfo.databinding.CattlesdashboardsBinding;
import com.weather.risk.mfi.myfarminfo.home.AppController;
import com.weather.risk.mfi.myfarminfo.retofitservices.ApiService;
import com.weather.risk.mfi.myfarminfo.retofitservices.ResponseObserver;
import com.weather.risk.mfi.myfarminfo.retrofitmodelnew.CattleDashboardOwnerListResponse;
import com.weather.risk.mfi.myfarminfo.retrofitmodelnew.LoginCheckCattleDashboardResponse;
import com.weather.risk.mfi.myfarminfo.services.CallRecordingService;
import com.weather.risk.mfi.myfarminfo.utils.AppConstant;
import com.weather.risk.mfi.myfarminfo.utils.AppManager;
import com.weather.risk.mfi.myfarminfo.utils.TransparentProgressDialog;
import com.weather.risk.mfi.myfarminfo.utils.UtilFonts;
import com.weather.risk.mfi.myfarminfo.utils.Utility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static com.weather.risk.mfi.myfarminfo.activities.LoginWithOtp_New.CattleDash_OwnerIDList;
import static com.weather.risk.mfi.myfarminfo.activities.LoginWithOtp_New.strPhoneno;
import static com.weather.risk.mfi.myfarminfo.activities.LoginWithOtp_New.strToken;
import static com.weather.risk.mfi.myfarminfo.logging.LocalFileWriter.SaveLocalFile;
import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.SN_CattleDashbaords;
import static com.weather.risk.mfi.myfarminfo.utils.AppManager.getCattleOwnerID;
import static com.weather.risk.mfi.myfarminfo.utils.AppManager.getOwnerIDLiset;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getAPIimeResponseinSecond;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getDateReplace;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getDynamicLanguageValue;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getSMS;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getStringCheck;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getUIDforScreenTracking;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setCustomSearchableSpinner;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setScreenTracking;


public class CattleDashboards extends AppCompatActivity {

    CattlesdashboardsBinding binding;
    String selectedLatitude = null, selectedLongitude = null, Contour = null;
    String UID = "", SelectCattleBreed = "0", SelectCattleType = "0",
            SelectCattleFarmerName = "0", CattleFarmerName = "0";
    DBAdapter db;
    public static String CattleDash_SelectCattleFarmerID = "0";

    public static ArrayList<JSONObject> jsonArrayLifeStage = new ArrayList<>();
    public static ArrayList<JSONObject> jsonArrayBreed = new ArrayList<>();
    ArrayList<CattleObject> CattleFarmerList = new ArrayList<>();
    String OwnerCattleList = null;
    JSONArray OwnerCattlenameListarray = new JSONArray();
    SharedPreferences prefs;


    private ApiService apiService;
    private CompositeDisposable compositeDisposable;
    TransparentProgressDialog dialog;
    private long mRequestStartTime;
    public static String SelectedLifeStageID = "", SelectedLifeStageName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.cattlesdashboards);
        setFonts();
        db = new DBAdapter(this);
        if (prefs == null) {
            prefs = getSharedPreferences(AppConstant.SHARED_PREFRENCE_NAME, MODE_PRIVATE);
        }

        apiService = AppController.getInstance().getApiServiceWeatherSecureProAPI();
        compositeDisposable = new CompositeDisposable();

        binding.SelectCattlefarmerID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectCattleFarmer();
            }
        });
        binding.SelectCattlefarmerID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectCattleFarmer();
            }
        });
        binding.llWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMoveWeather();
            }
        });
        binding.imageviewWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMoveWeather();
            }
        });
        binding.txtWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMoveWeather();
            }
        });
        binding.llCattleDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMoveCattleDoctors();
            }
        });
        binding.imageviewCattleDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMoveCattleDoctors();
            }
        });
        binding.txtCattleDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMoveCattleDoctors();
            }
        });
        binding.llCattleAdvisory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMoveCattleAdvisory();
            }
        });
        binding.imageviewCattleAdvisory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMoveCattleAdvisory();
            }
        });
        binding.txtCattleAdvisory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMoveCattleAdvisory();
            }
        });
        binding.editFarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Contour != null && Contour.length() > 10) {
                    setCattleFarmDraw();
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.Thefarmernocontour), Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.llCattleFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setMoveCattleFood();
            }
        });
        binding.imageviewCattleFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setMoveCattleFood();
            }
        });
        binding.txtCattleFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setMoveCattleFood();
            }
        });

        binding.cardvieemergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setEmergencyCall();
            }
        });
        binding.logoutLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accountAlert();
            }
        });

//        selectCattleFarmer(1);
//        SelectCattleFarmerID = "189";
//        if (SelectCattleFarmerID != null && SelectCattleFarmerID.length() > 0) {
//            new CattledetailsAsynctask().execute();
//        }
        //ScreenTracking
        UID = getUIDforScreenTracking();
        setScreenTracking(CattleDashboards.this, db, SN_CattleDashbaords, UID);

//        getOwnerCattleList();
        checkCattleDashboardLogin();
    }

    private void accountAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.Dear) + " " + AppConstant.visible_Name + " \n \n" + getResources().getString(R.string.areusurelogout));
        builder.setPositiveButton(getResources().getString(R.string.Logout), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (prefs == null) {
                    prefs = getSharedPreferences(AppConstant.SHARED_PREFRENCE_NAME, MODE_PRIVATE);
                }
                Utility.setLogout(CattleDashboards.this, prefs);

                Intent in = new Intent(getApplicationContext(), NewHomeScreen.class);
                in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(in);
                finish();

            }
        });
        builder.show();
    }

    public void setMoveWeather() {
        Intent in = new Intent(CattleDashboards.this, CattleWeather.class);
        in.putExtra("lat", selectedLatitude);
        in.putExtra("long", selectedLongitude);
        startActivity(in);
    }

    public void setMoveCattleDoctors() {
        Intent in = new Intent(CattleDashboards.this, CattleDoctors.class);
        in.putExtra("lat", selectedLatitude);
        in.putExtra("long", selectedLongitude);
        startActivity(in);
    }

    public void setMoveCattleAdvisory() {
        Intent in = new Intent(CattleDashboards.this, CattleAdvisory.class);
        in.putExtra("lat", selectedLatitude);
        in.putExtra("long", selectedLongitude);
        startActivity(in);
    }

    public void setCattleFarmDraw() {
        Intent in = new Intent(CattleDashboards.this, CattleFarmDraw.class);
        in.putExtra("CattleName", SelectCattleFarmerName);
        in.putExtra("contour", Contour);
        in.putExtra("lat", selectedLatitude);
        in.putExtra("long", selectedLongitude);
        startActivity(in);
    }

    public void setEmergencyCall() {
        if (CattleDash_SelectCattleFarmerID != null) {
            emergencyMethod();
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.Pleaseselectyour), Toast.LENGTH_SHORT).show();
        }
    }

    private void emergencyMethod() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.Success)).
                setMessage(getResources().getString(R.string.Youhavesuccessfully)).
                setPositiveButton(getResources().getString(R.string.Ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        discussPopupMethod();

                    }
                }).setNegativeButton(getResources().getString(R.string.Cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();


            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void discussPopupMethod() {

        final Dialog dialog = new Dialog(this, R.style.DialogSlideAnim);

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
        dialog.setContentView(R.layout.discuss_popup);


        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, 650);
        /*} else {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        }*/


        LinearLayout callPopup = (LinearLayout) dialog.findViewById(R.id.call_popup);
        LinearLayout chatPopup = (LinearLayout) dialog.findViewById(R.id.chat_popup);

        TextView hd = (TextView) dialog.findViewById(R.id.heading);
        TextView cl = (TextView) dialog.findViewById(R.id.cl_tv);
        TextView ch = (TextView) dialog.findViewById(R.id.sn_tv);


        callPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
//                callSmsMethod("8285686540"); //Vishal
//                callSmsMethod("07052023023");//Comment 2021-05-01
                callSmsMethod("07505022000");
            }
        });

        chatPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                setPlantDoctorCall();
            }
        });
        dialog.show();
    }

    public void setPlantDoctorCall() {
        if (CattleDash_SelectCattleFarmerID != null) {
//            Intent in = new Intent(MainProfileActivity.this, LiveCottonActivity.class);
//            in.putExtra("data", "dash");
//            startActivity(in);
            Intent in = new Intent(CattleDashboards.this, CattleDoctors.class);
            startActivity(in);
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.Pleaseselectyour), Toast.LENGTH_SHORT).show();
        }
    }

    public void callSmsMethod(final String number) {

        final Dialog dialog = new Dialog(this, R.style.DialogSlideAnim);

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
        dialog.setContentView(R.layout.call_sms_popup);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        RelativeLayout callBTN = (RelativeLayout) dialog.findViewById(R.id.call_btn);
        RelativeLayout smsBTN = (RelativeLayout) dialog.findViewById(R.id.sms_btn);

        callBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                try {
//                    boolean resultCam = Utility.checkPermissionCall(MainProfileActivity.this);
                    boolean resultCam = Utility.checkPermissionCallRecord(CattleDashboards.this);
                    if (resultCam) {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + number));
                        if (ActivityCompat.checkSelfPermission(CattleDashboards.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                            final Intent intent = new Intent(CattleDashboards.this, CallRecordingService.class);
//                            startService(intent);
                            startActivity(callIntent);
                        }
                    } else {
                        Utility.setPermissionsRecording(CattleDashboards.this);
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.Permissionigranted), Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.Permissionisdenied), Toast.LENGTH_SHORT).show();
                    }

                } catch (ActivityNotFoundException activityException) {
                    Log.e("Calling a Phone Number", "Call failed", activityException);
                }
            }
        });

        smsBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                //With Permission
//                boolean resultCam = Utility.checkPermissionSMS(MainProfileActivity.this);
//                if (resultCam) {
//                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + number));
//                    intent.putExtra("sms_body", "Type here");
//                    startActivity(intent);
//                }


//                String UserName = "Farmer Details: " + AppConstant.selected_farm;
                String FarmerID = AppConstant.farm_id;
                String Messages = FarmerID + "\n ";

                //Without Permission
                // Create the intent.
                Intent smsIntent = new Intent(Intent.ACTION_SENDTO);
                // Set the data for the intent as the phone number.
                smsIntent.setData(Uri.parse("smsto:" + number));
                // Add the message (sms) with the key ("sms_body").
                smsIntent.putExtra("sms_body", Messages);
                // If package resolves (target app installed), send intent.
                if (smsIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(smsIntent);
                } else {
                    //Log.e(TAG, "Can't resolve app for ACTION_SENDTO Intent");
                }
            }
        });
        dialog.show();


    }

    public void selectCattleFarmer() {
        try {
            final Dialog dialog = new Dialog(this, R.style.DialogSlideAnim);

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
            CattledashboardfarmerpopupBinding popbinding = CattledashboardfarmerpopupBinding.inflate(LayoutInflater.from(this));
            dialog.setContentView(popbinding.getRoot());

            window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);

            UtilFonts.UtilFontsInitialize(this);
            popbinding.title.setTypeface(UtilFonts.FS_Ultra);
            popbinding.txtPleaseselectyour.setTypeface(UtilFonts.KT_Light);

            ArrayAdapter<CattleObject> cattletype = new ArrayAdapter<CattleObject>(CattleDashboards.this, R.layout.spinner_layout, CattleFarmerList);
            popbinding.spinselectcattlefarmer.setAdapter(cattletype);
            setCustomSearchableSpinner(getApplicationContext(),popbinding.spinselectcattlefarmer,"SelectCattle", R.string.SelectCattle);
//            popbinding.spinselectcattlefarmer.setTitle(getDynamicLanguageValue(getApplicationContext(), "SelectCattle", R.string.SelectCattle));

            popbinding.spinselectcattlefarmer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                    if (position > 0 && CattleFarmerList.size() > 0) {
                        CattleObject obj = (CattleObject) adapterView.getSelectedItem();
                        CattleDash_SelectCattleFarmerID = obj.getId();
                        SelectCattleFarmerName = obj.getName();

                        try {
                            JSONObject obj1 = OwnerCattlenameListarray.getJSONObject(position - 1);
//                            String CattleIDs = obj1.getString("CattleID");
//                            String CattleShedName = obj1.getString("CattleShedName");
//                            String FarmerName = obj1.getString("FarmerName");
                            selectedLatitude = obj1.getString("Lat");
                            selectedLongitude = obj1.getString("Lon");
                            Contour = obj1.getString("Contour");
                            SelectCattleBreed = obj1.getString("Breed1");
                            SelectCattleType = obj1.getString("Lifestage");
                            CattleFarmerName = obj1.getString("FarmerName");
                            if (CattleDash_SelectCattleFarmerID != null && CattleDash_SelectCattleFarmerID.length() > 0) {
                                new CattledetailsAsynctask().execute();
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        if (CattleDash_SelectCattleFarmerID == "0") {
                            binding.cardviewPOP.setVisibility(View.GONE);
                            CattleDash_SelectCattleFarmerID = "0";
                            SelectCattleFarmerName = "";
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            popbinding.btnCross.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private class CattledetailsAsynctask extends AsyncTask<Void, Void, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(CattleDashboards.this);
            progressDialog.setMessage(getResources().getString(R.string.Dataisloading));
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(true);
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    // cancel AsyncTask
                    cancel(false);
                }
            });
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            String sendRequest = null;
            try {
//                sendRequest = AppManager.getInstance().GetCattleDetails(DasBreedID);
                sendRequest = AppManager.getInstance().GetCattleDetails(CattleDash_SelectCattleFarmerID);
                String response = AppManager.getInstance().httpRequestGetMethod(sendRequest);
                response = AppManager.getInstance().RemoveStringUnwanted(response);
                return response;

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return sendRequest;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPreExecute();
            try {
                if (response != null && response.contains("ID")) {
                    if (CattleDash_SelectCattleFarmerID != null && CattleDash_SelectCattleFarmerID.length() > 0 && response != null && response.length() > 0) {
                        progressDialog.dismiss();
                        binding.cardviewPOP.setVisibility(View.VISIBLE);
                        CattleoFarmDetailsShow(response);
                    }
                }


            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


    //Farm details show
    public void CattleoFarmDetailsShow(String response) {

        try {
            binding.txtProfileCrop.setText(SelectCattleType + "_" + SelectCattleBreed);
            binding.txtProfilename.setText(CattleFarmerName);
            binding.txtFarmerselected.setText(SelectCattleFarmerName);

            JSONArray jsonArray = new JSONArray(response);

            JSONObject obj = jsonArray.getJSONObject(0);
            JSONArray arry1 = obj.getJSONArray("lstCattleUpdate");
            JSONObject objnew = arry1.getJSONObject(0);

            JSONArray DTBreed = obj.getJSONArray("DTBreed");
            try {
                for (int i = 0; i < DTBreed.length(); i++) {
                    JSONObject obj1 = DTBreed.getJSONObject(i);
                    jsonArrayBreed.add(obj1);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            JSONArray DTLifeStage = obj.getJSONArray("DTLifeStage");
            try {
                if (jsonArrayLifeStage == null || jsonArrayLifeStage.size() == 0) {
                    for (int i = 0; i < DTLifeStage.length(); i++) {
                        JSONObject obj1 = DTLifeStage.getJSONObject(i);
                        jsonArrayLifeStage.add(obj1);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            try {
                String CattleName = getStringCheck(obj.getString("CattleName"));
                binding.txtCattleNames.setText(CattleName);
            } catch (Exception ex) {
                binding.tblrwCattleName.setVisibility(View.VISIBLE);
                ex.printStackTrace();
            }
            String MainCattleID = getStringCheck(obj.getString("MainCattleID"));
            if (MainCattleID.equalsIgnoreCase("1")) {
                binding.txtCattleTypes.setText(getResources().getString(R.string.Cow));
                binding.tblrwCattleType.setVisibility(View.VISIBLE);
            } else if (MainCattleID.equalsIgnoreCase("2")) {
                binding.txtCattleTypes.setText(getResources().getString(R.string.Buffalo));
                binding.tblrwCattleType.setVisibility(View.VISIBLE);
            } else {
                binding.tblrwCattleType.setVisibility(View.GONE);
            }

            String Gender = getStringCheck(obj.getString("Gender"));

            if (Gender.equalsIgnoreCase("1")) {
                binding.txtGenders.setText(getResources().getString(R.string.Male));
                binding.tblrwGender.setVisibility(View.VISIBLE);
            } else if (Gender.equalsIgnoreCase("2")) {
                binding.txtGenders.setText(getResources().getString(R.string.Female));
                binding.tblrwGender.setVisibility(View.VISIBLE);
            } else {
                binding.tblrwGender.setVisibility(View.GONE);
            }
            try {
                String Ages = getStringCheck(obj.getString("strAge"));
                Ages = getDateReplace(Ages);
                if (Ages != null) {
                    binding.txtAges.setText(Ages);
                    binding.tblrwAge.setVisibility(View.VISIBLE);
                }
            } catch (Exception ex) {
                binding.tblrwGender.setVisibility(View.GONE);
                ex.printStackTrace();
            }
            try {//LifeStage
                String LifestageIDs = getStringCheck(objnew.getString("LifestageID"));
                for (int i = 0; i < jsonArrayLifeStage.size(); i++) {
                    JSONObject obj1 = jsonArrayLifeStage.get(i);
                    String LifeStageid = obj1.getString("ID");
                    String LifeStagename = obj1.getString("Name");
                    SelectedLifeStageID = LifeStageid;
                    SelectedLifeStageName = LifeStagename;
                    if (LifestageIDs.equalsIgnoreCase(LifeStageid)) {
                        binding.txtLifeStages.setText(LifeStagename);
//                        DasLifeStageName = LifeStagename;
                        binding.tblrwLifeStage.setVisibility(View.VISIBLE);
                        break;
                    } else {
                        binding.tblrwLifeStage.setVisibility(View.GONE);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }


            try {//Breed
                String Breeds = getStringCheck(obj.getString("Breed"));
                for (int i = 0; i < jsonArrayBreed.size(); i++) {
                    JSONObject obj1 = jsonArrayBreed.get(i);
                    String breedid = obj1.getString("ID");
                    String breedname = obj1.getString("Name");
                    if (Breeds.equalsIgnoreCase(breedid)) {
                        binding.txtBreeds.setText(breedname);
                        binding.tblrwBreed.setVisibility(View.VISIBLE);
                        break;
                    } else {
                        binding.tblrwBreed.setVisibility(View.GONE);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            try {//BodyWeight
                String BodyWeight = getStringCheck(objnew.getString("BodyWeight"));
                binding.txtBodyWeights.setText(BodyWeight);
                binding.tblrwBodyWeight.setVisibility(View.VISIBLE);
            } catch (Exception ex) {
                ex.printStackTrace();
                binding.tblrwBodyWeight.setVisibility(View.GONE);
            }
            try {//HeatDate
                String HeatDate = getStringCheck(objnew.getString("HeatDate"));
                HeatDate = getDateReplace(HeatDate);
                if (HeatDate != null) {
                    binding.txtHeatDates.setText(HeatDate);
                    binding.tblrwHeatDate.setVisibility(View.VISIBLE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                binding.tblrwHeatDate.setVisibility(View.GONE);
            }
            try {//Vaccinations
                String Vaccinations = getStringCheck(objnew.getString("Vaccinations"));
                binding.txtVaccinations.setText(Vaccinations);
                binding.tblrwVaccination.setVisibility(View.VISIBLE);
            } catch (Exception ex) {
                ex.printStackTrace();
                binding.tblrwVaccination.setVisibility(View.GONE);
            }

            try {//Deworming
                String lstWormControl = getStringCheck(obj.getString("lstWormControl"));
                JSONArray ary = new JSONArray(lstWormControl);
                JSONObject obj1 = ary.getJSONObject(0);
                String Str = getStringCheck(obj1.getString("Str"));
                String dt = getStringCheck(obj1.getString("dt"));
                dt = getDateReplace(dt);
                if (Str.equalsIgnoreCase("1")) {
                    binding.txtDewormings.setText(getResources().getString(R.string.Yes));
                    binding.tblrwDeworming.setVisibility(View.VISIBLE);
                } else if (Str.equalsIgnoreCase("2")) {
                    binding.txtDewormings.setText(getResources().getString(R.string.No));
                    binding.tblrwDeworming.setVisibility(View.VISIBLE);
                } else {
                    binding.tblrwDeworming.setVisibility(View.GONE);
                }
                if (dt != null) {
                    binding.txtDewormingDates.setText(dt);
                    binding.tblrwDewormingDate.setVisibility(View.VISIBLE);
                } else {
                    binding.tblrwDewormingDate.setVisibility(View.GONE);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                binding.tblrwDeworming.setVisibility(View.GONE);
                binding.tblrwDewormingDate.setVisibility(View.GONE);
            }
            try {
                String lstVaccinations = getStringCheck(obj.getString("lstVaccinations"));
                String[] VaccinationNameID = {"1", "2", "3", "4", "5", "6", "7", "8", "9"};
                String[] VaccinationName = {getstrVal(R.string.FMD), getstrVal(R.string.Haemorrhagic), getstrVal(R.string.Septicaemia),
                        getstrVal(R.string.BlackQuarter), getstrVal(R.string.Brucellosis), getstrVal(R.string.Theileriosis),
                        getstrVal(R.string.Anthrax), getstrVal(R.string.IBR), getstrVal(R.string.Others)};
                JSONArray ary = new JSONArray(lstVaccinations);
                JSONArray aryadapterVac = new JSONArray();
                for (int i = 0; i < ary.length(); i++) {
                    JSONObject obj1 = ary.getJSONObject(i);
                    String Str = getStringCheck(obj1.getString("Str"));
                    String dt = getStringCheck(obj1.getString("dt"));
                    dt = getDateReplace(dt);
                    int position = Integer.parseInt(Str);
                    JSONObject val = new JSONObject();
                    val.put(VaccinationName[position - 1], dt);
                    aryadapterVac.put(val);
                }
                if (aryadapterVac.length() > 0) {
                    binding.llVaccination.setVisibility(View.VISIBLE);
                    LinearLayoutManager llm = new LinearLayoutManager(this);
                    llm.setOrientation(LinearLayoutManager.VERTICAL);
                    binding.recycleviewVaccination.setLayoutManager(llm);
                    VacDisDetailsAdapter adapter = new VacDisDetailsAdapter(getApplicationContext(), aryadapterVac, "VaccinationList");
                    binding.recycleviewVaccination.setAdapter(adapter);
                } else {
                    binding.llVaccination.setVisibility(View.GONE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                binding.llVaccination.setVisibility(View.GONE);
            }

            try {
                String lstDiseases = getStringCheck(obj.getString("lstDiseases"));
                String[] DiseaseNameID = {"1", "2", "3", "4", "5", "6", "7", "8", "9"};
                String[] DiseaseName = {getstrVal(R.string.Matitis), getstrVal(R.string.FMD), getstrVal(R.string.Septicaemia),
                        getstrVal(R.string.BlackQuarter), getstrVal(R.string.TB), getstrVal(R.string.DYSTOKIA),
                        getstrVal(R.string.PROLAPSEOFUTERUS), getstrVal(R.string.RETAINEDPLACENTA), getstrVal(R.string.Others)};
                JSONArray ary = new JSONArray(lstDiseases);
                JSONArray aryadapterVac = new JSONArray();
                for (int i = 0; i < ary.length(); i++) {
                    JSONObject obj1 = ary.getJSONObject(i);
                    String Str = getStringCheck(obj1.getString("Str"));
                    String dt = getStringCheck(obj1.getString("dt"));
                    dt = getDateReplace(dt);
                    int position = Integer.parseInt(Str);
                    JSONObject val = new JSONObject();
                    val.put(DiseaseName[position - 1], dt);
                    aryadapterVac.put(val);
                }
                if (aryadapterVac.length() > 0) {
                    binding.llDiseases.setVisibility(View.VISIBLE);
                    LinearLayoutManager llm = new LinearLayoutManager(this);
                    llm.setOrientation(LinearLayoutManager.VERTICAL);
                    binding.recycleviewDisease.setLayoutManager(llm);
                    VacDisDetailsAdapter adapter = new VacDisDetailsAdapter(getApplicationContext(), aryadapterVac, "VaccinationList");
                    binding.recycleviewDisease.setAdapter(adapter);
                } else {
                    binding.llDiseases.setVisibility(View.GONE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                binding.llDiseases.setVisibility(View.GONE);
            }

            try {
                String SimpleFeed = getStringCheck(objnew.getString("SimpleFeed"));
                String CompoundFeed = getStringCheck(objnew.getString("CompoundFeed"));
                //SimpleFeed
                String[] SimpleFeedNameID = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
                String[] SimpleFeedNameIDName = {getstrVal(R.string.WheatBran), getstrVal(R.string.MusterdCacke), getstrVal(R.string.CottonCake),
                        getstrVal(R.string.GramByProduct), getstrVal(R.string.GramHusk), getstrVal(R.string.SoyabeenHusk),
                        getstrVal(R.string.CottonSeed), getstrVal(R.string.MaizeProduct), getstrVal(R.string.WheatDaliya), getstrVal(R.string.Others)};
                JSONArray ary_simplefeed = new JSONArray(SimpleFeed);
                JSONArray aryadaptersimple = new JSONArray();
                for (int i = 0; i < ary_simplefeed.length(); i++) {
                    String IDs = ary_simplefeed.getString(i);
                    String Names = SimpleFeedNameIDName[Integer.parseInt(IDs) - 1];
                    JSONObject val = new JSONObject();
                    val.put("ID", Names);
                    aryadaptersimple.put(val);
                }
                if (aryadaptersimple.length() > 0) {
                    binding.llSimpleFeed.setVisibility(View.VISIBLE);
                    LinearLayoutManager llm = new LinearLayoutManager(this);
                    llm.setOrientation(LinearLayoutManager.VERTICAL);
                    binding.recycleviewSimpleFeed.setLayoutManager(llm);
                    FeedDetailsAdapter adapter = new FeedDetailsAdapter(getApplicationContext(), aryadaptersimple, "SimpleFeed");
                    binding.recycleviewSimpleFeed.setAdapter(adapter);
                } else {
                    binding.llSimpleFeed.setVisibility(View.GONE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                binding.llSimpleFeed.setVisibility(View.GONE);
            }

            try {
                String GreenFodder = getStringCheck(objnew.getString("GreenFodder"));
                //GreenFodder
                String[] GreenFodderNameID = {"1", "2", "3", "4", "5"};
                String[] GreenFodderNameIDName = {getstrVal(R.string.Barseem), getstrVal(R.string.SudanGras),
                        getstrVal(R.string.Oat), getstrVal(R.string.Maize), getstrVal(R.string.Others)};
                JSONArray ary_greenfodder = new JSONArray(GreenFodder);
                JSONArray aryadaptergreenfodder = new JSONArray();
                for (int i = 0; i < ary_greenfodder.length(); i++) {
                    String IDs = ary_greenfodder.getString(i);
                    String Names = GreenFodderNameIDName[Integer.parseInt(IDs) - 1];
                    JSONObject val = new JSONObject();
                    val.put("ID", Names);
                    aryadaptergreenfodder.put(val);
                }
                if (aryadaptergreenfodder.length() > 0) {
                    binding.llGreenFodder.setVisibility(View.VISIBLE);
                    LinearLayoutManager llm = new LinearLayoutManager(this);
                    llm.setOrientation(LinearLayoutManager.VERTICAL);
                    binding.recycleviewGreenFodder.setLayoutManager(llm);
                    FeedDetailsAdapter adapter = new FeedDetailsAdapter(getApplicationContext(), aryadaptergreenfodder, "GreenFodder");
                    binding.recycleviewGreenFodder.setAdapter(adapter);
                } else {
                    binding.llGreenFodder.setVisibility(View.GONE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            try {
                String Feedingperday = getStringCheck(objnew.getString("FeedingPerDay"));
                if (Feedingperday.length() > 0 && !Feedingperday.equalsIgnoreCase("0")) {
                    binding.tblFeedingPerDay.setVisibility(View.VISIBLE);
                    binding.txtFeedingPerDays.setText(Feedingperday);
                } else {
                    binding.tblFeedingPerDay.setVisibility(View.GONE);
                    binding.txtFeedingPerDays.setText("");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            try {
                String Conceivedate = getStringCheck(objnew.getString("Conceivedate"));
                Conceivedate = getDateReplace(Conceivedate);
                if (Conceivedate != null) {
                    binding.tblConceivedate.setVisibility(View.VISIBLE);
                    binding.txtConceivedates.setText(Conceivedate);
                } else {
                    binding.tblConceivedate.setVisibility(View.GONE);
                    binding.txtConceivedates.setText("");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            try {
                String Calvingdate = getStringCheck(objnew.getString("Calvingdate"));
                Calvingdate = getDateReplace(Calvingdate);
                if (Calvingdate != null) {
                    binding.tblCalvingdate.setVisibility(View.VISIBLE);
                    binding.txtCalvingdates.setText(Calvingdate);
                } else {
                    binding.tblCalvingdate.setVisibility(View.GONE);
                    binding.txtCalvingdates.setText("");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            try {
                String Numberofcalving = getStringCheck(objnew.getString("Numberofcalving"));
                if (Numberofcalving.length() > 0 && !Numberofcalving.equalsIgnoreCase("0")) {
                    binding.tblNumberOfCalving.setVisibility(View.VISIBLE);
                    binding.txtNumberOfCalvings.setText(Numberofcalving);
                } else {
                    binding.tblNumberOfCalving.setVisibility(View.GONE);
                    binding.txtNumberOfCalvings.setText("");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            try {
                String Dryingoffdate = getStringCheck(objnew.getString("Dryingoffdate"));
                Dryingoffdate = getDateReplace(Dryingoffdate);
                if (Dryingoffdate != null) {
                    binding.tblDryingOffDate.setVisibility(View.VISIBLE);
                    binding.txtDryingOffDates.setText(Dryingoffdate);
                } else {
                    binding.tblDryingOffDate.setVisibility(View.GONE);
                    binding.txtDryingOffDates.setText("");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            try {//InseminationBreedName
                String Inseminationname = getStringCheck(objnew.getString("Inseminationname"));
                if (!Inseminationname.equalsIgnoreCase("0")) {
                    for (int i = 0; i < jsonArrayBreed.size(); i++) {
                        JSONObject obj1 = jsonArrayBreed.get(i);
                        String breedid = obj1.getString("ID");
                        String breedname = obj1.getString("Name");
                        if (Inseminationname.equalsIgnoreCase(breedid)) {
                            binding.tblInseminationBreedName.setVisibility(View.VISIBLE);
                            binding.txtInseminationBreedNames.setText(breedname);
                            break;
                        } else {
                            binding.tblInseminationBreedName.setVisibility(View.GONE);
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            try {
                String Pregnancytest = getStringCheck(objnew.getString("Pregnancytest"));
                if (Pregnancytest.equalsIgnoreCase("1")) {
                    binding.tblPregnancyTest.setVisibility(View.VISIBLE);
                    binding.txtPregnancyTest.setText(getResources().getString(R.string.Yes));
                } else if (Pregnancytest.equalsIgnoreCase("2")) {
                    binding.tblPregnancyTest.setVisibility(View.VISIBLE);
                    binding.txtPregnancyTests.setText(getResources().getString(R.string.No));
                } else {
                    binding.tblPregnancyTest.setVisibility(View.GONE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            try {
                String Pregnancytestdate = getStringCheck(objnew.getString("Pregnancytestdate"));
                Pregnancytestdate = getDateReplace(Pregnancytestdate);
                if (Pregnancytestdate != null) {
                    binding.tblPregnancyTestDate.setVisibility(View.VISIBLE);
                    binding.txtPregnancyTestDates.setText(Pregnancytestdate);
                } else {
                    binding.tblPregnancyTestDate.setVisibility(View.GONE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            try {
                String Pregnancystatus = getStringCheck(objnew.getString("Pregnancystatus"));
                if (Pregnancystatus.equalsIgnoreCase("1")) {
                    binding.tblPregnancyStatus.setVisibility(View.VISIBLE);
                    binding.txtPregnancyStatus.setText(getResources().getString(R.string.Pregnant));
                } else if (Pregnancystatus.equalsIgnoreCase("2")) {
                    binding.tblPregnancyStatus.setVisibility(View.VISIBLE);
                    binding.txtPregnancyStatus.setText(getResources().getString(R.string.NotPregnant));
                } else {
                    binding.tblPregnancyStatus.setVisibility(View.GONE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            try {
                String Pregnancystatus = getStringCheck(objnew.getString("Pregnancystatus"));
                if (Pregnancystatus.equalsIgnoreCase("1")) {
                    binding.tblPregnancyStatus.setVisibility(View.VISIBLE);
                    binding.txtPregnancyStatus.setText(getResources().getString(R.string.Pregnant));
                } else if (Pregnancystatus.equalsIgnoreCase("2")) {
                    binding.tblPregnancyStatus.setVisibility(View.VISIBLE);
                    binding.txtPregnancyStatus.setText(getResources().getString(R.string.NotPregnant));
                } else {
                    binding.tblPregnancyStatus.setVisibility(View.GONE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            try {
                String DiseaseHistory = getStringCheck(objnew.getString("DiseaseHistory"));
                if (DiseaseHistory.equalsIgnoreCase("1")) {
                    binding.tblDiseaseHistory.setVisibility(View.VISIBLE);
                    binding.txtDiseaseHistorys.setText(getResources().getString(R.string.Yes));
                } else if (DiseaseHistory.equalsIgnoreCase("2")) {
                    binding.tblDiseaseHistory.setVisibility(View.VISIBLE);
                    binding.txtDiseaseHistorys.setText(getResources().getString(R.string.No));
                } else {
                    binding.tblDiseaseHistory.setVisibility(View.GONE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            try {
                String Mothermilkyeild = getStringCheck(objnew.getString("MilkYield"));
                if (Mothermilkyeild.length() > 0 & !Mothermilkyeild.equalsIgnoreCase("0")) {
                    binding.tblMotherMilkYieldPerDay.setVisibility(View.VISIBLE);
                    binding.txtMotherMilkYieldPerDays.setText(Mothermilkyeild);
                } else {
                    binding.tblMotherMilkYieldPerDay.setVisibility(View.GONE);
                }
            } catch (Exception ex) {
                binding.tblMotherMilkYieldPerDay.setVisibility(View.GONE);
                ex.printStackTrace();
            }
            try {
                String Repeatheatissue = getStringCheck(objnew.getString("Repeatheatissue"));
                if (Repeatheatissue.equalsIgnoreCase("1")) {
                    binding.tblRepeatHeatIssue.setVisibility(View.VISIBLE);
                    binding.txtRepeatHeatIssues.setText(getResources().getString(R.string.Yes));
                } else if (Repeatheatissue.equalsIgnoreCase("2")) {
                    binding.tblRepeatHeatIssue.setVisibility(View.VISIBLE);
                    binding.txtRepeatHeatIssues.setText(getResources().getString(R.string.No));
                } else {
                    binding.tblRepeatHeatIssue.setVisibility(View.GONE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            try {
                String Enfertilityissue = getStringCheck(objnew.getString("Enfertilityissue"));
                if (Enfertilityissue.equalsIgnoreCase("1")) {
                    binding.tblEnfertilityIssue.setVisibility(View.VISIBLE);
                    binding.txtEnfertilityIssues.setText(getResources().getString(R.string.Yes));
                } else if (Enfertilityissue.equalsIgnoreCase("2")) {
                    binding.tblEnfertilityIssue.setVisibility(View.VISIBLE);
                    binding.txtEnfertilityIssues.setText(getResources().getString(R.string.No));
                } else {
                    binding.tblEnfertilityIssue.setVisibility(View.GONE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            try {
                String MastitisIssue = getStringCheck(objnew.getString("MastitisIssue"));
                if (MastitisIssue.equalsIgnoreCase("1")) {
                    binding.tblMastitisIssue.setVisibility(View.VISIBLE);
                    binding.txtMastitisIssues.setText(getResources().getString(R.string.Yes));
                } else if (MastitisIssue.equalsIgnoreCase("2")) {
                    binding.tblMastitisIssue.setVisibility(View.VISIBLE);
                    binding.txtMastitisIssues.setText(getResources().getString(R.string.No));
                } else {
                    binding.tblMastitisIssue.setVisibility(View.GONE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            //Completed Pregnant Heifer


        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }

    @Override
    public void onPause() {
        super.onPause();
        if (db != null) {
            db = new DBAdapter(this);
        }
        Log.d("OnPause Method", "OnPause Method called");
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (db != null) {
            db = new DBAdapter(this);
        }
        Log.d("onStop Method", "onStop Method called");
        setScreenTracking(this, db, SN_CattleDashbaords, UID);
    }

    @Override
    public void onBackPressed() {
        // code here to show dialog
        super.onBackPressed();  // optional depending on your needs
        Log.d("onBackPressed Method", "onBackPressed Method called");

//        Intent in = new Intent(CattleDashboards.this, AdminDashboard_New.class);
//        startActivity(in);
        finish();
    }

    public String getstrVal(int id) {
        String value = "";
        try {
            value = getResources().getString(id);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return value;
    }

    public void setFonts() {
        UtilFonts.UtilFontsInitialize(CattleDashboards.this);
        binding.txtProfileCrop.setTypeface(UtilFonts.KT_Bold);
        binding.txtProfilename.setTypeface(UtilFonts.KT_Bold);
        binding.txtFarmerselected.setTypeface(UtilFonts.KT_Bold);
        binding.txtRegistrationDetails.setTypeface(UtilFonts.KT_Bold);
        binding.txtWeather.setTypeface(UtilFonts.KT_Medium);
        binding.txtCattleDoctor.setTypeface(UtilFonts.KT_Medium);
        binding.txtCattleAdvisory.setTypeface(UtilFonts.KT_Medium);
    }


    private void getOwnerCattleList() {

        final TransparentProgressDialog dialoug = new TransparentProgressDialog(
                CattleDashboards.this, getResources().getString(
                R.string.Dataisloading));
        dialoug.show();
//        String URL = AppManager.getInstance().getOwnerCattlelist(AppConstant.user_id);
        String URL = AppManager.getInstance().getOwnerCattlelist("123456");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialoug.cancel();
                        try {
//                            GsonBuilder builder = new GsonBuilder();
//                            Gson gson = builder.create();
//                            String str = gson.fromJson(response.toString(), String.class);
                            OwnerCattleList = response;

                            OwnerCattlenameListarray = new JSONArray(OwnerCattleList);

                            CattleFarmerList = new ArrayList<>();
                            CattleFarmerList.add(new CattleObject("0", getDynamicLanguageValue(getApplicationContext(), "Select", R.string.Select)));
//                            CattleFarmerList.add(new CattleObject("0", getResources().getString(R.string.SelectCattle)));
                            if (OwnerCattlenameListarray.length() > 0) {
                                for (int i = 0; i < OwnerCattlenameListarray.length(); i++) {
                                    JSONObject obj = OwnerCattlenameListarray.getJSONObject(i);
                                    String CattleIDs = obj.getString("CattleID");
                                    String CattleNames = obj.getString("CattleName");
                                    String FarmerName = obj.getString("FarmerName");
                                    selectedLatitude = obj.getString("Lat");
                                    selectedLongitude = obj.getString("Lon");
                                    Contour = obj.getString("Contour");
                                    SelectCattleBreed = obj.getString("Breed1");
                                    SelectCattleType = obj.getString("CattleType");
                                    CattleFarmerName = obj.getString("FarmerName");
                                    CattleFarmerList.add(new CattleObject(CattleIDs, CattleIDs + "_" + FarmerName + "_" + CattleNames));
                                }
                            }
                            if (CattleFarmerList.size() > 1) {
                                CattleObject obj = (CattleObject) CattleFarmerList.get(1);
                                CattleDash_SelectCattleFarmerID = obj.getId();
                                SelectCattleFarmerName = obj.getName();
                                if (CattleDash_SelectCattleFarmerID != null && CattleDash_SelectCattleFarmerID.length() > 0) {
                                    new CattledetailsAsynctask().execute();
                                }
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialoug.cancel();
                System.out.println("Volley Error : " + error);
            }
        });

        int socketTimeout = 60000;//60 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);

        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    public void checkCattleDashboardLogin() {
        dialog = new TransparentProgressDialog(
                CattleDashboards.this, getResources().getString(R.string.LoginCattleDashboardData));
        dialog.setCancelable(false);
        String strTokennew = "345678", srtOwnerIDList = "";
        if (CattleDash_OwnerIDList.size() > 0) {
            for (int i = 0; i < CattleDash_OwnerIDList.size(); i++) {
                if (i == 0) {
                    srtOwnerIDList = String.valueOf(CattleDash_OwnerIDList.get(i).getID());
                } else {
                    srtOwnerIDList = srtOwnerIDList + "," + String.valueOf(CattleDash_OwnerIDList.get(i).getID());
                }
            }
        }
        String requestString = getOwnerIDLiset(strTokennew, srtOwnerIDList);

        mRequestStartTime = System.currentTimeMillis();
        apiService.getCattleID(strTokennew, srtOwnerIDList).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResponseObserver<retrofit2.Response<List<CattleDashboardOwnerListResponse>>>(compositeDisposable) {
                    @Override
                    public void onNetworkError(Throwable e) {
                        // calculate the duration in milliseconds
                        int seconds = getAPIimeResponseinSecond(mRequestStartTime);
                        dialog.cancel();
                        Log.v("wvqevqevqe", e.getMessage() + " " + e.toString());
                        SaveLocalFile(db, CattleDashboards.this, SN_CattleDashbaords, requestString, "", getSMS(e.getMessage()), "" + seconds, AppConstant.farm_id, "Error");
                    }

                    @Override
                    public void onServerError(Throwable e, int code) { // calculate the duration in milliseconds
                        int seconds = getAPIimeResponseinSecond(mRequestStartTime);
                        dialog.cancel();
                        SaveLocalFile(db, CattleDashboards.this, SN_CattleDashbaords, requestString, "", "Server API Error", "" + seconds, AppConstant.farm_id, "Error");
                    }

                    @Override
                    public void onNext(retrofit2.Response<List<CattleDashboardOwnerListResponse>> Response) {
                        // calculate the duration in milliseconds
                        int seconds = getAPIimeResponseinSecond(mRequestStartTime);
                        dialog.cancel();
                        List<CattleDashboardOwnerListResponse> Jsonstring = Response.body();

                        Gson gson = new Gson();
                        String response = gson.toJson(Jsonstring);
                        Log.v("ABC Log", response.toString());


                        if (seconds > 3) {
                            SaveLocalFile(db, CattleDashboards.this, SN_CattleDashbaords, requestString, response, "", "" + seconds, AppConstant.farm_id, "Working");
                        }
                        try {
//                            GsonBuilder builder = new GsonBuilder();
//                            Gson gson = builder.create();
//                            String str = gson.fromJson(response.toString(), String.class);
                            OwnerCattleList = response;

                            OwnerCattlenameListarray = new JSONArray(OwnerCattleList);

                            CattleFarmerList = new ArrayList<>();
                            CattleFarmerList.add(new CattleObject("0", getResources().getString(R.string.SelectCattle)));
                            if (OwnerCattlenameListarray.length() > 0) {
                                for (int i = 0; i < OwnerCattlenameListarray.length(); i++) {
                                    JSONObject obj = OwnerCattlenameListarray.getJSONObject(i);
                                    String CattleIDs = obj.getString("CattleID");
                                    String CattleNames = obj.getString("CattleName");
                                    String FarmerName = obj.getString("FarmerName");
                                    selectedLatitude = obj.getString("Lat");
                                    selectedLongitude = obj.getString("Lon");
                                    Contour = obj.getString("Contour");
                                    SelectCattleBreed = obj.getString("Breed1");
                                    SelectCattleType = obj.getString("CattleType");
                                    CattleFarmerName = obj.getString("FarmerName");
                                    CattleFarmerList.add(new CattleObject(CattleIDs, CattleIDs + "_" + FarmerName + "_" + CattleNames));
                                }
                            }
                            if (CattleFarmerList.size() > 1) {
                                CattleObject obj = (CattleObject) CattleFarmerList.get(1);
                                CattleDash_SelectCattleFarmerID = obj.getId();
                                SelectCattleFarmerName = obj.getName();
                                if (CattleDash_SelectCattleFarmerID != null && CattleDash_SelectCattleFarmerID.length() > 0) {
                                    new CattledetailsAsynctask().execute();
                                }
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }

                });
    }

    public void setMoveCattleFood() {
        Intent in = new Intent(CattleDashboards.this, CattleFood.class);
        in.putExtra("lat", selectedLatitude);
        in.putExtra("long", selectedLongitude);
        startActivity(in);
    }

}
