package com.ces.config.dhtmlx.dao.construct;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.entity.construct.ConstructInputParam;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

/**
 * 组合构件中基础构件的入参Dao
 * 
 * @author wanglei
 * @date 2013-09-03
 */
public interface ConstructInputParamDao extends StringIDDao<ConstructInputParam> {

    /**
     * 获取构件入参列表数据
     * 
     * @param constructId 组合构件绑定关系ID
     * @return List<Object[]>
     */
    @Query(value = "select cmp.id,cmp.name,cp.remark,cmp.value from t_xtpz_construct_input_param cmp, t_xtpz_component_input_param cp"
            + " where cmp.input_param_id=cp.id and cmp.construct_id=?", nativeQuery = true)
    public List<Object[]> getInputParamList(String constructId);

    /**
     * 根据组合构件绑定关系ID获取构件入参
     * 
     * @param constructId 组合构件绑定关系ID
     * @return List<ConstructInputParam>
     */
    public List<ConstructInputParam> getByConstructId(String constructId);

    /**
     * 根据组合构件绑定关系ID获取构件入参
     * 
     * @param inputParamId 构件入参ID
     * @return List<ConstructInputParam>
     */
    public List<ConstructInputParam> getByInputParamId(String inputParamId);

    /**
     * 删除构件入参
     * 
     * @param constructId 组合构件绑定关系ID
     */
    @Transactional
    @Modifying
    @Query("delete from ConstructInputParam where constructId=?")
    public void deleteByConstructId(String constructId);

    /**
     * 删除构件入参
     * 
     * @param inputParamId 构件入参ID
     */
    @Transactional
    @Modifying
    @Query("delete from ConstructInputParam where inputParamId=?")
    public void deleteByInputParamId(String inputParamId);

    /**
     * 删除构件入参
     * 
     * @param componentVersionId 构件版本ID
     */
    @Transactional
    @Modifying
    @Query(value = "delete from t_xtpz_construct_input_param cip1 where cip1.input_param_id in (select id from t_xtpz_component_input_param cip2 where cip2.component_version_id=?)", nativeQuery = true)
    public void deleteByComponentVersionId(String componentVersionId);

    /**
     * 删除构件入参
     * 
     * @param constructId 组合构件绑定关系ID
     * @param nameLike 按钮名称前缀%
     */
    @Transactional
    @Modifying
    @Query(value = "delete from t_xtpz_construct_input_param cip where cip.construct_id=? and cip.name like ?", nativeQuery = true)
    public void deleteConstructInputParams(String constructId, String nameLike);
}
