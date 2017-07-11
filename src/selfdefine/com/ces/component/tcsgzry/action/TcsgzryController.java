package com.ces.component.tcsgzry.action;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.tcsgzry.service.TcsgzryService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class TcsgzryController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, TcsgzryService, TraceShowModuleDao> {

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
    
    public void getGzryda(){
    	String pageType=getParameter("P_pageType");
    	String zt="";
    	if(!"grid".equals(pageType))
    		zt="1";
    	setReturnData(getService().getGzryda(zt));
    }

}