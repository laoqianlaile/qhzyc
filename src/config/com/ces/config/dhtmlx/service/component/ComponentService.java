package com.ces.config.dhtmlx.service.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.config.dhtmlx.dao.component.ComponentDao;
import com.ces.config.dhtmlx.entity.component.Component;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;

/**
 * 构件Service
 * 
 * @author wanglei
 * @date 2013-07-22
 */
@org.springframework.stereotype.Component("componentService")
public class ComponentService extends ConfigDefineDaoService<Component, ComponentDao> {

    /*
     * (non-Javadoc)
     * 
     * @see com.ces.xarch.core.service.StringIDService#setDaoUnBinding(com.ces.xarch.core.persistence.jpa.StringIDDao)
     */
    @Autowired
    @Qualifier("componentDao")
    @Override
    protected void setDaoUnBinding(ComponentDao dao) {
        super.setDaoUnBinding(dao);
    }

    /**
     * 根据编码获取构件
     * 
     * @param code 构件编码
     * @return Component
     */
    public Component getComponentByCode(String code) {
        return getDao().getByCode(code);
    }

    /**
     * 根据名称获取构件
     * 
     * @param name 构件名称
     * @return Component
     */
    public Component getComponentByName(String name) {
        return getDao().getByName(name);
    }
}
