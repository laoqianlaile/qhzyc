package com.ces.config.dhtmlx.dao.appmanage;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.entity.appmanage.Module;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

public interface ModuleDao extends StringIDDao<Module> {

    /**
     * qiucs 2014-3-10
     * <p>描述: 根据构件分类查找最大显示顺序</p>
     */
    @Query("select max(showOrder) from Module where componentAreaId=?1")
    public Integer getMaxShowOrderByComponentAreaId(String componentAreaId);

    /**
     * qiucs 2013-9-9
     * <p>标题: getShowOrderById</p>
     * <p>描述: 根据ID获取显示顺序</p>
     * 
     * @param id
     * @return Integer 返回类型
     */
    @Query("SELECT showOrder from Module WHERE ID=?1")
    public Integer getShowOrderById(String id);

    /**
     * qiucs 2013-9-9
     * <p>标题: updateShowOrderById</p>
     * <p>描述: 根据ID更新显示顺序</p>
     * 
     * @param id
     * @param increaseNum 设定参数
     * @return void 返回类型
     */
    @Transactional
    @Modifying
    @Query("UPDATE Module SET showOrder=(showOrder + ?2) WHERE id=?1")
    public void updateShowOrderById(String id, Integer increaseNum);

    /**
     * qiucs 2013-9-9
     * <p>标题: upShowOrder</p>
     * <p>描述: 显示顺序批量更新(begin, end)开区间</p>
     * 
     * @param componentAreaId
     * @param begin
     * @param end
     * @param increaseNum 设定参数
     * @return void 返回类型
     * @throws
     */
    @Transactional
    @Modifying
    @Query("UPDATE Module SET showOrder = (showOrder + ?4) WHERE componentAreaId=?1 AND showOrder > ?2 AND showOrder < ?3")
    public void batchUpdateShowOrder(String componentAreaId, Integer begin, Integer end, Integer increaseNum);

    /**
     * 查询表关系数目
     * 
     * @param tableId
     * @return
     */
    @Query(value = "select count(*) from Module T where T.areaLayout like '%?%?%'")
    public Long getTotalTableRelation(String tableId, String tableId2);

    /**
     * qiucs 2013-10-28
     * <p>描述: 根据ID获取构件版本ID</p>
     * 
     * @param id
     * @return String 返回类型
     */
    @Query("SELECT T.componentVersionId FROM Module T WHERE T.id=?1")
    public String getComponentVersionId(String id);

    /**
     * qiucs 2013-11-28
     * <p>描述: 把表ID置空</p>
     * 
     * @param tableId 设定参数
     */
    @Modifying
    @Transactional
    @Query("update Module set treeId=null where treeId=?1 and type='3' and templateType = '1'")
    public void updateByTreeId(String treeId);

    /**
     * <p>描述: 根据构件版本ID获取模块</p>
     * 
     * @param componentVersionId 构件版本ID
     * @return Module 返回类型
     */
    public Module findByComponentVersionId(String componentVersionId);

    /**
     * qiucs 2014-2-10
     * <p>描述: 根据构件版本ID获取模块ID</p>
     * 
     * @param componentVersionId
     */
    @Query("select id from Module t where t.componentVersionId=?1")
    public String getIdByComponentVersionId(String componentVersionId);

    /**
     * wl 2014-4-29
     * <p>描述: 根据模块名称获取模块ID</p>
     * 
     * @param name 模块名称
     */
    @Query("select id from Module t where t.name=?1")
    public String getIdByName(String name);

    /**
     * qiucs 2014-2-10
     * <p>描述: 根据构件分类ID获取模块ID</p>
     * 
     * @param componentVersionId
     */
    @Query("from Module t where t.componentAreaId=?1 order by t.showOrder")
    public List<Module> getByComponentAreaId(String componentAreaId);

    /**
     * qiucs 2014-3-20
     * <p>描述: 根据版本构件ID获取树ID</p>
     */
    @Query("select t.treeId from Module t where t.type='3' and t.componentVersionId=?1")
    public String getTreeIdByComponentVersionId(String componentVersionId);

    /**
     * qiucs 2014-9-23
     * <p>描述: 获取与表ID相关的模块</p>
     */
    @Query(value = "select * from t_xtpz_module t where t.table1_id=?1 or "
            + "(t.template_type='1' and exists(select 1 from t_xtpz_tree_define tt where tt.root_id=t.tree_id and (tt.area_layout like ?1))) ", nativeQuery = true)
    public List<Module> getWorkflowModuleByTableId(String tableId);
    
    /**
     * qiucs 2014-12-10 
     * <p>描述: 根据表ID获取自定义构件</p>
     */
    @Query("from Module m1 where m1.type='4' and m1.areaLayout like ?1 ")
    public List<Module> getByTableId(String tableId);
    /**
     * qiucs 2014-12-10 
     * <p>描述: 根据表ID和逻辑表编码获取自定义构件</p>
     */
    @Query(value="select m1.* from t_xtpz_module m1 where m1.type='4' and m1.area_layout like ?1 " +
                 "union all " +
                 "select m2.* from t_xtpz_module m2 where m2.type='5' and m2.area_layout like ?2", nativeQuery=true)
    public List<Module> getByTableId(String tableId, String logicTableCode);
    
    /**
     * 批量修改逻辑表组编码
     * @param oldLogicGroupCode 旧的逻辑表组编码
     * @param newLogicGroupCode 新的逻辑表组编码
     */
    @Transactional
    @Modifying
    @Query("update Module set logicTableGroupCode=?2 WHERE logicTableGroupCode=?1")
    public void batchUpdateLogicGroupCode(String oldLogicGroupCode, String newLogicGroupCode);
    
    /**
     * qiucs 2015-2-3 下午2:16:49
     * <p>描述: 指定table(tableId or logicTableCode)是否在指定的构件中 </p>
     * @return Module
     */
    @Query("from Module m1 where m1.id=?1 and m1.areaLayout like ?2 ")
    public Module getByIdAndTableId(String moduleId, String table);
    
    /**
     * qiucs 2015-2-3 下午2:16:49
     * <p>描述: 指定table(tableId or logicTableCode)是否在指定的构件中 </p>
     * @return Module
     */
    @Query("select m1.componentVersionId from Module m1 where m1.areaLayout like ?1 ")
    public List<String> getCVIdsByTableId(String tableId);
    
    /**
     * 获取树构件中使用到该表的构件版本IDs
     * @return List<String>
     */
    @Query(value=" select md.component_version_id from t_xtpz_module md " +
            " join t_xtpz_tree_define td on td.root_id=md.tree_id " +
            " join t_xtpz_physical_group_relation pg on pg.group_id=td.db_id " +
            " where md.type='3' and pg.table_id=?1", nativeQuery=true)
    public List<String> getCVIdsByTableIdOfTree(String tableId);
    
    /**
     * qiucs 2015-2-3 下午3:01:41
     * <p>描述: 统计树型构件中是否存在指定tableId </p>
     * @return long
     */
    @Query(value=" select count(md.id) from t_xtpz_module md " +
    		" join t_xtpz_tree_define td on td.root_id=md.tree_id " +
    		" join t_xtpz_physical_group_relation pg on pg.group_id=td.db_id " +
    		" where md.id=?1 and md.type='3' and pg.table_id=?2", nativeQuery=true)
    public Object countTreeNodeByIdAndTableId(String moduleId, String tableId);
    
    @Transactional
    @Modifying
    @Query("delete from Module where id=?1")
    public void deleteById(String id);
}



