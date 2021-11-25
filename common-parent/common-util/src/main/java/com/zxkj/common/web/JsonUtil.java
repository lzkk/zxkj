package com.zxkj.common.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class JsonUtil {

    private static final ObjectMapper mapper = new ObjectMapper();
    public static final String RESULT = "result";

    static {
        mapper.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {
            @Override
            public void serialize(Object arg0, JsonGenerator arg1, SerializerProvider arg2) throws IOException, JsonProcessingException {
                arg1.writeString("");
            }
        });
        // 写死成中国时区
        TimeZone china = TimeZone.getTimeZone("GMT+08:00");
        mapper.setTimeZone(china);
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static String toJsonString(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Map<String, Object> parseJsonMap(String json) {
        try {
            return mapper.readValue(json, new TypeReference<Map<String, Object>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T parseJsonObject(String json, Class<T> entityClass) {
        try {
            return mapper.readValue(json, entityClass);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> List<T> parseJsonArray(String json, Class<T> entityClass) {
        JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, new Class[]{entityClass});
        try {
            return mapper.readValue(json, javaType);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T parseObject(String json, Class<T> clazz) {
        if (json == null) {
            return null;
        }
        JSONObject jsonObject = JSON.parseObject(json);
        JSONObject data = jsonObject.getJSONObject(RESULT);
        if (data == null) {
            return null;
        }
        return JSON.parseObject(data.toJSONString(), clazz);
    }


    public static <T> List<T> parseArray(String json, Class<T> clazz) {
        if (json == null) {
            return null;
        }
        JSONObject jsonObject = JSON.parseObject(json);
        JSONArray data = jsonObject.getJSONArray(RESULT);
        if (data == null) {
            return null;
        }
        return JSON.parseArray(data.toJSONString(), clazz);
    }

}
