package com.mypaybox;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import adapter.Month_adapter;
import adapter.ServiceDetailsListAdapter;
import common.ApiCall;
import common.AppController;
import common.Common;
import common.ScreenshotUtils;
import common.Utils;
import fonepaisa.com.fonepaisapg_sdk.FPConstants;
import fonepaisa.com.fonepaisapg_sdk.fonePaisaPG;
import model.ServiceDataSet;

/**
 * Created by Ashish.Kumar on 21-08-2017.
 */

public class AppartmentDetails extends Activity implements View.OnClickListener {
    ImageView back;
    TextView headingView;
    ListView list;
    LinearLayout error;
    ProgressDialog pd;
    Button pay;
Spinner months;
SharedPreferences prfs;
    String[] Months={"Select Month","January","February","March","April","May","June","July","August","September","October","November","December"};
    String[] years={"Select Year","2016","2017","2018","2019"};
AppController controller;
    TextView appName,appAddress,duration,totalCost,balance,units;
    ArrayList<ServiceDataSet> ServiceDetailedList=new ArrayList<ServiceDataSet>();
    Button share;
    Typeface typeface;
    Spinner year;
    int FONEPAISAPG_RET_CODE = 22;
    String hashkey="";
    String invoiceId="";
    String amounValue="";
    int getPaymentHash=4;
    int apiCall=0;
    String transactionId="";

