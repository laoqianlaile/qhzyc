package com.ces.component.tcsjjgscllylpch.action;

import com.ces.component.tcsjjgscllylpch.dao.TcsjjgscllylpchDao;
import com.ces.component.tcsjjgscllylpch.service.TcsjjgscllylpchService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class TcsjjgscllylpchController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, TcsjjgscllylpchService, TcsjjgscllylpchDao> {

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
