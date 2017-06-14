package cn.kemis.controller.admin

import cn.kemis.domain.SysUserDomain
import cn.kemis.domain.api.base.BasePageResponse
import cn.kemis.domain.api.base.BaseResponse
import cn.kemis.exceptions.KemisException
import cn.kemis.model.SysUser
import cn.kemis.service.RoleService
import cn.kemis.service.UserService
import com.github.pagehelper.PageInfo
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView
/**
 *
 * @author 刘体阳 jefferlzu@gmail.com
 * Created on 2016-10-30
 */
@RestController
@Slf4j
@RequestMapping("admin/systemConfig/userManager")
class UserManagerController {

    @Autowired
    private UserService userService

    @Autowired
    private RoleService roleService

    @GetMapping("")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    ModelAndView userManager() {
        new ModelAndView("admin/systemConfig/userManager").addObject("roleList", roleService.getAll())
    }

    @GetMapping("list")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    BasePageResponse list(
            @RequestParam("start") int start, @RequestParam("limit") int limit, @RequestParam("draw") int draw,
            @RequestParam("name") String name, @RequestParam("status") Byte status) {
        def map = new HashMap<String, Object>()

        PageInfo<SysUser> pageInfo = new PageInfo<>()

        try {
            pageInfo.setPages(draw)
            pageInfo.setPageNum(start)
            pageInfo.setSize(limit)

            SysUser user = new SysUser()
            user.realName = name
            user.status = status

            def list = new ArrayList<SysUser>()
            list.add(user)
            pageInfo.setList(list)

            pageInfo = userService.searchUserList(pageInfo)

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
    BaseResponse save(@RequestBody SysUserDomain sysUserDomain){
        userService.save(sysUserDomain)
    }

    @PostMapping("update")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    BaseResponse update(@RequestBody SysUserDomain sysUserDomain){
        userService.update(sysUserDomain)
    }

    @PostMapping("changePassword")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    BaseResponse changePassword(@RequestBody SysUserDomain sysUserDomain){
        userService.changePassword(sysUserDomain)
    }
}
