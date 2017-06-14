package cn.kemis.controller

import cn.kemis.domain.AvatarUploadDomain
import cn.kemis.domain.SysUserDomain
import cn.kemis.domain.api.base.BaseResponse
import cn.kemis.model.SysUser
import cn.kemis.security.SecurityUser
import cn.kemis.service.UserService
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody

/**
 * Created by liutiyang on 17/2/26.
 */
@Controller
@Slf4j
@RequestMapping("/userProfile")
class UserProfileController {

    @Autowired
    private UserService userService

    @GetMapping("/edit")
    String edit(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication()
        SysUser sysUser = userService.findById(((SecurityUser) auth.getPrincipal()).getSysUserId())
        sysUser.setPassword(null)

        model.addAttribute("user", sysUser)

        "userProfile/edit"
    }

    @PostMapping("/save")
    String save(@ModelAttribute("sysUserDomain") SysUserDomain sysUserDomain, Model model) {
        def auth = SecurityContextHolder.getContext().getAuthentication()
        sysUserDomain.setSysUserId(((SecurityUser)auth.getPrincipal()).getSysUserId())
        def baseResponse = userService.updateUser(sysUserDomain)

        SysUser sysUser = userService.findById(((SecurityUser) auth.getPrincipal()).getSysUserId())
        sysUser.setPassword(null)

        model.addAttribute("user", sysUser).addAttribute("baseResponse", baseResponse)

        "userProfile/edit"
    }

    @GetMapping("/password")
    String password() {
        "userProfile/password"
    }

    @PostMapping("/changePassword")
    String changePassword(@RequestParam(value = "password", required = true) String password,
                          @RequestParam(value = "newPassword", required = true) String newPassword,
                          @RequestParam(value = "newPassword2", required = true) String newPassword2,
                          Model model) {
        def auth = SecurityContextHolder.getContext().getAuthentication()
        def baseResponse = userService.changePassword(((SecurityUser) auth.getPrincipal()).getSysUserId(),
                password, newPassword, newPassword2)

        model.addAttribute("baseResponse", baseResponse)

        "userProfile/password"
    }

    @GetMapping("/avatar")
    String avatar() {
        "userProfile/avatar"
    }

    @PostMapping("/avatarUpload")
    @ResponseBody
    BaseResponse avatarUpload(@RequestBody AvatarUploadDomain avatarUploadDomain) {
        def auth = SecurityContextHolder.getContext().getAuthentication()
        userService.avatarUpload(((SecurityUser) auth.getPrincipal()).getSysUserId(), avatarUploadDomain)
    }

    @GetMapping("/avatarImage")
    @ResponseBody
    String getAvatarImage(@RequestParam(value = "size", required = false) String size) {
        def auth = SecurityContextHolder.getContext().getAuthentication()
        userService.getAvatarImage(((SecurityUser) auth.getPrincipal()).getSysUserId(), size)
    }

}
