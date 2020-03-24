package com.jdktomcat.redis.task;

import com.jdktomcat.redis.constant.SendDataConstant;
import com.jdktomcat.redis.zk.ZkCuratorDistributedState;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
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
     * 日志
     */
    private static final Logger logger = Logger.getLogger(RecycleCronTask.class);

    /**
     * redis客户端
     */
    @Autowired
    private JedisCluster jedisCluster;

    /**
     * 分布式配置
     */
    @Autowired
    private ZkCuratorDistributedState zkCuratorDistributedState;

    @Scheduled(cron = "0/5 * * * * ?")
    public void recycle() {
        if (!zkCuratorDistributedState.isOpenRecycleTask()) {
            return;
        }
        long startTime = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
        logger.info("定时回收任务开始：" + simpleDateFormat.format(new Date()));
        long maxExistTime = 1000L;
        int handleCount = 25;
        for (int i = 0; i < SendDataConstant.LIST_NUM; i++) {
            if (acquireLock(i)) {
                String listName = SendDataConstant.SEND_CLICK_LIST_NAME + ":" + i;
                String bakListName = String.format(SendDataConstant.BAK_LIST_PATTERN, listName);
                long size = jedisCluster.llen(bakListName);
                logger.info(String.format("队列：%s 备份队列：%s 长度：%d", listName, bakListName, size));
                ArrayList<String> recycleList = new ArrayList<>();
                for (long index = 1L; index <= handleCount; index++) {
                    String member = jedisCluster.lindex(bakListName, -index);
                    String[] paramArray = member.split("||");
                    if (paramArray.length == 4) {
                        long createTime = Long.parseLong(paramArray[2]);
                        if (System.currentTimeMillis() - createTime >= maxExistTime) {
                            recycleList.add(String.format("%s||%s||%s||%s", paramArray[0], paramArray[1], System.currentTimeMillis(), paramArray[3]));
                            jedisCluster.lrem(bakListName, -1, member);
                        }
                    } else {
                        jedisCluster.lrem(bakListName, -1, member);
                    }
                }
                if (CollectionUtils.isNotEmpty(recycleList)) {
                    jedisCluster.rpush(listName, recycleList.toArray(new String[recycleList.size()]));
                }
                releaseLock(i);
            }
        }
        logger.info("定时回收任务结束：" + simpleDateFormat.format(new Date()) + "耗时：" + (System.currentTimeMillis() - startTime) + "ms");
    }

    /**
     * 获取分布式锁
     *
     * @param index 索引
     * @return 成功：true 失败：false
     */
    private boolean acquireLock(Integer index) {
        return "ok".equalsIgnoreCase(jedisCluster.set(SendDataConstant.RECYCLE_BAK_LIST_MUTEX + index, "1", "NX", "ex", 10000));
    }

    /**
     * 释放分布式锁
     *
     * @param index 索引
     */
    private void releaseLock(Integer index) {
        jedisCluster.del(SendDataConstant.RECYCLE_BAK_LIST_MUTEX + index);
    }
}
