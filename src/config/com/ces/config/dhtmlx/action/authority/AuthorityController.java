package com.ces.config.dhtmlx.action.authority;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.authority.AuthorityDao;
import com.ces.config.dhtmlx.entity.authority.Authority;
import com.ces.config.dhtmlx.entity.menu.Menu;
import com.ces.config.dhtmlx.json.entity.common.DhtmlxTreeNode;
import com.ces.config.dhtmlx.service.authority.AuthorityService;
import com.ces.config.dhtmlx.service.menu.MenuService;
import com.ces.config.utils.ConstantVar;
import com.ces.config.utils.StringUtil;
import com.ces.xarch.core.exception.FatalException;

/**
 * 菜单权限Controller
 * 
 * @author wanglei
 * @date 2014-09-25
 */
public class AuthorityController extends ConfigDefineServiceDaoController<Authority, AuthorityService, AuthorityDao> {

    private static final long serialVersionUID = -4609059474518520871L;

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.web.struts2.BaseController#initModel()
     */
    @Override
    protected void initModel() {
        setModel(new Authority());
    }

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.web.struts2.BaseController#setService(com.ces.xarch.core.service.AbstractService)
     */
    @Override
    @Autowired
    @Qualifier("authorityService")
    protected void setService(AuthorityService service) {
        super.setService(service);
    }

    /**
     * 获取角色、用户资源
     */
    @Override
    protected void processTree() throws FatalException {
        list = getDataModel(getModelTemplate());
        processFilter(list);
        String id = getId();
        String pSystemId = getParameter("P_systemId");
        String pObjectId = getParameter("P_objectId");
        String pObjectType = getParameter("P_objectType");
        String roleId = null;
        String userId = null;
        if (Authority.OT_ROLE.equals(pObjectType)) {
            roleId = pObjectId;
        } else if (Authority.OT_USER.equals(pObjectType)) {
            userId = pObjectId;
        }
        try {
            // 判断是为角色节点还是用户节点： -U为用户节点 -R为角色节点
            if (id.equals("-U")) {
                list.setData(getService().getOrgUserList("-1", userId));
            } else if (id.startsWith("O_")) {
                list.setData(getService().getOrgUserList(id.replace("O_", ""), userId));
            } else if (id.equals("-R")) {
                List<Menu> rootMenuList = getService(MenuService.class).getRootMenuList();
                List<DhtmlxTreeNode> treeNodeList = new ArrayList<DhtmlxTreeNode>();
                if (CollectionUtils.isNotEmpty(rootMenuList)) {
                    DhtmlxTreeNode treeNode = null;
                    for (Menu menu : rootMenuList) {
                        if (StringUtil.isNotEmpty(pSystemId)) {
                            if (pSystemId.equals(menu.getId())) {
                                treeNode = new DhtmlxTreeNode();
                                treeNode.setId("S_" + menu.getId());
                                treeNode.setText(menu.getName());
                                treeNode.setIm0(ConstantVar.IconTreeNode.FOLDER_CLOSE);
                                treeNode.setIm1(ConstantVar.IconTreeNode.FOLDER_OPEN);
                                treeNode.setIm2(ConstantVar.IconTreeNode.FOLDER_CLOSE);
                                // 角色节点[0-角色 1-人员 2-部门]
                                treeNode.setChild("1");
                                treeNodeList.add(treeNode);
                                break;
                            }
                        } else {
                            treeNode = new DhtmlxTreeNode();
                            treeNode.setId("S_" + menu.getId());
                            treeNode.setText(menu.getName());
                            treeNode.setIm0(ConstantVar.IconTreeNode.FOLDER_CLOSE);
                            treeNode.setIm1(ConstantVar.IconTreeNode.FOLDER_OPEN);
                            treeNode.setIm2(ConstantVar.IconTreeNode.FOLDER_CLOSE);
                            // 角色节点[0-角色 1-人员 2-部门]
                            treeNode.setChild("1");
                            treeNodeList.add(treeNode);
                        }
                    }
                }
                list.setData(treeNodeList);
            } else if (id.startsWith("S_")) {
                String systemId = id.replaceAll("S_", "");
                list.setData(getService().getSystemRoleList(systemId, roleId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取有权限的菜单IDs
     * 
     * @return Object
     */
    public Object getMenuIds() {
        String objectId = getParameter("P_objectId");
        String objectType = getParameter("P_objectType");
        setReturnData(getService().getMenuIds(objectId, objectType));
        return new DefaultHttpHeaders("success").disableCaching();
    }

    /**
     * 权限保存
     * 
     * @return Object
     */
    public Object saveAuthoritys() {
        try {
            String objectId = getParameter("P_objectId");
            String objectType = getParameter("P_objectType");
            String authorityIds = getParameter("P_authorityIds");
            getService().saveAuthoritys(objectId, objectType, authorityIds);
            setReturnData("{'success':true}");
        } catch (Exception e) {
            setReturnData("{'success':false}");
            e.printStackTrace();
        }
        return new DefaultHttpHeaders("success").disableCaching();
    }

}
