package cn.kemis.controller.admin

import cn.kemis.domain.CustomPageInfo
import cn.kemis.domain.ExpressWorkProcessDomain
import cn.kemis.domain.ShipOrderDomain
import cn.kemis.domain.api.base.BasePageResponse
import cn.kemis.domain.api.base.BaseResponse
import cn.kemis.domain.request.ExportDispatchedExpressRequest
import cn.kemis.domain.request.ExportExpressRequest
import cn.kemis.exceptions.KemisException
import cn.kemis.model.ExportFile
import cn.kemis.model.Express
import cn.kemis.service.ExpressService
import cn.kemis.service.ShipOrderService
import cn.kemis.service.batch.BatchService
import groovy.json.JsonBuilder
import groovy.json.StringEscapeUtils
import groovy.util.logging.Slf4j
import org.apache.commons.beanutils.BeanUtils
import org.apache.commons.io.FileUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
/**
 * Created by liutiyang on 16/8/3.
 */
@Controller
@Slf4j
@RequestMapping("/admin/shipOrder")
class ShipOrderController {

    @Autowired
    private ShipOrderService shipOrderService;

    @Autowired
    BatchService batchService;
    @Autowired
    private ExpressService expressService;

    @Value('${file.server.url.download}')
    private String downloadPath;

    /**
     * 进入导入发货单 页面
     * @return
     */
    @GetMapping("/import")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    String importOrder() {
        "admin/shipOrder/import"
    }

    /**
     * 上传发货单文件
     * @param file
     * @param batchNo
     * @return
     */
    @PostMapping("/importSource")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseBody
    String importSource(@RequestParam(value = "inputFile",required = true) MultipartFile file,
                   @RequestParam(value="batchNo", required=true) String batchNo) {
        def json  = new JsonBuilder();
        def map = new HashMap<String,Object>();

        try {

            shipOrderService.dealShipOrderSource(file,batchNo);
            //现阶段 导入完成 需新开线程 触发批处理 todo 正式环境需去掉
            Thread.start {
                println "执行批处理线程."
                batchService.convertShipOrder(batchNo);
            }

            map.code = 0
            map.msg = "成功"
        } catch (KemisException e) {
            map.code = -2
            map.msg = e.getMessage()
        } catch (Exception e) {
            e.printStackTrace()
            log.error("--> importSource || " + e.message);
            map.code = -1
            map.msg = e.getMessage()
        }

        json{
            data map
        }
        StringEscapeUtils.unescapeJava(json.toPrettyString())
    }

    /**
     * 进入发货单列表页
     * @return
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    String list() {
        "admin/shipOrder/list"
    }

    /**
     * 发货单列表
     * @return
     */
    @GetMapping("/search")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseBody
    String search(HttpServletRequest request) {
        def json  = new JsonBuilder();
        def map = new HashMap<String,Object>();
        try {
            map = shipOrderService.searchShipOrderList(request);
        } catch (KemisException e) {
            map.code = -2
            map.msg = e.getMessage()
        } catch (Exception e) {
            e.printStackTrace()
            log.error("--> search error || " + e.message);
            map.code = -1
            map.msg = e.getMessage()
        }

        json {
            data  map
        }
        StringEscapeUtils.unescapeJava(json.toPrettyString())
    }

    /**
     * 发货单列表编辑保存
     * @return
     */
    @PostMapping("/edit")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseBody
    String edit(HttpServletRequest request, @RequestBody ShipOrderDomain shipOrderDomain ) {
        def json  = new JsonBuilder();
        def map = new HashMap<String,Object>();
        try {
            map = shipOrderService.editShipOrder(shipOrderDomain);
        } catch (KemisException e) {
            map.code = -2
            map.msg = e.getMessage()
        } catch (Exception e) {
            e.printStackTrace()
            log.error("--> edit error || " + e.message);
            map.code = -1
            map.msg = e.getMessage()
        }

        json {
            data  map
        }
        StringEscapeUtils.unescapeJava(json.toPrettyString())
    }

