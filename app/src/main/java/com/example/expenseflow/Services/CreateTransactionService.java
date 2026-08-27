package com.example.expenseflow.Services;

import android.content.Context;

import com.example.expenseflow.Converter.ConversionStatusConverter;
import com.example.expenseflow.Converter.DateTimeConverter;
import com.example.expenseflow.DataBase.AppDatabase;
import com.example.expenseflow.Entity.JournalLine;
import com.example.expenseflow.Entity.MpesaAssignmentRule;
import com.example.expenseflow.Entity.ParsedSms;
import com.example.expenseflow.Entity.Transaction;
import com.example.expenseflow.Enum.ConversionStatus;
import com.example.expenseflow.Enum.EntryType;
import com.example.expenseflow.Enum.SyncStatus;
import com.example.expenseflow.Enum.TransactionType;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CreateTransactionService {
    private Context context;
    private AppDatabase appDatabase = AppDatabase.getInstance(context);



    public CreateTransactionService(Context context) {
        this.context = context;
    }


    private long createTransactionLine(ParsedSms parsedSms, TransactionType transactionType){
        Transaction transaction = new Transaction();

        transaction.setTransactionDate(DateTimeConverter.toLocalDateTime(parsedSms.getTransactionTime()).toString());
        transaction.setTransactionType(transactionType);
        transaction.setDescription("Amount sent to " + parsedSms.getCounterparty());
        transaction.setRefNumber(parsedSms.getTransactionCode());
        transaction.setParsedStatus(SyncStatus.PENDING);
        transaction.setCurrencyCode(null);

       return  appDatabase.transactionDAO().insert(transaction);
    }

    private void createJournalLine(long transactionId, ParsedSms parsedSms, long debitAccount, long creditAccount){

        JournalLine debitJournalLine = new JournalLine();

        debitJournalLine.setTransactionId(transactionId);
        debitJournalLine.setAmount(parsedSms.getTransactionAmount());
        debitJournalLine.setAccountId(debitAccount);
        debitJournalLine.setEntryType(EntryType.DEBIT);

        appDatabase.journalLineDAO().insert(debitJournalLine);

        JournalLine creditJournalLine = new JournalLine();

        creditJournalLine.setTransactionId(transactionId);
        creditJournalLine.setAmount(parsedSms.getTransactionAmount());
        creditJournalLine.setEntryType(EntryType.CREDIT);
        creditJournalLine.setAccountId(creditAccount);

        appDatabase.journalLineDAO().insert(creditJournalLine);

    }
    @androidx.room.Transaction
    public boolean createTransaction(ParsedSms parsedSms, TransactionType transactionType, long debitAccount, long creditAccount){

        long id = createTransactionLine(parsedSms, transactionType);
        createJournalLine(id, parsedSms, debitAccount, creditAccount);
        appDatabase.parsedSmsDAO().updateConversionStatus(ConversionStatusConverter.fromConversionStatus(ConversionStatus.PARSED), parsedSms.getId());
        return true;
    }

    public void createTransactionBasedOnAssignmentRule(){
        List<MpesaAssignmentRule> mpesaAssignmentRuleList = appDatabase.mpesaAssignmentRuleDAO().getAll();
        for (MpesaAssignmentRule assignmentRule: mpesaAssignmentRuleList){
            String recipient = assignmentRule.getRecipient();
            String smsType = assignmentRule.getSmsType();

            List<ParsedSms> parsedSmsList = appDatabase.parsedSmsDAO().searchBasedOnMpesaAssignmentRule(recipient, smsType, ConversionStatusConverter.fromConversionStatus(ConversionStatus.PENDING));

            for (ParsedSms sms: parsedSmsList){
                createTransaction(sms, assignmentRule.getTransactionType(), assignmentRule.getDebitAccount(), assignmentRule.getCreditAccount());
            }
        }
    }

}
