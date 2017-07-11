package com.ces.component.sdzyccjgllbmxx.action;

import com.ces.component.sdzyccjgllbmxx.dao.SdzyccjgllbmxxDao;
import com.ces.component.sdzyccjgllbmxx.service.SdzyccjgllbmxxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class SdzyccjgllbmxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, SdzyccjgllbmxxService, SdzyccjgllbmxxDao> {

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
