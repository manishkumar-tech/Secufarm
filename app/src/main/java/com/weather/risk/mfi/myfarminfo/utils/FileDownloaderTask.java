package com.weather.risk.mfi.myfarminfo.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.PowerManager;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.FOLDER_NAME;


public class FileDownloaderTask extends AsyncTask<String, Integer, String> {
    String url;
    String nameFile;
    ProgressDialog mProgressDialog;
    private String nameFileDownload;
    private PowerManager.WakeLock mWakeLock;
    private static FileDownloaderTask task;
    private WeakReference<Context> contextweakreference;
    private WeakReference<OnDownloadListeners> listenersrefrence;




    public static FileDownloaderTask getInstance(Context ctx, String url , String name){
        return task==null ? new FileDownloaderTask(ctx,url,name) : task;
    }




    public void setListeners(OnDownloadListeners listeners){

        listenersrefrence = new WeakReference<>(listeners);
    }

    public FileDownloaderTask(Context context, String ur, String nn) {
        contextweakreference = new WeakReference<>(context);

        this.url = ur;
        this.nameFile = nn;

        mProgressDialog = new ProgressDialog(contextweakreference.get());
        mProgressDialog.setMessage("Downloading...");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(true);

        mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                //  DownloadFile.cancel(true); //cancel the task
                mProgressDialog.cancel();

            }
        });

    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // take CPU lock to prevent CPU from going off if the user
        // presses the power button during download
        PowerManager pm = (PowerManager) contextweakreference.get().getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                getClass().getName());
        mWakeLock.acquire();
        mProgressDialog.show();
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        super.onProgressUpdate(progress);
        // if we get here, length is known, now set indeterminate to false
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setMax(100);
        mProgressDialog.setProgress(progress[0]);
    }



    @Override
    protected String doInBackground(String... sUrl) {
        InputStream input = null;
        OutputStream output = null;
        HttpURLConnection connection = null;
        try {
            URL url = new URL(sUrl[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            // expect HTTP 200 OK, so we don't mistakenly save error report
            // instead of the file
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return "Server returned HTTP " + connection.getResponseCode()
                        + " " + connection.getResponseMessage();
            }

            // this will be useful to display download percentage
            // might be -1: server did not report the length
            int fileLength = connection.getContentLength();

            // download the file
            input = connection.getInputStream();
            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            File folder = new File(extStorageDirectory, FOLDER_NAME);
            folder.mkdir();
            File pdfFile = new File(folder, nameFile);
            output = new FileOutputStream(pdfFile);
            nameFileDownload = nameFile;

            byte data[] = new byte[4096];
            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {
                // allow canceling with back button
                if (isCancelled()) {
                    input.close();
                    return null;
                }
                total += count;
                // publishing the progress....
                if (fileLength > 0) // only if total length is known
                    publishProgress((int) (total * 100 / fileLength));
                output.write(data, 0, count);
            }
        } catch (Exception e) {
            return e.toString();
        } finally {
            try {
                if (output != null)
                    output.close();
                if (input != null)
                    input.close();
            } catch (IOException ignored) {
            }

            if (connection != null)
                connection.disconnect();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        mWakeLock.release();
        mProgressDialog.dismiss();
        if (result != null) {
            Toast.makeText(contextweakreference.get(), "Download error: " + result, Toast.LENGTH_LONG).show();
        } else {
            //Toast.makeText(context, "File downloaded", Toast.LENGTH_SHORT).show();
            if (nameFileDownload != null && listenersrefrence.get()!=null) {
                listenersrefrence.get().onDownloadSuccess(nameFileDownload);
            }
        }
    }



}

