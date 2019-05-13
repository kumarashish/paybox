package model;

import org.json.JSONObject;

/**
 * Created by Honey Singh on 5/27/2017.
 */

public class DeliverableDataset {
    String imagePath,itemId,brandId,itemName,itemType,itemCategory;
    int quantity=0;
    Boolean enabled=false;
  public DeliverableDataset(JSONObject job)
  {
try{
this.imagePath=job.getString("ImagePath");
    this.itemId=job.getString("Id");
    this.brandId=job.getString("brandId");
    this.itemName=job.getString("Name");
    this.itemType=job.getString("itemType");
    this.itemCategory=job.getString("itemCategory");
    this.quantity=job.getInt("vendorCapacity");
}catch (Exception ex){
    ex.fillInStackTrace();
}
  }

    public int getQuantity() {
        return quantity;
    }

    public String getBrandId() {
        return brandId;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getItemId() {
        return itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemType() {
        return itemType;
    }

    public String getTemCategory() {
        return itemCategory;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Boolean getEnabled() {
        return enabled;
    }
}

