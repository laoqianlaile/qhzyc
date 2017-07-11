package com.ces.component.sdzyczzflcg.action;

import com.ces.component.sdzyczzflcg.dao.SdzyczzflcgDao;
import com.ces.component.sdzyczzflcg.service.SdzyczzflcgService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class SdzyczzflcgController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, SdzyczzflcgService, SdzyczzflcgDao> {

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

    public void searchFlxx(){
        this.setReturnData(getService().getFlxx());
    }



}
