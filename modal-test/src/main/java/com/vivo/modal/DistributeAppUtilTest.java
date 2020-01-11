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
     * 外层密钥（非线上环境使用）
     */
    private static final String OUTER_SECRET_KEY = "b9e075748e054c3486cd62e399dfad50";

    /**
     * 内层密钥（各业务自行设定）
     */
    private static final String INNER_SECRET_KEY = "26a82ff4861b457dbe787140a332d26c";


    public static void main(String[] args) throws Exception {

        // 1：加密
        // 业务号
        String bussinessId = "1";
        // 自定字段1
        String field1 = "1000";
        // 自定字段2
        String field2 = "2000";
        // 自定字段3
        String field3 = "3000";

        StringBuilder innerTextToEncryptSb = new StringBuilder();
        innerTextToEncryptSb.append(bussinessId).append(DistributeAppUtils.SEPARATOR);
        innerTextToEncryptSb.append(field1).append(DistributeAppUtils.SEPARATOR);
        innerTextToEncryptSb.append(field2).append(DistributeAppUtils.SEPARATOR);
        innerTextToEncryptSb.append(field3);

        // 内层密文
        String innerEncryptedText = DistributeAppUtils.encrypt(innerTextToEncryptSb.toString(), INNER_SECRET_KEY);
        System.out.println("内层密文= " + innerEncryptedText);

        String platformId = "1";
        // 外层密文
        String outerEncryptedText = DistributeAppUtils.encrypt(platformId + DistributeAppUtils.SEPARATOR + innerEncryptedText, OUTER_SECRET_KEY);
        System.out.println("外层密文= " + outerEncryptedText);

        // 2: 解密
        String outerDecryptedText = DistributeAppUtils.decrypt(outerEncryptedText, OUTER_SECRET_KEY);
        // 分解平台号 和 内层密文
        platformId = outerDecryptedText.split(DistributeAppUtils.SEPARATOR)[0];
        innerEncryptedText = outerDecryptedText.split(DistributeAppUtils.SEPARATOR)[1];
        System.out.println("平台号= " + platformId);
        System.out.println("内层密文= " + innerEncryptedText);
        // 使用平台号对应的密钥 解密内层密文
        String innerDecryptedText = DistributeAppUtils.decrypt(innerEncryptedText, INNER_SECRET_KEY);
        String[] strArray = innerDecryptedText.split(DistributeAppUtils.SEPARATOR);
        System.out.println("业务号= " + strArray[0]);
        System.out.println("自定字段1= " +strArray[1]);
        System.out.println("自定字段2= " +strArray[2]);
        System.out.println("自定字段3= " +strArray[3]);
    }
}
