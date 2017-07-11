package com.ces.config.dhtmlx.action.authority;

import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.authority.AuthorityTreeCopyDao;
import com.ces.config.dhtmlx.entity.authority.AuthorityTreeCopy;
import com.ces.config.dhtmlx.service.authority.AuthorityTreeCopyService;
import com.ces.xarch.core.exception.FatalException;

/**
 * 树权限Controller（三权分立）
 * 
 * @author wanglei
 * @date 2015-07-21
 */
public class AuthorityTreeCopyController extends ConfigDefineServiceDaoController<AuthorityTreeCopy, AuthorityTreeCopyService, AuthorityTreeCopyDao> {

    private static final long serialVersionUID = -4609059474518520871L;

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.web.struts2.BaseController#initModel()
     */
    @Override
    protected void initModel() {
        setModel(new AuthorityTreeCopy());
    }

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.web.struts2.BaseController#setService(com.ces.xarch.core.service.AbstractService)
     */
    @Override
    @Autowired
    @Qualifier("authorityTreeCopyService")
    protected void setService(AuthorityTreeCopyService service) {
        super.setService(service);
    }

    /**
     * 删除树权限配置
     * 
     * @return Object
     */
    public Object deleteAuthorityTree() {
        String objectId = getParameter("P_objectId");
        String objectType = getParameter("P_objectType");
        String menuId = getParameter("P_menuId");
        String componentVersionId = getParameter("P_componentVersionId");
        getService().deleteAuthorityTree(objectId, objectType, menuId, componentVersionId);
        return new DefaultHttpHeaders("success").disableCaching();
    }

    /**
     * 保存树权限配置
     * 
     * @return Object
     */
    public Object saveAuthorityTree() {
        String objectId = getParameter("P_objectId");
        String objectType = getParameter("P_objectType");
        String menuId = getParameter("P_menuId");
        String componentVersionId = getParameter("P_componentVersionId");
        String treeDefineIds = getParameter("P_treeDefineIds");
        String controlDataAuth = getParameter("P_controlDataAuth");
        boolean flag = getService().saveAuthorityTree(objectId, objectType, menuId, componentVersionId, treeDefineIds, controlDataAuth);
        if (flag) {
            setReturnData("{'success':true}");
        } else {
            setReturnData("{'success':false}");
        }
        return new DefaultHttpHeaders("success").disableCaching();
    }

    /**
     * 获取树权限treeNodeIds
     * 
     * @return Object
     */
    public Object getAuthorityTreeNodeIds() {
        String objectId = getParameter("P_objectId");
        String objectType = getParameter("P_objectType");
        String menuId = getParameter("P_menuId");
        String componentVersionId = getParameter("P_componentVersionId");
        setReturnData(getService().getAuthorityTreeNodeIds(objectId, objectType, menuId, componentVersionId));
        return NONE;
    }

    /**
     * 树权限复制到其他角色或用户
     * 
     * @return Object
     */
    public Object copyAuthorityTree() throws FatalException {
        String roleIds = getParameter("P_roleIds");
        String userIds = getParameter("P_userIds");
        String objectId = getParameter("P_objectId");
        String objectType = getParameter("P_objectType");
        String menuId = getParameter("P_menuId");
        String componentVersionId = getParameter("P_componentVersionId");
        String treeDefineIds = getParameter("P_treeDefineIds");
        try {
            getService().copyAuthorityTree(roleIds, userIds, objectId, objectType, menuId, componentVersionId, treeDefineIds);
            setReturnData("{'success':true}");
        } catch (Exception e) {
            setReturnData("{'success':false}");
            e.printStackTrace();
        }
        return new DefaultHttpHeaders("success").disableCaching();
    }
}
