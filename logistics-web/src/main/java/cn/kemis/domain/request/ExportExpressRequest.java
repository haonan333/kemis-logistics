package cn.kemis.domain.request;

import cn.kemis.model.ShipOrder;

/**
 * .
 * 导出快递单
 * @author yt.zhang .
 * @version 1.0 .
 * @date 2016-08-21 08:29 .
 */
public class ExportExpressRequest extends ShipOrder {

    private String expressCompany ;
    private String path ;
    private String uuidPath ;
    private String tempPath ; //临时目录
    private String zipFileName ;

    public String getUuidPath() {
        return uuidPath;
    }

    public void setUuidPath(String uuidPath) {
        this.uuidPath = uuidPath;
    }

    public String getExpressCompany() {
        return expressCompany;
    }

    public void setExpressCompany(String expressCompany) {
        this.expressCompany = expressCompany;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTempPath() {
        return tempPath;
    }

    public void setTempPath(String tempPath) {
        this.tempPath = tempPath;
    }

    public String getZipFileName() {
        return zipFileName;
    }

    public void setZipFileName(String zipFileName) {
        this.zipFileName = zipFileName;
    }
}
