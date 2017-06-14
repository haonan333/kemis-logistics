package cn.kemis.tools.excel

import org.apache.poi.POIXMLDocument
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.xssf.usermodel.XSSFCell
import org.apache.poi.xssf.usermodel.XSSFRow
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook

/**
 *
 * ExcelUtil. 
 *
 * @author zhangyutong
 * @since 0.1
 */
class ExcelUtil {


    List<Object> readToObj(fileName,sheetNumber,title,obj){
        List<Map<String,String>> list = new ArrayList<>()

        XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(fileName));

        XSSFSheet sheet = wb.getSheetAt(sheetNumber)
        int rows = sheet.physicalNumberOfRows
        println " sheet:\"" + wb.getSheetName(sheetNumber) + "\" has " + rows + " row(s)."

        (0..<rows).each { rowIndex ->
            Object map = getRowToObj(sheet,rowIndex,obj)
            list.add(map)
        }

        if(list.isEmpty())
            return null

        list

    }

    /**
     * 读取所有内容行
     * @param fileName
     * @param sheetNumber
     * @param title
     * @return
     */
    List<Map<String,String>> read(fileName,sheetNumber,title){
        List<Map<String,String>> list = new ArrayList<>()

        XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(fileName));

        XSSFSheet sheet = wb.getSheetAt(sheetNumber)
        int rows = sheet.physicalNumberOfRows
        println " sheet:\"" + wb.getSheetName(sheetNumber) + "\" has " + rows + " row(s)."

        (1..<rows).each { rowIndex ->
            Map<String,String> map = getRow(sheet,rowIndex,title)
            list.add(map)
        }

        if(list.isEmpty())
            return null

        list

    }

    /**
     * 指定起始行数读取所有内容行
     * @param fileName
     * @param sheetNumber
     * @param title
     * @return
     */
    List<Map<String,String>> readByLine(fileName,sheetNumber,title,start,end){
        List<Map<String,String>> list = new ArrayList<>()

        XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(fileName));

        XSSFSheet sheet = wb.getSheetAt(sheetNumber)
        int rows = sheet.physicalNumberOfRows
        println " sheet:\"" + wb.getSheetName(sheetNumber) + "\" has " + rows + " row(s)."

        if(start <1)
            start = 1
        if(end > rows)
            end = rows

        (start..<end).each { rowIndex ->
            Map<String,String> map = getRow(sheet,rowIndex,title)
            list.add(map)
        }

        if(list.isEmpty())
            return null

        list

    }

    /**
     * 获取表头数组
     * @param sheet
     * @return
     */
    List<String> getTitle(fileName,sheetNumber){
        XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(fileName));
        XSSFSheet sheet = wb.getSheetAt(sheetNumber)
        List<String> title = []
        XSSFRow row = sheet.getRow(0)
        if (row != null) {
            int cells = row.physicalNumberOfCells
            (0 ..< cells).each { columnIndex ->
                XSSFCell cell = row.getCell(columnIndex)
                if (cell != null){
                    title.add(getCellValue(cell))
                }
            }
        }
        title

    }

    /**
     * 获取一行内容
     * @param sheet
     * @param index
     * @return 返回map key值表头列名 eg : ["用户ID":"1001"]
     */
    Map<String,String> getRow(XSSFSheet sheet,index,title){
        def map = [:]
        XSSFRow row = sheet.getRow(index)
        if (row != null) {
            int cells = row.physicalNumberOfCells
            (0 ..< cells).each { columnIndex ->
                XSSFCell cell = row.getCell(columnIndex)
                if (cell != null){
                    map."${title[columnIndex]}" = getCellValue(cell)
                }
            }
        }
        map
    }

    Object getRowToObj(XSSFSheet sheet,index,obj){
        List<String> title = getTitle(sheet)
        XSSFRow row = sheet.getRow(index)
        if (row != null) {
            int cells = row.physicalNumberOfCells
            (0 ..< cells).each { columnIndex ->
                XSSFCell cell = row.getCell(columnIndex)
                if (cell != null){
                    obj."${title[columnIndex]}" = getCellValue(cell)
                }
            }
        }
        obj
    }

    boolean is2007(bis){
        POIXMLDocument.hasOOXMLHeader(bis)
    }

    /***
     * 读取单元格的值
     */
    String getCellValue( cell) {
        Object result = "";
        if (cell != null) {
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_STRING:
                    result = cell.getStringCellValue();
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    result = cell.getNumericCellValue();
                    break;
                case Cell.CELL_TYPE_BOOLEAN:
                    result = cell.getBooleanCellValue();
                    break;
                case Cell.CELL_TYPE_FORMULA:
                    result = cell.getCellFormula();
                    break;
                case Cell.CELL_TYPE_ERROR:
                    result = cell.getErrorCellValue();
                    break;
                case Cell.CELL_TYPE_BLANK:
                    break;
                default:
                    break;
            }
        }
        return result.toString();
    }

    static main(args){
        ExcelUtil util = new ExcelUtil()

        def fileName = "/Users/zhangyutong/Downloads/1.xlsx"
        List<String> title = util.getTitle(fileName,0)
        println util.readByLine(fileName,0,title,1,10)


    }
}
