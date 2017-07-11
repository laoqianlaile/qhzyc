package com.ces.config.dhtmlx.dao.appmanage;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.entity.appmanage.ColumnDefine;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

public interface ColumnDefineDao extends StringIDDao<ColumnDefine>{
	
    /**
     * 根据字段名称和表Id获取字段
     * @param columnName 字段名称
     * @param tableId 表Id
     * @return ColumnDefine
     */
	public ColumnDefine findByColumnNameAndTableId(String columnName, String tableId);
	
	/**
	 * <p>描述: 根据表ID删除字段记录</p>
	 * @param  tableId    设定参数   
	 * @return void    返回类型   
	 * @throws
	 */
	@Modifying
	@Transactional
	@Query("delete ColumnDefine t_cd where t_cd.tableId=?1 ")
	public void deleteByTableId(String tableId);

	/**
	 * <p>描述: 获取表中所有字段</p>
	 * @param  tableId
	 * @return List<ColumnDefine>    返回类型   
	 * @throws
	 */
	@Query("from ColumnDefine t_cd where t_cd.tableId=?1 order by t_cd.showOrder")
	public List<ColumnDefine> findByTableId(String tableId);
	
	/**
	 * <p>描述: 获取表中业务字段</p>
	 * @param  tableId
	 * @return List<ColumnDefine>    返回类型   
	 * @throws
	 */
	public List<ColumnDefine> findByTableIdAndColumnType(String tableId, String columnType);
	
	/**
     * <p>描述: 获取表中业务字段</p>
     * @param  tableId
     * @return List<ColumnDefine>    返回类型   
     * @throws
     */
    public List<ColumnDefine> findByTableIdAndInputType(String tableId, String InputType);
    
    /**
     * <p>描述: 获取表中业务字段</p>
     * @param  tableId
     * @return List<ColumnDefine>    返回类型   
     * @throws
     */
    public List<ColumnDefine> findByTableIdAndInputOption(String tableId, String InputOption);
	
	/**
	 * qiucs 2013-9-29 
	 * <p>描述: 字段英文名称</p>
	 * @param  id
	 * @return String    返回类型   
	 * @throws
	 */
	@Query("SELECT T.columnName FROM ColumnDefine T WHERE T.id=?1")
	public String findColumnNameById(String id);

    /**
     *  根据id，获得实体
     */
    public ColumnDefine findById(String id);

    /**
     * qiucs 2013-9-29 
     * <p>描述: 字段数据类型</p>
     * @param  id
     * @return String    返回类型   
     * @throws
     */
    @Query("SELECT T.dataType FROM ColumnDefine T WHERE T.id=?1")
    public String findDataTypeById(String id);
    
    /**
     * <p>描述: 字段英文名称</p>
     * @param columnName
     * @param tableId
     * @return
     * @author Administrator
     * @date 2013-10-28  09:50:57
     */
    @Query("select count(id) from ColumnDefine where columnName=?1 and tableId=?2")
    public Long countByColumnNameAndTableId(String columnName, String tableId);
    
	/**
	 * wangmi 2013-12-10 
	 * <p>描述: 根据tableId 获取最大显示排序</p>
	 * @param  tableId
	 * @return String    返回类型   
	 * @throws
	 */
    @Query("select max(showOrder) from ColumnDefine t where tableId=?1")
    public Integer getMaxShowOrder(String tableId);
    
	/**
	 * wangmi 2013-12-10 
	 * <p>描述: 根据tableId 获取字段显示顺序范围内</p>
	 * @param  start  end tableId
	 * @return List<ColumnDefine>    返回类型   
	 * @throws
	 */
    public List<ColumnDefine> getByShowOrderBetweenAndTableId(Integer start, Integer end, String tableId);
    
    /**
     * qiucs 2014-1-3 
     * <p>描述: 编码类型删除时，级联更新codeTypeCode属性</p>
     * @param  codeTypeCode
     */
    @Transactional
    @Modifying
    @Query("update ColumnDefine set codeTypeCode=null where codeTypeCode=?1")
    public void updateByCodeTypeCode(String codeTypeCode);

    /**
     * 修改字段标签的值
     * 
     * @param oldColumnLabel 旧的字段标签Code
     * @param newColumnLabel 新的字段标签Code
     */
    @Transactional
    @Modifying
    @Query("update ColumnDefine set columnLabel=?2 WHERE columnLabel=?1")
    public void batchUpdateColumnLabel(String oldColumnLabel, String newColumnLabel);
    
    /**
     * qiucs 2014-9-5 
     * <p>描述: 获取修改字段</p>
     * @return List<ColumnDefine>    返回类型   
     * @throws
     */
    @Query("from ColumnDefine t where t.tableId=?1 and t.updateable = '1' order by t.showOrder ")
    public List<ColumnDefine> findByUpdateColumns(String tableId);

    /**
     * qiucs 2014-10-15 
     * <p>描述: 根据表ID获取字段标签</p>
     * @return List<Object>    返回类型   
     */
    @Query("select t.columnLabel from ColumnDefine t where t.tableId=?1 and t.columnLabel is not null order by t.showOrder ")
    public List<Object> getColumnLabelsByTableId(String tableId);
    
    /**
     * qiucs 2014-11-26 
     * <p>描述: 根据字段标签与表ID获取字段名称</p>
     * @return Long    返回类型   
     */
    @Query("select columnName from ColumnDefine where columnLabel=?1 and tableId=?2")
    public String getColumnNameByColumnLabelAndTableId(String columnLabel, String tableId);
    
    /**
     * qiujinwei 2014-12-15 
     * <p>描述: 根据字段名称与表ID获取字段ID</p>
     */
    @Query("select id from ColumnDefine where tableId=?1 and columnName=?2")
    public String getColumnIdByTableIdAndColumnName(String tableId, String columnName);
    
    /**
     * 批量修改逻辑表编码
     * @param oldLogicTableCode 旧的逻辑表编码
     * @param newLogicTableCode 新的逻辑表编码
     */
    @Transactional
    @Modifying
    @Query("update ColumnDefine set tableId=?2 WHERE tableId=?1")
    public void batchUpdateTableCode(String oldLogicTableCode, String newLogicTableCode);
    
    /**
     * qiucs 2015-4-30 下午5:12:50
     * <p>描述: 查找两张表中相同字段标签的字段 </p>
     * @return List<Object[]>
     */
    @Query("select t1.columnName, t2.columnName from ColumnDefine t1, ColumnDefine t2 where " +
    		"t1.tableId=?1 and t2.tableId=?2 and t1.columnLabel=t2.columnLabel")
    public List<Object[]> getInheritColumnList(String tableId, String masterTableId);
}
