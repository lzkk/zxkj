package com.zxkj.common.practice.mode.behavior.responsibility;

/**
 * @author yuh
 */
public class ThirdHandler<String> extends Handler<String> {

    @Override
    protected boolean filter(Request<String> request) {
        String data = request.getData();
        if ("2".equals(data)) {
            return true;
        }
        return false;
    }

    @Override
    protected void process(Request<String> request) {
        System.out.println("third_" + request);
    }
}