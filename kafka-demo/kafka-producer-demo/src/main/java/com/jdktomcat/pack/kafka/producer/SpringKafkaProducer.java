package com.jdktomcat.pack.kafka.producer;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
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
        String message = "1586418278732,867499030863696,90014a484347386134a23594b49e7400,ai_rom_ocpc_003,1586418268767_2bf7b363ff724f5ca64f987b0b06f9ab,20754,20000063,2,0,223.96.248.161,1,53652,25,46003,,vivo Y79A,2,,b86dddb758ef469d92f920aaf4f8371b,ai_rom_ocpc_003,mediaType\u00031\u0002version_is_effect\u00031\u0002newsSource\u0003\u0002newsId\u0003\u0002ctr\u00030.02504881598689766\u0002cvr\u00030.04868394136428833\u0002ecpm\u0003607231\u0002cAl\u0003ai_rom_ocpc_003\u0002stage\u00032\u0002risk_result\u0003\u0002local_ip\u0003169759854\u0002labIds\u0003\u0002use_index\u00030\u0002use_index_error\u00031\u0002cv_target_price\u0003300000\u0002ad_count\u0003329\u0002remaining_after_pre\u00030\u0002changeId\u0003892522\u0002ocpc_ext_status\u0003\u0002index_version\u0003prd\u0002sourceAppend\u0003eyJhZEZyb20iOiJjb20udml2by5oaWJvYXJkIn0=\u0002creative_id\u0003\u0002use_dynamic_title\u0003\u0002product_id\u0003\u0002recall_alg\u0003\u0002ad_left_top_x\u0003__AD_LT_X__\u0002ad_left_top_y\u0003__AD_LT_Y__\u0002ad_right_bottom_x\u0003__AD_RB_X__\u0002ad_right_bottom_y\u0003__AD_RB_Y__\u0002real_x\u0003__REAL_X__\u0002real_y\u0003__REAL_Y__\u0002rerank_front_version\u0003tarsPrd\u0002rerank_post_version\u0003tarsPrd\u0002load_time\u0003\u0002use_algo_proxy\u00031\u0002algo_proxy_dubbo_tag\u0003\u0002vep_lab_group_id\u0003\u0002oaid\u0003\u0002vaid\u0003\u0002labName\u0003\u0002experimentId\u0003\u0002clientPackage\u0003com.vivo.hiboard\u0002logId\u0003865eba0ade464cda99d2d5b63b3481ad\u0002cv_type\u00033\u0002sv\u0003\u0002use_targeting\u00031\u0002adxExpId\u0003\u0002previewed\u00030\u0002level_media\u0003\u0002use_targeting_dubbo_tag\u0003\u0002retrieve_type\u00030\u0002ua\u0003Dalvik%2F2.1.0+%28Linux%3B+U%3B+Android+7.1.2%3B+vivo+Y79A+Build%2FN2G47H%29\u0002srcLabId\u0003";
        for (int index = 0; index < 100000; index++) {
            try {
                send(message);
            } catch (Exception ex) {
                logger.error(ex.getMessage());
            }
        }
    }
}
