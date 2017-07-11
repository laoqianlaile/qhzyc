package com.ces.config.dhtmlx.dao.appmanage;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.entity.appmanage.AppSearchPanel;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

public interface AppSearchPanelDao extends StringIDDao<AppSearchPanel>{

    /**
     * qiucs 2014-12-10 
     * <p>描述: 获取检索配置</p>
     */
    @Query("from AppSearchPanel where tableId=?1 and componentVersionId=?2 and menuId=?3 and userId=?4")
    public AppSearchPanel findByFk(String tableId, String componentVersionId, String menuId, String userId);

    /**
     * qiucs 2014-12-10 
     * <p>描述: 根据表ID、模块ID删除检索字段配置</p>
     */
    @Transactional
    @Modifying
    @Query("delete AppSearchPanel where tableId=?1 and componentVersionId=?2 and menuId is null and userId=?3")
    public void deleteByFk(String tableId, String componentVersionId, String userId);

    /**
     * qiucs 2014-12-10 
     * <p>描述: 删除检索配置</p>
     */
    @Transactional
    @Modifying
    @Query("delete AppSearchPanel where tableId=?1 and componentVersionId=?2 and menuId=?3 and userId=?4")
    public void deleteByFk(String tableId, String componentVersionId, String menuId, String userId);

    /**
     * qiucs 2013-12-26 
     * <p>描述: 根据模块ID删除配置</p>
     * @param  tableId    设定参数   
     */
    @Transactional
    @Modifying
    @Query("delete AppSearchPanel where tableId=?1")
    public void deleteByTableId(String tableId);

    /**
     * qiucs 2013-12-26 
     * <p>描述: 根据表ID删除配置</p>
     * @param  tableId    设定参数   
     */
    @Transactional
    @Modifying
    @Query("delete AppSearchPanel where componentVersionId=?1")
    public void deleteByComponentVersionId(String componentVersionId);

    /**
     * qiucs 2014-12-10 
     * <p>描述: 根据菜单ID删除配置</p>
     * @param  tableId    设定参数   
     */
    @Transactional
    @Modifying
    @Query("delete AppSearchPanel where menuId=?1")
    public void deleteByMenuId(String menuId);

}
