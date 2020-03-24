package com.jdktomcat.redis.constant;

/**
 * 类描述：redis常量
 *
 * @author 11072131
 * @date 2020-03-2020/3/17 11:14
 */
public class SendDataConstant {

    /**
     * 队列个数
     */
    public static final int LIST_NUM = 5;

    /**
     * 队列名称前缀
     */
    public static final String SEND_CLICK_LIST_NAME = "sent:click:message:2:queue";

    /**
     * 备份队列名称
     */
    public static final String BAK_LIST_PATTERN = "{%s}:BAK";

    /**
     * 互斥锁
     */
    public static final String RECYCLE_BAK_LIST_MUTEX = "recycle:bak:list:mutex:";

    /**
     * 列表容量
     */
    public static final int LIST_CAPACITY = 100;

    /**
     * 消息发送开关zk节点路径
     */
    public static final String MESSAGE_SEND_DATA_OPEN_FLG_NODE = "/message/send/open/flg/node";

    /**
     * 消息回收定时任务开启zk节点路径
     */
    public static final String MESSAGE_RECYCLE_TASK_OPEN_FLG_NODE = "/message/recycle/task/open/flg/node";

    /**
     * 消息消费开启zk节点路径
     */
    public static final String MESSAGE_CUSTOM_OPEN_FLG_NODE = "/message/custom/open/flg/node";

    /**
     * 发送最大数量
     */
    public static final Integer SEND_ITEM_MAX = 10;

    /**
     * 发送最大等待时长
     */
    public static final Long TIME_LIMIT_MAX = 1000L;

    /**
     * 发送次数阈值
     */
    public static final Integer SEND_COUNT_MAX = 3;

}
