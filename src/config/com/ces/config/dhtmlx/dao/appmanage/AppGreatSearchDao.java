package com.ces.config.dhtmlx.dao.appmanage;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.entity.appmanage.AppGreatSearch;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

public interface AppGreatSearchDao extends StringIDDao<AppGreatSearch> {
	
    /**
     * <p>描述: 根据模块ID获取检索字段信息 </p>
     * @return List<AppGreatSearch>
     */
    @Query("from AppGreatSearch t where t.tableId=?1 and t.componentVersionId=?2 and t.menuId=?3")
    public List<AppGreatSearch> findByFk(String tableId, String componentVersionId, String menuId);
    
	/**
     * <p>描述: 根据表ID、定义过的检索字段IDs获取未定义过检索字段信息</p>
     * @return List<Object[]>    返回类型   
     */
    @Query(value="select t_cd.id as column_id, t_cd.show_name, t_cd.column_name from t_xtpz_column_define t_cd "
            + "left join t_xtpz_app_great_search t_ags on(t_ags.table_id=?1 and t_ags.component_version_id=?2 and t_ags.menu_id=?3  and t_cd.id=t_ags.column_id) "
            + "where t_cd.table_id=?1  and t_cd.searchable = '1' and t_ags.table_id is null order by t_cd.show_order ", nativeQuery=true)
    public List<Object[]> getDefaultColumn(String tableId, String componentVersionId, String menuId);

    /**
     * <p>描述: 根据表ID、字段IDs获取相应字段信息</p>
     * @return List<Object[]>    返回类型   
     */
    @Query(value="select t_ags.id,  t_cd.id as column_id, t_cd.show_name, t_cd.column_name,t_cd.data_type, t_cd.data_type_extend, t_cd.code_type_code, t_cd.input_type"
    		+ " from t_xtpz_app_great_search t_ags "
            + " join t_xtpz_column_define t_cd on(t_cd.table_id=?1 and t_cd.id=t_ags.column_id) "
            + " where t_ags.table_id=?1 and t_ags.component_version_id=?2 and t_ags.menu_id=?3 order by t_ags.show_order ", nativeQuery=true)
    public List<Object[]> getDefineColumn(String tableId, String componentVersionId, String menuId);
    
    /**
     * <p>描述: 根据表ID、模块Id、用户ID统计记录</p>
     */
    @Query("select count(id) from AppGreatSearch t where t.tableId=?1 and t.componentVersionId=?2 and t.menuId=?3") 
    public Long count(String tableId, String componentVersionId, String menuId);
    
    /**
     * <p>描述: 根据表ID、模块ID删除检索字段配置</p>
     */
    @Transactional
    @Modifying
    @Query("delete AppGreatSearch where tableId=?1 and componentVersionId=?2 and menuId is null")
    public void deleteByFk(String tableId, String componentVersionId);
    
    /**
     * <p>描述: 删除检索字段配置</p>
     */
    @Transactional
    @Modifying
    @Query("delete AppGreatSearch t where t.tableId=?1 and t.componentVersionId=?2 and t.menuId=?3")
    public void deleteByFk(String tableId, String componentVersionId, String menuId);
    
    /**
     * <p>描述: 根据字段ID删除列表字段配置</p>
     * @param  columnId    设定参数   
     */
    @Transactional
    @Modifying
    @Query("delete AppGreatSearch t where t.columnId=?1")
    public void deleteByColumnId(String columnId);

}
