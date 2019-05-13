package com.mypaybox;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import adapter.VendorReportListAdapter;
import common.ApiCall;
import common.AppController;
import common.Common;
import common.Utils;
import model.VendorReportDataset;

/**
 * Created by Ashish.Kumar on 16-05-2017.
 */

public class VendorReport extends AppCompatActivity implements View.OnClickListener{
    public static String heading="";
    ImageView back;
    TextView headingView;
    ListView list;
    ProgressDialog pd;
    public static String brandId="";
    ArrayList<VendorReportDataset> dataList=new ArrayList<VendorReportDataset>();
    Button email;
AppController controller;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sellerlist);
        initializeAll();
    }

    public void initializeAll() {
        controller=(AppController)getApplicationContext();
        Typeface typeface= Typeface.createFromAsset(getAssets(), "lato_light.ttf");
        back = (ImageView) findViewById(R.id.back);
        headingView = (TextView) findViewById(R.id.heading);
        email=(Button) findViewById(R.id.email);
        list=(ListView) findViewById(R.id.listView);
        back.setOnClickListener(this);
        headingView.setText("Delivery Report");
        headingView.setTypeface(typeface);
        if(Utils.isInternetAvailable(VendorReport.this))
        {
            new GetData().execute();
        }else{
            Toast.makeText(this,"Internet Unavailable", Toast.LENGTH_SHORT).show();
        }
       email.setOnClickListener(this);
    }
    public class GetData extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(VendorReport.this);
            pd.setMessage("Please wait....");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        public String doInBackground(String... params) {
            String response = "";
            response = ApiCall.getData(Common.getVendorReport + "" + Common.userId);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            jsonParsing(s);
            if (pd != null) {
                pd.cancel();
            }
        }
    }

    public class GenerateReport extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(VendorReport.this);
            pd.setMessage("Please wait!\nGenerating Report.....");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        public String doInBackground(String... params) {
            String response = "";
            try {
               return controller.createPdf(dataList, "Report("+ Common.UserName+")");
            }catch (Exception ex)
            {

                ex.fillInStackTrace();
                return "exception";
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s.equalsIgnoreCase("Sucess"))
            {
                sendEmail();
            }else{
                Toast.makeText(VendorReport.this,"Exception occured while generating report.Try again", Toast.LENGTH_SHORT).show();
            }

            if (pd != null) {
                pd.cancel();
            }
        }
    }
public void jsonParsing(String s)
{
    try{
        JSONArray jsonArray=new JSONArray(s);
        for(int i=0;i<jsonArray.length();i++)
        {
            JSONObject jsonObject=jsonArray.getJSONObject(i);
            VendorReportDataset ds=new VendorReportDataset(jsonObject);
            dataList.add(ds);
        }
        if (dataList.size() > 0) {
            email.setVisibility(View.VISIBLE);
            list.setAdapter(new VendorReportListAdapter(VendorReport.this, dataList));
        } else {
            email.setVisibility(View.GONE);
            Toast.makeText(VendorReport.this, "No Order.", Toast.LENGTH_SHORT).show();
        }

    }catch (Exception ex)
    {
        ex.fillInStackTrace();
    }
}
    @Override
    public void onClick(View v) {
        if (v.getId() == back.getId()) {
            onBackPressed();
        }else if(v.getId()==email.getId())
        {
if(dataList.size()>0)
{
   new GenerateReport().execute();
}else{
    Toast.makeText(this,"You dont have any order", Toast.LENGTH_SHORT).show();
}
        }
    }
    private void sendEmail(){
        File filelocation = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/1bazaar", "Report("+ Common.UserName+")"+".pdf");
        Uri path = Uri.fromFile(filelocation);
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:" + Common.UserName));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Delivery Report");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Hi! Please find your report");
        emailIntent.putExtra(Intent.EXTRA_STREAM, path);
        try {
            startActivityForResult(Intent.createChooser(emailIntent, "Send email using..."),1);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "No email clients installed.", Toast.LENGTH_SHORT).show();
        }

    }
}
