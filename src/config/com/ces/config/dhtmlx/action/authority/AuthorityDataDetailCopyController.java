package com.ces.config.dhtmlx.action.authority;

import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.authority.AuthorityDataDetailCopyDao;
import com.ces.config.dhtmlx.entity.authority.AuthorityDataDetailCopy;
import com.ces.config.dhtmlx.service.authority.AuthorityDataDetailCopyService;

/**
 * 数据权限详情Controller（三权分立）
 * 
 * @author wanglei
 * @date 2015-07-21
 */
public class AuthorityDataDetailCopyController extends
        ConfigDefineServiceDaoController<AuthorityDataDetailCopy, AuthorityDataDetailCopyService, AuthorityDataDetailCopyDao> {

    private static final long serialVersionUID = 1L;

    @Override
    protected void initModel() {
        setModel(new AuthorityDataDetailCopy());
    }

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.web.struts2.BaseController#setService(com.ces.xarch.core.service.AbstractService)
     */
    @Override
    @Autowired
    @Qualifier("authorityDataDetailCopyService")
    protected void setService(AuthorityDataDetailCopyService service) {
        super.setService(service);
    }

    /**
     * 根据数据权限ID和表ID获取数据权限详情
     * 
     * @return Object
     */
    public Object getDetailListOfTable() {
        String authorityDataId = getParameter("authorityDataId");
        String tableId = getParameter("tableId");
        setReturnData(getService().getByAuthorityDataIdAndTableId(authorityDataId, tableId));
        return new DefaultHttpHeaders("success").disableCaching();
    }

}
