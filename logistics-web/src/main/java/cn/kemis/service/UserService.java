package cn.kemis.service;

import cn.kemis.Constants;
import cn.kemis.dao.mapper.SysUserMapper;
import cn.kemis.domain.AvatarUploadDomain;
import cn.kemis.domain.SysUserDomain;
import cn.kemis.domain.UsersForSelectDomain;
import cn.kemis.domain.api.base.BaseResponse;
import cn.kemis.model.SysRole;
import cn.kemis.model.SysUser;
import cn.kemis.model.SysUserExample;
import cn.kemis.model.SysUserHasSysRoleExample;
import cn.kemis.model.SysUserHasSysRoleKey;
import cn.kemis.util.FileUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserService extends BaseService<SysUserMapper, SysUser, SysUserExample> {

    @Autowired
    private SysUserHasSysRoleService sysUserHasSysRoleService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Value("${file.server.url.upload}")
    private String uploadFilePath;

    public SysUser findByUsername(String username) {
        SysUserExample example = new SysUserExample();
        example.createCriteria().andUsernameEqualTo(username);
        List<SysUser> sysUserList = mapper.selectByExample(example);
        SysUser user = null;
        if (sysUserList.size() > 0) {
            user = sysUserList.get(0);
        }
        return user;
    }

    public PageInfo<SysUser> findByName(String name, int count, int pageSize) {
        SysUserExample example = new SysUserExample();
        example.createCriteria().andRealNameEqualTo(name);

        return selectPageByExample(example, count, pageSize);
    }

    public SysUser findById(int id) {
        return mapper.selectByPrimaryKey(id);
    }

    /**
     * 查询所有用户id和用户名 用于页面下拉列表
     * @return baseResponse
     */
    public BaseResponse usersForSelect() {
        List<UsersForSelectDomain> list = mapper.usersForSelect();
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setCode("0");
        baseResponse.setMsg("success");
        baseResponse.setData(list);
        return baseResponse;
    }

    public PageInfo<SysUserDomain> searchUserList(PageInfo<SysUser> pageInfo) {
        SysUser user = pageInfo.getList().get(0);

        SysUserExample example = new SysUserExample();
        SysUserExample.Criteria criteria = example.createCriteria();

        if (StringUtils.isNotBlank(user.getRealName())) {
            criteria.andRealNameLike("%" + user.getRealName() + "%");
        }

        if (user.getStatus() != null) {
            criteria.andStatusEqualTo(user.getStatus());
        }

        example.setOrderByClause("sysUserId desc");

        PageHelper.startPage(pageInfo.getPageNum(), pageInfo.getSize());
        PageInfo<SysUserDomain> userPageInfo = new PageInfo<>(mapper.selectWithRoleByRecord(user));
        userPageInfo.setPages(pageInfo.getPages());
        return userPageInfo;
    }

    @Transactional
    public BaseResponse save(SysUserDomain sysUserDomain) {
        BaseResponse baseResponse = new BaseResponse();
        if (StringUtils.isBlank(sysUserDomain.getUsername())) {
            baseResponse.setCode("450");
            baseResponse.setMsg("用户名不能为空！");
            return baseResponse;
        }

        if (StringUtils.isBlank(sysUserDomain.getRealName())) {
            baseResponse.setCode("451");
            baseResponse.setMsg("用户姓名不能为空！");
            return baseResponse;
        }

        // 检查用户名是否存在
        SysUser sysUser = this.findByUsername(sysUserDomain.getUsername());
        if (sysUser != null) {
            baseResponse.setCode("452");
            baseResponse.setMsg("用户名已存在！");
            return baseResponse;
        }

        // 加密密码
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(4);
        if (StringUtils.isBlank(sysUserDomain.getPassword())) {
            sysUserDomain.setPassword(encoder.encode(sysUserDomain.getUsername()));
        } else {
            sysUserDomain.setPassword(encoder.encode(sysUserDomain.getPassword()));
        }

        sysUser = new SysUser();
        BeanUtils.copyProperties(sysUserDomain, sysUser);

        super.insertSelective(sysUser);

        if (sysUser.getSysUserId() != null) {

            saveUserRole(sysUserDomain, sysUser);

            baseResponse.setCode("0");
            baseResponse.setMsg("新增用户成功！");
        } else {
            baseResponse.setCode("453");
            baseResponse.setMsg("保存用户失败！");
        }

        return baseResponse;
    }

    private void saveUserRole(SysUserDomain sysUserDomain, SysUser sysUser) {
        List<SysUserHasSysRoleKey> userRoles = new ArrayList<>();
        SysUserHasSysRoleKey userRole = null;
        for (SysRole role : sysUserDomain.getRoleList()) {
            userRole = new SysUserHasSysRoleKey();
            userRole.setUserId(sysUser.getSysUserId());
            userRole.setRoleId(role.getSysRoleId());
            userRoles.add(userRole);
        }

        sysUserHasSysRoleService.batchInsertSelective(userRoles, SysUserHasSysRoleKey.class);
    }

    @Transactional
    public BaseResponse update(SysUserDomain sysUserDomain) {
        BaseResponse baseResponse = new BaseResponse();

        if (StringUtils.isNotBlank(sysUserDomain.getUsername())) {
            SysUser user = findById(sysUserDomain.getSysUserId());
            if (!StringUtils.equals(sysUserDomain.getUsername(), user.getUsername())) {
                if (this.checkUsername(sysUserDomain.getUsername())){
                    baseResponse.setCode("450");
                    baseResponse.setMsg("用户名已存在！");

                    return baseResponse;
                }
            }
        }

        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(sysUserDomain, sysUser);

        super.updateByPrimaryKeySelective(sysUser);

        SysUserHasSysRoleExample example = new SysUserHasSysRoleExample();
        example.createCriteria().andUserIdEqualTo(sysUser.getSysUserId());
        sysUserHasSysRoleService.deleteByExample(example);

        saveUserRole(sysUserDomain, sysUser);

        baseResponse.setCode("0");
        baseResponse.setMsg("编辑用户成功！");

        return baseResponse;
    }

    @Transactional
    public BaseResponse updateUser(SysUserDomain sysUserDomain) {
        BaseResponse baseResponse = new BaseResponse();
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(sysUserDomain, sysUser);

        super.updateByPrimaryKeySelective(sysUser);

        baseResponse.setCode("0");
        baseResponse.setMsg("编辑用户成功！");

        return baseResponse;
    }

    @Transactional
    public BaseResponse changePassword(int userId, String password, String newPassword, String newPassword2) {
        BaseResponse baseResponse = new BaseResponse();

        if (StringUtils.isBlank(password)
                || StringUtils.isBlank(newPassword)
                || StringUtils.isBlank(newPassword2)) {
            baseResponse.setCode("540");
            baseResponse.setMsg("三项密码均不能为空！");

            return baseResponse;
        }

        if (newPassword.length() < 6) {
            baseResponse.setCode("541");
            baseResponse.setMsg("密码不能少于6个字符！");

            return baseResponse;
        }

        if (!StringUtils.equals(newPassword, newPassword2)) {
            baseResponse.setCode("542");
            baseResponse.setMsg("两次输入的新密码不同！");

            return baseResponse;
        }

        SysUser sysUser = findById(userId);

        if (passwordEncoder.matches(password, sysUser.getPassword())) {
            SysUser record = new SysUser();
            record.setSysUserId(userId);
            record.setPassword(passwordEncoder.encode(newPassword));
            super.updateByPrimaryKeySelective(record);

            baseResponse.setCode("0");
            baseResponse.setMsg("密码修改成功！");
        } else {
            baseResponse.setCode("543");
            baseResponse.setMsg("密码不正确！");
        }

        return baseResponse;
    }

    private boolean checkUsername(String username) {
        SysUserExample example = new SysUserExample();
        example.createCriteria().andUsernameEqualTo(username);
        List<SysUser> sysUserList = mapper.selectByExample(example);

        return sysUserList.size() > 0;
    }

    @Transactional
    public BaseResponse changePassword(SysUserDomain sysUserDomain) {
        BaseResponse baseResponse = new BaseResponse();

        if (StringUtils.isBlank(sysUserDomain.getPassword())) {
            baseResponse.setCode("540");
            baseResponse.setMsg("密码不能为空！");

            return baseResponse;
        }

        if (sysUserDomain.getPassword().length() < 6) {
            baseResponse.setCode("541");
            baseResponse.setMsg("密码不能少于6个字符！");

            return baseResponse;
        }

        SysUser record = new SysUser();
        record.setSysUserId(sysUserDomain.getSysUserId());
        record.setPassword(passwordEncoder.encode(sysUserDomain.getPassword()));
        super.updateByPrimaryKeySelective(record);

        baseResponse.setCode("0");
        baseResponse.setMsg("密码修改成功！");

        return baseResponse;
    }

    public BaseResponse avatarUpload(int userId, AvatarUploadDomain avatarUploadDomain) {
        BaseResponse baseResponse = new BaseResponse();

        String uuid = UUID.randomUUID().toString();
        String uuidPath = FileUtil.getFilePath(uuid);

        File file = new File(uploadFilePath + Constants.AVATAR_PATH + uuidPath);
        if (!file.exists()) {
            file.mkdirs();
        }

        String largeImage = uploadFilePath + Constants.AVATAR_PATH + uuidPath + uuid + Constants.AVATAR_SIZE_LARGE + Constants.AVATAR_IMAGE_SUFFIX;
        String smallImage = uploadFilePath + Constants.AVATAR_PATH + uuidPath + uuid + Constants.AVATAR_SIZE_SMALL + Constants.AVATAR_IMAGE_SUFFIX;

        if (StringUtils.isNotBlank(avatarUploadDomain.getSmallImageStr())
                && StringUtils.isNotBlank(avatarUploadDomain.getLargeImageStr())) {
            try {
                saveAvatar(avatarUploadDomain.getSmallImageStr(), smallImage);
                saveAvatar(avatarUploadDomain.getLargeImageStr(), largeImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            baseResponse.setCode("450");
            baseResponse.setMsg("未找到图片文件");

            return baseResponse;
        }

        SysUser sysUser = mapper.selectByPrimaryKey(userId);
        if (StringUtils.isNotBlank(sysUser.getAvatar())) {
            // 删除已存在头像文件
            deleteAvatar(sysUser.getAvatar());
        }

        SysUser user = new SysUser();
        user.setSysUserId(userId);
        user.setAvatar(uuidPath + uuid);
        mapper.updateByPrimaryKeySelective(user);

        baseResponse.setCode("0");
        baseResponse.setMsg("头像保存成功");

        return baseResponse;
    }

    private void deleteAvatar(String avatarPath) {
        File image = new File(uploadFilePath + Constants.AVATAR_PATH + avatarPath + Constants.AVATAR_SIZE_LARGE + Constants.AVATAR_IMAGE_SUFFIX);
        if (image.exists()) {
            image.delete();
        }
        image = new File(uploadFilePath + Constants.AVATAR_PATH + avatarPath + Constants.AVATAR_SIZE_SMALL + Constants.AVATAR_IMAGE_SUFFIX);
        if (image.exists()) {
            image.delete();
        }
    }

    private void saveAvatar(String imageStr, String imagePath) throws IOException {
        imageStr = imageStr.replace("data:image/jpeg;base64,", "");
        imageStr = imageStr.replace("data:image/png;base64,", "");
        imageStr = imageStr.replace("data:image/gif;base64,", "");
        byte[] bytes = Base64.decodeBase64(imageStr);
        for (int i = 0; i < bytes.length; i++) {
            if (bytes[i] < 0) {
                bytes[i] += 256;
            }
        }
        FileOutputStream out = new FileOutputStream(imagePath);
        out.write(bytes);
        out.flush();
        out.close();
    }

    /**
     * 获取用户头像相对地址
     * @param userId
     * @param size
     * @return
     */
    public String getAvatarImage(int userId, String size) {
        SysUser sysUser = mapper.selectByPrimaryKey(userId);
        if (StringUtils.isBlank(size)) {
            size = "s";
        }
        // 静态资源映射pathPattern + 文件路径
        if (StringUtils.isNotBlank(sysUser.getAvatar())) {
            return "/file" + Constants.AVATAR_PATH + sysUser.getAvatar() + "_" + size.toLowerCase() + Constants.AVATAR_IMAGE_SUFFIX;
        } else {
            return "";
        }
    }
}