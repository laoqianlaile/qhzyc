package com.ces.config.dhtmlx.service.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.ces.config.dhtmlx.dao.database.DatabaseDao;
import com.ces.config.dhtmlx.entity.database.Database;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;

/**
 * 数据库Service
 * 
 * @author wanglei
 * @date 2013-07-10
 */
@Component("databaseService")
public class DatabaseService extends ConfigDefineDaoService<Database, DatabaseDao> {

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.service.StringIDService#setDaoUnBinding(com.ces.xarch.core.persistence.jpa.StringIDDao)
     */
    @Autowired
    @Qualifier("databaseDao")
    @Override
    protected void setDaoUnBinding(DatabaseDao dao) {
        super.setDaoUnBinding(dao);
    }

    /**
     * 根据数据库名称获取数据库
     * 
     * @param name 数据库名称
     * @return Database
     */
    public Database getDatabaseByName(String name) {
        return getDao().getByName(name);
    }
}
