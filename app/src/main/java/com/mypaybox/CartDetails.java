package com.mypaybox;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.instamojo.android.Instamojo;
import com.instamojo.android.activities.PaymentDetailsActivity;
import com.instamojo.android.callbacks.OrderRequestCallBack;
import com.instamojo.android.helpers.Constants;
import com.instamojo.android.models.Errors;
import com.instamojo.android.models.Order;
import com.instamojo.android.network.Request;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;
import java.util.regex.Pattern;

import adapter.AddressAdapter;
import adapter.FreeItemAdapter;
import common.ApiCall;
import common.AppController;
import common.Common;
import common.CustomTextView;
import common.Utils;
import interfaces.Callback;
import interfaces.DeleteCallBack;

import model.AddressDataset;
import model.FreeItemsDataset;
import model.Offers;
import model.ProfileDataset;
import model.VendorItemsDataset;
import okhttp3.MediaType;

/**
 * Created by Honey Singh on 4/17/2017.
 */

public class CartDetails extends Activity implements View.OnClickListener, Callback, DeleteCallBack {
    ImageView imageView,back;
    TextView text,heading;
    LinearLayout layout;
    int daysCountValue = 1;
    int quantityCountValue = 1;
    Button mon,tues,wed,thurs,fri,sat,sun;
    TextView itemName,itemQuantiy,quantity,itemCount,daysCount,totalPrice,totalQuantity;
    ImageButton increaseUnits,decreaseUnits,increaseDays,decreaseDays;
    RadioGroup frequency;
    RadioButton daily,alternate,custom;
    HorizontalScrollView frequencyView;
    EditText promocode;
    Button redeem;
    CustomTextView alreadyCoupon;
    Button paynow;
    public static VendorItemsDataset selectedItem=null;
    Boolean isModaySelected = false;
    Boolean isTuesdaySelected = false;
    Boolean isWednesdaySelected = false;
    Boolean isThursdaySelected = false;
    Boolean isFridaySelected = false;
    Boolean isSaturdaySelected = false;
    Boolean isSundaySelected = false;
ProgressDialog pd;
ArrayList<AddressDataset> address=new ArrayList<>();
    AddressDataset addressData;
    Boolean isAddressSelected=false;
    Callback callback;
    DeleteCallBack deleteCallBack;
    Dialog dialog;
    int AdressApiRequested=1;
    int DeleteAdressApi=2;
    int SaveOrderApi=3;
    int redeemCoupon=4;
    int acessTokenApi=5;
    int getProfileApi=6;
    int requestedApiType;
    int frequencyType=1;
     TextView addressTv;
    Typeface typeface;
    public static Button date;
    Spinner timmings;
    public static Boolean isDeliveryStartDateSelected=false;
    String[]timmingArray={"5 am - 7.30 am","5 pm - 7.30 pm"};
    TextView timmingTv;
    public static String orderDeliveryStartDate="";
    public static String startDate="";
    AppController controller;
    Offers offer=null;
    boolean isOfferAplied=false;
    JSONObject couponJson=null;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    String AccessToken = null;

