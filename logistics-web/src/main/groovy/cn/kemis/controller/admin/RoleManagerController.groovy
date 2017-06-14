package cn.kemis.controller.admin

import cn.kemis.domain.api.base.BasePageResponse
import cn.kemis.domain.api.base.BaseResponse
import cn.kemis.exceptions.KemisException
import cn.kemis.model.SysRole
import cn.kemis.service.ModuleService
import cn.kemis.service.RoleService
import com.github.pagehelper.PageInfo
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
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
@RequestMapping("admin/systemConfig/roleManager")
class RoleManagerController {

    @Autowired
    private RoleService roleService

    @Autowired
    private ModuleService moduleService

    @GetMapping("")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    ModelAndView roleManager() {
        new ModelAndView("admin/systemConfig/roleManager")
                .addObject("roleList", roleService.getAll())
                .addObject("moduleList", moduleService.getAll())
    }

    @GetMapping("list")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    BasePageResponse list(
            @RequestParam("start") int start, @RequestParam("limit") int limit, @RequestParam("draw") int draw,
            @RequestParam(value = "name", required = false) String name, @RequestParam(value = "roleDesc", required = false) String roleDesc,
            @RequestParam(value = "parentId", required = false) Integer parentId, @RequestParam(value = "permissionName", required = false) String permissionName) {
        def map = new HashMap<String, Object>();

        PageInfo<SysRole> pageInfo = new PageInfo<>()

        try {
            pageInfo.setPages(draw)
            pageInfo.setPageNum(start)
            pageInfo.setSize(limit)

            SysRole module = new SysRole()
            module.roleName = name
            module.roleDesc = roleDesc
            module.roleParentId = parentId
            module.permissionName = permissionName

            def list = new ArrayList<SysRole>()
            list.add(module)
            pageInfo.setList(list)

            pageInfo = roleService.searchRoleList(pageInfo);

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
    BaseResponse save(@RequestBody SysRole sysRole){
        roleService.save(sysRole)
    }

    @PostMapping("update")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    BaseResponse update(@RequestBody SysRole sysRole){
        roleService.update(sysRole)
    }

    @GetMapping("getModuleIdList/{roleId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    List<Integer> getModuleIdList(@PathVariable Integer roleId){
        roleService.getModuleIdList(roleId)
    }

    @PostMapping("authoritySave/{roleId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    BaseResponse authoritySave(@PathVariable Integer roleId, @RequestBody Integer[] moduleIds){
        roleService.authoritySave(roleId, moduleIds)
    }
}
