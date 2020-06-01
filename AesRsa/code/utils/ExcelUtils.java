package utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ExcelUtils{
    public static void main(String[] args) throws Exception{
        //读取xlsx
        Map<Integer, List<String[]>> map = readXlsx("C:\\Users\\Administrator\\Desktop\\easyUI\\courtinfo.xlsx");
        for(int n=0;n<map.size();n++){
            List<String[]> list = map.get(n);
            System.out.println("-------------------------sheet"+n+"--------------------------------");
            for(int i=1;i<list.size();i++){
                String[] arr = (String[]) list.get(i);

                for(int j=0;j<arr.length;j++){
                    if(j==arr.length-1)
                        System.out.print(arr[j]);
                    else
                        System.out.print(arr[j]+"|");
                }
                System.out.println();

                if(arr[9]!=null && !"".equals(arr[9])){
                    SimpleDateFormat sdf1 = new SimpleDateFormat ("EEE MMM dd HH:mm:ss Z yyyy", Locale.UK);
                    Date date = null;
                    date = sdf1.parse(arr[9].toString());
                    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    SimpleDateFormat sdf_2=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String sDate=sdf.format(date);
                    String sDate2 = sdf.format(date);
//                    Date date_of_hearing = sdf_2.parse(sDate2.toString());
                    Date start = (Date)sdf.parseObject(sDate);
                    System.out.println("日期格式为:"+start);

//                    System.out.println(date_of_hearing);
                    System.out.println(sDate);
                    System.out.println(sDate2);

                }

                System.out.println();
                System.out.println("行数为:"+list.size());
                System.out.println("列数为:"+arr.length);
            }

        }
    }

    //读取Xlsx
    public static Map<Integer, List<String[]>> readXlsx(String fileName) {
        Map<Integer, List<String[]>> map = new HashMap<Integer, List<String[]>>();
        try {
            InputStream is = new FileInputStream(fileName);
            XSSFWorkbook workbook = new XSSFWorkbook(is);
            // 循环工作表Sheet
            for (int numSheet = 0; numSheet < workbook.getNumberOfSheets(); numSheet++) {
                XSSFSheet xssfSheet = workbook.getSheetAt(numSheet);
                if (xssfSheet == null) {
                    continue;
                }
                List<String[]> list = new ArrayList<String[]>();

                for (int row=0;row<=xssfSheet.getLastRowNum();row++){
                    if (getCellValToList(xssfSheet, list, row)) continue;
                }
                map.put(numSheet, list);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 从指定行读取Excel
     * @param fileName
     * @param beginRow
     * @return
     */
    public static Map<Integer, List<String[]>> readXlsxByBeginRow(String fileName, int beginRow) {
        Map<Integer, List<String[]>> map = new HashMap<Integer, List<String[]>>();
        try {
            InputStream is = new FileInputStream(fileName);
            XSSFWorkbook workbook = new XSSFWorkbook(is);
            // 循环工作表Sheet
            for (int numSheet = 0; numSheet < workbook.getNumberOfSheets(); numSheet++) {
                XSSFSheet xssfSheet = workbook.getSheetAt(numSheet);
                if (xssfSheet == null) {
                    continue;
                }
                List<String[]> list = new ArrayList<String[]>();

                for (int row=beginRow;row<=xssfSheet.getLastRowNum();row++){
                    if (getCellValToList(xssfSheet, list, row)) continue;
                }
                map.put(numSheet, list);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 获取单元格的值到列表
     * @param xssfSheet
     * @param list
     * @param row
     * @return
     */
    private static boolean getCellValToList(XSSFSheet xssfSheet, List<String[]> list, int row) {
        XSSFRow xssfRow = xssfSheet.getRow(row);
        if (xssfRow == null) {
            return true;
        }
        String[] singleRow = new String[xssfRow.getLastCellNum()];
        for(int column=0;column<xssfRow.getLastCellNum();column++){
            Cell cell = xssfRow.getCell(column, Row.CREATE_NULL_AS_BLANK);
            switch(cell.getCellType()){
                case Cell.CELL_TYPE_BLANK:
                    singleRow[column] = "";
                    break;
                case Cell.CELL_TYPE_BOOLEAN:
                    singleRow[column] = Boolean.toString(cell.getBooleanCellValue());
                    break;
                case Cell.CELL_TYPE_ERROR:
                    singleRow[column] = "";
                    break;
                case Cell.CELL_TYPE_FORMULA:
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    singleRow[column] = cell.getStringCellValue();
                    if (singleRow[column] != null) {
                        singleRow[column] = singleRow[column].replaceAll("#N/A", "").trim();
                    }
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        singleRow[column] = String.valueOf(cell.getDateCellValue());
                    } else {
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        String temp = cell.getStringCellValue();
                        // 判断是否包含小数点，如果不含小数点，则以字符串读取，如果含小数点，则转换为Double类型的字符串
                        if (temp.indexOf(".") > -1) {
                            singleRow[column] = String.valueOf(new Double(temp)).trim();
                        } else {
                            singleRow[column] = temp.trim();
                        }
                    }

                    break;
                case Cell.CELL_TYPE_STRING:
                    singleRow[column] = cell.getStringCellValue().trim();
                    break;
                default:
                    singleRow[column] = "";
                    break;
            }
        }
        list.add(singleRow);
        return false;
    }


    //导出xlsx
    private static void writeXlsx(OutputStream outputStream, Map<String,List<String[]>> map) {
        XSSFWorkbook wb = new XSSFWorkbook();
        for (String key : map.keySet()) {
            XSSFSheet sheet = wb.createSheet(key);
            List<String[]> list = map.get(key);
            for(int i=0;i<list.size();i++){
                XSSFRow row = sheet.createRow(i);
                String[] str = list.get(i);
                for(int j=0;j<str.length;j++){
                    XSSFCell cell = row.createCell(j);
                    cell.setCellValue(str[j]);
                }
            }
        }
        try {
            wb.write(outputStream);
            outputStream.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }



    /**
     * 导出excel
     * @param fileName   文件名
     * @param response
     * @param map  导入的数据： 标签map-->行list-->列String[]的形式
     */
    public static void  exportExcel(String fileName, HttpServletResponse response, Map<String,List<String[]>> map){
        try {
            fileName=java.net.URLEncoder.encode(fileName, "utf-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        response.reset();
        response.setContentType("application/vnd.ms-excel");        //改成输出excel文件
        response.setHeader("Content-disposition","attachment; filename="+fileName+".xlsx" );
        try {
            OutputStream outputStream=response.getOutputStream();
            writeXlsx(outputStream, map);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}