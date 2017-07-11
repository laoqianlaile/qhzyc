package com.ces.component.ypjcxxxx.action;

import com.ces.component.ypjcxxxx.dao.YpjcxxxxDao;
import com.ces.component.ypjcxxxx.service.YpjcxxxxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class YpjcxxxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, YpjcxxxxService, YpjcxxxxDao> {

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
