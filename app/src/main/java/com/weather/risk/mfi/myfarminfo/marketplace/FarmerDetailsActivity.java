package com.weather.risk.mfi.myfarminfo.marketplace;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.database.DBAdapter;
import com.weather.risk.mfi.myfarminfo.home.AppController;
import com.weather.risk.mfi.myfarminfo.mapfragments.LatLonCellID;
import com.weather.risk.mfi.myfarminfo.retofitservices.ApiService;
import com.weather.risk.mfi.myfarminfo.retofitservices.ResponseObserver;
import com.weather.risk.mfi.myfarminfo.retrofitmodelnew.FarmerDetailsResponse;
import com.weather.risk.mfi.myfarminfo.retrofitmodelnew.FarmerListRequest;
import com.weather.risk.mfi.myfarminfo.utils.AppConstant;
import com.weather.risk.mfi.myfarminfo.utils.AppManager;
import com.weather.risk.mfi.myfarminfo.utils.TransparentProgressDialog;
import com.weather.risk.mfi.myfarminfo.utils.UtilFonts;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.SN_FarmRegistration;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getUIDforScreenTracking;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setScreenTracking;

public class FarmerDetailsActivity extends AppCompatActivity {


    public static String ProjectID = null, ProjectName = null;


    ImageView project_refresh;

    ArrayList<HashMap<String, String>> Projects = new ArrayList<>();

    Button productButton, nextButton;


    String UID = "";


    TextView txt_FarmRegistration, txt_Project, txt_AadharNo, txt_FarmerName, txt_FatherName,
            txt_FarmerPhoneNumber, txt_BlockName, txt_DistrictName, txt_VillageName,
            txt_AccountNumber, farmerImage, passbookImage;

    TextView edtaadhar_no, edtFarmerName, edit_FatherName, editFarmerPhoneNumber,
            edit_AccountNumber, spin_block, spin_district, spin_villageName;

    Spinner spin_project;

    private ApiService apiService;
    private CompositeDisposable compositeDisposable;
    FarmerDetailsResponse responsesData = null;

    DBAdapter db;

    String balanceAmount = null;
    String farmerImageUrl = null;
    String passbookImageUrl = null;

    TableRow tblrow_aadhar;

    LinearLayout infoLay;
    RecyclerView recyclerView;

