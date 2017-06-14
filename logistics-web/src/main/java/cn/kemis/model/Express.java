package cn.kemis.model;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table express
 *
 * @mbggenerated do_not_delete_during_merge Sun Sep 25 20:38:29 CST 2016
 */
public class Express implements Serializable {
    /**
     * Database Column Remarks:
     *   快递单ID
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column express.expressId
     *
     * @mbggenerated Sun Sep 25 20:38:29 CST 2016
     */
    private Long expressId;

    /**
     * Database Column Remarks:
     *   批次号
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column express.batchNo
     *
     * @mbggenerated Sun Sep 25 20:38:29 CST 2016
     */
    private String batchNo;

    /**
     * Database Column Remarks:
     *   物流公司
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column express.expressCompany
     *
     * @mbggenerated Sun Sep 25 20:38:29 CST 2016
     */
    private String expressCompany;

    /**
     * Database Column Remarks:
     *   物流单号
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column express.expressNumber
     *
     * @mbggenerated Sun Sep 25 20:38:29 CST 2016
     */
    private String expressNumber;

    /**
     * Database Column Remarks:
     *   类型
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column express.type
     *
     * @mbggenerated Sun Sep 25 20:38:29 CST 2016
     */
    private String type;

    /**
     * Database Column Remarks:
     *   配送方式： EMS，普通快递
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column express.delivery
     *
     * @mbggenerated Sun Sep 25 20:38:29 CST 2016
     */
    private String delivery;

    /**
     * Database Column Remarks:
     *   是否导回
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column express.exportTag
     *
     * @mbggenerated Sun Sep 25 20:38:29 CST 2016
     */
    private Boolean exportTag;

    /**
     * Database Column Remarks:
     *   价格，单位分
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column express.price
     *
     * @mbggenerated Sun Sep 25 20:38:29 CST 2016
     */
    private Double price;

    /**
     * Database Column Remarks:
     *   重量
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column express.weight
     *
     * @mbggenerated Sun Sep 25 20:38:29 CST 2016
     */
    private String weight;

    /**
     * Database Column Remarks:
     *   收货人
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column express.expressConsignee
     *
     * @mbggenerated Sun Sep 25 20:38:29 CST 2016
     */
    private String expressConsignee;

    /**
     * Database Column Remarks:
     *   收货人电话
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column express.expressMobile
     *
     * @mbggenerated Sun Sep 25 20:38:29 CST 2016
     */
    private String expressMobile;

    /**
     * Database Column Remarks:
     *   学校 ID
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column express.expressSchoolId
     *
     * @mbggenerated Sun Sep 25 20:38:29 CST 2016
     */
    private String expressSchoolId;

    /**
     * Database Column Remarks:
     *   学校名称
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column express.expressSchool
     *
     * @mbggenerated Sun Sep 25 20:38:29 CST 2016
     */
    private String expressSchool;

    /**
     * Database Column Remarks:
     *   省
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column express.expressProvince
     *
     * @mbggenerated Sun Sep 25 20:38:29 CST 2016
     */
    private String expressProvince;

    /**
     * Database Column Remarks:
     *   市
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column express.expressCity
     *
     * @mbggenerated Sun Sep 25 20:38:29 CST 2016
     */
    private String expressCity;

    /**
     * Database Column Remarks:
     *   区
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column express.expressDistrict
     *
     * @mbggenerated Sun Sep 25 20:38:29 CST 2016
     */
    private String expressDistrict;

    /**
     * Database Column Remarks:
     *   详细地址
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column express.expressAddress
     *
     * @mbggenerated Sun Sep 25 20:38:29 CST 2016
     */
    private String expressAddress;

    /**
     * Database Column Remarks:
     *   备注
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column express.remark
     *
     * @mbggenerated Sun Sep 25 20:38:29 CST 2016
     */
    private String remark;

