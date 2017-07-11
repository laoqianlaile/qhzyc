package com.ces.config.dhtmlx.service.menu;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.dao.menu.MenuSelfParamDao;
import com.ces.config.dhtmlx.entity.menu.Menu;
import com.ces.config.dhtmlx.entity.menu.MenuSelfParam;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.utils.ComponentParamsUtil;

/**
 * 菜单绑定构件的自身配置参数Service
 * 
 * @author wanglei
 * @date 2014-09-11
 */
@Component("menuSelfParamService")
public class MenuSelfParamService extends ConfigDefineDaoService<MenuSelfParam, MenuSelfParamDao> {

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.service.StringIDService#setDaoUnBinding(com.ces.xarch.core.persistence.jpa.StringIDDao)
     */
    @Autowired
    @Qualifier("menuSelfParamDao")
    @Override
    protected void setDaoUnBinding(MenuSelfParamDao dao) {
        super.setDaoUnBinding(dao);
    }

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.service.AbstractService#save(com.ces.xarch.core.entity.BaseEntity)
     */
    @Override
    @Transactional
    public MenuSelfParam save(MenuSelfParam entity) {
        MenuSelfParam menuSelfParam = super.save(entity);
        ComponentParamsUtil.putMenuSelfParamList(menuSelfParam.getMenuId(), menuSelfParam);
        return menuSelfParam;
    }

    /**
     * 获取菜单绑定构件的自身配置参数
     * 
     * @param menuId 组合构件绑定关系ID
     * @return List<MenuSelfParam>
     */
    public List<MenuSelfParam> getByMenuId(String menuId) {
        return getDao().getByMenuId(menuId);
    }

    /**
     * 获取菜单绑定构件的自身配置参数
     * 
     * @param selfParamId 构件自身参数ID
     * @return List<MenuSelfParam>
     */
    public List<MenuSelfParam> getBySelfParamId(String selfParamId) {
        return getDao().getBySelfParamId(selfParamId);
    }

    /**
     * 获取菜单下的自身配置参数
     * 
     * @param menuIds 菜单IDs
     * @return List<MenuSelfParam>
     */
    public List<MenuSelfParam> getMenuSelfParams(String menuIds) {
        DatabaseHandlerDao dao = DatabaseHandlerDao.getInstance();
        String hql = "from MenuSelfParam t where t.menuId in ('" + menuIds.replace(",", "','") + "')";
        List<MenuSelfParam> menuSelfParamList = dao.queryEntityForList(hql, MenuSelfParam.class);
        return menuSelfParamList;
    }

    /**
     * 删除菜单绑定构件的自身配置参数
     * 
     * @param menuId 组合构件绑定关系ID
     */
    @Transactional
    public void deleteByMenuId(String menuId) {
        getDao().deleteByMenuId(menuId);
        ComponentParamsUtil.removeMenuSelfParamList(menuId);
    }

    /**
     * 删除菜单绑定构件的自身配置参数
     * 
     * @param componentVersionId 构件版本ID
     */
    @Transactional
    public void deleteByComponentVersionId(String componentVersionId) {
        getDao().deleteByComponentVersionId(componentVersionId);
        List<Menu> menuList = getService(MenuService.class).getByComponentVersionId(componentVersionId);
        if (CollectionUtils.isNotEmpty(menuList)) {
            for (Menu menu : menuList) {
                ComponentParamsUtil.removeMenuSelfParamList(menu.getId());
            }
        }
    }
}
