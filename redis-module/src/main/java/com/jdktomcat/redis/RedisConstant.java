package com.jdktomcat.redis;

/**
 * 类描述：redis常量
 *
 * @author 11072131
 * @date 2020-03-2020/3/17 11:14
 */
public class RedisConstant {

    /**
     * 队列个数
     */
    public static final int LIST_NUM = 5;

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
}
