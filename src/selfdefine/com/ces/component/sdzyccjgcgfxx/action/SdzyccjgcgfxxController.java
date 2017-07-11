package com.ces.component.sdzyccjgcgfxx.action;

import com.ces.component.sdzyccjgcgfxx.dao.SdzyccjgcgfxxDao;
import com.ces.component.sdzyccjgcgfxx.service.SdzyccjgcgfxxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class SdzyccjgcgfxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, SdzyccjgcgfxxService, SdzyccjgcgfxxDao> {

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

    public void getGzrydaGrid(){
        this.setReturnData(getService().getGzryda());
    }

}
