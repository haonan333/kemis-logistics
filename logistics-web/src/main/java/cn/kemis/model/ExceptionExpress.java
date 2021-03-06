package cn.kemis.model;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table exception_express
 *
 * @mbggenerated do_not_delete_during_merge Thu Aug 25 16:10:29 CST 2016
 */
public class ExceptionExpress implements Serializable {
    /**
     * Database Column Remarks:
     *   主键
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column exception_express.exceptionExpressId
     *
     * @mbggenerated Thu Aug 25 16:10:29 CST 2016
     */
    private Long exceptionExpressId;

    /**
     * Database Column Remarks:
     *   批次号
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column exception_express.shipOrderId
     *
     * @mbggenerated Thu Aug 25 16:10:29 CST 2016
     */
    private Long shipOrderId;

    /**
     * Database Column Remarks:
     *   处理方式
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column exception_express.mode
     *
     * @mbggenerated Thu Aug 25 16:10:29 CST 2016
     */
    private String mode;

    /**
     * Database Column Remarks:
     *   省
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column exception_express.province
     *
     * @mbggenerated Thu Aug 25 16:10:29 CST 2016
     */
    private String province;

    /**
     * Database Column Remarks:
     *   市
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column exception_express.city
     *
     * @mbggenerated Thu Aug 25 16:10:29 CST 2016
     */
    private String city;

    /**
     * Database Column Remarks:
     *   区
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column exception_express.district
     *
     * @mbggenerated Thu Aug 25 16:10:29 CST 2016
     */
    private String district;

    /**
     * Database Column Remarks:
     *   学校
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column exception_express.school
     *
     * @mbggenerated Thu Aug 25 16:10:29 CST 2016
     */
    private String school;

    /**
     * Database Column Remarks:
     *   班级
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column exception_express.theClass
     *
     * @mbggenerated Thu Aug 25 16:10:29 CST 2016
     */
    private String theClass;

    /**
     * Database Column Remarks:
     *   收货人
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column exception_express.consignee
     *
     * @mbggenerated Thu Aug 25 16:10:29 CST 2016
     */
    private String consignee;

    /**
     * Database Column Remarks:
     *   收货人电话号码
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column exception_express.mobile
     *
     * @mbggenerated Thu Aug 25 16:10:29 CST 2016
     */
    private String mobile;

    /**
     * Database Column Remarks:
     *   配送方式： EMS，普通快递
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column exception_express.delivery
     *
     * @mbggenerated Thu Aug 25 16:10:29 CST 2016
     */
    private String delivery;

    /**
     * Database Column Remarks:
     *   地址
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column exception_express.address
     *
     * @mbggenerated Thu Aug 25 16:10:29 CST 2016
     */
    private String address;

    /**
     * Database Column Remarks:
     *   快递单ID
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column exception_express.expressId
     *
     * @mbggenerated Thu Aug 25 16:10:29 CST 2016
     */
    private Long expressId;

    /**
     * Database Column Remarks:
     *   物流单号
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column exception_express.expressNumber
     *
     * @mbggenerated Thu Aug 25 16:10:29 CST 2016
     */
    private String expressNumber;

    /**
     * Database Column Remarks:
     *   物流公司
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column exception_express.expressCompany
     *
     * @mbggenerated Thu Aug 25 16:10:29 CST 2016
     */
    private String expressCompany;

    /**
     * Database Column Remarks:
     *   是打印过快递单
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column exception_express.printExpress
     *
     * @mbggenerated Thu Aug 25 16:10:29 CST 2016
     */
    private Boolean printExpress;

    /**
     * Database Column Remarks:
     *   是否导出过快递单
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column exception_express.outExpress
     *
     * @mbggenerated Thu Aug 25 16:10:29 CST 2016
     */
    private Boolean outExpress;

    /**
     * Database Column Remarks:
     *   是否导出校验老师单
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column exception_express.outVerify
     *
     * @mbggenerated Thu Aug 25 16:10:29 CST 2016
     */
    private Boolean outVerify;

