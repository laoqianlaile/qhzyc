package com.ces.config.dhtmlx.dao.construct;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.entity.component.ComponentVersion;
import com.ces.config.dhtmlx.entity.construct.ConstructDetail;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

/**
 * 组合构件中构件和预留区绑定关系Dao
 * 
 * @author wanglei
 * @date 2013-09-27
 */
public interface ConstructDetailDao extends StringIDDao<ConstructDetail> {

    /**
     * 根据组合构件绑定关系ID获取预留区和构件绑定关系列表
     * 
     * @param constructId 组合构件绑定关系ID
     * @return List<Object[]>
     */
    @Query(value = "select cd.id,cd.button_code,cd.button_display_name,crz.alias as reserve_zone_alias,c.alias as component_alias,cv.version as component_version, cd.component_version_id, cd.reserve_zone_id, cd.button_type, cd.parent_button_code, cd.button_source, cd.tree_node_property, cd.show_order, cd.tree_node_type, cd.position from"
            + " t_xtpz_construct_detail cd left join t_xtpz_component_version cv"
            + " on cd.component_version_id = cv.id"
            + " left join t_xtpz_component_reserve_zone crz"
            + " on cd.reserve_zone_id = crz.id"
            + " left join t_xtpz_component c"
            + " on cv.component_id = c.id" + " where cd.construct_id=? order by cd.is_common_reserve_zone desc, crz.show_order, cd.show_order", nativeQuery = true)
    public List<Object[]> getConstructInfoByConstructId(String constructId);

    /**
     * 根据组合构件绑定关系ID和预留区ID获取预留区和构件绑定关系列表
     * 
     * @param constructId 组合构件绑定关系ID
     * @param reserveZoneId 预留区ID
     * @return List<Object[]>
     */
    @Query(value = "select cd.id,cd.button_code,cd.button_display_name,crz.alias as reserve_zone_alias,c.alias as component_alias,cv.version as component_version, cd.component_version_id, cd.reserve_zone_id, cd.button_type, cd.parent_button_code, cd.button_source, cd.tree_node_property, cd.show_order, cd.tree_node_type, cd.position from"
            + " t_xtpz_construct_detail cd left join t_xtpz_component_version cv"
            + " on cd.component_version_id = cv.id"
            + " left join t_xtpz_component_reserve_zone crz"
            + " on cd.reserve_zone_id = crz.id"
            + " left join t_xtpz_component c"
            + " on cv.component_id = c.id" + " where cd.construct_id=? and crz.id=? order by cd.is_common_reserve_zone desc, crz.name, cd.show_order", nativeQuery = true)
    public List<Object[]> getConstructInfos(String constructId, String reserveZoneId);

    /**
     * 根据组合构件绑定关系ID和预留区ID获取预留区和构件绑定关系列表
     * 
     * @param constructId 组合构件绑定关系ID
     * @param treeNodeType 树节点类型
     * @return List<Object[]>
     */
    @Query(value = "select cd.id,cd.button_code,cd.button_display_name,'' as reserve_zone_alias,c.alias as component_alias,cv.version as component_version, cd.component_version_id, cd.reserve_zone_id, cd.button_type, cd.parent_button_code, cd.button_source, cd.tree_node_property, cd.show_order, cd.tree_node_type, cd.position from"
            + " t_xtpz_construct_detail cd left join t_xtpz_component_version cv"
            + " on cd.component_version_id = cv.id"
            + " left join t_xtpz_component c"
            + " on cv.component_id = c.id"
            + " where cd.construct_id=? and cd.reserve_zone_id='TREE' and cd.tree_node_type=? and cd.tree_node_property <> 'ThirdParty' order by cd.is_common_reserve_zone desc, cd.show_order", nativeQuery = true)
    public List<Object[]> getConstructInfosOfTree(String constructId, String treeNodeType);

