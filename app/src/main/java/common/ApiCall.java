package common;

import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Honey Singh on 4/5/2017.
 */

public class ApiCall {
    public static OkHttpClient client;
    public static MediaType mediaType = MediaType.parse("application/json");

    public static String
        postData(String url, JSONObject json) {
        client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        RequestBody body = RequestBody.create(mediaType, json.toString());
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("content-type", "application/json")
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200) {
              String message=  response.body().string();
                return "true";
            } else if (response.code() == 400) {
                return getMessage(response.body().string());
            }
            return response.body().string();
        } catch (Exception ex) {
            ex.fillInStackTrace();
            return ex.fillInStackTrace().toString();
        }
    }
    public static String post(String url, JSONObject json) {
        client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        RequestBody body = RequestBody.create(mediaType, json.toString());
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("content-type", "application/json")
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200) {
                return response.body().string();
            } else if (response.code() == 400) {
                return getMessage(response.body().string());
            }
            return response.body().string();
        } catch (Exception ex) {
            ex.fillInStackTrace();
            return ex.fillInStackTrace().toString();
        }
    }

    public static String getAcessToken(String url) {
        client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        RequestBody formBody = null;
        if (url.equalsIgnoreCase(Common.paymentGatewayUrlSandox)) {
            formBody = new FormBody.Builder()
                    .add("client_id", Common.sandBoxClientId)
                    .add("client_secret", Common.sandboxClientSecret)
                    .add("grant_type", "client_credentials").build();
        } else {
            formBody = new FormBody.Builder()
                    .add("client_id", Common.liveClientId)
                    .add("client_secret", Common.liveClientSecret)
                    .add("grant_type", "client_credentials").build();
        }
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200) {
                return response.body().string();
            } else if (response.code() == 400) {
                return getMessage(response.body().string());
            }
            return response.body().string();
        } catch (Exception ex) {
            ex.fillInStackTrace();
            return ex.fillInStackTrace().toString();
        }
    }



    public static String
   redeemCoupon(String url, JSONObject json) {
        client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        RequestBody body = RequestBody.create(mediaType, json.toString());
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("content-type", "application/json")
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200) {
                return response.body().string();
            } else if (response.code() == 400) {
                return getMessage(response.body().string());
            }
            return response.body().string();
        } catch (Exception ex) {
            ex.fillInStackTrace();
            return ex.fillInStackTrace().toString();
        }
    }

    public static String deleteData(String url, JSONObject json) {
        client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        RequestBody body = RequestBody.create(mediaType, json.toString());
        Request request = new Request.Builder()
                .url(url)
                .delete(body)
                .addHeader("content-type", "application/json")
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200) {
                return "true";
            } else if (response.code() == 400) {
                return getMessage(response.body().string());
            }
            return response.body().string();
        } catch (Exception ex) {
            ex.fillInStackTrace();
            return ex.fillInStackTrace().toString();
        }
    }
    public static String getMessage(String json) {
        JSONObject job=null;
        try {
            job = new JSONObject(json);
            JSONObject messageObject = job.getJSONObject("ModelState");
            return messageObject.toString();
        } catch (Exception ex) {
            try {
                return job.getString("Message");
            }catch (Exception exx) {
                return exx.fillInStackTrace().toString();
            }
        }

    }

    public static String loginData(String[] val) {
        client = new OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();
        FormBody.Builder formBuilder = new FormBody.Builder()
                .add("username", val[0])
                .add("password", val[1])
                .add("grant_type", "password");

// dynamically add more parameter like this:

        RequestBody formBody = formBuilder.build();
        Request request = new Request.Builder()
                .url(Common.loginUrl)
                .post(formBody)
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200) {
                return response.body().string();
            } else if (response.code() == 400) {
                return response.body().string();
            }
            return response.body().string();
        } catch (Exception ex) {
            ex.fillInStackTrace();
            return ex.fillInStackTrace().toString();
        }


    }
    public static String cancelOrderData(String[] val) {
        client = new OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();

        FormBody.Builder formBuilder = new FormBody.Builder()
                .add("orderId", val[0])
                .add("orderLastStartDate", val[1])
                .add("orderReStartDate", val[2])
                .add("orderStopDate", val[3]);

// dynamically add more parameter like this:

        RequestBody formBody = formBuilder.build();
        Request request = new Request.Builder()
                .url(Common.cancelOrder)
                .post(formBody)
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200) {
                return response.body().string();
            } else if (response.code() == 400) {
                return response.body().string();
            }
            return response.body().string();
        } catch (Exception ex) {
            ex.fillInStackTrace();
            return ex.fillInStackTrace().toString();
        }


    }

    public static String getData(String url) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("content-type", "application/json")
                .addHeader("authorization", Common.acessToken + "")

                .build();

        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (Exception ex) {
            ex.fillInStackTrace();
            return ex.fillInStackTrace().toString();
        }
    }




}
