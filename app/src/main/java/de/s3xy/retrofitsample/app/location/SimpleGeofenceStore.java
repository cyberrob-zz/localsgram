package de.s3xy.retrofitsample.app.location;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by robertwang on 14/6/10.
 */
public class SimpleGeofenceStore {
    // Keys for flattened geofences stored in SharedPreferences
    public static final String KEY_LATITUDE =
            "de.s3xy.retrofitsample.app.geofence.KEY_LATITUDE";
    public static final String KEY_LONGITUDE =
            "de.s3xy.retrofitsample.app.geofence.KEY_LONGITUDE";
    public static final String KEY_RADIUS =
            "de.s3xy.retrofitsample.app.geofence.KEY_RADIUS";
    public static final String KEY_EXPIRATION_DURATION =
            "de.s3xy.retrofitsample.app.geofence.KEY_EXPIRATION_DURATION";
    public static final String KEY_TRANSITION_TYPE =
            "de.s3xy.retrofitsample.app.geofence.KEY_TRANSITION_TYPE";
    // The prefix for flattened geofence keys
    public static final String KEY_PREFIX =
            "de.s3xy.retrofitsample.app.geofence.KEY";

    /*
         * Invalid values, used to test geofence storage when
         * retrieving geofences
         */
    public static final long INVALID_LONG_VALUE = -999l;
    public static final float INVALID_FLOAT_VALUE = -999.0f;
    public static final int INVALID_INT_VALUE = -999;
    // The SharedPreferences object in which geofences are stored
    private final SharedPreferences mPrefs;
    // The name of the SharedPreferences
    private static final String SHARED_PREFERENCES =
            "SharedPreferences";
    // Create the SharedPreferences storage with private access only
    public SimpleGeofenceStore(Context context) {
        mPrefs =
                context.getSharedPreferences(
                        SHARED_PREFERENCES,
                        Context.MODE_PRIVATE);
    }

    public SimpleGeofence getGeofence(String id) {
        double lat = mPrefs.getFloat(
                getGeofenceFieldKey(id, KEY_LATITUDE),
                INVALID_FLOAT_VALUE);

        double lng = mPrefs.getFloat(
                getGeofenceFieldKey(id, KEY_LONGITUDE),
                INVALID_FLOAT_VALUE);

        float radius = mPrefs.getFloat(
                getGeofenceFieldKey(id, KEY_RADIUS),
                INVALID_FLOAT_VALUE);

        long expirationDuration = mPrefs.getLong(
                getGeofenceFieldKey(id, KEY_EXPIRATION_DURATION),
                INVALID_LONG_VALUE);

        int transitionType = mPrefs.getInt(
                getGeofenceFieldKey(id, KEY_TRANSITION_TYPE),
                INVALID_INT_VALUE);

        if (
                lat != SimpleGeofenceStore.INVALID_FLOAT_VALUE &&
                        lng != SimpleGeofenceStore.INVALID_FLOAT_VALUE &&
                        radius != SimpleGeofenceStore.INVALID_FLOAT_VALUE &&
                        expirationDuration !=
                                SimpleGeofenceStore.INVALID_LONG_VALUE &&
                        transitionType != SimpleGeofenceStore.INVALID_INT_VALUE) {

            // Return a true Geofence object
            return
                    new SimpleGeofence.SimpleGeofenceBuilder(id, lat, lng, radius)
                            .build();
            // Otherwise, return null.
        } else {
            return null;
        }
    }

    public void setGeofence(String id, SimpleGeofence geofence) {
        SharedPreferences.Editor editor = mPrefs.edit();
        // Write the Geofence values to SharedPreferences
        editor.putFloat(
                getGeofenceFieldKey(id, KEY_LATITUDE),
                (float) geofence.getLatitude());
        editor.putFloat(
                getGeofenceFieldKey(id, KEY_LONGITUDE),
                (float) geofence.getLongitude());
        editor.putFloat(
                getGeofenceFieldKey(id, KEY_RADIUS),
                geofence.getRadius());
        editor.putLong(
                getGeofenceFieldKey(id, KEY_EXPIRATION_DURATION),
                geofence.getExpirationDuration());
        editor.putInt(
                getGeofenceFieldKey(id, KEY_TRANSITION_TYPE),
                geofence.getTransitionType());
        // Commit the changes
        editor.commit();
    }

    public void clearGeofence(String id) {
            /*
             * Remove a flattened geofence object from storage by
             * removing all of its keys
             */
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.remove(getGeofenceFieldKey(id, KEY_LATITUDE));
        editor.remove(getGeofenceFieldKey(id, KEY_LONGITUDE));
        editor.remove(getGeofenceFieldKey(id, KEY_RADIUS));
        editor.remove(getGeofenceFieldKey(id,
                KEY_EXPIRATION_DURATION));
        editor.remove(getGeofenceFieldKey(id, KEY_TRANSITION_TYPE));
        editor.commit();
    }

    private String getGeofenceFieldKey(String id,
                                       String fieldName) {
        return KEY_PREFIX + "_" + id + "_" + fieldName;
    }
}
