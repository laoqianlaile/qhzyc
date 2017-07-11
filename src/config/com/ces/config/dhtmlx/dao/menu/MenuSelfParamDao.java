package com.ces.config.dhtmlx.dao.menu;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.entity.menu.MenuSelfParam;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

/**
 * 菜单绑定构件的自身配置参数Dao
 * 
 * @author wanglei
 * @date 2013-09-11
 */
public interface MenuSelfParamDao extends StringIDDao<MenuSelfParam> {

    /**
     * 获取菜单绑定构件的自身配置参数
     * 
     * @param menuId 组合构件绑定关系ID
     * @return List<MenuSelfParam>
     */
    public List<MenuSelfParam> getByMenuId(String menuId);

    /**
     * 获取菜单绑定构件的自身配置参数
     * 
     * @param selfParamId 构件自身参数ID
     * @return List<MenuSelfParam>
     */
    public List<MenuSelfParam> getBySelfParamId(String selfParamId);

    /**
     * 删除菜单绑定构件的自身配置参数
     * 
     * @param menuId 组合构件绑定关系ID
     */
    @Transactional
    @Modifying
    @Query("delete from MenuSelfParam where menuId=?")
    public void deleteByMenuId(String menuId);

    /**
     * 删除菜单绑定构件的自身配置参数
     * 
     * @param selfParamId 构件自身参数ID
     */
    @Transactional
    @Modifying
    @Query("delete from MenuSelfParam where selfParamId=?")
    public void deleteBySelfParamId(String selfParamId);

    /**
     * 删除菜单绑定构件的自身配置参数
     * 
     * @param componentVersionId 构件版本ID
     */
    @Transactional
    @Modifying
    @Query("delete from MenuSelfParam where componentVersionId=?")
    public void deleteByComponentVersionId(String componentVersionId);
}
