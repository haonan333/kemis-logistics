package cn.kemis.domain.excelEntity;

import org.jeecgframework.poi.excel.annotation.Excel;

import java.io.Serializable;

/**
 * Created by xuhailong on 16/8/10.
 */

public class ShipOrderEntity implements Serializable {

    @Excel(name = "用户ID",orderNum = "1")
    private String userId;

    @Excel(name = "用户姓名",orderNum = "2")
    private String userName;

    @Excel(name = "省",orderNum = "3")
    private String province;

    @Excel(name = "市",orderNum = "4")
    private String city;

    @Excel(name = "区",orderNum = "5")
    private String district;

    @Excel(name = "学校",orderNum = "6")
    private String school;

    @Excel(name = "班级",orderNum = "7")
    private String theClass;

    @Excel(name = "收货人",orderNum = "8")
    private String consignee;

    @Excel(name = "电话",orderNum = "9")
    private String mobile;

    @Excel(name = "配送方式",orderNum = "10")
    private String delivery;

    @Excel(name = "奖品明细",orderNum = "11")
    private String shipGoods;

    @Excel(name = "详细地址",orderNum = "12")
    private String address;

    @Excel(name = "奖品数量",orderNum = "13")
    private int totalCount;

    @Excel(name = "单位",orderNum = "14")
    private String unitCredits;

    @Excel(name = "老师科目",orderNum = "15")
    private String subject;

//    @Excel(name = "是否校园大使",replace = {"是_true","否_false"},orderNum = "16")
//    private boolean campusAmbassador;
    @Excel(name = "是否校园大使")
    private String campusAmbassador;

    @Excel(name = "快递单ID",orderNum = "17")
    private long expressId;

    @Excel(name = "学校ID")
    private String schoolId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getTheClass() {
        return theClass;
    }

    public void setTheClass(String theClass) {
        this.theClass = theClass;
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

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public String getShipGoods() {
        return shipGoods;
    }

    public void setShipGoods(String shipGoods) {
        this.shipGoods = shipGoods;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public String getUnitCredits() {
        return unitCredits;
    }

    public void setUnitCredits(String unitCredits) {
        this.unitCredits = unitCredits;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getCampusAmbassador() {
        return campusAmbassador;
    }

    public void setCampusAmbassador(String campusAmbassador) {
        this.campusAmbassador = campusAmbassador;
    }

    public long getExpressId() {
        return expressId;
    }

    public void setExpressId(long expressId) {
        this.expressId = expressId;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }
}
