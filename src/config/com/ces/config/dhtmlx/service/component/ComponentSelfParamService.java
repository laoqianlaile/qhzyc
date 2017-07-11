package com.ces.config.dhtmlx.service.component;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.dao.component.ComponentSelfParamDao;
import com.ces.config.dhtmlx.dao.construct.ConstructDetailSelfParamDao;
import com.ces.config.dhtmlx.dao.construct.ConstructSelfParamDao;
import com.ces.config.dhtmlx.dao.menu.MenuSelfParamDao;
import com.ces.config.dhtmlx.entity.component.ComponentSelfParam;
import com.ces.config.dhtmlx.entity.construct.Construct;
import com.ces.config.dhtmlx.entity.construct.ConstructDetail;
import com.ces.config.dhtmlx.entity.construct.ConstructDetailSelfParam;
import com.ces.config.dhtmlx.entity.construct.ConstructSelfParam;
import com.ces.config.dhtmlx.entity.menu.Menu;
import com.ces.config.dhtmlx.entity.menu.MenuSelfParam;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.dhtmlx.service.construct.ConstructDetailSelfParamService;
import com.ces.config.dhtmlx.service.construct.ConstructDetailService;
import com.ces.config.dhtmlx.service.construct.ConstructSelfParamService;
import com.ces.config.dhtmlx.service.construct.ConstructService;
import com.ces.config.dhtmlx.service.menu.MenuSelfParamService;
import com.ces.config.dhtmlx.service.menu.MenuService;
import com.ces.config.utils.ComponentParamsUtil;
import com.ces.config.utils.StringUtil;

/**
 * 构件自身配置参数Service
 * 
 * @author wanglei
 * @date 2013-08-15
 */
