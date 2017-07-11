package com.ces.config.dhtmlx.dao.component;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.entity.component.ComponentCallbackParam;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

/**
 * 构件回调函数(供页面构件关闭时使用)参数Dao
 * 
 * @author wanglei
 * @date 2013-09-27
 */
public interface ComponentCallbackParamDao extends StringIDDao<ComponentCallbackParam> {

    /**
     * 根据构件回调函数ID获取该回调函数的参数
     * 
     * @param name 参数名称
     * @param callbackId 回调函数ID
     * @return ComponentCallbackParam
     */
    public ComponentCallbackParam getByNameAndCallbackId(String name, String callbackId);

    /**
     * 根据构件回调函数ID获取该回调函数的参数
     * 
     * @param callbackId 回调函数ID
     * @return List<ComponentCallbackParam>
     */
    public List<ComponentCallbackParam> getByCallbackId(String callbackId);

    /**
     * 根据回调函数ID删除该回调函数下的参数
     * 
     * @param callbackId 回调函数ID
     */
    @Transactional
    @Modifying
    @Query("delete from ComponentCallbackParam where callbackId=?")
    public void deleteByCallbackId(String callbackId);

    /**
     * 根据构件版本ID删除该构件版本下的回调函数的参数
     * 
     * @param componentVersionId 构件版本ID
     */
    @Transactional
    @Modifying
    @Query(value = "delete from t_xtpz_comp_callback_param where callback_id in (select id from t_xtpz_component_callback where component_version_id=?)", nativeQuery = true)
    public void deleteByComponentVersionId(String componentVersionId);
}
