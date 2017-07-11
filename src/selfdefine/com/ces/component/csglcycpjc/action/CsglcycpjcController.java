package com.ces.component.csglcycpjc.action;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.csglcycpjc.service.CsglcycpjcService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class CsglcycpjcController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, CsglcycpjcService, TraceShowModuleDao> {

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