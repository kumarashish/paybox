package common;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Honey Singh on 8/26/2017.
 */

public class PrefManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "welcome";

    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }
public void handleLogout()
{
    pref.edit().clear().apply();
}
    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public String getEmailId() {
        return pref.getString("emailId", "");
    }
    public String getMobileNumber() {
        return pref.getString("Mobile", "");
    }
    public String getUserName() {
        return pref.getString("UserName", "");
    }
      public String getPrefsString(String key)
      {
          return pref.getString(key, "");
      }
      public int getInt(String key)
      {
          return pref.getInt("LoggedInUserType", 1);
      }
    public boolean getBoolean(String key)
    {
        return pref.getBoolean(key, false);
    }
    public SharedPreferences.Editor getEditor() {
        return editor;
    }
}
