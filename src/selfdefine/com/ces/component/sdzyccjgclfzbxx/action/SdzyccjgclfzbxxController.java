package com.ces.component.sdzyccjgclfzbxx.action;

import com.ces.component.sdzyccjgclfzbxx.dao.SdzyccjgclfzbxxDao;
import com.ces.component.sdzyccjgclfzbxx.service.SdzyccjgclfzbxxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class SdzyccjgclfzbxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, SdzyccjgclfzbxxService, SdzyccjgclfzbxxDao> {

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
