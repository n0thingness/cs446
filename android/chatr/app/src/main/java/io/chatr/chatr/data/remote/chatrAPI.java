package io.chatr.chatr.data.remote;

/**
 * Created by Daniel on 2018-03-22.
 */

import io.chatr.chatr.LoginActivity;
import io.chatr.chatr.data.model.Location;
import io.chatr.chatr.data.model.LoginRequest;
import io.chatr.chatr.data.model.Match;
import io.chatr.chatr.data.model.StringData;
import io.chatr.chatr.data.model.User;
import io.chatr.chatr.data.model.Location;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface chatrAPI {

    String BASE_URL = "http://uw-chatr-api.herokuapp.com";

    @GET("users/{id}")
    Call<User> getUser(@Path("id") int id);

    @POST("users/login")
    Call<User> login(@Body LoginRequest body);

    @POST("users/register")
    Call<User> register(@Body LoginRequest body);

    @GET("resource")
    Call<StringData> getResource();

    @GET("location/{gid}")
    Call<Location> getLocation(@Path("gid") String gid);

    @POST("location")
    Call<Location> newLocation(@Body Location body);

    @POST("users/profile")
    Call<User> updateProfile(@Body User body);

    @GET("location/{gid}/checkin")
    Call<Match> checkIn(@Path("gid") String gid);

    @GET("users/match")
    Call<Match> getMatch();

    @GET("users/match/clear")
    Call<Match> clearMatch();

    @POST("users/match/message")
    Call<Match> setMatchMessage(@Body StringData body);
}