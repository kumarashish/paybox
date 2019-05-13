package model;

import org.json.JSONArray;
import org.json.JSONObject;

import common.Utils;

/**
 * Created by Ashish.Kumar on 16-05-2017.
 */

public class VendorReportDataset {
    String imageUrl="";
    String productName="",prductMeasurement="",productPrice="",productQuantity="",userName="",deliveryAddress="",deliveryDuration="",deliveryTimming="";

    public  VendorReportDataset(JSONObject job){try{

        this.deliveryDuration= Utils.getFormatedDate(job.getString("OrderStartDate"))+ " to "+ Utils.getFormatedDate(job.getString("OrderEndTime"));
       this.deliveryTimming=job.getString("orderDeliveryTime");
       this.userName=job.getString("userName");
        JSONObject adressObject=job.getJSONObject("deliveryAddress");
        this.deliveryAddress=adressObject.getString("Address1")+","+adressObject.getString("Address2")+",\n"+adressObject.getString("City")+","+adressObject.getString("State")+","+adressObject.getString("Pincode");
        JSONObject orderDetail=job.getJSONObject("orderDetail");
        JSONArray items=orderDetail.getJSONArray("items");
        JSONObject itemDetails=items.getJSONObject(0);
        this.productQuantity=itemDetails.getString("numberUnit");
        JSONObject item=itemDetails.getJSONObject("itemDetail");
        this.imageUrl=item.getString("ImagePath");
        this.productName=item.getString("Name")+"("+item.getString("itemType")+")";

        JSONObject itemPrice= item.getJSONObject("itemPriceDetail");
        this.productPrice="Rs. "+itemPrice.getString("price");
        this.prductMeasurement=itemPrice.getString("measurment");

    }catch (Exception ex){ex.fillInStackTrace();}}

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public String getDeliveryDuration() {
        return deliveryDuration;
    }

    public String getDeliveryTimming() {
        return deliveryTimming;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getPrductMeasurement() {
        return prductMeasurement;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public String getProductQuantity() {
        return productQuantity;
    }

    public String getUserName() {
        return userName;
    }
}
