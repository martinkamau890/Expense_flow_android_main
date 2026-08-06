package com.example.expenseflow.Services;

import android.content.Context;
import android.util.Log;

import com.example.expenseflow.Converter.ConversionStatusConverter;
import com.example.expenseflow.DataBase.AppDatabase;
import com.example.expenseflow.Entity.ParsedSms;
import com.example.expenseflow.Entity.RawSms;
import com.example.expenseflow.Enum.ConversionStatus;
import com.example.expenseflow.Enum.SmsType;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ParseSmsService {

    public void parseSms(Context context){
        AppDatabase database = AppDatabase.getInstance(context);
        CreditSmsParser creditSmsParser = new CreditSmsParser();

        ExecutorService executorService = Executors.newSingleThreadExecutor();

        executorService.execute(() ->{

            List<RawSms> rawSmsList = database.rawSmsDAO().getPendingSms(ConversionStatusConverter.fromConversionStatus(ConversionStatus.PENDING));

            for (RawSms rawSms: rawSmsList){
                SmsType smsType = new SmsClassifier().classify(rawSms);

                if (smsType.equals(SmsType.CREDIT_SMS)){
                    long id = rawSms.getId();
                    String conversionStatus = ConversionStatusConverter.fromConversionStatus(ConversionStatus.PARSED);
                    database.parsedSmsDAO().insert(creditSmsParser.parseSms(rawSms));
                    database.rawSmsDAO().updateRawSms(conversionStatus, id);
                }
            }

            executorService.shutdown();
        });


    }
}
