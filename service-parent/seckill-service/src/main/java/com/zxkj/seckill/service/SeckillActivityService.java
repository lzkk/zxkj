package com.zxkj.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zxkj.seckill.entity.SeckillActivity;
import com.zxkj.seckill.vo.SeckillActivityVo;

import java.util.List;

/**
 * @desc  服务
 *
 * @author yuhui
 * @version 1.0
 * @date 2022-09-05 14:43:27
 */
public interface SeckillActivityService extends IService<SeckillActivity> {

    /**
     *  批量新增重写
     * @param param model集合
     * @return Boolean
     */
    Boolean saveBatch(List<SeckillActivity> param);


    List<SeckillActivityVo> validActivity();
}
