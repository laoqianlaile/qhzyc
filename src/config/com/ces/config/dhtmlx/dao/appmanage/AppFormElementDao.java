package com.ces.config.dhtmlx.dao.appmanage;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.entity.appmanage.AppFormElement;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

public interface AppFormElementDao extends StringIDDao<AppFormElement> {
    
    /**
     * <p>标题: getDefaultColumn</p>
     * <p>描述: 获取可配置的界面字段</p>
     * @param  tableId
     * @param  componentVersionId
     * @return List<Object[]>    返回类型   
     * @throws
     */
    @Query(value="select t_cd.id as c_id, t_cd.show_name, t_cd.column_name, t_cd.data_type, t_cd.length, t_cd.code_type_code, " + // 0~5
            " t_fe.id as f_id, t_fe.colspan, t_fe.required, t_fe.readonly, t_fe.hidden, t_cd.input_type, t_fe.default_value, t_fe.kept, t_fe.validation, t_fe.tooltip, t_fe.increase, t_fe.inherit, t_cd.data_type_extend, " + // 6~18 
    		" t_cd.input_option, t_fe.pattern, t_fe.space_percent " + //19~21
    		" from t_xtpz_column_define t_cd " + 
            " left join t_xtpz_app_form_element t_fe on(t_fe.table_id=?1 and t_fe.component_version_id=?2 and t_fe.menu_id=?3 and t_cd.id=t_fe.column_id) " + 
            " where t_cd.table_id=?1  and t_cd.inputable = '1' and t_fe.id is null order by t_cd.show_order", nativeQuery=true)
    public List<Object[]> getDefaultColumn(String tableId, String componentVersionId, String menuId);
    
    /**
     * <p>标题: getAllDefineColumn</p>
     * <p>描述: 获取所有已配置的界面字段，初始化缓存时使用</p>
     * @return List<Object[]>    返回类型   
     * @throws
     */
    @Query(value="select t_fe.column_id as c_id, t_fe.show_name, t_cd.column_name, t_cd.data_type, t_cd.length, t_cd.code_type_code, " + // 0~5
            " t_fe.id as f_id, t_fe.colspan, t_fe.required, t_fe.readonly, t_fe.hidden, t_cd.input_type, t_fe.default_value, t_fe.kept, t_fe.validation, t_fe.tooltip, t_fe.increase, t_fe.inherit, t_cd.data_type_extend, " + // 6~18 
            " t_cd.input_option, t_fe.pattern, t_fe.space_percent, t_fe.table_id, t_fe.component_version_id, t_fe.menu_id " + //19~24
            " from t_xtpz_app_form_element t_fe " + 
            " left join t_xtpz_column_define t_cd on t_cd.id=t_fe.column_id order by t_fe.show_order ", nativeQuery=true)
    public List<Object[]> getAllDefineColumn();
    
    /**
     * <p>标题: getDefineColumn</p>
     * <p>描述: 获取已配置的界面字段</p>
     * @param  tableId
     * @param  componentVersionId
     * @return List<Object[]>    返回类型   
     * @throws
     */
    @Query(value="select t_fe.column_id as c_id, t_fe.show_name, t_cd.column_name, t_cd.data_type, t_cd.length, t_cd.code_type_code, " + // 0~5
            " t_fe.id as f_id, t_fe.colspan, t_fe.required, t_fe.readonly, t_fe.hidden, t_cd.input_type, t_fe.default_value, t_fe.kept, t_fe.validation, t_fe.tooltip, t_fe.increase, t_fe.inherit, t_cd.data_type_extend, " + // 6~18 
            " t_cd.input_option, t_fe.pattern, t_fe.space_percent " + //19-21
            " from t_xtpz_app_form_element t_fe " + 
            " left join t_xtpz_column_define t_cd on(t_cd.table_id=?1 and t_cd.id=t_fe.column_id) " + 
            " where t_fe.table_id=?1 and t_fe.component_version_id=?2 and t_fe.menu_id=?3 " +
            " order by t_fe.show_order ", nativeQuery=true)
    public List<Object[]> getDefineColumn(String tableId, String componentVersionId, String menuId);
    
    /**
     * <p>标题: deleteByFk</p>
     * <p>描述: 清除界面字段配置</p>
     * @param  tableId
     *         表ID
     * @param  componentVersionId
     *         模块ID
     * @return void    返回类型   
     * @throws
     */
    @Transactional
    @Modifying
    @Query("delete AppFormElement t where t.tableId=?1 and t.componentVersionId=?2 and t.menuId=?3")
    public void deleteByFk(String tableId, String componentVersionId, String menuId);
    
    /**
     * qiucs 2013-9-5 
     * <p>标题: findByFk</p>
     * <p>描述: </p>
     * @param  tableId
     * @param  componentVersionId
     * @return List<AppFormElement>    返回类型   
     * @throws
     */
    @Query("from AppFormElement t where t.tableId=?1 and t.componentVersionId=?2 and t.menuId=?3")
    public List<AppFormElement> findByFk(String tableId, String componentVersionId, String menuId);

    /**
     * qiucs 2013-11-27 
     * <p>描述: 根据模块ID删除配置</p>
     * @param  tableId    设定参数   
     */
    @Transactional
    @Modifying
    @Query("delete AppFormElement t where t.tableId=?1")
    public void deleteByTableId(String tableId);

    /**
     * qiucs 2013-11-27 
     * <p>描述: 根据表ID删除配置</p>
     * @param  tableId    设定参数   
     */
    @Transactional
    @Modifying
    @Query("delete AppFormElement t where t.componentVersionId=?1")
    public void deleteByComponentVersionId(String componentVersionId);

    /**
     * qiucs 2013-11-27 
     * <p>描述: 根据菜单ID删除配置</p>
     * @param  tableId    设定参数   
     */
    @Transactional
    @Modifying
    @Query("delete AppFormElement t where t.menuId=?1")
    public void deleteByMenuId(String menuId);
    
    /**
     * qiucs 2013-12-2 
     * <p>描述: 根据字段ID删除列表字段配置</p>
     * @param  columnId    设定参数   
     */
    @Transactional
    @Modifying
    @Query("delete AppFormElement t where t.columnId=?1")
    public void deleteByColumnId(String columnId);
}
