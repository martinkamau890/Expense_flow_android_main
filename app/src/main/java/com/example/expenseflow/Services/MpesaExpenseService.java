package com.example.expenseflow.Services;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.expenseflow.Converter.BigDecimalConverter;
import com.example.expenseflow.Converter.DateTimeConverter;
import com.example.expenseflow.DTO.JournalLineDTO;
import com.example.expenseflow.DTO.TransactionDTO;
import com.example.expenseflow.DTO.TransactionResponseDTO;
import com.example.expenseflow.Entity.JournalLine;
import com.example.expenseflow.DataBase.AppDatabase;
import com.example.expenseflow.Entity.Transaction;
import com.example.expenseflow.Enum.EntryType;
import com.example.expenseflow.Enum.SyncStatus;
import com.example.expenseflow.api.ApiService;
import com.google.gson.Gson;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MpesaExpenseService {
    private Context context;
    private ApiService apiService;

    private AppDatabase appDatabase;

    public MpesaExpenseService(Context context, ApiService apiService) {
        this.apiService = apiService;
        this.context = context;
        this.appDatabase = AppDatabase.getInstance(context);

    }

    public List<JournalLineDTO> returnJournalLineDTO(Transaction transaction){

        List<JournalLine> journalLineList = getJournalines(transaction);

        List<JournalLineDTO> journal = new ArrayList<>();

        for (JournalLine journalLine: journalLineList){

            if (journalLine.getEntryType().equals(EntryType.DEBIT)){

                JournalLineDTO debitJournalLine = new JournalLineDTO();

                debitJournalLine.setAmount(BigDecimalConverter.toBigDecimal(journalLine.getAmount()));
                debitJournalLine.setAccountId(journalLine.getAccountId());
                debitJournalLine.setEntryType(journalLine.getEntryType());

                journal.add(debitJournalLine);
            }


            if (journalLine.getEntryType().equals(EntryType.CREDIT)){

                JournalLineDTO creditJournalLine = new JournalLineDTO();

                creditJournalLine.setAmount(BigDecimalConverter.toBigDecimal(journalLine.getAmount()));
                creditJournalLine.setEntryType(journalLine.getEntryType());
                creditJournalLine.setAccountId(journalLine.getAccountId());

                journal.add(creditJournalLine);
            }
        }

        return journal;
    }

    public List<TransactionDTO> returnTransactionDTO(List<Transaction> transactions) {

        List<TransactionDTO> transactionDTOList = new ArrayList<>();

        for (Transaction transaction : transactions) {

            TransactionDTO transactionDTO = new TransactionDTO();

            transactionDTO.setTransactionDate(LocalDateTime.parse(transaction.getTransactionDate()));
            transactionDTO.setTransactionType(transaction.getTransactionType());
            transactionDTO.setTransactionEntryDTOList(
                    returnJournalLineDTO(transaction)
            );

            transactionDTO.setDescription(transaction.getDescription());
            transactionDTO.setRefNumber(transaction.getRefNumber());
            transactionDTO.setCurrencyCode(null);

            transactionDTOList.add(transactionDTO);
            Log.d(
                    "TRANSACTIONS",
                    "Preparing: " + transactionDTO.getRefNumber()
            );
        }

        return transactionDTOList;
    }

    public void createTransaction() {

        ExecutorService executorService =
                Executors.newSingleThreadExecutor();

        executorService.execute(() -> {

            List<Transaction> transactionsToSync = getPendingTransactions();

            if (transactionsToSync.isEmpty()) {

                Log.d("SYNC", "No pending M-Pesa transactions");

                return;
            }

            List<TransactionDTO> transactionDTOList = returnTransactionDTO(transactionsToSync);

            /*for (TransactionDTO transactionDTO: transactionDTOList){
                Log.d("DATE_DEBUG", "for ref number " + transactionDTO.getRefNumber() + " The date is " + transactionDTO.getTransactionDate());
            }*/

            Call<TransactionResponseDTO> call =
                    apiService.createExpense(transactionDTOList);



            call.enqueue(new Callback<TransactionResponseDTO>() {

                @Override
                public void onResponse(
                        Call<TransactionResponseDTO> call,
                        Response<TransactionResponseDTO> response) {

                    if (response.isSuccessful()) {

                        for (Transaction transaction: transactionsToSync){
                            Log.d("DATE_DEBUG", "for ref number " + transaction.getRefNumber() + " Recepient is " + transaction.getDescription());
                        }

                        updatePendingTransactions(transactionsToSync);

                        Log.d(
                                "SYNC",
                                "Successfully synced "
                                        + transactionsToSync.size()
                                        + " transactions"
                        );

                    } else {

                        Log.e(
                                "SYNC",
                                "Sync failed. HTTP code: "
                                        + response.code()
                        );
                    }
                }

                @Override
                public void onFailure(
                        Call<TransactionResponseDTO> call,
                        Throwable throwable) {

                    Log.e(
                            "SYNC",
                            "Network error",
                            throwable
                    );
                }
            });
        });

        executorService.shutdown();
    }

    private void updatePendingTransactions(List<Transaction> transactionList){
        for (Transaction transaction: transactionList){
            ExecutorService executorService =
                    Executors.newSingleThreadExecutor();

            executorService.execute(() -> {

            appDatabase.transactionDAO().updateTransactionStatus(SyncStatus.SYNCED, transaction.getId());
        });
        }
    }

    private List<Transaction> getPendingTransactions(){
        List<Transaction> transactionList = appDatabase.transactionDAO().getPendingTransactions(SyncStatus.PENDING);

        if (!transactionList.isEmpty()) {

            for (Transaction transaction: transactionList){
                Log.d("Transaction_list_debug", "Transaction " + transaction.getRefNumber() + " To " + transaction.getDescription());
            }

            return transactionList;
        }
        return new ArrayList<>();
    }

    private List<JournalLine> getJournalines(Transaction transaction){


        List<JournalLine> journalLineList = appDatabase.journalLineDAO().getPendingJournalLines(transaction.getId());

        return journalLineList;
    }

}
