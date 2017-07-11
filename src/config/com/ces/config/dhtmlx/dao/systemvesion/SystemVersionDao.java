package com.ces.config.dhtmlx.dao.systemvesion;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.entity.systemversion.SystemVersion;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

/**
 * 系统版本Dao
 * 
 * @author wanglei
 * @date 2015-04-18
 */
public interface SystemVersionDao extends StringIDDao<SystemVersion> {

    /**
     * 获取系统版本
     * 
     * @param systemId 系统ID
     * @param name 系统版本名称
     * @return SystemVersion
     */
    public SystemVersion getBySystemIdAndName(String systemId, String name);

    /**
     * 获取系统版本
     * 
     * @param systemId 系统ID
     * @return List<SystemVersion>
     */
    @Query(value = "from SystemVersion where systemId=? order by isDefault desc, name")
    public List<SystemVersion> getBySystemId(String systemId);

    /**
     * 根据系统ID删除（系统版本）
     * 
     * @param systemId 系统ID
     */
    @Transactional
    @Modifying
    @Query("delete from SystemVersion where systemId=?")
    public void deleteBySystemId(String systemId);

    /**
     * 获取显示顺序范围内的系统版本
     * 
     * @param start 开始显示顺序
     * @param end 结束显示顺序
     * @param systemId 系统ID
     * @return List<SystemVersion>
     */
    public List<SystemVersion> getByShowOrderBetweenAndSystemId(Integer start, Integer end, String systemId);

    /**
     * 获取系统下的系统版本最大显示顺序
     * 
     * @param systemId 系统ID
     * @return Integer
     */
    @Query("select max(showOrder) from SystemVersion where systemId=?")
    public Integer getMaxShowOrder(String systemId);
}
