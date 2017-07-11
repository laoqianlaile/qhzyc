package com.ces.config.dhtmlx.action.authority;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.authority.AuthorityDataDao;
import com.ces.config.dhtmlx.entity.authority.AuthorityData;
import com.ces.config.dhtmlx.service.authority.AuthorityDataService;
import com.ces.config.utils.StringUtil;
import com.ces.xarch.core.exception.FatalException;

/**
 * 数据权限Controller
 * 
 * @author wanglei
 * @date 2014-09-25
 */
public class AuthorityDataController extends ConfigDefineServiceDaoController<AuthorityData, AuthorityDataService, AuthorityDataDao> {

    private static final long serialVersionUID = 1L;

    @Override
    protected void initModel() {
        setModel(new AuthorityData());
    }

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.web.struts2.BaseController#setService(com.ces.xarch.core.service.AbstractService)
     */
    @Override
    @Autowired
    @Qualifier("authorityDataService")
    protected void setService(AuthorityDataService service) {
        super.setService(service);
    }

    /**
     * 根据构件版本ID获取该构件的数据权限
     * return Object
     */
    public Object getAuthorityDataList() {
        String objectId = getParameter("P_objectId");
        String objectType = getParameter("P_objectType");
        String menuId = getParameter("P_menuId");
        String componentVersionId = getParameter("P_componentVersionId");
        setReturnData(getService().getAuthorityDataList(objectId, objectType, menuId, componentVersionId));
        return new DefaultHttpHeaders("success").disableCaching();
    }

    /**
     * 保存数据权限
     * 
     * @return Object
     */
    public Object saveAuthorityData() {
        String name = getParameter("P_name");
        String authorityDataId = getParameter("P_authorityDataId");
        String objectId = getParameter("P_objectId");
        String objectType = getParameter("P_objectType");
        String menuId = getParameter("P_menuId");
        String componentVersionId = getParameter("P_componentVersionId");
        String tableId = getParameter("P_tableId");
        String controlTableIds = getParameter("P_controlTableIds");
        String rowsValue = getParameter("P_rowsValue");
        try {
            AuthorityData authorityData = new AuthorityData();
            authorityData.setId(authorityDataId);
            authorityData.setName(name);
            authorityData.setObjectId(objectId);
            authorityData.setObjectType(objectType);
            authorityData.setMenuId(menuId);
            authorityData.setComponentVersionId(componentVersionId);
            authorityData.setTableId(tableId);
            authorityData.setControlTableIds(controlTableIds);
            getService().saveAuthorityData(authorityData, rowsValue);
            setReturnData("{'success':true}");
        } catch (Exception e) {
            setReturnData("{'success':false}");
            e.printStackTrace();
        }
        return new DefaultHttpHeaders("success").disableCaching();
    }

    /**
     * 校验字段是否已经存在
     * 
     * @return Object
     */
    public Object validateFields() {
        String objectId = getParameter("P_objectId");
        String objectType = getParameter("P_objectType");
        String menuId = getParameter("P_menuId");
        String authorityDataId = getParameter("P_authorityDataId");
        String componentVersionId = getParameter("P_componentVersionId");
        String name = getParameter("P_name");
        AuthorityData temp = getService().getAuthorityDataByName(objectId, objectType, menuId, componentVersionId, name);
        boolean nameExist = false;
        if (StringUtil.isNotEmpty(authorityDataId)) {
            AuthorityData oldAuthorityData = getService().getByID(authorityDataId);
            if (null != temp && null != oldAuthorityData && !temp.getId().equals(oldAuthorityData.getId())) {
                nameExist = true;
            }
        } else {
            if (null != temp) {
                nameExist = true;
            }
        }
        setReturnData("{'nameExist' : " + nameExist + "}");
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 校验本表权限是否配置过，本表权限只能配置一次
     * 
     * @return Object
     */
    public Object validateSelfTableDataAuth() {
        String authorityDataId = getParameter("P_authorityDataId");
        String objectId = getParameter("P_objectId");
        String objectType = getParameter("P_objectType");
        String menuId = getParameter("P_menuId");
        String componentVersionId = getParameter("P_componentVersionId");
        String tableId = getParameter("P_tableId");
        List<AuthorityData> authorityDataList = getService().getAuthorityDataList(objectId, objectType, menuId, componentVersionId);
        boolean selfTableDataAuthExist = false;
        for (AuthorityData authorityData : authorityDataList) {
            if (tableId.equals(authorityData.getTableId()) && tableId.equals(authorityData.getControlTableIds())
                    && (StringUtil.isEmpty(authorityDataId) || !authorityDataId.equals(authorityData.getId()))) {
                selfTableDataAuthExist = true;
            }
        }
        setReturnData("{'selfTableDataAuthExist' : " + selfTableDataAuthExist + "}");
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 数据权限复制到其他角色或用户
     * 
     * @return Object
     */
    public Object copyAuthorityData() throws FatalException {
        String roleIds = getParameter("P_roleIds");
        String userIds = getParameter("P_userIds");
        String objectId = getParameter("P_objectId");
        String objectType = getParameter("P_objectType");
        String menuId = getParameter("P_menuId");
        String componentVersionId = getParameter("P_componentVersionId");
        try {
            getService().copyAuthorityData(roleIds, userIds, objectId, objectType, menuId, componentVersionId);
            setReturnData("{'success':true}");
        } catch (Exception e) {
            setReturnData("{'success':false}");
            e.printStackTrace();
        }
        return new DefaultHttpHeaders("success").disableCaching();
    }

    /**
     * 删除数据权限配置
     * 
     * @return Object
     */
    public Object deleteAuthorityData() {
        String objectId = getParameter("P_objectId");
        String objectType = getParameter("P_objectType");
        String menuId = getParameter("P_menuId");
        String componentVersionId = getParameter("P_componentVersionId");
        List<AuthorityData> authorityDataList = getService().getAuthorityDataList(objectId, objectType, menuId, componentVersionId);
        if (CollectionUtils.isNotEmpty(authorityDataList)) {
            StringBuilder authorityDataIdSb = new StringBuilder();
            for (AuthorityData authorityData : authorityDataList) {
                authorityDataIdSb.append(",").append(authorityData.getId());
            }
            authorityDataIdSb.deleteCharAt(0);
            getService().delete(authorityDataIdSb.toString());
        }
        return new DefaultHttpHeaders("success").disableCaching();
    }
}
