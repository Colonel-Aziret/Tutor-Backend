package com.example.tutor.util;

import com.google.gson.*;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.time.LocalDate;

@Component
public class LocalDateAdapter implements JsonDeserializer<LocalDate>, JsonSerializer<LocalDate> {
    @Override
    public JsonElement serialize(LocalDate src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(DateUtils.formatToString(src, DateUtils.FORMAT_YYYY_MM_DD));
    }

    @Override
    public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        return DateUtils.parseString2LocalDate(json.getAsString(), DateUtils.FORMAT_YYYY_MM_DD);
    }
}
