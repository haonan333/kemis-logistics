package cn.kemis.service;

import cn.kemis.Constants;
import cn.kemis.dao.mapper.ShipOrderMapper;
import cn.kemis.domain.SchoolOrderDomain;
import cn.kemis.domain.ShipOrderAndGoodsDomain;
import cn.kemis.domain.ShipOrderDomain;
import cn.kemis.domain.excelEntity.ShipOrderEntity;
import cn.kemis.domain.excelEntity.VerityTeacherEntity;
import cn.kemis.domain.request.ExportExpressRequest;
import cn.kemis.exceptions.KemisException;
import cn.kemis.model.*;
import cn.kemis.tools.excel.ExcelImportForSax;
import cn.kemis.tools.flying.PdfHelper;
import cn.kemis.util.*;
import org.apache.commons.lang3.StringUtils;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

import static cn.kemis.tools.flying.PdfUtils.generateToFile;
import static cn.kemis.util.FileUtil.touchDirs;
import static cn.kemis.util.ZipFileUtil.zipSimpleFolder;

/**
 * @author 刘体阳 jefferlzu@gmail.com
 *         Created on 2016-08-11
 */
@Service
public class ShipOrderService extends BaseService<ShipOrderMapper, ShipOrder, ShipOrderExample> {

    private static Logger logger = LoggerFactory.getLogger(ShipOrderService.class);

    //导入发货单业务标示
    private static final String SHIP_ORDER_BUSINESS_ID_SOURCE = "source";
    //导入老师校验
    private static final String SHIP_ORDER_BUSINESS_ID_VERITY_TEACHER = "verity_teacher";
    //导入快递单业务标示
    private static final String SHIP_ORDER_BUSINESS_ID_EXPRESS_NO = "express_no";
    //导入快递收货人业务标示
    private static final String SHIP_ORDER_BUSINESS_ID_EXPRESS_CONSIGNEE = "express_consignee";
    //导入价格业务标示
    private static final String SHIP_ORDER_BUSINESS_ID_EXPRESS_PRICE = "express_price";

    private static final String SHIP_ORDER_PDF_TEMPLATE_NAME = "shipOrder.ftl";
    private static final String SHIP_ORDER_TEACHER_PDF_TEMPLATE_NAME = "shipOrderTeacher.ftl";

    @Value("${file.server.url.upload}")
    private String uploadFilePath;

    @Value("${file.server.url.download}")
    private String downloadFilePath;

    @Value("${file.server.url.pdfTemplates}")
    private String pdfTemplatePath;

    @Autowired
    private UploadFileService uploadFileService;

    @Autowired
    private ShipOrderSourceService shipOrderSourceService;

    @Autowired
    private ExpressService expressService;

    @Autowired
    private ExportFileService exportFileService;

    /**
     * 订单数据的插入
     */
    public String insertShipOrder(HttpServletRequest request) {
        String f = "false";
        String connector = "_";
        String prefix = "DM";
        Map map = Servlets.getParameterMap(request);

        ShipOrder shipOrder = new ShipOrder();
        //shipOrder.setShipOrderId(Long.parseLong((String)map.get("order_id")));
        shipOrder.setExpressId(Long.parseLong((String)map.get("order_sn")));
        shipOrder.setSourceOrder(Integer.valueOf((String)map.get("order_sn")));
        shipOrder.setMobile((String)map.get("phone_mob"));
        shipOrder.setConsignee((String)map.get("consignee"));
        String str = (String) map.get("region_name");
        shipOrder.setProvince(str.split("\t")[0]);
        shipOrder.setCity(str.split("\t")[1]);
        shipOrder.setDistrict(str.split("\t")[2]);
        shipOrder.setAddress((String)map.get("address"));
        shipOrder.setBatchNo(DateUtil.date2String(new Date(), DateUtil.FORMAT_DATE)+connector+prefix);
        shipOrder.setUserName((String)map.get("buyer_name"));
        shipOrder.setUserId((String) map.get("buyer_id"));
        shipOrder.setStatus(Byte.valueOf("1"));
        shipOrder.setTeacherTag(true);
        int so = mapper.insertShipOrders(shipOrder);

        try{ if( so > 0 ){
            f = "true";
        }
            return f;
        }catch(Exception e){
            e.printStackTrace();
            throw new KemisException("新增订单表失败");
        }
    }

    /**
     * 根据订单号查询订单状态、快递号
     */
    public Map<String,Object> selectStatus(HttpServletRequest request){

        String sourceOrder = request.getParameter("order_sn");
        return mapper.selectShipOrderByShipOrderIds(Long.parseLong(sourceOrder));
    }

    /**
     * 查询指定批次教师核验数据
     * @param batchNo
     * @return
     */
    public List<ShipOrder> selectTeacherVerify(String batchNo) {
        ShipOrderExample example = new ShipOrderExample();
        example.createCriteria().andBatchNoEqualTo(batchNo).andTeacherTagEqualTo(true)
                .andOutExpressEqualTo(false).andStatusEqualTo((byte) 0);
        // example.setOrderByClause("province, city, district, school");
        example.setOrderByClause("CONVERT( province USING gbk ), CONVERT( city USING gbk ), CONVERT( district USING gbk ), CONVERT( school USING gbk ), CONVERT( theClass USING gbk )");

        return mapper.selectByExample(example);
    }

    /**
     * 查询指定批次学生发货单涉及的省份
     * @param batchNo   批次号
     * @return          省份列表
     */
    public List<String> selectProvinceListByBatchNo(String batchNo) {
        return mapper.selectPorvinceListByBatchNo(batchNo);
    }

    /**
     * 查询指定条件学生发货单涉及的省份
     * @param request   请求参数
     * @return          省份列表
     */
    public List<String> selectProvinceListByMap(ExportExpressRequest request) {

        Map<String,Object> param = new HashMap<>();
        if (StringUtils.isNotBlank(request.getProvince())) {
            param.put("province",request.getProvince());
        }
        if (StringUtils.isNotBlank(request.getCity())) {
            param.put("city",request.getCity());
        }
        if (StringUtils.isNotBlank(request.getDistrict())) {
            param.put("district",request.getDistrict());
        }
        if (StringUtils.isNotBlank(request.getSchool())) {
            param.put("school",request.getSchool());
        }
        if (StringUtils.isNotBlank(request.getConsignee())) {
            param.put("consignee",request.getConsignee());
        }

        return mapper.selectPorvinceListByMap(param);
    }

