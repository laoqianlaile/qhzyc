package com.ces.component.lsrptck.action;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.lsrptck.service.LsrptckService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class LsrptckController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, LsrptckService, TraceShowModuleDao> {

    private static final long serialVersionUID = 1L;
    
    /*
     * (非 Javadoc)   
     * <p>标题: initModel</p>   
     * <p>描述: </p>      
     * @see com.ces.xarch.core.web.struts2.BaseController#initModel()
     */
    @Override
    protected void initModel() {
        setModel(new StringIDEntity());
    }

}