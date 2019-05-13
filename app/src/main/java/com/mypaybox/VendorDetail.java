package com.mypaybox;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.instamojo.android.activities.PaymentDetailsActivity;
import com.instamojo.android.callbacks.OrderRequestCallBack;
import com.instamojo.android.helpers.Constants;
import com.instamojo.android.models.Errors;
import com.instamojo.android.models.Order;
import com.instamojo.android.network.Request;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import adapter.AddressAdapter;
import adapter.VendorDetailsListAdapter;
import common.ApiCall;
import common.AppController;
import common.Common;
import common.Utils;
import interfaces.Callback;
import interfaces.DeleteCallBack;
import login.Login;
import model.AddressDataset;
import model.ProfileDataset;
import model.VendorDataset;
import model.VendorItemsDataset;

/**
 * Created by ashish.kumar on 11-04-2017.
 */

public class VendorDetail  extends AppCompatActivity implements View.OnClickListener, Callback, DeleteCallBack {
    Dialog dialog,dialog1;
    ImageView back;
    TextView headingView;
    ListView list;
    ProgressDialog pd;
    public static VendorDataset selectedVendor=null;
    ArrayList<VendorItemsDataset> dataList = new ArrayList<VendorItemsDataset>();
    ImageView vendorImage;
    TextView vendorName,reviews;
    RatingBar vendorRating;
    Button subscribe,getToday;
    VendorDetailsListAdapter adapter;
    VendorItemsDataset selectedItem=null;
    Typeface typeface;
    ArrayList<AddressDataset> address=new ArrayList<>();
    Callback callback;
    DeleteCallBack deleteCallBack;
    AddressDataset addressData;
    Boolean isAddressSelected=false;

    int quantityCountValue=1;
    TextView addresss;
    Spinner timmings;
    String[]timmingArray={"5 am - 11 am","5 pm - 9.30 pm"};
    String accessToken=null;
    AppController controller;
    int requestedApiType;
    int tommorowApi=1;
    int adressApi=2;
    int deleteAdressApi=3;
    int getVendorProduct=4;
    int getProfileApi=5;
    int acessTokenApi=6;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vendordetails);
        initializeAll();
    }

    public void initializeAll() {
        controller=(AppController)getApplicationContext();
        accessToken=controller.getPaymentGatewayToken();
        callback=this;
        deleteCallBack=this;
        typeface= Typeface.createFromAsset(getAssets(), "lato_light.ttf");
        back = (ImageView) findViewById(R.id.back);
        headingView = (TextView) findViewById(R.id.heading);
        list = (ListView) findViewById(R.id.listView);
        subscribe=(Button)findViewById(R.id.subscribe);
        getToday=(Button)findViewById(R.id.getToday);
        vendorImage=(ImageView) findViewById(R.id.bgImage);
        vendorName= (TextView) findViewById(R.id.vendorName);
        reviews= (TextView) findViewById(R.id.reviews);
        vendorRating=(RatingBar) findViewById(R.id.ratingBar);
        back.setOnClickListener(this);
        headingView.setText("Vendor's Products");
        subscribe.setTypeface(typeface);
        getToday.setTypeface(typeface);
        if(selectedVendor!=null)
        {
            vendorName.setText(selectedVendor.getName());
            Picasso.with(VendorDetail.this).load(selectedVendor.getImagepath()).fit().placeholder(R.drawable.daymilk).into(vendorImage);
            reviews.setText(selectedVendor.getFeedBack().size()+ " Reviews");
            reviews.setPaintFlags(reviews.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            vendorRating.setRating(Float.parseFloat(selectedVendor.getRating()));
        }
        if (Utils.isInternetAvailable(VendorDetail.this)) {
            requestedApiType=getVendorProduct;
            new GetData().execute();
        } else {
            Toast.makeText(this, "Internet Unavailable", Toast.LENGTH_SHORT).show();
        }
        subscribe.setOnClickListener(this);
        getToday.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == back.getId()) {
            onBackPressed();
        }else if((v.getId()==subscribe.getId())) {
            if(isItemChecked()==true) {

                    CartDetails.selectedItem = selectedItem;
                    Intent in = new Intent(VendorDetail.this, CartDetails.class);
                    startActivity(in);
            }else{
                Toast.makeText(VendorDetail.this,"Please select one item", Toast.LENGTH_SHORT).show();
            }
        }
            else if((v.getId()==getToday.getId())) {
            if(isItemChecked()==true) {
                showTommorowAlert();
            }else{
                Toast.makeText(VendorDetail.this,"Please select one item", Toast.LENGTH_SHORT).show();
            }
            }
        }

    public String getTommorowsDate()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date tomorrow = calendar.getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String tomorrowAsString = dateFormat.format(tomorrow);
        return tomorrowAsString;
    }
