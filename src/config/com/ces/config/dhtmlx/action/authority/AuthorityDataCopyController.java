package com.ces.config.dhtmlx.action.authority;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.authority.AuthorityDataCopyDao;
import com.ces.config.dhtmlx.entity.authority.AuthorityApprove;
import com.ces.config.dhtmlx.entity.authority.AuthorityDataCopy;
import com.ces.config.dhtmlx.service.authority.AuthorityApproveService;
import com.ces.config.dhtmlx.service.authority.AuthorityDataCopyService;
import com.ces.config.utils.ConstantVar;
import com.ces.config.utils.StringUtil;
import com.ces.xarch.core.exception.FatalException;

/**
 * 数据权限Controller（三权分立）
 * 
 * @author wanglei
 * @date 2015-07-21
 */
public class AuthorityDataCopyController extends ConfigDefineServiceDaoController<AuthorityDataCopy, AuthorityDataCopyService, AuthorityDataCopyDao> {

    private static final long serialVersionUID = 1L;

    @Override
    protected void initModel() {
        setModel(new AuthorityDataCopy());
    }

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.web.struts2.BaseController#setService(com.ces.xarch.core.service.AbstractService)
     */
    @Override
    @Autowired
    @Qualifier("authorityDataCopyService")
    protected void setService(AuthorityDataCopyService service) {
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
        List<AuthorityDataCopy> authorityDataCopyList = getService().getAuthorityDataList(objectId, objectType, menuId, componentVersionId);
        if (CollectionUtils.isNotEmpty(authorityDataCopyList)) {
            AuthorityApprove authorityApprove = null;
            for (AuthorityDataCopy authorityDataCopy : authorityDataCopyList) {
                authorityApprove = getService(AuthorityApproveService.class).getByRelateAuthId(authorityDataCopy.getId());
                if (authorityApprove != null) {
                    String msg = "<font color='red'>";
                    if (ConstantVar.AuthorityApprove.Operate.NEW.equals(authorityApprove.getOperate())) {
                        msg += "新增";
                    } else if (ConstantVar.AuthorityApprove.Operate.UPDATE.equals(authorityApprove.getOperate())) {
                        msg += "修改";
                    } else if (ConstantVar.AuthorityApprove.Operate.DELETE.equals(authorityApprove.getOperate())) {
                        msg += "删除";
                    }
                    if (ConstantVar.AuthorityApprove.Status.APPROVING.equals(authorityApprove.getStatus())) {
                        msg += "待审批";
                    } else if (ConstantVar.AuthorityApprove.Status.APPROVED_SUCCESS.equals(authorityApprove.getStatus())) {
                        msg += "审批通过";
                    } else if (ConstantVar.AuthorityApprove.Status.APPROVED_BACK.equals(authorityApprove.getStatus())) {
                        msg += "审批退回";
                    }
                    msg += "</font>";
                    authorityDataCopy.setStatus(msg);
                }
            }
        }
        setReturnData(authorityDataCopyList);
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
            AuthorityDataCopy authorityDataCopy = new AuthorityDataCopy();
            authorityDataCopy.setId(authorityDataId);
            authorityDataCopy.setName(name);
            authorityDataCopy.setObjectId(objectId);
            authorityDataCopy.setObjectType(objectType);
            authorityDataCopy.setMenuId(menuId);
            authorityDataCopy.setComponentVersionId(componentVersionId);
            authorityDataCopy.setTableId(tableId);
            authorityDataCopy.setControlTableIds(controlTableIds);
            getService().saveAuthorityData(authorityDataCopy, rowsValue);
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
        AuthorityDataCopy temp = getService().getAuthorityDataByName(objectId, objectType, menuId, componentVersionId, name);
        boolean nameExist = false;
        if (StringUtil.isNotEmpty(authorityDataId)) {
            AuthorityDataCopy oldAuthorityDataCopy = getService().getByID(authorityDataId);
            if (null != temp && null != oldAuthorityDataCopy && !temp.getId().equals(oldAuthorityDataCopy.getId())) {
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
        List<AuthorityDataCopy> authorityDataCopyList = getService().getAuthorityDataList(objectId, objectType, menuId, componentVersionId);
        boolean selfTableDataAuthExist = false;
        for (AuthorityDataCopy authorityDataCopy : authorityDataCopyList) {
            if (tableId.equals(authorityDataCopy.getTableId()) && tableId.equals(authorityDataCopy.getControlTableIds())
                    && (StringUtil.isEmpty(authorityDataId) || !authorityDataId.equals(authorityDataCopy.getId()))) {
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
        List<AuthorityDataCopy> authorityDataCopyList = getService().getAuthorityDataList(objectId, objectType, menuId, componentVersionId);
        if (CollectionUtils.isNotEmpty(authorityDataCopyList)) {
            StringBuilder authorityDataIdSb = new StringBuilder();
            for (AuthorityDataCopy authorityDataCopy : authorityDataCopyList) {
                authorityDataIdSb.append(",").append(authorityDataCopy.getId());
            }
            authorityDataIdSb.deleteCharAt(0);
            getService().delete(authorityDataIdSb.toString());
        }
        return new DefaultHttpHeaders("success").disableCaching();
    }
}
