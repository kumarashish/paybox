package model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Honey Singh on 4/10/2017.
 */

public class ProductDataset {
    String name, imagePath, id;
    ArrayList<VendorItemsDataset> items = new ArrayList<VendorItemsDataset>();

    public ProductDataset(JSONObject job) {
        try {
            items.clear();
            this.name = job.getString("Name");
            this.imagePath = job.getString("ImagePath");
            this.id = job.getString("Id");
            JSONArray jsonArray = job.getJSONArray("items");
            for (int i = 0; i < jsonArray.length(); i++) {
                VendorItemsDataset vds = new VendorItemsDataset(jsonArray.getJSONObject(i));
                items.add(vds);
            }
        } catch (Exception ex) {
        }
    }

    public String getImagePath() {
        return imagePath;
    }

    public ArrayList<VendorItemsDataset> getItems() {
        return items;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }
}
