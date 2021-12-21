package com.weather.risk.mfi.myfarminfo.payment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.FileProvider;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.weather.risk.mfi.myfarminfo.BuildConfig;
import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.databinding.OrderHistoryActivityBinding;
import com.weather.risk.mfi.myfarminfo.home.AppController;
import com.weather.risk.mfi.myfarminfo.marketplace.BaseActivity;
import com.weather.risk.mfi.myfarminfo.marketplace.OrderDetailsActivity;
import com.weather.risk.mfi.myfarminfo.marketplace.ProductOrderDetailsActivity;
import com.weather.risk.mfi.myfarminfo.payment.adapter.OrderHistoryAdapter;
import com.weather.risk.mfi.myfarminfo.payment.model.OrderHistoryBean;
import com.weather.risk.mfi.myfarminfo.payment.model.OrderHistoryRequest;
import com.weather.risk.mfi.myfarminfo.retofitservices.ApiService;
import com.weather.risk.mfi.myfarminfo.retofitservices.ResponseObserver;
import com.weather.risk.mfi.myfarminfo.utils.AppConstant;
import com.weather.risk.mfi.myfarminfo.utils.Utility;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

import static com.weather.risk.mfi.myfarminfo.customcamera.CameraSurfaceView.IMAGE_DIRECTORY;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getDynamicLanguageToast;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getDynamicLanguageValue;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setDynamicLanguage;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setFontsStyle;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setFontsStyleTxt;

public class OrderHistoryActivity extends BaseActivity implements ItemClick {

    List<List<OrderHistoryBean>> responseGlobal = null;
    List<OrderHistoryBean> newArray = new ArrayList<OrderHistoryBean>();
    private ApiService apiService;
    OrderHistoryActivityBinding binding;


    public TextView farmerIdText, pendingAmount, totalAmount, collectedAmount;
    String farmerId = null;
    Double pAmnt = 0.0;
    String projectId = null;

    String farmID = null;
    ImageView whatsappShare;

