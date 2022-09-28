package com.zxkj.goods.foreign.controller;

import com.zxkj.common.web.RespResult;
import com.zxkj.goods.condition.TbProductTranslationAddCondition;
import com.zxkj.goods.condition.TbProductTranslationQueryCondition;
import com.zxkj.goods.foreign.entity.TbProductTranslation;
import com.zxkj.goods.foreign.service.TbProductTranslationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author yuhui
 * @version 1.0
 * @desc 前端控制器
 * @date 2022-09-28 15:45:53
 */
@RestController
@RequestMapping("/tb-product-translation")
public class TbProductTranslationController {

    @Autowired
    private TbProductTranslationService bProductTranslationService;

    @PostMapping(value = "/productTranslation/add")
    public RespResult<Boolean> add(@RequestBody TbProductTranslationAddCondition condition){
        bProductTranslationService.add(condition);
        return RespResult.ok(true);
    }

    @PostMapping(value = "/productTranslation/queryList")
    public RespResult<List<TbProductTranslation>> queryList(@RequestBody TbProductTranslationQueryCondition condition) {
        List<TbProductTranslation> list = bProductTranslationService.selectList(condition);
        return RespResult.ok(list);
    }

}
