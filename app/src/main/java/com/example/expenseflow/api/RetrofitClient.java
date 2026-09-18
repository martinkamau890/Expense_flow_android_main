package com.example.expenseflow.api;

import android.content.Context;
import android.util.Log;

import com.example.expenseflow.AuthenticationInterceptor;
import com.example.expenseflow.TokenManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.time.LocalDateTime;


public class RetrofitClient {

    private static final String BASE_URL = "http://192.168.1.48:9090/";

    private static Retrofit retrofit;

    public static Retrofit getRetrofitInstance(Context context){

        if (retrofit == null){
            TokenManager tokenManager = new TokenManager(context);

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor( message ->
                    Log.d("EXPENSE_FLOW_JSON", message));
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(
                            LocalDateTime.class,
                            (JsonSerializer<LocalDateTime>)
                                    (src, typeOfSrc, context1) ->
                                            context1.serialize(src.toString())
                    )
                    .registerTypeAdapter(
                            LocalDateTime.class,
                            (JsonDeserializer<LocalDateTime>)
                                    (json, typeOfT, context1) ->
                                            LocalDateTime.parse(
                                                    json.getAsString()
                                            )
                    )
                    .create();

            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .addInterceptor(new AuthenticationInterceptor(tokenManager))
                    .addInterceptor(logging)
                    .build();

            retrofit = new Retrofit.Builder().baseUrl(BASE_URL).client(client).addConverterFactory(GsonConverterFactory.create(gson)).build();

        }
        return retrofit;
    }
}
