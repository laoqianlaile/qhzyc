package com.ces.config.dhtmlx.dao.appmanage;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.entity.appmanage.PhysicalGroupRelation;
import com.ces.config.dhtmlx.entity.appmanage.PhysicalTableDefine;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

public interface PhysicalGroupRelationDao extends StringIDDao<PhysicalGroupRelation> {

    @Query("FROM PhysicalGroupRelation WHERE groupId=?1 order by showOrder")
    List<PhysicalGroupRelation> findByGroupId(String groupId);
    
    /**
     * qiujinwei 2014-12-02 
     * <p>标题: getIdByTableIdAndGroupId</p>
     * <p>描述: 根据物理表Id和物理表组Id获取UUID</p>
     * @param tableId
     * @param groupId
     * @return String    返回类型   
     * @throws
     */
    @Query("SELECT id FROM PhysicalGroupRelation T WHERE tableId=?1 and groupId=?2")
    public String getIdByTableIdAndGroupId(String tableId, String groupId);
    
    /**
     * qiujinwei 2014-12-03 
     * <p>标题: getIdsByGroupId</p>
     * <p>描述: 根据物理表Id获取UUID</p>
     * @param groupId
     * @return String[]    返回类型   
     * @throws
     */
    @Query("SELECT id FROM PhysicalGroupRelation T WHERE groupId=?1")
    public List<String> getIdsByGroupId(String groupId);
    
    /**
     * qiujinwei 2014-12-03 
     * <p>标题: deleteByGroupId</p>
     * <p>描述: 根据物理表组Id删除表关系</p>
     * @param groupId
     * @return     返回类型   
     * @throws
     */
    @Modifying
	@Transactional
    @Query("DELETE FROM PhysicalGroupRelation T WHERE groupId=?1")
    public void deleteByGroupId(String groupId);
    
    /**
     * qiujinwei 2014-12-09
     * <p>标题: getPhysicalGroupRelationByGroupId</p>
     * <p>描述: 根据物理表组Id获取表关系</p>
     * @param  groupId
     * @return List<Object[]>    返回类型   
     * @throws
     */
    @Query(value="select pgr.id, pt.show_name, pt.table_name, lt.show_name as logic_table_show_name " +
            " from t_xtpz_physical_group_relation pgr " +
            " inner join t_xtpz_physical_table_define pt on pgr.table_id=pt.id" +
            " inner join t_xtpz_logic_table_define lt on pt.logic_table_code=lt.code" +
            " inner join t_xtpz_physical_group_define pg on pgr.group_id=pg.id" +
            " where pg.id=?1 order by pgr.show_order", nativeQuery=true)
    public List<Object[]> getPhysicalGroupRelationByGroupId(String groupId);

    /**
     * 根据表组ID获取表组下的表
     * 
     * @param groupId 表组ID
     * @return List<PhysicalTableDefine>
     */
    @Query("select t from PhysicalTableDefine t, PhysicalGroupRelation gr, PhysicalGroupDefine g where t.id=gr.tableId and g.id=gr.groupId and g.id=? order by gr.showOrder")
    public List<PhysicalTableDefine> getPhysicalTableDefineByGroupId(String groupId);
    
    /**
     * qiucs 2015-10-15 下午4:01:42
     * <p>描述: 根据逻辑表CODE获取物理表组节点相关信息 </p>
     * @return List<Object[]>
     */
    @Query("select p.id, p.groupName, r.tableId from PhysicalGroupDefine p, PhysicalGroupRelation r " +
    		"where p.id=r.groupId and p.logicGroupCode=? and r.showOrder = 1 order by p.showOrder")
    public List<Object[]> getPhysicalGroupNode(String logicGroupCode);
}
