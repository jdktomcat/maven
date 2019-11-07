package com.vivo.jdk.pack.json.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 类描述：广告主行为数据集合信息
 *
 * @author 汤旗
 * @date 2019-08-15 21:39
 */
@Data
public class BehaviorSetInfo implements Serializable {

    /**
     * 序列化版本号
     */
    private static final long serialVersionUID = -3856165317714186263L;

    /**
     * 数据源类型，APP/Web/offline
     */
    private String srcType;

    /**
     * 应用包名, APP行为数据源需要
     */
    private String pkgName;

    /**
     * 页面url, web类行为数据源需要
     */
    private String pageUrl;

    /**
     * 数据源id, 营销平台转化管理工具中新建，每个产品在每个账号下仅可新建一个
     */
    private String srcId;

    /**
     * 明细数据列表
     */
    private List<BehaviorInfo> dataList;
}
