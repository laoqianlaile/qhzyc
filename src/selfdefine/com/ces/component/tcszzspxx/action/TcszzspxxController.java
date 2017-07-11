package com.ces.component.tcszzspxx.action;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.tcszzspxx.service.TcszzspxxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class TcszzspxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, TcszzspxxService, TraceShowModuleDao> {

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
    
    public  String getSpxxGridData(){
    	setReturnData(getService().getSpxx());
    	return null;
    }

}