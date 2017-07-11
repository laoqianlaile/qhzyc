package com.ces.component.zzqt.action;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.zzqt.service.ZzqtService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class ZzqtController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, ZzqtService, TraceShowModuleDao> {

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

    public Object deleteQt() {
        String dataId = getParameter("dataId");
        setReturnData(getService().deleteQt(dataId));
        return SUCCESS;
    }

    //删除灌溉投入品信息
    public Object deleteQtTrp () {
        String dataId = getParameter("dataId");
        setReturnData(getService().deleteQtTrp(dataId));
        return SUCCESS;
    }
}