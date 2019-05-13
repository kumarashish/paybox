package com.mypaybox;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import adapter.RentAdapter;
import common.ApiCall;
import common.AppController;
import common.Common;
import common.ScreenshotUtils;
import common.Utils;
import fonepaisa.com.fonepaisapg_sdk.FPConstants;
import fonepaisa.com.fonepaisapg_sdk.fonePaisaPG;
import interfaces.Callback;
import login.Test;
import model.BankModel;
import model.PropertyModel;

import static java.lang.System.currentTimeMillis;

/**
 * Created by Ashish.Kumar on 18-12-2017.
 */

public class Rent  extends Activity implements View.OnClickListener , Callback {
    AppController controller;
    Typeface typeface;
    EditText amount,comment;
    LinearLayout paymentDetails;
    TextView amountValue,transactionFees,total;
    RadioGroup paymentOptions;
    RadioButton cc,db,wallet,netbanking,upi;
    CheckBox terms;
    Button pay;
    common.CustomTextView selectProperty,registerBank;
    ImageView back;
    int getPropertyApiCall=1,getBankAccountDetails=2,postRent=3,getPaymentHash=4;
    int apiCall=0;
    ProgressDialog pd;
    ArrayList<PropertyModel> propertyList=new ArrayList<PropertyModel>();
    Boolean isPopertySelected=false;
    PropertyModel selectedProperty=null;
    BankModel bankDetails=null;
    boolean isforFirstTime=false;
    String transactionId="";
    String transsactionTime="";
TextView selectProp;
    LinearLayout sellerView;
    TextView heading;
    int FONEPAISAPG_RET_CODE = 22;
    String hashkey="";
    String invoiceId="";
    double amountVal=0;
  String paymentMode=Common.net_Banking;
  double transactionFeesValue=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_rent);
        initializeAll();
    }
