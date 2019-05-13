package model;

import org.json.JSONObject;

/**
 * Created by Honey Singh on 4/7/2017.
 */

public class LocalityDataset {
    String localityId,localityName,localityLat,localityLon;
    int cityId;

    public LocalityDataset(JSONObject job) {
        try {
            localityId = job.getString("LocalityId");
            cityId = job.getInt("CityId");
            localityName = job.getString("LocalityName");
            localityLat = job.getString("LocalityLat");
            localityLon = job.getString("LocalityLon");
        } catch (Exception ex) {
            ex.fillInStackTrace();
        }
    }
    public int getCityId(){return cityId;}

    public String getLocalityLat() {
        return localityLat;
    }

    public String getLocalityLon() {
        return localityLon;
    }

    public String getLocalityName() {
        return localityName;
    }

    public String getLocalityId() {
        return localityId;
    }
}
