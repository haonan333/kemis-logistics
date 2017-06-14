package cn.kemis.service;

import cn.kemis.dao.mapper.SysUserMapper;
import cn.kemis.domain.UsersForSelectDomain;
import cn.kemis.model.SysUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

/**
 * Mockito 和 SpringBootTest 结合，不支持 静态 私有 和 final 方法。
 * @author 刘体阳 jefferlzu@gmail.com
 *         Created on 2017-02-21
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceSpringMockTest {

    /**
     * @Mock 对象会注入到 @InjectMocks 对象中
     */
    @Mock
    private SysUserMapper mapper;

    @InjectMocks
    private UserService userService;

    @Test
    public void testUsersForSelect() throws Exception {

        List<UsersForSelectDomain> list = new ArrayList<>();
        UsersForSelectDomain usersForSelectDomain = new UsersForSelectDomain();
        usersForSelectDomain.setRealName("Test");
        list.add(usersForSelectDomain);
        // mock mapper.usersForSelect()
        when(mapper.usersForSelect()).thenReturn(list);

        ObjectMapper mapper = new ObjectMapper();
        String string = mapper.writeValueAsString(userService.usersForSelect());
        System.out.println(string);
        assertThat(string, allOf(containsString("{\"code\":\"0\",\"msg\":\"success\""), containsString("\"realName\":\"Test\"")));
        //assertThat(string, anyOf(containsString("{\"code\":\"0\",\"msg\":\"success\""), containsString("\"realName\":\"Test\"")));
    }

    /**
     * 假设 userService.usersForSelect() 调用了尚未实现的 userService.findById()
     * 需要用 spy 来部分 mock userService
     */
    @Test
    public void testServiceCallSelfMethod() throws Exception {
        // 将 userService 部分 mock
        userService = spy(userService);

        SysUser sysUser = new SysUser();
        sysUser.setRealName("spy");
        // 这里必须用 doReturn() 而不能是 when().thenReturn()
        doReturn(sysUser).when(userService).findById(anyInt());

        ObjectMapper mapper = new ObjectMapper();
        String string = mapper.writeValueAsString(userService.findById(anyInt()));
        System.out.println(string);
        assertThat(string, containsString("\"realName\":\"spy\""));
    }
}
