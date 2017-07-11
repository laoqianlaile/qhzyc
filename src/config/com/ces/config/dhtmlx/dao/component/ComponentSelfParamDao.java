package com.ces.config.dhtmlx.dao.component;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.entity.component.ComponentSelfParam;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

/**
 * 构件自身配置参数Dao
 * 
 * @author wanglei
 * @date 2013-08-15
 */
public interface ComponentSelfParamDao extends StringIDDao<ComponentSelfParam> {

    /**
     * 根据名称和构件版本ID获取构件自身配置参数
     * 
     * @param name 名称
     * @param componentVersionId 构件版本ID
     * @return ComponentSelfParam
     */
    public ComponentSelfParam getByNameAndComponentVersionId(String name, String componentVersionId);

    /**
     * 根据构件版本ID获取构件自身配置参数
     * 
     * @param componentVersionId 构件版本ID
     * @return List<ComponentSelfParam>
     */
    public List<ComponentSelfParam> getByComponentVersionId(String componentVersionId);

    /**
     * 根据构件版本ID删除构件自身配置参数
     * 
     * @param componentVersionId 构件版本ID
     */
    @Transactional
    @Modifying
    @Query("delete from ComponentSelfParam where componentVersionId=?")
    public void deleteByComponentVersionId(String componentVersionId);
}
