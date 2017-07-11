package com.ces.config.dhtmlx.action.authority;

import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.authority.AuthorityTreeDataDao;
import com.ces.config.dhtmlx.entity.authority.AuthorityTreeData;
import com.ces.config.dhtmlx.service.authority.AuthorityTreeDataService;

/**
 * 树数据权限Controller
 * 
 * @author wanglei
 * @date 2015-06-08
 */
public class AuthorityTreeDataController extends ConfigDefineServiceDaoController<AuthorityTreeData, AuthorityTreeDataService, AuthorityTreeDataDao> {

    private static final long serialVersionUID = -1L;

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.web.struts2.BaseController#initModel()
     */
    @Override
    protected void initModel() {
        setModel(new AuthorityTreeData());
    }

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.web.struts2.BaseController#setService(com.ces.xarch.core.service.AbstractService)
     */
    @Override
    @Autowired
    @Qualifier("authorityTreeDataService")
    protected void setService(AuthorityTreeDataService service) {
        super.setService(service);
    }

    /**
     * 获取数据权限配置列表数据
     * 
     * @return Object
     */
    public Object getControlDataAuth() {
        String objectId = getParameter("P_objectId");
        String objectType = getParameter("P_objectType");
        String menuId = getParameter("P_menuId");
        String componentVersionId = getParameter("P_componentVersionId");
        AuthorityTreeData authorityTreeData = getService().getAuthorityTreeData(objectId, objectType, menuId, componentVersionId);
        String controlDataAuth = "0";
        if (authorityTreeData != null) {
            controlDataAuth = authorityTreeData.getControlDataAuth();
        }
        setReturnData("{'controlDataAuth':'" + controlDataAuth + "'}");
        return new DefaultHttpHeaders("success").disableCaching();
    }
}
