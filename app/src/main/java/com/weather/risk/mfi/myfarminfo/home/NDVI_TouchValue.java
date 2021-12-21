package com.weather.risk.mfi.myfarminfo.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.mapfragments.LatLonCellID;
import com.weather.risk.mfi.myfarminfo.utils.AppConstant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class NDVI_TouchValue extends AppCompatActivity {

  private GoogleMap mMap;
  BitmapDescriptor image;
  private GroundOverlay mGroundOverlay;
  float zoomValue = 19.0f;
  private Bitmap bitmap;
  Double nD, eD, sD, wD;
  String contour = null;
  LatLngBounds.Builder bc = null;
  private Marker myMarker;
  String imm = null;
  String date = null;
  ImageView backBTN;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.ndvi_touch);

    backBTN = (ImageView) findViewById(R.id.back_btn);
    backBTN.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        finish();
      }
    });

    setUpMapIfNeeded();
    //VMRuntime.getRuntime().setMinimumHeapSize(4 * 1024 * 1024);
    Log.v("Utils", "Max mem in MB : " + (Runtime.getRuntime().maxMemory() / (1024 * 1024)));
    Runtime.getRuntime().maxMemory();
    Debug.getNativeHeapSize();


  }


  public void setUpMapIfNeeded() {
    // Do a null check to confirm that we have not already instantiated the map.
    if (mMap == null) {
      // Try to obtain the map from the SupportMapFragment.
      ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.soil_info_map)).getMapAsync(new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
          mMap = googleMap;
          setUpMap();
        }
      });
    }

  }


  private void setUpMap() {

    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
      // TODO: Consider calling
      //    ActivityCompat#requestPermissions
      // here to request the missing permissions, and then overriding
      //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
      //                                          int[] grantResults)
      // to handle the case where the user grants the permission. See the documentation
      // for ActivityCompat#requestPermissions for more details.
      return;
    }
    mMap.setMyLocationEnabled(true);
    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

    contour = getIntent().getStringExtra("contour");
    date = getIntent().getStringExtra("date");
    Log.v("ccc", contour + "--");
    imm = getIntent().getStringExtra("image");
    if (imm != null && imm.length() > 10) {
      drawOnMap(imm, contour);
    }

    //  getInfoMap();
  }


  public void drawOnMap(String im, String cont) {


    try {
      cont = cont.trim();
      Log.v("contour", cont + "");


      // for (int i = 0; i < js.length(); i++) {

      String n = AppConstant.maxY;
      String e = AppConstant.minY;
      String s = AppConstant.maxX;
      String w = AppConstant.minX;
      if (n != null) {
        nD = Double.parseDouble(n);
      }
      if (e != null) {
        eD = Double.parseDouble(e);
      }
      if (s != null) {
        sD = Double.parseDouble(s);
      }
      if (w != null) {
        wD = Double.parseDouble(w);
      }

      if (mMap != null) {


        new getBitmapFromURL(im, nD, eD, sD, wD, cont).execute().get();
      }

      // }


    } catch (Exception e) {
      e.printStackTrace();
      Toast.makeText(getApplicationContext(), "Response Formatting Error", Toast.LENGTH_LONG).show();
    }

  }

  public void drawOnMap1(String im, String cont) {


    try {
      cont = cont.trim();
      Log.v("contour", cont + "");


      // for (int i = 0; i < js.length(); i++) {

      String n = AppConstant.maxY;
      String e = AppConstant.minY;
      String s = AppConstant.maxX;
      String w = AppConstant.minX;
      if (n != null) {
        nD = Double.parseDouble(n);
      }
      if (e != null) {
        eD = Double.parseDouble(e);
      }
      if (s != null) {
        sD = Double.parseDouble(s);
      }
      if (w != null) {
        wD = Double.parseDouble(w);
      }

      if (mMap != null) {


        new getBitmapFromURL1(im, nD, eD, sD, wD, cont).execute().get();
      }

      // }


    } catch (Exception e) {
      e.printStackTrace();
      Toast.makeText(getApplicationContext(), "Response Formatting Error", Toast.LENGTH_LONG).show();
    }

  }

  @SuppressLint("StaticFieldLeak")
  class getBitmapFromURL extends AsyncTask<String, Void, Bitmap> {
    private String mUrl;
    Double nD, wD, sD, eD;
    String contt = null;

    getBitmapFromURL(String url, Double n, Double e, Double s, Double w, String connn) {
      mUrl = url;
      nD = n;
      wD = w;
      sD = s;
      eD = e;
      contt = connn;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
      Log.v("bitmap_url", "--" + mUrl);
      Bitmap image = null;

      try {
        URL url = new URL(mUrl);
        image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
      } catch (IOException e) {
        System.out.println(e);
      }
      Log.v("bitmap_result", image + "");
      return image;
    }

    protected void onPostExecute(Bitmap feed) {
      // TODO: check this.exception
      // TODO: do something with the feed
      super.onPostExecute(null);

      if (feed != null) {
        Log.v("bitmap_result", feed + "");

        try {
          if (contt != null && contt.length() > 10) {
            drawImageOverMap(feed, nD, eD, sD, wD, contt);
          } else {
            String iddddd = AppConstant.iddd;
            if (iddddd != null && date != null) {
              getMapContour(iddddd, date);
            }
          }
        } catch (ExecutionException e) {
          e.printStackTrace();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }
  }

  @SuppressLint("StaticFieldLeak")
  class getBitmapFromURL1 extends AsyncTask<String, Void, Bitmap> {
    private String mUrl;
    Double nD, wD, sD, eD;
    String contt = null;

    getBitmapFromURL1(String url, Double n, Double e, Double s, Double w, String connn) {
      mUrl = url;
      nD = n;
      wD = w;
      sD = s;
      eD = e;
      contt = connn;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
      Log.v("bitmap_url", "--" + mUrl);
      Bitmap image = null;

      try {
        URL url = new URL(mUrl);
        image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
      } catch (IOException e) {
        System.out.println(e);
      }
      Log.v("bitmap_result", image + "");
      return image;
    }

    protected void onPostExecute(Bitmap feed) {
      // TODO: check this.exception
      // TODO: do something with the feed
      super.onPostExecute(null);

      if (feed != null) {
        Log.v("bitmap_result", feed + "");

        try {
          if (contt != null && contt.length() > 10) {
            drawImageOverMap(feed, nD, eD, sD, wD, contt);
          }
        } catch (ExecutionException e) {
          e.printStackTrace();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }
  }

  public void drawImageOverMap(Bitmap bit, Double nr, Double ea, Double so, Double wst, final String contr) throws ExecutionException, InterruptedException {


    //image =BitmapDescriptorFactory.fromResource(R.drawable.omnistar_g);

    //centerpoint_vrs
    bitmap = bit;


    Log.v("bitmap", bitmap + "");

    final LatLng southwest = new LatLng(ea, wst);
    final LatLng northeast = new LatLng(nr, so);

    final Double centerLat = (ea + nr) / 2;
    final Double centerLon = (wst + so) / 2;
    Log.v("lat1", southwest + "");
    Log.v("lat2", northeast + "");
    final LatLng cenLatLon = new LatLng(centerLat, centerLon);

    BitmapFactory.Options o2 = new BitmapFactory.Options();
    o2.inSampleSize = 4;
    image = BitmapDescriptorFactory.fromBitmap(bitmap);
    final LatLngBounds bounds = new LatLngBounds(southwest, northeast);
    Log.v("bound", bounds + "--" + southwest);
    mGroundOverlay = mMap.addGroundOverlay(new GroundOverlayOptions().image(image).position(cenLatLon, 70));
    // mMap.animateCamera(CameraUpdateFactory.zoomTo(zoomValue));


    mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
      @Override
      public void onMapLoaded() {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(centerLat, centerLon), zoomValue), 2000, null);


        if (contr != null && contr.length() > 9) {
          bc = new LatLngBounds.Builder();
          try {
            JSONArray js = new JSONArray(contr);
            for (int i = 0; i < js.length(); i++) {
              String latt = js.getJSONObject(i).getString("Y");
              String lonn = js.getJSONObject(i).getString("X");
              String va = js.getJSONObject(i).getString("Value");
              if (latt != null && latt.length() > 4) {
                Double l1 = Double.valueOf(latt);
                Double l2 = Double.valueOf(lonn);
                BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.transparent_ic);

                MarkerOptions markerOptions = null;

                markerOptions = new MarkerOptions().position(new LatLng(l1, l2))
                        .title("" + va)
                        .icon(icon);

                Marker mMarker = mMap.addMarker(markerOptions);
              }
            }


          } catch (JSONException e) {
            e.printStackTrace();
          }
        }

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
          @Override
          public boolean onMarkerClick(Marker marker) {
            // Toast.makeText(getApplicationContext(),marker.getTitle(),Toast.LENGTH_SHORT).show();
            return false;
          }
        });

      }
    });
    // positionFromBounds(bounds).
    //.transparency(0.4f)

    //.anchor(0, 1)


    bitmap.recycle();
    bitmap = null;
    System.gc();
    Runtime.getRuntime().gc();
  }

  private void getMapContour(String idd, String date) {
    final ProgressDialog dialoug = ProgressDialog.show(this, "",
            "Fetching Data\nPlease wait...", true);
    StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://www.myfarminfo.com/yfirest.svc/Clients/GetFarmData/" + idd + "/Ndvi/" + date,
            new Response.Listener<String>() {
              @Override
              public void onResponse(String response) {
                dialoug.cancel();
                // Display the first 500 characters of the response string.
                System.out.println("Volley Response : " + response);
                try {
                  response = response.trim();

                  response = response.replace("\\", "");
                  response = response.replace("\\", "");
                  response = response.replace("\\", "");
                  response = response.replace("\"[", "[");
                  response = response.replace("]\"", "]");
                  response = response.replace("\"{", "{");
                  response = response.replace("}\"", "}");
                  JSONObject jb = new JSONObject(response);
                  if (jb.has("DT")) {
                    JSONArray js = jb.getJSONArray("DT");
                    if (js.length() > 0) {
                      JSONArray jss = js.getJSONObject(0).getJSONArray("Farm_Data");
                      if (jss != null && jss.length() > 10) {
                        drawOnMap1(imm, jss.toString());
                      } else {
                        noData();
                      }
                    } else {
                      noData();
                    }
                  } else {
                    noData();
                  }


                } catch (Exception e) {
                  e.printStackTrace();
                  Toast.makeText(getApplicationContext(), "Response Formatting Error", Toast.LENGTH_LONG).show();
                }

              }
            }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        System.out.println("Volley Error : " + error);
        dialoug.cancel();
      }
    });

    // Adding request to volley request queue
    int socketTimeout = 60000;//60 seconds - change to what you want
    RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
    stringRequest.setRetryPolicy(policy);
    AppController.getInstance().addToRequestQueue(stringRequest);
  }

  public void noData() {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setMessage("Data not found?")
            .setCancelable(false)
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int id) {
                finish();
              }
            });
    AlertDialog alert = builder.create();
    alert.show();
  }

}
