package com.ces.component.tcsyprkpch.action;

import com.ces.component.tcsyprkpch.dao.TcsyprkpchDao;
import com.ces.component.tcsyprkpch.service.TcsyprkpchService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class TcsyprkpchController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, TcsyprkpchService, TcsyprkpchDao> {

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
