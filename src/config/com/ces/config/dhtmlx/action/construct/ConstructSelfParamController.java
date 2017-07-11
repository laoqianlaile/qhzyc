package com.ces.config.dhtmlx.action.construct;

import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.construct.ConstructSelfParamDao;
import com.ces.config.dhtmlx.entity.construct.ConstructSelfParam;
import com.ces.config.dhtmlx.service.construct.ConstructSelfParamService;

/**
 * 组合构件中基础构件的自身配置参数Controller
 * 
 * @author wanglei
 * @date 2013-08-26
 */
public class ConstructSelfParamController extends ConfigDefineServiceDaoController<ConstructSelfParam, ConstructSelfParamService, ConstructSelfParamDao> {

    private static final long serialVersionUID = 9099376023325191240L;

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.web.struts2.BaseController#initModel()
     */
    @Override
    protected void initModel() {
        setModel(new ConstructSelfParam());
    }

    /*
     * (非 Javadoc) <p>标题: setService</p> <p>描述: 注入自定义服务层SERVICE</p> @param service
     * @see com.ces.config.dhtmlx.action.base.StringIDDhtmlxConfigController#setService(com.ces.xarch.core.service.
     * StringIDService)
     */
    @Autowired
    @Qualifier("constructSelfParamService")
    @Override
    protected void setService(ConstructSelfParamService service) {
        super.setService(service);
    }

    /**
     * 保存组合构件中基础构件的自身配置参数
     * 
     * @return Object
     */
    public Object saveConstructSelfParam() {
        ConstructSelfParam constructSelfParam = getService().getByID(model.getId());
        constructSelfParam.setValue(model.getValue());
        constructSelfParam.setText(model.getText());
        getService().save(constructSelfParam);
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }
}