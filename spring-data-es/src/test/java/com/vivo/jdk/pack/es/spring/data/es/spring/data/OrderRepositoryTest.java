package com.vivo.jdk.pack.es.spring.data.es.spring.data;

import com.vivo.jdk.pack.es.spring.data.entry.Order;
import com.vivo.jdk.pack.es.spring.data.repository.OrderRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * 类描述：订单数据访问测试类
 *
 * @author 汤旗
 * @date 2019-10-25 15:34
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/spring-data-es.xml"})
public class OrderRepositoryTest {

    /**
     * 订单数据访问接口
     */
    @Resource
    private OrderRepository orderRepository;

    @Test
    public void order() {
        Iterable<Order> iterable = orderRepository.findAll();
        List<Order> orderList = new ArrayList<>();
        iterable.forEach(new Consumer<Order>() {
            /**
             * Performs this operation on the given argument.
             *
             * @param order the input argument
             */
            @Override
            public void accept(Order order) {
                orderList.add(order);
            }
        });
        Assert.assertTrue(!orderList.isEmpty());
    }

}
