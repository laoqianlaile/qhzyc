package com.ces.config.dhtmlx.service.component;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.dao.component.ComponentInputParamDao;
import com.ces.config.dhtmlx.dao.construct.ConstructFunctionDao;
import com.ces.config.dhtmlx.dao.construct.ConstructInputParamDao;
import com.ces.config.dhtmlx.dao.menu.MenuInputParamDao;
import com.ces.config.dhtmlx.entity.component.ComponentInputParam;
import com.ces.config.dhtmlx.entity.construct.Construct;
import com.ces.config.dhtmlx.entity.construct.ConstructInputParam;
import com.ces.config.dhtmlx.entity.menu.Menu;
import com.ces.config.dhtmlx.entity.menu.MenuInputParam;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.dhtmlx.service.construct.ConstructInputParamService;
import com.ces.config.dhtmlx.service.construct.ConstructService;
import com.ces.config.dhtmlx.service.menu.MenuInputParamService;
import com.ces.config.dhtmlx.service.menu.MenuService;
import com.ces.config.utils.ComponentParamsUtil;
import com.ces.config.utils.StringUtil;

/**
 * 构件入参Service
 * 
 * @author wanglei
 * @date 2013-07-29
 */
@Component("componentInputParamService")
public class ComponentInputParamService extends ConfigDefineDaoService<ComponentInputParam, ComponentInputParamDao> {

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.service.StringIDService#setDaoUnBinding(com.ces.xarch.core.persistence.jpa.StringIDDao)
     */
    @Autowired
    @Qualifier("componentInputParamDao")
    @Override
    protected void setDaoUnBinding(ComponentInputParamDao dao) {
        super.setDaoUnBinding(dao);
    }

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.service.AbstractService#save(com.ces.xarch.core.entity.BaseEntity)
     */
    @SuppressWarnings("unchecked")
    @Override
    @Transactional
    public ComponentInputParam save(ComponentInputParam entity) {
        ComponentInputParam componentInputParam = null;
        if (StringUtil.isNotEmpty(entity.getId())) {
            componentInputParam = getDao().save(entity);
            List<MenuInputParam> menuInputParamList = getService(MenuInputParamService.class).getByInputParamId(componentInputParam.getId());
            if (CollectionUtils.isNotEmpty(menuInputParamList)) {
                for (MenuInputParam menuInputParam : menuInputParamList) {
                    if (!componentInputParam.getName().equals(menuInputParam.getName())) {
                        menuInputParam.setName(componentInputParam.getName());
                        getService(MenuInputParamService.class).save(menuInputParam);
                    }
                }
            }
            List<ConstructInputParam> constructInputParamList = getService(ConstructInputParamService.class).getByInputParamId(componentInputParam.getId());
            if (CollectionUtils.isNotEmpty(constructInputParamList)) {
                for (ConstructInputParam constructInputParam : constructInputParamList) {
                    String oldInputParamName = constructInputParam.getName();
                    if (!componentInputParam.getName().equals(oldInputParamName)) {
                        if (oldInputParamName.indexOf("--") != -1) {
                            constructInputParam.setName(oldInputParamName.substring(0, oldInputParamName.indexOf("--") + 2) + componentInputParam.getName());
                        } else {
                            constructInputParam.setName(componentInputParam.getName());
                        }
                        getService(ConstructInputParamService.class).save(constructInputParam);
                    }
                }
            }
        } else {
            componentInputParam = getDao().save(entity);
            List<Menu> menuList = new ArrayList<Menu>();
            menuList.addAll(getService(MenuService.class).getByComponentVersionId(componentInputParam.getComponentVersionId()));
            List<Construct> constructList = getService(ConstructService.class).getByBaseComponentVersionId(componentInputParam.getComponentVersionId());
            if (CollectionUtils.isNotEmpty(constructList)) {
                ConstructInputParam constructInputParam = null;
                for (Construct construct : constructList) {
                    constructInputParam = new ConstructInputParam();
                    constructInputParam.setConstructId(construct.getId());
                    constructInputParam.setInputParamId(componentInputParam.getId());
                    constructInputParam.setName(componentInputParam.getName());
                    constructInputParam.setValue(componentInputParam.getValue());
                    getService(ConstructInputParamService.class).save(constructInputParam);
                    menuList.addAll(getService(MenuService.class).getByComponentVersionId(construct.getAssembleComponentVersion().getId()));
                }
            }
            // 该参数所在构件的组合构件是否在Tab构件中被使用，如果被使用，需更改Tab组合构件的参数
            String sql = "select cd.construct_id,cd.button_code from t_xtpz_component c, t_xtpz_component_version cv,t_xtpz_construct c1,t_xtpz_construct_detail cd"
                    + " where c.id=cv.component_id"
                    + " and cv.id=c1.base_component_version_id"
                    + " and c1.id=cd.construct_id"
                    + " and c.type='7'"
                    + " and cd.component_version_id in ("
                    + " select c2.component_version_id from t_xtpz_construct c2 where c2.base_component_version_id in ("
                    + " select cv.id from t_xtpz_component c, t_xtpz_component_version cv where c.id=cv.component_id and cv.id='"
                    + entity.getComponentVersionId() + "')" + ")";
            List<Object[]> list = DatabaseHandlerDao.getInstance().queryForList(sql);
            String sql1 = "select cd.construct_id,cd.button_code from t_xtpz_component c, t_xtpz_component_version cv,t_xtpz_construct c1,t_xtpz_construct_detail cd"
                    + " where c.id=cv.component_id"
                    + " and cv.id=c1.base_component_version_id"
                    + " and c1.id=cd.construct_id"
                    + " and c.type='7'"
                    + " and cd.component_version_id in ("
                    + " select cv.id from t_xtpz_component c, t_xtpz_component_version cv where c.id=cv.component_id and cv.id='"
                    + entity.getComponentVersionId() + "')";
            list.addAll(DatabaseHandlerDao.getInstance().queryForList(sql1));
            if (CollectionUtils.isNotEmpty(list)) {
                ConstructInputParam constructInputParam = null;
                for (Object[] objs : list) {
                    String constructId = String.valueOf(objs[0]);
                    String buttonCode = String.valueOf(objs[1]);
                    if (StringUtil.isNotEmpty(constructId) && StringUtil.isNotEmpty(buttonCode)) {
                        constructInputParam = new ConstructInputParam();
                        constructInputParam.setConstructId(constructId);
                        constructInputParam.setInputParamId(componentInputParam.getId());
                        constructInputParam.setName(buttonCode + "--" + componentInputParam.getName());
                        constructInputParam.setValue(componentInputParam.getValue());
                        getService(ConstructInputParamService.class).save(constructInputParam);
                    }
                }
            }
            if (CollectionUtils.isNotEmpty(menuList)) {
                MenuInputParam menuInputParam = null;
                for (Menu menu : menuList) {
                    menuInputParam = new MenuInputParam();
                    menuInputParam.setMenuId(menu.getId());
                    menuInputParam.setInputParamId(componentInputParam.getId());
                    menuInputParam.setName(componentInputParam.getName());
                    menuInputParam.setValue(componentInputParam.getValue());
                    getService(MenuInputParamService.class).save(menuInputParam);
                }
            }
        }
        return componentInputParam;
    }

