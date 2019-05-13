package model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Honey Singh on 4/7/2017.
 */

public class ServicesDataset {
   String serviceId,serviceName,serviceType,serviceImage,serviceMessage;

    public ServicesDataset(JSONObject job) {

        try {
            serviceId = job.getString("ServiceId");
            serviceName = job.getString("ServiceName");
            serviceType = job.getString("ServiceType");
            serviceImage= job.getString("ImagePath");
            serviceMessage=job.getString("ServiceMessage");


        } catch (JSONException ex) {
            ex.fillInStackTrace();
        }
    }
    public ServicesDataset(String serviceName) {


            serviceId = "1234567890";
            this.serviceName = serviceName;
            serviceType = "Delivery";
            serviceImage= "dummy";
        serviceMessage="N/A";


    }

    public String getServiceMessage() {
        return serviceMessage;
    }

    public String getServiceName() {
        return serviceName.toUpperCase();
    }

    public String getServiceType() {
        return serviceType;
    }

    public String getServiceId() {
        return serviceId;
    }
    public String getServiceImage(){return serviceImage;}
}

