package com.ces.config.dhtmlx.dao.appmanage;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.entity.appmanage.ColumnBusiness;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

public interface ColumnBusinessDao extends StringIDDao<ColumnBusiness> {

    /**
     * qiucs 2013-10-22 
     * <p>描述: </p>
     * @param  columnRelationId
     */
    @Query("FROM ColumnBusiness T WHERE T.columnRelationId = ?1")
    public ColumnBusiness findByColumnRelationId(String columnRelationId);
    
    /**
     * 根据表ID获取该表下所有的特殊业务
     * 
     * @param tableId 表ID
     * @return List<ColumnBusiness>
     */
    public List<ColumnBusiness> findByTableId(String tableId);
    
    /**
     * qiucs 2013-11-28 
     * <p>描述: 根据表ID删除特殊业务</p>
     * @param  tableId    设定参数   
     */
    @Modifying
    @Transactional
    @Query("delete from ColumnBusiness where tableId=?1")
    public void deleteByTableId(String tableId);
    
    /**
     * qiucs 2013-12-2 
     * <p>描述: 根据字段ID更新配置</p>
     * @param  columnId    字段ID   
     */
    @Modifying
    @Transactional
    @Query("update ColumnBusiness set itemColumnId=null where itemColumnId=?1")
    public void updateByItemColumnId(String columnId);
    
    /**
     * qiucs 2013-12-2 
     * <p>描述: 根据字段ID更新配置</p>
     * @param  columnId    字段ID   
     */
    @Modifying
    @Transactional
    @Query("update ColumnBusiness set pagesColumnId=null where pagesColumnId=?1")
    public void updateByPagesColumnId(String columnId);
    
    /**
     * qiucs 2013-12-2 
     * <p>描述: 根据字段ID更新配置</p>
     * @param  columnId    字段ID   
     */
    @Modifying
    @Transactional
    @Query("update ColumnBusiness set pagenoColumnId=null where pagenoColumnId=?1")
    public void updateByPagenoColumnId(String columnId);
}
