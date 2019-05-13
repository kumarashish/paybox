package common;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

import com.mypaybox.ProfileDetails;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.DeliverableDataset;


/**
 * Created by Honey Singh on 4/5/2017.
 */

public class Utils {

    public static int getIntegerFromString(String valuee)
    {
        if (valuee.contains(".")) {
            String[] value = valuee.split(Pattern.quote("."));
            double val = Double.parseDouble(value[1]);
            if (val > 0.5) {
                return (Integer.parseInt(value[0]) + 1);
            }
            return Integer.parseInt(value[0]);
        } else {
            return Integer.parseInt(valuee);
        }
    }


    public static boolean isEmailIdValidated(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isPassword_ConfirmPasswordSame(String password, String confirmPassword) {
        if (password.equals(confirmPassword)) {
            return true;
        }
        return false;
    }

    public static boolean isPasswordValid(String password) {
        if (password.length() > 2) {
            return true;
        }
        return false;
    }

    public static boolean isMobileNumberValid(String mobile) {
        if ((mobile.length() == 10)) {
            return true;
        }
        return false;
    }

    public static boolean isInternetAvailable(Context context) {

        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }
    public static String getFormatedDate(String val)
    {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            Date newDate = format.parse(val);

            format = new SimpleDateFormat("dd-MMM");
            String date = format.format(newDate);
            return date;
        }catch (Exception ex)
        {
            ex.fillInStackTrace();
        }
        return "";
    }
    /*************************
     * camera module popup
     *************************************/
    public static void selectImageDialog(final Activity act) {
        final CharSequence[] items = {"Take Photo", "Choose from Library(Image)", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(act);
        builder.setTitle("Add File(Photo/Video)");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    if (isDeviceSupportCamera(act)) {
                        captureImage(act);
                    } else {
                        Toast.makeText(act, "Sorry! Your device doesn't support camera", Toast.LENGTH_LONG).show();
                    }
                } else if (items[item].equals("Choose from Library(Image)")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    act.startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            ProfileDetails.SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    /**
     * Checking device has camera hardware or not
     */
    private static boolean isDeviceSupportCamera(Activity act) {
        if (act.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    public static void captureImage(Activity act) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        Common.imageUri = getOutputMediaFileUri(Common.MEDIA_TYPE_IMAGE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, Common.imageUri);

        // start the image capture Intent
        try {
            act.startActivityForResult(intent, Common.CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
        }catch (Exception ex)
        {
            ex.fillInStackTrace();
        }
    }

    public static Uri getOutputMediaFileUri(int type) {
        File tempFile = getOutputMediaFile(type);
        Uri uri = Uri.fromFile(tempFile);
        return uri;
    }

    private static File getOutputMediaFile(int type) {
        // External sdcard location
        File mediaStorageDir = new File(Common.sdCardPath);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {

                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == Common.MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }
        File files = mediaFile;
        return mediaFile;
    }

    public static byte[] getImageByteArray(File inputFile) {
        try {
            FileInputStream input = new FileInputStream(inputFile);
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            byte[] buffer = new byte[65536];
            int l;

            while ((l = input.read(buffer)) > 0)
                output.write(buffer, 0, l);

            input.close();
            output.close();

            return output.toByteArray();

        } catch (IOException e) {
            System.err.println(e);
            return null;
        }
    }
    public static boolean isItemAlreadyDelivering(ArrayList<DeliverableDataset> deliveringList, DeliverableDataset item) {
        boolean status = false;
       if(deliveringList!=null) {
           if (deliveringList.size() > 0) {
               for (int i = 0; i < deliveringList.size(); i++) {
                   if (deliveringList.get(i).getItemId().trim().equalsIgnoreCase(item.getItemId().trim())) {
                       status = true;
                       item.setQuantity(deliveringList.get(i).getQuantity());
                       item.setEnabled(true);
                   }
               }
           }
       }
        return status;
    }

    public static String getMyDate(String date) {
        if (date.contains("T")) {
            String[] newDate = date.split("T");
            return newDate[0];
        } else {
            return date;
        }
    }

    public static boolean comparedDate(String date1, String date2) {
        date1 = getMyDate(date1);
        date2 = getMyDate(date2);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date11 = format.parse(date1);
            Date date22 = format.parse(date2);
            if (date11.compareTo(date22) <= 0) {
                return true;
            }
        } catch (Exception ex) {
            ex.fillInStackTrace();
        }
        return false;
    }
    public static String getRandomNumber()
    {
        int passwordSize = 10;
        char[] chars = "abcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < passwordSize; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        String output = sb.toString();
        return output;
    }
    public static String getCurrentDate()
    {
       DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        return format.format(cal.getTime());
    }


}

