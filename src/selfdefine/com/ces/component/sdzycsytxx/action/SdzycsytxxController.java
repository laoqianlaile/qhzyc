package com.ces.component.sdzycsytxx.action;

import com.ces.component.sdzycsytxx.dao.SdzycsytxxDao;
import com.ces.component.sdzycsytxx.service.SdzycsytxxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class SdzycsytxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, SdzycsytxxService, SdzycsytxxDao> {

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
