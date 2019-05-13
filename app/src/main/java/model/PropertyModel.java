package model;

import org.json.JSONObject;

/**
 * Created by Ashish.Kumar on 18-12-2017.
 */

public class PropertyModel {
    String PropertyModelId;
    String PropetyName;
    String OwnerName;
    String OwnerEmail;
    String OwnerMobile;
    String Address;
    String City;
    String Locality;
    String State;
    String ZipCode;
    public  PropertyModel(JSONObject jsonObject)
    {
        try{
            PropertyModelId = jsonObject.isNull("PropertyModelId")?"":jsonObject.getString("PropertyModelId");
            PropetyName =jsonObject.isNull("PropetyName")?"": jsonObject.getString("PropetyName");
            OwnerName = jsonObject.isNull("OwnerName")?"":jsonObject.getString("OwnerName");
            OwnerEmail =jsonObject.isNull("OwnerEmail")?"": jsonObject.getString("OwnerEmail");
            OwnerMobile =jsonObject.isNull("OwnerMobile")?"": jsonObject.getString("OwnerMobile");
            Address =jsonObject.isNull("Address")?"": jsonObject.getString("Address");
            City =jsonObject.isNull("City")?"": jsonObject.getString("City");
            Locality =jsonObject.isNull("Locality")?"": jsonObject.getString("Locality");
            State =jsonObject.isNull("State")?"": jsonObject.getString("State");
            ZipCode =jsonObject.isNull("ZipCode")?"": jsonObject.getString("ZipCode");
        }catch (Exception ex)
        {
            ex.fillInStackTrace();
        }
    }

    public String getAddress() {
        return Address;
    }

    public String getCity() {
        return City;
    }

    public String getLocality() {
        return Locality;
    }

    public String getOwnerEmail() {
        return OwnerEmail;
    }

    public String getOwnerMobile() {
        return OwnerMobile;
    }

    public String getOwnerName() {
        return OwnerName;
    }

    public String getPropertyModelId() {
        return PropertyModelId;
    }

    public String getPropetyName() {
        return PropetyName;
    }

    public String getState() {
        return State;
    }

    public String getZipCode() {
        return ZipCode;
    }

}
