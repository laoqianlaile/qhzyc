package com.ces.config.dhtmlx.action.component;

import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.component.ComponentSystemParameterRelationDao;
import com.ces.config.dhtmlx.entity.component.ComponentSystemParameterRelation;
import com.ces.config.dhtmlx.entity.component.ComponentVersion;
import com.ces.config.dhtmlx.service.component.ComponentSystemParameterRelationService;
import com.ces.config.dhtmlx.service.component.ComponentVersionService;
import com.ces.xarch.core.exception.FatalException;

/**
 * 构件版本中系统参数和系统中系统参数的关联关系Controller
 * 
 * @author wanglei
 * @date 2013-08-20
 */
public class ComponentSystemParameterRelationController
        extends
        ConfigDefineServiceDaoController<ComponentSystemParameterRelation, ComponentSystemParameterRelationService, ComponentSystemParameterRelationDao> {

    private static final long serialVersionUID = 5448108829911125571L;

    /*
     * (non-Javadoc)
     * 
     * @see com.ces.xarch.core.web.struts2.BaseController#initModel()
     */
    @Override
    protected void initModel() {
        setModel(new ComponentSystemParameterRelation());
    }

    /*
     * (非 Javadoc) <p>标题: setService</p> <p>描述: 注入自定义服务层SERVICE</p> @param service
     * 
     * @see com.ces.config.dhtmlx.action.base.StringIDDhtmlxConfigController#setService(com.ces.xarch.core.service.
     *      StringIDService)
     */
    @Autowired
    @Qualifier("componentSystemParameterRelationService")
    @Override
    protected void setService(ComponentSystemParameterRelationService service) {
        super.setService(service);
    }

    /**
     * 获取构件系统参数列表数据
     * 
     * @return Object
     * @throws FatalException
     */
    public Object getComponentSystemParamList() throws FatalException {
        String componentVersionId = getParameter("componentVersionId");
        setReturnData(getService().getComponentSystemParamList(componentVersionId));
        return new DefaultHttpHeaders("success").disableCaching();
    }

    /**
     * 获取系统参数列表数据
     * 
     * @return Object
     * @throws FatalException
     */
    public Object getSystemParamList() throws FatalException {
        String componentVersionId = getParameter("componentVersionId");
        setReturnData(getService().getSystemParamList(componentVersionId));
        return new DefaultHttpHeaders("success").disableCaching();
    }

    /**
     * 获取方法返回值列表数据
     * 
     * @return Object
     * @throws FatalException
     */
    public Object getComponentSystemParamRelationList() throws FatalException {
        String componentVersionId = getParameter("componentVersionId");
        setReturnData(getService().getComponentSystemParamRelationList(componentVersionId));
        return new DefaultHttpHeaders("success").disableCaching();
    }

    /**
     * 保存方法返回值列表数据
     * 
     * @return Object
     */
    public Object saveComponentSystemParameterRelation() {
        try {
            String rowIds = getParameter("rowIds");
            String componentVersionId = getParameter("componentVersionId");
            String systemParamConfig = getParameter("systemParamConfig");
            getService().saveComponentSystemParameterRelation(rowIds, componentVersionId);
            ComponentVersion componentVersion = getService(ComponentVersionService.class).getByID(componentVersionId);
            componentVersion.setSystemParamConfig(systemParamConfig);
            getService(ComponentVersionService.class).save(componentVersion);
            setReturnData("{'success':true}");
        } catch (Exception e) {
            setReturnData("{'success':false}");
            setMessage(e.toString());
        }
        return new DefaultHttpHeaders("success").disableCaching();
    }
}