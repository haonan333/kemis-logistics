package cn.kemis.controller.admin

import cn.kemis.domain.api.base.BasePageResponse
import cn.kemis.exceptions.KemisException
import cn.kemis.model.ExportFile
import cn.kemis.model.ShipOrder
import cn.kemis.service.ExportFileService
import cn.kemis.service.ShipOrderService
import com.github.pagehelper.PageInfo
import groovy.json.JsonBuilder
import groovy.json.StringEscapeUtils
import groovy.util.logging.Slf4j
import org.apache.commons.io.FileUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 *
 * @author 刘体阳 jefferlzu@gmail.com
 * Created on 2016-08-21
 */
@Slf4j
@Controller
@RequestMapping("/admin/export")
class ExportFileController {

    @Value('${file.server.url.download}')
    private String downloadPath

    @Autowired
    private ExportFileService exportFileService
    @Autowired
    private ShipOrderService shipOrderService

    @GetMapping("/exportExpress")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    String exportExpress(HttpServletRequest request) {
        "admin/shipOrder/exportExpressList"
    }

    @PostMapping("/exportShipOrder")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseBody
    String exportShipOrder(@RequestBody ShipOrder shipOrder) {
        println shipOrder
        def json = new JsonBuilder()

        ExportFile file = shipOrderService.exportShipOrder(shipOrder)

        json {
            data file
        }

        StringEscapeUtils.unescapeJava(json.toPrettyString())
    }

    /**
     * 文件列表
     * @return
     */
    @GetMapping("/search")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseBody
    BasePageResponse search(@RequestParam("start") int start, @RequestParam("limit") int limit, @RequestParam("draw") int draw,
                  @RequestParam("fileName") fileName,
                  @RequestParam("fileGuid") String fileGuid, @RequestParam("type") String type) {
        def map = new HashMap<String, Object>();
        PageInfo<ExportFile> pageInfo = new PageInfo<>()
        try {
            pageInfo.setPages(draw)
            pageInfo.setPageNum(start)
            pageInfo.setSize(limit)

            ExportFile exportFile = new ExportFile()
            exportFile.fileGuid = fileGuid
            exportFile.fileName = fileName
            exportFile.type = type
            def list = new ArrayList<ExportFile>()
            list.add(exportFile)
            pageInfo.setList(list)

            pageInfo = exportFileService.searchExportFileList(pageInfo);

        } catch (KemisException e) {
            map.code = -2
            map.msg = e.getMessage()
        } catch (Exception e) {
            e.printStackTrace()
            log.error("--> search error || " + e.message);
            map.code = -1
            map.msg = e.getMessage()
        }

        BasePageResponse basePageResponse = new BasePageResponse()
        basePageResponse.data = pageInfo

        basePageResponse
    }

    @GetMapping("/download")
    void download(HttpServletResponse response, @RequestParam("exportFileId") Long exportFileId) {

        def exportFile = exportFileService.selectByPrimaryKey(exportFileId)

        OutputStream os = response.getOutputStream();
        try {

            File file = new File(downloadPath + exportFile.fileUrl)

            response.reset();
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(exportFile.getFileName() + "." + exportFile.getFileSuffix(), "UTF-8"));
            response.setContentType("application/octet-stream; charset=utf-8");
            os.write(FileUtils.readFileToByteArray(file));
            os.flush();
        } finally {
            if (os != null) {
                os.close();
            }
        }
    }

    @GetMapping("/dl")
    void dl(HttpServletResponse response) {

        OutputStream os = response.getOutputStream();
        try {

            InputStream is = this.getClass().getResourceAsStream("/templates/pdfTemplates/中文.xlsx");

            File temp = File.createTempFile("temp", ".tmp");
            temp.deleteOnExit();

            OutputStream fos = new FileOutputStream(temp);

            byte[] buffer = new byte[1024];
            int bytesRead;
            //read from is to buffer
            while ((bytesRead = is.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
            is.close();
            //flush OutputStream to write any buffered data to file
            fos.flush();
            fos.close();

            response.reset();
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("ship.xlsx", "UTF-8"));
            response.setContentType("application/octet-stream; charset=utf-8");
            os.write(FileUtils.readFileToByteArray(temp));
            os.flush();
        } finally {
            if (os != null) {
                os.close();
            }
        }
    }
}
