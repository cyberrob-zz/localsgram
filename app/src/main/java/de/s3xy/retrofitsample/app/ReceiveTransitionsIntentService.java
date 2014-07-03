package de.s3xy.retrofitsample.app;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationClient;



/**
 * Created by robertwang on 14/6/10.
 */
public class ReceiveTransitionsIntentService extends IntentService {

    private static final String NAME = ReceiveTransitionsIntentService.class.getSimpleName();


    public ReceiveTransitionsIntentService() {
        super(NAME);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // First check for errors
        if (LocationClient.hasError(intent)) {
            // Get the error code with a static method
            int errorCode = LocationClient.getErrorCode(intent);
            // Log the error
            Log.e("ReceiveTransitionsIntentService",
                    "Location Services error: " +
                            Integer.toString(errorCode)
            );
            /*
             * You can also send the error code to an Activity or
             * Fragment with a broadcast Intent
             */
        /*
         * If there's no error, get the transition type and the IDs
         * of the geofence or geofences that triggered the transition
         */
        } else {
            // Get the type of transition (entry or exit)
            int transitionType =
                    LocationClient.getGeofenceTransition(intent);
            // Test that a valid transition was reported
            if ((transitionType == Geofence.GEOFENCE_TRANSITION_ENTER)
                    ||
                    (transitionType == Geofence.GEOFENCE_TRANSITION_EXIT)
                    ) {
//                List<Geofence> triggerList =
//                        getTriggeringGeofences(intent);
//
//                String[] triggerIds = new String[triggerList.size()];
//
//                for (int i = 0; i < triggerIds.length; i++) {
//                    // Store the Id of each geofence
//                    triggerIds[i] = triggerList.get(i).getRequestId();
//                }
                /*
                 * At this point, you can store the IDs for further use
                 * display them, or display the details associated with
                 * them.
                 */

                // An invalid transition was reported
            } else {
                Log.e("ReceiveTransitionsIntentService",
                        "Geofence transition error: " + transitionType);
            }
        }
    }
}
