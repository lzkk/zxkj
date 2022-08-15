package com.zxkj.goods.task;

import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.zxkj.common.web.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
public class XxlJobTestHandler {

    /**
     * 1、简单任务示例（Bean模式）
     */
    @XxlJob("firstJobHandler")
    public void firstJobHandler() throws Exception {
        log.info("--firstJobHandler111--");
        Thread.sleep(10000);
    }

    /**
     * 1、简单任务示例（Bean模式）
     */
    @XxlJob("firstJobHandler2")
    public void firstJobHandler2() throws Exception {
        log.info("--firstJobHandler222--");
    }

    /**
     * 2、分片广播任务
     */
    @XxlJob("shardingJobHandler")
    public void shardingJobHandler() throws Exception {
        log.info("--shardingJobHandler--");
        // 分片参数
        String jobParam = XxlJobHelper.getJobParam();
        int shardIndex = XxlJobHelper.getShardIndex();
        int shardTotal = XxlJobHelper.getShardTotal();

        XxlJobHelper.log("分片参数：当前分片序号 = {}, 总分片数 = {}", shardIndex, shardTotal);

        String[] paramArray = jobParam.split("-");
        if (paramArray.length == 1) {
            log.info("当前分片:{},分得值:{}", shardIndex, jobParam);
            return;
        }
        int begin = Integer.parseInt(paramArray[0]);
        int end = Integer.parseInt(paramArray[1]);
        if (begin > end) {
            log.info("当前分片:{},错误参数值:{}", shardIndex, jobParam);
            return;
        }
        Map<Integer, List<Integer>> dataMap = new HashMap<>();
        for (int k = begin; k <= end; k++) {
            if (k % shardTotal == shardIndex) {
                if (!dataMap.containsKey(shardIndex)) {
                    List<Integer> dataList = new ArrayList<>();
                    dataList.add(k);
                    dataMap.put(shardIndex, dataList);
                } else {
                    List<Integer> dataList = dataMap.get(shardIndex);
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

