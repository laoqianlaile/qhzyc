package com.ces.component.v_j_enter_info.action;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.v_j_enter_info.service.V_J_ENTER_INFOService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class V_J_ENTER_INFOController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, V_J_ENTER_INFOService, TraceShowModuleDao> {

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