package com.ces.config.dhtmlx.action.authority;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.authority.AuthorityReportDao;
import com.ces.config.dhtmlx.entity.authority.AuthorityReport;
import com.ces.config.dhtmlx.service.authority.AuthorityReportService;

/**
 * 自定义报表按钮权限Controller
 * 
 * @author Administrator
 * @date 2013-11-01
 */
public class AuthorityReportController extends ConfigDefineServiceDaoController<AuthorityReport, AuthorityReportService, AuthorityReportDao> {

    private static final long serialVersionUID = 1L;

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.web.struts2.BaseController#initModel()
     */
    @Override
    protected void initModel() {
        setModel(new AuthorityReport());
    }

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.web.struts2.BaseController#setService(com.ces.xarch.core.service.AbstractService)
     */
    @Override
    @Autowired
    @Qualifier("authorityReportService")
    protected void setService(AuthorityReportService service) {
        super.setService(service);
    }

}
