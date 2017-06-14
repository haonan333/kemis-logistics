package cn.kemis.domain.excelEntity;

import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * Created by xuhailong on 16/8/18.
 */
public class ExpressPriceEntity {

    @Excel(name = "快递单ID")
    private Long expressId;

    @Excel(name = "价格")
    private String price;

    @Excel(name = "重量")
    private String weight;

    public Long getExpressId() {
        return expressId;
    }

    public void setExpressId(Long expressId) {
        this.expressId = expressId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }
}

