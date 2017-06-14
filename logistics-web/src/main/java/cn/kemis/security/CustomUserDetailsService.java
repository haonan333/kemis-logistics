package cn.kemis.security;

import cn.kemis.domain.SysRoleDomain;
import cn.kemis.model.SysUser;
import cn.kemis.service.RoleService;
import cn.kemis.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        /* user对应数据库中的用户表，是最终存储用户和密码的表 */
        SysUser user = userService.findByUsername(username);

        if (user == null) throw new UsernameNotFoundException("UserName " + username + " not found");

        SecurityUser securityUser = new SecurityUser(user);
        List<SysRoleDomain> roles = roleService.selectRoleDomainByUserId(user.getSysUserId());
        securityUser.setRoles(roles);

        return securityUser;
    }

}




