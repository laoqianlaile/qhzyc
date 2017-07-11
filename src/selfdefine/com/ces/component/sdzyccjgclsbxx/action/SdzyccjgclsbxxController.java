package com.ces.component.sdzyccjgclsbxx.action;

import com.ces.component.sdzyccjgclsbxx.dao.SdzyccjgclsbxxDao;
import com.ces.component.sdzyccjgclsbxx.service.SdzyccjgclsbxxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class SdzyccjgclsbxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, SdzyccjgclsbxxService, SdzyccjgclsbxxDao> {

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

    public void searchSbh(){
        String id = getParameter("id");
        setReturnData(getService().searchSbh(id));
    }
}
