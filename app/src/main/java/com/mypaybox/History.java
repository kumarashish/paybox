package com.mypaybox;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import adapter.TransactionHistoryAdapter;
import common.ApiCall;
import common.Common;
import common.Utils;
import model.TransactionHistoryModel;


/**
 * Created by Ashish.Kumar on 26-12-2017.
 */

public class History  extends Activity implements View.OnClickListener {
    ImageView back;
    TextView headingView;
    ListView list;
    ProgressDialog pd;
ArrayList<TransactionHistoryModel> transactionList=new ArrayList<>();
TextView noData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sellerlist);
        initializeAll();
        if (Utils.isInternetAvailable(History.this)) {

            new GetData().execute();

        }

    }

    public void initializeAll() {
        Typeface typeface = Typeface.createFromAsset(getAssets(), "font.ttf");
        back = (ImageView) findViewById(R.id.back);
        headingView = (TextView) findViewById(R.id.heading);
        noData = (TextView) findViewById(R.id.noData);
        headingView.setTypeface(typeface);
        headingView.setText("Transaction History ");
        list = (ListView) findViewById(R.id.listView);
        back.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                onBackPressed();
                break;


        }
    }




    public class GetData extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(History.this);
            pd.setMessage("Please wait....");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        public String doInBackground(String... params) {

               String response = ApiCall.getData(Common.getTransactionHistoryUrl(Common.userId));

            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONArray jsonArray = new JSONArray(s);
                if (jsonArray.length()>0)
                {
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        transactionList.add(new TransactionHistoryModel(jsonObject.getJSONObject("Property")));
                    }
                    if(transactionList.size()>0)
                    {

                        list.setVisibility(View.VISIBLE);
                        noData.setVisibility(View.GONE);
                      list.setAdapter(new TransactionHistoryAdapter(transactionList,History.this));

                    }
                }else{
                    list.setVisibility(View.GONE);
                    noData.setVisibility(View.VISIBLE);
                    noData.setText("No Transactions");
                    Toast.makeText(History.this,"No Transactions", Toast.LENGTH_SHORT).show();
                }
            }catch (Exception ex)
            {
                ex.fillInStackTrace();
            }
                if (pd!=null) {
                     pd.cancel();
                }
            }

        }
    }
