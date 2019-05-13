package common;

import android.app.Application;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Environment;
import android.os.StrictMode;
import android.widget.EditText;

import com.instamojo.android.Instamojo;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

import model.Offers;
import model.ProfileDataset;
import model.VendorReportDataset;


/**
 * Created by Honey Singh on 5/7/2017.
 */

public class AppController extends Application {
    Typeface typeface;
    Typeface typeface1;
    Offers offer=null;
    EditText enterCoupon;
    String token=null;
    ProfileDataset profile;
    CustomImageView profilePicView;
    PrefManager manger;
    SharedPreferences prfs;
    @Override
    public void onCreate() {
        super.onCreate();
        Instamojo.initialize(this);
        FontsOverride.setDefaultFont(this, "MONOSPACE", "rale.ttf");
        manger=new PrefManager(this);
        typeface= Typeface.createFromAsset(getAssets(), "font.ttf");
        typeface1= Typeface.createFromAsset(getAssets(), "font.otf");
        makeFolder(String.valueOf(Environment.getExternalStorageDirectory()), "/1bazaar");
        Common.sdCardPath= Environment.getExternalStorageDirectory()+"/1bazaar";
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
    }

    public PrefManager getPrefsManger() {
        return manger;
    }

    public void setProfilePicView(CustomImageView profilePicView) {
        this.profilePicView = profilePicView;
    }

    public CustomImageView getProfilePicView() {
        return profilePicView;
    }

    public Typeface getTypeface() {
        return typeface;
    }
    public Typeface getBoldTypeface() {
        return typeface1;
    }
    public static void makeFolder(String path, String folder) {
        File directory = new File(path, folder);
        if (directory.exists() == false) {
            directory.mkdirs();
        }

    }
    public void setProfile(ProfileDataset profile)
    {
        this.profile=profile;
    }

    public ProfileDataset getProfile() {
        return profile;
    }

    public void setPaymentGatewayToken(String token)
{this.token=token;

}



    public String getPaymentGatewayToken() {
        return token;
    }

    public void setEnterCoupon(EditText enterCoupon) {
        this.enterCoupon = enterCoupon;
    }

    public EditText getEnterCoupon() {
        return enterCoupon;
    }

    public String createPdf(ArrayList<VendorReportDataset> list, String fileName) throws FileNotFoundException, DocumentException {
        Document document = new Document();
        PdfPTable table = new PdfPTable(new float[]{10, 14, 10, 10, 10, 10});
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        Font f1 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 14);
        f1.setColor(BaseColor.WHITE);
        Font f2 = FontFactory.getFont(FontFactory.HELVETICA, 12);
        f2.setColor(BaseColor.GRAY);
        table.addCell(new Phrase("Product Name", f1));
        table.addCell(new Phrase("Measurement", f1));
        table.addCell(new Phrase("Quantity", f1));
        table.addCell(new Phrase("Delivery Address", f1));
        table.addCell(new Phrase("Duration", f1));
        table.addCell(new Phrase("Timming", f1));
        table.setHeaderRows(1);
        PdfPCell[] cells = table.getRow(0).getCells();
        for (int j = 0; j < cells.length; j++) {
            cells[j].setBackgroundColor(BaseColor.BLACK);

        }
        for (int i = 1; i < list.size()+1; i++) {
            VendorReportDataset vds = list.get(i-1);
            table.addCell(new Phrase(vds.getProductName(),f2));
            table.addCell(new Phrase(vds.getPrductMeasurement(),f2));
            table.addCell(new Phrase(vds.getProductQuantity(),f2));
            table.addCell(new Phrase(vds.getDeliveryAddress(),f2));
            table.addCell(new Phrase(vds.getDeliveryDuration(),f2));
            table.addCell(new Phrase(vds.getDeliveryTimming(),f2));
            if(i%2==0)
            { cells = table.getRow(i).getCells();
                for (int j = 0; j < cells.length; j++) {
                    cells[j].setBackgroundColor(BaseColor.WHITE);

                }
            }

        }
        String path = Environment.getExternalStorageDirectory().toString();
        File directory = new File(path, "1Bazaar");

        if (!directory.exists()) {
            directory.mkdirs();
        }
        File f = new File(path, "1Bazaar/" + fileName + ".pdf");
        if (f.exists()) {
            f.delete();
        }
        PdfWriter.getInstance(document, new FileOutputStream(path + "/1Bazaar/" + fileName + ".pdf"));
        document.open();
        document.add(table);
        document.close();
        return "Sucess";
    }


    public void setOffer(Offers offer) {
        this.offer = offer;
    }

    public Offers getOffer() {
        return offer;
    }
    public void resetOffer() {
       offer=null;
    }
}
