package model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Honey Singh on 7/16/2017.
 */

public class Offers {
    String offerId,offerCode,offerStartDate,offerEndDate,offerPercentage,maximumOfferAmount,numberOfUses,minOrderAmount;
Boolean isActive;
    ArrayList<FreeItemsDataset> freeItems=new ArrayList<>();
    public Offers(JSONObject job) {
        try {
            this.offerCode = job.getString("OfferCode");
            this.offerId = job.getString("OfferId");
            this.offerStartDate = job.getString("OfferStartDate");
            this.offerEndDate = job.getString("OfferEndDate");
            this.offerPercentage = job.getString("OfferPercentage");
            this.maximumOfferAmount = job.getString("MaximumOfferAmount");
            this.numberOfUses = job.getString("NumberOfUses");
            this.isActive=job.getBoolean("IsActive");
            this.minOrderAmount=job.getString("MinOrderAmount");
            if(job.getJSONArray("OfferItems")!=null)
            {
                JSONArray freeItemsJSONArray=job.getJSONArray("OfferItems");
                for(int i=0;i<freeItemsJSONArray.length();i++)
                {
                    JSONObject items=freeItemsJSONArray.getJSONObject(i);
                    freeItems.add(new FreeItemsDataset(items));
                }
            }
        } catch (Exception ex) {
            ex.fillInStackTrace();
        }
    }

    public ArrayList<FreeItemsDataset> getFreeItems() {
        return freeItems;
    }

    public String getMinOrderAmount() {
        return minOrderAmount;
    }

    public Boolean getActive() {
        return isActive;
    }

    public String getMaximumOfferAmount() {
        return maximumOfferAmount;
    }

    public String getNumberOfUses() {
        return numberOfUses;
    }

    public String getOfferCode() {
        return offerCode;
    }

    public String getOfferEndDate() {
        return offerEndDate;
    }

    public String getOfferId() {
        return offerId;
    }

    public String getOfferPercentage() {
        return offerPercentage;
    }

    public String getOfferStartDate() {
        return offerStartDate;
    }
}
