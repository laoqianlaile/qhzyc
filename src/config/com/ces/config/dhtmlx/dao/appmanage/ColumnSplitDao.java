package com.ces.config.dhtmlx.dao.appmanage;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.entity.appmanage.ColumnSplit;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

public interface ColumnSplitDao extends StringIDDao<ColumnSplit>{
	 
    @Query(value="select * from t_xtpz_column_split t " +
    		"where t.table_id=?1", nativeQuery=true)
    public List<Object[]> getSplitBytableId(String tableId);
    
    /**
     * qiucs 2013-10-21 
     * <p>描述: 根据表ID查询截取配置信息</p>
     * @param  tableId
     * @return List<ColumnSplit>    返回类型   
     * @throws
     */
    @Query("FROM ColumnSplit T WHERE T.tableId=?1")
    public List<ColumnSplit> findByTableId(String tableId);
    
    /**
     * qiucs 2013-10-22 
     * <p>描述: </p>
     * @param  columnRelationId
     */
    @Query("FROM ColumnSplit T WHERE T.columnRelationId = ?1")
    public ColumnSplit findByColumnRelationId(String columnRelationId);
    
    /**
     * qiucs 2013-11-28 
     * <p>描述: 根据表ID删除</p>
     * @param  tableId    设定参数   
     */
    @Modifying
    @Transactional
    @Query("delete from ColumnSplit where tableId=?1")
    public void deleteByTableId(String tableId);
    
    /**
     * qiucs 2013-12-2 
     * <p>描述: 根据字段ID更新配置</p>
     * @param  columnId    字段ID   
     */
    @Modifying
    @Transactional
    @Query("update ColumnSplit set toColumnId=null where toColumnId=?1")
    public void updateByToColumnId(String columnId);
    
    /**
     * qiucs 2013-12-2 
     * <p>描述: 根据字段ID更新配置</p>
     * @param  columnId    字段ID   
     */
    @Modifying
    @Transactional
    @Query("update ColumnSplit set fromColumnId=null where fromColumnId=?1")
    public void updateByFromColumnId(String columnId);

}
