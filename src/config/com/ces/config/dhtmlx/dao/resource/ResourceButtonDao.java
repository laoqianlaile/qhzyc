package com.ces.config.dhtmlx.dao.resource;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.entity.resource.ResourceButton;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

/**
 * 按钮资源与实际按钮关系Dao
 * 
 * @author wanglei
 * @date 2015-04-15
 */
public interface ResourceButtonDao extends StringIDDao<ResourceButton> {

    /**
     * 获取按钮资源与实际按钮关系
     * 
     * @param resourceId 资源ID
     * @return List<ResourceButton>
     */
    public List<ResourceButton> getByResourceId(String resourceId);

    /**
     * 获取按钮资源与实际按钮关系
     * 
     * @param buttonId 按钮ID
     * @return List<ResourceButton>
     */
    public List<ResourceButton> getByButtonId(String buttonId);

    /**
     * 获取按钮资源与实际按钮关系
     * 
     * @param systemId 系统ID
     * @return List<ResourceButton>
     */
    @Query(value = "select rb.* from t_xtpz_resource_button rb, t_xtpz_resource r where rb.resource_id=r.id and r.system_id=?", nativeQuery = true)
    public List<ResourceButton> getBySystemId(String systemId);

    /**
     * 获取按钮资源与实际按钮关系IDs
     * 
     * @param resourceId 资源ID
     * @return List<ResourceButton>
     */
    @Query(value = "select button_id from t_xtpz_resource_button where resource_id=?", nativeQuery = true)
    public List<String> getButtonIdsByResourceId(String resourceId);

    /**
     * 获取菜单资源下所有（按钮资源与实际按钮关系）IDs
     * 
     * @param menuResourceId 菜单资源ID
     * @return List<ResourceButton>
     */
    @Query(value = "select rb.button_id from t_xtpz_resource_button rb where rb.resource_id in (select r.id from t_xtpz_resource r where r.parent_id=?)", nativeQuery = true)
    public List<String> getButtonIdsByMenuResourceId(String menuResourceId);

    /**
     * 根据按钮资源ID删除（按钮资源与实际按钮关系）
     * 
     * @param resourceId 按钮资源ID
     */
    @Transactional
    @Modifying
    @Query("delete from ResourceButton where resourceId=?")
    public void deleteByResourceId(String resourceId);

    /**
     * 根据按钮ID删除（按钮资源与实际按钮关系）
     * 
     * @param buttonId 按钮ID
     */
    @Transactional
    @Modifying
    @Query("delete from ResourceButton where buttonId=?")
    public void deleteByButtonId(String buttonId);

}
