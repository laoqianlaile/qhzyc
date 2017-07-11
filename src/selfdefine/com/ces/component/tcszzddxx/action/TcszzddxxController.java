package com.ces.component.tcszzddxx.action;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.tcszzddxx.service.TcszzddxxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class TcszzddxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, TcszzddxxService, TraceShowModuleDao> {

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

    public Object getDdxxByKhbh () {
        String khbh = getParameter("khbh");
        setReturnData(getService().getDdxxByKhbh(khbh));
        return SUCCESS;
    }
}