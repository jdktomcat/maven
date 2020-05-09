package com.jdktomcat.pack.redis.lettuce.properties;

import lombok.Data;

/**
 * lettuce单点配置
 *
 * @author: 汤旗
 * @date: 2020-05-09 15:36
 **/
@Data
public class LettuceSingleProperties {

    private String host;

    private Integer port;

    private String password;
}
