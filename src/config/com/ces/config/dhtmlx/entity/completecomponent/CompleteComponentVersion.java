package com.ces.config.dhtmlx.entity.completecomponent;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ces.xarch.core.entity.StringIDEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 成品构件版本实体类
 * 
 * @author wanglei
 * @date 2014-02-17
 */
@Entity
@Table(name = "T_XTPZ_COMP_COMPONENT_VERSION")
public class CompleteComponentVersion extends StringIDEntity {

    private static final long serialVersionUID = -8353178873323581026L;

    /** * 成品构件版本号 */
    private String version;

    /** * 前台技术[dhtmlx/ext] */
    private String views;

    /** * 成品构件说明 */
    private String remark;

    /** * 成品构件分类ID */
    private String areaId;

    /** * 成品构件存储路径 */
    private String path;

    /** * 成品构件导入时间 */
    private Date importDate;

    /** * 成品构件 */
    private CompleteComponent component;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public Date getImportDate() {
        return importDate;
    }

    public void setImportDate(Date importDate) {
        this.importDate = importDate;
    }

    @ManyToOne
    @JoinColumn(name = "COMPONENT_ID")
    public CompleteComponent getComponent() {
        return component;
    }

    public void setComponent(CompleteComponent component) {
        this.component = component;
    }
}
