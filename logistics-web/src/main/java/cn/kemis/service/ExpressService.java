package cn.kemis.service;

import cn.kemis.Constants;
import cn.kemis.dao.mapper.ExpressMapper;
import cn.kemis.dao.mapper.ShipOrderMapper;
import cn.kemis.domain.ExpressWorkProcessDomain;
import cn.kemis.domain.api.base.BaseResponse;
import cn.kemis.domain.excelEntity.ExpressConsigneeEntity;
import cn.kemis.domain.excelEntity.ExpressNoEntity;
import cn.kemis.domain.excelEntity.ExpressPriceEntity;
import cn.kemis.domain.request.ExportDispatchedExpressRequest;
import cn.kemis.domain.request.ExportExpressRequest;
import cn.kemis.exceptions.KemisException;
import cn.kemis.model.*;
import cn.kemis.service.excel.ExportExcelService;
import cn.kemis.tools.excel.ExcelImportForSax;
import cn.kemis.util.DateUtil;
import cn.kemis.util.ExportExcelUtil;
import cn.kemis.util.FileUtil;
import cn.kemis.util.Servlets;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;

import static cn.kemis.util.DateUtil.FORMAT_DATE_FILE;
import static cn.kemis.util.ZipFileUtil.zipSimpleFolder;


/**
 * Created by xuhailong on 16/8/18.
 */
@Service
public class ExpressService extends BaseService<ExpressMapper, Express, ExpressExample> {

    Logger logger = LoggerFactory.getLogger(ShipOrderSourceService.class);

    @Autowired
    private ExportExcelService exportExcelService;
    @Autowired
    private ExportFileService exportFileService;
    @Autowired
    private ShipOrderMapper shipOrderMapper;

    @Value("${file.server.url.download}")
    private String downloadPath;

    /**
     * 快递数据的插入
     */
    public String insertExpress(HttpServletRequest request) {
        String f = "false";
        String connector = "_";
        String prefix = "DM";
        Map map = Servlets.getParameterMap(request);

        Express express = new Express();
        express.setExpressId(Long.parseLong((String)map.get("order_sn")));
        express.setBatchNo(DateUtil.date2String(new Date(), DateUtil.FORMAT_DATE)+connector+prefix);
        express.setExpressCompany((String) map.get("expressCompany"));
        express.setExpressMobile((String)map.get("phone_mob"));
        String str = (String) map.get("region_name");
        express.setExpressProvince(str.split("\t")[0]);
        express.setExpressCity(str.split("\t")[1]);
        express.setExpressDistrict(str.split("\t")[2]);
        express.setExpressAddress((String)map.get("address"));
        express.setPrice(Double.valueOf((String)map.get("shipping_fee")));
        //express.setWeight((String)map.get("weight"));
        express.setExpressConsignee((String)map.get("consignee"));
        int ex = mapper.insertExpress(express);
        try {
            if (ex > 0) {

                f = "true";
            }
            return f;
        }catch(Exception e){
            throw new KemisException("新增快递表失败");
        }
    }


    /**
     * 解析快递单表格,并批量插入到物流信息表
     * @param batchNo           批次号
     * @param fileSavePath      文件路径
     */
    void readExpressNoAndBatchInsert(String batchNo, String fileSavePath) {
        //从文件中取读的待处理内容 到list中
        List<ExpressNoEntity> list;
        try {
            list = ExcelImportForSax.importExcelBySax(new FileInputStream(fileSavePath), ExpressNoEntity.class, new ImportParams());
        } catch (Exception e) {
            e.printStackTrace();
            throw new KemisException("excel 解析失败");
        }

        //批量添加到物流信息表
        int total = list.size();
        int pageSize = 1000;//每次处理1000
        int page = 0;

        List<Express> batchInsertList = null;
        while (page * pageSize <= total) {
            batchInsertList = new ArrayList<>(pageSize);
            for (int i = page * pageSize; i < (page + 1) * pageSize; i++) {
                try {
                    if (i >= total) break;
                    ExpressNoEntity expressNoEntity = list.get(i);
                    if (StringUtils.isBlank(expressNoEntity.getExpressNumber())) continue;
                    Express express = new Express();
                    express.setExpressId(expressNoEntity.getExpressId());
                    express.setExpressNumber(expressNoEntity.getExpressNumber());
                    express.setExpressCompany(expressNoEntity.getExpressCompany());
                    batchInsertList.add(express);
                } catch (Exception e) {
                    logger.error("--> ExpressNoEntity to Express error ! row= " + (i+1) + " " + e.getMessage());
                    continue;
                }
            }
            //批量插入
            if (batchInsertList.size() > 0) {
                batchUpdateSelective(batchInsertList, Express.class);
            }
            page++;
        }
    }

