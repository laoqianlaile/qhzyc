package com.ces.config.dhtmlx.service.release;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.ces.config.dhtmlx.dao.release.ReleaseMenuDao;
import com.ces.config.dhtmlx.entity.release.ReleaseMenu;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;

/**
 * 系统发布菜单Service
 * 
 * @author wanglei
 * @date 2014-10-13
 */
@Component("releaseMenuService")
public class ReleaseMenuService extends ConfigDefineDaoService<ReleaseMenu, ReleaseMenuDao> {

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.service.StringIDService#setDaoUnBinding(com.ces.xarch.core.persistence.jpa.StringIDDao)
     */
    @Autowired
    @Qualifier("releaseMenuDao")
    @Override
    protected void setDaoUnBinding(ReleaseMenuDao dao) {
        super.setDaoUnBinding(dao);
    }
}
