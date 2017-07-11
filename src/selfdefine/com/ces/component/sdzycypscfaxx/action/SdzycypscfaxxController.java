package com.ces.component.sdzycypscfaxx.action;

import com.ces.component.sdzycypscfaxx.dao.SdzycypscfaxxDao;
import com.ces.component.sdzycypscfaxx.service.SdzycypscfaxxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class SdzycypscfaxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, SdzycypscfaxxService, SdzycypscfaxxDao> {

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
