package com.sid.mvn.pack.json.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * 类描述：广告主行为数据信息
 *
 * @author 汤旗
 * @date 2019-08-14 15:27
 */
@Data
public class BehaviorInfo implements Serializable {
    /**
     * 类序列化版本号
     */
    private static final long serialVersionUID = -1608818378417828306L;

    /**
     * 用户标识类型 IMEI/IMEI md5/其他
     */
    private String imeiType;

    /**
     * 标识的值，如IMEI号等
     */
    private String imei;

    /**
     * 事件类型 激活/注册/付费/表单提交/其他（支持扩展）
     */
    private String cvType;

    /**
     * 事件发生时间 该事件的发生时间
     */
    private Long cvTime;

    /**
     * 事件参数 选填；事件附加参数，如付费金额等
     */
    private String cvParam;

    /**
     * 自定义事件参数
     */
    private String cvCustom;

    /**
     * vivo回传点击数据时，透传给广告主的RequestID，使用点击归因的广告主需要回传。
     */
    private String requestId;

    /**
     * vivo回传点击数据时，透传给广告主的CreativeID，使用点击归因的广告主需要回传。
     */
    private String creativeId;

    /**
     * 下载来源 广告主从分包服务获取到的下载信息，对接了下载来源接口的广告主可以使用，传入完整的加密信息即可；
     */
    private String dlrSrc;
}
