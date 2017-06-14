package cn.kemis.domain.api.base;

import java.io.Serializable;

/**
 * .
 *
 * @author yt.zhang .
 * @version 1.0 .
 * @date 2016-08-18 01:35 .
 */
public class BaseResponse  implements Serializable {

    private String code;
    private String msg;
    private Object data;
    private int total;
    private int draw;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getDraw() {
        return draw;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
