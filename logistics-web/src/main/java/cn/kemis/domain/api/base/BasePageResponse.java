package cn.kemis.domain.api.base;

import java.io.Serializable;

/**
 * .
 *
 * @author yt.zhang .
 * @version 1.0 .
 * @date 2016-08-18 01:35 .
 */
public class BasePageResponse implements Serializable {

    private Object data;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
