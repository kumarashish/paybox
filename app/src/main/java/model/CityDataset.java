package model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Honey Singh on 4/7/2017.
 */

public class CityDataset {
    int cityId;
    String cityName, state, zipCode;

    public CityDataset(JSONObject job) {
        try {
            cityId = job.getInt("CityId");
            cityName = job.getString("CityName");
            state = job.getString("StateName");
            zipCode = job.getString("ZipCode");
        } catch (JSONException ex) {
            ex.fillInStackTrace();
        }
    }

    public int getId() {
        return cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public String getState() {
        return state;
    }

    public String getZipCode() {
        return zipCode;
    }
}