public int getTotalPrice()
{
    return quantityCountValue* selectedItem.getPrice();
}
    public JSONObject getOrderJson(String orderId, String transactionId, String amount, String paymentId) {
        JSONObject job = new JSONObject();
        JSONArray items = new JSONArray();
        JSONObject item = new JSONObject();
        JSONObject orderDetail = new JSONObject();
        JSONObject paymentDetails=new JSONObject();
        try {
            job.put("Id", Common.userId);
            job.put("userName", Common.UserName);
            job.put( "AddressId",addressData.getAddressId());
            job.put("VendorId",VendorDetail.selectedVendor.getId());
            item.put("itemId", selectedItem.getId());
            item.put("quantity", selectedItem.getMeasurement());
            job.put("orderDeliveryTime",timmings.getSelectedItem().toString());
            item.put("numberUnit", String.valueOf(quantityCountValue));
            item.put("deliveryDuration", "Tomorrow:" + getTommorowsDate());
            job.put("OrderStartDate", getTommorowsDate()+"T03:53:25.1131434-07:00");
            items.put(0, item);
            paymentDetails.put("PaymentTransactionId", transactionId);
            paymentDetails.put("PaymentApprovalId", paymentId);
            paymentDetails.put("PaymentAmount", amount);
            paymentDetails.put("OtherPaymentInformation", "Sucess");
            paymentDetails.put("PaymentMethod","Online");
            orderDetail.put("items", items);
            orderDetail.put("id", orderId);
            job.put("orderDetail", orderDetail);
            job.put("OrderPayment", paymentDetails);
        } catch (Exception ex) {
            ex.fillInStackTrace();
        }
        return job;
    }
    public boolean isItemChecked() {
        for (int i = 0; i < dataList.size(); i++) {
            if (dataList.get(i).isItemChecked() == true) {
                selectedItem = dataList.get(i);
                return true;
            }

        }
        return false;
    }
    @Override
    public void onClick(int pos) {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDeleteClick(String addressId) {
        if(Utils.isInternetAvailable(VendorDetail.this)) {
            requestedApiType=deleteAdressApi;
            new GetData().execute(new String[]{addressId});
        }else{
            Toast.makeText(VendorDetail.this,"Internet Unavailable.", Toast.LENGTH_SHORT).show();
        }
        if (dialog != null) {
            dialog.cancel();
        }

    }

    @Override
    public void onEditClick(AddressDataset ds) {
        Address.ds=ds;
        Intent in=new Intent(VendorDetail.this,Address.class);
        startActivityForResult(in,1);
        if (dialog != null) {
            dialog.cancel();
        }
    }

    @Override
    public void onAddresssClick(int pos) {
        isAddressSelected = true;
        addressData = address.get(pos);
        if (dialog != null) {
            dialog.cancel();
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                addresss.setText("Delivery Address: "+addressData.getAddress1()+","+addressData.getAddress2()+","+addressData.getCity()+",\n"+addressData.getState()+","+addressData.getPincode());
            }
        });

    }
