package com.ces.config.dhtmlx.action.component;

import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.component.ComponentFunctionDao;
import com.ces.config.dhtmlx.entity.component.ComponentFunction;
import com.ces.config.dhtmlx.service.component.ComponentFunctionService;

/**
 * 构件方法Controller
 * 
 * @author wanglei
 * @date 2014-03-18
 */
public class ComponentFunctionController extends
        ConfigDefineServiceDaoController<ComponentFunction, ComponentFunctionService, ComponentFunctionDao> {

    private static final long serialVersionUID = 5448108829911125571L;

    /*
     * (non-Javadoc)
     * 
     * @see com.ces.xarch.core.web.struts2.BaseController#initModel()
     */
    @Override
    protected void initModel() {
        setModel(new ComponentFunction());
    }

    /*
     * (非 Javadoc) <p>标题: setService</p> <p>描述: 注入自定义服务层SERVICE</p> @param service
     * 
     * @see com.ces.config.dhtmlx.action.base.StringIDDhtmlxConfigController#setService(com.ces.xarch.core.service.
     *      StringIDService)
     */
    @Autowired
    @Qualifier("componentFunctionService")
    @Override
    protected void setService(ComponentFunctionService service) {
        super.setService(service);
    }

    /**
     * 校验字段是否已经存在
     * 
     * @return Object
     */
    public Object validateFields() {
        ComponentFunction componentFunction = (ComponentFunction) getModel();
        ComponentFunction temp = getService().getByNameAndComponentVersionId(componentFunction.getName(),
                componentFunction.getComponentVersionId());
        boolean nameExist = false;
        if (null != componentFunction.getId() && !"".equals(componentFunction.getId())) {
            ComponentFunction oldComponentFunction = getService().getByID(componentFunction.getId());
            if (null != temp && null != oldComponentFunction && !temp.getId().equals(oldComponentFunction.getId())) {
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
