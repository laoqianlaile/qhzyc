package com.ces.config.dhtmlx.action.construct;

import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.construct.ConstructDetailSelfParamDao;
import com.ces.config.dhtmlx.entity.construct.ConstructDetailSelfParam;
import com.ces.config.dhtmlx.service.construct.ConstructDetailSelfParamService;

/**
 * 构件绑定预留区后的自身配置参数Controller
 * 
 * @author wanglei
 * @date 2013-08-26
 */
public class ConstructDetailSelfParamController extends
        ConfigDefineServiceDaoController<ConstructDetailSelfParam, ConstructDetailSelfParamService, ConstructDetailSelfParamDao> {

    private static final long serialVersionUID = 9099376023325191240L;

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.web.struts2.BaseController#initModel()
     */
    @Override
    protected void initModel() {
        setModel(new ConstructDetailSelfParam());
    }

    /*
     * (非 Javadoc) <p>标题: setService</p> <p>描述: 注入自定义服务层SERVICE</p> @param service
     * @see com.ces.config.dhtmlx.action.base.StringIDDhtmlxConfigController#setService(com.ces.xarch.core.service.
     * StringIDService)
     */
    @Autowired
    @Qualifier("constructDetailSelfParamService")
    @Override
    protected void setService(ConstructDetailSelfParamService service) {
        super.setService(service);
    }

    /**
     * 保存构件在某组合构件中自身参数的值
     * 
     * @return Object
     */
    public Object saveConstructDetailSelfParam() {
        ConstructDetailSelfParam constructDetailSelfParam = getService().getByID(model.getId());
        constructDetailSelfParam.setValue(model.getValue());
        constructDetailSelfParam.setText(model.getText());
        getService().save(constructDetailSelfParam);
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }
}