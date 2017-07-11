package com.ces.config.dhtmlx.dao.construct;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.entity.construct.ConstructDetailSelfParam;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

/**
 * 构件绑定预留区后的自身配置参数Dao
 * 
 * @author wanglei
 * @date 2013-08-26
 */
public interface ConstructDetailSelfParamDao extends StringIDDao<ConstructDetailSelfParam> {

    /**
     * 获取构件绑定预留区后的自身配置参数
     * 
     * @param constructDetailId 组合构件中构件和预留区绑定关系ID
     * @return List<ConstructDetailSelfParam>
     */
    public List<ConstructDetailSelfParam> getByConstructDetailId(String constructDetailId);

    /**
     * 获取构件绑定预留区后的自身配置参数
     * 
     * @param selfParamId 构件自身参数ID
     * @return List<ConstructDetailSelfParam>
     */
    public List<ConstructDetailSelfParam> getBySelfParamId(String selfParamId);

    /**
     * 删除构件绑定预留区后的自身配置参数
     * 
     * @param constructDetailId 组合构件中构件和预留区绑定关系ID
     */
    @Transactional
    @Modifying
    @Query("delete from ConstructDetailSelfParam where constructDetailId=?")
    public void deleteByConstructDetailId(String constructDetailId);

    /**
     * 删除构件绑定预留区后的自身配置参数
     * 
     * @param selfParamId 构件自身参数ID
     */
    @Transactional
    @Modifying
    @Query("delete from ConstructDetailSelfParam where selfParamId=?")
    public void deleteBySelfParamId(String selfParamId);

    /**
     * 删除构件绑定预留区后的自身配置参数
     * 
     * @param componentVersionId 构件版本ID
     */
    @Transactional
    @Modifying
    @Query("delete from ConstructDetailSelfParam where componentVersionId=?")
    public void deleteByComponentVersionId(String componentVersionId);
}
