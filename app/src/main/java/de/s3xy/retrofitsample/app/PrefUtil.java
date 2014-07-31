package de.s3xy.retrofitsample.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gms.location.DetectedActivity;

/**
 * Created by robertwang on 6/30/14.
 */
public class PrefUtil {

    public static final String PREF_SEARCH_RANGE = "PREF_SEARCH_RANGE";
    public static final String PREF_NOTIFIED_IN_ACITIVITY_TYPE = "PREF_NOTIFIED_IN_ACITIVITY_TYPE";

    public static final String PREF_LAST_KNOWN_LAT = "PREF_LAST_KNOWN_LAT";
    public static final String PREF_LAST_KNOWN_LNG = "PREF_LAST_KNOWN_LNG";

    private static final String TAG = PrefUtil.class.getSimpleName();

    public static SharedPreferences getDefaultPref(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void saveLastKnownLat(Context context, String lastLat) {
        getDefaultPref(context).edit().putString(PREF_LAST_KNOWN_LAT, lastLat).commit();
    }

    public static void saveLastKnownLng(Context context, String lastLng) {
        getDefaultPref(context).edit().putString(PREF_LAST_KNOWN_LNG, lastLng).commit();
    }

    public static String getLastKnownLat(Context context) {
        return getDefaultPref(context).getString(PREF_LAST_KNOWN_LAT, "");
    }

    public static String getLastKnownLng(Context context) {
        return getDefaultPref(context).getString(PREF_LAST_KNOWN_LNG, "");
    }

    public static void savePrefNotifiedInActivityType(Context context, int type) {
        if(getDefaultPref(context).edit().putInt(PREF_NOTIFIED_IN_ACITIVITY_TYPE, type).commit()) {
            Log.d(TAG, "@ Saving got_notification_in_type: " + type);
        } else {
            Log.d(TAG, "@ Failed Saving got_notification_in_type.");
        }

    }

    public static void saveSearchPref(Context context, int range) {
        if(getDefaultPref(context).edit().putInt(PREF_SEARCH_RANGE, range).commit()){
            Log.d(TAG, "@ Saving range pref: " + range);
            RetroApp.search_range = String.valueOf(range);
        } else {
            Log.d(TAG, "@ Failed Saving Range Pref.");
        }
    }

    public static String getSearchPref(Context context) {
        SharedPreferences preferences = getDefaultPref(context);
        return String.valueOf(preferences.getInt(PREF_SEARCH_RANGE, 5000));
    }
}
