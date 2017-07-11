package com.ces.component.tcsdkxx.action;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.tcsdkxx.service.TcsdkxxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class TcsdkxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, TcsdkxxService, TraceShowModuleDao> {

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