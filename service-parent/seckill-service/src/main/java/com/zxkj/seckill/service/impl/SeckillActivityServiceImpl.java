package com.zxkj.seckill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxkj.common.util.bean.BeanUtil;
import com.zxkj.seckill.entity.SeckillActivity;
import com.zxkj.seckill.mapper.SeckillActivityMapper;
import com.zxkj.seckill.service.SeckillActivityService;
import com.zxkj.seckill.vo.SeckillActivityVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yuhui
 * @version 1.0
 * @desc 服务实现
 * @date 2022-09-05 14:43:27
 */
@Service
@Slf4j
public class SeckillActivityServiceImpl extends ServiceImpl<SeckillActivityMapper, SeckillActivity> implements SeckillActivityService {

    /**
     * 批量新增重写
     *
     * @param param model集合
     * @return Boolean
     */
    @Override
    public Boolean saveBatch(List<SeckillActivity> param) {
        return this.getBaseMapper().insertAll(param);
    }

    /***
     * 有效活动列表
     * @return
     */
    @Override
    public List<SeckillActivityVo> validActivity() {
        return BeanUtil.copyList(this.getBaseMapper().validActivity(), SeckillActivityVo.class);
    }

}
