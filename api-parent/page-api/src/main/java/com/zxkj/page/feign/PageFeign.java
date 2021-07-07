package com.zxkj.page.feign;

import com.zxkj.common.constant.ServiceIdConstant;
import com.zxkj.common.web.RespResult;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/*****
 * @Author:
 * @Description:
 ****/
@FeignClient(value = ServiceIdConstant.PAGE_WEB_SERVICE, path = "/", fallbackFactory = PageFeignFallback.class)
public interface PageFeign {

    /***
     * 生成静态页
     * @param id
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/page/{id}")
    RespResult html(@PathVariable(value = "id") String id) throws Exception;
}


@Component
class PageFeignFallback implements PageFeign, FallbackFactory<PageFeign> {
    private static final Logger logger = LoggerFactory.getLogger(PageFeignFallback.class);
    private Throwable throwable;

    @Override
    public PageFeign create(Throwable throwable) {
        this.throwable = throwable;
        return this;
    }

    @Override
    public RespResult html(String acid) {
        logger.error("PageFeignFallback -> html错误信息：{}", throwable.getMessage());
        return RespResult.error(throwable.getMessage());
    }

}