    /*
     * (non-Javadoc)
     * @see com.ces.config.dhtmlx.servie.base.ConfigDefineDaoService#delete(java.lang.String)
     */
    @Override
    @Transactional
    public void delete(String ids) {
        String[] inputParamIds = ids.split(",");
        for (int i = 0; i < inputParamIds.length; i++) {
            getDaoFromContext(ConstructInputParamDao.class).deleteByInputParamId(inputParamIds[i]);
            getDaoFromContext(ConstructFunctionDao.class).deleteByInputParamId(inputParamIds[i]);
            getDaoFromContext(MenuInputParamDao.class).deleteByInputParamId(inputParamIds[i]);
            getDao().delete(inputParamIds[i]);
        }
    }

    /**
     * 根据名称和构件版本ID获取构件入参
     * 
     * @param name 名称
     * @param componentVersionId 构件版本ID
     * @return ComponentInputParam
     */
    public ComponentInputParam getByNameAndComponentVersionId(String name, String componentVersionId) {
        return getDao().getByNameAndComponentVersionId(name, componentVersionId);
    }

    /**
     * 根据构件版本ID获取构件入参
     * 
     * @param componentVersionId 构件版本ID
     * @return List<ComponentParameter>
     */
    public List<ComponentInputParam> getByComponentVersionId(String componentVersionId) {
        return getDao().getByComponentVersionId(componentVersionId);
    }

    /**
     * 根据构件版本ID获取构件入参
     * 
     * @param componentVersionIds 构件版本IDs
     * @return List<ComponentParameter>
     */
    public List<ComponentInputParam> getByComponentVersionIds(String componentVersionIds) {
        List<ComponentInputParam> componentInputParamList = new ArrayList<ComponentInputParam>();
        if (StringUtil.isNotEmpty(componentVersionIds)) {
            String hql = "from ComponentInputParam t where t.componentVersionId in ('" + componentVersionIds.replace(",", "','") + "')";
            componentInputParamList = DatabaseHandlerDao.getInstance().queryEntityForList(hql, ComponentInputParam.class);
        }
        return componentInputParamList;
    }

    /**
     * 获取构件公用入参
     * 
     * @return List<ComponentParameter>
     */
    public List<ComponentInputParam> getCommonInputParams() {
        return getDao().getCommonInputParams();
    }

    /**
     * 根据构件版本ID删除构件入参
     * 
     * @param componentVersionId 构件版本ID
     */
    @Transactional
    public void deleteByComponentVersionId(String componentVersionId) {
        getService(ConstructInputParamService.class).deleteByComponentVersionId(componentVersionId);
        getDaoFromContext(ConstructFunctionDao.class).deleteInputParamByComponentVersionId(componentVersionId);
        getDao().deleteByComponentVersionId(componentVersionId);
        ComponentParamsUtil.removeComponentInputParamList(componentVersionId);
    }
}
