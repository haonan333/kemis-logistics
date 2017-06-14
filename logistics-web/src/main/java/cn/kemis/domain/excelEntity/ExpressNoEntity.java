package cn.kemis.domain.excelEntity;

import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * Created by xuhailong on 16/8/17.
 */
public class ExpressNoEntity {

    @Excel(name = "快递单ID")
    private Long expressId;

    @Excel(name = "物流单号")
    private String expressNumber;

    @Excel(name = "物流公司")
    private String expressCompany;

//    @Excel(name = "配送方式")
//    private String delivery;

    public Long getExpressId() {
        return expressId;
    }

    public void setExpressId(Long expressId) {
        this.expressId = expressId;
    }

    public String getExpressNumber() {
        return expressNumber;
    }

    public void setExpressNumber(String expressNumber) {
        this.expressNumber = expressNumber;
    }

    public String getExpressCompany() {
        return expressCompany;
    }

    public void setExpressCompany(String expressCompany) {
        this.expressCompany = expressCompany;
    }

//    public String getDelivery() {
//        return delivery;
//    }
//
//    public void setDelivery(String delivery) {
//        this.delivery = delivery;
//    }
}
