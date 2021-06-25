package com.zxkj.common.kafka.support;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 帮助类
 */
public class KafkaMessageHelper {
    private static final Logger logger = LoggerFactory.getLogger(KafkaMessageHelper.class);

    private static final ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public static <T> String toJson(String busiKey, T busiObject) {
        try {
            BusiTransferObject<T> eto = new BusiTransferObject<>(busiKey, busiObject);
            return objectMapper.writeValueAsString(eto);
        } catch (JsonProcessingException e) {
            logger.error("KafkaMessageHelper->toJson", e);
        }
        return null;
    }

    public static <T> BusiTransferObject<T> toTransferObject(String json, Class<T> clazz) throws IOException {
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(BusiTransferObject.class, clazz);
        return objectMapper.readValue(json, javaType);
    }

    public static class BusiTransferObject<T> {
        private String busiKey;
        private T busiObject;

        public BusiTransferObject() {
        }

        public BusiTransferObject(String busiKey, T busiObject) {
            this.busiKey = busiKey;
            this.busiObject = busiObject;
        }

        public String getBusiKey() {
            return busiKey;
        }

        public void setBusiKey(String busiKey) {
            this.busiKey = busiKey;
        }

        public T getBusiObject() {
            return busiObject;
        }

        public void setBusiObject(T busiObject) {
            this.busiObject = busiObject;
        }
    }
}
