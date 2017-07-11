package com.ces.component.sdzycjjgllbmxx.action;

import com.ces.component.sdzycjjgllbmxx.dao.SdzycjjgllbmxxDao;
import com.ces.component.sdzycjjgllbmxx.service.SdzycjjgllbmxxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class SdzycjjgllbmxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, SdzycjjgllbmxxService, SdzycjjgllbmxxDao> {

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
