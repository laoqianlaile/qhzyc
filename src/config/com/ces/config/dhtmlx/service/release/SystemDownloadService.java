package com.ces.config.dhtmlx.service.release;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.ces.config.dhtmlx.dao.release.SystemDownloadDao;
import com.ces.config.dhtmlx.entity.release.SystemDownload;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;

/**
 * 系统下载Service
 * 
 * @author wanglei
 * @date 2013-11-19
 */
@Component("systemDownloadService")
public class SystemDownloadService extends ConfigDefineDaoService<SystemDownload, SystemDownloadDao> {

    /*
     * (non-Javadoc)
     * 
     * @see com.ces.xarch.core.service.StringIDService#setDaoUnBinding(com.ces.xarch.core.persistence.jpa.StringIDDao)
     */
    @Autowired
    @Qualifier("systemDownloadDao")
    @Override
    protected void setDaoUnBinding(SystemDownloadDao dao) {
        super.setDaoUnBinding(dao);
    }

    /**
     * 获取系统下载信息
     * 
     * @param systemReleaseId 系统发布ID
     * @return List<SystemDownload>
     */
    public List<SystemDownload> getBySystemReleaseId(String systemReleaseId) {
        return getDao().getBySystemReleaseId(systemReleaseId);
    }
}
