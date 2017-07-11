package com.ces.component.zzcs.action;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.zzcs.service.ZzcsService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class ZzcsController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, ZzcsService, TraceShowModuleDao> {

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

    public Object deleteCs() {
        String dataId = getParameter("dataId");
        setReturnData(getService().deleteCs(dataId));
        return SUCCESS;
    }

    //删除灌溉投入品信息
    public Object deleteCsTrp () {
        String dataId = getParameter("dataId");
        setReturnData(getService().deleteCsTrp(dataId));
        return SUCCESS;
    }

}