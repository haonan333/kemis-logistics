package cn.kemis.excel;

import cn.kemis.domain.ShipOrderAndGoodsDomain;
import cn.kemis.domain.request.ExportExpressRequest;
import cn.kemis.model.ShipOrder;
import cn.kemis.service.ShipOrderService;
import cn.kemis.service.excel.ExportExcelService;
import cn.kemis.util.DateUtil;
import cn.kemis.util.ExportExcelUtil;
import cn.kemis.util.FileUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static cn.kemis.util.DateUtil.FORMAT_DATE_FILE;

/**
 * @author 刘体阳 jefferlzu@gmail.com
 *         Created on 2016-08-16
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ExportExcelUtilTest {

    @Autowired
    private ExportExcelService exportExcelService;
    @Autowired
    private ShipOrderService shipOrderService;

    @Value("${file.server.url.download}")
    private String downloadPath;

    @Test
    public void createDeliveryWorkbook() {
        String batchNo = "201606";
        String expressCompany = "圆通快递";
        String path = System.getProperty("user.dir");
        String excelFolderPath = path + "/temp";
        // String zipFileName = "快递单.zip";

        // exportExcelService.createDeliveryWorkbook(batchNo, expressCompany, excelFolderPath, path, zipFileName);

        ExportExpressRequest request = new ExportExpressRequest();
        request.setBatchNo(batchNo);

        request.setPath(downloadPath);
        request.setTempPath("temp/");

        String uuid = UUID.randomUUID().toString();

        String uuidPath = FileUtil.getFilePath(uuid);
        String realFilePath = uuidPath + uuid + ".zip";

        String zipFileName = "快递单" + DateUtil.date2String(new Date(), FORMAT_DATE_FILE) + ".zip";

        //真实物理文件名
        request.setZipFileName(realFilePath);
        request.setUuidPath(uuidPath);

        exportExcelService.createDeliveryWorkbook(request);
    }

    /**
     * 主动产生用户导入的发货单测试数据
     */
    @Test
    public void createShipOrderTestWorkbook() {
        String filename = System.getProperty("user.dir") + "/6月测试数据.xlsx";

        ShipOrder shipOrder = new ShipOrder();
        List<ShipOrderAndGoodsDomain> shipOrderAndGoodsDomainList = shipOrderService.selectShipOrderByRecord(shipOrder);

        ExportExcelUtil.createShipOrderTestWorkbook(filename, shipOrderAndGoodsDomainList);
    }


    @Test
    public void createDeliveryTestWorkbook() {
        String filename = System.getProperty("user.dir") + "/6月测试快递单.xlsx";

        ShipOrder shipOrder = new ShipOrder();
        List<ShipOrderAndGoodsDomain> shipOrderAndGoodsDomainList = shipOrderService.selectDeliveryByRecord(shipOrder);

        ExportExcelUtil.createDeliveryTestWorkbook(filename, shipOrderAndGoodsDomainList);
    }
}
