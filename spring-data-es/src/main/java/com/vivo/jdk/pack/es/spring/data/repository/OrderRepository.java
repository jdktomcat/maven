package com.sid.mvn.pack.es.spring.data.repository;

import com.sid.mvn.pack.es.spring.data.entry.Order;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * 基础的repository接口
 * spring data自动生成实现类，此处集成自定义的接口是为了在自动生成的实现类中添加自定义的实现
 * （注意：实现类是这个基础的repository接口加上Impl后缀，这样才能被spring自动扫描到）
 * <p>
 * ElasticsearchRepository 继承 PagingAndSortingRepository, PagingAndSortingRepository提供了分页和排序的支持
 *
 * @author 汤旗
 * @date 2019-10-25 15:19
 */
@Repository(value = "orderRepository")
public interface OrderRepository extends ElasticsearchRepository<Order, Long> {

    /**
     * spring data提供的根据方法名称的查询方式
     *
     * @param userName 用户名
     * @param goodName 商品名
     * @return 查询订单
     */
    Order findByUserNameAndGoodName(String userName, String goodName);

    /**
     * 使用Query注解指定查询语句
     * //注意：需要替换的参数？需要加双引号；需要指定参数下标，从0开始
     * //双引号和不加引号都可，不能是单引号
     *
     * @param userName 用户名
     * @param goodName 商品名
     * @return 查询订单
     */
    @Query("{\"bool\" : {\"must\" : [ {\"term\" : {\"goodName\" : \"?1\"}}, {\"term\" : {\"userName\" : \"?0\"}} ]}}")
    Order findByUserNameAndGoodName2(String userName, String goodName);
}
