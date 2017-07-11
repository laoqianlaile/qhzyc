package com.ces.component.tcssdzycyd.action;

import com.ces.component.tcssdzycyd.dao.TcssdzycydDao;
import com.ces.component.tcssdzycyd.service.TcssdzycydService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class TcssdzycydController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, TcssdzycydService, TcssdzycydDao> {

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
