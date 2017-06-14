package cn.kemis.domain;

import cn.kemis.model.ShipOrder;

/**
 * Created by xuhailong on 16/8/15.
 */
public class ShipOrderDomain extends ShipOrder {

    private String goodsName;

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }
}
