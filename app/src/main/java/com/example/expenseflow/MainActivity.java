package com.example.expenseflow;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.expenseflow.DTO.AuthenticationResponse;
import com.example.expenseflow.DTO.LoginDTO;
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

public class MainActivity extends AppCompatActivity {

    private EditText emailEV;
    private EditText passwordEV;
    private ImageView imageEye;
    private TextView forgotPasswordTV;

    private Button loginButton;

    private TextView createAccountTV;

    private boolean passwordVisible = false;

    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeViews();
        viewListeners();
        apiService = RetrofitClient.getRetrofitInstance(MainActivity.this).create(ApiService.class);
    }

    private void initializeViews(){
        emailEV = findViewById(R.id.email_edit_text);
        passwordEV = findViewById(R.id.password_edit_text);
        imageEye = findViewById(R.id.imgEye);
        forgotPasswordTV = findViewById(R.id.password_edit_text);
        loginButton = findViewById(R.id.login_button);
        createAccountTV = findViewById(R.id.create_account_link);
    }

    private void viewListeners(){

        imageEye.setOnClickListener(v ->{
            setPasswordVisibility();
        });

        createAccountTV.setOnClickListener(v -> {
             loadSignInPage();
        });

        loginButton.setOnClickListener(v -> {
             logInUser();
        });
    }


    public void setPasswordVisibility(){

        if(passwordVisible){

            passwordEV.setInputType(
                    InputType.TYPE_CLASS_TEXT |
                            InputType.TYPE_TEXT_VARIATION_PASSWORD
            );

            imageEye.setImageResource(R.drawable.baseline_visibility_off_24);

            passwordVisible = false;
        }
        else{

            passwordEV.setInputType(
                    InputType.TYPE_CLASS_TEXT |
                            InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            );

            imageEye.setImageResource(R.drawable.baseline_visibility_24);

            passwordVisible = true;
        }
    }

    private void loadSignInPage(){
        Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
        startActivity(intent);
        finish();
    }
    private void loadHomePage(){
        Intent intent = new Intent(MainActivity.this, HomePageActivity.class);
        startActivity(intent);
    }

    private boolean validEntries(String userName, String password){
        if (userName.isEmpty()){
            emailEV.setError("Enter the phone number or email");
            emailEV.requestFocus();
            return false;
        }

        if(password.isEmpty()){
            passwordEV.setError("Enter your password");
            passwordEV.requestFocus();
            return false;
        }
        return true;
    }

    private void logInUser(){

        String userName = emailEV.getText().toString().trim();
        String password = passwordEV.getText().toString().trim();

        if (validEntries(userName, password)){

            LoginDTO loginDTO = new LoginDTO(userName, password);

            Call<AuthenticationResponse> call = apiService.login(loginDTO);

            call.enqueue(new Callback<AuthenticationResponse>() {
                @Override
                public void onResponse(Call<AuthenticationResponse> call, Response<AuthenticationResponse> response) {

                    if(response.isSuccessful()){

                        AuthenticationResponse authenticationResponse = response.body();

                        TokenManager tokenManager = new TokenManager(MainActivity.this);

                        tokenManager.saveToken(authenticationResponse.getToken());

                        ExecutorService executorService = Executors.newSingleThreadExecutor();

                        executorService.execute(() ->{

                            TransactionDateService transactionDateService = new TransactionDateService(MainActivity.this);

                            String regDate = transactionDateService.getRegDate();

                            Log.d("TRANSACTION_DATE_DEBUG", "Pre login date " + regDate);

                            if(regDate == null || regDate.isEmpty()){
                                TransactionDate transactionDate = new TransactionDate();
                                transactionDate.setRegDate(authenticationResponse.getRegistrationDate());
                                transactionDateService.insert(transactionDate);

                                Log.d("TRANSACTION_DATE_DEBUG", "Post login date " + transactionDate.getRegDate());

                            }
                            executorService.shutdown();
                        });

                        loadHomePage();
                    }else {
                        try {
                            String error = response.errorBody().string();

                            Toast.makeText(MainActivity.this, "Error " + error, Toast.LENGTH_SHORT).show();


                        }catch (Exception e){
                            Toast.makeText(MainActivity.this, "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                }

                @Override
                public void onFailure(Call<AuthenticationResponse> call, Throwable throwable) {

                    Log.e("LOGIN", "Request failed", throwable);

                    Toast.makeText(
                            MainActivity.this,
                            throwable.getMessage(),
                            Toast.LENGTH_LONG
                    ).show();
                }
        });
        }


    }
}