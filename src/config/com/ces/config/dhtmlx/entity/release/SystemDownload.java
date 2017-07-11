package com.ces.config.dhtmlx.entity.release;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ces.xarch.core.entity.StringIDEntity;

/**
 * 系统下载实体类
 * 
 * @author wanglei
 * @date 2013-11-19
 */
@Entity
@Table(name = "T_XTPZ_SYSTEM_DOWNLOAD")
public class SystemDownload extends StringIDEntity {

    private static final long serialVersionUID = 1L;

    /** * 系统发布ID */
    private String systemReleaseId;

    /** * 基本信息 */
    private String baseInfo;

    /** * 下载日期 */
    private Date downloadDate;

    /** * 上线日期 */
    private Date onlineDate;

    /** * 介绍 */
    private String remark;

    public String getSystemReleaseId() {
        return systemReleaseId;
    }

    public void setSystemReleaseId(String systemReleaseId) {
        this.systemReleaseId = systemReleaseId;
    }

    public String getBaseInfo() {
        return baseInfo;
    }

    public void setBaseInfo(String baseInfo) {
        this.baseInfo = baseInfo;
    }

    public Date getDownloadDate() {
        return downloadDate;
    }

    public void setDownloadDate(Date downloadDate) {
        this.downloadDate = downloadDate;
    }

    public Date getOnlineDate() {
        return onlineDate;
    }

    public void setOnlineDate(Date onlineDate) {
        this.onlineDate = onlineDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}