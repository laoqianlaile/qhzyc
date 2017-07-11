package com.ces.component.sdzycymxx.action;

import com.ces.component.sdzycymxx.dao.SdzycymxxDao;
import com.ces.component.sdzycymxx.service.SdzycymxxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class SdzycymxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, SdzycymxxService, SdzycymxxDao> {

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
