package cn.kemis.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * 修正工序列表 VO
 * Created by xuhailong on 16/9/8.
 */
public class RevisionListDomain {

    private int workProcessId;
    private String userName;
    private String expressNumber;
    private String workProcess;
    private String status;
    @JsonFormat(pattern ="yyyy-MM-dd HH:mm:ss", locale = "zh" , timezone="GMT+8")
    private Date createTime;

    public int getWorkProcessId() {
        return workProcessId;
    }

    public void setWorkProcessId(int workProcessId) {
        this.workProcessId = workProcessId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getExpressNumber() {
        return expressNumber;
    }

    public void setExpressNumber(String expressNumber) {
        this.expressNumber = expressNumber;
    }

    public String getWorkProcess() {
        return workProcess;
    }

    public void setWorkProcess(String workProcess) {
        this.workProcess = workProcess;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
