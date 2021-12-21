package com.weather.risk.mfi.myfarminfo.adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.entities.VoiceMessageBean;
import com.weather.risk.mfi.myfarminfo.utils.AppManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Admin on 25-07-2017.
 */
public class MessageAdapter  extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private ArrayList<VoiceMessageBean> mDataset = new ArrayList<VoiceMessageBean>();

    public Context mContext;
    String imageString;
    TextToSpeech t1;
    public String village_Id;

    // Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView expenseType;
        public Button btn;

        public Button sendBTN, cancelBTN;

        TextView date;


        public ViewHolder(View v) {
            super(v);
            expenseType = (TextView) v.findViewById(R.id.new_text);
            btn = (Button) v.findViewById(R.id.voice_btn);
            date = (TextView) v.findViewById(R.id.advisory_date);

            sendBTN = (Button) v.findViewById(R.id.send_sms);
            cancelBTN = (Button) v.findViewById(R.id.cancel_sms);
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
    public MessageAdapter(Context con, ArrayList<VoiceMessageBean> myDataset, String vlid) {
        mDataset = myDataset;
        mContext = con;

        village_Id= vlid;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.voice_list, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {


        // holder.setIsRecyclable(false);


        holder.expenseType.setText(mDataset.get(position).getMessageText());
        holder.date.setText(mDataset.get(position).getScheduleDate());

        Log.v("slkas", mDataset.get(position) + "");

        t1 = new TextToSpeech(mContext, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    t1.setLanguage(new Locale("hin"));
                }
            }
        });


        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // final Dialog dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Voice Message");
                builder.setMessage("શું તમે સંદેશ સાંભળવા માંગો છો?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();


                        String strrrr = mDataset.get(position).getMessageText();
                        String OutputString = null;

                       /* GoogleAPI.setHttpReferrer("https://code.google.com/p/google-api-translate-java/");


                        try {
                            OutputString = Translate.execute(toSpeak, Language.ENGLISH, Language.HINDI);
                        }catch (Exception e){

                            System.out.println(e.getMessage());
                        }

                        Log.v("klklklk",OutputString+"");
*/
                        // Toast.makeText(getApplicationContext(), toSpeak,Toast.LENGTH_SHORT).show();

                        //    String strrrr = "महत्वपूर्ण सूचना:प्रिय किसान भाइयों मौसम के अनुसार सफेद मक्खी (White fly) आने की परिस्थितियां बनी हुई है. यदि आप ने सात-दस दिन तक कोई छिडकाव नहीं किया है तो  इस रोग से फसल को बचाने के लिए निम्न रसायनों  में से किसी एक का इस्तेमाल करें।  Ulala @ 80 gram /Acre या  Lancer Gold + Phoskil या Renova + Josh का 120 लीटर पानी में मिला कर छिड़काव करे। WRMS-Unimart\n";

                        t1.setSpeechRate(0.7f);

                        t1.speak(strrrr, TextToSpeech.QUEUE_FLUSH, null);
                    }
                });
                builder.show();
            }
        });


        holder.sendBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strrrr = mDataset.get(position).getMessageId();
                String jsonParameterString = createJsonParameterForSend(strrrr,  village_Id);
                String createdString = AppManager.getInstance().removeSpaceForUrl(jsonParameterString);
                new sendMessage(createdString,position).execute();
            }
        });

        holder.cancelBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strrrr = mDataset.get(position).getMessageId();
                String jsonParameterString = createJsonParameterForCancel(strrrr,  village_Id);
                String createdString = AppManager.getInstance().removeSpaceForUrl(jsonParameterString);
                new sendMessage(createdString,position).execute();
            }
        });

    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    private class sendMessage extends AsyncTask<Void, Void, String> {

        String result = null;
        String createdString;
        int pos;

        public sendMessage(String createdString,int posi) {
            this.createdString = createdString;
            pos = posi;
        }

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(mContext);
            progressDialog.setMessage("Processing . . ");
            progressDialog.setCancelable(true);
            progressDialog.setIndeterminate(true);
            progressDialog.show();

        }

        @Override
        protected String doInBackground(Void... params) {
            String response = null;
            String sendPath = AppManager.getInstance().getAdvisoryUpdate;

            response = AppManager.getInstance().httpRequestPutMethodReport(sendPath, createdString);


            System.out.println("AllFarmResponse :---" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPreExecute();
            progressDialog.dismiss();
            try {
                response = response.trim();
                // response = response.substring(1, response.length() - 1);
                response = response.replace("\\", "");
                response = response.replace("\"[", "[");
                response = response.replace("]\"", "]");
                response = response.replace("\"{", "{");
                response = response.replace("}\"", "}");
                if (response!=null){
                    remove(pos);
                }


            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(mContext, "Response Formatting Error", Toast.LENGTH_LONG).show();
            }


        }


    }


    private class cancelMessage extends AsyncTask<Void, Void, String> {

        String result = null;
        String createdString;

        public cancelMessage(String createdString) {
            this.createdString = createdString;
        }

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(mContext);
            progressDialog.setMessage("Processing . . ");
            progressDialog.setCancelable(true);
            progressDialog.setIndeterminate(true);
            progressDialog.show();

        }

        @Override
        protected String doInBackground(Void... params) {
            String response = null;
            String sendPath = AppManager.getInstance().getAdvisoryReport;

            response = AppManager.getInstance().httpRequestPutMethodReport(sendPath, createdString);


            System.out.println("AllFarmResponse :---" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPreExecute();
            progressDialog.dismiss();
            try {
                response = response.trim();
                // response = response.substring(1, response.length() - 1);
                response = response.replace("\\", "");
                response = response.replace("\"[", "[");
                response = response.replace("]\"", "]");
                response = response.replace("\"{", "{");
                response = response.replace("}\"", "}");
                System.out.println("All_Advisory_Response : " + response);
                JSONObject jb = new JSONObject(response);
                JSONObject jsonObject = jb.getJSONObject("getGGRCSMSDataResult");
                JSONArray jsonArray = jsonObject.getJSONArray("DTLegends");

                ArrayList<VoiceMessageBean> msgList = new ArrayList<VoiceMessageBean>();


                for (int i = 0; i < jsonArray.length(); i++) {
                    VoiceMessageBean messageBean = new VoiceMessageBean();
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                    messageBean.setMessageId(jsonObject1.getString("ID"));
                    messageBean.setMessageText(jsonObject1.getString("Message"));

                    messageBean.setVillageId(jsonObject1.getString("VillageID"));
                    messageBean.setScheduleDate(jsonObject1.getString("ScheduleDate"));
                    messageBean.setStatus(jsonObject1.getString("Status"));
                    messageBean.setMessageType(jsonObject1.getString("MessageType"));
                    msgList.add(messageBean);
                }


            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(mContext, "Response Formatting Error", Toast.LENGTH_LONG).show();
            }
        }
    }

    private String createJsonParameterForSend(String sms_id, String vil_id) {
        String parameterString = "";


        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("BlockID", "Village");
            jsonObject.put("DistrictID", vil_id);
            jsonObject.put("MessageID", sms_id);
            jsonObject.put("MessageType", "0");
            jsonObject.put("Status", "Approve");
            jsonObject.put("VillageID", vil_id);


            Log.v("user_id", jsonObject.toString() + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        parameterString = jsonObject.toString();

        return parameterString;
    }

    private String createJsonParameterForCancel(String sms_id, String vil_id) {
        String parameterString = "";


        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("BlockID", "Village");
            jsonObject.put("DistrictID", vil_id);
            jsonObject.put("MessageID", sms_id);
            jsonObject.put("MessageType", "0");
            jsonObject.put("Status", "Cancel");
            jsonObject.put("VillageID", vil_id);


            Log.v("user_id", jsonObject.toString() + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        parameterString = jsonObject.toString();

        return parameterString;
    }


}