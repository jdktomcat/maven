package com.jdktomcat.pack.redis.lettuce.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * lettuce配置类
 *
 * @author: 汤旗
 * @date: 2020-05-09 15:41
 **/
@Data
@ConfigurationProperties(prefix = "lettuce")
public class LettuceProperties {

    private LettuceSingleProperties single;

    private LettuceReplicaProperties replica;

    private LettuceSentinelProperties sentinel;

    private LettuceClusterProperties cluster;
}
