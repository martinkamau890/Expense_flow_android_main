package com.example.expenseflow.Converter;

import androidx.room.TypeConverter;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class BigDecimalConverter {
    @TypeConverter
    public static String fromBigDecimal(BigDecimal value) {
        if (value == null) {
            return null;
        }

        NumberFormat formatter = NumberFormat.getNumberInstance(Locale.UK);
        formatter.setMinimumFractionDigits(2);
        formatter.setMaximumFractionDigits(2);

        return "Ksh " + formatter.format(value);
    }

    @TypeConverter
    public static BigDecimal toBigDecimal(String value) {
        return value == null ? null : new BigDecimal(value);
    }
}
