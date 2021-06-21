package com.zxkj.feign.decoder;

import feign.FeignException;
import feign.Response;
import feign.codec.DecodeException;
import feign.codec.Decoder;
import feign.optionals.OptionalDecoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * 处理业务异常Decoder
 *
 * @author yuhui
 */
public class BusinessDecoder implements Decoder {
    private Decoder delegate;

    public BusinessDecoder(ObjectFactory<HttpMessageConverters> messageConverters) {
        delegate = new OptionalDecoder(new ResponseEntityDecoder(new SpringDecoder(messageConverters)));
    }

    @Override
    public Object decode(Response response, Type type) throws IOException, DecodeException, FeignException {
        Object obj = delegate.decode(response, type);
        return obj;
    }
}
