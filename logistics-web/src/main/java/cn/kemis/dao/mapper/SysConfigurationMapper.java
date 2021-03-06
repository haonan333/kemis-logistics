package cn.kemis.dao.mapper;

import cn.kemis.dao.BaseMapper;
import cn.kemis.model.SysConfiguration;
import cn.kemis.model.SysConfigurationExample;
import org.apache.ibatis.annotations.*;

import java.util.List;
/**
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table sys_configuration
 *
 * @mbggenerated Sun Aug 28 18:48:29 CST 2016
 */
@Mapper
public interface SysConfigurationMapper extends BaseMapper<SysConfiguration, SysConfigurationExample> {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_configuration
     *
     * @mbggenerated Sun Aug 28 18:48:29 CST 2016
     */
    int countByExample(SysConfigurationExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_configuration
     *
     * @mbggenerated Sun Aug 28 18:48:29 CST 2016
     */
    int deleteByExample(SysConfigurationExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_configuration
     *
     * @mbggenerated Sun Aug 28 18:48:29 CST 2016
     */
    @Delete({
        "delete from sys_configuration",
        "where propertyKey = #{propertyKey,jdbcType=VARCHAR}"
    })
    int deleteByPrimaryKey(String propertyKey);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_configuration
     *
     * @mbggenerated Sun Aug 28 18:48:29 CST 2016
     */
    @Insert({
        "insert into sys_configuration (propertyKey, propertyValue, ",
        "`desc`, `status`, createTime, ",
        "updateTime)",
        "values (#{propertyKey,jdbcType=VARCHAR}, #{propertyValue,jdbcType=VARCHAR}, ",
        "#{desc,jdbcType=VARCHAR}, #{status,jdbcType=TINYINT}, #{createTime,jdbcType=TIMESTAMP}, ",
        "#{updateTime,jdbcType=TIMESTAMP})"
    })
    int insert(SysConfiguration record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_configuration
     *
     * @mbggenerated Sun Aug 28 18:48:29 CST 2016
     */
    int insertSelective(SysConfiguration record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_configuration
     *
     * @mbggenerated Sun Aug 28 18:48:29 CST 2016
     */
    List<SysConfiguration> selectByExample(SysConfigurationExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_configuration
     *
     * @mbggenerated Sun Aug 28 18:48:29 CST 2016
     */
    @Select({
        "select",
        "propertyKey, propertyValue, `desc`, `status`, createTime, updateTime",
        "from sys_configuration",
        "where propertyKey = #{propertyKey,jdbcType=VARCHAR}"
    })
    @ResultMap("cn.kemis.dao.mapper.SysConfigurationMapper.BaseResultMap")
    SysConfiguration selectByPrimaryKey(String propertyKey);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_configuration
     *
     * @mbggenerated Sun Aug 28 18:48:29 CST 2016
     */
    int updateByExampleSelective(@Param("record") SysConfiguration record, @Param("example") SysConfigurationExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_configuration
     *
     * @mbggenerated Sun Aug 28 18:48:29 CST 2016
     */
    int updateByExample(@Param("record") SysConfiguration record, @Param("example") SysConfigurationExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_configuration
     *
     * @mbggenerated Sun Aug 28 18:48:29 CST 2016
     */
    int updateByPrimaryKeySelective(SysConfiguration record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_configuration
     *
     * @mbggenerated Sun Aug 28 18:48:29 CST 2016
     */
    @Update({
        "update sys_configuration",
        "set propertyValue = #{propertyValue,jdbcType=VARCHAR},",
          "`desc` = #{desc,jdbcType=VARCHAR},",
          "`status` = #{status,jdbcType=TINYINT},",
          "createTime = #{createTime,jdbcType=TIMESTAMP},",
          "updateTime = #{updateTime,jdbcType=TIMESTAMP}",
        "where propertyKey = #{propertyKey,jdbcType=VARCHAR}"
    })
    int updateByPrimaryKey(SysConfiguration record);
}