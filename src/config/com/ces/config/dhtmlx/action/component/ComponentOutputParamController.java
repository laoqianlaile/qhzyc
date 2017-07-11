package com.ces.config.dhtmlx.action.component;

import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.component.ComponentOutputParamDao;
import com.ces.config.dhtmlx.entity.component.ComponentOutputParam;
import com.ces.config.dhtmlx.service.component.ComponentOutputParamService;

/**
 * 构件出参Controller
 * 
 * @author wanglei
 * @date 2014-03-18
 */
public class ComponentOutputParamController extends
        ConfigDefineServiceDaoController<ComponentOutputParam, ComponentOutputParamService, ComponentOutputParamDao> {

    private static final long serialVersionUID = 5448108829911125571L;

    /*
     * (non-Javadoc)
     * 
     * @see com.ces.xarch.core.web.struts2.BaseController#initModel()
     */
    @Override
    protected void initModel() {
        setModel(new ComponentOutputParam());
    }

    /*
     * (非 Javadoc) <p>标题: setService</p> <p>描述: 注入自定义服务层SERVICE</p> @param service
     * 
     * @see com.ces.config.dhtmlx.action.base.StringIDDhtmlxConfigController#setService(com.ces.xarch.core.service.
     *      StringIDService)
     */
    @Autowired
    @Qualifier("componentOutputParamService")
    @Override
    protected void setService(ComponentOutputParamService service) {
        super.setService(service);
    }

    /**
     * 校验字段是否已经存在
     * 
     * @return Object
     */
    public Object validateFields() {
        ComponentOutputParam componentOutputParam = (ComponentOutputParam) getModel();
        ComponentOutputParam temp = getService().getByNameAndComponentVersionId(componentOutputParam.getName(),
                componentOutputParam.getComponentVersionId());
        boolean nameExist = false;
        if (null != componentOutputParam.getId() && !"".equals(componentOutputParam.getId())) {
            ComponentOutputParam oldComponentOutputParam = getService().getByID(componentOutputParam.getId());
            if (null != temp && null != oldComponentOutputParam
                    && !temp.getId().equals(oldComponentOutputParam.getId())) {
                nameExist = true;
            }
        } else {
            if (null != temp) {
                nameExist = true;
            }
        }
        setReturnData("{'nameExist' : " + nameExist + "}");
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

}
