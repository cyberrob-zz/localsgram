package de.s3xy.retrofitsample.app;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import de.s3xy.retrofitsample.app.api.ApiClient;
import de.s3xy.retrofitsample.app.api.InstagramClient;
import de.s3xy.retrofitsample.app.pojo.Datum;
import de.s3xy.retrofitsample.app.pojo.PopularPhotos;
import de.s3xy.retrofitsample.app.ui.InstagramActivity;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by robertwang on 14/6/10.
 */
public class ActivityRecognitionIntentService extends IntentService {

    private static final String NAME = ActivityRecognitionIntentService.class.getSimpleName();

    public ActivityRecognitionIntentService() {
        super(NAME);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // If the incoming intent contains an update
        if (ActivityRecognitionResult.hasResult(intent)) {
            // Get the update
            ActivityRecognitionResult result =
                    ActivityRecognitionResult.extractResult(intent);
            // Get the most probable activity
            DetectedActivity mostProbableActivity =
                    result.getMostProbableActivity();
            /*
             * Get the probability that this activity is the
             * the user's actual activity
             */
            int confidence = mostProbableActivity.getConfidence();
            /*
             * Get an integer describing the type of activity
             */
            int activityType = mostProbableActivity.getType();
            String activityName = getNameFromType(activityType);
            /*
             * At this point, you have retrieved all the information
             * for the current update. You can display this
             * information to the user in a notification, or
             * send it to an Activity or Service in a broadcast
             * Intent.
             */

            Log.d(NAME, confidence + "% sure you're " + activityName);
            //sendToast(confidence + "% sure you're " + activityName);

            int prefer_activity_type =
                    Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(getBaseContext()).
                            getString("notify_activity_type", "2"));

            //Log.d(NAME, "Prefer type " + getNameFromType(prefer_activity_type));

            // Do NOTHING if user don't want to get notified
            if (PreferenceManager
                    .getDefaultSharedPreferences(getBaseContext())
                    .getBoolean("pref_notification", true)
                    == Boolean.FALSE) {
                Log.d(NAME, " Do NOTHING if user don\'t want to get notified");
                return;
            }

            /* when the counter == 0 & confidence > 70%
                    then we fire a notification for nearby posts
             */
            if (prefer_activity_type == activityType && confidence > 50) {

                Log.d(NAME, "Counter: " + RetroApp.NOTIFICATION_TRIGGER_COUNTER);

                if (RetroApp.NOTIFICATION_TRIGGER_COUNTER == 0) {
                    Log.d(NAME, "Ready to trigger notification!");
                    // Time to fire notification
                    if (RetroApp.CACHED_PHOTOS == null) {
                        retrieveNewPosts();
                        return;
                    }

                    Datum thePost = pickMostLiked(RetroApp.CACHED_PHOTOS);
                    if (thePost != null) {
                        fireNotification(thePost);
                    }

                    // RESET the counter
                    RetroApp.NOTIFICATION_TRIGGER_COUNTER = 3;
                } else {
                    RetroApp.NOTIFICATION_TRIGGER_COUNTER--;
                }
                //sendToast("Counter: " + RetroApp.NOTIFICATION_TRIGGER_COUNTER);

                fireDebugNotification(confidence + "% sure you're " + activityName,
                        RetroApp.NOTIFICATION_TRIGGER_COUNTER);
                Log.d(NAME, "Counter: " + RetroApp.NOTIFICATION_TRIGGER_COUNTER);
            }


        } else {
            /*
             * This implementation ignores intents that don't contain
             * an activity update. If you wish, you can report them as
             * errors.
             */
        }
    }

    private void sendToast(final String msg) {
        Handler mHandler = new Handler(getMainLooper());
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private Datum pickMostLiked(PopularPhotos photos) {
        // Choose the most liked post
        Datum chosenPhoto = null;
        int likes = 0;
        for (Datum photo : photos.getData()) {
            if (photo.getLikes().getCount() > likes) {
                likes = photo.getLikes().getCount();
                chosenPhoto = photo;
            }
        }
        return chosenPhoto;
    }

    private void retrieveNewPosts() {

        if (TextUtils.isEmpty(RetroApp.cur_lng) || TextUtils.isEmpty(RetroApp.cur_lat)) {
            return;
        }

        InstagramClient.getInstagramApiInterface()
                .searchMedia(
                        RetroApp.instagram_client_id,
                        RetroApp.cur_lat,
                        RetroApp.cur_lng,
                        RetroApp.search_range,
                        new Callback<PopularPhotos>() {
                            @Override
                            public void success(PopularPhotos nearbyPhotos, Response response) {

                                Log.d(NAME, "@ API response: " + response.getStatus());
                                Log.d(NAME, "@ Photo count: " + nearbyPhotos.getData().size());

                                if (nearbyPhotos != null) {

                                    RetroApp.CACHED_PHOTOS = nearbyPhotos;
                                    Datum thePost = pickMostLiked(RetroApp.CACHED_PHOTOS);
                                    if (thePost != null) {
                                        fireNotification(thePost);
                                    }
                                }
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                Log.e(NAME, error.toString());
                            }
                        }
                );
    }

    private void fireDebugNotification(String msg, int counter) {
        int mNotificationId = counter;
        Notification.Builder theBuilder = new Notification.Builder(getBaseContext())
                .setContentTitle("Counter " + counter)
                .setContentText(msg)
                .setSmallIcon(R.drawable.ic_launcher);

        ((NotificationManager)getSystemService(NOTIFICATION_SERVICE))
                .notify(mNotificationId, theBuilder.build());
    }

    private void fireNotification(Datum thePost) {

        Intent resultIntent = new Intent(this, InstagramActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(InstagramActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        int numMessage = 0;
        final int mNotificationId = 001;
        final Notification.Builder nBuilder = new Notification.Builder(getBaseContext())
                .setContentIntent(resultPendingIntent)
                .setContentTitle(getString(R.string.interested_in_looking_around))
                .setContentText(getString(R.string.got_num_of_photos, RetroApp.CACHED_PHOTOS.getData().size()))
                .setNumber(++numMessage)
                .setSmallIcon(R.drawable.ic_launcher);

        final NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        InstagramClient.getInstagramApiInterface().
                getPostImage(
                        thePost.getImages().getStandard_resolution().getUrl(),
                        new Callback<com.squareup.okhttp.Response>() {
                            @Override
                            public void success(com.squareup.okhttp.Response response, Response response2) {
                                try {
                                    Bitmap largeIcon = BitmapFactory.decodeStream(response2.getBody().in());
                                    nBuilder.setLargeIcon(largeIcon);

                                } catch (IOException e) {
                                    Log.e(NAME, e.toString());
                                }

                                mNotifyMgr.notify(mNotificationId, nBuilder.build());
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                Log.e(NAME, error.toString());

                                mNotifyMgr.notify(mNotificationId, nBuilder.build());
                            }
                        }
                );


    }

    /**
     * Map detected activity types to strings
     *
     * @param activityType The detected activity type
     * @return A user-readable name for the type
     */
    private String getNameFromType(int activityType) {
        switch (activityType) {
            case DetectedActivity.IN_VEHICLE:
                return "in_vehicle";
            case DetectedActivity.ON_BICYCLE:
                return "on_bicycle";
            case DetectedActivity.ON_FOOT:
                return "on_foot";
            case DetectedActivity.STILL:
                return "still";
            case DetectedActivity.UNKNOWN:
                return "unknown";
            case DetectedActivity.TILTING:
                return "tilting";
        }
        return "unknown";
    }
}
