package com.mypaybox;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONObject;

import common.ApiCall;
import common.AppController;
import common.Common;
import common.Utils;

/**
 * Created by Ashish.Kumar on 18-12-2017.
 */

public class RegisterBank  extends Activity implements View.OnClickListener{
    AppController controller;
    Typeface typeface;
    ImageView back;
    EditText prop_name,mobile,propert_address,bank_name,bank_address,account_num, ifsc,city,state,pincode,locality;
    CheckBox terms;
    Button submit;
    ProgressDialog pd;
    int apiCall=0;
    int registerProperty=1;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_property);
        initializeAll();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.done:
                if (isAllFieldsValidated()) {
                    if (terms.isChecked()) {
                        if (Utils.isInternetAvailable(RegisterBank.this))
                        {
                            apiCall = registerProperty;
                            new PostData().execute();
                        }
                    } else {
                        Toast.makeText(RegisterBank.this, "Please check Agree terms and condition by selecting checkbox", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.back:
                onBackPressed();
                break;
        }
    }

    public void initializeAll() {
        typeface = Typeface.createFromAsset(getAssets(), "font.ttf");
        controller = (AppController) getApplicationContext();
        back = (ImageView) findViewById(R.id.back);
        prop_name = (EditText) findViewById(R.id.prop_name);
        mobile = (EditText) findViewById(R.id.mobile);
        propert_address = (EditText) findViewById(R.id.propert_address);
        locality= (EditText) findViewById(R.id.propert_locality);
        city= (EditText) findViewById(R.id.city);
        state= (EditText) findViewById(R.id.state);
        pincode= (EditText) findViewById(R.id.pincode);
        bank_name = (EditText) findViewById(R.id.bank_name);
        bank_address = (EditText) findViewById(R.id.bank_address);
        ifsc = (EditText) findViewById(R.id.ifsc);
        account_num = (EditText) findViewById(R.id.account_num);
        terms = (CheckBox) findViewById(R.id.checkBox2);
        submit=(Button) findViewById(R.id.done);
        prop_name.setTypeface(typeface);
        mobile.setTypeface(typeface);
        propert_address.setTypeface(typeface);
        bank_name.setTypeface(typeface);
        bank_address.setTypeface(typeface);
        ifsc.setTypeface(typeface);
        account_num.setTypeface(typeface);
        terms.setTypeface(typeface);
        submit.setTypeface(typeface);
        city.setTypeface(typeface);
        state.setTypeface(typeface);
        pincode.setTypeface(typeface);
        locality.setTypeface(typeface);
        back.setOnClickListener(this);
        submit.setOnClickListener(this);
        city.setText(Common.city);
        locality.setText(Common.locality);
//        city.setEnabled(false);
//        locality.setEnabled(false);

    }

    public boolean isAllFieldsValidated() {
        if (prop_name.getText().length() > 0) {
                    if (mobile.getText().length() > 0) {
                        if (propert_address.getText().length() > 0) {
                            if (locality.getText().length() > 0) {
                                if (city.getText().length() > 0) {
                                    if (state.getText().length() > 0) {
                                        if (pincode.getText().length() > 0) {

                                            if (bank_name.getText().length() > 0) {
                                                if (bank_address.getText().length() > 0) {
                                                    if (account_num.getText().length() > 0) {
                                                        if (ifsc.getText().length() > 0) {
                                                            return true;
                                                        } else {
                                                            Toast.makeText(RegisterBank.this, "Please enter ifsc code", Toast.LENGTH_SHORT).show();
                                                        }
                                                    } else {
                                                        Toast.makeText(RegisterBank.this, "Please enter account number", Toast.LENGTH_SHORT).show();
                                                    }
                                                } else {
                                                    Toast.makeText(RegisterBank.this, "Please enter bank address", Toast.LENGTH_SHORT).show();
                                                }

                                            } else {
                                                Toast.makeText(RegisterBank.this, "Please enter bank name", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(RegisterBank.this, "Please enter  pincode", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(RegisterBank.this, "Please enter  state", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(RegisterBank.this, "Please enter  city", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(RegisterBank.this, "Please enter locality", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(RegisterBank.this, "Please enter house no", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(RegisterBank.this, "Please enter owner mobile number", Toast.LENGTH_SHORT).show();
                    }


        } else {
            Toast.makeText(RegisterBank.this, "Please enter property name", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public class PostData extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(RegisterBank.this);
            pd.setMessage("Please wait....");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        public String doInBackground(String... params) {
            String response="";
            if(apiCall==registerProperty) {

                //response = ApiCall.getData(Common.getPropertyUrl(Common.city,Common.locality));
                response = ApiCall.post(Common.getSavePropertyWithBankDetailsUrl,getPropertyJson());
            }

            Log.d("Api respone:",response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(apiCall==registerProperty) {
                //JSONParsing(s);
                if(s.contains("Property Saved.."))
                {
                    Toast.makeText(RegisterBank.this,"Registered Sucessfully", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(RegisterBank.this,s, Toast.LENGTH_SHORT).show();
                }
            }
            if (pd != null) {
                pd.cancel();
            }
        }
    }

    public JSONObject getPropertyJson() {
        JSONObject jsonObject = new JSONObject();
        JSONObject property = new JSONObject();
        JSONObject propertyBank = new JSONObject();
        JSONObject propertyBankModel = new JSONObject();
        try {
            property.put("PropertyModelId", "");
            property.put("PropetyName", prop_name.getText().toString());
            property.put("OwnerName", controller.getPrefsManger().getUserName());
            property.put("OwnerEmail",  controller.getPrefsManger().getEmailId());
            property.put("OwnerMobile", mobile.getText().toString());
            property.put("Address", propert_address.getText().toString());
            property.put("Locality", locality.getText().toString());
            property.put("City", city.getText().toString());
            property.put("State", state.getText().toString());
            property.put("ZipCode", pincode.getText().toString());
            propertyBankModel.put("BankModelId", "");
            propertyBankModel.put("BankName", bank_name.getText().toString());
            propertyBankModel.put("BankAddress", bank_address.getText().toString());
            propertyBankModel.put("BankIfsc", ifsc.getText().toString());
            propertyBankModel.put("AccNumber", account_num.getText().toString());
            propertyBank.put("BankModel", propertyBankModel);
            jsonObject.put("Property", property);
            jsonObject.put("PropertyBank", propertyBank);


        } catch (Exception ex) {
            ex.fillInStackTrace();
        }
        return jsonObject;
    }

}
