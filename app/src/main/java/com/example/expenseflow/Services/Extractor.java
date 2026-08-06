package com.example.expenseflow.Services;

import com.example.expenseflow.Converter.DateTimeConverter;
import com.example.expenseflow.Entity.RawSms;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Extractor {

    public String getTransactionCode(RawSms rawSms){
        Pattern codePattern = Pattern.compile("^([A-Z0-9]+)\\s+Confirmed");
        Matcher matcher = codePattern.matcher(rawSms.getBody());
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }

    public Long getTransactionTime(RawSms rawSms) {

        Pattern pattern = Pattern.compile(
                "on\\s(\\d{1,2}/\\d{1,2}/\\d{2}\\sat\\s\\d{1,2}:\\d{2}\\s(?:AM|PM))"
        );

        Matcher matcher = pattern.matcher(rawSms.getBody());

        if (matcher.find()) {
            String dateTime = matcher.group(1);

            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern("d/M/yy 'at' h:mm a");

            return DateTimeConverter.fromLocalDateTime(LocalDateTime.parse(dateTime, formatter));
        }
        return null;
    }

    public String getTransactionAmount(RawSms rawSms) {

        String sms = rawSms.getBody();

        Pattern amountParser = Pattern.compile(
                "(?:Ksh\\s*([0-9,]+\\.[0-9]{2})\\s+(?:sent|paid)\\s+to|You\\s+bought\\s+Ksh\\s*([0-9,]+\\.[0-9]{2}))",
                Pattern.CASE_INSENSITIVE
        );

        Matcher matcher = amountParser.matcher(sms);

        if (matcher.find()) {

            String amount = matcher.group(1);

            if (amount == null) {
                amount = matcher.group(2);
            }

            return amount.replace(",", "");
        }

        return "0.00";
    }

    public String getTransactionFee(RawSms rawSms) {

        Pattern pattern = Pattern.compile(
                "Transaction cost,\\s+Ksh([\\d,]+\\.\\d{2})",
                Pattern.CASE_INSENSITIVE
        );

        Matcher matcher = pattern.matcher(rawSms.getBody());

        if (matcher.find()) {

            return matcher.group(1).replace(",", "");

        }

        return "0.00";
    }

    public String getCounterParty(RawSms rawSms) {

        Pattern pattern = Pattern.compile(
                "(?:(?:sent|paid)\\s+to\\s+(.+?)\\s+on|You\\s+bought\\s+Ksh\\s*[0-9,]+\\.[0-9]{2}\\s+of\\s+airtime\\s+for\\s+(.+?)\\s+on)",
                Pattern.CASE_INSENSITIVE

        );

        Matcher matcher = pattern.matcher(rawSms.getBody());

        if (matcher.find()) {

            if (matcher.group(1) != null) {
                return matcher.group(1).trim();
            }

            return matcher.group(2).trim();
        }

        return "";
    }

    public BigDecimal getMpesaBalance(RawSms rawSms){

        Pattern mpesaPattern =  Pattern.compile("(?:New|Your)\\s+M-PESA\\s+balance\\s+is\\s+Ksh?\\s?([\\d,]+\\.\\d{2})", Pattern.CASE_INSENSITIVE);

        Matcher mpesaMatcher = mpesaPattern.matcher(rawSms.getBody());

        Pattern fulizaPattern =  Pattern.compile( "Total\\s+Fuliza\\s+M-PESA\\s+outstanding\\s+amount\\s+is\\s+Ksh\\s?([\\d,]+\\.\\d{2})", Pattern.CASE_INSENSITIVE);

        Matcher fulizaMatcher =  fulizaPattern.matcher(rawSms.getBody());

        if (mpesaMatcher.find()){
            String amount =  mpesaMatcher.group(1);
            return new BigDecimal(amount.replace(",", ""));
        }
        if (fulizaMatcher.find()){
            String amount = fulizaMatcher.group(1);

            return new BigDecimal(amount.replace(",", ""));
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal getFulizaBalance(RawSms sms){

        Pattern fulizaPattern =  Pattern.compile( "Total\\s+Fuliza\\s+M-PESA\\s+outstanding\\s+amount\\s+is\\s+Ksh\\s?([\\d,]+\\.\\d{2})", Pattern.CASE_INSENSITIVE);

        Matcher fulizaMatcher =  fulizaPattern.matcher(sms.getBody());

        if (fulizaMatcher.find()){
            String amount = fulizaMatcher.group(1);

            return new BigDecimal(amount.replace(",", ""));
        }
        return BigDecimal.ZERO;

    }

}
