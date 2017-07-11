package com.ces.config.dhtmlx.dao.appmanage;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.entity.appmanage.AppSort;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

public interface AppSortDao extends StringIDDao<AppSort> {

    /**
     * <p>标题: getDefaultColumn</p>
     * <p>描述: 根据表ID、定义过的列表字段IDs获取未定义过列表字段信息</p>
     * @param  tableId
     * @param  componentVersionId
     * @return List<Object[]>    返回类型   
     * @throws
     */
    @Query(value="select t_cd.id column_id, t_cd.show_name from t_xtpz_column_define t_cd "
            + "left join t_xtpz_app_sort t_app on(t_app.table_id=?1 and t_app.component_version_id=?2 and t_app.menu_id=?3 and t_app.user_id=?4 and t_cd.id=t_app.column_id) "
            + "where t_cd.table_id=?1 and t_cd.sortable = '1' and t_app.table_id is null order by t_cd.show_order", nativeQuery=true)
    public List<Object[]> getDefaultColumn(String tableId, String componentVersionId, String menuId, String userId);

    /**
     * <p>标题: getAllDefineColumn</p>
     * <p>描述: 获取相应字段信息，初始化缓存时使用</p>
     * @return List<Object[]>    返回类型   
     * @throws
     */
    @Query(value="select t_app.id as id, t_cd.id as column_id, t_cd.show_name, t_app.sort_type, t_cd.column_name, " //0~4
            + "t_app.table_id, t_app.component_version_id, t_app.menu_id, t_app.user_id " //5~8
            + "from t_xtpz_app_sort t_app "
            + "join t_xtpz_column_define t_cd on t_cd.id=t_app.column_id order by t_app.show_order ", nativeQuery=true)
    public List<Object[]> getAllDefineColumn();
    
    /**
     * <p>标题: getDefineColumn</p>
     * <p>描述: 根据表ID、字段IDs获取相应字段信息</p>
     * @param  tableId
     * @param  componentVersionId
     * @return List<Object[]>    返回类型   
     * @throws
     */
    @Query(value="select t_app.id as id, t_cd.id as column_id, t_cd.show_name, t_app.sort_type, t_cd.column_name from t_xtpz_app_sort t_app "
            + " join t_xtpz_column_define t_cd on(t_cd.table_id=?1 and t_cd.id=t_app.column_id) "
            + "where t_app.table_id=?1 and t_app.component_version_id=?2 and t_app.menu_id=?3 and t_app.user_id=?4 order by t_app.show_order ", nativeQuery=true)
    public List<Object[]> getDefineColumn(String tableId, String componentVersionId, String menuId, String userId);

    /**
     * 获取排序字段数据
     * 
     * @param tableId
     * @param componentVersionId
     * @param userId
     * @return
     */
    @Query("from AppSort where tableId=?1 and componentVersionId=?2 and menuId=?3 and userId=?4 order by showOrder")
    public List<AppSort> findByFk(String tableId, String componentVersionId, String menuId, String userId);
    
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
    @Query("delete AppSort t where t.tableId=?1 and t.componentVersionId=?2 and t.menuId=?3 and t.userId=?4")
    public void deleteByFk(String tableId, String componentVersionId, String menuId, String userId);

    /**
     * qiucs 2013-11-27 
     * <p>描述: 根据模块ID删除配置</p>
     * @param  tableId    设定参数   
     */
    @Transactional
    @Modifying
    @Query("delete AppSort t where t.tableId=?1")
    public void deleteByTableId(String tableId);

    /**
     * qiucs 2013-11-27 
     * <p>描述: 根据自定义构件ID删除配置</p>
     */
    @Transactional
    @Modifying
    @Query("delete AppSort t where t.componentVersionId=?1")
    public void deleteByComponentVersionId(String componentVersionId);

    /**
     * qiucs 2014-12-11 
     * <p>描述: 根据菜单ID删除配置</p>
     */
    @Transactional
    @Modifying
    @Query("delete AppSort t where t.menuId=?1")
    public void deleteByMenuId(String menuId);
    
    /**
     * qiucs 2013-12-2 
     * <p>描述: 根据字段ID删除列表字段配置</p>
     * @param  columnId    设定参数   
     */
    @Transactional
    @Modifying
    @Query("delete AppSort t where t.columnId=?1")
    public void deleteByColumnId(String columnId);

    /**
     * qiucs  2014-4-3
     * <p>描述: 根据表ID、模块Id、用户ID查统计记录</p>
     */
    @Query("select count(id) from AppSort t where t.tableId=?1 and t.componentVersionId=?2 and t.menuId=?3 and t.userId=?4") 
    public Long count(String tableId, String componentVersionId, String menuId, String userId);
    
}
