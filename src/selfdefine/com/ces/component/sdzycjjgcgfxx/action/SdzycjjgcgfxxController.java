package com.ces.component.sdzycjjgcgfxx.action;

import com.ces.component.sdzycjjgcgfxx.dao.SdzycjjgcgfxxDao;
import com.ces.component.sdzycjjgcgfxx.service.SdzycjjgcgfxxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class SdzycjjgcgfxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, SdzycjjgcgfxxService, SdzycjjgcgfxxDao> {

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
