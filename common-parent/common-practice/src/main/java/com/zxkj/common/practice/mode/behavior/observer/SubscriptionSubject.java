package com.zxkj.common.practice.mode.behavior.observer;

import java.util.ArrayList;
import java.util.List;

public class SubscriptionSubject implements Subject {
    //储存订阅公众号的微信用户
    private List<Observer> wxUserList = new ArrayList<Observer>();

    @Override
    public void attach(Observer observer) {
        wxUserList.add(observer);
    }

    @Override
    public void detach(Observer observer) {
        wxUserList.remove(observer);
    }

    @Override
    public void notify(String message) {
        for (Observer observer : wxUserList) {
            observer.update(message);
        }
    }
}