    /**
     * 根据组合构件绑定关系ID和预留区ID获取预留区和构件绑定关系列表
     * 
     * @param constructId 组合构件绑定关系ID
     * @param treeNodeType 树节点类型
     * @return List<Object[]>
     */
    @Query(value = "select cd.id,cd.button_code,cd.button_display_name,'' as reserve_zone_alias,c.alias as component_alias,cv.version as component_version, cd.component_version_id, cd.reserve_zone_id, cd.button_type, cd.parent_button_code, cd.button_source, cd.tree_node_property, cd.show_order, cd.tree_node_type, cd.position from"
            + " t_xtpz_construct_detail cd left join t_xtpz_component_version cv"
            + " on cd.component_version_id = cv.id"
            + " left join t_xtpz_component c"
            + " on cv.component_id = c.id"
            + " where cd.construct_id=? and cd.reserve_zone_id='TREE' and cd.tree_node_property='ThirdParty' order by cd.is_common_reserve_zone desc, cd.show_order", nativeQuery = true)
    public List<Object[]> getCommonConstructInfosOfTree(String constructId);

    /**
     * 获取预设的公用预留区和构件绑定关系列表
     * 
     * @param reserveZoneId 预留区ID
     * @return List<Object[]>
     */
    @Query(value = "select cd.id,cd.button_code,cd.button_display_name,crz.alias as reserve_zone_alias,c.alias as component_alias,cv.version as component_version, cd.component_version_id, cd.reserve_zone_id, cd.button_type, cd.parent_button_code, cd.button_source, cd.tree_node_property, cd.show_order, cd.tree_node_type, cd.position from"
            + " t_xtpz_construct_detail cd left join t_xtpz_component_version cv"
            + " on cd.component_version_id = cv.id"
            + " left join t_xtpz_component_reserve_zone crz"
            + " on cd.reserve_zone_id = crz.id"
            + " left join t_xtpz_component c"
            + " on cv.component_id = c.id" + " where cd.construct_id is null and crz.id=? order by cd.is_common_reserve_zone desc, crz.name, cd.show_order", nativeQuery = true)
    public List<Object[]> getConstructInfosOfCommonBinding(String reserveZoneId);

    /**
     * 根据组合构件绑定关系ID和预留区类型获取预留区和构件绑定关系列表
     * 
     * @param constructId 组合构件绑定关系ID
     * @param isCommonReserveZone 是否使用公共预留区
     * @return List<ConstructDetail>
     */
    @Query("from ConstructDetail where constructId=? and isCommonReserveZone=?")
    public List<ConstructDetail> getConstructDetails(String constructId, String isCommonReserveZone);

    /**
     * 根据组合构件绑定关系ID获取预留区和构件绑定关系列表
     * 
     * @param constructId 组合构件绑定关系ID
     * @return List<ConstructDetail>
     */
    public List<ConstructDetail> getByConstructId(String constructId);

    /**
     * 获取公用预留区和构件绑定关系列表
     * 
     * @return List<ConstructDetail>
     */
    @Query("from ConstructDetail where constructId is null")
    public List<ConstructDetail> getOfCommonBinding();

    /**
     * 根据预留区ID获取预留区和构件绑定关系列表
     * 
     * @param reserveZoneId 预留区ID
     * @return List<ConstructDetail>
     */
    @Query("from ConstructDetail where reserveZoneId=? order by showOrder")
    public List<ConstructDetail> getByReserveZoneId(String reserveZoneId);

    /**
     * 根据预留区ID获取预留区和构件绑定关系列表
     * 
     * @param reserveZoneId 预留区ID
     * @return List<ConstructDetail>
     */
    @Query("from ConstructDetail where reserveZoneId=? and (constructId is null or constructId='') order by showOrder")
    public List<ConstructDetail> getByReserveZoneIdOfCommonBinding(String reserveZoneId);

    /**
     * 根据构件版本ID获取预留区和构件绑定关系列表
     * 
     * @param componentVersionId 构件版本ID
     * @return List<ConstructDetail>
     */
    public List<ConstructDetail> getByComponentVersionId(String componentVersionId);

