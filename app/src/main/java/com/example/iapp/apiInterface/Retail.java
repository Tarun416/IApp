package com.example.iapp.apiInterface;

import com.example.iapp.models.FundTransfer;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by rahul on 10/04/17.
 */

public interface Retail {
    @GET("/fundTransfer")
    void fundTransfer(@Query("client_id") String client_id, @Query("token") String password, @Query("srcAccount") String sourceAccount,@Query("destAccount") String destinationAccount,@Query("amt") String amount,@Query("payeeDesc") String payeeDescription,@Query("payeeId") String payeeId,@Query("type_of_transaction") String type,Callback<Response> responseCallback);

    @GET("/balanceenquiry")
    void balanceEnquiry(@Query("client_id") String client_id, @Query("token") String password,@Query("accountno") String accountNo,Callback<Response> responseCallback);
}
