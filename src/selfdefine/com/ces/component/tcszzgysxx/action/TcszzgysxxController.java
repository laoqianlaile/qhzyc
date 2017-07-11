package com.ces.component.tcszzgysxx.action;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.tcszzgysxx.service.TcszzgysxxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class TcszzgysxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, TcszzgysxxService, TraceShowModuleDao> {

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

    public void searchGysxxData(){
        setReturnData(getService().searchGysxxData());
    }
    
    public void searchGysxxDataGrid(){
    	String gysbh = getParameter("GYSBH");
        setReturnData(getService().searchGysxxDataGrid(gysbh));
    }
}