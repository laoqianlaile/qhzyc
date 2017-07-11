package com.ces.config.dhtmlx.service.completecomponent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.ces.config.dhtmlx.dao.completecomponent.CompleteComponentDao;
import com.ces.config.dhtmlx.entity.completecomponent.CompleteComponent;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;

/**
 * 成品构件Service
 * 
 * @author wanglei
 * @date 2013-07-22
 */
@Component("completeComponentService")
public class CompleteComponentService extends ConfigDefineDaoService<CompleteComponent, CompleteComponentDao> {

    /*
     * (non-Javadoc)
     * 
     * @see com.ces.xarch.core.service.StringIDService#setDaoUnBinding(com.ces.xarch.core.persistence.jpa.StringIDDao)
     */
    @Autowired
    @Qualifier("completeComponentDao")
    @Override
    protected void setDaoUnBinding(CompleteComponentDao dao) {
        super.setDaoUnBinding(dao);
    }

    /**
     * 根据编码获取成品构件
     * 
     * @param code 成品构件编码
     * @return CompleteComponent
     */
    public CompleteComponent getCompleteComponentByCode(String code) {
        return getDao().getByCode(code);
    }

    /**
     * 根据名称获取成品构件
     * 
     * @param name 成品构件名称
     * @return CompleteComponent
     */
    public CompleteComponent getCompleteComponentByName(String name) {
        return getDao().getByName(name);
    }
}
