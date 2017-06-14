package cn.kemis.service;

import cn.kemis.dao.mapper.ShipOrderGoodsMapper;
import cn.kemis.exceptions.KemisException;
import cn.kemis.model.ShipOrderGoods;
import cn.kemis.model.ShipOrderGoodsExample;
import cn.kemis.util.Servlets;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.util.Map;

/**
 * @author 刘体阳 jefferlzu@gmail.com
 *         Created on 2016-08-14
 */
@Service
public class ShipOrderGoodsService extends BaseService<ShipOrderGoodsMapper, ShipOrderGoods, ShipOrderGoodsExample> {

    /**
     * 订单商品数据的插入
     */
    public String insertShipOrderGoods(HttpServletRequest request) {
        String f = "false";
        int ex = 0;
        Map map = Servlets.getParameterMap(request);

        ShipOrderGoods shipOrderGoods = new ShipOrderGoods();
        String gn = (String)map.get("goods_name");
        String q = (String)map.get("quantity");
        String o = (String)map.get("order_id");
        String[] s1 = gn.split(",");//转换 数组
        String[] s2 = q.split(",");//转换 数组
        String[] s3 = o.split(",");

        for(int i=0;i<=s1.length-1;i++){
            String str1 = s1[i];
            String str2 = s2[i];
            String str3 = s3[i];
            shipOrderGoods.setGoodsName(str1);
            shipOrderGoods.setGoodsCount(Short.valueOf(str2));
            shipOrderGoods.setShipOrderId(Long.parseLong(str3));
            ex = mapper.insertShipOrderGoods(shipOrderGoods);
        }
        if( ex > 0 ){
            f = "true";
        }
        return f;

    }

}
