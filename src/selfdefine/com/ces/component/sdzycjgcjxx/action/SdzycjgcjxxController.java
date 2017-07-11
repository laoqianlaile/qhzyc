package com.ces.component.sdzycjgcjxx.action;

import com.ces.component.sdzycjgcjxx.dao.SdzycjgcjxxDao;
import com.ces.component.sdzycjgcjxx.service.SdzycjgcjxxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class SdzycjgcjxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, SdzycjgcjxxService, SdzycjgcjxxDao> {

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
