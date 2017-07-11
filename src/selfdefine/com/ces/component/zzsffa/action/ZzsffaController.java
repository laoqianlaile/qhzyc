package com.ces.component.zzsffa.action;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.zzsffa.service.ZzsffaService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class ZzsffaController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, ZzsffaService, TraceShowModuleDao> {

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

    public Object deleteSffa() {
        String dataId = getParameter("dataId");
        setReturnData(getService().deleteSffa(dataId));
        return SUCCESS;
    }

    //删除施肥方案投入品
    public Object deleteSffaTrp() {
        String dataId = getParameter("dataId");
        setReturnData(getService().deleteSffaTrp(dataId));
        return SUCCESS;
    }
}