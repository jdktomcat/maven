package com.jdktomcat.pack.redis.lettuce.properties;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * lettuce哨兵配置类
 *
 * @author: 汤旗
 * @date: 2020-05-09 15:39
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class LettuceSentinelProperties extends LettuceSingleProperties {
    private String masterId;
}
