package com.jdktomcat.redis.zk;

import com.jdktomcat.redis.constant.SendDataConstant;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 类描述：zk客户端分布式状态类
 *
 * @author 11072131
 * @date 2020-03-2020/3/24 15:05
 */
@Component
public class ZkCuratorDistributedState {
    /**
     * 日志
     */
    private static Logger logger = Logger.getLogger(ZkCuratorDistributedState.class);

    /**
     * 发送开关
     */
    private volatile boolean sendOpenFlg = false;

    /**
     * 回收定时任务开关
     */
    private volatile boolean recycleTaskOpenFlg = false;

    /**
     * 消费kafka消息开关
     */
    private volatile boolean customMessageOpenFlg = false;

    /**
     * zk连接客户端
     */
    @Autowired
    private CuratorFramework curatorFramework;

    /**
     * 是否开启发送开关
     *
     * @return 是：true 否：false
     */
    public boolean isOpenSend() {
        return sendOpenFlg;
    }

    /**
     * 是否开通回收定时任务
     *
     * @return 是：true 否：false
     */
    public boolean isOpenRecycleTask() {
        return recycleTaskOpenFlg;
    }

    /**
     * 是否开通消费kafka消息
     *
     * @return 是：true 否：false
     */
    public boolean isOpenCustomMessage() {
        return customMessageOpenFlg;
    }

    /**
     * 初始化监听
     */
    @PostConstruct
    public void initMethod() {
        try {
            //检查节点是否存在，没有则创建
            boolean isSendNodeExist = curatorFramework.checkExists().forPath(SendDataConstant.MESSAGE_SEND_DATA_OPEN_FLG_NODE) != null;
            if (!isSendNodeExist) {
                curatorFramework.create().creatingParentsIfNeeded().forPath(SendDataConstant.MESSAGE_SEND_DATA_OPEN_FLG_NODE, "false".getBytes());
            }
            NodeCache sendOpenFlgNodeCache = new NodeCache(curatorFramework, SendDataConstant.MESSAGE_SEND_DATA_OPEN_FLG_NODE, false);
            NodeCacheListener sendOpenFlgNodeCacheListener = () -> {
                ChildData childData = sendOpenFlgNodeCache.getCurrentData();
                logger.info(String.format("ZNode消息发送节点状态改变,path=%s,data=%s,stat=%s", childData.getPath(), new String(childData.getData(), "Utf-8"), childData.getStat()));
            };
            sendOpenFlgNodeCache.getListenable().addListener(sendOpenFlgNodeCacheListener);
            sendOpenFlgNodeCache.start();

            //检查节点是否存在，没有则创建
            boolean isRecycleTaskNodeExist = curatorFramework.checkExists().forPath(SendDataConstant.MESSAGE_RECYCLE_TASK_OPEN_FLG_NODE) != null;
            if (!isRecycleTaskNodeExist) {
                curatorFramework.create().creatingParentsIfNeeded().forPath(SendDataConstant.MESSAGE_RECYCLE_TASK_OPEN_FLG_NODE, "false".getBytes());
            }
            NodeCache recycleTaskOpenFlgNodeCache = new NodeCache(curatorFramework, SendDataConstant.MESSAGE_RECYCLE_TASK_OPEN_FLG_NODE, false);
            NodeCacheListener recycleTaskOpenFlgNodeCacheListener = () -> {
                ChildData childData = recycleTaskOpenFlgNodeCache.getCurrentData();
                logger.info(String.format("ZNode消息回收定时任务节点状态改变,path=%s,data=%s,stat=%s", childData.getPath(), new String(childData.getData(), "Utf-8"), childData.getStat()));
            };
            recycleTaskOpenFlgNodeCache.getListenable().addListener(recycleTaskOpenFlgNodeCacheListener);
            recycleTaskOpenFlgNodeCache.start();

            //检查节点是否存在，没有则创建
            boolean isCustomMessageNodeExist = curatorFramework.checkExists().forPath(SendDataConstant.MESSAGE_CUSTOM_OPEN_FLG_NODE) != null;
            if (!isCustomMessageNodeExist) {
                curatorFramework.create().creatingParentsIfNeeded().forPath(SendDataConstant.MESSAGE_CUSTOM_OPEN_FLG_NODE, "false".getBytes());
            }
            NodeCache customMessageOpenFlgNodeCache = new NodeCache(curatorFramework, SendDataConstant.MESSAGE_CUSTOM_OPEN_FLG_NODE, false);
            NodeCacheListener customMessageOpenFlgNodeCacheListener = () -> {
                ChildData childData = customMessageOpenFlgNodeCache.getCurrentData();
                logger.info(String.format("ZNode消息消费节点状态改变,path=%s,data=%s,stat=%s", childData.getPath(), new String(childData.getData(), "Utf-8"), childData.getStat()));
            };
            customMessageOpenFlgNodeCache.getListenable().addListener(customMessageOpenFlgNodeCacheListener);
            customMessageOpenFlgNodeCache.start();
        } catch (Exception ex) {
            logger.error("创建NodeCache监听失败!", ex);
        }
    }
}
