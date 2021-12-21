package com.weather.risk.mfi.myfarminfo.pdfdownload;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.ContextThemeWrapper;

import com.weather.risk.mfi.myfarminfo.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.weather.risk.mfi.myfarminfo.customcamera.CameraSurfaceView.IMAGE_DIRECTORY;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getDynamicLanguageValue;

public class PDFDownloadTask {
    private static final String TAG = "Download Task";
    private Context context;

    private String downloadUrl = "", downloadFileName = "";
    private ProgressDialog progressDialog;
    int flags = 0;

    public PDFDownloadTask(Context context, String downloadUrl, int flag) {
        this.context = context;
        this.downloadUrl = downloadUrl;
        this.flags = flag;

        downloadFileName = downloadUrl.substring(downloadUrl.lastIndexOf('/'), downloadUrl.length());//Create file name by picking download file name from URL
        Log.e(TAG, downloadFileName);

        //Start Downloading Task
        new DownloadingTask().execute();
    }

    private class DownloadingTask extends AsyncTask<Void, Void, Void> {

        File apkStorage = null;
        File outputFile = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage(getDynamicLanguageValue(context, "FileDownloading", R.string.FileDownloading));
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                if (outputFile != null) {
                    progressDialog.dismiss();

                    if (flags == 0) {
                        ContextThemeWrapper ctw = new ContextThemeWrapper(context, R.style.popupMenuStyle);
                        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctw);
                        alertDialogBuilder.setTitle(getDynamicLanguageValue(context, "PolicyDocument", R.string.PolicyDocument));
                        alertDialogBuilder.setMessage(getDynamicLanguageValue(context, "Documentdownloaded", R.string.Documentdownloaded));
                        alertDialogBuilder.setCancelable(false);
                        alertDialogBuilder.setPositiveButton(getDynamicLanguageValue(context, "Ok", R.string.Ok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });
                        alertDialogBuilder.show();
                    } else if (flags == 1) {

//                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
//                StrictMode.setVmPolicy(builder.build());

//                File pdfFile = new File(Environment.getExternalStoragePublicDirectory
//                        (Environment.DIRECTORY_DOWNLOADS), "Your file");
                        try {
                            String local = apkStorage + "" + downloadFileName;
                            Uri uri = Uri.parse(local);
                            //Uri uri = Uri.fromFile(outputFile);
                            String packageName[] = {"com.whatsapp"};
                            Intent shareIntent = new Intent();
                            shareIntent.setAction(Intent.ACTION_SEND);
                            shareIntent.setType("application/pdf");
                            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
//                            shareIntent.setPackage("com.whatsapp");
                            context.startActivity(Intent.createChooser(shareIntent, "Share Docs"));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }


//                    alertDialogBuilder.setNegativeButton(context.getResources().getString(R.string.OpenDocument), new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            try {
//                                File pdfFile = new File(Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY + downloadFileName);
////                            File pdfFile = new File(Environment.getExternalStorageDirectory() + "/CodePlayon/" + downloadFileName);  // -> filename = maven.pdf
//                                Uri path = Uri.fromFile(pdfFile);
//                                Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
//                                pdfIntent.setDataAndType(path, "application/pdf");
//                                pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                try {
//                                    context.startActivity(pdfIntent);
//                                } catch (ActivityNotFoundException e) {
//                                    Toast.makeText(context, context.getResources().getString(R.string.NoApplicationavailable), Toast.LENGTH_SHORT).show();
//                                }
//                            } catch (Exception ex) {
//                                ex.printStackTrace();
//                            }
//                        }
//                    });

//                    Toast.makeText(context, "Document Downloaded Successfully", Toast.LENGTH_SHORT).show();
                } else {

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                        }
                    }, 3000);

                    Log.e(TAG, "Download Failed");

                }
            } catch (Exception e) {
                e.printStackTrace();

                //Change button text if exception occurs

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                    }
                }, 3000);
                Log.e(TAG, "Download Failed with Exception - " + e.getLocalizedMessage());

            }


            super.onPostExecute(result);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                URL url = new URL(downloadUrl);//Create Download URl
                HttpURLConnection c = (HttpURLConnection) url.openConnection();//Open Url Connection
                c.setRequestMethod("GET");//Set Request Method to "GET" since we are grtting data
                c.connect();//connect the URL Connection

                //If Connection response is not OK then show Logs
                if (c.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    Log.e(TAG, "Server returned HTTP " + c.getResponseCode()
                            + " " + c.getResponseMessage());

                }

//                //Get File if SD card is present
//                if (new CheckForSDCard().isSDCardPresent()) {
//
//                    apkStorage = new File(Environment.getExternalStorageDirectory() + "/" + "CodePlayon");
//                } else
//                    Toast.makeText(context, "Oops!! There is no SD Card.", Toast.LENGTH_SHORT).show();
                apkStorage = new File(Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
                //If File is not present create directory
                if (!apkStorage.exists()) {
                    apkStorage.mkdir();
                    Log.e(TAG, "Directory Created.");
                }

                outputFile = new File(apkStorage, downloadFileName);//Create Output file in Main File

                //Create New File if not present
                if (!outputFile.exists()) {
                    outputFile.createNewFile();
                    Log.e(TAG, "File Created");
                }

                FileOutputStream fos = new FileOutputStream(outputFile);//Get OutputStream for NewFile Location

                InputStream is = c.getInputStream();//Get InputStream for connection

                byte[] buffer = new byte[1024];//Set buffer type
                int len1 = 0;//init length
                while ((len1 = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len1);//Write new file
                }

                //Close all connection after doing task
                fos.close();
                is.close();

            } catch (Exception e) {

                //Read exception if something went wrong
                e.printStackTrace();
                outputFile = null;
                Log.e(TAG, "Download Error Exception " + e.getMessage());
            }

            return null;
        }
    }
}