@Component("componentSelfParamService")
public class ComponentSelfParamService extends ConfigDefineDaoService<ComponentSelfParam, ComponentSelfParamDao> {

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.service.StringIDService#setDaoUnBinding(com.ces.xarch.core.persistence.jpa.StringIDDao)
     */
    @Autowired
    @Qualifier("componentSelfParamDao")
    @Override
    protected void setDaoUnBinding(ComponentSelfParamDao dao) {
        super.setDaoUnBinding(dao);
    }

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.service.AbstractService#save(com.ces.xarch.core.entity.BaseEntity)
     */
    @Override
    @Transactional
    public ComponentSelfParam save(ComponentSelfParam entity) {
        ComponentSelfParam componentSelfParam = null;
        if (StringUtil.isNotEmpty(entity.getId())) {
            if (StringUtil.isEmpty(entity.getText())) {
                entity.setText(entity.getValue());
            }
            componentSelfParam = getDao().save(entity);
            List<MenuSelfParam> menuSelfParamList = getService(MenuSelfParamService.class).getBySelfParamId(componentSelfParam.getId());
            if (CollectionUtils.isNotEmpty(menuSelfParamList)) {
                for (MenuSelfParam menuSelfParam : menuSelfParamList) {
                    if (!componentSelfParam.getName().equals(menuSelfParam.getName()) || !componentSelfParam.getType().equals(menuSelfParam.getType())
                            || !componentSelfParam.getOptions().equals(menuSelfParam.getOptions())
                            || !componentSelfParam.getRemark().equals(menuSelfParam.getRemark())) {
                        menuSelfParam.setName(componentSelfParam.getName());
                        menuSelfParam.setType(componentSelfParam.getType());
                        menuSelfParam.setOptions(componentSelfParam.getOptions());
                        menuSelfParam.setRemark(componentSelfParam.getRemark());
                        getService(MenuSelfParamService.class).save(menuSelfParam);
                    }
                }
            }
            List<ConstructSelfParam> constructSelfParamList = getService(ConstructSelfParamService.class).getBySelfParamId(componentSelfParam.getId());
            if (CollectionUtils.isNotEmpty(constructSelfParamList)) {
                for (ConstructSelfParam constructSelfParam : constructSelfParamList) {
                    if (!componentSelfParam.getName().equals(constructSelfParam.getName())
                            || !componentSelfParam.getType().equals(constructSelfParam.getType())
                            || !componentSelfParam.getOptions().equals(constructSelfParam.getOptions())
                            || !componentSelfParam.getRemark().equals(constructSelfParam.getRemark())) {
                        constructSelfParam.setName(componentSelfParam.getName());
                        constructSelfParam.setType(componentSelfParam.getType());
                        constructSelfParam.setOptions(componentSelfParam.getOptions());
                        constructSelfParam.setRemark(componentSelfParam.getRemark());
                        getService(ConstructSelfParamService.class).save(constructSelfParam);
                    }
                }
            }
            List<ConstructDetailSelfParam> constructDetailSelfParamList = getService(ConstructDetailSelfParamService.class).getBySelfParamId(
                    componentSelfParam.getId());
            if (CollectionUtils.isNotEmpty(constructDetailSelfParamList)) {
                for (ConstructDetailSelfParam constructDetailSelfParam : constructDetailSelfParamList) {
                    if (!componentSelfParam.getName().equals(constructDetailSelfParam.getName())
                            || !componentSelfParam.getType().equals(constructDetailSelfParam.getType())
                            || !componentSelfParam.getOptions().equals(constructDetailSelfParam.getOptions())
                            || !componentSelfParam.getRemark().equals(constructDetailSelfParam.getRemark())) {
                        constructDetailSelfParam.setName(componentSelfParam.getName());
                        constructDetailSelfParam.setType(componentSelfParam.getType());
                        constructDetailSelfParam.setOptions(componentSelfParam.getOptions());
                        constructDetailSelfParam.setRemark(componentSelfParam.getRemark());
                        getService(ConstructDetailSelfParamService.class).save(constructDetailSelfParam);
                    }
                }
            }
        } else {
            componentSelfParam = getDao().save(entity);
            List<Construct> constructList = getService(ConstructService.class).getByBaseComponentVersionId(componentSelfParam.getComponentVersionId());
            List<Menu> menuList = new ArrayList<Menu>();
            List<ConstructDetail> constructDetailList = new ArrayList<ConstructDetail>();
            menuList.addAll(getService(MenuService.class).getByComponentVersionId(componentSelfParam.getComponentVersionId()));
            constructDetailList.addAll(getService(ConstructDetailService.class).getByComponentVersionId(componentSelfParam.getComponentVersionId()));
            if (CollectionUtils.isNotEmpty(constructList)) {
                ConstructSelfParam constructSelfParam = null;
                for (Construct construct : constructList) {
                    constructSelfParam = new ConstructSelfParam();
                    constructSelfParam.setComponentVersionId(componentSelfParam.getComponentVersionId());
                    constructSelfParam.setConstructId(construct.getId());
                    constructSelfParam.setSelfParamId(componentSelfParam.getId());
                    constructSelfParam.setName(componentSelfParam.getName());
                    constructSelfParam.setType(componentSelfParam.getType());
                    constructSelfParam.setValue(componentSelfParam.getValue());
                    constructSelfParam.setText(componentSelfParam.getValue());
                    constructSelfParam.setRemark(componentSelfParam.getRemark());
                    constructSelfParam.setOptions(componentSelfParam.getOptions());
                    getService(ConstructSelfParamService.class).save(constructSelfParam);
                    menuList.addAll(getService(MenuService.class).getByComponentVersionId(construct.getAssembleComponentVersion().getId()));
                    constructDetailList.addAll(getService(ConstructDetailService.class)
                            .getByComponentVersionId(construct.getAssembleComponentVersion().getId()));
                }
            }
            if (CollectionUtils.isNotEmpty(menuList)) {
                MenuSelfParam menuSelfParam = null;
                for (Menu menu : menuList) {
                    menuSelfParam = new MenuSelfParam();
                    menuSelfParam.setComponentVersionId(componentSelfParam.getComponentVersionId());
                    menuSelfParam.setMenuId(menu.getId());
                    menuSelfParam.setSelfParamId(componentSelfParam.getId());
                    menuSelfParam.setName(componentSelfParam.getName());
                    menuSelfParam.setType(componentSelfParam.getType());
                    menuSelfParam.setValue(componentSelfParam.getValue());
                    menuSelfParam.setText(componentSelfParam.getValue());
                    menuSelfParam.setRemark(componentSelfParam.getRemark());
                    menuSelfParam.setOptions(componentSelfParam.getOptions());
                    getService(MenuSelfParamService.class).save(menuSelfParam);
                }
            }
            if (CollectionUtils.isNotEmpty(constructDetailList)) {
                ConstructDetailSelfParam constructDetailSelfParam = null;
                for (ConstructDetail constructDetail : constructDetailList) {
                    constructDetailSelfParam = new ConstructDetailSelfParam();
                    constructDetailSelfParam.setComponentVersionId(componentSelfParam.getComponentVersionId());
                    constructDetailSelfParam.setConstructDetailId(constructDetail.getId());
                    constructDetailSelfParam.setSelfParamId(componentSelfParam.getId());
                    constructDetailSelfParam.setName(componentSelfParam.getName());
                    constructDetailSelfParam.setType(componentSelfParam.getType());
                    constructDetailSelfParam.setValue(componentSelfParam.getValue());
                    constructDetailSelfParam.setText(componentSelfParam.getValue());
                    constructDetailSelfParam.setRemark(componentSelfParam.getRemark());
                    constructDetailSelfParam.setOptions(componentSelfParam.getOptions());
                    getService(ConstructDetailSelfParamService.class).save(constructDetailSelfParam);
                }
            }
        }
        return componentSelfParam;
    }

