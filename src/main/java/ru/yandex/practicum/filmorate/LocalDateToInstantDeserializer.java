package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

public class LocalDateToInstantDeserializer extends JsonDeserializer<Instant> {

    @Override
    public Instant deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        String dateString = parser.getText();
        LocalDate localDate = LocalDate.parse(dateString); // Используем формат по умолчанию yyyy-MM-dd
        return localDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
    }
}