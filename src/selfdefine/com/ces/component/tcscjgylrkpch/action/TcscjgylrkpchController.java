package com.ces.component.tcscjgylrkpch.action;

import com.ces.component.tcscjgylrkpch.dao.TcscjgylrkpchDao;
import com.ces.component.tcscjgylrkpch.service.TcscjgylrkpchService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class TcscjgylrkpchController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, TcscjgylrkpchService, TcscjgylrkpchDao> {

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
