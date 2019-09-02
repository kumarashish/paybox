package common;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class SignatureGenerate {
    public static byte[] encodeWithHMACSHA2(String text,String keyString) throws UnsupportedEncodingException {
        java.security.Key sk = new

                javax.crypto.spec.SecretKeySpec(keyString.getBytes("UTF-8"),"HMACSHA512");

        javax.crypto.Mac mac =
                null;
        try {
            mac = javax.crypto.Mac.getInstance(sk.getAlgorithm());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            mac.init(sk);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        byte[] hmac = new byte[0];
        try {
            hmac = mac.doFinal(text.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return hmac;
    }
    /*
     * Convert from byte array to HexString
     */
    public static String byteToHexString(byte byData[])
    {
        StringBuilder sb = new StringBuilder(byData.length * 2);
        for(int i = 0; i < byData.length; i++)
        {
            int v = byData[i] & 0xff;
            if(v < 16)
                sb.append('0');
            sb.append(Integer.toHexString(v));
        }
        return sb.toString();
    }

    /*
    * Encoded with HMACSHA512 and encoded with utf-8 using url encoder for
    given list of parameter values appended with the key
    */
    public static String getEncodedValueWithSha2(String hashKey,String ...param)
    {
        String resp = null;
        StringBuilder sb = new StringBuilder();
        for (String s : param) {
            sb.append(s);
        }
        try{
            System.out.println("[getEncodedValueWithSha2]String to Encode ="
                    + sb.toString());
            resp = byteToHexString(encodeWithHMACSHA2(sb.toString(),
                    hashKey));
//resp = URLEncoder.encode(resp,"UTF-8");
        }catch(Exception e)
        {
            System.out.println("[getEncodedValueWithSha2]Unable to encocdvalue with key :" + hashKey + " and input :" + sb.toString());
            e.printStackTrace();
        }
        return resp;
    }
}
