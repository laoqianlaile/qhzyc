package com.ces.config.dhtmlx.entity.construct;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.ces.config.utils.StringUtil;
import com.ces.xarch.core.entity.StringIDEntity;

/**
 * 组合构件中构件和预留区绑定关系实体类
 * 
 * @author wanglei
 * @date 2013-09-27
 */
@Entity
@Table(name = "T_XTPZ_CONSTRUCT_DETAIL")
public class ConstructDetail extends StringIDEntity implements Comparable<ConstructDetail> {

    private static final long serialVersionUID = 1L;

    /** * 下拉框检索的buttonCode */
    public static final String COMBOBOX_SEARCH = "COMBOBOX_SEARCH";

    /** * 组合构件绑定关系ID */
    private String constructId;

    /** * 构件版本ID */
    private String componentVersionId;

    /** * 预留区ID */
    private String reserveZoneId;

    /** * 是否使用公共预留区 */
    private String isCommonReserveZone;

    /** * 按钮编码 */
    private String buttonCode;

    /** * 按钮名称 */
    private String buttonName;

    /** * 按钮显示名称 */
    private String buttonDisplayName;

    /** * 按钮类型 0：一级按钮、1：按钮组、2：二级按钮 */
    private String buttonType;

    /** * 所属按钮组Code */
    private String parentButtonCode;

    /** * 按钮样式*/
    private String buttonCls;

    /** * 按钮图标 */
    private String buttonIcon;

    /** * 按钮来源 0：默认按钮 1：组装按钮 */
    private String buttonSource;

    /** * 按钮位置：0: 左边 1: 居中 2: 右边 */
    private String position;

    /** * 显示顺序 */
    private Integer showOrder;

    /** * 构件宽度 */
    private String width;

    /** * 构件高度 */
    private String height;

    /** * 树节点类型 0：根节点 1：空节点 2：表节点 4：空字段节点 5：物理表组节点 */
    private String treeNodeType;

    /** * 树节点属性：根节点——空；空节点——值；字段节点（跨表）——字段标签；表节点——表名；物理表组节点——逻辑表组Code或ThirdParty */
    private String treeNodeProperty;

    /** * 页面组装类型 0:弹出 1:嵌入 2:标签页 */
    private String assembleType;

    /** * 按钮点击事件处理前调用的JS */
    private String beforeClickJs;

    /** * 下拉框检索的选项 */
    private String searchComboOptions;

    /** * 预留区别名 */
    private String reserveZoneAlias;

    /** * 构件别名+版本 */
    private String componentAliasAndVersion;

    public String getConstructId() {
        return constructId;
    }

    public void setConstructId(String constructId) {
        this.constructId = constructId;
    }

    public String getComponentVersionId() {
        return componentVersionId;
    }

    public void setComponentVersionId(String componentVersionId) {
        this.componentVersionId = componentVersionId;
    }

    public String getReserveZoneId() {
        return reserveZoneId;
    }

    public void setReserveZoneId(String reserveZoneId) {
        this.reserveZoneId = reserveZoneId;
    }

    public String getIsCommonReserveZone() {
        return isCommonReserveZone;
    }

    public void setIsCommonReserveZone(String isCommonReserveZone) {
        this.isCommonReserveZone = isCommonReserveZone;
    }

    public String getButtonCode() {
        return buttonCode;
    }

    public void setButtonCode(String buttonCode) {
        this.buttonCode = buttonCode;
    }

    public String getButtonName() {
        return buttonName;
    }

    public void setButtonName(String buttonName) {
        this.buttonName = buttonName;
    }

    public String getButtonDisplayName() {
        return buttonDisplayName;
    }

    public void setButtonDisplayName(String buttonDisplayName) {
        this.buttonDisplayName = buttonDisplayName;
    }

    public String getButtonType() {
        return buttonType;
    }

    public void setButtonType(String buttonType) {
        this.buttonType = buttonType;
    }

    public String getParentButtonCode() {
        return parentButtonCode;
    }

    public void setParentButtonCode(String parentButtonCode) {
        this.parentButtonCode = parentButtonCode;
    }

