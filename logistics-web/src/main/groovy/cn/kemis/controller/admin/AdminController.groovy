package cn.kemis.controller.admin

import groovy.util.logging.Slf4j
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

/**
 * Created by liutiyang on 16/8/3.
 */
@Controller
@Slf4j
@RequestMapping("/admin")
class AdminController {

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    String idx() {
        "redirect:/admin/"
    }

    @GetMapping("/")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    String index() {
        if (log.isDebugEnabled()) {
            log.debug("/admin/index")
        }
        "admin/index"
    }
}
