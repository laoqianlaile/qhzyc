package com.ces.component.tcscsgl.action;

import com.ces.component.tcscsgl.dao.TcscsglDao;
import com.ces.component.tcscsgl.service.TcscsglService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class TcscsglController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, TcscsglService, TcscsglDao> {

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
