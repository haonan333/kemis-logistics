package cn.kemis.domain;

import cn.kemis.model.Express;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * @author 刘体阳 jefferlzu@gmail.com
 *         Created on 2016-09-11
 */
public class ExpressWorkProcessDomain extends Express {

    private String workProcess;

    @JsonFormat(pattern ="yyyy-MM-dd HH:mm", locale = "zh" , timezone="GMT+8")
    private Date workProcessCreateTime;

    public String getWorkProcess() {
        return workProcess;
    }

    public void setWorkProcess(String workProcess) {
        this.workProcess = workProcess;
    }

    public Date getWorkProcessCreateTime() {
        return workProcessCreateTime;
    }

    public void setWorkProcessCreateTime(Date workProcessCreateTime) {
        this.workProcessCreateTime = workProcessCreateTime;
    }
}
