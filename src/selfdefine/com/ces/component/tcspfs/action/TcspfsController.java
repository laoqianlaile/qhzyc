package com.ces.component.tcspfs.action;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.tcspfs.service.TcspfsService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class TcspfsController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, TcspfsService, TraceShowModuleDao> {

    private static final long serialVersionUID = 1L;
    
    /*
     * (非 Javadoc)   
     * <p>标题: initModel</p>   
     * <p>描述: </p>      
     * @see com.ces.xarch.core.web.struts2.BaseController#initModel()
     */
    @Override
    protected void setService(TcspfsService service){
    	super.setService(service);
    }
    
    @Override
    protected void initModel() {
        setModel(new StringIDEntity());
    }

    public void getJyzdaGrid() {
    	String pageType=getParameter("P_pageType");
    	String zt="";
    	if(!"grid".equals(pageType))
    		zt="1";    	
    	setReturnData(getService().getJyzda(zt));
    }

    public void getJyzGrid(){
        setReturnData(getService().getJyzGrid());
    }
}