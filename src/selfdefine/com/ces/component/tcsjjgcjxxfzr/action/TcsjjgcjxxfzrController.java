package com.ces.component.tcsjjgcjxxfzr.action;

import com.ces.component.tcsjjgcjxxfzr.dao.TcsjjgcjxxfzrDao;
import com.ces.component.tcsjjgcjxxfzr.service.TcsjjgcjxxfzrService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class TcsjjgcjxxfzrController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, TcsjjgcjxxfzrService, TcsjjgcjxxfzrDao> {

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
