package com.ces.config.dhtmlx.dao.database;

import com.ces.config.dhtmlx.entity.database.Database;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

/**
 * 数据库Dao
 * 
 * @author wanglei
 * @date 2013-07-10
 */
public interface DatabaseDao extends StringIDDao<Database> {

    /**
     * 根据数据库名称获取数据库
     * 
     * @param name 数据库名称
     * @return Database
     */
    public Database getByName(String name);
}
