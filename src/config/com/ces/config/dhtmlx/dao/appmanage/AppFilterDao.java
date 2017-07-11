package com.ces.config.dhtmlx.dao.appmanage;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.entity.appmanage.AppFilter;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

public interface AppFilterDao extends StringIDDao<AppFilter> {
    
    /**
     * qiucs 2013-10-17 
     * <p>描述: 获取列表过滤条件</p>
     * @return List<AppColumn> 返回类型
     */
    @Query("from AppFilter t where t.tableId=?1 and t.componentVersionId=?2 and t.menuId=?3")
    public List<AppFilter> findByFk(String tableId, String componentVersionId, String menuId);
    
    /**
     * qiucs 2013-10-17 
     * <p>描述: 可选字段</p>
     * @return List<Object[]>    返回类型   
     */
    @Query(value="select t_cd.id as column_id, t_cd.show_name, t_cd.column_name, t_cd.data_type, t_cd.code_type_code from t_xtpz_column_define t_cd "
            + "left join t_xtpz_app_filter t_app on(t_app.table_id=?1 and t_app.component_version_id=?2 and t_app.menu_id=?3 and t_cd.id=t_app.column_id) "
            + "where t_cd.table_id=?1  and t_cd.searchable = '1'", nativeQuery=true)
    public List<Object[]> getDefaultColumn(String tableId, String componentVersionId, String menuId);
    
    /**
     * qiucs 2013-10-17 
     * <p>描述: 已选字段</p>
     * @return List<Object[]>    返回类型   
     */
    @Query(value="select t_app.id, t_cd.show_name, t_app.filter_type, t_app.value, t_cd.column_name, t_cd.id columnId," +
    		" t_app.relation, t_app.left_parenthesis, t_app.right_parenthesis, t_cd.data_type, t_cd.code_type_code " +
    		" from t_xtpz_column_define t_cd " +
            " join t_xtpz_app_filter t_app on(t_app.table_id=?1 and t_app.component_version_id=?2 and t_app.menu_id=?3 and t_cd.id=t_app.column_id) " + 
            "where t_cd.table_id=?1 order by t_app.show_order", nativeQuery=true)
    public List<Object[]> getDefineColumn(String tableId, String componentVersionId, String menuId);
    
    /**
     * qiucs 2013-10-17 
     * <p>描述: 清空配置</p>
     * @return void    返回类型   
     */
    @Transactional
    @Modifying
    @Query("DELETE AppFilter T WHERE T.tableId=?1 AND T.componentVersionId=?2 AND T.menuId=?3")
    public void clear(String tableId, String componentVersionId, String menuId);

    /**
     * qiucs 2013-11-27 
     * <p>描述: 根据模块ID删除配置</p>
     * @param  tableId    设定参数   
     */
    @Transactional
    @Modifying
    @Query("delete AppFilter t where t.tableId=?1")
    public void deleteByTableId(String tableId);

    /**
     * qiucs 2013-11-27 
     * <p>描述: 根据表ID删除配置</p>
     * @param  tableId    设定参数   
     */
    @Transactional
    @Modifying
    @Query("delete AppFilter t where t.menuId=?1")
    public void deleteByMenuId(String menuId);

    /**
     * qiucs 2013-11-27 
     * <p>描述: 根据表ID删除配置</p>
     * @param  tableId    设定参数   
     */
    @Transactional
    @Modifying
    @Query("delete AppFilter t where t.componentVersionId=?1")
    public void deleteByComponentVersionId(String componentVersionId);
    
    /**
     * qiucs 2013-12-2 
     * <p>描述: 根据字段ID删除列表字段配置</p>
     * @param  columnId    设定参数   
     */
    @Transactional
    @Modifying
    @Query("delete AppFilter t where t.columnId=?1")
    public void deleteByColumnId(String columnId);
}
