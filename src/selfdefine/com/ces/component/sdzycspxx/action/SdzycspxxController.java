package com.ces.component.sdzycspxx.action;

import com.ces.component.sdzycspxx.dao.SdzycspxxDao;
import com.ces.component.sdzycspxx.service.SdzycspxxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class SdzycspxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, SdzycspxxService, SdzycspxxDao> {

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
