package cn.kemis.tools.batch.mapper;

import cn.kemis.model.ShipOrderSource;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author 刘体阳 jefferlzu@gmail.com
 *         Created on 2016-08-12
 */
public class ShipOrderSourceRowMapper implements RowMapper<ShipOrderSource> {

    private static final String SHIPORDERID_COLUMN = "shipOrderSourceId";
    private static final String BATCHNO_COLUMN = "batchNo";
    private static final String USERID_COLUMN = "userId";
    private static final String USERNAME_COLUMN = "userName";
    private static final String PROVINCE_COLUMN = "province";
    private static final String CITY_COLUMN = "city";
    private static final String DISTRICT_COLUMN = "district";
    private static final String SCHOOLID_COLUMN = "schoolId";
    private static final String SCHOOL_COLUMN = "school";
    private static final String THECLASS_COLUMN = "theClass";
    private static final String CONSIGNEE_COLUMN = "consignee";
    private static final String MOBILE_COLUMN = "mobile";
    private static final String DELIVERY_COLUMN = "delivery";
    private static final String SHIPGOODS_COLUMN = "shipGoods";
    private static final String ADDRESS_COLUMN = "address";
    private static final String TOTALCOUNT_COLUMN = "totalCount";
    private static final String UNITCREDITS_COLUMN = "unitCredits";
    private static final String SUBJECT_COLUMN = "subject";
    private static final String CAMPUSAMBASSADOR_COLUMN = "campusAmbassador";
    private static final String EXPRESSID_COLUMN = "expressId";
    private static final String STATUS_COLUMN = "status";
    private static final String CREATETIME_COLUMN = "createTime";
    private static final String UPDATETIME_COLUMN = "updateTime";

    @Override
    public ShipOrderSource mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        ShipOrderSource shipOrderSource = new ShipOrderSource();

        shipOrderSource.setShipOrderSourceId(resultSet.getLong(SHIPORDERID_COLUMN));
        shipOrderSource.setBatchNo(resultSet.getString(BATCHNO_COLUMN));
        shipOrderSource.setUserId(resultSet.getString(USERID_COLUMN));
        shipOrderSource.setUserName(resultSet.getString(USERNAME_COLUMN));
        shipOrderSource.setProvince(resultSet.getString(PROVINCE_COLUMN));
        shipOrderSource.setCity(resultSet.getString(CITY_COLUMN));
        shipOrderSource.setDistrict(resultSet.getString(DISTRICT_COLUMN));
        shipOrderSource.setSchoolId(resultSet.getString(SCHOOLID_COLUMN));
        shipOrderSource.setSchool(resultSet.getString(SCHOOL_COLUMN));
        shipOrderSource.setTheClass(resultSet.getString(THECLASS_COLUMN));
        shipOrderSource.setConsignee(resultSet.getString(CONSIGNEE_COLUMN));
        shipOrderSource.setMobile(resultSet.getString(MOBILE_COLUMN));
        shipOrderSource.setDelivery(resultSet.getString(DELIVERY_COLUMN));
        shipOrderSource.setShipGoods(resultSet.getString(SHIPGOODS_COLUMN));
        shipOrderSource.setAddress(resultSet.getString(ADDRESS_COLUMN));
        shipOrderSource.setTotalCount(resultSet.getShort(TOTALCOUNT_COLUMN));
        shipOrderSource.setUnitCredits(resultSet.getString(UNITCREDITS_COLUMN));
        shipOrderSource.setSubject(resultSet.getString(SUBJECT_COLUMN));
        shipOrderSource.setCampusAmbassador(resultSet.getBoolean(CAMPUSAMBASSADOR_COLUMN));
        shipOrderSource.setExpressId(resultSet.getLong(EXPRESSID_COLUMN));
        shipOrderSource.setStatus(resultSet.getByte(STATUS_COLUMN));
        shipOrderSource.setCreateTime(resultSet.getTimestamp(CREATETIME_COLUMN));
        shipOrderSource.setUpdateTime(resultSet.getTimestamp(UPDATETIME_COLUMN));

        return shipOrderSource;
    }
}
