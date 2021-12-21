package com.weather.risk.mfi.myfarminfo.adapter;

import static com.weather.risk.mfi.myfarminfo.utils.Utility.setDynamicLanguage;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setFontsStyleTxt;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.speech.tts.TextToSpeech;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.bean.KeyValueBean;
import com.weather.risk.mfi.myfarminfo.utils.AppConstant;
import com.weather.risk.mfi.myfarminfo.utils.UtilFonts;

import java.util.ArrayList;


public class PestDiseaseAdapterNew extends RecyclerView.Adapter<PestDiseaseAdapterNew.ViewHolder> {
    private ArrayList<KeyValueBean> mDataset = new ArrayList<KeyValueBean>();
    private ArrayList<ArrayList<String>> imgList = new ArrayList<ArrayList<String>>();

    private ArrayList<ArrayList<String>> mgmtList = new ArrayList<ArrayList<String>>();

    public Context mContext;
    String imageString;
    TextToSpeech t1;

    // Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView name, value;
        ImageView imgeview_details, imageView1/*,imageView2,imageView3,imageView4,imageView5,imageView6*/;

        public ViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.name);
            value = (TextView) v.findViewById(R.id.value);

            imgeview_details = (ImageView) v.findViewById(R.id.imgeview_details);
            imageView1 = (ImageView) v.findViewById(R.id.image1);
//            imageView2=(ImageView)v.findViewById(R.id.image2);
//            imageView3=(ImageView)v.findViewById(R.id.image3);
//            imageView4=(ImageView)v.findViewById(R.id.image4);
//            imageView5=(ImageView)v.findViewById(R.id.image5);
//            imageView6=(ImageView)v.findViewById(R.id.image6);

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
    public PestDiseaseAdapterNew(Context con, ArrayList<KeyValueBean> myDataset, ArrayList<ArrayList<String>> imList, ArrayList<ArrayList<String>> mgList) {
        mDataset = myDataset;
        mContext = con;
        imgList = imList;
        mgmtList = mgList;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_pest_row, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {



        setFontsStyleTxt(mContext, holder.name, 2);
        setFontsStyleTxt(mContext, holder.value, 6);

        holder.name.setText(mDataset.get(position).getName());
        holder.value.setText(mDataset.get(position).getValue());
        ArrayList<String> imageList = imgList.get(position);
        ArrayList<String> managementList = mgmtList.get(position);

        holder.imageView1.setVisibility(View.GONE);
//        holder.imageView2.setVisibility(View.GONE);
//        holder.imageView3.setVisibility(View.GONE);
//        holder.imageView4.setVisibility(View.GONE);
//        holder.imageView5.setVisibility(View.GONE);
//        holder.imageView6.setVisibility(View.GONE);

        for (int i = 0; i < imageList.size(); i++) {
            if (i == 0) {
                holder.imageView1.setVisibility(View.VISIBLE);
                Glide.with(mContext).load(imageList.get(i)).into(holder.imageView1);
            }
        }

//        holder.mgmtText.setText(abcd);
        holder.imgeview_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<String> imageLists = imgList.get(position);
                ArrayList<String> managementLists = mgmtList.get(position);
                String tile, mangt, imageurl;
                tile = mDataset.get(position).getName();
                String abcd = "";
                for (int i = 0; i < managementLists.size(); i++) {
                    if (i == 0) {
                        abcd = managementLists.get(i);
                    } else {
                        abcd = abcd + "\n" + managementLists.get(i);
                    }
                }
                mangt = abcd;
                imageurl = imageLists.get(0);

                selectImagePopup(tile, mangt, imageurl);
            }
        });


    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void selectImagePopup(String titles, String Value, String imageURL) {


        //final Dialog dialog = new Dialog(OtherUserProfile.this,android.R.style.Theme_Translucent_NoTitleBar);
        final Dialog dialog = new Dialog(mContext);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        Window window = dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.CENTER;
        wlp.dimAmount = 0.7f;
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        // wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);

        // Include dialog.xml file
        dialog.setContentView(R.layout.pop_pestdiseasedetails);

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        Button btn_cross = (Button) dialog.findViewById(R.id.btn_cross);
        ImageView imageview = (ImageView) dialog.findViewById(R.id.imageview);
        TextView title = (TextView) dialog.findViewById(R.id.title);
        TextView txt_Mangtitle = (TextView) dialog.findViewById(R.id.txt_Mangtitle);
        TextView txt_management = (TextView) dialog.findViewById(R.id.txt_management);

        setFontsStyleTxt(mContext, title, 2);
        setFontsStyleTxt(mContext, txt_Mangtitle, 2);
        setFontsStyleTxt(mContext, txt_management, 6);

        setDynamicLanguage(mContext, txt_Mangtitle, "Management", R.string.Management);


        title.setText(titles);
        txt_management.setText(Value);

        Glide.with(mContext).load(imageURL).into(imageview);

        btn_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });


        dialog.show();
    }


}
