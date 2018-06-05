package bit.basemodule.utility;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by bit on 13/10/17.
 */

public class AppSharedPref {
    private static final int MODE_PRIVATE = 0;
    private static AppSharedPref pref;
    private static SharedPreferences appSharedPrefs;
    private static SharedPreferences.Editor prefsEditor;
    private static String PREF_FILE = "MY_FILE.XML";

    AppSharedPref(Context context) {
        appSharedPrefs = context.getSharedPreferences(PREF_FILE, MODE_PRIVATE);
        prefsEditor = appSharedPrefs.edit();
    }

    public static AppSharedPref initialize(Context context) {
        if (pref == null) {
            pref = new AppSharedPref(context);
        }
        return pref;
    }

    public static AppSharedPref getInstance(Context context) {
        synchronized (pref) {
            if (pref == null) {
                initialize(context);
            }
        }
        return pref;
    }

    public static String getStringValue(String keyName) {
        if (appSharedPrefs.contains(keyName)) {
            return appSharedPrefs.getString(keyName, null);
        }
        return null;

    }

    public static void setStringValue(String keyName, String value) {
        prefsEditor.putString(keyName, value);
        prefsEditor.commit();
    }


}
