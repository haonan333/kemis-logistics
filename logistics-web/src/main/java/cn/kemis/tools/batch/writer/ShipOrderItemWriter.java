package cn.kemis.tools.batch.writer;

import cn.kemis.model.ShipOrder;
import cn.kemis.model.ShipOrderGoods;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Writes ShipOrder and ShipOrderGoods to a database
 */
@Component
public class ShipOrderItemWriter implements ItemWriter<Map<String, Object>> {

    private static final Log LOGGER = LogFactory.getLog(ShipOrderItemWriter.class);

    private static final String INSERT_SHIP_ORDER = "INSERT INTO `ship_order` (`shipOrderId`, `batchNo`, `userId`, `userName`, `province`, `city`, `district`, `schoolId`, `school`, `theClass`, `consignee`, `mobile`, `delivery`, `address`, `totalCount`, `unitCredits`, `subject`, `campusAmbassador`, `expressId`, `printExpress`, `outExpress`, `outVerify`, `teacherTag`, `bigPackage`, `status`, `createTime`, `updateTime`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, b'0', b'0', b'0', ?, ?, ?, ?, ?);";
    private static final String INSERT_SHIP_ORDER_GOODS = "INSERT INTO `ship_order_goods` (`shipOrderId`, `goodsName`, `goodsCount`, `createTime`, `updateTime`) VALUES (?, ?, ?, ?, ?);";

    @Autowired
    private JdbcTemplate jdbcTemplate;
    // @Autowired
    // private ShipOrderService shipOrderService;
    // @Autowired
    // private ShipOrderGoodsService shipOrderGoodsService;

    @Override
    public void write(List<? extends Map<String, Object>> ShipOrderList) throws Exception {
        for (Map<String, Object> shipOrderMap : ShipOrderList) {

            ShipOrder shipOrder = (ShipOrder) shipOrderMap.get("shipOrder");
            jdbcTemplate.update(INSERT_SHIP_ORDER, shipOrder.getShipOrderId(), shipOrder.getBatchNo(), shipOrder.getUserId(), shipOrder.getUserName(), shipOrder.getProvince(), shipOrder.getCity(), shipOrder.getDistrict(), shipOrder.getSchoolId(), shipOrder.getSchool(), shipOrder.getTheClass(), shipOrder.getConsignee(), shipOrder.getMobile(), shipOrder.getDelivery(), shipOrder.getAddress(), shipOrder.getTotalCount(), shipOrder.getUnitCredits(), shipOrder.getSubject(), shipOrder.getCampusAmbassador(), shipOrder.getExpressId(), shipOrder.getTeacherTag(), shipOrder.getBigPackage(), shipOrder.getStatus(), shipOrder.getCreateTime(), shipOrder.getUpdateTime());
            // shipOrderService.insertSelective(shipOrder);

            List<ShipOrderGoods> shipOrderGoodsList = (List<ShipOrderGoods>) shipOrderMap.get("shipOrderGoodsList");
            // shipOrderGoodsService.batchInsertSelective(shipOrderGoodsList, ShipOrderGoods.class);

            for (ShipOrderGoods shipOrderGoods : shipOrderGoodsList) {
                jdbcTemplate.update(INSERT_SHIP_ORDER_GOODS, shipOrderGoods.getShipOrderId(), shipOrderGoods.getGoodsName(), shipOrderGoods.getGoodsCount(), shipOrderGoods.getCreateTime(), shipOrderGoods.getUpdateTime());
            }
        }
    }
}
