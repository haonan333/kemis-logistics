package cn.kemis.model;

import java.io.Serializable;

/**
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table sys_user_has_sys_role
 *
 * @mbggenerated do_not_delete_during_merge Wed Aug 03 23:17:35 CST 2016
 */
public class SysUserHasSysRoleKey implements Serializable {
    /**
     * Database Column Remarks:
     *   用户角色对应表
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_user_has_sys_role.userId
     *
     * @mbggenerated Wed Aug 03 23:17:35 CST 2016
     */
    private Integer userId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_user_has_sys_role.roleId
     *
     * @mbggenerated Wed Aug 03 23:17:35 CST 2016
     */
    private Integer roleId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table sys_user_has_sys_role
     *
     * @mbggenerated Wed Aug 03 23:17:35 CST 2016
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_user_has_sys_role
     *
     * @mbggenerated Wed Aug 03 23:17:35 CST 2016
     */
    public SysUserHasSysRoleKey(Integer userId, Integer roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_user_has_sys_role
     *
     * @mbggenerated Wed Aug 03 23:17:35 CST 2016
     */
    public SysUserHasSysRoleKey() {
        super();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_user_has_sys_role.userId
     *
     * @return the value of sys_user_has_sys_role.userId
     *
     * @mbggenerated Wed Aug 03 23:17:35 CST 2016
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_user_has_sys_role.userId
     *
     * @param userId the value for sys_user_has_sys_role.userId
     *
     * @mbggenerated Wed Aug 03 23:17:35 CST 2016
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_user_has_sys_role.roleId
     *
     * @return the value of sys_user_has_sys_role.roleId
     *
     * @mbggenerated Wed Aug 03 23:17:35 CST 2016
     */
    public Integer getRoleId() {
        return roleId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_user_has_sys_role.roleId
     *
     * @param roleId the value for sys_user_has_sys_role.roleId
     *
     * @mbggenerated Wed Aug 03 23:17:35 CST 2016
     */
    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_user_has_sys_role
     *
     * @mbggenerated Wed Aug 03 23:17:35 CST 2016
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", userId=").append(userId);
        sb.append(", roleId=").append(roleId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}