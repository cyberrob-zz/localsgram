package de.s3xy.retrofitsample.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RangeFragment.OnRangeFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RangeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RangeFragment extends DialogFragment implements SeekBar.OnSeekBarChangeListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String TAG = RangeFragment.class.getSimpleName();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnRangeFragmentInteractionListener mListener;
    //private int SEARCH_RANGE = 5000;
    private View rootView;
    private SeekBar rangeSeekbar;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RangeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RangeFragment newInstance(String param1, String param2) {
        RangeFragment fragment = new RangeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public RangeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//
//        rootView = inflater.inflate(R.layout.fragment_range, container, false);
//
//        getDialog().setTitle(R.string.search_range);
//        SeekBar rangeSeekbar = (SeekBar) rootView.findViewById(R.id.seekBar);
//        rangeSeekbar.setProgress(Integer.parseInt(RetroApp.search_range));
//        rangeSeekbar.setOnSeekBarChangeListener(this);
//        getDialog().setCanceledOnTouchOutside(true);
//        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
//            @Override
//            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
//                mListener.onDialogDismissed();
//                return true;
//            }
//        });
//        getDialog().setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialogInterface) {
//                dialogInterface.dismiss();
//                Log.d(TAG, "@ Nothing changed.");
//            }
//        });
//
//        return rootView;
//    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnRangeFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int range, boolean b) {
        int myIncrement = 50;
        int progress = ((int) Math.round(range / myIncrement)) * myIncrement;
        seekBar.setProgress(progress);

        Log.d(TAG, "@ search range: " + progress);
        RetroApp.search_range = String.valueOf(progress);
        mListener.onRangeChanged(progress);
        getDialog().setTitle(getString(R.string.select_range, RetroApp.search_range));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        RetroApp.search_range = PrefUtil.getSearchPref(getActivity());
        Log.i(TAG, "@ getting range pref: " + RetroApp.search_range);

        rootView =
                getActivity()
                        .getLayoutInflater()
                        .inflate(R.layout.fragment_range, null);

        rangeSeekbar = (SeekBar) rootView.findViewById(R.id.seekBar);
        rangeSeekbar.setKeyProgressIncrement(100);
        rangeSeekbar.setProgress(Integer.parseInt(RetroApp.search_range));
        rangeSeekbar.setOnSeekBarChangeListener(this);

        return new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.select_range, RetroApp.search_range))
                .setView(rootView)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mListener.onDialogDismissed(rangeSeekbar.getProgress());
                            }
                        }
                )
                .setNegativeButton(android.R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                Log.d(TAG, "@ Nothing changed.");
                            }
                        }
                )
                .create();

    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnRangeFragmentInteractionListener {

        public void onRangeChanged(int range);

        public void onDialogDismissed(int final_range);
    }

}
