package com.mypaybox;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import adapter.OrderHistoryAdapter;
import common.ApiCall;
import common.Common;
import common.Utils;
import interfaces.CancelOrderCallBack;
import model.VendorItemsDataset;

/**
 * Created by ashish.kumar on 03-05-2017.
 */

public class MyOrder extends Activity implements View.OnClickListener, CancelOrderCallBack {
    ImageView back;
    TextView headingView;
    ListView list;
    ProgressDialog pd;
    ArrayList<VendorItemsDataset> orderList=new ArrayList<>();
    boolean isCancelOrderCalled=false;
    int position=-1;
    Typeface typeface;
   static Button stopdate;
   static Button restartdate;
    static boolean isStopSelected=false;
    static String stopDateValue="";
    static String restartDateValue="null";
    OrderHistoryAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sellerlist);
        initializeAll();
        if (Utils.isInternetAvailable(MyOrder.this)) {
            new GetOrderHistory().execute();
        } else {
            Toast.makeText(this,"Internet Unavailable.", Toast.LENGTH_SHORT).show();
        }
    }

    public void initializeAll() {
      typeface= Typeface.createFromAsset(getAssets(), "font.ttf");
        back = (ImageView) findViewById(R.id.back);
        headingView = (TextView) findViewById(R.id.heading);
        headingView.setTypeface(typeface);
        list = (ListView) findViewById(R.id.listView);
        back.setOnClickListener(this);
        headingView.setText("My Orders");
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.back) {
            onBackPressed();
        }
    }

    @Override
    public void onCancelClicked(final int pos) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                position=pos;
                isCancelOrderCalled=true;
                showCancelOrderAlert();
            }
        });

    }

    public class GetOrderHistory extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(MyOrder.this);
            pd.setMessage("Please wait....");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        public String doInBackground(String... params) {
            String response=null;
            if(isCancelOrderCalled==true)
            {JSONObject jsonObject=new JSONObject();
                try {
                    jsonObject.put("id", "ww");
                }catch (Exception ex)
                {ex.fillInStackTrace();}
                response = ApiCall.post(Common.getCancelOrderUrl(params[0],params[1],params[2],params[3]),jsonObject);
            }else {
                response = ApiCall.getData(Common.getOrderHistory + "" + Common.UserName);
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            ParseData(s);
            if (pd != null) {
                pd.cancel();
            }
        }}
    public void ParseData(String s)
    {

        if(s!=null)
        {
            try {
                if(isCancelOrderCalled==true)
                {isCancelOrderCalled=false;
                    if(restartDateValue.equalsIgnoreCase("null")) {
                        orderList.get(position).setMessage(s);
                        orderList.get(position).setOrderCancelled(true);
                        adapter.notifyDataSetChanged();
                    }
                    Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
                }else {
                    orderList.clear();
                    JSONArray jsonArray = new JSONArray(s);
                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject job = jsonArray.getJSONObject(i);
                            JSONObject order = job.getJSONObject("orderDetail");
                            JSONArray itemArray = order.getJSONArray("items");
                            VendorItemsDataset ds = new VendorItemsDataset(itemArray.getJSONObject(0), 0);
                            ds.setStartDate(job.getString("OrderStartDate"));
                            ds.setEndDate(job.getString("OrderEndTime"));
                            ds.setOrderId(job.getString("Id"));
                            orderList.add(ds);
                        }
                        if (orderList.size() > 0) {
                          adapter=  new OrderHistoryAdapter(orderList, MyOrder.this);
                            list.setAdapter(adapter);
                        }
                    } else {
                        Toast.makeText(this, "No Orders.", Toast.LENGTH_SHORT).show();
                    }
                }
                }catch(Exception ex)
                {
                    ex.fillInStackTrace();
                }

        }
    }
    public void showCancelOrderAlert() {
        final Dialog dialog = new Dialog(MyOrder.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.cancel_order_dailog);
        TextView header=(TextView)dialog.findViewById(R.id.header);
        RadioGroup group=(RadioGroup)dialog.findViewById(R.id.radioGroup);
        final RadioButton cancelOrder=(RadioButton)dialog.findViewById(R.id.permanetCancel);
        RadioButton tempCancel=(RadioButton)dialog.findViewById(R.id.customCancel);
         stopdate=(Button)dialog.findViewById(R.id.stopDate);
        restartdate=(Button)dialog.findViewById(R.id.restartDate);
        final Button cancel=(Button)dialog.findViewById(R.id.cancel);
        Button submit=(Button)dialog.findViewById(R.id.submit);
        header.setTypeface(typeface);
        cancelOrder.setTypeface(typeface);
       tempCancel.setTypeface(typeface);
        stopdate.setTypeface(typeface);
        restartdate.setTypeface(typeface);
         cancel.setTypeface(typeface);
        submit.setTypeface(typeface);
        group.check(cancelOrder.getId());
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if(checkedId==cancelOrder.getId())
                {
                    restartdate.setVisibility(View.GONE);
                    restartDateValue="null";
                }else {
                    restartdate.setVisibility(View.VISIBLE);
                    restartDateValue="";
                }
            }
        });
        stopdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStopSelected=true;
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getFragmentManager(), "Select Stop Date");
            }
        });
        restartdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStopSelected=false;
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getFragmentManager(), "Select Restart Date");
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
          dialog.cancel();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(restartdate.getVisibility()== View.VISIBLE) {
                    if ((restartDateValue.length() > 0) && (stopDateValue.length() > 0)) {
                        String[] orderDetils=new String[]{orderList.get(position).getOrderId(),orderList.get(position).getStartDate(),restartDateValue+"T00:00:00",stopDateValue+"T00:00:00"};
                        new GetOrderHistory().execute(orderDetils);
                    } else {
                        if (stopDateValue.length() == 0) {
                            Toast.makeText(MyOrder.this, "Please select Stop Date", Toast.LENGTH_SHORT).show();
                        } else if (restartDateValue.length() == 0) {
                            Toast.makeText(MyOrder.this, "Please select restartDate", Toast.LENGTH_SHORT).show();
                        }
                    }

                }else{
                    if (stopDateValue.length() != 0)
                    {
                        new GetOrderHistory().execute(new String[]{orderList.get(position).getOrderId(),orderList.get(position).getStartDate(),"null",stopDateValue+"T00:00:00"});
                    }else{
                        Toast.makeText(MyOrder.this, "Please select Stop Date", Toast.LENGTH_SHORT).show();
                    }
                }
                dialog.cancel();
            }
        });
        dialog.show();

    }
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month, day);
            long currentTime = System.currentTimeMillis();

            dialog.getDatePicker().setMinDate(currentTime);
            return  dialog;
        }
        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {

            month=month+1;
            String orderDate=""+year;
            if(month<10)
            {
                orderDate= orderDate+"-0"+month;
            }else{
                orderDate=  orderDate+"-"+month;
            }
            if(day<10)
            {  orderDate=  orderDate+"-0"+day;}
            else{
                orderDate=  orderDate+"-"+day;
            }
            //orderDeliveryStartDate=year+"-"+month+"-"+day;
            if(isStopSelected==true) {
                stopdate.setText("Stop Date :"+ orderDate);
                stopDateValue=orderDate;
            }else{
               restartdate.setText("Restart Date :"+orderDate);
                restartDateValue=orderDate;
            }
        }


    }

}
