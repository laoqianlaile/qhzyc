package com.ces.config.dhtmlx.service.release;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.ces.config.dhtmlx.dao.release.ReleaseButtonDao;
import com.ces.config.dhtmlx.entity.release.ReleaseButton;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;

/**
 * 系统发布菜单中组装的按钮Service
 * 
 * @author wanglei
 * @date 2014-10-13
 */
@Component("releaseButtonService")
public class ReleaseButtonService extends ConfigDefineDaoService<ReleaseButton, ReleaseButtonDao> {

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.service.StringIDService#setDaoUnBinding(com.ces.xarch.core.persistence.jpa.StringIDDao)
     */
    @Autowired
    @Qualifier("releaseButtonDao")
    @Override
    protected void setDaoUnBinding(ReleaseButtonDao dao) {
        super.setDaoUnBinding(dao);
    }
}
