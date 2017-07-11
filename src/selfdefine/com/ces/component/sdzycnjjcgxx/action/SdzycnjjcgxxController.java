package com.ces.component.sdzycnjjcgxx.action;

import com.ces.component.sdzycnjjcgxx.dao.SdzycnjjcgxxDao;
import com.ces.component.sdzycnjjcgxx.service.SdzycnjjcgxxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class SdzycnjjcgxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, SdzycnjjcgxxService, SdzycnjjcgxxDao> {

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

    public void searchNjjxx(){
        this.setReturnData(getService().getNjjxx());
    }

}
