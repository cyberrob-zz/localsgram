package de.s3xy.retrofitsample.app.api;

import de.s3xy.retrofitsample.app.pojo.PopularPhotos;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by robertwang on 14/6/9.
 */
public interface InstagramApiInterface {

    @GET("/media/popular")
    void getPopularPhotos(@Query("client_id") String clientID,
                          Callback<PopularPhotos> cb);

    @GET("/media/search")
    void searchMedia(@Query("client_id") String clientID,
                     @Query("lat") String lat,
                     @Query("lng") String lng,
                     @Query("distance") String distance,
                     Callback<PopularPhotos> cb);
}