    /**
     * 根据根菜单ID获取预留区和构件绑定关系ID
     * 
     * @param rootMenuId 根菜单ID
     * @return List<String>
     */
    @Query(value = "select cd.id from t_xtpz_menu m, t_xtpz_component_version cv, t_xtpz_component c, t_xtpz_construct co, t_xtpz_construct_detail cd"
            + " where m.component_version_id=cv.id and cv.component_id=c.id and cv.id=co.component_version_id"
            + " and co.id=cd.construct_id and c.type='9' and m.root_menu_id=?", nativeQuery = true)
    public List<String> getByRootMenuId(String rootMenuId);

    /**
     * 获取预留区和构件绑定关系的最大显示顺序
     * 
     * @param constructId 组合构件绑定关系ID
     * @param reserveZoneId 预留区ID
     * @return Integer
     */
    @Query("select max(showOrder) from ConstructDetail where constructId=? and reserveZoneId=?")
    public Integer getMaxShowOrder(String constructId, String reserveZoneId);

    /**
     * 获取预留区和构件绑定关系的最大显示顺序
     * 
     * @param constructId 组合构件绑定关系ID
     * @param reserveZoneId 预留区ID
     * @param parentButtonCode 按钮组
     * @return Integer
     */
    @Query("select max(showOrder) from ConstructDetail where constructId=? and reserveZoneId=? and parentButtonCode=?")
    public Integer getMaxShowOrderOfP(String constructId, String reserveZoneId, String parentButtonCode);

    /**
     * 获取预留区和构件绑定关系的最大显示顺序
     * 
     * @param constructId 组合构件绑定关系ID
     * @param reserveZoneId 预留区ID
     * @param treeNodeType 树节点类型
     * @return Integer
     */
    @Query("select max(showOrder) from ConstructDetail where constructId=? and reserveZoneId=? and treeNodeType=?")
    public Integer getMaxShowOrder(String constructId, String reserveZoneId, String treeNodeType);

    /**
     * 获取预留区和构件绑定关系的最大显示顺序
     * 
     * @param constructId 组合构件绑定关系ID
     * @param reserveZoneId 预留区ID
     * @return Integer
     */
    @Query("select max(showOrder) from ConstructDetail where constructId is null and reserveZoneId=?")
    public Integer getMaxShowOrderOfCommonBinding(String reserveZoneId);

    /**
     * 获取显示顺序范围内的预留区和构件绑定关系（一级按钮或按钮组）
     * 
     * @param start 开始显示顺序
     * @param end 结束显示顺序
     * @param constructId 组合构件绑定关系ID
     * @param reserveZoneId 预留区ID
     * @return List<ConstructDetail>
     */
    @Query("from ConstructDetail where showOrder>=?1 and showOrder<=?2 and constructId=?3 and reserveZoneId=?4 and parentButtonCode is null")
    public List<ConstructDetail> getByShowOrder(Integer start, Integer end, String constructId, String reserveZoneId);

    /**
     * 获取显示顺序范围内的预留区和构件绑定关系（二级按钮）
     * 
     * @param start 开始显示顺序
     * @param end 结束显示顺序
     * @param constructId 组合构件绑定关系ID
     * @param reserveZoneId 预留区ID
     * @param parentButtonCode 按钮组编码
     * @return List<ConstructDetail>
     */
    @Query("from ConstructDetail where showOrder>=?1 and showOrder<=?2 and constructId=?3 and reserveZoneId=?4 and parentButtonCode=?5")
    public List<ConstructDetail> getByShowOrder(Integer start, Integer end, String constructId, String reserveZoneId, String parentButtonCode);

