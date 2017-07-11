package com.ces.component.ycjxxx.action;

import com.ces.component.ycjxxx.dao.YcjxxxDao;
import com.ces.component.ycjxxx.service.YcjxxxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class YcjxxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, YcjxxxService, YcjxxxDao> {

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
