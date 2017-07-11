package com.ces.config.dhtmlx.dao.appmanage;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.entity.appmanage.AppGrid;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

public interface AppGridDao extends StringIDDao<AppGrid> {
    
    /**
     * qiucs 2014-9-23 
     * <p>描述: 查找</p>
     * @return AppGrid    返回类型   
     * @throws
     */
    @Query("from AppGrid t where t.tableId=?1 and t.componentVersionId=?2 and t.menuId=?3 and t.userId=?4")
    public AppGrid findByFk(String tableId, String componentVersionId, String menuId, String userId);
    
    
    /**
     * qiucs 2014-9-23 
     * <p>描述: 删除</p>
     * @throws
     */
    @Transactional
    @Modifying
    @Query("delete AppGrid t where t.tableId=?1 and t.componentVersionId=?2 and t.menuId=?3 and t.userId=?4")
    public void deleteByFk(String tableId, String componentVersionId, String menuId, String userId);

    /**
     * qiucs 2014-9-23 
     * <p>描述: 删除</p>
     * @throws
     */
    @Transactional
    @Modifying
    @Query("delete AppGrid t where t.tableId=?1")
    public void deleteByTableId(String tableId);

    /**
     * qiucs 2014-9-23 
     * <p>描述: 删除</p>
     * @throws
     */
    @Transactional
    @Modifying
    @Query("delete AppGrid t where t.componentVersionId=?1")
    public void deleteByComponentVersionId(String componentVersionId);

    /**
     * qiucs 2014-12-11 
     * <p>描述: 删除</p>
     * @throws
     */
    @Transactional
    @Modifying
    @Query("delete AppGrid t where t.menuId=?1")
    public void deleteByMenuId(String menuId);
}