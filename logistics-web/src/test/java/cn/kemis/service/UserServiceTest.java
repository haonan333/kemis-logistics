package cn.kemis.service;

import cn.kemis.model.SysUser;
import com.github.pagehelper.PageInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Created by liutiyang on 16/7/28.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    public void findByUsername() {
        SysUser user = userService.findByUsername("admin");
        //user = userService.findById(1);
        assertThat(user).isNotNull();
    }

    @Test
    public void findByName() {
        PageInfo<SysUser> pageInfo = userService.findByName("user", 1, 15);
        assertThat(pageInfo.getList().size()).isGreaterThanOrEqualTo(0);
    }

    @Test
    public void findById() {
        SysUser user = userService.selectByPrimaryKey(1L);
        assertThat(user).isNotNull();
    }

    @Test
    public void batchInsertSelective() {
        List<SysUser> users = new ArrayList<>();
        SysUser user = null;
        for (int i = 0; i < 10; i++) {
            user = new SysUser();
            user.setUsername(i + 1 + "");
            user.setStatus((byte) 1);
            user.setUserCode("CODE_" + (i + 1));

            users.add(user);
        }
        int selective = userService.batchInsertSelective(users, SysUser.class);
        // System.out.println(selective);
    }

    @Test
    public void batchUpdateSelective() {
        List<SysUser> users = new ArrayList<>();
        SysUser user = null;
        for (int i = 0; i < 10; i++) {
            user = new SysUser();
            user.setSysUserId(i + 1);
            user.setUserCode("CODE_" + (i + 1));

            users.add(user);
        }
        int selective = userService.batchUpdateSelective(users, SysUser.class);
        // System.out.println(selective);
    }
}
