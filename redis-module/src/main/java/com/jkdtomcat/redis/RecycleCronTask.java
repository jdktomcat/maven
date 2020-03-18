package com.jkdtomcat.redis;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 类描述：回收消息定时任务
 *
 * @author 11072131
 * @date 2020-03-2020/3/18 10:09
 */
@Component
public class RecycleCronTask {

    @Scheduled(cron = "0/1 * * * * ?")
    public void sc1()  {
        System.out.println(Thread.currentThread().getName() + " | sc1 " + System.currentTimeMillis());
    }
}
