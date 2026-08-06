package com.example.expenseflow;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expenseflow.Converter.DateTimeConverter;
import com.example.expenseflow.DataBase.AppDatabase;
import com.example.expenseflow.Entity.AccountCategory;
import com.example.expenseflow.Entity.ParsedSms;
import com.example.expenseflow.Entity.UserAccount;
import com.example.expenseflow.Services.MpesaExpenseService;
import com.example.expenseflow.Services.TransactionCallback;
import com.example.expenseflow.api.ApiService;
import com.google.android.material.materialswitch.MaterialSwitch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UnassignedTransactionAdapter extends RecyclerView.Adapter<UnassignedTransactionAdapter.UnassignedViewAdaptor>{

    private final List<ParsedSms> parsedSmsList;
    private final List<UserAccount> userAccounts;
    private final List<AccountCategory> accountCategories;
    private final Context context;
    private final ApiService apiService;

    private UserAccount userAccount;



    public UnassignedTransactionAdapter(List<ParsedSms> parsedSmsList, List<UserAccount> userAccounts, List<AccountCategory> accountCategories, Context context, ApiService apiService) {
        this.parsedSmsList = parsedSmsList;
        this.userAccounts = userAccounts;
        this.accountCategories = accountCategories;
        this.context = context;
        this.apiService = apiService;
    }

    public UserAccount getUserAccount() {
        return userAccount;
    }

    @NonNull
    @Override
    public UnassignedTransactionAdapter.UnassignedViewAdaptor onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.unassigned_transaction_card, parent, false);
        return new UnassignedViewAdaptor(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UnassignedViewAdaptor holder, int position) {

        ParsedSms parsedSms = parsedSmsList.get(position);

        holder.transactionRecipient.setText(parsedSms.getCounterparty());
        holder.transactionAmount.setText("KES " + parsedSms.getTransactionAmount());
        holder.transactionDateTime.setText(
                DateTimeConverter.toLocalDateTime(parsedSms.getTransactionTime()).toString());
        holder.messageSnippet.setText("place holder for message snippet");


        ArrayAdapter<AccountCategory> categoryAdapter =
                new ArrayAdapter<>(
                        holder.itemView.getContext(),
                        android.R.layout.simple_spinner_item,
                        accountCategories);

        categoryAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        holder.categories.setAdapter(categoryAdapter);

        holder.categories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int spinnerPosition, long id) {

                AccountCategory selectedCategory =
                        (AccountCategory) parent.getItemAtPosition(spinnerPosition);

                List<UserAccount> filteredAccounts = new ArrayList<>();

                for (UserAccount account : userAccounts) {

                    if (account.getAccountCategory() == selectedCategory.getId()) {
                        filteredAccounts.add(account);
                    }
                }

                ArrayAdapter<UserAccount> accountAdapter =
                        new ArrayAdapter<>(
                                holder.itemView.getContext(),
                                android.R.layout.simple_spinner_item,
                                filteredAccounts);

                accountAdapter.setDropDownViewResource(
                        android.R.layout.simple_spinner_dropdown_item);

                holder.account.setAdapter(accountAdapter);

                holder.account.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        userAccount = (UserAccount) parent.getItemAtPosition(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        holder.assign.setOnClickListener(v -> {

            int currentPosition = holder.getBindingAdapterPosition();

            MpesaExpenseService expenseService =
                    new MpesaExpenseService(parsedSms, userAccount, context, apiService);

            expenseService.createTransaction(new TransactionCallback() {

                @Override
                public void onSuccess() {

                    ((TransactionActivity) context).runOnUiThread(() -> {

                        parsedSmsList.remove(currentPosition);

                        notifyItemRemoved(currentPosition);
                    });
                }

                @Override
                public void onFailure(String error) {

                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                }
            });
        });

    }

    @Override
    public int getItemCount() {
        return parsedSmsList.size();
    }


    public static class UnassignedViewAdaptor extends RecyclerView.ViewHolder{

        private TextView transactionAmount;
        private TextView transactionDateTime;

        private TextView transactionRecipient;

        private TextView messageSnippet;

        private Spinner categories;

        private Spinner account;

        private MaterialSwitch quickAssignSwitch;

        private Button assign;

        public UnassignedViewAdaptor(@NonNull View itemView) {
            super(itemView);

            transactionAmount = itemView.findViewById(R.id.tv_transactionAmount);
            transactionDateTime = itemView.findViewById(R.id.tv_transactionDateTime);
            transactionRecipient = itemView.findViewById(R.id.tv_transactionRecipient);
            messageSnippet = itemView.findViewById(R.id.tv_message_snippet);
            categories = itemView.findViewById(R.id.category_spinner);
            account = itemView.findViewById(R.id.account_spinner);
            quickAssignSwitch = itemView.findViewById(R.id.quick_assign_switch);
            assign = itemView.findViewById(R.id.assign_button);


        }
    }
}