public void initializeAll()
{
    typeface= Typeface.createFromAsset(getAssets(), "font.ttf");
    controller=(AppController)getApplicationContext();
    heading=(TextView)findViewById(R.id.heading) ;
    back=(ImageView) findViewById(R.id.back);
    amount=(EditText)findViewById(R.id.amount);
    comment=(EditText)findViewById(R.id.comment);;
    terms=(CheckBox) findViewById(R.id.checkBox2);
    pay=(Button)findViewById(R.id.pay);
    selectProperty=(common.CustomTextView)findViewById(R.id.selectProperty);
    selectProp=(TextView) findViewById(R.id.selectprop);
    sellerView=(LinearLayout) findViewById(R.id.sellerView);
    registerBank=(common.CustomTextView)findViewById(R.id.register);
    paymentDetails=(LinearLayout)findViewById(R.id.paymentView);
    amountValue = (TextView) findViewById(R.id.amountValue);
    transactionFees = (TextView) findViewById(R.id.processingfess);
    total = (TextView) findViewById(R.id.total);
    paymentOptions = (RadioGroup) findViewById(R.id.paymentMethod);
    cc = (RadioButton) findViewById(R.id.ccard);
    db = (RadioButton) findViewById(R.id.dcard);
    wallet = (RadioButton) findViewById(R.id.wallet);
    netbanking = (RadioButton) findViewById(R.id.nbanking);
    upi = (RadioButton) findViewById(R.id.upi);
    paymentDetails.setVisibility(View.GONE);
    amount.setTypeface(typeface);
    comment.setTypeface(typeface);
    pay.setTypeface(typeface);
    terms.setTypeface(typeface);
    pay.setOnClickListener(this);
    registerBank.setOnClickListener(this);
    sellerView.setOnClickListener(this);
    back.setOnClickListener(this);
     selectProp.setOnClickListener(this);
    selectProperty.setOnClickListener(this);
    paymentOptions.check(netbanking.getId());
    if(Utils.isInternetAvailable(Rent.this))
    {   apiCall=getPropertyApiCall;
        new GetData().execute();
        isforFirstTime=true;
    }else{
        Toast.makeText(Rent.this,"Internet Unavailable!", Toast.LENGTH_SHORT).show();
    }
    amount.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if(editable.length()==0)
            {
                amount.setText("0");
                transactionFees.setText("0");
                total.setText("0");
              paymentDetails.setVisibility(View.GONE);
            }else{
                updatePaymentValue(paymentMode);
                paymentDetails.setVisibility(View.VISIBLE);
            }

        }
    });
    paymentOptions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
          if  (amount.getText().length()>0)
            { paymentDetails.setVisibility(View.VISIBLE);
                switch (i)
                {
                    case R.id.ccard:
                        paymentMode=Common.creditCard;
                        break;
                    case R.id.dcard:
                        paymentMode=Common.debitCard;
                        break;
                    case R.id.wallet:
                        paymentMode=Common.wallet;
                        break;
                    case R.id.nbanking:
                        paymentMode=Common.net_Banking;
                        break;
                    case R.id.upi:
                        paymentMode=Common.upi;
                        break;
                }

                updatePaymentValue(paymentMode);

            }else{
              paymentDetails.setVisibility(View.GONE);
          }
        }
    });
}

    private void updatePaymentValue(String paymentMode) {
        amountVal = Integer.parseInt(amount.getText().toString().trim());
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
        double totalValue = Math.round(amountVal + transactionFeesValue);
        total.setText("Rs. " + totalValue);
    }

    public boolean isFieldValidated() {
        if (amount.getText().length() > 0) {
            if (comment.getText().length() > 0) {
                if(terms.isChecked()) {
                    return true;
                }else{
                    Toast.makeText(Rent.this, "Please accept terms and conditions!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(Rent.this, "Please enter comments!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(Rent.this, "Please enter amount!", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.pay:
                if((bankDetails!=null)&(selectedProperty!=null)) {
                    if(isFieldValidated()) {
                        if (Utils.isInternetAvailable(Rent.this)) {
                            apiCall =getPaymentHash;
                            new GetData().execute();
                        } else {
                            Toast.makeText(Rent.this, "Internet Unavailable!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }else{
                    if(selectedProperty==null)
                    {
                        Toast.makeText(Rent.this, "Please select property.", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(Rent.this, "Unable to find bank details for selected property.", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.register:
                Intent in=new Intent(this,RegisterBank.class);
                startActivityForResult(in,1);
                break;
            case R.id.back:
                onBackPressed();
                break;
            case R.id.selectProperty:
            case R.id.selectprop:
            case R.id.sellerView:
                showAlertDialog("Select Property");
                break;
        }

    }
    public void showAlertDialog(String heading) {
        final Dialog dialog = new Dialog(Rent.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert);
        // set the custom dialog components - text, image and button
        TextView header = (TextView) dialog.findViewById(R.id.header);
        header.setText(heading);
        ListView listView = (ListView) dialog.findViewById(R.id.list);
        boolean isDialogTobeShown = false;
        if (propertyList.size() > 0) {
            isDialogTobeShown = true;
           listView.setAdapter(new RentAdapter(Rent.this, propertyList, selectProperty, dialog, this));
        }else{
            Toast.makeText(Rent.this, "Unable to fetch Property list for selected location ,please go back and select other location.", Toast.LENGTH_SHORT).show();
        }

            dialog.show();

    }

    public void showRentReceipt() {
        final Dialog dialog = new Dialog(Rent.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.rent_receipt);
        dialog.setCancelable(false);
        // set the custom dialog components - text, image and button
       final LinearLayout receipt=(LinearLayout) dialog.findViewById(R.id.receiptView);
        final LinearLayout bottomButton=(LinearLayout) dialog.findViewById(R.id.bottomButton);
        common.CustomTextView amountt = (common.CustomTextView) dialog.findViewById(R.id.amountValue);
        TextView owner = (TextView) dialog.findViewById(R.id.ownerName);
        TextView bankaccount = (TextView) dialog.findViewById(R.id.bankAccount);
        TextView fees=(TextView) dialog.findViewById(R.id.fees);
        common.CustomTextView propName = (common.CustomTextView) dialog.findViewById(R.id.propName);
        common.CustomTextView address = (common.CustomTextView) dialog.findViewById(R.id.address);
        common.CustomTextView transId = (common.CustomTextView) dialog.findViewById(R.id.trans_id);
        common.CustomTextView date = (common.CustomTextView) dialog.findViewById(R.id.date);
        Button save = (Button) dialog.findViewById(R.id.save);
        Button cancel = (Button) dialog.findViewById(R.id.cancel);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomButton.setVisibility(View.GONE);
                Bitmap bmp = ScreenshotUtils.getScreenShot(receipt);
                if (bmp != null) {


                    File saveFile = ScreenshotUtils.getMainDirectoryName(Rent.this);//get the path to save screenshot
                    File file = ScreenshotUtils.store(bmp, "Rent_Receipt_" + selectedProperty.getPropetyName() + ".jpg", saveFile);
                    bottomButton.setVisibility(View.VISIBLE);// /save the screenshot to selected path
                    Toast.makeText(Rent.this, "Receipt saved sucessfully to " + file.getParent() + " folder.", Toast.LENGTH_SHORT).show();
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
        amountt.setText("Amount of Rs." + amount.getText().toString() + " Paid to");
        fees.setText("Rs. "+Math.round(transactionFeesValue));
        owner.setText(selectedProperty.getOwnerName());
        bankaccount.setText("( A/c " + bankDetails.getAccNumber() + " )");
        propName.setText(selectedProperty.getPropetyName());
        address.setText(selectedProperty.getAddress());
        transId.setText(transactionId);
        date.setText(transsactionTime);
        save.setTypeface(typeface);
        cancel.setTypeface(typeface);
        dialog.show();

    }
    @Override
    public void onClick(int pos) {
        isPopertySelected=true;
        selectedProperty=propertyList.get(pos);
        apiCall=getBankAccountDetails;
        new GetData().execute();
    }

    public class GetData extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(Rent.this);
            pd.setMessage("Please wait....");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        public String doInBackground(String... params) {
            String response="";
        if(apiCall==getPropertyApiCall) {
            propertyList.clear();
            response = ApiCall.getData(Common.getPropertyUrl(controller.getPrefsManger().getUserName()));
            //response = ApiCall.getData(Common.getPropertyUrl(Common.city,"Manikonda"));
        }
            else if(apiCall==getBankAccountDetails) {
                response = ApiCall.getData(Common.getBankDetailsForSelectedProperty(selectedProperty.getPropertyModelId()));
            }else if(apiCall==postRent)
             {
             response= ApiCall.post(Common.getSaveRentUrl,getSaveRentJSON());
             }else if(apiCall==getPaymentHash){
            JSONObject jsonObject=new JSONObject();
            try {
                jsonObject.put("test", "test");
            } catch (Exception ex) {
                ex.fillInStackTrace();
            }
            invoiceId=""+ System.currentTimeMillis();

            response= ApiCall.post(Common.getPaymentHash(Double.toString(amountVal+transactionFeesValue),invoiceId),jsonObject);
        }

            Log.d("Api respone:",response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(apiCall==getPropertyApiCall) {
                JSONParsing(s);
            }else if(apiCall==getBankAccountDetails)
            {
                JSONParsing(s);
            }else if(apiCall==postRent)
            {
              if(s.contains("Rent Saved.."))
              {
                  Toast.makeText(Rent.this,"Rent paid sucessfully", Toast.LENGTH_SHORT).show();
                  showRentReceipt();
              }else{
                  Toast.makeText(Rent.this,s, Toast.LENGTH_SHORT).show();
              }
            }else if(apiCall==getPaymentHash)
            {
                hashkey=s;
                pd.cancel();
                madePayment(invoiceId,Double.toString(amountVal+transactionFeesValue), hashkey);

            }
            if (pd != null) {
                pd.cancel();
            }
        }
    }

    public void JSONParsing(String s)
    {
        try{
            JSONArray jsonArray=new JSONArray(s);
            if(jsonArray.length()>0)
            if(apiCall==getPropertyApiCall) {
                {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        propertyList.add(new PropertyModel(jsonArray.getJSONObject(i)));
                    }
                }
            }else if(apiCall==getBankAccountDetails)
            {
                bankDetails=new BankModel(jsonArray.getJSONObject(0));

            }

        }catch (Exception ex)
        {
            ex.fillInStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent returned_intent) {
        super.onActivityResult(requestCode, resultCode,returned_intent);
        if(requestCode==1)
        {
            if(Utils.isInternetAvailable(Rent.this))
            {   selectedProperty=null;
                bankDetails=null;
                selectProperty.setText("Select Property");
                apiCall=getPropertyApiCall;
                new GetData().execute();
            }
        }
        else if (requestCode == FONEPAISAPG_RET_CODE) {

            if (resultCode == Activity.RESULT_OK) {
                System.out.println("returned message" + returned_intent.getStringExtra("resp_msg"));
                System.out.println("returned code" + returned_intent.getStringExtra("resp_code"));
                System.out.println("sent json" + returned_intent.getStringExtra("data_sent"));
                String returnJson= returned_intent.getStringExtra("data_recieved");
                System.out.println("returned json" +returnJson);
                transactionId=paymentId(returnJson);
                Toast toast = Toast.makeText(getApplicationContext(), returned_intent.getStringExtra("resp_msg").toString(), Toast.LENGTH_SHORT);
                toast.show();
                if(!returned_intent.getStringExtra("resp_msg").contains("cancelled")) {
                    apiCall = postRent;
                    new GetData().execute();
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                System.out.println("returned message on cancelled " + returned_intent.getStringExtra("resp_msg"));
                System.out.println("returned code on cancelled " + returned_intent.getStringExtra("resp_code"));
                Toast toast = Toast.makeText(getApplicationContext(), returned_intent.getStringExtra("resp_msg").toString(), Toast.LENGTH_SHORT);
                toast.show();
            } else {

            }
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
    public JSONObject getSaveRentJSON() {
        JSONObject jsonObject = new JSONObject();
        JSONObject propertyModel = new JSONObject();
        JSONObject property = new JSONObject();
        JSONObject propertyBank = new JSONObject();
        JSONObject propertyBankModel = new JSONObject();
        JSONObject transactionModel = new JSONObject();
        try {
            property.put("PropertyModelId", selectedProperty.getPropertyModelId());
            property.put("PropetyName", selectedProperty.getPropetyName());
            property.put("OwnerName", selectedProperty.getOwnerName());
            property.put("OwnerEmail", selectedProperty.getOwnerEmail());
            property.put("OwnerMobile", selectedProperty.getOwnerMobile());
            property.put("Address", selectedProperty.getAddress());
            property.put("Locality", selectedProperty.getLocality());
            property.put("City", selectedProperty.getCity());
            property.put("State", selectedProperty.getState());
            property.put("ZipCode", selectedProperty.getZipCode());
            propertyModel.put("Property", property);
            propertyBankModel.put("BankModelId", bankDetails.getBankModelId());
            propertyBankModel.put("BankName", bankDetails.getBankName());
            propertyBankModel.put("BankAddress", bankDetails.getBankAddress());
            propertyBankModel.put("BankIfsc", bankDetails.getBankIfsc());
            propertyBankModel.put("AccNumber", bankDetails.getAccNumber());
            transsactionTime= Utils.getCurrentDate();
            transactionModel.put("TransactionModelId", "");
            transactionModel.put("TransactionId",transactionId);
            transactionModel.put("Comment", comment.getText().toString());
            transactionModel.put("TransactionDate", transsactionTime);
            transactionModel.put("TransactionAmount", amount.getText().toString());
            propertyBank.put("BankModel", propertyBankModel);
            propertyBank.put("TransactionModel", transactionModel);
            propertyModel.put("PropertyBank",propertyBank);
            jsonObject.put("UserId", Common.userId);
            jsonObject.put("Property",propertyModel);
        } catch (Exception ex) {
            ex.fillInStackTrace();
        }
        return jsonObject;
    }

    public void madePayment(String invoiceId,String amount,String signKey){    Intent intent = new Intent(Rent.this, fonePaisaPG.class);
            JSONObject json_to_be_sent = new JSONObject();
            try {
                json_to_be_sent.put("id", "A991");    // Mandatory .. FPTEST is just for testing it has to be changed before going to production
                json_to_be_sent.put("merchant_id", "A991");   // Mandatory .. FPTEST is just for testing it has to be changed before going to production
                json_to_be_sent.put("merchant_display", "PAYBOX");  // Mandatory ..  change it to whatever you want to get it displayed
                json_to_be_sent.put("invoice", invoiceId); //mandatory  .. this is the unique reference against which you can enquire and it can be system generated or manually entered
                json_to_be_sent.put("mobile_no", controller.getPrefsManger().getMobileNumber());    ///pass the mobile number if you have else send it blank and the customer will be prompted for the mobile no so that confirmation msg can be sent
                json_to_be_sent.put("email", controller.getPrefsManger().getEmailId());          // pass email if an invoice details has to be mailed
                json_to_be_sent.put("invoice_amt", amount);    //pass the amount with two decimal rounded off
                json_to_be_sent.put("note", "");         // pass any notes if you need
                json_to_be_sent.put("payment_types", paymentMode);
                // not mandatory . this is to restrict the payment types
                json_to_be_sent.put("addnl_info", "");
                // pass any addnl data which u need to get baack
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
    }




