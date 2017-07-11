package com.ces.config.dhtmlx.service.systemversion;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.dao.systemvesion.SystemVersionDao;
import com.ces.config.dhtmlx.entity.systemversion.SystemVersion;
import com.ces.config.dhtmlx.entity.systemversion.SystemVersionResource;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.utils.StringUtil;

/**
 * 系统版本Service
 * 
 * @author wanglei
 * @date 2015-04-18
 */
@Component("systemVersionService")
public class SystemVersionService extends ConfigDefineDaoService<SystemVersion, SystemVersionDao> {

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.service.StringIDService#setDaoUnBinding(com.ces.xarch.core.persistence.jpa.StringIDDao)
     */
    @Autowired
    @Qualifier("systemVersionDao")
    @Override
    protected void setDaoUnBinding(SystemVersionDao dao) {
        super.setDaoUnBinding(dao);
    }

    /**
     * 获取系统版本
     * 
     * @param systemId 系统ID
     * @param name 系统版本名称
     * @return SystemVersion
     */
    public SystemVersion getBySystemIdAndName(String systemId, String name) {
        return getDao().getBySystemIdAndName(systemId, name);
    }

    /**
     * 获取系统版本
     * 
     * @param systemId 系统ID
     * @return List<SystemVersion>
     */
    public List<SystemVersion> getBySystemId(String systemId) {
        return getDao().getBySystemId(systemId);
    }

    /**
     * 获取显示顺序范围内的系统版本
     * 
     * @param start 开始显示顺序
     * @param end 结束显示顺序
     * @param systemId 系统ID
     * @return List<SystemVersion>
     */
    public List<SystemVersion> getSystemVersionListByShowOrder(Integer start, Integer end, String systemId) {
        return getDao().getByShowOrderBetweenAndSystemId(start, end, systemId);
    }

    /**
     * 获取系统下的系统版本最大显示顺序
     * 
     * @param systemId 系统ID
     * @return Integer
     */
    public Integer getMaxShowOrder(String systemId) {
        Integer maxShowOrder = getDao().getMaxShowOrder(systemId);
        if (maxShowOrder == null) {
            maxShowOrder = new Integer(0);
        }
        return maxShowOrder;
    }

    /**
     * 根据系统ID删除（系统版本）
     * 
     * @param systemId 系统ID
     */
    @Transactional
    public void deleteBySystemId(String systemId) {
        getDao().deleteBySystemId(systemId);
    }

    /**
     * 保存系统版本与资源关系列表数据
     * 
     * @param systemId 系统ID
     */
    @Transactional
    public void saveVersionResource(String systemId, String checkedIds) {
        if (StringUtil.isNotEmpty(checkedIds)) {
            List<SystemVersionResource> versionResourceList = new ArrayList<SystemVersionResource>();
            SystemVersionResource versionResource = null;
            String[] strs = checkedIds.split(";");
            for (String str : strs) {
                String[] resourceIds = str.split(",");
                String systemVersionId = resourceIds[0];
                for (int i = 1; i < resourceIds.length; i++) {
                    versionResource = new SystemVersionResource();
                    versionResource.setSystemVersionId(systemVersionId);
                    versionResource.setResourceId(resourceIds[i]);
                    versionResourceList.add(versionResource);
                }
                getService(SystemVersionResourceService.class).deleteBySystemVersionId(systemVersionId);
            }
            getService(SystemVersionResourceService.class).save(versionResourceList);
        }
    }
}
