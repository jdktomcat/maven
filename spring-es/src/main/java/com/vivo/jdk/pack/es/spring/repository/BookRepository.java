package com.vivo.jdk.pack.es.spring.repository;

import com.vivo.jdk.pack.es.spring.entry.Book;

import java.util.List;

/**
 * 接口描述：测试数据访问接口
 *
 * @author 汤旗
 * @date 2019-10-26 15:28
 */
public interface BookRepository {
    /**
     * 保存
     *
     * @param book 图书信息
     * @return 标识
     */
    String save(Book book);

    /**
     * 查询所有图书信息列表
     *
     * @return 图书列表
     */
    List<Book> queryAll();
}
