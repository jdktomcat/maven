package com.sid.mvn.receive.data.module.response;

import lombok.Data;

/**
 * 类描述：响应类
 *
 * @author 11072131
 * @date 2020-04-2020/4/9 14:02
 */
@Data
public class ResponseEntry {
    /**
     * 响应编码
     */
    private String code;

    /**
     * 响应描述
     */
    private String msg;
}
