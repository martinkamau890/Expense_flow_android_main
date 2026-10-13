package com.example.expenseflow;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.expenseflow.Converter.DateTimeConverter;
import com.example.expenseflow.DTO.AuthenticationResponse;
import com.example.expenseflow.DTO.RegistrationDTO;
import com.example.expenseflow.Entity.TransactionDate;
import com.example.expenseflow.Services.TransactionDateService;
import com.example.expenseflow.api.ApiService;
import com.example.expenseflow.api.RetrofitClient;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    private TextView fullNameTV;
    private TextView emailTV;
    private TextView phoneNumberTV;
    private TextView passwordTV;
    private TextView confirmedPasswordTV;

    private Button signUpBT;

    private CheckBox checkBox;

    private TextView loginTV;

    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getWindow().setSoftInputMode(
                android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
        );

        initializeViews();
        viewListeners();
        apiService = RetrofitClient.getRetrofitInstance(SignUpActivity.this).create(ApiService.class);
    }

    private void initializeViews(){
        fullNameTV = findViewById(R.id.signup_name);
        emailTV = findViewById(R.id.signup_email);
        phoneNumberTV = findViewById(R.id.signup_phone);
        passwordTV = findViewById(R.id.signup_password);
        confirmedPasswordTV = findViewById(R.id.signup_confirm_password);
        signUpBT = findViewById(R.id.signup_button);
        checkBox = findViewById(R.id.signup_terms_check);
        loginTV = findViewById(R.id.login_redirect_link);
    }

    private void viewListeners(){

        signUpBT.setOnClickListener(v -> {
            signUp();
        });

        loginTV.setOnClickListener(v -> {
            loadLoginPage();
        });
    }

    private boolean validInputs() {

        String fullName = fullNameTV.getText().toString().trim();

        String email = emailTV.getText().toString().trim();

        String phoneNumber = phoneNumberTV.getText().toString().trim();

        String password = passwordTV.getText().toString().trim();

        String confirmPassword = confirmedPasswordTV.getText().toString().trim();

        if (fullName.isEmpty()) {
            fullNameTV.setError("Full name is required");
            fullNameTV.requestFocus();
            return false;
        }


        if (email.isEmpty()) {
            emailTV.setError("Email is required");
            emailTV.requestFocus();
            return false;
        }


        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailTV.setError("Enter a valid email");
            emailTV.requestFocus();
            return false;
        }


        if (phoneNumber.isEmpty()) {
            phoneNumberTV.setError("Phone number is required");
            phoneNumberTV.requestFocus();
            return false;
        }


        if (phoneNumber.length() < 10) {
            phoneNumberTV.setError("Invalid phone number");
            phoneNumberTV.requestFocus();
            return false;
        }


        if (password.isEmpty()) {
            passwordTV.setError("Password is required");
            passwordTV.requestFocus();
            return false;
        }


        if (password.length() < 4) {
            passwordTV.setError("Password must be at least 4 characters");
            passwordTV.requestFocus();
            return false;
        }

        if(confirmPassword.isEmpty()){
            confirmedPasswordTV.setError("Confirm your password");
            confirmedPasswordTV.requestFocus();
            return false;
        }

        if (!confirmPassword.equals(password)){
            confirmedPasswordTV.setError("Password does not match");
            confirmedPasswordTV.requestFocus();
            return false;
        }

        if (!checkBox.isChecked()){
            checkBox.setError("Confirm agreement to the terms and condition");
            checkBox.requestFocus();
            return false;
        }

        return true;
    }

    private void signUp(){
        if (validInputs()){

            RegistrationDTO registrationDTO = new RegistrationDTO(fullNameTV.getText().toString(), emailTV.getText().toString(), phoneNumberTV.getText().toString(), passwordTV.getText().toString());
            Call<AuthenticationResponse> call = apiService.registerUser(registrationDTO);

            call.enqueue(new Callback<AuthenticationResponse>() {
                @Override
                public void onResponse(Call<AuthenticationResponse> call, Response<AuthenticationResponse> response) {
                    if(response.isSuccessful()){

                        AuthenticationResponse authenticationResponse = response.body();

                        TokenManager tokenManager = new TokenManager(SignUpActivity.this);

                        tokenManager.saveToken(authenticationResponse.getToken());

                        ExecutorService executorService = Executors.newSingleThreadExecutor();

                        executorService.execute(() ->{

                            TransactionDateService transactionDateService = new TransactionDateService(SignUpActivity.this);
                            TransactionDate transactionDate = new TransactionDate();
                            transactionDate.setRegDate(authenticationResponse.getRegistrationDate());

                            transactionDateService.signupDate(transactionDate);

                            executorService.shutdown();
                        });

                        loadHomePage();
                    }else {
                        try {
                            String error = response.errorBody().string();

                            Toast.makeText(SignUpActivity.this, "Error " + error, Toast.LENGTH_SHORT).show();


                        }catch (Exception e){
                            Toast.makeText(SignUpActivity.this, "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                }

                @Override
                public void onFailure(Call<AuthenticationResponse> call, Throwable throwable) {
                    Toast.makeText(SignUpActivity.this, "Network error", Toast.LENGTH_SHORT).show();
                }
            });

            Toast.makeText(SignUpActivity.this, "Signup successful", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadLoginPage(){
        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void loadHomePage(){
        Intent intent = new Intent(SignUpActivity.this, HomePageActivity.class);
        startActivity(intent);
        finish();
    }
}