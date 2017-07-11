package com.ces.component.tcsjclhsp.action;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.tcsjclhsp.service.TcsjclhspService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class TcsjclhspController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, TcsjclhspService, TraceShowModuleDao> {

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