package cn.bjtu.nourriture.Preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by ftb on 15-1-3.
 */
public class PrefUtils {
    public static final String PREFS_PSEUDO_KEY = "__PSEUDO__" ;
    public static final String PREFS_PASSWORD_KEY = "__PASSWORD__" ;
    public static final String PREFS_TOKEN_KEY = "__TOKEN__" ;
    public static final String PREFS_EMAIL_KEY = "__EMAIL__";
    public static final String PREFS_ID_KEY = "__ID__" ;
    public static final String PREFS_FIRSTNAME_KEY = "__FIRSTNAME__" ;
    public static final String PREFS_LASTNAME_KEY = "__LASTNAME__" ;
    public static final String PREFS_AVATAR_KEY = "__AVATAR__" ;

    /**
     * Called to save supplied value in shared preferences against given key.
     * @param context Context of caller activity
     * @param key Key of value to save against
     * @param value Value to save
     */
    public static void saveToPrefs(Context context, String key, String value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key,value);
        editor.commit();
    }

    /**
     * Called to retrieve required value from shared preferences, identified by given key.
     * Default value will be returned of no value found or error occurred.
     * @param context Context of caller activity
     * @param key Key to find value against
     * @param defaultValue Value to return if no data found against given key
     * @return Return the value found against given key, default if not found or any error occurs
     */
    public static String getFromPrefs(Context context, String key, String defaultValue) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        try {
            return sharedPrefs.getString(key, defaultValue);
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        }
    }
}