package com.ces.config.dhtmlx.entity.release;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ces.xarch.core.entity.StringIDEntity;

/**
 * 系统发布菜单中组装的按钮实体类
 * 
 * @author wanglei
 * @date 2014-10-13
 */
@Entity
@Table(name = "T_XTPZ_SYSTEM_RELEASE_BUTTON")
public class ReleaseButton extends StringIDEntity {

    private static final long serialVersionUID = 1L;

    /** * 系统发布ID */
    private String systemReleaseId;

    /** * 系统发布的菜单的ID */
    private String releaseMenuId;

    /** * 预留区和构件绑定关系ID */
    private String constructDetailId;

    /** * 按钮名称 */
    private String buttonName;

    /** * 预留区ID */
    private String reserveZoneId;

    /** * 预留区名称 */
    private String reserveZoneName;

    /** * 构件版本ID */
    private String componentVersionId;

    /** * 构件版本名称 */
    private String componentVersionName;

    public String getSystemReleaseId() {
        return systemReleaseId;
    }

    public void setSystemReleaseId(String systemReleaseId) {
        this.systemReleaseId = systemReleaseId;
    }

    public String getReleaseMenuId() {
        return releaseMenuId;
    }

    public void setReleaseMenuId(String releaseMenuId) {
        this.releaseMenuId = releaseMenuId;
    }

    public String getConstructDetailId() {
        return constructDetailId;
    }

    public void setConstructDetailId(String constructDetailId) {
        this.constructDetailId = constructDetailId;
    }

    public String getButtonName() {
        return buttonName;
    }

    public void setButtonName(String buttonName) {
        this.buttonName = buttonName;
    }

    public String getReserveZoneId() {
        return reserveZoneId;
    }

    public void setReserveZoneId(String reserveZoneId) {
        this.reserveZoneId = reserveZoneId;
    }

    public String getReserveZoneName() {
        return reserveZoneName;
    }

    public void setReserveZoneName(String reserveZoneName) {
        this.reserveZoneName = reserveZoneName;
    }

    public String getComponentVersionId() {
        return componentVersionId;
    }

    public void setComponentVersionId(String componentVersionId) {
        this.componentVersionId = componentVersionId;
    }

    public String getComponentVersionName() {
        return componentVersionName;
    }

    public void setComponentVersionName(String componentVersionName) {
        this.componentVersionName = componentVersionName;
    }

}