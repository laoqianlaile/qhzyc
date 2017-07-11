package com.ces.config.dhtmlx.action.component;

import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.component.ComponentInputParamDao;
import com.ces.config.dhtmlx.entity.component.ComponentInputParam;
import com.ces.config.dhtmlx.service.component.ComponentInputParamService;

/**
 * 构件入参Controller
 * 
 * @author wanglei
 * @date 2014-03-10
 */
public class ComponentInputParamController extends
        ConfigDefineServiceDaoController<ComponentInputParam, ComponentInputParamService, ComponentInputParamDao> {

    private static final long serialVersionUID = 5448108829911125571L;

    /*
     * (non-Javadoc)
     * 
     * @see com.ces.xarch.core.web.struts2.BaseController#initModel()
     */
    @Override
    protected void initModel() {
        setModel(new ComponentInputParam());
    }

    /*
     * (非 Javadoc) <p>标题: setService</p> <p>描述: 注入自定义服务层SERVICE</p> @param service
     * 
     * @see com.ces.config.dhtmlx.action.base.StringIDDhtmlxConfigController#setService(com.ces.xarch.core.service.
     *      StringIDService)
     */
    @Autowired
    @Qualifier("componentInputParamService")
    @Override
    protected void setService(ComponentInputParamService service) {
        super.setService(service);
    }

    /**
     * 校验字段是否已经存在
     * 
     * @return Object
     */
    public Object validateFields() {
        ComponentInputParam componentInputParam = (ComponentInputParam) getModel();
        ComponentInputParam temp = getService().getByNameAndComponentVersionId(componentInputParam.getName(),
                componentInputParam.getComponentVersionId());
        boolean nameExist = false;
        if (null != componentInputParam.getId() && !"".equals(componentInputParam.getId())) {
            ComponentInputParam oldComponentInputParam = getService().getByID(componentInputParam.getId());
            if (null != temp && null != oldComponentInputParam && !temp.getId().equals(oldComponentInputParam.getId())) {
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
