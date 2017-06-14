package cn.kemis.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * ytz 工资统计.
 */
public class StatisticSalaryDomain {
    private String userId;
    private String username;
    private String realName;
    @JsonFormat(pattern ="yyyy-MM-dd HH:mm", locale = "zh" , timezone="GMT+8")
    private Date createTime;
    private int cutOrderCount;
    private int pickingGoodsCount;
    private int wrappCount;
    private int packageCount;
    private int shipCutOrderCount;
    private int shipPickingGoodsCount;
    private int shipWrappCount;
    private int shipPackageCount;
    private String cutOrderSalary;
    private String pickingGoodsSalary;
    private String wrappSalary;
    private String packageSalary;
    private String sumSalary;//合计

    public int getShipCutOrderCount() {
        return shipCutOrderCount;
    }

    public void setShipCutOrderCount(int shipCutOrderCount) {
        this.shipCutOrderCount = shipCutOrderCount;
    }

    public int getShipPickingGoodsCount() {
        return shipPickingGoodsCount;
    }

    public void setShipPickingGoodsCount(int shipPickingGoodsCount) {
        this.shipPickingGoodsCount = shipPickingGoodsCount;
    }

    public int getShipWrappCount() {
        return shipWrappCount;
    }

    public void setShipWrappCount(int shipWrappCount) {
        this.shipWrappCount = shipWrappCount;
    }

    public int getShipPackageCount() {
        return shipPackageCount;
    }

    public void setShipPackageCount(int shipPackageCount) {
        this.shipPackageCount = shipPackageCount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getCutOrderCount() {
        return cutOrderCount;
    }

    public void setCutOrderCount(int cutOrderCount) {
        this.cutOrderCount = cutOrderCount;
    }

    public int getPickingGoodsCount() {
        return pickingGoodsCount;
    }

    public void setPickingGoodsCount(int pickingGoodsCount) {
        this.pickingGoodsCount = pickingGoodsCount;
    }

    public int getWrappCount() {
        return wrappCount;
    }

    public void setWrappCount(int wrappCount) {
        this.wrappCount = wrappCount;
    }

    public int getPackageCount() {
        return packageCount;
    }

    public void setPackageCount(int packageCount) {
        this.packageCount = packageCount;
    }

    public String getCutOrderSalary() {
        return cutOrderSalary;
    }

    public void setCutOrderSalary(String cutOrderSalary) {
        this.cutOrderSalary = cutOrderSalary;
    }

    public String getPickingGoodsSalary() {
        return pickingGoodsSalary;
    }

    public void setPickingGoodsSalary(String pickingGoodsSalary) {
        this.pickingGoodsSalary = pickingGoodsSalary;
    }

    public String getWrappSalary() {
        return wrappSalary;
    }

    public void setWrappSalary(String wrappSalary) {
        this.wrappSalary = wrappSalary;
    }

    public String getPackageSalary() {
        return packageSalary;
    }

    public void setPackageSalary(String packageSalary) {
        this.packageSalary = packageSalary;
    }

    public String getSumSalary() {
        return sumSalary;
    }

    public void setSumSalary(String sumSalary) {
        this.sumSalary = sumSalary;
    }
}
