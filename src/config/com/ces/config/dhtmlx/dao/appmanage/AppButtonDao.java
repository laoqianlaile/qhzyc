package com.ces.config.dhtmlx.dao.appmanage;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.entity.appmanage.AppButton;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

public interface AppButtonDao extends StringIDDao<AppButton> {
    
    /**
     * qiucs 2013-9-12 
     * <p>描述: 工具条展现</p>
     */
    @Query("from AppButton where tableId=?1 and componentVersionId=?2 and menuId=?3 and buttonType=?4 order by showOrder")
    public List<AppButton> findByFk(String tableId, String componentVersionId, String menuId, String buttonType);
    
    /**
     * qiucs 2013-10-16 
     * <p>描述: 删除配置</p>
     */
    @Transactional
    @Modifying
    @Query("DELETE FROM AppButton WHERE tableId=?1 AND componentVersionId=?2 AND menuId=?3 AND buttonType=?4")
    public void deleteByFk(String tableId, String componentVersionId, String menuId, String buttonType);
    
    /**
     * qiucs 2013-11-27 
     * <p>描述: 根据表ID删除配置</p>
     */
    @Transactional
    @Modifying
    @Query("DELETE FROM AppButton WHERE tableId=?1")
    public void deleteByTableId(String tableId);
    
    /**
     * qiucs 2013-11-27 
     * <p>描述: 根据模块ID删除配置</p>
     */
    @Transactional
    @Modifying
    @Query("DELETE FROM AppButton WHERE componentVersionId=?1")
    public void deleteByComponentVersionId(String componentVersionId);

    /**
     * qiucs 2013-11-27 
     * <p>描述: 根据表ID和模块ID删除配置</p>
     */
    @Transactional
    @Modifying
    @Query("DELETE FROM AppButton WHERE tableId=?1 and componentVersionId=?2")
    public void deleteByTableIdAndComponentVersionId(String tableId, String componentVersionId);

	/**
	 * qiucs 2015-1-21 下午1:23:57
	 * <p>描述: 根据菜单ID删除配置 </p>
	 * @return void
	 */
    @Transactional
    @Modifying
    @Query("DELETE FROM AppButton WHERE menuId=?1")
	public void deleteByMenuId(String menuId);

}
