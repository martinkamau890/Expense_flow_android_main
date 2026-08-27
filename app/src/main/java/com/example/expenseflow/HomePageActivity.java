package com.example.expenseflow;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expenseflow.DTO.HomePageData;
import com.example.expenseflow.DTO.PageResponse;
import com.example.expenseflow.DTO.PostedTransactionDTO;
import com.example.expenseflow.DTO.TransactionSummaryDTO;
import com.example.expenseflow.Services.ParseSmsService;
import com.example.expenseflow.Services.SaveExtractedSms;
import com.example.expenseflow.Services.SaveUserAccounts;
import com.example.expenseflow.api.ApiService;
import com.example.expenseflow.api.RetrofitClient;

import java.math.BigDecimal;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomePageActivity extends AppCompatActivity {

    private TextView netWorthText;
    private TextView lastUpdated;
    private TextView totalIncomeText;
    private TextView totalExpensesText;
    private TextView totalAssetsText;
    private TextView totalLiabilitiesText;

    private RecyclerView recyclerView;

    private ApiService apiService;


    private ImageButton addTransactionButton;


    private static final int SMS_PERMISSION_CODE = 100;

    @RequiresApi(api = Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeViews();
        apiService = RetrofitClient.getRetrofitInstance(HomePageActivity.this).create(ApiService.class);
        initializeActionListeners();

        loadHomePageData();

        //Ask permission to the user to allow the app to read messages
        if (ContextCompat.checkSelfPermission(
                HomePageActivity.this,
                Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.READ_SMS},
                    SMS_PERMISSION_CODE);
        }

        SaveUserAccounts saveUserAccounts = new SaveUserAccounts();
        saveUserAccounts.saveUserAccountBundle(apiService, HomePageActivity.this);
        SaveExtractedSms saveExtractedSms = new SaveExtractedSms();
        saveExtractedSms.saveExtractedSms(HomePageActivity.this);


    }

    private void initializeViews(){
        netWorthText = findViewById(R.id.net_worth_val_text);
        lastUpdated = findViewById(R.id.last_updated);
        totalIncomeText = findViewById(R.id.income_amount_text);
        totalExpensesText = findViewById(R.id.expenses_amount_text);
        totalAssetsText = findViewById(R.id.asset_amount_text);
        totalLiabilitiesText = findViewById(R.id.total_liability_text);
        recyclerView = findViewById(R.id.recent_activity_recycler_view);
        addTransactionButton = findViewById(R.id.add_transaction_Entry);
    }

    @RequiresApi(api = Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    private void initializeActionListeners() {

        addTransactionButton.setOnClickListener(v -> {
            ParseSmsService parseSmsService = new ParseSmsService();
            parseSmsService.parseSms(HomePageActivity.this);
            loadTransactionPage();
             });

    }

    private void loadTransactionPage(){
        Intent intent = new Intent(HomePageActivity.this, TransactionActivity.class);
        startActivity(intent);
    }

    private void loadRecyclerView(List<PostedTransactionDTO> postedTransactions){

        AllTransactionAdapter adapter = new AllTransactionAdapter(postedTransactions);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(HomePageActivity.this));
    }

    private void loadTransactionSummaries(@NonNull TransactionSummaryDTO transactionSummary){

        BigDecimal netWorth = transactionSummary.getTotalAssets().subtract(transactionSummary.getTotalLiabilities());

        netWorthText.setText("Ksh" + netWorth);
        totalIncomeText.setText("Ksh" + transactionSummary.getTotalIncome());
        totalExpensesText.setText("Ksh" + transactionSummary.getTotalExpenses());
        totalAssetsText.setText("Ksh" + transactionSummary.getTotalAssets());
        totalLiabilitiesText.setText("Ksh" + transactionSummary.getTotalLiabilities());
    }

    private void loadHomePageData(){
        Call<HomePageData> call = apiService.loadHomePageData();

        call.enqueue(new Callback<HomePageData>() {
            @Override
            public void onResponse(Call<HomePageData> call, Response<HomePageData> response) {
                if(response.isSuccessful()){
                    PageResponse<PostedTransactionDTO> postedTransactionDTO = response.body().getPostedTransaction();

                    List<PostedTransactionDTO> transactionDTOList = postedTransactionDTO.getContent();

                    for(PostedTransactionDTO postedTransactionDTO1: transactionDTOList ){
                        Log.d("Transactions", postedTransactionDTO1.getAccount());
                    }
                    TransactionSummaryDTO summaryDTO = response.body().getTransactionSummary();

                    loadTransactionSummaries(summaryDTO);
                    loadRecyclerView(transactionDTOList);
                }else {
                try {
                    String errorBody = response.errorBody() != null
                            ? response.errorBody().string()
                            : "No error body";

                    Toast.makeText(
                            HomePageActivity.this,
                            "Code: " + response.code(),
                            Toast.LENGTH_LONG
                    ).show();

                    android.util.Log.e(
                            "API_ERROR",
                            "Code: " + response.code() + "\nBody: " + errorBody
                    );

                } catch (Exception e) {
                    android.util.Log.e("API_ERROR", e.getMessage(), e);
                }
            }
                }
            @Override
            public void onFailure(Call<HomePageData> call, Throwable throwable) {

                Log.e("API_FAILURE", "Request failed", throwable);

                Toast.makeText(
                        HomePageActivity.this,
                        throwable.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }
        });

    }

   /* @RequiresApi(api = Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    private void readSms() {

        TransactionTime transactionTime = new TransactionTime();

        //Uri is the address that points to the sms inbox
        Uri uri = Uri.parse("content://sms/inbox");

        //Cursor is the iterator that points to one row at a time in the database
        //Content resolver is the method that the app uses to query the data base
        Cursor cursor = getContentResolver().query(
                //the address or table
                uri,
                //which column to return null means all
                null,
                //this is the where clause
                "address = ? AND date BETWEEN ? AND ?",
                //the value of the where clause
                new String[]{"MPESA", String.valueOf(transactionTime.getStartTime()), String.valueOf(transactionTime.getCurrentTime())},
                //Order by
                "date DESC"
        );

        if(cursor != null){

            while(cursor.moveToNext()){

                String address =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow("address"));

                String body =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow("body"));

                long timeStamp = cursor.getLong(cursor.getColumnIndexOrThrow("date"));

                Date date = new Date(timeStamp);
                LocalDate date1 = LocalDate.ofInstant(date.toInstant(), ZoneId.systemDefault());

                Log.d("SMS", "Sender: " + address);
                Log.d("SMS", "Message: " + body);
                Log.d("SMS", "Date: " + date1);

                Toast.makeText(HomePage.this, address, Toast.LENGTH_LONG).show();
            }

            cursor.close();
        }
    }

    private class TransactionTime {

        long startTime;
        long currentTime;

        public TransactionTime() {
            setCurrentTime();
            setStartTime();
        }

        public long getStartTime() {
            return startTime;
        }

        public void setStartTime() {
            LocalDate date = LocalDate.now().minusDays(1);
            LocalDateTime localDateTime = date.atStartOfDay();
            this.startTime = localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        }

        public long getCurrentTime() {
            return currentTime;
        }

        public void setCurrentTime() {
            this.currentTime = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        }
    }*/

}