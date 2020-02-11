package com.battmobile.battmobiletechnician.utility;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

public class Apiurl {

  public static UploadAPIs getClint(){

        Gson gson=new GsonBuilder()
                .setLenient()
                .create();

        OkHttpClient okHttpClient= new OkHttpClient.Builder()
                .readTimeout(2, TimeUnit.MINUTES)
                .connectTimeout(4,TimeUnit.MINUTES)
                .build();


        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl("http://mindsmetricksdemo.com/MB/web/users/api/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();

      UploadAPIs apiInterface=retrofit.create(UploadAPIs.class);
        return apiInterface;

    }

}
