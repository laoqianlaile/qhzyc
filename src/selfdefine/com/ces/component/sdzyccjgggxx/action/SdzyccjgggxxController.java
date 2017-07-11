package com.ces.component.sdzyccjgggxx.action;

import com.ces.component.sdzyccjgggxx.dao.SdzyccjgggxxDao;
import com.ces.component.sdzyccjgggxx.service.SdzyccjgggxxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class SdzyccjgggxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, SdzyccjgggxxService, SdzyccjgggxxDao> {

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
