package com.ces.component.tcsjdmc.action;

import com.ces.component.tcsjdmc.dao.TcsjdmcDao;
import com.ces.component.tcsjdmc.service.TcsjdmcService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class TcsjdmcController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, TcsjdmcService, TcsjdmcDao> {

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
