package com.ces.config.dhtmlx.dao.construct;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.entity.construct.ConstructCallback;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

/**
 * 预留区绑定的构件和回调函数的绑定关系Dao
 * 
 * @author wanglei
 * @date 2013-09-28
 */
public interface ConstructCallbackDao extends StringIDDao<ConstructCallback> {

    /**
     * 获取回调函数参数列表数据
     * 
     * @param componentVersionId 页面构件版本ID
     * @param constructDetailId 组合构件中构件和预留区绑定关系ID
     * @return List<Object[]>
     */
    @Query(value = "select cc.id as id1,ccp.id as id2,cc.name as name1,cc.remark as remark1,ccp.name as name2,ccp.remark as remark2 from t_xtpz_component_callback cc left join t_xtpz_comp_callback_param ccp"
            + " on cc.id=ccp.callback_id"
            + " where cc.component_version_id=? and cc.page in"
            + " (select crz.page from t_xtpz_construct_detail cd, t_xtpz_component_reserve_zone crz where cd.reserve_zone_id=crz.id and cd.id=?)", nativeQuery = true)
    public List<Object[]> getCallbackParamList(String componentVersionId, String constructDetailId);

    /**
     * 获取公用回调函数参数列表数据
     * 
     * @param constructDetailId 组合构件中构件和预留区绑定关系ID
     * @return List<Object[]>
     */
    @Query(value = "select cc.id as id1,ccp.id as id2,cc.name as name1,cc.remark as remark1,ccp.name as name2,ccp.remark as remark2 from t_xtpz_component_callback cc left join t_xtpz_comp_callback_param ccp"
            + " on cc.id=ccp.callback_id"
            + " where cc.component_version_id is null and cc.page in"
            + " (select crz.page from t_xtpz_construct_detail cd, t_xtpz_component_reserve_zone crz where cd.reserve_zone_id=crz.id and cd.id=?)", nativeQuery = true)
    public List<Object[]> getCommonCallbackParamList(String constructDetailId);

    /**
     * 获取预留区绑定的构件的出参列表数据
     * 
     * @param componentVersionId 预留区绑定的构件版本ID
     * @return List<Object[]>
     */
    @Query(value = "select t.id,t.name,t.remark from t_xtpz_component_output_param t where t.component_version_id=?", nativeQuery = true)
    public List<Object[]> getOutputParamList(String componentVersionId);

    /**
     * 获取回调函数参数和构件出参绑定关系列表数据
     * 
     * @param constructDetailId 组合构件中构件和预留区绑定关系ID
     * @return List<Object[]>
     */
    @Query(value = "select cc.id as id1,ccp.id as id2,cop.id as id3,cc.name as name1,cc.remark as remark1,ccp.name as name2,ccp.remark as remark2,cop.name as name3,cop.remark as remark3 from t_xtpz_construct_callback t left join t_xtpz_component_callback cc"
            + " on t.callback_id=cc.id"
            + " left join t_xtpz_comp_callback_param ccp"
            + " on t.input_param_id=ccp.id"
            + " left join t_xtpz_component_output_param cop"
            + " on t.output_param_id=cop.id"
            + " where t.construct_detail_id=?", nativeQuery = true)
    public List<Object[]> getConstructCallbackList(String constructDetailId);

    /**
     * 获取所有回调函数参数和构件出参绑定关系列表数据
     * 
     * @return List<Object[]>
     */
    @Query(value = "select t.construct_detail_id, cc.id as id1,ccp.id as id2,cop.id as id3,cc.name as name1,cc.remark as remark1,ccp.name as name2,ccp.remark as remark2,cop.name as name3,cop.remark as remark3 from t_xtpz_construct_callback t left join t_xtpz_component_callback cc"
            + " on t.callback_id=cc.id"
            + " left join t_xtpz_comp_callback_param ccp"
            + " on t.input_param_id=ccp.id"
            + " left join t_xtpz_component_output_param cop"
            + " on t.output_param_id=cop.id", nativeQuery = true)
    public List<Object[]> getAllConstructCallbacks();

    /**
     * 获取预留区绑定的构件和回调函数的绑定关系
     * 
     * @param constructDetailId 组合构件中构件和预留区绑定关系ID
     * @return List<ConstructCallback>
     */
    public List<ConstructCallback> getByConstructDetailId(String constructDetailId);

    /**
     * 删除预留区绑定的构件和回调函数的绑定关系
     * 
     * @param constructDetailId 组合构件中构件和预留区绑定关系ID
     */
    @Transactional
    @Modifying
    @Query("delete from ConstructCallback where constructDetailId=?")
    public void deleteByConstructDetailId(String constructDetailId);

    /**
     * 删除预留区绑定的构件和回调函数的绑定关系
     * 
     * @param callbackId 回调函数ID
     */
    @Transactional
    @Modifying
    @Query("delete from ConstructCallback where callbackId=?")
    public void deleteByCallbackId(String callbackId);

    /**
     * 删除预留区绑定的构件和回调函数的绑定关系
     * 
     * @param outputParamId 构件出参ID
     */
    @Transactional
    @Modifying
    @Query("delete from ConstructCallback where outputParamId=?")
    public void deleteByOutputParamId(String outputParamId);

    /**
     * 删除预留区绑定的构件和回调函数的绑定关系
     * 
     * @param inputParamId 回调函数的入参ID
     */
    @Transactional
    @Modifying
    @Query("delete from ConstructCallback where inputParamId=?")
    public void deleteByInputParamId(String inputParamId);

    /**
     * 删除预留区绑定的构件和回调函数的绑定关系
     * 
     * @param componentVersionId 构件版本ID
     */
    @Transactional
    @Modifying
    @Query(value = "delete from t_xtpz_construct_callback cc where cc.output_param_id in (select id from t_xtpz_component_output_param cop where cop.component_version_id=?)", nativeQuery = true)
    public void deleteOutParamByComponentVersionId(String componentVersionId);

    /**
     * 删除预留区绑定的构件和回调函数的绑定关系
     * 
     * @param componentVersionId 构件版本ID
     */
    @Transactional
    @Modifying
    @Query(value = "delete from t_xtpz_construct_callback cc1 where cc1.callback_id in (select id from t_xtpz_component_callback cc2 where cc2.component_version_id=?)", nativeQuery = true)
    public void deleteCallbackByComponentVersionId(String componentVersionId);
}
