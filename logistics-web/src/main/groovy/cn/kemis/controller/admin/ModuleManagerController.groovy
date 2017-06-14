package cn.kemis.controller.admin

import cn.kemis.domain.ModuleTreeNode
import cn.kemis.domain.api.base.BasePageResponse
import cn.kemis.domain.api.base.BaseResponse
import cn.kemis.exceptions.KemisException
import cn.kemis.model.SysModule
import cn.kemis.service.ModuleService
import com.github.pagehelper.PageInfo
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
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
@RequestMapping("admin/systemConfig/moduleManager")
class ModuleManagerController {

    @Autowired
    private ModuleService moduleService

    @GetMapping("")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    ModelAndView moduleManager() {
        new ModelAndView("admin/systemConfig/moduleManager").addObject("moduleList", moduleService.getAll())
    }

    @GetMapping("list")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    BasePageResponse list(
            @RequestParam("start") int start, @RequestParam("limit") int limit, @RequestParam("draw") int draw,
            @RequestParam(value = "name", required = false) String name, @RequestParam(value = "moduleUrl", required = false) String moduleUrl,
            @RequestParam(value = "parentId", required = false) Integer parentId, @RequestParam(value = "status", required = false) Byte status) {
        def map = new HashMap<String, Object>();

        PageInfo<SysModule> pageInfo = new PageInfo<>()

        try {
            pageInfo.setPages(draw)
            pageInfo.setPageNum(start)
            pageInfo.setSize(limit)

            SysModule module = new SysModule()
            module.moduleName = name
            module.moduleUrl = moduleUrl
            module.moduleParentId = parentId
            module.status = status

            def list = new ArrayList<SysModule>()
            list.add(module)
            pageInfo.setList(list)

            pageInfo = moduleService.searchModuleList(pageInfo);

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

    @PostMapping("save")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    BaseResponse save(@RequestBody SysModule sysModule){
        def baseResponse = moduleService.save(sysModule)
        baseResponse
    }

    @PostMapping("update")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    BaseResponse update(@RequestBody SysModule sysModule){
        moduleService.update(sysModule)
    }

    @GetMapping("getModuleTree")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    Collection<ModuleTreeNode> getModuleTree() {
        moduleService.getTree()
    }
}
