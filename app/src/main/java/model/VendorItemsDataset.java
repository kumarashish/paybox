package model;

import org.json.JSONObject;

/**
 * Created by Honey Singh on 4/10/2017.
 */

public class VendorItemsDataset {
    String brandId,itemDetails,itemType,itemCategory,id,name,imagePath,suppliedBy,measurement;
    String quantity,numberOfUnit,deliveryDuration;
    Boolean isItemChecked=false;
    String supplierId="";
int price;
    String startDate;
    String endDate;
    String orderId;
    boolean isOrderCancelled=false;
    boolean isRated=false;
    String message;
    public VendorItemsDataset(JSONObject job) {
        try {
            this.brandId = job.getString("brandId");
            this.itemDetails = job.getString("itemDetail");
            this.itemType = job.getString("itemType");
            this.itemCategory = job.getString("itemCategory");
            this.id = job.getString("Id");
            this.name = job.getString("Name");
            this.imagePath = job.getString("ImagePath");
            this.suppliedBy=job.getString("itemSuppliedBy");
            JSONObject priceObject=job.getJSONObject("itemPriceDetail");
            this.price=priceObject.getInt("price");
            this.measurement=priceObject.getString("measurment");
        } catch (Exception ex) {
            ex.fillInStackTrace();
        }
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public boolean isOrderCancelled() {
        return isOrderCancelled;
    }

    public boolean isRated() {
        return isRated;
    }

    public void setOrderCancelled(boolean orderCancelled) {
        isOrderCancelled = orderCancelled;
    }

    public void setRated(boolean rated) {
        isRated = rated;
    }

    public VendorItemsDataset(JSONObject jobb, int val) {
        try {

            this.numberOfUnit=jobb.getString("numberUnit");
            this.deliveryDuration=jobb.getString("deliveryDuration");
            JSONObject job=jobb.getJSONObject("itemDetail");
            this.brandId = job.getString("brandId");
            this.itemDetails = job.getString("itemDetail");
            this.itemType = job.getString("itemType");
            this.itemCategory = job.getString("itemCategory");
            this.suppliedBy=job.isNull("itemSuppliedBy")?"":job.getString("itemSuppliedBy");
            this.id = job.getString("Id");
            this.name = job.getString("Name");
            this.imagePath = job.getString("ImagePath");
            this.suppliedBy=job.getString("itemSuppliedBy");

            JSONObject itemPriceDetails=job.getJSONObject("itemPriceDetail");
            this.price=itemPriceDetails.isNull("price")?0:itemPriceDetails.getInt("price");
            this.measurement=itemPriceDetails.isNull("measurment")?"":itemPriceDetails.getString("measurment");
            this.quantity=itemPriceDetails.isNull("quantity")?"":itemPriceDetails.getString("quantity");
            JSONObject jsonObject=job.getJSONObject("supplierDetail");
            supplierId=jsonObject.isNull("Id")?"":jsonObject.getString("Id");
        } catch (Exception ex) {
            ex.fillInStackTrace();
        }
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setStartDate(String val)
{
    this.startDate=val;
}
public void setEndDate(String val)
{
    this.endDate=val;
}
    public String getEndDate() {
        return endDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public String getDeliveryDuration() {
        return deliveryDuration;
    }

    public String getNumberOfUnit() {
        return numberOfUnit;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getBrandId() {
        return brandId;
    }

    public String getId() {
        return id;
    }

    public String getItemDetails() {
        return itemDetails;
    }

    public String getItemType() {
        return itemType;
    }

    public String getItemCategory() {
        return itemCategory;
    }

    public String getName() {
        return name;
    }

    public String getImagePath() {
        return imagePath;
    }

    public int getPrice() {
        return price;
    }

    public String getMeasurement() {
        return measurement;
    }

    public String getSuppliedBy() {
        return suppliedBy;
    }
    public boolean isItemChecked()
    {
        return isItemChecked;
    }

    public void setItemChecked(Boolean itemChecked) {
        isItemChecked = itemChecked;
    }
}
