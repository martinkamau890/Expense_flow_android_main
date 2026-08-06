package com.example.expenseflow;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthenticationInterceptor implements Interceptor {

    private TokenManager tokenManager;

public  AuthenticationInterceptor(TokenManager tokenManager){
    this.tokenManager = tokenManager;
}

    @Override
    public Response intercept(Chain chain) throws IOException {
        String token = tokenManager.getToken();

        Request request = chain.request();

        String path = request.url().encodedPath();

        Log.d("APIINTERCEPTOR", "Token: " + token);

        if (path.equals("/auth/login") || path.equals("/auth/register")) {

            return chain.proceed(request);
        }

        String value  = "Bearer " + token;

        if (token != null){


            request = request.newBuilder().addHeader("Authorization", value).build();
        }

        return chain.proceed(request);
    }
}
