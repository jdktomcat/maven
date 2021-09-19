package com.sid.mvn.receive.data.module.controller;

import com.sid.mvn.receive.data.module.entry.ClickDataInfo;
import com.sid.mvn.receive.data.module.response.ResponseEntry;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

/**
 * 类描述：接收数据测试接口
 *
 * @author 11072131
 * @date 2020-04-2020/4/9 13:58
 */
@RestController
public class ReceiveDataController {

    /**
     * 数据接收接口
     *
     * @param receiveData 接收数据
     * @return 响应体
     */
    @RequestMapping(method = RequestMethod.POST, value = "/receive/data")
    public ResponseEntry receiveData(@RequestBody ClickDataInfo[] receiveData) {
        ResponseEntry responseEntry = new ResponseEntry();
        responseEntry.setCode("0");
        responseEntry.setMsg("数据接收成功！");
        System.out.println(String.format("接收数据：%s", Arrays.toString(receiveData)));
        return responseEntry;
    }
}