    @Override
    protected void initView(Bundle bundle, ViewDataBinding viewDataBinding) {
        binding = (OrderHistoryActivityBinding) viewDataBinding;
        farmerIdText = binding.farmerId;
        whatsappShare = binding.whatsappShare;
        pendingAmount = binding.pendingAmount;
        totalAmount = binding.totalAmount;
        collectedAmount = binding.collectAmount;
        apiService = AppController.getInstance().getApiService();
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        projectId = getIntent().getStringExtra("projectId");
        farmerId = getIntent().getStringExtra("farmerId");
        farmID = getIntent().getStringExtra("farmID");
        if (farmerId != null && farmerId.length() > 0) {
            getDetailsMethod(farmerId);
        }
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        whatsappShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("fvdfvdfvdf", "efeffwefefw");
                if (responseGlobal.get(0) != null && responseGlobal.get(0).size() > 0) {
                    Log.v("fvdfvdfvdf", "efeffwefefw22222");
                    if (Utility.checkPermissionGallery(OrderHistoryActivity.this)) {
                        sharePdfHistory();
                    }

                } else {
                    Log.v("fvdfvdfvdf", "efeffwefefw33333");
                    getDynamicLanguageToast(getApplicationContext(), "Nodataavailable", R.string.Nodataavailable);
                }

            }
        });


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected int getActivityLayout() {
        return R.layout.order_history_activity;
    }


    private void getDetailsMethod(String id) {
        // display a progress dialog
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage(getDynamicLanguageValue(getApplicationContext(), "Pleasewait", R.string.Pleasewait)); // set message
        progressDialog.show(); // show progress dialog
        OrderHistoryRequest request = new OrderHistoryRequest();
        request.setFarmerID(Integer.valueOf(id));
        if (projectId != null && !projectId.equalsIgnoreCase("null")) {
            request.setProjectID(Integer.valueOf(projectId));
        } else {
            request.setProjectID(0);
        }
        request.setOrderStatus("all");
        request.setDeliveryStatus("all");
        if (AppConstant.user_id != null && !AppConstant.user_id.equalsIgnoreCase("null")) {
            request.setUserID(Integer.valueOf(AppConstant.user_id));
        }
        apiService.orderHistory(request).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResponseObserver<Response<List<List<OrderHistoryBean>>>>(getCompositeDisposable()) {
                    @Override
                    public void onNetworkError(Throwable e) {
                        progressDialog.cancel();
                        Log.v("wvqevqevqe", e.getMessage() + " " + e.toString());
                        showError(getDynamicLanguageValue(getApplicationContext(), "network_error", R.string.network_error));

                    }

                    @Override
                    public void onServerError(Throwable e, int code) {
                        progressDialog.cancel();
                        showError(getDynamicLanguageValue(getApplicationContext(), "server_not_found", R.string.server_not_found));


                    }

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onNext(Response<List<List<OrderHistoryBean>>> response) {
                        progressDialog.cancel();
                        List<List<OrderHistoryBean>> responseD = response.body();
                        if (responseD != null && responseD.size() > 0) {
                            responseGlobal = responseD;
                            if (responseD.get(0) != null && responseD.get(0).size() > 0) {
                                farmerIdText.setText("Farmer Id -" + responseD.get(0).get(0).getFarmerID());
                                collectedAmount.setText("" + responseD.get(0).get(0).getCollectedAmount());
                                totalAmount.setText("" + responseD.get(0).get(0).getTotalAmount());
                                pendingAmount.setText("" + responseD.get(0).get(0).getPendingAmount());
                                pAmnt = responseD.get(0).get(0).getBalanceAmount();
                            }
                            if (responseD.size() > 1 && responseD.get(1) != null && responseD.get(1).size() > 0) {
                                newArray = new ArrayList<OrderHistoryBean>();
                                for (int i = 0; i < responseD.get(1).size(); i++) {
                                   /* if (i!=0){
                                        newArray.add(responseD.get(1).get(i));
                                    }*/
                                    newArray.add(responseD.get(1).get(i));
                                }
                                binding.recyclerView.setVisibility(View.VISIBLE);
                                OrderHistoryAdapter adapter = new OrderHistoryAdapter(OrderHistoryActivity.this, newArray, OrderHistoryActivity.this);
                                binding.recyclerView.setLayoutManager(new LinearLayoutManager(OrderHistoryActivity.this, LinearLayoutManager.VERTICAL, false));
                                binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
                                binding.recyclerView.setAdapter(adapter);
                            } else {
                                binding.recyclerView.setVisibility(View.GONE);

                            }


                        } else {
                            binding.recyclerView.setVisibility(View.GONE);

                        }
                    }
                });
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                EditText edit = ((EditText) v);
                Rect outR = new Rect();
                edit.getGlobalVisibleRect(outR);
                Boolean isKeyboardOpen = !outR.contains((int) ev.getRawX(), (int) ev.getRawY());
                System.out.print("Is Keyboard? " + isKeyboardOpen);
                if (isKeyboardOpen) {
                    System.out.print("Entro al IF");
                    edit.clearFocus();
                    InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(edit.getWindowToken(), 0);
                }
                edit.setCursorVisible(!isKeyboardOpen);

            }
        }
        return super.dispatchTouchEvent(ev);
    }


    @Override
    public void onClick(String value) {
        if (value != null) {
            String[] split = value.split("=");
            if (split.length > 1 && farmerId != null) {
                Intent in = new Intent(OrderHistoryActivity.this, ProductOrderDetailsActivity.class);
                in.putExtra("order_number", split[0]);
                in.putExtra("farmer_id", farmerId);
                in.putExtra("delivery", split[1]);
                in.putExtra("farmID", farmID);
                startActivity(in);
            } else {
                getDynamicLanguageToast(getApplicationContext(), "Idnotfound", R.string.Idnotfound);
            }
        }
    }


    public void sharePdfHistory() {
        Document doc = new Document();
        PdfWriter docWriter = null;
        DecimalFormat df = new DecimalFormat("0.00");
        try {
            //special font sizes
            Font bold_XL = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
            Font bold = new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD);

            Font yellow_font = FontFactory.getFont(FontFactory.TIMES_ROMAN, 13, Font.BOLD);
            yellow_font.setColor(BaseColor.YELLOW);


            BaseColor myColorr = WebColors.getRGBColor("#6ABD46");

            Font green_font = FontFactory.getFont(FontFactory.TIMES_ROMAN, 13, Font.BOLD);
            green_font.setColor(myColorr);

            Font red_font = FontFactory.getFont(FontFactory.TIMES_ROMAN, 13, Font.BOLD);
            red_font.setColor(BaseColor.RED);


            Font normal = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);
            //Current Date
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            System.out.println(dateFormat.format(date)); //2016/11/16 12:08:43
            String CurrentDate = String.valueOf(dateFormat.format(date));
            CurrentDate = CurrentDate.replace("/", "");
            CurrentDate = CurrentDate.replace(":", "").trim();
            //file path
            String mFileName = "MFI_ORDER_HISTORY" + CurrentDate + ".pdf";
            //  String mFilePath = Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY + "/" + mFileName + ".pdf";
            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root + IMAGE_DIRECTORY);
            if (!myDir.exists()) {
                myDir.mkdirs();
            }

            File file = new File(myDir, mFileName);
           /* if (file.exists()) file.delete();
            file.createNewFile();
*/
            docWriter = PdfWriter.getInstance(doc, new FileOutputStream(file));

            doc.addAuthor("betterThanZero");
            doc.addCreationDate();
            doc.addProducer();
            doc.addCreator("MySampleCode.com");
            doc.addTitle("Report with Column Headings");
            doc.setPageSize(PageSize.LETTER);
            //open document
            doc.open();
            //specify column widths
            float[] columnWidths = {5f, 5f};
            //create PDF table with the given widths
            PdfPTable table = new PdfPTable(columnWidths);
            // set table width a percentage of the page width
            table.setWidthPercentage(90f);

            double orderTotal, total = 0;

            //specify column widths
            float[] columnWidths1 = {3f, 2f, 2f, 2f, 2f, 2f, 2f};
            //create PDF table with the given widths
            PdfPTable table1 = new PdfPTable(columnWidths1);
            // set table width a percentage of the page width
            table1.setWidthPercentage(100f);
            //insert column headings

            Font f1 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 13, Font.BOLD);
            f1.setColor(BaseColor.WHITE);

            insertCellHeader(table1, "Order No.", Element.ALIGN_CENTER, 1, f1);
            insertCellHeader(table1, "Date", Element.ALIGN_CENTER, 1, f1);
            insertCellHeader(table1, "Delivery Status", Element.ALIGN_CENTER, 1, f1);
            insertCellHeader(table1, "Payment Mode", Element.ALIGN_CENTER, 1, f1);
            insertCellHeader(table1, "Amount (Rs.)", Element.ALIGN_CENTER, 1, f1);
            insertCellHeader(table1, "Collected Amount (Rs.)", Element.ALIGN_CENTER, 1, f1);
            insertCellHeader(table1, "Pending Amount (Rs.)", Element.ALIGN_CENTER, 1, f1);
            table1.setHeaderRows(1);


            //insert an empty row

            if (newArray != null && newArray.size() > 0) {
                for (int i = 0; i < newArray.size(); i++) {
                    insertCell(table1, newArray.get(i).getOrderID(), Element.ALIGN_LEFT, 1, bold);
                    insertCell(table1, newArray.get(i).getOrderdate(), Element.ALIGN_LEFT, 1, normal);
                    insertCell(table1, newArray.get(i).getDeliveryStatus(), Element.ALIGN_LEFT, 1, normal);
                    insertCell(table1, newArray.get(i).getPaymentType(), Element.ALIGN_LEFT, 1, normal);
                    insertCell(table1, "" + newArray.get(i).getOrderAmount(), Element.ALIGN_LEFT, 1, bold);
                    insertCell(table1, "" + newArray.get(i).getCollectedAmount(), Element.ALIGN_LEFT, 1, green_font);
                    insertCell(table1, "" + newArray.get(i).getPendingAmount(), Element.ALIGN_LEFT, 1, red_font);
                }
                if (responseGlobal.get(0) != null && responseGlobal.get(0).size() > 0) {
                    insertCell(table1, getDynamicLanguageValue(getApplicationContext(), "pending_amount_rs", R.string.pending_amount_rs), Element.ALIGN_LEFT, 4, bold);
                    insertCell(table1, "" + responseGlobal.get(0).get(0).getPendingAmount(), Element.ALIGN_LEFT, 3, bold);
                    insertCell(table1, getDynamicLanguageValue(getApplicationContext(), "collected_amount", R.string.collected_amount), Element.ALIGN_LEFT, 4, bold);
                    insertCell(table1, "" + responseGlobal.get(0).get(0).getCollectedAmount(), Element.ALIGN_LEFT, 3, bold);
                    insertCell(table1, getDynamicLanguageValue(getApplicationContext(), "total_amount_rs", R.string.total_amount_rs), Element.ALIGN_LEFT, 4, bold);
                    insertCell(table1, "" + responseGlobal.get(0).get(0).getTotalAmount(), Element.ALIGN_LEFT, 3, bold);


                }


            }
            //create a paragraph
            Paragraph paragraph = new Paragraph();
            Chunk chunk0 = new Chunk(getDynamicLanguageValue(getApplicationContext(), "order_history", R.string.order_history), bold_XL);
            Phrase ph0 = new Phrase(chunk0);
            Chunk chunk1 = new Chunk("\n\n" + getDynamicLanguageValue(getApplicationContext(), "GSTIN", R.string.GSTIN), normal);
            Phrase ph1 = new Phrase(chunk1);
            Chunk chunk2 = new Chunk("\n" + getDynamicLanguageValue(getApplicationContext(), "WRMS", R.string.WRMS), bold);
            Phrase ph2 = new Phrase(chunk2);
            Chunk chunk3 = new Chunk("\n" + getDynamicLanguageValue(getApplicationContext(), "HeadOffice", R.string.HeadOffice), normal);
            Phrase ph3 = new Phrase(chunk3);
            Chunk chunk4 = new Chunk("\n" + getDynamicLanguageValue(getApplicationContext(), "Telphoneno", R.string.Telphoneno) + "\n\n", normal);
            Phrase ph4 = new Phrase(chunk4);
            paragraph.setAlignment(Element.ALIGN_CENTER);
            paragraph.add(ph0);
            paragraph.add(ph1);
            paragraph.add(ph2);
            paragraph.add(ph3);
            paragraph.add(ph4);
            //add the PDF table to the paragraph
            // add one empty line
            paragraph.add(table);
            //create  paragraph
            Paragraph paragraph1 = new Paragraph();
            Chunk chunk5 = new Chunk("\n\n" + getDynamicLanguageValue(getApplicationContext(), "DisclaimerPleasenote", R.string.DisclaimerPleasenote), normal);
            Phrase ph5 = new Phrase(chunk5);
            paragraph1.add(table1);
            paragraph1.add(ph5);
            // add the paragraph to the document
            doc.add(paragraph);
            doc.add(new Paragraph("\n\n"));
            doc.add(paragraph1);
            //Share via whatsapp pdf of order history

            if (file.exists()) {


                Uri path = FileProvider.getUriForFile(OrderHistoryActivity.this, BuildConfig.APPLICATION_ID + ".provider", file);

                Intent pdfOpenintent = new Intent();
                pdfOpenintent.setAction(Intent.ACTION_SEND);
                pdfOpenintent.setType("application/pdf");
                pdfOpenintent.putExtra(Intent.EXTRA_STREAM, path);
                pdfOpenintent.setPackage("com.whatsapp");


                try {
                    startActivity(pdfOpenintent);
                } catch (ActivityNotFoundException e) {

                    e.printStackTrace();
                }

            }

            //  Toast.makeText(this, getResources().getString(R.string.PDFFilegenerated), Toast.LENGTH_SHORT).show();

        } catch (DocumentException dex) {
            dex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (doc != null) {
                //close the document
                doc.close();
            }
            if (docWriter != null) {
                //close the writer
                docWriter.close();
            }
        }
    }

    private void insertCell(PdfPTable table, String text, int align, int colspan, Font font) {
        //create a new cell with the specified Text and Font
        PdfPCell cell = new PdfPCell(new Phrase(text.trim(), font));
        //set the cell alignment
        cell.setHorizontalAlignment(align);

        //set the cell column span in case you want to merge two or more cells
        cell.setColspan(colspan);
        //in case there is no text and you wan to create an empty row
        if (text.trim().equalsIgnoreCase("")) {
            cell.setMinimumHeight(100f);
        }
        cell.setPadding(3f);
        //add the call to the table
        table.addCell(cell);

    }

    private void insertCellHeader(PdfPTable table, String text, int align, int colspan, Font font) {
        //create a new cell with the specified Text and Font
        PdfPCell cell = new PdfPCell(new Phrase(text.trim(), font));
        //set the cell alignment
        cell.setHorizontalAlignment(align);
        //set the cell column span in case you want to merge two or more cells
        cell.setColspan(colspan);
        BaseColor myColor = WebColors.getRGBColor("#6ABD46");
        cell.setBackgroundColor(myColor);
        cell.setPadding(3f);

        //in case there is no text and you wan to create an empty row
        if (text.trim().equalsIgnoreCase("")) {
            cell.setMinimumHeight(100f);
        }
        //add the call to the table
        table.addCell(cell);

    }

    @Override
    protected void onResume() {
        super.onResume();
        setLanguages();
    }

    public void setLanguages() {
        //Heading
        setFontsStyleTxt(this, binding.txtFarmRegistration, 2);
        setFontsStyleTxt(this, binding.farmerId, 5);
        setFontsStyleTxt(this, binding.txttotalAmount, 6);
        setFontsStyleTxt(this, binding.totalAmount, 5);
        setFontsStyleTxt(this, binding.txtcollectAmount, 6);
        setFontsStyleTxt(this, binding.collectAmount, 5);
        setFontsStyleTxt(this, binding.txtpendingAmount, 6);
        setFontsStyleTxt(this, binding.pendingAmount, 5);

        //Tab Service
        setDynamicLanguage(this, binding.txtFarmRegistration, "order_history", R.string.order_history);
        setDynamicLanguage(this, binding.txttotalAmount, "total_amount", R.string.total_amount);
        setDynamicLanguage(this, binding.txtcollectAmount, "collected_amount", R.string.collected_amount);
        setDynamicLanguage(this, binding.txtpendingAmount, "pending_amount_rs", R.string.pending_amount_rs);

    }
}
