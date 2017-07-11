package com.ces.config.dhtmlx.dao.component;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.entity.component.ComponentJar;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

/**
 * 构件中的jar文件Dao
 * 
 * @author wanglei
 * @date 2013-08-09
 */
public interface ComponentJarDao extends StringIDDao<ComponentJar> {

    /**
     * 根据构件版本ID获取该构件版本下的jar包
     * 
     * @param componentVersionId 构件版本ID
     * @return List<ComponentJar>
     */
    public List<ComponentJar> getByComponentVersionId(String componentVersionId);

    /**
     * 根据构件版本ID删除该构件版本下的jar包
     * 
     * @param componentVersionId 构件版本ID
     */
    @Transactional
    @Modifying
    @Query("delete from ComponentJar where componentVersionId=?")
    public void deleteByComponentVersionId(String componentVersionId);
}