    /**
     * Database Column Remarks:
     *   状态 默认值 1。仅值为1的可以打印快递单发货。值为2已发货，3已签收，4异常件。
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column exception_express.status
     *
     * @mbggenerated Thu Aug 25 16:10:29 CST 2016
     */
    private Byte status;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column exception_express.createTime
     *
     * @mbggenerated Thu Aug 25 16:10:29 CST 2016
     */
    private Date createTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column exception_express.updateTime
     *
     * @mbggenerated Thu Aug 25 16:10:29 CST 2016
     */
    private Date updateTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table exception_express
     *
     * @mbggenerated Thu Aug 25 16:10:29 CST 2016
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table exception_express
     *
     * @mbggenerated Thu Aug 25 16:10:29 CST 2016
     */
    public ExceptionExpress(Long exceptionExpressId, Long shipOrderId, String mode, String province, String city, String district, String school, String theClass, String consignee, String mobile, String delivery, String address, Long expressId, String expressNumber, String expressCompany, Boolean printExpress, Boolean outExpress, Boolean outVerify, Byte status, Date createTime, Date updateTime) {
        this.exceptionExpressId = exceptionExpressId;
        this.shipOrderId = shipOrderId;
        this.mode = mode;
        this.province = province;
        this.city = city;
        this.district = district;
        this.school = school;
        this.theClass = theClass;
        this.consignee = consignee;
        this.mobile = mobile;
        this.delivery = delivery;
        this.address = address;
        this.expressId = expressId;
        this.expressNumber = expressNumber;
        this.expressCompany = expressCompany;
        this.printExpress = printExpress;
        this.outExpress = outExpress;
        this.outVerify = outVerify;
        this.status = status;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table exception_express
     *
     * @mbggenerated Thu Aug 25 16:10:29 CST 2016
     */
    public ExceptionExpress() {
        super();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column exception_express.exceptionExpressId
     *
     * @return the value of exception_express.exceptionExpressId
     *
     * @mbggenerated Thu Aug 25 16:10:29 CST 2016
     */
    public Long getExceptionExpressId() {
        return exceptionExpressId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column exception_express.exceptionExpressId
     *
     * @param exceptionExpressId the value for exception_express.exceptionExpressId
     *
     * @mbggenerated Thu Aug 25 16:10:29 CST 2016
     */
    public void setExceptionExpressId(Long exceptionExpressId) {
        this.exceptionExpressId = exceptionExpressId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column exception_express.shipOrderId
     *
     * @return the value of exception_express.shipOrderId
     *
     * @mbggenerated Thu Aug 25 16:10:29 CST 2016
     */
    public Long getShipOrderId() {
        return shipOrderId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column exception_express.shipOrderId
     *
     * @param shipOrderId the value for exception_express.shipOrderId
     *
     * @mbggenerated Thu Aug 25 16:10:29 CST 2016
     */
    public void setShipOrderId(Long shipOrderId) {
        this.shipOrderId = shipOrderId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column exception_express.mode
     *
     * @return the value of exception_express.mode
     *
     * @mbggenerated Thu Aug 25 16:10:29 CST 2016
     */
    public String getMode() {
        return mode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column exception_express.mode
     *
     * @param mode the value for exception_express.mode
     *
     * @mbggenerated Thu Aug 25 16:10:29 CST 2016
     */
    public void setMode(String mode) {
        this.mode = mode == null ? null : mode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column exception_express.province
     *
     * @return the value of exception_express.province
     *
     * @mbggenerated Thu Aug 25 16:10:29 CST 2016
     */
    public String getProvince() {
        return province;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column exception_express.province
     *
     * @param province the value for exception_express.province
     *
     * @mbggenerated Thu Aug 25 16:10:29 CST 2016
     */
    public void setProvince(String province) {
        this.province = province == null ? null : province.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column exception_express.city
     *
     * @return the value of exception_express.city
     *
     * @mbggenerated Thu Aug 25 16:10:29 CST 2016
     */
    public String getCity() {
        return city;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column exception_express.city
     *
     * @param city the value for exception_express.city
     *
     * @mbggenerated Thu Aug 25 16:10:29 CST 2016
     */
    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column exception_express.district
     *
     * @return the value of exception_express.district
     *
     * @mbggenerated Thu Aug 25 16:10:29 CST 2016
     */
    public String getDistrict() {
        return district;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column exception_express.district
     *
     * @param district the value for exception_express.district
     *
     * @mbggenerated Thu Aug 25 16:10:29 CST 2016
     */
    public void setDistrict(String district) {
        this.district = district == null ? null : district.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column exception_express.school
     *
     * @return the value of exception_express.school
     *
     * @mbggenerated Thu Aug 25 16:10:29 CST 2016
     */
    public String getSchool() {
        return school;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column exception_express.school
     *
     * @param school the value for exception_express.school
     *
     * @mbggenerated Thu Aug 25 16:10:29 CST 2016
     */
    public void setSchool(String school) {
        this.school = school == null ? null : school.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column exception_express.theClass
     *
     * @return the value of exception_express.theClass
     *
     * @mbggenerated Thu Aug 25 16:10:29 CST 2016
     */
    public String getTheClass() {
        return theClass;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column exception_express.theClass
     *
     * @param theClass the value for exception_express.theClass
     *
     * @mbggenerated Thu Aug 25 16:10:29 CST 2016
     */
    public void setTheClass(String theClass) {
        this.theClass = theClass == null ? null : theClass.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column exception_express.consignee
     *
     * @return the value of exception_express.consignee
     *
     * @mbggenerated Thu Aug 25 16:10:29 CST 2016
     */
    public String getConsignee() {
        return consignee;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column exception_express.consignee
     *
     * @param consignee the value for exception_express.consignee
     *
     * @mbggenerated Thu Aug 25 16:10:29 CST 2016
     */
    public void setConsignee(String consignee) {
        this.consignee = consignee == null ? null : consignee.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column exception_express.mobile
     *
     * @return the value of exception_express.mobile
     *
     * @mbggenerated Thu Aug 25 16:10:29 CST 2016
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column exception_express.mobile
     *
     * @param mobile the value for exception_express.mobile
     *
     * @mbggenerated Thu Aug 25 16:10:29 CST 2016
     */
    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column exception_express.delivery
     *
     * @return the value of exception_express.delivery
     *
     * @mbggenerated Thu Aug 25 16:10:29 CST 2016
     */
    public String getDelivery() {
        return delivery;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column exception_express.delivery
     *
     * @param delivery the value for exception_express.delivery
     *
     * @mbggenerated Thu Aug 25 16:10:29 CST 2016
     */
    public void setDelivery(String delivery) {
        this.delivery = delivery == null ? null : delivery.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column exception_express.address
     *
     * @return the value of exception_express.address
     *
     * @mbggenerated Thu Aug 25 16:10:29 CST 2016
     */
    public String getAddress() {
        return address;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column exception_express.address
     *
     * @param address the value for exception_express.address
     *
     * @mbggenerated Thu Aug 25 16:10:29 CST 2016
     */
    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column exception_express.expressId
     *
     * @return the value of exception_express.expressId
     *
     * @mbggenerated Thu Aug 25 16:10:29 CST 2016
     */
    public Long getExpressId() {
        return expressId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column exception_express.expressId
     *
     * @param expressId the value for exception_express.expressId
     *
     * @mbggenerated Thu Aug 25 16:10:29 CST 2016
     */
    public void setExpressId(Long expressId) {
        this.expressId = expressId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column exception_express.expressNumber
     *
     * @return the value of exception_express.expressNumber
     *
     * @mbggenerated Thu Aug 25 16:10:29 CST 2016
     */
    public String getExpressNumber() {
        return expressNumber;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column exception_express.expressNumber
     *
     * @param expressNumber the value for exception_express.expressNumber
     *
     * @mbggenerated Thu Aug 25 16:10:29 CST 2016
     */
    public void setExpressNumber(String expressNumber) {
        this.expressNumber = expressNumber == null ? null : expressNumber.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column exception_express.expressCompany
     *
     * @return the value of exception_express.expressCompany
     *
     * @mbggenerated Thu Aug 25 16:10:29 CST 2016
     */
    public String getExpressCompany() {
        return expressCompany;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column exception_express.expressCompany
     *
     * @param expressCompany the value for exception_express.expressCompany
     *
     * @mbggenerated Thu Aug 25 16:10:29 CST 2016
     */
    public void setExpressCompany(String expressCompany) {
        this.expressCompany = expressCompany == null ? null : expressCompany.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column exception_express.printExpress
     *
     * @return the value of exception_express.printExpress
     *
     * @mbggenerated Thu Aug 25 16:10:29 CST 2016
     */
    public Boolean getPrintExpress() {
        return printExpress;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column exception_express.printExpress
     *
     * @param printExpress the value for exception_express.printExpress
     *
     * @mbggenerated Thu Aug 25 16:10:29 CST 2016
     */
    public void setPrintExpress(Boolean printExpress) {
        this.printExpress = printExpress;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column exception_express.outExpress
     *
     * @return the value of exception_express.outExpress
     *
     * @mbggenerated Thu Aug 25 16:10:29 CST 2016
     */
    public Boolean getOutExpress() {
        return outExpress;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column exception_express.outExpress
     *
     * @param outExpress the value for exception_express.outExpress
     *
     * @mbggenerated Thu Aug 25 16:10:29 CST 2016
     */
    public void setOutExpress(Boolean outExpress) {
        this.outExpress = outExpress;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column exception_express.outVerify
     *
     * @return the value of exception_express.outVerify
     *
     * @mbggenerated Thu Aug 25 16:10:29 CST 2016
     */
    public Boolean getOutVerify() {
        return outVerify;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column exception_express.outVerify
     *
     * @param outVerify the value for exception_express.outVerify
     *
     * @mbggenerated Thu Aug 25 16:10:29 CST 2016
     */
    public void setOutVerify(Boolean outVerify) {
        this.outVerify = outVerify;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column exception_express.status
     *
     * @return the value of exception_express.status
     *
     * @mbggenerated Thu Aug 25 16:10:29 CST 2016
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column exception_express.status
     *
     * @param status the value for exception_express.status
     *
     * @mbggenerated Thu Aug 25 16:10:29 CST 2016
     */
    public void setStatus(Byte status) {
        this.status = status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column exception_express.createTime
     *
     * @return the value of exception_express.createTime
     *
     * @mbggenerated Thu Aug 25 16:10:29 CST 2016
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column exception_express.createTime
     *
     * @param createTime the value for exception_express.createTime
     *
     * @mbggenerated Thu Aug 25 16:10:29 CST 2016
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column exception_express.updateTime
     *
     * @return the value of exception_express.updateTime
     *
     * @mbggenerated Thu Aug 25 16:10:29 CST 2016
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column exception_express.updateTime
     *
     * @param updateTime the value for exception_express.updateTime
     *
     * @mbggenerated Thu Aug 25 16:10:29 CST 2016
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table exception_express
     *
     * @mbggenerated Thu Aug 25 16:10:29 CST 2016
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", exceptionExpressId=").append(exceptionExpressId);
        sb.append(", shipOrderId=").append(shipOrderId);
        sb.append(", mode=").append(mode);
        sb.append(", province=").append(province);
        sb.append(", city=").append(city);
        sb.append(", district=").append(district);
        sb.append(", school=").append(school);
        sb.append(", theClass=").append(theClass);
        sb.append(", consignee=").append(consignee);
        sb.append(", mobile=").append(mobile);
        sb.append(", delivery=").append(delivery);
        sb.append(", address=").append(address);
        sb.append(", expressId=").append(expressId);
        sb.append(", expressNumber=").append(expressNumber);
        sb.append(", expressCompany=").append(expressCompany);
        sb.append(", printExpress=").append(printExpress);
        sb.append(", outExpress=").append(outExpress);
        sb.append(", outVerify=").append(outVerify);
        sb.append(", status=").append(status);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}