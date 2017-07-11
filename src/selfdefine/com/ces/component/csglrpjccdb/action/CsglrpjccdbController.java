package com.ces.component.csglrpjccdb.action;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.csglrpjccdb.service.CsglrpjccdbService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class CsglrpjccdbController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, CsglrpjccdbService, TraceShowModuleDao> {

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