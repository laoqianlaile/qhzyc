package com.ces.config.dhtmlx.dao.resource;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.ces.config.dhtmlx.entity.resource.Resource;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

/**
 * 资源Dao
 * 
 * @author wanglei
 * @date 2015-04-15
 */
public interface ResourceDao extends StringIDDao<Resource> {

    /**
     * 获取资源
     * 
     * @param targetId 对应菜单ID或其他资源ID
     * @return Resource
     */
    public Resource getByTargetId(String targetId);

    /**
     * 获取资源
     * 
     * @param name 资源名称
     * @param parentId 父资源ID
     * @return Resource
     */
    public Resource getByNameAndParentId(String name, String parentId);

    /**
     * 获取可用或不可用的资源
     * 
     * @param canUse 是否可用 0：不可用 1：可用
     * @return List<Resource>
     */
    public List<Resource> getByCanUse(String canUse);

    /**
     * 获取子资源
     * 
     * @param parentId 父资源ID
     * @return List<Resource>
     */
    @Query("from Resource where parentId=? order by showOrder")
    public List<Resource> getByParentId(String parentId);

    /**
     * 获取系统下的资源
     * 
     * @param systemId 系统ID
     * @return List<Resource>
     */
    @Query("from Resource where systemId=? order by showOrder")
    public List<Resource> getBySystemId(String systemId);

    /**
     * 获取系统下的资源（不包含系统菜单资源）（可用）
     * 
     * @param systemId 系统ID
     * @return List<Resource>
     */
    @Query("from Resource where systemId=?1 and (targetId!=?1 or targetId is null) and canUse='1' order by showOrder")
    public List<Resource> getResListNotWithSysRes(String systemId);

    /**
     * 获取所有（可用）的菜单资源
     * 
     * @return List<String>
     */
    @Query("select targetId from Resource where type='0' and canUse='1'")
    public List<String> getAllCanUseMenuId();

    /**
     * 获取资源下子资源最大显示顺序
     * 
     * @param parentId 父资源ID
     */
    @Query("select max(showOrder) from Resource where parentId=?")
    public Integer getMaxShowOrder(String parentId);

    /**
     * 获取资源树（可用）
     * 
     * @param parentId 父资源ID
     * @return List<Object[]>
     */
    @Query(value = "select r.id, r.name, r.system_id, r.parent_id, m.binding_type from t_xtpz_resource r, t_xtpz_menu m where r.target_id=m.id and r.type='0' and r.can_use='1' and r.parent_id=? order by r.show_order", nativeQuery = true)
    public List<Object[]> getResourceTree(String parentId);

    /**
     * 获取显示顺序范围内的资源
     * 
     * @param start 开始显示顺序
     * @param end 结束显示顺序
     * @param parentId 父资源ID
     * @return List<Resource>
     */
    public List<Resource> getByShowOrderBetweenAndParentId(Integer start, Integer end, String parentId);

    /**
     * 根据资源类型获取资源
     * 
     * @param type 资源类型
     * @return List<Resource>
     */
    public List<Resource> getByType(String type);
}
