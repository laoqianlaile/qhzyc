package com.ces.component.sdzycjjgggxx.action;

import com.ces.component.sdzycjjgggxx.dao.SdzycjjgggxxDao;
import com.ces.component.sdzycjjgggxx.service.SdzycjjgggxxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class SdzycjjgggxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, SdzycjjgggxxService, SdzycjjgggxxDao> {

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
