package com.example.expenseflow.Entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.example.expenseflow.Enum.ConversionStatus;
import com.example.expenseflow.Enum.SmsType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity(
        foreignKeys = @ForeignKey(
                entity = RawSms.class,
                parentColumns = "id",
                childColumns = "rawSmsId",
                onDelete = ForeignKey.SET_NULL
        )
)
public class ParsedSms {
    @PrimaryKey(autoGenerate = true)
    private Long id;

    private long rawSmsId;

    private String smsType;

    private String transactionCode;

    private Long transactionTime;

    private String transactionAmount;

    private String transactionFee;

    private String counterparty;

    private String conversionStatus;

    private Long createdAt;

    private Long parsedAt;

    public String getSmsType() {
        return smsType;
    }

    public void setSmsType(String smsType) {
        this.smsType = smsType;
    }

    public String getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(String transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getTransactionFee() {
        return transactionFee;
    }

    public void setTransactionFee(String transactionFee) {
        this.transactionFee = transactionFee;
    }

    public String getConversionStatus() {
        return conversionStatus;
    }

    public void setConversionStatus(String conversionStatus) {
        this.conversionStatus = conversionStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getRawSmsId() {
        return rawSmsId;
    }

    public void setRawSmsId(long rawSmsId) {
        this.rawSmsId = rawSmsId;
    }


    public String getTransactionCode() {
        return transactionCode;
    }

    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }

    public Long getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(Long transactionTime) {
        this.transactionTime = transactionTime;
    }

    public String getCounterparty() {
        return counterparty;
    }

    public void setCounterparty(String counterparty) {
        this.counterparty = counterparty;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getParsedAt() {
        return parsedAt;
    }

    public void setParsedAt(Long parsedAt) {
        this.parsedAt = parsedAt;
    }

}
