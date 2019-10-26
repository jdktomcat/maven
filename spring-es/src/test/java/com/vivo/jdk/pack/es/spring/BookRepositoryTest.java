package com.vivo.jdk.pack.es.spring;

import com.vivo.jdk.pack.es.spring.entry.Book;
import com.vivo.jdk.pack.es.spring.repository.BookRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：数据接口测试类
 *
 * @author 汤旗
 * @date 2019-10-26 15:47
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/spring-es.xml"})
public class BookRepositoryTest {

    /**
     * 订单数据访问接口
     */
    @Resource
    private BookRepository bookRepository;

    @Test
    public void save() {
        List<String> idList = new ArrayList<>(10000);
        for (int i = 0; i < 10000; i++) {
            Book book = new Book();
            book.setName("book" + i);
            book.setAuthor("author" + i);
            book.setPrice(i * 1.0f);
            idList.add(bookRepository.save(book));
        }
        Assert.assertTrue(idList.size() == 10000);
    }

    @Test
    public void query() {
        List<Book> bookList = bookRepository.queryAll();
        Assert.assertTrue(bookList.size() == 10000);
    }
}

