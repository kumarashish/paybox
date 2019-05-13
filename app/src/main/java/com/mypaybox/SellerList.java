package com.mypaybox;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import adapter.VendorIistAdapter;
import common.ApiCall;
import common.Common;
import common.Utils;
import login.Login;
import model.VendorDataset;

/**
 * Created by Honey Singh on 4/11/2017.
 */

public class SellerList extends AppCompatActivity implements View.OnClickListener{
    public static String heading="";
    ImageView back;
    TextView headingView;
    ListView list;
    ProgressDialog pd;
    public static String brandId="";
    ArrayList<VendorDataset> dataList=new ArrayList<VendorDataset>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sellerlist);
        initializeAll();
    }

    public void initializeAll() {
        Typeface typeface= Typeface.createFromAsset(getAssets(), "lato_light.ttf");
        back = (ImageView) findViewById(R.id.back);
        headingView = (TextView) findViewById(R.id.heading);
        list=(ListView) findViewById(R.id.listView);
        back.setOnClickListener(this);
        headingView.setText(heading +" Vendor's");
        headingView.setTypeface(typeface);
       if(Utils.isInternetAvailable(SellerList.this))
       {
           new GetData().execute();
       }else{
           Toast.makeText(SellerList.this,"Internet Unavailable", Toast.LENGTH_SHORT).show();
       }

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == back.getId()) {
            onBackPressed();
        }
    }

    public class GetData extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(SellerList.this);
            pd.setMessage("Please wait....");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        public String doInBackground(String... params) {
            String response = "";
           response = ApiCall.getData(Common.getVendorList + "" + brandId);
          //  response = ApiCall.getData(Common.getVendorList + "f363743f-2934-4f3f-a8a9-4fcf9fe8b90e" );
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            JsonParsing(s);
            if (pd != null) {
                pd.cancel();
            }
        }

        public void JsonParsing(String s) {
            try {
                dataList.clear();
                JSONArray jsonArray=new JSONArray(s);
                for(int i=0;i<jsonArray.length();i++)
                {
              JSONObject job=jsonArray.getJSONObject(i);
                    VendorDataset vds=new VendorDataset(job);
                    dataList.add(vds);
                }
                if(dataList.size()>0) {
                    list.setAdapter(new VendorIistAdapter(SellerList.this, dataList));
                }else{
                    Toast.makeText(SellerList.this, "No vendor for seleted item.", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception ex) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (!jsonObject.isNull("Message")) {
                        PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().clear().apply();
                        Toast.makeText(SellerList.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
                        Intent in = new Intent(SellerList.this, Login.class);
                        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(in);


                    }
                } catch (Exception exx) {
                    exx.fillInStackTrace();
                }
            }
        }
    }
}
