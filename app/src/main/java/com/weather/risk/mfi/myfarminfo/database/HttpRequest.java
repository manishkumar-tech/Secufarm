package com.weather.risk.mfi.myfarminfo.database;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * <p>
 * Copyright © 2017, Calculistik . All rights reserved.
 * <p>
 * Oracle and Java are registered trademarks of Oracle and/or its
 * affiliates. Other names may be trademarks of their respective owners.
 * <p>
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common
 * Development and Distribution License("CDDL") (collectively, the
 * "License"). You may not use this file except in compliance with the
 * License. You can obtain a copy of the License at
 * https://netbeans.org/cddl-gplv2.html or
 * nbbuild/licenses/CDDL-GPL-2-CP. See the License for the specific
 * language governing permissions and limitations under the License.
 * When distributing the software, include this License Header
 * Notice in each file and include the License file at
 * nbbuild/licenses/CDDL-GPL-2-CP. Oracle designates this particular file
 * as subject to the "Classpath" exception as provided by Oracle in the
 * GPL Version 2 section of the License file that accompanied this code. If
 * applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * <p>
 * Contributor(s):
 * Created by alejandro tkachuk @aletkachuk
 * www.calculistik.com
 */
public class HttpRequest {

    public static enum Method {
        POST, PUT, DELETE, GET;
    }

    private URL url;
    private HttpURLConnection connection;
    private OutputStream outputStream;
    private HashMap<String, String> params = new HashMap<String, String>();

    public HttpRequest(String url) throws IOException {
        this.url = new URL(url);
        connection = (HttpURLConnection) this.url.openConnection();
    }

    public int get() throws IOException {
        return this.send();
    }

    public int post(String data) throws IOException {
        connection.setDoInput(true);
        connection.setRequestMethod(Method.POST.toString());
        connection.setDoOutput(true);
        outputStream = connection.getOutputStream();
        this.sendData(data);
        return this.send();
    }

    public int post() throws IOException {
        connection.setDoInput(true);
        connection.setRequestMethod(Method.POST.toString());
        connection.setDoOutput(true);
        outputStream = connection.getOutputStream();
        return this.send();
    }

    public int put(String data) throws IOException {
        connection.setDoInput(true);
        connection.setRequestMethod(Method.PUT.toString());
        connection.setDoOutput(true);
        outputStream = connection.getOutputStream();
        this.sendData(data);
        return this.send();
    }

    public int put() throws IOException {
        connection.setDoInput(true);
        connection.setRequestMethod(Method.PUT.toString());
        connection.setDoOutput(true);
        outputStream = connection.getOutputStream();
        return this.send();
    }

    public HttpRequest addHeader(String key, String value) {
        connection.setRequestProperty(key, value);
        return this;
    }

    public HttpRequest addParameter(String key, String value) {
        this.params.put(key, value);
        return this;
    }

    public JSONObject getJSONObjectResponse() throws JSONException, IOException {
        return new JSONObject(getStringResponse());
    }

    public JSONArray getJSONArrayResponse() throws JSONException, IOException {
        return new JSONArray(getStringResponse());
    }

    public String getStringResponse() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        for (String line; (line = br.readLine()) != null; ) response.append(line + "\n");
        return response.toString();
    }

    public byte[] getBytesResponse() throws IOException {
        byte[] buffer = new byte[8192];
        InputStream is = connection.getInputStream();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        for (int bytesRead; (bytesRead = is.read(buffer)) >= 0; )
            output.write(buffer, 0, bytesRead);
        return output.toByteArray();
    }

    public void close() {
        if (null != connection)
            connection.disconnect();
    }

    private int send() throws IOException {
        int httpStatusCode = HttpURLConnection.HTTP_BAD_REQUEST;

        if (!this.params.isEmpty()) {
            this.sendData();
        }
        httpStatusCode = connection.getResponseCode();

        return httpStatusCode;
    }

    private void sendData() throws IOException {
        StringBuilder result = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            result.append((result.length() > 0 ? "&" : "") + entry.getKey() + "=" + entry.getValue());//appends: key=value (for first param) OR &key=value(second and more)
        }
        sendData(result.toString());
    }

    private HttpRequest sendData(String query) throws IOException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
        writer.write(query);
        writer.close();
        return this;
    }

}
