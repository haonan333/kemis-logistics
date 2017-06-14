package cn.kemis.demo;

import cn.kemis.domain.excelEntity.ShipOrderEntity;
import cn.kemis.exceptions.KemisException;
import cn.kemis.tools.excel.ExcelImportForSax;
import org.apache.poi.ss.usermodel.Workbook;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.entity.TemplateExportParams;
import org.jeecgframework.poi.excel.entity.enmus.ExcelType;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.*;

public class EasyPOIDemo {

    /**
     * 模板导入
     * @throws Exception
     */
    public void templateExport() throws Exception {
        String tempUrl = EasyPOIDemo.class.getResource("/demo_export.xlsx").getPath();
        TemplateExportParams params = new TemplateExportParams(tempUrl);
        Map<String, Object> table = new HashMap<String, Object>();

        List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
        //瓶颈在内存上 真正写入文件很快10万26秒 20万 45秒 30万outmemory 默认内存
        for (int i = 0; i < 1000; i++) {
            Map<String, Object> row = new HashMap<String, Object>();

            row.put("id", "080101" + i);
            row.put("name", "姓名" + i );
            row.put("age",  i );

            rows.add(row);
        }
        table.put("rows", rows);
        long start = System.currentTimeMillis();
        Workbook workbook = ExcelExportUtil.exportExcel(params, table);
        File savefile = new File(EasyPOIDemo.class.getResource("/").getPath());
        if (!savefile.exists()) {
            savefile.mkdirs();
        }
        FileOutputStream fos = new FileOutputStream(EasyPOIDemo.class.getResource("/").getPath() +"/demo_exprot.xlsx");
        workbook.write(fos);
        fos.close();
        System.out.println("(System.currentTimeMillis()-start = " + (System.currentTimeMillis() - start)/1000);

    }

    /**
     * 注解导出
     * @throws Exception
     */
    public void exportForAnnotation() throws Exception {
        ExportParams params = new ExportParams();
        params.setType(ExcelType.XSSF);

        // DemoEntity 注解类
        List<DemoEntity> rows = new ArrayList<DemoEntity>();
        // 10万14秒 20万27秒 30万42秒 如果数据量大建议用注解导出 默认内存
        for (int i = 0; i < 300000; i++) {
            DemoEntity row = new DemoEntity();
            row.setArea("1080101" + i);
            row.setName1("3name" +i);
            row.setName3("5name" +i);
            rows.add(row);
        }
        long start = System.currentTimeMillis();
        Workbook workbook = ExcelExportUtil.exportExcel(params,DemoEntity.class, rows);
        File savefile = new File(EasyPOIDemo.class.getResource("/").getPath());
        if (!savefile.exists()) {
            savefile.mkdirs();
        }
        FileOutputStream fos = new FileOutputStream(EasyPOIDemo.class.getResource("/").getPath() +"/demo_exprot_annotation.xlsx");
        workbook.write(fos);
        fos.close();
        System.out.println("(System.currentTimeMillis()-start = " + (System.currentTimeMillis() - start)/1000);

    }


    /**
     * 导入Excel 只能用注解导入
     */
    public void importExcel() throws Exception {
        ImportParams params = new ImportParams();
        long start = new Date().getTime();
        String excelPath = "/Users/zhangyutong/Downloads/9月单号汇总 - 副本 - 副本.xlsx";
        //importExcelBySax 使用sax导入支持海量数据
        List<DemoEntity> list = ExcelImportForSax.importExcelBySax(new FileInputStream(excelPath), DemoEntity.class, params);
        System.out.println((new Date().getTime() - start)/1000);
        System.out.println(list.size());
        for (int i= 0;i<list.size();i++) {
            System.out.println(list.get(i).toString());
        }

    }

    public void test(){
        String fileSavePath = "/Users/ytz/Downloads/201609.xlsx";
        List<ShipOrderEntity> list;
        try {
            ImportParams importParams = new ImportParams();
            list = ExcelImportForSax.importExcelBySax(new FileInputStream(fileSavePath), ShipOrderEntity.class, importParams);
            System.out.println("list.size() = " + list.size());

        } catch (Exception e) {
            e.printStackTrace();
            throw new KemisException("excel 解析失败");
        }
    }

    public static void main(String[] args) {
        EasyPOIDemo demo = new EasyPOIDemo();
        try {

//            demo.templateExport();
//            demo.exportForAnnotation();
            demo.importExcel();
//            demo.test();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

