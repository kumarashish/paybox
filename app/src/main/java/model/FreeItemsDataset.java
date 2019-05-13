package model;

import org.json.JSONObject;

/**
 * Created by Honey Singh on 12/8/2017.
 */

public class FreeItemsDataset {
 String Price;
    String Detail;
    String Id;
    String Name;
    String ImagePath;
    boolean isItemChecked=false;
    public FreeItemsDataset(JSONObject freeItems)
    {
        try{
            this.Price=freeItems.getString("Price");
            this.Detail=freeItems.getString("Detail");
            this.Id=freeItems.getString("Id");
            this.Name=freeItems.getString("Name");
            this.ImagePath=freeItems.getString("ImagePath");
        }catch (Exception ex)
        {
            ex.fillInStackTrace();
        }
    }

    public String getDetail() {
        return Detail;
    }

    public String getId() {
        return Id;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public String getName() {
        return Name;
    }

    public String getPrice() {
        return Price;
    }
    public void setisItemChecked(boolean value) {
      this.isItemChecked=value;
    }

    public boolean isItemChecked() {
        return isItemChecked;
    }
}
