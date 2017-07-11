package com.ces.component.tcsjjgckxx.action;

import com.ces.component.tcsjjgckxx.dao.TcsjjgckxxDao;
import com.ces.component.tcsjjgckxx.service.TcsjjgckxxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class TcsjjgckxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, TcsjjgckxxService, TcsjjgckxxDao> {

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

}
