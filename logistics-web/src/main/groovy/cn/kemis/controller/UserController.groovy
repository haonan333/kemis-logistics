package cn.kemis.controller

import cn.kemis.domain.api.base.BaseResponse
import cn.kemis.service.UserService
import groovy.json.JsonBuilder
import groovy.json.StringEscapeUtils
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

/**
 * Created by liutiyang on 16/7/30.
 */
@RestController
@Slf4j
@RequestMapping("/user")
class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/info/{id}")
    Map<String, Object> info(@PathVariable("id")Integer id) {
        if (log.isDebugEnabled()) {
            log.debug("id: ${id}")
        }
        println("id: ${id}")
        Map<String, Object> map = new HashMap<>()
        map.id = id

        map
    }

    @GetMapping("/infos/{uid}")
    String infos(@PathVariable("uid") Integer uid) {
        def json = new JsonBuilder();
        json.data {
            id uid
            date new Date()
            count 2
            countryName "中国"
            district "海淀区"
        }

        StringEscapeUtils.unescapeJava(json.toPrettyString())
    }


    @GetMapping("/usersForSelect")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseBody
    BaseResponse usersForSelect() {
        return userService.usersForSelect();
    }
}
