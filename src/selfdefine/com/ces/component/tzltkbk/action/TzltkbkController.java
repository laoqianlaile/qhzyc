package com.ces.component.tzltkbk.action;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.tzltkbk.service.TzltkbkService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class TzltkbkController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, TzltkbkService, TraceShowModuleDao> {

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