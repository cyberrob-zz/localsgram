package de.s3xy.retrofitsample.app.location;

import com.google.android.gms.location.Geofence;

/**
 * Created by robertwang on 14/6/10.
 */
public class SimpleGeofence {
    // Instance variables
    private final String mId;
    private final double mLatitude;
    private final double mLongitude;
    private final float mRadius;
    private long mExpirationDuration;
    private int mTransitionType;

    public static class SimpleGeofenceBuilder {
        // Required parameters
        private final String mId;
        private final double mLatitude;
        private final double mLongitude;
        private final float mRadius;

        // optional parameters
        private long mExpirationDuration;
        private int mTransitionType;

        public SimpleGeofenceBuilder(String id, double lat, double lon, float radius) {
            this.mId = id;
            this.mLatitude = lat;
            this.mLongitude = lon;
            this.mRadius = radius;
        }

        public SimpleGeofenceBuilder duration(long duration) {
            this.mExpirationDuration = duration;
            return this;
        }

        public SimpleGeofenceBuilder transitionType(int type) {
            this.mTransitionType = type;
            return this;
        }

        public SimpleGeofence build() {
            return new SimpleGeofence(this);
        }
    }

    public SimpleGeofence(SimpleGeofenceBuilder builder) {
        this.mId = builder.mId;
        this.mLatitude = builder.mLatitude;
        this.mLongitude = builder.mLongitude;
        this.mRadius = builder.mRadius;
        this.mExpirationDuration = builder.mExpirationDuration;
        this.mTransitionType = builder.mTransitionType;
    }


    // Instance field getters
    public String getId() {
        return mId;
    }
    public double getLatitude() {
        return mLatitude;
    }
    public double getLongitude() {
        return mLongitude;
    }
    public float getRadius() {
        return mRadius;
    }
    public long getExpirationDuration() {
        return mExpirationDuration;
    }
    public int getTransitionType() {
        return mTransitionType;
    }
    /**
     * Creates a Location Services Geofence object from a
     * SimpleGeofence.
     *
     * @return A Geofence object
     */
    public Geofence toGeofence() {
        // Build a new Geofence object
        return new Geofence.Builder()
                .setRequestId(getId())
                .setTransitionTypes(mTransitionType)
                .setCircularRegion(
                        getLatitude(), getLongitude(), getRadius())
                .setExpirationDuration(mExpirationDuration)
                .build();
    }
}
