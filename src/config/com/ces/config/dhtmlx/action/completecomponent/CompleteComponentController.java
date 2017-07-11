package com.ces.config.dhtmlx.action.completecomponent;

import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.completecomponent.CompleteComponentDao;
import com.ces.config.dhtmlx.entity.completecomponent.CompleteComponent;
import com.ces.config.dhtmlx.service.completecomponent.CompleteComponentService;

/**
 * 成品构件Controller
 * 
 * @author wanglei
 * @date 2014-02-17
 */
public class CompleteComponentController extends
        ConfigDefineServiceDaoController<CompleteComponent, CompleteComponentService, CompleteComponentDao> {

    private static final long serialVersionUID = 5705084000332560433L;

    /**
     * (non-Javadoc)
     * 
     * @see com.ces.xarch.core.web.struts2.BaseController#initModel()
     */
    @Override
    protected void initModel() {
        setModel(new CompleteComponent());
    }

    /*
     * (非 Javadoc) <p>标题: setService</p> <p>描述: 注入自定义服务层SERVICE</p> @param service
     * 
     * @see com.ces.config.dhtmlx.action.base.StringIDDhtmlxConfigController#setService(com.ces.xarch.core.service.
     *      StringIDService)
     */
    @Autowired
    @Qualifier("completeComponentService")
    @Override
    protected void setService(CompleteComponentService service) {
        super.setService(service);
    }

    /**
     * 校验字段是否已经存在
     * 
     * @return Object
     */
    public Object validateFields() {
        String name = getParameter("name");
        CompleteComponent temp = getService().getCompleteComponentByName(name);
        boolean nameExist = false;
        if (null != temp) {
            nameExist = true;
        }
        setReturnData("{'nameExist' : " + nameExist + "}");
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }
}
