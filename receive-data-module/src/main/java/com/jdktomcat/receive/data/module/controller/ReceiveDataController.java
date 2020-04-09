package com.jdktomcat.receive.data.module.controller;

import com.jdktomcat.receive.data.module.entry.ClickDataInfo;
import com.jdktomcat.receive.data.module.response.ResponseEntry;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
     * @return 响应体
     */
    @RequestMapping(method = RequestMethod.POST, value = "/receive/data")
    public ResponseEntry receiveData(List<ClickDataInfo> receiveDataList) {
        ResponseEntry responseEntry = new ResponseEntry();
        responseEntry.setCode("0");
        responseEntry.setMsg("数据接收成功！");
        return responseEntry;
    }
}
