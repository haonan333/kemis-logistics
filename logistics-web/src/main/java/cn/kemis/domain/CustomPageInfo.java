package cn.kemis.domain;

import com.github.pagehelper.PageInfo;

/**
 * @author 刘体阳 jefferlzu@gmail.com
 *         Created on 2016-09-11
 */
public class CustomPageInfo<T> extends PageInfo{
    private int draw;

    public int getDraw() {
        return draw;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }
}
