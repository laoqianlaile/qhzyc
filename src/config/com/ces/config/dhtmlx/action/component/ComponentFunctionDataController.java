package com.ces.config.dhtmlx.action.component;

import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.component.ComponentFunctionDataDao;
import com.ces.config.dhtmlx.entity.component.ComponentFunctionData;
import com.ces.config.dhtmlx.service.component.ComponentFunctionDataService;

/**
 * 构件方法返回值Controller
 * 
 * @author wanglei
 * @date 2014-03-18
 */
public class ComponentFunctionDataController extends
        ConfigDefineServiceDaoController<ComponentFunctionData, ComponentFunctionDataService, ComponentFunctionDataDao> {

    private static final long serialVersionUID = 5448108829911125571L;

    /*
     * (non-Javadoc)
     * 
     * @see com.ces.xarch.core.web.struts2.BaseController#initModel()
     */
    @Override
    protected void initModel() {
        setModel(new ComponentFunctionData());
    }

    /*
     * (非 Javadoc) <p>标题: setService</p> <p>描述: 注入自定义服务层SERVICE</p> @param service
     * 
     * @see com.ces.config.dhtmlx.action.base.StringIDDhtmlxConfigController#setService(com.ces.xarch.core.service.
     *      StringIDService)
     */
    @Autowired
    @Qualifier("componentFunctionDataService")
    @Override
    protected void setService(ComponentFunctionDataService service) {
        super.setService(service);
    }

    /**
     * 校验字段是否已经存在
     * 
     * @return Object
     */
    public Object validateFields() {
        ComponentFunctionData componentFunctionData = (ComponentFunctionData) getModel();
        ComponentFunctionData temp = getService().getByNameAndFunctionId(componentFunctionData.getName(),
                componentFunctionData.getFunctionId());
        boolean nameExist = false;
        if (null != componentFunctionData.getId() && !"".equals(componentFunctionData.getId())) {
            ComponentFunctionData oldComponentFunctionData = getService().getByID(componentFunctionData.getId());
            if (null != temp && null != oldComponentFunctionData
                    && !temp.getId().equals(oldComponentFunctionData.getId())) {
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
