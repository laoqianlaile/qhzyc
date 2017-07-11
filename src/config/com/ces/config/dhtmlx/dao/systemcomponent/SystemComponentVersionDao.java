package com.ces.config.dhtmlx.dao.systemcomponent;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.entity.component.ComponentVersion;
import com.ces.config.dhtmlx.entity.systemcomponent.SystemComponentVersion;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

/**
 * 系统中直接绑定的构件（非通过菜单方式）Dao
 * 
 * @author wanglei
 * @date 2014-04-24
 */
public interface SystemComponentVersionDao extends StringIDDao<SystemComponentVersion> {

    /**
     * 获取系统与构件绑定关系
     * 
     * @param rootMenuId 根菜单ID
     * @return List<SystemComponentVersion>
     */
    @Query("from SystemComponentVersion where rootMenuId=?")
    public List<SystemComponentVersion> getByRootMenuId(String rootMenuId);

    /**
     * 获取系统中直接绑定的构件
     * 
     * @param rootMenuId 根菜单ID
     * @return List<ComponentVersion>
     */
    @Query("select t.componentVersion from SystemComponentVersion t where t.rootMenuId=?")
    public List<ComponentVersion> getComponentVersions(String rootMenuId);

    /**
     * 根据构件版本ID删除系统中直接绑定的构件
     * 
     * @param componentVersionId 构件版本ID
     */
    @Transactional
    @Modifying
    @Query(value = "delete from t_xtpz_system_component t where t.component_version_id=?", nativeQuery = true)
    public void deleteByComponentVersionId(String componentVersionId);
}
