package com.ces.component.sdzyccjgclfzzbxx.action;

import com.ces.component.sdzyccjgclfzzbxx.dao.SdzyccjgclfzzbxxDao;
import com.ces.component.sdzyccjgclfzzbxx.service.SdzyccjgclfzzbxxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class SdzyccjgclfzzbxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, SdzyccjgclfzzbxxService, SdzyccjgclfzzbxxDao> {

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
