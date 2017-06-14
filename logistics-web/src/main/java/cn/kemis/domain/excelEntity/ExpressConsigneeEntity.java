package cn.kemis.domain.excelEntity;

import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * ytz
 */
public class ExpressConsigneeEntity {

    @Excel(name = "快递单ID")
    private Long expressId;

    @Excel(name = "类型")
    private String type;

    @Excel(name = "配送方式")
    private String delivery;

    @Excel(name = "收货人")
    private String consignee;

    @Excel(name = "收货人电话")
    private String mobile;

    @Excel(name = "学校名称")
    private String school;

    @Excel(name = "详细地址")
    private String address;

    @Excel(name = "省")
    private String province;

    @Excel(name = "市")
    private String city;

    @Excel(name = "区")
    private String district;

    @Excel(name = "学校ID")
    private String schoolId;

    public Long getExpressId() {
        return expressId;
    }

    public void setExpressId(Long expressId) {
        this.expressId = expressId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }
}
