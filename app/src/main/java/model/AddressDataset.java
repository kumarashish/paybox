package model;

import org.json.JSONObject;

/**
 * Created by Honey Singh on 4/20/2017.
 */

public class AddressDataset {
   String AddressId,UserId,AddressType,Address1,Address2,Address3,City,State,Pincode,CurrentVersion;
    public AddressDataset(JSONObject job) {
        try {
            this.AddressId = job.getString("AddressId");
            this.UserId = job.getString("AddressId");
            this.AddressType = job.getString("AddressType");
            this.Address1 = job.getString("Address1");
            this.Address2 = job.getString("Address2");
            this.Address3 = job.getString("Address3");
            this.City = job.getString("City");
            this.State = job.getString("State");
            this.Pincode = job.getString("Pincode");
            this.CurrentVersion = job.getString("CurrentVersion");
        } catch (Exception ex)
        {
            ex.fillInStackTrace();
        }
    }

    public String getAddress1() {
        return Address1;
    }

    public String getAddress2() {
        return Address2;
    }

    public String getAddress3() {
        return Address3;
    }

    public String getAddressId() {
        return AddressId;
    }

    public String getAddressType() {
        return AddressType;
    }

    public String getCity() {
        return City;
    }

    public String getCurrentVersion() {
        return CurrentVersion;
    }

    public String getPincode() {
        return Pincode;
    }

    public String getState() {
        return State;
    }

    public String getUserId() {
        return UserId;
    }
}