    /**
     * 导入老师校验结果页面
     * @return
     */
    @GetMapping("/importVerifyTeacher")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    String importVerifyTeacher() {
        "admin/shipOrder/importVerifyTeacher"
    }

    /**
     * 上传老师校验结果
     * @return
     */
    @PostMapping("/uploadVerifyTeacher")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseBody
    String uploadVerifyTeacher(@RequestParam(value = "inputFile",required = true) MultipartFile file,
                           @RequestParam(value="batchNo", required=true) String batchNo) {
        def json  = new JsonBuilder();
        def map = new HashMap<String,Object>();
        try {
//            shipOrderService.dealFileVerifyTeacher(file,batchNo);
            map.code = 0
            map.msg = "成功"
        } catch (KemisException e) {
            map.code = -2
            map.msg = e.getMessage()
        } catch (Exception e) {
            e.printStackTrace()
            log.error("--> uploadVerifyTeacher || " + e.message);
            map.code = -1
            map.msg = e.getMessage()
        }

        json{
            data map
        }
        StringEscapeUtils.unescapeJava(json.toPrettyString())
    }

    /**
     * 进入导入快递单收货人页面
     * @return
     */
    @GetMapping("/importExpressConsignee")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    String importExpressConsignee() {
        "admin/shipOrder/importExpressConsignee"
    }

    /**
     * 上传快递单收货人文件
     * @return
     */
    @PostMapping("/uploadExpressConsignee")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseBody
    String uploadExpressConsignee(@RequestParam(value = "inputFile",required = true) MultipartFile file,
                           @RequestParam(value="batchNo", required=true) String batchNo) {
        def json  = new JsonBuilder();
        def map = new HashMap<String,Object>();
        try {
            shipOrderService.dealFileExpressConsignee(file,batchNo);
            map.code = 0
            map.msg = "成功"
        } catch (KemisException e) {
            map.code = -2
            map.msg = e.getMessage()
        } catch (Exception e) {
            e.printStackTrace()
            log.error("--> uploadExpressConsignee || " + e.message);
            map.code = -1
            map.msg = e.getMessage()
        }

        json{
            data map
        }
        StringEscapeUtils.unescapeJava(json.toPrettyString())
    }

    /**
     * 进入配送方式修改页面
     * @return
     */
    @GetMapping("/deliveryChange")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    String deliveryChange() {
        "admin/shipOrder/deliveryChange"
    }

    /**
     * 进入导入快递单号页面
     * @return
     */
    @GetMapping("/importExpressNo")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    String importExpressNo() {
        "admin/shipOrder/importExpressNo"
    }

    /**
     * 上传快递单号文件
     * @return
     */
    @PostMapping("/uploadExpressNo")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseBody
    String uploadExpressNo(@RequestParam(value = "inputFile",required = true) MultipartFile file,
                           @RequestParam(value="batchNo", required=true) String batchNo) {
        def json  = new JsonBuilder();
        def map = new HashMap<String,Object>();
        try {
            shipOrderService.dealFileExpressNo(file,batchNo);
            map.code = 0
            map.msg = "成功"
        } catch (KemisException e) {
            map.code = -2
            map.msg = e.getMessage()
        } catch (Exception e) {
            e.printStackTrace()
            log.error("--> uploadExpressNo || " + e.message);
            map.code = -1
            map.msg = e.getMessage()
        }

        json{
            data map
        }
        StringEscapeUtils.unescapeJava(json.toPrettyString())
    }

    /**
     * 进入导价格页面
     * @return
     */
    @GetMapping("/importExpressPrice")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    String importExpressPrice() {
        "admin/shipOrder/importExpressPrice"
    }

