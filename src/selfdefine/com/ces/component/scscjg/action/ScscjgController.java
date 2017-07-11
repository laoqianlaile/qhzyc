package com.ces.component.scscjg.action;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.scscjg.service.ScscjgService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class ScscjgController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, ScscjgService, TraceShowModuleDao> {

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


    public void checkUserName(){
        String id = getParameter("id");
        String dlm = getParameter("dlm");
        setReturnData(getService().checkUserName(id,dlm));
    }
}