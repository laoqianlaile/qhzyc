package com.ces.config.dhtmlx.dao.component;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.entity.component.ComponentVersion;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

/**
 * 构件版本Dao
 * 
 * @author wanglei
 * @date 2013-07-22
 */
public interface ComponentVersionDao extends StringIDDao<ComponentVersion> {

    /**
     * 根据构件ID和构件版本号获取构件版本
     * 
     * @param componentId 构件ID
     * @param version 构件版本号
     * @return ComponentVersion
     */
    public ComponentVersion getByComponentIdAndVersion(String componentId, String version);

    /**
     * 根据构件名称和构件版本号获取构件版本
     * 
     * @param componentName 构件名称
     * @param version 构件版本号
     * @return ComponentVersion
     */
    public ComponentVersion getByComponentNameAndVersion(String componentName, String version);

    /**
     * 根据构件ID获取构件版本
     * 
     * @param componentId 构件ID
     * @return List<ComponentVersion>
     */
    public List<ComponentVersion> getByComponentId(String componentId);

    /**
     * 根据构件名称获取构件版本
     * 
     * @param componentName 构件名称
     * @return List<ComponentVersion>
     */
    public List<ComponentVersion> getByComponentName(String componentName);

    /**
     * 根据构件类型获取构件版本
     * 
     * @param componentName 构件名称
     * @return List<ComponentVersion>
     */
    public List<ComponentVersion> getByComponentType(String componentType);

    /**
     * 获取页面构件和组合构件
     * 
     * @param isSystemUsed 是否被本系统使用
     * @return List<Object[]>
     */
    @Query(value = "select cv.id, c.alias, cv.version from t_xtpz_component c, t_xtpz_component_version cv where c.id=cv.component_id and c.type in ('1', '9') and cv.is_system_used=?", nativeQuery = true)
    public List<Object[]> getPageCVInfoList(String isSystemUsed);

    /**
     * 获取页面构件和组合构件
     * 
     * @return List<Object[]>
     */
    @Query(value = "select cv.id, c.alias, cv.version from t_xtpz_component c, t_xtpz_component_version cv where c.id=cv.component_id and c.type in ('1', '9')", nativeQuery = true)
    public List<Object[]> getPageCVInfoList();

    /**
     * 根据构件分类ID获取构件版本
     * 
     * @param areaId 构件分类ID
     * @return List<ComponentVersion>
     */
    public List<ComponentVersion> getByAreaId(String areaId);

    /**
     * 根据构件分类ID获取组合构件版本
     * 
     * @param assembleAreaId 组合构件分类ID
     * @return List<ComponentVersion>
     */
    @Query("from ComponentVersion where component.type='9' and assembleAreaId=? order by component.name,version")
    public List<ComponentVersion> getComponentVersionListByAssembleAreaId(String assembleAreaId);

    /**
     * 获取构件的相关表信息
     * 
     * @param componentVersionId 构件版本ID
     * @return List<Object[]>
     */
    @Query(value = "select ct.name as tableName,cc.id,cc.name as columnName,cc.type,cc.length,cc.is_null,cc.default_value from t_xtpz_component_column cc, t_xtpz_component_table ct, t_xtpz_component_table_column ctc"
            + " where cc.id=ctc.column_id and ct.id=ctc.table_id" + " and ctc.component_version_id = ?", nativeQuery = true)
    public List<Object[]> getTablesByComponentVersionId(String componentVersionId);

    /**
     * 获取构件关联的公用构件列表
     * 
     * @param componentVersionId 构件版本ID
     * @return List<ComponentVersion>
     */
    @Query("select cv from ComponentVersion cv, CommonComponentRelation ccr where cv.id=ccr.commonComponentVersionId and ccr.componentVersionId=?")
    public List<ComponentVersion> getCommonComponentList(String componentVersionId);

