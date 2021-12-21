package com.weather.risk.mfi.myfarminfo.mapfragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.bean.NDBI_Bean;
import com.weather.risk.mfi.myfarminfo.dabwali.DabwaliNdviAdapter;
import com.weather.risk.mfi.myfarminfo.home.AppController;
import com.weather.risk.mfi.myfarminfo.utils.AppConstant;
import com.weather.risk.mfi.myfarminfo.utils.AppManager;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

@SuppressLint("ValidFragment")
public class NewNDVIFragment extends Fragment {
  // TODO: Rename parameter arguments, choose names that match
  // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
  private static final String CALLING_ACTIVITY = "callingActivity";
  private static final String FARM_NAME = "FarmName";
  private static final String ALL_POINTS = "AllLatLngPount";
  private static final String AREA = "area";

  // TODO: Rename and change types of parameters
  private int callingActivity;
  private String selectedFarmName;
  private String area;
  String data = null;

  String nextResponseNDVI = null;
  String id;


  @SuppressLint("ValidFragment")
  public NewNDVIFragment(String nextResponseNDVI1) {
    // Required empty public constructor

    nextResponseNDVI = nextResponseNDVI1;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

  }

  /*  Spinner districtSpinner,villageSpinner;
    Button submitBtn;
*/

  private String cityArr[];
  String villageID = null;
  String vill_id = null;
  String lat = null;
  String lon = null;
  String villageName = null;
  RecyclerView recyclerView;
  //Button nextBtn;


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.new_ndvi_fragment, container, false);


    String role = AppConstant.role;
    Log.v("roleeeeeelllll", role + "");


    recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_ndvi);
    LinearLayoutManager llm = new LinearLayoutManager(getActivity());
    llm.setOrientation(LinearLayoutManager.VERTICAL);
    recyclerView.setLayoutManager(llm);


    SharedPreferences prefs = getActivity().getSharedPreferences(AppConstant.SHARED_PREFRENCE_NAME, getActivity().MODE_PRIVATE);

    String lat1 = prefs.getString("lat", null);
    String lon1 = prefs.getString("lon", null);
    String villageId1 = prefs.getString("villageId", null);
    String villageName1 = prefs.getString("villageName", null);
    String role1 = prefs.getString(AppConstant.PREFRENCE_KEY_ROLE, null);

    lat = lat1;
    lon = lon1;
    villageID = villageId1;
    villageName = villageName1;

    AppConstant.maxY = null;
    AppConstant.maxX = null;
    AppConstant.minY = null;
    AppConstant.minX = null;
    id = AppConstant.dabFarmId;


    if (nextResponseNDVI != null && nextResponseNDVI.length() > 10) {
      messageData(nextResponseNDVI);
    } else if (AppConstant.farm_id != null) {
      ndviData(AppConstant.farm_id);
    }

    return view;
  }


  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    // Do something that differs the Activity's menu here
    inflater.inflate(R.menu.pepsico_menu, menu);
    super.onCreateOptionsMenu(menu, inflater);

  }


  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {

      //   villageMethod();
      return true;
    } else if (id == R.id.action_error) {
      File logFile = new File(Environment.getExternalStorageDirectory(), "MFIErrorLog.txt");
      if (logFile.exists()) {

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        // set the type to 'email'
        emailIntent.setType("vnd.android.cursor.dir/email");
        String to[] = {"vishal.tripathi@iembsys.com"};
        emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
        // the attachment
        emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(logFile));
        // the mail subject
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "MFI Error log");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "MFI app");

        if (emailIntent.resolveActivity(getActivity().getPackageManager()) != null) {
          startActivity(Intent.createChooser(emailIntent, "Send email..."));
        } else {
          Toast.makeText(getActivity(), getResources().getString(R.string.Noemailapplicationisavailble), Toast.LENGTH_LONG).show();
        }

      } else {
        Toast.makeText(getActivity(), getResources().getString(R.string.MFI_pepsicoErrorLogfiledoes), Toast.LENGTH_LONG).show();
      }

    }

    return super.onOptionsItemSelected(item);
  }


  @Override
  public void onResume() {
    super.onResume();

  }


  public void messageData(String response) {

    response = response.trim();

    response = response.replace("\\", "");
    response = response.replace("\"{", "{");
    response = response.replace("}\"", "}");
    response = response.replace("\"[", "[");
    response = response.replace("]\"", "]");

    response = response.replace("\\", "");
    response = response.replace("\"{", "{");
    response = response.replace("}\"", "}");
    response = response.replace("\"[", "[");
    response = response.replace("]\"", "]");

    System.out.println(" Response : " + response);

    if (response != null) {
      try {

        JSONObject jsonObject = new JSONObject(response);
        JSONArray jsonArray = jsonObject.getJSONArray("DT");
        JSONArray jsonArray1 = jsonObject.getJSONArray("DT10");

        for (int i = 0; i < jsonArray1.length(); i++) {

          JSONObject jsonObject1 = jsonArray1.getJSONObject(i);


          AppConstant.maxY = jsonObject1.getString("MaxY");
          AppConstant.maxX = jsonObject1.getString("MaxX");
          AppConstant.minY = jsonObject1.getString("MinY");
          AppConstant.minX = jsonObject1.getString("MinX");
        }

        ArrayList<NDBI_Bean> msgList = new ArrayList<NDBI_Bean>();


        for (int i = 0; i < jsonArray.length(); i++) {
          NDBI_Bean bean = new NDBI_Bean();
          JSONObject jsonObject1 = jsonArray.getJSONObject(i);

          // bean.setId(jsonObject1.getString("ID"));
          bean.setFinal_soilImg(jsonObject1.getString("Final_Img"));
          bean.setDate(jsonObject1.getString("Date"));
          bean.setVillage_mean(jsonObject1.getString("Village_mean"));
          bean.setStart_Date(jsonObject1.getString("Start_Date"));
          bean.setCaptcha_Img(jsonObject1.getString("Captcha_Img"));


          if (jsonObject1.isNull("Farm_Data") || jsonObject1.optString("Farm_Data").length() < 5) {
            bean.setFarm_Data("");
          } else {

            JSONArray jsss = jsonObject1.getJSONArray("Farm_Data");
            bean.setFarm_Data(jsss.toString());
          }

          msgList.add(bean);
        }

        if (msgList.size() > 0) {
          DabwaliNdviAdapter adapter = new DabwaliNdviAdapter(getActivity(), msgList);
          recyclerView.setAdapter(adapter);
        } else {
          noDataFoundMethod();
        }


      } catch (JSONException e) {
        e.printStackTrace();
      }


    }


  }


  private void noDataFoundMethod() {
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    builder.setTitle(getResources().getString(R.string.no_data)).
            setMessage(getResources().getString(R.string.NDVIDataisnotfound)).
            setPositiveButton(getResources().getText(R.string.Ok), new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();

              }
            });

    AlertDialog dialog = builder.create();
    dialog.show();

  }


  public void ndviData(String id) {
    String usID = AppConstant.user_id;

    if (id != null) {
      id = id.trim();
    }

    final Dialog dialoug2 = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.FetchingNDVIData), true);

    //  String ssss = "https://myfarminfo.com/yfirest.svc/Clients/DataVersion2/wrmsdabwali/" + id + "/" + usID;
    String ssss = "https://myfarminfo.com/yfirest.svc/Clients/GetNdviValue/MFI/" + id + "/" + usID;
    String url = AppManager.getInstance().removeSpaceForUrl(ssss);
    Log.v("kkkkk", url);
    final String finalId = id;
    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
            new Response.Listener<String>() {
              @Override
              public void onResponse(String response) {
                dialoug2.cancel();
                // Display the first 500 characters of the response string.
                response = response.trim();
                //  response = response.substring(1, response.length() - 1);
                response = response.replace("\\", "");
                response = response.replace("\\", "");
                response = response.replace("\\", "");
                response = response.replace("\"{", "{");
                response = response.replace("}\"", "}");
                response = response.replace("\"[", "[");
                response = response.replace("]\"", "]");

                response = response.replace("\"{", "{");
                response = response.replace("}\"", "}");
                response = response.replace("\"[", "[");
                response = response.replace("]\"", "]");
                response = response.trim();
                System.out.println(" Response : " + response);
                nextResponseNDVI = response;

                messageData(nextResponseNDVI);
              }
            }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        dialoug2.cancel();
        System.out.println("Volley Error : " + error);

      }
    });

    int socketTimeout = 60000;//60 seconds - change to what you want
    RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, 0, 0);
    stringRequest.setRetryPolicy(policy);
    // Adding request to volley request queue
    AppController.getInstance().addToRequestQueue(stringRequest);
  }


}
