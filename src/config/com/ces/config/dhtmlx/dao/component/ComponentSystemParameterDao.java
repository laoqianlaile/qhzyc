package com.ces.config.dhtmlx.dao.component;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.entity.component.ComponentSystemParameter;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

/**
 * 构件版本中系统参数Dao
 * 
 * @author wanglei
 * @date 2013-08-20
 */
public interface ComponentSystemParameterDao extends StringIDDao<ComponentSystemParameter> {

    /**
     * 根据构件版本ID获取该构件版本下的系统参数
     * 
     * @param componentVersionId 构件版本ID
     */
    public List<ComponentSystemParameter> getByComponentVersionId(String componentVersionId);

    /**
     * 根据构件版本ID删除该构件版本下的系统参数
     * 
     * @param componentVersionId 构件版本ID
     */
    @Transactional
    @Modifying
    @Query("delete from ComponentSystemParameter where componentVersionId=?")
    public void deleteByComponentVersionId(String componentVersionId);
}
