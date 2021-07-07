package com.zxkj.page.controller;

import com.zxkj.common.web.RespResult;
import com.zxkj.page.feign.PageFeign;
import com.zxkj.page.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/*****
 * @Author:
 * @Description:
 ****/
@RestController
public class PageController implements PageFeign {

    @Autowired
    private PageService pageService;

    /****
     * 生成静态页
     */
    public RespResult html(@PathVariable(value = "id") String id) throws Exception {
        pageService.html(id);
        return RespResult.ok();
    }
}
