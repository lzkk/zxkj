package com.zxkj.order.mq;

import com.alibaba.rocketmq.client.producer.LocalTransactionExecuter;
import com.alibaba.rocketmq.client.producer.LocalTransactionState;
import com.alibaba.rocketmq.common.message.Message;
import org.springframework.stereotype.Component;

@Component
public class LocalTransactionRefund implements LocalTransactionExecuter {

    public LocalTransactionState executeLocalTransactionBranch(Message message, Object o) {
        System.out.println("msg = " + new String(message.getBody()));
        System.out.println("o = " + o);
        String tag = message.getTags();
        if (tag.equals("Transaction3")) {
            //这里有一个分阶段提交任务的概念
            System.out.println("这里处理业务逻辑，比如操作数据库，失败情况下进行ROLLBACK");

            return LocalTransactionState.ROLLBACK_MESSAGE;
        }
        return LocalTransactionState.COMMIT_MESSAGE;
//        return LocalTransactionState.ROLLBACK_MESSAGE;
//        return LocalTransactionState.COMMIT_MESSAGE.UNKNOW;
    }
}