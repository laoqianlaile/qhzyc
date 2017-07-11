package com.ces.component.tzrpjyxx.action;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.tzrpjyxx.service.TzrpjyxxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class TzrpjyxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, TzrpjyxxService, TraceShowModuleDao> {

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