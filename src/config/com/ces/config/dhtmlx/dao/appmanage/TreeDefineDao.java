package com.ces.config.dhtmlx.dao.appmanage;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.entity.appmanage.TreeDefine;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

public interface TreeDefineDao  extends StringIDDao<TreeDefine>{

	
	/**
	 * 字段关联列表查询
	 * @return
	 */
	@Query(value="select t.id,t.name,t.type from t_xtpz_tree_define t where t.parent_id=?1 order by t.show_order", nativeQuery=true)
	public List<Object[]> getArchiveTreesList(String parId);
	
	/**
     * 根节点查询
     * @return
     */
    @Query(value="select t.id,t.name,t.type from t_xtpz_tree_define t where t.parent_id='-1' and type='0' order by t.show_order", nativeQuery=true)
    public List<Object[]> findRootTrees();
	
	/**
	 * qiucs 2013-8-26 
	 * <p>标题: getMaxShowOrderByParentId</p>
	 * <p>描述: 根据父节点ID获取最大显示顺序值</p>
	 * @param  parentId
	 * @return Integer    返回类型   
	 * @throws
	 */
	@Query("select max(t.showOrder) from TreeDefine t where t.parentId=?1")
	public Integer getMaxShowOrderByParentId(String parentId);
	
	/**
     * qiucs 2013-8-27 
     * <p>标题: getShowOrderById</p>
     * <p>描述: 根据ID获取显示顺序</p>
     * @param  id
     * @return Integer    返回类型   
     * @throws
     */
    @Query("SELECT showOrder from TreeDefine WHERE ID=?1")
    public Integer getShowOrderById(String id);
    
    /**
     * qiucs 2013-8-27 
     * <p>标题: upShowOrder</p>
     * <p>描述: 显示顺序批量更新(begin, end)开区间</p>
     * @param  parentId
     * @param  begin
     * @param  end    
     * @param  increaseNum    设定参数   
     * @return void    返回类型   
     * @throws
     */
    @Transactional
    @Modifying
    @Query("UPDATE TreeDefine SET showOrder = (showOrder + ?4) WHERE parentId=?1 AND showOrder > ?2 AND showOrder < ?3")
    public void batchUpdateShowOrder(String parentId, Integer begin, Integer end, Integer increaseNum);
    
    /**
     * qiucs 2013-8-27 
     * <p>标题: updateShowOrderById</p>
     * <p>描述: 根据ID更新显示顺序</p>
     * @param  id
     * @param  increaseNum    设定参数   
     * @return void    返回类型   
     * @throws
     */
    @Transactional
    @Modifying
    @Query("UPDATE TreeDefine SET showOrder=(showOrder + ?2) WHERE id=?1")
    public void updateShowOrderById(String id, Integer increaseNum);
    
    @Query("FROM TreeDefine T WHERE T.parentId=?1 ORDER BY T.showOrder")
    public List<TreeDefine> getByParentId(String parentId);
    
    @Query("FROM TreeDefine T WHERE T.parentId=?1 and T.nodeRule=?2 ORDER BY T.showOrder")
    public List<TreeDefine> getByParentIdAndNodeRule(String parentId, String nodeRule);

	/**
	 * 查询表关系数目
	 * @param tableId
	 * @return
	 */	
	@Query(value="select count(*) from TreeDefine t where t.tableId=?1 or t.tableId=?2")
	public Long getTotalTableRelation(String tableId, String relateTableId);
	
    /**
     * 根据父节点ID获取子节点数目
     * @param parentId
     * @return
     * @author Administrator
     * @date 2013-10-28  09:52:46
     */
    @Query("select count(id) from TreeDefine where parentId=?1")
    public Long countByParentId(String parentId);
    
    /**
     * qiucs 2013-10-30 
     * <p>描述: 根据父节点获取动态节点列表</p>
     * @param  parentId
     * @return List<TreeDefine>    返回类型   
     */
    @Query("FROM TreeDefine T WHERE T.parentId=?1 AND T.dynamicFromId=?2 AND T.dynamic='1'")
    public List<TreeDefine> getDynamicNodes(String parentId, String dynamicFromId);
    
    /**
     * qiucs 2013-10-30 
     * <p>描述: 查找用来生成动态节点(业务表)的所有记录(oracle)</p>
     * @param  tableId
     * @return List<TreeDefine>    返回类型   
     */
    @Query(value="SELECT * FROM " +
    		"(SELECT T.* FROM T_XTPZ_TREE_DEFINE T START WITH T.PARENT_ID='-1' CONNECT BY PRIOR T.ID = T.PARENT_ID) TMP " +
    		"WHERE TMP.TABLE_ID=?1 AND TMP.NODE_RULE='1' AND TMP.DATA_SOURCE='0'", nativeQuery=true)
    public List<TreeDefine> getRuleTreeNodesOfOracle(String tableId);
    
    /**
     * qiucs 2013-11-5 
     * <p>描述: 查找用来生成动态节点(业务表)的所有记录(sqlserver)</p>
     * @param  tableId
     * @return List<TreeDefine>    返回类型   
     * @throws
     */
    @Query(value="WITH RTU_1 AS (SELECT * FROM T_XTPZ_TREE_DEFINE )," +
            "RTU_2 AS (SELECT * FROM RTU_1 WHERE PARENT_ID='-1' " +
            "UNION ALL " +
            "SELECT RTU_1.* FROM RTU_2 " +
            "INNER JOIN RTU_1 ON RTU_2.ID=RTU_1.PARENT_ID) " +
            "SELECT * FROM RTU_2  WHERE TABLE_ID=?1 AND NODE_RULE='1' AND DATA_SOURCE='0'", nativeQuery=true)
    public List<TreeDefine> getRuleTreeNodesOfSqlserver(String tableId);
    