    public static FreeItemsDataset freeItem=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cartdetails);
        initializeAll();

    }

    public void initializeAll() {
        Instamojo.initialize(this);
        controller=(AppController)getApplicationContext();
        AccessToken=controller.getPaymentGatewayToken();
       typeface= Typeface.createFromAsset(getAssets(), "lato_light.ttf");
        callback=this;
        deleteCallBack=this;
        date=(Button) findViewById(R.id.date);
        timmings=(Spinner)findViewById(R.id.timming);
        back = (ImageView) findViewById(R.id.back);
        heading = (TextView) findViewById(R.id.heading);
        itemName = (TextView) findViewById(R.id.itemName);
        itemQuantiy=(TextView)findViewById(R.id.itemQuantity);
        quantity = (TextView) findViewById(R.id.quantity);
        itemCount = (TextView) findViewById(R.id.itemCount);
        daysCount = (TextView) findViewById(R.id.daysCount);
        totalPrice = (TextView) findViewById(R.id.totalPrice);
        addressTv= (TextView) findViewById(R.id.address);
        timmingTv=(TextView) findViewById(R.id.timmint_tv);
        totalQuantity = (TextView) findViewById(R.id.quantity);
        increaseUnits = (ImageButton) findViewById(R.id.add);
        decreaseUnits = (ImageButton) findViewById(R.id.minus);
        increaseDays = (ImageButton) findViewById(R.id.daysadd);
        decreaseDays = (ImageButton) findViewById(R.id.minusDays);
        promocode = (EditText) findViewById(R.id.promoCodeEditText);
        redeem = (Button) findViewById(R.id.redeem);
        alreadyCoupon = (CustomTextView) findViewById(R.id.havePromoCode);
        controller.setEnterCoupon(promocode);
        promocode.setFocusable(false);
        promocode.setTypeface(controller.getTypeface());
        redeem.setTypeface(controller.getTypeface());
        frequency=(RadioGroup) findViewById(R.id.frequency);
        frequencyView=(HorizontalScrollView)findViewById(R.id.customview);
        daily=(RadioButton)findViewById(R.id.daily);
        alternate=(RadioButton)findViewById(R.id.alternate);
        custom=(RadioButton)findViewById(R.id.custom);
        daily.setTypeface(typeface);
        alternate.setTypeface(typeface);
        custom.setTypeface(typeface);
        mon=(Button)findViewById(R.id.m);
        tues=(Button)findViewById(R.id.t);
        wed=(Button)findViewById(R.id.w);
        thurs=(Button)findViewById(R.id.th);
        fri=(Button)findViewById(R.id.fr);
        sat=(Button)findViewById(R.id.s);
        sun=(Button)findViewById(R.id.su);

        paynow=(Button)findViewById(R.id.pay);
        paynow.setTypeface(typeface);
        date.setTypeface(typeface);
        mon.setTypeface(typeface);
        tues.setTypeface(typeface);
        wed.setTypeface(typeface);
        thurs.setTypeface(typeface);
        fri.setTypeface(typeface);
        sat.setTypeface(typeface);
        sun.setTypeface(typeface);
        timmingTv.setTypeface(typeface);
        increaseUnits.setOnClickListener(this);
        decreaseUnits.setOnClickListener(this);
        increaseDays.setOnClickListener(this);
        decreaseDays.setOnClickListener(this);
        paynow.setOnClickListener(this);
        back.setOnClickListener(this);
        mon.setOnClickListener(this);
        tues.setOnClickListener(this);
        wed.setOnClickListener(this);
        thurs.setOnClickListener(this);
        fri.setOnClickListener(this);
        sat.setOnClickListener(this);
        sun.setOnClickListener(this);
        addressTv.setOnClickListener(this);
        date.setOnClickListener(this);
        alreadyCoupon.setOnClickListener(this);
        redeem.setOnClickListener(this);
        addressTv.setTextColor(getResources().getColor(R.color.white));
        frequency.check(R.id.daily);
        timmings.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,timmingArray));
        checkAllDays();
        itemName.setText(selectedItem.getName()+"("+selectedItem.getItemCategory()+")");
      itemQuantiy.setText(selectedItem.getMeasurement());
frequency.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
       if(checkedId==alternate.getId())
        {frequencyType=1;
            checkAlternateDays();
            enableAll();
            updateValues();
        }else if(checkedId==daily.getId())
        {frequencyType=2;
            checkAllDays();
            disableAll();
            updateValues();
        }else if(checkedId==custom.getId())
       {   frequencyType=3;
           clearAllDays();
           enableAll();
           updateValues();
       }
    }
});
        itemCount.setText(Integer.toString(quantityCountValue));
        totalPrice.setText("Rs."+ Integer.toString(getTotalPrice()));
        totalQuantity.setText(Integer.toString(quantityCountValue*daysCountValue*getSelectedDays()));
        paynow.setText("Pay Rs."+ Integer.toString(getTotalPrice()));
        if(controller.getOffer()!=null)
        {
           promocode.setText(controller.getOffer().getOfferCode());
        }
        promocode.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                offer=null;
                isOfferAplied=false;
                paynow.setText("Pay Rs."+ Integer.toString(getTotalPrice()));
                alreadyCoupon.setText("Check offers");

            }
        });
if(Utils.isInternetAvailable(CartDetails.this)) {
    requestedApiType=AdressApiRequested;
    new WebAPI().execute();
}else{
    Toast.makeText(CartDetails.this,"Intenet Unavailable", Toast.LENGTH_SHORT).show();
}
disableAll();
    }
