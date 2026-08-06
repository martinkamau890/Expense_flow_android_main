package com.example.expenseflow.Converter;

import androidx.room.TypeConverter;

import com.example.expenseflow.Enum.SmsType;

public class SmsTypeConverter {
    @TypeConverter
    public static String fromSmsType(SmsType type) {
        return type == null ? null : type.name();
    }

    @TypeConverter
    public static SmsType toSmsType(String value) {
        return value == null ? null : SmsType.valueOf(value);
    }
}
