package cn.kemis.domain.excelEntity;

import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * ytz
 */
public class KeywordEntity {

    @Excel(name = "产品名称")
    private String keyword;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
