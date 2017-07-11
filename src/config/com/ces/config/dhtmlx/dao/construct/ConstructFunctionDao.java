package com.ces.config.dhtmlx.dao.construct;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.entity.construct.ConstructFunction;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

/**
 * 预留区绑定的构件和页面方法的绑定关系Dao
 * 
 * @author wanglei
 * @date 2013-08-27
 */
public interface ConstructFunctionDao extends StringIDDao<ConstructFunction> {

    /**
     * 获取方法返回值列表数据
     * 
     * @param componentVersionId 页面构件版本ID
     * @param constructDetailId 组合构件中构件和预留区绑定关系ID
     * @return List<Object[]>
     */
    @Query(value = "select cfd.id,cf.name as name1,cf.remark as remark1,cfd.name as name2,cfd.remark as remark2 from t_xtpz_component_function_data cfd, t_xtpz_component_function cf, t_xtpz_construct_detail cd, t_xtpz_component_reserve_zone crz"
            + " where cfd.function_id=cf.id"
            + " and cd.reserve_zone_id=crz.id"
            + " and cf.page=crz.page"
            + " and cf.component_version_id = ? and cd.id = ? order by cf.name, cfd.name, cf.remark", nativeQuery = true)
    public List<Object[]> getFunctionDataList(String componentVersionId, String constructDetailId);

    /**
     * 获取公用方法返回值列表数据
     * 
     * @param constructDetailId 组合构件中构件和预留区绑定关系ID
     * @return List<Object[]>
     */
    @Query(value = "select cfd.id,cf.name as name1,cf.remark as remark1,cfd.name as name2,cfd.remark as remark2 from t_xtpz_component_function_data cfd, t_xtpz_component_function cf, t_xtpz_construct_detail cd, t_xtpz_component_reserve_zone crz"
            + " where cfd.function_id=cf.id"
            + " and cd.reserve_zone_id=crz.id"
            + " and cf.page=crz.page"
            + " and cf.component_version_id is null"
            + " and cd.id = ? order by cf.name, cfd.name, cf.remark", nativeQuery = true)
    public List<Object[]> getCommonFunctionDataList(String constructDetailId);

    /**
     * 获取构件入参列表数据
     * 
     * @param componentVersionId 预留区绑定的构件版本ID
     * @param constructDetailId 组合构件中构件和预留区绑定关系ID
     * @return List<Object[]>
     */
    @Query(value = "select cp.id,cp.name,cp.remark from t_xtpz_component_input_param cp where cp.component_version_id=? and cp.id not in"
            + " (select cf.input_param_id from t_xtpz_construct_function cf where cf.construct_detail_id=?)", nativeQuery = true)
    public List<Object[]> getInputParamList(String componentVersionId, String constructDetailId);

    /**
     * 获取构件公用入参列表数据
     * 
     * @param componentVersionId 预留区绑定的构件版本ID
     * @param constructDetailId 组合构件中构件和预留区绑定关系ID
     * @return List<Object[]>
     */
    @Query(value = "select cp.id,cp.name,cp.remark from t_xtpz_component_input_param cp where cp.component_version_id is null and cp.id not in"
            + " (select cf.input_param_id from t_xtpz_construct_function cf where cf.construct_detail_id=?)", nativeQuery = true)
    public List<Object[]> getCommonInputParamList(String constructDetailId);

    /**
     * 获取方法出参和构件入参绑定关系列表数据
     * 
     * @param constructDetailId 组合构件中构件和预留区绑定关系ID
     * @return List<Object[]>
     */
    @Query(value = "select cp.id as id1,cfd.id as id2,cp.name as name3,cp.remark as remark3,cf.name as name1,cf.remark as remark1,cfd.name as name2,cfd.remark as remark2 from t_xtpz_construct_function t, t_xtpz_component_function cf, t_xtpz_component_function_data cfd, t_xtpz_component_input_param cp"
            + " where t.function_id=cf.id and t.output_param_id=cfd.id and t.input_param_id=cp.id and t.construct_detail_id=?1"
            + "  union"
            + " select cp1.id as id1, cfd1.id as id2, cp1.name as name3, '', cf1.name as name1, cf1.remark as remark1, cfd1.name as name2, cfd1.remark as remark2 from t_xtpz_construct_function t1, t_xtpz_component_function cf1, t_xtpz_component_function_data cfd1, t_xtpz_construct_input_param cp1"
            + " where t1.function_id = cf1.id and t1.output_param_id = cfd1.id and t1.input_param_id = cp1.id and t1.construct_detail_id = ?1", nativeQuery = true)
    public List<Object[]> getConstructFunctionList(String constructDetailId);

