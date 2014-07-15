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

    private static final String TAG = PrefUtil.class.getSimpleName();

    public static SharedPreferences getDefaultPref(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static int getPreferNotifiedInActivityType(Context context) {
        return
                getDefaultPref(context).
                        getInt(PREF_NOTIFIED_IN_ACITIVITY_TYPE, DetectedActivity.ON_FOOT);
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
