package com.ces.component.tcsycdbpch.action;

import com.ces.component.tcsycdbpch.dao.TcsycdbpchDao;
import com.ces.component.tcsycdbpch.service.TcsycdbpchService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class TcsycdbpchController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, TcsycdbpchService, TcsycdbpchDao> {

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
