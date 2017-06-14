package cn.kemis.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Set;

@Controller
@RequestMapping("/")
public class IndexController {

/*    @GetMapping("/hello")
    public String hello() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getPrincipal() instanceof UserDetails) {
            SysUser user = (SysUser) auth.getPrincipal();
            System.out.println(user.getEmail());
        }

        //本段代码演示如何获取登录的用户资料
        return "hello";
    }*/


    @GetMapping("/")
    public String idx() {

        Set<String> roles = AuthorityUtils.authorityListToSet(SecurityContextHolder.getContext()
                .getAuthentication().getAuthorities());

        if (roles.contains("ROLE_ADMIN")) {
            return "redirect:/admin/";
        } else {
            return "index";
        }
    }

    @GetMapping("/login")
    ModelAndView login(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (StringUtils.isNotBlank(auth.getName()) && !"anonymousUser".equals(auth.getName())) {
            return new ModelAndView("redirect:/");
        } else {
            return new ModelAndView("login");
        }
    }
}
