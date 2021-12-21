package com.weather.risk.mfi.myfarminfo.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.annotation.Nullable;

import com.weather.risk.mfi.myfarminfo.activities.MainProfileActivity;
import com.weather.risk.mfi.myfarminfo.utils.AppManager;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class CallRecordingService extends Service {

    private static final String IMAGE_DIRECTORY = "/SecurFarm";
    private MediaRecorder mediaRecorder;
    private boolean recordstarted;
    private File file;
    String path = "";

    //Call Recording varibales
    private static final String AUDIO_RECORDER_FILE_EXT_3GP = ".3gp";
    private static final String AUDIO_RECORDER_FILE_EXT_MP4 = ".mp4";
    private static final String AUDIO_RECORDER_FOLDER = "AudioRecorder";

//    private MediaRecorder recorder = null;
//    private int currentFormat = 0;
//    private int output_formats[] = {MediaRecorder.OutputFormat.MPEG_4,
//            MediaRecorder.OutputFormat.THREE_GPP};
//    private String file_exts[] = {AUDIO_RECORDER_FILE_EXT_MP4,
//            AUDIO_RECORDER_FILE_EXT_3GP};
//
//    AudioManager audioManager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    String AudioSavePathInDevice = null;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        super.onStartCommand(intent, flags, startId);

        try {
            file = new File(Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
            if (!file.exists()) {
                Log.d("dirrrrrr", "" + file.mkdirs());
                file.mkdirs();
            }
            String filename = AppManager.getInstance().getCreateCallRecord();
            mediaRecorder = new MediaRecorder();

//            audioManager = (AudioManager) getApplicationContext().getSystemService(getApplicationContext().AUDIO_SERVICE);
//            audioManager.setMode(AudioManager.MODE_IN_CALL);
//            audioManager.setSpeakerphoneOn(true);
//
//            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);
//            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
//            //recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
//            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
//            mediaRecorder.setOutputFile(file.getAbsolutePath() + "/" + filename);
//            mediaRecorder.setOnErrorListener(errorListener);
//            mediaRecorder.setOnInfoListener(infoListener);


            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
            mediaRecorder.setOutputFile(file.getAbsolutePath() + "/" + filename);
            TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(getApplicationContext().TELEPHONY_SERVICE);
            telephonyManager.listen(new PhoneStateListener() {
                @Override
                public void onCallStateChanged(int state, String incomingNumber) {
//                super.onCallStateChanged(state, incomingNumber) {
                    try {
                        if (TelephonyManager.CALL_STATE_IDLE == state) {
                            //Went to idle-  this is the end of a call.  What type depends on previous state(s)
                            /**Device call state: Ringing. A new call arrived and is ringing or waiting. In the
                             * latter case, another call is already active.*/

                            try {
                                mediaRecorder.stop();
                                recordstarted = false;
//                            startRecording();
                            } catch (IllegalStateException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                            onDestroy();
                        } else if (TelephonyManager.CALL_STATE_OFFHOOK == state) {
                            try {
                                mediaRecorder.prepare();
                                mediaRecorder.start();
                                recordstarted = true;
//                            startRecording();
                            } catch (IllegalStateException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        } else if (TelephonyManager.CALL_STATE_RINGING == state) {
                            /** Device call state: Off-hook. At least one call exists that is dialing, active, or on hold,
                             * and no calls are ringing or waiting.*/
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }, PhoneStateListener.LISTEN_CALL_STATE);


        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Service.START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("EXIT", "ondestroy!");

    }


}
