package com.zxkj.canal.listener;

import com.zxkj.seckill.model.SeckillActivity;
import org.springframework.stereotype.Component;
import top.javatool.canal.client.annotation.CanalTable;
import top.javatool.canal.client.handler.EntryHandler;

/*****
 * @Author:
 * @Description:
 ****/
@Component
@CanalTable(value = "seckill_activity")
public class SeckillActivityHandler implements EntryHandler<SeckillActivity> {

//    @Autowired
//    private DynamicTaskCreate dynamicTaskCreate;

    /****
     * 增加活动
     * @param seckillActivity
     */
    @Override
    public void insert(SeckillActivity seckillActivity) {
        //创建任务调度，活动结束的时候执行
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ss mm HH dd MM ? yyyy");
//        String cron = simpleDateFormat.format(seckillActivity.getEndTime());
//        System.out.println("cron:"+cron);
//        dynamicTaskCreate.create(seckillActivity.getId(), cron, 1, new DynamicJob(),seckillActivity.getId());
    }

    @Override
    public void update(SeckillActivity before, SeckillActivity after) {
        //创建任务调度，活动结束的时候执行
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ss mm HH dd MM ? yyyy");
//        String cron = simpleDateFormat.format(after.getEndTime());
//        System.out.println("cron:"+cron);
//        dynamicTaskCreate.create(after.getId(), cron, 1, new DynamicJob(),after.getId());
    }

    @Override
    public void delete(SeckillActivity seckillActivity) {

    }
}
