package com.vivo.modal;

import com.vivo.ads.distribute.app.util.DistributeAppUtils;

/**
 * 测试应用分发工具类测试类
 *
 * @author 汤旗
 * @date 2020-01-11
 */
public class DistributeAppUtilTest {

    /**
     * 外层加密解密密钥
     */
    private static final String OUTER_SECRET = "f5e9856c7d3f4ce4b3b5ee45045936e9";

    /**
     * 内层密钥（各业务自行设定）
     */
    private static final String INNER_SECRET = "9d2f32d77cd346be92d3541b02cea04e";


    public static void main(String[] args) throws Exception {

//        // 1：加密
//        // 业务号
//        String bussinessId = "2";
//        // 自定字段1
//        String field1 = "1000";
//        // 自定字段2
//        String field2 = "2000";
//        // 自定字段3
//        String field3 = "3000";
//
//        StringBuilder innerTextToEncryptSb = new StringBuilder();
//        innerTextToEncryptSb.append(bussinessId).append(DistributeAppUtils.SEPARATOR);
//        innerTextToEncryptSb.append(field1).append(DistributeAppUtils.SEPARATOR);
//        innerTextToEncryptSb.append(field2).append(DistributeAppUtils.SEPARATOR);
//        innerTextToEncryptSb.append(field3);
//
//        // 内层密文
//        String innerEncryptedText = DistributeAppUtils.encrypt(innerTextToEncryptSb.toString(), INNER_SECRET);
//        System.out.println("内层密文= " + innerEncryptedText);
//
//        String platformId = "2";
//        // 外层密文
//        String outerEncryptedText = DistributeAppUtils.encrypt(platformId + DistributeAppUtils.SEPARATOR + innerEncryptedText, OUTER_SECRET);
//        System.out.println("外层密文= " + outerEncryptedText);

        String outerEncryptedText1 = "Aiuz012K3YdMu5vkgfyXQcw33GFBYeUUgELeseH1u5pSiMt_IyaK2YhSGesh_XJ5";

        // 2: 解密
        String outerDecryptedText = DistributeAppUtils.decrypt(outerEncryptedText1, OUTER_SECRET);
        // 分解平台号 和 内层密文
        String platformId = outerDecryptedText.split(DistributeAppUtils.SEPARATOR)[0];
        System.out.println("平台号= " + platformId);



        String outerEncryptedText2 = "O4b2UB8leWI1WsTI4emQSe9hOmLTYlAbnsv94rMY61WozQ92eUmnwM5cZWSp-FfEX1O1jYbUE2fofGlu0bXoHAddhrtWrbQ8r9bP01DGkGc";

        // 2: 解密
        String outerDecryptedText2 = DistributeAppUtils.decrypt(outerEncryptedText2, OUTER_SECRET);
        // 分解平台号 和 内层密文
        String platformId2 = outerDecryptedText.split(DistributeAppUtils.SEPARATOR)[0];
        System.out.println("平台号= " + platformId2);
    }
}
