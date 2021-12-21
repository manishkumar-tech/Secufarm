package com.weather.risk.mfi.myfarminfo.database;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.home.AppController;
import com.weather.risk.mfi.myfarminfo.utils.AppConstant;
import com.weather.risk.mfi.myfarminfo.utils.AppManager;
import com.weather.risk.mfi.myfarminfo.utils.TransparentProgressDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import io.requery.android.database.sqlite.SQLiteStatement;

public class State_Crop_StateDistr_Project_download {
    Context context;
    DBAdapter db;

    public State_Crop_StateDistr_Project_download(Context contexts) {
        context = contexts;
        db = new DBAdapter(context);
    }

    public void loadData() {
        final TransparentProgressDialog dialoug = new TransparentProgressDialog(
                context, context.getResources().getString(
                R.string.Loadingstate));
        dialoug.show();

//        final ProgressDialog dialoug = ProgressDialog.show(context, "",
//                context.getResources().getString(R.string.Loadingstate), true);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppManager.getInstance().AllStatesListURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialoug.cancel();
                        try {
                            // Display the first 500 characters of the response string.
                            System.out.println("Volley State Response : " + response);

                            response = response.trim();
                            response = response.substring(1, response.length() - 1);
                            response = response.replace("\\", "");

                            db.open();
                            db.getSQLiteDatabase().execSQL("DROP TABLE IF EXISTS " + db.TABLE_STATE);
                            db.getSQLiteDatabase().execSQL(db.CREATE_TABLE_STATE);

                            io.requery.android.database.sqlite.SQLiteDatabase SqliteDB = db.getSQLiteDatabase();
                            SqliteDB.beginTransaction();
                            try {
                                JSONArray jsonArray = new JSONArray(response);

                                String query = "INSERT INTO " + DBAdapter.TABLE_STATE + "(" + DBAdapter.STATE_ID + "," + DBAdapter.STATE_NAME + ") VALUES (?,?)";
                                SQLiteStatement stmt = SqliteDB.compileStatement(query);

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject territoryElementArray = jsonArray.getJSONObject(i);

                                    String stateId = String.valueOf((int) territoryElementArray.getDouble("StateID"));

                                    stmt.bindString(1, stateId);
                                    stmt.bindString(2, territoryElementArray.getString("StateName"));
                                    stmt.execute();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(context, context.getResources().getString(R.string.FormattingError), Toast.LENGTH_LONG).show();
                            }
                            SqliteDB.setTransactionSuccessful();
                            SqliteDB.endTransaction();
                            db.getClass();
                            getCropVariety();
                        } catch (Exception e) {
                            e.printStackTrace();
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


    private void getCropVariety() {
        final TransparentProgressDialog dialoug = new TransparentProgressDialog(
                context, context.getResources().getString(
                R.string.Loadingcrop));
        dialoug.show();
//        final ProgressDialog dialoug = ProgressDialog.show(context, "",
//                context.getResources().getString(R.string.Loadingcrop), true);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppManager.getInstance().CropsListURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            dialoug.cancel();
                            // Display the first 500 characters of the response string.
                            System.out.println("Volley State Response : " + response);

                            response = response.trim();
                            response = response.substring(1, response.length() - 1);
                            response = response.replace("\\", "");

                            db.open();
                            db.getSQLiteDatabase().execSQL("DROP TABLE IF EXISTS " + db.TABLE_CROP_VARIETY);
                            db.getSQLiteDatabase().execSQL(db.CREATE_CROP_VATIETY);

                            io.requery.android.database.sqlite.SQLiteDatabase SqliteDB = db.getSQLiteDatabase();
                            SqliteDB.beginTransaction();
                            try {

                                JSONArray jsonArray = new JSONArray(response);

                                String query = "INSERT INTO " + DBAdapter.TABLE_CROP_VARIETY + "(" + DBAdapter.STATE_ID + "," + DBAdapter.CROP_ID + "," + DBAdapter.CROP + "," + DBAdapter.VARIETY + ") VALUES (?,?,?,?)";
                                SQLiteStatement stmt = SqliteDB.compileStatement(query);

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONArray territoryElementArray = jsonArray.getJSONArray(i);

                                    stmt.bindString(1, territoryElementArray.get(3).toString());
                                    stmt.bindString(2, territoryElementArray.get(0).toString());
                                    stmt.bindString(3, territoryElementArray.get(1).toString());
                                    stmt.bindString(4, territoryElementArray.get(2).toString());
                                    stmt.execute();
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(context, context.getResources().getString(R.string.FormattingError), Toast.LENGTH_LONG).show();
                            }
                            SqliteDB.setTransactionSuccessful();
                            SqliteDB.endTransaction();
//                        db.close();
                            getStateDistrict();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialoug.cancel();
                Toast.makeText(context, context.getResources().getString(R.string.Couldnotconnect), Toast.LENGTH_LONG).show();
                System.out.println("Volley Error : " + error);
            }
        });
        int socketTimeout = 60000;//60 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);

        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    public void getStateDistrict() {
        final TransparentProgressDialog dialoug = new TransparentProgressDialog(
                context, context.getResources().getString(
                R.string.Loadingstate));
        dialoug.show();
//        final ProgressDialog dialoug = ProgressDialog.show(context, "",
//                context.getResources().getString(R.string.Loadingstate), true);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppManager.getInstance().StateDistrictURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        System.out.println("Volley State Response : " + response);
                        try {
                            dialoug.cancel();
                            response = response.trim();
                            response = response.substring(1, response.length() - 1);
                            response = response.replace("\\", "");

                            db.open();
                            db.getSQLiteDatabase().execSQL("DROP TABLE IF EXISTS " + db.TABLE_StateDistrict);
                            db.getSQLiteDatabase().execSQL(db.CREATE_StateDistrict);

                            try {
                                if (response != null) {

                                    JSONObject jsonObject = new JSONObject(response);
                                    JSONArray jsonArray = jsonObject.getJSONArray("DT");
                                    db.insertStateDistrict(jsonArray);
                                    db.close();
//                                getAllProjectList();
                                } else {
                                    Toast.makeText(context, context.getResources().getString(R.string.ServerError), Toast.LENGTH_LONG).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(context, context.getResources().getString(R.string.FormattingError), Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
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

    public void getAllProjectList() {
        final TransparentProgressDialog dialoug = new TransparentProgressDialog(
                context, context.getResources().getString(
                R.string.LoadingProject));
        dialoug.show();
//        final ProgressDialog dialoug = ProgressDialog.show(context, "",
//                context.getResources().getString(R.string.LoadingProject), true);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppManager.getInstance().ProjectListURL(AppConstant.user_id),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialoug.cancel();
                        // Display the first 500 characters of the response string.
                        System.out.println("Volley State Response : " + response);

                        response = response.trim();
                        response = response.substring(1, response.length() - 1);
                        response = response.replace("\\", "");

                        db.open();
                        if (response != null) {
                            db.getSQLiteDatabase().execSQL("DROP TABLE IF EXISTS " + db.TABLE_Projectlist);
                            db.getSQLiteDatabase().execSQL(db.CREATE_TABLE_Projectlist);
                        }


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
                            Toast.makeText(context, context.getResources().getString(R.string.FormattingError), Toast.LENGTH_LONG).show();
                        }
                        SqliteDB.setTransactionSuccessful();
                        SqliteDB.endTransaction();
                        db.getClass();
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


}

