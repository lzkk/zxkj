package com.zxkj.task.job;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.zxkj.common.web.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
public class MyJob3 implements SimpleJob {

    @Override
    public void execute(ShardingContext shardingContext) {
        int shardingTotalCount = shardingContext.getShardingTotalCount();
        String jobParameter = shardingContext.getJobParameter();
        if (jobParameter == null) {
            return;
        }
        int shardingItem = shardingContext.getShardingItem();
        String[] paramArray = jobParameter.split("-");
        if (paramArray.length == 1) {
            log.info("当前分片:{},分得值:{}", shardingItem, jobParameter);
            return;
        }
        int begin = Integer.parseInt(paramArray[0]);
        int end = Integer.parseInt(paramArray[1]);
        if (begin > end) {
            log.info("当前分片:{},错误参数值:{}", shardingItem, jobParameter);
            return;
        }
        Map<Integer, List<Integer>> dataMap = new HashMap<>();
        for (int k = begin; k <= end; k++) {
            if (k % shardingTotalCount == shardingItem) {
                if (!dataMap.containsKey(shardingItem)) {
                    List<Integer> dataList = new ArrayList<>();
                    dataList.add(k);
                    dataMap.put(shardingItem, dataList);
                } else {
                    List<Integer> dataList = dataMap.get(shardingItem);
                    dataList.add(k);
                }
            }
        }
        Iterator<Integer> it = dataMap.keySet().iterator();
        while (it.hasNext()) {
            Integer tmpKey = it.next();
            List<Integer> dataList = dataMap.get(tmpKey);
            log.info("当前分片:{},参数值:{}", tmpKey, JsonUtil.toJsonString(dataList));
        }
    }
}

