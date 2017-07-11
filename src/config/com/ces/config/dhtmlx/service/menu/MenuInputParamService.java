package com.ces.config.dhtmlx.service.menu;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.dao.menu.MenuInputParamDao;
import com.ces.config.dhtmlx.entity.menu.MenuInputParam;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.utils.ComponentParamsUtil;

/**
 * 菜单绑定的构件入参Service
 * 
 * @author wanglei
 * @date 2014-09-11
 */
@Component("menuInputParamService")
public class MenuInputParamService extends ConfigDefineDaoService<MenuInputParam, MenuInputParamDao> {

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.service.StringIDService#setDaoUnBinding(com.ces.xarch.core.persistence.jpa.StringIDDao)
     */
    @Autowired
    @Qualifier("menuInputParamDao")
    @Override
    protected void setDaoUnBinding(MenuInputParamDao dao) {
        super.setDaoUnBinding(dao);
    }

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.service.AbstractService#save(com.ces.xarch.core.entity.BaseEntity)
     */
    @Override
    public MenuInputParam save(MenuInputParam entity) {
        MenuInputParam menuInputParam = super.save(entity);
        List<MenuInputParam> menuInputParamList = getByMenuId(entity.getMenuId());
        ComponentParamsUtil.putMenuInputParamList(menuInputParam.getId(), menuInputParamList);
        return menuInputParam;
    }

    /**
     * 获取菜单绑定的构件入参列表数据
     * 
     * @param menuId 菜单ID
     * @return List<Object[]>
     */
    public List<Object[]> getInputParamList(String menuId) {
        return getDao().getInputParamList(menuId);
    }

    /**
     * 获取菜单绑定的构件的自身配置参数
     * 
     * @param menuId 菜单ID
     * @return List<MenuInputParam>
     */
    public List<MenuInputParam> getByMenuId(String menuId) {
        return getDao().getByMenuId(menuId);
    }

    /**
     * 根据构件入参ID获取构件入参
     * 
     * @param inputParamId 构件入参ID
     * @return List<MenuInputParam>
     */
    public List<MenuInputParam> getByInputParamId(String inputParamId) {
        return getDao().getByInputParamId(inputParamId);
    }

    /**
     * 获取菜单下的入参
     * 
     * @param menuIds 菜单IDs
     * @return List<MenuInputParam>
     */
    public List<MenuInputParam> getMenuInputParams(String menuIds) {
        DatabaseHandlerDao dao = DatabaseHandlerDao.getInstance();
        String hql = "from MenuInputParam t where t.menuId in ('" + menuIds.replace(",", "','") + "')";
        List<MenuInputParam> menuInputParamList = dao.queryEntityForList(hql, MenuInputParam.class);
        return menuInputParamList;
    }

    /**
     * 删除构件入参
     * 
     * @param menuId 菜单ID
     */
    @Transactional
    public void deleteByMenuId(String menuId) {
        getDao().deleteByMenuId(menuId);
        ComponentParamsUtil.removeMenuInputParamList(menuId);
    }
}
