package com.ces.component.cpglb.action;

import com.ces.component.cpglb.dao.CpglbDao;
import com.ces.component.cpglb.service.CpglbService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class CpglbController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, CpglbService, CpglbDao> {

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
