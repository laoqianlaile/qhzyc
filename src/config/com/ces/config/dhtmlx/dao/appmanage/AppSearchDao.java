package com.ces.config.dhtmlx.dao.appmanage;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.entity.appmanage.AppSearch;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

public interface AppSearchDao extends StringIDDao<AppSearch> {

    /**
     * qiucs 2014-12-18 下午10:38:19
     * <p>描述: 根据模块ID获取检索字段信息 </p>
     * @return List<AppSearch>
     */
    @Query("from AppSearch t where t.tableId=?1 and t.componentVersionId=?2 and t.menuId=?3 and t.userId=?4")
    public List<AppSearch> findByFk(String tableId, String componentVersionId, String menuId, String userId);

    /**
     * <p>描述: 根据表ID、定义过的检索字段IDs获取未定义过检索字段信息</p>
     * @return List<Object[]>    返回类型   
     */
    @Query(value="select t_cd.id as column_id, t_cd.show_name, t_cd.column_name,t_cd.filter_type from t_xtpz_column_define t_cd "
            + "left join t_xtpz_app_search t_as on(t_as.table_id=?1 and t_as.component_version_id=?2 and t_as.menu_id=?3 and t_as.user_id=?4 and t_cd.id=t_as.column_id) "
            + "where t_cd.table_id=?1  and t_cd.searchable = '1' and t_as.table_id is null order by t_cd.show_order ", nativeQuery=true)
    public List<Object[]> getDefaultColumn(String tableId, String componentVersionId, String menuId, String userId);

    /**
     * <p>描述:获取所有的相应字段信息，初始化缓存时使用</p>
     * @return List<Object[]>    返回类型   
     */
    @Query(value="select t_as.id, t_cd.id as column_id, t_as.show_name, t_cd.column_name,t_as.filter_type, t_cd.data_type, t_cd.data_type_extend, t_cd.code_type_code, t_cd.input_type, t_cd.input_option, " //0~9
            + " t_as.table_id, t_as.component_version_id, t_as.menu_id, t_as.user_id" //10~13
    		+ " from t_xtpz_app_search t_as "
            + " join t_xtpz_column_define t_cd on t_cd.id=t_as.column_id order by t_as.show_order ", nativeQuery=true)
    public List<Object[]> getAllDefineColumn();

    /**
     * <p>描述: 根据表ID、字段IDs获取相应字段信息</p>
     * @return List<Object[]>    返回类型   
     */
    @Query(value="select t_as.id,  t_cd.id as column_id, t_as.show_name, t_cd.column_name,t_as.filter_type, t_cd.data_type, t_cd.data_type_extend, t_cd.code_type_code, t_cd.input_type, t_cd.input_option"
            + " from t_xtpz_app_search t_as "
            + " join t_xtpz_column_define t_cd on(t_cd.table_id=?1 and t_cd.id=t_as.column_id) "
            + "where t_as.table_id=?1 and t_as.component_version_id=?2 and t_as.menu_id=?3 and t_as.user_id=?4 order by t_as.show_order ", nativeQuery=true)
    public List<Object[]> getDefineColumn(String tableId, String componentVersionId, String menuId, String userId);

    /**
     * qiucs 2013-11-27 
     * <p>描述: 删除检索字段配置</p>
     */
    @Transactional
    @Modifying
    @Query("delete AppSearch t where t.tableId=?1 and t.componentVersionId=?2 and t.menuId=?3 and t.userId=?4")
    public void deleteByFk(String tableId, String componentVersionId, String menuId, String userId);
    
    /**
     * <p>描述: 根据表ID、字段IDs获取相应字段信息</p>
     * @return List<Object[]>    返回类型   
     */
    @Query(value="select t_cd.show_name, t_as.column_name, t_as.filter_type, t_cd.data_type, t_cd.code_type_code from t_xtpz_app_search t_as "
            + " join t_xtpz_column_define t_cd on(t_cd.table_id=?1 and t_cd.id=t_as.column_id) "
            + "where t_as.table_id=?1 and t_as.component_version_id=?2 and t_as.user_id=?3 order by t_as.show_order ", nativeQuery=true)
    public List<Object[]> getSearchColumn(String tableId, String componentVersionId, String userId);

    /**
     * qiucs 2013-11-27 
     * <p>描述: 根据模块ID删除配置</p>
     * @param  tableId    设定参数   
     */
    @Transactional
    @Modifying
    @Query("delete AppSearch t where t.tableId=?1")
    public void deleteByTableId(String tableId);

    /**
     * qiucs 2013-11-27 
     * <p>描述: 根据表ID删除配置</p>
     * @param  tableId    设定参数   
     */
    @Transactional
    @Modifying
    @Query("delete AppSearch t where t.componentVersionId=?1")
    public void deleteByComponentVersionId(String componentVersionId);

    /**
     * qiucs 2014-12-10 
     * <p>描述: 根据菜单ID删除配置</p>
     * @param  tableId    设定参数   
     */
    @Transactional
    @Modifying
    @Query("delete AppSearch t where t.menuId=?1")
    public void deleteByMenuId(String menuId);
    
    /**
     * qiucs 2013-12-2 
     * <p>描述: 根据字段ID删除列表字段配置</p>
     * @param  columnId    设定参数   
     */
    @Transactional
    @Modifying
    @Query("delete AppSearch t where t.columnId=?1")
    public void deleteByColumnId(String columnId);

    /**
     * qiucs  2014-4-3
     * <p>描述: 根据表ID、模块Id、用户ID统计记录</p>
     */
    @Query("select count(id) from AppSearch t where t.tableId=?1 and t.componentVersionId=?2 and t.menuId=?3 and t.userId=?4") 
    public Long count(String tableId, String componentVersionId, String menuId, String userId);
}
