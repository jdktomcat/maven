package com.sid.mvn.pack.json;

import com.alibaba.fastjson.JSONObject;

import java.io.FileNotFoundException;

/**
 * 类描述：Bean序列化JSON
 *
 * @author 汤旗
 * @date 2019-08-15 10:24
 */
public class BeanJsonUtil {

    public static void main(String[] args) throws FileNotFoundException {
        String jsonStr = "";
        JSONObject jsonObject = JSONObject.parseObject(jsonStr);
        System.out.println(jsonObject == null);

    }
}
