package com.ces.component.tcscjggysxx.action;

import com.ces.component.tcscjggysxx.dao.TcscjggysxxDao;
import com.ces.component.tcscjggysxx.service.TcscjggysxxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class TcscjggysxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, TcscjggysxxService, TcscjggysxxDao> {

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
