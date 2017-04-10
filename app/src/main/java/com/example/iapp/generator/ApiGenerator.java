package com.example.iapp.generator;

import com.example.iapp.BuildConfig;
import com.squareup.okhttp.OkHttpClient;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Created by rahul on 06/04/17.
 */

public class ApiGenerator {

    public static final String BASE_URL_AUTHORIZATION =  "https://corporateapiprojectwar.mybluemix.net/corporate_banking/mybank/";
    public static final String BASE_URL_RETAILBANKING=  "https://retailbanking.mybluemix.net/banking/icicibank/";

    public static final String client_id= BuildConfig.CLIENT_ID;
    public static final String access_code=BuildConfig.ACCESS_CODE;

    public  static <S> S createServiceAuthorization(Class<S> serviceClass)
    {

        RequestInterceptor requestInterceptor=new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader("Accept", "application/json");
            }
        };

        OkHttpClient okHttpClient = new OkHttpClient();

        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setEndpoint(BASE_URL_AUTHORIZATION)
                .setClient(new OkClient(okHttpClient))
                .setLogLevel(RestAdapter.LogLevel.FULL);
        builder.setRequestInterceptor(requestInterceptor);
        RestAdapter adapter = builder.build();
        return adapter.create(serviceClass);
    }


    public  static <S> S createServiceRetailBanking(Class<S> serviceClass)
    {

        RequestInterceptor requestInterceptor=new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader("Accept", "application/json");

            }
        };

        OkHttpClient okHttpClient = new OkHttpClient();

        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setEndpoint(BASE_URL_RETAILBANKING)
                .setClient(new OkClient(okHttpClient))
                .setLogLevel(RestAdapter.LogLevel.FULL);
        builder.setRequestInterceptor(requestInterceptor);
        RestAdapter adapter = builder.build();

        return adapter.create(serviceClass);

    }
}
