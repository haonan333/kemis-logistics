package cn.kemis.domain.excelEntity;

import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * ytz
 */
public class SalaryEntity {


//    @JsonFormat(pattern ="yyyy-MM-dd HH:mm", locale = "zh" , timezone="GMT+8")
//    @Excel(name = "时间") private Date createTime;
    @Excel(name = "员工编号") private String username;
    @Excel(name = "员工姓名") private String realName;
    @Excel(name = "划单数量") private int cutOrderCount;
    @Excel(name = "捡货数量") private int pickingGoodsCount;
    @Excel(name = "包装数量") private int wrappCount;
    @Excel(name = "打包数量") private int packageCount;
    @Excel(name = "划单工资") private String cutOrderSalary;
    @Excel(name = "捡货工资") private String pickingGoodsSalary;
    @Excel(name = "打包工资") private String wrappSalary;
    @Excel(name = "包装工资") private String packageSalary;
    @Excel(name = "合计工资") private String sumSalary;//合计

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
