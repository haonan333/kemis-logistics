package cn.kemis.user;

import cn.kemis.model.SysUser;
import cn.kemis.model.SysUserHasSysRoleKey;
import cn.kemis.service.SysUserHasSysRoleService;
import cn.kemis.service.UserService;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author 刘体阳 jefferlzu@gmail.com
 * Created on 2016-08-28
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ImportUser {

    @Autowired
    private UserService userService;
    @Autowired
    private SysUserHasSysRoleService userRoleService;

    /**
     * 通过 txt 文件导入用户,用户名密码均为姓名拼音
     * @throws Exception
     */
    @Test
    public void importUser() throws Exception {
        InputStream inputStream = ImportUser.class.getResourceAsStream("/initUser.txt");
        List<String> stringList = read(inputStream);

        String[] userArray = null;
        List<SysUser> userList = new ArrayList<>();
        SysUser sysUser = null;

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(4);

        for (String str : stringList) {
            userArray = str.split("\t");

            sysUser = new SysUser();
            sysUser.setUserCode(userArray[0]);
            sysUser.setUsername(getStringPinYin(userArray[1]));
            sysUser.setPassword(bCryptPasswordEncoder.encode(sysUser.getUsername()));
            sysUser.setRealName(userArray[1]);

            userList.add(sysUser);
        }

        List<SysUserHasSysRoleKey> userRoleList = new ArrayList<>();
        SysUserHasSysRoleKey userRole = null;
        // userService.batchInsertSelective(userList, SysUser.class);
        for (SysUser user : userList) {
            int id = userService.insertSelective(user);
            System.out.println(sysUser);

            userRole = new SysUserHasSysRoleKey();
            userRole.setRoleId(2);
            userRole.setUserId(user.getSysUserId());

            userRoleList.add(userRole);
        }

        userRoleService.batchInsertSelective(userRoleList, SysUserHasSysRoleKey.class);
    }

    private String getStringPinYin(String str) throws BadHanyuPinyinOutputFormatCombination {

        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);

        StringBuilder sb = new StringBuilder();
        String tempPinyin = null;

        for (int i = 0; i < str.length(); ++i) {
            tempPinyin = PinyinHelper.toHanyuPinyinStringArray(str.charAt(i), format)[0];
            if (tempPinyin == null) {
                // 如果str.charAt(i)非汉字，则保持原样
                sb.append(str.charAt(i));
            } else {
                sb.append(tempPinyin);
            }
        }
        return sb.toString();
    }

    private List<String> read(InputStream inputStream) throws Exception {
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        List<String> stringList = new ArrayList<>();
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            if (StringUtils.isNotBlank(line)) {
                stringList.add(line);
            }
        }
        bufferedReader.close();
        inputStreamReader.close();
        inputStream.close();
        return stringList;
    }
}
