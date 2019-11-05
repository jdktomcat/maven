package com.vivo.jdk.pack.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * 类描述：Excel文件读取
 *
 * @author 汤旗
 * @date 2019-08-16 17:47
 */
public class ExcelUtil {

    /**
     * 读取文件创建文档对象
     *
     * @param filePath 文件路径
     * @return 文档对象
     */
    private static Workbook getReadWorkBookType(String filePath) {
        //xls-2003, xlsx-2007
        FileInputStream is = null;
        try {
            is = new FileInputStream(filePath);
            return new XSSFWorkbook(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(is);
        }
        return null;
    }

    /**
     * 读取imei集合
     *
     * @param sourceFilePath 文件路径
     * @return imei集合
     */
    public static Set<String> readExcel(String sourceFilePath) {
        Set<String> imeiSet = new HashSet<>();
        Workbook workbook = null;
        try {
            workbook = getReadWorkBookType(sourceFilePath);
            //获取第一个sheet
            int sheetNumber = workbook.getNumberOfSheets();
            for (int i = 0; i < sheetNumber; i++) {
                Sheet sheet = workbook.getSheetAt(i);
                //第0行是表名，忽略，从第二行开始读取
                for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
                    Row row = sheet.getRow(rowNum);
                    Cell imeiCell = row.getCell(2);
                    if (imeiCell != null) {
                        imeiSet.add(imeiCell.getStringCellValue());
                    }
                }
            }
        } finally {
            IOUtils.closeQuietly(workbook);
        }
        return imeiSet;
    }
}
