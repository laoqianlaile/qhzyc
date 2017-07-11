package com.ces.config.dhtmlx.entity.code;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.ces.xarch.core.entity.StringIDEntity;

/**
 * 编码类型实体类
 * 
 * @author wanglei
 * @date 2013-07-15
 */
@Entity
@Table(name = "T_XTPZ_CODE_TYPE")
public class CodeType extends StringIDEntity {

    private static final long serialVersionUID = -1591101565425646864L;

    /** * 编码类型名称 */
    private String name;

    /** * 类型编码 */
    private String code;

    /** * 是否是系统编码 0:不是 1:是 */
    private String isSystem;

    /** * 是否是业务表编码 0:不是 1:是 */
    private String isBusiness;

    /** 是否使用缓存 0:否 1:是 */
    private String isCache;

    /** 系统ID */
    private String systemId;

    /** * 显示顺序 */
    private Integer showOrder;

    @Column(name = "NAME")
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

    public String getIsSystem() {
        return isSystem;
    }

    public void setIsSystem(String isSystem) {
        this.isSystem = isSystem;
    }

    public String getIsBusiness() {
        return isBusiness;
    }

    public void setIsBusiness(String isBusiness) {
        this.isBusiness = isBusiness;
    }

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public String getIsCache() {
        return isCache;
    }

    public void setIsCache(String isCache) {
        this.isCache = isCache;
    }

    public Integer getShowOrder() {
        return showOrder;
    }

    public void setShowOrder(Integer showOrder) {
        this.showOrder = showOrder;
    }

}
