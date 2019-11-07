package com.vivo.jdk.pack.file;

import com.vivo.jdk.pack.excel.ExcelUtil;
import com.vivo.jdk.pack.md5.MD5Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * 类描述：文件扫描器
 *
 * @author 汤旗
 * @date 2019-11-05 16:35
 */
public class FileScanner {

    /**
     * 字段分隔符
     */
    private static final String LOG_SPLITTER = "\001";

    /**
     * 过滤
     *
     * @param fileSource 文件源路径
     * @param adId       广告id
     * @param fileTarget 过滤文件路径
     */
    public static void filter(String fileSource, String adId, String fileTarget) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(fileSource));
        PrintWriter printWriter = new PrintWriter(fileTarget);
        while (scanner.hasNext()) {
            String content = scanner.next();
            String[] paramArray = content.split(LOG_SPLITTER);
            if (paramArray.length > 4 && adId.equalsIgnoreCase(paramArray[3])) {
                printWriter.println(paramArray[1]);
                printWriter.flush();
            }
        }
        printWriter.close();
        scanner.close();
    }

    /**
     * 读取
     *
     * @param filterPath
     * @return
     * @throws FileNotFoundException
     */
    public static Set<String> getContent(String filterPath) throws FileNotFoundException {
        Set<String> imeiSet = new HashSet<>();
        Scanner scanner = new Scanner(new File(filterPath));
        while (scanner.hasNext()) {
            imeiSet.add(scanner.next());
        }
        scanner.close();
        return imeiSet;
    }

    /**
     * 比较
     *
     * @param filterPath 过滤文件路径
     * @param excelPath  excel文件路径
     */
    public static void compare(String filterPath, String excelPath) throws FileNotFoundException {
        Set<String> imeiSet = ExcelUtil.readExcel(excelPath);
        Set<String> sourceImeiSet = getContent(filterPath);
        int lineNum = 1;
        for (String imei : imeiSet) {
            if (!sourceImeiSet.contains(imei) && !sourceImeiSet.contains(MD5Utils.MD5(imei))) {
                System.out.println(String.format("%d %s", lineNum++, imei));
            }
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
//        String fileSource = "C:\\Users\\Administrator\\Desktop\\2019-11-05.log";
//        String adId = "20051130";
        String fileTarget = "C:\\Users\\Administrator\\Desktop\\filter.log";
        String excelPath = "C:\\Users\\Administrator\\Desktop\\excel.xlsx";
        compare(fileTarget, excelPath);
    }
}
