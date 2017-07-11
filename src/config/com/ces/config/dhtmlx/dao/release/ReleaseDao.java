package com.ces.config.dhtmlx.dao.release;

import java.util.List;

import com.ces.config.dhtmlx.entity.release.Release;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

/**
 * 系统发布Dao
 * 
 * @author wanglei
 * @date 2013-11-15
 */
public interface ReleaseDao extends StringIDDao<Release> {

    /**
     * 根据根菜单ID和版本号获取系统发布
     * 
     * @param rootMenuId 根菜单ID
     * @param version 版本号
     * @return Release
     */
    public Release getByRootMenuIdAndVersion(String rootMenuId, String version);

    /**
     * 根据根菜单ID和类型获取系统发布
     * 
     * @param rootMenuId 根菜单ID
     * @param type 类型
     * @return List<Release>
     */
    public List<Release> getByRootMenuIdAndType(String rootMenuId, String type);
}
