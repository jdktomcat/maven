package com.sid.mvn.pack.es.spring.repository.impl;

import com.alibaba.fastjson.JSONObject;
import com.sid.mvn.pack.es.spring.entry.Book;
import com.sid.mvn.pack.es.spring.repository.BookRepository;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：数据访问接口实现
 *
 * @author 汤旗
 * @date 2019-10-26 15:33
 */
@Repository
public class BookRepositoryImpl implements BookRepository {

    /**
     * es客户端
     */
    @Autowired
    private TransportClient client;


    /**
     * 保存
     *
     * @param book 图书信息
     * @return 标识
     */
    @Override
    public String save(Book book) {
        try {
            // 构造ES的文档，这里注意startObject()开始构造，结束构造一定要加上endObject()
            XContentBuilder content = XContentFactory.jsonBuilder().startObject().field("name", book.getName()).
                    field("author", book.getAuthor()).field("price", book.getPrice()).endObject();
            IndexResponse result = client.prepareIndex("book", "novel").setSource(content).get();
            return result.getId();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 查询所有图书信息列表
     *
     * @return 图书列表
     */
    @Override
    public List<Book> queryAll() {
        List<Book> bookList = new ArrayList<>();
        SearchResponse searchResponse = client.prepareSearch("book").get();
        SearchHits searchHits = searchResponse.getHits();
        SearchHit[] searchHitArray = searchHits.getHits();
        for (SearchHit searchHit : searchHitArray) {
            bookList.add(JSONObject.parseObject(searchHit.getSourceAsString(), Book.class));
        }
        return bookList;
    }
}
