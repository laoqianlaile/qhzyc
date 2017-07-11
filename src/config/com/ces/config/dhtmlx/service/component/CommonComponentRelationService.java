package com.ces.config.dhtmlx.service.component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.ces.config.dhtmlx.dao.component.CommonComponentRelationDao;
import com.ces.config.dhtmlx.dao.component.ComponentVersionDao;
import com.ces.config.dhtmlx.entity.component.CommonComponentRelation;
import com.ces.config.dhtmlx.entity.component.ComponentVersion;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;

/**
 * 公用构件依赖关系Service
 * 
 * @author wanglei
 * @date 2014-06-09
 */
@Component("commonComponentRelationService")
public class CommonComponentRelationService extends ConfigDefineDaoService<CommonComponentRelation, CommonComponentRelationDao> {

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.service.StringIDService#setDaoUnBinding(com.ces.xarch.core.persistence.jpa.StringIDDao)
     */
    @Autowired
    @Qualifier("commonComponentRelationDao")
    @Override
    protected void setDaoUnBinding(CommonComponentRelationDao dao) {
        super.setDaoUnBinding(dao);
    }

    /**
     * 根据构件版本ID获取公用构件依赖关系
     * 
     * @param componentVersionId 构件版本ID
     * @return List<CommonComponentRelation>
     */
    public List<CommonComponentRelation> getByComponentVersionId(String componentVersionId) {
        return getDao().getByComponentVersionId(componentVersionId);
    }

    /**
     * 根据构件版本ID获取公用构件依赖关系
     * 
     * @param componentVersionId 构件版本ID
     * @return CommonComponentRelation
     */
    public CommonComponentRelation getCommonComponentRelation(String componentVersionId, String commonComponentVersionId) {
        return getDao().getCommonComponentRelation(componentVersionId, commonComponentVersionId);
    }

    /**
     * 根据构件版本ID获取该构件下所有公用构件
     * 
     * @param componentVersionId 构件版本ID
     * @return Set<ComponentVersion>
     */
    public Set<ComponentVersion> getAllCommonComponentVersion(String componentVersionId) {
        Set<ComponentVersion> componentVersionSet = new HashSet<ComponentVersion>();
        getCommonComponentVersions(componentVersionId, componentVersionSet);
        return componentVersionSet;
    }

    /**
     * 根据构件版本ID获取该构件下所有公用构件
     * 
     * @param componentVersionId 构件版本ID
     * @param Set<ComponentVersion> 公用构件
     */
    private void getCommonComponentVersions(String componentVersionId, Set<ComponentVersion> componentVersionSet) {
        List<ComponentVersion> commonComponentList = getDaoFromContext(ComponentVersionDao.class).getCommonComponentList(componentVersionId);
        if (CollectionUtils.isNotEmpty(commonComponentList)) {
            for (ComponentVersion componentVersion : commonComponentList) {
                if (!componentVersionSet.contains(componentVersion)) {
                    componentVersionSet.add(componentVersion);
                    getCommonComponentVersions(componentVersion.getId(), componentVersionSet);
                }
            }
        }
    }

    /**
     * 根据公用构件版本ID获取所用依赖该公用构件的构件
     * 
     * @param commonComponentVersionId 公用构件版本ID
     * @return Set<ComponentVersion>
     */
    public Set<ComponentVersion> getAllComponentVersion(String commonComponentVersionId) {
        Set<ComponentVersion> componentVersionSet = new HashSet<ComponentVersion>();
        getComponentVersions(commonComponentVersionId, componentVersionSet);
        return componentVersionSet;
    }

    /**
     * 根据公用构件版本ID获取所用依赖该公用构件的构件
     * 
     * @param commonComponentVersionId 公用构件版本ID
     * @param Set<ComponentVersion> 公用构件
     */
    private void getComponentVersions(String commonComponentVersionId, Set<ComponentVersion> componentVersionSet) {
        List<ComponentVersion> componentList = getDaoFromContext(ComponentVersionDao.class).getComponentList(commonComponentVersionId);
        if (CollectionUtils.isNotEmpty(componentList)) {
            for (ComponentVersion componentVersion : componentList) {
                if (!componentVersionSet.contains(componentVersion)) {
                    componentVersionSet.add(componentVersion);
                    getCommonComponentVersions(componentVersion.getId(), componentVersionSet);
                }
            }
        }
    }

    /**
     * 删除构件关联的公用构件
     * 
     * @param componentVersionId 构件ID
     * @param commonComponentVersionIds 关联公用构件IDs
     */
    @Transactional
    public void saveCommonComponent(String componentVersionId, String commonComponentVersionIds) {
        Assert.notNull(componentVersionId, "构件ID不能为空");
        Assert.notNull(commonComponentVersionIds, "关联公用构件ID不能为空");
        String[] commonComponentVersionIdArray = commonComponentVersionIds.split(",");
        if (commonComponentVersionIdArray.length > 0) {
            CommonComponentRelation commonComponentRelation = null;
            for (String commonComponentVersionId : commonComponentVersionIdArray) {
                commonComponentRelation = new CommonComponentRelation();
                commonComponentRelation.setComponentVersionId(componentVersionId);
                commonComponentRelation.setCommonComponentVersionId(commonComponentVersionId);
                getDao().save(commonComponentRelation);
            }
        }
    }

    /**
     * 删除构件关联的公用构件
     * 
     * @param componentVersionId 构件ID
     * @param commonComponentVersionIds 关联公用构件IDs
     */
    @Transactional
    public void deleteCommonComponent(String componentVersionId, String commonComponentVersionIds) {
        Assert.notNull(componentVersionId, "构件ID不能为空");
        Assert.notNull(commonComponentVersionIds, "关联公用构件ID不能为空");
        String[] commonComponentVersionIdArray = commonComponentVersionIds.split(",");
        if (commonComponentVersionIdArray.length > 0) {
            CommonComponentRelation commonComponentRelation = null;
            for (String commonComponentVersionId : commonComponentVersionIdArray) {
                commonComponentRelation = getDao().getCommonComponentRelation(componentVersionId, commonComponentVersionId);
                if (commonComponentRelation != null) {
                    getDao().delete(commonComponentRelation);
                }
            }
        }
    }

    /**
     * 根据构件版本ID删除公用构件依赖关系
     * 
     * @param componentVersionId 构件版本ID
     */
    @Transactional
    public void deleteByComponentVersionId(String componentVersionId) {
        getDao().deleteByComponentVersionId(componentVersionId);
    }

    /**
     * 根据公用构件版本ID删除公用构件依赖关系
     * 
     * @param commonComponentVersionId 公用构件版本ID
     */
    @Transactional
    public void deleteByCommonComponentVersionId(String commonComponentVersionId) {
        getDao().deleteByCommonComponentVersionId(commonComponentVersionId);
    }
}