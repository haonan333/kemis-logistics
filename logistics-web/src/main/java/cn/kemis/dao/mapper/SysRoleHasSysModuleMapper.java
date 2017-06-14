package cn.kemis.dao.mapper;

import cn.kemis.dao.BaseMapper;
import cn.kemis.model.SysRoleHasSysModuleExample;
import cn.kemis.model.SysRoleHasSysModuleKey;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
/**
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table sys_role_has_sys_module
 *
 * @mbggenerated Wed Aug 03 23:17:35 CST 2016
 */
@Mapper
public interface SysRoleHasSysModuleMapper extends BaseMapper<SysRoleHasSysModuleKey, SysRoleHasSysModuleExample> {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_role_has_sys_module
     *
     * @mbggenerated Wed Aug 03 23:17:35 CST 2016
     */
    int countByExample(SysRoleHasSysModuleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_role_has_sys_module
     *
     * @mbggenerated Wed Aug 03 23:17:35 CST 2016
     */
    int deleteByExample(SysRoleHasSysModuleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_role_has_sys_module
     *
     * @mbggenerated Wed Aug 03 23:17:35 CST 2016
     */
    @Delete({
        "delete from sys_role_has_sys_module",
        "where roleId = #{roleId,jdbcType=INTEGER}",
          "and moduleId = #{moduleId,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(SysRoleHasSysModuleKey key);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_role_has_sys_module
     *
     * @mbggenerated Wed Aug 03 23:17:35 CST 2016
     */
    @Insert({
        "insert into sys_role_has_sys_module (roleId, moduleId)",
        "values (#{roleId,jdbcType=INTEGER}, #{moduleId,jdbcType=INTEGER})"
    })
    int insert(SysRoleHasSysModuleKey record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_role_has_sys_module
     *
     * @mbggenerated Wed Aug 03 23:17:35 CST 2016
     */
    int insertSelective(SysRoleHasSysModuleKey record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_role_has_sys_module
     *
     * @mbggenerated Wed Aug 03 23:17:35 CST 2016
     */
    List<SysRoleHasSysModuleKey> selectByExample(SysRoleHasSysModuleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_role_has_sys_module
     *
     * @mbggenerated Wed Aug 03 23:17:35 CST 2016
     */
    int updateByExampleSelective(@Param("record") SysRoleHasSysModuleKey record, @Param("example") SysRoleHasSysModuleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_role_has_sys_module
     *
     * @mbggenerated Wed Aug 03 23:17:35 CST 2016
     */
    int updateByExample(@Param("record") SysRoleHasSysModuleKey record, @Param("example") SysRoleHasSysModuleExample example);
}