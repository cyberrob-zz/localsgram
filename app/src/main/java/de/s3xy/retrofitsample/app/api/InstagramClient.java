package de.s3xy.retrofitsample.app.api;

import de.s3xy.retrofitsample.app.RetroApp;
import retrofit.RestAdapter;

/**
 * Created by robertwang on 14/6/9.
 */
public class InstagramClient {

    private static InstagramApiInterface instagramApiInterface;

    public static InstagramApiInterface getInstagramApiInterface() {
        if (instagramApiInterface == null) {
            RestAdapter restAdapter =
                    new RestAdapter.Builder()
                            .setEndpoint(RetroApp.HOST)
                            .setLogLevel(RestAdapter.LogLevel.BASIC)
                            .build();

            instagramApiInterface = restAdapter.create(InstagramApiInterface.class);
        }
        return instagramApiInterface;
    }
}
