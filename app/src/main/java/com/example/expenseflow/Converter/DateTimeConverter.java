package com.example.expenseflow.Converter;

import androidx.room.TypeConverter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class DateTimeConverter {

    @TypeConverter
    public static Long fromLocalDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }

        return dateTime.atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();
    }

    @TypeConverter
    public static LocalDateTime toLocalDateTime(Long value) {
        if (value == null) {
            return null;
        }

        return LocalDateTime.ofInstant(
                Instant.ofEpochMilli(value),
                ZoneId.systemDefault()
        );
    }

    @TypeConverter
    public static Long stringToLong(String dateTime) {
        if (dateTime == null || dateTime.isEmpty()) {
            return null;
        }

        LocalDateTime localDateTime = LocalDateTime.parse(dateTime);

        return localDateTime
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();
    }
}
