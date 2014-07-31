package de.s3xy.retrofitsample.app;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.text.Spannable;
import android.text.SpannableString;

import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import de.s3xy.retrofitsample.app.pojo.PopularPhotos;
import de.s3xy.retrofitsample.app.ui.font.TypefaceSpan;

@ReportsCrashes(
        formKey = "", // This is required for backward compatibility but not used
        mailTo = "robertwang.tw@gmail.com",
        mode = ReportingInteractionMode.TOAST,
        resToastText = R.string.crash_toast_text
)
public class RetroApp extends Application {

    public static PopularPhotos CACHED_PHOTOS = null;
    public static String HOST = "https://api.instagram.com/v1/";
    public static String instagram_client_id = "6893ad42f6fa4f40aea2f3c584f71313";

    public static String cur_lat, cur_lng = "";
    public static String search_range = "5000";
    public static String theAddress = "";
    public static Location cur_location;

    public static boolean IS_WEARABLE_FEATURE_PURCHASED = false;



    /* A parameter for limiting the frequency of firing notification
        If user find it too bothering the value can be up to 10 or 20.
        For more frequent notification just set the value to smaller
     */
    public static int NOTIFICATION_TRIGGER_COUNTER = 3;
    private Context mCtx;

    @Override
    public void onCreate() {
        super.onCreate();
        mCtx = getBaseContext();
    }




}
