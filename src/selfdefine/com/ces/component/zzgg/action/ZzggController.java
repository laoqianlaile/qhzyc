package com.ces.component.zzgg.action;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.zzgg.service.ZzggService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class ZzggController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, ZzggService, TraceShowModuleDao> {

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

    public Object deleteGg() {
        String dataId = getParameter("dataId");
        setReturnData(getService().deleteGg(dataId));
        return SUCCESS;
    }

    //删除灌溉投入品信息
    public Object deleteGgTrp () {
        String dataId = getParameter("dataId");
        setReturnData(getService().deleteGgTrp(dataId));
        return SUCCESS;
    }
}