    String farmId = null;
    String state_id = null;
    String district_id = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.farmer_detail_activity);
        compositeDisposable = new CompositeDisposable();
        apiService = AppController.getInstance().getApiService();
        setIdDefine();
        db = new DBAdapter(this);

        db.open();
        db.clearCartItem();
        db.close();

        setProject();
        project_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    new getStateProjectRefresh("Project", AppManager.getInstance().ProjectListURL(AppConstant.user_id)).execute();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        final String farmerId = getIntent().getStringExtra("farmerId");
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* if (checkValidation(1) == true) {
                    new setFarmRegistrationSave("Submit").execute();
                }*/
                if (farmerId != null && !farmerId.equalsIgnoreCase("null")) {
                    if (state_id != null && !state_id.equalsIgnoreCase("null")) {
                        //AppConstant.FarmRegistration_AadharNo = Aadhar;
                        AppConstant.FarmRegistration_ProjectID = ProjectID;
                        Intent intent = new Intent(FarmerDetailsActivity.this, AddFarmOnMap_New.class);
                        intent.putExtra("calling-activity", AppConstant.HomeActivity);
                        intent.putExtra("lat", String.valueOf(LatLonCellID.currentLat));
                        intent.putExtra("log", String.valueOf(LatLonCellID.currentLon));
                        intent.putExtra("balance", balanceAmount);
                        intent.putExtra("farmerId", farmerId);
                        intent.putExtra("project_id", ProjectID);
                        intent.putExtra("farm_id", farmId);
                        intent.putExtra("state_id", state_id);
                        //intent.putExtra("hashMapValue", hashMap);
                        startActivity(intent);

                 /*  Intent in = new Intent(getApplicationContext(), ProductActivity.class);
                   in.putExtra("balance", balanceAmount);
                   in.putExtra("farmerId", farmerId);
                   in.putExtra("project_id", ProjectID);
                   startActivity(in);*/
                    } else {
                        Toast.makeText(getApplicationContext(), "State Id not found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Farmer Id not found", Toast.LENGTH_SHORT).show();
                }


            }
        });

      /*  editButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(getApplicationContext(), FarmRegistration.class);
                startActivity(in);
                finish();

            }
        });*/
        productButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (farmerId != null && !farmerId.equalsIgnoreCase("null")) {
                    if (state_id != null && !state_id.equalsIgnoreCase("null")) {

                     /*   Intent in = new Intent(getApplicationContext(), ProductActivity.class);
                        in.putExtra("balance", balanceAmount);
                        in.putExtra("farmerId", farmerId);
                        in.putExtra("project_id", ProjectID);
                        in.putExtra("farm_id",farmId);
                      //  in.putExtra("crop_id",crop)
                        in.putExtra("from","from");
                        startActivity(in);*/
                        Intent in = new Intent(getApplicationContext(), NewProductActivity.class);
                        in.putExtra("farmerId", farmerId);
                        in.putExtra("state_id", state_id);
                        in.putExtra("district_id", district_id);
                        startActivity(in);

                    } else {
                        Toast.makeText(getApplicationContext(), "State Id not found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Farmer Id not found", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //ScreenTracking
        UID = getUIDforScreenTracking();
        setScreenTracking(this, db, SN_FarmRegistration, UID);
        if (farmerId != null) {
            getFarmerDetailsMethod(farmerId);
        }
        farmerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /* if (Utility.checkPermissionGallery(FarmerDetailsActivity.this)){
                   if (farmerImageUrl!=null && farmerImageUrl.length()>10){
                       String ext = ".png";
                       if (farmerImageUrl.contains(".jpeg")){
                           ext = ".jpeg";
                       }else  if (farmerImageUrl.contains(".jpg")){
                           ext = ".jpg";
                       }else  if (farmerImageUrl.contains(".png")){
                           ext = ".png";
                       }else  if (farmerImageUrl.contains(".pdf")){
                           ext = ".pdf";
                       }else  if (farmerImageUrl.contains(".xlsx")){
                           ext = ".xlsx";
                       }

                       Long time = System.currentTimeMillis();
                       FileDownloaderTask downloader = FileDownloaderTask.getInstance(FarmerDetailsActivity.this, farmerImageUrl, "farmer_image_" + time + ext);
                       downloader.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, farmerImageUrl);
                       downloader.setListeners(new OnDownloadListeners() {
                           @Override
                           public void onDownloadSuccess(String filename) {
                               if (filename != null && filename.length() > 0) {
                                   Toast.makeText(getApplicationContext(), "Download Successfully", Toast.LENGTH_SHORT).show();
                                   Utility.downloadResponseMethod(filename, FarmerDetailsActivity.this);
                               }
                           }
                       });

                   }
               }*/
                imagePopup("https://ndviimages.s3.ap-south-1.amazonaws.com/SchedulerImages/Images/" + farmerImageUrl);

            }
        });
        passbookImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* if (Utility.checkPermissionGallery(FarmerDetailsActivity.this)){
                    if (passbookImageUrl!=null && passbookImageUrl.length()>10){
                        String ext = ".png";
                        if (passbookImageUrl.contains(".jpeg")){
                            ext = ".jpeg";
                        }else  if (passbookImageUrl.contains(".jpg")){
                            ext = ".jpg";
                        }else  if (passbookImageUrl.contains(".png")){
                            ext = ".png";
                        }else  if (passbookImageUrl.contains(".pdf")){
                            ext = ".pdf";
                        }else  if (passbookImageUrl.contains(".xlsx")){
                            ext = ".xlsx";
                        }

                        Long time = System.currentTimeMillis();
                        FileDownloaderTask downloader = FileDownloaderTask.getInstance(FarmerDetailsActivity.this, passbookImageUrl, "farmer_image_" + time + ext);
                        downloader.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, passbookImageUrl);
                        downloader.setListeners(new OnDownloadListeners() {
                            @Override
                            public void onDownloadSuccess(String filename) {
                                if (filename != null && filename.length() > 0) {
                                    Toast.makeText(getApplicationContext(), "Download Successfully", Toast.LENGTH_SHORT).show();
                                    Utility.downloadResponseMethod(filename, FarmerDetailsActivity.this);
                                }
                            }
                        });

                    }
                }*/
                imagePopup("https://ndviimages.s3.ap-south-1.amazonaws.com/SchedulerImages/Images/" + passbookImageUrl);

            }
        });
    }


    public void setProject() {
        db.open();
        Projects = new ArrayList<>();
        ArrayList<HashMap<String, String>> hasmap = new ArrayList<>();
        hasmap = db.getDynamicTableValue("Select * from " + db.TABLE_Projectlist + " where ProjectName !='' and  ProjectName !='null' order by ProjectName");
        ArrayList<String> list = new ArrayList<>();
        HashMap<String, String> hash = new HashMap<>();
        hash.put("ProjectID", "0");
        hash.put("ProjectName", "Select Project");
        Projects.add(hash);
        list.add("Select Project");
        if (hasmap.size() > 0) {
            for (int i = 1; i <= hasmap.size(); i++) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("ProjectID", hasmap.get(i - 1).get("ProjectID"));
                list.add(hasmap.get(i - 1).get("ProjectName"));
                hashMap.put("ProjectName", hasmap.get(i - 1).get("ProjectName"));
                Projects.add(hashMap);
            }
        }
        ArrayAdapter<String> stateListAdapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, list);
        spin_project.setAdapter(stateListAdapter);
        spin_project.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position > 0 && Projects.size() > 0) {
                    ProjectID = Projects.get(position).get("ProjectID");
                    ProjectName = Projects.get(position).get("ProjectName");
                } else {
                    ProjectID = null;
                    ProjectName = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        db.close();
    }


    private class getStateProjectRefresh extends AsyncTask<Void, Void, String> {

        String flag = "";
        String URL = "";
        TransparentProgressDialog progressDialog;

        public getStateProjectRefresh(String flags, String URLs) {
            flag = flags;
            URL = URLs;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            String sms = "";
            switch (flag) {
                case "Project"://State
                    sms = getResources().getString(R.string.LoadingProject);
                    break;
                case "StateDistrict"://District
                    sms = getResources().getString(R.string.Loadingstate);
                    break;
                case "Crops"://District
                    sms = getResources().getString(R.string.LoadingCrops);
                    break;

            }
            progressDialog = new TransparentProgressDialog(FarmerDetailsActivity.this, sms);
            progressDialog.setCancelable(false);
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
            try {
                String response = AppManager.getInstance().httpRequestGetMethod(URL);
                return "[" + response + "]";

            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(String response) {
            super.onPreExecute();
            if (response != null) {
                switch (flag) {
                    case "Project":
                        response = response.trim();
                        response = response.substring(1, response.length() - 1);
                        response = response.replace("\\", "");
                        db.open();
                        db.getSQLiteDatabase().execSQL("DROP TABLE IF EXISTS " + db.TABLE_Projectlist);
                        db.getSQLiteDatabase().execSQL(db.CREATE_TABLE_Projectlist);
                        io.requery.android.database.sqlite.SQLiteDatabase SqliteDB = db.getSQLiteDatabase();
                        SqliteDB.beginTransaction();
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject obj = new JSONObject(jsonArray.get(i).toString());
                                String query = "INSERT INTO " + DBAdapter.TABLE_Projectlist + "(ProjectID,ProjectName) VALUES ('" + obj.get("ID").toString() + "','" + obj.get("Name").toString() + "')";
                                db.getSQLiteDatabase().execSQL(query);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(FarmerDetailsActivity.this, getResources().getString(R.string.FormattingError), Toast.LENGTH_LONG).show();
                        }
                        SqliteDB.setTransactionSuccessful();
                        SqliteDB.endTransaction();
                        db.getClass();
                        setProject();
                        break;

                }

            } else
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.Nodataavailable), Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (db != null) {
            db = new DBAdapter(this);
        }
        Log.d("onResume Method", "onResume Method called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (db != null) {
            db = new DBAdapter(this);
        }
        Log.d("OnPause Method", "OnPause Method called");
        setScreenTracking(this, db, SN_FarmRegistration, UID);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (db != null) {
            db = new DBAdapter(this);
        }
        Log.d("onStop Method", "onStop Method called");
        setScreenTracking(this, db, SN_FarmRegistration, UID);
    }

    @Override
    public void onBackPressed() {
        // code here to show dialog
        super.onBackPressed();  // optional depending on your needs

    }


    private void setIdDefine() {
        infoLay = (LinearLayout) findViewById(R.id.info_lay);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        txt_FarmRegistration = (TextView) findViewById(R.id.txt_FarmRegistration);
        txt_Project = (TextView) findViewById(R.id.txt_Project);
        spin_project = (Spinner) findViewById(R.id.spin_project);
        txt_AadharNo = (TextView) findViewById(R.id.txt_AadharNo);
        edtaadhar_no = (TextView) findViewById(R.id.edtaadhar_no);
        tblrow_aadhar = (TableRow) findViewById(R.id.tblrow_aadhar);
        txt_FarmerName = (TextView) findViewById(R.id.txt_FarmerName);
        edtFarmerName = (TextView) findViewById(R.id.edtFarmerName);
        txt_FatherName = (TextView) findViewById(R.id.txt_FatherName);
        edit_FatherName = (TextView) findViewById(R.id.edit_FatherName);
        txt_FarmerPhoneNumber = (TextView) findViewById(R.id.txt_FarmerPhoneNumber);
        editFarmerPhoneNumber = (TextView) findViewById(R.id.editFarmerPhoneNumber);
        txt_BlockName = (TextView) findViewById(R.id.txt_BlockName);
        spin_block = (TextView) findViewById(R.id.spin_block);
        txt_DistrictName = (TextView) findViewById(R.id.txt_DistrictName);
        spin_district = (TextView) findViewById(R.id.spin_district);
        txt_VillageName = (TextView) findViewById(R.id.txt_VillageName);
        spin_villageName = (TextView) findViewById(R.id.spin_villageName);
        txt_AccountNumber = (TextView) findViewById(R.id.txt_AccountNumber);
        edit_AccountNumber = (TextView) findViewById(R.id.edit_AccountNumber);
        project_refresh = (ImageView) findViewById(R.id.project_refresh);
        farmerImage = (TextView) findViewById(R.id.farmer_image);
        passbookImage = (TextView) findViewById(R.id.passbook_image);
        productButton = (Button) findViewById(R.id.product_btn);
        nextButton = (Button) findViewById(R.id.next_btn);
        setFonts();
    }

    public void setFonts() {
        UtilFonts.UtilFontsInitialize(this);
        txt_FarmRegistration.setTypeface(UtilFonts.FS_Ultra);
        txt_Project.setTypeface(UtilFonts.KT_Medium);
        txt_AadharNo.setTypeface(UtilFonts.KT_Medium);
        edtaadhar_no.setTypeface(UtilFonts.KT_Regular);
        txt_FarmerName.setTypeface(UtilFonts.KT_Medium);
        edtFarmerName.setTypeface(UtilFonts.KT_Regular);
        txt_FatherName.setTypeface(UtilFonts.KT_Medium);
        edit_FatherName.setTypeface(UtilFonts.KT_Regular);
        txt_FarmerPhoneNumber.setTypeface(UtilFonts.KT_Medium);
        editFarmerPhoneNumber.setTypeface(UtilFonts.KT_Regular);
        txt_BlockName.setTypeface(UtilFonts.KT_Medium);
        spin_block.setTypeface(UtilFonts.KT_Regular);
        txt_DistrictName.setTypeface(UtilFonts.KT_Medium);
        spin_district.setTypeface(UtilFonts.KT_Regular);
        txt_VillageName.setTypeface(UtilFonts.KT_Medium);
        spin_villageName.setTypeface(UtilFonts.KT_Regular);
        txt_AccountNumber.setTypeface(UtilFonts.KT_Medium);
        edit_AccountNumber.setTypeface(UtilFonts.KT_Regular);
        farmerImage.setTypeface(UtilFonts.KT_Medium);
        passbookImage.setTypeface(UtilFonts.KT_Medium);
        productButton.setTypeface(UtilFonts.KT_Bold);
        nextButton.setTypeface(UtilFonts.KT_Bold);


    }


    private void getFarmerDetailsMethod(final String fId) {
        // display a progress dialog
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Please Wait..."); // set message
        progressDialog.show(); // show progress dialog
        FarmerListRequest request = new FarmerListRequest();
        // request.setProjectID(pId);
        request.setFarmerPersonelID(Integer.valueOf(fId));
        apiService.getFarmerDetails(request).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResponseObserver<Response<FarmerDetailsResponse>>(compositeDisposable) {
                    @Override
                    public void onNetworkError(Throwable e) {
                        progressDialog.cancel();
                        Log.v("wvqevqevqe", e.getMessage() + " " + e.toString());


                    }

                    @Override
                    public void onServerError(Throwable e, int code) {
                        progressDialog.cancel();


                    }

                    @Override
                    public void onNext(Response<FarmerDetailsResponse> response) {
                        progressDialog.cancel();
                        responsesData = response.body();
                        if (responsesData != null && responsesData.getFarmerpersonalData() != null && responsesData.getFarmerpersonalData().size() > 0) {
                            tblrow_aadhar.setVisibility(View.GONE);
                            edtFarmerName.setText(responsesData.getFarmerpersonalData().get(0).getFarmerName());
                            edit_FatherName.setText(responsesData.getFarmerpersonalData().get(0).getFatherName());
                            editFarmerPhoneNumber.setText(responsesData.getFarmerpersonalData().get(0).getFarmerPhno());
                            edit_AccountNumber.setText(responsesData.getFarmerpersonalData().get(0).getAccountNo());
                            spin_district.setText(responsesData.getFarmerpersonalData().get(0).getDistrict());
                            spin_villageName.setText(responsesData.getFarmerpersonalData().get(0).getVillage());
                            spin_block.setText(responsesData.getFarmerpersonalData().get(0).getBlock());
                            state_id = responsesData.getFarmerpersonalData().get(0).getStateID();
                            district_id = responsesData.getFarmerpersonalData().get(0).getDistrictID();
                            if (responsesData.getFarmerpersonalData().get(0).getFarmerImage() != null) {
                                farmerImage.setVisibility(View.VISIBLE);
                                farmerImageUrl = responsesData.getFarmerpersonalData().get(0).getFarmerImage();
                            } else {
                                farmerImage.setVisibility(View.GONE);
                                farmerImageUrl = null;
                            }
                            if (responsesData.getFarmerpersonalData().get(0).getPassBookImage() != null) {
                                passbookImageUrl = responsesData.getFarmerpersonalData().get(0).getFarmerImage();
                                passbookImage.setVisibility(View.VISIBLE);
                            } else {
                                passbookImage.setVisibility(View.GONE);
                                passbookImageUrl = null;
                            }
                            balanceAmount = responsesData.getFarmerpersonalData().get(0).getBalanceAmount();
                            if (responsesData.getInfoDataDT() != null && responsesData.getInfoDataDT().size() > 0) {
                                for (int i = 0; i < responsesData.getInfoDataDT().size(); i++) {
                                    if (i == 0) {
                                        if (responsesData.getInfoDataDT().get(i).getFarmID() != null && responsesData.getInfoDataDT().get(i).getFarmID() > 0) {
                                            farmId = "" + responsesData.getInfoDataDT().get(i).getFarmID();
                                        }
                                    } else {
                                        if (responsesData.getInfoDataDT().get(i).getFarmID() != null && responsesData.getInfoDataDT().get(i).getFarmID() > 0) {
                                            farmId = farmId + "=" + responsesData.getInfoDataDT().get(i).getFarmID();
                                        }
                                    }
                                }
                                infoLay.setVisibility(View.VISIBLE);
                                TaggedAdapter adapterMakes = new TaggedAdapter(FarmerDetailsActivity.this, responsesData.getInfoDataDT());
                                recyclerView.setLayoutManager(new LinearLayoutManager(FarmerDetailsActivity.this, LinearLayoutManager.HORIZONTAL, false));
                                recyclerView.setItemAnimator(new DefaultItemAnimator());
                                recyclerView.setAdapter(adapterMakes);
                            } else {
                                infoLay.setVisibility(View.GONE);
                            }//   responsesData.getInfoDataDT().get(0).
                        }


                    }
                });

    }

    public void imagePopup(String imag) {
        final Dialog dialog = new Dialog(this);
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
        dialog.setContentView(R.layout.image_popup);
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.show();
        final ImageView cancel = (ImageView) dialog.findViewById(R.id.cancel_popup);
        final ImageView img = (ImageView) dialog.findViewById(R.id.image_popup);
        Glide.with(this).load(imag).into(img);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

    }

}
