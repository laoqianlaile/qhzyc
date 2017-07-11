package com.ces.config.dhtmlx.dao.appmanage;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.entity.appmanage.AppDefine;
import com.ces.config.utils.CommonUtil;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

public interface AppDefineDao extends StringIDDao<AppDefine> {

    /**
     * qiucs 2013-11-27 
     * <p>描述: 按自定义构件查询配置信息 </p>
     * @return List<AppDefine>    返回类型   
     * @throws
     */
    @Query(value="select t.id, t.component_version_id, t.name, d.searched, d.columned, d.sorted, " +
            "s.filtered, s.grid_buttoned, s.formed, s.form_buttoned, s.reported, cv.version " +
            "from t_xtpz_module t " +
            "inner join t_xtpz_component_version cv on t.component_version_id=cv.id " +
            "left join t_xtpz_app_define d on (d.table_id=?1 and d.component_version_id=t.component_version_id and d.menu_id=?2 and d.user_id=?3) " +
            "left join t_xtpz_app_define s on (s.table_id=?1 and s.component_version_id=t.component_version_id and d.menu_id=?2  and s.user_id='" + CommonUtil.SUPER_ADMIN_ID + "') " +
            "left join t_xtpz_component_area ca on t.component_area_id=ca.id " +
            "order by ca.show_order,t.show_order", nativeQuery=true)
    public List<Object[]> findModuleByFk(String tableId, String menuId, String userId);

    /**
     * qiucs 2014-12-19 下午5:13:32
     * <p>描述: 按菜单查询配置信息 </p>
     * @return List<Object[]>
     */
    @Query(value="select d.id, d.component_version_id, cast('' as character varying ) as name, d.searched, d.columned, d.sorted, " +
            "s.filtered, s.grid_buttoned, s.formed, s.form_buttoned, s.reported, cv.version " +
            "from t_xtpz_app_define d " +
            "left join t_xtpz_component_version cv on d.component_version_id=cv.id " +
            "left join t_xtpz_app_define s on (s.table_id=?1 and s.component_version_id=d.component_version_id and s.menu_id=?2  and s.user_id='" + CommonUtil.SUPER_ADMIN_ID + "') " +
            "where d.table_id=?1 and d.menu_id=?2 and d.user_id=?3", nativeQuery=true)
    public List<Object[]> findMenuByFk(String tableId, String menuId, String userId);

    /**
     * qiucs 2014-12-19 下午5:13:32
     * <p>描述: 按菜单查询配置信息 </p>
     * @return List<Object[]>
     */
    @Query(value="select d.id, d.component_version_id,cast('' as character varying ) as name, d.searched, d.columned, d.sorted, " +
            "s.filtered, s.grid_buttoned, s.formed, s.form_buttoned, s.reported " +
            "from t_xtpz_app_define d " +
            "left join t_xtpz_app_define s on (s.table_id=?1 and s.component_version_id=d.component_version_id and s.menu_id=?2  and s.user_id='" + CommonUtil.SUPER_ADMIN_ID + "') " +
            "where d.table_id=?1 and d.menu_id='-1' and d.user_id=?3 and d.component_version_id in(" +
            "'applyfor','todo','hasdone','complete','toread','hasread'" +
            ")", nativeQuery=true)
    public List<Object[]> findCoflowByFk(String tableId, String userId);

    /**
     * qiucs 2012-12-10 
     * <p>描述: 根据表ID、模块ID查询AppDefine</p>
     */
    @Query("from AppDefine t where t.tableId=?1 and t.componentVersionId=?2 and t.menuId=?3 and t.userId=?4")
    public AppDefine findByFk(String tableId, String componentVersionId, String menuId, String userId);

    /**
     * qiucs 2012-12-10 
     * <p>描述: 根据表ID、用户ID查询AppDefine</p>
     */
    @Query("from AppDefine t where t.tableId=?1 and t.userId=?2")
    public List<AppDefine> findByTableIdAndUserId(String tableId, String userId);

    /**
     * qiucs 2013-11-27 
     * <p>描述: 根据模块ID删除配置</p>
     * @param  tableId    设定参数   
     */
    @Transactional
    @Modifying
    @Query("delete AppDefine where tableId=?1")
    public void deleteByTableId(String tableId);

    /**
     * qiucs 2013-11-27 
     * <p>描述: 根据表ID删除配置</p>
     * @param  tableId    设定参数   
     */
    @Transactional
    @Modifying
    @Query("delete AppDefine where componentVersionId=?1")
    public void deleteByComponentVersionId(String componentVersionId);

    /**
     * qiucs 2014-12-10 
     * <p>描述: 根据菜单ID删除配置</p>
     * @param  menuId    设定参数   
     */
    @Transactional
    @Modifying
    @Query("delete AppDefine where menuId=?1")
    public void deleteByMenuId(String menuId);

    @Transactional
    @Modifying
    @Query("delete AppDefine where id=?1")
    public void deleteById(String id);
}
