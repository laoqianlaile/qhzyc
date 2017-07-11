package com.ces.config.dhtmlx.entity.release;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ces.xarch.core.entity.StringIDEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 系统发布实体类
 * 
 * @author wanglei
 * @date 2013-11-15
 */
@Entity
@Table(name = "T_XTPZ_SYSTEM_RELEASE")
public class Release extends StringIDEntity implements Comparable<Release> {

    private static final long serialVersionUID = 1L;

    /** * 发布日期 */
    private Date releaseDate;

    /** * 已发布系统的版本号，发布更新包时使用 */
    private String releaseSystemVersion;

    /** * 版本号 */
    private String version;

    /** * 文件名称 */
    private String fileName;

    /** * 介绍 */
    private String remark;

    /** * 系统名称 */
    private String systemName;

    /** * 系统菜单的ID */
    private String rootMenuId;

    /** * 类型 0：系统，1：更新包 */
    private String type;

    /** * 系统版本ID */
    private String systemVersionId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getReleaseSystemVersion() {
        return releaseSystemVersion;
    }

    public void setReleaseSystemVersion(String releaseSystemVersion) {
        this.releaseSystemVersion = releaseSystemVersion;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getRootMenuId() {
        return rootMenuId;
    }

    public void setRootMenuId(String rootMenuId) {
        this.rootMenuId = rootMenuId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSystemVersionId() {
        return systemVersionId;
    }

    public void setSystemVersionId(String systemVersionId) {
        this.systemVersionId = systemVersionId;
    }

    @Override
    public int compareTo(Release o) {
        if (o != null) {
            String[] sourceVersions = this.getVersion().split("\\.");
            String[] targetVersions = o.getVersion().split("\\.");
            if (sourceVersions.length > 0 && targetVersions.length > 0) {
                if (sourceVersions.length >= targetVersions.length) {
                    for (int i = 0; i < targetVersions.length; i++) {
                        if (Integer.parseInt(sourceVersions[i]) > Integer.parseInt(targetVersions[i])) {
                            return 1;
                        } else if (Integer.parseInt(sourceVersions[i]) < Integer.parseInt(targetVersions[i])) {
                            return -1;
                        }
                    }
                    return 1;
                } else {
                    for (int i = 0; i < sourceVersions.length; i++) {
                        if (Integer.parseInt(sourceVersions[i]) > Integer.parseInt(targetVersions[i])) {
                            return 1;
                        } else if (Integer.parseInt(sourceVersions[i]) < Integer.parseInt(targetVersions[i])) {
                            return -1;
                        }
                    }
                    return -1;
                }
            }
        }
        return 0;
    }

}