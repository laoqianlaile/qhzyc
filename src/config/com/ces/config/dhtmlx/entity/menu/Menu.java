package com.ces.config.dhtmlx.entity.menu;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.ces.xarch.core.entity.StringIDEntity;

/**
 * 菜单实体类
 * 
 * @author wanglei
 * @date 2013-07-15
 */
@Entity
@Table(name = "T_XTPZ_MENU")
public class Menu extends StringIDEntity implements Comparable<Menu> {

    private static final long serialVersionUID = 1L;

    /** * 名称 */
    private String name;

    /** * 编码 */
    private String code;

    /** * 父菜单ID */
    private String parentId;

    /** * 系统ID */
    private String rootMenuId;

    /** * 是否有子菜单 */
    private Boolean hasChild;

    /** * 显示顺序 */
    private int showOrder;

    /** * 绑定类型 0-URL 1-构件 */
    private String bindingType;

    /** * 绑定构件版本的ID */
    private String componentVersionId;

    /** * 访问地址 */
    private String url;

    /** * 是否使用导航 0：不使用 1：使用 */
    private String useNavigation;

    /** * 菜单图标1 */
    private String icon1;

    /** * 菜单图标2 */
    private String icon2;

    /*** 是否快捷菜单 **/
    private String isQuickMenu;

    /** 快捷菜单图标 **/
    private String quickIcon;

    /** * 绑定构件版本的基础构件版本的ID，缓存菜单时使用 */
    private String baseComponentVersionId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getRootMenuId() {
        return rootMenuId;
    }

    public void setRootMenuId(String rootMenuId) {
        this.rootMenuId = rootMenuId;
    }

    public int getShowOrder() {
        return showOrder;
    }

    public void setShowOrder(int showOrder) {
        this.showOrder = showOrder;
    }

    public Boolean getHasChild() {
        return hasChild;
    }

    public void setHasChild(Boolean hasChild) {
        this.hasChild = hasChild;
    }

    public String getBindingType() {
        return bindingType;
    }

    public void setBindingType(String bindingType) {
        this.bindingType = bindingType;
    }

    public String getComponentVersionId() {
        return componentVersionId;
    }

    public void setComponentVersionId(String componentVersionId) {
        this.componentVersionId = componentVersionId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUseNavigation() {
        return useNavigation;
    }

    public void setUseNavigation(String useNavigation) {
        this.useNavigation = useNavigation;
    }

    public String getIcon1() {
        return icon1;
    }

    public void setIcon1(String icon1) {
        this.icon1 = icon1;
    }

    public String getIcon2() {
        return icon2;
    }

    public void setIcon2(String icon2) {
        this.icon2 = icon2;
    }

    public String getIsQuickMenu() {
        return isQuickMenu;
    }

    public void setIsQuickMenu(String isQuickMenu) {
        this.isQuickMenu = isQuickMenu;
    }

    public String getQuickIcon() {
        return quickIcon;
    }

    public void setQuickIcon(String quickIcon) {
        this.quickIcon = quickIcon;
    }

    @Transient
    public String getBaseComponentVersionId() {
        return baseComponentVersionId;
    }

    public void setBaseComponentVersionId(String baseComponentVersionId) {
        this.baseComponentVersionId = baseComponentVersionId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Menu other = (Menu) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public int compareTo(Menu o) {
        if (o == null) {
            return 1;
        }
        if (this.getShowOrder() > o.getShowOrder()) {
            return 1;
        } else if (this.getShowOrder() < o.getShowOrder()) {
            return -1;
        }
        return 0;
    }

}