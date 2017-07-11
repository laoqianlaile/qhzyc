package com.ces.config.dhtmlx.dao.appmanage;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.entity.appmanage.AppForm;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

public interface AppFormDao extends StringIDDao<AppForm> {
    
    /**
     * <p>标题: deleteByFk</p>
     * <p>描述: 根据表ID、模块ID删除界面配置</p>
     * @param  tableId
     * @param  componentVersionId
     * @throws
     */
    @Modifying
    @Transactional
    @Query("delete from AppForm t where t.tableId=?1 and t.componentVersionId=?2 and t.menuId=?3")
    public void deleteByFk(String tableId, String componentVersionId, String menuId);
    
    /**
     * <p>标题: findByFk</p>
     * <p>描述: 根据表ID、模块ID检索</p>
     * @param  tableId
     * @param  componentVersionId
     * @return AppForm    返回类型   
     * @throws
     */
    @Query("from AppForm t where t.tableId=?1 and t.componentVersionId=?2 and t.menuId=?3")
    public AppForm findByFk(String tableId, String componentVersionId, String menuId);

    /**
     * qiucs 2013-11-27 
     * <p>描述: 根据模块ID删除配置</p>
     * @param  tableId    设定参数   
     */
    @Transactional
    @Modifying
    @Query("delete AppForm t where t.tableId=?1")
    public void deleteByTableId(String tableId);

    /**
     * qiucs 2013-11-27 
     * <p>描述: 根据表ID删除配置</p>
     * @param  tableId    设定参数   
     */
    @Transactional
    @Modifying
    @Query("delete AppForm t where t.componentVersionId=?1")
    public void deleteByComponentVersionId(String componentVersionId);

    /**
     * qiucs 2013-11-27 
     * <p>描述: 根据表ID删除配置</p>
     * @param  tableId    设定参数   
     */
    @Transactional
    @Modifying
    @Query("delete AppForm t where t.menuId=?1")
    public void deleteByMenuId(String menuId);

}
