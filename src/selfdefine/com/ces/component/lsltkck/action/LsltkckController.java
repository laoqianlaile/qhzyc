package com.ces.component.lsltkck.action;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.lsltkck.service.LsltkckService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class LsltkckController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, LsltkckService, TraceShowModuleDao> {

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