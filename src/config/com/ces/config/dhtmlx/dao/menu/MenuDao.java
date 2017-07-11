package com.ces.config.dhtmlx.dao.menu;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.entity.appmanage.Module;
import com.ces.config.dhtmlx.entity.menu.Menu;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

/**
 * 菜单Dao
 * 
 * @author wanglei
 * @date 2013-07-15
 */
public interface MenuDao extends StringIDDao<Menu> {

    /**
     * 获取菜单
     * 
     * @param name 菜单名称
     * @param parentId 父菜单ID
     * @return Menu
     */
    public Menu getByNameAndParentId(String name, String parentId);

    /**
     * 获取菜单
     * 
     * @param code 菜单编码
     * @param rootMenuId 父菜单ID
     * @return Menu
     */
    public Menu getByCodeAndRootMenuId(String code, String rootMenuId);

    /**
     * 获取子菜单
     * 
     * @param parentId 父菜单ID
     * @return List<Menu>
     */
    @Query("from Menu where parentId=? order by showOrder")
    public List<Menu> getByParentId(String parentId);

    /**
     * 获取系统下的菜单
     * 
     * @param rootMenuId 根菜单ID
     * @return List<Menu>
     */
    @Query("from Menu where rootMenuId=? order by showOrder")
    public List<Menu> getByRootMenuId(String rootMenuId);

    /**
     * 获取菜单下子菜单最大显示顺序
     * 
     * @param parentId 父菜单ID
     */
    @Query("select max(showOrder) from Menu where parentId=?")
    public Integer getMaxShowOrder(String parentId);

    /**
     * 获取显示顺序范围内的菜单
     * 
     * @param start 开始显示顺序
     * @param end 结束显示顺序
     * @param parentId 父菜单ID
     * @return List<Menu>
     */
    public List<Menu> getByShowOrderBetweenAndParentId(Integer start, Integer end, String parentId);

    /**
     * 根据根菜单ID获取其下子孙菜单绑定的构件版本ID
     * 
     * @param rootMenuId 根菜单ID
     * @return List<String>
     */
    @Query(value = "select distinct component_version_id from t_xtpz_menu where root_menu_id=?", nativeQuery = true)
    public List<String> getComponentVersionIdByRootMenuId(String rootMenuId);

    /**
     * 根据根菜单ID获取其下子孙菜单绑定构件的菜单
     * 
     * @param rootMenuId 根菜单ID
     * @return List<Menu>
     */
    @Query("from Menu where bindingType='1' and rootMenuId=?")
    public List<Menu> getComponentMenuList(String rootMenuId);

    /**
     * 根据构件版本ID获取绑定该构件的菜单
     * 
     * @param componentVersionId 版本ID
     * @return List<String>
     */
    public List<Menu> getByComponentVersionId(String componentVersionId);

    /**
     * 根据菜单ID获取绑定的构件版本的ID
     * 
     * @param menuId 菜单ID
     * @return String
     */
    @Query("select t.componentVersionId from Menu t where t.id=?1")
    public String getComponentVersionIdById(String menuId);

    /**
     * 将菜单的显示顺序加一
     * 
     * @param start 开始显示顺序
     * @param parentId 父菜单ID
     */
    @Transactional
    @Modifying
    @Query("update Menu set showOrder=showOrder+1 where showOrder>? and parentId=?")
    public void updateShowOrderPlusOne(Integer start, String parentId);

    /**
     * 获取所有的系统菜单
     * 
     * @return List<Menu>
     */
    @Query("from Menu where parentId='-1' and id<>'sys_0' order by showOrder")
    public List<Menu> getRootMenuList();

    /**
     * qiucs 2015-5-18 下午1:22:53
     * <p>描述: 获取所有带树的菜单 </p>
     * 
     * @return List<Menu>
     */
    @Query("select mu from Menu mu, Construct cs, ComponentVersion cv, Component ct " + "where mu.componentVersionId=cs.assembleComponentVersion.id "
            + "and cs.baseComponentVersionId=cv.id and cv.component.id=ct.id and ct.type='3'")
    public List<Menu> getMenuContainTree();
    
    /**
     * qiucs 2015-8-13 下午8:11:06
     * <p>描述: 获取指定带树的菜单 </p>
     * @return List<Menu>
     */
    @Query("select mu from Menu mu, Construct cs, ComponentVersion cv, Component ct " 
    		+ "where mu.bindingType='1' and mu.parentId=?1 and mu.componentVersionId=cs.assembleComponentVersion.id "
            + "and cs.baseComponentVersionId=cv.id and cv.component.id=ct.id and ct.type='3' "
            + "order by mu.showOrder")
    public List<Menu> getMenuContainTreeByParentId(String parentId);

    /**
     * qiucs 2015-5-18 下午1:22:57
     * <p>描述: 根据指定树ID获取菜单 </p>
     * 
     * @return List<Menu>
     */
    @Query("select mu from Menu mu, Construct cs, ComponentVersion cv, Component ct, Module md, Menu pmu "
            + "where mu.componentVersionId=cs.assembleComponentVersion.id " 
    		+ "and mu.parentId=pmu.id "
    		+ "and cs.baseComponentVersionId=cv.id and cv.component.id=ct.id and ct.type='3'"
            + "and ct.code=md.id and md.treeId=?1 " 
    		+ "order by pmu.showOrder, mu.showOrder")
    public List<Menu> getMenuContainTree(String treeId);

    /**
     * qiucs 2015-5-18 下午2:10:23
     * <p>描述: 根据指定菜单ID获取对应的自定义构件 </p>
     * 
     * @return List<Menu>
     */
    @Query("select md from Menu mu, Construct cs, ComponentVersion cv, Component ct, Module md "
            + "where mu.id=?1 and mu.componentVersionId=cs.assembleComponentVersion.id "
            + "and cs.baseComponentVersionId=cv.id and cv.component.id=ct.id and ct.type='3'" + "and ct.code=md.id")
    public Module getModuleOfMenu(String menuId);

    /**
     * 获取可以进行应用定义的菜单
     * 
     * @return List<Menu>
     */
    @Query(value = "select m.* from t_xtpz_menu m, t_xtpz_component_version cv, t_xtpz_component c "
            + "where m.component_version_id=cv.id and cv.component_id=c.id and c.type='9'", nativeQuery = true)
    public List<Menu> getAppMenus();
}
