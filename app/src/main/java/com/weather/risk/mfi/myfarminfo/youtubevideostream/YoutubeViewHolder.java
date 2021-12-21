package com.weather.risk.mfi.myfarminfo.youtubevideostream;

import android.view.View;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.youtube.player.YouTubeThumbnailView;
import com.weather.risk.mfi.myfarminfo.R;

public class YoutubeViewHolder extends RecyclerView.ViewHolder{
    public YouTubeThumbnailView videoThumbnailImageView;
    public CardView youtubeCardView;
    public TextView txttitle;
    public TextView txtdate;

    public YoutubeViewHolder(View itemView) {
        super(itemView);
        videoThumbnailImageView = itemView.findViewById(R.id.video_thumbnail_image_view);
        youtubeCardView = itemView.findViewById(R.id.youtube_row_card_view);
        txttitle = itemView.findViewById(R.id.txttitle);
        txtdate = itemView.findViewById(R.id.txtdatee);
    }
}
