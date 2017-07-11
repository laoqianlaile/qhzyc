package com.ces.config.dhtmlx.service.component;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.dao.component.ComponentReserveZoneDao;
import com.ces.config.dhtmlx.entity.appmanage.LogicTableDefine;
import com.ces.config.dhtmlx.entity.component.ComponentReserveZone;
import com.ces.config.dhtmlx.service.appmanage.LogicTableDefineService;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.dhtmlx.service.construct.ConstructDetailService;
import com.ces.config.utils.StringUtil;

/**
 * 构件预留区实体类
 * 
 * @author wanglei
 * @date 2013-08-08
 */
@Component("componentReserveZoneService")
public class ComponentReserveZoneService extends ConfigDefineDaoService<ComponentReserveZone, ComponentReserveZoneDao> {

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.service.StringIDService#setDaoUnBinding(com.ces.xarch.core.persistence.jpa.StringIDDao)
     */
    @Autowired
    @Qualifier("componentReserveZoneDao")
    @Override
    protected void setDaoUnBinding(ComponentReserveZoneDao dao) {
        super.setDaoUnBinding(dao);
    }

    /*
     * (non-Javadoc)
     * @see com.ces.config.dhtmlx.servie.base.ConfigDefineDaoService#delete(java.lang.String)
     */
    @Override
    @Transactional
    public void delete(String ids) {
        String[] reserveZoneIds = ids.split(",");
        for (int i = 0; i < reserveZoneIds.length; i++) {
            getService(ConstructDetailService.class).deleteByReserveZoneId(reserveZoneIds[i]);
            getDao().delete(reserveZoneIds[i]);
        }
    }

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.service.AbstractService#save(com.ces.xarch.core.entity.BaseEntity)
     */
    @Override
    @Transactional
    public ComponentReserveZone save(ComponentReserveZone entity) {
        if (entity.getShowOrder() == null) {
            if (entity.getIsCommon()) {
                entity.setShowOrder(getCommonMaxShowOrder() + 1);
            } else {
                entity.setShowOrder(getMaxShowOrder(entity.getComponentVersionId()) + 1);
            }
        }
        return super.save(entity);
    }

    /**
     * 调整公用预留区的顺序
     */
    @Transactional
    public void adjustCommonShowOrder() {
        List<LogicTableDefine> logicTableDefineList = getService(LogicTableDefineService.class).getAllLogicTableDefine();
        if (CollectionUtils.isNotEmpty(logicTableDefineList)) {
            ComponentReserveZone reserveZone1 = null;
            ComponentReserveZone reserveZone2 = null;
            ComponentReserveZone reserveZone3 = null;
            List<ComponentReserveZone> commonReserveZoneList = new ArrayList<ComponentReserveZone>();
            for (LogicTableDefine logicTableDefine : logicTableDefineList) {
                Integer reserveZoneMaxShowOrder = logicTableDefine.getShowOrder() * 3;
                reserveZone1 = getService(ComponentReserveZoneService.class).getCommonReserveZoneByName(logicTableDefine.getCode() + "_GRID");
                reserveZone2 = getService(ComponentReserveZoneService.class).getCommonReserveZoneByName(logicTableDefine.getCode() + "_FORM");
                reserveZone3 = getService(ComponentReserveZoneService.class).getCommonReserveZoneByName(logicTableDefine.getCode() + "_GRID_LINK");
                if (reserveZone1 != null) {
                    reserveZone1.setShowOrder(reserveZoneMaxShowOrder - 2);
                    commonReserveZoneList.add(reserveZone1);
                }
                if (reserveZone2 != null) {
                    reserveZone2.setShowOrder(reserveZoneMaxShowOrder - 1);
                    commonReserveZoneList.add(reserveZone2);
                }
                if (reserveZone3 != null) {
                    reserveZone3.setShowOrder(reserveZoneMaxShowOrder);
                    commonReserveZoneList.add(reserveZone3);
                }
            }
            getDao().save(commonReserveZoneList);
        }
    }

    /**
     * 根据构件版本ID获取构件预留区
     * 
     * @param componentVersionId 构件版本ID
     * @return List<ComponentReserveZone>
     */
    public List<ComponentReserveZone> getByComponentVersionId(String componentVersionId) {
        return getDao().getByComponentVersionId(componentVersionId);
    }

