package com.jdktomcat.pack.kafka.producer;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import javax.annotation.PostConstruct;

/**
 * 类描述：kafka生产者
 *
 * @author 11072131
 * @date 2020-03-2020/3/28 16:33
 */
@Component
public class SpringKafkaProducer {

    /**
     * 日志
     */
    private static final Logger logger = Logger.getLogger(SpringKafkaProducer.class);


    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    /**
     * 发送数据
     *
     * @param message 消息
     */
    public void send(String message) throws Exception {
        ListenableFuture<SendResult<String, String>> future1 = kafkaTemplate.send("stat_ads-log_show-click", message);
        future1.addCallback(result -> {
            logger.info("发送队列1成功：" + result.toString());
        }, ex -> {
            logger.error("发送队列1失败：" + ex.toString());
        });
    }

    /**
     * 初始化
     */
    @PostConstruct
    public void init() {
        String message = "1588142611760,861079032384978,01ce003200360035006200320062006400630035006300300062,s001,1588142552411_3c0df01d68d841afa4af2c1e27409423,1746,20098238,3,2000000,10.58.34.21,1,55076,27,10600,vivo,vivo X9i,2,113.881941*22.557163,2bcb0401d7cb435499056f8db00d7ff3,s001,mediaType\u00031\u0002version_is_effect\u00031\u0002newsSource\u0003\u0002newsId\u0003\u0002ctr\u00030.0082288419031\u0002cvr\u0003null\u0002ecpm\u000316457683\u0002cAl\u0003s001\u0002stage\u00030\u0002risk_result\u0003NORISK\u0002local_ip\u0003174397273\u0002labIds\u0003\u0002use_index\u00031\u0002use_index_error\u00031\u0002cv_target_price\u00032000000\u0002ad_count\u00032\u0002remaining_after_pre\u00032\u0002changeId\u0003\u0002ocpc_ext_status\u0003\u0002index_version\u0003prd\u0002sourceAppend\u0003\u0002creative_id\u0003\u0002use_dynamic_title\u0003\u0002product_id\u0003\u0002recall_alg\u0003\u0002ad_left_top_x\u0003__AD_LT_X__\u0002ad_left_top_y\u0003__AD_LT_Y__\u0002ad_right_bottom_x\u0003__AD_RB_X__\u0002ad_right_bottom_y\u0003__AD_RB_Y__\u0002real_x\u0003__REAL_X__\u0002real_y\u0003__REAL_Y__\u0002rerank_front_version\u0003\u0002rerank_post_version\u0003tarsGrey\u0002load_time\u00031\u0002use_algo_proxy\u00031\u0002algo_proxy_dubbo_tag\u0003tag1\u0002vep_lab_group_id\u0003761_762_880\u0002oaid\u0003\u0002vaid\u0003\u0002labName\u0003\u0002experimentId\u0003\u0002clientPackage\u0003com.vivo.browser\u0002logId\u0003e720435c937546d3a4fc00183be00e59\u0002original_1st_real_ecpm\u00031.64576838062E7\u0002sv\u0003263\u0002rerank_1st_boost_ecpm\u00031.64576838062E7\u0002adxExpId\u0003\u0002level_media\u0003\u0002retrieve_type\u00030\u0002ua\u0003Mozilla%2F5.0+%28Windows+NT+10.0%3B+Win64%3B+x64%29+AppleWebKit%2F537.36+%28KHTML%2C+like+Gecko%29+Chrome%2F81.0.4044.129+Safari%2F537.36\u0002uni\u000319\u0002original_2nd_real_ecpm\u00034937305.14186\u0002rerank_2nd_real_ecpm\u00034937305.14186\u0002use_targeting\u00030\u0002previewed\u00030\u0002level_uni\u0003\u0002rerank_2nd_boost_ecpm\u00034937305.14186\u0002use_targeting_dubbo_tag\u0003\u0002rerank_1st_real_ecpm\u00031.64576838062E7\u0002srcLabId\u0003\u0002basic_page\u00030";
        for (int index = 0; index < 10000000; index++) {
            try {
                send(message);
            } catch (Exception ex) {
                logger.error(ex.getMessage());
            }
        }
    }
}
