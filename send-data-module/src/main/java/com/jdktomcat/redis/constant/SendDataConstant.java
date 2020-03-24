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
    public static final int LIST_NUM = 25;

    /**
     * 队列名称前缀
     */
    public static final String SEND_CLICK_LIST_NAME = "sent:click:data:list";

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
}
