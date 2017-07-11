package com.ces.config.dhtmlx.dao.menu;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.entity.menu.MenuInputParam;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

/**
 * 菜单绑定的构件入参Dao
 * 
 * @author wanglei
 * @date 2014-09-11
 */
public interface MenuInputParamDao extends StringIDDao<MenuInputParam> {

    /**
     * 获取菜单绑定的构件入参列表数据
     * 
     * @param menuId 组合构件绑定关系ID
     * @return List<Object[]>
     */
    @Query(value = "select cmp.id,cmp.name,cp.remark,cmp.value from t_xtpz_menu_input_param cmp, t_xtpz_component_input_param cp"
            + " where cmp.input_param_id=cp.id and cmp.menu_id=?", nativeQuery = true)
    public List<Object[]> getInputParamList(String menuId);

    /**
     * 根据菜单ID获取构件入参
     * 
     * @param menuId 组合构件绑定关系ID
     * @return List<MenuInputParam>
     */
    public List<MenuInputParam> getByMenuId(String menuId);

    /**
     * 根据入参ID获取构件入参
     * 
     * @param inputParamId 构件入参ID
     * @return List<MenuInputParam>
     */
    public List<MenuInputParam> getByInputParamId(String inputParamId);

    /**
     * 删除构件入参
     * 
     * @param menuId 菜单ID
     */
    @Transactional
    @Modifying
    @Query("delete from MenuInputParam where menuId=?")
    public void deleteByMenuId(String menuId);

    /**
     * 删除构件入参
     * 
     * @param inputParamId 构件入参ID
     */
    @Transactional
    @Modifying
    @Query("delete from MenuInputParam where inputParamId=?")
    public void deleteByInputParamId(String inputParamId);

    /**
     * 删除构件入参
     * 
     * @param componentVersionId 构件版本ID
     */
    @Transactional
    @Modifying
    @Query(value = "delete from t_xtpz_menu_input_param cip1 where cip1.input_param_id in (select id from t_xtpz_component_input_param cip2 where cip2.component_version_id=?)", nativeQuery = true)
    public void deleteByComponentVersionId(String componentVersionId);
}
