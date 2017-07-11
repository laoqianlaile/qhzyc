package com.ces.config.dhtmlx.dao.component;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.entity.component.ComponentCallback;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

/**
 * 构件回调函数(供页面构件关闭时使用)Dao
 * 
 * @author wanglei
 * @date 2013-09-10
 */
public interface ComponentCallbackDao extends StringIDDao<ComponentCallback> {

    /**
     * 根据回调函数名称和构件版本ID获取回调函数
     * 
     * @param name 回调函数名称
     * @param componentVersionId 构件版本ID
     * @return ComponentCallback
     */
    public ComponentCallback getByNameAndComponentVersionId(String name, String componentVersionId);

    /**
     * 根据构件版本ID获取该构件版本下的页面回调函数
     * 
     * @param componentVersionId 构件版本ID
     * @return List<ComponentCallback>
     */
    public List<ComponentCallback> getByComponentVersionId(String componentVersionId);

    /**
     * 获取自定义构件公用的页面回调函数
     * 
     * @return List<ComponentCallback>
     */
    @Query("from ComponentCallback where componentVersionId is null")
    public List<ComponentCallback> getCommonCallbacks();

    /**
     * 根据构件版本ID删除该构件版本下的页面回调函数
     * 
     * @param componentVersionId 构件版本ID
     */
    @Transactional
    @Modifying
    @Query("delete from ComponentCallback where componentVersionId=?")
    public void deleteByComponentVersionId(String componentVersionId);
}