    /**
     * 获取显示顺序范围内的预留区和构件绑定关系（一级按钮或按钮组）
     * 
     * @param start 开始显示顺序
     * @param end 结束显示顺序
     * @param reserveZoneId 预留区ID
     * @return List<ConstructDetail>
     */
    @Query("from ConstructDetail where showOrder>=?1 and showOrder<=?2 and constructId is null and reserveZoneId=?3 and parentButtonCode is null")
    public List<ConstructDetail> getByShowOrderOfCommonBinding(Integer start, Integer end, String reserveZoneId);

    /**
     * 获取显示顺序范围内的预留区和构件绑定关系（二级按钮）
     * 
     * @param start 开始显示顺序
     * @param end 结束显示顺序
     * @param reserveZoneId 预留区ID
     * @param parentButtonCode 按钮组编码
     * @return List<ConstructDetail>
     */
    @Query("from ConstructDetail where showOrder>=?1 and showOrder<=?2 and constructId is null and reserveZoneId=?3 and parentButtonCode=?4")
    public List<ConstructDetail> getByShowOrderOfCommonBinding(Integer start, Integer end, String reserveZoneId, String parentButtonCode);

    /**
     * 获取某预留区和构件绑定关系
     * 
     * @param constructId 组合构件绑定关系ID
     * @param reserveZoneId 预留区ID
     * @return List<ConstructDetail>
     */
    public List<ConstructDetail> getByConstructIdAndReserveZoneIdOrderByShowOrderAsc(String constructId, String reserveZoneId);

    /**
     * 获取某预留区和构件绑定关系
     * 
     * @param buttonDisplayName 按钮显示名称
     * @param constructId 组合构件绑定关系ID
     * @param reserveZoneId 预留区ID
     * @return ConstructDetail
     */
    public ConstructDetail getByButtonDisplayNameAndConstructIdAndReserveZoneId(String buttonDisplayName, String constructId, String reserveZoneId);

    /**
     * 获取某预留区和构件绑定关系（预设）
     * 
     * @param buttonDisplayName 按钮显示名称
     * @param reserveZoneId 预留区ID
     * @return ConstructDetail
     */
    @Query("from ConstructDetail where constructId is null and buttonDisplayName=? and reserveZoneId=?")
    public ConstructDetail getByButtonDisplayNameAndReserveZoneIdOfCommonBinding(String buttonDisplayName, String reserveZoneId);

    /**
     * 获取某预留区和构件绑定关系
     * 
     * @param buttonCode 按钮编码
     * @param constructId 组合构件绑定关系ID
     * @param reserveZoneId 预留区ID
     * @return ConstructDetail
     */
    public ConstructDetail getByButtonCodeAndConstructIdAndReserveZoneId(String buttonCode, String constructId, String reserveZoneId);

    /**
     * 获取某预留区和构件绑定关系
     * 
     * @param buttonCode 按钮编码
     * @param reserveZoneId 预留区ID
     * @return ConstructDetail
     */
    @Query("from ConstructDetail where constructId is null and buttonCode=? and reserveZoneId=?")
    public ConstructDetail getByButtonCodeAndReserveZoneIdOfCommonBinding(String buttonCode, String reserveZoneId);
    
    /**
     * 获取某预留区和构件绑定关系
     * 
     * @param buttonCode 按钮编码
     * @return List<ConstructDetail>
     */
    public List<ConstructDetail> getByButtonCode(String buttonCode);

    /**
     * 获取构件绑定关系
     * 
     * @param componentVersionId 构件版本ID
     * @param id 绑定关系ID
     * @return List<ConstructDetail>
     */
    public List<ConstructDetail> getByComponentVersionIdAndIdNot(String componentVersionId, String id);

    /**
     * 获取按钮组
     * 
     * @param constructId 组合构件绑定关系ID
     * @param reserveZoneId 预留区ID
     * @return List<ConstructDetail>
     */
    @Query("from ConstructDetail where constructId=? and reserveZoneId=? and buttonType='1' order by showOrder")
    public List<ConstructDetail> getParentButtonCodesOfReserveZone(String constructId, String reserveZoneId);

