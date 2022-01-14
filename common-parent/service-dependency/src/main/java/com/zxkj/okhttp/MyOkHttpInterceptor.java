package com.zxkj.okhttp;

import com.alibaba.fastjson.JSON;
import okhttp3.Interceptor;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class MyOkHttpInterceptor implements Interceptor {
    private Logger logger = LoggerFactory.getLogger(MyOkHttpInterceptor.class);

    @Override
    public Response intercept(Chain chain) throws IOException {
        long now = System.currentTimeMillis();
        Response response = chain.proceed(chain.request());
        long spend = System.currentTimeMillis() - now;
        if (spend >= 50) {
            logger.info("okhttp request:{},response:{},spend:{}", chain.request().url().toString(), JSON.toJSONString(response.code()), spend);
        }
        return response;
    }
}