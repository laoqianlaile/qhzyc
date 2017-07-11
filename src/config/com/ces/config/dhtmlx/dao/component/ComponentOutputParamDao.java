package com.ces.config.dhtmlx.dao.component;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.entity.component.ComponentOutputParam;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

/**
 * 构件出参Dao
 * 
 * @author wanglei
 * @date 2013-09-27
 */
public interface ComponentOutputParamDao extends StringIDDao<ComponentOutputParam> {

    /**
     * 根据构件出参名称和构件版本ID获取构件出参
     * 
     * @param name 构件出参名称
     * @param componentVersionId 构件版本ID
     * @return ComponentParameter
     */
    public ComponentOutputParam getByNameAndComponentVersionId(String name, String componentVersionId);

    /**
     * 根据构件版本ID获取构件出参
     * 
     * @param componentVersionId 构件版本ID
     * @return List<ComponentParameter>
     */
    public List<ComponentOutputParam> getByComponentVersionId(String componentVersionId);

    /**
     * 根据构件版本ID删除构件出参
     * 
     * @param componentVersionId 构件版本ID
     */
    @Transactional
    @Modifying
    @Query("delete from ComponentOutputParam where componentVersionId=?")
    public void deleteByComponentVersionId(String componentVersionId);
}
