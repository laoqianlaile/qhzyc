package com.ces.config.dhtmlx.dao.construct;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.entity.construct.ConstructSelfParam;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

/**
 * 组合构件中基础构件的自身配置参数Dao
 * 
 * @author wanglei
 * @date 2013-08-26
 */
public interface ConstructSelfParamDao extends StringIDDao<ConstructSelfParam> {

    /**
     * 获取组合构件中基础构件的自身配置参数
     * 
     * @param constructId 组合构件绑定关系ID
     * @return List<ConstructSelfParam>
     */
    public List<ConstructSelfParam> getByConstructId(String constructId);

    /**
     * 获取组合构件中基础构件的自身配置参数
     * 
     * @param selfParamId 构件自身参数ID
     * @return List<ConstructSelfParam>
     */
    public List<ConstructSelfParam> getBySelfParamId(String selfParamId);

    /**
     * 删除组合构件中基础构件的自身配置参数
     * 
     * @param constructId 组合构件绑定关系ID
     */
    @Transactional
    @Modifying
    @Query("delete from ConstructSelfParam where constructId=?")
    public void deleteByConstructId(String constructId);

    /**
     * 删除组合构件中基础构件的自身配置参数
     * 
     * @param selfParamId 构件自身参数ID
     */
    @Transactional
    @Modifying
    @Query("delete from ConstructSelfParam where selfParamId=?")
    public void deleteBySelfParamId(String selfParamId);

    /**
     * 删除组合构件中基础构件的自身配置参数
     * 
     * @param componentVersionId 构件版本ID
     */
    @Transactional
    @Modifying
    @Query("delete from ConstructSelfParam where componentVersionId=?")
    public void deleteByComponentVersionId(String componentVersionId);
}
