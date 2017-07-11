package com.ces.component.tcscjgjggyxx.action;

import com.ces.component.tcscjgjggyxx.dao.TcscjgjggyxxDao;
import com.ces.component.tcscjgjggyxx.service.TcscjgjggyxxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class TcscjgjggyxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, TcscjgjggyxxService, TcscjgjggyxxDao> {

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