    public String getButtonCls() {
        return buttonCls;
    }

    public void setButtonCls(String buttonCls) {
        this.buttonCls = buttonCls;
    }

    public String getButtonIcon() {
        return buttonIcon;
    }

    public void setButtonIcon(String buttonIcon) {
        this.buttonIcon = buttonIcon;
    }

    public String getButtonSource() {
        return buttonSource;
    }

    public void setButtonSource(String buttonSource) {
        this.buttonSource = buttonSource;
    }

    public String getPosition() {
        if (StringUtil.isEmpty(position)) {
            position = "0";
        }
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Integer getShowOrder() {
        return showOrder;
    }

    public void setShowOrder(Integer showOrder) {
        this.showOrder = showOrder;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getTreeNodeType() {
        return treeNodeType;
    }

    public void setTreeNodeType(String treeNodeType) {
        this.treeNodeType = treeNodeType;
    }

    public String getTreeNodeProperty() {
        return treeNodeProperty;
    }

    public void setTreeNodeProperty(String treeNodeProperty) {
        this.treeNodeProperty = treeNodeProperty;
    }

    public String getAssembleType() {
        return assembleType;
    }

    public void setAssembleType(String assembleType) {
        this.assembleType = assembleType;
    }

    public String getBeforeClickJs() {
        return beforeClickJs;
    }

    public void setBeforeClickJs(String beforeClickJs) {
        this.beforeClickJs = beforeClickJs;
    }

    public String getSearchComboOptions() {
        return searchComboOptions;
    }

    public void setSearchComboOptions(String searchComboOptions) {
        this.searchComboOptions = searchComboOptions;
    }

    public void setComponentAliasAndVersion(String componentAliasAndVersion) {
        this.componentAliasAndVersion = componentAliasAndVersion;
    }

    @Transient
    public String getReserveZoneAlias() {
        return reserveZoneAlias;
    }

    public void setReserveZoneAlias(String reserveZoneAlias) {
        this.reserveZoneAlias = reserveZoneAlias;
    }

    @Transient
    public String getComponentAliasAndVersion() {
        return componentAliasAndVersion;
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
        final ConstructDetail other = (ConstructDetail) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public int compareTo(ConstructDetail o) {
        if (o == null) {
            return 1;
        }
        if (StringUtil.isEmpty(o.getButtonType())) {
            return 1;
        }
        if (StringUtil.isEmpty(this.getButtonType())) {
            return -1;
        }
        if (("0".equals(this.getButtonType()) || "1".equals(this.getButtonType())) && ("0".equals(o.getButtonType()) || "1".equals(o.getButtonType()))) {
            if (Integer.parseInt(this.getPosition()) > Integer.parseInt(o.getPosition())) {
                return 1;
            } else if (Integer.parseInt(this.getPosition()) < Integer.parseInt(o.getPosition())) {
                return -1;
            } else {
                if (this.getShowOrder().intValue() > o.getShowOrder().intValue()) {
                    return 1;
                } else if (this.getShowOrder().intValue() < o.getShowOrder().intValue()) {
                    return -1;
                } else {
                    return 0;
                }
            }
        }
        if (Integer.parseInt(StringUtil.null2zero(this.getButtonType())) > Integer.parseInt(StringUtil.null2zero(o.getButtonType()))) {
            return 1;
        } else if (Integer.parseInt(StringUtil.null2zero(this.getButtonType())) < Integer.parseInt(StringUtil.null2zero(o.getButtonType()))) {
            return -1;
        } else {
            if (!this.getParentButtonCode().equals(o.getParentButtonCode())) {
                if (this.getParentButtonCode().compareTo(o.getParentButtonCode()) > 0) {
                    return 1;
                } else {
                    return -1;
                }
            } else {
                if (this.getShowOrder().intValue() > o.getShowOrder().intValue()) {
                    return 1;
                } else if (this.getShowOrder().intValue() < o.getShowOrder().intValue()) {
                    return -1;
                }
            }
        }
        return 0;
    }
}