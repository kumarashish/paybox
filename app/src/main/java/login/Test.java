package login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.mypaybox.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Formatter;

import fonepaisa.com.fonepaisapg_sdk.FPConstants;
import fonepaisa.com.fonepaisapg_sdk.fonePaisaPG;

/**
 * Created by ashish.kumar on 05-09-2018.
 */

public class Test extends AppCompatActivity {
    Button PG_VIEW_BTN;
    int FONEPAISAPG_RET_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        PG_VIEW_BTN = (Button) findViewById(R.id.test_pg);
        PG_VIEW_BTN.setOnClickListener(new Test_PG());
    }


    public class Test_PG implements Button.OnClickListener {
        @Override
        public void onClick(View v) {
            //TODO6BBADA098265FF559B728CD342CC076DF1A606EC30A777799F7705542FA62F6487CCD8061894F111D848FC70352885359DF419EB69BA0B6F5A611144CA43FE8BAE41968953DCB38CE52DC0B0478D748849E78884215AA5580AD741B0281F107DB411E7F92E768121A34EB299057177E9B477C72B62DDF399A8638A27053C2C06983D477E6D772364E27B5BF7AEBB324E3C06D477862FB6E87999E5E44EF97E9784FAF571F431BECD747226298F9A07ED4403F4C1E6CFAC0114C8C5F47A420DEBF1566D1D3063DB4EC3718422772DB4E15331033783E6611BC07FBBDCBB00A4C887C565062690FE0CA619ECA7AC68AD4B0DE804814783F2619518388327B86EBE

            Intent intent = new Intent(Test.this, fonePaisaPG.class);
            JSONObject json_to_be_sent = new JSONObject();
            try {
                json_to_be_sent.put("id", "A991");    // Mandatory .. FPTEST is just for testing it has to be changed before going to production
                json_to_be_sent.put("merchant_id", "A991");   // Mandatory .. FPTEST is just for testing it has to be changed before going to production
                json_to_be_sent.put("merchant_display", "PAYBOX");  // Mandatory ..  change it to whatever you want to get it displayed
                json_to_be_sent.put("invoice", "12121"); //mandatory  .. this is the unique reference against which you can enquire and it can be system generated or manually entered
                json_to_be_sent.put("mobile_no", "8121484289");    ///pass the mobile number if you have else send it blank and the customer will be prompted for the mobile no so that confirmation msg can be sent
                json_to_be_sent.put("email", "");          // pass email if an invoice details has to be mailed
                json_to_be_sent.put("invoice_amt", "6.00");    //pass the amount with two decimal rounded off
                json_to_be_sent.put("note", "");         // pass any notes if you need
                json_to_be_sent.put("payment_types", "");    // not mandatory . this is to restrict the payment types
                json_to_be_sent.put("addnl_info", "");          // pass any addnl data which u need to get baack
//                //input for signing  API_KET#id#merchant_id#invoice#amount
//                String signed_ip = API_KEY + "#" + json_to_be_sent.getString("id") + "#" + json_to_be_sent.getString("merchant_id") + "#" + json_to_be_sent.getString("invoice") + "#" + json_to_be_sent.getString("invoice_amt") + "#";
//
//                /* *********************************************************************************************
//                *               TODO                                                                           *
//                *     Just for testing we have signed in the client side .                                     *
//                *    Please do the signing on your server side . and pass the signed message in the json       *
//                *                                                                                              *
//                ************************************************************************************************/
//
//                String signed_msg = getSignedMsg(signed_ip);
                json_to_be_sent.put("sign", "6BBADA098265FF559B728CD342CC076DF1A606EC30A777799F7705542FA62F6487CCD8061894F111D848FC70352885359DF419EB69BA0B6F5A611144CA43FE8BAE41968953DCB38CE52DC0B0478D748849E78884215AA5580AD741B0281F107DB411E7F92E768121A34EB299057177E9B477C72B62DDF399A8638A27053C2C06983D477E6D772364E27B5BF7AEBB324E3C06D477862FB6E87999E5E44EF97E9784FAF571F431BECD747226298F9A07ED4403F4C1E6CFAC0114C8C5F47A420DEBF1566D1D3063DB4EC3718422772DB4E15331033783E6611BC07FBBDCBB00A4C887C565062690FE0CA619ECA7AC68AD4B0DE804814783F2619518388327B86EBE");
                json_to_be_sent.put("Environment", FPConstants.Production_Environment);  //mandatory   //Change it based on the environment you are using
                intent.putExtra("data", json_to_be_sent.toString());
                startActivityForResult(intent, FONEPAISAPG_RET_CODE);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /*************************************************************************************
     *                                   TODO                                             *
     * This is the code you need to modify to handle your success and failure scenarios . *
     **************************************************************************************/
    @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent returned_intent) {
        if (requestCode == FONEPAISAPG_RET_CODE) {

            if (resultCode == Activity.RESULT_OK) {
                System.out.println("returned message" + returned_intent.getStringExtra("resp_msg"));
                System.out.println("returned code" + returned_intent.getStringExtra("resp_code"));
                System.out.println("sent json" + returned_intent.getStringExtra("data_sent"));
                System.out.println("returned json" + returned_intent.getStringExtra("data_recieved"));
                Toast toast = Toast.makeText(getApplicationContext(), returned_intent.getStringExtra("resp_msg").toString(), Toast.LENGTH_SHORT);
                toast.show();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                System.out.println("returned message on cancelled " + returned_intent.getStringExtra("resp_msg"));
                System.out.println("returned code on cancelled " + returned_intent.getStringExtra("resp_code"));
                Toast toast = Toast.makeText(getApplicationContext(), returned_intent.getStringExtra("resp_msg").toString(), Toast.LENGTH_SHORT);
                toast.show();
            } else {

            }
        }
    }

    //TODO
//    private String getSignedMsg(String input) {
//        try {
//            /**************************************************************************************************************
//             *                                    TODO                                                                     *
//             *IMPORTANT : Signing of the message should be done in the backend to ensure that the keys are not compromised *
//             *               and  can be modified on a regular  basis.                                                     *
//             ***************************************************************************************************************/
//
//
//            String privKey = "ziw0k/sv94zFjb0m7nUW1ry8JPef95P0/xGwv0eXQIDAQABAoIBAECPg1aoDnWTAhmJUnyasZbU89W592v0b4EmjPVPkucd9dcGGTFcKyn79lgGpGu0xx1T5GF5fI8raTWYCeObDYiLpcqcR0kLL1XAakbiMPlp5LnwCPmRo8bRvCySdGDyMrfZsWHbDdNZ3c26XjbVZvLfPxhKbLZX/0XOzrtxWQP8jzIDCBpzIWpEzuZG3JUWHLuP5rQBduqyrjfL3IZe3QtheCcwJZNmy49jxjHw3UJAroPP48XP9R58Jmvmql/jc4V3vrssLiyxVYA9g3vkbWLGz2MlIIztiMVx6EF60fViElfFBoPHovN8FiuCSv9SGOaPTOdgSy0XLVUQkYqINtECgYEA+1s4JEoCSTjsgX/sF3tbuV+l25MNHd0kB4GvBCg+CAuk/CObFJVo82UpyqZfxFrIDAxKaS6bkjVB4esEc08R4ZjEgKFzvLFlngYT1dnmQYF+tsK3dMwIM+3tX4tV2SmFyWi6+HtIKTNGTbdUwDNscAkcfBYU2MzRBSCTnZ+O0h8CgYEAhH7Ia4FYh5zYyDgqPpw4+F59Duk82PK62m7ij7V9faXD/vlw88HVvZ4Zh8qwGO/OH5I8Uf5NDSTYcVrgoKq8f1cy1fqplHtVJSa6FqoBFdWt1aZDbKE8DVrBgPtGzjobm6VjQVKK6BP76LYPdal3a/+QdDgfGb5+ZXXvrU0qWAMCgYA8XzuL087awXZk4FHXjgSI8MVIiPhkjOIDf42G5ReJHSrieLbd6ckMdOblyrJD2j4kTqtsugoZwvrxBV7LR1mXIvdHof/HeaBbpUAMZErevvitcvVXc04hEuupwDC9eTCuIJ6P4iSB6NPhBMCxbWiFPFsAHtmg/QjTP3DVMia+pwKBgB2Omu9An5tBJiskPGhTOXJOwd89sNFE9OIdsnUUq7YH8L1EsCnwp9nxFwjv8nDdrkHkwYgZDt9LxO0ktM+ixwIxVanejU4OZWQwS3sa2bshO/JZcNke7uKbyGcZpyYCWH5UWtwLqekz7BJx6uj4sSdqj7MdgfKgSFoiASHMARI5AoGBAOJEgmSVfKQs1jbLPxNKAF8WXqjPa5P/Sc0Q/0mNsSpJAa79RLBnliFIUXz24ls7mUPGK5j29Kopaq5umHcYPjMv6SYvgY4f2GgRsYDmUxi+Yq72fmeiyNHj1HAMOGM6KoE4+KCChpcw8J1oLZ1HtswDvevtleHlRMJtc8Gqk/OQ";
//            // Get private key from String
//            PrivateKey pk = loadPrivateKey(privKey);
//
//            Signature sign = Signature.getInstance("SHA512withRSA");
//            sign.initSign(pk);
//            System.out.println("TEST3");
//            sign.update(input.getBytes());
//            byte[] hashBytes = sign.sign();
//            return bytesToHexString(hashBytes);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//
//
//    }

//    private static String bytesToHexString(byte[] bytes) {
//        StringBuilder sb = new StringBuilder(bytes.length * 2);
//
//        Formatter formatter = new Formatter(sb);
//        for (byte b : bytes) {
//            formatter.format("%02x", b);
//        }
//        formatter.close();
//        return sb.toString();
//    }
//
//    private PrivateKey loadPrivateKey(String key64) throws GeneralSecurityException {
//        byte[] privkeybytes = Base64.decode(key64, Base64.NO_WRAP);
//        KeyFactory fact;
//        try {
//            fact = KeyFactory.getInstance("RSA");
//            PKCS8EncodedKeySpec privateSpec = new PKCS8EncodedKeySpec(privkeybytes);
//            PrivateKey privateKey1 = fact.generatePrivate(privateSpec);
//            return privateKey1;
//        } catch (NoSuchAlgorithmException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (InvalidKeySpecException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        return null;
//    }
}