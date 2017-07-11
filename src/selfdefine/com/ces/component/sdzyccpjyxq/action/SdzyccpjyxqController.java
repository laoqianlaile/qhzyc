package com.ces.component.sdzyccpjyxq.action;

import com.ces.component.sdzyccpjyxq.dao.SdzyccpjyxqDao;
import com.ces.component.sdzyccpjyxq.service.SdzyccpjyxqService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class SdzyccpjyxqController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, SdzyccpjyxqService, SdzyccpjyxqDao> {

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
