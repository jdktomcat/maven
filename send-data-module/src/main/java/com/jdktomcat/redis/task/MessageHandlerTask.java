package com.jdktomcat.redis.task;

import com.jdktomcat.redis.constant.SendDataConstant;
import com.jdktomcat.redis.util.HttpSendUtil;
import com.jdktomcat.redis.zk.ZkCuratorDistributedState;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import redis.clients.jedis.JedisCluster;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 类描述：消息处理任务
 *
 * @author 11072131
 * @date 2020-03-2020/3/12 18:14
 */
public class MessageHandlerTask implements Runnable {

    /**
     * 日志
     */
    private static Logger logger = Logger.getLogger("send");

    /**
     * 索引
     */
    private Integer index;

    /**
     * redis客户端
     */
    private JedisCluster jedisCluster;

    /**
     * 分布式配置
     */
    private ZkCuratorDistributedState zkCuratorDistributedState;

    /**
     * 构造器
     *
     * @param jedisCluster              redis集群客户端
     * @param zkCuratorDistributedState 状态配置
     * @param index                     索引
     */
    public MessageHandlerTask(JedisCluster jedisCluster, ZkCuratorDistributedState zkCuratorDistributedState, Integer index) {
        this.jedisCluster = jedisCluster;
        this.zkCuratorDistributedState = zkCuratorDistributedState;
        this.index = index;
    }

    @Override
    public void run() {
        if (jedisCluster != null && index != null) {
            String listName = SendDataConstant.SEND_CLICK_LIST_NAME + ":" + index;
            String bakListName = String.format(SendDataConstant.BAK_LIST_PATTERN, listName);

            while (true) {
                if (!zkCuratorDistributedState.isOpenSend()) {
                    continue;
                }
                int handleCount = 0;
                Long startTime = System.currentTimeMillis();
                Map<String, List<String>> dataMap = new HashedMap<>();
                Map<String, Integer> messageSendMap = new HashedMap<>();
                List<String> messageList = new ArrayList<>();
                while (handleCount < SendDataConstant.SEND_ITEM_MAX && (System.currentTimeMillis() - startTime < SendDataConstant.TIME_LIMIT_MAX)) {
                    String message = jedisCluster.rpoplpush(listName, bakListName);
                    if (StringUtils.isNotBlank(message)) {
                        logger.info(String.format("源队列：%s,备份队列：%s，消息：%s", listName, bakListName, message));
                        messageList.add(message);
                        String[] paramArray = message.split("\\|\\|");
                        if (paramArray.length == 4) {
                            String url = paramArray[0];
                            String infoJson = paramArray[1];
                            Integer sendCount = Integer.parseInt(paramArray[3]);
                            List<String> dataList = dataMap.get(url);
                            if (dataList == null) {
                                dataList = new ArrayList<>();
                            }
                            dataList.add(infoJson);
                            dataMap.put(url, dataList);
                            messageSendMap.put(infoJson, sendCount);
                        }
                        handleCount++;
                    }
                }
                Map<String, Boolean> messageSendStateMap = new HashedMap<>();
                for (Map.Entry<String, List<String>> entry : dataMap.entrySet()) {
                    messageSendStateMap.put(entry.getKey(), HttpSendUtil.send(entry.getKey(), entry.getValue()));
                }
                for (String message : messageList) {
                    logger.info(String.format("删除备份队列:%s中元素：%s 删除结果：%s", bakListName, message, jedisCluster.lrem(bakListName, -1, message)));
                }
                List<String> failDataList = new ArrayList<>();
                for (Map.Entry<String, Boolean> entry : messageSendStateMap.entrySet()) {
                    if (!entry.getValue()) {
                        failDataList.addAll(dataMap.get(entry.getKey()));
                    }
                }
                if (CollectionUtils.isNotEmpty(failDataList)) {
                    List<String> wasteDataList = new ArrayList<>();
                    List<String> recycleDataList = new ArrayList<>();
                    for (String message : failDataList) {
                        String[] paramArray = message.split("\\|\\|");
                        String url = paramArray[0];
                        String infoJson = paramArray[1];
                        Integer sendCount = Integer.parseInt(paramArray[3]);
                        if (sendCount + 1 > SendDataConstant.SEND_COUNT_MAX) {
                            wasteDataList.add(message);
                        } else {
                            recycleDataList.add(String.format("%s||%s||%s||%s", url, infoJson, System.currentTimeMillis(), (sendCount + 1)));
                        }
                    }
                    if (CollectionUtils.isNotEmpty(recycleDataList)) {
                        String[] memberArray = recycleDataList.toArray(new String[0]);
                        // 重发
                        logger.info(String.format("需要重新发送数据，队列名称：%s 元素：%s 队列长度：%s", listName, Arrays.toString(memberArray), jedisCluster.lpush(listName, memberArray)));
                    }
                    if (CollectionUtils.isNotEmpty(wasteDataList)) {
                        // 回收
                        logger.info(String.format("保存到数据库元素:%s", Arrays.toString(wasteDataList.toArray(new String[0]))));
                    }
                }
            }
        }
    }
}