package com.ces.config.dhtmlx.action.component;

import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.component.ComponentSelfParamDao;
import com.ces.config.dhtmlx.entity.component.ComponentSelfParam;
import com.ces.config.dhtmlx.service.component.ComponentSelfParamService;

/**
 * 构件自身参数Controller
 * 
 * @author wanglei
 * @date 2014-04-09
 */
public class ComponentSelfParamController extends ConfigDefineServiceDaoController<ComponentSelfParam, ComponentSelfParamService, ComponentSelfParamDao> {

    private static final long serialVersionUID = 5448108829911125571L;

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.web.struts2.BaseController#initModel()
     */
    @Override
    protected void initModel() {
        setModel(new ComponentSelfParam());
    }

    /*
     * (非 Javadoc) <p>标题: setService</p> <p>描述: 注入自定义服务层SERVICE</p> @param service
     * @see com.ces.config.dhtmlx.action.base.StringIDDhtmlxConfigController#setService(com.ces.xarch.core.service.
     * StringIDService)
     */
    @Autowired
    @Qualifier("componentSelfParamService")
    @Override
    protected void setService(ComponentSelfParamService service) {
        super.setService(service);
    }

    /**
     * 校验字段是否已经存在
     * 
     * @return Object
     */
    public Object validateFields() {
        ComponentSelfParam componentSelfParam = (ComponentSelfParam) getModel();
        ComponentSelfParam temp = getService().getByNameAndComponentVersionId(componentSelfParam.getName(), componentSelfParam.getComponentVersionId());
        boolean nameExist = false;
        if (null != componentSelfParam.getId() && !"".equals(componentSelfParam.getId())) {
            ComponentSelfParam oldComponentSelfParam = getService().getByID(componentSelfParam.getId());
            if (null != temp && null != oldComponentSelfParam && !temp.getId().equals(oldComponentSelfParam.getId())) {
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
