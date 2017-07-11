package com.ces.config.dhtmlx.dao.component;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.entity.component.Component;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

/**
 * 构件Dao
 * 
 * @author wanglei
 * @date 2013-07-22
 */
public interface ComponentDao extends StringIDDao<Component> {

    /**
     * 根据编码获取构件
     * 
     * @param code 构件编码
     * @return Component
     */
    public Component getByCode(String code);

    /**
     * 根据名称获取构件
     * 
     * @param name 构件名称
     * @return Component
     */
    public Component getByName(String name);

    @Transactional
    @Modifying
    @Query("delete Component where id=?1")
    public void deleteById(String id);
}
