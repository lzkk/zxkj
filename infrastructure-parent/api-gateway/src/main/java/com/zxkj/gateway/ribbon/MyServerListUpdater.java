package com.zxkj.gateway.ribbon;

import com.netflix.loadbalancer.PollingServerListUpdater;
import org.springframework.stereotype.Component;

/**
 * @author ：yuhui
 * @date ：Created in 2022/1/6 16:10
 */
@Component
public class MyServerListUpdater extends PollingServerListUpdater {

    private UpdateAction updateAction;

    @Override
    public synchronized void start(UpdateAction updateAction) {
        this.updateAction = updateAction;
        super.start(updateAction);

    }

    public UpdateAction getUpdateAction() {
        return updateAction;
    }

}
