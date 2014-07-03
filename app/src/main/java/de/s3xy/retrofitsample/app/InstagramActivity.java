package de.s3xy.retrofitsample.app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.ActivityRecognitionClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

import java.util.List;

import de.s3xy.retrofitsample.app.api.ApiClient;
import de.s3xy.retrofitsample.app.location.SimpleGeofenceStore;


public class InstagramActivity extends ActionBarActivity
        implements
        InstagramFragment.OnFragmentInteractionListener,
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener,
        LocationListener, RangeFragment.OnRangeFragmentInteractionListener {

    private static final String TAG = InstagramActivity.class.getSimpleName();

    /**
     * PARAMS FOR LOCATION UPDATE & ACTIVITY RECOGNITION
     */

    public static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private static boolean LOCATION_READY = false;
    private LocationClient mLocationClient;

    // Milliseconds per second
    private static final int MILLISECONDS_PER_SECOND = 1000;
    // Update frequency in seconds
    public static final int UPDATE_INTERVAL_IN_SECONDS = 60;
    // Update frequency in milliseconds
    private static final long UPDATE_INTERVAL =
            MILLISECONDS_PER_SECOND * UPDATE_INTERVAL_IN_SECONDS;
    // The fastest update frequency, in seconds
    private static final int FASTEST_INTERVAL_IN_SECONDS = 1;
    // A fast frequency ceiling in milliseconds
    private static final long FASTEST_INTERVAL =
            MILLISECONDS_PER_SECOND * FASTEST_INTERVAL_IN_SECONDS;

    private static final long SECONDS_PER_HOUR = 60;
    private static final long GEOFENCE_EXPIRATION_IN_HOURS = 12;
    private static final long GEOFENCE_EXPIRATION_TIME =
            GEOFENCE_EXPIRATION_IN_HOURS *
                    SECONDS_PER_HOUR *
                    MILLISECONDS_PER_SECOND;

    public static final int DETECTION_INTERVAL_SECONDS = 20;
    public static final int DETECTION_INTERVAL_MILLISECONDS =
            MILLISECONDS_PER_SECOND * DETECTION_INTERVAL_SECONDS;

    private List<Geofence> mGeofenceList;
    private SimpleGeofenceStore mGeofenceStorage;

    // Global variable to hold the current location
    Location mCurrentLocation;
    // Define an object that holds accuracy and frequency parameters
    LocationRequest mLocationRequest;

    boolean mUpdatesRequested;

    private IntentFilter filter;

    // Stores the PendingIntent used to request geofence monitoring
    //private PendingIntent mGeofenceRequestIntent;

    /*
     * Store the PendingIntent used to send activity recognition events
     * back to the app
     */
    private PendingIntent mActivityRecognitionPendingIntent;
    // Store the current activity recognition client
    private ActivityRecognitionClient mActivityRecognitionClient;

    @Override
    public void onRangeChanged(int range) {
        Log.d(TAG, "@ range changed: " + range);
    }

    @Override
    public void onDialogDismissed(int final_range) {
        PrefUtil.saveSearchPref(getBaseContext(), final_range);
        Log.d(TAG, "range dialog dismissed.");
        instagramFrag.refreshNearbyData();
    }

    // Defines the allowable request types.
    public enum REQUEST_TYPE {
        ADD, REMOVE_INTENT,
        START_ACTIVITY_RECOGNITION,
        STOP_ACTIVITY_RECOGNITION
    }

    ;
    private REQUEST_TYPE mRequestType;

    // Flag that indicates if a request is underway.
    private boolean mInProgress;
    // Flag that indicates if a activity request is underway.
    private boolean mActivityRecognitionInProgress;

    //private String popularPhotos;
    private InstagramFragment instagramFrag;


    public void isLocationServiceOn() {

        LocationManager lm = null;
        boolean gps_enabled = false, network_enabled = false;
        if (lm == null)
            lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }
        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gps_enabled && !network_enabled) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            AlertDialog dialog =
                    builder.setTitle(getString(R.string.gentle_notice))
                            .setMessage(getResources().getString(R.string.gps_network_not_enabled))
                            .setPositiveButton(getResources().getString(R.string.open_location_settings),
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                            startActivity(
                                                    new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                        }
                                    }
                            )
                            .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                    paramDialogInterface.dismiss();
                                    Toast.makeText(getBaseContext(), getString(R.string.seeya), Toast.LENGTH_SHORT).show();
                                    InstagramActivity.this.finish();
                                }
                            }).create();

            dialog.show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // must be called before setContentView...
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_instagram);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Window window = getWindow();
//            // Translucent status bar
//            window.setFlags(
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // Translucent navigation bar
//            window.setFlags(
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        }

        getActionBar().setHomeButtonEnabled(false);
        getActionBar().setDisplayShowHomeEnabled(false);

        // LOCATION & ACTIVITY

        isLocationServiceOn();
        mInProgress = false;
        mActivityRecognitionInProgress = false;

        mActivityRecognitionClient =
                new ActivityRecognitionClient(getBaseContext(), this, this);
        startUpdates();

         /*
         * Create the PendingIntent that Location Services uses
         * to send activity recognition updates back to this app.
         */
        Intent intent = new Intent(
                getBaseContext(), ActivityRecognitionIntentService.class);
        /*
         * Return a PendingIntent that starts the IntentService.
         */
        mActivityRecognitionPendingIntent =
                PendingIntent.getService(getBaseContext(), 0, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

        servicesConnected();

        /*
         * Create a new location client, using the enclosing class to
         * handle callbacks.
         */
        mLocationClient = new LocationClient(this, this, this);

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create();
        // Use high accuracy
        mLocationRequest.setPriority(
                LocationRequest.PRIORITY_LOW_POWER);
        // Set the update interval to 5 seconds
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        // Set the fastest update interval to 1 second
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        mUpdatesRequested = true;

        filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);

        setProgressBarIndeterminateVisibility(true);

        if (instagramFrag == null) {
            instagramFrag = InstagramFragment.newInstance();
        }

        if (getFragmentManager().findFragmentById(R.id.container) == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, instagramFrag)
                    .commit();
        }
    }

    private boolean servicesConnected() {
        // Check that Google Play services is available
        int resultCode =
                GooglePlayServicesUtil.
                        isGooglePlayServicesAvailable(this);
        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            // In debug mode, log the status
            Log.d("Location Updates",
                    "Google Play services is available.");
            // Continue
            return true;
            // Google Play services was not available for some reason
        } else {
            // Get the error code

            // Get the error dialog from Google Play services
            Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
                    resultCode,
                    this,
                    CONNECTION_FAILURE_RESOLUTION_REQUEST);

            // If Google Play services can provide an error dialog
            if (errorDialog != null) {
                // Create a new DialogFragment for the error dialog
                ErrorDialogFragment errorFragment =
                        new ErrorDialogFragment();
                // Set the dialog in the DialogFragment
                errorFragment.setDialog(errorDialog);
                // Show the error dialog in the DialogFragment
                errorFragment.show(getSupportFragmentManager(),
                        "Location Updates");
            }
            return false;
        }
    }

    // We should just connect to mActivityRecognitionClient every once a while
    // NOT always
    private void startUpdates() {
        mRequestType = REQUEST_TYPE.START_ACTIVITY_RECOGNITION;

        // Check for Google Play services
        if (!servicesConnected()) {
            return;
        }
        // If a request is not already underway
        if (!mActivityRecognitionInProgress) {
            // Indicate that a request is in progress
            mActivityRecognitionInProgress = true;
            // Request a connection to Location Services
            mActivityRecognitionClient.connect();
            //
        } else {
            /*
             * A request is already underway. You can handle
             * this situation by disconnecting the client,
             * re-setting the flag, and then re-trying the
             * request.
             */
        }
    }

    /**
     * Turn off activity recognition updates
     */
    public void stopUpdates() {
        // Set the request type to STOP
        mRequestType = REQUEST_TYPE.STOP_ACTIVITY_RECOGNITION;
        /*
         * Test for Google Play services after setting the request type.
         * If Google Play services isn't present, the request can be
         * restarted.
         */
        if (!servicesConnected()) {
            return;
        }
        // If a request is not already underway
        if (!mActivityRecognitionInProgress) {
            // Indicate that a request is in progress
            mActivityRecognitionInProgress = true;
            // Request a connection to Location Services
            mActivityRecognitionClient.connect();
            //
        } else {
            /*
             * A request is already underway. You can handle
             * this situation by disconnecting the client,
             * re-setting the flag, and then re-trying the
             * request.
             */
            mActivityRecognitionInProgress = false;
            mActivityRecognitionClient = null;

            NotificationManager
                    mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            mNotificationManager.cancel(001);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mLocationClient.connect();
        registerReceiver(NetworkChangeReceiver, filter);
    }

    @Override
    protected void onStop() {
        // If the client is connected
        if (mLocationClient.isConnected()) {
            /*
             * Remove location updates for a listener.
             * The current Activity is the listener, so
             * the argument is "this".
             */
            mLocationClient.removeLocationUpdates(this);
        }
        // Disconnecting the client invalidates it.
        mLocationClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(NetworkChangeReceiver);
        super.onDestroy();
    }

    @Override
    public void onFragmentInteraction(String url) {
        Intent viewIntent = new Intent("android.intent.action.VIEW", Uri.parse(url));
        startActivity(viewIntent);
    }

    @Override
    public void onConnected(Bundle bundle) {
        //Toast.makeText(this, getString(R.string.got_positioned), Toast.LENGTH_SHORT).show();

        if (mUpdatesRequested && mLocationClient.isConnected()) {
            mLocationClient.requestLocationUpdates(mLocationRequest, this);
        }
//        switch (mRequestType) {
//            case START_ACTIVITY_RECOGNITION:
//                if (mActivityRecognitionClient.isConnected()) {
//                    /*
//                     * Request activity recognition updates using the preset
//                     * detection interval and PendingIntent. This call is
//                     * synchronous.
//                     */
//                    mActivityRecognitionClient.requestActivityUpdates(
//                            DETECTION_INTERVAL_MILLISECONDS,
//                            mActivityRecognitionPendingIntent);
//                    /*
//                     * Since the preceding call is synchronous, turn off the
//                     * in progress flag and disconnect the client
//                     */
//                    mActivityRecognitionInProgress = false;
//                    mActivityRecognitionClient.disconnect();
//                }
//                break;
//            case STOP_ACTIVITY_RECOGNITION:
//                mActivityRecognitionClient.removeActivityUpdates(
//                        mActivityRecognitionPendingIntent);
//                break;
//            default:
//                Log.e(TAG, "Unknown request type in onConnected().");
//                break;
//        }
    }

    @Override
    public void onDisconnected() {
        /*
        * Called by Location Services if the connection to the location client
        * drops because of errors
        * */

        Toast.makeText(this, getString(R.string.lost_position), Toast.LENGTH_SHORT).show();

        // Turn off the request flag
        mInProgress = false;
        // Destroy the current location client
        mLocationClient = null;

//        mActivityRecognitionInProgress = false;
//        mActivityRecognitionClient = null;
    }

    @Override
    public void onLocationChanged(Location location) {
        // Report to the UI that the location was updated

        // if device moved less than 500 meters than just return
        // Try to reduce network access frequency
        if (mCurrentLocation != null &&
                location.distanceTo(mCurrentLocation) < 500) {
            return;
        }

        mCurrentLocation = location;

        RetroApp.cur_location = location;
        RetroApp.cur_lat = Double.toString(location.getLatitude());
        RetroApp.cur_lng = Double.toString(location.getLongitude());


        if (LOCATION_READY == false) {
            if (!TextUtils.isEmpty(RetroApp.cur_lat) &&
                    !TextUtils.isEmpty(RetroApp.cur_lng)) {

                LOCATION_READY = !LOCATION_READY;
                if (NetworkUtil.getConnectivityStatus(getBaseContext())
                        != NetworkUtil.TYPE_NOT_CONNECTED) {

                    ApiClient.getTheClient(getBaseContext()).getAddress(location);

                    if(InstagramFragment.CMD == InstagramFragment.INSTA_CMD.NEARBY) {
                        instagramFrag.refreshNearbyData();
                    } else if(InstagramFragment.CMD == InstagramFragment.INSTA_CMD.POPULAR) {
                        instagramFrag.refreshPopularData();
                    }
                }
            }
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // Turn off the request flag
        mInProgress = false;
        mActivityRecognitionInProgress = false;

        /*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(
                        this,
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            /*
             * If no resolution is available, display a dialog to the
             * user with the error.
             */
            Log.e(TAG, "@ no resolution available: " + connectionResult.getErrorCode());
        }
    }

    public void createSearchRangeDialog() {

        FragmentManager fm = getSupportFragmentManager();
        RangeFragment rangeFragment = new RangeFragment();
        rangeFragment.show(fm, "fragment_search_range");

    }

    static int receive_count = 1;

    private BroadcastReceiver NetworkChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            // NO_NETWORK
            if (NetworkUtil.getConnectivityStatus(context) == NetworkUtil.TYPE_NOT_CONNECTED) {

                Toast.makeText(getBaseContext(), R.string.no_network, Toast.LENGTH_SHORT).show();
                receive_count = 1;

            } else if(NetworkUtil.getConnectivityStatus(context) == NetworkUtil.TYPE_MOBILE ||
                    NetworkUtil.getConnectivityStatus(context) == NetworkUtil.TYPE_WIFI) {


                if(receive_count == 1) {
                    if (InstagramFragment.CMD == InstagramFragment.INSTA_CMD.NEARBY) {
                        instagramFrag.refreshNearbyData();
                    } else if (InstagramFragment.CMD == InstagramFragment.INSTA_CMD.POPULAR) {
                        instagramFrag.refreshPopularData();
                    }
                }

                Log.d(TAG, "@ Connected via " +
                        ((NetworkUtil.getConnectivityStatus(context) == NetworkUtil.TYPE_MOBILE) ? " mobile" : " wifi") +
                        " : " + (receive_count++));
            }
        }
    };


}
