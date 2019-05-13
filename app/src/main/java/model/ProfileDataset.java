package model;

import org.json.JSONObject;

/**
 * Created by Honey Singh on 5/6/2017.
 */

public class ProfileDataset {
    String mobileNumber=null,address,city,zipCode,name,imagePath,businessName,emailId;
    public ProfileDataset(JSONObject job)
    {
try{
    this.emailId=job.isNull("Email")?"":job.getString("Email");
    this.businessName=job.isNull("BusinessName")?"":job.getString("BusinessName");
    this.mobileNumber=job.isNull("MobileNumber")?"":job.getString("MobileNumber");
    this.address=job.isNull("Address")?"":job.getString("Address");
    this.city=job.isNull("City")?"":job.getString("City");
    this.zipCode=job.isNull("ZipCode")?"":job.getString("ZipCode");
    this.name=job.isNull("Name")?"":job.getString("Name");
    this.imagePath=job.isNull("ImagePath")?"":job.getString("ImagePath");



}catch (Exception ex)
{
    ex.fillInStackTrace();
}
    }

    public String getEmailId() {
        return emailId;
    }

    public String getBusinessName() {
        return businessName;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getName() {
        return name;
    }

    public String getZipCode() {
        return zipCode;
    }
    /* "MobileNumber": "3232323",
             "Address": "2323",
             "City": "HYD",
             "ZipCode": "5434333",
             "Email": "nvatsya@gmail.com",
             "BusinessName": null,
             "addressProofs": [
    {
        "Id": "00000000-0000-0000-0000-000000000000",
            "Name": "Voter Id Card",
            "ImagePath": "bazaar.gjitsolution.in/UploadImages/user/addressproof"
    }
  ],
          "serviceCity": [
    {
        "CityId": 0,
            "CityName": "",
            "StateName": null,
            "ZipCode": null
    }
  ],
          "serviceAreas": null,
          "items": [],
          "Id": "41e9aedf-7f53-47fe-8c03-9b912ae0cede",
          "Name": "",
          "ImagePath": null*/
}
