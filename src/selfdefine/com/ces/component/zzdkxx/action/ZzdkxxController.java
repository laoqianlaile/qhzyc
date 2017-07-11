package com.ces.component.zzdkxx.action;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.zzdkxx.service.ZzdkxxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class ZzdkxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, ZzdkxxService, TraceShowModuleDao> {

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

    public void isExistZzdyxx(){

        String ids = getParameter("ids");
        setReturnData(getService().isExistZzdyxx(ids));
    }

    public Object getCGQZ(){
        setReturnData(getService().getCGQZ());
        return SUCCESS;
    }

    public Object validCgqz(){
        String id = getParameter("id");
        String cgqz = getParameter("cgqz");
        setReturnData(getService().validCgqz(id, cgqz));
        return SUCCESS;
    }



}