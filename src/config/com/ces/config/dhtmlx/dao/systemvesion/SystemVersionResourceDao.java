package com.ces.config.dhtmlx.dao.systemvesion;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.entity.systemversion.SystemVersionResource;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

/**
 * 系统版本与资源关联关系Dao
 * 
 * @author wanglei
 * @date 2015-04-15
 */
public interface SystemVersionResourceDao extends StringIDDao<SystemVersionResource> {

    /**
     * 获取系统版本与资源关联关系
     * 
     * @param systemVersionId 系统版本ID
     * @return List<SystemVersionResource>
     */
    public List<SystemVersionResource> getBySystemVersionId(String systemVersionId);

    /**
     * 获取系统版本与资源关联关系IDs
     * 
     * @param systemVersionId 系统版本ID
     * @return List<ResourceButton>
     */
    @Query(value = "select resource_id from t_xtpz_sys_version_resource where system_version_id=?", nativeQuery = true)
    public List<String> getResourceIdsBySystemVersionId(String systemVersionId);

    /**
     * 根据系统版本ID删除（系统版本与资源关联关系）
     * 
     * @param systemVersionId 系统版本ID
     */
    @Transactional
    @Modifying
    @Query("delete from SystemVersionResource where systemVersionId=?")
    public void deleteBySystemVersionId(String systemVersionId);

    /**
     * 根据资源ID删除（系统版本与资源关联关系）
     * 
     * @param resourceId 资源ID
     */
    @Transactional
    @Modifying
    @Query("delete from SystemVersionResource where resourceId=?")
    public void deleteByResourceId(String resourceId);
}
