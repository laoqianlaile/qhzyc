package com.ces.component.tcsjdxxfzr.action;

import com.ces.component.tcsjdxxfzr.dao.TcsjdxxfzrDao;
import com.ces.component.tcsjdxxfzr.service.TcsjdxxfzrService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class TcsjdxxfzrController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, TcsjdxxfzrService, TcsjdxxfzrDao> {

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
