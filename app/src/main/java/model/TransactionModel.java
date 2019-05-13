package model;

import org.json.JSONObject;

/**
 * Created by Ashish.Kumar on 26-12-2017.
 */

public class TransactionModel {
    String transactionModelId;
    String transactionId;
    String comment;
    String transactionDate;
    String transactionAmount;

    public TransactionModel(JSONObject jsonObject) {
        try {
            transactionModelId = jsonObject.getString("TransactionModelId");
            transactionId = jsonObject.getString("TransactionId");
            comment = jsonObject.getString("Comment");
            transactionDate = jsonObject.getString("TransactionDate");
            transactionAmount = jsonObject.getString("TransactionAmount");
        } catch (Exception ex) {

        }
    }

    public String getComment() {
        return comment;
    }

    public String getTransactionAmount() {
        return transactionAmount;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getTransactionModelId() {
        return transactionModelId;
    }
}
