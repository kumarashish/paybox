package com.mypaybox;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import adapter.CityAdapter;
import adapter.DeliverableListAdpter;
import adapter.LocalityAdapter;
import common.ApiCall;
import common.Common;
import common.Utils;
import interfaces.LocalityCallBack;
import login.Login;
import model.CityDataset;
import model.DeliverableDataset;
import model.LocalityDataset;

/**
 * Created by Honey Singh on 5/27/2017.
 */

public class MyDeliverables extends Activity implements View.OnClickListener,interfaces.Callback, LocalityCallBack {
    ImageView back;
    TextView headingView;
    ListView list;
    ProgressDialog pd;
    TextView city,locality;
    Button update;
    ArrayList<CityDataset> cityList=null;
    ArrayList<LocalityDataset> localityList=null;
    Boolean isCity=false;
    boolean isLocalityRequested=false;
    boolean isProductListRequested=false;
    boolean isPostDataRequested=false;
    public static int cityId=0;
    public static String localityId="";
    ArrayList<DeliverableDataset> deliverableList=new ArrayList<>();
    ArrayList<DeliverableDataset> deliveringList=new ArrayList<>();
    DeliverableListAdpter adapter;
    boolean isLocalitySelected=false;
    boolean isCitySelected=false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mydeliverables);
        initializeAll();
        if (Utils.isInternetAvailable(MyDeliverables.this)) {
            isCity = true;
            new GetData().execute();
            Thread t=new Thread(new Runnable() {
                @Override
                public void run() {
                isProductListRequested=true;
                   String response = ApiCall.getData(Common.getDeliverableItemsUrl("",""));
                    ProductJSONParsing(response);
                }
            });
            t.start();
        }

    }

    public void initializeAll() {
        Typeface typeface = Typeface.createFromAsset(getAssets(), "lato_light.ttf");
        back = (ImageView) findViewById(R.id.back);
        headingView = (TextView) findViewById(R.id.heading);
        headingView.setTypeface(typeface);
        city = (TextView) findViewById(R.id.city);
        locality = (TextView) findViewById(R.id.locality);
        update = (Button) findViewById(R.id.done);
        list = (ListView) findViewById(R.id.listView);
        back.setOnClickListener(this);
        headingView.setText("My Deliverables");
        city.setOnClickListener(this);
        locality.setOnClickListener(this);
        update.setOnClickListener(this);
        update.setVisibility(View.GONE);
        update.setTypeface(typeface);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                onBackPressed();
                break;

            case R.id.city:
                if (cityList != null) {
                    showAlertDialog("Select City", 0);
                } else {
                    Intent in = new Intent(MyDeliverables.this, Login.class);
                    startActivity(in);
                    finish();
                    Toast.makeText(MyDeliverables.this, "Session Expired ,Please login.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.locality:
                if (localityList != null) {
                    showAlertDialog("Select Locality", 1);
                } else {
                    Toast.makeText(MyDeliverables.this, "Please select city first.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.done:
                if(isCitySelected==true)
                {
                    if(isLocalitySelected==true)
                    { isPostDataRequested=true;
                        new GetData().execute();
                    }else{
                        Toast.makeText(MyDeliverables.this,"Please select locality.", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(MyDeliverables.this,"Please select city.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    public void showAlertDialog(String heading, final int val) {
        final Dialog dialog = new Dialog(MyDeliverables.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert);


        // set the custom dialog components - text, image and button
        TextView header = (TextView) dialog.findViewById(R.id.header);
        header.setText(heading);
        ListView listView = (ListView) dialog.findViewById(R.id.list);

        switch (val) {
            case 0:
                listView.setAdapter(new CityAdapter(MyDeliverables.this,cityList,city,dialog,this));
                break;
            case 1:
                listView.setAdapter(new LocalityAdapter(MyDeliverables.this,localityList,locality,dialog,this));
                break;
        }

        dialog.show();
    }

    @Override
    public void onClick(int pos) {
        isLocalitySelected=false;
        isCitySelected=true;
        isLocalityRequested=true;
        cityId=pos;
        locality.setText("Select Locality");

        new GetData().execute();
    }

    @Override
    public void onLocalityClicked(int pos) {
         isLocalitySelected=true;
        isProductListRequested=true;
        localityId=localityList.get(pos).getLocalityId();
        new GetData().execute();
    }

    public class GetData extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(MyDeliverables.this);
            pd.setMessage("Please wait....");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        public String doInBackground(String... params) {
            String response="";
            if(isCity==true) {
                response = ApiCall.getData(Common.getAllCategoriesUrl);
            }
            else if (isLocalityRequested==true) {
                response = ApiCall.getData(Common.getLocalityUrl+""+cityId);
            }
            else if(isProductListRequested==true)
            {
                response = ApiCall.getData(Common.getDeliverableItemsUrl(localityId, Common.userId));
            }
            else if(isPostDataRequested==true)
            {
                response= ApiCall.postData(Common.vendorCapityUpdateUrl,getDeliverableListJSONObject());
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(isCity==true) {
                isCity=false;
                JSONParsing(s);
            }else if(isLocalityRequested==true){
                localityJSONParsing(s);
                isLocalityRequested=false;
            }else if(isProductListRequested==true)
            {
                isProductListRequested=false;
                ProductDeliveringJSONParsing(s);
            }
            else if(isPostDataRequested==true)
            {
                if(s.equalsIgnoreCase("true")) {
                    isPostDataRequested = false;
                    Toast.makeText(MyDeliverables.this,"Deliverable capacity updated Sucessfully.", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MyDeliverables.this,s, Toast.LENGTH_SHORT).show();
                }
            }
            if (pd != null) {
                pd.cancel();
            }
        }
    }

    public JSONObject getDeliverableListJSONObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Id", Common.userId);
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < deliverableList.size(); i++) {
                if (deliverableList.get(i).getEnabled() == true) {
                    JSONObject item = new JSONObject();
                    item.put("vendorCapacity", deliverableList.get(i).getQuantity());
                    item.put("Id", deliverableList.get(i).getItemId());
                    item.put("localityId",localityId);
                    if (jsonArray.length() == 0) {
                        jsonArray.put(0, item);
                    } else if (jsonArray.length() == 1) {
                        jsonArray.put(1, item);
                    } else {
                        jsonArray.put(jsonArray.length() - 1, item);
                    }
                }
            }
            if (jsonArray.length() > 0) {
                jsonObject.put("items", jsonArray);
            }
        } catch (Exception ex) {
            ex.fillInStackTrace();
        }
        return jsonObject;
    }
    public void localityJSONParsing(String s) {
        try {
            JSONArray jsonArray = new JSONArray(s);
            {
                localityList = new ArrayList<LocalityDataset>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject job = jsonArray.getJSONObject(i);
                    LocalityDataset ds = new LocalityDataset(job);
                    localityList.add(ds);
                }
                showAlertDialog("Select Locality", 1);
            }
        } catch (Exception ex) {
            ex.fillInStackTrace();
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (!jsonObject.isNull("Message")) {
                    PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().clear().apply();
                    Toast.makeText(MyDeliverables.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(MyDeliverables.this, Login.class);
                    startActivity(in);
                    finish();
                }
            } catch (Exception exx) {
                exx.fillInStackTrace();
            }
        }
    }

    public void JSONParsing(String s) {
        try {
            JSONObject jsonObject = new JSONObject(s);
            if (!jsonObject.isNull("Message")) {
                PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().clear().apply();
                Toast.makeText(MyDeliverables.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
                Intent in = new Intent(MyDeliverables.this, Login.class);
                startActivity(in);
                finish();

            } else {
                if (!jsonObject.isNull("cities")) {
                    cityList = new ArrayList<CityDataset>();
                    JSONArray jsonArray = jsonObject.getJSONArray("cities");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        CityDataset ds = new CityDataset(object);
                        cityList.add(ds);
                    }
                }

            }
        } catch (Exception ex) {
            ex.fillInStackTrace();
        }
    }

    public void ProductDeliveringJSONParsing(String s) {
        try {
            deliveringList.clear();
            JSONArray jsonArray = new JSONArray(s);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                JSONArray items = jsonObject.getJSONArray("items");
                if (items.length() > 0) {
                    for (int j = 0; j < items.length(); j++) {
                        JSONObject job = items.getJSONObject(j);
                        deliveringList.add(new DeliverableDataset(job));
                    }
                }
            }
            if (deliveringList.size() > 0) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter = new DeliverableListAdpter(MyDeliverables.this, deliverableList,deliveringList);
                        list.setAdapter(adapter);
                    }
                });
            }
        } catch (Exception ex) {
            ex.fillInStackTrace();
        }
        update.setVisibility(View.VISIBLE);
    }
    public void ProductJSONParsing(String s) {
        try {
            deliverableList.clear();
            JSONArray jsonArray = new JSONArray(s);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                JSONArray items = jsonObject.getJSONArray("items");
                if (items.length() > 0) {
                    for (int j = 0; j < items.length(); j++) {
                        JSONObject job = items.getJSONObject(j);
                        deliverableList.add(new DeliverableDataset(job));
                    }
                }
            }
//            if (deliverableList.size() > 0) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        adapter = new DeliverableListAdpter(MyDeliverables.this, deliverableList);
//                        list.setAdapter(adapter);
//                    }
//                });
//            }
        } catch (Exception ex) {
            ex.fillInStackTrace();
        }
    }
}
