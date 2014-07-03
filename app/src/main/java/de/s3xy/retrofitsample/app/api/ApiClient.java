package de.s3xy.retrofitsample.app.api;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import de.s3xy.retrofitsample.app.R;
import de.s3xy.retrofitsample.app.RetroApp;

/**
 * Created by robertwang on 14/6/10.
 */
public class ApiClient {

    private static final String TAG = ApiClient.class.getSimpleName();
    private static ApiClient theClient;
    private static Context mContext;
    private String mAddress;

    private ApiClient(Context context) {
        mContext = context;
    }

    public static ApiClient getTheClient(Context context) {

        if (theClient == null) {
            theClient = new ApiClient(context);
        }

        return theClient;
    }

    public String getAddress(Location location) {

        new AddressGetter(mContext).execute(location);

        return mAddress == null ? "No address" : mAddress;
    }


    private class AddressGetter extends AsyncTask<Location, Void, String> {

        Context mContext;
        public AddressGetter(Context context) {
            super();
            mContext = context;
        }

        @Override
        protected String doInBackground(Location... locations) {
            Geocoder geocoder =
                    new Geocoder(mContext, Locale.getDefault());
            // Get the current location from the input parameter list
            Location loc = locations[0];
            // Create a list to contain the result address
            List<Address> addresses = null;
            try {
                /*
                 * Return 1 address.
                 */
                addresses = geocoder.getFromLocation(loc.getLatitude(),
                        loc.getLongitude(), 1);
            } catch (IOException e1) {
                Log.e("LocationSampleActivity",
                        "IO Exception in getFromLocation()");
                e1.printStackTrace();
                return "";//("IO Exception trying to get address");
            } catch (IllegalArgumentException e2) {
                // Error message to post in the log
                String errorString = "Illegal arguments " +
                        Double.toString(loc.getLatitude()) +
                        " , " +
                        Double.toString(loc.getLongitude()) +
                        " passed to address service";
                Log.e("LocationSampleActivity", errorString);
                e2.printStackTrace();
                return errorString = "";
            }

            // If the reverse geocode returned an address
            if (addresses != null && addresses.size() > 0) {
                // Get the first address
                Address address = addresses.get(0);
                /*
                 * Format the first line of address (if available),
                 * city, and country name.
                 */
                String addressText = String.format(
                        "%s, %s, %s",
                        // If there's a street address, add it
                        address.getMaxAddressLineIndex() > 0 ?
                                address.getAddressLine(0) : "",
                        // Locality is usually a city
                        address.getLocality().contains("null") ?
                            "" : address.getLocality(),
                        // The country of the address
                        address.getCountryName()
                );
                // Return the text
                return addressText;
            } else {
                return "No address found";
            }
        }

        @Override
        protected void onPostExecute(String address) {

            if(address.startsWith(",")) address = address.substring(1);

            if(TextUtils.isEmpty(address) ||
                    address.equalsIgnoreCase("No address found")) {

                address = mContext.getString(R.string.your_location);
            }

            RetroApp.theAddress = address;
            Log.d(TAG, "Current Address: " + RetroApp.theAddress);

        }
    }
}
