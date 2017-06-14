package cn.kemis.tools.batch.processor;

import cn.kemis.Constants;
import cn.kemis.model.ReplaceKeywords;
import cn.kemis.model.ShipOrder;
import cn.kemis.model.ShipOrderGoods;
import cn.kemis.model.ShipOrderSource;
import cn.kemis.service.ReplaceKeywordsService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ShipOrderSourceItemProcessor implements ItemProcessor<ShipOrderSource, Map<String, Object>> {

    private static final Logger log = LoggerFactory.getLogger(ShipOrderSourceItemProcessor.class);

    @Autowired
    private ReplaceKeywordsService keywordsService;

    @Override
    public Map<String, Object> process(final ShipOrderSource shipOrderSource) throws Exception {
        Map<String, Object> map = new HashMap<>();

        ShipOrder shipOrder = new ShipOrder();
        BeanUtils.copyProperties(shipOrderSource, shipOrder);

        shipOrder.setShipOrderId(shipOrderSource.getShipOrderSourceId());

        // 替换
        List<ReplaceKeywords> goodsReplaceKeywordsList = keywordsService.selectByType(Constants.REPLACE_KEYWORDS_TYPE_GOODS);

        if (StringUtils.isNotBlank(shipOrder.getUserName()))
            shipOrder.setUserName(replaceSpecialSymbol(shipOrder.getUserName()));

        // 读取 Excel 文件 学校ID 可能产生小数，此处修正
        if (shipOrder.getSchoolId() != null && shipOrder.getSchoolId().contains(".")) {
            shipOrder.setSchoolId(shipOrder.getSchoolId().split("\\.")[0]);
        }

        if (StringUtils.isNotBlank(shipOrder.getSchool()))
            shipOrder.setSchool(replaceSpecialSymbol(shipOrder.getSchool()));

        if (StringUtils.isNotBlank(shipOrder.getTheClass()))
            shipOrder.setTheClass(replaceSpecialSymbol(shipOrder.getTheClass()));

        if (StringUtils.isNotBlank(shipOrder.getAddress()))
            shipOrder.setAddress(replaceSpecialSymbol(shipOrder.getAddress()));

        if (StringUtils.isNotBlank(shipOrder.getDelivery()))
            shipOrder.setDelivery(shipOrder.getDelivery().replace("请选择", "普通快递"));

        // 读取 Excel 文件 userId 可能产生小数，此处修正
        if (shipOrder.getUserId().contains(".")) {
            shipOrder.setUserId(shipOrder.getUserId().split("\\.")[0]);
        }

        // userId 以 1 8 开头的是老师 2 家长 3 4 5 学生
        // TODO 十月份再加入家长身份及判断
        if (shipOrder.getUserId().startsWith("1") || shipOrder.getUserId().startsWith("8")) {
            shipOrder.setTeacherTag(true);
        } else {
            shipOrder.setTeacherTag(false);
        }
        /*if (StringUtils.isBlank(shipOrder.getUnitCredits()) || shipOrder.getUnitCredits().equals("学豆")) {
            shipOrder.setTeacherTag(false);
        } else {
            shipOrder.setTeacherTag(true);
        }*/

        shipOrder.setStatus((byte) 1);

        // log.info("Converting (" + shipOrderSource + ") into (" + shipOrder + ")");

        // convert to Goods ...
        List<ReplaceKeywords> bigPackageKeywordsList = keywordsService.selectByType(Constants.REPLACE_KEYWORDS_TYPE_BIG_PACKAGE);

        List<ShipOrderGoods> shipOrderGoodsList = new ArrayList<>();
        ShipOrderGoods shipOrderGoods = null;
        String shipGoodsStr = shipOrderSource.getShipGoods();
        if (StringUtils.isNotBlank(shipGoodsStr)) {

            shipOrder.setBigPackage(false);
            // 辨识大小包
            for (ReplaceKeywords bigPackage : bigPackageKeywordsList) {
                if (shipGoodsStr.contains(bigPackage.getKeyword())) {
                    shipOrder.setBigPackage(true);
                    break;
                }
            }


            // 关键字替换
            shipGoodsStr = shipGoodsStr.replaceAll("\"", "").replaceAll(" ", "");
            for (ReplaceKeywords replaceKeywords : goodsReplaceKeywordsList) {
                shipGoodsStr = shipGoodsStr.replaceAll(replaceKeywords.getKeyword(), "");
            }

            String[] strings = shipGoodsStr.split("\n");
            for (String string : strings) {
                String[] goodsSplit = string.split("_X");
                shipOrderGoods = new ShipOrderGoods();
                shipOrderGoods.setShipOrderId(shipOrder.getShipOrderId());
                shipOrderGoods.setCreateTime(shipOrder.getCreateTime());
                shipOrderGoods.setUpdateTime(shipOrder.getUpdateTime());

                if (goodsSplit.length > 1) {
                    shipOrderGoods.setGoodsName(goodsSplit[0]);
                    shipOrderGoods.setGoodsCount(Short.valueOf(goodsSplit[1]));
                } else {
                    shipOrderGoods.setGoodsName(goodsSplit[0]);
                }
                shipOrderGoodsList.add(shipOrderGoods);
                // log.info("Converting into (" + shipOrderGoods + ")");
            }
        }

        map.put("shipOrder", shipOrder);
        map.put("shipOrderGoodsList", shipOrderGoodsList);

        return map;
    }

    private String replaceSpecialSymbol(String string) {
        return string.replaceAll(" ", "").replaceAll("&nbsp;", "")
                .replaceAll("&", "").replaceAll("\"", "")
                .replaceAll("'", "").replaceAll("\\.", "")
                .replaceAll(" ", "").replaceAll("\t", "")
                .replaceAll("<", "").replaceAll(">", "")
                .replaceAll(",", "").replaceAll("\n", "");
    }

}