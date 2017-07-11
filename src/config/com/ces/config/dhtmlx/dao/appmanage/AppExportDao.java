package com.ces.config.dhtmlx.dao.appmanage;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.entity.appmanage.AppExport;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

public interface AppExportDao extends StringIDDao<AppExport> {
	
	/**
     * 
     * <p>标题: getDefaultColumn</p>
     * <p>描述: 根据表ID、定义过的列表字段IDs获取未定义过列表字段信息</p>
     * @param  tableId
     * @param  columnIds
     * @return List<Object[]>    返回类型   
     * @throws
     */
    @Query(value="select t_cd.id as column_id, t_cd.show_name, t_cd.column_name from t_xtpz_column_define t_cd "
            + "where t_cd.id not in(select t_ae.column_id from t_xtpz_app_export t_ae where t_ae.table_id=?1 and t_ae.component_version_id=?2 and t_ae.menu_id=?3) "
            + "and t_cd.table_id=?1", nativeQuery=true)
    public List<Object[]> getDefaultColumn(String tableId, String componentVersionId, String menuId);

    /**
     * <p>标题: getDefineColumn</p>
     * <p>描述: 根据表ID、字段IDs获取相应字段信息</p>
     * @param  tableId
     * @param  componentVersionId
     * @return List<Object[]>    返回类型   
     * @throws
     */
    @Query(value="select t_ae.id, t_ae.column_id, t_cd.show_name, t_cd.column_name" +  
    		" from t_xtpz_app_export t_ae " +
            " left join t_xtpz_column_define t_cd on(t_cd.table_id=?1 and t_cd.id=t_ae.column_id) " +
            " where t_ae.table_id=?1 and t_ae.component_version_id=?2 and t_ae.menu_id=?3 order by t_ae.show_order", nativeQuery=true)
    public List<Object[]> getDefineColumn(String tableId, String componentVersionId, String menuId);
    
    /**
     * 
     * <p>标题: getDefaultColumn</p>
     * <p>描述: 根据表ID、定义过的列表字段IDs获取未定义过列表字段信息(不显示系统字段)</p>
     * @param  tableId
     * @param  columnIds
     * @return List<Object[]>    返回类型   
     * @throws
     */
    @Query(value="select t_cd.id as column_id, t_cd.show_name, t_cd.column_name from t_xtpz_column_define t_cd "
            + "where t_cd.id not in(select t_ae.column_id from t_xtpz_app_export t_ae where t_ae.table_id=?1 and t_ae.component_version_id=?2 and t_ae.menu_id=?3) "
            + "and t_cd.table_id=?1 and t_cd.column_type=?4", nativeQuery=true)
    public List<Object[]> getDefaultColumn(String tableId, String componentVersionId, String menuId, String columnType);

    /**
     * <p>标题: getDefineColumn</p>
     * <p>描述: 根据表ID、字段IDs获取相应字段信息(不显示系统字段)</p>
     * @param  tableId
     * @param  componentVersionId
     * @return List<Object[]>    返回类型   
     * @throws
     */
    @Query(value="select t_ae.id, t_ae.column_id, t_cd.show_name, t_cd.column_name" +  
    		" from t_xtpz_app_export t_ae " +
            " left join t_xtpz_column_define t_cd on(t_cd.table_id=?1 and t_cd.id=t_ae.column_id) " +
            " where t_ae.table_id=?1 and t_ae.component_version_id=?2 and t_ae.menu_id=?3 and t_cd.column_type=?4 order by t_ae.show_order", nativeQuery=true)
    public List<Object[]> getDefineColumn(String tableId, String componentVersionId, String menuId, String columnType);
    
    /**
     * <p>描述: 删除检索字段配置</p>
     */
    @Transactional
    @Modifying
    @Query("delete AppExport t where t.tableId=?1 and t.componentVersionId=?2 and t.menuId=?3")
    public void deleteByFk(String tableId, String componentVersionId, String menuId);
    
    /**
     * <p>描述: 根据字段ID删除列表字段配置</p>
     * @param  columnId    设定参数   
     */
    @Transactional
    @Modifying
    @Query("delete AppExport t where t.columnId=?1")
    public void deleteByColumnId(String columnId);

}
