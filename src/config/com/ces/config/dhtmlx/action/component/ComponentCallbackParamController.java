package com.ces.config.dhtmlx.action.component;

import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.component.ComponentCallbackParamDao;
import com.ces.config.dhtmlx.entity.component.ComponentCallbackParam;
import com.ces.config.dhtmlx.service.component.ComponentCallbackParamService;

/**
 * 构件回调函数参数Controller
 * 
 * @author wanglei
 * @date 2014-03-18
 */
public class ComponentCallbackParamController
        extends
        ConfigDefineServiceDaoController<ComponentCallbackParam, ComponentCallbackParamService, ComponentCallbackParamDao> {

    private static final long serialVersionUID = 5448108829911125571L;

    /*
     * (non-Javadoc)
     * 
     * @see com.ces.xarch.core.web.struts2.BaseController#initModel()
     */
    @Override
    protected void initModel() {
        setModel(new ComponentCallbackParam());
    }

    /*
     * (非 Javadoc) <p>标题: setService</p> <p>描述: 注入自定义服务层SERVICE</p> @param service
     * 
     * @see com.ces.config.dhtmlx.action.base.StringIDDhtmlxConfigController#setService(com.ces.xarch.core.service.
     *      StringIDService)
     */
    @Autowired
    @Qualifier("componentCallbackParamService")
    @Override
    protected void setService(ComponentCallbackParamService service) {
        super.setService(service);
    }

    /**
     * 校验字段是否已经存在
     * 
     * @return Object
     */
    public Object validateFields() {
        ComponentCallbackParam componentCallbackParam = (ComponentCallbackParam) getModel();
        ComponentCallbackParam temp = getService().getByNameAndCallbackId(componentCallbackParam.getName(),
                componentCallbackParam.getCallbackId());
        boolean nameExist = false;
        if (null != componentCallbackParam.getId() && !"".equals(componentCallbackParam.getId())) {
            ComponentCallbackParam oldComponentCallbackParam = getService().getByID(componentCallbackParam.getId());
            if (null != temp && null != oldComponentCallbackParam
                    && !temp.getId().equals(oldComponentCallbackParam.getId())) {
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