    /**
     * 获取按钮组
     * 
     * @param reserveZoneId 预留区ID
     * @return List<ConstructDetail>
     */
    @Query("from ConstructDetail where constructId is null and reserveZoneId=? and buttonType='1' order by showOrder")
    public List<ConstructDetail> getParentButtonCodesOfReserveZoneOfCommonBinding(String reserveZoneId);

    /**
     * 获取按钮组
     * 
     * @param constructId 组合构件绑定关系ID
     * @return List<ConstructDetail>
     */
    @Query("from ConstructDetail where constructId=? and buttonType='1' order by showOrder")
    public List<ConstructDetail> getParentButtonCodes(String constructId);

    /**
     * 获取按钮组
     * 
     * @return List<ConstructDetail>
     */
    @Query("from ConstructDetail where constructId is null and buttonType='1' order by showOrder")
    public List<ConstructDetail> getParentButtonCodesOfCommonBinding();

    /**
     * 获取下拉按钮的子按钮
     * 
     * @param parentButtonCode 所属按钮组名称
     * @param constructId 组合构件绑定关系ID
     * @return List<ConstructDetail>
     */
    public List<ConstructDetail> getByParentButtonCodeAndConstructId(String parentButtonCode, String constructId);

    /**
     * 获取预设的公用预留区和构件绑定关系列表
     * 
     * @param constructId 组合构件绑定关系ID
     * @return List<ConstructDetail>
     */
    public List<ConstructDetail> getByConstructIdIsNull();

    /**
     * 获取预设的公用预留区和构件绑定关系列表
     * 
     * @param reserveZoneId 预留区ID
     * @return List<ConstructDetail>
     */
    @Query("from ConstructDetail where constructId is null and reserveZoneId=?")
    public List<ConstructDetail> getConstructDetailsOfCommonBinding(String reserveZoneId);

    /**
     * 获取预设的公用预留区中绑定的所有构件
     * 
     * @return List<ComponentVersion>
     */
    @Query("select distinct cv from ConstructDetail cd, ComponentVersion cv where cd.componentVersionId=cv.id and cd.constructId is null")
    public List<ComponentVersion> getComponentVersionsOfCommonBinding();

    /**
     * 获取选中的预置按钮
     * 
     * @param constructId 组合构件绑定关系ID
     * @param reserveZoneId 预留区ID
     * @return List<ConstructDetail>
     */
    @Query("from ConstructDetail where constructId=? and reserveZoneId=? and buttonSource='0' and buttonCode!='COMBOBOX_SEARCH'")
    public List<ConstructDetail> getCheckedDefaultButtons(String constructId, String reserveZoneId);

    /**
     * 获取预设的公用预留区中选中的预置按钮
     * 
     * @param reserveZoneId 预留区ID
     * @return List<ConstructDetail>
     */
    @Query("from ConstructDetail where constructId is null and reserveZoneId=? and buttonSource='0' and buttonCode!='COMBOBOX_SEARCH'")
    public List<ConstructDetail> getCheckedDefaultButtonsOfCommonBinding(String reserveZoneId);

    /**
     * 获取树预留区上节点绑定的构件
     * 
     * @param constructId 组合构件绑定关系ID
     * @param treeNodeType 树节点类型
     * @param treeNodeProperty 树节点属性
     * @return ConstructDetail
     */
    @Query("from ConstructDetail where constructId=? and treeNodeType=? and treeNodeProperty=?")
    public ConstructDetail getOfTreeNode(String constructId, String treeNodeType, String treeNodeProperty);

    /**
     * 获取树预留区上根节点绑定的构件
     * 
     * @param constructId 组合构件绑定关系ID
     * @return ConstructDetail
     */
    @Query("from ConstructDetail where constructId=? and treeNodeType='0'")
    public ConstructDetail getOfRootTreeNode(String constructId);

    /**
     * 获取树预留区上物理表组节点绑定的构件
     * 
     * @param constructId 组合构件绑定关系ID
     * @return List<ConstructDetail>
     */
    @Query("from ConstructDetail where constructId=? and treeNodeType='5'")
    public List<ConstructDetail> getOfRootPhysicalGroupNode(String constructId);

