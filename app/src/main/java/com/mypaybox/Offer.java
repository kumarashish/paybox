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

import adapter.OfferAdapter;
import common.ApiCall;
import common.Common;
import common.Utils;
import model.Offers;

/**
 * Created by Honey Singh on 7/16/2017.
 */

public class Offer extends Activity implements View.OnClickListener {
    ImageView back;
    TextView headingView;
    ListView list;
    ProgressDialog pd;
    ArrayList<Offers> offerList = new ArrayList<>();
public static int amount=0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sellerlist);
        initializeAll();
        if (Utils.isInternetAvailable(Offer.this)) {
            new GetOrderHistory().execute();
        } else {
            Toast.makeText(this, "Internet Unavailable.", Toast.LENGTH_SHORT).show();
        }
    }

    public void initializeAll() {
        Typeface typeface = Typeface.createFromAsset(getAssets(), "font.ttf");
        back = (ImageView) findViewById(R.id.back);
        headingView = (TextView) findViewById(R.id.heading);
        headingView.setTypeface(typeface);
        list = (ListView) findViewById(R.id.listView);
        back.setOnClickListener(this);
        headingView.setText("My Offers");
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.back) {
            onBackPressed();
        }
    }

    public class GetOrderHistory extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(Offer.this);
            pd.setMessage("Please wait....");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        public String doInBackground(String... params) {
            String response = ApiCall.getData(Common.getOffers + "" + Common.UserName);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            ParseData(s);
            if (pd != null) {
                pd.cancel();
            }
        }
    }

    public void ParseData(String s) {
        offerList.clear();
        if (s != null) {
            try {
                JSONArray jsonArray = new JSONArray(s);
                if (jsonArray.length() > 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject job = jsonArray.getJSONObject(i);
                       Offers ds = new Offers(job);
                        offerList.add(ds);
                    }
                    if (offerList.size() > 0) {
                        list.setAdapter(new OfferAdapter( Offer.this,offerList));
                    }
                } else {
                    Toast.makeText(this, "No Offer.", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception ex) {
                ex.fillInStackTrace();
            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        amount=0;
    }
}