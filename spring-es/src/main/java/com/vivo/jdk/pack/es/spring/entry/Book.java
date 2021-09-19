package com.sid.mvn.pack.es.spring.entry;

import lombok.Data;

/**
 * 类描述：测试实体类
 *
 * @author 汤旗
 * @date 2019-10-26 15:29
 */
@Data
public class Book {

    /**
     * 标识
     */
    private String id;

    /**
     * 书名
     */
    private String name;

    /**
     * 作者
     */
    private String author;

    /**
     * 价格
     */
    private Float price;

}
