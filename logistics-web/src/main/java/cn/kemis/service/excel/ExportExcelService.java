package cn.kemis.service.excel;

import cn.kemis.Constants;
import cn.kemis.domain.ShipOrderAndGoodsDomain;
import cn.kemis.domain.request.ExportExpressRequest;
import cn.kemis.service.ShipOrderService;
import cn.kemis.util.ExportExcelUtil;
import cn.kemis.util.FileUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

import static cn.kemis.util.ZipFileUtil.zipSimpleFolder;

/**
 * @author 刘体阳 jefferlzu@gmail.com
 *         Created on 2016-08-17
 */
@Service
public class ExportExcelService {
    @Autowired
    private ShipOrderService shipOrderService;

    public String createDeliveryWorkbook(ExportExpressRequest request) {

        String filePath = "";

        try {
            String expressCompany = request.getExpressCompany();
            String path = request.getPath();
            String uuidPath = request.getUuidPath();
            String excelFolderPath = path + uuidPath + request.getTempPath();
            String zipFileName = request.getZipFileName();

            File excelFolder = new File(excelFolderPath);
            if (!excelFolder.exists()) {
                excelFolder.mkdirs();
            }

            String delivery = request.getDelivery();

            String filename = excelFolderPath + "/教师普通_大包.xlsx";
            if (StringUtils.isBlank(delivery) || delivery.equalsIgnoreCase(Constants.SHIP_ORDER_DELIVERY_NORMAL)) {
                request.setBigPackage(true);

                request.setDelivery(Constants.SHIP_ORDER_DELIVERY_NORMAL);
                List<ShipOrderAndGoodsDomain> teacherNormalDeliveryBigPackageList = shipOrderService.selectTeacherDelivery(request);
                ExportExcelUtil.createDeliveryWorkbook(filename, teacherNormalDeliveryBigPackageList, expressCompany);
                // 设置打印快递单状态
                teacherNormalDeliveryBigPackageList = null;

                filename = excelFolderPath + "/教师普通_小包.xlsx";
                request.setBigPackage(false);
                List<ShipOrderAndGoodsDomain> teacherNormalDeliveryList = shipOrderService.selectTeacherDelivery(request);
                ExportExcelUtil.createDeliveryWorkbook(filename, teacherNormalDeliveryList, expressCompany);
                teacherNormalDeliveryList = null;

                request.setBigPackage(null);
                List<String> provinceList = shipOrderService.selectProvinceListByMap(request);
                for (String province : provinceList) {
                    request.setDelivery(Constants.SHIP_ORDER_DELIVERY_NORMAL);
                    List<ShipOrderAndGoodsDomain> studentNormalDeliveryList = shipOrderService.selectStudentDelivery(request, province);
                    filename = excelFolderPath + "/" + province + "学生普通.xlsx";
                    ExportExcelUtil.createDeliveryWorkbook(filename, studentNormalDeliveryList, expressCompany);
                    // 设置打印快递单状态
                    studentNormalDeliveryList = null;
                }
            }

            if (StringUtils.isBlank(delivery) || delivery.equalsIgnoreCase(Constants.SHIP_ORDER_DELIVERY_EMS)) {
                filename = excelFolderPath + "/教师EMS_小包.xlsx";

                request.setDelivery(Constants.SHIP_ORDER_DELIVERY_EMS);
                List<ShipOrderAndGoodsDomain> teacherEMSDeliveryList = shipOrderService.selectTeacherDelivery(request);
                ExportExcelUtil.createDeliveryWorkbook(filename, teacherEMSDeliveryList, expressCompany);
                // 设置打印快递单状态
                teacherEMSDeliveryList = null;

                filename = excelFolderPath + "/教师EMS_大包.xlsx";
                request.setBigPackage(true);
                List<ShipOrderAndGoodsDomain> teacherEMSDeliveryBigPackageList = shipOrderService.selectTeacherDelivery(request);
                ExportExcelUtil.createDeliveryWorkbook(filename, teacherEMSDeliveryBigPackageList, expressCompany);
                teacherEMSDeliveryBigPackageList = null;

                request.setDelivery(Constants.SHIP_ORDER_DELIVERY_EMS);
                List<ShipOrderAndGoodsDomain> studentEMSDeliveryList = shipOrderService.selectStudentDelivery(request, null);
                filename = excelFolderPath + "/学生EMS.xlsx";
                ExportExcelUtil.createDeliveryWorkbook(filename, studentEMSDeliveryList, expressCompany);
                // 设置打印快递单状态
                studentEMSDeliveryList = null;
            }

            filePath = path + zipFileName;

            zipSimpleFolder(excelFolder, "", filePath);

            // 删除临时文件夹
            FileUtil.deletefile(excelFolder.getPath());
            return  filePath;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  filePath;
    }
}
