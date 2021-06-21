package com.zxkj.common.web;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JsonUtil {
    public static String jsonFromObject(Object object) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(SerializationFeature.INDENT_OUTPUT);
            return mapper.writeValueAsString(object);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T objectFromJson(String json, TypeReference<T> klass) {
        T object;
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            object = mapper.readValue(json, klass);
            return object;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T convertToTypeReferenceType(Object o, TypeReference<T> t) {
        return objectFromJson(jsonFromObject(o), t);
    }

    public static <T> T convertToTypeReferenceType(Object o, Class<T> clazz) {
        return objectFromJson(jsonFromObject(o), new TypeReference<T>() {
        });
    }
}
