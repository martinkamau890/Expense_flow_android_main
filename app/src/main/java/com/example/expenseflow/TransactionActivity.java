package com.example.expenseflow;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expenseflow.Converter.ConversionStatusConverter;
import com.example.expenseflow.DataBase.AppDatabase;
import com.example.expenseflow.Entity.AccountCategory;
import com.example.expenseflow.Entity.ParsedSms;
import com.example.expenseflow.Entity.UserAccount;
import com.example.expenseflow.Enum.AccountType;
import com.example.expenseflow.Enum.ConversionStatus;
import com.example.expenseflow.api.ApiService;
import com.example.expenseflow.api.RetrofitClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TransactionActivity extends AppCompatActivity {

    List<ParsedSms> parsedSmsList = new ArrayList<>();

    private RecyclerView recyclerView;
    private ImageButton assignButton;

    private Button manualCashEntryButton;

    private LinearLayout manualTransactionEntryField;

    private AppDatabase appDatabase = AppDatabase.getInstance(TransactionActivity.this);

    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_transaction);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeView();
        loadParsedSmsList();
        implementActionListeners();
        loadRecyclerView();
        apiService = RetrofitClient.getRetrofitInstance(TransactionActivity.this).create(ApiService.class);
    }

    private void initializeView(){
        recyclerView = findViewById(R.id.unassigned_transactions_recycler);
        assignButton = findViewById(R.id.sync_mpesa_sms_fab);
        manualCashEntryButton = findViewById(R.id.manual_cash_entry_button);
        manualTransactionEntryField = findViewById(R.id.manual_entry_container);
    }

    public void loadParsedSmsList(){

        ExecutorService executorService = Executors.newSingleThreadExecutor();

        executorService.execute(() ->{

           parsedSmsList = appDatabase.parsedSmsDAO().getAll(ConversionStatusConverter.fromConversionStatus(ConversionStatus.PENDING));

            executorService.shutdown();
        });

    }

    public void loadRecyclerView(){
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        executorService.execute(() -> {
            List<AccountCategory> accountCategories = appDatabase.accountCategoryDAO().getAccountCategoryList(AccountType.EXPENSE.toString());
            List<UserAccount> userAccountList = appDatabase.userAccountDAO().getUserAccount();

            runOnUiThread(() -> {

                UnassignedTransactionAdapter transactionAdapter = new UnassignedTransactionAdapter(parsedSmsList, userAccountList, accountCategories, TransactionActivity.this, apiService);

                recyclerView.setAdapter(transactionAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(TransactionActivity.this));});
        });


    }

    public void implementActionListeners(){
        assignButton.setOnClickListener(view -> {

        });

        manualCashEntryButton.setOnClickListener(view -> {
            if (manualTransactionEntryField.getVisibility() == View.VISIBLE) {
                manualTransactionEntryField.setVisibility(View.GONE);
            } else {
                manualTransactionEntryField.setVisibility(View.VISIBLE);
            }
        });
    }


}