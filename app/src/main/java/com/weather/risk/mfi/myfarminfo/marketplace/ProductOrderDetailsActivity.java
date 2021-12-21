package com.weather.risk.mfi.myfarminfo.marketplace;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


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
import com.weather.risk.mfi.myfarminfo.databinding.ProductOrderDetailActivityBinding;
import com.weather.risk.mfi.myfarminfo.home.AppController;
import com.weather.risk.mfi.myfarminfo.retofitservices.ResponseObserver;
import com.weather.risk.mfi.myfarminfo.retrofitmodelnew.OrderDetailRequest;
import com.weather.risk.mfi.myfarminfo.retrofitmodelnew.OrderDetailResponse;
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

public class ProductOrderDetailsActivity extends BaseActivity {

    OrderDetailResponse responsesGlobal = null;

    List<DataDT> listData = new ArrayList<DataDT>();
    ProductOrderDetailActivityBinding binding;
    public TextView orderNumber, pendingAmount, totalAmount, collectedAmount;

    String orderNo = null;
    RecyclerView recyclerView;
    Button payNowBTN;
    String farmer_id = null;
    String deliveryType = null;
    boolean flagOnlyCollection = false;
    String farmID = null;

    @SuppressLint("SetTextI18n")
    @Override
    protected void initView(Bundle bundle, ViewDataBinding viewDataBinding) {
        binding = (ProductOrderDetailActivityBinding) viewDataBinding;
        orderNumber = binding.orderNumber;
        pendingAmount = binding.pendingAmount;
        totalAmount = binding.totalAmount;
        collectedAmount = binding.collectAmount;
        recyclerView = binding.recyclerView;

//        Bundle bundle1 = getIntent().getExtras();
        payNowBTN = binding.payNowBtn;
        orderNo = getIntent().getStringExtra("order_number");
        farmer_id = getIntent().getStringExtra("farmer_id");
        deliveryType = getIntent().getStringExtra("delivery");
        farmID = getIntent().getStringExtra("farmID");


        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        payNowBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Log.v("ecewewe", listData.size() + "");
                String serviceID = null;
                String delverQTY = null;
                Double totalA = 0.0;

                for (int i = 0; i < listData.size(); i++) {
                    if (listData.get(i).getQuantityPurchased() != null && listData.get(i).getQuantityPurchased() > 0) {
                        if (i == 0) {
                            if (listData.get(i).getServiceID() != null && listData.get(i).getDeliverQty() > 0) {
                                serviceID = "" + listData.get(i).getServiceID();
                                delverQTY = "" + listData.get(i).getDeliverQty();
                                if (listData.get(i).getProductPrice() != null) {
                                    totalA = listData.get(i).getQuantityPurchased() * listData.get(i).getProductPrice();
                                }
                            }
                        } else if (listData.get(i).getDeliverQty() > 0) {
                            serviceID = serviceID + "=" + listData.get(i).getServiceID();
                            delverQTY = delverQTY + "=" + listData.get(i).getDeliverQty();
                            totalA = totalA + listData.get(i).getQuantityPurchased() * listData.get(i).getProductPrice();
                        }
                    }
                }

                if (orderNo != null && orderNo.length() > 0 && farmer_id != null) {
                    Intent in = new Intent(ProductOrderDetailsActivity.this, OrderDetailsActivity.class);
                    in.putExtra("order_number", orderNo);
                    in.putExtra("from", "checkout");
                    in.putExtra("farmerId", farmer_id);
                    in.putExtra("delivery", deliveryType);

                    if (!flagOnlyCollection) {
                        in.putExtra("serviceIds", serviceID);
                        in.putExtra("deliveryQty", delverQTY);
                        in.putExtra("totalA", "" + totalA);
                    }
                    in.putExtra("farmID", farmID);
                    startActivity(in);
                }
            }
        });

        if (deliveryType != null && deliveryType.equalsIgnoreCase("pay_delivery")) {
            flagOnlyCollection = false;
            payNowBTN.setText(getDynamicLanguageValue(getApplicationContext(), "CollectionDelivery", R.string.CollectionDelivery));
        } else if (deliveryType != null && deliveryType.equalsIgnoreCase("delivery")) {
            flagOnlyCollection = false;
            payNowBTN.setText(getDynamicLanguageValue(getApplicationContext(), "OnlyDelivery", R.string.OnlyDelivery));
        } else {
            flagOnlyCollection = true;
            payNowBTN.setText(getDynamicLanguageValue(getApplicationContext(), "OnlyCollection", R.string.OnlyCollection));
        }

        if (orderNo != null && orderNo.length() > 0) {
            getOrderMethod(orderNo);
        }

        binding.whatsappShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listData != null && listData.size() > 0) {
                    if (Utility.checkPermissionGallery(ProductOrderDetailsActivity.this)) {
                        sharePdfHistory();
                    }
                } else {
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
        return R.layout.product_order_detail_activity;
    }


    private void getOrderMethod(final String orderN) {
        // display a progress dialog
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage(getDynamicLanguageValue(getApplicationContext(), "Dataisloading", R.string.Dataisloading)); // set message
        progressDialog.show(); // show progress dialog
        final OrderDetailRequest request = new OrderDetailRequest();
        // request.setProjectID(pId);
        request.setOrderNumber(orderN);

        AppController.getInstance().getApiService().orderDetailMethod(request).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResponseObserver<Response<OrderDetailResponse>>(getCompositeDisposable()) {
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
                    public void onNext(Response<OrderDetailResponse> response) {
                        progressDialog.cancel();
                        OrderDetailResponse responsesData = response.body();
                        if (responsesData != null) {
                            responsesGlobal = responsesData;
                            orderNumber.setText("" + orderN);
                            if (responsesData.getCollectedAmount() != null) {
                                collectedAmount.setText(responsesData.getCollectedAmount() + "");
                            } else {
                                collectedAmount.setText("-");
                            }
                            if (responsesData.getTotalAmount() != null) {
                                totalAmount.setText(responsesData.getTotalAmount() + "");
                            } else {
                                totalAmount.setText("-");
                            }
                            if (responsesData.getBalanceAmount() != null) {
                                pendingAmount.setText(responsesData.getBalanceAmount() + "");
                            } else {
                                pendingAmount.setText("-");
                            }
                            if (responsesData.getDataDT() != null && responsesData.getDataDT().size() > 0) {

                                listData = new ArrayList<>();
                                listData.addAll(responsesData.getDataDT());

                                binding.recyclerView.setVisibility(View.VISIBLE);
                                ProductOrderDetailAdapter adapter = new ProductOrderDetailAdapter(ProductOrderDetailsActivity.this, listData, flagOnlyCollection);
                                binding.recyclerView.setLayoutManager(new LinearLayoutManager(ProductOrderDetailsActivity.this, LinearLayoutManager.VERTICAL, false));
                                binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
                                binding.recyclerView.setAdapter(adapter);
                            } else {
                                binding.recyclerView.setVisibility(View.GONE);

                            }
                            Double pAmnt = responsesData.getBalanceAmount();
                            Double cAmnt = responsesData.getCollectedAmount();
                            Double tAmnt = responsesData.getTotalAmount();
                            String userTypeID = AppConstant.userTypeID;

                            if (pAmnt != null && pAmnt > 0) {
                                payNowBTN.setVisibility(View.VISIBLE);
                            } else if (deliveryType != null && deliveryType.contains("eliver")) {
                                payNowBTN.setVisibility(View.VISIBLE);
                            } else {
                                payNowBTN.setVisibility(View.GONE);
                            }


                            if (deliveryType != null && deliveryType.contains("view_detail")) {
                                payNowBTN.setVisibility(View.GONE);
                            }

                           /* if (userTypeID != null && (userTypeID.equalsIgnoreCase("1") || userTypeID.equalsIgnoreCase("2") || userTypeID.equalsIgnoreCase("18"))) {
                                if (pAmnt != null && pAmnt > 0) {
                                    if (deliveryType != null && deliveryType.equalsIgnoreCase("pay_delivery")) {

                                        payNowBTN.setText("Collect & Deliver");
                                    }else if (deliveryType != null && deliveryType.equalsIgnoreCase("delivery")) {
                                        payNowBTN.setText("Deliver");

                                    } else {
                                        payNowBTN.setText("Collect");
                                    }

                                }

                                if (!responsesData.getFlgDelivery()) {
                                    payNowBTN.setVisibility(View.GONE);
                                    markDeliverBTN.setVisibility(View.VISIBLE);
                                    if (deliveryType != null && deliveryType.contains("delivery")) {
                                        markDeliverBTN.setVisibility(View.VISIBLE);
                                    } else {
                                        markDeliverBTN.setVisibility(View.GONE);
                                    }

                                } else {
                                    markDeliverBTN.setVisibility(View.GONE);
                                    payNowBTN.setVisibility(View.GONE);
                                }


                            } else {
                                markDeliverBTN.setVisibility(View.GONE);
                                payNowBTN.setVisibility(View.GONE);
                            }
*/

                        } else {
                            recyclerView.setVisibility(View.GONE);
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


    public void sharePdfHistory() {
        Document doc = new Document();
        PdfWriter docWriter = null;
        DecimalFormat df = new DecimalFormat("0.00");
        try {
            //special font sizes
            Font bold_XL = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
            Font bold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);

            Font yellow_font = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD);
            yellow_font.setColor(BaseColor.YELLOW);


            BaseColor myColorr = WebColors.getRGBColor("#6ABD46");

            Font green_font = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD);
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
            String mFileName = "MFI_ORDER_DETAIL" + CurrentDate + ".pdf";
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

            //document header attributes
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

            //Create new Table
            //specify column widths
            float[] columnWidths1 = {4f, 2f, 3f, 2f, 2f, 2f, 2f};
            //create PDF table with the given widths
            PdfPTable table1 = new PdfPTable(columnWidths1);
            // set table width a percentage of the page width
            table1.setWidthPercentage(100f);
            //insert column headings

            Font f1 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD);
            f1.setColor(BaseColor.WHITE);

            insertCellHeader(table1, "Product Name", Element.ALIGN_CENTER, 1, f1);
            insertCellHeader(table1, "Amount (Rs.)", Element.ALIGN_CENTER, 1, f1);
            insertCellHeader(table1, "Category", Element.ALIGN_CENTER, 1, f1);
            insertCellHeader(table1, "Brand", Element.ALIGN_CENTER, 1, f1);
            insertCellHeader(table1, "Purchased Qty", Element.ALIGN_CENTER, 1, f1);
            insertCellHeader(table1, "Delivered Qty", Element.ALIGN_CENTER, 1, f1);
            insertCellHeader(table1, "Pending Qty", Element.ALIGN_CENTER, 1, f1);

            table1.setHeaderRows(1);


            if (listData != null && listData.size() > 0) {
                for (int i = 0; i < listData.size(); i++) {
                    insertCell(table1, listData.get(i).getProductName(), Element.ALIGN_LEFT, 1, normal);
                    insertCell(table1, listData.get(i).getProductPrice() + " / " + listData.get(i).getProductUnit(), Element.ALIGN_LEFT, 1, bold);
                    insertCell(table1, listData.get(i).getProductCategory(), Element.ALIGN_LEFT, 1, normal);
                    insertCell(table1, listData.get(i).getBrandName(), Element.ALIGN_LEFT, 1, normal);
                    insertCell(table1, "" + listData.get(i).getQuantityPurchased(), Element.ALIGN_LEFT, 1, bold);
                    if (listData.get(i).getNoOfUnitSold() != null) {
                        insertCell(table1, "" + listData.get(i).getNoOfUnitSold(), Element.ALIGN_LEFT, 1, green_font);
                    } else {
                        insertCell(table1, "-", Element.ALIGN_LEFT, 1, normal);
                    }

                    if (listData.get(i).getRemainingUnitToSold() != null) {
                        insertCell(table1, "" + listData.get(i).getRemainingUnitToSold(), Element.ALIGN_LEFT, 1, red_font);
                    } else {
                        insertCell(table1, "-", Element.ALIGN_LEFT, 1, normal);
                    }

                }
                if (responsesGlobal != null && responsesGlobal.getTotalAmount() != null) {
                    insertCell(table1, getDynamicLanguageValue(getApplicationContext(), "pending_amount_rs", R.string.pending_amount_rs), Element.ALIGN_LEFT, 4, bold);

                    if (responsesGlobal.getBalanceAmount() != null) {
                        insertCell(table1, "" + responsesGlobal.getBalanceAmount(), Element.ALIGN_LEFT, 3, bold);
                    } else {
                        insertCell(table1, "-", Element.ALIGN_LEFT, 3, normal);
                    }

                    insertCell(table1, getDynamicLanguageValue(getApplicationContext(), "collected_amount", R.string.collected_amount), Element.ALIGN_LEFT, 4, bold);

                    if (responsesGlobal.getCollectedAmount() != null) {
                        insertCell(table1, "" + responsesGlobal.getCollectedAmount(), Element.ALIGN_LEFT, 3, bold);
                    } else {
                        insertCell(table1, "-", Element.ALIGN_LEFT, 3, normal);
                    }

                    insertCell(table1, getDynamicLanguageValue(getApplicationContext(), "total_amount_rs", R.string.total_amount_rs), Element.ALIGN_LEFT, 4, bold);

                    if (responsesGlobal.getTotalAmount() != null) {
                        insertCell(table1, "" + responsesGlobal.getTotalAmount(), Element.ALIGN_LEFT, 3, bold);
                    } else {
                        insertCell(table1, "-", Element.ALIGN_LEFT, 3, normal);
                    }


                }


            }
            //create a paragraph
            Paragraph paragraph = new Paragraph();
            Chunk chunk0 = new Chunk(getDynamicLanguageValue(getApplicationContext(), "order_no", R.string.order_no) + " - " + responsesGlobal.getOrderNumber(), bold_XL);
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


                Uri path = FileProvider.getUriForFile(ProductOrderDetailsActivity.this, BuildConfig.APPLICATION_ID + ".provider", file);

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
        setFontsStyleTxt(this, binding.orderNumber, 5);
        setFontsStyleTxt(this, binding.txttotalAmount, 6);
        setFontsStyleTxt(this, binding.totalAmount, 5);
        setFontsStyleTxt(this, binding.txtcollectAmount, 6);
        setFontsStyleTxt(this, binding.collectAmount, 5);
        setFontsStyleTxt(this, binding.pendingAmountRs, 6);
        setFontsStyleTxt(this, binding.pendingAmount, 5);
        setFontsStyle(this, binding.payNowBtn);

        //Tab Service
        setDynamicLanguage(this, binding.txtFarmRegistration, "order_details", R.string.order_details);
        setDynamicLanguage(this, binding.txttotalAmount, "total_amount_rs", R.string.total_amount_rs);
        setDynamicLanguage(this, binding.txtcollectAmount, "collected_amount", R.string.collected_amount);
        setDynamicLanguage(this, binding.pendingAmountRs, "pending_amount_rs", R.string.pending_amount_rs);

    }
}

