package com.ces.component.sdzyccjgyldbxx.action;

import com.ces.component.sdzyccjgyldbxx.dao.SdzyccjgyldbxxDao;
import com.ces.component.sdzyccjgyldbxx.service.SdzyccjgyldbxxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class SdzyccjgyldbxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, SdzyccjgyldbxxService, SdzyccjgyldbxxDao> {

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
