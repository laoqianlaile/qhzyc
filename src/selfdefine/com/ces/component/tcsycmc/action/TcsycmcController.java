package com.ces.component.tcsycmc.action;

import com.ces.component.tcsycmc.dao.TcsycmcDao;
import com.ces.component.tcsycmc.service.TcsycmcService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class TcsycmcController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, TcsycmcService, TcsycmcDao> {

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
