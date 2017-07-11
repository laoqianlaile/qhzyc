package com.ces.component.syxtzjb.action;

import com.ces.component.syxtzjb.dao.SyxtzjbDao;
import com.ces.component.syxtzjb.service.SyxtzjbService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;
import com.ces.xarch.core.exception.FatalException;

import java.util.Map;

public class SyxtzjbController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, SyxtzjbService, SyxtzjbDao> {

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

    public Object ejscx() throws FatalException {
        setReturnData(getService().ejscx());
        return null;
    }

}
