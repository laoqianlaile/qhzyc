package com.ces.config.dhtmlx.dao.component;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.entity.component.ComponentButton;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

/**
 * 构件按钮Dao
 * 
 * @author wanglei
 * @date 2013-07-30
 */
public interface ComponentButtonDao extends StringIDDao<ComponentButton> {

    /**
     * 根据名称和构件版本ID获取构件按钮
     * 
     * @param name 名称
     * @param componentVersionId 构件版本ID
     * @return ComponentButton
     */
    public ComponentButton getByNameAndComponentVersionId(String name, String componentVersionId);

    /**
     * 根据构件版本ID获取构件按钮
     * 
     * @param componentVersionId 构件版本ID
     * @return List<ComponentParameter>
     */
    public List<ComponentButton> getByComponentVersionId(String componentVersionId);

    /**
     * 根据构件版本ID删除构件按钮
     * 
     * @param componentVersionId 构件版本ID
     */
    @Transactional
    @Modifying
    @Query("delete from ComponentButton where componentVersionId=?")
    public void deleteByComponentVersionId(String componentVersionId);
}
