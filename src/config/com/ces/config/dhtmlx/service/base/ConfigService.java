package com.ces.config.dhtmlx.service.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ces.xarch.core.entity.StringIDEntity;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

@Component("configService")
@Scope("prototype")
@Transactional(readOnly = true)
public class ConfigService<T extends StringIDEntity> extends ConfigDefineDaoService<T, StringIDDao<T>>{

    /*
     * (非 Javadoc)   
     * <p>标题: setDaoUnBinding</p>   
     * <p>描述: 注入StringIDDao</p>   
     * @param dao   
     * @see com.ces.xarch.core.service.AbstractService#setDaoUnBinding(com.ces.xarch.core.persistence.jpa.BaseDao)
     */
    @Override
    @Autowired
    @Qualifier("stringIDDao")
    protected void setDaoUnBinding(StringIDDao<T> dao) {
        super.setDaoUnBinding(dao);
    }
}
