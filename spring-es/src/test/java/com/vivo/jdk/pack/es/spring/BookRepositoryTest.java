package com.sid.mvn.pack.es.spring;

import com.sid.mvn.pack.es.spring.entry.Book;
import com.sid.mvn.pack.es.spring.repository.BookRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
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
        Book book = new Book();
        book.setName("book");
        book.setAuthor("author");
        book.setPrice(1.0f);
        bookRepository.save(book);
        Assert.assertTrue(true);
    }

    @Test
    public void query() {
        List<Book> bookList = bookRepository.queryAll();
        Assert.assertTrue(bookList.size() == 10000);
    }
}

