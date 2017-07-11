package com.ces.component.scpfscspxx.action;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.scpfscspxx.service.ScpfscspxxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class ScpfscspxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, ScpfscspxxService, TraceShowModuleDao> {

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