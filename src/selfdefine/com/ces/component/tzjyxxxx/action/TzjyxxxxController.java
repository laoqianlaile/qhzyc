package com.ces.component.tzjyxxxx.action;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.tzjyxxxx.service.TzjyxxxxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class TzjyxxxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, TzjyxxxxService, TraceShowModuleDao> {

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

    public void getXgdate(){
        String id=getParameter("id");
        setReturnData(getService().getXgdate(id));
    }

}