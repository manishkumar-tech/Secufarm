package com.weather.risk.mfi.myfarminfo.pdfgenerate;


import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.weather.risk.mfi.myfarminfo.R;

import org.json.JSONObject;

import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.Iterator;

import static com.weather.risk.mfi.myfarminfo.customcamera.CameraSurfaceView.IMAGE_DIRECTORY;


//https://itextpdf.com/en/resources/installation-guides/installing-itext-g-android
public class PDFGenerate_a4size extends Activity {
    private static final int STORAGE_CODE = 1000;

    Button btn_generate;
    TextView text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pdfgenerate);

        text = (TextView) findViewById(R.id.text);
        btn_generate = (Button) findViewById(R.id.btn_generate);

        String value = "A document is a written, drawn, presented, or memorialized representation of thought. The word originates from the " +
                "Latin documentum, which denotes a \"teaching\" or \"lesson\": the verb doceō denotes \"to teach\". In the past, the " +
                "word was usually used to denote a written proof useful as evidence of a truth or fact. In the computer age, \"document\" " +
                "usually denotes a primarily textual computer file, including its structure and format, e.g. fonts, colors, and images. " +
                "Contemporarily, \"document\" is not defined by its transmission medium, e.g., paper, given the existence of electronic " +
                "documents. \"Documentation\" is distinct because it has more denotations than \"document\". Documents are also distinguished" +
                " from \"realia\", which are three-dimensional objects that would otherwise satisfy the definition of \"document\" because " +
                "they memorialize or represent thought; documents are considered more as 2 dimensional representations. While documents are " +
                "able to have large varieties of customization, all documents are able to be shared freely, and have the right to do so," +
                " creativity can be represented by documents, also. History, events, examples, opinion, etc. all can be expressed in " +
                "documents. \nA document is a written, drawn, presented, or memorialized representation of thought. The word originates " +
                "from the Latin documentum, which denotes a \"teaching\" or \"lesson\": the verb doceō denotes \"to teach\". In the past, " +
                "the word was usually used to denote a written proof useful as evidence of a truth or fact. In the computer age, \"document\"" +
                " usually denotes a primarily textual computer file, including its structure and format, e.g. fonts, colors, and images. " +
                "Contemporarily, \"document\" is not defined by its transmission medium, e.g., paper, given the existence of electronic " +
                "documents. \"Documentation\" is distinct because it has more denotations than \"document\". Documents are also distinguished" +
                " from \"realia\", which are three-dimensional objects that would otherwise satisfy the definition of \"document\" because " +
                "they memorialize or represent thought; documents are considered more as 2 dimensional representations. While documents are " +
                "able to have large varieties of customization, all documents are able to be shared freely, and have the right to do so, " +
                "creativity can be represented by documents, also. History, events, examples, opinion, etc. all can be expressed in " +
                "documents.\nA document is a written, drawn, presented, or memorialized representation of thought. The word originates " +
                "from the Latin documentum, which denotes a \"teaching\" or \"lesson\": the verb doceō denotes \"to teach\". In the past, " +
                "the word was usually used to denote a written proof useful as evidence of a truth or fact. In the computer age, \"document\" " +
                "usually denotes a primarily textual computer file, including its structure and format, e.g. fonts, colors, and images. " +
                "Contemporarily, \"document\" is not defined by its transmission medium, e.g., paper, given the existence of electronic " +
                "documents. \"Documentation\" is distinct because it has more denotations than \"document\". Documents are also distinguished " +
                "from \"realia\", which are three-dimensional objects that would otherwise satisfy the definition of \"document\" because they " +
                "memorialize or represent thought; documents are considered more as 2 dimensional representations. While documents are able to " +
                "have large varieties of customization, all documents are able to be shared freely, and have the right to do so, creativity can " +
                "be represented by documents, also. History, events, examples, opinion, etc. all can be expressed in documents.\nA document is a " +
                "written, drawn, presented, or memorialized representation of thought. The word originates from the Latin documentum, which denotes " +
                "a \"teaching\" or \"lesson\": the verb doceō denotes \"to teach\". In the past, the word was usually used to denote a written proof " +
                "useful as evidence of a truth or fact. In the computer age, \"document\" usually denotes a primarily textual computer file, including " +
                "its structure and format, e.g. fonts, colors, and images. Contemporarily, \"document\" is not defined by its transmission medium, e.g., " +
                "paper, given the existence of electronic documents. \"Documentation\" is distinct because it has more denotations than \"document\"." +
                " Documents are also distinguished from \"realia\", which are three-dimensional objects that would otherwise satisfy the definition of " +
                "\"document\" because they memorialize or represent thought; documents are considered more as 2 dimensional representations. " +
                "While documents are able to have large varieties of customization, all documents are able to be shared freely, and have the right " +
                "to do so, creativity can be represented by documents, also. History, events, examples, opinion, etc. all can be expressed in documents." +
                "\nA document is a written, drawn, presented, or memorialized representation of thought. The word originates from the Latin documentum, " +
                "which denotes a \"teaching\" or \"lesson\": the verb doceō denotes \"to teach\". In the past, the word was usually used to denote a " +
                "written proof useful as evidence of a truth or fact. In the computer age, \"document\" usually denotes a primarily textual computer " +
                "file, including its structure and format, e.g. fonts, colors, and images. Contemporarily, \"document\" is not defined by its " +
                "transmission medium, e.g., paper, given the existence of electronic documents. \"Documentation\" is distinct because it has more " +
                "denotations than \"document\". Documents are also distinguished from \"realia\", which are three-dimensional objects that would " +
                "otherwise satisfy the definition of \"document\" because they memorialize or represent thought; documents are considered more as 2 " +
                "dimensional representations. While documents are able to have large varieties of customization, all documents are able to be shared " +
                "freely, and have the right to do so, creativity can be represented by documents, also. History, events, examples, opinion, etc. all " +
                "can be expressed in documents.\nA document is a written, drawn, presented, or memorialized representation of thought. The word " +
                "originates from the Latin documentum, which denotes a \"teaching\" or \"lesson\": the verb doceō denotes \"to teach\". In the past, " +
                "the word was usually used to denote a written proof useful as evidence of a truth or fact. In the computer age, \"document\" usually " +
                "denotes a primarily textual computer file, including its structure and format, e.g. fonts, colors, and images. Contemporarily, " +
                "\"document\" is not defined by its transmission medium, e.g., paper, given the existence of electronic documents. \"Documentation\"" +
                " is distinct because it has more denotations than \"document\". Documents are also distinguished from \"realia\", which are " +
                "three-dimensional objects that would otherwise satisfy the definition of \"document\" because they memorialize or represent thought; " +
                "documents are considered more as 2 dimensional representations. While documents are able to have large varieties of customization, all " +
                "documents are able to be shared freely, and have the right to do so, creativity can be represented by documents, also. History, events, " +
                "examples, opinion, etc. all can be expressed in documents.";


        text.setText(value);

        btn_generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                savePdf();
                savePdfTable();
            }
        });
    }

    public JSONObject getjsonvalue() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Project Name", "Auroch");
            jsonObject.put("Farm Name", "Ashok Farm");
            jsonObject.put("Farmer Name", "Ashok Kumar");
            jsonObject.put("Farmer Phone Number", "9990909092");
            jsonObject.put("State Name", "Haryana");
            jsonObject.put("District Name", "Gurgaon");
            jsonObject.put("Sub District Name", "Farrukhnagar");
            jsonObject.put("Other Sub District Name", "New Farrukhnagar");
            jsonObject.put("Village Name", "Farrukhnagar");
            jsonObject.put("Other Village Name", "New Farrukhnagar");
            jsonObject.put("Tagged Farm Area (Acre)", "2.122");
            jsonObject.put("Crop", "Cotton");
            jsonObject.put("Variety", "F-1378");
            jsonObject.put("Sow Period From", "10-05-2019");
            jsonObject.put("Sow Period To", "30-05-2019");
            jsonObject.put("Have you applied basal dose to your crop", "Yes");
            jsonObject.put("Please fill the basal dose table", "  ");
            jsonObject.put("Nitrogen (N)", " 20");
            jsonObject.put("Phosphorus (P)", " 10");
            jsonObject.put("Potassium (K)", " 14");
            jsonObject.put("What is your main concern", "Increase my revenue");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return jsonObject;
    }

    //https://www.mysamplecode.com/2012/10/create-table-pdf-java-and-itext.html
    public void savePdfTable() {
        Document doc = new Document();
        PdfWriter docWriter = null;

        DecimalFormat df = new DecimalFormat("0.00");
        try {
            //special font sizes
            Font bfBold12 = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, new BaseColor(0, 0, 0));
            Font bf12 = new Font(Font.FontFamily.TIMES_ROMAN, 12);

            //file path
            String mFileName = "testPDF";
            String mFilePath = Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY + "/" + mFileName + ".pdf";
            docWriter = PdfWriter.getInstance(doc, new FileOutputStream(mFilePath));
//            String path = "docs/" + "PDFFileNameTest";
//            docWriter = PdfWriter.getInstance(doc, new FileOutputStream(path));

            //document header attributes
            doc.addAuthor("betterThanZero");
            doc.addCreationDate();
            doc.addProducer();
            doc.addCreator("MySampleCode.com");
            doc.addTitle("Report with Column Headings");
            doc.setPageSize(PageSize.LETTER);

            //open document
            doc.open();

            //create a paragraph
            Paragraph paragraph = new Paragraph("You can write something !!");


            //specify column widths
            float[] columnWidths = {5f, 5f};
            //create PDF table with the given widths
            PdfPTable table = new PdfPTable(columnWidths);
            // set table width a percentage of the page width
            table.setWidthPercentage(90f);

            //insert column headings
            insertCell(table, "Question", Element.ALIGN_LEFT, 1, bfBold12);
            insertCell(table, "Answer", Element.ALIGN_LEFT, 1, bfBold12);
            table.setHeaderRows(1);

            //insert an empty row
//            insertCell(table, "", Element.ALIGN_LEFT, 4, bfBold12);
            //create section heading by cell merging
//            insertCell(table, "New York Orders ...", Element.ALIGN_LEFT, 4, bfBold12);
            double orderTotal, total = 0;

            //just some random data to fill

            JSONObject jsonObject = getjsonvalue();
            if (jsonObject != null && jsonObject.length() > 0) {
                Iterator<String> keysItr = jsonObject.keys();
                while (keysItr.hasNext()) {
                    String getkey = keysItr.next();
                    String getvalue = jsonObject.getString(getkey);
                    insertCell(table, getkey, Element.ALIGN_LEFT, 1, bf12);
                    insertCell(table, getvalue, Element.ALIGN_LEFT, 1, bf12);
                }
            }
            //add the PDF table to the paragraph
            paragraph.add(table);
            // add the paragraph to the document
            doc.add(paragraph);
            Toast.makeText(this, "PDF is completely generated", Toast.LENGTH_SHORT).show();

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
            cell.setMinimumHeight(10f);
        }
        //add the call to the table
        table.addCell(cell);

    }

    //simple document format
    public void savePdf() {
        //create object of document class
        Document mDoc = new Document();
        //pdf file name
        String mFileName = "texstPDF";
        String mFilePath = Environment.getExternalStorageDirectory() + "/" + mFileName + ".pdf";
        try {
            //create instance of PdfWriter class
            PdfWriter.getInstance(mDoc, new FileOutputStream(mFilePath));
            //open the document for writing
            mDoc.open();
            //get text value
            String mText = text.getText().toString();

            //add author of the document optional
            mDoc.addAuthor("Atif Pervaiz");
            //you can add more features like this

            //add paragragh to the document value
            mDoc.add(new Paragraph(mText));

            ////close the document
            mDoc.close();

            Toast.makeText(this, "PDF is generated", Toast.LENGTH_SHORT).show();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
