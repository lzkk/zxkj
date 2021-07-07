package com.zxkj.order.mq.transaction;

import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

/**
 * 抽象事务监听类
 *
 * @author ：yuhui
 * @date ：Created in 2021/6/28 9:42
 */
public class MyTransaction2Listener implements TransactionListener {

    @Override
    public LocalTransactionState executeLocalTransaction(Message message, Object o) {
        System.out.println("------executeLocalTransaction2----");
        return LocalTransactionState.COMMIT_MESSAGE;
    }

    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt messageExt) {
        System.out.println("------checkLocalTransaction2----");
        return LocalTransactionState.COMMIT_MESSAGE;
    }
}
