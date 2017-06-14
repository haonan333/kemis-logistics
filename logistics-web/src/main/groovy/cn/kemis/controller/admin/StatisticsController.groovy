package cn.kemis.controller.admin

import cn.kemis.domain.api.base.BasePageResponse
import cn.kemis.domain.request.ExportSalaryRequest
import cn.kemis.exceptions.KemisException
import cn.kemis.service.StatisticsService
import groovy.json.JsonBuilder
import groovy.json.StringEscapeUtils
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
/**
 * Created by liutiyang on 16/8/3.
 */
@Controller
@Slf4j
@RequestMapping("/admin/statistics")
class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;
    /**
     * 进入统计页面
     * @return
     */
    @GetMapping("/piecework")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    String piecework() {
        "admin/statistics/piecework"
    }

    /**
     * 统计
     * @return
     */
    @GetMapping("/search")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseBody
    String search(HttpServletRequest request) {
        def json  = new JsonBuilder();
        def map = new HashMap<String,Object>();
        try {
            map = statisticsService.statistics(request);
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
     * 进入计件详情页面
     * @return
     */
    //    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/info")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    String info() {
        "admin/statistics/info"
    }

    /**
     * 计件详情
     * @return
     */
    @GetMapping("/infoSearch")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseBody
    BasePageResponse infoSearch(HttpServletRequest request) {
        statisticsService.infoSearch(request);
    }

    /**
     * 进入工资统计页面
     * @return
     */
    @GetMapping("/salary")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    String salary() {
        "admin/statistics/salary"
    }

    /**
     * 工资统计
     * @return
     */
    @GetMapping("/salarySearch")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @ResponseBody
    @Deprecated
    BasePageResponse salarySearch(HttpServletRequest request) {
        statisticsService.salarySearch(request);
    }

    /**
     * 导出工资统计
     * @return
     */
    @PostMapping("/exportSalary")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseBody
    void downExportExpress(HttpServletResponse response, @RequestBody ExportSalaryRequest request) {
        statisticsService.exportSalary(response, request)
    }

    /**
     * 进入未打包数据统计页面
     * @return
     */
    @GetMapping("/pieceworkUnPackage")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    String pieceworkUnPackage() {
        "admin/statistics/pieceworkUnPackage"
    }

    /**
     * 计件详情
     * @return
     */
    @GetMapping("/statisticsUnPackage")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseBody
    BasePageResponse statisticsUnPackage(HttpServletRequest request) {
        statisticsService.statisticsUnPackage(request);
    }
}