    /**
     * 获取所有的方法出参和构件入参绑定关系列表数据
     * 
     * @return List<Object[]>
     */
    @Query(value = "select t.construct_detail_id,cp.id as id1,cfd.id as id2,cp.name as name3,cp.remark as remark3,cf.name as name1,cf.remark as remark1,cfd.name as name2,cfd.remark as remark2 from t_xtpz_construct_function t, t_xtpz_component_function cf, t_xtpz_component_function_data cfd, t_xtpz_component_input_param cp"
            + " where t.function_id=cf.id and t.output_param_id=cfd.id and t.input_param_id=cp.id"
            + " union "
            + " select t1.construct_detail_id, cp1.id as id1, cfd1.id as id2, cp1.name as name3, '', cf1.name as name1, cf1.remark as remark1, cfd1.name as name2, cfd1.remark as remark2 from t_xtpz_construct_function t1, t_xtpz_component_function cf1, t_xtpz_component_function_data cfd1, t_xtpz_construct_input_param cp1"
            + " where t1.function_id = cf1.id and t1.output_param_id = cfd1.id and t1.input_param_id = cp1.id", nativeQuery = true)
    public List<Object[]> getAllConstructFunctions();

    /**
     * 获取预留区绑定的构件和页面方法的绑定关系
     * 
     * @param constructDetailId 组合构件中构件和预留区绑定关系ID
     * @return List<ConstructFunction>
     */
    public List<ConstructFunction> getByConstructDetailId(String constructDetailId);

    /**
     * 删除预留区绑定的构件和页面方法的绑定关系
     * 
     * @param constructDetailId 组合构件中构件和预留区绑定关系ID
     */
    @Transactional
    @Modifying
    @Query("delete from ConstructFunction where constructDetailId=?")
    public void deleteByConstructDetailId(String constructDetailId);

    /**
     * 删除预留区绑定的构件和页面方法的绑定关系
     * 
     * @param inputParamId 构件入参ID
     */
    @Transactional
    @Modifying
    @Query("delete from ConstructFunction where inputParamId=?")
    public void deleteByInputParamId(String inputParamId);

    /**
     * 删除预留区绑定的构件和页面方法的绑定关系
     * 
     * @param functionId 页面方法ID
     */
    @Transactional
    @Modifying
    @Query("delete from ConstructFunction where functionId=?")
    public void deleteByFunctionId(String functionId);

    /**
     * 删除预留区绑定的构件和页面方法的绑定关系
     * 
     * @param outputParamId 方法返回值ID
     */
    @Transactional
    @Modifying
    @Query("delete from ConstructFunction where outputParamId=?")
    public void deleteByOutputParamId(String outputParamId);

    /**
     * 删除预留区绑定的构件和页面方法的绑定关系
     * 
     * @param componentVersionId 构件版本ID
     */
    @Transactional
    @Modifying
    @Query(value = "delete from t_xtpz_construct_function cf where cf.input_param_id in (select id from t_xtpz_component_input_param cip where cip.component_version_id=?)", nativeQuery = true)
    public void deleteInputParamByComponentVersionId(String componentVersionId);

    /**
     * 删除预留区绑定的构件和页面方法的绑定关系
     * 
     * @param componentVersionId 构件版本ID
     */
    @Transactional
    @Modifying
    @Query(value = "delete from t_xtpz_construct_function cf1 where cf1.function_id in (select id from t_xtpz_component_function cf2 where cf2.component_version_id=?)", nativeQuery = true)
    public void deleteFunctionByComponentVersionId(String componentVersionId);
}
