package com.example.expenseflow.Services;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.expenseflow.Converter.ConversionStatusConverter;
import com.example.expenseflow.Converter.DateTimeConverter;
import com.example.expenseflow.DataBase.AppDatabase;
import com.example.expenseflow.Entity.RawSms;
import com.example.expenseflow.DTO.ExtractedMessageDTO;
import com.example.expenseflow.Enum.ConversionStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SaveExtractedSms {

    //read sms
    @RequiresApi(api = Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    public List<ExtractedMessageDTO> readSms(Context context) {

        List<ExtractedMessageDTO> extractedMessageDTOList = new ArrayList<>();
        TransactionTime transactionTime = new TransactionTime();

        Uri uri = Uri.parse("content://sms/inbox");

        ContextWrapper contextWrapper = new ContextWrapper(context);

        Cursor cursor = contextWrapper.getContentResolver().query(
                uri,
                null,
                "address = ? AND date BETWEEN ? AND ?",
                new String[]{"MPESA", String.valueOf(transactionTime.startTime), String.valueOf(transactionTime.currentTime)},
                "date DESC"
        );

        if(cursor != null){

            while(cursor.moveToNext()){

                long id = cursor.getLong(cursor.getColumnIndexOrThrow("_id"));

                String address =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow("address"));

                String body =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow("body"));

                long timeStamp = cursor.getLong(cursor.getColumnIndexOrThrow("date"));

                ExtractedMessageDTO messageDTO = new ExtractedMessageDTO(id, address, body, timeStamp, extractTransactionCode(body));

                extractedMessageDTOList.add(messageDTO);
            }

            cursor.close();
        }

        Log.d("SMS", "Found " + extractedMessageDTOList.size() + " SMS");

       /* for (ExtractedMessageDTO dto : extractedMessageDTOList) {
            Log.d("SMS",
                    dto.getId() + " | " +
                            dto.getDate() + " | " +
                            dto.getTransactionCode());
        }*/
        return extractedMessageDTOList;
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
            LocalDate date = LocalDate.now().minusDays(15);
            LocalDateTime localDateTime = date.atStartOfDay();
            this.startTime = DateTimeConverter.fromLocalDateTime(localDateTime);
        }

        public long getCurrentTime() {
            return currentTime;
        }

        public void setCurrentTime() {
            this.currentTime = DateTimeConverter.fromLocalDateTime(LocalDateTime.now());
        }
    }

    //save sms

    @RequiresApi(api = Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    public void saveExtractedSms(Context context){

        int savedData = 0;

        ExecutorService executorService = Executors.newSingleThreadExecutor();

        executorService.execute(() -> {
            List<ExtractedMessageDTO> dtoList = readSms(context);

            AppDatabase database = AppDatabase.getInstance(context);

            if (!dtoList.isEmpty()){
                for (ExtractedMessageDTO messageDTO: dtoList){
                    RawSms rawSms = new RawSms();

                    rawSms.setProviderSmsId(messageDTO.getId());
                    rawSms.setSender(messageDTO.getSender());
                    rawSms.setSmsDate(messageDTO.getDate());
                    rawSms.setBody(messageDTO.getMessage());
                    rawSms.setConversionStatus(ConversionStatusConverter.fromConversionStatus(ConversionStatus.PENDING));
                    rawSms.setTransactionCode(messageDTO.getTransactionCode());

                    try {
                        database.rawSmsDAO().insert(rawSms);
                       } catch (Exception e) {
                        Log.e("SMS", "Insert failed", e);
                    }
                }

            }
            executorService.shutdown();
        });


    }

    private static final Pattern TRANSACTION_CODE_PATTERN = Pattern.compile("^([A-Z0-9]+)\\s+Confirmed");

    public String extractTransactionCode(String smsBody) {
        if (smsBody == null || smsBody.isEmpty()) {
            return "";
        }

        Matcher matcher = TRANSACTION_CODE_PATTERN.matcher(smsBody);

        if (matcher.find()) {
            return matcher.group(1);
        }

        return "";
    }
}
