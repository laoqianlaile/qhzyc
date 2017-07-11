package com.ces.config.dhtmlx.service.completecomponent;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.dao.completecomponent.CompleteComponentVersionDao;
import com.ces.config.dhtmlx.entity.completecomponent.CompleteComponent;
import com.ces.config.dhtmlx.entity.completecomponent.CompleteComponentVersion;
import com.ces.config.dhtmlx.entity.component.ComponentConfig;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.utils.ComponentFileUtil;
import com.ces.config.utils.FileUtil;

/**
 * 成品构件版本Service
 * 
 * @author wanglei
 * @date 2014-02-18
 */
@Component("completeComponentVersionService")
public class CompleteComponentVersionService extends ConfigDefineDaoService<CompleteComponentVersion, CompleteComponentVersionDao> {

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.service.StringIDService#setDaoUnBinding(com.ces.xarch.core.persistence.jpa.StringIDDao)
     */
    @Autowired
    @Qualifier("completeComponentVersionDao")
    @Override
    protected void setDaoUnBinding(CompleteComponentVersionDao dao) {
        super.setDaoUnBinding(dao);
    }

    /**
     * 根据成品构件ID和版本号获取成品构件版本
     * 
     * @param componentId 成品构件ID
     * @param version 成品构件版本号
     * @return CompleteComponentVersion
     */
    public CompleteComponentVersion getByComponentIdAndVersion(String componentId, String version) {
        return getDao().getByComponentIdAndVersion(componentId, version);
    }

    /**
     * 根据成品构件ID获取成品构件版本
     * 
     * @param componentId 成品构件ID
     * @return List<CompleteComponentVersion>
     */
    public List<CompleteComponentVersion> getByComponentId(String componentId) {
        return getDao().getByComponentId(componentId);
    }

    /**
     * 根据成品构件分类ID获取成品构件版本
     * 
     * @param areaId 成品构件分类ID
     * @return List<CompleteComponentVersion>
     */
    public List<CompleteComponentVersion> getByAreaId(String areaId) {
        return getDao().getByAreaId(areaId);
    }

    /**
     * 保存成品构件
     * 
     * @param componentConfig 构件配置信息
     * @return CompleteComponentVersion
     */
    @Transactional
    public CompleteComponentVersion saveComponentConfig(ComponentConfig componentConfig) {
        HttpServletRequest request = ServletActionContext.getRequest();
        CompleteComponentVersion completeComponentVersion = null;
        CompleteComponent completeComponent = getService(CompleteComponentService.class).getCompleteComponentByName(componentConfig.getName());
        if (completeComponent != null) {
            CompleteComponentVersion oldCompleteComponentVersion = getByComponentIdAndVersion(completeComponent.getId(), componentConfig.getVersion());
            if (oldCompleteComponentVersion != null) {
                // 修改构件，将旧的zip文件删掉，关联新的构件文件
                FileUtil.deleteFile(ComponentFileUtil.getCompleteCompPath() + oldCompleteComponentVersion.getPath());
                oldCompleteComponentVersion.setRemark(componentConfig.getRemark());
                oldCompleteComponentVersion.setAreaId(request.getParameter("areaId"));
                oldCompleteComponentVersion.setPath(componentConfig.getPackageFileName());
                oldCompleteComponentVersion.setImportDate(new Date());
                getDao().save(oldCompleteComponentVersion);
                completeComponentVersion = oldCompleteComponentVersion;
            } else {
                // 新增新的构件版本
                completeComponentVersion = new CompleteComponentVersion();
                completeComponentVersion.setComponent(completeComponent);
                completeComponentVersion.setVersion(componentConfig.getVersion());
                completeComponentVersion.setViews(componentConfig.getViews());
                completeComponentVersion.setRemark(componentConfig.getRemark());
                completeComponentVersion.setAreaId(request.getParameter("areaId"));
                completeComponentVersion.setPath(componentConfig.getPackageFileName());
                completeComponentVersion.setImportDate(new Date());
                getDao().save(completeComponentVersion);
            }
        } else {
            // 保存构件
            completeComponent = new CompleteComponent();
            completeComponent.setCode(componentConfig.getCode());
            completeComponent.setName(componentConfig.getName());
            completeComponent.setAlias(componentConfig.getAlias());
            completeComponent.setType(componentConfig.getType());
            getService(CompleteComponentService.class).save(completeComponent);
            // 新增新的构件版本
            completeComponentVersion = new CompleteComponentVersion();
            completeComponentVersion.setComponent(completeComponent);
            completeComponentVersion.setVersion(componentConfig.getVersion());
            completeComponentVersion.setViews(componentConfig.getViews());
            completeComponentVersion.setRemark(componentConfig.getRemark());
            completeComponentVersion.setAreaId(request.getParameter("areaId"));
            completeComponentVersion.setPath(componentConfig.getPackageFileName());
            completeComponentVersion.setImportDate(new Date());
            getDao().save(completeComponentVersion);
        }
        return completeComponentVersion;
    }

}
