package com.ces.config.dhtmlx.action.authority;

import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.authority.AuthorityComponentButtonDao;
import com.ces.config.dhtmlx.entity.authority.AuthorityComponentButton;
import com.ces.config.dhtmlx.service.authority.AuthorityComponentButtonService;

/**
 * 开发的构件按钮权限Controller
 * 
 * @author wanglei
 * @date 2014-07-31
 */
public class AuthorityComponentButtonController extends
        ConfigDefineServiceDaoController<AuthorityComponentButton, AuthorityComponentButtonService, AuthorityComponentButtonDao> {

    private static final long serialVersionUID = 1L;

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.web.struts2.BaseController#initModel()
     */
    @Override
    protected void initModel() {
        setModel(new AuthorityComponentButton());
    }

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.web.struts2.BaseController#setService(com.ces.xarch.core.service.AbstractService)
     */
    @Override
    @Autowired
    @Qualifier("authorityComponentButtonService")
    protected void setService(AuthorityComponentButtonService service) {
        super.setService(service);
    }

    /**
     * 保存不可用开发的构件按钮
     * 
     * @return Object
     */
    public Object saveAuthButton() {
        String objectId = getParameter("P_objectId");
        String objectType = getParameter("P_objectType");
        String menuId = getParameter("P_menuId");
        String componentVersionId = getParameter("P_componentVersionId");
        String componentButtonIds = getParameter("P_componentButtonIds");
        getService().saveAuthButton(objectId, objectType, menuId, componentVersionId, componentButtonIds);
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

}