    /**
     * 根据构件版本ID获取构件预留区
     * 
     * @param componentVersionIds 构件版本IDs
     * @return List<ComponentReserveZone>
     */
    public List<ComponentReserveZone> getByComponentVersionIds(String componentVersionIds) {
        List<ComponentReserveZone> componentReserveZoneList = new ArrayList<ComponentReserveZone>();
        if (StringUtil.isNotEmpty(componentVersionIds)) {
            String hql = "from ComponentReserveZone t where t.componentVersionId in ('" + componentVersionIds.replace(",", "','") + "')";
            componentReserveZoneList = DatabaseHandlerDao.getInstance().queryEntityForList(hql, ComponentReserveZone.class);
        }
        return componentReserveZoneList;
    }

    /**
     * 根据构件版本ID和page获取构件预留区
     * 
     * @param componentVersionId 构件版本ID
     * @param page 页面
     * @return List<ComponentReserveZone>
     */
    public List<ComponentReserveZone> getByComponentVersionIdAndPage(String componentVersionId, String page) {
        return getDao().getByComponentVersionIdAndPage(componentVersionId, page);
    }

    /**
     * 获取某个组合构件中使用的构件预留区
     * 
     * @param constructId 组合构件绑定关系ID
     * @param componentVersionId 构件版本ID
     * @param page 页面
     * @return List<ComponentReserveZone>
     */
    public List<ComponentReserveZone> getUsedReserveZone(String constructId, String componentVersionId, String page) {
        if (StringUtil.isEmpty(componentVersionId)) {
            return getDao().getUsedCommonReserveZone(constructId, page);
        } else {
            return getDao().getUsedReserveZone(constructId, componentVersionId, page);
        }
    }

    /**
     * 根据构件版本ID和名称获取构件预留区
     * 
     * @param componentVersionId 构件版本ID
     * @param name 名称
     * @param page 页面
     * @return List<ComponentReserveZone>
     */
    public ComponentReserveZone getByComponentVersionIdAndNameAndPage(String componentVersionId, String name, String page) {
        return getDao().getByComponentVersionIdAndNameAndPage(componentVersionId, name, page);
    }

    /**
     * 获取所有公共预留区
     * 
     * @return List<ComponentReserveZone>
     */
    public List<ComponentReserveZone> getAllCommonReserveZone() {
        return getDao().getAllCommonReserveZone();
    }

    /**
     * 根据预留区名称获取构件预留区
     * 
     * @param name 预留区名称
     * @return ComponentReserveZone
     */
    public ComponentReserveZone getCommonReserveZoneByName(String name) {
        return getDao().getCommonReserveZoneByName(name);
    }

    /**
     * 根据预留区类型获取公共预留区
     * 
     * @param type 预留区类型
     * @return List<ComponentReserveZone>
     */
    public List<ComponentReserveZone> getCommonReserveZoneByType(String type) {
        return getDao().getCommonReserveZoneByType(type);
    }

    /**
     * 获取构件版本ID下预留区最大显示顺序
     * 
     * @param componentVersionId 构件版本ID
     */
    public Integer getMaxShowOrder(String componentVersionId) {
        Integer maxShowOrder = getDao().getMaxShowOrder(componentVersionId);
        if (maxShowOrder == null) {
            maxShowOrder = 0;
        }
        return maxShowOrder;
    }

    /**
     * 获取公用预留区最大显示顺序
     */
    public Integer getCommonMaxShowOrder() {
        Integer maxShowOrder = getDao().getCommonMaxShowOrder();
        if (maxShowOrder == null) {
            maxShowOrder = 0;
        }
        return maxShowOrder;
    }

    /**
     * 根据构件版本ID删除该构件版本下的预留区
     * 
     * @param componentVersionId 构件版本ID
     */
    @Transactional
    public void deleteByComponentVersionId(String componentVersionId) {
        List<ComponentReserveZone> componentReserveZoneList = getDao().getByComponentVersionId(componentVersionId);
        for (ComponentReserveZone componentReserveZone : componentReserveZoneList) {
            getService(ConstructDetailService.class).deleteByReserveZoneId(componentReserveZone.getId());
        }
        getDao().deleteByComponentVersionId(componentVersionId);
    }

    /**
     * 根据预留区名称删除公共预留区
     * 
     * @param name 预留区名称
     */
    @Transactional
    public void deleteCommonReserveZoneByName(String name) {
        getDao().deleteCommonReserveZoneByName(name);
    }
}
