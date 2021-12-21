package com.weather.risk.mfi.myfarminfo.youtubevideostream;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragmentX;
import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.database.DBAdapter;
import com.weather.risk.mfi.myfarminfo.utils.Utility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.SN_YoutubeVideoActivity;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getUIDforScreenTracking;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setScreenTracking;


public class YoutubeVideoRecyclerView extends AppCompatActivity {

    //    private static final String TAG = class.getSimpleName();
    private RecyclerView recyclerView;
    //youtube player fragment
    private YouTubePlayerSupportFragmentX youTubePlayerFragment;
    private ArrayList<String> youtubeVideoArrayList;
    private ArrayList<String> titles;
    private ArrayList<String> logDates;

    //youtube player to play video when new video selected
    private YouTubePlayer youTubePlayer;
    DBAdapter db;
    ImageView imgeview_Youtube, back_button;
    String UID = "";

    //VECTOR FOR VIDEO URLS
    ArrayList<String> title = new ArrayList<>();
    String URL = "", YoutubeVideo = "";
    ArrayList<HashMap<String, String>> YoutubeVideolist = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.youtubevideorecyclerview);

        imgeview_Youtube = (ImageView) findViewById(R.id.imgeview_Youtube);
        back_button = (ImageView) findViewById(R.id.back_button);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        //Horizontal direction recycler view
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        db = new DBAdapter(this);
        db.open();

        Bundle bundle = getIntent().getExtras();
        try {
            if (bundle.size() > 0) {
                URL = bundle.getString("YoutubeVideoURL");
//                URL = "https://www.youtube.com/watch?v=RSKRs200gxQ&feature=youtu.be";
                YoutubeVideolist = (ArrayList<HashMap<String, String>>) getIntent().getSerializableExtra("YoutubeVideolist");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        UID = getUIDforScreenTracking();
        setScreenTracking(this, db, SN_YoutubeVideoActivity, UID);
        if (URL != null && URL.length() > 2) {
            imgeview_Youtube.setVisibility(View.VISIBLE);
        } else {
            imgeview_Youtube.setVisibility(View.GONE);
        }
        checktodayyoutubevideoexistornot();

        generateDummyVideoList();
        initializeYoutubePlayer();
        setRecyclerView();
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        imgeview_Youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (VideoID != null && VideoID.length() > 2) {
//                    Intent in = new Intent(YoutubeVideoList.this, YoutubeWebview.class);
//                    in.putExtra("YoutubeVideoURL", VideoID);
//                    startActivity(in);
//                }
                if (URL != null && URL.length() > 2) {
                    Intent in = new Intent(YoutubeVideoRecyclerView.this, YoutubeWebview.class);
                    in.putExtra("YoutubeVideoURL", URL);
                    startActivity(in);
                }
            }
        });

    }

    /**
     * initialize youtube player via Fragment and get instance of YoutubePlayer
     */
    private void initializeYoutubePlayer() {

//        YouTubePlayerFragmentX youTubePlayerFragment = YouTubePlayerFragmentX.newInstance();
//        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
//        transaction.add(R.id.youtube_fragment, youTubePlayerFragment).commit();

        youTubePlayerFragment = (YouTubePlayerSupportFragmentX) getSupportFragmentManager()
                .findFragmentById(R.id.youtube_player_fragment);

        if (youTubePlayerFragment == null)
            return;

        youTubePlayerFragment.initialize(getResources().getString(R.string.GoogleAPIkeyfor_youtube), new YouTubePlayer.OnInitializedListener() {

            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
                                                boolean wasRestored) {
                if (!wasRestored) {
                    youTubePlayer = player;

                    //set the player style default
                    youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);

                    //cue the 1st video by default
                    youTubePlayer.cueVideo(youtubeVideoArrayList.get(0));
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider arg0, YouTubeInitializationResult arg1) {

                //print or show error if initialization failed
                Log.e("TAG", "Youtube Player View initialization failed");
            }
        });
    }


    /**
     * populate the recycler view and implement the click event here
     */
    private void setRecyclerView() {
        final YoutubeVideoAdapter adapter = new YoutubeVideoAdapter(this, youtubeVideoArrayList,titles,logDates);
        recyclerView.setAdapter(adapter);

        //set click event
        recyclerView.addOnItemTouchListener(new RecyclerViewOnClickListener(this, new RecyclerViewOnClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                if (youTubePlayerFragment != null && youTubePlayer != null) {
                    //update selected position
                    adapter.setSelectedPosition(position);

                    //load selected video
                    youTubePlayer.cueVideo(youtubeVideoArrayList.get(position));
                }

            }
        }));
    }


    public String getEmbedString(String url) {
        String pattern = "(?<=watch\\?v=|/videos/|embed\\/|youtu.be\\/|\\/v\\/|\\/e\\/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%2Fvideos%2F|embed%\u200C\u200B2F|youtu.be%2F|%2Fv%2F)[^#\\&\\?\\n]*";

        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(url); //url is youtube url for which you want to extract the id.
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    //YoutubeLiveVideo
    String Title = null, Description = null, DateFrom = null, DateFromTime = null, DateTo = null,
            DateToTime = null, ProjectID = null, VideoID = null, VisibleName = null;

    public void checktodayyoutubevideoexistornot() {
        try {
            String TodayDate = Utility.getdate();
//        TodayDate = "03-06-2020";

            db.open();
            ArrayList<HashMap<String, String>> hasmap = new ArrayList<>();
//        String SQL = "Select * from  tblYOutubeVideoDateTime where DateFrom='" + TodayDate + "'";
            String SQL = "Select (select date('now'))as TodayDate,date(DateFrom) as DateFrom,DateFromTime,date(DateTo) as DateTo,DateToTime,VideoID,ProjectID  from tblYoutubeVideoDateTime where (TodayDate  between DateFrom AND DateTo)";
            hasmap = db.getDynamicTableValue(SQL);
            if (hasmap.size() > 0) {
                for (int i = 0; i < hasmap.size(); i++) {
                    HashMap<String, String> hashMap = new HashMap<>();
                    Title = hasmap.get(i).get("Title");
                    Description = hasmap.get(i).get("Description");
                    DateFrom = hasmap.get(i).get("DateFrom");
                    DateFromTime = hasmap.get(i).get("DateFromTime");
                    DateTo = hasmap.get(i).get("DateTo");
                    DateToTime = hasmap.get(i).get("DateToTime");
                    ProjectID = hasmap.get(i).get("ProjectID");
                    VideoID = hasmap.get(i).get("VideoID");
                    VisibleName = hasmap.get(i).get("VisibleName");
                }
                if (DateFrom != null) {
                    TimerCheck();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    //Youtube Timmer Check

    Thread startThread = null;
    Thread closethread = null;

    public void TimerCheck() {

        String TodayDate = Utility.getdateYYYYMMDD();
//        String TodayDate = "2020-06-07";
        if (!DateTo.equalsIgnoreCase(TodayDate)) {
            imgeview_Youtube.setVisibility(View.VISIBLE);
        } else {
            imgeview_Youtube.setVisibility(View.GONE);
            //Thread for STart check
            Runnable startRunnableThread = new CountDownRunnerForStart();
            startThread = new Thread(startRunnableThread);
            startThread.start();
        }
    }

    class CountDownRunnerForStart implements Runnable {
        // @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    doWorkstart();
//                    Thread.sleep(1000); // Pause of 1 Second
                    Thread.sleep(600000); // Pause of 10 mins
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (Exception e) {
                }
            }
        }
    }

    public void doWorkstart() {
        runOnUiThread(new Runnable() {
            public void run() {
                try {
                    String CurrentTime = Utility.gettime();
                    String fromtime = DateFromTime;
                    String totime = DateToTime;


                    long millse_start = Utility.reversetime(fromtime).getTime() - Utility.reversetime(CurrentTime).getTime();
//                    long mills_start = Math.abs(millse_start);
                    long mills_start = (int) millse_start;
                    int Hours_start = (int) (mills_start / (1000 * 60 * 60));
                    int Mins_start = (int) (mills_start / (1000 * 60)) % 60;
                    long Secs_start = (int) (mills_start / 1000) % 60;


                    long millse_close = Utility.reversetime(totime).getTime() - Utility.reversetime(CurrentTime).getTime();
//                    long mills_close = Math.abs(millse_close);
                    int mills_close = (int) millse_close;
                    int Hours_close = (int) (mills_close / (1000 * 60 * 60));
                    int Mins_close = (int) (mills_close / (1000 * 60)) % 60;
                    long Secs_close = (int) (mills_close / 1000) % 60;

//                    String TodayDate = Utility.getdateYYYYMMDD();
//                    String TodayDate = "2020-06-07";
//                    if (!DateTo.equalsIgnoreCase(TodayDate)) {
//                        imgeview_Youtube.setVisibility(View.VISIBLE);
//                    } else {
                    if ((Hours_start == 0 && Mins_start <= 15) || (Hours_close >= 0 && Mins_close > 0)) {
//                    if (Mins_start < 35) {//for Testing
                        imgeview_Youtube.setVisibility(View.VISIBLE);
                    } else {
//                    } else if (Mins_close < 10) {//for testing
                        imgeview_Youtube.setVisibility(View.GONE);
                        startThread.stop();
                    }
//                    }

                } catch (Exception e) {

                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
//        cropName.setText(AppConstant.selected_crop);
        try {
            if (db != null) {
                db = new DBAdapter(this);
            }
            db.open();
            Log.d("OnPause Method", "OnPause Method called");
            setScreenTracking(this, db, SN_YoutubeVideoActivity, UID);
            //Youtubevideo call update
//            checktodayyoutubevideoexistornot();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            if (db != null) {
                db = new DBAdapter(this);
            }
            db.open();
            Log.d("OnPause Method", "OnPause Method called");
            setScreenTracking(this, db, SN_YoutubeVideoActivity, UID);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            if (db != null) {
                db = new DBAdapter(this);
            }
            db.open();
            Log.d("onStop Method", "onStop Method called");
            setScreenTracking(this, db, SN_YoutubeVideoActivity, UID);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    /**
     * method to generate dummy array list of videos
     */
    private void generateDummyVideoList() {
        youtubeVideoArrayList = new ArrayList<>();
        titles = new ArrayList<>();
        logDates = new ArrayList<>();

        try {
            if (YoutubeVideolist != null && YoutubeVideolist.size() > 0) {
                for (int i = 0; i < YoutubeVideolist.size(); i++) {
                    String title = YoutubeVideolist.get(i).get("Title");
                    String URL = YoutubeVideolist.get(i).get("VideoID");
                    String LogDate = YoutubeVideolist.get(i).get("UploadedDate");
                    String VideosID = getEmbedString(URL);
                    //add all videos to array list
                    Collections.addAll(youtubeVideoArrayList, VideosID);
                    Collections.addAll(titles, title);
                    Collections.addAll(logDates, LogDate);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
