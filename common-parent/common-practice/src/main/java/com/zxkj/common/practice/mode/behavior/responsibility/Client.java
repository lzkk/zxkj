package com.zxkj.common.practice.mode.behavior.responsibility;

public class Client {

    public static void main(String[] args) {
        Handler handler = new Handler.Builder()
                .addBuilder(new FirstHandler())
                .addBuilder(new SecondHandler())
                .addBuilder(new ThirdHandler())
                .build();
        for (int k = 0; k < 5; k++) {
            handler.doWorkOnce(Handler.Request.build(k + ""));
        }
    }
}