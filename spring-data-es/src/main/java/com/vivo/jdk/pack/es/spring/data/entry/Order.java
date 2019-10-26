package com.vivo.jdk.pack.es.spring.data.entry;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * 类描述：订单类
 *
 * @author 汤旗
 * @date 2019-10-25 15:16
 */
@Data
@Document(indexName = "test_es_order_index", type = "test_es_order_type")
public class Order {
    /**
     * 标识
     */
    @Id
    private Long id;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 商品名称
     */
    private String goodName;
}