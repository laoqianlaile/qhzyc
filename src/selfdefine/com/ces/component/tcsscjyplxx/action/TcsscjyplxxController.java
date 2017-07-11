package com.ces.component.tcsscjyplxx.action;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.tcsscjyplxx.service.TcsscjyplxxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class TcsscjyplxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, TcsscjyplxxService, TraceShowModuleDao> {

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
    public void getJyplGrid(){
        setReturnData(getService().getJyplGrid());
    }
}