    String paymentMode=Common.net_Banking;
    double transactionFeesValue=0;
    double amountVal=0;
    String transactionType=Common.net_Banking;
    double totalValue=0.00;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appartment);
        initializeAll();

    }

    public void initializeAll() {
        typeface = Typeface.createFromAsset(getAssets(), "font.ttf");
        pay=(Button) findViewById(R.id.pay);
        back = (ImageView) findViewById(R.id.back);
        controller=(AppController) getApplicationContext();
        prfs= PreferenceManager.getDefaultSharedPreferences(AppartmentDetails.this);
        headingView = (TextView) findViewById(R.id.heading);
        error=(LinearLayout) findViewById(R.id.errorresult);
        share= (Button) findViewById(R.id.share);
        appName = (TextView) findViewById(R.id.appName);
        appAddress = (TextView) findViewById(R.id.address);
        duration = (TextView) findViewById(R.id.duration);
        totalCost = (TextView) findViewById(R.id.totalPrice);
        balance = (TextView) findViewById(R.id.balance);
        units = (TextView) findViewById(R.id.units);
        headingView.setTypeface(typeface);
        months=(Spinner) findViewById(R.id.months);
        year=(Spinner) findViewById(R.id.year);
        year.setAdapter(new Month_adapter(AppartmentDetails.this,years));
        list = (ListView) findViewById(R.id.listView);
        pay.setTypeface(typeface);
        back.setOnClickListener(this);
        headingView.setText("Maintenance");
        pay.setOnClickListener(this);
        year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position != 0) {

                    months.setAdapter(new Month_adapter(AppartmentDetails.this, Months));
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        months.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    new GetData().execute();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        months.setSelection(getCurrentMonth());
        share.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.back) {
            onBackPressed();
        } else if (v.getId() == share.getId()) {
            shareAdress();
        } else if (v.getId() == pay.getId()) {
         showPaymentPopUp();
        }
    }
    public void shareAdress()
    {
        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{""});
        email.putExtra(Intent.EXTRA_SUBJECT, "My Appartment Address");
        if(controller.getProfile()!=null) {
            email.putExtra(Intent.EXTRA_TEXT, "My Address : " + controller.getProfile().getAddress() + ", " + controller.getProfile().getCity() + ", " + controller.getProfile().getZipCode());

        }
        email.setType("message/rfc822");
        startActivity(Intent.createChooser(email, "Choose an Email client :"));
    }

    public class GetData extends AsyncTask<String, Void, String> {
        int month=months.getSelectedItemPosition();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(AppartmentDetails.this);
            pd.setMessage("Please wait....");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        public String doInBackground(String... params) {
            String response="";
           if(apiCall==getPaymentHash){
                JSONObject jsonObject=new JSONObject();
                try {
                    jsonObject.put("test", "test");
                } catch (Exception ex) {
                    ex.fillInStackTrace();
                }
                invoiceId=""+ System.currentTimeMillis();
                double amount=Math.round(amountVal+transactionFeesValue);
                response= ApiCall.post(Common.getPaymentHash(Double.toString(amount),invoiceId),jsonObject);
            }else {
               response = ApiCall.getData(Common.getAppartmentDetails(month, year.getSelectedItem().toString(),controller.getPrefsManger().getUserName()));
           }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(apiCall==getPaymentHash)
            {
                hashkey=s;
                pd.cancel();
                double amount=Math.round(amountVal+transactionFeesValue);
                //madePayment(invoiceId,Double.toString(amount), hashkey);

            }else {
                jsonParsing(s);
            }
            if (pd != null) {
                pd.cancel();
            }
        }
    }
    public  void jsonParsing(String s)
    {
        if(s!=null) {
            ServiceDetailedList.clear();
            try {
                JSONObject job = new JSONObject(s);
                if (!job.isNull("Cost")) {
                    JSONObject jsonObject = job.getJSONObject("Cost");
                    appName.setText("  " + jsonObject.getString("ApartmentName"));
                    appAddress.setText("  " + jsonObject.getString("Locality") + "," + jsonObject.getString("City"));
                    duration.setText("  " + job.getString("Duration"));
                    units.setText("  " + jsonObject.getString("TotalUnit" ) + "");
                    totalCost.setText("  Rs." + jsonObject.getString("TotalCost"));
                    amounValue=jsonObject.getString("userBalance");
                    balance.setText("  Rs." + jsonObject.getString("userBalance"));
                    amountVal=Double.parseDouble(jsonObject.getString("userBalance"));
                    JSONArray jsonArray = jsonObject.getJSONArray("ApartmentServices");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        ServiceDataSet ds = new ServiceDataSet(jsonArray.getJSONObject(i));
                        ServiceDetailedList.add(ds);
                    }

                }else{
                    list.setVisibility(View.GONE);
                    duration.setText("-");
                    units.setText("-");
                    totalCost.setText("-");
                    balance.setText("-");

                    Toast.makeText(AppartmentDetails.this,"Details not available for selected month.", Toast.LENGTH_SHORT).show();
                }
                }catch(Exception ex)
                {
                    ex.fillInStackTrace();
                }
                if (ServiceDetailedList.size() > 0) {
                    pay.setVisibility(View.VISIBLE);
                    list.setVisibility(View.VISIBLE);
                    list.setAdapter(new ServiceDetailsListAdapter(AppartmentDetails.this, ServiceDetailedList, Integer.parseInt(units.getText().toString().trim())));
                }else{
                    pay.setVisibility(View.GONE);
                    list.setVisibility(View.GONE);
                }

        }else{
            Toast.makeText(AppartmentDetails.this,"Details not available for selected month.", Toast.LENGTH_SHORT).show();
        }
    }

    public int getCurrentMonth() {
        DateFormat dateFormat = new SimpleDateFormat("MM");
        Date date = new Date();
        return Integer.parseInt(dateFormat.format(date));
    }
    public int getCurrentYear() {
        Date today = new Date(); // Fri Jun 17 14:54:28 PDT 2016 Calendar cal = Calendar.getInstance();

        Calendar c = Calendar.getInstance();

       int digyear = c.get(Calendar.YEAR);
        String yrStr = Integer.toString(digyear);
        String yrStrEnd = yrStr.substring(yrStr.length() - 2);
        int year =2000+ Integer.parseInt(yrStrEnd);
        return year;
    }
    public void showPaymentPopUp()
    {

        final Dialog dialog = new Dialog(AppartmentDetails.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fees_popup);
        dialog.setCancelable(false);

     final  LinearLayout  paymentDetails=(LinearLayout)dialog.findViewById(R.id.paymentView);
        final  TextView  amountValue = (TextView)dialog. findViewById(R.id.amountValue);
        final  TextView  transactionFees = (TextView)dialog. findViewById(R.id.processingfess);
        final  TextView  total = (TextView)dialog. findViewById(R.id.total);
        RadioGroup  paymentOptions = (RadioGroup) dialog.findViewById(R.id.paymentMethod);
        RadioButton  cc = (RadioButton) dialog.findViewById(R.id.ccard);
        RadioButton  db = (RadioButton)dialog. findViewById(R.id.dcard);
        RadioButton  wallet = (RadioButton)dialog. findViewById(R.id.wallet);
        RadioButton   netbanking = (RadioButton)dialog. findViewById(R.id.nbanking);
        RadioButton  upi = (RadioButton)dialog. findViewById(R.id.upi);
        Button proceed=(Button) dialog.findViewById(R.id.pay);
        paymentDetails.setVisibility(View.VISIBLE);

        paymentOptions.check( netbanking.getId());
        paymentMode=Common.net_Banking;
        updatePaymentValue(paymentMode,amountValue,transactionFees,total);
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.cancel();
                Common.madePayment(AppartmentDetails.this,Double.toString(totalValue),transactionType,paymentMode);

            }
        });
        paymentOptions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                {
                    paymentDetails.setVisibility(View.VISIBLE);

                        switch (i)
                        {
                            case R.id.ccard:
                                transactionType="";
                                paymentMode=Common.creditCard;
                                break;
                            case R.id.dcard:
                                transactionType="";
                                paymentMode=Common.debitCard;
                                break;
                            case R.id.wallet:
                                paymentMode=Common.wallet;
                                break;
                            case R.id.nbanking:
                                transactionType=Common.net_Banking;
                                paymentMode="";
                                break;
                            case R.id.upi:
                                transactionType=Common.upi;
                                paymentMode="";
                                break;
                        }

                    }

                    updatePaymentValue(paymentMode,amountValue,transactionFees,total);

                }

        });
        // set the custom dialog components - text, image and button
        dialog.show();

    }
    private void updatePaymentValue(String paymentMode,TextView amountValue,TextView transactionFees,TextView total) {

        double gst = 0;
        transactionFeesValue = 0;
        switch (paymentMode) {
            case Common.creditCard:
            case Common.debitCard:

                transactionFeesValue = 0.0139 * amountVal;
                gst = 0.18 * transactionFeesValue;
                transactionFeesValue = Math.round(transactionFeesValue + gst+3);
                break;
            case Common.net_Banking:
            case Common.upi:
            case Common.wallet:
                transactionFeesValue = 0;
                break;

        }
        amountValue.setText("Rs " + amountVal);
        transactionFees.setText("Rs." + transactionFeesValue);
        totalValue = Math.round(amountVal + transactionFeesValue);
        total.setText("Rs. " + totalValue);
    }
    public void showMaintenanceReceipt() {
        final Dialog dialog = new Dialog(AppartmentDetails.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.rent_receipt);
        dialog.setCancelable(false);
        // set the custom dialog components - text, image and button
        final LinearLayout receipt=(LinearLayout) dialog.findViewById(R.id.receiptView);
        final LinearLayout bottomButton=(LinearLayout) dialog.findViewById(R.id.bottomButton);
        TextView headerTitle = (TextView) dialog.findViewById(R.id.header);
        common.CustomTextView amountt = (common.CustomTextView) dialog.findViewById(R.id.amountValue);
        TextView owner = (TextView) dialog.findViewById(R.id.ownerName);
        TextView bankaccount = (TextView) dialog.findViewById(R.id.bankAccount);
        TextView fees=(TextView) dialog.findViewById(R.id.fees);
        fees.setText("Rs. "+Math.round(transactionFeesValue));
        common.CustomTextView propName = (common.CustomTextView) dialog.findViewById(R.id.propName);
        common.CustomTextView address = (common.CustomTextView) dialog.findViewById(R.id.address);
        common.CustomTextView transId = (common.CustomTextView) dialog.findViewById(R.id.trans_id);
        common.CustomTextView date = (common.CustomTextView) dialog.findViewById(R.id.date);
        headerTitle.setText("Maintenance");
        Button save = (Button) dialog.findViewById(R.id.save);
        Button cancel = (Button) dialog.findViewById(R.id.cancel);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomButton.setVisibility(View.GONE);
                Bitmap bmp = ScreenshotUtils.getScreenShot(receipt);
                if (bmp != null) {


                    File saveFile = ScreenshotUtils.getMainDirectoryName(AppartmentDetails.this);//get the path to save screenshot
                    File file = ScreenshotUtils.store(bmp, "Maintenance_Receipt_"+months.getSelectedItemPosition()+"-"+getCurrentYear()+".jpg", saveFile);
                    bottomButton.setVisibility(View.VISIBLE);// /save the screenshot to selected path
                    Toast.makeText(AppartmentDetails.this, "Receipt saved sucessfully to " + file.getParent() + " folder.", Toast.LENGTH_SHORT).show();
                    dialog.cancel();
                    Intent output = new Intent();
                    setResult(RESULT_OK, output);
                    finish();
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                Intent output = new Intent();
                setResult(RESULT_OK, output);
                finish();
            }
        });


        amountt.setText("Amount of Rs." + balance.getText().toString() + " Paid for");
        owner.setText("maintenance of month "+months.getSelectedItem().toString());
