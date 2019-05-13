package model;

import org.json.JSONObject;

/**
 * Created by Ashish.Kumar on 26-12-2017.
 */

public class TransactionHistoryModel {
    PropertyModel property = null;
    BankModel model = null;
    TransactionModel transactionModel = null;

    public TransactionHistoryModel(JSONObject job) {
        try {
            JSONObject propertyBank = job.getJSONObject("PropertyBank");
            this.property = new PropertyModel(job.getJSONObject("Property"));
            this.model = new BankModel(propertyBank.getJSONObject("BankModel"));
            this.transactionModel = new TransactionModel(propertyBank.getJSONObject("TransactionModel"));
        } catch (Exception ex) {
            ex.fillInStackTrace();
        }
    }

    public BankModel getModel() {
        return model;
    }

    public PropertyModel getProperty() {
        return property;
    }

    public TransactionModel getTransactionModel() {
        return transactionModel;
    }
}
