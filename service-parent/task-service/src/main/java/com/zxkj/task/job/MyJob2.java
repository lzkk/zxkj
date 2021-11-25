package com.zxkj.task.job;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.zxkj.common.web.JsonUtil;
import com.zxkj.common.web.RespResult;
import com.zxkj.goods.feign.SkuFeign;
import com.zxkj.goods.model.Sku;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MyJob2 implements SimpleJob {

    @Autowired
    private SkuFeign skuFeign;

    @Override
    public void execute(ShardingContext shardingContext) {
        RespResult<Sku> respResult1 = skuFeign.one("1318596430360813570");
        log.info("1--" + JsonUtil.toJsonString(respResult1));
    }
}

