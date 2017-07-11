package com.ces.config.dhtmlx.action.authority;

import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.authority.AuthorityTreeDataCopyDao;
import com.ces.config.dhtmlx.entity.authority.AuthorityTreeDataCopy;
import com.ces.config.dhtmlx.service.authority.AuthorityTreeDataCopyService;

/**
 * 树数据权限Controller（三权分立）
 * 
 * @author wanglei
 * @date 2015-07-21
 */
public class AuthorityTreeDataCopyController extends
        ConfigDefineServiceDaoController<AuthorityTreeDataCopy, AuthorityTreeDataCopyService, AuthorityTreeDataCopyDao> {

    private static final long serialVersionUID = -1L;

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.web.struts2.BaseController#initModel()
     */
    @Override
    protected void initModel() {
        setModel(new AuthorityTreeDataCopy());
    }

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.web.struts2.BaseController#setService(com.ces.xarch.core.service.AbstractService)
     */
    @Override
    @Autowired
    @Qualifier("authorityTreeDataCopyService")
    protected void setService(AuthorityTreeDataCopyService service) {
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
        AuthorityTreeDataCopy authorityTreeDataCopy = getService().getAuthorityTreeData(objectId, objectType, menuId, componentVersionId);
        String controlDataAuth = "0";
        if (authorityTreeDataCopy != null) {
            controlDataAuth = authorityTreeDataCopy.getControlDataAuth();
        }
        setReturnData("{'controlDataAuth':'" + controlDataAuth + "'}");
        return new DefaultHttpHeaders("success").disableCaching();
    }

}
