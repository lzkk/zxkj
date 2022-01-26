package com.zxkj.feign.encoder;

import feign.RequestTemplate;
import feign.codec.EncodeException;
import feign.codec.Encoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;

import java.lang.reflect.Type;

/**
 * 处理业务异常Encoder
 *
 * @author yuhui
 */
public class BusinessEncoder implements Encoder {
    private Encoder delegate;

    public BusinessEncoder(ObjectFactory<HttpMessageConverters> messageConverters) {
        delegate = new SpringEncoder(messageConverters);
    }

    @Override
    public void encode(Object o, Type type, RequestTemplate requestTemplate) throws EncodeException {
        delegate.encode(o,type,requestTemplate);
    }
}