    /**
     * 删除树预留区上物理表组节点绑定的构件
     * 
     * @param constructId 组合构件绑定关系ID
     */
    @Transactional
    @Modifying
    @Query("delete from ConstructDetail where constructId=? and treeNodeType='5'")
    public void deleteAllPhysicalGroupNode(String constructId);

    /**
     * 获取按钮
     * 
     * @param constructId 组合构件绑定关系ID
     * @param reserveZoneId 预留区ID
     * @param buttonCode 按钮编码
     * @return ConstructDetail
     */
    @Query("from ConstructDetail where constructId=? and reserveZoneId=? and buttonCode=?")
    public ConstructDetail getButton(String constructId, String reserveZoneId, String buttonCode);

    /**
     * 获取按钮
     * 
     * @param reserveZoneId 预留区ID
     * @param buttonCode 按钮编码
     * @return ConstructDetail
     */
    @Query("from ConstructDetail where constructId is null and reserveZoneId=? and buttonCode=?")
    public ConstructDetail getButtonOfCommonBinding(String reserveZoneId, String buttonCode);

    /**
     * 获取二级按钮
     * 
     * @param constructId 组合构件绑定关系ID
     * @param reserveZoneId 预留区ID
     * @param parentButtonCode 按钮组编码
     * @return List<ConstructDetail>
     */
    @Query("from ConstructDetail where constructId=? and reserveZoneId=? and parentButtonCode=?")
    public List<ConstructDetail> getSecondButtonList(String constructId, String reserveZoneId, String parentButtonCode);

    /**
     * 获取二级按钮
     * 
     * @param reserveZoneId 预留区ID
     * @param parentButtonCode 按钮组编码
     * @return List<ConstructDetail>
     */
    @Query("from ConstructDetail where constructId is null and reserveZoneId=? and parentButtonCode=?")
    public List<ConstructDetail> getSecondButtonListOfCommonBinding(String reserveZoneId, String parentButtonCode);

    /**
     * 获取组合构件的按钮数
     * 
     * @param constructId 组合构件绑定关系ID
     * @return long
     */
    @Query("select count(id) from ConstructDetail t where t.constructId=?1")
    public long getConstructDetailCount(String constructId);

    /**
     * 获取预留区上按钮数
     * 
     * @param constructId 组合构件绑定关系ID
     * @param reserveZoneId 预留区ID
     * @return long
     */
    @Query("select count(id) from ConstructDetail t where t.constructId=?1 and t.reserveZoneId=?2")
    public long getConstructDetailCount(String constructId, String reserveZoneId);

    /**
     * qiucs 2014-10-22
     * <p>描述: 获取一级组装按钮</p>
     * 
     * @return List<ConstructDetail> 返回类型
     * @throws
     */
    /*
     * @Query(
     * "select cd from ConstructDetail cd, Construct ct, ComponentReserveZone cz where cd.constructId=ct.id and cd.reserveZoneId=cz.id"
     * +
     * " and ct.assembleComponentVersion.id=?1 and cz.name=?2 and cd.buttonType<>'2' order by cd.showOrder")
     */
    @Query(value = "select cd.* from T_XTPZ_CONSTRUCT_DETAIL cd, T_XTPZ_CONSTRUCT ct, T_XTPZ_COMPONENT_RESERVE_ZONE cz "
            + "where cd.CONSTRUCT_ID=ct.ID and cd.RESERVE_ZONE_ID=cz.ID" + " and ct.COMPONENT_VERSION_ID=?1 and cz.NAME=?2 ORDER BY cd.SHOW_ORDER", nativeQuery = true)
    public List<ConstructDetail> getAppConstructDetails(String cvId, String zoneName);

    @Transactional
    @Modifying
    @Query("delete ConstructDetail where id=?1")
    public void deleteById(String id);
}
