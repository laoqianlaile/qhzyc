package com.ces.config.dhtmlx.dao.release;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.entity.release.ReleaseMenu;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

/**
 * 系统发布菜单DAO
 * 
 * @author wanglei
 * @date 2014-10-13
 */
public interface ReleaseMenuDao extends StringIDDao<ReleaseMenu> {

    /**
     * 根据系统发布的ID删除相关发布的菜单信息
     * 
     * @param systemReleaseId 系统发布ID
     */
    @Transactional
    @Modifying
    @Query("delete from ReleaseMenu where systemReleaseId=?")
    public void deleteBySystemReleaseId(String systemReleaseId);
}
