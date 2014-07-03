package de.s3xy.retrofitsample.app;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import de.s3xy.retrofitsample.app.api.ApiClient;
import de.s3xy.retrofitsample.app.api.InstagramClient;
import de.s3xy.retrofitsample.app.pojo.PopularPhotos;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class InstagramFragment extends Fragment implements AbsListView.OnItemClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PHOTOS = "popular_photos";
    private static final String TAG = InstagramFragment.class.getSimpleName();
    private static final String KEY_CMD = "key_cmd";

    // TODO: Rename and change types of parameters
    private PopularPhotos photos;

    private OnFragmentInteractionListener mListener;

    /**
     * The fragment's ListView/GridView.
     */
    private GridView mGridView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private InstagramAdapter mAdapter;

    public enum INSTA_CMD {
        NEARBY, POPULAR
    }

    public static INSTA_CMD CMD = INSTA_CMD.NEARBY;

    public static InstagramFragment newInstance() {
        InstagramFragment fragment = new InstagramFragment();
        return fragment;
    }

    public static InstagramFragment newInstance(String photoStringfied) {
        InstagramFragment fragment = new InstagramFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PHOTOS, photoStringfied);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public InstagramFragment() {
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.instagram, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (NetworkUtil.getConnectivityStatus(getActivity()) == NetworkUtil.TYPE_NOT_CONNECTED) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.search_range:

                if (CMD == INSTA_CMD.NEARBY) {
                    ((InstagramActivity) getActivity()).createSearchRangeDialog();
                }

                break;

            case R.id.action_nearby:

                refreshNearbyData();

                break;

            case R.id.action_popular:

                refreshPopularData();

                break;
        }

        return true;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (CMD == INSTA_CMD.NEARBY)
            menu.findItem(R.id.search_range).setEnabled(true);
        else if (CMD == INSTA_CMD.POPULAR)
            menu.findItem(R.id.search_range).setEnabled(false);
    }

    void refreshPopularData() {

        CMD = INSTA_CMD.POPULAR;
        photos = null;
        mAdapter.notifyDataSetInvalidated();

        getActivity().setProgressBarIndeterminateVisibility(true);

        ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.popular_default_title));

        InstagramClient.getInstagramApiInterface()
                .getPopularPhotos(RetroApp.instagram_client_id,
                        new Callback<PopularPhotos>() {
                            @Override
                            public void success(PopularPhotos popularPhotos, Response response) {
                                getActivity().setProgressBarIndeterminateVisibility(false);
                                Log.d(TAG, "@ API response: " + response.getStatus());
                                Log.d(TAG, "@ Photo count: " + popularPhotos.getData().size());

                                photos = popularPhotos;

                                mAdapter.setPhotos(popularPhotos);
                                mAdapter.notifyDataSetInvalidated();
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                getActivity().setProgressBarIndeterminateVisibility(false);
                                Log.e(TAG, error.getMessage());
                            }
                        }
                );
    }

    void refreshNearbyData() {

        photos = null;
        CMD = INSTA_CMD.NEARBY;
        if (mAdapter == null) mAdapter = new InstagramAdapter(getActivity());
        mAdapter.notifyDataSetInvalidated();

        try {
            if (TextUtils.isEmpty(RetroApp.theAddress)) {
                ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.around_default_title));
            } else {
                ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.around, RetroApp.theAddress));
            }
        } catch (java.lang.NullPointerException npe) {

        }

        // protect ourselves from NPE
        if (TextUtils.isEmpty(RetroApp.cur_lat) || TextUtils.isEmpty(RetroApp.cur_lng)) {
            return;
        }

        getActivity().setProgressBarIndeterminateVisibility(true);

        InstagramClient.getInstagramApiInterface()
                .searchMedia(
                        RetroApp.instagram_client_id,
                        RetroApp.cur_lat,
                        RetroApp.cur_lng,
                        RetroApp.search_range,
                        new Callback<PopularPhotos>() {
                            @Override
                            public void success(PopularPhotos nearbyPhotos, Response response) {

                                getActivity().setProgressBarIndeterminateVisibility(false);

                                Log.d(TAG, "@ API response: " + response.getStatus());
                                Log.d(TAG, "@ Photo count: " + nearbyPhotos.getData().size());

                                if (nearbyPhotos == null) {
                                    setEmptyText(getString(R.string.retro_error));
                                } else {
                                    Toast.
                                            makeText(
                                                    getActivity(),
                                                    getString(R.string.got_num_of_photos, nearbyPhotos.getData().size()),
                                                    Toast.LENGTH_SHORT)
                                            .show();

                                    ApiClient.getTheClient(getActivity()).getAddress(RetroApp.cur_location);

                                    if (TextUtils.isEmpty(RetroApp.theAddress)) {
                                        ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.around_default_title));
                                    } else {
                                        ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.around, RetroApp.theAddress));
                                    }
                                }

                                photos = nearbyPhotos;

                                mAdapter.setPhotos(nearbyPhotos);
                                mAdapter.notifyDataSetInvalidated();
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                getActivity().setProgressBarIndeterminateVisibility(false);
                                setEmptyText(getString(R.string.retro_error));
                                Log.e(TAG, error.toString());
                            }
                        }
                );
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        setRetainInstance(true);

        mAdapter = new InstagramAdapter(getActivity());
    }

    @Override
    public void onStart() {
        super.onStart();
        if (NetworkUtil.getConnectivityStatus(getActivity()) == NetworkUtil.TYPE_NOT_CONNECTED) {

            getActivity().setProgressBarIndeterminateVisibility(false);
            //getActivity().getActionBar().setTitle(getString(R.string.oops));
            setEmptyText(getText(R.string.no_network));

        } else {

//            if (mAdapter.getCount() == 0) {
//                if (CMD == INSTA_CMD.NEARBY) {
//                    if (!TextUtils.isEmpty(RetroApp.cur_lat) &&
//                            !TextUtils.isEmpty(RetroApp.cur_lng)) {
//                        refreshNearbyData();
//                    }
//                } else if (CMD == INSTA_CMD.POPULAR)
//                    refreshPopularData();
//            }

            getActivity().setProgressBarIndeterminateVisibility(false);

            if (CMD == INSTA_CMD.NEARBY) {
                if (TextUtils.isEmpty(RetroApp.theAddress)) {
                    ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.around_default_title));
                } else {
                    ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.around, RetroApp.theAddress));
                }
            } else if (CMD == INSTA_CMD.POPULAR) {
                ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.popular_default_title));
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        Log.d(TAG, "@save CMD: " + CMD.name());

        if (CMD == INSTA_CMD.NEARBY)
            outState.putInt(KEY_CMD, 0);
        else if (CMD == INSTA_CMD.POPULAR)
            outState.putInt(KEY_CMD, 1);

        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_instagram, container, false);

        // Set the adapter
        mGridView = (GridView) view.findViewById(R.id.gridView);
        mGridView.setEmptyView(view.findViewById(android.R.id.empty));
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(this);

        if (savedInstanceState != null) {

            if (savedInstanceState.getInt(KEY_CMD) == 0) {
                CMD = INSTA_CMD.NEARBY;
            } else if (savedInstanceState.getInt(KEY_CMD) == 1) {
                CMD = INSTA_CMD.POPULAR;
            }
            Log.d(TAG, "@ retrieve CMD: " + CMD.name());
        }

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            mListener.onFragmentInteraction(photos.getData().get(position).getLink());
        }
    }

    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {

        View emptyView = mGridView.getEmptyView();

        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String url);
    }

}
