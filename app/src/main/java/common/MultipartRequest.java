package common;

import android.content.Context;
import android.util.Base64;

import java.io.File;
import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Ashish.Kumar on 19-05-2017.
 */

public class MultipartRequest {
    public Context caller;
    public RequestBody body;
    public OkHttpClient client;
    File file=null;
    String encodedImage;
    MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
    public MultipartRequest(String filePath) {
        File file=new File(filePath);
        byte[] byteArray = Utils.getImageByteArray(file);
        encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
        body= new FormBody.Builder().add("imageStr", encodedImage).build();
        this.client = new OkHttpClient();



    }


    public String execute(String url) {
        Request request = null;
        Response response = null;
        int code = 200;
        String strResponse = null;
        try {
            request = new Request.Builder() .url(url).post(body).addHeader("content-type", "application/x-www-form-urlencoded").build();
            response = client.newCall(request).execute();
            if (!response.isSuccessful())
                throw new IOException();

            code = response.networkResponse().code();

            if (response.isSuccessful()) {
                strResponse = response.body().string();
            } else if (code == 404) {
                // ** "Invalid URL or Server not available, please try again" */
                strResponse = "Source not found";
            } else if (code == 408) {
                // * "Connection timeout, please try again", */
                strResponse = "Connection timeout";

            } else if (code == 503) {
                // *
                // "Invalid URL or Server is not responding, please try again",
                // */
                strResponse = "Service Unavailable";
            }
        } catch (Exception e) {
            e.fillInStackTrace();

        } finally {
            body = null;
            request = null;
            response = null;
            if (client != null)
                client = null;
            System.gc();
        }
        return strResponse;
    }
}
