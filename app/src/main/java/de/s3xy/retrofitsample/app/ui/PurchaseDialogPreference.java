package de.s3xy.retrofitsample.app.ui;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.util.Log;

/**
 * Created by robertwang on 7/15/14.
 */
public class PurchaseDialogPreference extends DialogPreference {

    private static final String TAG = PurchaseDialogPreference.class.getSimpleName();

    public interface PurchaseDecisionListener {
        public void onUserWantToBuy();

        public void onMaybeNextTime();
    }

    PurchaseDecisionListener decisionListener;


    public PurchaseDialogPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setupListener(Activity activity) {
        decisionListener = (PurchaseDecisionListener) activity;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        super.onClick(dialog, which);
        if (which == -1) {
            Log.d(TAG, "@ now user wants buy the wearable support!");
            //TODO trigger analytics event
            decisionListener.onUserWantToBuy();
        } else {
            Log.d(TAG, "@ user don't want to but the support.");
            //TODO trigger analytics event
            decisionListener.onMaybeNextTime();
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }
}
