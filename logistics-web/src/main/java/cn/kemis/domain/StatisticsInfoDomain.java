package cn.kemis.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * ytz 计件详情VO.
 */
public class StatisticsInfoDomain {

    private String expressAddress;
    private String expressSchool;
    private String expressConsignee;
    private String expressMobile;
    private String expressNumber;
    private String delivery;
    private String type;//老师快递 学生快递
    private String realName; //操作人
    private String workProcess;//工序
    private String username;
    private String goodsName;

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsName(){
        return goodsName;}
//    @DateTimeFormat(pattern ="yyyy-MM-dd HH:mm")
    @JsonFormat(pattern ="yyyy-MM-dd HH:mm", locale = "zh" , timezone="GMT+8")
    private Date createTime;//时间
    private int userId;
    private int workProcessId;

    public String getExpressConsignee() {
        return expressConsignee;
    }

    public void setExpressConsignee(String expressConsignee) {
        this.expressConsignee = expressConsignee;
    }

    public String getExpressMobile() {
        return expressMobile;
    }

    public void setExpressMobile(String expressMobile) {
        this.expressMobile = expressMobile;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getWorkProcessId() {
        return workProcessId;
    }

    public void setWorkProcessId(int workProcessId) {
        this.workProcessId = workProcessId;
    }

    public String getWorkProcess() {
        return workProcess;
    }

    public void setWorkProcess(String workProcess) {
        this.workProcess = workProcess;
    }

    public String getExpressNumber() {
        return expressNumber;
    }

    public void setExpressNumber(String expressNumber) {
        this.expressNumber = expressNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getExpressAddress() {
        return expressAddress;
    }

    public void setExpressAddress(String expressAddress) {
        this.expressAddress = expressAddress;
    }

    public String getExpressSchool() {
        return expressSchool;
    }

    public void setExpressSchool(String expressSchool) {
        this.expressSchool = expressSchool;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