    /**
     * qiucs 2013-12-26 
     * <p>描述: 查找用来生成动态节点(编码表)的所有记录(oracle)</p>
     * @return List<TreeDefine>    返回类型   
     */
    @Query(value="SELECT * FROM " +
            "(SELECT T.* FROM T_XTPZ_TREE_DEFINE T START WITH T.PARENT_ID='-1' CONNECT BY PRIOR T.ID = T.PARENT_ID) TMP " +
            "WHERE TMP.NODE_RULE='1' AND TMP.DATA_SOURCE='1'", nativeQuery=true)
    public List<TreeDefine> getCodeRuleTreeNodesOfOracle();
    
    /**
     * qiucs 2013-12-26
     * <p>描述: 查找用来生成动态节点(编码表)的所有记录(sqlserver)</p>
     * @return List<TreeDefine>    返回类型   
     * @throws
     */
    @Query(value="WITH RTU_1 AS (SELECT * FROM T_XTPZ_TREE_DEFINE )," +
            "RTU_2 AS (SELECT * FROM RTU_1 WHERE PARENT_ID='-1' " +
            "UNION ALL " +
            "SELECT RTU_1.* FROM RTU_2 " +
            "INNER JOIN RTU_1 ON RTU_2.ID=RTU_1.PARENT_ID) " +
            "SELECT * FROM RTU_2  WHERE NODE_RULE='1' AND DATA_SOURCE='1'", nativeQuery=true)
    public List<TreeDefine> getCodeRuleTreeNodesOfSqlserver();
    
    /**
     * qiucs 2014-11-18 
     * <p>描述: 根据父ID查找</p>
     */
    public TreeDefine findByParentId(String parentId);
    
    /**
     * 根据树根节点名称获取树根节点
     * 
     * @param name 树根节点名称
     * @return TreeDefine 返回类型
     */
    @Query("from TreeDefine where parentId='-1' and name=?")
    public TreeDefine getTreeByName(String name);
    
    /**
     * qiucs 2013-11-28 
     * <p>描述: 把表ID置空</p>
     * @param  tableId    设定参数   
     */
    @Modifying
    @Transactional
    @Query("update TreeDefine set dbId=null, tableId=null where dbId=?1 and type='2' ")
    public void updateByDbId(String tableId);
    
    /**
     * qiucs 2013-11-28 
     * <p>描述: 把表ID置空</p>
     * @param  tableId    设定参数   
     */
    @Modifying
    @Transactional
    @Query("update TreeDefine set dbId=null, tableId=null where tableId=?1 and type!='0' and type!='1'")
    public void updateByTableId(String tableId);
    
    /**
     * 更改表节点的表后，更新该表节点下所有节点的dbId和tableId
     * @date 2013-12-18  09:40:40
     */
    @Modifying
    @Transactional
    @Query("update TreeDefine set dbId=null, tableId=?1 where parentIds like ?2")
    public void updateTableId(String tableId, String parentIds);
    
    /**
     * 更新columnNames和columnValues的字段值
     * @param oldColumnNames
     * @param newColumnNames
     * @param oldColumnValues
     * @param newColumnValues
     * @param parentIds
     * @date 2013-12-18  09:40:35
     */
    @Modifying
    @Transactional
    @Query(value="update t_xtpz_tree_define set column_names=replace(column_names,?1,?2), column_values=replace(column_values,?3,?4) where parent_ids like ?5", nativeQuery=true)
    public void updateColumnNamesAndColumnValues(String oldColumnNames, String newColumnNames, String oldColumnValues, String newColumnValues, String parentIds);
    
    /**
     * qiucs 2014-3-19 
     * <p>描述: 把动态节点转化为普通节点</p>
     */
    @Modifying
    @Transactional
    @Query("update TreeDefine set dynamic='0', dynamicFromId='', remark='' where dynamicFromId=?1")
    public void updateDynamicNodes(String id);
    
    /**
     * 根据树节点ID获取使用这颗树的所有构件的IDs
     * @param rootId 树根节点ID
     * @return List<String>
     */
    @Query(value="select cv.id from t_xtpz_module m, t_xtpz_component_version cv where m.component_version_id=cv.id and m.tree_id=?", nativeQuery=true)
    public List<String> getComponentVersionIdsByRootId(String rootId);
    
    /**
     * qiucs 2014-9-29 
     * <p>描述: 根据根节点统计有配置显示数量的节点</p>
     * @param  treeId
     * @return Long    返回类型   
     * @throws
     */
    @Query(value="select count(*) from TreeDefine t where t.rootId=?1 and t.showNodeCount='1'")
    public Long countShowNodeCount(String treeId);
    
    /**
     * 获取某棵树下某种类型的节点
     * @param rootId 树根节点ID
     * @param type 树节点类型
     * @return List<TreeDefine>
     */
    public List<TreeDefine> getByRootIdAndType(String rootId, String type);
}
