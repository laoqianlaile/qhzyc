package com.ces.component.ycjx.action;

import com.ces.component.ycjx.dao.YcjxDao;
import com.ces.component.ycjx.service.YcjxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class YcjxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, YcjxService, YcjxDao> {

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
