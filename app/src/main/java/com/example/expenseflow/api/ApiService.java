package com.example.expenseflow.api;

import com.example.expenseflow.DTO.AuthenticationResponse;
import com.example.expenseflow.DTO.HomePageData;
import com.example.expenseflow.DTO.LoginDTO;
import com.example.expenseflow.DTO.RegistrationDTO;
import com.example.expenseflow.DTO.TransactionDTO;
import com.example.expenseflow.Entity.Transaction;
import com.example.expenseflow.DTO.TransactionResponseDTO;
import com.example.expenseflow.DTO.UserAccountBundle;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {

    @POST("/auth/register")
    Call<AuthenticationResponse> registerUser(@Body RegistrationDTO registrationDTO);

    @POST("/auth/login")
    Call<AuthenticationResponse> login(@Body LoginDTO loginDTO);

    @GET("/api/homePageData")
    Call<HomePageData> loadHomePageData();

    @GET("api/account/getAccounts")
    Call<UserAccountBundle> loadUserAccounts();

    @POST("/api/transaction/expense")
    Call<TransactionResponseDTO> createExpense(@Body List<TransactionDTO> transaction);
}