    /**
     * 解析快递单表格,并批量插入到物流信息表
     * @param batchNo           批次号
     * @param fileSavePath      文件路径
     */
    void readExpressConsigneeAndBatchInsert(String batchNo, String fileSavePath) {
        //从文件中取读的待处理内容 到list中
        List<ExpressConsigneeEntity> list;
        try {
            list = ExcelImportForSax.importExcelBySax(new FileInputStream(fileSavePath), ExpressConsigneeEntity.class, new ImportParams());
        } catch (Exception e) {
            e.printStackTrace();
            throw new KemisException("excel 解析失败");
        }

        //批量添加到物流信息表
        int total = list.size();
        int pageSize = 1000;//每次处理1000
        int page = 0;

        List<Express> batchInsertList = null;
        while (page * pageSize <= total) {
            batchInsertList = new ArrayList<>(pageSize);
            for (int i = page * pageSize; i < (page + 1) * pageSize; i++) {
                try {
                    if (i >= total) break;
                    ExpressConsigneeEntity expressConsigneeEntity = list.get(i);
                    Express express = new Express();
                    express.setExpressId(expressConsigneeEntity.getExpressId());
                    express.setBatchNo(batchNo);
                    express.setType(expressConsigneeEntity.getType());
                    express.setDelivery(expressConsigneeEntity.getDelivery());
                    express.setExpressConsignee(expressConsigneeEntity.getConsignee());
                    express.setExpressMobile(expressConsigneeEntity.getMobile());
                    express.setExpressSchool(expressConsigneeEntity.getSchool());
                    express.setExpressProvince(expressConsigneeEntity.getProvince());
                    express.setExpressCity(expressConsigneeEntity.getCity());
                    express.setExpressDistrict(expressConsigneeEntity.getDistrict());
                    express.setExpressAddress(expressConsigneeEntity.getAddress());
                    express.setExpressSchoolId(expressConsigneeEntity.getSchoolId());
                    batchInsertList.add(express);
                } catch (Exception e) {
                    logger.error("--> ExpressConsigneeEntity to Express error ! row= " + (i+1) + " " + e.getMessage());
                    continue;
                }
            }
            //批量插入
            if (batchInsertList.size() > 0) {
                batchInsertSelective(batchInsertList, Express.class);
            }

            // 批量更新发货单配送方式及省市区
            for (Express express : batchInsertList) {
                ShipOrder record = new ShipOrder();
                record.setDelivery(express.getDelivery());
                record.setProvince(express.getExpressProvince());
                record.setCity(express.getExpressCity());
                record.setDistrict(express.getExpressDistrict());

                ShipOrderExample example = new ShipOrderExample();
                example.createCriteria().andExpressIdEqualTo(express.getExpressId());
                shipOrderMapper.updateByExampleSelective(record, example);
            }

            page++;
        }

    }

    /**
     * 解析快递价格表,并批量更新到物流信息表
     * @param batchNo           批次号
     * @param fileSavePath      文件路径
     */
    void readExpressPriceAndBatchUpdate(String batchNo, String fileSavePath) {
        //从文件中取读的待处理内容 到list中
        List<ExpressPriceEntity> list;
        try {
            list = ExcelImportForSax.importExcelBySax(new FileInputStream(fileSavePath), ExpressPriceEntity.class, new ImportParams());
        } catch (Exception e) {
            e.printStackTrace();
            throw new KemisException("excel 解析失败");
        }

        //批量更新到物流信息表
        int total = list.size();
        int pageSize = 1000;//每次处理1000
        int page = 0;

        List<Express> batchUpdateList = null;
        while (page * pageSize <= total) {
            batchUpdateList = new ArrayList<>(pageSize);
            for (int i = page * pageSize; i < (page + 1) * pageSize; i++) {
                try {
                    if (i >= total) break;
                    ExpressPriceEntity expressPriceEntity = list.get(i);
                    Express express = new Express();
                    express.setExpressId(expressPriceEntity.getExpressId());

                    if (StringUtils.isNotBlank(expressPriceEntity.getPrice())) {
                        BigDecimal price = new BigDecimal(expressPriceEntity.getPrice());
                        express.setPrice(price.doubleValue() * 100);
                    }

                    express.setWeight(expressPriceEntity.getWeight());
                    batchUpdateList.add(express);
                } catch (Exception e) {
                    logger.error("--> ExpressPriceEntity to Express error ! row= " + (i+1) + " " + e.getMessage());
                    continue;
                }
            }
            //批量插入
            if (batchUpdateList.size() > 0) {
                batchUpdateSelective(batchUpdateList, Express.class);
            }
            page++;
        }
    }

