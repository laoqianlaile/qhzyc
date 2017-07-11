package com.ces.config.dhtmlx.action.menu;

import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.menu.MenuSelfParamDao;
import com.ces.config.dhtmlx.entity.menu.MenuSelfParam;
import com.ces.config.dhtmlx.service.menu.MenuSelfParamService;

/**
 * 菜单绑定构件的自身配置参数Controller
 * 
 * @author wanglei
 * @date 2014-09-11
 */
public class MenuSelfParamController extends ConfigDefineServiceDaoController<MenuSelfParam, MenuSelfParamService, MenuSelfParamDao> {

    private static final long serialVersionUID = 9099376023325191240L;

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.web.struts2.BaseController#initModel()
     */
    @Override
    protected void initModel() {
        setModel(new MenuSelfParam());
    }

    /*
     * (非 Javadoc) <p>标题: setService</p> <p>描述: 注入自定义服务层SERVICE</p> @param service
     * @see com.ces.config.dhtmlx.action.base.StringIDDhtmlxConfigController#setService(com.ces.xarch.core.service.
     * StringIDService)
     */
    @Autowired
    @Qualifier("menuSelfParamService")
    @Override
    protected void setService(MenuSelfParamService service) {
        super.setService(service);
    }

    /**
     * 保存菜单绑定构件的自身配置参数
     * 
     * @return Object
     */
    public Object saveMenuSelfParam() {
        MenuSelfParam menuSelfParam = getService().getByID(model.getId());
        menuSelfParam.setValue(model.getValue());
        menuSelfParam.setText(model.getText());
        getService().save(menuSelfParam);
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }
}