    /**
     * 查询指定批次内,所有可以配送的教师指定类型发货单
     * @param shipOrder   请求参数
     * @return          发货单列表
     */
    public List<ShipOrderAndGoodsDomain> selectTeacherDelivery(ShipOrder shipOrder) {

        shipOrder.setTeacherTag(true);
        shipOrder.setStatus((byte) 1);
        shipOrder.setPrintExpress(false);

        return mapper.selectDeliveryByShipOrder(shipOrder);
    }

    /**
     * 查询指定批次内,所有可以配送的教师指定类型发货单
     * @param batchNo   批次号
     * @param delivery  配送方式,见常量
     * @return          发货单列表
     */
    public List<ShipOrder> selectTeacherDelivery(String batchNo, String delivery) {
        ShipOrderExample example = new ShipOrderExample();
        example.createCriteria().andBatchNoEqualTo(batchNo).andDeliveryEqualTo(delivery)
                .andTeacherTagEqualTo(true).andStatusEqualTo((byte) 1).andPrintExpressEqualTo(false);
        // example.setOrderByClause("province, city, district, school");
        example.setOrderByClause("CONVERT( province USING gbk ), CONVERT( city USING gbk ), CONVERT( district USING gbk ), CONVERT( school USING gbk ), CONVERT( consignee USING gbk )");

        return mapper.selectByExample(example);
    }

    /**
     * 查询指定批次内,所有可以配送的学生指定类型发货单
     * @param batchNo   批次号
     * @param delivery  配送方式,见常量
     * @param province  省份
     * @return          发货单列表
     */
    public List<ShipOrder> selectStudentDelivery(String batchNo, String delivery, String province) {
        ShipOrderExample example = new ShipOrderExample();
        example.createCriteria().andBatchNoEqualTo(batchNo).andProvinceEqualTo(province)
                .andDeliveryEqualTo(delivery).andTeacherTagEqualTo(false)
                .andStatusEqualTo((byte) 1).andPrintExpressEqualTo(false);
        // example.setOrderByClause("province, city, district, school");
        example.setOrderByClause("CONVERT( province USING gbk ), CONVERT( city USING gbk ), CONVERT( district USING gbk ), CONVERT( school USING gbk ), CONVERT( consignee USING gbk )");

        return mapper.selectByExample(example);
    }

    /**
     * 查询指定批次内,所有可以配送的学生指定类型发货单
     * @param shipOrder   请求参数
     * @param province  省份
     * @return          发货单列表
     */
    public List<ShipOrderAndGoodsDomain> selectStudentDelivery(ShipOrder shipOrder, String province) {

        shipOrder.setProvince(province);
        shipOrder.setTeacherTag(false);
        shipOrder.setStatus((byte) 1);
        shipOrder.setPrintExpress(false);

        return mapper.selectDeliveryByShipOrder(shipOrder);
    }

    /**
     * 查询指定批次内,指定配送类型,指定学校的教师发货单
     * @param shipOrder     搜索条件
     * @return              发货单列表
     */
    public List<ShipOrderAndGoodsDomain> selectTeacherShipOrder(ShipOrder shipOrder) {

        shipOrder.setTeacherTag(true);
        shipOrder.setStatus((byte) 1);

        return mapper.selectDeliveryByRecord(shipOrder);
    }

    /**
     * 查询指定批次内,指定配送类型,指定学校的学生发货单
     * @param shipOrder 查询条件
     * @return          发货单列表
     */
    public List<ShipOrderAndGoodsDomain> selectStudentShipOrder(ShipOrder shipOrder) {

        shipOrder.setTeacherTag(false);
        shipOrder.setStatus((byte) 1);

        return mapper.selectDeliveryByRecord(shipOrder);
    }

    /**
     *
     * @param record   查询条件
     * @return          发货单列表
     */
    public List<ShipOrderAndGoodsDomain> selectShipOrderByRecord(ShipOrder record) {
        return mapper.selectDeliveryByRecord(record);
    }

    public List<ShipOrderAndGoodsDomain> selectDeliveryByRecord(ShipOrder record) {
        return mapper.selectDeliveryByRecord(record);
    }

    /**
     * 查询指定批次学生发货单涉及的学校
     * @param batchNo   批次号
     * @return          学校列表
     */
    public List<String> selectSchoolListByBatchNo(String batchNo) {
        return mapper.selectSchoolListByBatchNo(batchNo);
    }

    /**
     * 查询指定批次学生发货单涉及的学校
     * @param batchNo   批次号
     * @param province  省
     * @return          学校列表
     */
    public List<SchoolOrderDomain> selectSchoolListByBatchNoAndProvince(String batchNo, String province, String delivery) {
        return mapper.selectSchoolListByBatchNoAndProvince(batchNo, province, delivery);
    }

    /**
     * 查询指定批次指定学校的学生发货单
     * @param batchNo   批次号
     * @param school    学校名称
     * @return          发货单列表
     */
    public List<ShipOrder> selectByScholl(String batchNo, String school) {
        ShipOrderExample example = new ShipOrderExample();
        example.createCriteria().andBatchNoEqualTo(batchNo).andSchoolEqualTo(school)
                .andTeacherTagEqualTo(false).andStatusEqualTo((byte) 1);
        // example.setOrderByClause("province, city, district, school");
        example.setOrderByClause("CONVERT( province USING gbk ), CONVERT( city USING gbk ), CONVERT( district USING gbk ), CONVERT( school USING gbk ), CONVERT( subject USING gbk ), CONVERT( consignee USING gbk ), CONVERT( theClass USING gbk )");

        return mapper.selectByExample(example);
    }

