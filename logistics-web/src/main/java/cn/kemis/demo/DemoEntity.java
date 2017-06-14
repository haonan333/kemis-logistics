package cn.kemis.demo;

import org.jeecgframework.poi.excel.annotation.Excel;

public class DemoEntity implements java.io.Serializable {

    @Excel(name = "快递单ID")
    private String        area;

    @Excel(name = "物流单号")
    private String        name;
    @Excel(name = "名称1")
    private String        name1;
    @Excel(name = "名称2")
    private String        name2;
    @Excel(name = "名称3")
    private String        name3;

    public String getName3() {
        return name3;
    }

    public void setName3(String name3) {
        this.name3 = name3;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    @Override
    public String toString() {
        return "DemoEntity{" +
                "area='" + area + '\'' +
                ", name='" + name + '\'' +
                ", name1='" + name1 + '\'' +
                ", name2='" + name2 + '\'' +
                ", name3='" + name3 + '\'' +
                '}';
    }
}