public void updateValues()
{
    itemCount.setText(Integer.toString(quantityCountValue));
    totalPrice.setText("Rs."+ Integer.toString(getTotalPrice()));
    totalQuantity.setText(Integer.toString(quantityCountValue*daysCountValue*getSelectedDays()));
    paynow.setText("Pay Rs."+ Integer.toString(getTotalPrice()));
    isOfferAplied=false;
    alreadyCoupon.setText("Check offers");
    promocode.setText("");
}
    public void disableAll() {
        mon.setEnabled(false);
        tues.setEnabled(false);
        wed.setEnabled(false);
        thurs.setEnabled(false);
        fri.setEnabled(false);
        sat.setEnabled(false);
        sun.setEnabled(false);
    }

    public void enableAll() {
        mon.setEnabled(true);
        tues.setEnabled(true);
        wed.setEnabled(true);
        thurs.setEnabled(true);
        fri.setEnabled(true);
        sat.setEnabled(true);
        sun.setEnabled(true);
    }
    public void checkAllDays() {
        mon.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_border_box));
        tues.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_border_box));
        wed.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_border_box));
        thurs.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_border_box));
        fri.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_border_box));
        sat.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_border_box));
        sun.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_border_box));
        isModaySelected = true;
        isTuesdaySelected = true;
        isWednesdaySelected = true;
        isThursdaySelected = true;
        isFridaySelected = true;
        isSaturdaySelected = true;
        isSundaySelected = true;


    }
    public void clearAllDays() {
        mon.setBackgroundDrawable(getResources().getDrawable(R.drawable.grey_border));
        tues.setBackgroundDrawable(getResources().getDrawable(R.drawable.grey_border));
        wed.setBackgroundDrawable(getResources().getDrawable(R.drawable.grey_border));
        thurs.setBackgroundDrawable(getResources().getDrawable(R.drawable.grey_border));
        fri.setBackgroundDrawable(getResources().getDrawable(R.drawable.grey_border));
        sat.setBackgroundDrawable(getResources().getDrawable(R.drawable.grey_border));
        sun.setBackgroundDrawable(getResources().getDrawable(R.drawable.grey_border));
        isModaySelected = false;
        isTuesdaySelected = false;
        isWednesdaySelected = false;
        isThursdaySelected = false;
        isFridaySelected = false;
        isSaturdaySelected = false;
        isSundaySelected =false;


    }

    public void checkAlternateDays() {
        mon.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_border_box));
        tues.setBackgroundDrawable(getResources().getDrawable(R.drawable.grey_border));
        wed.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_border_box));
        thurs.setBackgroundDrawable(getResources().getDrawable(R.drawable.grey_border));
        fri.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_border_box));
        sat.setBackgroundDrawable(getResources().getDrawable(R.drawable.grey_border));
        sun.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_border_box));
        isModaySelected = true;
        isTuesdaySelected = false;
        isWednesdaySelected = true;
        isThursdaySelected = false;
        isFridaySelected = true;
        isSaturdaySelected = false;
        isSundaySelected = true;
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==back.getId())
        {
            onBackPressed();
        }else if(v.getId()==date.getId())
        {
            DialogFragment newFragment = new DatePickerFragment();
            newFragment.show(getFragmentManager(), "Select Start Date");
        }
        else if(v.getId()==increaseDays.getId())
        {
            increaseDayCount();
            daysCount.setText(Integer.toString(daysCountValue));
            updateValues();
        }
        else if(v.getId()==decreaseDays.getId())
        {
            decreaseDaysCount();
            daysCount.setText(Integer.toString(daysCountValue));
            updateValues();
        }
        else if(v.getId()==increaseUnits.getId())
        {
            increaseQuantityCount();
            itemCount.setText(Integer.toString(quantityCountValue));
            updateValues();
        }
        else if(v.getId()==decreaseUnits.getId())
        {
            decreaseQuantityCount();
            itemCount.setText(Integer.toString(quantityCountValue));
            updateValues();

        } else if (v.getId() == alreadyCoupon.getId()) {
            Offer.amount=getTotalPrice();
            Intent in = new Intent(CartDetails.this, Offer.class);
            startActivityForResult(in,23);
        }
        else if(v.getId()==mon.getId())
        {
            if (isModaySelected== true) {
                isModaySelected = false;
                mon.setBackgroundDrawable(getResources().getDrawable(R.drawable.grey_border));
                updateValues();
            } else {
                isModaySelected = true;
                mon.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_border_box));
                updateValues();
            }
        }
        else if(v.getId()==tues.getId())
        {
            if (isTuesdaySelected == true) {
                isTuesdaySelected = false;
                tues.setBackgroundDrawable(getResources().getDrawable(R.drawable.grey_border));
                updateValues();
            } else {
                isTuesdaySelected = true;
                tues.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_border_box));
                updateValues();
            }
        }
        else if(v.getId()==wed.getId())
        {
            if (isWednesdaySelected == true) {
                isWednesdaySelected = false;
                wed.setBackgroundDrawable(getResources().getDrawable(R.drawable.grey_border));
                updateValues();
            } else {
                isWednesdaySelected = true;
                wed.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_border_box));
                updateValues();
            }
        }
        else if(v.getId()==thurs.getId())
        {
            if (isThursdaySelected == true) {
                isThursdaySelected  = false;
                thurs.setBackgroundDrawable(getResources().getDrawable(R.drawable.grey_border));
                updateValues();
            } else {
                isThursdaySelected  = true;
                thurs.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_border_box));
                updateValues();
            }
        } else if(v.getId()==fri.getId())
        {
            if (isFridaySelected == true) {
                isFridaySelected = false;
                fri.setBackgroundDrawable(getResources().getDrawable(R.drawable.grey_border));
                updateValues();
            } else {
                isFridaySelected = true;
               fri.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_border_box));
                updateValues();
            }
        }
        else if(v.getId()==sat.getId())
        { if (isSaturdaySelected == true) {
            isSaturdaySelected= false;
            sat.setBackgroundDrawable(getResources().getDrawable(R.drawable.grey_border));
            updateValues();
        } else {
            isSaturdaySelected= true;
            sat.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_border_box));
            updateValues();
        }
        }
        else if(v.getId()==sun.getId())
        {
            if (isSundaySelected == true) {
                isSundaySelected = false;
                sun.setBackgroundDrawable(getResources().getDrawable(R.drawable.grey_border));
                updateValues();
            } else {
                isSundaySelected = true;
                sun.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_border_box));
                updateValues();
            }
        }else if(v.getId()==addressTv.getId()){
            showAddressAlert();
        }
        else if(v.getId()==paynow.getId())
        {if(isAddressSelected==true) {
            if(isDeliveryStartDateSelected==true) {
                if (Utils.isInternetAvailable(CartDetails.this)) {
//                    requestedApiType=SaveOrderApi;
//                    new WebAPI().execute(new String[]{"123" ,"345",Integer.toString(getTotalPrice()),"157"});
                    if(AccessToken!=null)
                    {
                        callOrder();
                    }else {

                        requestedApiType = acessTokenApi;
                        new WebAPI().execute();
                    }
                }
            }else{
                Toast.makeText(this,"Select Subscription start date.", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(this,"Select Address", Toast.LENGTH_SHORT).show();
        }
        }
        else if(v.getId()==redeem.getId()) {
            if(isOfferAplied==false) {
                if (promocode.getText().length() > 0) {
                    if(Utils.getIntegerFromString(controller.getOffer().getMinOrderAmount())<getTotalPrice()) {
                        if (Utils.isInternetAvailable(CartDetails.this)) {
                            requestedApiType = redeemCoupon;
                            new WebAPI().execute();

                        } else {
                            Toast.makeText(this, "Select Address", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(this, "Coupon code can not be applied for current order as it is less than minimum order value.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Please enter coupon code.", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(this, "Coupon code already applied.", Toast.LENGTH_SHORT).show();
            }
        }


    }

    public void increaseDayCount() {
        daysCountValue = daysCountValue + 1;
    }

    public void increaseQuantityCount() {
        quantityCountValue = quantityCountValue + 1;
    }

    public void decreaseDaysCount() {
        if (daysCountValue > 1) {
            daysCountValue = daysCountValue - 1;
        }
    }

    public void decreaseQuantityCount() {
        if (quantityCountValue > 1) {
            quantityCountValue = quantityCountValue - 1;
        }
    }

    public int getTotalPrice() {
        int val = quantityCountValue * selectedItem.getPrice() * daysCountValue * getSelectedDays();
        if (offer != null) {
            val = val - getDiscountedValue();
        }
        return val;
    }
    public int getOrderTotal() {
        int val = quantityCountValue * selectedItem.getPrice() * daysCountValue * getSelectedDays();

        return val;
    }
    public int getDiscountedValue() {
        if (offer.getMaximumOfferAmount().contains(".")) {
            String[] value = offer.getMaximumOfferAmount().split(Pattern.quote("."));
            double val = Double.parseDouble(value[1]);
            if (val > 0.5) {
                return (Integer.parseInt(value[0]) + 1);
            }
            return Integer.parseInt(value[0]);
        } else {
            return Integer.parseInt(offer.getMaximumOfferAmount());
        }
    }

    public int getSelectedDays() {
        int val = 0;
        if (isModaySelected) {
            val = val + 1;
        }
        if (isTuesdaySelected) {
            val = val + 1;
        }
        if (isWednesdaySelected) {
            val = val + 1;
        }
        if (isThursdaySelected) {
            val = val + 1;
        }
        if (isFridaySelected) {
            val = val + 1;
        }
        if (isSaturdaySelected) {
            val = val + 1;
        }
        if (isSundaySelected) {
            val = val + 1;
        }
        return val;
    }

    @Override
    public void onClick(int pos) {
         isAddressSelected=true;
        addressData=address.get(pos);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                addressTv.setBackgroundResource(R.color.white);
                addressTv.setTextColor(getResources().getColor(R.color.blue));
                addressTv.setText(addressData.getAddress1()+","+addressData.getAddress2()+","+addressData.getCity()+",\n"+addressData.getState()+","+addressData.getPincode());
            }
        });
        if (dialog != null) {
            dialog.cancel();
        }

    }

    @Override
    public void onDeleteClick(String addressId) {
        if(Utils.isInternetAvailable(CartDetails.this)) {
            requestedApiType=DeleteAdressApi;

            new WebAPI().execute(new String[]{addressId});
        }else{
            Toast.makeText(CartDetails.this,"Internet Unavailable.", Toast.LENGTH_SHORT).show();
        }
        if (dialog != null) {
            dialog.cancel();
        }

    }

    @Override
    public void onEditClick(AddressDataset ds) {
        Address.ds=ds;
        Intent in=new Intent(CartDetails.this,Address.class);
        startActivityForResult(in,1);
        if (dialog != null) {
            dialog.cancel();
        }
    }

    @Override
    public void onAddresssClick(int pos) {
        isAddressSelected=true;
        addressData=address.get(pos);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                addressTv.setText("Delivery Address: "+addressData.getAddress1()+","+addressData.getAddress2()+","+addressData.getCity()+",\n"+addressData.getState()+","+addressData.getPincode());
            }
        });
        if (dialog != null) {
            dialog.cancel();
        }
    }

    public void requestAdressApi()
{
    if(Utils.isInternetAvailable(CartDetails.this)) {
       requestedApiType=AdressApiRequested;
        new WebAPI().execute();
    }
}

    public class WebAPI extends AsyncTask<String, Void, String> {
        String promoCode="";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(requestedApiType==redeemCoupon) {
                promoCode = promocode.getText().toString();
            }
            pd = new ProgressDialog(CartDetails.this);
            pd.setMessage("Please wait....");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        public String doInBackground(String... params) {
            String response="";
if(requestedApiType==AdressApiRequested) {
    response = ApiCall.getData(Common.getAllAddress+"userId="+ Common.UserName+"&addressId=");
}else if(requestedApiType==DeleteAdressApi){
    response = ApiCall.postData(Common.deleteAddress,getJson(params[0]));
}else if(requestedApiType==SaveOrderApi) {
    response = ApiCall.postData(Common.saveOrder,getOrderJson(params[0],params[1],params[2],params[3]));
}
else if(requestedApiType==redeemCoupon) {
    JSONObject jsonObject=new JSONObject();
    {
        try {
            jsonObject.put("id", "122");
        } catch (Exception ex) {
        }
    }
    response = ApiCall.redeemCoupon(Common.getRedeemCouponUrl(promoCode,getTotalPrice()),jsonObject);
}else if(requestedApiType==acessTokenApi)

{
    response = ApiCall.getAcessToken(Common.paymentGatewayUrlLive);
} else if (requestedApiType == getProfileApi) {
    response = ApiCall.getData(Common.getUserProfileUrl + "" + Common.userId);
}

            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(requestedApiType==AdressApiRequested) {
                jsonParsing(s);
            }else if(requestedApiType==DeleteAdressApi){
                if(s.equalsIgnoreCase("true"))
                {
                    if(pd!=null)
                    {pd.cancel();
                    }
                    requestAdressApi();
                    Toast.makeText(CartDetails.this,"Address Deleted.", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(CartDetails.this,s, Toast.LENGTH_SHORT).show();
                }

            } else if(requestedApiType==SaveOrderApi){
                if (s.contains("Order Saved !!!") || (s.equalsIgnoreCase("true"))) {
                    Intent in = new Intent(CartDetails.this, DashBoard.class);
                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(in);
                    Toast.makeText(CartDetails.this, "Order Placed Sucessfully.", Toast.LENGTH_SHORT).show();
                    finish();

                }
            }
            else if(requestedApiType==redeemCoupon){
                if (!s.contains("null") || (s!=null)) {

                    try {

                        offer = new Offers(new JSONObject(s));
                        Toast.makeText(CartDetails.this, "Coupon Applied Sucessfully.", Toast.LENGTH_SHORT).show();
                        String value="You got discount of Rs "+getDiscountedValue();
                        if(freeItem!=null)
                        {
                            value=value+ ", "+freeItem.getName()+" as free item.";
                        }
                        paynow.setText("Pay Rs."+ Integer.toString(getTotalPrice()));
                        alreadyCoupon.setText(value);
                        try {
                            couponJson = new JSONObject(s);
                        }catch (Exception ex)
                        {
                            ex.fillInStackTrace();
                        }
                        isOfferAplied=true;
                    }catch (Exception ex)
                    {
                        ex.fillInStackTrace();
                        Toast.makeText(CartDetails.this, "Invalid coupon code/entered coupon has expired.", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(CartDetails.this, "Invalid coupon code/entered coupon has expired.", Toast.LENGTH_SHORT).show();
                }
            }
            else if(requestedApiType==acessTokenApi)
            {
                if (!s.contains("null") || (s!=null)) {
                    getAcessToken(s);
                }

            }else if(requestedApiType==getProfileApi)
            {
                try{
                    JSONObject jsonObject=new JSONObject(s);
                    controller.setProfile(new ProfileDataset(jsonObject));
                    callOrder();
                }catch (Exception ex)
                {
                    ex.fillInStackTrace();
                }
            }
            if (pd != null) {
                pd.cancel();
            }
        }
    }

    public void jsonParsing(String s) {
        try {
            address.clear();
            JSONArray jsonArray = new JSONArray(s);
            if (jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    AddressDataset ds = new AddressDataset(jsonArray.getJSONObject(i));
                    address.add(ds);
                }

            }else{
                Toast.makeText(CartDetails.this,"No Adress !Please add one address.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            ex.fillInStackTrace();
        }
    }
    public void showFreeItemAlert() {
        dialog = new Dialog(CartDetails.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.free_item_popup);
        ListView list = (ListView) dialog.findViewById(R.id.listView);
        Button done = (Button) dialog.findViewById(R.id.done);
        done.setTypeface( typeface);
        list.setAdapter(new FreeItemAdapter(controller.getOffer().getFreeItems(),CartDetails.this));

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(freeItem!=null)
                {dialog.cancel();
                    alreadyCoupon.setText("You have selected "+freeItem.getName()+" as free item.");
                    Toast.makeText(CartDetails.this,"You have selected "+freeItem.getName()+" as free item.", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(CartDetails.this,"Select any item from above list. "+freeItem.getName()+" as free item.", Toast.LENGTH_SHORT).show();

                }

            }
        });


        dialog.show();
    }

    public void showAddressAlert() {
       dialog = new Dialog(CartDetails.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.addressalert);
        ListView list = (ListView) dialog.findViewById(R.id.list);
        Button addAdress = (Button) dialog.findViewById(R.id.add_Address);
        addAdress.setTypeface( typeface);
        if (address.size() > 0) {
            list.setAdapter(new AddressAdapter(CartDetails.this, address, callback,deleteCallBack));
        }

        addAdress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in =new Intent(CartDetails.this,Address.class);
                startActivityForResult(in,1);
                dialog.cancel();
            }
        });


        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1)
        {
            if(Utils.isInternetAvailable(CartDetails.this)) {
               requestedApiType=AdressApiRequested;
                new WebAPI().execute();
            }else{
                Toast.makeText(CartDetails.this,"Intenet UnAvailable!", Toast.LENGTH_SHORT).show();
            }
        }else if(requestCode ==23){
            if(controller.getOffer()!=null) {
                if (controller.getOffer().getFreeItems().size() > 0) {
                    showFreeItemAlert();
                }
            }
        }
        else  if (requestCode == Constants.REQUEST_CODE && data != null) {
            String orderID = data.getStringExtra(Constants.ORDER_ID);
            String transactionID = data.getStringExtra(Constants.TRANSACTION_ID);
            String paymentID = data.getStringExtra(Constants.PAYMENT_ID);
            if (pd != null) {
                pd.cancel();
            }
            // Check transactionID, orderID, and orderID for null before using them to check the Payment status.
            if (orderID != null && transactionID != null && paymentID != null) {
                requestedApiType=SaveOrderApi;
               final String mobileNumber=controller.getProfile().getMobileNumber();
               final String message="Thank you for making payment of Rs."+ Integer.toString(getTotalPrice())+", your PaymentId:"+paymentID+", your Transaction id :" +transactionID;
                new WebAPI().execute(new String[]{orderID ,transactionID, Integer.toString(getTotalPrice()),paymentID});
                Thread t=new Thread(new Runnable() {
                    @Override
                    public void run() {

                        ApiCall.getData( "https://www.ontimesms.in/Rest/AIwebservice/Bulk?user=mypaybox&password=mypaybox&mobilenumber="+mobileNumber+"&message="+message+"&mtype=n");
                    }
                });
                t.start();
            } else {
                showToast("Your Payment Was Unsucessfull.");
            }

        }
    }

    public JSONObject getOrderJson(String orderId, String transactionId, String amount, String paymentId) {
        JSONObject job = new JSONObject();
        JSONArray items = new JSONArray();
        JSONObject item = new JSONObject();
        JSONObject paymentDetails=new JSONObject();
        JSONObject orderDetail = new JSONObject();
        try {
            job.put("Id", Common.userId);
            job.put("userName", Common.UserName);
            job.put( "AddressId",addressData.getAddressId());
            job.put("VendorId",VendorDetail.selectedVendor.getId());
            job.put("OrderStartDate", orderDeliveryStartDate+"T03:53:25.1131434-07:00");
            job.put("weekCount",daysCount.getText().toString());
            job.put("orderDeliveryTime",timmings.getSelectedItem().toString());

            item.put("itemId", selectedItem.getId());
            item.put("quantity", selectedItem.getMeasurement());
            item.put("numberUnit", totalQuantity.getText().toString());
            item.put("deliveryDuration", getFrequency());
            items.put(0, item);
            orderDetail.put("items", items);
            orderDetail.put("id", "");
            job.put("orderDetail", orderDetail);
            job.put("OrderTotal",getOrderTotal());
            if(isOfferAplied==true)
            {
                job.put("offerCoupon", couponJson);
            }else{
                job.put("offerCoupon", "null");
            }
            if(freeItem!=null)
            {
                job.put("OfferItem",freeItem.getName());
            }else{
                job.put("OfferItem", "null");
            }
            paymentDetails.put("PaymentTransactionId", transactionId);
            paymentDetails.put("PaymentApprovalId", paymentId);
            paymentDetails.put("PaymentAmount", amount);
            paymentDetails.put("OtherPaymentInformation", "Sucess");
            paymentDetails.put("PaymentMethod","Online");
            job.put("OrderPayment", paymentDetails);
        } catch (Exception ex) {
            ex.fillInStackTrace();
        }
        return job;
    }

    public String getFrequency() {
        String val = "";
        switch (frequencyType) {
            case 1:
                val = "All Date: All";
                break;
            case 2:
                val = "custom Day: Mon,Wed,Fri,Sun";
            case 3:
                if ((isModaySelected) && (isTuesdaySelected) && (isWednesdaySelected) && (isThursdaySelected) && (isFridaySelected) && (isSaturdaySelected) && (isSundaySelected))

                {
                    val = "custom Day: Sun, Mon, Tue,Wed, Thur,Fri,Sat,Sun";
                } else {
                    if (isSundaySelected) {
                        val += "Sun";
                    }
                    if (isModaySelected) {
                        val += ",Mon";
                    }
                    if (isTuesdaySelected) {
                        val += ",Tue";
                    }
                    if (isWednesdaySelected) {
                        val += ",Wed";
                    }
                    if (isThursdaySelected) {
                        val += ",Thur";
                    }
                    if (isFridaySelected) {
                        val += ",Fri";
                    }
                    if (isSaturdaySelected) {
                        val += ",Sat";
                    }
                    val = "custom Day:" + val;
                }
                break;
        }
        return val;
    }

    public JSONObject getJson(String addressId) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("AddressId", addressId);
            jsonObject.put("UserId", Common.UserName);
        } catch (Exception ex) {
            ex.fillInStackTrace();
        }
        return jsonObject;
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month, day+2);
            long currentTime = System.currentTimeMillis();
            long endOfTomorrow = currentTime + DateUtils.DAY_IN_MILLIS
                    + (DateUtils.DAY_IN_MILLIS - currentTime % DateUtils.DAY_IN_MILLIS);
            dialog.getDatePicker().setMinDate(endOfTomorrow);
            return  dialog;
        }
        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            isDeliveryStartDateSelected=true;
            month=month+1;
            orderDeliveryStartDate=""+year;
            if(month<10)
            {
                orderDeliveryStartDate=  orderDeliveryStartDate+"-0"+month;
            }else{
                orderDeliveryStartDate=  orderDeliveryStartDate+"-"+month;
            }
            if(day<10)
            {  orderDeliveryStartDate=  orderDeliveryStartDate+"-0"+day;}
            else{
                orderDeliveryStartDate=  orderDeliveryStartDate+"-"+day;
            }
            //orderDeliveryStartDate=year+"-"+month+"-"+day;
            date.setText("Start date :"+orderDeliveryStartDate );
        }


    }

    /**********************payment gateway***********************/
    public void startRequest(Order order) {
        Request request = new Request(order, new OrderRequestCallBack() {
            @Override
            public void onFinish(Order order, Exception error) {
                //dismiss the dialog if showed

                // Make sure the follwoing code is called on UI thread to show Toasts or to
                //update UI elements
                if (error != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(pd!=null)
                            {
                                pd.cancel();
                            }
                        }
                    });
                    if (error instanceof Errors.ConnectionError) {
                        showToast("No internet connection");
                        Log.e("App", "No internet connection");
                    } else if (error instanceof Errors.ServerError) {
                        showToast("Server Error. Try again");
                        Log.e("App", "Server Error. Try again");
                    } else if (error instanceof Errors.AuthenticationError) {
                        showToast("Access token is invalid or expired");
                        Log.e("App", "Access token is invalid or expired");
                    } else if (error instanceof Errors.ValidationError) {
                        // Cast object to validation to pinpoint the issue
                        Errors.ValidationError validationError = (Errors.ValidationError) error;
                        if (!validationError.isValidTransactionID()) {
                            showToast("Transaction ID is not Unique");
                            Log.e("App", "Transaction ID is not Unique");
                            return;
                        }
                        if (!validationError.isValidRedirectURL()) {
                            Log.e("App", "Redirect url is invalid");
                            return;
                        }


                        if (!validationError.isValidWebhook()) {

                            return;
                        }

                        if (!validationError.isValidPhone()) {
                            showToast("Buyer's Phone Number is invalid/empty");
                            Log.e("App", "Buyer's Phone Number is invalid/empty");
                            return;
                        }
                        if (!validationError.isValidEmail()) {
                            showToast("Buyer's Email is invalid/empty");
                            Log.e("App", "Buyer's Email is invalid/empty");
                            return;
                        }
                        if (!validationError.isValidAmount()) {
                            showToast("Amount is either less than Rs.9 or has more than two decimal places");
                            Log.e("App", "Amount is either less than Rs.9 or has more than two decimal places");
                            return;
                        }
                        if (!validationError.isValidName()) {
                            showToast("Buyer's Name is required");
                            Log.e("App", "Buyer's Name is required");
                            return;
                        }
                    } else {
                        Log.e("App", error.getMessage());
                    }
                    return;
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(pd!=null)
                        {
                            pd.cancel();
                        }
                    }
                });
                startPreCreatedUI(order);
            }
        });

        request.execute();
    }

    public void showDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pd = new ProgressDialog(CartDetails.this);
                pd.setMessage("Please wait....");
                pd.setCancelable(false);
                pd.show();
            }
        });
    }
    private void startPreCreatedUI(Order order){
        //Using Pre created UI
        showDialog();
        Intent intent = new Intent(getBaseContext(), PaymentDetailsActivity.class);
        intent.putExtra(Constants.ORDER, order);
        startActivityForResult(intent, Constants.REQUEST_CODE);
    }

    public void callOrder() {
        if (pd != null) {
            pd.cancel();
        }
        if (controller.getProfile() != null) {
            Order order = new Order(AccessToken, getOrderNumber(), controller.getProfile().getName(), controller.getProfile().getEmailId(), controller.getProfile().getMobileNumber(), Integer.toString(getTotalPrice()), "Milk");
            if (validateOrder(order)) {
                showDialog();
                startRequest(order);
            } else {
                showToast("Invalid Order Details");
            }
        } else {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    requestedApiType = getProfileApi;
                    new WebAPI().execute();
                }
            });

        }
    }

    public void getAcessToken(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            AccessToken = jsonObject.getString("access_token");
            controller.setPaymentGatewayToken(AccessToken);
            callOrder();
        } catch (Exception ex) {
            ex.fillInStackTrace();
        }
    }

    public String getOrderNumber() {
        int min = 10000;
        int max = 999999999;
        Random r = new Random();
        int i1 = r.nextInt(max - min + 1) + min;
        return Integer.toString(i1);
    }

    public void showToast(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(CartDetails.this, message, Toast.LENGTH_SHORT).show();
            }
        });

    }

    public boolean validateOrder(Order order) {
        if (!order.isValid()) {
            if (!order.isValidName()) {
                Log.e("App", "Buyer name is invalid");
                showToast("Buyer name is invalid");
            }

            if (!order.isValidEmail()) {
                Log.e("App", "Buyer email is invalid");
                showToast("Buyer email is invalid");
            }

            if (!order.isValidPhone()) {
                Log.e("App", "Buyer phone is invalid");
                showToast("Buyer phone is invalid");
            }

            if (!order.isValidAmount()) {
                Log.e("App", "Amount is invalid");
                showToast("Amount is invalid");

            }

            if (!order.isValidDescription()) {
                Log.e("App", "description is invalid");
                showToast("description is invalid");

            }

            if (!order.isValidTransactionID()) {
                Log.e("App", "Transaction ID is invalid");
                showToast("Transaction ID is invalid");
            }

            if (!order.isValidRedirectURL()) {
                Log.e("App", "Redirection URL is invalid");
                showToast( "Redirection URL is invalid");
            }

            if (!order.isValidWebhook()) {
             showToast("Webhook URL is invalid");

            }

            return false;
        } else {
            return true;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        controller.resetOffer();
        freeItem=null;
    }
}
