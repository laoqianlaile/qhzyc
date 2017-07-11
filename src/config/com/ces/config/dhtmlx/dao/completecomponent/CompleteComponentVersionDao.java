package com.ces.config.dhtmlx.dao.completecomponent;

import java.util.List;

import com.ces.config.dhtmlx.entity.completecomponent.CompleteComponentVersion;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

/**
 * 成品构件版本Dao
 * 
 * @author wanglei
 * @date 2014-02-17
 */
public interface CompleteComponentVersionDao extends StringIDDao<CompleteComponentVersion> {

    /**
     * 根据成品构件ID和成品构件版本号获取成品构件版本
     * 
     * @param componentId 成品构件ID
     * @param version 成品构件版本号
     * @return CompleteComponentVersion
     */
    public CompleteComponentVersion getByComponentIdAndVersion(String componentId, String version);

    /**
     * 根据成品构件ID获取成品构件版本
     * 
     * @param componentId 成品构件ID
     * @return List<CompleteComponentVersion>
     */
    public List<CompleteComponentVersion> getByComponentId(String componentId);

    /**
     * 根据成品构件分类ID获取成品构件版本
     * 
     * @param areaId 成品构件分类ID
     * @return List<CompleteComponentVersion>
     */
    public List<CompleteComponentVersion> getByAreaId(String areaId);
}
