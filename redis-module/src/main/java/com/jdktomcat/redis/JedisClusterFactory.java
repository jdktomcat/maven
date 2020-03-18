package com.jdktomcat.redis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.StringUtils;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.util.HashSet;
import java.util.Set;

/**
 * 类描述：Redis集群工厂类
 *
 * @author 11072131
 * @date 2020-03-2020/3/13 16:30
 */
public class JedisClusterFactory implements FactoryBean<JedisCluster>, InitializingBean {

    /**
     * 配置数组分割符
     */
    private static final String SPLIT_NODE = ",";

    /**
     * 地址端口分割符
     */
    private static final String SPLIT_INFO = ":";

    /**
     * redis集群客户端
     */
    private JedisCluster jedisCluster;

    /**
     * 连接超时时间
     */
    private Integer connectionTimeout;

    /**
     * 读取超时时间
     */
    private Integer soTimeout;

    /**
     * 重试次数，在执行失败后，进行的重试次数，默认是5
     */
    private Integer maxRedirection;

    /**
     * 池状物配置
     */
    private GenericObjectPoolConfig genericObjectPoolConfig;

    /**
     * 集群节点
     * hostAndPorts， clusterAddress 二者设置一个即可
     */
    private Set<HostAndPort> hostAndPorts;

    /**
     * 集群节点 ip:port,ip:port...
     * hostAndPorts， clusterAddress 二者设置一个即可
     */
    private String clusterAddress;

    /**
     * redis连接密码，如果redis没有设置密码的话，该属性可以不设置或设置为字符串""
     */
    private String password;

    @Override
    public JedisCluster getObject() {
        return jedisCluster;
    }

    @Override
    public Class<? extends JedisCluster> getObjectType() {
        return (this.jedisCluster != null ? this.jedisCluster.getClass() : JedisCluster.class);
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        password = StringUtils.isEmpty(password) ? null : password;
        if (null == hostAndPorts) {
            hostAndPorts = parseHostAndPort();
        }
        jedisCluster = new JedisCluster(hostAndPorts, connectionTimeout, soTimeout, maxRedirection, password, genericObjectPoolConfig);
    }

    private Set<HostAndPort> parseHostAndPort() throws Exception {
        if (StringUtils.isEmpty(clusterAddress)) {
            throw new Exception("redis cluster 配置错误， hostAndPorts， clusterAddress 请至少配置一个。");
        }
        try {
            HashSet<HostAndPort> hostAndPorSet = new HashSet<>();
            String[] nodes = clusterAddress.split(SPLIT_NODE);
            if (null != nodes && nodes.length > 0) {
                for (String node : nodes) {
                    node = node.trim();
                    if (StringUtils.isEmpty(node)) {
                        continue;
                    }
                    String[] info = node.split(SPLIT_INFO);
                    HostAndPort hap = new HostAndPort(info[0], Integer.parseInt(info[1]));
                    hostAndPorSet.add(hap);
                }
            }
            return hostAndPorSet;
        } catch (Exception ex) {
            throw new Exception("解析 jedis 配置文件失败", ex);
        }
    }

    public void setConnectionTimeout(Integer connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public void setSoTimeout(Integer soTimeout) {
        this.soTimeout = soTimeout;
    }

    public void setMaxRedirection(int maxRedirection) {
        this.maxRedirection = maxRedirection;
    }

    public void setGenericObjectPoolConfig(GenericObjectPoolConfig genericObjectPoolConfig) {
        this.genericObjectPoolConfig = genericObjectPoolConfig;
    }

    public void setHostAndPorts(Set<HostAndPort> hostAndPorts) {
        this.hostAndPorts = hostAndPorts;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setClusterAddress(String clusterAddress) {
        this.clusterAddress = clusterAddress;
    }
}
