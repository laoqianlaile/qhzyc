package com.ces.component.zlaqyj.action;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.zlaqyj.service.ZlaqyjService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class ZlaqyjController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, ZlaqyjService, TraceShowModuleDao> {

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

    public void sendoneMessage(){
        String id=getParameter("id");
//        String[] ids=id.split(",");
        int i=1;
//        String cpzsm=getParameter("cpzsm");
        setReturnData(getService().sendoneMessage(id));
    }

    public void sendGroupMessage(){
        String ids=getParameter("ids");
        String[] idarr=ids.split(",");
        
        setReturnData(getService().sendGroupMessage(idarr));
    }
    
    public void viewEarlyWarning(){
    	String cpzsm = getParameter("CPZSM");
    	setReturnData(getService().viewEarlyWarning(cpzsm));
    	
    }
    
}