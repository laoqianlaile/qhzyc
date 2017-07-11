package com.ces.component.ickxx.action;

import com.ces.component.ickxx.dao.ICkxxDao;
import com.ces.component.ickxx.service.ICkxxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class ICkxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, ICkxxService, ICkxxDao> {

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
