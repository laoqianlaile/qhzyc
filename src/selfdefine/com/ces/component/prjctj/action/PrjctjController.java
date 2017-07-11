package com.ces.component.prjctj.action;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.prjctj.service.PrjctjService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class PrjctjController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, PrjctjService, TraceShowModuleDao> {

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