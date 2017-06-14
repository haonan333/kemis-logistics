package cn.kemis.service;

import cn.kemis.domain.api.base.BaseResponse;
import cn.kemis.model.SysUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * PowerMockito 手工 Mock，支持 静态 私有 和 final 方法。
 * @author 刘体阳 jefferlzu@gmail.com
 *         Created on 2017-02-21
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({UserService.class})
public class UserServiceMockTest {

    @Test
    public void testMockStatic() {
        /*
        // 静态方法的mock
        PowerMockito.mockStatic(UserService.class);
        PowerMockito.when(UserService.sleep("A")).thenReturn("B");
        System.out.println(UserService.sleep("A"));
        PowerMockito.verifyStatic();
        */
    }

    @Test
    public void testMockPrivate() throws Exception {
        // 私有方法的mock,getEatInfo => eat, eat 是私有方法
        /*UserService userServiceMock = PowerMockito.mock(UserService.class);
        SysUserDomain userDomain = new SysUserDomain();
        SysUser sysUser = new SysUser();
        BaseResponse baseResponse = new BaseResponse();
        PowerMockito.when(userServiceMock, "saveUserRole", userDomain, sysUser).thenReturn(baseResponse);

        PowerMockito.doCallRealMethod().when(userServiceMock).save(userDomain);*/
    }

    @Test
    public void testMockFinal() {
        // final 方法的 mock
        UserService userServiceMock = PowerMockito.mock(UserService.class);
        SysUser sysUser = new SysUser();
        PowerMockito.when(userServiceMock.findByUsername("A")).thenReturn(sysUser);
        System.out.println(userServiceMock.findByUsername("A"));
    }

    @Test
    public void testMock() throws Exception {
        UserService userServiceMock = PowerMockito.mock(UserService.class);

        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setCode("0");
        baseResponse.setMsg("success");

        PowerMockito.when(userServiceMock.usersForSelect()).thenReturn(baseResponse);

        ObjectMapper mapper = new ObjectMapper();
        System.out.println(mapper.writeValueAsString(userServiceMock.usersForSelect()));

    }

}
