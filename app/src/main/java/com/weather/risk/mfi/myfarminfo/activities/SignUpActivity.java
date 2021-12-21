package com.weather.risk.mfi.myfarminfo.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.database.DBAdapter;
import com.weather.risk.mfi.myfarminfo.entities.AllFarmDetail;
import com.weather.risk.mfi.myfarminfo.entities.CropQueryData;
import com.weather.risk.mfi.myfarminfo.entities.Register;
import com.weather.risk.mfi.myfarminfo.entities.SignInData;
import com.weather.risk.mfi.myfarminfo.mapfragments.LatLonCellID;
import com.weather.risk.mfi.myfarminfo.utils.AppConstant;
import com.weather.risk.mfi.myfarminfo.utils.AppManager;
import com.weather.risk.mfi.myfarminfo.utils.CustomHttpClient;
import com.weather.risk.mfi.myfarminfo.utils.UtilFonts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SignUpActivity extends AppCompatActivity {


    Register register;
    SharedPreferences pref;
    DBAdapter db;
    String creatString, redirectData = null;
    private Toolbar toolbar;


    TextView logo,  txt_username, txt_VisibleNames,
            txt_MobileNumbers, txt_Passwords, txt_ConfirmPasswords, txt_EmailAddressoptional;
    EditText edit_createusername, edit_visibleusername, edit_mobileno, edit_password, edit_confirmpassword,
            edit_emailaddress;
    Button btn_submit;


    public static String syncForRegistration(String data, Register register) {
        String completeStringForRegister = "";
        String message = "";
        completeStringForRegister = data;
        String response = null;
        try {

            response = CustomHttpClient.executeHttpPut("https://myfarminfo.com/yfirest.svc/Register/" + completeStringForRegister);
            System.out.println("response " + response);
            Log.d("RegistrationData", response);
            if (response.contains("Error")) {
                String s = "Error";
                return s;
            }
            response = response.trim();
            System.out.println("response " + response);
            response = response.substring(1, response.length() - 1);
//            response "[{\"UserID\":100055,\"VisibleName\":\"jyfjyd\",\"UserSince\":\"September 2015\"}]"
            System.out.println("response " + response);
            response = response.replace("\\", "");
            System.out.println("response " + response);
            JSONArray jArray = new JSONArray(response);
            Log.d("afterfilterResponse", response);

            if (jArray.length() == 0) {
                message = "No Registered";
                return message;
            }
            for (int i = 0; i < jArray.length(); i++) {
                System.out.println(" i " + i);
                JSONObject jObject = jArray.getJSONObject(i);
                System.out.println("UserID " + jObject.getString("UserID"));
                AppConstant.user_id = jObject.getString("UserID");
                register.setUser_id(jObject.getString("UserID"));
                String s = AppConstant.user_id;
                System.out.println("appconstant " + s);
                System.out.println("VisibleName " + jObject.getString("VisibleName"));
                AppConstant.visible_Name = jObject.getString("VisibleName");
                register.setVisibleName(jObject.getString("VisibleName"));
                String s1 = AppConstant.user_id;
                System.out.println("appconstant " + s1);
                message = "OK";
            }
            return message;
        } catch (Exception e) {
//
            e.printStackTrace();
            Log.d("Status", "" + e);

            return "NoResponse";
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        setIdDefine();

        redirectData = getIntent().getStringExtra("add_farm");


        db = new DBAdapter(SignUpActivity.this);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isValidate())
                    return;


                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                register = new Register();
                register.setUserName(edit_createusername.getText().toString());
                register.setVisibleName(edit_visibleusername.getText().toString());
                register.setPassword(edit_password.getText().toString());
                String ss = edit_emailaddress.getText().toString();
                if (ss != null && ss.length() > 8) {
                    register.setMailId(edit_emailaddress.getText().toString());
                } else {
                    register.setMailId(edit_mobileno.getText().toString());
                }
                register.setMobileNo(edit_mobileno.getText().toString());
                Date date = new Date();
                register.setCreatedDateTime(sdf.format(date));

                String userId = "";
                register.setUser_id(userId);
                creatString = creatStringForRegister();

                Log.d("createstring_signup", creatString);

                if (AppConstant.APP_MODE == AppConstant.OFFLINE) {
                    db.open();
                    boolean isSaved = register.save(db, DBAdapter.SAVE);
                    if (!isSaved) {
                        Toast.makeText(SignUpActivity.this, getResources().getString(R.string.Emailaddressalready), Toast.LENGTH_LONG).show();
                        return;
                    }
                    db.close();
                    if (pref == null) {
                        pref = getSharedPreferences(AppConstant.SHARED_PREFRENCE_NAME, MODE_PRIVATE);
                    }
                    SharedPreferences.Editor editor = pref.edit();
                    System.out.println("Saved User id : " + null);
                    editor.putString(AppConstant.PREFRENCE_KEY_USER_ID, null);
                    editor.putString(AppConstant.PREFRENCE_KEY_VISIBLE_NAME, register.getVisibleName());
                    editor.putBoolean(AppConstant.PREFRENCE_KEY_ISLOGIN, true);
                    editor.putString(AppConstant.PREFRENCE_KEY_EMAIL, register.getMailId());
                    editor.putString(AppConstant.PREFRENCE_KEY_PASS, register.getPassword());
                    editor.putBoolean(AppConstant.PREFRENCE_KEY_ISSAVED, true);
                    editor.commit();
                    AppConstant.isLogin = true;
                    AppConstant.user_id = null;
                    finish();
//                    Intent intent = new Intent(SignUpActivity.this, AddFarmWithoutMap.class);
//                    intent.putExtra("calling-activity", AppConstant.HomeActivity);
//                    intent.putExtra("lat", String.valueOf(LatLonCellID.currentLat));
//                    intent.putExtra("log", String.valueOf(LatLonCellID.currentLon));
//                    startActivity(intent);
                }/* else if (AppManager.isOnline(SignUpActivity.this)) {
                    new syncFarRegister(creatString).execute();

                } */ else {
                    new syncFarRegister(creatString).execute();
//                    Toast.makeText(getBaseContext(), "Network not available", Toast.LENGTH_LONG).show();
                }


            }
        });


    }


    public void contestMethod11() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.title_activity_navigation_drawer));
        builder.setMessage(getResources().getString(R.string.yourregisterhasbeen))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.Ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        if (redirectData == null || !redirectData.equalsIgnoreCase("farm")) {

                            if (pref == null) {
                                pref = getSharedPreferences(AppConstant.SHARED_PREFRENCE_NAME, MODE_PRIVATE);
                            }
                            SharedPreferences.Editor editor = pref.edit();

                            editor.putBoolean(AppConstant.PREFRENCE_KEY_ISLOGIN, false);
                            editor.apply();

                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.Pleaselogin), Toast.LENGTH_LONG).show();
                            finish();


                        } else {
                            if (pref == null) {
                                pref = getSharedPreferences(AppConstant.SHARED_PREFRENCE_NAME, MODE_PRIVATE);
                            }
                            SharedPreferences.Editor editor = pref.edit();

                            editor.putBoolean(AppConstant.PREFRENCE_KEY_ISLOGIN, true);
                            editor.apply();
                            finish();
                        }

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public String creatStringForRegister() {
        String str = "";


        str = register.getMailId() + "/" +
                register.getUserName() + "/" +
                register.getPassword() + "/" +
                register.getVisibleName() + "/" +
                register.getMobileNo();

        return str;

    }

    private boolean isValidate() {

        if (edit_createusername.getText().toString().length() > 0) {

            if (edit_visibleusername.getText().toString().length() > 0) {

                if (AppManager.getInstance().isMobileNoValid(edit_mobileno.getText().toString())) {

                    if (edit_password.getText().toString().length() > 0) {

                        if (edit_confirmpassword.getText().toString().length() > 0) {

                            if (edit_password.getText().toString().trim().equalsIgnoreCase(edit_confirmpassword.getText().toString().trim())) {

                                if (edit_emailaddress.getText().toString().trim().equalsIgnoreCase("") ||
                                        (edit_emailaddress.getText().toString().length() > 0 && AppManager.getInstance().isEmailIDValid(edit_emailaddress.getText().toString()))) {
                                    return true;
                                } else {
                                    Toast.makeText(getBaseContext(), getResources().getString(R.string.validemailid), Toast.LENGTH_LONG).show();
                                }

                            } else {
                                Toast.makeText(getBaseContext(), getResources().getString(R.string.passwordconfirmpassword), Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getBaseContext(), getResources().getString(R.string.Enteryourconfirmpassword), Toast.LENGTH_LONG).show();

                        }

                    } else {
                        Toast.makeText(getBaseContext(), getResources().getString(R.string.Enteryourpassword), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.Pleaseentervalid), Toast.LENGTH_LONG).show();
                }

            } else {
                Toast.makeText(getBaseContext(), getResources().getString(R.string.entervisiblename), Toast.LENGTH_LONG).show();

            }

        } else {
            Toast.makeText(getBaseContext(), getResources().getString(R.string.Enteryourname), Toast.LENGTH_LONG).show();
        }
        return false;
    }

    private boolean isValid() {

        if (edit_createusername.getText().toString().trim().length() > 0) {
            register.setUserName(edit_createusername.getText().toString());
        } else {
            Toast.makeText(SignUpActivity.this, getResources().getString(R.string.Enteryourname), Toast.LENGTH_LONG).show();
            return false;
        }


        if (edit_visibleusername.getText().toString().trim().length() > 0) {
            register.setVisibleName(edit_visibleusername.getText().toString());
        } else {
            Toast.makeText(SignUpActivity.this, getResources().getString(R.string.entervisiblename), Toast.LENGTH_LONG).show();
            ;
            return false;
        }

        if (edit_password.getText().toString().trim().length() > 0) {
            register.setPassword(edit_password.getText().toString());
        } else {
            Toast.makeText(SignUpActivity.this, getResources().getString(R.string.Enteryourpassword), Toast.LENGTH_LONG).show();
            return false;
        }


        if (edit_confirmpassword.getText().toString().trim().length() > 0) {
//            data.setPassword(confirm_password.getText().toString());
        } else {
            Toast.makeText(SignUpActivity.this, getResources().getString(R.string.Enteryourconfirmpassword), Toast.LENGTH_LONG).show();
            return false;
        }

        if (edit_emailaddress.getText().toString().trim().length() > 0) {
            register.setMailId(edit_emailaddress.getText().toString());
        } else {
            Toast.makeText(SignUpActivity.this, getResources().getString(R.string.Enteryouremailid), Toast.LENGTH_LONG).show();
            return false;
        }


        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       /* if (id == R.id.action_signup) {
            Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private class syncFarRegister extends AsyncTask<Void, Void, String> {


        String result = null;
        String createdString;

        ProgressDialog progressDialog;

        public syncFarRegister(String createdString) {


            this.createdString = createdString;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //   progressDialog.show(SignUpActivity.this, "", "Please wait...", true);
        }

        @Override
        protected String doInBackground(Void... params) {

            result = syncForRegistration(AppManager.getInstance().removeSpaceForUrl(createdString), register);
            Log.d("result_signup-----", result);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPreExecute();
//            progressDialog.cancel();
            if (pref == null) {
                pref = getSharedPreferences(AppConstant.SHARED_PREFRENCE_NAME, MODE_PRIVATE);
            }
            SharedPreferences.Editor editor = pref.edit();
            editor.putString(AppConstant.PREFRENCE_KEY_USER_ID, AppConstant.user_id);

            editor.putString(AppConstant.PREFRENCE_KEY_VISIBLE_NAME, AppConstant.visible_Name);
            editor.putBoolean(AppConstant.PREFRENCE_KEY_ISLOGIN, false);
            editor.putString(AppConstant.PREFRENCE_KEY_EMAIL, register.getMailId());
            editor.putString(AppConstant.PREFRENCE_KEY_PASS, register.getPassword());
            editor.putBoolean(AppConstant.PREFRENCE_KEY_ISSAVED, true);
            AppConstant.mobile_no = edit_mobileno.getText().toString();
            AppConstant.visible_Name = register.getVisibleName();
            editor.putString(AppConstant.PREFRENCE_KEY_MOBILE, AppConstant.mobile_no);


           /* Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
            intent.putExtra(AppConstant.REGISTER_USER, register);
*/
            if (result != null) {
                if (result.contains("OK")) {

                    editor.apply();
                    AppConstant.isLogin = true;
                   /* Toast.makeText(SignUpActivity.this, "Successfully registered please login", Toast.LENGTH_LONG).show();
                    finish();
*/
//                    new getFarmDetailAsyncTask().execute();

                    //contestMethod();


                    db.open();
                    register.save(db, DBAdapter.SENT);
                    db.close();

                    contestMethod11();

                } else if (result.contains("Error")) {
                    Toast.makeText(SignUpActivity.this, getResources().getString(R.string.ServerError), Toast.LENGTH_LONG).show();
                    return;
                } else {
                    Toast.makeText(SignUpActivity.this, getResources().getString(R.string.Somethingwentwrong), Toast.LENGTH_LONG).show();
                    return;
                  /*  db.open();
                    register.save(db, DBAdapter.SAVE);
                    db.close();
                    editor.commit();
                    AppConstant.isLogin = true;
                    AppConstant.user_id = register.getUser_id();

                    Intent in = new Intent(getApplicationContext(), NewHomeScreen.class);
                    in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(in);
                    finish();*/
                }
            } else {
                Toast.makeText(SignUpActivity.this, getResources().getString(R.string.Couldnotconnect) + "\n \n " + getResources().getString(R.string.usersaved), Toast.LENGTH_LONG).show();
                db.open();
                register.save(db, DBAdapter.SAVE);
                db.close();
                editor.commit();
                AppConstant.isLogin = true;
                AppConstant.user_id = register.getUser_id();

                // new getFarmDetailAsyncTask().execute();

            }


        }


    }

    private class getFarmDetailAsyncTask extends AsyncTask<Void, Void, String> {
        SignInData data;
        String result = "";
        ProgressDialog progressDialog;

        public getFarmDetailAsyncTask() {
            this.data = data;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(SignUpActivity.this);
            progressDialog.setMessage(getResources().getString(R.string.getfarmdetails));
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(true);
            progressDialog.show();
            progressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                                                @Override
                                                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                                                    return false;
                                                }
                                            }
            );
        }

        @Override
        protected String doInBackground(Void... params) {
            String sendRequest = null;
            try {
                sendRequest = AppManager.getInstance().getFarmList + AppConstant.user_id;
                Log.d("get farm url", sendRequest);
                String response = AppManager.getInstance().httpRequestGetMethod(sendRequest);
                System.out.println("farm_details :" + response);
                return response;

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null; //show network problem
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPreExecute();
            try {

                if (response != null) {
                    if (response.contains("No Farms")) {
                        System.out.println("Farm not available");
                        Toast.makeText(getBaseContext(), getResources().getString(R.string.Farmnotavailable), Toast.LENGTH_LONG).show();

                    } else {
                        AllFarmDetail addFarmDetail;

                        db.open();
                        //  db.deleteAllFarmDetailTable();
                        System.out.println("farm detail response " + response);

                        JSONArray jArray = new JSONArray(AppManager.getInstance().placeSpaceIntoString(response));
                        System.out.println("farm detail response " + jArray.length());
                        if (jArray.length() > 0) {
                            int deleteCount = db.db.delete(DBAdapter.DATABASE_TABLE_ALL_FARM_DETAIL, DBAdapter.SENDING_STATUS + " = '" + DBAdapter.SENT + "'", null);
                            int deleteCount1 = db.db.delete(DBAdapter.TABLE_QUERY_CROP, DBAdapter.SENDING_STATUS + " = '" + DBAdapter.SENT + "'", null);
                            System.out.println("deleteCount : " + deleteCount + " deleteCount1 : " + deleteCount1);
                        }
                        for (int i = 0; i < jArray.length(); i++) {

                            JSONObject jsonObject = jArray.getJSONObject(i);
                            addFarmDetail = new AllFarmDetail(jsonObject);
                            addFarmDetail.setUserId(AppConstant.user_id);
                            String farmId = addFarmDetail.getFarmId();
                            String farmName = addFarmDetail.getFarmName();
                            String farmerName = addFarmDetail.getFarmerName();
                            String farmerNumber = addFarmDetail.getFarmerPhone();
                            String concern = addFarmDetail.getConcern();
                            Long l = db.insertAllFarmDetail(addFarmDetail, DBAdapter.SENT);
                            if (jsonObject.has("CropInfo")) {
                                JSONArray corpInfoArray = jsonObject.getJSONArray("CropInfo");

                                for (int j = 0; j < corpInfoArray.length(); j++) {
                                    JSONObject cropJsonObject = corpInfoArray.getJSONObject(j);
                                    CropQueryData data = new CropQueryData();
                                    data.setFarmId(farmId);
                                    data.setFarmName(farmName);

                                    data.setYourCencern(concern);
                                    data.setCropID(cropJsonObject.isNull("CropID") ? "" : cropJsonObject.getString("CropID"));
                                    data.setCrop(cropJsonObject.isNull("CropName") ? "" : cropJsonObject.getString("CropName"));
                                    String variety = cropJsonObject.isNull("Variety") ? "" : cropJsonObject.getString("Variety");
                                    data.setVariety(variety.replaceAll("%20", " "));
                                    data.setBasalDoseN(cropJsonObject.isNull("N") ? "0" : cropJsonObject.getString("N"));
                                    data.setBasalDoseP(cropJsonObject.isNull("P") ? "0" : cropJsonObject.getString("P"));
                                    data.setBasalDoseK(cropJsonObject.isNull("K") ? "0" : cropJsonObject.getString("K"));
                                    data.setSowPeriodForm(cropJsonObject.isNull("SowDate") ? "" : cropJsonObject.getString("SowDate"));
                                    data.setOtherNutrition(cropJsonObject.isNull("OtherNutrient") ? "" : cropJsonObject.getString("OtherNutrient"));
                                    data.setBesalDoseApply(cropJsonObject.isNull("BasalDoseApply") ? "" : cropJsonObject.getString("BasalDoseApply"));
                                    long inserted = data.insert(db, DBAdapter.SENT);
                                    System.out.println("database return value=" + l);
                                }
                            }

                        }
                        db.close();

                        //   contestMethod();
                    }
                } else {
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.ServerError), Toast.LENGTH_LONG).show();
                    //   contestMethod();
                }
            } catch (JSONException e) {
                e.printStackTrace();

                System.out.println("catch block Pls Try again");
                //   contestMethod();
            }

            progressDialog.dismiss();

        }
    }

    private void setIdDefine() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        logo = (TextView) findViewById(R.id.logo);

        txt_username = (TextView) findViewById(R.id.txt_username);
        edit_createusername = (EditText) findViewById(R.id.edit_createusername);
        txt_VisibleNames = (TextView) findViewById(R.id.txt_VisibleNames);
        edit_visibleusername = (EditText) findViewById(R.id.edit_visibleusername);
        txt_MobileNumbers = (TextView) findViewById(R.id.txt_MobileNumbers);
        edit_mobileno = (EditText) findViewById(R.id.edit_mobileno);
        txt_Passwords = (TextView) findViewById(R.id.txt_Passwords);
        edit_password = (EditText) findViewById(R.id.edit_password);
        txt_ConfirmPasswords = (TextView) findViewById(R.id.txt_ConfirmPasswords);
        edit_confirmpassword = (EditText) findViewById(R.id.edit_confirmpassword);
        txt_EmailAddressoptional = (TextView) findViewById(R.id.txt_EmailAddressoptional);
        edit_emailaddress = (EditText) findViewById(R.id.edit_emailaddress);
        btn_submit = (Button) findViewById(R.id.btn_submit);

        setFonts();
    }

    public void setFonts() {
        UtilFonts.UtilFontsInitialize(this);
        logo.setTypeface(UtilFonts.FS_Ultra);

        logo.setText(getResources().getString(R.string.UserRegistration));
        logo.setTextColor(getResources().getColor(R.color.white));

        txt_username.setTypeface(UtilFonts.KT_Medium);
        edit_createusername.setTypeface(UtilFonts.KT_Regular);
        txt_VisibleNames.setTypeface(UtilFonts.KT_Medium);
        edit_visibleusername.setTypeface(UtilFonts.KT_Regular);
        txt_MobileNumbers.setTypeface(UtilFonts.KT_Medium);
        edit_mobileno.setTypeface(UtilFonts.KT_Regular);
        txt_Passwords.setTypeface(UtilFonts.KT_Medium);
        edit_password.setTypeface(UtilFonts.KT_Regular);
        txt_ConfirmPasswords.setTypeface(UtilFonts.KT_Medium);
        edit_confirmpassword.setTypeface(UtilFonts.KT_Regular);
        txt_EmailAddressoptional.setTypeface(UtilFonts.KT_Medium);
        edit_emailaddress.setTypeface(UtilFonts.KT_Regular);

        btn_submit.setTypeface(UtilFonts.KT_Bold);


    }

}