    /**
     * Database Column Remarks:
     *   状态 
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column express.status
     *
     * @mbggenerated Sun Sep 25 20:38:29 CST 2016
     */
    private Byte status;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column express.createTime
     *
     * @mbggenerated Sun Sep 25 20:38:29 CST 2016
     */
    private Date createTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column express.updateTime
     *
     * @mbggenerated Sun Sep 25 20:38:29 CST 2016
     */
    private Date updateTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table express
     *
     * @mbggenerated Sun Sep 25 20:38:29 CST 2016
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table express
     *
     * @mbggenerated Sun Sep 25 20:38:29 CST 2016
     */
    public Express(Long expressId, String batchNo, String expressCompany, String expressNumber, String type, String delivery, Boolean exportTag, Double price, String weight, String expressConsignee, String expressMobile, String expressSchoolId, String expressSchool, String expressProvince, String expressCity, String expressDistrict, String expressAddress, String remark, Byte status, Date createTime, Date updateTime) {
        this.expressId = expressId;
        this.batchNo = batchNo;
        this.expressCompany = expressCompany;
        this.expressNumber = expressNumber;
        this.type = type;
        this.delivery = delivery;
        this.exportTag = exportTag;
        this.price = price;
        this.weight = weight;
        this.expressConsignee = expressConsignee;
        this.expressMobile = expressMobile;
        this.expressSchoolId = expressSchoolId;
        this.expressSchool = expressSchool;
        this.expressProvince = expressProvince;
        this.expressCity = expressCity;
        this.expressDistrict = expressDistrict;
        this.expressAddress = expressAddress;
        this.remark = remark;
        this.status = status;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table express
     *
     * @mbggenerated Sun Sep 25 20:38:29 CST 2016
     */
    public Express() {
        super();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column express.expressId
     *
     * @return the value of express.expressId
     *
     * @mbggenerated Sun Sep 25 20:38:29 CST 2016
     */
    public Long getExpressId() {
        return expressId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column express.expressId
     *
     * @param expressId the value for express.expressId
     *
     * @mbggenerated Sun Sep 25 20:38:29 CST 2016
     */
    public void setExpressId(Long expressId) {
        this.expressId = expressId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column express.batchNo
     *
     * @return the value of express.batchNo
     *
     * @mbggenerated Sun Sep 25 20:38:29 CST 2016
     */
    public String getBatchNo() {
        return batchNo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column express.batchNo
     *
     * @param batchNo the value for express.batchNo
     *
     * @mbggenerated Sun Sep 25 20:38:29 CST 2016
     */
    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo == null ? null : batchNo.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column express.expressCompany
     *
     * @return the value of express.expressCompany
     *
     * @mbggenerated Sun Sep 25 20:38:29 CST 2016
     */
    public String getExpressCompany() {
        return expressCompany;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column express.expressCompany
     *
     * @param expressCompany the value for express.expressCompany
     *
     * @mbggenerated Sun Sep 25 20:38:29 CST 2016
     */
    public void setExpressCompany(String expressCompany) {
        this.expressCompany = expressCompany == null ? null : expressCompany.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column express.expressNumber
     *
     * @return the value of express.expressNumber
     *
     * @mbggenerated Sun Sep 25 20:38:29 CST 2016
     */
    public String getExpressNumber() {
        return expressNumber;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column express.expressNumber
     *
     * @param expressNumber the value for express.expressNumber
     *
     * @mbggenerated Sun Sep 25 20:38:29 CST 2016
     */
    public void setExpressNumber(String expressNumber) {
        this.expressNumber = expressNumber == null ? null : expressNumber.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column express.type
     *
     * @return the value of express.type
     *
     * @mbggenerated Sun Sep 25 20:38:29 CST 2016
     */
    public String getType() {
        return type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column express.type
     *
     * @param type the value for express.type
     *
     * @mbggenerated Sun Sep 25 20:38:29 CST 2016
     */
    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column express.delivery
     *
     * @return the value of express.delivery
     *
     * @mbggenerated Sun Sep 25 20:38:29 CST 2016
     */
    public String getDelivery() {
        return delivery;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column express.delivery
     *
     * @param delivery the value for express.delivery
     *
     * @mbggenerated Sun Sep 25 20:38:29 CST 2016
     */
    public void setDelivery(String delivery) {
        this.delivery = delivery == null ? null : delivery.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column express.exportTag
     *
     * @return the value of express.exportTag
     *
     * @mbggenerated Sun Sep 25 20:38:29 CST 2016
     */
    public Boolean getExportTag() {
        return exportTag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column express.exportTag
     *
     * @param exportTag the value for express.exportTag
     *
     * @mbggenerated Sun Sep 25 20:38:29 CST 2016
     */
    public void setExportTag(Boolean exportTag) {
        this.exportTag = exportTag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column express.price
     *
     * @return the value of express.price
     *
     * @mbggenerated Sun Sep 25 20:38:29 CST 2016
     */
    public Double getPrice() {
        return price;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column express.price
     *
     * @param price the value for express.price
     *
     * @mbggenerated Sun Sep 25 20:38:29 CST 2016
     */
    public void setPrice(Double price) {
        this.price = price;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column express.weight
     *
     * @return the value of express.weight
     *
     * @mbggenerated Sun Sep 25 20:38:29 CST 2016
     */
    public String getWeight() {
        return weight;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column express.weight
     *
     * @param weight the value for express.weight
     *
     * @mbggenerated Sun Sep 25 20:38:29 CST 2016
     */
    public void setWeight(String weight) {
        this.weight = weight == null ? null : weight.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column express.expressConsignee
     *
     * @return the value of express.expressConsignee
     *
     * @mbggenerated Sun Sep 25 20:38:29 CST 2016
     */
    public String getExpressConsignee() {
        return expressConsignee;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column express.expressConsignee
     *
     * @param expressConsignee the value for express.expressConsignee
     *
     * @mbggenerated Sun Sep 25 20:38:29 CST 2016
     */
    public void setExpressConsignee(String expressConsignee) {
        this.expressConsignee = expressConsignee == null ? null : expressConsignee.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column express.expressMobile
     *
     * @return the value of express.expressMobile
     *
     * @mbggenerated Sun Sep 25 20:38:29 CST 2016
     */
    public String getExpressMobile() {
        return expressMobile;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column express.expressMobile
     *
     * @param expressMobile the value for express.expressMobile
     *
     * @mbggenerated Sun Sep 25 20:38:29 CST 2016
     */
    public void setExpressMobile(String expressMobile) {
        this.expressMobile = expressMobile == null ? null : expressMobile.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column express.expressSchoolId
     *
     * @return the value of express.expressSchoolId
     *
     * @mbggenerated Sun Sep 25 20:38:29 CST 2016
     */
    public String getExpressSchoolId() {
        return expressSchoolId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column express.expressSchoolId
     *
     * @param expressSchoolId the value for express.expressSchoolId
     *
     * @mbggenerated Sun Sep 25 20:38:29 CST 2016
     */
    public void setExpressSchoolId(String expressSchoolId) {
        this.expressSchoolId = expressSchoolId == null ? null : expressSchoolId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column express.expressSchool
     *
     * @return the value of express.expressSchool
     *
     * @mbggenerated Sun Sep 25 20:38:29 CST 2016
     */
    public String getExpressSchool() {
        return expressSchool;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column express.expressSchool
     *
     * @param expressSchool the value for express.expressSchool
     *
     * @mbggenerated Sun Sep 25 20:38:29 CST 2016
     */
    public void setExpressSchool(String expressSchool) {
        this.expressSchool = expressSchool == null ? null : expressSchool.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column express.expressProvince
     *
     * @return the value of express.expressProvince
     *
     * @mbggenerated Sun Sep 25 20:38:29 CST 2016
     */
    public String getExpressProvince() {
        return expressProvince;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column express.expressProvince
     *
     * @param expressProvince the value for express.expressProvince
     *
     * @mbggenerated Sun Sep 25 20:38:29 CST 2016
     */
    public void setExpressProvince(String expressProvince) {
        this.expressProvince = expressProvince == null ? null : expressProvince.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column express.expressCity
     *
     * @return the value of express.expressCity
     *
     * @mbggenerated Sun Sep 25 20:38:29 CST 2016
     */
    public String getExpressCity() {
        return expressCity;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column express.expressCity
     *
     * @param expressCity the value for express.expressCity
     *
     * @mbggenerated Sun Sep 25 20:38:29 CST 2016
     */
    public void setExpressCity(String expressCity) {
        this.expressCity = expressCity == null ? null : expressCity.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column express.expressDistrict
     *
     * @return the value of express.expressDistrict
     *
     * @mbggenerated Sun Sep 25 20:38:29 CST 2016
     */
    public String getExpressDistrict() {
        return expressDistrict;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column express.expressDistrict
     *
     * @param expressDistrict the value for express.expressDistrict
     *
     * @mbggenerated Sun Sep 25 20:38:29 CST 2016
     */
    public void setExpressDistrict(String expressDistrict) {
        this.expressDistrict = expressDistrict == null ? null : expressDistrict.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column express.expressAddress
     *
     * @return the value of express.expressAddress
     *
     * @mbggenerated Sun Sep 25 20:38:29 CST 2016
     */
    public String getExpressAddress() {
        return expressAddress;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column express.expressAddress
     *
     * @param expressAddress the value for express.expressAddress
     *
     * @mbggenerated Sun Sep 25 20:38:29 CST 2016
     */
    public void setExpressAddress(String expressAddress) {
        this.expressAddress = expressAddress == null ? null : expressAddress.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column express.remark
     *
     * @return the value of express.remark
     *
     * @mbggenerated Sun Sep 25 20:38:29 CST 2016
     */
    public String getRemark() {
        return remark;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column express.remark
     *
     * @param remark the value for express.remark
     *
     * @mbggenerated Sun Sep 25 20:38:29 CST 2016
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column express.status
     *
     * @return the value of express.status
     *
     * @mbggenerated Sun Sep 25 20:38:29 CST 2016
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column express.status
     *
     * @param status the value for express.status
     *
     * @mbggenerated Sun Sep 25 20:38:29 CST 2016
     */
    public void setStatus(Byte status) {
        this.status = status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column express.createTime
     *
     * @return the value of express.createTime
     *
     * @mbggenerated Sun Sep 25 20:38:29 CST 2016
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column express.createTime
     *
     * @param createTime the value for express.createTime
     *
     * @mbggenerated Sun Sep 25 20:38:29 CST 2016
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column express.updateTime
     *
     * @return the value of express.updateTime
     *
     * @mbggenerated Sun Sep 25 20:38:29 CST 2016
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column express.updateTime
     *
     * @param updateTime the value for express.updateTime
     *
     * @mbggenerated Sun Sep 25 20:38:29 CST 2016
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table express
     *
     * @mbggenerated Sun Sep 25 20:38:29 CST 2016
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", expressId=").append(expressId);
        sb.append(", batchNo=").append(batchNo);
        sb.append(", expressCompany=").append(expressCompany);
        sb.append(", expressNumber=").append(expressNumber);
        sb.append(", type=").append(type);
        sb.append(", delivery=").append(delivery);
        sb.append(", exportTag=").append(exportTag);
        sb.append(", price=").append(price);
        sb.append(", weight=").append(weight);
        sb.append(", expressConsignee=").append(expressConsignee);
        sb.append(", expressMobile=").append(expressMobile);
        sb.append(", expressSchoolId=").append(expressSchoolId);
        sb.append(", expressSchool=").append(expressSchool);
        sb.append(", expressProvince=").append(expressProvince);
        sb.append(", expressCity=").append(expressCity);
        sb.append(", expressDistrict=").append(expressDistrict);
        sb.append(", expressAddress=").append(expressAddress);
        sb.append(", remark=").append(remark);
        sb.append(", status=").append(status);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}