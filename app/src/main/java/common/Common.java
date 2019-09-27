package common;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.atom.mobilepaymentsdk.PayActivity;
import com.mypaybox.Rent;

/**
 * Created by Honey Singh on 4/5/2017.
 */

public class Common {


    public static String baseUrl="http://api.mypaybox.in/";
    public static String localityId = "";
    public static String locality = "";
    public static String city = "";
    public static String serviceId = "";
    public static String registartionUrl = baseUrl+"api/Account/register";
    public static String loginUrl = baseUrl+"Token";
    public static String acessToken = "";
    public static String userId = "";
    public static String emailId = "";
    public static String profilePic = "";
    public static String getAllCategoriesUrl = baseUrl+"api/bazaar/GetAllData";
    public static String getLocalityUrl =baseUrl+"api/City/GetLocality?cityId=";
    public static String getServiceUrl = baseUrl+"api/Bazaar/RetrieveLocalityServices?localityId=";
    public static String getVendorList = baseUrl+"api/Bazaar/GetVenorList?brandId=";
    public static String getVendorDetailsUrl = baseUrl+"api/Bazaar/GetAllItemListForVendor?vendorId=";
    public static String getVendorReport=baseUrl+"api/Order/GetVendorOrder?vendorId=";
    public static String serviceImageUrl = "";
    public static String saveAddress=baseUrl+"api/Bazaar/SaveUserAddress";
    public static String getAllAddress=baseUrl+"api/Bazaar/GetUserAddress?";
    public static String updateAddress=baseUrl+"api/Bazaar/UpdateUserAddress";
    public static String deleteAddress=baseUrl+"api/Bazaar/DeleteUserAddress";
    public static String changePassword=baseUrl+"api/Account/ChangePassword";
    public static String updateUserProfileUrl=baseUrl+"api/Bazaar/UpdateProfile";
    public static String getUserProfileUrl=baseUrl+"api/Bazaar/GetProfile?userId=";
    public static String saveRating=baseUrl+"api/Bazaar/SaveReviewRating";
    public static String saveOrder=baseUrl+"api/Order/SaveOrder";
    public static String saveSubscription=baseUrl+"api/Order/SaveSubscription";
    public static String getOrderHistory=baseUrl+"api/Order/GetOrder?userName=";
    public static String getOffers=baseUrl+"api/Order/GetOffers?userName=";
    public static String updateUserProfileImage=baseUrl+"api/Upload/UploadImage?userId=";
    public static String forgetPasswordUrl=baseUrl+"api/Account/ForgotPassword?email=";
    public static String vendorCapityUpdateUrl=baseUrl+"api/Bazaar/UpdateItemCapacity";
    public static String redeemCouponUrl=baseUrl+"api/Bazaar/RedeemCoupon?couponCode";
    public static String cancelOrder=baseUrl+"api/Order/UpdateOrder?";

    public static String getPropertyUrl=baseUrl+"api/Rent/GetPropertyListForRent?";
    public static String getSavePropertyWithBankDetailsUrl=baseUrl+"api/Rent/SavePropertyForRent";
    public static String getSaveRentUrl=baseUrl+"api/Rent/SaveRent";
    public static String getPaymentHashUrl="https://mypaybox.in/Payment/GetPaymentHashData?";

    public static String getTransactionHistoryUrl = baseUrl+"api/Rent/GetTransactionPropertyList?userId=";
    public static String UserName="";
    public static String AddressId="";
    public static int loggedInUserType;
    public static boolean isAppartmentUser;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static Uri imageUri = null;
    public static String sdCardPath;
    public static String folderName;
    public static String tempPath;
    public static String paymentGatewayUrlLive="https://api.instamojo.com/oauth2/token/";
    public static String liveClientId="sR9xqV6RiwAb86XJfut8H2GFAAkMDoAUwvwybV3W";
    public static String liveClientSecret="brFgaYzsY5KcI4C8wbaxnzIAog5HokLN0HBxqcuzP3QwQkaNJPuzOu4gzlCCwVjJFAje0s6M39mbOINcuZ2Bn8gnsCNGV8n2GcXukEgTlOAzyGW3chGsbl6rEu0k5hAM";

