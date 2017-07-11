package com.ces.config.dhtmlx.dao.component;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.entity.component.ComponentInputParam;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

/**
 * 构件入参Dao
 * 
 * @author wanglei
 * @date 2013-07-29
 */
public interface ComponentInputParamDao extends StringIDDao<ComponentInputParam> {

    /**
     * 根据名称和构件版本ID获取构件入参
     * 
     * @param name 名称
     * @param componentVersionId 构件版本ID
     * @return ComponentInputParam
     */
    public ComponentInputParam getByNameAndComponentVersionId(String name, String componentVersionId);

    /**
     * 根据构件版本ID获取构件入参
     * 
     * @param componentVersionId 构件版本ID
     * @return List<ComponentParameter>
     */
    public List<ComponentInputParam> getByComponentVersionId(String componentVersionId);

    /**
     * 获取构件公用入参
     * 
     * @return List<ComponentParameter>
     */
    @Query("from ComponentInputParam where componentVersionId is null")
    public List<ComponentInputParam> getCommonInputParams();

    /**
     * 根据构件版本ID删除构件入参
     * 
     * @param componentVersionId 构件版本ID
     */
    @Transactional
    @Modifying
    @Query("delete from ComponentInputParam where componentVersionId=?")
    public void deleteByComponentVersionId(String componentVersionId);
}
