package com.ces.config.dhtmlx.entity.systemupdate;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ces.xarch.core.entity.StringIDEntity;

/**
 * 系统更新实体类
 * 
 * @author wanglei
 * @date 2015-02-12
 */
@Entity
@Table(name = "T_XTPZ_SYSTEM_UPDATE")
public class SystemUpdate extends StringIDEntity {

    private static final long serialVersionUID = -4018572037286511120L;

    /** * 更新包版本 */
    private String updateVersion;

    /** * 数据库实例名 */
    private String updateTime;

    public String getUpdateVersion() {
        return updateVersion;
    }

    public void setUpdateVersion(String updateVersion) {
        this.updateVersion = updateVersion;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

}