    /**
     * 获取公用构件关联的构件列表
     * 
     * @param componentVersionId 构件版本ID
     * @return List<ComponentVersion>
     */
    @Query("select cv from ComponentVersion cv, CommonComponentRelation ccr where cv.id=ccr.componentVersionId and ccr.commonComponentVersionId=?")
    public List<ComponentVersion> getComponentList(String commonComponentVersionId);

    /**
     * 获取组合构件的基础构件下拉框信息
     * 
     * @param isSystemUsed 是否应用到本系统
     * @return List<Object[]>
     */
    @Query(value = "select cv.id, c.alias, cv.version from t_xtpz_component c, t_xtpz_component_version cv"
            + " where c.id=cv.component_id and c.type in ('1', '3', '4', '5', '6', '7', '8') and cv.is_system_used = ?", nativeQuery = true)
    public List<Object[]> getBaseComponentVersionCombo(String isSystemUsed);

    /**
     * 获取基础构件是页面构件或树构件或物理表构件的组合构件
     * 
     * @param isSystemUsed 是否应用到本系统
     * @return List<Object[]>
     */
    @Query(value = "select cv1.id, c.alias, cv1.version, cv1.button_use from t_xtpz_component c, t_xtpz_component_version cv1, t_xtpz_construct con"
            + " where cv1.id=con.component_version_id and c.id=cv1.component_id and cv1.is_system_used = ? and con.base_component_version_id in"
            + " (select cv2.id from t_xtpz_component_version cv2, t_xtpz_component com where cv2.component_id=com.id and com.type in ('1','3','4','5','6','7','8'))", nativeQuery = true)
    public List<Object[]> getAssembleComponentVersions(String isSystemUsed);

    /**
     * 获取组合构件
     * 
     * @param isSystemUsed 是否应用到本系统
     * @return List<Object[]>
     */
    @Query(value = "select cv1.id, c.alias, cv1.version from t_xtpz_component c, t_xtpz_component_version cv1, t_xtpz_construct con"
            + " where cv1.id=con.component_version_id and c.id=cv1.component_id and cv1.is_system_used = ? and con.base_component_version_id in"
            + " (select cv2.id from t_xtpz_component_version cv2, t_xtpz_component com where cv2.component_id=com.id and com.type = ?)", nativeQuery = true)
    public List<Object[]> getAssembleComponentVersionsByType(String isSystemUsed, String type);

    /**
     * 根据逻辑表组Code获取相关的构件
     * 
     * @param logicTableGroupCode 逻辑表组编码
     * @return List<ComponentVersion>
     */
    @Query("select cv from ComponentVersion cv, Module m where cv.id=m.componentVersionId and m.logicTableGroupCode=?")
    public List<ComponentVersion> getLogicComponentList(String logicTableGroupCode);

    /**
     * 获取构件对应构件版本的数量（大于1的）
     * 
     * @return List<Object[]>
     */
    @Query(value = "select c.id, count(cv.id) as cv_num from t_xtpz_component c, t_xtpz_component_version cv where c.id=cv.component_id group by c.id having count(cv.id)>1", nativeQuery = true)
    public List<Object[]> getCompCvCount();

    @Transactional
    @Modifying
    @Query("delete ComponentVersion where id=?1")
    public void deleteById(String id);

    /**
     * 获取所有的构件版本
     * 
     * @return List<Object[]>
     */
    @Query(value = "select c.id,c.code,c.name,c.alias,c.type,cv.id as cvId,cv.component_id,cv.version,cv.url,cv.remark,cv.area_id,cv.path," // 0~11
            + "cv.import_date,cv.views,cv.system_param_config,cv.is_package,cv.is_system_used,cv.package_time,cv.before_click_js," // 12~18
            + "cv.assemble_area_id,cv.button_use,cv.menu_use,cv.area_path,cv.assemble_area_path" // 19~23
            + " from t_xtpz_component c, t_xtpz_component_version cv where c.id=cv.component_id", nativeQuery = true)
    public List<Object[]> getAll();
}
