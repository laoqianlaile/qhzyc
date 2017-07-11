package com.ces.component.tcsjjgycrkpch.action;

import com.ces.component.tcsjjgycrkpch.dao.TcsjjgycrkpchDao;
import com.ces.component.tcsjjgycrkpch.service.TcsjjgycrkpchService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class TcsjjgycrkpchController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, TcsjjgycrkpchService, TcsjjgycrkpchDao> {

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
