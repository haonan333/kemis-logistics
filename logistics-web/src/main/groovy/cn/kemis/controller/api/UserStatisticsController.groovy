package cn.kemis.controller.api

import cn.kemis.exceptions.KemisException
import cn.kemis.service.StatisticsService
import groovy.json.JsonBuilder
import groovy.json.StringEscapeUtils
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody

import javax.servlet.http.HttpServletRequest

/**
 * 用户统计
 * Created by xuhailong on 16/9/25.
 */
@Controller
@Slf4j
@RequestMapping("/user/statistics")
class UserStatisticsController {

    @Autowired
    private StatisticsService statisticsService;
    /**
     * 进入统计页面
     * @return
     */
    @GetMapping("/piecework")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    String piecework() {
        "user/statistics/piecework"
    }

    /**
     * 统计
     * @return
     */
    @GetMapping("/search")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @ResponseBody
    String search(HttpServletRequest request) {
        def json  = new JsonBuilder();
        def map = new HashMap<String,Object>();
        try {
             map = statisticsService.userStatistics(request);
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

}
