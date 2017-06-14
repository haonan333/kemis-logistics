package cn.kemis.domain;

/**
 * @author 刘体阳 jefferlzu@gmail.com
 *         Created on 2017-03-11
 */
public class AvatarUploadDomain {
    private String largeImageStr;
    private String smallImageStr;

    public String getLargeImageStr() {
        return largeImageStr;
    }

    public void setLargeImageStr(String largeImageStr) {
        this.largeImageStr = largeImageStr;
    }

    public String getSmallImageStr() {
        return smallImageStr;
    }

    public void setSmallImageStr(String smallImageStr) {
        this.smallImageStr = smallImageStr;
    }
}
