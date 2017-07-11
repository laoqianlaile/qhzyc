package com.ces.component.csglttrpjca.action;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.csglttrpjca.service.CsglttrpjcaService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class CsglttrpjcaController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, CsglttrpjcaService, TraceShowModuleDao> {

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