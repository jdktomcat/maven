package com.jdktomcat.redis;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisCluster;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * 类描述：回收消息定时任务
 *
 * @author 11072131
 * @date 2020-03-2020/3/18 10:09
 */
@Component
public class RecycleCronTask {

    /**
     * redis客户端
     */
    @Autowired(required = false)
    private JedisCluster jedisCluster;

    @Scheduled(cron = "0/5 * * * * ?")
    public void recycle() {
        long startTime = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
        System.out.println("定时回收任务开始：" + simpleDateFormat.format(new Date()));
        long maxExistTime = 10000L;
        for (int i = 0; i < RedisConstant.LIST_NUM; i++) {
            String listName = RedisConstant.SEND_CLICK_LIST_NAME + ":" + i;
            String bakListName = String.format(RedisConstant.BAK_LIST_PATTERN, listName);
            long size = jedisCluster.llen(bakListName);
            System.out.println(String.format("队列：%s 备份队列：%s 长度：%d", listName, bakListName, size));
            ArrayList<String> recycleList = new ArrayList<>();
            for (long index = 0L; index < size; index++) {
                String member = jedisCluster.lindex(bakListName, index);
                String[] paramArray = member.split("#");
                if (paramArray.length == 4) {
                    long createTime = Long.parseLong(paramArray[3]);
                    if (System.currentTimeMillis() - createTime >= maxExistTime) {
                        recycleList.add(paramArray[0] + "#" + paramArray[1] + "#" + paramArray[2]);
                        jedisCluster.lrem(bakListName, 1, member);
                    }
                } else {
                    jedisCluster.lrem(bakListName, 1, member);
                }
            }
            if (CollectionUtils.isNotEmpty(recycleList)) {
                jedisCluster.lpush(listName, (String[]) recycleList.toArray());
            }
        }
        System.out.println("定时回收任务结束：" + simpleDateFormat.format(new Date()) + "耗时：" + (System.currentTimeMillis() - startTime) + "ms");
    }
}
