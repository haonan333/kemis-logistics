package cn.kemis.domain;

import cn.kemis.model.SysModule;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;

/**
 * Created by liutiyang on 16/8/4.
 */
public class SysModuleDomain extends SysModule implements Serializable, GrantedAuthority {
    private static final long serialVersionUID = -6558237585114800888L;

    @Override
    public String getAuthority() {
        return getModuleUrl();
    }
}
