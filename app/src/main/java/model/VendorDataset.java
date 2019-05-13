package model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Honey Singh on 4/10/2017.
 */

public class VendorDataset {
    String address,rating,review,name,imagepath,id;
ArrayList<String> feedback=new ArrayList<>();
    public VendorDataset(JSONObject job) {
        try {
            this.address = job.getString("address1") + "," + job.getString("address2") + "," + job.getString("address3");
            this.rating = job.getString("rating");
            this.review = job.getString("review");
            this.name = job.getString("Name");
            this.imagepath = job.getString("ImagePath");
            this.id=job.getString("Id");
            JSONArray jsonArray=job.getJSONArray("feedback");
            for(int i=0;i<jsonArray.length();i++)
            {
                feedback.add(jsonArray.getString(i));
            }
        } catch (Exception ex) {
            ex.fillInStackTrace();
        }
    }

    public String getAddress() {
        return address;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImagepath() {
        return imagepath;
    }

    public String getRating() {
        return rating;
    }

    public String getReview() {
        return review;
    }
    public ArrayList<String> getFeedBack()
    {
        return feedback;
    }
}
