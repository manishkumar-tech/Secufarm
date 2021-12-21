package com.weather.risk.mfi.myfarminfo.retofitservices;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.Buffer;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class MyJsonConverterCropSche extends Converter.Factory {

    public static MyJsonConverterCropSche create() {
        return create(new Gson());
    }

    public static MyJsonConverterCropSche create(Gson gson) {
        return new MyJsonConverterCropSche(gson);
    }

    private final Gson gson;

    private MyJsonConverterCropSche(Gson gson) {
        if (gson == null) throw new NullPointerException("gson == null");
        this.gson = gson;
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations,
                                                            Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new GsonResponseBodyConverter<>(gson, adapter);
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type,
                                                          Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new GsonRequestBodyConverter<>(gson, adapter);
    }


    final class GsonRequestBodyConverter<T> implements Converter<T, RequestBody> {
        private final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");
        private final Charset UTF_8 = Charset.forName("UTF-8");

        private final Gson gson;
        private final TypeAdapter<T> adapter;

        GsonRequestBodyConverter(Gson gson, TypeAdapter<T> adapter) {
            this.gson = gson;
            this.adapter = adapter;
        }

        @Override
        public RequestBody convert(T value) throws IOException {
            Buffer buffer = new Buffer();
            Writer writer = new OutputStreamWriter(buffer.outputStream(), UTF_8);
            JsonWriter jsonWriter = gson.newJsonWriter(writer);
            adapter.write(jsonWriter, value);
            jsonWriter.close();
            return RequestBody.create(MEDIA_TYPE, buffer.readByteString());
        }
    }

    final class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
        private final Gson gson;
        private final TypeAdapter<T> adapter;

        GsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
            this.gson = gson;
            this.adapter = adapter;
        }

        @Override
        public T convert(ResponseBody value) throws IOException {
            String dirty = value.string();
            String jsonvalue = dirty.replace("\\\\\\\\\\\\\\", "\\");
            jsonvalue = jsonvalue.replace("\\\\\\", "\\");
            jsonvalue = jsonvalue.replace("\\", "");
            jsonvalue = jsonvalue.replace("\"[", "[");
            jsonvalue = jsonvalue.replace("]\"", "]");

            jsonvalue = jsonvalue.replace("\"{", "{");
            jsonvalue = jsonvalue.replace("}\"", "}");

            try {
                JSONArray jsonArray = new JSONArray(jsonvalue);
                JSONArray jsonArraynew = new JSONArray();
                for (int i = 0; i < jsonArray.length(); i++) {
                    String str1 = jsonArray.getJSONObject(i).getString("Str1");
                    JSONArray str2 = jsonArray.getJSONObject(i).getJSONArray("Str2");
                    JSONObject obj = new JSONObject();
                    obj.put("Str1", str1);
                    if (str1.equalsIgnoreCase("Status")) {
                        JSONArray abc = new JSONArray();
                        for (int j = 0; j < str2.length(); j++) {
                            JSONObject ob = new JSONObject();
                            ob.put("CustomCreateID", str2.getString(j));
                            abc.put(ob);
                        }
                        obj.put("Str2", abc);
                    } else {
                        obj.put("Str2", str2);
                    }
                    jsonArraynew.put(obj);
                }
                jsonvalue = jsonArraynew.toString();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            jsonvalue = jsonvalue.replace("\\\\\\", "\\");
            jsonvalue = jsonvalue.replace("\\", "");
            jsonvalue = jsonvalue.replace("\"[", "[");
            jsonvalue = jsonvalue.replace("]\"", "]");

            jsonvalue = jsonvalue.replace("\"{", "{");
            jsonvalue = jsonvalue.replace("}\"", "}");

            Log.v("wvwevwvwe", "" + jsonvalue);
            try {
                return adapter.fromJson(jsonvalue);
            } finally {
                value.close();
            }
        }
    }


}
