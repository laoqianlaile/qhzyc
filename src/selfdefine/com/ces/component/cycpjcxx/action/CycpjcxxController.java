package com.ces.component.cycpjcxx.action;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.cycpjcxx.service.CycpjcxxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class CycpjcxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, CycpjcxxService, TraceShowModuleDao> {

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

    public Object barCodeSave() {
        String barCode = getParameter("barCode");
        setReturnData(getService().barCodeSave(barCode));
        return SUCCESS;
    }

}