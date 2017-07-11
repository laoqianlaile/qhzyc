package com.ces.component.tcsyljyjcxx.action;

import com.ces.component.tcsyljyjcxx.dao.TcsyljyjcxxDao;
import com.ces.component.tcsyljyjcxx.service.TcsyljyjcxxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class TcsyljyjcxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, TcsyljyjcxxService, TcsyljyjcxxDao> {

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
