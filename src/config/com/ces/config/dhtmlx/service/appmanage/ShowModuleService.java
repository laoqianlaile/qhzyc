package com.ces.config.dhtmlx.service.appmanage;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.ces.config.dhtmlx.dao.appmanage.ShowModuleDao;
import com.ces.config.service.base.ShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;

@Component
public class ShowModuleService extends ShowModuleDefineDaoService<StringIDEntity, ShowModuleDao>{
    
    /*
     * (非 Javadoc)   
     * <p>标题: bindingDao</p>   
     * <p>描述: 注入自定义持久层(Dao)</p>   
     * @param entityClass   
     * @see com.ces.xarch.core.service.AbstractService#bindingDao(java.lang.Class)
     */
    @Autowired
    @Qualifier("showModuleDao")
    @Override
    protected void setDaoUnBinding(ShowModuleDao dao) {
        super.setDaoUnBinding(dao);
    }
    
}
