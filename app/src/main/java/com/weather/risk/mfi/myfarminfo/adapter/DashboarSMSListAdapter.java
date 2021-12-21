package com.weather.risk.mfi.myfarminfo.adapter;

import static com.weather.risk.mfi.myfarminfo.activities.MainProfileActivity.dashboardSMS;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getDynamicLanguageToast;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setDynamicLanguage;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setFontsStyleTxt;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragmentX;
import com.google.android.youtube.player.YouTubePlayerView;
import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.activities.YoutubeVideoPlayerActivity;
import com.weather.risk.mfi.myfarminfo.bean.DashboardSMS;
import com.weather.risk.mfi.myfarminfo.utils.AppManager;
import com.weather.risk.mfi.myfarminfo.utils.TransparentProgressDialog;
import com.weather.risk.mfi.myfarminfo.utils.UtilFonts;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class DashboarSMSListAdapter extends RecyclerView.Adapter<DashboarSMSListAdapter.ViewHolder> {
    private ArrayList<DashboardSMS> mDataset = new ArrayList<DashboardSMS>();
    public Context mContext;
    String FarmID;
    String LastSMSType = "";
    private TextToSpeech mTTS;
    // Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView txtSMS, txtDateTime, txtIstheadvisoryhelpful, txtSMSType;
        TableRow tb_Thumbupdown;
        Button btn_thumbdown, btn_thumbup,audio_sound_btn,audio_sound_btn_mute;
        ImageView sms_image,playbtn;
        RelativeLayout rl;

        public ViewHolder(View v) {
            super(v);
            tb_Thumbupdown = (TableRow) v.findViewById(R.id.tb_Thumbupdown);
            btn_thumbdown = (Button) v.findViewById(R.id.btn_thumbdown);
            btn_thumbup = (Button) v.findViewById(R.id.btn_thumbup);
            txtSMS = (TextView) v.findViewById(R.id.txtSMS);
            txtDateTime = (TextView) v.findViewById(R.id.txtDateTime);
            txtIstheadvisoryhelpful = (TextView) v.findViewById(R.id.txtIstheadvisoryhelpful);
            txtSMSType = (TextView) v.findViewById(R.id.txtSMSType);
            audio_sound_btn = (Button) v.findViewById(R.id.audio_sound_btn);
            audio_sound_btn_mute = (Button) v.findViewById(R.id.audio_sound_btn_mute);
            sms_image = (ImageView) v.findViewById(R.id.sms_image);
            playbtn = (ImageView) v.findViewById(R.id.playbtn);
            rl = (RelativeLayout) v.findViewById(R.id.rl);
        }
    }

      /*public void add(int position, String item) {
          mDataset.add(position, item);
          notifyItemInserted(position);
      }*/

    public void remove(int pos) {
        //   int position = mDataset.indexOf(item);
        mDataset.remove(pos);
        notifyItemRemoved(pos);
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public DashboarSMSListAdapter(Context con, ArrayList<DashboardSMS> Dataset, String FarmIDs) {
        mDataset = Dataset;
        mContext = con;
        FarmID = FarmIDs;

        mTTS = new TextToSpeech(con, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                   // int result = mTTS.setLanguage(Locale.forLanguageTag("hin"));
                    int result = mTTS.setLanguage(new Locale("en", "IN"));
                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "Language not supported");
                    } else {


                    }
                } else {
                    Log.e("TTS", "Initialization failed");
                }
            }
        });
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboarsmslistadapter, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {


        if(!mDataset.get(position).getMediaType().equals("null") || mDataset.get(position).getMediaType() != null){
           holder.rl.setVisibility(View.VISIBLE);
            holder.sms_image.setVisibility(View.VISIBLE);
            if(mDataset.get(position).getMediaType().equals("Image")){
                holder.playbtn.setVisibility(View.GONE);
                Glide.with(mContext).load(mDataset.get(position).getMediaURL()).into(holder.sms_image);
            }else if(mDataset.get(position).getMediaType().equals("Video")){
                holder.playbtn.setVisibility(View.VISIBLE);
               String videourl= mDataset.get(position).getMediaURL();

              String thumbnailurl=  "https://i.ytimg.com/vi/"+videourl.split("=")[1]+"/maxresdefault.jpg";
                Glide.with(mContext).load(thumbnailurl).into(holder.sms_image);

                holder.sms_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                            Intent intent = new Intent(mContext, YoutubeVideoPlayerActivity.class);
                            intent.putExtra("videoId",videourl.split("=")[1]);
                            mContext.startActivity(intent);
                    }
                });

            }else{
                holder.sms_image.setVisibility(View.GONE);
                holder.playbtn.setVisibility(View.GONE);
                holder.rl.setVisibility(View.GONE);
            }

           // Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(mDataset.get(position).getMediaURL(), MediaStore.Video.Thumbnails.MICRO_KIND);

        }else {
            holder.sms_image.setVisibility(View.GONE);
        }



        holder.audio_sound_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Soundspeak(1,holder.txtSMSType,holder.txtSMS);
                holder.audio_sound_btn.setVisibility(View.GONE);
                holder.audio_sound_btn_mute.setVisibility(View.VISIBLE);
            }
        });


        holder.audio_sound_btn_mute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Soundspeak(2, holder.txtSMSType,holder.txtSMS);
                holder.audio_sound_btn.setVisibility(View.VISIBLE);
                holder.audio_sound_btn_mute.setVisibility(View.GONE);
            }
        });

        setFontsStyleTxt(mContext, holder.txtSMSType, 2);
        setFontsStyleTxt(mContext, holder.txtSMS, 6);
        setFontsStyleTxt(mContext, holder.txtDateTime, 6);
        setFontsStyleTxt(mContext, holder.txtIstheadvisoryhelpful, 5);
        setDynamicLanguage(mContext, holder.txtIstheadvisoryhelpful, "Istheadvisoryhelpful", R.string.Istheadvisoryhelpful);

        String SMSType = mDataset.get(position).getMessageType();
        String SMS = mDataset.get(position).getMessage();
        String date = mDataset.get(position).getOutDate();

        if (SMSType != null && SMSType.length() > 0 && !SMSType.equalsIgnoreCase("null")) {
            holder.txtSMSType.setText(SMSType);
            if (position > 0) {
                if (SMSType.equalsIgnoreCase(LastSMSType)) {
                    holder.txtSMSType.setVisibility(View.GONE);
                } else {
                    LastSMSType = SMSType;
                    holder.txtSMSType.setVisibility(View.VISIBLE);
                }
            } else {
                LastSMSType = SMSType;
                holder.txtSMSType.setVisibility(View.VISIBLE);

            }
            holder.txtSMSType.setText(SMSType);
        }
        if (SMS != null && SMS.length() > 0 && !SMS.equalsIgnoreCase("null")) {
            holder.txtSMS.setText(Html.fromHtml(SMS.replaceAll("\\|","\u0964")));
        }

        if (date != null && date.length() > 0 && !date.equalsIgnoreCase("null")) {
            date = date.replace("T", "  ");
            holder.txtDateTime.setText(date);
        }
        String Feedback = mDataset.get(position).getFeedback();
        if (Feedback != null && !Feedback.equalsIgnoreCase("null") && Feedback.length() > 1) {
            holder.tb_Thumbupdown.setVisibility(View.GONE);
        } else {
            holder.tb_Thumbupdown.setVisibility(View.VISIBLE);
        }
        holder.btn_thumbdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getPosition();
                new setFarmRegistrationSave("No", pos, holder.tb_Thumbupdown).execute();
            }
        });
        holder.btn_thumbup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getPosition();
                new setFarmRegistrationSave("Yes", pos, holder.tb_Thumbupdown).execute();
            }
        });


    }

    //Audio Sound Display
    private void Soundspeak(int ONOFF_Flag,TextView title, TextView des) {

//        float pitch = (float) seek_bar_pitch.getProgress() / 50;0.66
        float pitch = 0.66f;
        if (pitch < 0.1) pitch = 0.1f;
//        float speed = (float) seek_bar_speed.getProgress() / 50;.96
        float speed = 0.85f;
        if (speed < 0.1) speed = 0.1f;

        mTTS.setPitch(pitch);
        mTTS.setSpeechRate(speed);
        String value = title.getText().toString() + " \n  " + des.getText().toString();
        String ReadingText =  value.replace(".", " \n ").toString().replace(",", " \n ").toString();;
        if (ONOFF_Flag == 1)
            mTTS.speak(ReadingText, TextToSpeech.QUEUE_FLUSH, null);
        else if (ONOFF_Flag == 2)
            mTTS.stop();
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    private class setFarmRegistrationSave extends AsyncTask<Void, Void, String> {

        TransparentProgressDialog progressDialog;
        String Status = "";
        int position = 0;
        TableRow tb_Thumbupdown;

        public setFarmRegistrationSave(String status, int pos, TableRow tableRow) {
            Status = status;
            position = pos;
            tb_Thumbupdown = tableRow;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new TransparentProgressDialog(
                    mContext, mContext.getResources().getString(R.string.Dataisloading));
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @Override
        protected String doInBackground(Void... params) {
            String response = null;
            String URL = AppManager.getInstance().UploadNotificationDataURL;//Herojit Comment
            JSONObject object = getjsonvalueUpload(position, Status);
            String jsonvalue = object.toString();

            response = AppManager.getInstance().httpRequestPutMethod(URL, jsonvalue);
            return "{" + response + "}";
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPreExecute();
            progressDialog.dismiss();
            try {
                if (response.contains("SaveNotifyResponseResult")) {
                    JSONObject obj = new JSONObject(response);
                    String res = obj.getString("SaveNotifyResponseResult");
                    if (res != null && res.equalsIgnoreCase("Response Save Sucessfully")) {
                        DashboardSMS dash = new DashboardSMS();
                        String Feedback = "Completed";
                        dash.setId(mDataset.get(position).getId());
                        dash.setMessage(mDataset.get(position).getMessage());
                        dash.setMessageType(mDataset.get(position).getMessageType());
                        dash.setOutDate(mDataset.get(position).getOutDate());
                        dash.setFeedback(Feedback);
                        dash.setFoundflag(mDataset.get(position).getFoundflag());
                        dashboardSMS.set(position, dash);
                        tb_Thumbupdown.setVisibility(View.GONE);
                       // Toast.makeText(mContext, mContext.getResources().getString(R.string.SubmittedSuccessfully), Toast.LENGTH_LONG).show();
                        getDynamicLanguageToast(mContext,"thankyouforfeedback",R.string.thankyouforfeedback);
                    } else {
                        Toast.makeText(mContext, mContext.getResources().getString(R.string.FormattingError), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(mContext, mContext.getResources().getString(R.string.ServerError), Toast.LENGTH_LONG).show();
                }
            } catch (Exception ex) {

                Toast.makeText(mContext, mContext.getResources().getString(R.string.FormattingError), Toast.LENGTH_LONG).show();
                ex.printStackTrace();
            }
        }
    }

    public JSONObject getjsonvalueUpload(int pos, String Status) {

        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject.put("NotifyResponseMessage", mDataset.get(pos).getMessage());
            jsonObject.put("FarmID", FarmID);
            jsonObject.put("stepID", mDataset.get(pos).getId());
            jsonObject.put("ImagePath", "");
            //Add New Field
            jsonObject.put("Status", Status);
            jsonObject1 = new JSONObject();
            jsonObject1.put("notifyresponse", jsonObject.toString());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return jsonObject1;
    }

}
