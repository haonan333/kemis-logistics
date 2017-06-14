package cn.kemis.dao.mapper;

import cn.kemis.dao.BaseMapper;
import cn.kemis.model.ExceptionExpress;
import cn.kemis.model.ExceptionExpressExample;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table exception_express
 *
 * @mbggenerated Thu Aug 25 16:10:29 CST 2016
 */
@Mapper
public interface ExceptionExpressMapper extends BaseMapper<ExceptionExpress, ExceptionExpressExample> {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table exception_express
     *
     * @mbggenerated Thu Aug 25 16:10:29 CST 2016
     */
    int countByExample(ExceptionExpressExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table exception_express
     *
     * @mbggenerated Thu Aug 25 16:10:29 CST 2016
     */
    int deleteByExample(ExceptionExpressExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table exception_express
     *
     * @mbggenerated Thu Aug 25 16:10:29 CST 2016
     */
    @Delete({
        "delete from exception_express",
        "where exceptionExpressId = #{exceptionExpressId,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long exceptionExpressId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table exception_express
     *
     * @mbggenerated Thu Aug 25 16:10:29 CST 2016
     */
    @Insert({
        "insert into exception_express (shipOrderId, `mode`, ",
        "province, city, ",
        "district, school, ",
        "theClass, consignee, ",
        "mobile, delivery, ",
        "address, expressId, ",
        "expressNumber, expressCompany, ",
        "printExpress, outExpress, ",
        "outVerify, `status`, createTime, ",
        "updateTime)",
        "values (#{shipOrderId,jdbcType=BIGINT}, #{mode,jdbcType=CHAR}, ",
        "#{province,jdbcType=VARCHAR}, #{city,jdbcType=VARCHAR}, ",
        "#{district,jdbcType=VARCHAR}, #{school,jdbcType=VARCHAR}, ",
        "#{theClass,jdbcType=VARCHAR}, #{consignee,jdbcType=VARCHAR}, ",
        "#{mobile,jdbcType=VARCHAR}, #{delivery,jdbcType=VARCHAR}, ",
        "#{address,jdbcType=VARCHAR}, #{expressId,jdbcType=BIGINT}, ",
        "#{expressNumber,jdbcType=VARCHAR}, #{expressCompany,jdbcType=VARCHAR}, ",
        "#{printExpress,jdbcType=BIT}, #{outExpress,jdbcType=BIT}, ",
        "#{outVerify,jdbcType=BIT}, #{status,jdbcType=TINYINT}, #{createTime,jdbcType=TIMESTAMP}, ",
        "#{updateTime,jdbcType=TIMESTAMP})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="exceptionExpressId", before=false, resultType=Long.class)
    int insert(ExceptionExpress record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table exception_express
     *
     * @mbggenerated Thu Aug 25 16:10:29 CST 2016
     */
    int insertSelective(ExceptionExpress record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table exception_express
     *
     * @mbggenerated Thu Aug 25 16:10:29 CST 2016
     */
    List<ExceptionExpress> selectByExample(ExceptionExpressExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table exception_express
     *
     * @mbggenerated Thu Aug 25 16:10:29 CST 2016
     */
    @Select({
        "select",
        "exceptionExpressId, shipOrderId, `mode`, province, city, district, school, theClass, ",
        "consignee, mobile, delivery, address, expressId, expressNumber, expressCompany, ",
        "printExpress, outExpress, outVerify, `status`, createTime, updateTime",
        "from exception_express",
        "where exceptionExpressId = #{exceptionExpressId,jdbcType=BIGINT}"
    })
    @ResultMap("cn.kemis.dao.mapper.ExceptionExpressMapper.BaseResultMap")
    ExceptionExpress selectByPrimaryKey(Long exceptionExpressId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table exception_express
     *
     * @mbggenerated Thu Aug 25 16:10:29 CST 2016
     */
    int updateByExampleSelective(@Param("record") ExceptionExpress record, @Param("example") ExceptionExpressExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table exception_express
     *
     * @mbggenerated Thu Aug 25 16:10:29 CST 2016
     */
    int updateByExample(@Param("record") ExceptionExpress record, @Param("example") ExceptionExpressExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table exception_express
     *
     * @mbggenerated Thu Aug 25 16:10:29 CST 2016
     */
    int updateByPrimaryKeySelective(ExceptionExpress record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table exception_express
     *
     * @mbggenerated Thu Aug 25 16:10:29 CST 2016
     */
    @Update({
        "update exception_express",
        "set shipOrderId = #{shipOrderId,jdbcType=BIGINT},",
          "`mode` = #{mode,jdbcType=CHAR},",
          "province = #{province,jdbcType=VARCHAR},",
          "city = #{city,jdbcType=VARCHAR},",
          "district = #{district,jdbcType=VARCHAR},",
          "school = #{school,jdbcType=VARCHAR},",
          "theClass = #{theClass,jdbcType=VARCHAR},",
          "consignee = #{consignee,jdbcType=VARCHAR},",
          "mobile = #{mobile,jdbcType=VARCHAR},",
          "delivery = #{delivery,jdbcType=VARCHAR},",
          "address = #{address,jdbcType=VARCHAR},",
          "expressId = #{expressId,jdbcType=BIGINT},",
          "expressNumber = #{expressNumber,jdbcType=VARCHAR},",
          "expressCompany = #{expressCompany,jdbcType=VARCHAR},",
          "printExpress = #{printExpress,jdbcType=BIT},",
          "outExpress = #{outExpress,jdbcType=BIT},",
          "outVerify = #{outVerify,jdbcType=BIT},",
          "`status` = #{status,jdbcType=TINYINT},",
          "createTime = #{createTime,jdbcType=TIMESTAMP},",
          "updateTime = #{updateTime,jdbcType=TIMESTAMP}",
        "where exceptionExpressId = #{exceptionExpressId,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(ExceptionExpress record);
}