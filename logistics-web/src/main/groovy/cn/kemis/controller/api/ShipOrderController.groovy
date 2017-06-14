package cn.kemis.controller.api

import cn.kemis.exceptions.KemisException
import cn.kemis.service.ExpressService
import cn.kemis.service.ShipOrderGoodsService
import cn.kemis.service.ShipOrderService
import cn.kemis.service.StatisticsService
import groovy.json.JsonBuilder
import groovy.json.StringEscapeUtils
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody

import javax.servlet.http.HttpServletRequest

/**
 * Create by E450c-1 on 2017-05-21.
 */
@Controller
@Slf4j
@RequestMapping("/admin/shipOrders")
class ShipOrdersController {

    @Autowired
    private ShipOrderService shipOrderService;
    @Autowired
    private ExpressService expressService1;
    @Autowired
    private ShipOrderGoodsService shipOrderGoodsService;

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone()
    }
/**
 * 根据订单Id查询快递号、订单状态、订单号
 * @return
 */
    @PostMapping("/search")
    @ResponseBody
    String search(HttpServletRequest request) {
        def json = new JsonBuilder();
        def map = new HashMap<String, Object>()
        def s;
        try {
            map = shipOrderService.selectStatus(request)
            if (map != null) {
                s = 1 // 成功
            } else {
                s = 0 //失败
            }
        } catch (KemisException e) {
            map.code = -2
            map.msg = e.getMessage()
            s = 0//失败
        } catch (Exception e) {
            e.printStackTrace()
            log.error("--> search error || " + e.message)
            map.code = -1
            map.msg = e.getMessage()
            s = 0//失败
        }
        if (s == 0) {
            json {
                status s
                error map
            }
        }else{
            json {
                status s
                result map
            }
        }
        StringEscapeUtils.unescapeJava(json.toPrettyString())
    }


    /**
     * 订单数据的插入
     */

    @PostMapping("/insert")
    @ResponseBody
    String insert (HttpServletRequest request) {
        def json = new JsonBuilder();
        def map = new HashMap<String, Object>();
        def f,t,x,s;
        try {
            f = shipOrderService.insertShipOrder(request)
            t = expressService1.insertExpress(request)
            x = shipOrderGoodsService.insertShipOrderGoods(request)
            if(f == "true" && t == "true" && x == "true"){
                s = 1//成功
            }else{
                s = 0//失败
            }
        } catch (KemisException e) {
            map.code = -2;
            map.msg = e.getMessage()
            s = 0//失败
        } catch (Exception e) {
            e.printStackTrace()
            log.error("--> insert error || " + e.message)
            map.code = -1
            map.msg = e.getMessage()
            s = 0//失败
        }
        if(s == 0){
            json {
                status s
                error map
            }
        }else{
            json {
                status s
            }
        }

        StringEscapeUtils.unescapeJava(json.toPrettyString())

    }
}