    public static String paymentGatewayUrlSandox="https://test.instamojo.com/oauth2/token/";
    public static String sandBoxClientId="lT3TLB48RanWrdfoOr8n4seECMX97OXbf38FxuMF";
    public static String sandboxClientSecret="aIR3E60tOQMko01jp0mdEORPles98C5hTD8e3d2lARZUYut3XxI1HKwpLrNTdNtwiBXQ7HTenEvY4ve5WjX32q8P3JR6psn6NXuC9i29nnJhNgoszvIGcyZBIhd0fQF7";
   final public static String creditCard="CC";
    final public static String debitCard="DC";
    final  public static String wallet="OW";
    final  public static String net_Banking="NB";
    final public static String upi="UP";
    final public static String all="All";
   public static String getUpdateImageUrl(String userId, String type) {
       return Common.updateUserProfileImage + "" + userId + "&type=" + type;
   }
    public static String getRedeemCouponUrl(String couponCode, int totalAmount)
    {
        return  baseUrl+"api/Bazaar/RedeemCoupon?couponCode="+couponCode+"&orderTotalAmount="+totalAmount+"";
    }
    public static String getProductUrl(String serviceId, String locality)
    {
      return  baseUrl+"api/Bazaar/GetItemList?id="+serviceId+"&localityId="+locality+"";
    }
    public static String getDeliverableItemsUrl(String locality, String vendorId)
    {
        return  baseUrl+"api/Bazaar/GeAlltItemList?localityId="+locality+"&&vendorId="+vendorId+"";
    }
    public static String getCancelOrderUrl(String orderId, String startDate, String restartDate, String orderStopDate)
    {
        return cancelOrder+"orderId="+orderId+"&orderLastStartDate="+startDate+"&orderReStartDate="+restartDate+"&orderStopDate="+orderStopDate+"";
    }
    public static String getAppartmentDetails(int month,String year,String userName)
    {

        return  baseUrl+"api/Apartment/GetUserApartment?city=test&locality=test&userEmail="+  userName+"&monthId="+month+"&selectedYear="+year;
      // return  baseUrl+"api/Apartment/GetUserApartment?city=test&locality=test&userEmail=Nishantk@kstrata.com&monthId="+month+"&selectedYear="+year;
    }

    public static String getPropertyUrl(String emaiid) {

        //    return baseUrl + "api/Rent/GetUserApartmentEmailBased?userEmail="+Common.emailId;
            return baseUrl + "api/Rent/GetUserApartmentEmailBased?userEmail="+emaiid;


    }
    public static String getBankDetailsForSelectedProperty(String id)
    {
        return baseUrl+"api/Rent/GetPropertyBankListForRent?propertyId="+id;


    } public static String getTransactionHistoryUrl(String id)
    {
        return getTransactionHistoryUrl+""+id;


    }
    public static String getPaymentHash(String amount,String invoiceId)
    {
      return getPaymentHashUrl+"invoiceAmt="+amount+"&invoiceId="+invoiceId+"";
    }
    public static void madePayment(Activity act,String amount,String disclaimer,String cardType)
    {
        Intent newPayIntent = new Intent(act,	PayActivity.class);
        newPayIntent.putExtra("merchantId", "197");
        newPayIntent.putExtra("txnscamt", "0"); //Fixed. Must be 0
        newPayIntent.putExtra("loginid", "197");
        newPayIntent.putExtra("password", "Test@123");
        newPayIntent.putExtra("prodid", "NSE");
        newPayIntent.putExtra("txncurr", "INR"); //Fixed. Must be ?INR?
        newPayIntent.putExtra("clientcode", "001");
        newPayIntent.putExtra("custacc", "100000036600");
        newPayIntent.putExtra("amt", amount);//Should be 3 decimal number i.e 51.000
        newPayIntent.putExtra("txnid", "013");
        newPayIntent.putExtra("date", "25/08/2015 18:31:00");//Should be in same format
      if(disclaimer.length()>0) {
          newPayIntent.putExtra("discriminator", disclaimer);
      }// NB or IMPS or All ONLY (value should be same as commented)
        if(cardType.length()>0) {
            newPayIntent.putExtra("cardtype", "DC");
        }// CC or DC ONLY (value should be same as commented)
       // newPayIntent.putExtra("cardAssociate", "VISA");// VISA or MASTER or MAESTRO ONLY (value should be same as commented)
        newPayIntent.putExtra("surcharge", "NO");
        newPayIntent.putExtra("signature_request", "KEY123657234");
        newPayIntent.putExtra("signature_response", "KEYRESP123657234");
        //use below Production url only with Production "Library-MobilePaymentSDK", Located inside PROD folder
        //newPayIntent.putExtra("ru","https://payment.atomtech.in/mobilesdk/param"); //ru FOR Production
        //use below UAT url only with UAT "Library-MobilePaymentSDK", Located inside UAT folder
        newPayIntent.putExtra("ru", "https://paynetzuat.atomtech.in/mobilesdk/param"); // FOR UAT (Testing)
        //Optinal Parameters
        newPayIntent.putExtra("customerName", "JKL PQR"); //Only for Name
        newPayIntent.putExtra("customerEmailID", "jkl.pqr@atomtech.in");//Only for Email ID
        newPayIntent.putExtra("customerMobileNo", "9876543210");//Only for Mobile Number
        newPayIntent.putExtra("billingAddress", "Mumbai");//Only for Address
        newPayIntent.putExtra("optionalUdf9", "OPTIONAL DATA 1");// Can pass any data
        act.startActivityForResult(newPayIntent, 3);
    }
}
