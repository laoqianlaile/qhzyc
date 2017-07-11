package com.ces.config.dhtmlx.dao.component;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.entity.component.ComponentClass;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

/**
 * 构件中的class文件Dao
 * 
 * @author wanglei
 * @date 2013-08-09
 */
public interface ComponentClassDao extends StringIDDao<ComponentClass> {

    /**
     * 根据构件版本ID获取构件类
     * 
     * @param componentVersionId 构件版本ID
     * @return List<ComponentClass>
     */
    public List<ComponentClass> getByComponentVersionId(String componentVersionId);

    /**
     * 根据构件版本ID删除该构件版本下的类
     * 
     * @param componentVersionId 构件版本ID
     */
    @Transactional
    @Modifying
    @Query("delete from ComponentClass where componentVersionId=?")
    public void deleteByComponentVersionId(String componentVersionId);
}