    /**
     * 上传导入快递单价格文件
     * @return
     */
    @PostMapping("/uploadExpressPrice")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseBody
    String uploadExpressPrice(@RequestParam(value = "inputFile",required = true) MultipartFile file,
                           @RequestParam(value="batchNo", required=true) String batchNo) {
        def json  = new JsonBuilder();
        def map = new HashMap<String,Object>();
        try {
            shipOrderService.dealFileExpressPrice(file,batchNo);
            map.code = 0
            map.msg = "成功"
        } catch (KemisException e) {
            map.code = -2
            map.msg = e.getMessage()
        } catch (Exception e) {
            e.printStackTrace()
            log.error("--> uploadExpressPrice || " + e.message);
            map.code = -1
            map.msg = e.getMessage()
        }

        json{
            data map
        }
        StringEscapeUtils.unescapeJava(json.toPrettyString())
    }

    @GetMapping("/dispatched")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    String dispatched() {
        "admin/shipOrder/dispatched"
    }

    /**
     * 扫描列表
     * @return
     */
    @GetMapping("/searchDispatched")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseBody
    BasePageResponse searchDispatched(@RequestParam("start") int start, @RequestParam("limit") int limit, @RequestParam("draw") int draw,
                  @RequestParam("queryDate") String queryDate, @RequestParam("expressSchool") String expressSchool,
                  @RequestParam("expressNumber") String expressNumber, @RequestParam("expressCompany") String expressCompany,
                  @RequestParam("delivery") String delivery, @RequestParam("expressAddress") String expressAddress,
                  @RequestParam("type") String type, @RequestParam("workProcess") String workProcess,
                  @RequestParam("expressConsignee") String expressConsignee, @RequestParam("expressMobile") String expressMobile,
                  @RequestParam("batchNo") String batchNo, @RequestParam("expressProvince") String expressProvince,
                  @RequestParam("expressCity") String expressCity, @RequestParam("expressDistrict") String expressDistrict) {

        ExportDispatchedExpressRequest request = new ExportDispatchedExpressRequest()
        request.queryDate = queryDate
        request.batchNo = batchNo
        request.expressProvince = expressProvince
        request.expressCity = expressCity
        request.expressDistrict = expressDistrict
        request.expressSchool = expressSchool
        request.expressNumber = expressNumber
        request.expressCompany = expressCompany
        request.delivery = delivery
        request.expressAddress = expressAddress
        request.workProcess = workProcess
        request.expressConsignee = expressConsignee
        request.expressMobile = expressMobile

        request.type = type
        request.draw = draw
        request.start = start
        request.limit = limit

        def pageInfo = expressService.searchByRequest(request);
        CustomPageInfo<ExpressWorkProcessDomain> customPageInfo =  new CustomPageInfo<>()
        BeanUtils.copyProperties(customPageInfo, pageInfo)

        customPageInfo.draw = request.draw

        BasePageResponse response = new BasePageResponse()
        response.data = customPageInfo;

        response
    }

    /**
     * 导出快递单压缩包
     * @return
     */
    @PostMapping("/downDispatchedExpress")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseBody
    void downDispatchedExpress(HttpServletResponse response,@RequestBody ExportDispatchedExpressRequest request) {

        ExportFile fileInfo = expressService.exportDispatchedExpress(request);
        if (fileInfo != null) {
            String fileName = fileInfo.getFileName();
            String file = downloadPath + fileInfo.getFileUrl();
            OutputStream os = response.getOutputStream();
            try {
                response.reset();
                response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
                response.setContentType("application/zip");
                os.write(FileUtils.readFileToByteArray(new File(file)));
                os.flush();
            } finally {
                if (os != null) {
                    os.close();
                }
            }
        }
    }

    /**
     * 导出快递单页面
     * @return
     */
    @GetMapping("/exportExpress")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    String exportExpress() {
        "admin/shipOrder/exportExpress"
    }

    /**
     * 导出快递单压缩包
     * @return
     */
    @PostMapping("/downExportExpress")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseBody
    void downExportExpress(HttpServletResponse response,@RequestBody ExportExpressRequest request) {
        expressService.downExportExpressZip(response, request)
    }


    /**
     * 修改学生单配送方式
     * @return
     */
    @PostMapping("/changeStudentDelivery")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseBody
    BaseResponse changeStudentDelivery(@RequestBody Express express) {
        expressService.changeStudentDelivery(express)
    }

}
