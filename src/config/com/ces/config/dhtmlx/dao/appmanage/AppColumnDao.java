package com.ces.config.dhtmlx.dao.appmanage;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.entity.appmanage.AppColumn;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

public interface AppColumnDao extends StringIDDao<AppColumn> {

    /**
     * 
     * <p>标题: getDefaultColumn</p>
     * <p>描述: 根据表ID、定义过的列表字段IDs获取未定义过列表字段信息</p>
     * @param  tableId
     * @param  columnIds
     * @return List<Object[]>    返回类型   
     * @throws
     */
    @Query(value="select t_cd.id as column_id, t_cd.show_name, t_cd.width, t_cd.align, t_ac.type, t_cd.column_name from t_xtpz_column_define t_cd "
            + "left join t_xtpz_app_column t_ac on(t_ac.table_id=?1 and t_ac.component_version_id=?2 and t_ac.menu_id=?3 and t_cd.id=t_ac.column_id and t_ac.user_id=?4) "
            + "where t_cd.table_id=?1  and t_cd.listable = '1' and t_ac.table_id is null order by t_cd.show_order ", nativeQuery=true)
    public List<Object[]> getDefaultColumn(String tableId, String componentVersionId, String menuId, String userId);

    /**
     * <p>标题: getAllDefineColumn</p>
     * <p>描述: 获取所有的列定义，初始化缓存时使用</p>
     * @return List<Object[]>    返回类型   
     * @throws
     */
    @Query(value="select t_ac.id, t_ac.show_name, t_ac.width, t_ac.align, t_ac.type, t_ac.column_id," +  // 0~5
            " (case when (t_ac.column_type != '0' or t_cd.column_name is null) then t_ac.column_name else t_cd.column_name end) column_name," + // 6
            " t_cd.data_type, t_cd.code_type_code, t_ac.url, t_ac.column_alias, t_ac.column_type, t_cd.input_type, " + // 7~ 12
            " t_ac.table_id, t_ac.component_version_id, t_ac.menu_id, t_ac.user_id, t_cd.data_type_extend " + // 13~17
            " from t_xtpz_app_column t_ac " +
            " left join t_xtpz_column_define t_cd on t_cd.id=t_ac.column_id " +
            " order by t_ac.table_id, t_ac.menu_id, t_ac.component_version_id, t_ac.user_id, t_ac.show_order", nativeQuery=true)
    public List<Object[]> getAllDefineColumn();

    /**
     * <p>标题: getDefineColumn</p>
     * <p>描述: 根据表ID、字段IDs获取相应字段信息</p>
     * @param  tableId
     * @param  componentVersionId
     * @return List<Object[]>    返回类型   
     * @throws
     */
    @Query(value="select t_ac.id, t_ac.show_name, t_ac.width, t_ac.align, t_ac.type, t_ac.column_id," +  // 0~5
    		" (case when (t_ac.column_type != '0' or t_cd.column_name is null) then t_ac.column_name else t_cd.column_name end) column_name," + // 6
    		" t_cd.data_type, t_cd.code_type_code, t_ac.url, t_ac.column_alias, t_ac.column_type, t_cd.input_type, " + // 7~ 12 
    		" t_ac.table_id, t_ac.component_version_id, t_ac.menu_id, t_ac.user_id, t_cd.data_type_extend " + // 13~17
    		" from t_xtpz_app_column t_ac " +
            " left join t_xtpz_column_define t_cd on(t_cd.table_id=?1 and t_cd.id=t_ac.column_id) " +
            " where t_ac.table_id=?1 and t_ac.component_version_id=?2 and t_ac.menu_id=?3 and t_ac.user_id=?4 order by t_ac.show_order", nativeQuery=true)
    public List<Object[]> getDefineColumn(String tableId, String componentVersionId, String menuId, String userId);

    /**
     * <p>标题: deleteAll</p>
     * <p>描述: 根据表ID、模块ID删除列表字段配置</p>
     * @param  tableId
     * @param  componentVersionId    设定参数   
     * @return void    返回类型   
     * @throws
     */
    @Transactional
    @Modifying
    @Query("delete AppColumn t_as where t_as.tableId=?1 and t_as.componentVersionId=?2 and t_as.menuId=?3 and t_as.userId=?4")
    public void deleteByFk(String tableId, String componentVersionId, String menuId, String userId);

    /**
     * qiucs 2013-11-27 
     * <p>描述: 根据模块ID删除列表字段配置</p>
     * @param  tableId    设定参数   
     */
    @Transactional
    @Modifying
    @Query("delete AppColumn t where t.tableId=?1")
    public void deleteByTableId(String tableId);

    /**
     * qiucs 2013-11-27 
     * <p>描述: 根据表ID删除列表字段配置</p>
     * @param  componentVersionId    设定参数   
     */
    @Transactional
    @Modifying
    @Query("delete AppColumn t where t.componentVersionId=?1")
    public void deleteByComponentVersionId(String componentVersionId);
    
    /**
     * qiucs 2014-12-11 
     * <p>描述: 根据菜单ID删除列表字段配置</p>
     * @param  columnId    设定参数   
     */
    @Transactional
    @Modifying
    @Query("delete AppColumn t where t.menuId=?1")
    public void deleteByMenuId(String menuId);
    
    /**
     * qiucs 2013-12-2 
     * <p>描述: 根据字段ID删除列表字段配置</p>
     * @param  columnId    设定参数   
     */
    @Transactional
    @Modifying
    @Query("delete AppColumn t where t.columnId=?1")
    public void deleteByColumnId(String columnId);
    
    /**
     * wangmi  2013-12-26 
     * <p>描述: 根据表ID、模块Id、用户ID查记录</p>
     * @param  tableId,componentVersionId userId    设定参数   
     */
    @Query("select count(id) from AppColumn t where t.tableId=?1 and t.componentVersionId=?2 and t.menuId=?3 and t.userId=?4") 
    public Long count(String tableId, String componentVersionId, String menuId, String userId);
    
    /**
     * qiucs 2014-3-4 
     * <p>描述: </p>
     * @return List<AppColumn>    返回类型   
     */
    @Query("from AppColumn t where t.tableId=?1 and t.componentVersionId=?2 and t.menuId=?3 and t.userId=?4 order by t.showOrder") 
    public List<AppColumn> findByFk(String tableId, String componentVersionId, String menuId, String userId);

    /**
     * qiucs 2014-3-4 
     * <p>描述: </p>
     * @return AppColumn    返回类型   
     */
    @Query("from AppColumn t where t.tableId=?1 and t.componentVersionId=?2 and t.menuId=?3 and t.userId=?3 and t.columnId=?4 and t.showOrder=?5") 
    public AppColumn findByFk(String tableId, String componentVersionId, String menuId, String userId, String columnId, Integer showOrder);
}
