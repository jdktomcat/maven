package com.vivo.jdk.pack.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 类描述：Excel文件读取
 *
 * @author 汤旗
 * @date 2019-08-16 17:47
 */
public class ExcelUtil {

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

    public static Map<String, List<String>> readExcel(String sourceFilePath) {
        Map<String, List<String>> contents = new HashMap<>();
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
                    Cell industryCell = row.getCell(0);
                    Cell titleCell = row.getCell(1);
                    if (industryCell != null && titleCell != null) {
                        String industryId = (int) industryCell.getNumericCellValue() + "";
                        String title = titleCell.getStringCellValue();
                        List<String> titleList = contents.get(industryId);
                        if (titleList == null) {
                            titleList = new ArrayList<>();
                        }
                        titleList.add(title);
                        contents.put(industryId, titleList);
                    }
                }
            }
        } finally {
            IOUtils.closeQuietly(workbook);
        }
        return contents;
    }

    /**
     * 生成SQL文件
     *
     * @param content 读取内容
     */
    private static void writeSQLFile(Map<String, List<String>> content) throws FileNotFoundException {
        try (PrintWriter printWriter = new PrintWriter("C:\\Users\\Administrator\\Desktop\\insert.sql")) {
            for (Map.Entry<String, List<String>> entry : content.entrySet()) {
                StringBuilder stringBuilder = new StringBuilder("insert into t_multimedia_title_template (title,industry,create_time,update_time) values ");
                String industryId = entry.getKey();
                for (String title : entry.getValue()) {
                    stringBuilder.append("('" + title + "',").append(industryId + ",now(),now()),");
                }
                printWriter.println(stringBuilder.substring(0, stringBuilder.length() - 1) + ";");
                printWriter.flush();
            }
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        String excelFile = "C:\\Users\\Administrator\\Desktop\\data.xlsx";
        writeSQLFile(readExcel(excelFile));
    }
}
