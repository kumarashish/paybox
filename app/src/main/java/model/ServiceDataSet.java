package model;

import org.json.JSONObject;

/**
 * Created by Ashish.Kumar on 21-08-2017.
 */

public class ServiceDataSet {
    String ServiceId;
    String ServiceDuration;
    String ServiceName;
    String ServiceCost;
    public ServiceDataSet(JSONObject jsonObject)
    {try{
        this.ServiceId=jsonObject.getString("ServiceId");
        this.ServiceDuration=jsonObject.getString("ServiceDuration");
        this.ServiceName=jsonObject.getString("ServiceName");
        this.ServiceCost=jsonObject.getString("ServiceCost");
    }catch (Exception ex)
    {
        ex.fillInStackTrace();
    }}

    public String getServiceCost() {
        return ServiceCost;
    }

    public String getServiceDuration() {
        return ServiceDuration;
    }

    public String getServiceId() {
        return ServiceId;
    }

    public String getServiceName() {
        return ServiceName;
    }
}