//        bankaccount.setText("( A/c " + bankDetails.getAccNumber() + " )");
        propName.setText(appName.getText().toString());
        address.setText(appAddress.getText().toString());
        transId.setText(transactionId);
        date.setText(Utils.getCurrentDate());
        save.setTypeface(typeface);
        cancel.setTypeface(typeface);
        dialog.show();

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent returned_intent) {
        super.onActivityResult(requestCode, resultCode,returned_intent);
       if (requestCode == FONEPAISAPG_RET_CODE) {

            if (resultCode == Activity.RESULT_OK) {
                System.out.println("returned message" + returned_intent.getStringExtra("resp_msg"));
                System.out.println("returned code" + returned_intent.getStringExtra("resp_code"));
                System.out.println("sent json" + returned_intent.getStringExtra("data_sent"));
                String returnJson= returned_intent.getStringExtra("data_recieved");
                System.out.println("returned json" +returnJson);
                //transactionId=paymentId(returnJson);
                Toast toast = Toast.makeText(getApplicationContext(), returned_intent.getStringExtra("resp_msg").toString(), Toast.LENGTH_SHORT);
                toast.show();
                if(!returned_intent.getStringExtra("resp_msg").contains("cancelled")) {
                    showMaintenanceReceipt();
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                System.out.println("returned message on cancelled " + returned_intent.getStringExtra("resp_msg"));
                System.out.println("returned code on cancelled " + returned_intent.getStringExtra("resp_code"));
                Toast toast = Toast.makeText(getApplicationContext(), returned_intent.getStringExtra("resp_msg").toString(), Toast.LENGTH_SHORT);
                toast.show();
            } else {

            }
        }else if  (requestCode == 3) {
           System.out.println("---------INSIDE-------");
           if (returned_intent != null) {
               String message = returned_intent.getStringExtra("status");
               String[] resKey = returned_intent.getStringArrayExtra("responseKeyArray");
               String[] resValue = returned_intent.getStringArrayExtra("responseValueArray");
               if (resKey != null && resValue != null) {
                   for (int i = 0; i < resKey.length; i++) {
                       if (resKey[i].equalsIgnoreCase("bank_txn")) {
                           transactionId = resValue[i];
                           break;
                       }
                       System.out.println("  " + i + " resKey : " + resKey[i] + " resValue : " + resValue[i]);
                   }
                   if (message.contains("Successful")) {

                       showMaintenanceReceipt();
                   }

                   Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                   System.out.println("RECEIVED BACK--->" + message);
               }


           }
       }
    }
    public void madePayment(String invoiceId,String amount,String signKey){    Intent intent = new Intent(AppartmentDetails.this, fonePaisaPG.class);
        JSONObject json_to_be_sent = new JSONObject();
        try {
            json_to_be_sent.put("id", "A991");    // Mandatory .. FPTEST is just for testing it has to be changed before going to production
            json_to_be_sent.put("merchant_id", "A991");   // Mandatory .. FPTEST is just for testing it has to be changed before going to production
            json_to_be_sent.put("merchant_display", "PAYBOX");  // Mandatory ..  change it to whatever you want to get it displayed
            json_to_be_sent.put("invoice", invoiceId); //mandatory  .. this is the unique reference against which you can enquire and it can be system generated or manually entered
            json_to_be_sent.put("mobile_no", controller.getPrefsManger().getMobileNumber());    ///pass the mobile number if you have else send it blank and the customer will be prompted for the mobile no so that confirmation msg can be sent
            json_to_be_sent.put("email", controller.getPrefsManger().getEmailId());        // pass email if an invoice details has to be mailed
            json_to_be_sent.put("invoice_amt", amount);    //pass the amount with two decimal rounded off
            json_to_be_sent.put("note", "");         // pass any notes if you need
            json_to_be_sent.put("payment_types", paymentMode);   // not mandatory . this is to restrict the payment types
            json_to_be_sent.put("addnl_info", "");          // pass any addnl data which u need to get baack
//                //input for signing  API_KET#id#merchant_id#invoice#amount
//                String signed_ip = API_KEY + "#" + json_to_be_sent.getString("id") + "#" + json_to_be_sent.getString("merchant_id") + "#" + json_to_be_sent.getString("invoice") + "#" + json_to_be_sent.getString("invoice_amt") + "#";
//
//                /* *********************************************************************************************
//                *               TODO                                                                           *
//                *     Just for testing we have signed in the client side .                                     *
//                *    Please do the signing on your server side . and pass the signed message in the json       *
//                *                                                                                              *
//                ************************************************************************************************/
//
//                String signed_msg = getSignedMsg(signed_ip);
            json_to_be_sent.put("sign", signKey);
            json_to_be_sent.put("Environment", FPConstants.Production_Environment);  //mandatory   //Change it based on the environment you are using
            intent.putExtra("data", json_to_be_sent.toString());
            startActivityForResult(intent, FONEPAISAPG_RET_CODE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public String paymentId(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            return jsonObject.getString("payment_reference");
        } catch (Exception ex) {
            ex.fillInStackTrace();
        }
        return "";
    }
}
