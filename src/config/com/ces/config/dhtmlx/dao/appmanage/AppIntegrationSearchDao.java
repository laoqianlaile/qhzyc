package com.ces.config.dhtmlx.dao.appmanage;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.entity.appmanage.AppIntegrationSearch;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

public interface AppIntegrationSearchDao extends StringIDDao<AppIntegrationSearch> {
	
    /**
     * <p>描述: 根据模块ID获取检索字段信息 </p>
     * @return List<AppIntegrationSearch>
     */
    @Query("from AppIntegrationSearch t where t.tableId=?1 and t.componentVersionId=?2 and t.menuId=?3")
    public List<AppIntegrationSearch> findByFk(String tableId, String componentVersionId, String menuId);
    
	/**
     * <p>描述: 根据表ID、定义过的检索字段IDs获取未定义过检索字段信息</p>
     * @return List<Object[]>    返回类型   
     */
    @Query(value="select t_cd.id as column_id, t_cd.show_name, t_cd.column_name from t_xtpz_column_define t_cd "
            + "left join t_xtpz_app_integration_search t_ais on(t_ais.table_id=?1 and t_ais.component_version_id=?2 and t_ais.menu_id=?3 and t_cd.id=t_ais.column_id) "
            + "where t_cd.table_id=?1  and t_cd.searchable = '1' and t_ais.table_id is null order by t_cd.show_order ", nativeQuery=true)
    public List<Object[]> getDefaultColumn(String tableId, String componentVersionId, String menuId);

    /**
     * <p>描述: 根据表ID、字段IDs获取相应字段信息</p>
     * @return List<Object[]>    返回类型   
     */
    @Query(value="select t_ais.id,  t_cd.id as column_id, t_cd.show_name, t_cd.column_name"
    		+ " from t_xtpz_app_integration_search t_ais "
            + " join t_xtpz_column_define t_cd on(t_cd.table_id=?1 and t_cd.id=t_ais.column_id) "
            + " where t_ais.table_id=?1 and t_ais.component_version_id=?2 and t_ais.menu_id=?3 order by t_ais.show_order ", nativeQuery=true)
    public List<Object[]> getDefineColumn(String tableId, String componentVersionId, String menuId);
    
    /**
     * <p>描述: 根据表ID、模块Id、用户ID统计记录</p>
     */
    @Query("select count(id) from AppIntegrationSearch t where t.tableId=?1 and t.componentVersionId=?2 and t.menuId=?3") 
    public Long count(String tableId, String componentVersionId, String menuId);
    
    /**
     * <p>描述: 根据表ID、模块ID删除检索字段配置</p>
     */
    @Transactional
    @Modifying
    @Query("delete AppIntegrationSearch where tableId=?1 and componentVersionId=?2 and menuId is null")
    public void deleteByFk(String tableId, String componentVersionId);
    
    /**
     * <p>描述: 删除检索字段配置</p>
     */
    @Transactional
    @Modifying
    @Query("delete AppIntegrationSearch t where t.tableId=?1 and t.componentVersionId=?2 and t.menuId=?3")
    public void deleteByFk(String tableId, String componentVersionId, String menuId);
    
    /**
     * <p>描述: 根据字段ID删除列表字段配置</p>
     * @param  columnId    设定参数   
     */
    @Transactional
    @Modifying
    @Query("delete AppIntegrationSearch t where t.columnId=?1")
    public void deleteByColumnId(String columnId);

}
