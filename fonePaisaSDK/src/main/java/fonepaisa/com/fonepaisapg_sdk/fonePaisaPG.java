package fonepaisa.com.fonepaisapg_sdk;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;


import org.json.JSONException;
import org.json.JSONObject;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by nischal on 11/21/2016.
 */
public class fonePaisaPG extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_READ_SMS = 23;
    String test_CBSU = "https://test.fonepaisa.com/portal/ASDPay/success";
    String test_CBFU = "https://test.fonepaisa.com/portal/ASDPay/success";
    String test_PGURL = "https://test.fonepaisa.com/pg/pay";
    String prod_CBSU = "https://secure.fonepaisa.com/portal/ASDPay/success";
    String prod_CBFU = "https://secure.fonepaisa.com/portal/ASDPay/success";
    String prod_PGURL = "https://secure.fonepaisa.com/pg/pay";
    String Environment = "";
    String callback_failure_url = "";
    String callback_success_url = "";
    String PG_URL = "";
    JSONObject json_to_be_sent_toPG = new JSONObject();
    ImageButton close_btn;
    int PG_First_Load=0;
    Boolean broadcastReceiverStarted = false;
    OTPReader otpReader = new OTPReader();
    WebView fPWV1;
    Boolean webViewStarted = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_fp);
        close_btn = (ImageButton) findViewById(R.id.close_btn);
        close_btn.setOnClickListener(new closeBtn());
        Intent intent = getIntent();
        String json_str_recieved = intent.getStringExtra("data");
        try {
            JSONObject json_obj = new JSONObject(json_str_recieved);
            json_check(json_obj);
            Environment = json_obj.getString("Environment");
            if (Environment.equals(FPConstants.Production_Environment)) {
                callback_failure_url = prod_CBFU;
                callback_success_url = prod_CBSU;
                PG_URL = prod_PGURL;
            } else if (Environment.equals(FPConstants.Test_Environment)) {
                callback_failure_url = test_CBFU;
                callback_success_url = test_CBSU;
                PG_URL = test_PGURL;
            } else {
                returnTheactivity("8013", "TEST_OR_PROD key can accept only values TEST or PROD");
            }
            json_obj.put("callback_failure_url", callback_failure_url);
            json_obj.put("callback_url", callback_success_url);
            json_to_be_sent_toPG = json_obj;
            start_webView();
        } catch (JSONException e) {
            e.printStackTrace();
            returnTheactivity("8999", "JSON Exeption! Please fix the JSON being sent");
        }
    }

    private void json_check(JSONObject json_obj){
        if (!json_obj.has("Environment")) {
            returnTheactivity("8001", "Environment is mandatory");
        }
        if (!json_obj.has("id")) {
            returnTheactivity("8002", "id is mandatory");
        }
        if (!json_obj.has("merchant_id")) {
            returnTheactivity("8003", "merchant_id is mandatory");
        }
        if (!json_obj.has("invoice")) {
            returnTheactivity("8004", "invoice is mandatory");
        }
        if (!json_obj.has("invoice_amt")) {
            returnTheactivity("8005", "invoice_amt is mandatory");
        }
        if (!json_obj.has("sign")) {
            returnTheactivity("8011", "sign is mandatory ");
        }
        try {
            if (json_obj.get("Environment").toString().equals("")||json_obj.get("Environment").toString().trim().isEmpty()) {
                returnTheactivity("8006", "Environment is mandatory");
            }
            if (json_obj.get("id").toString().equals("")||json_obj.get("id").toString().trim().isEmpty()) {
                returnTheactivity("8007", "id is mandatory");
            }
            if (json_obj.get("merchant_id").toString().equals("")||json_obj.get("merchant_id").toString().trim().isEmpty()) {
                returnTheactivity("8008", "merchant_id  is mandatory");
            }
            if (json_obj.get("invoice").toString().equals("")||json_obj.get("invoice").toString().trim().isEmpty()) {
                returnTheactivity("8009", "invoice is mandatory");
            }
            if (json_obj.get("invoice_amt").toString().equals("")||json_obj.get("invoice_amt").toString().trim().isEmpty()) {
                returnTheactivity("8010", "invoice_amt is mandatory");
            }
            if(json_obj.get("sign").toString().equals("")||json_obj.get("sign").toString().trim().isEmpty()) {
                returnTheactivity("8012", "sign is mandatory");
            }
        }catch (JSONException e) {
            e.printStackTrace();
            returnTheactivity("8999", "JSON Exeption! Please fix the JSON being sent");
        }
        return;
    }

    private void start_webView() {

        fPWV1 = (WebView) findViewById(R.id.wv_fp);
        String request;
        fPWV1.addJavascriptInterface(new WebAppInterface(this), "Android");
        fPWV1.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            fPWV1.setWebContentsDebuggingEnabled(true);    //can be enabled to do chrome inspect
        }
        fPWV1.getSettings().setJavaScriptEnabled(true);
        fPWV1.setWebViewClient(new FPWebViewClient());
        request = getjsonString(json_to_be_sent_toPG);
        System.out.println(request);
        fPWV1.postUrl(PG_URL, request.getBytes());
        webViewStarted = true;
    }
    private class FPWebViewClient extends WebViewClient{
        ProgressDialog progressDialog;
        //Show loader on url load
        public void onLoadResource (WebView view, String url) {
            if(url.equals(PG_URL)&&PG_First_Load==0) {
                if (progressDialog == null) {
                    // in standard case YourActivity.this
                    progressDialog = new ProgressDialog(fonePaisaPG.this);
                    progressDialog.setMessage("Loading...");
                    progressDialog.show();
                }
            }
        }
        public void onPageFinished(WebView view, String url) {
            if(url.equals(PG_URL)&&PG_First_Load==0) {
                try{
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                        progressDialog = null;
                        PG_First_Load++;
                    }
                }catch(Exception exception){
                    exception.printStackTrace();
                }
            }else{
                if(!broadcastReceiverStarted && !url.contains("fonepaisa")) {
                    if (ContextCompat.checkSelfPermission(fonePaisaPG.this,
                            Manifest.permission.RECEIVE_SMS)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(fonePaisaPG.this,
                                new String[]{Manifest.permission.RECEIVE_SMS},
                                MY_PERMISSIONS_REQUEST_READ_SMS);
                    } else {
                        registerReceiver(
                                otpReader,
                                new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
                        broadcastReceiverStarted = true;
                    }
                }
            }
        }
    }
    private String getjsonString(JSONObject json) {
        String Post_data = "";
        Iterator<String> iter = json.keys();
        while (iter.hasNext()) {
            String key = iter.next();
            try {
                Object value = json.get(key);
                if (Post_data.equals("")) {
                    Post_data = key + "=" + value;
                } else {
                    Post_data += "&" + key + "=" + value;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                returnTheactivity("8999", "JSON Exeption! Please fix the JSON being sent");
            }
        }
        return Post_data;
    }


    public void returnTheactivity(String code, String message) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("data_sent", json_to_be_sent_toPG.toString());
        resultIntent.putExtra("resp_code", code);
        resultIntent.putExtra("resp_msg", message);
        /*if(broadcastReceiverStarted){
            unregisterReceiver(otpReader);
        }*/
        setResult(Activity.RESULT_CANCELED, resultIntent);
        finish();
    }
    public class closeBtn implements Button.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("data_sent", json_to_be_sent_toPG.toString());
            resultIntent.putExtra("resp_code", "8000");
            resultIntent.putExtra("resp_msg", "customer cancelled the payment");
            unregisterObjs();
            setResult(Activity.RESULT_CANCELED, resultIntent);
            finish();
        }
    }
    public void unregisterObjs(){
        try {
            if (broadcastReceiverStarted) {
                unregisterReceiver(otpReader);
                broadcastReceiverStarted = false;
            }
            if (webViewStarted) {
                fPWV1.destroy();
                fPWV1 = null;
                webViewStarted = false;
            }
        }catch (Exception e){
            e.printStackTrace();
            Log.e("fonepaisaPG","Failed to destroy object");
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Toast toast = Toast.makeText(getApplicationContext(),"Please click the close button on the top right corner to cancel the payment.", Toast.LENGTH_SHORT);
            toast.show();
            return true ;
        }
        return super.onKeyDown(keyCode, event);
    }

    public class WebAppInterface {
        Context mContext;

        WebAppInterface(Context c) {
            mContext = c;
        }

        @JavascriptInterface
        public void showToast(String toast) {
            Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
        }

        @JavascriptInterface
        public void SuccessPayment(String json_data) {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("data_sent", json_to_be_sent_toPG.toString());
            resultIntent.putExtra("data_recieved", json_data);
            try {
                JSONObject json = new JSONObject(json_data);
                if (json.get("status").equals("S")) {
                    resultIntent.putExtra("resp_code", "0000");
                    resultIntent.putExtra("resp_msg", "Payment Successful");
                } else {
                    resultIntent.putExtra("resp_code", json.getString("error"));
                    resultIntent.putExtra("resp_msg", json.getString("error_msg"));
                }
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
    @Override
    protected void onDestroy() {
        unregisterObjs();
        super.onDestroy();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    registerReceiver(
                            otpReader,
                            new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
                    broadcastReceiverStarted = true;
                } else {

                    Toast toast = Toast.makeText(getApplicationContext(), "No permission given to Read SMS", Toast.LENGTH_LONG);
                    toast.show();
                }
                return;
            }
        }
    }
    public class OTPReader extends BroadcastReceiver {
        final SmsManager sms = SmsManager.getDefault();
        private Bundle bundle;
        private SmsMessage currentSMS;
        private String message;
        String otpStr1 = "\\bOTP\\b";
        String otpStr2 = "\\sOne\\sTime\\sPassword";
       // String otpStr3 = "\\sone\\stime\\spassword";
        String otpDigits = "\\b\\d{6}\\b";
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
                    bundle = intent.getExtras();
                    if (bundle != null) {
                        Object[] pdu_Objects = (Object[]) bundle.get("pdus");
                        if (pdu_Objects != null) {

                            for (Object aObject : pdu_Objects) {
                                currentSMS = getIncomingMessage(aObject, bundle);
                                String senderNo = currentSMS.getDisplayOriginatingAddress();
                                message = currentSMS.getDisplayMessageBody();
                                //Toast.makeText(context, "senderNum: " + senderNo + " :\n message: " + message, Toast.LENGTH_SHORT).show();
                                Log.e("OTPReader.class","senderNum: " + senderNo + " :\n message: " + message);
                                Pattern otpStr1Pattern = Pattern.compile(otpStr1,Pattern.CASE_INSENSITIVE);
                                Matcher otpStr1Matcher = otpStr1Pattern.matcher(message);
                                Pattern otpStr2Pattern = Pattern.compile(otpStr2,Pattern.CASE_INSENSITIVE);
                                Matcher otpStr2Matcher = otpStr2Pattern.matcher(message);
                                if(otpStr1Matcher.find() || otpStr2Matcher.find()){
                                    Pattern otpDigitsPattern = Pattern.compile(otpDigits,Pattern.CASE_INSENSITIVE);
                                    Matcher otpDigitsMatcher = otpDigitsPattern.matcher(message);
                                    if(otpDigitsMatcher.find()){
                                        int startChar = otpDigitsMatcher.start();
                                        int endChar   = otpDigitsMatcher.end();
                                        Log.e("OTPReader.class","start char "+startChar+" end char "+endChar);
                                        //Toast.makeText(context,"start char "+startChar+" end char "+endChar,Toast.LENGTH_SHORT).show();
                                        String readOTP = message.substring(startChar,endChar);
                                        Log.e("OTPReader.class","read OTP "+readOTP);
                                        Toast.makeText(context,"Your  OTP is "+readOTP,Toast.LENGTH_LONG).show();
                                        final String webUrlscript  = "javascript:(function test(){ " +
                                                    "var inpBoxes =  document.querySelectorAll('input[type$=\"text\"],input[type$=\"password\"],input[type$=\"tel\"],input[type$=\"number\"]');" +
                                                    "var inpBoxesLength = inpBoxes.length;" +
                                                    "console.log(inpBoxesLength); " +
                                                    "for(var i = 0;i<inpBoxesLength;i++){" +
                                                        "if(inpBoxes[i].value == null || inpBoxes[i].value == undefined ||inpBoxes[i].value == '' ){" +
                                                            "inpBoxes[i].value = \"" + readOTP + "\";" +
                                                        "}" +
                                                    "}" +
                                                //"else {console.log(document.getElementsByTagName('input')[2].value);document.getElementsByTagName('input')[2].value = \"" + readOTP + "\";}" + //Test added remove later
                                                "})();";
                                        fPWV1.loadUrl(webUrlscript);
                                    }else{
                                        Log.e("OTPReader.class","dint find  6 digit OTP");
                                    }

                                }else{
                                    Log.e("OTPReader.class","dint find OTP char");
                                }
                            }
                            this.abortBroadcast();
                        }
                    }
                } // bundle null
            } catch (Exception e) {
                Log.e("SmsReceiver", "Exception smsReceiver" +e);
            }
        }
        private SmsMessage getIncomingMessage(Object aObject, Bundle bundle) {
            SmsMessage currentSMS;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                String format = bundle.getString("format");
                currentSMS = SmsMessage.createFromPdu((byte[]) aObject, format);
            } else {
                currentSMS = SmsMessage.createFromPdu((byte[]) aObject);
            }
            return currentSMS;
        }
    }
}
