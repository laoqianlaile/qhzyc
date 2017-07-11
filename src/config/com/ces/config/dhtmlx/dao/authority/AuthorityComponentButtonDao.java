package com.ces.config.dhtmlx.dao.authority;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.entity.authority.AuthorityComponentButton;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

/**
 * 开发的构件按钮权限DAO
 * 
 * @author wanglei
 * @date 2014-07-31
 */
public interface AuthorityComponentButtonDao extends StringIDDao<AuthorityComponentButton> {

    /**
     * 根据菜单ID删除开发的构件按钮权限设定
     * 
     * @param menuId 菜单ID
     */
    @Transactional
    @Modifying
    @Query("delete from AuthorityComponentButton where menuId=?")
    public void deleteByMenuId(String menuId);

    /**
     * 根据开发构件版本ID删除开发的构件按钮权限设定
     * 
     * @param componentVersionId 开发的构件版本ID
     */
    @Transactional
    @Modifying
    @Query("delete from AuthorityComponentButton where componentVersionId=?")
    public void deleteByComponentVersionId(String componentVersionId);

    /**
     * 根据开发构件中按钮ID删除开发的构件按钮权限设定
     * 
     * @param componentButtonId 开发构件中按钮ID
     */
    @Transactional
    @Modifying
    @Query("delete from AuthorityComponentButton where componentButtonId=?")
    public void deleteByComponentButtonId(String componentButtonId);
}