    /**
     * 查询列表
     * @param request
     * @return
     */
    public Map searchShipOrderList(HttpServletRequest request) {
        int limit = Integer.parseInt(request.getParameter("limit"));
        int start = Integer.parseInt(request.getParameter("start"));
        int drawCount = Integer.parseInt(request.getParameter("draw"));

        String province = request.getParameter("province");
        String city = request.getParameter("city");
        String district = request.getParameter("district");
        String school = request.getParameter("school");
        String consignee = request.getParameter("consignee");
        String expressNumber = request.getParameter("expressNumber");

        String queryDate = request.getParameter("querydate");
        String mobile = request.getParameter("mobile");
        String delivery = request.getParameter("delivery");
        String theClass = request.getParameter("theClass");
        String unitCredits = request.getParameter("unitCredits");//区分老师快递 学生快递
        String userName = request.getParameter("userName");  //下单人

        String orderName = request.getParameter("orderColumn");
        String orderDir = request.getParameter("orderDir");

        Map<String,Object> param = new HashMap<>();
        //获取查询时间
        if (StringUtils.isNotBlank(queryDate)) {
            String[] dateStr = queryDate.trim().split("/");
            param.put("startTime",dateStr[0].trim());
            param.put("endTime",dateStr[1].trim());
        }
        if(StringUtils.isNotBlank(mobile))
            param.put("mobile",mobile);

        if(StringUtils.isNotBlank(delivery))
            param.put("delivery",delivery);

        if(StringUtils.isNotBlank(theClass))
            param.put("theClass",theClass);

        if(StringUtils.isNotBlank(unitCredits)){
            if(unitCredits.equals("学生快递")){
                param.put("unitCredits","学生豆");
            }else{
                param.put("unitCredits","其它豆");
            }
        }

        if(StringUtils.isNotBlank(userName))
            param.put("userName",userName);


        if (StringUtils.isNotBlank(province)) {
            param.put("province",province);
        }
        if (StringUtils.isNotBlank(city)) {
            param.put("city",city);
        }
        if (StringUtils.isNotBlank(district)) {
            param.put("district",district);
        }
        if (StringUtils.isNotBlank(school)) {
            param.put("school",school);
        }
        if (StringUtils.isNotBlank(consignee)) {
            param.put("consignee",consignee);
        }
        if (StringUtils.isNotBlank(expressNumber)) {
            param.put("expressNumber",expressNumber);
        }

        int count = mapper.countByMap(param);
        List<ShipOrderDomain> list = new ArrayList<>();
        if (count > 0) {
            param.put("start",start);
            param.put("limit",limit);
            param.put("orderName",orderName);
            param.put("orderDir",orderDir);
            list = mapper.selectByMap(param);
        }


        Map<String,Object> result = new HashMap<String,Object>();
        result.put("code", 0);
        result.put("msg", "success");
        result.put("data",list);
        result.put("total",count);
        result.put("draw",drawCount);

        return result;
    }

    /**
     * 编辑保存
     * @param shipOrderDomain
     * @return
     */
    @Transactional
    public Map<String, Object> editShipOrder(ShipOrderDomain shipOrderDomain) {
        if (shipOrderDomain == null) throw new KemisException("保存失败");

        ShipOrder shipOrder = new ShipOrder();
        BeanUtils.copyProperties(shipOrderDomain,shipOrder);

        int count = mapper.updateByPrimaryKeySelective(shipOrder);
        //todo 暂未支持修改明细
//        if (count > 0) {
//
//            //处理明细
//            String goodsName = shipOrderDomain.getGoodsName();
//            if (StringUtils.isNotBlank(goodsName)) {
//                goodsName = goodsName.trim().replaceAll("\n", ",");
//                String[] goods = goodsName.split(",");
//                //获取明细
//                List<ShipOrderGoods> shipOrderGoodsList = this.findShipOrderGoodsList(shipOrderDomain.getShipOrderId());
//                for (int i = 0; i < goods.length; i++) {
//                    if (!shipOrderGoodsList.contains(goods[i])) {
//                        continue;
//                    }
//                }
//
//            } else {
//                //删除所有明细
//            }
//        }

        Map<String,Object> result = new HashMap<String,Object>();
        result.put("code", 0);
        result.put("msg", "success");

        return result;
    }


    /**
     * 处理发货单上传文件
     * @param file      文件
     * @param batchNo   批次号
     * @throws KemisException
     * @throws IOException
     */
    @Transactional
    public void dealShipOrderSource(MultipartFile file, String batchNo) throws KemisException, IOException {

        String fileSavePath = uploadFileService.checkAndSaveFile(file,batchNo);

        //处理文件和指派任务
        readFileAndAssignBusiness(batchNo, fileSavePath, "source");
    }

    /**
     * 处理导入快递单收货人
     * @param file
     * @param batchNo
     * @throws IOException
     */
    @Transactional
    public void dealFileExpressConsignee(MultipartFile file, String batchNo) throws IOException {
        //处理文件
        String fileSavePath = uploadFileService.checkAndSaveFile(file,batchNo);

        //处理文件和指派任务
        readFileAndAssignBusiness(batchNo, fileSavePath, "express_consignee");

    }

    /**
     * 处理导入快递单
     * @param file
     * @param batchNo
     * @throws IOException
     */
    @Transactional
    public void dealFileExpressNo(MultipartFile file, String batchNo) throws IOException {
        //处理文件
        String fileSavePath = uploadFileService.checkAndSaveFile(file,batchNo);

        //处理文件和指派任务
        readFileAndAssignBusiness(batchNo, fileSavePath, "express_no");

    }

    /**
     * 处理导入价格
     * @param file
     * @param batchNo
     * @return
     */
    public void dealFileExpressPrice(MultipartFile file, String batchNo) {
        //处理文件
        String fileSavePath = uploadFileService.checkAndSaveFile(file,batchNo);

        //处理文件和指派任务
        readFileAndAssignBusiness(batchNo, fileSavePath, "express_price");

    }

    /**
     * 处理导入价格
     * @param file
     * @param batchNo
     * @return
     */
    public void dealFileVerifyTeacher(MultipartFile file, String batchNo) {
        //处理文件
        String fileSavePath = uploadFileService.checkAndSaveFile(file,batchNo);

        //处理文件和指派任务
        readFileAndAssignBusiness(batchNo, fileSavePath, "verity_teacher");

    }

    /**
     * 处理文件和指派任务
     * @param batchNo               批次号
     * @param fileSavePath          文件路径
     * @param businessId            业务标示
     */
    private void readFileAndAssignBusiness(String batchNo, String fileSavePath,String businessId) {
        if (StringUtils.isBlank(fileSavePath)) {
            throw new KemisException("文件上传失败");
        }

        //如果是 zip包 先解压  再循环处理
        if (fileSavePath.endsWith(".zip")) {
            String outZipPath = uploadFilePath + "/unzip/"+businessId+"/"+batchNo+"/";
            ZipFileUtil.unzip(fileSavePath, outZipPath);//解压
            File fileList = new File(outZipPath);
            File[] directory = fileList.listFiles();
            if (directory != null && directory.length > 0) {
                for (File excel : directory) {
                    if (excel.isDirectory() || excel.isHidden()) continue;
                    String excelName = excel.getName();
                    assignBusiness(batchNo, outZipPath + excelName, businessId);
                }
            }
        } else {
            assignBusiness(batchNo, fileSavePath, businessId);
        }
    }

