package com.ces.config.dhtmlx.dao.release;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.ces.config.dhtmlx.entity.release.SystemDownload;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

/**
 * 系统下载Dao
 * 
 * @author wanglei
 * @date 2013-11-19
 */
public interface SystemDownloadDao extends StringIDDao<SystemDownload> {

    /**
     * 获取系统下载信息
     * 
     * @param systemReleaseId 系统发布ID
     * @return List<SystemDownload>
     */
    @Query("from SystemDownload where systemReleaseId=? order by downloadDate")
    public List<SystemDownload> getBySystemReleaseId(String systemReleaseId);
}
