package com.ces.config.dhtmlx.dao.completecomponent;

import com.ces.config.dhtmlx.entity.completecomponent.CompleteComponent;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

/**
 * 成品构件Dao
 * 
 * @author wanglei
 * @date 2014-02-17
 */
public interface CompleteComponentDao extends StringIDDao<CompleteComponent> {

    /**
     * 根据编码获取成品构件
     * 
     * @param code 成品构件编码
     * @return CompleteComponent
     */
    public CompleteComponent getByCode(String code);

    /**
     * 根据名称获取成品构件
     * 
     * @param name 成品构件名称
     * @return CompleteComponent
     */
    public CompleteComponent getByName(String name);
}
