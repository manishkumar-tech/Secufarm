package com.weather.risk.mfi.myfarminfo.retofitservices;

import android.content.Context;
import android.util.Log;

//import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.weather.risk.mfi.myfarminfo.BuildConfig;
//import com.wps.weather_secure.BuildConfig;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class ApiFactory {
    private static final String BASE_URL;
    private static final String BASE_URL_Test;
    private static final String BASE_URL_CHECKOUT;
    private static final String BASE_URL_WeatherSecurePro;
    private static final String BASE_URL_WeedMangt;

    private static Context context;
    private static Retrofit retrofit;

    static {
        BASE_URL = "https://apimfi.com/api/";
        BASE_URL_Test = "https://apimfi.com/api/";
//        BASE_URL_Test = "https://test.apimfi.com/api/";
        BASE_URL_CHECKOUT = "https://myfarminfo.com/yfirest.svc/";
        BASE_URL_WeatherSecurePro = "http://test.weathersecurepro.com/";
        BASE_URL_WeedMangt = "https://secu.farm/";
//        "https://myfarminfo.com/yfirest.svc/Activities/Tasks/" + userI + "/" + projectID;
    }

    public static ApiService create(Context ctx) {
        //prefManager = PrefManager.getInstance(ctx);
        context = ctx;

        retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                /* .addConverterFactory(ScalarsConverterFactory.create()) //important
                 .addConverterFactory(GsonConverterFactory.create(new Gson()))*/
                .addConverterFactory(MyJsonConverter.create(new Gson()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(gethttpClient(ctx))

                .build();
        return retrofit.create(ApiService.class);
    }

    public static ApiService createTest(Context ctx) {
        //prefManager = PrefManager.getInstance(ctx);
        context = ctx;

        retrofit = new Retrofit.Builder().baseUrl(BASE_URL_Test)
                /* .addConverterFactory(ScalarsConverterFactory.create()) //important
                 .addConverterFactory(GsonConverterFactory.create(new Gson()))*/
                .addConverterFactory(MyJsonConverter.create(new Gson()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(gethttpClient(ctx))

                .build();
        return retrofit.create(ApiService.class);
    }

    public static ApiService createChecout(Context ctx) {
        //prefManager = PrefManager.getInstance(ctx);
        context = ctx;

        retrofit = new Retrofit.Builder().baseUrl(BASE_URL_CHECKOUT)
                /* .addConverterFactory(ScalarsConverterFactory.create()) //important
                 .addConverterFactory(GsonConverterFactory.create(new Gson()))*/
                .addConverterFactory(MyJsonConverter.create(new Gson()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(gethttpClient(ctx))

                .build();
        return retrofit.create(ApiService.class);
    }

    public static ApiService createChecoutGson(Context ctx) {
        //prefManager = PrefManager.getInstance(ctx);
        context = ctx;

        retrofit = new Retrofit.Builder().baseUrl(BASE_URL_CHECKOUT)
                /* .addConverterFactory(ScalarsConverterFactory.create()) //important
                 .addConverterFactory(GsonConverterFactory.create(new Gson()))*/
                .addConverterFactory(MyGsonConverter.create(new Gson()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(gethttpClient(ctx))

                .build();
        return retrofit.create(ApiService.class);
    }

    public static ApiService createChecoutGson_weedMngt(Context ctx) {
        //prefManager = PrefManager.getInstance(ctx);
        context = ctx;

        retrofit = new Retrofit.Builder().baseUrl(BASE_URL_WeedMangt)
                /* .addConverterFactory(ScalarsConverterFactory.create()) //important
                 .addConverterFactory(GsonConverterFactory.create(new Gson()))*/
                .addConverterFactory(MyGsonConverter.create(new Gson()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(gethttpClient(ctx))
                .build();
        return retrofit.create(ApiService.class);
    }

    public static ApiService createChecoutGson_WeatherSecurePro(Context ctx) {
        //prefManager = PrefManager.getInstance(ctx);
        context = ctx;
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL_WeatherSecurePro)
                /* .addConverterFactory(ScalarsConverterFactory.create()) //important
                 .addConverterFactory(GsonConverterFactory.create(new Gson()))*/
                .addConverterFactory(MyGsonConverter.create(new Gson()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(gethttpClient(ctx))

                .build();
        return retrofit.create(ApiService.class);
    }

    /*
     * Add header */
    private static class RetrofitHeaderInterceptor implements Interceptor {

        RetrofitHeaderInterceptor() {
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            // String token = Constant.tokenId;//prefManager.getPreference(Constant.TOKEN);
            Request.Builder request = chain.request().newBuilder();
            request.addHeader("Accept", "application/json");
            //   request.addHeader("Authorization", "Bearer " + token);
            Response response = chain.proceed(request.build());
            int tryCount = 0;

            while (!response.isSuccessful() && tryCount < 2) {
                // retry the request
                tryCount++;
                response = chain.proceed(request.build());
            }
            int headerCode = response.code();
            Log.d("ApiFactory", "Response header code - " + headerCode);
            if (headerCode == 401) {
                logout();
            }
            return response;
        }
    }

    private static void logout() {
        //CommonUtils.logout(context);


    }

    /*
     * Add client
     * */
    private static OkHttpClient gethttpClient(Context context) {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.interceptors().add(new RetrofitHeaderInterceptor());
        builder.connectTimeout(30, TimeUnit.SECONDS);
        builder.readTimeout(50, TimeUnit.SECONDS);

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addNetworkInterceptor(new StethoInterceptor());
            builder.addInterceptor(logging);
        }

        return builder.build();
    }

    public static Retrofit getRetrofit() {
        return retrofit;
    }

    public static Cache getCache(Context context) {
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        return new Cache(context.getCacheDir(), cacheSize);
    }




    /*res = res.replace("\":\"[\\\"", "\":\"");
    res = res.replace("\\\"]\"", "\"");
    res = res.replace("\\", "");*/


}
