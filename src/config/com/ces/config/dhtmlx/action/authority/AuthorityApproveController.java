package com.ces.config.dhtmlx.action.authority;

import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.authority.AuthorityApproveDao;
import com.ces.config.dhtmlx.entity.authority.AuthorityApprove;
import com.ces.config.dhtmlx.service.authority.AuthorityApproveService;
import com.ces.config.utils.ConstantVar;

/**
 * 数据权限待审批Controller（三权分立）
 * 
 * @author wanglei
 * @date 2015-06-08
 */
public class AuthorityApproveController extends ConfigDefineServiceDaoController<AuthorityApprove, AuthorityApproveService, AuthorityApproveDao> {

    private static final long serialVersionUID = -1L;

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.web.struts2.BaseController#initModel()
     */
    @Override
    protected void initModel() {
        setModel(new AuthorityApprove());
    }

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.web.struts2.BaseController#setService(com.ces.xarch.core.service.AbstractService)
     */
    @Override
    @Autowired
    @Qualifier("authorityApproveService")
    protected void setService(AuthorityApproveService service) {
        super.setService(service);
    }

    /**
     * 获取数据权限待审批的记录（树权限使用）
     * 
     * @return Object
     */
    public Object getTreeAuthorityApprove() {
        String objectId = getParameter("P_objectId");
        String objectType = getParameter("P_objectType");
        String menuId = getParameter("P_menuId");
        String componentVersionId = getParameter("P_componentVersionId");
        AuthorityApprove authorityApprove = getService().getTreeAuthorityApprove(objectId, objectType, menuId, componentVersionId);
        String msg = "";
        if (authorityApprove != null) {
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
        }
        setReturnData(msg);
        return new DefaultHttpHeaders("success").disableCaching();
    }

    /**
     * 获取数据权限待审批的记录（数据权限、编码权限使用）
     * 
     * @return Object
     */
    public Object getByRelateAuthId() {
        String relateAuthId = getParameter("P_relateAuthId");
        AuthorityApprove authorityApprove = getService().getByRelateAuthId(relateAuthId);
        String msg = "";
        if (authorityApprove != null) {
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
        }
        setReturnData(msg);
        return new DefaultHttpHeaders("success").disableCaching();
    }
}
