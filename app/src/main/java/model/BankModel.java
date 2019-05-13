package model;

import org.json.JSONObject;

/**
 * Created by Ashish.Kumar on 18-12-2017.
 */

public class BankModel {
  String BankModelId;
    String BankName;
    String BankAddress;
    String BankIfsc;
    String AccNumber;

    public BankModel(JSONObject jsonObject) {
        try {
            BankModelId = jsonObject.getString("BankModelId");
            BankName = jsonObject.getString("BankName");
            BankAddress = jsonObject.getString("BankAddress");
            BankIfsc = jsonObject.getString("BankIfsc");
            AccNumber = jsonObject.getString("AccNumber");
        } catch (Exception ex) {
            ex.fillInStackTrace();
        }
    }

    public String getAccNumber() {
        return AccNumber;
    }

    public String getBankAddress() {
        return BankAddress;
    }

    public String getBankIfsc() {
        return BankIfsc;
    }

    public String getBankModelId() {
        return BankModelId;
    }

    public String getBankName() {
        return BankName;
    }
}
