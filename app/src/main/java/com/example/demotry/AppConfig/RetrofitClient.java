package com.example.demotry.AppConfig;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import okhttp3.logging.HttpLoggingInterceptor.*;




public class RetrofitClient {

    private static Retrofit retrofit;


    public static Retrofit getRetrofit() {
      
        //219.91.243.8 - blast


        if (retrofit == null) {
            HttpLoggingInterceptor loginterceptor = new HttpLoggingInterceptor();
            loginterceptor.setLevel(Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().build();
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://219.91.243.8:7373/")
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}
