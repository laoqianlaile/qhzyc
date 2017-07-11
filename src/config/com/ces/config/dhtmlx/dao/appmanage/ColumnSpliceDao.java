package com.ces.config.dhtmlx.dao.appmanage;


import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.entity.appmanage.ColumnSplice;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

public interface ColumnSpliceDao extends StringIDDao<ColumnSplice>{
	
  
  
    	
    @Query(value="  select * from t_xtpz_column_splice t " +
    		"where t.table_id=?1", nativeQuery=true)
    public List<Object[]> getSpliceBytableId(String tableId);
    
    
    public List<ColumnSplice> findByTableId(String tableId);
    
    /**
     * qiucs 2013-10-22 
     * <p>描述: </p>
     * @param  columnRelationId
     * @return ColumnSplice    返回类型   
     * @throws
     */
    @Query("FROM ColumnSplice T WHERE T.columnRelationId = ?1")
    public ColumnSplice findByColumnRelationId(String columnRelationId);
    
    /**
     * qiucs 2013-11-28 
     * <p>描述: 根据表ID删除</p>
     * @param  tableId    设定参数   
     */
    @Modifying
    @Transactional
    @Query("delete from ColumnSplice where tableId=?1")
    public void deleteByTableId(String tableId);
    
    /**
     * qiucs 2013-12-2 
     * <p>描述: 根据字段ID更新配置</p>
     * @param  columnId    字段ID   
     */
    @Modifying
    @Transactional
    @Query("update ColumnSplice set storeColumnId=null where storeColumnId=?1")
    public void updateByStoreColumnId(String columnId);
    
    /**
     * qiucs 2013-12-2 
     * <p>描述: 根据字段ID更新配置</p>
     * @param  columnId    字段ID   
     */
    @Modifying
    @Transactional
    @Query("update ColumnSplice set column1Id=null where column1Id=?1")
    public void updateByColumn1Id(String columnId);
    
    /**
     * qiucs 2013-12-2 
     * <p>描述: 根据字段ID更新配置</p>
     * @param  columnId    字段ID   
     */
    @Modifying
    @Transactional
    @Query("update ColumnSplice set column2Id=null where column2Id=?1")
    public void updateByColumn2Id(String columnId);
    
    /**
     * qiucs 2013-12-2 
     * <p>描述: 根据字段ID更新配置</p>
     * @param  columnId    字段ID   
     */
    @Modifying
    @Transactional
    @Query("update ColumnSplice set column3Id=null where column3Id=?1")
    public void updateByColumn3Id(String columnId);
    
    /**
     * qiucs 2013-12-2 
     * <p>描述: 根据字段ID更新配置</p>
     * @param  columnId    字段ID   
     */
    @Modifying
    @Transactional
    @Query("update ColumnSplice set column4Id=null where column4Id=?1")
    public void updateByColumn4Id(String columnId);
    
    /**
     * qiucs 2013-12-2 
     * <p>描述: 根据字段ID更新配置</p>
     * @param  columnId    字段ID   
     */
    @Modifying
    @Transactional
    @Query("update ColumnSplice set column5Id=null where column5Id=?1")
    public void updateByColumn5Id(String columnId);

}
