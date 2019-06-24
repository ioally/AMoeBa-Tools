package com.ioally.amoeba.dto;

/**
 * 菜单信息
 */
public class MenuInfoDto {

    /**
     * 菜单名称
     */
    private String name;
    /**
     * 菜单目标链接
     */
    private String target;
    /**
     * 菜单图标
     */
    private String icon;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
}
