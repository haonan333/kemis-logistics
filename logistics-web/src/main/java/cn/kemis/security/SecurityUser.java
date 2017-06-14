package cn.kemis.security;

import cn.kemis.domain.SysModuleDomain;
import cn.kemis.domain.SysRoleDomain;
import cn.kemis.model.SysModule;
import cn.kemis.model.SysRole;
import cn.kemis.model.SysUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SecurityUser extends SysUser implements UserDetails {
    private static final long serialVersionUID = 1L;

    private List<SysRoleDomain> roles;

    private Set<SysModuleDomain> modules;

    public List<SysRoleDomain> getRoles() {
        return roles;
    }

    public Set<SysModuleDomain> getModules() {
        return modules;
    }

    public void setRoles(List<SysRoleDomain> roles) {
        this.roles = roles;
        this.modules = getModuleDomains();
    }

    public SecurityUser(SysUser user) {
        if (user != null) {
            this.setSysUserId(user.getSysUserId());
            this.setUsername(user.getUsername());
            this.setPassword(user.getPassword());
            this.setUserCode(user.getUserCode());
            this.setRealName(user.getRealName());
            this.setMobile(user.getMobile());
            this.setEmail(user.getEmail());
            this.setAddress(user.getAddress());
            this.setAvatar(user.getAvatar());
            this.setLoginCount(user.getLoginCount());
            this.setLastLogin(user.getLastLogin());
            this.setStatus(user.getStatus());
            this.setCreateTime(user.getCreateTime());
            this.setUpdateTime(user.getUpdateTime());
        }
    }

    @Transient
    private Set<SysModuleDomain> getModuleDomains() {
        Set<SysModuleDomain> moduleDomains = new HashSet<>();
        for (SysRoleDomain role : roles) {
            moduleDomains.addAll(role.getSysModules());
        }
        return moduleDomains;
    }

    @Override
    @Transient
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        List<SysRoleDomain> userRoles = this.getRoles();
        Set<SysModuleDomain> roleModule = this.getModules();

        authorities.addAll(userRoles);
        authorities.addAll(roleModule);

        return authorities;
    }

    @Override
    public String getPassword() {
        return super.getPassword();
    }

    @Override
    public String getUsername() {
        //return super.getEmail();
        return super.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}