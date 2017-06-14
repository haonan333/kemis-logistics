package cn.kemis.service;

import cn.kemis.dao.mapper.SysRoleMapper;
import cn.kemis.domain.SysRoleDomain;
import cn.kemis.domain.api.base.BaseResponse;
import cn.kemis.model.SysRole;
import cn.kemis.model.SysRoleExample;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RoleService extends BaseService<SysRoleMapper, SysRole, SysRoleExample> {

    @Autowired
    private SysRoleHasSysModuleService sysRoleHasSysModuleService;

    public List<SysRoleDomain> selectRoleDomainByUserId(Integer id) {
        return mapper.selectRoleDomainByUserId(id);
    }

    public PageInfo<SysRole> searchRoleList(PageInfo<SysRole> pageInfo) {
        SysRole role = pageInfo.getList().get(0);

        SysRoleExample example = new SysRoleExample();
        SysRoleExample.Criteria criteria = example.createCriteria();

        if (StringUtils.isNotBlank(role.getRoleName())) {
            criteria.andRoleNameLike("%" + role.getRoleName() + "%");
        }

        if (StringUtils.isNotBlank(role.getRoleDesc())) {
            criteria.andRoleDescLike("%" + role.getRoleDesc() + "%");
        }

        if (role.getRoleParentId() != null) {
            criteria.andRoleParentIdEqualTo(role.getRoleParentId());
        }

        if (StringUtils.isNotBlank(role.getPermissionName())) {
            criteria.andPermissionNameEqualTo(role.getPermissionName());
        }

        example.setOrderByClause("sysRoleId asc");

        PageInfo<SysRole> rolePageInfoPageInfo = super.selectPageByExample(example, pageInfo.getPageNum(), pageInfo.getSize());
        rolePageInfoPageInfo.setPages(pageInfo.getPages());
        return rolePageInfoPageInfo;
    }

    public List<SysRole> getAll() {
        SysRoleExample example = new SysRoleExample();
        return super.selectByExample(example);
    }


    private SysRole findByRoleName(String roleName) {
        return findByRoleName(roleName, null);
    }


    private SysRole findByRoleName(String roleName, Integer sysRoleId) {
        SysRoleExample example = new SysRoleExample();
        SysRoleExample.Criteria criteria = example.createCriteria();
        criteria.andRoleNameEqualTo(roleName);

        if (sysRoleId != null) {
            criteria.andSysRoleIdNotEqualTo(sysRoleId);
        }

        List<SysRole> roleList = super.selectByExample(example);
        SysRole sysRole = null;
        if (roleList != null && roleList.size() > 0) {
            sysRole = roleList.get(0);
        }

        return sysRole;
    }

    public BaseResponse save(SysRole sysRole) {
        BaseResponse baseResponse = new BaseResponse();

        if (StringUtils.isBlank(sysRole.getRoleName())) {
            baseResponse.setCode("450");
            baseResponse.setMsg("角色名不能为空！");
            return baseResponse;
        }

        if (StringUtils.isBlank(sysRole.getPermissionName())) {
            baseResponse.setCode("451");
            baseResponse.setMsg("类型不能为空！");
            return baseResponse;
        }

        SysRole role = this.findByRoleName(sysRole.getRoleName());
        if (role != null) {
            baseResponse.setCode("452");
            baseResponse.setMsg("角色名已存在！");
            return baseResponse;
        }

        super.insertSelective(sysRole);

        if (sysRole.getSysRoleId() != null) {
            baseResponse.setCode("0");
            baseResponse.setMsg("新增角色成功！");
        } else {
            baseResponse.setCode("453");
            baseResponse.setMsg("保存角色失败！");
        }

        return baseResponse;
    }


    public BaseResponse update(SysRole sysRole) {
        BaseResponse baseResponse = new BaseResponse();

        if (sysRole.getSysRoleId() == null) {
            baseResponse.setCode("453");
            baseResponse.setMsg("角色ID不能为空！请刷新后重新尝试操作！");
            return baseResponse;
        }

        if (StringUtils.isBlank(sysRole.getRoleName())) {
            baseResponse.setCode("450");
            baseResponse.setMsg("角色名不能为空！");
            return baseResponse;
        }

        if (StringUtils.isBlank(sysRole.getPermissionName())) {
            baseResponse.setCode("451");
            baseResponse.setMsg("类型不能为空！");
            return baseResponse;
        }

        SysRole role = this.findByRoleName(sysRole.getRoleName(), sysRole.getSysRoleId());
        if (role != null) {
            baseResponse.setCode("452");
            baseResponse.setMsg("同名角色已存在！");
            return baseResponse;
        }

        super.updateByPrimaryKeySelective(sysRole);

        baseResponse.setCode("0");
        baseResponse.setMsg("编辑角色成功！");

        return baseResponse;
    }

    public List<Integer> getModuleIdList(int roleId) {

        return mapper.selectModuleIdByRoleId(roleId);
    }

    @Transactional
    public BaseResponse authoritySave(Integer roleId, Integer[] moduleIds) {

        sysRoleHasSysModuleService.deleteByRoleId(roleId);
        sysRoleHasSysModuleService.saveRoleHasModule(roleId, moduleIds);

        BaseResponse response = new BaseResponse();
        response.setCode("0");
        return response;
    }
}