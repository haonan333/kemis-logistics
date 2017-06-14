package cn.kemis.tools.excel;

import org.jeecgframework.poi.excel.entity.ImportParams;

import java.io.InputStream;
import java.util.List;

/**
 * .
 *
 * @author yt.zhang .
 * @version 1.0 .
 * @date 2016-08-13 15:36 .
 */
public class ExcelImportForSax {

    /**
     * 导入Excel 自定义SheetHandler
     * @param inputstream
     * @param pojoClass
     * @param params
     * @param <T>
     * @return
     */
    public static <T> List<T> importExcelBySax(InputStream inputstream, Class<?> pojoClass,
                                               ImportParams params) {
        return new SaxReadExcelExt().process(inputstream, pojoClass, params);
    }
}