    /*
     * (non-Javadoc)
     * @see com.ces.config.dhtmlx.servie.base.ConfigDefineDaoService#delete(java.lang.String)
     */
    @Override
    @Transactional
    public void delete(String ids) {
        String[] selfParamIds = ids.split(",");
        for (int i = 0; i < selfParamIds.length; i++) {
            getDaoFromContext(ConstructSelfParamDao.class).deleteBySelfParamId(selfParamIds[i]);
            getDaoFromContext(ConstructDetailSelfParamDao.class).deleteBySelfParamId(selfParamIds[i]);
            getDaoFromContext(MenuSelfParamDao.class).deleteBySelfParamId(selfParamIds[i]);
            getDao().delete(selfParamIds[i]);
        }
    }

    /**
     * 根据名称和构件版本ID获取构件自身参数
     * 
     * @param name 名称
     * @param componentVersionId 构件版本ID
     * @return ComponentSelfParam
     */
    public ComponentSelfParam getByNameAndComponentVersionId(String name, String componentVersionId) {
        return getDao().getByNameAndComponentVersionId(name, componentVersionId);
    }

    /**
     * 根据构件版本ID获取构件自身配置参数
     * 
     * @param componentVersionId 构件版本ID
     * @return List<ComponentSelfParam>
     */
    public List<ComponentSelfParam> getByComponentVersionId(String componentVersionId) {
        return getDao().getByComponentVersionId(componentVersionId);
    }

    /**
     * 根据构件版本ID获取构件自身配置参数
     * 
     * @param componentVersionIds 构件版本IDs
     * @return List<ComponentSelfParam>
     */
    public List<ComponentSelfParam> getByComponentVersionIds(String componentVersionIds) {
        List<ComponentSelfParam> componentSelfParamList = new ArrayList<ComponentSelfParam>();
        if (StringUtil.isNotEmpty(componentVersionIds)) {
            String hql = "from ComponentSelfParam t where t.componentVersionId in ('" + componentVersionIds.replace(",", "','") + "')";
            componentSelfParamList = DatabaseHandlerDao.getInstance().queryEntityForList(hql, ComponentSelfParam.class);
        }
        return componentSelfParamList;
    }

    /**
     * 根据构件版本ID删除构件自身配置参数
     * 
     * @param componentVersionId 构件版本ID
     */
    @Transactional
    public void deleteByComponentVersionId(String componentVersionId) {
        getService(ConstructSelfParamService.class).deleteByComponentVersionId(componentVersionId);
        getService(ConstructDetailSelfParamService.class).deleteByComponentVersionId(componentVersionId);
        getDao().deleteByComponentVersionId(componentVersionId);
        ComponentParamsUtil.removeComponentSelfParamList(componentVersionId);
    }
}
