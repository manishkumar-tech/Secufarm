package com.weather.risk.mfi.myfarminfo.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by Admin on 08-03-2018.
 */
public class GalleryImageAdapter extends BaseAdapter
{
    private Context mContext;

    ArrayList<Bitmap> imageList = new ArrayList<Bitmap>();


    public GalleryImageAdapter(Context context, ArrayList<Bitmap> imgList)
    {
        mContext = context;
        imageList = imgList;
    }

    public int getCount() {
        return imageList.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }


    // Override this method according to your need
    public View getView(int index, View view, ViewGroup viewGroup)
    {
        // TODO Auto-generated method stub
        ImageView i = new ImageView(mContext);

        i.setImageBitmap(imageList.get(index));
        i.setLayoutParams(new Gallery.LayoutParams(500, 500));

        i.setScaleType(ImageView.ScaleType.FIT_XY);




        return i;
    }

}