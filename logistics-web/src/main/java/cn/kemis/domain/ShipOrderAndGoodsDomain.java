package cn.kemis.domain;

import cn.kemis.model.Express;
import cn.kemis.model.ShipOrder;
import cn.kemis.model.ShipOrderGoods;

import java.util.Date;
import java.util.List;

/**
 * @author 刘体阳 jefferlzu@gmail.com
 *         Created on 2016-08-20
 */
public class ShipOrderAndGoodsDomain extends ShipOrder {

    private static final long serialVersionUID = 6416624670045558825L;

    private List<ShipOrderGoods> shipOrderGoodsList;

    private int sequenceNo;     // 生成文件时动态创建的序列

    private Express express;

    public ShipOrderAndGoodsDomain() {

    }

    public ShipOrderAndGoodsDomain(Long shipOrderId, String batchNo, String userId, String userName, String province, String city, String district, String school, String theClass, String consignee, String mobile, String delivery, String address, Short totalCount, String unitCredits, String subject, Boolean campusAmbassador, Long expressId, Boolean printExpress, Boolean outExpress, Boolean outVerify, Boolean teacherTag, Boolean bigPackage, Byte status, Date createTime, Date updateTime) {
        super.setShipOrderId(shipOrderId);
        super.setBatchNo(batchNo);
        super.setUserId(userId);
        super.setUserName(userName);
        super.setProvince(province);
        super.setCity(city);
        super.setDistrict(district);
        super.setSchool(school);
        super.setTheClass(theClass);
        super.setConsignee(consignee);
        super.setMobile(mobile);
        super.setDelivery(delivery);
        super.setAddress(address);
        super.setTotalCount(totalCount);
        super.setUnitCredits(unitCredits);
        super.setSubject(subject);
        super.setCampusAmbassador(campusAmbassador);
        super.setExpressId(expressId);
        super.setPrintExpress(printExpress);
        super.setOutExpress(outExpress);
        super.setOutVerify(outVerify);
        super.setTeacherTag(teacherTag);
        super.setBigPackage(bigPackage);
        super.setStatus(status);
        super.setCreateTime(createTime);
        super.setUpdateTime(updateTime);
    }

    public List<ShipOrderGoods> getShipOrderGoodsList() {
        return shipOrderGoodsList;
    }

    public void setShipOrderGoodsList(List<ShipOrderGoods> shipOrderGoodsList) {
        this.shipOrderGoodsList = shipOrderGoodsList;
    }

    public int getSequenceNo() {
        return sequenceNo;
    }

    public void setSequenceNo(int sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    public Express getExpress() {
        return express;
    }

    public void setExpress(Express express) {
        this.express = express;
    }
}
