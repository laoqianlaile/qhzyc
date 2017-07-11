package com.ces.config.dhtmlx.dao.authority;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.entity.authority.AuthorityConstructButton;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

/**
 * 构件组装按钮权限DAO
 * 
 * @author wanglei
 * @date 2014-05-08
 */
public interface AuthorityConstructButtonDao extends StringIDDao<AuthorityConstructButton> {

    /**
     * 根据菜单ID删除构件组装按钮权限设定
     * 
     * @param menuId 菜单ID
     * @param componentVersionId 组合构件ID
     */
    @Transactional
    @Modifying
    @Query("delete from AuthorityConstructButton where menuId=? and componentVersionId=?")
    public void deleteByMenuIdAndComponentVersionId(String menuId, String componentVersionId);

    /**
     * 根据菜单ID删除构件组装按钮权限设定
     * 
     * @param menuId 菜单ID
     */
    @Transactional
    @Modifying
    @Query("delete from AuthorityConstructButton where menuId=?")
    public void deleteByMenuId(String menuId);

    /**
     * 根据组合构件ID删除构件组装按钮权限设定
     * 
     * @param componentVersionId 组合构件ID
     */
    @Transactional
    @Modifying
    @Query("delete from AuthorityConstructButton where componentVersionId=?")
    public void deleteByComponentVersionId(String componentVersionId);

    /**
     * 根据预留区和构件绑定关系ID删除构件组装按钮权限设定
     * 
     * @param constructDetailId 预留区和构件绑定关系ID
     */
    @Transactional
    @Modifying
    @Query("delete from AuthorityConstructButton where constructDetailId=?")
    public void deleteByConstructDetailId(String constructDetailId);
}
