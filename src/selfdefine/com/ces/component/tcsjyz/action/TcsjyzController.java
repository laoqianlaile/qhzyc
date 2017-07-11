package com.ces.component.tcsjyz.action;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.tcsjyz.service.TcsjyzService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class TcsjyzController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, TcsjyzService, TraceShowModuleDao> {

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

	@Override
	protected void setService(TcsjyzService service){
		super.setService(service);
	}
	
	public void getJyzdaGrid(){
		String pageType=getParameter("P_pageType");
    	String zt="";
    	if(!"grid".equals(pageType))
    		zt="1";    	
		super.setReturnData(getService().getJyzda(zt));
	}
}