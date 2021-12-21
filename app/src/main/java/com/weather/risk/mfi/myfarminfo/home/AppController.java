package com.weather.risk.mfi.myfarminfo.home;

import android.content.Context;
import android.content.res.Configuration;
import android.text.TextUtils;
import android.util.Log;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.facebook.stetho.Stetho;
import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.retofitservices.ApiFactory;
import com.weather.risk.mfi.myfarminfo.retofitservices.ApiService;

import org.acra.ReportField;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import java.io.File;
import java.util.Locale;

import okhttp3.logging.HttpLoggingInterceptor;


@ReportsCrashes(formKey = "", // will not be used
        mailTo = "vishal.tripathi@weather-risk.com",
        customReportContent = {
                ReportField.APP_VERSION_CODE,
                ReportField.APP_VERSION_NAME,
                ReportField.ANDROID_VERSION,
                ReportField.PACKAGE_NAME,
                ReportField.REPORT_ID,
                ReportField.BUILD,
                ReportField.STACK_TRACE
        },
        mode = ReportingInteractionMode.TOAST,
        resToastText = R.string.toast_crash)

public class AppController extends MultiDexApplication {

    public static final String TAG = AppController.class.getSimpleName();

    private RequestQueue mRequestQueue;

    private static AppController mInstance;
    private static boolean activityVisible;

    // The following line should be changed to include the correct property id.
    private static final String PROPERTY_ID = "IN-57639076-1";

    public static int GENERAL_TRACKER = 0;
    private ApiService apiService;
    private static AppController appController;
    private Context context;

    public enum TrackerName {
        APP_TRACKER, // Tracker used only in this app.
        GLOBAL_TRACKER, // Tracker used by all the apps from a company. eg: roll-up tracking.
        ECOMMERCE_TRACKER, // Tracker used by all ecommerce transactions from a company.
    }

    public String BASE_URL_CHECKOUT = "https://myfarminfo.com/yfirest.svc/";

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

		/*ACRA.init(this);
		ACRA.getErrorReporter().setReportSender(new LocalReportSender(this));*/

        Log.d("AppController: ", "Application Created!");
        Stetho.initializeWithDefaults(this);

    }


    public static AppController getInstancenew() {
        return appController == null ? new AppController() : appController;
    }

    public Context getContext() {
        return context;
    }

    public ApiService getApiService() {
//        if (apiService == null) {
        apiService = ApiFactory.create(context);
//        }
        return apiService;
    }

    public ApiService getApiServiceTest() {
//        if (apiService == null) {
        apiService = ApiFactory.createTest(context);
//        }
        return apiService;
    }

    public ApiService getApiServiceCheckout() {
//        if (apiService == null) {
        apiService = ApiFactory.createChecout(context);
//        }
        return apiService;
    }

    public ApiService getApiServiceGson() {

//        if (apiService == null) {
        apiService = ApiFactory.createChecoutGson(context);
//        }
        return apiService;
    }

    public ApiService getApiServiceGson_weed() {

//        if (apiService == null) {
        apiService = ApiFactory.createChecoutGson_weedMngt(context);
//        }
        return apiService;
    }

    //WeatherSecurePro API
    public ApiService getApiServiceWeatherSecureProAPI() {
//        if (apiService == null) {
        apiService = ApiFactory.createChecoutGson_WeatherSecurePro(context);
//        }
        return apiService;
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        // free your memory, clean cache for example
        //Toast.makeText(getApplicationContext(), "Application on Low memory.", Toast.LENGTH_LONG).show();
        Log.d("AppController: ", "Application on Low memory.");
    }


    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public boolean clearApplicationData() {
        boolean val = false;
        File cache = getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!s.equals("lib")) {
                    deleteDir(new File(appDir, s));
                    Log.i("TAG", "**************** File /data/data/APP_PACKAGE/" + s + " DELETED *******************");
                }
            }
            val = true;
        }
        return val;
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }


    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;
    }

    public static void activityPaused() {
        activityVisible = false;
    }


    /*
    public synchronized Tracker getTracker(TrackerName trackerId) {
        if (!mTrackers.containsKey(trackerId)) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            Tracker t = (trackerId == TrackerName.APP_TRACKER) ? analytics.newTracker(PROPERTY_ID)
                    : (trackerId == TrackerName.GLOBAL_TRACKER) ? analytics.newTracker(R.xml.global_tracker)
                    : analytics.newTracker(R.xml.ecommerce_tracker);
            mTrackers.put(trackerId, t);
        }
        return mTrackers.get(trackerId);
    }
    */

    public static void updateLanguage(Context ctx, String lang) {

        Configuration cfg = new Configuration();
        String language = lang;

        if (TextUtils.isEmpty(language) && lang == null) {
            cfg.locale = Locale.getDefault();
            String tmp_locale = "";
            tmp_locale = Locale.getDefault().toString().substring(0, 2);
            //manager.SaveValueToSharedPrefs("force_locale", tmp_locale);

        } else if (lang != null) {
            cfg.locale = new Locale(lang);
            //manager.SaveValueToSharedPrefs("force_locale", lang);

        } else if (!TextUtils.isEmpty(language)) {
            cfg.locale = new Locale(language);
        }
        ctx.getResources().updateConfiguration(cfg, null);


    }

}