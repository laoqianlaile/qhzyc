package com.ces.component.v_l_enter_info.action;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.v_l_enter_info.service.V_L_ENTER_INFOService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class V_L_ENTER_INFOController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, V_L_ENTER_INFOService, TraceShowModuleDao> {

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