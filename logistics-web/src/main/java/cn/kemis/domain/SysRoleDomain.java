package cn.kemis.domain;

import cn.kemis.model.SysRole;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liutiyang on 16/8/2.
 */
public class SysRoleDomain extends SysRole implements Serializable, GrantedAuthority {

    private static final long serialVersionUID = -5166971395270023298L;

    private List<SysModuleDomain> sysModules;

    public List<SysModuleDomain> getSysModules() {
        return sysModules;
    }

    public void setSysModules(List<SysModuleDomain> sysModules) {
        this.sysModules = sysModules;
    }

    @Override
    public String getAuthority() {
        return getPermissionName();
    }
}
