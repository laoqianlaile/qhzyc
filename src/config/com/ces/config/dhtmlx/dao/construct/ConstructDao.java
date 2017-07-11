package com.ces.config.dhtmlx.dao.construct;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.entity.component.ComponentVersion;
import com.ces.config.dhtmlx.entity.construct.Construct;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

/**
 * 组合构件绑定关系Dao
 * 
 * @author wanglei
 * @date 2013-08-26
 */
public interface ConstructDao extends StringIDDao<Construct> {

    /**
     * 根据组合构件版本ID获取组合构件绑定关系
     * 
     * @param assembleComponentVersionId 组合构件版本ID
     * @return Construct
     */
    @Query("from Construct where assembleComponentVersion.id = ?")
    public Construct getByAssembleComponentVersionId(String assembleComponentVersionId);

    /**
     * 根据基础构件版本ID获取组合构件绑定关系
     * 
     * @param baseComponentVersionId 基础构件版本ID
     * @return List<Construct>
     */
    public List<Construct> getByBaseComponentVersionId(String baseComponentVersionId);

    /**
     * 根据基础构件版本ID获取组合构件
     * 
     * @param baseComponentVersionId 基础构件版本ID
     * @return List<Construct>
     */
    @Query("select c.assembleComponentVersion from Construct c where c.baseComponentVersionId=?")
    public List<ComponentVersion> getAssembleByBaseComponentVersionId(String baseComponentVersionId);

    /**
     * 根据根菜单ID获取组合构件绑定关系ID
     * 
     * @param rootMenuId 根菜单ID
     * @return List<String>
     */
    @Query(value = "select co.id from t_xtpz_menu m, t_xtpz_component_version cv, t_xtpz_component c, t_xtpz_construct co"
            + " where m.component_version_id=cv.id and cv.component_id=c.id" + " and cv.id=co.component_version_id and c.type='9' and m.root_menu_id=?", nativeQuery = true)
    public List<String> getByRootMenuId(String rootMenuId);

    /**
     * qiucs 2014-12-19 下午1:19:51
     * <p>描述: 根据组合构件ID获取基础构件ID </p>
     * 
     * @return String
     */
    @Query(value = "select t.base_component_version_id from t_xtpz_construct t where t.component_version_id=?1", nativeQuery = true)
    public String getBaseComponentVersionId(String componentVersionId);

    /**
     * qiucs 2014-12-19 下午6:00:20
     * <p>描述: 判断组合构件对应的基础构件是否为树型构件 </p>
     * 
     * @return Object
     */
    @Query(value = "select count(t.id) from t_xtpz_construct t  " + "join t_xtpz_component_version cv on (t.base_component_version_id=cv.id) "
            + "join t_xtpz_component c on (c.id=cv.component_id and c.type='3') " + "where t.component_version_id=?1", nativeQuery = true)
    public Object isTreeComponent(String componentVersionId);

    /**
     * 根据逻辑表编码获取使用到该公用预留区的组合构件
     * 
     * @param logicTableCode 逻辑表编码
     * @return List<Object[]>
     */
    @Query(value = "select cv.id,com.alias,cv.version,con.id as constructId from t_xtpz_component_version cv, t_xtpz_component com, t_xtpz_construct con where cv.component_id=com.id and cv.id=con.component_version_id and cv.id in"
            + " (select c.component_version_id from t_xtpz_module m, t_xtpz_construct c where m.component_version_id=c.base_component_version_id"
            + " and m.type='5' and m.area_layout like ?)", nativeQuery = true)
    public List<Object[]> getAssembleComponentByLogicTableCode(String logicTableCode);

    /**
     * 根据构件分类ID和逻辑表编码获取使用到该公用预留区的组合构件
     * 
     * @param assembleAreaId 组合构件分类ID
     * @param logicTableCode 逻辑表编码
     * @return List<Object[]>
     */
    @Query(value = "select cv.id,com.alias,cv.version,con.id as constructId from t_xtpz_component_version cv, t_xtpz_component com, t_xtpz_construct con where cv.component_id=com.id and cv.id=con.component_version_id and cv.assemble_area_id = ? and cv.id in"
            + " (select c.component_version_id from t_xtpz_module m, t_xtpz_construct c where m.component_version_id=c.base_component_version_id"
            + " and m.type='5' and m.area_layout like ?)", nativeQuery = true)
    public List<Object[]> getAssembleComponentByLogicTableCode(String assembleAreaId, String logicTableCode);

    @Transactional
    @Modifying
    @Query("delete Construct where id=?1")
    public void deleteById(String id);

    /**
     * 获取所有的构件版本
     * 
     * @return List<Object[]>
     */
    @Query(value = "select c.id,c.component_version_id,c.base_component_version_id from t_xtpz_construct c", nativeQuery = true)
    public List<Object[]> getAll();
}