    public String[] exportExpressZip(ExportExpressRequest request){
        String[] fileInfo = new String[2];
        try {
            request.setPath(downloadPath);
            request.setTempPath("temp/");

            String uuid = UUID.randomUUID().toString();

            String batchNo = request.getBatchNo();
            String uuidPath = FileUtil.getFilePath(uuid);
            String realFilePath = uuidPath + uuid + ".zip";

            String zipFileName = "快递单" + DateUtil.date2String(new Date(), FORMAT_DATE_FILE) + ".zip";

            //真实物理文件名
            request.setZipFileName(realFilePath);
            request.setUuidPath(uuidPath);
            fileInfo[0] = exportExcelService.createDeliveryWorkbook(request);

            ExportFile file = new ExportFile();
            file.setBatchNo(batchNo);
            file.setFileName(zipFileName);
            file.setFileSuffix("zip");
            file.setFileGuid(uuid);
            file.setFileUrl(uuidPath  + uuid + ".zip");
            file.setType(Constants.EXPORT_FILE_TYPE_DELIVERY);
            this.exportFileService.insertSelective(file);

            fileInfo[0] = zipFileName;
            fileInfo[1] = downloadPath + realFilePath;
            return fileInfo;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileInfo;

    }


    public void downExportExpressZip(HttpServletResponse response,ExportExpressRequest request) throws Exception {
        String[] fileInfo = exportExpressZip(request);
        String fileName = fileInfo[0];
        String file = fileInfo[1];
        OutputStream os = response.getOutputStream();
        try {
            response.reset();
            response.setHeader("Content-Disposition","attachment;filename="+ URLEncoder.encode(fileName,"UTF-8"));
            response.setContentType("application/octet-stream; charset=utf-8");
            os.write(FileUtils.readFileToByteArray(new File(file)));
            os.flush();
        } finally {
            if (os != null) {
                os.close();
            }
        }

    }

    /**
     * 导出发货结果单
     * @param request
     * @return
     */
    public ExportFile exportDispatchedExpress(ExportDispatchedExpressRequest request) {
        // 生成文件数据
        ExportFile exportFile = getExportFile(request);

        //获取查询时间
        if (StringUtils.isBlank(request.getQueryDate())) {
            throw new KemisException("请指定时间范围");
        }

        String[] dateStr = request.getQueryDate().trim().split("/");
        String startTime = dateStr[0].trim();
        String endTime = dateStr[1].trim();
        request.setDateStart(startTime);
        request.setDateEnd(endTime);

        // 按条件查数据
        List<ExpressWorkProcessDomain> expressList = mapper.selectByExportDispatchedExpressRequest(request);

        // 生成文件
        if (expressList != null && expressList.size() > 0) {
            String path = downloadPath + FileUtil.getFilePath(exportFile.getFileGuid());
            File excelFolder = FileUtil.touchDirs(path + "temp");
            String fileName = excelFolder.getPath() + "/" + exportFile.getFileName().substring(0, exportFile.getFileName().indexOf(".zip") - 1) + ".xlsx";

            ExportExcelUtil.createDispatchedWorkbook(fileName, expressList);

            zipSimpleFolder(excelFolder, "", path + "/" + exportFile.getFileGuid() + "." + exportFile.getFileSuffix());

            // FileUtil.deletefile(excelFolder.getPath());

            // 入库
            exportFileService.insertSelective(exportFile);
            return exportFile;
        }
        return null;
    }

    /**
     * 根据查询条件生成文件名,组装导出文件信息
     * @param request
     * @return
     */
    private ExportFile getExportFile(ExportDispatchedExpressRequest request) {
        String fileGuid = UUID.randomUUID().toString();
        String fileSavePath = FileUtil.getFilePath(fileGuid);
        String connector = "_";
        String suffix = ".zip";

        ExportFile exportFile = new ExportFile();
        exportFile.setFileGuid(fileGuid);

        StringBuilder fileName = new StringBuilder();
        fileName.append("发货结果单");

        if (StringUtils.isNotBlank(request.getQueryDate())) {
            String[] dateStr = request.getQueryDate().trim().split("/");
            String startTime = dateStr[0].trim();
            String endTime = dateStr[1].trim();

            fileName.append(connector).append(startTime).append(connector).append(endTime);
        }

        if (StringUtils.isNotBlank(request.getExpressSchool())) {
            fileName.append(connector).append(request.getExpressSchool());
        }

        if (StringUtils.isNotBlank(request.getExpressNumber())) {
            fileName.append(connector).append(request.getExpressNumber());
        }

        if (StringUtils.isNotBlank(request.getExpressCompany())) {
            fileName.append(connector).append(request.getExpressCompany());
        }

        if (StringUtils.isNotBlank(request.getDelivery())) {
            fileName.append(connector).append(request.getDelivery());
        }

        if (StringUtils.isNotBlank(request.getExpressAddress())) {
            fileName.append(connector).append(request.getExpressAddress());
        }

        if (StringUtils.isNotBlank(request.getType())) {
            fileName.append(connector).append(request.getType());
        }

        String date2String = DateUtil.date2String(new Date(), DateUtil.FORMAT_DATE_TIMESTAMP);
        fileName.append(connector).append(date2String);
        fileName.append(suffix);

        exportFile.setFileName(fileName.toString());
        exportFile.setFileUrl(fileSavePath + fileGuid + suffix);
        exportFile.setFileSuffix("zip");
        exportFile.setType(Constants.EXPORT_FILE_TYPE_DELIVERY_RESULT);
        exportFile.setBatchNo("");
        return exportFile;
    }

    /**
     * 分页查找
     * @param request
     * @return
     */
    public PageInfo<ExpressWorkProcessDomain> searchByRequest(ExportDispatchedExpressRequest request) {

        //获取查询时间
        /*if (StringUtils.isBlank(request.getQueryDate())) {
            throw new KemisException("请指定时间范围");
        }*/

        if (StringUtils.isNotBlank(request.getQueryDate())) {
            String[] dateStr = request.getQueryDate().trim().split("/");
            String startTime = dateStr[0].trim();
            String endTime = dateStr[1].trim();
            request.setDateStart(startTime);
            request.setDateEnd(endTime);
        }
/*
        int count = mapper.countByExportDispatchedExpressRequest(request);
        List<ExpressWorkProcessDomain> list = new ArrayList<>();
        if (count > 0) {
            list = mapper.selectByExportDispatchedExpressRequest(request);
        }



        Map<String,Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("msg", "success");
        result.put("data",list);
        result.put("total",count);
        result.put("draw",request.getDraw());

        return result;*/


        PageHelper.startPage(request.getDraw(), request.getLimit());
        return new PageInfo<>(mapper.selectByExportDispatchedExpressRequest(request));
    }

    public BaseResponse changeStudentDelivery(Express express) {
        BaseResponse baseResponse = new BaseResponse();

        if (StringUtils.isBlank(express.getBatchNo())) {
            baseResponse.setCode("450");
            baseResponse.setMsg("批次号不能为空！");
            return baseResponse;
        }

        ExpressExample example = new ExpressExample();
        example.createCriteria().andBatchNoEqualTo(express.getBatchNo());
        int countByExample = mapper.countByExample(example);
        if (countByExample < 1) {
            baseResponse.setCode("451");
            baseResponse.setMsg("批次号 " + express.getBatchNo() + " 快递单数据不存在！");
            return baseResponse;
        }

        if (StringUtils.isBlank(express.getExpressProvince())
                && StringUtils.isBlank(express.getExpressCity())
                && StringUtils.isBlank(express.getExpressDistrict())
                && StringUtils.isBlank(express.getExpressSchool())) {
            baseResponse.setCode("452");
            baseResponse.setMsg("省、市、区和学校至少填写一项！");
            return baseResponse;
        }

        if (StringUtils.isBlank(express.getDelivery())) {
            baseResponse.setCode("453");
            baseResponse.setMsg("请指定配送方式！");
            return baseResponse;
        }

        example.clear();
        ExpressExample.Criteria criteria = example.createCriteria();
        criteria.andBatchNoEqualTo(express.getBatchNo()).andTypeEqualTo("学生快递");

        if (StringUtils.isNotBlank(express.getExpressProvince())) {
            criteria.andExpressProvinceEqualTo(express.getExpressProvince());
        }

        if (StringUtils.isNotBlank(express.getExpressCity())) {
            criteria.andExpressCityEqualTo(express.getExpressCity());
        }

        if (StringUtils.isNotBlank(express.getExpressDistrict())) {
            criteria.andExpressDistrictEqualTo(express.getExpressDistrict());
        }

        if (StringUtils.isNotBlank(express.getExpressSchool())) {
            criteria.andExpressSchoolEqualTo(express.getExpressSchool());
        }

        Express record = new Express();
        record.setDelivery(express.getDelivery());
        int count = mapper.updateByExampleSelective(record, example);

        baseResponse.setCode("0");
        baseResponse.setMsg("修改学生单配送方式成功，工处理 " + count + " 条数据！");
        return baseResponse;
    }
}
