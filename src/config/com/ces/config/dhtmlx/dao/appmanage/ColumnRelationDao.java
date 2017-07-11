package com.ces.config.dhtmlx.dao.appmanage;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.entity.appmanage.ColumnRelation;
import com.ces.xarch.core.persistence.jpa.StringIDDao;
/**
 * 字段自定义
 * @author wang
 *
 */
public interface ColumnRelationDao extends StringIDDao<ColumnRelation>{
	
	/**
	 * 字段关联列表查询
	 * @return
	 */
	@Query(value="select cr.id,cr.name,cr.type,cr.type as ty from t_xtpz_column_relation cr where cr.table_id=?1 order by cr.show_order", nativeQuery=true)
	public List<Object[]> getAllColumnRelation(String tableId);
	
	/**
	 * 根据表ID获取该表下所有字段关联
	 * 
	 * @param tableId 表ID
	 * @return List<ColumnRelation>
	 */
	public List<ColumnRelation> findByTableId(String tableId);
	
    /**
     * qiucs 2013-11-28 
     * <p>描述: 根据表ID删除</p>
     * @param  tableId    设定参数   
     */
    @Modifying
    @Transactional
    @Query("delete from ColumnRelation where tableId=?1")
    public void deleteByTableId(String tableId);

    /**
	 * liaomingsong 2014-5-14 
	 * <p>描述: 根据tableId 获取字段显示顺序范围内</p>
	 * @param  start  end tableId
	 * @return List<ColumnRelation>    返回类型   
	 * @throws
	 */
	public List<ColumnRelation> getByShowOrderBetweenAndTableId(Integer start, Integer end, String tableId);

	/**
     * 获取最大显示顺序
     * 
     * @param parentId 父菜单ID
     */
    @Query("select max(showOrder) from ColumnRelation where tableId=?")
	public Integer getMaxShowOrder(String tableId);
    
}
