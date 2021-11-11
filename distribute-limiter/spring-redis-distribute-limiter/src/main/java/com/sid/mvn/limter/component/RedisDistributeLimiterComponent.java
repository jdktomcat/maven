package com.sid.mvn.limter.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.*;

/**
 * 类概述：Redis分布式限流器（基于令牌桶思想）
 *
 * @author tangqi
 * @date 2021-11-10
 */
@Slf4j
@Component
public class RedisDistributeLimiterComponent {

    /**
     * 脚本文件
     */
    private static final String SCRIPT_LUA = "RedLimiter.lua";

    /**
     * 亚当提交QPS限制KEY
     */
    private static final String ADAM_SUBMIT_KEY = "amap:mp:shop:adam:submit:qps:limiter";

    /**
     * 存储令牌
     */
    private static final String STORED_PERMITS = "storedPermits";

    /**
     * 最大令牌
     */
    private static final String MAX_PERMITS = "maxPermits";

    /**
     * 正常可用时间
     */
    private static final String STABLE_INTERVAL_MICROS = "stableIntervalMicros";

    /**
     * 下一个可用时长
     */
    private static final String NEXT_FREE_TICKET_MICROS = "nextFreeTicketMicros";

    /**
     * jedis客户端池
     */
    @Autowired
    private JedisPool jedisPool;

    /**
     * 脚本标识
     */
    private String sha;

    /**
     * 初始化
     */
    @PostConstruct
    private void init() {
        this.getClass().getResource(SCRIPT_LUA);
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(SCRIPT_LUA);
        Objects.requireNonNull(is);
        StringBuilder builder = new StringBuilder();
        int qps = 10;
        try (Jedis jedis = jedisPool.getResource(); BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            Map<String, String> property = new HashMap<>();
            property.put(STORED_PERMITS, Integer.toString(qps));
            property.put(MAX_PERMITS, Integer.toString(qps));
            property.put(STABLE_INTERVAL_MICROS, TimeUnit.SECONDS.toMicros(1L) / qps + "");
            property.put(NEXT_FREE_TICKET_MICROS, "0");
            jedis.hmset(ADAM_SUBMIT_KEY, property);
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append("\n");
            }
            this.sha = jedis.scriptLoad(builder.toString());
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
    }

    /**
     * 阻塞式获取令牌
     *
     * @param qps 限流
     * @return 时长
     */
    public double acquire(int qps) {
        long nowMicros = MILLISECONDS.toMicros(System.currentTimeMillis());
        long waitMicros;
        try (Jedis jedis = jedisPool.getResource()) {
            waitMicros = (long) jedis.evalsha(sha, 1, ADAM_SUBMIT_KEY, "acquire", qps + "", nowMicros + "");
        }
        double wait = 1.0 * waitMicros / SECONDS.toMicros(1L);
        if (waitMicros > 0) {
            sleepUninterruptedly(waitMicros);
        }
        return wait;
    }

    /**
     * 休眠时长
     *
     * @param sleepFor 时长
     */
    private void sleepUninterruptedly(long sleepFor) {
        boolean interrupted = false;
        try {
            long remainingNanos = TimeUnit.MICROSECONDS.toNanos(sleepFor);
            long end = System.nanoTime() + remainingNanos;
            while (true) {
                try {
                    // TimeUnit.sleep() treats negative timeouts just like zero.
                    NANOSECONDS.sleep(remainingNanos);
                    return;
                } catch (InterruptedException e) {
                    interrupted = true;
                    remainingNanos = end - System.nanoTime();
                }
            }
        } finally {
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }
}



