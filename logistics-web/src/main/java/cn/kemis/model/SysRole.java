package cn.kemis.model;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table sys_role
 *
 * @mbggenerated do_not_delete_during_merge Thu Aug 18 00:33:50 CST 2016
 */
public class SysRole implements Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_role.sysRoleId
     *
     * @mbggenerated Thu Aug 18 00:33:50 CST 2016
     */
    private Integer sysRoleId;

    /**
     * Database Column Remarks:
     *   角色名称
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_role.roleName
     *
     * @mbggenerated Thu Aug 18 00:33:50 CST 2016
     */
    private String roleName;

    /**
     * Database Column Remarks:
     *   角色描述
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_role.roleDesc
     *
     * @mbggenerated Thu Aug 18 00:33:50 CST 2016
     */
    private String roleDesc;

    /**
     * Database Column Remarks:
     *   父角色
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_role.roleParentId
     *
     * @mbggenerated Thu Aug 18 00:33:50 CST 2016
     */
    private Integer roleParentId;

    /**
     * Database Column Remarks:
     *   权限标识
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_role.permissionName
     *
     * @mbggenerated Thu Aug 18 00:33:50 CST 2016
     */
    private String permissionName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_role.status
     *
     * @mbggenerated Thu Aug 18 00:33:50 CST 2016
     */
    private Byte status;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_role.createTime
     *
     * @mbggenerated Thu Aug 18 00:33:50 CST 2016
     */
    private Date createTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_role.updateTime
     *
     * @mbggenerated Thu Aug 18 00:33:50 CST 2016
     */
    private Date updateTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table sys_role
     *
     * @mbggenerated Thu Aug 18 00:33:50 CST 2016
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_role
     *
     * @mbggenerated Thu Aug 18 00:33:50 CST 2016
     */
    public SysRole(Integer sysRoleId, String roleName, String roleDesc, Integer roleParentId, String permissionName, Byte status, Date createTime, Date updateTime) {
        this.sysRoleId = sysRoleId;
        this.roleName = roleName;
        this.roleDesc = roleDesc;
        this.roleParentId = roleParentId;
        this.permissionName = permissionName;
        this.status = status;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_role
     *
     * @mbggenerated Thu Aug 18 00:33:50 CST 2016
     */
    public SysRole() {
        super();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_role.sysRoleId
     *
     * @return the value of sys_role.sysRoleId
     *
     * @mbggenerated Thu Aug 18 00:33:50 CST 2016
     */
    public Integer getSysRoleId() {
        return sysRoleId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_role.sysRoleId
     *
     * @param sysRoleId the value for sys_role.sysRoleId
     *
     * @mbggenerated Thu Aug 18 00:33:50 CST 2016
     */
    public void setSysRoleId(Integer sysRoleId) {
        this.sysRoleId = sysRoleId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_role.roleName
     *
     * @return the value of sys_role.roleName
     *
     * @mbggenerated Thu Aug 18 00:33:50 CST 2016
     */
    public String getRoleName() {
        return roleName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_role.roleName
     *
     * @param roleName the value for sys_role.roleName
     *
     * @mbggenerated Thu Aug 18 00:33:50 CST 2016
     */
    public void setRoleName(String roleName) {
        this.roleName = roleName == null ? null : roleName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_role.roleDesc
     *
     * @return the value of sys_role.roleDesc
     *
     * @mbggenerated Thu Aug 18 00:33:50 CST 2016
     */
    public String getRoleDesc() {
        return roleDesc;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_role.roleDesc
     *
     * @param roleDesc the value for sys_role.roleDesc
     *
     * @mbggenerated Thu Aug 18 00:33:50 CST 2016
     */
    public void setRoleDesc(String roleDesc) {
        this.roleDesc = roleDesc == null ? null : roleDesc.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_role.roleParentId
     *
     * @return the value of sys_role.roleParentId
     *
     * @mbggenerated Thu Aug 18 00:33:50 CST 2016
     */
    public Integer getRoleParentId() {
        return roleParentId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_role.roleParentId
     *
     * @param roleParentId the value for sys_role.roleParentId
     *
     * @mbggenerated Thu Aug 18 00:33:50 CST 2016
     */
    public void setRoleParentId(Integer roleParentId) {
        this.roleParentId = roleParentId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_role.permissionName
     *
     * @return the value of sys_role.permissionName
     *
     * @mbggenerated Thu Aug 18 00:33:50 CST 2016
     */
    public String getPermissionName() {
        return permissionName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_role.permissionName
     *
     * @param permissionName the value for sys_role.permissionName
     *
     * @mbggenerated Thu Aug 18 00:33:50 CST 2016
     */
    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName == null ? null : permissionName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_role.status
     *
     * @return the value of sys_role.status
     *
     * @mbggenerated Thu Aug 18 00:33:50 CST 2016
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_role.status
     *
     * @param status the value for sys_role.status
     *
     * @mbggenerated Thu Aug 18 00:33:50 CST 2016
     */
    public void setStatus(Byte status) {
        this.status = status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_role.createTime
     *
     * @return the value of sys_role.createTime
     *
     * @mbggenerated Thu Aug 18 00:33:50 CST 2016
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_role.createTime
     *
     * @param createTime the value for sys_role.createTime
     *
     * @mbggenerated Thu Aug 18 00:33:50 CST 2016
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_role.updateTime
     *
     * @return the value of sys_role.updateTime
     *
     * @mbggenerated Thu Aug 18 00:33:50 CST 2016
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_role.updateTime
     *
     * @param updateTime the value for sys_role.updateTime
     *
     * @mbggenerated Thu Aug 18 00:33:50 CST 2016
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_role
     *
     * @mbggenerated Thu Aug 18 00:33:50 CST 2016
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", sysRoleId=").append(sysRoleId);
        sb.append(", roleName=").append(roleName);
        sb.append(", roleDesc=").append(roleDesc);
        sb.append(", roleParentId=").append(roleParentId);
        sb.append(", permissionName=").append(permissionName);
        sb.append(", status=").append(status);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}