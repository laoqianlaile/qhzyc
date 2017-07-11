package com.ces.config.dhtmlx.dao.component;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.entity.component.CommonComponentRelation;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

/**
 * 公用构件依赖关系Dao
 * 
 * @author wanglei
 * @date 2014-06-09
 */
public interface CommonComponentRelationDao extends StringIDDao<CommonComponentRelation> {

    /**
     * 根据构件版本ID获取公用构件依赖关系
     * 
     * @param componentVersionId 构件版本ID
     * @return List<CommonComponentRelation>
     */
    public List<CommonComponentRelation> getByComponentVersionId(String componentVersionId);

    /**
     * 根据构件版本ID获取公用构件依赖关系
     * 
     * @param componentVersionId 构件版本ID
     * @return CommonComponentRelation
     */
    @Query("from CommonComponentRelation where componentVersionId=? and commonComponentVersionId=?")
    public CommonComponentRelation getCommonComponentRelation(String componentVersionId, String commonComponentVersionId);

    /**
     * 根据构件版本ID删除公用构件依赖关系
     * 
     * @param componentVersionId 构件版本ID
     */
    @Transactional
    @Modifying
    @Query(value = "delete from t_xtpz_commom_comp_relation t where t.component_version_id=?", nativeQuery = true)
    public void deleteByComponentVersionId(String componentVersionId);

    /**
     * 根据公用构件版本ID删除公用构件依赖关系
     * 
     * @param commonComponentVersionId 公用构件版本ID
     */
    @Transactional
    @Modifying
    @Query(value = "delete from t_xtpz_commom_comp_relation t where t.common_comp_version_id=?", nativeQuery = true)
    public void deleteByCommonComponentVersionId(String commonComponentVersionId);
}