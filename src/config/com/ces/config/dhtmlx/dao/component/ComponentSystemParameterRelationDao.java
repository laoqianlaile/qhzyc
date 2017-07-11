package com.ces.config.dhtmlx.dao.component;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.entity.component.ComponentSystemParameterRelation;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

/**
 * 构件版本中系统参数和系统中系统参数的关联关系Dao
 * 
 * @author wanglei
 * @date 2013-08-20
 */
public interface ComponentSystemParameterRelationDao extends StringIDDao<ComponentSystemParameterRelation> {

    /**
     * 获取构件系统参数列表数据
     * 
     * @param componentVersionId 模块构件版本ID
     * @return List<Object[]>
     */
    @Query(value = "select csp.id,csp.name,csp.remark from t_xtpz_component_system_param csp"
            + " where csp.component_version_id = :componentVersionId"
            + " and id not in"
            + " (select cspr.component_system_param_id from t_xtpz_comp_sys_param_relation cspr where cspr.component_version_id = :componentVersionId)", nativeQuery = true)
    public List<Object[]> getComponentSystemParamList(@Param("componentVersionId")
                                                              String componentVersionId);

    /**
     * 获取构件系统参数所有数据
     * 
     * @param componentVersionId 模块构件版本ID
     * @return List<Object[]>
     */
    @Query(value = "select csp.id,csp.name,csp.remark from t_xtpz_component_system_param csp"
            + " where csp.component_version_id = :componentVersionId", nativeQuery = true)
    public List<Object[]> getAllComponentSystemParamList(@Param("componentVersionId")
                                                                 String componentVersionId);

    /**
     * 获取系统参数列表数据
     * 
     * @return List<Object[]>
     */
    @Query(value = "select sp.id,sp.name,sp.remark from t_xtpz_system_param sp", nativeQuery = true)
    public List<Object[]> getSystemParamList();

    /**
     * 获取构件系统参数和系统参数绑定关系列表数据
     * 
     * @param componentVersionId 模块构件版本ID
     * @return List<Object[]>
     */
    @Query(value = "select csp.id as id1,sp.id as id2,csp.name as name1,csp.remark as remark1,sp.name as name2,sp.remark as remark2 from t_xtpz_comp_sys_param_relation cspr, t_xtpz_component_system_param csp, t_xtpz_system_param sp"
            + " where cspr.component_system_param_id=csp.id"
            + " and cspr.system_param_id=sp.id"
            + " and cspr.component_version_id=?", nativeQuery = true)
    public List<Object[]> getComponentSystemParamRelationList(String componentVersionId);

    /**
     * 获取构件系统参数名称和值
     * 
     * @param componentVersionId 模块构件版本ID
     * @return List<Object[]>
     */
    @Query(value = "select csp.name,sp.value from t_xtpz_comp_sys_param_relation cspr, t_xtpz_component_system_param csp, t_xtpz_system_param sp"
            + " where cspr.component_system_param_id=csp.id"
            + " and cspr.system_param_id=sp.id"
            + " and cspr.component_version_id=?", nativeQuery = true)
    public List<Object[]> getComponentSystemParams(String componentVersionId);

    /**
     * 根据构件版本ID获取该构件系统参数关联配置
     * 
     * @param componentVersionId 构件版本ID
     */
    public List<ComponentSystemParameterRelation> getByComponentVersionId(String componentVersionId);

    /**
     * 删除构件系统参数关联配置
     * 
     * @param componentVersionId 构件版本ID
     */
    @Transactional
    @Modifying
    @Query("delete from ComponentSystemParameterRelation where componentVersionId=?")
    public void deleteByComponentVersionId(String componentVersionId);
}
