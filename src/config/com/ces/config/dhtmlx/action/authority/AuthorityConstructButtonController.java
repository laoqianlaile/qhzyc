package com.ces.config.dhtmlx.action.authority;

import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.authority.AuthorityConstructButtonDao;
import com.ces.config.dhtmlx.entity.authority.AuthorityConstructButton;
import com.ces.config.dhtmlx.service.authority.AuthorityConstructButtonService;

/**
 * 构件组装按钮权限Controller
 * 
 * @author wanglei
 * @date 2014-05-08
 */
public class AuthorityConstructButtonController extends
        ConfigDefineServiceDaoController<AuthorityConstructButton, AuthorityConstructButtonService, AuthorityConstructButtonDao> {

    private static final long serialVersionUID = 1L;

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.web.struts2.BaseController#initModel()
     */
    @Override
    protected void initModel() {
        setModel(new AuthorityConstructButton());
    }

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.web.struts2.BaseController#setService(com.ces.xarch.core.service.AbstractService)
     */
    @Override
    @Autowired
    @Qualifier("authorityConstructButtonService")
    protected void setService(AuthorityConstructButtonService service) {
        super.setService(service);
    }

    /**
     * 保存不可用构件组装按钮
     * 
     * @return Object
     */
    public Object saveAuthButton() {
        String objectId = getParameter("P_objectId");
        String objectType = getParameter("P_objectType");
        String menuId = getParameter("P_menuId");
        String componentVersionId = getParameter("P_componentVersionId");
        String constructDetailIds = getParameter("P_constructDetailIds");
        getService().saveAuthButton(objectId, objectType, menuId, componentVersionId, constructDetailIds);
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

}
