package com.ces.config.dhtmlx.service.systemupdate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.ces.config.dhtmlx.dao.systemupdate.SystemUpdateDao;
import com.ces.config.dhtmlx.entity.systemupdate.SystemUpdate;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;

/**
 * 系统更新Service
 * 
 * @author wanglei
 * @date 2015-02-12
 */
@Component("systemUpdateService")
public class SystemUpdateService extends ConfigDefineDaoService<SystemUpdate, SystemUpdateDao> {

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.service.StringIDService#setDaoUnBinding(com.ces.xarch.core.persistence.jpa.StringIDDao)
     */
    @Autowired
    @Qualifier("systemUpdateDao")
    @Override
    protected void setDaoUnBinding(SystemUpdateDao dao) {
        super.setDaoUnBinding(dao);
    }
}
