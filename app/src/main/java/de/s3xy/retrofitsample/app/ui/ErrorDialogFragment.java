package de.s3xy.retrofitsample.app.ui;



import android.app.Dialog;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.DialogFragment;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class ErrorDialogFragment extends DialogFragment {

    private Dialog mDialog;

    public ErrorDialogFragment() {
        super();
        mDialog = null;
    }

    public void setDialog(Dialog iDialog) {
        mDialog = iDialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return mDialog;
    }

}
