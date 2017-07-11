package com.ces.component.tcszzcpxx.action;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.tcszzcpxx.service.TcszzcpxxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class TcszzcpxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, TcszzcpxxService, TraceShowModuleDao> {

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