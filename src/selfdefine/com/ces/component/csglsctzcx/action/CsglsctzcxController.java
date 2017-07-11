package com.ces.component.csglsctzcx.action;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.csglsctzcx.service.CsglsctzcxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class CsglsctzcxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, CsglsctzcxService, TraceShowModuleDao> {

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