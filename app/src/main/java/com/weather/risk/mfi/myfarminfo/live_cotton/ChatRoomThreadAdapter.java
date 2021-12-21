package com.weather.risk.mfi.myfarminfo.live_cotton;

import android.app.Dialog;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.utils.AppConstant;
import com.weather.risk.mfi.myfarminfo.utils.UtilFonts;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.weather.risk.mfi.myfarminfo.utils.AppManager.URLdomain;
import static com.weather.risk.mfi.myfarminfo.utils.AppManager.URLdomain_PlantDocImage;


public class ChatRoomThreadAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static String TAG = ChatRoomThreadAdapter.class.getSimpleName();


    private int SELF = 100;
    private static String today;


    private Context mContext;
    private ArrayList<MessageBean> messageArrayList;
    MediaPlayer player = new MediaPlayer();

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView message, timestamp;
        ImageView image, image2;
        ImageView audioPlay, btn_stop;
        LinearLayout hideRow;


        public ViewHolder(View view) {
            super(view);
            message = (TextView) itemView.findViewById(R.id.message);
            timestamp = (TextView) itemView.findViewById(R.id.timestamp);
            image = (ImageView) itemView.findViewById(R.id.image);
            image2 = (ImageView) itemView.findViewById(R.id.image2);
            audioPlay = (ImageView) itemView.findViewById(R.id.audio_play);
            btn_stop = (ImageView) itemView.findViewById(R.id.btn_stop);
            hideRow = (LinearLayout) itemView.findViewById(R.id.aaaaa);
        }
    }


    public ChatRoomThreadAdapter(Context mContext, ArrayList<MessageBean> messageArrayList) {
        this.mContext = mContext;
        this.messageArrayList = messageArrayList;

        Calendar calendar = Calendar.getInstance();
        today = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;

        // view type is to identify where to render the chat message
        // left or right
        if (viewType == SELF) {
            // self message
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_self, parent, false);
        } else {
            // others message
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_other, parent, false);
        }


        return new ViewHolder(itemView);
    }


    @Override
    public int getItemViewType(int position) {
        MessageBean message = messageArrayList.get(position);
        String user = AppConstant.visible_Name;
        if (message.getUser().equalsIgnoreCase(user)) {
            return SELF;
        }

        return position;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final MessageBean msg = messageArrayList.get(position);

        UtilFonts.UtilFontsInitialize(mContext);
        ((ViewHolder) holder).message.setTypeface(UtilFonts.KT_Light);
        ((ViewHolder) holder).timestamp.setTypeface(UtilFonts.KT_Medium);

        String msggg = msg.getMessage();
        if (msggg.contains("? n")) {
//            msggg = msggg.replace("? n", "? \\n ");
            String sms[] = msggg.split("\\? n");
            msggg = sms[0] + "?" + "\n" + sms[1];
        }
        if (msggg.equalsIgnoreCase("Voice_Message")) {
            ((ViewHolder) holder).hideRow.setVisibility(View.GONE);
        } else {
            ((ViewHolder) holder).hideRow.setVisibility(View.VISIBLE);
        }

//        ((ViewHolder) holder).message.setText(msg.getMessage());
        ((ViewHolder) holder).message.setText(msggg);
        String timestamp = msg.getMsgDate();
        String sss = null;

        if (timestamp != null) {

            List<String> dateList = Arrays.asList(timestamp.split("T"));
            if (dateList.size() > 1) {
                sss = dateList.get(0) + " & " + dateList.get(1);
            }

        }
        if (msg.getUser() != null)
            timestamp = msg.getUser() + ", " + sss;

        ((ViewHolder) holder).timestamp.setText(timestamp);

        String img = msg.getImage();
        String img2 = msg.getImage2();
        if (img != null && img.length() > 4) {
            Log.v("imagellll", img + "--" + img.length());
            ((ViewHolder) holder).image.setVisibility(View.VISIBLE);
//            String imagePath = URLdomain + "LogImage/" + img;
            String imagePath = URLdomain_PlantDocImage + img;
            Picasso.with(mContext).load(imagePath).into(((ViewHolder) holder).image);
        } else {
            ((ViewHolder) holder).image.setVisibility(View.GONE);
        }

        if (img2 != null && img2.length() > 4) {
            Log.v("imagellll", img2 + "--" + img2.length());
            ((ViewHolder) holder).image2.setVisibility(View.VISIBLE);
//            String imagePath = URLdomain + "LogImage/" + img2;
            String imagePath = URLdomain_PlantDocImage + img2;
            Picasso.with(mContext).load(imagePath).into(((ViewHolder) holder).image2);
        } else {
            ((ViewHolder) holder).image2.setVisibility(View.GONE);
        }


        final String aud = msg.getAudioPath();
        if (aud != null && aud.length() > 4) {
            player.stop();
            player = new MediaPlayer();
            ((ViewHolder) holder).audioPlay.setVisibility(View.VISIBLE);
            ((ViewHolder) holder).btn_stop.setVisibility(View.GONE);
        } else {
            player.stop();
            player = new MediaPlayer();
            ((ViewHolder) holder).audioPlay.setVisibility(View.GONE);
            ((ViewHolder) holder).btn_stop.setVisibility(View.GONE);
        }

        ((ViewHolder) holder).audioPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (aud != null) {
                        String audPath = URLdomain + "LogVoice/" + aud;
                        Log.v("audioPath", audPath + "--" + audPath.length());

                        player = new MediaPlayer();
                        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        player.setDataSource(audPath);
                        player.prepare();
                        player.start();

                        ((ViewHolder) holder).audioPlay.setVisibility(View.GONE);
                        ((ViewHolder) holder).btn_stop.setVisibility(View.VISIBLE);

                    }
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        });

        ((ViewHolder) holder).btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (aud != null) {
                        String audPath = URLdomain + "LogVoice/" + aud;
                        Log.v("audioPath", audPath + "--" + audPath.length());

                        player.stop();
                        player = new MediaPlayer();
                        ((ViewHolder) holder).audioPlay.setVisibility(View.VISIBLE);
                        ((ViewHolder) holder).btn_stop.setVisibility(View.GONE);

                    }
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        });


        ((ViewHolder) holder).image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(mContext);

                dialog.setCanceledOnTouchOutside(true);
                Window window = dialog.getWindow();
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);


                WindowManager.LayoutParams wlp = window.getAttributes();

                wlp.gravity = Gravity.BOTTOM;
                wlp.dimAmount = 0.5f;

                dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                // wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                window.setAttributes(wlp);
                // Include dialog.xml file
                dialog.setContentView(R.layout.screen_popup);
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);


                dialog.show();

                final ImageView cancel = (ImageView) dialog.findViewById(R.id.cancel_popup);
                final ImageView img = (ImageView) dialog.findViewById(R.id.image_popup);

                String str = messageArrayList.get(position).getImage();
                if (str != null && str.length() > 4) {
//                    String imagePath = URLdomain + "LogImage/" + str;
                    String imagePath = URLdomain_PlantDocImage + str;
                    Picasso.with(mContext).load(imagePath).into(img);
                }

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.cancel();
                    }
                });
            }
        });

        ((ViewHolder) holder).image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(mContext);

                dialog.setCanceledOnTouchOutside(true);
                Window window = dialog.getWindow();
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);


                WindowManager.LayoutParams wlp = window.getAttributes();

                wlp.gravity = Gravity.BOTTOM;
                wlp.dimAmount = 0.5f;

                dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                // wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                window.setAttributes(wlp);
                // Include dialog.xml file
                dialog.setContentView(R.layout.screen_popup);
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);


                dialog.show();

                final ImageView cancel = (ImageView) dialog.findViewById(R.id.cancel_popup);
                final ImageView img = (ImageView) dialog.findViewById(R.id.image_popup);

                String str = messageArrayList.get(position).getImage2();
                if (str != null && str.length() > 4) {
//                    String imagePath = URLdomain + "LogImage/" + str;
                    String imagePath = URLdomain_PlantDocImage + str;
                    Picasso.with(mContext).load(imagePath).into(img);
                }

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return messageArrayList.size();
    }

    public static String getTimeStamp(String dateStr) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timestamp = "";

        today = today.length() < 2 ? "0" + today : today;

        try {
            Date date = format.parse(dateStr);
            SimpleDateFormat todayFormat = new SimpleDateFormat("dd");
            String dateToday = todayFormat.format(date);
            format = dateToday.equals(today) ? new SimpleDateFormat("hh:mm a") : new SimpleDateFormat("dd LLL, hh:mm a");
            String date1 = format.format(date);
            timestamp = date1.toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return timestamp;
    }
}

