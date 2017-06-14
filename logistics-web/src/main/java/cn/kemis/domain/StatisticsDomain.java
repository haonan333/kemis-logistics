package cn.kemis.domain;

/**
 * Created by xuhailong on 16/8/24.
 */
public class StatisticsDomain {
    private String username;
    private String realName;
    private String workProcess;
    private int count; //订单数量
    private int countProcess;//处理条数
    private String orderType;
    private String packageType;
    private String salary;
    private String expressNumber;
    private int cutOrderCount;
    private int pickingGoodsCount;
    private int wrappCount;
    private int packageCount;
    private int shipCutOrderCount;
    private int shipPickingGoodsCount;
    private int shipWrappCount;
    private int shipPackageCount;

    public int getCountProcess() {
        return countProcess;
    }

    public void setCountProcess(int countProcess) {
        this.countProcess = countProcess;
    }

    public String getExpressNumber() {
        return expressNumber;
    }

    public void setExpressNumber(String expressNumber) {
        this.expressNumber = expressNumber;
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

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getPackageType() {
        return packageType;
    }

    public void setPackageType(String packageType) {
        this.packageType = packageType;
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

    public String getWorkProcess() {
        return workProcess;
    }

    public void setWorkProcess(String workProcess) {
        this.workProcess = workProcess;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }
}
