package com.ces.config.dhtmlx.dao.component;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.entity.component.ComponentReserveZone;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

/**
 * 构件预留区Dao
 * 
 * @author wanglei
 * @date 2013-08-08
 */
public interface ComponentReserveZoneDao extends StringIDDao<ComponentReserveZone> {

    /**
     * 根据构件版本ID获取构件预留区
     * 
     * @param componentVersionId 构件版本ID
     * @return List<ComponentReserveZone>
     */
    @Query("from ComponentReserveZone where componentVersionId=? order by showOrder")
    public List<ComponentReserveZone> getByComponentVersionId(String componentVersionId);

    /**
     * 根据构件版本ID和page获取构件预留区
     * 
     * @param componentVersionId 构件版本ID
     * @param page 页面
     * @return List<ComponentReserveZone>
     */
    @Query("from ComponentReserveZone where componentVersionId=? and page=? order by showOrder")
    public List<ComponentReserveZone> getByComponentVersionIdAndPage(String componentVersionId, String page);

    /**
     * 获取某个组合构件中使用的构件预留区
     * 
     * @param constructId 组合构件绑定关系ID
     * @param componentVersionId 构件版本ID
     * @param page 页面
     * @return List<ComponentReserveZone>
     */
    @Query("select distinct crz from ComponentReserveZone crz, ConstructDetail cd where crz.id=cd.reserveZoneId"
            + " and cd.constructId=?1 and crz.componentVersionId=?2 and crz.page=?3 order by crz.showOrder")
    public List<ComponentReserveZone> getUsedReserveZone(String constructId, String componentVersionId, String page);

    /**
     * 获取某个组合构件中使用的公用预留区
     * 
     * @param constructId 组合构件绑定关系ID
     * @param page 页面
     * @return List<ComponentReserveZone>
     */
    @Query("select distinct crz from ComponentReserveZone crz, ConstructDetail cd where crz.id=cd.reserveZoneId"
            + " and cd.constructId=?1 and crz.componentVersionId is null and crz.page=?2 order by crz.showOrder")
    public List<ComponentReserveZone> getUsedCommonReserveZone(String constructId, String page);

    /**
     * 根据构件版本ID和名称获取构件预留区
     * 
     * @param componentVersionId 构件版本ID
     * @param name 名称
     * @param page 页面
     * @return List<ComponentReserveZone>
     */
    public ComponentReserveZone getByComponentVersionIdAndNameAndPage(String componentVersionId, String name, String page);

    /**
     * 获取所有公共预留区
     * 
     * @return List<ComponentReserveZone>
     */
    @Query("from ComponentReserveZone where isCommon='1' order by showOrder, name")
    public List<ComponentReserveZone> getAllCommonReserveZone();

    /**
     * 根据预留区名称获取公共预留区
     * 
     * @param name 预留区名称
     * @return ComponentReserveZone
     */
    @Query("from ComponentReserveZone where name=? and isCommon='1'")
    public ComponentReserveZone getCommonReserveZoneByName(String name);

    /**
     * 根据预留区类型获取公共预留区
     * 
     * @param type 预留区类型
     * @return List<ComponentReserveZone>
     */
    @Query("from ComponentReserveZone where type=? and isCommon='1' order by showOrder")
    public List<ComponentReserveZone> getCommonReserveZoneByType(String type);

    /**
     * 获取构件版本ID下预留区最大显示顺序
     * 
     * @param componentVersionId 构件版本ID
     */
    @Query("select max(showOrder) from ComponentReserveZone where componentVersionId=?")
    public Integer getMaxShowOrder(String componentVersionId);

    /**
     * 获取公用预留区最大显示顺序
     */
    @Query("select max(showOrder) from ComponentReserveZone where isCommon='1'")
    public Integer getCommonMaxShowOrder();

    /**
     * 根据构件版本ID删除该构件版本下的预留区
     * 
     * @param componentVersionId 构件版本ID
     */
    @Transactional
    @Modifying
    @Query("delete from ComponentReserveZone where componentVersionId=?")
    public void deleteByComponentVersionId(String componentVersionId);

    /**
     * 根据预留区名称删除公共预留区
     * 
     * @param name 预留区名称
     */
    @Transactional
    @Modifying
    @Query("delete from ComponentReserveZone where name=? and isCommon='1'")
    public void deleteCommonReserveZoneByName(String name);
    
    /**
     * 根据预留区名称删除预留区
     * 
     * @param name 预留区名称
     */
    @Transactional
    @Modifying
    @Query("delete from ComponentReserveZone where name=?")
    public void deleteReserveZoneByName(String name);
}
