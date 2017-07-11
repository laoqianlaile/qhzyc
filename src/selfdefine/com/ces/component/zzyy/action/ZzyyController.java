package com.ces.component.zzyy.action;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.zzyy.service.ZzyyService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class ZzyyController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, ZzyyService, TraceShowModuleDao> {

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

    public Object deleteYy() {
        String dataId = getParameter("dataId");
        setReturnData(getService().deleteYy(dataId));
        return SUCCESS;
    }

    public Object deleteYyTrp() {
        String dataId = getParameter("dataId");
        setReturnData(getService().deleteYyTrp(dataId));
        return SUCCESS;
    }
}