public void callGetAddress()
{requestedApiType=adressApi;
    new GetData().execute();
}

    public class GetData extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(VendorDetail.this);
            pd.setMessage("Please wait....");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        public String doInBackground(String... params) {
            String response = "";
            if(requestedApiType==adressApi) {
                response = ApiCall.getData(Common.getAllAddress+"userId="+ Common.UserName+"&addressId=");
            }
           else if(requestedApiType==deleteAdressApi){
                response = ApiCall.postData(Common.deleteAddress,getJson(params[0]));
            }
            else if(requestedApiType==tommorowApi){

               response = ApiCall.postData(Common.saveOrder,getOrderJson(params[0],params[1],params[2],params[3]));
           }
            else if (requestedApiType==getVendorProduct){
                response = ApiCall.getData(Common.getVendorDetailsUrl + "" + selectedVendor.getId());
            }else if(requestedApiType==getProfileApi)
            {
                response = ApiCall.getData(Common.getUserProfileUrl + "" + Common.userId);
            }else if(requestedApiType==acessTokenApi)

            {
                response = ApiCall.getAcessToken(Common.paymentGatewayUrlLive);
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(requestedApiType==adressApi) {
                jsonParsing(s);
            }else if(requestedApiType==deleteAdressApi){
                if(s.equalsIgnoreCase("true"))
                {
                    if(pd!=null)
                    {pd.cancel();
                    }
                    callGetAddress();
                    Toast.makeText(VendorDetail.this,"Address Deleted.", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(VendorDetail.this,s, Toast.LENGTH_SHORT).show();
                }

            }
            else if(requestedApiType==tommorowApi)
            {
                if((s.contains("Order Saved !!!"))||(s.equalsIgnoreCase("true")))
                {
                    Intent in =new Intent(VendorDetail.this,DashBoard.class);
                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(in);
                    Toast.makeText(VendorDetail.this,"Order Placed Sucessfully.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            } else if (requestedApiType == getVendorProduct) {
                JsonParsing(s);
                if (pd != null) {
                    pd.cancel();
                    callGetAddress();
                }
            } else if(requestedApiType==acessTokenApi)
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
                Toast.makeText(VendorDetail.this,"No Adress !Please add one address.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            ex.fillInStackTrace();
        }
    }
    public void JsonParsing(String s) {
        try {
            dataList.clear();
            JSONArray jsonArray=new JSONArray(s);
            for(int i=0;i<jsonArray.length();i++)
            {
                JSONObject job=jsonArray.getJSONObject(i);
                VendorItemsDataset vds=new VendorItemsDataset(job);
                dataList.add(vds);
            }
            if(dataList.size()>0) {
               adapter=new VendorDetailsListAdapter(VendorDetail.this, dataList,this);
                list.setAdapter(adapter);
            }else{
                Toast.makeText(VendorDetail.this, "No Item found for selected vendor.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (!jsonObject.isNull("Message")) {
                    PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().clear().apply();
                    Toast.makeText(VendorDetail.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(VendorDetail.this, Login.class);
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(in);


                }
            } catch (Exception exx) {
                exx.fillInStackTrace();
            }
        }
    }
    public void showAddressAlert() {
        dialog = new Dialog(VendorDetail.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.addressalert);
        ListView list = (ListView) dialog.findViewById(R.id.list);
        Button addAdress = (Button) dialog.findViewById(R.id.add_Address);
        addAdress.setTypeface( typeface);
        if (address.size() > 0) {
            list.setAdapter(new AddressAdapter(VendorDetail.this, address, callback,deleteCallBack));
        }

        addAdress.setOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Address.ds=null;
                Intent in =new Intent(VendorDetail.this,Address.class);
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
            if(Utils.isInternetAvailable(VendorDetail.this)) {
                requestedApiType=adressApi;
                new GetData().execute();
            }else{
                Toast.makeText(this,"Intenet UnAvailable!", Toast.LENGTH_SHORT).show();
            }
        }else  if (requestCode == Constants.REQUEST_CODE && data != null) {
            String orderID = data.getStringExtra(Constants.ORDER_ID);
            String transactionID = data.getStringExtra(Constants.TRANSACTION_ID);
            String paymentID = data.getStringExtra(Constants.PAYMENT_ID);
            if (pd != null) {
                pd.cancel();
            }
            // Check transactionID, orderID, and orderID for null before using them to check the Payment status.
            if (orderID != null && transactionID != null && paymentID != null) {
                requestedApiType=tommorowApi;
                new GetData().execute(new String[]{orderID ,transactionID, Integer.toString(getTotalPrice()),paymentID});
            } else {
                showToast("Your Payment Was Unsucessfull");
                //Oops!! Payment was cancelled
            }
        }
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
public void showTommorowAlert()
{
    dialog1 = new Dialog(VendorDetail.this);
    dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);

    dialog1.setContentView(R.layout.tommorowalert);
    dialog1.setCancelable(true);
    ImageButton addUnit=(ImageButton)dialog1.findViewById(R.id.add);
    ImageButton minus=(ImageButton)dialog1.findViewById(R.id.minus);
    final TextView unitsCount=(TextView)dialog1.findViewById(R.id.itemCount);
    addresss=(TextView)dialog1.findViewById(R.id.address);
    timmings=(Spinner)dialog1.findViewById(R.id.timming);
    timmings.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,timmingArray));
    Button done=(Button)dialog1.findViewById(R.id.done);
    done.setTypeface(typeface);
    addUnit.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            increaseQuantityCount();
            unitsCount.setText(String.valueOf(quantityCountValue));
        }
    });
    minus.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            decreaseQuantityCount();
            unitsCount.setText(String.valueOf(quantityCountValue));
        }
    });
    addresss.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showAddressAlert();
        }
    });
    done.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(quantityCountValue>0)
            {
                if(isAddressSelected==true)
                {
                    if (Utils.isInternetAvailable(VendorDetail.this)) {
                        if(accessToken!=null)
                        {
                            callOrder();
                        }else {

                            requestedApiType = acessTokenApi;
                            new GetData().execute();
                        }
                    }
                }else{
                    Toast.makeText(VendorDetail.this,"Select Address", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(VendorDetail.this,"Quantity must be greater than 0 ", Toast.LENGTH_SHORT).show();
            }
        }
    });
    dialog1.show();
}


    public void decreaseQuantityCount() {
        if (quantityCountValue > 1) {
            quantityCountValue = quantityCountValue - 1;
        }
    }
    public void increaseQuantityCount() {
        quantityCountValue = quantityCountValue + 1;
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
                pd = new ProgressDialog(VendorDetail.this);
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
            Order order = new Order(accessToken, getOrderNumber(), controller.getProfile().getName(), controller.getProfile().getEmailId(), controller.getProfile().getMobileNumber(), Integer.toString(getTotalPrice()), "Milk");
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
                    new GetData().execute();
                }
            });

        }
    }

    public void getAcessToken(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            accessToken = jsonObject.getString("access_token");
            controller.setPaymentGatewayToken(accessToken);
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
                Toast.makeText(VendorDetail.this, message, Toast.LENGTH_SHORT).show();
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
}
