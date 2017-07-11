package com.ces.component.sdzyccjgcdxx.action;

import com.ces.component.sdzyccjgcdxx.dao.SdzyccjgcdxxDao;
import com.ces.component.sdzyccjgcdxx.service.SdzyccjgcdxxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class SdzyccjgcdxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, SdzyccjgcdxxService, SdzyccjgcdxxDao> {

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
