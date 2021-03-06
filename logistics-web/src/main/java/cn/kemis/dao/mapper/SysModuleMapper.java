package cn.kemis.dao.mapper;

import cn.kemis.dao.BaseMapper;
import cn.kemis.model.SysModule;
import cn.kemis.model.SysModuleExample;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
/**
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table sys_module
 *
 * @mbggenerated Thu Aug 18 00:36:46 CST 2016
 */
@Mapper
public interface SysModuleMapper extends BaseMapper<SysModule, SysModuleExample> {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_module
     *
     * @mbggenerated Thu Aug 18 00:36:46 CST 2016
     */
    int countByExample(SysModuleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_module
     *
     * @mbggenerated Thu Aug 18 00:36:46 CST 2016
     */
    int deleteByExample(SysModuleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_module
     *
     * @mbggenerated Thu Aug 18 00:36:46 CST 2016
     */
    @Delete({
        "delete from sys_module",
        "where sysModuleId = #{sysModuleId,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer sysModuleId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_module
     *
     * @mbggenerated Thu Aug 18 00:36:46 CST 2016
     */
    @Insert({
        "insert into sys_module (sysModuleId, moduleName, ",
        "moduleParentId, moduleUrl, ",
        "`status`, createTime, ",
        "updateTime)",
        "values (#{sysModuleId,jdbcType=INTEGER}, #{moduleName,jdbcType=VARCHAR}, ",
        "#{moduleParentId,jdbcType=INTEGER}, #{moduleUrl,jdbcType=VARCHAR}, ",
        "#{status,jdbcType=TINYINT}, #{createTime,jdbcType=TIMESTAMP}, ",
        "#{updateTime,jdbcType=TIMESTAMP})"
    })
    int insert(SysModule record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_module
     *
     * @mbggenerated Thu Aug 18 00:36:46 CST 2016
     */
    int insertSelective(SysModule record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_module
     *
     * @mbggenerated Thu Aug 18 00:36:46 CST 2016
     */
    List<SysModule> selectByExample(SysModuleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_module
     *
     * @mbggenerated Thu Aug 18 00:36:46 CST 2016
     */
    @Select({
        "select",
        "sysModuleId, moduleName, moduleParentId, moduleUrl, `status`, createTime, updateTime",
        "from sys_module",
        "where sysModuleId = #{sysModuleId,jdbcType=INTEGER}"
    })
    @ResultMap("cn.kemis.dao.mapper.SysModuleMapper.BaseResultMap")
    SysModule selectByPrimaryKey(Integer sysModuleId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_module
     *
     * @mbggenerated Thu Aug 18 00:36:46 CST 2016
     */
    int updateByExampleSelective(@Param("record") SysModule record, @Param("example") SysModuleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_module
     *
     * @mbggenerated Thu Aug 18 00:36:46 CST 2016
     */
    int updateByExample(@Param("record") SysModule record, @Param("example") SysModuleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_module
     *
     * @mbggenerated Thu Aug 18 00:36:46 CST 2016
     */
    int updateByPrimaryKeySelective(SysModule record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_module
     *
     * @mbggenerated Thu Aug 18 00:36:46 CST 2016
     */
    @Update({
        "update sys_module",
        "set moduleName = #{moduleName,jdbcType=VARCHAR},",
          "moduleParentId = #{moduleParentId,jdbcType=INTEGER},",
          "moduleUrl = #{moduleUrl,jdbcType=VARCHAR},",
          "`status` = #{status,jdbcType=TINYINT},",
          "createTime = #{createTime,jdbcType=TIMESTAMP},",
          "updateTime = #{updateTime,jdbcType=TIMESTAMP}",
        "where sysModuleId = #{sysModuleId,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(SysModule record);
}