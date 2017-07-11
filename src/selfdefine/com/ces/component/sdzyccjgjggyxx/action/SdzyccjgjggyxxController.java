package com.ces.component.sdzyccjgjggyxx.action;

import com.ces.component.sdzyccjgjggyxx.dao.SdzyccjgjggyxxDao;
import com.ces.component.sdzyccjgjggyxx.service.SdzyccjgjggyxxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class SdzyccjgjggyxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, SdzyccjgjggyxxService, SdzyccjgjggyxxDao> {

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
    public void searchjggyxx(){
        this.setReturnData(getService().searchjggyxxBygybh());
    }
}



