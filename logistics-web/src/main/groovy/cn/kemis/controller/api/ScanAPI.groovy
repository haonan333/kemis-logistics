package cn.kemis.controller.api

import cn.kemis.domain.api.base.BaseResponse
import cn.kemis.domain.api.request.ScanInputRequest
import cn.kemis.service.WorkProcessService
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
/**
 * .
 * @author yt.zhang .
 * @version 1.0 .
 * @date 2016-08-15 00:53 .
 */
@Controller
@Slf4j
@RequestMapping("/scan")
class ScanAPI {
    
    @Autowired
    private WorkProcessService workProcessService;

    /**
     * 扫描枪录入接口
     * @return
     */
    @PostMapping("/input")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @ResponseBody
    BaseResponse input(@RequestBody ScanInputRequest request) {
        return this.workProcessService.scanCode(request.getCode(),request.getWorkProcess());
    }

}
