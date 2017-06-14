package cn.kemis.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 刘体阳 jefferlzu@gmail.com
 *         Created on 2017-01-14
 */
public class ModuleTreeNode {
    private Integer id;
    private String title;
    private Boolean has_children;
    private int level;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getHas_children() {
        return has_children;
    }

    public void setHas_children(Boolean has_children) {
        this.has_children = has_children;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    private List<ModuleTreeNode> children;

    public ModuleTreeNode() {
        children = new ArrayList<>();
    }

    public void addChildren(ModuleTreeNode node) {
        children.add(node);
    }

    public List<ModuleTreeNode> getChildren() {
        return children;
    }
}
