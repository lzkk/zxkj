package com.zxkj.common.practice.mode.behavior.observer;

public class Client {
    public static void main(String[] args) {
        SubscriptionSubject mSubscriptionSubject = new SubscriptionSubject();
        //订阅公众号
        mSubscriptionSubject.attach(new WeixinUser("杨影枫"));
        mSubscriptionSubject.attach(new WeixinUser("月眉儿"));
        mSubscriptionSubject.attach(new WeixinUser("紫轩"));
        //公众号更新发出消息给订阅的微信用户
        mSubscriptionSubject.notify("刘望舒的专栏更新了");
    }
}
