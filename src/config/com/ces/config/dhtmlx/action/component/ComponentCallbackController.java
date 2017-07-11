package com.ces.config.dhtmlx.action.component;

import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.component.ComponentCallbackDao;
import com.ces.config.dhtmlx.entity.component.ComponentCallback;
import com.ces.config.dhtmlx.service.component.ComponentCallbackService;

/**
 * 构件回调函数Controller
 * 
 * @author wanglei
 * @date 2014-03-18
 */
public class ComponentCallbackController extends
        ConfigDefineServiceDaoController<ComponentCallback, ComponentCallbackService, ComponentCallbackDao> {

    private static final long serialVersionUID = 5448108829911125571L;

    /*
     * (non-Javadoc)
     * 
     * @see com.ces.xarch.core.web.struts2.BaseController#initModel()
     */
    @Override
    protected void initModel() {
        setModel(new ComponentCallback());
    }

    /*
     * (非 Javadoc) <p>标题: setService</p> <p>描述: 注入自定义服务层SERVICE</p> @param service
     * 
     * @see com.ces.config.dhtmlx.action.base.StringIDDhtmlxConfigController#setService(com.ces.xarch.core.service.
     *      StringIDService)
     */
    @Autowired
    @Qualifier("componentCallbackService")
    @Override
    protected void setService(ComponentCallbackService service) {
        super.setService(service);
    }

    /**
     * 校验字段是否已经存在
     * 
     * @return Object
     */
    public Object validateFields() {
        ComponentCallback componentCallback = (ComponentCallback) getModel();
        ComponentCallback temp = getService().getByNameAndComponentVersionId(componentCallback.getName(),
                componentCallback.getComponentVersionId());
        boolean nameExist = false;
        if (null != componentCallback.getId() && !"".equals(componentCallback.getId())) {
            ComponentCallback oldComponentCallback = getService().getByID(componentCallback.getId());
            if (null != temp && null != oldComponentCallback && !temp.getId().equals(oldComponentCallback.getId())) {
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
