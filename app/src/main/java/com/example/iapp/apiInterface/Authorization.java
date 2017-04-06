package com.example.iapp.apiInterface;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by rahul on 06/04/17.
 */

public interface Authorization {
    @GET("/authenticate_client")
    void authorizeClient(@Query("client_id") String client_id,@Query("password") String password, Callback<Response> responseCallback);
}
