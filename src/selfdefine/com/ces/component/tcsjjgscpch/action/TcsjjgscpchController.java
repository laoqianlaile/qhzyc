package com.ces.component.tcsjjgscpch.action;

import com.ces.component.tcsjjgscpch.dao.TcsjjgscpchDao;
import com.ces.component.tcsjjgscpch.service.TcsjjgscpchService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class TcsjjgscpchController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, TcsjjgscpchService, TcsjjgscpchDao> {

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