    /**
     * 指派任务
     * @param batchNo               批次号
     * @param fileSavePath          文件路径
     * @param businessId            业务标示
     */
    private void assignBusiness(String batchNo, String fileSavePath, String businessId) {
        switch (businessId) {
            case SHIP_ORDER_BUSINESS_ID_SOURCE:
                //导入发货单业务,保存到源数据表
                shipOrderSourceService.readSourceAndBatchInsert(batchNo, fileSavePath);
                break;
            case SHIP_ORDER_BUSINESS_ID_EXPRESS_CONSIGNEE:
                //导入快递收货人业务
                expressService.readExpressConsigneeAndBatchInsert(batchNo, fileSavePath);
                break;
            case SHIP_ORDER_BUSINESS_ID_EXPRESS_NO:
                //导入快递单业务
                expressService.readExpressNoAndBatchInsert(batchNo, fileSavePath);
                break;

            case SHIP_ORDER_BUSINESS_ID_EXPRESS_PRICE:
                //导入价格业务
                expressService.readExpressPriceAndBatchUpdate(batchNo, fileSavePath);
                break;
            case SHIP_ORDER_BUSINESS_ID_VERITY_TEACHER:
                //导入老师校验
                this.readVerityTeacherAndBatchUpdate(batchNo, fileSavePath);
                break;
        }
    }

    /**
     * 解析老师检验表,并批量更新到ship_order表
     * @param batchNo           批次号
     * @param fileSavePath      文件路径
     */
    private void readVerityTeacherAndBatchUpdate(String batchNo, String fileSavePath) {
        //从文件中取读的待处理内容 到list中
        List<VerityTeacherEntity> list;
        try {
            list = ExcelImportForSax.importExcelBySax(new FileInputStream(fileSavePath), VerityTeacherEntity.class, new ImportParams());
        } catch (Exception e) {
            e.printStackTrace();
            throw new KemisException("excel 解析失败");
        }

        //批量更新到物流信息表
        int total = list.size();
        int pageSize = 1000;//每次处理1000
        int page = 0;

        List<ShipOrder> batchUpdateList = null;
        ShipOrder shipOrder;
        while (page * pageSize <= total) {
            batchUpdateList = new ArrayList<>(pageSize);
            for (int i = page * pageSize; i < (page + 1) * pageSize; i++) {
                try {
                    if (i >= total) break;
                    //TODO
                    VerityTeacherEntity verityTeacherEntity = list.get(i);
                    shipOrder = new ShipOrder();
                    batchUpdateList.add(shipOrder);
                } catch (Exception e) {
                    logger.error("--> VerityTeacherEntity to ShipOrder error ! row= " + (i+1) + " " + e.getMessage());
                    continue;
                }
            }
            //批量插入
            if (batchUpdateList.size() > 0) {
                batchUpdateSelective(batchUpdateList, ShipOrder.class);
            }
            page++;
        }
    }

