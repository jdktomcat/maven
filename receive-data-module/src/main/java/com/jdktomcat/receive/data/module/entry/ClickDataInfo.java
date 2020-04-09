package com.jdktomcat.receive.data.module.entry;

import lombok.Data;

/**
 * 类描述：点击参数信息
 *
 * @author 11072131
 * @date 2020-04-2020/4/9 14:11
 */
@Data
public class ClickDataInfo {
    /**
     * 请求id
     */
    private String requestId;

    /**
     * 广告(创意)id
     */
    private Long adId;

    /**
     * 设备标识，MD5加密，与oaid必选其一
     */
    private String imei;

    /**
     * 点击时间，unix时间戳
     */
    private Long clickTime;

    /**
     * 请求IP
     */
    private String ip;

    /**
     * 唯一标识
     */
    private String ua;

    /**
     * 设备标识，与imei必选其一
     */
    private String oaid;

    /**
     * 广告(创意)id
     */
    private Long creativeId;

    /**
     * 计划类型：商店、非商店、联盟
     */
    private Integer mediaType;

    /**
     * 广告标识
     */
    private Long advertisementId;

    /**
     * 广告名称
     */
    private String adName;

    /**
     * 广告组ID
     */
    private Long groupId;

    /**
     * 广告组名称
     */
    private String groupName;

    /**
     * 广告计划标识
     */
    private Long campaignId;

    /**
     * 广告计划名称
     */
    private String campaignName;

    /**
     * 广告主id
     */
    private String advertiserId;

    /**
     * 广告账户名称
     */
    private String advertiserName;
}
