package com.ces.component.tcscjgcjxx.action;

import com.ces.component.tcscjgcjxx.dao.TcscjgcjxxDao;
import com.ces.component.tcscjgcjxx.service.TcscjgcjxxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class TcscjgcjxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, TcscjgcjxxService, TcscjgcjxxDao> {

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
