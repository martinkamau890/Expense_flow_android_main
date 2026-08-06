package com.example.expenseflow.Converter;

import androidx.room.TypeConverter;

import com.example.expenseflow.Enum.ConversionStatus;

public class ConversionStatusConverter {

    @TypeConverter
    public static String fromConversionStatus(ConversionStatus type) {
        return type == null ? null : type.name();
    }

    @TypeConverter
    public static ConversionStatus toConversionStatus(String value) {
        return value == null ? null : ConversionStatus.valueOf(value);
    }
}
