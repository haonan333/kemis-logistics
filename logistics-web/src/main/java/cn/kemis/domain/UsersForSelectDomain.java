package cn.kemis.domain;

/**
 * 查询所有用户id和用户名 用于页面下拉列表
 * Created by xuhailong on 16/9/7.
 */
public class UsersForSelectDomain {

    private int sysUserId;

    private String realName;

    public int getSysUserId() {
        return sysUserId;
    }

    public void setSysUserId(int sysUserId) {
        this.sysUserId = sysUserId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }
}
