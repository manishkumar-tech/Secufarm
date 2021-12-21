package com.weather.risk.mfi.myfarminfo.pdfgenerate;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.weather.risk.mfi.myfarminfo.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

//https://stackoverflow.com/questions/34296149/creating-a-pdf-file-in-android-programmatically-and-writing-in-it
//Without Image
public class PDFGenerate_new extends AppCompatActivity {

    Button btn_generate;
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pdfgenerate);

        text = (TextView) findViewById(R.id.text);
        btn_generate = (Button) findViewById(R.id.btn_generate);
        btn_generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stringtopdf(text.getText().toString());
            }
        });
    }

    public void stringtopdf(String datavalue) {
//        String extstoragedir = Environment.getExternalStorageDirectory().toString();
//        File fol = new File(extstoragedir, "pdf");
//        File folder = new File(fol, "pdf");
//        if (!folder.exists()) {
//            boolean bool = folder.mkdir();
//        }
        try {
            File sdcard = Environment.getExternalStorageDirectory();
            File outputFile = new File(sdcard, "MFI");
            if (!outputFile.exists())
                outputFile.createNewFile();

//            File data = Environment.getDataDirectory();
//            File inputFile = new File(data, "PDF/");
//            String targetPdf = "samplenew.pdf";
            String targetPdf = "/sdcard/testnew.pdf";
            final File file = new File(targetPdf);
//            final File file = new File(folder, "sample.pdf");
            file.createNewFile();
            FileOutputStream fOut = new FileOutputStream(file);


            PdfDocument document = new PdfDocument();
            PdfDocument.PageInfo pageInfo = new
                    PdfDocument.PageInfo.Builder(300, 300, 1).create();
            PdfDocument.Page page = document.startPage(pageInfo);
            Canvas canvas = page.getCanvas();
            Paint paint = new Paint();

            canvas.drawText(datavalue, 10, 10, paint);


            document.finishPage(page);
            document.writeTo(fOut);
            document.close();

        } catch (IOException e) {
            Log.i("error", e.getLocalizedMessage());
        }
    }
}
