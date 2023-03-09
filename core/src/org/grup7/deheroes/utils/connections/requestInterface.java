package org.grup7.deheroes.utils.connections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface requestInterface {
    // as we are making a post request to post a data
    // so we are annotating it with post
    // and along with that we are passing a parameter as users

    @POST("/api/auth/signin")
    //void login(@Field("username") String username, @Field("password") String password, Callback<userData> callback);

    //on below line we are creating a method to post our data.
    Call<userData> createPost(@Body userData dataModal);
}
