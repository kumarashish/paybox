package com.mypaybox;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONObject;

import common.ApiCall;
import common.Common;
import common.Utils;
import model.AddressDataset;


/**
 * Created by ashish.kumar on 02-05-2017.
 */

public class Address extends Activity implements View.OnClickListener {
    EditText address1,address2,city,state,pincode;
    RadioGroup addressType;
    RadioButton home,office;
    String addressTyp="Home";
    Button submit;
    ProgressDialog pd;
    public static AddressDataset ds=null;
    Typeface typeface;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adress);
        initializeAll();
    }
public void initializeAll() {
    typeface= Typeface.createFromAsset(getAssets(), "lato_light.ttf");
    address1 = (EditText) findViewById(R.id.address1);
    address2 = (EditText) findViewById(R.id.address2);
    city = (EditText) findViewById(R.id.city);
    state = (EditText) findViewById(R.id.state);
    pincode = (EditText) findViewById(R.id.pincode);
    addressType=(RadioGroup)findViewById(R.id.addressType );
    home=(RadioButton)findViewById(R.id.home);
    office=(RadioButton)findViewById(R.id.officee);
    if(ds!=null)
    {
        address1.setText(ds.getAddress1());
        address2.setText(ds.getAddress2());
        city.setText(ds.getCity());
        state.setText(ds.getState());
        pincode.setText(ds.getPincode());
    }
    submit=(Button) findViewById(R.id.submit);
    address1.setTypeface(typeface);
    address2.setTypeface(typeface);
    city.setTypeface(typeface);
    state.setTypeface(typeface);
    pincode.setTypeface(typeface);
    submit.setTypeface(typeface);
    addressType.check(R.id.home);
    addressType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
            switch (checkedId)
            {
                case R.id.home:
                    addressTyp="Home";
                    break;
                case R.id.officee:
                    addressTyp="Office";
                    break;
            }
        }
    });
    submit.setOnClickListener(this);
}

    @Override
    public void onClick(View v) {
if((address1.getText().length()>0)&&(address2.getText().length()>0)&&(city.getText().length()>0)&&(state.getText().length()>0)&&(pincode.getText().length()>0))
{
    if(Utils.isInternetAvailable(Address.this))
    {
        new WebApi().execute();
    }else{
        Toast.makeText(Address.this,"Internet Unavailable", Toast.LENGTH_SHORT).show();
    }
}else{
    if(address1.getText().length()==0)
    {
        Toast.makeText(Address.this,"Please enter house no", Toast.LENGTH_SHORT);
    }else if(address2.getText().length()==0)
    {
        Toast.makeText(Address.this,"Please enter street", Toast.LENGTH_SHORT);
    }else if(city.getText().length()==0)
    {
        Toast.makeText(Address.this,"Please enter city", Toast.LENGTH_SHORT);
    }else if(state.getText().length()==0)
    {
        Toast.makeText(Address.this,"Please enter state", Toast.LENGTH_SHORT);
    }else if(pincode.getText().length()==0)
    {
        Toast.makeText(Address.this,"Please enter pincode", Toast.LENGTH_SHORT);
    }
}
    }

    public class WebApi extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(Address.this);
            pd.setMessage("Please wait....");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        public String doInBackground(String... params) {
            String response="";
            if(ds!=null)
            {
                response = ApiCall.postData(Common.updateAddress, getJSon(1));
            }else {
                response = ApiCall.postData(Common.saveAddress, getJSon(0));
            }
            return response;
        }

        public JSONObject getJSon(int type) {
            JSONObject jsonObject = new JSONObject();
            try {
                if (type == 0) {
                    jsonObject.put("AddressId", "");
                } else {
                    jsonObject.put("AddressId",ds.getAddressId());
                }
                jsonObject.put("UserId", Common.UserName);
                jsonObject.put("AddressType", addressTyp);
                jsonObject.put("Address1", address1.getText().toString());
                jsonObject.put("Address2", address2.getText().toString());
                jsonObject.put("City", city.getText().toString());
                jsonObject.put("State", state.getText().toString());
                jsonObject.put("Pincode", pincode.getText().toString());
                jsonObject.put("CurrentVersion", "N");
            } catch (Exception ex) {
                ex.fillInStackTrace();
            }
            return jsonObject;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s.equalsIgnoreCase("true"))
            {
                Toast.makeText(Address.this,"Address Saved", Toast.LENGTH_SHORT).show();
                finish();
            }
            //jsonParsing(s);
            if (pd != null) {
                pd.cancel();
            }
        }
    }
}