    /**
     * 请求导出发货单
     * @param shipOrder 前端请求参数
     * @return          导出文件详情
     */
    public ExportFile exportShipOrder(ShipOrder shipOrder) {

        String fileGuid = UUID.randomUUID().toString();
        String fileSavePath = FileUtil.getFilePath(fileGuid);

        // 未指定批次号时,默认当前期次
        String batchNo = shipOrder.getBatchNo();
        if (StringUtils.isBlank(batchNo)) {
            batchNo = DateUtil.date2String(new Date(), DateUtil.FORMAT_MONTH);
            shipOrder.setBatchNo(batchNo);
        }

        ExportFile exportFile = new ExportFile();
        exportFile.setBatchNo(batchNo);
        exportFile.setFileGuid(fileGuid);

        StringBuilder rootZipFileName = new StringBuilder();
        rootZipFileName.append("快递单_发货单_").append(batchNo);
        String connector = "_";
        String teacher = "教师";
        String student = "学生";
        String suffix = ".zip";

        if (StringUtils.isNotBlank(shipOrder.getProvince())) {
            rootZipFileName.append(connector).append(shipOrder.getProvince());
        }

        if (shipOrder.getTeacherTag() != null) {
            if (shipOrder.getTeacherTag()) {
                rootZipFileName.append(connector).append(teacher);
            } else {
                rootZipFileName.append(connector).append(student);
            }
        }

        if (StringUtils.isNotBlank(shipOrder.getDelivery())) {
            rootZipFileName.append(connector).append(shipOrder.getDelivery());
        }

        String date2String = DateUtil.date2String(new Date(), DateUtil.FORMAT_DATE_TIMESTAMP);
        rootZipFileName.append(connector).append(date2String);
        // .append(suffix);

        exportFile.setFileName(rootZipFileName.toString());
        exportFile.setFileUrl(fileSavePath + fileGuid + suffix);
        exportFile.setFileSuffix("zip");
        exportFile.setType(Constants.EXPORT_FILE_TYPE_ORDER);

        new Thread(() -> {
            try {
                // 创建 PDF 文件 并写入数据库
                createShipOrderPDF(shipOrder, exportFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        return exportFile;
    }


    /**
     * 生成发货单
     * 已启用参数
     * batchNo      必填  批次号
     * province     可选  省
     * teacherTag   可选  老师 true/学生 false/全部 null
     * @param shipOrder     前端查询参数
     * @param exportFile    导出文件的详情
     * @throws Exception
     */
    public void createShipOrderPDF(ShipOrder shipOrder, ExportFile exportFile) throws Exception {
        String batchNo = shipOrder.getBatchNo();
        String province = shipOrder.getProvince();
        Boolean teacherTag = shipOrder.getTeacherTag();

        String expressCompany = "";

        String fileGuid = exportFile.getFileGuid();
        String fileSavePath = FileUtil.getFilePath(fileGuid);
        String path = downloadFilePath + fileSavePath;

        //region 组装 zip 文件名
        String connector = "_";
        String teacher = "教师";
        String student = "学生";
        String suffix = ".zip";

        StringBuilder excelZipFileName = new StringBuilder();
        excelZipFileName.append("快递单_").append(batchNo);
        StringBuilder orderExcelZipFileName = new StringBuilder();
        orderExcelZipFileName.append("发货单Excel_").append(batchNo);
        StringBuilder pdfZipFileName = new StringBuilder();
        pdfZipFileName.append("发货单_").append(batchNo);

        if (StringUtils.isNotBlank(shipOrder.getProvince())) {
            excelZipFileName.append(connector).append(shipOrder.getProvince());
            pdfZipFileName.append(connector).append(shipOrder.getProvince());
            orderExcelZipFileName.append(connector).append(shipOrder.getProvince());
        }

        if (shipOrder.getTeacherTag() != null) {
            if (shipOrder.getTeacherTag()) {
                excelZipFileName.append(connector).append(teacher);
                pdfZipFileName.append(connector).append(teacher);
                orderExcelZipFileName.append(connector).append(teacher);
            } else {
                excelZipFileName.append(connector).append(student);
                pdfZipFileName.append(connector).append(student);
                orderExcelZipFileName.append(connector).append(student);
            }
        }

        if (StringUtils.isNotBlank(shipOrder.getDelivery())) {
            excelZipFileName.append(connector).append(shipOrder.getDelivery());
            pdfZipFileName.append(connector).append(shipOrder.getDelivery());
            orderExcelZipFileName.append(connector).append(shipOrder.getDelivery());
        }

        String date2String = DateUtil.date2String(new Date(), DateUtil.FORMAT_DATE_TIMESTAMP);
        excelZipFileName.append(connector).append(date2String).append(suffix);
        pdfZipFileName.append(connector).append(date2String).append(suffix);
        orderExcelZipFileName.append(connector).append(date2String).append(suffix);

        //endregion

        File pdfFolder = touchDirs(path + "/temp-pdf");     // PDF 临时文件夹
        File excelFolder = touchDirs(path + "/temp-excel"); // Excel 临时文件夹
        File orderExcelFolder = touchDirs(path + "/temp-order-excel"); // Excel 临时文件夹
        File zipFolder = touchDirs(path + "/temp-zip"); // Zip 临时文件夹
        // File teacherEMSBigPackageFolder = touchDirs(pdfFolder.getPath() + "/teacherEMSBigPackage");
        // File teacherEMSFolder = touchDirs(pdfFolder.getPath() + "/teacherEMS");
        // File teacherNormalBigPackageFolder = touchDirs(pdfFolder.getPath() + "/teacherNormalBigPackage");
        // File teacherNormalFolder = touchDirs(pdfFolder.getPath() + "/teacherNormal");
        File studentProvinceFolder = touchDirs(pdfFolder.getPath() + "/province");
        File studentEMSFolder = touchDirs(pdfFolder.getPath() + "/studentEMSPath");
        // File studentNormalFolder = touchDirs(pdfFolder.getPath() + "/studentNormalPath");

        String targetName = null;
        String delivery = shipOrder.getDelivery();      // 配送方式 过滤条件
        String excelFileName = null;                    // 快递单 excel 文件名

        ShipOrder shipOrderParma = new ShipOrder();
        BeanUtils.copyProperties(shipOrder, shipOrderParma);


        Map<Long, Integer> sequenceMap = new HashMap<>();

        // 导出全部或学生
        if (teacherTag == null || !teacherTag) {
            shipOrderParma.setTeacherTag(false);

            shipOrderParma.setDelivery(Constants.SHIP_ORDER_DELIVERY_EMS);
            excelFileName = excelFolder.getPath() + "/学生EMS.xlsx";
            List<ShipOrderAndGoodsDomain> studentEMSDeliveryList = this.selectStudentDelivery(shipOrderParma, null);

            if (studentEMSDeliveryList != null) {
                for (int i = 0; i < studentEMSDeliveryList.size(); i++) {
                    sequenceMap.put(studentEMSDeliveryList.get(i).getExpressId(), i + 1);
                }
                ExportExcelUtil.createDeliveryWorkbook(excelFileName, studentEMSDeliveryList, expressCompany);
                // ExportExcelUtil.createStudentShipOrderWorkbook(orderExcelFolder.getPath() + "/学生EMS发货单.xlsx", studentEMSDeliveryList, sequenceMap);
            }

            // 设置打印快递单状态
            studentEMSDeliveryList = null;

            shipOrderParma.setDelivery(shipOrder.getDelivery());
            shipOrderParma.setProvince(shipOrder.getProvince());
            if (StringUtils.isNotBlank(province)) {     // 按查询条件导出省内
                createStudentPDFByProvince(path, expressCompany, excelFolder, pdfFolder, studentProvinceFolder, studentEMSFolder, shipOrderParma, sequenceMap, 1, orderExcelFolder);
            } else {    // 所有省
                List<String> provinceList = selectProvinceListByBatchNo(batchNo);// 学生发货单涉及的省份
                int provinceIndex = 1;

                final CountDownLatch countDownLatch = new CountDownLatch(provinceList.size());

                for (String provinceStr : provinceList) {
                    ShipOrder shipOrderParmaThread = new ShipOrder();
                    BeanUtils.copyProperties(shipOrder, shipOrderParmaThread);
                    shipOrderParmaThread.setTeacherTag(false);
                    shipOrderParmaThread.setProvince(provinceStr);

                    final  int idx = provinceIndex;

                    // System.out.println(String.format("%d %s", idx, provinceStr));

                    new Thread(() -> {

                        try {
                            createStudentPDFByProvince(path, expressCompany, excelFolder, pdfFolder, studentProvinceFolder, studentEMSFolder, shipOrderParmaThread, sequenceMap, idx, orderExcelFolder);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        countDownLatch.countDown();
                    }).start();

                    // shipOrderParma.setProvince(provinceStr);
                    // shipOrderParma.setDelivery(shipOrder.getDelivery());
                    // createStudentPDFByProvince(path, expressCompany, excelFolder, pdfFolder, studentProvinceFolder, studentEMSFolder, shipOrderParma, sequenceMap, provinceIndex);
                    // shipOrderParma.setProvince(null);
                    provinceIndex++;
                }
                countDownLatch.await();
            }
            PdfHelper.mergePDFFiles(studentEMSFolder.getPath(), pdfFolder.getPath() + "/学生EMS.pdf");

            // FileUtil.deletefile(studentProvinceFolder.getPath());
            // FileUtil.deletefile(studentEMSFolder.getPath());
        }

        // 导出全部或老师
        shipOrderParma = new ShipOrder();
        BeanUtils.copyProperties(shipOrder, shipOrderParma);
        if (teacherTag == null || teacherTag) {
            shipOrderParma.setTeacherTag(true);

            if (StringUtils.isBlank(delivery) || delivery.equalsIgnoreCase(Constants.SHIP_ORDER_DELIVERY_EMS)) {

                shipOrderParma.setBigPackage(true);
                shipOrderParma.setDelivery(Constants.SHIP_ORDER_DELIVERY_EMS);
                excelFileName = excelFolder.getPath() + "/教师EMS_大件.xlsx";
                List<ShipOrderAndGoodsDomain> teacherEMSDeliveryBigPackageList = this.selectTeacherDelivery(shipOrderParma);

                sequenceMap.clear();
                if (teacherEMSDeliveryBigPackageList != null && teacherEMSDeliveryBigPackageList.size() > 0) {
                    for (int i = 0; i < teacherEMSDeliveryBigPackageList.size(); i++) {
                        sequenceMap.put(teacherEMSDeliveryBigPackageList.get(i).getExpressId(), i + 1);
                    }
                    ExportExcelUtil.createDeliveryWorkbook(excelFileName, teacherEMSDeliveryBigPackageList, expressCompany);
                    // ExportExcelUtil.createTeacherShipOrderWorkbook(orderExcelFolder.getPath() + "/教师EMS_大件发货单.xlsx", teacherEMSDeliveryBigPackageList, sequenceMap);
                }

                teacherEMSDeliveryBigPackageList = null;

                // 教师EMS 大件
                List<ShipOrderAndGoodsDomain> teacherShipOrderEMSBigPackageList = selectTeacherShipOrder(shipOrderParma);

                if (sequenceMap.size() > 0) {
                    for (ShipOrderAndGoodsDomain shipOrderAndGoodsDomain : teacherShipOrderEMSBigPackageList) {
                        shipOrderAndGoodsDomain.setSequenceNo(sequenceMap.get(shipOrderAndGoodsDomain.getExpressId()));
                    }
                }

                targetName = "教师_EMS_大件";
                generatePDF(path, pdfFolder, Constants.SHIP_ORDER_DELIVERY_EMS, targetName, teacherShipOrderEMSBigPackageList);
                ExportExcelUtil.createTeacherShipOrderWorkbook(orderExcelFolder.getPath() + "/教师EMS_大件发货单.xlsx", teacherShipOrderEMSBigPackageList, sequenceMap);

                shipOrderParma.setBigPackage(false);
                excelFileName = excelFolder.getPath() + "/教师EMS_小件.xlsx";
                List<ShipOrderAndGoodsDomain> teacherEMSDeliveryList = this.selectTeacherDelivery(shipOrderParma);

                sequenceMap.clear();
                if (teacherEMSDeliveryList != null && teacherEMSDeliveryList.size() > 0) {
                    for (int i = 0; i < teacherEMSDeliveryList.size(); i++) {
                        sequenceMap.put(teacherEMSDeliveryList.get(i).getExpressId(), i + 1);
                    }
                    ExportExcelUtil.createDeliveryWorkbook(excelFileName, teacherEMSDeliveryList, expressCompany);
                    // ExportExcelUtil.createTeacherShipOrderWorkbook(orderExcelFolder.getPath() + "/教师EMS_小件发货单.xlsx", teacherEMSDeliveryList, sequenceMap);
                }

                // 设置打印快递单状态
                teacherEMSDeliveryList = null;

                // 教师EMS 小件
                List<ShipOrderAndGoodsDomain> teacherShipOrderEMSList = selectTeacherShipOrder(shipOrderParma);

                if (sequenceMap.size() > 0) {
                    for (ShipOrderAndGoodsDomain shipOrderAndGoodsDomain : teacherShipOrderEMSList) {
                        shipOrderAndGoodsDomain.setSequenceNo(sequenceMap.get(shipOrderAndGoodsDomain.getExpressId()));
                    }
                }

                targetName = "教师_EMS_小件";
                generatePDF(path, pdfFolder, Constants.SHIP_ORDER_DELIVERY_EMS, targetName, teacherShipOrderEMSList);
                ExportExcelUtil.createTeacherShipOrderWorkbook(orderExcelFolder.getPath() + "/教师EMS_小件发货单.xlsx", teacherShipOrderEMSList, sequenceMap);
            }

            if (StringUtils.isBlank(delivery) || delivery.equalsIgnoreCase(Constants.SHIP_ORDER_DELIVERY_NORMAL)) {

                shipOrderParma.setBigPackage(true);
                shipOrderParma.setDelivery(Constants.SHIP_ORDER_DELIVERY_NORMAL);

                excelFileName = excelFolder.getPath() + "/教师普通_大件.xlsx";
                List<ShipOrderAndGoodsDomain> teacherNormalDeliveryBigPackageList = this.selectTeacherDelivery(shipOrderParma);

                sequenceMap.clear();
                if (teacherNormalDeliveryBigPackageList != null && teacherNormalDeliveryBigPackageList.size() > 0) {
                    for (int i = 0; i < teacherNormalDeliveryBigPackageList.size(); i++) {
                        sequenceMap.put(teacherNormalDeliveryBigPackageList.get(i).getExpressId(), i + 1);
                    }
                    ExportExcelUtil.createDeliveryWorkbook(excelFileName, teacherNormalDeliveryBigPackageList, expressCompany);
                    // ExportExcelUtil.createTeacherShipOrderWorkbook(orderExcelFolder.getPath() + "/教师普通_大件发货单.xlsx", teacherNormalDeliveryBigPackageList, sequenceMap);
                }

                // 设置打印快递单状态
                teacherNormalDeliveryBigPackageList = null;

                // 教师普通大件
                List<ShipOrderAndGoodsDomain> teacherShipOrderNormalBigPackageList = selectTeacherShipOrder(shipOrderParma);

                if (sequenceMap.size() > 0) {
                    for (ShipOrderAndGoodsDomain shipOrderAndGoodsDomain : teacherShipOrderNormalBigPackageList) {
                        shipOrderAndGoodsDomain.setSequenceNo(sequenceMap.get(shipOrderAndGoodsDomain.getExpressId()));
                    }
                }

                targetName = "教师_普通快递_大件";
                generatePDF(path, pdfFolder, Constants.SHIP_ORDER_DELIVERY_NORMAL, targetName, teacherShipOrderNormalBigPackageList);
                ExportExcelUtil.createTeacherShipOrderWorkbook(orderExcelFolder.getPath() + "/教师普通_大件发货单.xlsx", teacherShipOrderNormalBigPackageList, sequenceMap);

                excelFileName = excelFolder.getPath() + "/教师普通_小件.xlsx";
                shipOrderParma.setBigPackage(false);
                List<ShipOrderAndGoodsDomain> teacherNormalDeliveryList = this.selectTeacherDelivery(shipOrderParma);

                sequenceMap.clear();
                if (teacherNormalDeliveryList != null && teacherNormalDeliveryList.size() > 0) {
                    for (int i = 0; i < teacherNormalDeliveryList.size(); i++) {
                        sequenceMap.put(teacherNormalDeliveryList.get(i).getExpressId(), i + 1);
                    }
                    ExportExcelUtil.createDeliveryWorkbook(excelFileName, teacherNormalDeliveryList, expressCompany);
                    // ExportExcelUtil.createTeacherShipOrderWorkbook(orderExcelFolder.getPath() + "/教师普通_小件发货单.xlsx", teacherNormalDeliveryList, sequenceMap);
                }

                teacherNormalDeliveryList = null;

                // 教师普通小件
                List<ShipOrderAndGoodsDomain> teacherShipOrderNormalList = selectTeacherShipOrder(shipOrderParma);

                if (sequenceMap.size() >0) {
                    for (ShipOrderAndGoodsDomain shipOrderAndGoodsDomain : teacherShipOrderNormalList) {
                        shipOrderAndGoodsDomain.setSequenceNo(sequenceMap.get(shipOrderAndGoodsDomain.getExpressId()));
                    }
                }

                targetName = "教师_普通快递_小件";
                generatePDF(path, pdfFolder, Constants.SHIP_ORDER_DELIVERY_NORMAL, targetName, teacherShipOrderNormalList);
                ExportExcelUtil.createTeacherShipOrderWorkbook(orderExcelFolder.getPath() + "/教师普通_小件发货单.xlsx", teacherShipOrderNormalList, sequenceMap);
            }
        }

        // Zip
        zipSimpleFolder(excelFolder, "", zipFolder.getPath() + "/" + excelZipFileName.toString());
        zipSimpleFolder(pdfFolder, "", zipFolder.getPath() + "/" + pdfZipFileName.toString());
        zipSimpleFolder(orderExcelFolder, "", zipFolder.getPath() + "/" + orderExcelZipFileName.toString());
        zipSimpleFolder(zipFolder, "", path + "/" + exportFile.getFileGuid() + "." + exportFile.getFileSuffix());

        // delete temp dir
        // FileUtil.deletefile(excelFolder.getPath());
        // FileUtil.deletefile(pdfFolder.getPath());
        // FileUtil.deletefile(orderExcelFolder.getPath());
        // FileUtil.deletefile(zipFolder.getPath());

        // 保存文件信息
        exportFileService.insertSelective(exportFile);
    }

    /**
     * 按省创建学生发货单
     * @param path                  存储文件的绝对路径
     * @param expressCompany
     * @param excelFolder
     * @param pdfFolder             PDF文件临时目录
     * @param studentProvinceFolder 按省生成学生PDF文件临时目录
     * @param studentEMSFolder      学生EMS PDF文件临时目录
     * @param shipOrder             发货单查询条件     @throws Exception
     * @param sequenceEMSMap
     **/
    private void createStudentPDFByProvince(String path, String expressCompany, File excelFolder, File pdfFolder,
                                            File studentProvinceFolder, File studentEMSFolder, ShipOrder shipOrder,
                                            Map<Long, Integer> sequenceEMSMap, Integer provinceIndex, File orderExcelFolder) throws Exception {
        int index = 1;
        String targetName;
        if (logger.isDebugEnabled()) {
            logger.debug("Create PDF for student: {}", shipOrder.getProvince());
        }
        File provinceFolder = touchDirs(studentProvinceFolder.getPath() + "/" + shipOrder.getProvince());
        File provinceEMSFolder = touchDirs(studentEMSFolder.getPath() + "/" + shipOrder.getProvince());
        List<SchoolOrderDomain> schoolList = selectSchoolListByBatchNoAndProvince(shipOrder.getBatchNo(), shipOrder.getProvince(), Constants.SHIP_ORDER_DELIVERY_EMS);

        String delivery = shipOrder.getDelivery();      // 配送方式 过滤条件
        String excelFileName = null;                    // 快递单 excel 文件名

        if (StringUtils.isBlank(delivery) || delivery.equalsIgnoreCase(Constants.SHIP_ORDER_DELIVERY_EMS)) {
            shipOrder.setDelivery(Constants.SHIP_ORDER_DELIVERY_EMS);
            for (SchoolOrderDomain school : schoolList) {
                // 学生EMS
                shipOrder.setSchool(school.getSchool());
                shipOrder.setExpressId(school.getExpressId());
                targetName = "_" + shipOrder.getProvince() + "_学生_EMS_";
                List<ShipOrderAndGoodsDomain> studentShipOrderEMSList = selectStudentShipOrder(shipOrder);

                if (sequenceEMSMap.size() > 0 && studentShipOrderEMSList != null && studentShipOrderEMSList.size() > 0) {
                    /*for (ShipOrderAndGoodsDomain shipOrderAndGoodsDomain : studentShipOrderEMSList) {
                        shipOrderAndGoodsDomain.setSequenceNo(sequenceEMSMap.get(shipOrderAndGoodsDomain.getExpressId()));
                    }*/

                    generatePDF(path, provinceEMSFolder, index, school, Constants.SHIP_ORDER_DELIVERY_EMS, sequenceEMSMap.get(studentShipOrderEMSList.get(0).getExpressId()), targetName, studentShipOrderEMSList);

                    String orderExcelName = orderExcelFolder.getPath() + "/" + shipOrder.getProvince()
                            + "_" + sequenceEMSMap.get(studentShipOrderEMSList.get(0).getExpressId())
                            + "_" + school.getSchool() + "_" + school.getExpressId() + "_学生EMS发货单.xlsx";
                    ExportExcelUtil.createStudentShipOrderWorkbook(orderExcelName, studentShipOrderEMSList, sequenceEMSMap.get(studentShipOrderEMSList.get(0).getExpressId()));
                }

                index++;
                shipOrder.setSchool(null);
            }
            PdfHelper.mergePDFFiles(provinceEMSFolder.getPath(), studentEMSFolder.getPath() + "/" + provinceIndex + "_" + shipOrder.getProvince() + "_学生_EMS.pdf");
            // FileUtil.deletefile(provinceEMSFolder.getPath());
        }
        index = 1;
        Map<Long, Integer> sequenceMap = new HashMap<>();
        if (StringUtils.isBlank(delivery) || delivery.equalsIgnoreCase(Constants.SHIP_ORDER_DELIVERY_NORMAL)) {

            shipOrder.setDelivery(Constants.SHIP_ORDER_DELIVERY_NORMAL);
            excelFileName = excelFolder.getPath() + "/" + shipOrder.getProvince() + "学生普通.xlsx";
            List<ShipOrderAndGoodsDomain> studentNormalDeliveryList = this.selectStudentDelivery(shipOrder, shipOrder.getProvince());

            sequenceMap.clear();
            if (studentNormalDeliveryList != null) {
                for (int i = 0; i < studentNormalDeliveryList.size(); i++) {
                    sequenceMap.put(studentNormalDeliveryList.get(i).getExpressId(), i + 1);
                }
                ExportExcelUtil.createDeliveryWorkbook(excelFileName, studentNormalDeliveryList, expressCompany);
                // ExportExcelUtil.createStudentShipOrderWorkbook(orderExcelFolder.getPath() + "/" + shipOrder.getProvince() + "学生普通发货单.xlsx", studentNormalDeliveryList, sequenceMap);
            }

            // sequenceMap.forEach((key, value) -> System.out.println(key + " => " + value));

            // 设置打印快递单状态
            studentNormalDeliveryList = null;

            schoolList = selectSchoolListByBatchNoAndProvince(shipOrder.getBatchNo(), shipOrder.getProvince(), Constants.SHIP_ORDER_DELIVERY_NORMAL);
            for (SchoolOrderDomain school : schoolList) {
                // 学生普通
                targetName = "_学生_普通快递_";
                shipOrder.setSchool(school.getSchool());
                shipOrder.setExpressId(school.getExpressId());
                List<ShipOrderAndGoodsDomain> studentShipOrderNormalList = selectStudentShipOrder(shipOrder);

                if (sequenceMap.size() > 0 && studentShipOrderNormalList != null && studentShipOrderNormalList.size() > 0) {
                    /*for (ShipOrderAndGoodsDomain shipOrderAndGoodsDomain : studentShipOrderNormalList) {
                        shipOrderAndGoodsDomain.setSequenceNo(sequenceMap.get(shipOrderAndGoodsDomain.getExpressId()));
                    }*/
                    if (logger.isDebugEnabled()) {
                        logger.debug("序列: {} {} 索引: {} 学校: {} 快递单ID: {}", shipOrder.getProvince(), sequenceMap.get(studentShipOrderNormalList.get(0).getExpressId()), index, shipOrder.getSchool(), school.getExpressId());
                    }
                    generatePDF(path, provinceFolder, index, school, Constants.SHIP_ORDER_DELIVERY_NORMAL, sequenceMap.get(studentShipOrderNormalList.get(0).getExpressId()), targetName, studentShipOrderNormalList);

                    String orderExcelName = orderExcelFolder.getPath() + "/" + shipOrder.getProvince()
                            + "_" + sequenceMap.get(studentShipOrderNormalList.get(0).getExpressId())
                            + "_" + school.getSchool() + "_" + school.getExpressId() + "_学生普通发货单.xlsx";
                    ExportExcelUtil.createStudentShipOrderWorkbook(orderExcelName, studentShipOrderNormalList, sequenceMap.get(studentShipOrderNormalList.get(0).getExpressId()));
                }

                index++;
                shipOrder.setSchool(null);
            }
            PdfHelper.mergePDFFiles(provinceFolder.getPath(), pdfFolder.getPath() + "/" + shipOrder.getProvince() + "学生.pdf");
        }
    }

    /**
     * 创建学生发货单PDF文件
     * @param path              存储文件的绝对路径
     * @param pdfFolder         PDF文件临时目录
     * @param index             文件创建序号
     * @param school            学校
     * @param delivery          发货方式
     * @param sequenceNo
     *@param targetName        存储文件名称
     * @param shipOrderList     数据   @throws Exception
     */
    private void generatePDF(String path, File pdfFolder, int index, SchoolOrderDomain school, String delivery, int sequenceNo, String targetName, List<ShipOrderAndGoodsDomain> shipOrderList) throws Exception {
        Map<Object, Object> data = new HashMap<>();
        if (!shipOrderList.isEmpty()) {
            data.put("title", "发货单");
            data.put("express", expressService.selectByPrimaryKey(school.getExpressId()));
            data.put("school", school.getSchool() + "." + delivery + "." + sequenceNo);

            Set<String> teachers = shipOrderList.stream()
                    .map(ShipOrder::getConsignee)
                    .collect(Collectors.toCollection(LinkedHashSet<String>::new));

            data.put("data", shipOrderList);
            data.put("teachers", teachers);

            generateToFile(pdfTemplatePath, SHIP_ORDER_PDF_TEMPLATE_NAME, path, data, pdfFolder.getPath() + "/" + index + targetName + school.getSchool() + ".pdf");
        }
    }

    /**
     * 创建教师发货单PDF文件
     * @param path              存储文件的绝对路径
     * @param pdfFolder         PDF文件临时目录
     * @param delivery          发货方式
     * @param targetName        存储文件名称
     * @param shipOrderList     数据
     * @throws Exception
     */
    private void generatePDF(String path, File pdfFolder, String delivery, String targetName, List<ShipOrderAndGoodsDomain> shipOrderList) throws Exception {
        Map<Object, Object> data = new HashMap<>();
        if (!shipOrderList.isEmpty()) {
            data.put("title", "发货单");
            data.put("school",  targetName);
            data.put("data", shipOrderList);
            data.put("teachers", new HashSet<>());

            generateToFile(pdfTemplatePath, SHIP_ORDER_TEACHER_PDF_TEMPLATE_NAME, path + "/", data, pdfFolder.getPath() + "/" + targetName + ".pdf");
        }
    }
}
