package com.zxkj.page.feign;

import com.zxkj.common.constant.ServiceIdConstant;
import com.zxkj.feign.fallback.CustomFallbackFactory;
import com.zxkj.common.web.RespResult;
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
class PageFeignFallback extends CustomFallbackFactory implements PageFeign {
    private static final Logger logger = LoggerFactory.getLogger(PageFeignFallback.class);

    @Override
    public RespResult html(String acid) {
        logger.error("PageFeignFallback -> html错误信息：{}", throwable.getMessage());
        return RespResult.error(throwable.getMessage());
    }

}