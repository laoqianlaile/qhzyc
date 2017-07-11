package com.ces.config.dhtmlx.service.release;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.ces.config.dhtmlx.dao.release.ReleaseDetailDao;
import com.ces.config.dhtmlx.entity.release.ReleaseDetail;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;

/**
 * 系统发布详情Service
 * 
 * @author wanglei
 * @date 2014-10-13
 */
@Component("releaseDetailService")
public class ReleaseDetailService extends ConfigDefineDaoService<ReleaseDetail, ReleaseDetailDao> {

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.service.StringIDService#setDaoUnBinding(com.ces.xarch.core.persistence.jpa.StringIDDao)
     */
    @Autowired
    @Qualifier("releaseDetailDao")
    @Override
    protected void setDaoUnBinding(ReleaseDetailDao dao) {
        super.setDaoUnBinding(dao);
    }

    /**
     * 获取发布系统或更新包的根节点
     * 
     * @param systemReleaseId 系统发布ID
     * @param dataId 数据ID（菜单ID）
     */
    public ReleaseDetail getReleaseDetailRootNode(String systemReleaseId, String dataId) {
        return getDao().getReleaseDetailRootNode(systemReleaseId, dataId);
    }
}
