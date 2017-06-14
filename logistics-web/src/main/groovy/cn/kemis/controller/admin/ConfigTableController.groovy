package cn.kemis.controller.admin

import cn.kemis.domain.api.base.BasePageResponse
import cn.kemis.domain.api.base.BaseResponse
import cn.kemis.exceptions.KemisException
import cn.kemis.model.SysConfiguration
import cn.kemis.service.SysConfigurationService
import com.github.pagehelper.PageInfo
import groovy.util.logging.Slf4j
import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.ModelAndView
/**
 *
 * @author 刘体阳 jefferlzu@gmail.com
 * Created on 2016-10-30
 */
@RestController
@Slf4j
@RequestMapping("admin/systemConfig/configTable")
class ConfigTableController {

    @Autowired
    private SysConfigurationService sysConfigurationService

    @GetMapping("")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    ModelAndView configTable() {
        new ModelAndView("admin/systemConfig/configTable")
    }

    @GetMapping("list")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    BasePageResponse list(
            @RequestParam("start") int start, @RequestParam("limit") int limit, @RequestParam("draw") int draw,
            @RequestParam("propertyKey") String propertyKey, @RequestParam("propertyValue") String propertyValue,
            @ModelAttribute("orderColumn") String orderColumn, @ModelAttribute("orderDir") String orderDir) {
        def map = new HashMap<String, Object>()

        PageInfo<SysConfiguration> pageInfo = new PageInfo<>()

        try {
            pageInfo.setPages(draw)
            pageInfo.setPageNum(start)
            pageInfo.setSize(limit)

            if (StringUtils.isNotBlank(orderColumn) && StringUtils.isNotBlank(orderDir)) {
                    pageInfo.orderBy = "CONVERT(" + (orderColumn == "desc" ? "`" + orderColumn + "`": orderColumn) + " USING gbk ) " + orderDir
            }

            SysConfiguration sysConfiguration = new SysConfiguration()
            sysConfiguration.propertyKey = propertyKey
            sysConfiguration.propertyValue = propertyValue

            def list = new ArrayList<SysConfiguration>()
            list.add(sysConfiguration)
            pageInfo.setList(list)

            pageInfo = sysConfigurationService.searchConfigurationList(pageInfo)

        } catch (KemisException e) {
            map.code = -2
            map.msg = e.getMessage()
        } catch (Exception e) {
            e.printStackTrace()
            log.error("--> search error || " + e.message)
            map.code = -1
            map.msg = e.getMessage()
        }

        BasePageResponse basePageResponse = new BasePageResponse()
        basePageResponse.data = pageInfo

        basePageResponse
    }

    @PostMapping("save")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    BaseResponse save(@RequestBody SysConfiguration sysConfiguration){
        sysConfigurationService.save(sysConfiguration)
    }

    @PostMapping("update")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    BaseResponse update(@RequestBody SysConfiguration sysConfiguration){
        sysConfigurationService.update(sysConfiguration)
    }

    @DeleteMapping("delete")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    BaseResponse delete(@RequestBody SysConfiguration sysConfiguration){
        sysConfigurationService.delete(sysConfiguration)
    }
}
