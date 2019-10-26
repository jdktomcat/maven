package com.vivo.jdk.pack.es.spring.config;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 类描述：ElasticSearch配置类
 *
 * @author 汤旗
 * @date 2019-10-26 15:15
 */
@Configuration
public class ElasticSearchConfig {

    /**
     * 集群名称
     */
    @Value("${es.cluster.name}")
    private String clusterName;

    /**
     * 集群地址 ip:port(,ip:port)+
     */
    @Value("${es.cluster.address}")
    private String clusterAddress;

    @Bean
    public TransportClient client() throws UnknownHostException {
        // 指定集群名,默认为elasticsearch,如果改了集群名,这里一定要加
        Settings settings = Settings.builder().put("cluster.name", clusterName).build();
        TransportClient client = new PreBuiltTransportClient(settings);
        // ES的TCP端口为9300,而不是之前练习的HTTP端口9200
        // 这里只配置了一个节点的地址然添加进去,也可以配置多个从节点添加进去再返回
        String[] ipPortPairArray = clusterAddress.split(",");
        for (String ipPortPair : ipPortPairArray) {
            String[] ipPort = ipPortPair.split(":");
            client.addTransportAddress(new TransportAddress(InetAddress.getByName(ipPort[0]), Integer.parseInt(ipPort[1])));
        }
        return client;
    }
}
