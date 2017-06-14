package cn.kemis.domain.api.request;

/**
 * .
 *
 * @author yt.zhang .
 * @version 1.0 .
 * @date 2016-08-19 01:11 .
 */
public class ScanInputRequest {

    private String code;
    private String workProcess;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getWorkProcess() {
        return workProcess;
    }

    public void setWorkProcess(String workProcess) {
        this.workProcess = workProcess;
    }
}
